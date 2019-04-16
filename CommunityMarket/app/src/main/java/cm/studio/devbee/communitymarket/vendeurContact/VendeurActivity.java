package cm.studio.devbee.communitymarket.vendeurContact;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import cm.studio.devbee.communitymarket.Accueil;
import cm.studio.devbee.communitymarket.messagerie.MessageActivity;
import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.gridView_post.ModelGridView;
import cm.studio.devbee.communitymarket.utilsForVendeur.VendeurAdapteur;
import de.hdodenhof.circleimageview.CircleImageView;

public class VendeurActivity extends AppCompatActivity {
    private static CircleImageView vendeur_image;
    private static TextView vendeur_user_name;
    private static TextView vendeur_residence;
    private static TextView vendeur_phone;
    private static TextView vendeur_email;
    private static FloatingActionButton message_button_vendeur;
    private static ProgressBar  vendeur_progressbar;
    private  static String iddupost;
    private static String current_user_id;
    private static FirebaseFirestore firebaseFirestore;
    private static Toolbar vendeur_toolbar;
    private static RecyclerView vendeur_recyclerView;
    private static VendeurAdapteur gridViewAdapter;
    private static List<ModelGridView> modelGridViewList;
    private WeakReference<VendeurActivity> vendeurActivityWeakReference;
    private  AsyncTask asyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendeur);
        vendeur_toolbar=findViewById(R.id.message_button_vendeur );
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
        modelGridViewList=new ArrayList<>();
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        vendeur_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity ( new Intent ( getApplicationContext (),Accueil.class ).setFlags ( Intent.FLAG_ACTIVITY_CLEAR_TOP ) );
                finish ();
            }
        });
        gridViewAdapter=new VendeurAdapteur (modelGridViewList,VendeurActivity.this);
        vendeur_recyclerView=findViewById(R.id.vendeur_recyclerView);
        vendeur_recyclerView.setAdapter(gridViewAdapter);
        vendeur_recyclerView.setLayoutManager(new GridLayoutManager(VendeurActivity.this,2));
        vendeurActivityWeakReference=new WeakReference<>(this);
        asyncTask=new AsyncTask();
        asyncTask.execute();
        message_button_vendeur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotochat= new Intent(getApplicationContext(),MessageActivity.class);
                gotochat.putExtra("id de l'utilisateur",current_user_id);
                startActivity ( gotochat );

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
                        String telephone_user =task.getResult ().getString ("user_telephone");
                        String residence_user  =task.getResult ().getString ("user_residence");
                        String email_user =task.getResult ().getString ("user_mail");
                        vendeur_email.setText(email_user);
                        vendeur_phone.setText(telephone_user);
                        vendeur_residence.setText(residence_user);
                        vendeur_user_name.setText(name_user+" "+prenom);
                        Picasso.with(VendeurActivity.this).load(image_user).placeholder(R.drawable.use).into(vendeur_image);
                        getSupportActionBar().setTitle(name_user+" "+prenom);
                    }
                }else {
                    String error=task.getException().getMessage();

                }
            }
        });
    }
    public void vendeur_produit(){

        Query firstQuery =firebaseFirestore.collection ( "publication" ).document ("post utilisateur").collection ( current_user_id ).orderBy ( "prix_du_produit",Query.Direction.DESCENDING );
        firstQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentChange doc:queryDocumentSnapshots.getDocumentChanges()){
                    if (doc.getType()==DocumentChange.Type.ADDED){
                        String idupost=doc.getDocument ().getId ();
                        ModelGridView modelGridView =doc.getDocument().toObject(ModelGridView.class).withId ( idupost );
                        modelGridViewList.add(modelGridView);
                        gridViewAdapter.notifyDataSetChanged();
                    }
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
            vendeur_produit();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute ( aVoid );

        }
    }

    @Override
    protected void onDestroy() {
        asyncTask.cancel(true);
        super.onDestroy();
        asyncTask.cancel(true);
        vendeur_image=null;
       vendeur_user_name=null;
        vendeur_residence=null;
        vendeur_phone=null;
        vendeur_email=null;
         message_button_vendeur=null;
        vendeur_progressbar=null;
       iddupost=null;
        current_user_id=null;
       firebaseFirestore=null;
        vendeur_toolbar=null;
         vendeur_recyclerView=null;
        gridViewAdapter=null;
        modelGridViewList=null;
    }
}
