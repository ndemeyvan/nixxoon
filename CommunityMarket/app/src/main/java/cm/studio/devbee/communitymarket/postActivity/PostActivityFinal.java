package cm.studio.devbee.communitymarket.postActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import cm.studio.devbee.communitymarket.Accueil;
import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.messagerie.ChatMessageActivity;
import cm.studio.devbee.communitymarket.profile.ParametrePorfilActivity;
import cm.studio.devbee.communitymarket.search.SearchActivity;
import id.zelory.compressor.Compressor;

public class PostActivityFinal extends AppCompatActivity implements RewardedVideoAdListener {
    private static  final int MAX_LENGTH =100;
    private static Toolbar postfinaltoolbar;
    private static EditText nomProduit;
    private static EditText descriptionProduit;
    private static EditText prixPorduit;
    private static ImageView imageProduit;
    private static String categoryName ,nom_du_produit,decription_du_produit,prix_du_produit,saveCurrentTime,saveCurrentDate;
    private static Button vendreButton;
    private static ProgressBar progressBar_post;
    private static Uri mImageUri;
    private static String randomKey;
    private static String current_user_id;
    private static FirebaseFirestore firebaseFirestore;
    private static StorageReference storageReference;
    private static FirebaseAuth firebaseAuth;
    private static Bitmap compressedImageFile;
    private static AsyncTask asyncTask;
    private static WeakReference<PostActivityFinal> postActivityWeakReference;
    private static FloatingActionButton post_new_button;
    private RewardedVideoAd mad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_post_final );
        firebaseAuth=FirebaseAuth.getInstance ();
        storageReference=FirebaseStorage.getInstance ().getReference ();
        firebaseFirestore=FirebaseFirestore.getInstance ();
        current_user_id=firebaseAuth.getCurrentUser ().getUid ();
        postfinaltoolbar=findViewById ( R.id.final_toolbar );
        setSupportActionBar(postfinaltoolbar);
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        postfinaltoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity ( new Intent ( getApplicationContext (),PostActivity.class ).setFlags ( Intent.FLAG_ACTIVITY_CLEAR_TOP ) );
                finish ();
            }
        });
        imageProduit=findViewById ( R.id.imageProduit );
        nomProduit=findViewById ( R.id.post_product_name );
        post_new_button=findViewById ( R.id.post_new_button );
        descriptionProduit=findViewById ( R.id.post_product_description );
        prixPorduit=findViewById ( R.id.post_production_prix );
        vendreButton=findViewById ( R.id.post_button );
        setSupportActionBar ( postfinaltoolbar );
        progressBar_post=findViewById ( R.id.progressBar_post );
        categoryName=getIntent ().getExtras ().get ( "categoryName" ).toString ();
        Toast.makeText ( getApplicationContext(),categoryName,Toast.LENGTH_LONG ).show ();
        asyncTask=new AsyncTask();
        asyncTask.execute();
        ///////ads"ca-app-pub-3940256099942544~3347511713
        ////my id : ca-app-pub-4353172129870258~6890094527
        MobileAds.initialize(this,"ca-app-pub-4353172129870258~6890094527");
        mad=MobileAds.getRewardedVideoAdInstance(this);
        mad.setRewardedVideoAdListener(this);
        loadRewardedVideo();

        //ads

        postActivityWeakReference=new WeakReference<>(this);


    }

    public void loadRewardedVideo(){
        if (!mad.isLoaded()){
            // ca-app-pub-3940256099942544/5224354917
            // my pub id : ca-app-pub-4353172129870258/9670857450
            mad.loadAd("ca-app-pub-4353172129870258/9670857450",new AdRequest.Builder().build());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater ().inflate ( R.menu.menu_post_article, menu );

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId ();
        if (id == R.id.send_article) {
            progressBar_post.setVisibility ( View.VISIBLE );
            vendreButton.setEnabled ( false );
            prendreDonnerDevente ();
            if (TextUtils.isEmpty ( nom_du_produit )&&TextUtils.isEmpty ( decription_du_produit )&&TextUtils.isEmpty ( prix_du_produit )&&mImageUri==null){
                progressBar_post.setVisibility (View.INVISIBLE);
                Toast.makeText ( getApplicationContext(),"Veuillez remplir tous les champs",Toast.LENGTH_LONG ).show ();

            }else{
                if (mad.isLoaded()) {
                    mad.show();
                }
            }



            return true;
        }
        return super.onOptionsItemSelected ( item );
    }

    public void setimage(){
        post_new_button.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    try {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 555);
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .start(PostActivityFinal.this);
                    }catch (Exception e){
                        e.printStackTrace ();
                    }
                } else {
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .start(PostActivityFinal.this);
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
                imageProduit.setImageURI ( mImageUri );
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public void prendreDonner(){
        vendreButton.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                progressBar_post.setVisibility ( View.VISIBLE );
                vendreButton.setEnabled ( false );
                prendreDonnerDevente ();
            }
        } );
    }
    public void prendreDonnerDevente(){
        nom_du_produit=nomProduit.getText ().toString ();
        decription_du_produit=descriptionProduit.getText ().toString ();
        prix_du_produit=(prixPorduit.getText ().toString ()+" fcfa");
        if (!TextUtils.isEmpty ( nom_du_produit )&&!TextUtils.isEmpty ( decription_du_produit )&&!TextUtils.isEmpty ( prix_du_produit )&&mImageUri!=null){
            stocker();
        }else{
            progressBar_post.setVisibility (View.INVISIBLE);
            Toast.makeText ( getApplicationContext(),"Veuillez remplir tous les champs",Toast.LENGTH_LONG ).show ();
        }
    }
    public void stocker(){
        Date date=new Date();
        SimpleDateFormat sdf= new SimpleDateFormat("d/MM/y H:mm:ss");
        final String date_avec_seconde=sdf.format(date);
                    File newImageFile= new File(mImageUri.getPath ());
                    try {
                        compressedImageFile = new Compressor(getApplicationContext())
                                .setMaxWidth(200)
                                .setMaxHeight(200)
                                .setQuality(10)
                                .compressToBitmap (newImageFile);
                    } catch (IOException e) {
                        e.printStackTrace ();
                    }
                    String random =random ();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();

                    UploadTask uploadTask=storageReference.child ( "image_des_produits" ).child ( random +".jpg" ).putBytes ( data );

                    uploadTask.addOnFailureListener(new OnFailureListener () {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText ( getApplicationContext(),"un probleme est survenue,reessayer plus tard svp" ,Toast.LENGTH_LONG).show ();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot> () {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                            Calendar calendar=Calendar.getInstance ();
                            SimpleDateFormat currentDate=new SimpleDateFormat (" dd MMM yyyy" );
                            saveCurrentDate=currentDate.format ( calendar.getTime () );
                            randomKey=saveCurrentDate;
                            String random =random ();
                            final StorageReference image_product_post=storageReference.child ( "image_des_produits_compresse" ).child ( random+".jpg" );
                            UploadTask uploadTask =image_product_post.putFile ( mImageUri );
                            Task<Uri> urlTask = uploadTask.continueWithTask( new Continuation<UploadTask.TaskSnapshot, Task<Uri>> () {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    if (!task.isSuccessful()) {
                                        throw task.getException();
                                    }
                                    // Continue with the task to get the download URL
                                    return image_product_post.getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri> () {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        Uri downloadUri = task.getResult();
                                        String download= taskSnapshot.getUploadSessionUri().toString();
                                        progressBar_post.setVisibility (View.INVISIBLE);
                                        Map <String,Object> user_post = new HashMap ();
                                        user_post.put ( "nom_du_produit",nom_du_produit );
                                        user_post.put ( "decription_du_produit",decription_du_produit );
                                        user_post.put ( "prix_du_produit",prix_du_produit );
                                        user_post.put ( "date_de_publication",randomKey );
                                        user_post.put ( "utilisateur",current_user_id );
                                        user_post.put ( "image_du_produit",downloadUri.toString() );
                                        user_post.put ( "dete-en-seconde",date_avec_seconde );
                                        user_post.put("categories",categoryName);
                                        firebaseFirestore.collection ( "publication" ).document ("categories").collection ( categoryName ).add(user_post).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                                if (task.isSuccessful()){

                                                }else{

                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(getApplicationContext(),error,Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                        firebaseFirestore.collection ( "publication" ).document ("categories").collection ( "nouveaux" ).add(user_post).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                                if (task.isSuccessful()){

                                                }else{
                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(getApplicationContext(),error,Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                        firebaseFirestore.collection ( "publication" ).document ("post utilisateur").collection ( current_user_id ).add(user_post).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                                if (task.isSuccessful()){
                                                    Intent gotoRecherche=new Intent(getApplicationContext(),Accueil.class);
                                                    startActivity(gotoRecherche);
                                                    finish();
                                                    Toast.makeText(getApplicationContext(),"envoie effectuer",Toast.LENGTH_LONG).show();
                                                }else{
                                                    vendreButton.setEnabled ( true );
                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(getApplicationContext(),error,Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                    } else {

                                    }
                                }
                            });
                        }
                    });


    }


    public static String random(){
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(MAX_LENGTH);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {

        Toast.makeText(getApplicationContext(),"regardez la video jusqu'a la fin pour poster votre article",Toast.LENGTH_LONG).show();

    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        Toast.makeText(getApplicationContext(),"merci d'avoir regarder la publicite ",Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(),"votre article est en cour d'envoie ",Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(),"Patientez svp",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        /*Toast.makeText(getApplicationContext(),"un probleme est survenu",Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(),"verifier la qualite de votre connexion internet svp. ",Toast.LENGTH_LONG).show();*/
    }

    @Override
    public void onRewardedVideoCompleted() {

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
    public void onResume() {
        mad.resume(this);
        super.onResume ();
        userstatus("online");

    }

    @Override
    public void onPause() {
        mad.pause(this);
        super.onPause ();
        userstatus("offline");

    }



    public class AsyncTask extends android.os.AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute ();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            setimage ();
            prendreDonner ();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute ( aVoid );

        }
    }
    @Override
    public void onBackPressed() {
            super.onBackPressed ();
            Intent intent=new Intent ( getApplicationContext (),PostActivity.class );
            startActivity ( intent );
            finish ();
    }



    @Override
    protected void onDestroy() {
            asyncTask.cancel(true);
            super.onDestroy();
            asyncTask.cancel(true);
            postfinaltoolbar=null;;
            nomProduit=null;;
            descriptionProduit=null;;
            prixPorduit=null;;
            imageProduit=null;;
            categoryName=null;
            nom_du_produit=null;
            decription_du_produit=null;
            prix_du_produit=null;
            saveCurrentTime=null;
            saveCurrentDate=null;;
            vendreButton=null;;
            progressBar_post=null;;
            mImageUri=null;;
            randomKey=null;;
            current_user_id=null;
            firebaseFirestore=null;;
            storageReference=null;
            firebaseAuth=null;
            compressedImageFile=null;
            mad.destroy(this);
    }
}
