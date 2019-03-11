package cm.studio.devbee.communitymarket.postActivity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.vendeurContact.VendeurActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class DetailActivity extends AppCompatActivity {
    private  String iddupost;
    private FirebaseFirestore firebaseFirestore;
    private ImageView detail_image_post;
    private TextView detail_post_titre_produit;
    private CircleImageView detail_profil_image;
    private Button vendeur_button;
    private TextView detail_user_name;
    private TextView detail_prix_produit;
    private TextView detail_description;
    private TextView date_de_publication;
    private FirebaseAuth firebaseAuth;
    private String current_user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        firebaseFirestore=FirebaseFirestore.getInstance();
        iddupost =getIntent().getExtras().getString("id du post");
        current_user_id =getIntent().getExtras().getString("id de l'utilisateur");
        detail_image_post=findViewById(R.id.detail_image_post);
        detail_post_titre_produit=findViewById(R.id.detail_titre_produit);
        detail_prix_produit=findViewById(R.id.detail_prix_produit);
        detail_profil_image=findViewById(R.id.detail_image_du_profil);
        vendeur_button=findViewById(R.id.vendeur_button);
        detail_user_name=findViewById(R.id.detail_user_name);
        detail_description=findViewById(R.id.detail_description);
        date_de_publication=findViewById(R.id.date_de_publication);
        firebaseAuth=FirebaseAuth.getInstance();
        //likez();
        vendeurActivity();
        ///////////no
        firebaseFirestore.collection ( "publication" ).document ("categories").collection ( "nouveaux" ).document (iddupost).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult ().exists ()){
                        String postLike= task.getResult ().getString ( "user_name" );
                        String image_user=task.getResult ().getString ( "user_profil_image" );

                        ////sockelerie a faire after
                        String titreDuProduit=task.getResult().getString("nom_du_produit");
                        String description= task.getResult ().getString ( "decription_du_produit" );
                        String imageduproduit=task.getResult ().getString ( "image_du_produit" );
                        String prixduproduit= task.getResult ().getString ( "prix_du_produit" );
                        String datedepublication=task.getResult ().getString ( "date_de_publication" );
                        detail_post_titre_produit.setText(titreDuProduit);
                        detail_prix_produit.setText(prixduproduit);
                        detail_description.setText(description);
                        date_de_publication.setText(datedepublication);
                        Picasso.with(getApplicationContext()).load(imageduproduit).into(detail_image_post);
                    }
                }else {
                    String error=task.getException().getMessage();

                }
            }
        });
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
                        detail_user_name.setText(name_user+" "+prenom);
                        Picasso.with(getApplicationContext()).load(image_user).into(detail_profil_image);
                    }
                }else {
                    String error=task.getException().getMessage();

                }
            }
        });
    }

   public void vendeurActivity(){
       vendeur_button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent vendeur=new Intent(getApplicationContext(),VendeurActivity.class);
               vendeur.putExtra("id du post",iddupost);
               vendeur.putExtra("id de l'utilisateur",current_user_id);
               startActivity(vendeur);
           }
       });
   }


}
