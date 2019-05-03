package cm.studio.devbee.communitymarket.postActivity;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import cm.studio.devbee.communitymarket.Accueil;
import cm.studio.devbee.communitymarket.messagerie.MessageActivity;
import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.vendeurContact.VendeurActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserGeneralPresentation extends AppCompatActivity {
    private static FirebaseFirestore firebaseFirestore;
    private static FirebaseAuth firebaseAuth;
    String current_user;
    private  static CircleImageView profilImage;
    private static TextView user_name;
    private static TextView residence;
    private static TextView last_seen;
    private static TextView operation;
    private static  Button button_message;
    private static  Button button_voir;
   private static   String iddupost;
    private static  String current_user_id;
    private static  AsyncTask asyncTask;
    private static  String categorie;
    private static Toolbar toolbargeneral;
    private  TextView detail_user;
    private static ProgressBar user_general_progress;
    private ImageView backgroundgeneral;
    private static TextView general_last_seen;
    String lien_image;
    private static WeakReference<UserGeneralPresentation> userGeneralPresentationWeakReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_general_presentation);
        toolbargeneral=findViewById(R.id.general_toolbar);
        setSupportActionBar(toolbargeneral);
        detail_user=findViewById(R.id.detail_user);
        iddupost =getIntent().getExtras().getString("id du post");
        current_user_id =getIntent().getExtras().getString("id de l'utilisateur");
        categorie =getIntent().getExtras().getString("id_categories");
        lien_image =getIntent().getExtras().getString("image_en_vente");
        profilImage=findViewById(R.id.generalImageProfilUser);
        backgroundgeneral=findViewById(R.id.backgroundgeneral);
        residence=findViewById(R.id.general_residence);
        last_seen=findViewById(R.id.general_last_view);
        firebaseAuth=FirebaseAuth.getInstance ();
        general_last_seen=findViewById ( R.id.general_last_view );
        //getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        toolbargeneral.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity ( new Intent ( getApplicationContext (),DetailActivity.class ).setFlags ( Intent.FLAG_ACTIVITY_CLEAR_TOP ) );
                finish ();
            }
        });
        button_message=findViewById(R.id.general_button_message);
        button_voir=findViewById(R.id.general_voir_ventes);
        user_name=findViewById(R.id.general_user_name);
        user_general_progress=findViewById ( R.id.user_general_progress );
        firebaseFirestore=FirebaseFirestore.getInstance();
        userGeneralPresentationWeakReference=new WeakReference<>(this);
        asyncTask=new AsyncTask();
        asyncTask.execute();
        button_message.setEnabled ( false );
        button_voir.setEnabled ( false );
        user_general_progress.setVisibility ( View.VISIBLE );
        button_message.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent gotoMessage =new Intent(getApplicationContext(),MessageActivity.class);
                gotoMessage.putExtra("id du post",iddupost);
                gotoMessage.putExtra("id de l'utilisateur",current_user_id);
                gotoMessage.putExtra("id_categories",categorie);
                gotoMessage.putExtra("image_en_vente",lien_image);
                Map<String, String> donnees_utilisateur = new HashMap<> ();
                donnees_utilisateur.put ( "image_en_vente",lien_image);
                current_user=firebaseAuth.getCurrentUser ().getUid ();
                firebaseFirestore.collection ( "sell_image" ).document ( current_user_id ).collection ( current_user ).document (current_user_id).set ( donnees_utilisateur ).addOnCompleteListener ( new OnCompleteListener<Void> () {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                } );
                firebaseFirestore.collection ( "sell_image" ).document ( current_user ).collection ( current_user_id ).document (current_user).set ( donnees_utilisateur ).addOnCompleteListener ( new OnCompleteListener<Void> () {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                } );
                DocumentReference user =  firebaseFirestore.collection ( "sell_image" ).document ( current_user_id ).collection ( current_user ).document (current_user_id);
                user.update("image_en_vente", lien_image)
                        .addOnSuccessListener(new OnSuccessListener<Void> () {
                            @Override
                            public void onSuccess(Void aVoid) {
                            }
                        })
                        .addOnFailureListener(new OnFailureListener () {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
                startActivity(gotoMessage);
                finish();

            }
        } );
        button_voir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoDetail =new Intent(getApplicationContext(),VendeurActivity.class);
                gotoDetail.putExtra("id du post",iddupost);
                gotoDetail.putExtra("id de l'utilisateur",current_user_id);
                gotoDetail.putExtra("id_categories",categorie);
                startActivity(gotoDetail);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity ( new Intent ( getApplicationContext (),DetailActivity.class ).setFlags ( Intent.FLAG_ACTIVITY_CLEAR_TOP ) );
        finish ();
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
                        String residence_user  =task.getResult ().getString ("user_residence");
                        String user_mail  =task.getResult ().getString ("user_mail");
                        String derniere_conection  =task.getResult ().getString ("derniere_conection");
                        general_last_seen.setText ( derniere_conection );
                        residence.setText(residence_user);
                        user_name.setText(name_user+" "+prenom);
                        Picasso.with(UserGeneralPresentation.this).load(image_user).placeholder(R.drawable.boy).into(profilImage);
                        Picasso.with(UserGeneralPresentation.this).load(image_user).into(backgroundgeneral);
                        getSupportActionBar().setTitle(name_user+" "+prenom);
                        button_voir.setText("voir les ventes de "+prenom);
                        button_message.setText("ecrire a "+prenom);
                        detail_user.setText(user_mail);
                        button_message.setEnabled ( true );
                        button_voir.setEnabled ( true );
                        user_general_progress.setVisibility ( View.INVISIBLE );

                    }
                }else {
                    String error=task.getException().getMessage();
                    Toast.makeText(getApplicationContext(),error,Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    public class AsyncTask extends android.os.AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {

            super.onPreExecute ();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            nomEtImageProfil();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute ( aVoid );

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       residence=null;
         last_seen=null;
      operation=null;
      button_message=null;
       button_voir=null;
    }
}
