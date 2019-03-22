package cm.studio.devbee.communitymarket;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import cm.studio.devbee.communitymarket.utilForChat.ChatApdapter;
import cm.studio.devbee.communitymarket.utilForChat.ModeChat;
import cm.studio.devbee.communitymarket.utilsForNouveautes.CategoriesModelNouveaux;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {
    private static CircleImageView user_message_image;
    private static TextView user_name;
    private static Intent intent ;
    private static String user_id_message;
    private static String user_categories_message;
    private static String id_du_post;
    private static  String current_user;
    private static FirebaseFirestore firebaseFirestore;
    private static FirebaseAuth firebaseAuth;
    private static Toolbar mesage_toolbar;
    private static ImageButton send_button;
    private static EditText message_user_send;
    private static RecyclerView message_recyclerview;
    private static ChatApdapter chatApdapter;
    private static List<ModeChat> modeChatList;
    ModeChat modeChat;


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
        firebaseFirestore=FirebaseFirestore.getInstance();
        current_user=firebaseAuth.getCurrentUser ().getUid ();
        user_id_message=intent.getStringExtra ( "id de l'utilisateur" );
        send_button=findViewById ( R.id.imageButton_to_send );
        message_user_send=findViewById ( R.id.user_message_to_send );
        message_recyclerview=findViewById ( R.id.message_recyclerView );
        message_recyclerview.setHasFixedSize (true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager ( getApplicationContext () );
        linearLayoutManager.setStackFromEnd ( true );
        message_recyclerview.setLayoutManager ( linearLayoutManager );
        nomEtImageProfil ();
        mesage_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        send_button.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                send_button.setEnabled(false);
                String message_utilisateur=message_user_send.getText ().toString ();
                if (!TextUtils.isEmpty ( message_utilisateur )){
                    messagee(current_user,user_id_message,message_utilisateur);
                }else{
                    Toast.makeText ( getApplicationContext (), "ecrire quelque chose", Toast.LENGTH_SHORT ).show ();
                }
                message_user_send.setText ( "" );

            }
        } );
        send_button.setEnabled(true);
        /*firebaseFirestore.collection ( "mes donnees utilisateur" ).document(current_user).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                recuperation();
            }
        });*/

    }

    private void recuperation() {
        if (current_user==null){
            firebaseFirestore.collection ( "chats" ).document ( user_id_message ).collection(current_user).orderBy ( "temps",Query.Direction.ASCENDING ).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    List<DocumentChange> documentChanges = queryDocumentSnapshots.getDocumentChanges();
                    if (documentChanges!=null){
                        for (DocumentChange doc:documentChanges){
                            if (doc.getType()==DocumentChange.Type.ADDED){
                                modeChatList.add(modeChat);
                            }
                        }
                    }
                }
            });
        }
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
                        user_name.setText(name_user+" "+prenom);
                        Picasso.with(getApplicationContext()).load(image_user).into(user_message_image);
                        sendMessage(current_user,user_id_message,image_user);
                    }
                }else {
                    String error=task.getException().getMessage();
                    Toast.makeText ( getApplicationContext (), error, Toast.LENGTH_LONG ).show ();

                }
            }
        });
    }
    public void messagee(String sender,String receiver,String message){
        Date date=new Date();
        SimpleDateFormat sdf= new SimpleDateFormat("d/MM/y H:mm:ss");
        final String date_avec_seconde=sdf.format(date);
        Map<String,Object> messageMap=new HashMap<> (  );
        messageMap.put ( "expediteur",sender );
        messageMap.put ( "recepteur",receiver );
        messageMap.put ( "message",message );
        messageMap.put ( "temps",date_avec_seconde );

       /* firebaseFirestore.collection ( "chats" ).document ( current_user ).set( messageMap ).addOnCompleteListener ( new OnCompleteListener<Void> () {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful ()) {
                    Toast.makeText ( getApplicationContext (), "message envoye", Toast.LENGTH_LONG ).show ();
                } else {
                    String error = task.getException ().getMessage ();
                    Toast.makeText ( getApplicationContext (), error, Toast.LENGTH_LONG ).show ();
                }
            }
        } );*/
        firebaseFirestore.collection ( "chats" ).document ( current_user ).collection(user_id_message).add( messageMap ).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText ( getApplicationContext (), "message envoye", Toast.LENGTH_LONG ).show ();
                send_button.setEnabled(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText ( getApplicationContext (), "une erreur est suvenu veiller resseayer svp", Toast.LENGTH_LONG ).show ();
            }
        });

        firebaseFirestore.collection ( "chats" ).document ( user_id_message ).collection(current_user).add( messageMap ).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText ( getApplicationContext (), "une erreur est suvenu veiller resseayer svp", Toast.LENGTH_LONG ).show ();
            }
        });
    }

   public void sendMessage(final String myId, final String userId, final String imageUrl){

        modeChatList=new ArrayList<> (  );
        Query firstQuery = firebaseFirestore.collection ( "chats" ).document ( user_id_message ).collection(current_user).orderBy ( "temps",Query.Direction.ASCENDING );
        firstQuery .addSnapshotListener(new EventListener<QuerySnapshot> () {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentChange doc:queryDocumentSnapshots.getDocumentChanges()){
                    if (doc.getType()==DocumentChange.Type.ADDED){
                         modeChat =doc.getDocument().toObject(ModeChat.class);
                       if (modeChat.getRecepteur ().equals ( myId )&&modeChat.getExpediteur ().equals ( userId )||
                               modeChat.getRecepteur ().equals ( userId )&&modeChat.getExpediteur ().equals ( myId )){
                           modeChatList.add(modeChat);
                       }
                       chatApdapter=new ChatApdapter ( modeChatList,getApplicationContext (),imageUrl );
                       message_recyclerview.setAdapter ( chatApdapter );
                        chatApdapter.notifyDataSetChanged();
                    }
                }

            }
        });

    }
}
