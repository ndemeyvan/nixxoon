package cm.studio.devbee.communitymarket.postActivity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;

import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.vendeurContact.VendeurActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserGeneralPresentation extends AppCompatActivity {
    private static FirebaseFirestore firebaseFirestore;
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
    private static WeakReference<UserGeneralPresentation> userGeneralPresentationWeakReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_general_presentation);
        toolbargeneral=findViewById(R.id.general_toolbar);
        setSupportActionBar(toolbargeneral);
        iddupost =getIntent().getExtras().getString("id du post");
        current_user_id =getIntent().getExtras().getString("id de l'utilisateur");
        categorie =getIntent().getExtras().getString("id_categories");
        profilImage=findViewById(R.id.generalImageProfilUser);
        residence=findViewById(R.id.general_residence);
        last_seen=findViewById(R.id.general_last_view);
        button_message=findViewById(R.id.general_button_message);
        button_voir=findViewById(R.id.general_voir_ventes);
        user_name=findViewById(R.id.general_user_name);
        firebaseFirestore=FirebaseFirestore.getInstance();
        userGeneralPresentationWeakReference=new WeakReference<>(this);
        asyncTask=new AsyncTask();
        asyncTask.execute();
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
                        residence.setText(residence_user);
                        user_name.setText(name_user+" "+prenom);
                        Picasso.with(UserGeneralPresentation.this).load(image_user).placeholder(R.drawable.use).into(profilImage);
                        getSupportActionBar().setTitle(name_user+" "+prenom);
                        button_voir.setText("voir les ventes de "+prenom);
                        button_message.setText("ecrire a "+prenom);
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