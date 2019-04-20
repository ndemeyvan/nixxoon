package cm.studio.devbee.communitymarket.profile;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import cm.studio.devbee.communitymarket.Accueil;
import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.postActivity.PostActivityFinal;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class ParametrePorfilActivity extends AppCompatActivity {
    private static android.support.v7.widget.Toolbar parametre_toolbar;
    private static EditText nom;
    private static EditText premon;
    private static EditText telephone;
    private static EditText residence;
    private static EditText email;
    private static Button button_enregister;
    private static Uri mImageUri;
    private static CircleImageView parametreImage;
    private static FirebaseAuth mAuth;
    private static StorageReference storageReference;
    private static FirebaseFirestore firebaseFirestore;
    private static String current_user_id;
    private static ProgressBar parametre_progressbar;
    private static Bitmap compressedImageFile;
    private static AsyncTask asyncTask;
    private static boolean ischange=false;
    private static ImageButton imageButton;
    private static WeakReference<ParametrePorfilActivity> parametrePorfilActivityWeakReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_parametre_porfil );
        nom=findViewById ( R.id.param_nom );
        premon=findViewById ( R.id.param_premon );
        telephone=findViewById ( R.id.param_telephone );
        residence=findViewById ( R.id.param_residence );
        email=findViewById ( R.id.param_mail );
        button_enregister=findViewById ( R.id.param_button );
        parametreImage=findViewById ( R.id.parametre_image );
        mAuth=FirebaseAuth.getInstance ();
        storageReference=FirebaseStorage.getInstance ().getReference ();
        firebaseFirestore=FirebaseFirestore.getInstance ();
        current_user_id=mAuth.getCurrentUser ().getUid ();
        parametre_progressbar=findViewById ( R.id.parametre_progressbar );
        imageButton=findViewById(R.id.imageButton);
        parametrePorfilActivityWeakReference=new WeakReference<>(this);
        asyncTask=new AsyncTask();
        asyncTask.execute();
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    try {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 555);
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .start(ParametrePorfilActivity.this);

                    }catch (Exception e){
                        e.printStackTrace ();
                    }
                } else {
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .start(ParametrePorfilActivity.this);
                }
            }
        });

    }
    public void setimage(){
        parametreImage.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    try {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 555);
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .start(ParametrePorfilActivity.this);

                    }catch (Exception e){
                        e.printStackTrace ();
                    }
                } else {
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .start(ParametrePorfilActivity.this);
                }
            }
        } );

    }


    public void getuserdata(){
        button_enregister.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                parametre_progressbar.setVisibility ( View.VISIBLE );
                final String user_name = nom.getText ().toString ();
                final String user_premon = premon.getText ().toString ();
                final String user_telephone = telephone.getText ().toString ();
                final String user_residence = residence.getText ().toString ();
                final String user_email = email.getText ().toString ();
                /////////// envoi des fichier dans la base de donnee
                if (ischange) {
                    if (!TextUtils.isEmpty ( user_name ) && !TextUtils.isEmpty ( user_telephone ) && !TextUtils.isEmpty ( user_premon ) && !TextUtils.isEmpty ( user_residence ) && mImageUri != null && !TextUtils.isEmpty ( user_email )) {
                        parametre_progressbar.setVisibility ( View.VISIBLE );
                        final StorageReference image_de_profil = storageReference.child ( "image_de_profil" ).child ( current_user_id + " .jpg" );
                        UploadTask uploadTask = image_de_profil.putFile ( mImageUri );
                        Task<Uri> urlTask = uploadTask.continueWithTask ( new Continuation<UploadTask.TaskSnapshot, Task<Uri>> () {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful ()) {
                                    throw task.getException ();
                                }
                                // Continue with the task to get the download URL
                                return image_de_profil.getDownloadUrl ();
                            }
                        } ).addOnCompleteListener ( new OnCompleteListener<Uri> () {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful ()) {
                                    stockage ( task, user_name, user_premon, user_telephone, user_residence, user_email );
                                } else {
                                    String error = task.getException ().getMessage ();
                                    Toast.makeText ( getApplicationContext (), error, Toast.LENGTH_LONG ).show ();
                                    parametre_progressbar.setVisibility ( View.INVISIBLE );
                                }
                            }
                        } );
                        ////////fin de l'nvoie
                    } else {
                        Toast.makeText ( getApplicationContext (), "remplir tous les champs svp", Toast.LENGTH_LONG ).show ();
                        parametre_progressbar.setVisibility ( View.INVISIBLE );
                    }
                }else{
                    stockage ( null, user_name, user_premon, user_telephone, user_residence, user_email );

                }
            }
        });
    }

    public void stockage(@NonNull Task<Uri> task,String user_name,String user_premon,String user_telephone,String user_residence,String user_email ){
        Uri downloadUri;

        if (task!=null){
            downloadUri = task.getResult ();
        }else{
            downloadUri=mImageUri;
        }
        Calendar calendar=Calendar.getInstance ();
        SimpleDateFormat currentDate=new SimpleDateFormat (" dd MMM yyyy" );
       String saveCurrentDate=currentDate.format ( calendar.getTime () );
        String randomKey=saveCurrentDate;
        Map<String, String> donnees_utilisateur = new HashMap<> ();
        donnees_utilisateur.put ( "user_name",user_name.toUpperCase().charAt(0)+""+user_name.substring(1, user_name.length()) );
        donnees_utilisateur.put ( "user_prenom",user_premon.toUpperCase().charAt(0)+""+user_premon.substring(1, user_premon.length()));
        donnees_utilisateur.put ( "user_telephone", user_telephone );
        donnees_utilisateur.put ( "user_residence", user_residence );
        donnees_utilisateur.put ( "user_mail", user_email );
        donnees_utilisateur.put ( "user_profil_image", downloadUri.toString () );
        donnees_utilisateur.put ( "id_utilisateur", current_user_id);
        donnees_utilisateur.put ( "status","online" );
        donnees_utilisateur.put ( "search",user_name.toLowerCase() );
        donnees_utilisateur.put ( "message","lu" );
        donnees_utilisateur.put ( "derniere_conection",randomKey );

        firebaseFirestore.collection ( "mes donnees utilisateur" ).document ( current_user_id ).set ( donnees_utilisateur ).addOnCompleteListener ( new OnCompleteListener<Void> () {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful ()) {
                    Intent intent = new Intent ( getApplicationContext (), Accueil.class );
                    startActivity ( intent );
                    finish ();
                    Toast.makeText ( getApplicationContext (), "compte enregistre", Toast.LENGTH_LONG ).show ();
                } else {
                    String error = task.getException ().getMessage ();
                    Toast.makeText ( getApplicationContext (), error, Toast.LENGTH_LONG ).show ();
                    parametre_progressbar.setVisibility ( View.INVISIBLE );
                }
            }
        } );
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult ( requestCode, resultCode, data );
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mImageUri = result.getUri();
                parametreImage.setImageURI ( mImageUri );
                ischange=true;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
    public void userstatus(String status){
        DocumentReference user = firebaseFirestore.collection("mes donnees utilisateur" ).document(current_user_id);
        user.update("status", status)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume ();
        userstatus("online");

    }

    @Override
    protected void onPause() {
        super.onPause ();
        userstatus("offline");
    }

    /* public void recuperation(){
                firebaseFirestore.collection ( "mes donnees utilisateur" ).document (current_user_id).get ().addOnCompleteListener ( new OnCompleteListener<DocumentSnapshot> () {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        String nom_user = task.getResult ().toString ();
                        String prenom_user =task.getResult ().toString ();
                        String telephone_user =task.getResult ().toString ();
                        String residence_user  =task.getResult ().toString ();
                        String image_profil_user =task.getResult ().toString ();

                    }
                } );
            }*/
    public class AsyncTask extends android.os.AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {

            super.onPreExecute ();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            setimage();
            getuserdata ();
           // recuperation ();
            firebaseFirestore.collection ( "mes donnees utilisateur" ).document (current_user_id).get ().addOnCompleteListener ( new OnCompleteListener<DocumentSnapshot> () {
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
                            nom.setText ( nom_user );
                            premon.setText ( prenomuser );
                            telephone.setText ( telephone_user );
                            residence.setText ( residence_user );
                            email.setText ( email_user );
                            mImageUri=Uri.parse ( image_profil_user );
                            parametre_progressbar.setVisibility ( View.INVISIBLE );
                            Picasso.with ( getApplicationContext()).load ( image_profil_user ).placeholder(R.drawable.use).into ( parametreImage );
                        }
                    }else{

                    }
                }
            } );
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
        parametre_toolbar=null;
        nom=null;
        premon=null;
        telephone=null;
       residence=null;
        email=null;
        button_enregister=null;
        mImageUri=null;
        parametreImage=null;
        mAuth=null;
       storageReference=null;
       firebaseFirestore=null;
       current_user_id=null;
        parametre_progressbar=null;
        compressedImageFile=null;
    }
}
