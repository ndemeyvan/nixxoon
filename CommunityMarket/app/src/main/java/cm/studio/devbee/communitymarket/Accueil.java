package cm.studio.devbee.communitymarket;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
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
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import cm.studio.devbee.communitymarket.Utilpantalons.CategoriesAdaptePantalons;
import cm.studio.devbee.communitymarket.Utilpantalons.CategoriesModelPantalons;
import cm.studio.devbee.communitymarket.a_propos.AproposActivity;
import cm.studio.devbee.communitymarket.login.LoginActivity;
import cm.studio.devbee.communitymarket.postActivity.PostActivity;
import cm.studio.devbee.communitymarket.profile.ParametrePorfilActivity;
import cm.studio.devbee.communitymarket.profile.ProfileActivity;
import cm.studio.devbee.communitymarket.utilForCullotes.CategoriesAdapteCullote;
import cm.studio.devbee.communitymarket.utilForCullotes.CategoriesModelCullote;
import cm.studio.devbee.communitymarket.utilForJupe.CategoriesAdapteJupe;
import cm.studio.devbee.communitymarket.utilForJupe.CategoriesModelJupe;
import cm.studio.devbee.communitymarket.utilForPost.CategoriesModelPost;
import cm.studio.devbee.communitymarket.utilForT_shirt.CategoriesAdapteTshirt;
import cm.studio.devbee.communitymarket.utilForT_shirt.CategoriesModelTshirt;
import cm.studio.devbee.communitymarket.utilsForAccessoire.CategoriesAdapteAccessoire;
import cm.studio.devbee.communitymarket.utilsForAccessoire.CategoriesModelAccessoire;
import cm.studio.devbee.communitymarket.utilsForCategories.CategoriesAdapte;
import cm.studio.devbee.communitymarket.utilsForCategories.CategoriesModel;
import cm.studio.devbee.communitymarket.utilsForChaussure.CategoriesAdapteChaussure;
import cm.studio.devbee.communitymarket.utilsForChaussure.CategoriesModelChaussure;
import cm.studio.devbee.communitymarket.utilsForChemise.CategoriesAdapteChemise;
import cm.studio.devbee.communitymarket.utilsForChemise.CategoriesModelChemise;
import cm.studio.devbee.communitymarket.utilsForNouveautes.CategoriesAdapteNouveaux;
import cm.studio.devbee.communitymarket.utilsForNouveautes.CategoriesModelNouveaux;
import cm.studio.devbee.communitymarket.utilsForPull.CategoriesAdaptePull;
import cm.studio.devbee.communitymarket.utilsForPull.CategoriesModelPull;
import cm.studio.devbee.communitymarket.utilsForRobe.CategoriesAdapteRobe;
import cm.studio.devbee.communitymarket.utilsForRobe.CategoriesModelRobe;
import cm.studio.devbee.communitymarket.utilsForUserApp.UserAdapter;
import cm.studio.devbee.communitymarket.utilsForUserApp.UserModel;
import de.hdodenhof.circleimageview.CircleImageView;

