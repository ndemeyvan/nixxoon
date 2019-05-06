package cm.studio.devbee.communitymarket.postActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import cm.studio.devbee.communitymarket.Accueil;
import cm.studio.devbee.communitymarket.Fragments.HomeFragment;
import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.vendeurContact.VendeurActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class DetailActivity extends AppCompatActivity {
    private  static String iddupost;
    private static FirebaseFirestore firebaseFirestore;
    private static ImageView detail_image_post;
    private static TextView detail_post_titre_produit;
    private static CircleImageView detail_profil_image;
    private static Button vendeur_button;
    private static TextView detail_user_name;
    private static TextView detail_prix_produit;
    private static TextView detail_description;
    private static TextView date_de_publication;
    private static FirebaseAuth firebaseAuth;
    private static String current_user_id;
    private static String utilisateur_actuel;
    private static AsyncTask asyncTask;
    private static ProgressBar detail_progress;
    private static Button supprime_detail_button;
    private static String lien_image;
    private  static Toolbar toolbarDetail;


    private static WeakReference<DetailActivity> detailActivityWeakReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        toolbarDetail=findViewById(R.id.toolbarDetail);
        setSupportActionBar(toolbarDetail);
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        toolbarDetail.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish ();
            }
        });
        firebaseAuth=FirebaseAuth.getInstance ();
        utilisateur_actuel=firebaseAuth.getCurrentUser ().getUid ();
        firebaseFirestore=FirebaseFirestore.getInstance();
        iddupost =getIntent().getExtras().getString("id du post");
        current_user_id =getIntent().getExtras().getString("id de l'utilisateur");
        detail_image_post=findViewById(R.id.detail_image_post);
        detail_post_titre_produit=findViewById(R.id.detail_titre_produit);
        detail_prix_produit=findViewById(R.id.detail_prix_produit);
        detail_profil_image=findViewById(R.id.detail_image_du_profil);
        vendeur_button=findViewById(R.id.vendeur_button);
        toolbarDetail.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish ();
            }
        });
        detail_user_name=findViewById(R.id.detail_user_name);
        detail_description=findViewById(R.id.detail_description);
        date_de_publication=findViewById(R.id.date_de_publication);
        firebaseAuth=FirebaseAuth.getInstance();
        detail_progress=findViewById ( R.id.detail_progress );
        supprime_detail_button=findViewById ( R.id.supprime_detail_button );
        detailActivityWeakReference=new WeakReference<>(this);
        vendeur_button.setEnabled ( false );
        asyncTask=new AsyncTask();
        asyncTask.execute();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*Intent gotohome=new Intent(getApplicationContext(),Accueil.class);
        startActivity(gotohome);*/
        finish();
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
    public void supprime(){
        if (current_user_id.equals ( utilisateur_actuel )){
            supprime_detail_button.setText ( "supprimer de cette categories ?");
            supprime_detail_button.setVisibility ( View.VISIBLE );
            supprime_detail_button.setEnabled ( true );
            supprime_detail_button.setVisibility ( View.VISIBLE );
            supprime_detail_button.setOnClickListener ( new View.OnClickListener () {
                @Override
                public void onClick(View v) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DetailActivity.this);
                        alertDialogBuilder.setMessage("voulez vous vraiment supprimer ?");
                                alertDialogBuilder.setPositiveButton("oui",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                firebaseFirestore.collection ( "publication" ).document ("categories").collection ("nouveaux" ).document (iddupost).get ().addOnCompleteListener ( new OnCompleteListener<DocumentSnapshot> () {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.getResult ().exists ()){
                                                            if (task.isSuccessful ()){
                                                                detail_progress.setVisibility ( View.VISIBLE );
                                                                firebaseFirestore.collection ( "publication" ).document ("categories").collection ( "nouveaux" ).document (iddupost).delete ();
                                                                Toast.makeText ( getApplicationContext (),"supprimer des nouveautés",Toast.LENGTH_LONG ).show ();
                                                                Intent gtohome=new Intent ( getApplicationContext (),Accueil.class );
                                                                //startActivity ( gtohome );
                                                                finish ();
                                                            }else {
                                                                detail_progress.setVisibility ( View.INVISIBLE );
                                                                String error=task.getException ().getMessage ();
                                                                Toast.makeText ( getApplicationContext (),error,Toast.LENGTH_LONG ).show ();

                                                            }
                                                        }else {

                                                        }
                                                    }
                                                } );
                                            }
                                        });

                        alertDialogBuilder.setNegativeButton("non",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               dialog.cancel();

                            }
                        });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                }
            } );
        }else{
            vendeur_button.setVisibility ( View.VISIBLE );
            supprime_detail_button.setVisibility ( View.INVISIBLE );
        }
    }


    public class AsyncTask extends android.os.AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute ();
        }

        @Override
        protected Void doInBackground(Void... voids) {
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
                            lien_image=imageduproduit;
                            getSupportActionBar().setTitle(titreDuProduit);
                            Picasso.with(getApplicationContext()).load(imageduproduit).into(detail_image_post);
                            vendeur_button.setEnabled ( true );


                        }
                    }else {
                        String error=task.getException().getMessage();

                    }
                }
            });
            vendeurActivity();
            nomEtImageProfil();
            supprime ();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute ( aVoid );

        }
    }
    public void vendeurActivity(){
        vendeur_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vendeur=new Intent(getApplicationContext(),UserGeneralPresentation.class);
                vendeur.putExtra("id du post",iddupost);
                vendeur.putExtra("id de l'utilisateur",current_user_id);
                vendeur.putExtra("image_en_vente",lien_image);
                startActivity(vendeur);
                //finish();
            }
        });
    }


    @Override
    protected void onDestroy() {
        asyncTask.cancel(true);
        super.onDestroy();
        asyncTask.cancel(true);
        iddupost=null;
        firebaseFirestore=null;
        detail_image_post=null;
         detail_post_titre_produit=null;
        detail_profil_image=null;
        vendeur_button=null;

        detail_prix_produit=null;
         detail_description=null;
        date_de_publication=null;
         firebaseAuth=null;
         current_user_id=null;
        utilisateur_actuel=null;
        supprime_detail_button=null;
        detail_progress=null;

    }
}
