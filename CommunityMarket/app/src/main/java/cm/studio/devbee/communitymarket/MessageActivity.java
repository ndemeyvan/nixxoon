package cm.studio.devbee.communitymarket;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

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
        mesage_toolbar.setNavigationOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                finish ();
            }
        } );
        send_button.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                String message_utilisateur=message_user_send.getText ().toString ();
                if (!TextUtils.isEmpty ( message_utilisateur )){
                    messagee(current_user,user_id_message,message_utilisateur);
                }else{
                    Toast.makeText ( getApplicationContext (), "ecrire quelque chose", Toast.LENGTH_SHORT ).show ();
                }
                message_user_send.setText ( "" );

            }
        } );


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
                    }
                }else {
                    String error=task.getException().getMessage();

                }
            }
        });
    }
    public void messagee(String sender,String receiver,String message){
        Map<String,Object> messageMap=new HashMap<> (  );
        messageMap.put ( "expediteur",sender );
        messageMap.put ( "recepteur",receiver );
        messageMap.put ( "message",message );
        firebaseFirestore.collection ( "chats" ).document ( current_user ).set ( messageMap ).addOnCompleteListener ( new OnCompleteListener<Void> () {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful ()) {
                    Intent intent = new Intent ( getApplicationContext (), Accueil.class );
                    startActivity ( intent );
                    finish ();
                    Toast.makeText ( getApplicationContext (), "compte enregistre", Toast.LENGTH_LONG ).show ();
                } else {
                    String error = task.getException ().getMessage ();
                    Toast.makeText ( getApplicationContext (), error, Toast.LENGTH_LONG ).show ();
                }
            }
        } );
    }
}