public class Accueil extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth mAuth;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private String current_user_id;
    private ImageView acceuille_image;
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
    private RecyclerView user_recyclerView;
    private UserAdapter userAdapter;
    private List<UserModel> userList;
    //////////tshirt
    private CategoriesAdapteTshirt categoriesAdapteTshirt;
    private List<CategoriesModelTshirt> categoriesModelTshirtList;
    private RecyclerView tshirtRecycler;
    //////chargement
    private DocumentSnapshot lastVisible;
    private  RecyclerView recyclerpull;
    private CategoriesAdaptePull categoriesAdaptePull;
    List<CategoriesModelPull> categoriesModelPullList;
    ////accesoire
    private  List<CategoriesModelAccessoire> categoriesModelAccessoireList;
    private CategoriesAdapteAccessoire categoriesAdapteAccessoire;
    private RecyclerView recycleraccessoire;
    /////pubfixe
    private ImageView imagePubFixe;
    private TextView imagePubText;
    //cullote
    private RecyclerView recyclercullote;
    private CategoriesAdapteCullote categoriesAdapteCullote;
    private List<CategoriesModelCullote> categoriesModelCulloteList;
    ////pantaloos
    private  RecyclerView recyclerpantalons;
    private CategoriesAdaptePantalons categoriesAdaptePantalons;
    List<CategoriesModelPantalons> categoriesModelPantalonsList;
    ///chemise
    private RecyclerView recyclerchemise;
    private CategoriesAdapteChemise categoriesAdapteChemise;
    private List<CategoriesModelChemise> categoriesModelChemiseList;
    ///robe
    private List<CategoriesModelRobe> categoriesModelRobeList;
    private CategoriesAdapteRobe categoriesAdapteRobe;
    private RecyclerView recyclerrobe;


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
        nouveauxRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Boolean arriveOcoin = !nouveauxRecyclerView.canScrollHorizontally(1);
                if (arriveOcoin){
                    Toast.makeText(Accueil.this,"chargement",Toast.LENGTH_LONG).show();
                    chargerPlus();
                }else{

                }

            }
        });
        ////nouveaux
        ///////jupes
        jupesRecyclerView=findViewById ( R.id.jupesRecyclerView );
        categoriesModelJupeList=new ArrayList<> (  );
        categoriesAdapteJupe=new CategoriesAdapteJupe (categoriesModelJupeList,Accueil.this);
        jupesRecyclerView.setAdapter ( categoriesAdapteJupe );
        jupesRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        ///////fin jupes
        ///////:utilisateur
        user_recyclerView=findViewById ( R.id.user_recyclerView );
        userList=new ArrayList<> (  );
        userAdapter=new UserAdapter (userList,Accueil.this);
        user_recyclerView.setAdapter ( userAdapter );
        user_recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        //////fin utilisateur
        //////tshirt
        tshirtRecycler=findViewById ( R.id.tshirtRecycler );
        categoriesModelTshirtList=new ArrayList<> (  );
        categoriesAdapteTshirt=new CategoriesAdapteTshirt (categoriesModelTshirtList,Accueil.this);
        tshirtRecycler.setAdapter ( categoriesAdapteTshirt );
        tshirtRecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        ///fin tshirt
        //////pull
        recyclerpull=findViewById ( R.id.recyclerpull );
        categoriesModelPullList=new ArrayList<> (  );
        categoriesAdaptePull=new CategoriesAdaptePull (categoriesModelPullList,Accueil.this);
        recyclerpull.setAdapter ( categoriesAdaptePull );
        recyclerpull.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        /////pull
        ///accessoire
        recycleraccessoire=findViewById ( R.id.recycleraccessoire );
        categoriesModelAccessoireList=new ArrayList<> (  );
        categoriesAdapteAccessoire=new CategoriesAdapteAccessoire (categoriesModelAccessoireList,Accueil.this);
        recycleraccessoire.setAdapter ( categoriesAdapteAccessoire );
        recycleraccessoire.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        //accessoire
        ///cullote
        recyclercullote=findViewById ( R.id.recyclercullote );
        categoriesModelCulloteList=new ArrayList<> (  );
        categoriesAdapteCullote=new CategoriesAdapteCullote (categoriesModelCulloteList,Accueil.this);
        recyclercullote.setAdapter ( categoriesAdapteCullote );
        recyclercullote.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        //culotte
        ///pantalos
        recyclerpantalons=findViewById ( R.id. recyclerpantalons );
        categoriesModelPantalonsList=new ArrayList<> (  );
        categoriesAdaptePantalons=new CategoriesAdaptePantalons (categoriesModelPantalonsList,Accueil.this);
        recyclerpantalons.setAdapter ( categoriesAdaptePantalons );
        recyclerpantalons.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        //pantalons
        ////chemise
        recyclerchemise=findViewById ( R.id. recyclerchemise );
        categoriesModelChemiseList=new ArrayList<> (  );
        categoriesAdapteChemise=new CategoriesAdapteChemise(categoriesModelChemiseList,Accueil.this);
        recyclerchemise.setAdapter ( categoriesAdapteChemise );
        recyclerchemise.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        //chemise
        ////robe
        recyclerrobe=findViewById(R.id.recyclerrobe);
        categoriesModelRobeList=new ArrayList<> (  );
        categoriesAdapteRobe=new CategoriesAdapteRobe(categoriesModelRobeList,Accueil.this);
        recyclerrobe.setAdapter ( categoriesAdapteRobe );
        recyclerrobe.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        //robe
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
        categoriesModelList.add ( new CategoriesModel ( "accessoires",R.drawable.accessoires ) );
        categoriesModelList.add ( new CategoriesModel ( "Cullotes",R.drawable.cullotes ) );
        categoriesModelList.add ( new CategoriesModel ( "Pantalons",R.drawable.pantalons ) );
        categoriesModelList.add ( new CategoriesModel ( "T-shirts",R.drawable.t_shirt ) );
        categoriesModelList.add ( new CategoriesModel ( "Chemises",R.drawable.chemise ) );
        categoriesModelList.add ( new CategoriesModel ( "robe",R.drawable.robe ) );
        categoriesModelList.add ( new CategoriesModel( "pull",R.drawable.robe ) );
        categoriesAdapte=new CategoriesAdapte ( categoriesModelList,Accueil.this );
        content_recyclerView.setAdapter ( categoriesAdapte );
        content_recyclerView.setLayoutManager ( new LinearLayoutManager ( this,LinearLayoutManager.HORIZONTAL,false ) );
        ///////fin recyclerview
        imagePubFixe=findViewById(R.id.pubImage);
        imagePubText=findViewById(R.id.pubImageText);

        vaTopost ();
        nouveautes();
        chaussuresRecycler ();
        juperecycler ();
        utilisateurREcycler ();
        tshirtRecyclerView();
        recyclerPull();
        recyclerAccessoire();
        imagePub();
        recyclerCullote();
        recyclerpantalons();
        recyclerChemise();
        recyclerRobe();

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
                        Picasso.with ( Accueil.this ).load ( image_profil_user ).transform(new CircleTransform()).placeholder(R.drawable.use).into ( acceuille_image );
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
    public void imagePub(){
        DocumentReference user = firebaseFirestore.collection("publicit").document("imageFixe");
        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()){
                    DocumentSnapshot doc =task.getResult();
                    StringBuilder imagePub=new StringBuilder("");
                    imagePub.append(doc.get("pub"));
                    imagePubText.setText(imagePub.toString());
                    String lien = imagePubText.getText().toString();
                    Picasso.with(Accueil.this).load(lien).into(imagePubFixe);

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
        Query firstQuery =firebaseFirestore.collection ( "publication" ).document ("categories").collection ( "nouveaux" ).orderBy ( "date_de_publication",Query.Direction.DESCENDING )
                .limit(3);
        firstQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                lastVisible = queryDocumentSnapshots.getDocuments()
                        .get(queryDocumentSnapshots.size() -1);
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
    public void utilisateurREcycler(){
        Query firstQuery =firebaseFirestore.collection ( "mes donnees utilisateur" ).orderBy ( "user_telephone",Query.Direction.DESCENDING );
        firstQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for (DocumentChange doc:queryDocumentSnapshots.getDocumentChanges()){
                    if (doc.getType()==DocumentChange.Type.ADDED){
                        UserModel userModel =doc.getDocument().toObject(UserModel.class);
                        userList.add(userModel);
                        userAdapter.notifyDataSetChanged();
                    }
                }

            }
        });

    }
    public void tshirtRecyclerView(){
        Query firstQuery =firebaseFirestore.collection ( "publication" ).document ("categories").collection ( "T-shirts" ).orderBy ( "date_de_publication",Query.Direction.DESCENDING );
        firstQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for (DocumentChange doc:queryDocumentSnapshots.getDocumentChanges()){
                    if (doc.getType()==DocumentChange.Type.ADDED){
                        String idupost=doc.getDocument ().getId ();
                        CategoriesModelTshirt categoriesModelTshirt =doc.getDocument().toObject(CategoriesModelTshirt.class).withId ( idupost );
                        categoriesModelTshirtList.add(categoriesModelTshirt);
                        categoriesAdapteTshirt.notifyDataSetChanged();
                    }
                }

            }
        });
    }

    public void chargerPlus(){

        Query prochain =firebaseFirestore.collection ( "publication" ).document ("categories").collection ( "nouveaux" ).orderBy ( "date_de_publication",Query.Direction.DESCENDING )
                .startAfter(lastVisible)
                .limit(3);


        prochain.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    lastVisible = queryDocumentSnapshots.getDocuments()
                            .get(queryDocumentSnapshots.size() - 1);
                    for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                        if (doc.getType() == DocumentChange.Type.ADDED) {
                            String idupost = doc.getDocument().getId();
                            CategoriesModelNouveaux categoriesModelNouveaux = doc.getDocument().toObject(CategoriesModelNouveaux.class).withId(idupost);
                            categoriesModelNouveauxList.add(categoriesModelNouveaux);
                            categoriesAdapteNouveaux.notifyDataSetChanged();
                        }
                    }
                }else {

                }

            }
        });
    }
    public void recyclerPull(){
        Query firstQuery =firebaseFirestore.collection ( "publication" ).document ("categories").collection ( "pull" ).orderBy ( "date_de_publication",Query.Direction.DESCENDING );
        firstQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for (DocumentChange doc:queryDocumentSnapshots.getDocumentChanges()){
                    if (doc.getType()==DocumentChange.Type.ADDED){
                        String idupost=doc.getDocument ().getId ();
                        CategoriesModelPull categoriesModelPull =doc.getDocument().toObject(CategoriesModelPull.class).withId ( idupost );
                        categoriesModelPullList.add(categoriesModelPull);
                        categoriesAdaptePull.notifyDataSetChanged();
                    }
                }

            }
        });
    }
    public void recyclerAccessoire(){
        Query firstQuery =firebaseFirestore.collection ( "publication" ).document ("categories").collection ( "accessoires" ).orderBy ( "date_de_publication",Query.Direction.DESCENDING );
        firstQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for (DocumentChange doc:queryDocumentSnapshots.getDocumentChanges()){
                    if (doc.getType()==DocumentChange.Type.ADDED){
                        String idupost=doc.getDocument ().getId ();
                        CategoriesModelAccessoire categoriesModelAccessoire =doc.getDocument().toObject(CategoriesModelAccessoire.class).withId ( idupost );
                        categoriesModelAccessoireList.add(categoriesModelAccessoire);
                        categoriesAdapteAccessoire.notifyDataSetChanged();
                    }
                }

            }
        });
    }
    public void recyclerCullote(){
        Query firstQuery =firebaseFirestore.collection ( "publication" ).document ("categories").collection ( "Cullotes" ).orderBy ( "date_de_publication",Query.Direction.DESCENDING );
        firstQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for (DocumentChange doc:queryDocumentSnapshots.getDocumentChanges()){
                    if (doc.getType()==DocumentChange.Type.ADDED){
                        String idupost=doc.getDocument ().getId ();
                        CategoriesModelCullote categoriesModelCullote =doc.getDocument().toObject(CategoriesModelCullote.class).withId ( idupost );
                        categoriesModelCulloteList.add(categoriesModelCullote);
                        categoriesAdapteCullote.notifyDataSetChanged();
                    }
                }

            }
        });
    }
    public void recyclerpantalons(){
        Query firstQuery =firebaseFirestore.collection ( "publication" ).document ("categories").collection ( "Pantalons" ).orderBy ( "date_de_publication",Query.Direction.DESCENDING );
        firstQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for (DocumentChange doc:queryDocumentSnapshots.getDocumentChanges()){
                    if (doc.getType()==DocumentChange.Type.ADDED){
                        String idupost=doc.getDocument ().getId ();
                        CategoriesModelPantalons categoriesModelPantalons =doc.getDocument().toObject(CategoriesModelPantalons.class).withId ( idupost );
                        categoriesModelPantalonsList.add(categoriesModelPantalons);
                        categoriesAdaptePantalons.notifyDataSetChanged();
                    }
                }

            }
        });
    }
    public void recyclerChemise(){
        Query firstQuery =firebaseFirestore.collection ( "publication" ).document ("categories").collection ( "Chemises" ).orderBy ( "date_de_publication",Query.Direction.DESCENDING );
        firstQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for (DocumentChange doc:queryDocumentSnapshots.getDocumentChanges()){
                    if (doc.getType()==DocumentChange.Type.ADDED){
                        String idupost=doc.getDocument ().getId ();
                        CategoriesModelChemise categoriesModelChemise=doc.getDocument().toObject(CategoriesModelChemise.class).withId ( idupost );
                        categoriesModelChemiseList.add(categoriesModelChemise);
                        categoriesAdapteChemise.notifyDataSetChanged();
                    }
                }

            }
        });
    }
    public void recyclerRobe(){
        Query firstQuery =firebaseFirestore.collection ( "publication" ).document ("categories").collection ( "robe" ).orderBy ( "date_de_publication",Query.Direction.DESCENDING );
        firstQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for (DocumentChange doc:queryDocumentSnapshots.getDocumentChanges()){
                    if (doc.getType()==DocumentChange.Type.ADDED){
                        String idupost=doc.getDocument ().getId ();
                        CategoriesModelRobe categoriesModelRobe=doc.getDocument().toObject(CategoriesModelRobe.class).withId ( idupost );
                        categoriesModelRobeList.add(categoriesModelRobe);
                        categoriesAdapteRobe.notifyDataSetChanged();
                    }
                }

            }
        });
    }
    public class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }


    }

}
