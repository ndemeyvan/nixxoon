package cm.studio.devbee.communitymarket.profile;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import cm.studio.devbee.communitymarket.Accueil;
import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.gridView_post.GridViewAdapter;
import cm.studio.devbee.communitymarket.gridView_post.ModelGridView;
import cm.studio.devbee.communitymarket.messagerie.ChatMessageActivity;
import cm.studio.devbee.communitymarket.postActivity.DetailActivity;
import cm.studio.devbee.communitymarket.search.SearchActivity;
import cm.studio.devbee.communitymarket.utilsForVendeur.ProfilAdapteur;
import cm.studio.devbee.communitymarket.utilsForVendeur.VendeurAdapteur;
import cm.studio.devbee.communitymarket.vendeurContact.VendeurActivity;

public class ProfileActivity extends AppCompatActivity {
    private static FirebaseAuth mAuth;
    private static FirebaseFirestore firebaseFirestore;
    private static String current_user_id;
    private static TextView user_name;
    private static TextView telephone;
    private static TextView residence;
    private static TextView email;
    private static TextView operation;
    private static ImageView profilImage;
    private static ProgressBar progressBar;
    private static android.support.v7.widget.Toolbar profil_toolbar;
    private WeakReference<ProfileActivity> profileActivityWeakReference;
    private static AsyncTask asyncTask;
    private static ProfilAdapteur gridViewAdapter;
    private static List<ModelGridView> modelGridViewList;
    private static RecyclerView Recycler;
    private static ImageButton param_profil_button;
   private static ImageView profilbacck_image;
    private static Button vente_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profil_toolbar=findViewById(R.id.profil_de_la_toolbar);
        setSupportActionBar(profil_toolbar);
        user_name=findViewById ( R.id.user_name );
        progressBar=findViewById(R.id.progressBar);
        mAuth=FirebaseAuth.getInstance();
        param_profil_button=findViewById ( R.id.param_profil );
        telephone=findViewById(R.id.profil_user_phone);
        residence=findViewById(R.id.profil_user_residence);
        email=findViewById(R.id.profil_user_email);
        firebaseFirestore=FirebaseFirestore.getInstance ();
        profilImage=findViewById(R.id.circleImageView_profil);
        progressBar.setVisibility(View.VISIBLE);
        profilbacck_image=findViewById ( R.id.profilbacck_image );
        profileActivityWeakReference=new WeakReference<>(this);
        asyncTask=new AsyncTask();
        vente_button=findViewById ( R.id.vente_button );
        modelGridViewList=new ArrayList<>();
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        profil_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity ( new Intent ( getApplicationContext (),Accueil.class ).setFlags ( Intent.FLAG_ACTIVITY_CLEAR_TOP ) );
                finish ();
            }
        });
        /*profilImage.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent gotoparam=new Intent ( getApplicationContext (),ParametrePorfilActivity.class );
                startActivity ( gotoparam );
                finish ();
            }
        } );*/
        /*gridViewAdapter=new ProfilAdapteur(modelGridViewList,getApplicationContext());
        Recycler.setAdapter(gridViewAdapter);
        Recycler.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));*/
        asyncTask.execute();
        vente_button.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent vendre=new Intent ( ProfileActivity.this ,VenteUtilisateurActivity.class);
                startActivity ( vendre );
                //finish ();
            }
        } );

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed ();
        /*Intent gotoHome =new Intent ( ProfileActivity.this,Accueil.class );
        startActivity ( gotoHome );*/
        finish ();
    }

    public void recupererDonne(){
        current_user_id=mAuth.getCurrentUser ().getUid ();
        firebaseFirestore.collection ( "mes donnees utilisateur" ).document (current_user_id).get ().addOnCompleteListener ( new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful ()){
                    if (task.getResult ().exists ()){
                        String nom_user = task.getResult ().getString ("user_name");
                        String prenomuser =task.getResult ().getString ("user_prenom");
                        String telephone_user =task.getResult ().getString ("user_telephone");
                        String residence_user  =task.getResult ().getString ("user_residence");
                        String image_profil_user =task.getResult ().getString ("user_profil_image");
                        String email_user =task.getResult ().getString ("user_mail");
                        telephone.setText ( telephone_user );
                        residence.setText ( residence_user );
                        email.setText ( email_user );
                        getSupportActionBar().setTitle( nom_user + " " + prenomuser);
                        user_name.setText ( nom_user + " " + prenomuser );
                        user_name.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_transition_animation));
                        email.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_scale_animation));
                        Picasso.with ( getApplicationContext() ).load ( image_profil_user ).into ( profilbacck_image );
                        Picasso.with ( getApplicationContext() ).load ( image_profil_user ).transform(new CircleTransform()).placeholder(R.drawable.use).into ( profilImage );
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }else{

                }
            }
        } );
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater ().inflate ( R.menu.profile_menu, menu );
        return true;
    }*/

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId ();
        if (id == R.id.account_setting) {
            Intent gotoparam=new Intent ( getApplicationContext (),ParametrePorfilActivity.class );
            startActivity ( gotoparam );
            return true;
        }
        return super.onOptionsItemSelected ( item );
    }*/

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
            recupererDonne();
           // recyclerprofil();
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
        user_name=null;
        telephone=null;
        residence=null;
        email=null;
        operation=null;
        profilImage=null;
        progressBar=null;
        profil_toolbar=null;
        Recycler=null;
        gridViewAdapter=null;
        modelGridViewList=null;
    }
}
