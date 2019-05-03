
package cm.studio.devbee.communitymarket.profile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import cm.studio.devbee.communitymarket.Accueil;
import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.gridView_post.ModelGridView;
import cm.studio.devbee.communitymarket.utilsForVendeur.ProfilAdapteur;

public class VenteUtilisateurActivity extends AppCompatActivity {
    private static RecyclerView Recycler;
    private static android.support.v7.widget.Toolbar profil_toolbar;
    private static List<ModelGridView> modelGridViewList;
    private static FirebaseAuth mAuth;
    private static FirebaseFirestore firebaseFirestore;
    private static String current_user_id;
    private static ProfilAdapteur gridViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_vente_utilisateur );
        profil_toolbar=findViewById(R.id.profil_toolbar);
        Recycler=findViewById(R.id.profilRecycler);
        setSupportActionBar(profil_toolbar);
        mAuth=FirebaseAuth.getInstance (  );
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        firebaseFirestore=FirebaseFirestore.getInstance ();
        profil_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity ( new Intent ( getApplicationContext (),ProfileActivity.class ).setFlags ( Intent.FLAG_ACTIVITY_CLEAR_TOP ) );
                finish ();
            }
        });
        modelGridViewList=new ArrayList<> ();
        gridViewAdapter=new ProfilAdapteur (modelGridViewList,getApplicationContext());
        Recycler.setAdapter(gridViewAdapter);
        Recycler.setLayoutManager(new GridLayoutManager (getApplicationContext(),2));
        recyclerprofil();

        firebaseFirestore.collection ( "publication" ).document ("post utilisateur").collection ( current_user_id ).addSnapshotListener ( new EventListener<QuerySnapshot> () {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (!queryDocumentSnapshots.isEmpty ()){
                    int i=queryDocumentSnapshots.size ();
                    getSupportActionBar ().setTitle ( i +" articles en ligne " );

                }else{
                    getSupportActionBar ().setTitle ( " vous n'avez emis(e) aucune vente ." );
                }
            }
        } );


    }
    public void recyclerprofil(){
        current_user_id=mAuth.getCurrentUser ().getUid ();
        Query firstQuery =firebaseFirestore.collection ( "publication" ).document ("post utilisateur").collection ( current_user_id ).orderBy ( "prix_du_produit",Query.Direction.DESCENDING );
        firstQuery.addSnapshotListener(VenteUtilisateurActivity.this,new EventListener<QuerySnapshot> () {
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
    @Override
    public void onBackPressed() {
        super.onBackPressed ();
        Intent gotoHome =new Intent ( VenteUtilisateurActivity.this,ProfileActivity.class );
        startActivity ( gotoHome );
        finish ();
    }
}
