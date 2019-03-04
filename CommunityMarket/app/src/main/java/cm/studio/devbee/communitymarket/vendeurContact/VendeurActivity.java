package cm.studio.devbee.communitymarket.vendeurContact;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.postActivity.DetailActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class VendeurActivity extends AppCompatActivity {
    private CircleImageView vendeur_image;
    private TextView vendeur_user_name;
    private TextView vendeur_residence;
    private TextView vendeur_phone;
    private TextView vendeur_email;
    private Button message_button_vendeur;
    private ProgressBar  vendeur_progressbar;
    private  String iddupost;
    private String current_user_id;
    private FirebaseFirestore firebaseFirestore;
    private Toolbar vendeur_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendeur);
        vendeur_toolbar=findViewById(R.id.toolbarVendeur);
        setSupportActionBar(vendeur_toolbar);
        vendeur_image=findViewById(R.id.vendeur_image);
        vendeur_user_name=findViewById(R.id.vendeur_user_name);
        vendeur_residence=findViewById(R.id.vendeur_residence);
        vendeur_phone=findViewById(R.id.vendeur_phone);
        vendeur_email=findViewById(R.id.vendeur_email);
        message_button_vendeur=findViewById(R.id.message_button_vendeur);
        vendeur_progressbar=findViewById(R.id.vendeur_progressbar);
        iddupost =getIntent().getExtras().getString("id du post");
        current_user_id =getIntent().getExtras().getString("id de l'utilisateur");
        firebaseFirestore=FirebaseFirestore.getInstance();

        nomEtImageProfil();
    }
    public void nomEtImageProfil(){
        firebaseFirestore.collection("mes donnees utilisateur").document(current_user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult ().exists ()){
                        String prenom=task.getResult ().getString ( "user_prenom" );
                        String name_user= task.getResult ().getString ( "user_name" );
                        String image_user=task.getResult ().getString ( "user_profil_image" );
                        String telephone_user =task.getResult ().getString ("user_telephone");
                        String residence_user  =task.getResult ().getString ("user_residence");
                        String email_user =task.getResult ().getString ("user_mail");
                        vendeur_email.setText(email_user);
                        vendeur_phone.setText(telephone_user);
                        vendeur_residence.setText(residence_user);
                        vendeur_user_name.setText(name_user+" "+prenom);
                        Picasso.with(VendeurActivity.this).load(image_user).into(vendeur_image);
                        getSupportActionBar().setTitle(name_user+" "+prenom);
                    }
                }else {
                    String error=task.getException().getMessage();

                }
            }
        });
    }
}
