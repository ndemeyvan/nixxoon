package cm.studio.devbee.communitymarket;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
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

import cm.studio.devbee.communitymarket.Fragments.HomeFragment;
import cm.studio.devbee.communitymarket.Fragments.TshirtFragment;
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
    private TabLayout tabLayout;
    private ViewPager tabsviewpager;
    private TabsAdapter tabsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_accueil );
        Toolbar toolbar =findViewById ( R.id.toolbar );
        setSupportActionBar ( toolbar );
        NavigationView navigationView =findViewById ( R.id.nav_view );

        tabLayout=findViewById(R.id.tabslayout);
        tabsviewpager=findViewById(R.id.tabsview);
        tabsAdapter =new TabsAdapter(getSupportFragmentManager());
        setupViewPager(tabsviewpager);
        tabLayout.setupWithViewPager(tabsviewpager);
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
        vaTopost ();




    }
    public void setupViewPager(ViewPager viewPager){
        TabsAdapter tabsAdapter=new TabsAdapter(getSupportFragmentManager());
        tabsAdapter.addFragment(new HomeFragment(),"Home");
        tabsAdapter.addFragment(new TshirtFragment(),"T_shirt");
        tabsAdapter.addFragment(new HomeFragment(),"chaussure");
        tabsAdapter.addFragment(new HomeFragment(),"jupes");
        tabsAdapter.addFragment(new HomeFragment(),"accessoire");
        tabsAdapter.addFragment(new HomeFragment(),"cullotes");
        tabsAdapter.addFragment(new HomeFragment(),"pantalons");
        tabsAdapter.addFragment(new HomeFragment(),"chemises");
        tabsAdapter.addFragment(new HomeFragment(),"robe");
        tabsAdapter.addFragment(new HomeFragment(),"pull");
        viewPager.setAdapter(tabsAdapter);


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

