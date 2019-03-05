package cm.studio.devbee.communitymarket;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import cm.studio.devbee.communitymarket.a_propos.AproposActivity;
import cm.studio.devbee.communitymarket.login.LoginActivity;
import cm.studio.devbee.communitymarket.postActivity.PostActivity;
import cm.studio.devbee.communitymarket.profile.ParametrePorfilActivity;
import cm.studio.devbee.communitymarket.profile.ProfileActivity;
import cm.studio.devbee.communitymarket.utilForJupe.CategoriesAdapteJupe;
import cm.studio.devbee.communitymarket.utilForJupe.CategoriesModelJupe;
import cm.studio.devbee.communitymarket.utilsForCategories.CategoriesAdapte;
import cm.studio.devbee.communitymarket.utilsForCategories.CategoriesModel;
import cm.studio.devbee.communitymarket.utilsForChaussure.CategoriesAdapteChaussure;
import cm.studio.devbee.communitymarket.utilsForChaussure.CategoriesModelChaussure;
import cm.studio.devbee.communitymarket.utilsForNouveautes.CategoriesAdapteNouveaux;
import cm.studio.devbee.communitymarket.utilsForNouveautes.CategoriesModelNouveaux;
import de.hdodenhof.circleimageview.CircleImageView;

public class Accueil extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth mAuth;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private String current_user_id;
    private CircleImageView acceuille_image;
    private TextView drawer_user_name;
    private ImageView imageOne,imageTwo,imageThree,imageFour;
    private TextView img1,img2,img3,img4;
    private ProgressBar content_progresbar;
    private RecyclerView content_recyclerView;
    private CategoriesAdapte categoriesAdapte;
    private List<CategoriesModel> categoriesModelList;
    private FloatingActionButton content_floating_action_btn;
    private ViewFlipper viewFlipper;
    private CategoriesAdapteNouveaux categoriesAdapteNouveaux;
    private RecyclerView nouveauxRecyclerView;
    private RecyclerView chaussureRecyclerView;
    private CategoriesAdapteChaussure categoriesAdapteChaussure;
    private List<CategoriesModelChaussure> categoriesAdapteChaussureList;
    private List<CategoriesModelNouveaux> categoriesModelNouveauxList;
    private RecyclerView jupesRecyclerView;
    private CategoriesAdapteJupe categoriesAdapteJupe;
    private CategoriesModelJupe categoriesModelJupe;
    private List<CategoriesModelJupe> categoriesModelJupeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_accueil );
        Toolbar toolbar =findViewById ( R.id.toolbar );
        setSupportActionBar ( toolbar );
        NavigationView navigationView =findViewById ( R.id.nav_view );
        ////////chaussure
        categoriesAdapteChaussureList=new ArrayList<> (  );
        chaussureRecyclerView=findViewById ( R.id.chaussureRecyclerView );
        categoriesAdapteChaussure=new CategoriesAdapteChaussure (categoriesAdapteChaussureList,Accueil.this);
        chaussureRecyclerView.setAdapter ( categoriesAdapteChaussure );
        chaussureRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        /////chaussure
        ////////nouveaux
        categoriesModelNouveauxList=new ArrayList<>();
        nouveauxRecyclerView=findViewById(R.id.nouveautes_recyclerView);
        categoriesAdapteNouveaux=new CategoriesAdapteNouveaux(categoriesModelNouveauxList,Accueil.this);
        nouveauxRecyclerView.setAdapter(categoriesAdapteNouveaux);
        nouveauxRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        ////nouveaux
        ///////jupes
        jupesRecyclerView=findViewById ( R.id.jupesRecyclerView );
        categoriesModelJupeList=new ArrayList<> (  );
        categoriesAdapteJupe=new CategoriesAdapteJupe (categoriesModelJupeList,Accueil.this);
        jupesRecyclerView.setAdapter ( categoriesAdapteJupe );
        jupesRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        ///////fin jupes
        mAuth=FirebaseAuth.getInstance ();
        viewFlipper=findViewById(R.id.viewFlipper);
        storageReference=FirebaseStorage.getInstance ().getReference ();
        firebaseFirestore=FirebaseFirestore.getInstance ();
        acceuille_image=navigationView.getHeaderView(0).findViewById(R.id.acceuille_image);
        drawer_user_name=navigationView.getHeaderView(0).findViewById(R.id.drawer_user_name);
        DrawerLayout drawer =findViewById ( R.id.drawer_layout );
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle (
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
        drawer.addDrawerListener ( toggle );
        toggle.syncState ();
        navigationView.setNavigationItemSelectedListener ( this );
        recup();
        //////////////slider
        imageOne=findViewById(R.id.imageSlideOne);
        imageTwo=findViewById(R.id.imageSlideTwo);
        imageThree=findViewById(R.id.imageSlideThree);
        imageFour=findViewById(R.id.imageSlideFour);
        img1=findViewById(R.id.img1);
        img2=findViewById(R.id.img2);
        img3=findViewById(R.id.img3);
        img4=findViewById(R.id.img4);
        ///////fin slider
        content_progresbar=findViewById ( R.id.content_progresbar );
        uptdate();
        ////////////recyclerView
        content_recyclerView=findViewById ( R.id.content_recyclerView );
        categoriesModelList=new ArrayList<> (  );
        categoriesModelList.add ( new CategoriesModel ( "Marques",R.drawable.logo ) );
        categoriesModelList.add ( new CategoriesModel ( "Chaussures",R.drawable.chaussure ) );
        categoriesModelList.add ( new CategoriesModel ( "jupes",R.drawable.jupes ) );
        categoriesModelList.add ( new CategoriesModel ( "accesoires",R.drawable.accessoires ) );
        categoriesModelList.add ( new CategoriesModel ( "Cullotes",R.drawable.cullotes ) );
        categoriesModelList.add ( new CategoriesModel ( "Pantalons",R.drawable.pantalons ) );
        categoriesModelList.add ( new CategoriesModel ( "T-shirts",R.drawable.t_shirt ) );
        categoriesModelList.add ( new CategoriesModel ( "Chemises",R.drawable.chemise ) );
        categoriesModelList.add ( new CategoriesModel ( "robe",R.drawable.robe ) );

        categoriesAdapte=new CategoriesAdapte ( categoriesModelList,Accueil.this );
        content_recyclerView.setAdapter ( categoriesAdapte );
        content_recyclerView.setLayoutManager ( new LinearLayoutManager ( this,LinearLayoutManager.HORIZONTAL,false ) );
        ///////fin recyclerview

        vaTopost ();
        nouveautes();
        chaussuresRecycler ();
        juperecycler ();

    }
    public void recup(){
        current_user_id=mAuth.getCurrentUser ().getUid ();
        firebaseFirestore.collection ( "mes donnees utilisateur" ).document (current_user_id).get ().addOnCompleteListener ( new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful ()){
                    if (task.getResult ().exists ()){
                        String image_profil_user =task.getResult ().getString ("user_profil_image");
                        String nom_user = task.getResult ().getString ("user_name");
                        String prenomuser =task.getResult ().getString ("user_prenom");
                        drawer_user_name.setText ( nom_user + " " + prenomuser);
                        Log.d("cle",image_profil_user);
                        Picasso.with ( Accueil.this ).load ( image_profil_user ).placeholder(R.drawable.use).into ( acceuille_image );
                    }
                }else{


                }
            }
        } );
    }
    public void uptdate(){
        DocumentReference user = firebaseFirestore.collection("sliders").document("images");
        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()){
                    viewFlipper.setOutAnimation(Accueil.this,android.R.anim.slide_out_right);
                    viewFlipper.setInAnimation(Accueil.this,android.R.anim.slide_in_left);
                    content_progresbar.setVisibility ( View.INVISIBLE );
                    DocumentSnapshot doc =task.getResult();
                    StringBuilder image=new StringBuilder("");
                    image.append(doc.get("imageOne"));
                    img1.setText(image.toString());
                    String lien = img1.getText().toString();
                    Picasso.with(Accueil.this).load(lien).into(imageOne);
                    //////////image deux
                    StringBuilder image2=new StringBuilder("");
                    image2.append(doc.get("imageTwo"));
                    img1.setText(image.toString());
                    String lien2 = img1.getText().toString();
                    Picasso.with(Accueil.this).load(lien2).into(imageTwo);
                    //////image trois
                    StringBuilder image3=new StringBuilder("");
                    image3.append(doc.get("imageThree"));
                    img1.setText(image3.toString());
                    String lien3 = img1.getText().toString();
                    Picasso.with(Accueil.this).load(lien3).into(imageThree);
                    //////////image quatre
                    StringBuilder image4=new StringBuilder("");
                    image4.append(doc.get("imageFour"));
                    img1.setText(image4.toString());
                    String lien4 = img1.getText().toString();
                    Picasso.with(Accueil.this).load(lien4).into(imageFour);

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Accueil.this,"erreur lors du chargement du slider",Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById ( R.id.drawer_layout );
        if (drawer.isDrawerOpen ( GravityCompat.START )) {
            drawer.closeDrawer ( GravityCompat.START );
        } else {
            super.onBackPressed ();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater ().inflate ( R.menu.accueil, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId ();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search) {
           /* Intent parametre=new Intent(Accueil.this,ParametrePorfilActivity.class);
            startActivity(parametre);*/
            return true;
        }

        return super.onOptionsItemSelected ( item );
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId ();

        if (id == R.id.ic_user) {
            Intent intent = new Intent ( Accueil.this,ProfileActivity.class );
            startActivity ( intent );
        } else if (id == R.id.ic_logout) {
            Intent intenttwo = new Intent ( Accueil.this,LoginActivity.class );
            startActivity ( intenttwo );
            finish ();
        }else if (id == R.id.setting) {
            Intent parametre=new Intent(Accueil.this,ParametrePorfilActivity.class);
            startActivity(parametre);
        }
        else if (id == R.id.nous_contacter) {
            Intent nous_contacter=new Intent(Accueil.this,AproposActivity.class);
            startActivity(nous_contacter);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById ( R.id.drawer_layout );
        drawer.closeDrawer ( GravityCompat.START );
        return true;
        //nous_contacter
    }

    @Override
    protected void onStart() {
        super.onStart();
        recup();
    }
    public void vaTopost(){
        content_floating_action_btn=findViewById ( R.id.content_floating_action_btn );
        content_floating_action_btn.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent vaTopost =new Intent ( Accueil.this,PostActivity.class );
                startActivity ( vaTopost );
            }
        } );
    }
    public void nouveautes(){
        Query firstQuery =firebaseFirestore.collection ( "publication" ).document ("categories").collection ( "nouveaux" ).orderBy ( "date_de_publication",Query.Direction.DESCENDING );
        firstQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for (DocumentChange doc:queryDocumentSnapshots.getDocumentChanges()){
                    if (doc.getType()==DocumentChange.Type.ADDED){
                        String idupost=doc.getDocument ().getId ();
                        CategoriesModelNouveaux categoriesModelNouveaux =doc.getDocument().toObject(CategoriesModelNouveaux.class).withId ( idupost );
                        categoriesModelNouveauxList.add(categoriesModelNouveaux);
                        categoriesAdapteNouveaux.notifyDataSetChanged();
                    }
                }

            }
        });
    }
    public void chaussuresRecycler(){
        Query firstQuery =firebaseFirestore.collection ( "publication" ).document ("categories").collection ( "Chaussures" ).orderBy ( "date_de_publication",Query.Direction.DESCENDING );
        firstQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for (DocumentChange doc:queryDocumentSnapshots.getDocumentChanges()){
                    if (doc.getType()==DocumentChange.Type.ADDED){
                        String idupost=doc.getDocument ().getId ();
                        CategoriesModelChaussure categoriesModelChaussure =doc.getDocument().toObject(CategoriesModelChaussure.class).withId ( idupost );
                        categoriesAdapteChaussureList.add(categoriesModelChaussure);
                        categoriesAdapteChaussure.notifyDataSetChanged();
                    }
                }

            }
        });

    }
    public void juperecycler(){
        Query firstQuery =firebaseFirestore.collection ( "publication" ).document ("categories").collection ( "jupes" ).orderBy ( "date_de_publication",Query.Direction.DESCENDING );
        firstQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for (DocumentChange doc:queryDocumentSnapshots.getDocumentChanges()){
                    if (doc.getType()==DocumentChange.Type.ADDED){
                        String idupost=doc.getDocument ().getId ();
                        CategoriesModelJupe categoriesModelJupe =doc.getDocument().toObject(CategoriesModelJupe.class).withId ( idupost );
                        categoriesModelJupeList.add(categoriesModelJupe);
                        categoriesAdapteJupe.notifyDataSetChanged();
                    }
                }

            }
        });
    }
}
