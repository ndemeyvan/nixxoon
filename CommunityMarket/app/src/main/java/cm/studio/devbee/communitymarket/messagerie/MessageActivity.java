package cm.studio.devbee.communitymarket.messagerie;

import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupAdapter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import cm.studio.devbee.communitymarket.Accueil;
import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.utilForChat.ChatAdapter;
import cm.studio.devbee.communitymarket.utilForChat.DiplayAllChat;
import cm.studio.devbee.communitymarket.utilForChat.ModelChat;
import cm.studio.devbee.communitymarket.utilsForUserApp.UserModel;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {
    private static CircleImageView user_message_image;
    private static TextView user_name;
    private static String randomKey;
    private static Intent intent ;
    private static String user_id_message;
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
                String message =message_user_send.getText ().toString ();
                        if(!TextUtils.isEmpty ( message )){
                            sendMessage (current_user,user_id_message,message);
                            Toast.makeText ( getApplicationContext (),"message envoye",Toast.LENGTH_LONG ).show ();

                        }else{
                            Toast.makeText ( getApplicationContext (),"ecrire quelque chose svp",Toast.LENGTH_LONG ).show ();

                        }
                         message_user_send.setText ( "" );
            }
        } );

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
        userstatus("offline");
        super.onDestroy ();
        userstatus("offline");


    }

    public void sendMessage(final String expediteur, final String recepteur , final String message){
         time=System.currentTimeMillis ();

        final HashMap<String,Object> mesageMap = new HashMap<> (  );
        mesageMap.put ( "expediteur",expediteur );
        mesageMap.put ( "recepteur",recepteur );
        mesageMap.put ( "message",message );
        mesageMap.put ( "temps",time);
        firebaseFirestore.collection ( "conversation" ).document ( expediteur ).collection(recepteur).add ( mesageMap ).addOnSuccessListener ( new OnSuccessListener<DocumentReference> () {
            @Override
            public void onSuccess(DocumentReference documentReference) {
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
                contact.setNom_utilisateur (nom_utilisateur );
                contact.setDernier_message ( message );
                firebaseFirestore.collection ( "dernier_message" )
                        .document (expediteur).collection ( "contacts" )
                        .document (recepteur)
                        .set ( contact );
                Map<String,Object > notificationMap= new HashMap<>();
                notificationMap.put("message",message);
                notificationMap.put("from",current_user);
                firebaseFirestore.collection ( "mes donnees utilisateur" ).document ( user_id_message ).collection("notification").add(notificationMap)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(getApplicationContext(),"notification envoyer",Toast.LENGTH_LONG).show();
                            }
                        });
            }
        } ).addOnFailureListener ( new OnFailureListener () {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        } );
        firebaseFirestore.collection ( "conversation" ).document ( recepteur ).collection(expediteur).add ( mesageMap ).addOnSuccessListener ( new OnSuccessListener<DocumentReference> () {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Calendar calendar=Calendar.getInstance ();
                SimpleDateFormat currentDate=new SimpleDateFormat (" dd MMM yyyy" );
                saveCurrentDate=currentDate.format ( calendar.getTime () );
                randomKey=saveCurrentDate;
                contact.setId_recepteur ( user_id_message );
                contact.setNom_utilisateur (nom_utilisateur );
                contact.setImage_profil (lien_profil_contact );
                contact.setId_expediteur ( current_user );
                contact.setTemps (randomKey );
                contact.setDernier_message ( message );
                firebaseFirestore.collection ( "dernier_message" )
                        .document (recepteur).collection ( "contacts" )
                        .document (expediteur)
                        .set ( contact );

            }
        } ).addOnFailureListener ( new OnFailureListener () {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        } );
        //statusUSer ();
    }
    public void readMessage(final String monId, final String sonID, final String imageYrl){
        modelChatList.clear();
        Query firstQuery =firebaseFirestore.collection ( "conversation" ).document ( current_user ).collection(user_id_message).orderBy ( "temps",Query.Direction.ASCENDING );
        firstQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentChange doc:queryDocumentSnapshots.getDocumentChanges()){
                    if (doc.getType()==DocumentChange.Type.ADDED){
                        ModelChat userModel =doc.getDocument().toObject(ModelChat.class);
                        if (userModel.getRecepteur ().equals ( monId )&& userModel.getExpediteur ().equals ( sonID )||
                                userModel.getRecepteur ().equals ( sonID )&&userModel.getExpediteur ().equals ( monId ) ){
                                modelChatList.add(userModel);
                        }
                        chatAdapter=new ChatAdapter (getApplicationContext (),modelChatList,imageYrl,true);
                        message_recyclerview.setAdapter ( chatAdapter );
                        chatAdapter.notifyDataSetChanged();
                    }
                }
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
