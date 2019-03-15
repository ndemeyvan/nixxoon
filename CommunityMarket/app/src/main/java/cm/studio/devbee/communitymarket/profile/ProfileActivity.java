package cm.studio.devbee.communitymarket.profile;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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

import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.gridView_post.GridViewAdapter;
import cm.studio.devbee.communitymarket.gridView_post.ModelGridView;
import cm.studio.devbee.communitymarket.vendeurContact.VendeurActivity;

public class ProfileActivity extends AppCompatActivity {
    private static FirebaseAuth mAuth;
    private static FirebaseFirestore firebaseFirestore;
    private static String current_user_id;
    private static TextView nom;
    private static TextView telephone;
    private static TextView residence;
    private static TextView email;
    private static TextView operation;
    private static ImageView profilImage;
    private static ProgressBar progressBar;
    private static android.support.v7.widget.Toolbar profil_toolbar;
    private WeakReference<ProfileActivity> profileActivityWeakReference;
    private static AsyncTask asyncTask;
    private static GridViewAdapter gridViewAdapter;
    private static List<ModelGridView> modelGridViewList;
    private static RecyclerView profilRecycler;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profil_toolbar=findViewById(R.id.profil_de_la_toolbar);
        setSupportActionBar(profil_toolbar);
        //////////////////
        modelGridViewList=new ArrayList<>();
        progressBar=findViewById(R.id.progressBar);
        mAuth=FirebaseAuth.getInstance();
        nom=findViewById(R.id.profil_user_name);
        telephone=findViewById(R.id.profil_user_phone);
        residence=findViewById(R.id.profil_user_residence);
        email=findViewById(R.id.profil_user_email);
        operation=findViewById(R.id.user_operations);
        firebaseFirestore=FirebaseFirestore.getInstance ();
        profilImage=findViewById(R.id.circleImageView_profil);
        progressBar.setVisibility(View.VISIBLE);
        profileActivityWeakReference=new WeakReference<>(this);
        asyncTask=new AsyncTask();
        profilRecycler=findViewById(R.id.profilRecycler);
        modelGridViewList=new ArrayList<>();
        gridViewAdapter=new GridViewAdapter(modelGridViewList,getApplicationContext());
        profilRecycler.setAdapter(gridViewAdapter);
        profilRecycler.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        asyncTask.execute();

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
                        nom.setText ( nom_user + " " + prenomuser);
                        telephone.setText ( telephone_user );
                        residence.setText ( residence_user );
                        email.setText ( email_user );
                        operation.setText("0");
                        getSupportActionBar().setTitle(prenomuser);
                        Picasso.with ( getApplicationContext() ).load ( image_profil_user ).transform(new CircleTransform()).placeholder(R.drawable.use).into ( profilImage );
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }else{

                }
            }
        } );
    }
    public void recyclerprofil(){

        Query firstQuery =firebaseFirestore.collection ( "publication" ).document ("post utilisateur").collection ( current_user_id ).orderBy ( "date_de_publication",Query.Direction.DESCENDING );
        firstQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentChange doc:queryDocumentSnapshots.getDocumentChanges()){
                    if (doc.getType()==DocumentChange.Type.ADDED){
                        ModelGridView modelGridView =doc.getDocument().toObject(ModelGridView.class);
                        modelGridViewList.add(modelGridView);
                        gridViewAdapter.notifyDataSetChanged();

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
    public class AsyncTask extends android.os.AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {

            super.onPreExecute ();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            recupererDonne();
            recyclerprofil();
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
       nom=null;
        telephone=null;
        residence=null;
        email=null;
        operation=null;
        profilImage=null;
        progressBar=null;
        profil_toolbar=null;
        profilRecycler=null;
        gridViewAdapter=null;
        modelGridViewList=null;
    }
}
