package cm.studio.devbee.communitymarket.profile;

import android.Manifest;
import android.content.Intent;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

import cm.studio.devbee.communitymarket.Accueil;
import cm.studio.devbee.communitymarket.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class ParametrePorfilActivity extends AppCompatActivity {
    private android.support.v7.widget.Toolbar parametre_toolbar;
    private EditText nom;
    private EditText premon;
    private EditText telephone;
    private EditText residence;
    private EditText email;
    private Button button_enregister;
    private Uri mImageUri;
    private CircleImageView parametreImage;
    private FirebaseAuth mAuth;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private String current_user_id;
    private ProgressBar parametre_progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_parametre_porfil );
        parametre_toolbar=findViewById (R.id.parametre_toolbar  );
        setSupportActionBar ( parametre_toolbar );
        getSupportActionBar ().setTitle ( "Parametre du compte" );
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
        setimage();
        getuserdata ();
        recuperation ();
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
                      Picasso.with ( ParametrePorfilActivity.this ).load ( image_profil_user ).placeholder(R.drawable.use).into ( parametreImage );
                  }
               }else{

               }
            }
        } );

    }
    public void setimage(){
        parametreImage.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    try {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 555);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult ( requestCode, resultCode, data );
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
               mImageUri = result.getUri();
               parametreImage.setImageURI ( mImageUri );
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
    public void getuserdata(){
        button_enregister.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                final String user_name=nom.getText ().toString ();
                final String user_premon=premon.getText ().toString ();
                final String user_telephone=telephone.getText ().toString ();
                final String user_residence=residence.getText ().toString ();
                final String user_email=email.getText ().toString ();
                /////////// envoi des fichier dans la base de donnee
                if (!TextUtils.isEmpty ( user_name )&&!TextUtils.isEmpty ( user_telephone )&&!TextUtils.isEmpty ( user_premon )&&!TextUtils.isEmpty ( user_residence )&&mImageUri!=null&&!TextUtils.isEmpty ( user_email )){
                    parametre_progressbar.setVisibility ( View.VISIBLE );
                    final StorageReference image_de_profil =storageReference.child ( "image_de_profil" ).child ( current_user_id+ " .jpg" );
                    UploadTask uploadTask=image_de_profil.putFile ( mImageUri );

                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>> () {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            // Continue with the task to get the download URL
                            return image_de_profil.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                Map<String,String> donnees_utilisateur =new HashMap<> (  );
                                donnees_utilisateur.put ( "user_name",user_name );
                                donnees_utilisateur.put ( "user_prenom",user_premon );
                                donnees_utilisateur.put ( "user_telephone",user_telephone );
                                donnees_utilisateur.put ( "user_residence",user_residence );
                                donnees_utilisateur.put ( "user_mail",user_email);
                                donnees_utilisateur.put ( "user_profil_image",downloadUri.toString () );
                                firebaseFirestore.collection ( "mes donnees utilisateur" ).document (current_user_id).set ( donnees_utilisateur ).addOnCompleteListener ( new OnCompleteListener<Void> () {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful ()){
                                            Intent intent =new Intent ( ParametrePorfilActivity.this,Accueil.class );
                                            startActivity ( intent );
                                            finish ();
                                            Toast.makeText ( ParametrePorfilActivity.this,"compte enregistre",Toast.LENGTH_LONG ).show ();
                                        }else{
                                            String error =task.getException ().getMessage ();
                                            Toast.makeText ( ParametrePorfilActivity.this,error,Toast.LENGTH_LONG ).show ();
                                            parametre_progressbar.setVisibility ( View.INVISIBLE );
                                        }
                                    }
                                } );
                            } else {

                            }
                        }
                    });
                    ////////fin de l'nvoie
                    /*image_de_profil.putFile ( mImageUri ).addOnCompleteListener ( new OnCompleteListener<UploadTask.TaskSnapshot> () {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful ()){
                                Uri profil_sur_la_base_de_donnee_firebasefirestore =task.getResult ().getUploadSessionUri ();
                                Map<String,String> donnees_utilisateur =new HashMap<> (  );
                                donnees_utilisateur.put ( "user_name",user_name );
                                donnees_utilisateur.put ( "user_prenom",user_premon );
                                donnees_utilisateur.put ( "user_telephone",user_telephone );
                                donnees_utilisateur.put ( "user_residence",user_residence );
                                donnees_utilisateur.put ( "user_mail",user_email);
                                donnees_utilisateur.put ( "user_profil_image",profil_sur_la_base_de_donnee_firebasefirestore.toString () );
                                firebaseFirestore.collection ( "mes donnees utilisateur" ).document (current_user_id).set ( donnees_utilisateur ).addOnCompleteListener ( new OnCompleteListener<Void> () {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful ()){
                                            Intent intent =new Intent ( ParametrePorfilActivity.this,Accueil.class );
                                            startActivity ( intent );
                                            finish ();
                                            Toast.makeText ( ParametrePorfilActivity.this,"compte enregistre",Toast.LENGTH_LONG ).show ();
                                        }else{
                                            String error =task.getException ().getMessage ();
                                            Toast.makeText ( ParametrePorfilActivity.this,error,Toast.LENGTH_LONG ).show ();
                                            parametre_progressbar.setVisibility ( View.INVISIBLE );
                                        }
                                    }
                                } );
                            }else {
                                String error =task.getException ().getMessage ();
                                Toast.makeText ( ParametrePorfilActivity.this,error,Toast.LENGTH_LONG ).show ();
                                parametre_progressbar.setVisibility ( View.INVISIBLE );
                            }
                        }
                    } );*/
                }else {
                    Toast.makeText ( ParametrePorfilActivity.this,"remplir tous les champs svp",Toast.LENGTH_LONG ).show ();
                    parametre_progressbar.setVisibility ( View.INVISIBLE );
                }
            }
        } );
    }

    public void recuperation(){
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
    }

}
