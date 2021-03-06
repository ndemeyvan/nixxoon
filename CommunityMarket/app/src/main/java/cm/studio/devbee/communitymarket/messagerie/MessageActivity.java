package cm.studio.devbee.communitymarket.messagerie;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
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

import cm.studio.devbee.communitymarket.Accueil;
import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.postActivity.PostActivityFinal;
import cm.studio.devbee.communitymarket.postActivity.UserGeneralPresentation;
import cm.studio.devbee.communitymarket.search.SearchActivity;
import cm.studio.devbee.communitymarket.utilForChat.ChatAdapter;
import cm.studio.devbee.communitymarket.utilForChat.DiplayAllChat;
import cm.studio.devbee.communitymarket.utilForChat.ModelChat;
import cm.studio.devbee.communitymarket.utilsForNouveautes.CategoriesModelNouveaux;
import cm.studio.devbee.communitymarket.utilsForUserApp.UserModel;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {
    private static CircleImageView user_message_image;
    private static TextView user_name;
    private static String randomKey;
    private static Intent intent ;
    public static  String user_id_message;
    private static String user_categories_message;
    private static String id_du_post;
    private static String current_user;
    private static FirebaseFirestore firebaseFirestore;
    private static FirebaseAuth firebaseAuth;
    private static Toolbar mesage_toolbar;
    private static ImageButton send_button;
    private static EditText message_user_send;
    private static RecyclerView message_recyclerview,media_recycler;
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
    private static DatabaseReference reference;
    private static ImageView image_en_fond;
    private static ValueEventListener valueEventListener;
    private static ImageView image_de_discutions;
    private static  String lien_image;
    private static  String image;
    private static ProgressBar message_progressbar;
    private static Uri mImageUri;
    private static String id_recepteur;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_message );
        user_message_image=findViewById ( R.id.image_user_message );
        user_name=findViewById ( R.id.user_messag_name );
        firebaseAuth=FirebaseAuth.getInstance ();
        mesage_toolbar=findViewById ( R.id.message_toolbar );
        setSupportActionBar ( mesage_toolbar );
        message_progressbar=findViewById(R.id.message_progressbar);
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        intent=getIntent (  );
        modelChatList=new ArrayList<> (  );
        firebaseFirestore=FirebaseFirestore.getInstance();
        current_user=firebaseAuth.getCurrentUser ().getUid ();
        user_id_message=intent.getStringExtra ( "id de l'utilisateur" );
        lien_image=intent.getStringExtra ( "image_en_vente" );
        id_recepteur=intent.getStringExtra ( "id_recepteur" );
        send_button=findViewById ( R.id.imageButton_to_send );
        message_user_send=findViewById ( R.id.user_message_to_send );
        message_recyclerview=findViewById ( R.id.message_recyclerView );
        image_de_discutions=findViewById ( R.id.image_de_discutions );
        message_recyclerview.setHasFixedSize ( true );
        //image_en_fond=findViewById ( R.id.image_en_fond );
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager ( MessageActivity.this );
        linearLayoutManager.setStackFromEnd ( true );
        message_recyclerview.setLayoutManager ( linearLayoutManager );
        nomEtImageProfil ();
        online_status=findViewById ( R.id.online_status_image );
        offline_status=findViewById ( R.id.offline_status_image );
        mesage_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotochatlist=new Intent(getApplicationContext(),ChatMessageActivity.class);
                startActivity(gotochatlist);
                finish();
            }
        });


        send_button.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                send_button.setAnimation ( AnimationUtils.loadAnimation ( getApplicationContext (),R.anim.fade_transition_animation ) );
                String message =message_user_send.getText ().toString ();
                        if(!TextUtils.isEmpty ( message )){
                            sendmessage (current_user,user_id_message,message);
                            Map<String, String> notification = new HashMap<> ();
                            notification.put ( "message",message);
                            notification.put("from",current_user);

                            firebaseFirestore.collection ( "mes donnees utilisateur" ).document ( user_id_message ).collection("notification").add ( notification ).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {

                                }
                            });

                        }else{
                            Toast.makeText ( getApplicationContext (),getString(R.string.message_vide),Toast.LENGTH_LONG ).show ();
                        }
                         message_user_send.setText ( "" );
            }
        } );



        Calendar calendar=Calendar.getInstance ();
        SimpleDateFormat currentDate=new SimpleDateFormat (" dd MMM yyyy" );
        String saveCurrentDate=currentDate.format ( calendar.getTime () );
        String randomKey=saveCurrentDate;
        DocumentReference user = firebaseFirestore.collection("mes donnees utilisateur" ).document(current_user);
        user.update("derniere_conection", randomKey)
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

        firebaseFirestore.collection ( "sell_image" ).document ( user_id_message ).collection ( current_user ).document (user_id_message).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult ().exists ()){
                         image= task.getResult ().getString ( "image_en_vente" );
                        Picasso.with ( MessageActivity.this ).load ( image ).into ( image_de_discutions );
                    }else{

                    }
                }else {
                    String error=task.getException().getMessage();
                }
            }
        });

        firebaseFirestore.collection ( "sell_image" ).document ( current_user ).collection ( user_id_message ).document (current_user).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult ().exists ()){
                        image= task.getResult ().getString ( "image_en_vente" );
                        Picasso.with ( MessageActivity.this ).load ( image ).into ( image_de_discutions );
                        image_de_discutions.setAnimation ( AnimationUtils.loadAnimation ( getApplicationContext (),R.anim.fade_transition_animation ) );
                    }else{

                    }
                }else {
                    String error=task.getException().getMessage();
                }
            }
        });
        userstatus("online");


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
        Calendar calendar=Calendar.getInstance ();
        SimpleDateFormat currentDate=new SimpleDateFormat (" dd MMM yyyy" );
        String saveCurrentDate=currentDate.format ( calendar.getTime () );
        String randomKey=saveCurrentDate;
        DocumentReference user = firebaseFirestore.collection("mes donnees utilisateur" ).document(current_user);
        user.update("derniere_conection", randomKey)
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
    protected void onDestroy() {
        userstatus("offline");
        super.onDestroy ();
        userstatus("offline");
        Calendar calendar=Calendar.getInstance ();
        SimpleDateFormat currentDate=new SimpleDateFormat (" dd MMM yyyy" );
        String saveCurrentDate=currentDate.format ( calendar.getTime () );
        String randomKey=saveCurrentDate;
        DocumentReference user = firebaseFirestore.collection("mes donnees utilisateur" ).document(current_user);
        user.update("derniere_conection", randomKey)
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
        contact.setLu ( "lu" );
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
        contact.setLu ( "non lu" );
        contact.setTempsMilli ( String.valueOf ( milli ) );
        contact.setDernier_message ( message );
        firebaseFirestore.collection ( "dernier_message" )
                .document (recepteur).collection ( "contacts" )
                .document (expediteur)
                .set ( contact );

        DocumentReference read_or_not = firebaseFirestore.collection("dernier_message" ).document (recepteur).collection("contacts").document (expediteur);
        read_or_not.update("lu", "non lu")
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
        userstatus("offline");
        startActivity ( new Intent ( getApplicationContext (),ChatMessageActivity.class ).setFlags ( Intent.FLAG_ACTIVITY_CLEAR_TOP ) );
        finish();
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
        if (current_user.equals(id_recepteur)){
            /*DocumentReference user = firebaseFirestore.collection("dernier_message" ).document (user_id_message).collection("contacts").document (current_user);
            user.update("lu", "lu")
                    .addOnSuccessListener(new OnSuccessListener<Void> () {
                        @Override
                        public void onSuccess(Void aVoid) {
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });*/

            DocumentReference usertwo = firebaseFirestore.collection("dernier_message" ).document (current_user).collection("contacts").document (user_id_message);
            usertwo.update("lu", "lu")
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
        }else{
            DocumentReference usertwo = firebaseFirestore.collection("dernier_message" ).document (current_user).collection("contacts").document (user_id_message);
            usertwo.update("lu", "lu")
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

        message_progressbar.setVisibility(View.INVISIBLE);

    }




    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater ().inflate ( R.menu.message_menu, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId ();
        if (id == R.id.message_menu) {

            return true;
        }
        return super.onOptionsItemSelected ( item );
    }*/

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
                        user_name.setAnimation ( AnimationUtils.loadAnimation ( getApplicationContext (),R.anim.fade_transition_animation ) );
//                        Picasso.with(getApplicationContext()).load(image_user).into(image_en_fond);
                        Picasso.with(getApplicationContext()).load(image_user).into(user_message_image);
                        user_message_image.setAnimation ( AnimationUtils.loadAnimation ( getApplicationContext (),R.anim.fade_transition_animation ) );

                    }
                }else {
                    String error=task.getException().getMessage();
                    Toast.makeText ( getApplicationContext (), error, Toast.LENGTH_LONG ).show ();

                }
            }
        });

    }



}



