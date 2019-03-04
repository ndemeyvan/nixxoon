package cm.studio.devbee.communitymarket.vendeurContact;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import cm.studio.devbee.communitymarket.Accueil;
import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.gridView_post.GridViewAdapter;
import cm.studio.devbee.communitymarket.gridView_post.ModelGridView;
import cm.studio.devbee.communitymarket.postActivity.DetailActivity;
import cm.studio.devbee.communitymarket.utilsForNouveautes.CategoriesAdapteNouveaux;
import cm.studio.devbee.communitymarket.utilsForNouveautes.CategoriesModelNouveaux;
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
    private RecyclerView vendeur_recyclerView;
    private GridViewAdapter gridViewAdapter;
    private List<ModelGridView> modelGridViewList;

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
        modelGridViewList=new ArrayList<>();
        gridViewAdapter=new GridViewAdapter(modelGridViewList,VendeurActivity.this);
        vendeur_recyclerView=findViewById(R.id.vendeur_recyclerView);
        vendeur_recyclerView.setAdapter(gridViewAdapter);
        vendeur_recyclerView.setLayoutManager(new GridLayoutManager(VendeurActivity.this,2));
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
                        Picasso.with(VendeurActivity.this).load(image_user).placeholder(R.drawable.use).into(vendeur_image);
                        getSupportActionBar().setTitle(name_user+" "+prenom);
                    }
                }else {
                    String error=task.getException().getMessage();

                }
            }
        });
    }
    public void vendeurp_produit(){

        Query firstQuery =firebaseFirestore.collection ( "publication" ).document ("categories").collection ( "nouveaux" ).orderBy ( "date_de_publication",Query.Direction.DESCENDING );
        firstQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for (DocumentChange doc:queryDocumentSnapshots.getDocumentChanges()){
                    if (doc.getType()==DocumentChange.Type.ADDED){
                        String idupost=doc.getDocument ().getId ();
                        ModelGridView modelGridView =doc.getDocument().toObject(ModelGridView.class);
                        modelGridViewList.add(modelGridView);
                        gridViewAdapter.notifyDataSetChanged();

                    }
                }

            }
        });

    }
}
