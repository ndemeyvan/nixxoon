package cm.studio.devbee.communitymarket.messagerie;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.onesignal.OneSignal;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupAdapter;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.annotation.Nullable;

import cm.studio.devbee.communitymarket.Accueil;
import cm.studio.devbee.communitymarket.Fragments.APIService;
import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.notification.Client;
import cm.studio.devbee.communitymarket.notification.Data;
import cm.studio.devbee.communitymarket.notification.MyResponse;
import cm.studio.devbee.communitymarket.notification.Sender;
import cm.studio.devbee.communitymarket.notification.Token;
import cm.studio.devbee.communitymarket.utilForChat.ChatAdapter;
import cm.studio.devbee.communitymarket.utilForChat.DiplayAllChat;
import cm.studio.devbee.communitymarket.utilForChat.ModelChat;
import cm.studio.devbee.communitymarket.utilsForNouveautes.CategoriesModelNouveaux;
import cm.studio.devbee.communitymarket.utilsForUserApp.UserModel;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.appevents.codeless.internal.UnityReflection.sendMessage;

public class MessageActivity extends AppCompatActivity {
    private static CircleImageView user_message_image;
    private static TextView user_name;
    private static String randomKey;
    private static Intent intent ;
    public static String user_id_message;
    private static String user_categories_message;
    private static String id_du_post;
    private static String current_user;
    private static FirebaseFirestore firebaseFirestore;
    private static FirebaseAuth firebaseAuth;
    private static Toolbar mesage_toolbar;
    private static ImageButton send_button;
    private static EditText message_user_send;
    private static RecyclerView message_recyclerview;
    private static List<ModelChat> modelChatListmodeChatList;
    private static GroupAdapter groupAdapter;
    private static UserModel userModel;
    private static String lien_profil_contact;
    private static DiplayAllChat contact;
    private static String nom_utilisateur;
    private static String saveCurrentDate;
    private static ChatAdapter chatAdapter;
    private static List<ModelChat> modelChatList;
    private  static  String current_user_image;
    private static  ModelChat modelChat;
    private static long time;
    private static  CircleImageView online_status;
    private static CircleImageView offline_status;
    private static APIService apiService;
   private static DatabaseReference reference;
   private static ImageView image_en_fond;
   private static ValueEventListener valueEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_message );
        user_message_image=findViewById ( R.id.image_user_message );
        user_name=findViewById ( R.id.user_messag_name );
        firebaseAuth=FirebaseAuth.getInstance ();
        mesage_toolbar=findViewById ( R.id.message_toolbar );
        setSupportActionBar ( mesage_toolbar );
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        intent=getIntent (  );
        modelChatList=new ArrayList<> (  );
        firebaseFirestore=FirebaseFirestore.getInstance();
        current_user=firebaseAuth.getCurrentUser ().getUid ();
        user_id_message=intent.getStringExtra ( "id de l'utilisateur" );
        send_button=findViewById ( R.id.imageButton_to_send );
        message_user_send=findViewById ( R.id.user_message_to_send );
        message_recyclerview=findViewById ( R.id.message_recyclerView );
        message_recyclerview.setHasFixedSize ( true );
        image_en_fond=findViewById ( R.id.image_en_fond );
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager ( MessageActivity.this );
        linearLayoutManager.setStackFromEnd ( true );
        message_recyclerview.setLayoutManager ( linearLayoutManager );
        nomEtImageProfil ();

        online_status=findViewById ( R.id.online_status_image );
        offline_status=findViewById ( R.id.offline_status_image );
        mesage_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity ( new Intent ( getApplicationContext (),ChatMessageActivity.class ).setFlags ( Intent.FLAG_ACTIVITY_CLEAR_TOP ) );
            }
        });
        send_button.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                //notify=true;
                String message =message_user_send.getText ().toString ();
                        if(!TextUtils.isEmpty ( message )){
                            sendmessage (current_user,user_id_message,message);
                            //sendNotification();
                        }else{
                            Toast.makeText ( getApplicationContext (),"vous ne pouvez pas envoyer un message vide",Toast.LENGTH_LONG ).show ();
                        }
                         message_user_send.setText ( "" );
            }
        } );
        apiService=Client.getClient ( "https://fcm.googleapis.com/" ).create ( APIService.class );

    }


    public void userstatus(String status){
        DocumentReference user = firebaseFirestore.collection("mes donnees utilisateur" ).document(current_user);
        user.update("status", status)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume ();
        userstatus("online");
    }

    @Override
    protected void onPause() {
        super.onPause ();
        userstatus("offline");
    }

    @Override
    protected void onDestroy() {
        userstatus("online");
        super.onDestroy ();
        userstatus("online");


    }
    public void sendmessage(String expediteur,String recepteur,String message){
        DatabaseReference reference =FirebaseDatabase.getInstance ().getReference ();
        long milli;
        milli=SystemClock.currentThreadTimeMillis ();
        final HashMap<String,Object> mesageMap = new HashMap<> (  );
        mesageMap.put ( "expediteur",expediteur );
        mesageMap.put ( "recepteur",recepteur );
        mesageMap.put ( "message",message );
        mesageMap.put ( "temps",time);
        mesageMap.put ( "milli",milli);
        reference.child ( "Chats" ).push ().setValue ( mesageMap );
        ///////////////////////////////////////////
        Calendar calendar=Calendar.getInstance ();
        SimpleDateFormat currentDate=new SimpleDateFormat (" dd MMM yyyy" );
        saveCurrentDate=currentDate.format ( calendar.getTime () );
        randomKey=saveCurrentDate;
        contact =new DiplayAllChat (  );
        modelChat= new ModelChat();
        contact.setId_recepteur ( user_id_message );
        contact.setId_expediteur ( current_user );
        contact.setImage_profil (lien_profil_contact );
        contact.setTemps ( randomKey );
        contact.setTempsMilli ( String.valueOf ( milli ) );
        contact.setNom_utilisateur (nom_utilisateur );
        contact.setDernier_message ( message );
        firebaseFirestore.collection ( "dernier_message" )
                .document (expediteur).collection ( "contacts" )
                .document (recepteur)
                .set ( contact );
        ///////////////////////////////////////////////////
        contact.setId_recepteur ( user_id_message );
        contact.setNom_utilisateur (nom_utilisateur );
        contact.setImage_profil (lien_profil_contact );
        contact.setId_expediteur ( current_user );
        contact.setTemps (randomKey );
        contact.setTempsMilli ( String.valueOf ( milli ) );
        contact.setDernier_message ( message );
        firebaseFirestore.collection ( "dernier_message" )
                .document (recepteur).collection ( "contacts" )
                .document (expediteur)
                .set ( contact );

        DocumentReference user = firebaseFirestore.collection("mes donnees utilisateur" ).document(user_id_message);
        user.update("message", "non_lu")
                .addOnSuccessListener(new OnSuccessListener<Void> () {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

        final String msg =message;
        firebaseFirestore.collection ( "mes donnees utilisateur" ).document ( current_user );

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed ();
        startActivity ( new Intent ( getApplicationContext (),ChatMessageActivity.class ).setFlags ( Intent.FLAG_ACTIVITY_CLEAR_TOP ) );
    }

    private void sendNotification() {
        Toast.makeText(this, "Current Recipients is : user1@gmail.com ( Just For Demo )", Toast.LENGTH_SHORT).show();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    String send_email;

                    //This is a Simple Logic to Send Notification different Device Programmatically....
                    if (current_user.equals(current_user)) {
                        send_email =user_id_message;
                    } else {
                        send_email =current_user;
                    }

                    try {
                        String jsonResponse;
                        URL url = new URL("https://onesignal.com/api/v1/notifications");
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setUseCaches(false);
                        con.setDoOutput(true);
                        con.setDoInput(true);

                        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                        con.setRequestProperty("Authorization", "Basic MmQxNjExZDktZjFkNi00MzFiLTgzZGQtOWZkMDEyYjYyZWE2");
                        con.setRequestMethod("POST");

                        String strJsonBody = "{"
                                + "\"app_id\": \"0ee417cd-5789-427c-bea7-8bf2fc90eec1\","

                                + "\"filters\": [{\"field\": \"tag\", \"key\": \"user_id\", \"relation\": \"=\", \"value\": \"" + send_email + "\"}],"

                                + "\"data\": {\"foo\": \"bar\"},"
                                + "\"contents\": {\"en\": \"English Message\"}"
                                + "}";


                        System.out.println("strJsonBody:\n" + strJsonBody);

                        byte[] sendBytes = strJsonBody.getBytes("UTF-8");
                        con.setFixedLengthStreamingMode(sendBytes.length);

                        OutputStream outputStream = con.getOutputStream();
                        outputStream.write(sendBytes);

                        int httpResponse = con.getResponseCode();
                        System.out.println("httpResponse: " + httpResponse);

                        if (httpResponse >= HttpURLConnection.HTTP_OK
                                && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                            Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        } else {
                            Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        }
                        System.out.println("jsonResponse:\n" + jsonResponse);

                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        });

    }

    public void readMessage(final String monId, final String sonID, final String imageYrl){
       // modelChatList.clear();
        modelChatList=new ArrayList<> (  );
        reference=FirebaseDatabase.getInstance ().getReference ("Chats");
        reference.addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                modelChatList.clear ();
                for (DataSnapshot snapshot:dataSnapshot.getChildren ()){
                    ModelChat chat = snapshot.getValue (ModelChat.class);
                    if (chat.getRecepteur ().equals ( monId )&&chat.getExpediteur ().equals ( sonID)||chat.getRecepteur ().equals ( sonID )&&chat.getExpediteur ().equals ( monId )){
                        modelChatList.add ( chat );
                    }
                    chatAdapter=new ChatAdapter (getApplicationContext (),modelChatList,imageYrl,true);
                    message_recyclerview.setAdapter ( chatAdapter );
                    chatAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
        DocumentReference user = firebaseFirestore.collection("mes donnees utilisateur" ).document(current_user);
        user.update("message", "lu")
                .addOnSuccessListener(new OnSuccessListener<Void> () {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

    }

public void nomEtImageProfil(){
        firebaseFirestore.collection("mes donnees utilisateur").document(user_id_message).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot> () {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult ().exists ()){

                        String prenom=task.getResult ().getString ( "user_prenom" );
                        String name_user= task.getResult ().getString ( "user_name" );
                        String image_user=task.getResult ().getString ( "user_profil_image" );
                        lien_profil_contact =task.getResult ().getString ( "user_profil_image" );
                        nom_utilisateur=task.getResult ().getString ( "user_name" );
                        String status=task.getResult ().getString ( "status" );
                        Log.e ("keytwo",status);
                        if (status.equals ( "online" )){
                            online_status.setVisibility ( View.VISIBLE );
                            offline_status.setVisibility ( View.INVISIBLE );
                        }else {
                            online_status.setVisibility ( View.INVISIBLE );
                            offline_status.setVisibility ( View.VISIBLE );
                        }
                        readMessage ( current_user,user_id_message,lien_profil_contact );
                        user_name.setText(name_user+" "+prenom);
                        Picasso.with(getApplicationContext()).load(image_user).into(image_en_fond);
                        Picasso.with(getApplicationContext()).load(image_user).into(user_message_image);

                    }
                }else {
                    String error=task.getException().getMessage();
                    Toast.makeText ( getApplicationContext (), error, Toast.LENGTH_LONG ).show ();

                }
            }
        });

    }

}
