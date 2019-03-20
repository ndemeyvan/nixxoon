package cm.studio.devbee.communitymarket;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.lang.ref.WeakReference;
import java.util.List;

import cm.studio.devbee.communitymarket.Fragments.AccesoireFragment;
import cm.studio.devbee.communitymarket.Fragments.ChaussureFragment;
import cm.studio.devbee.communitymarket.Fragments.ChemiseFragment;
import cm.studio.devbee.communitymarket.Fragments.CulloteFragment;
import cm.studio.devbee.communitymarket.Fragments.HomeFragment;
import cm.studio.devbee.communitymarket.Fragments.JupesFragment;
import cm.studio.devbee.communitymarket.Fragments.PantalonFragment;
import cm.studio.devbee.communitymarket.Fragments.PullFragment;
import cm.studio.devbee.communitymarket.Fragments.RobeFragment;
import cm.studio.devbee.communitymarket.Fragments.TshirtFragment;
import cm.studio.devbee.communitymarket.a_propos.AproposActivity;
import cm.studio.devbee.communitymarket.login.LoginActivity;
import cm.studio.devbee.communitymarket.postActivity.PostActivity;
import cm.studio.devbee.communitymarket.profile.ParametrePorfilActivity;
import cm.studio.devbee.communitymarket.profile.ProfileActivity;
import cm.studio.devbee.communitymarket.search.SearchActivity;
import cm.studio.devbee.communitymarket.utilsForNouveautes.CategoriesModelNouveaux;
import cm.studio.devbee.communitymarket.utilsforsearch.SearchModel;

public class Accueil extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private String current_user_id;
    private ImageView acceuille_image;
    private TextView drawer_user_name;
    private FloatingActionButton content_floating_action_btn;
    private TabLayout tabLayout;
    private ViewPager tabsviewpager;
    private static AsyncTask asyncTask;
    private static List<SearchModel> searchModelList;
    private static WeakReference<Accueil> accueilWeakReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_accueil );
        Toolbar toolbar =findViewById ( R.id.toolbar );
        setSupportActionBar ( toolbar );
        NavigationView navigationView =findViewById ( R.id.nav_view );
        tabLayout=findViewById(R.id.tabslayout);
        tabsviewpager=findViewById(R.id.tabsview);
        setupViewPager(tabsviewpager);
        tabLayout.setupWithViewPager(tabsviewpager);
        mAuth=FirebaseAuth.getInstance ();
        firebaseFirestore=FirebaseFirestore.getInstance ();
        accueilWeakReference=new WeakReference<>(this);
        acceuille_image=navigationView.getHeaderView(0).findViewById(R.id.acceuille_image);
        drawer_user_name=navigationView.getHeaderView(0).findViewById(R.id.drawer_user_name);
        DrawerLayout drawer =findViewById ( R.id.drawer_layout );
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle (
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
        drawer.addDrawerListener ( toggle );
        toggle.syncState ();
        navigationView.setNavigationItemSelectedListener ( this );
        asyncTask=new AsyncTask();
        asyncTask.execute();

    }
    public void setupViewPager(ViewPager viewPager){
        TabsAdapter tabsAdapter=new TabsAdapter(getSupportFragmentManager());
        tabsAdapter.addFragment(new HomeFragment(),"Decouvrir");
        tabsAdapter.addFragment(new TshirtFragment(),"T_shirts");
        tabsAdapter.addFragment(new PullFragment () ,"Pulls");
        tabsAdapter.addFragment(new JupesFragment () ,"jupes");
        tabsAdapter.addFragment(new ChaussureFragment (),"chaussures");
        tabsAdapter.addFragment(new PantalonFragment (),"pantalons");
        tabsAdapter.addFragment(new CulloteFragment (),"cullotes");
        tabsAdapter.addFragment(new ChemiseFragment (),"chemises");
        tabsAdapter.addFragment(new RobeFragment (),"robes");
        tabsAdapter.addFragment(new AccesoireFragment (),"accessoire");
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
                        Picasso.with ( getApplicationContext()).load ( image_profil_user ).transform(new CircleTransform()).placeholder(R.drawable.use).into ( acceuille_image );
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
            Intent gogotoSearch = new Intent(getApplicationContext(),SearchActivity.class);
            startActivity(gogotoSearch);
            return true;
        }

        return super.onOptionsItemSelected ( item );
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId ();

        if (id == R.id.ic_user) {
            Intent intent = new Intent ( getApplicationContext(),ProfileActivity.class );
            startActivity ( intent );
        } else if (id == R.id.ic_logout) {
            Intent intenttwo = new Intent ( getApplicationContext(),LoginActivity.class );
            startActivity ( intenttwo );
            finish ();
        }else if (id == R.id.setting) {
            Intent parametre=new Intent(getApplicationContext(),ParametrePorfilActivity.class);
            startActivity(parametre);
        }
        else if (id == R.id.nous_contacter) {
            Intent nous_contacter=new Intent(getApplicationContext(),AproposActivity.class);
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
                Intent vaTopost =new Intent ( getApplicationContext(),PostActivity.class );
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
    public class AsyncTask extends android.os.AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {

            super.onPreExecute ();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            recup();
            vaTopost ();
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
        mAuth=null;
        firebaseFirestore=null;
        current_user_id=null;
       acceuille_image=null;
       drawer_user_name=null;
       content_floating_action_btn=null;
      tabLayout=null;
        tabsviewpager=null;
         accueilWeakReference=null;
    }
}

