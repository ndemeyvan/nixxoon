package cm.studio.devbee.communitymarket.profile;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.utilsForAccessoire.CategoriesAdapteAccessoire;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private String current_user_id;
    private TextView nom;
    private TextView telephone;
    private TextView residence;
    private TextView email;
    private TextView operation;
    private ImageView profilImage;
    private ProgressBar progressBar;
    private android.support.v7.widget.Toolbar profil_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profil_toolbar=findViewById(R.id.profil_de_la_toolbar);
        setSupportActionBar(profil_toolbar);
        //////////////////
        progressBar=findViewById(R.id.progressBar);
        mAuth=FirebaseAuth.getInstance();
        nom=findViewById(R.id.profil_user_name);
        telephone=findViewById(R.id.profil_user_phone);
        residence=findViewById(R.id.profil_user_residence);
        email=findViewById(R.id.profil_user_email);
        operation=findViewById(R.id.user_operations);
        storageReference=FirebaseStorage.getInstance ().getReference ();
        firebaseFirestore=FirebaseFirestore.getInstance ();
        profilImage=findViewById(R.id.circleImageView_profil);
        progressBar.setVisibility(View.VISIBLE);
        recupererDonne();
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
                        Picasso.with ( ProfileActivity.this ).load ( image_profil_user ).transform(new CircleTransform()).placeholder(R.drawable.use).into ( profilImage );
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }else{

                }
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
