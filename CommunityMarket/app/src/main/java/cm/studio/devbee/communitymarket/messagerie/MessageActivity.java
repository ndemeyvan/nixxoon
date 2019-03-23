package cm.studio.devbee.communitymarket.messagerie;

import android.app.ProgressDialog;
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
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.utilForChat.DiplayAllChat;
import cm.studio.devbee.communitymarket.utilForChat.ModelChat;
import cm.studio.devbee.communitymarket.utilsForUserApp.UserModel;
import cm.studio.devbee.communitymarket.utilsforsearch.SearchAdapter;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {
    private static CircleImageView user_message_image;
    private static TextView user_name;
    private static String randomKey;
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
    private static List<ModelChat> modeChatList;
    private static GroupAdapter groupAdapter;
    private static UserModel userModel;
    private String lien_profil_contact;
    private static   DiplayAllChat contact;
    private static  String nom_utilisateur;
    private String saveCurrentDate;


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
                sendmessagee ();

                message_user_send.setText ( "" );
            }
        } );
        groupAdapter=new GroupAdapter();
        message_recyclerview.setLayoutManager ( new LinearLayoutManager ( getApplicationContext () ) );
        message_recyclerview.setAdapter ( groupAdapter );

        firebaseFirestore.collection("mes donnees utilisateur").document(current_user).get().addOnSuccessListener ( new OnSuccessListener<DocumentSnapshot> () {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

            }
        } ).addOnFailureListener ( new OnFailureListener () {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText ( getApplicationContext (), " erreur,resseayer svp", Toast.LENGTH_LONG ).show ();

            }
        } );
        firebaseFirestore.collection("mes donnees utilisateur").document(current_user).get()
                .addOnSuccessListener ( new OnSuccessListener<DocumentSnapshot> () {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        userModel=documentSnapshot.toObject ( UserModel.class );
                        recuperation ();
                    }
                } );




    }

    private void recuperation() {
        if (userModel!=null){
            String fromId=userModel.getId_utilisateur ();
            String vers=userModel.getId_utilisateur ();
            firebaseFirestore.collection ( "chats" ).document ( fromId ).collection(user_id_message).orderBy ( "temps",Query.Direction.ASCENDING ).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    List<DocumentChange> documentChanges = queryDocumentSnapshots.getDocumentChanges();
                    if (documentChanges!=null){
                        for (DocumentChange doc:documentChanges){
                            if (doc.getType()==DocumentChange.Type.ADDED){
                                ModelChat model=doc.getDocument ().toObject ( ModelChat.class );
                                groupAdapter.add ( new MessageItem ( model ) );
                                groupAdapter.notifyDataSetChanged ();
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
                        lien_profil_contact =task.getResult ().getString ( "user_profil_image" );
                        nom_utilisateur=task.getResult ().getString ( "user_name" );
                        user_name.setText(name_user+" "+prenom);
                        Picasso.with(getApplicationContext()).load(image_user).into(user_message_image);
                       // sendMessage(current_user,user_id_message,image_user);
                    }
                }else {
                    String error=task.getException().getMessage();
                    Toast.makeText ( getApplicationContext (), error, Toast.LENGTH_LONG ).show ();

                }
            }
        });
    }
    public void sendmessagee(){
        Date date=new Date();
        SimpleDateFormat sdf= new SimpleDateFormat("d/MM/y H:mm:ss");
        final String date_avec_seconde=sdf.format(date);
        String message_utilisateur=message_user_send.getText ().toString ();
        final ModelChat modelChat=new ModelChat (  );
        Calendar calendar=Calendar.getInstance ();
        SimpleDateFormat currentDate=new SimpleDateFormat (" MMM dd,yyyy" );
        saveCurrentDate=currentDate.format ( calendar.getTime () );
        randomKey=saveCurrentDate;
        modelChat.setExpediteur ( current_user );
        modelChat.setRecepteur ( user_id_message );
        modelChat.setTemps ( randomKey );
        modelChat.setMessage ( message_utilisateur );
        if (!modelChat.getMessage ().isEmpty ()){
            firebaseFirestore.collection ( "chats" ).document ( current_user ).collection(user_id_message).add( modelChat ).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText ( getApplicationContext (), "message envoye", Toast.LENGTH_LONG ).show ();
                   contact =new DiplayAllChat (  );
                    contact.setId_recepteur ( user_id_message );
                    contact.setImage_profil (lien_profil_contact );
                    contact.setTemps ( modelChat.getTemps () );
                    contact.setDernier_message ( modelChat.getMessage () );
                    firebaseFirestore.collection ( "dernier_message" )
                            .document (current_user).collection ( "contacts" )
                            .document (user_id_message)
                            .set ( contact );

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText ( getApplicationContext (), " erreur,resseayer svp", Toast.LENGTH_LONG ).show ();
                }
            });
            firebaseFirestore.collection ( "chats" ).document ( user_id_message ).collection(current_user).add( modelChat ).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    contact.setId_recepteur ( user_id_message );
                    contact.setNom_utilisateur (nom_utilisateur );
                    contact.setImage_profil (lien_profil_contact );
                    contact.setTemps ( modelChat.getTemps () );
                    contact.setDernier_message ( modelChat.getMessage () );
                    firebaseFirestore.collection ( "dernier_message" )
                            .document (user_id_message).collection ( "contacts" )
                            .document (current_user)
                            .set ( contact );
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }else{
            Toast.makeText ( getApplicationContext (), "ecrire svp", Toast.LENGTH_SHORT ).show ();

        }
    }
//////////////////////////////////////////////////////////////////////////////////
    public class MessageItem extends Item<ViewHolder> {

        private ModelChat modelChat;

public MessageItem(ModelChat modelChat){
    this.modelChat=modelChat;
}

    @Override
    public void bind(@NonNull ViewHolder viewHolder, int position) {
        TextView text=viewHolder.itemView.findViewById ( R.id.show_message );
        final CircleImageView image=viewHolder.itemView.findViewById ( R.id.chat_imag_item );
        text.setText ( modelChat.getMessage () );
        firebaseFirestore.collection("mes donnees utilisateur").document(user_id_message).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult().exists ()){
                        String image_user=task.getResult ().getString ( "user_profil_image" );
                        Picasso.with ( getApplicationContext () ).load ( image_user ).into ( image );
                    }
                }else {
                    String error=task.getException().getMessage();
                    Toast.makeText(getApplicationContext(),error,Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public int getLayout() {
        return modelChat.getExpediteur ().equals (firebaseAuth.getCurrentUser ().getUid ()) ? R.layout.right_item_chat:R.layout.left_item_chat;
    }


}





}
