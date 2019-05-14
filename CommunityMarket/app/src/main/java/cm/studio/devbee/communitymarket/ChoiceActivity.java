package cm.studio.devbee.communitymarket;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

import java.lang.ref.WeakReference;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import cm.studio.devbee.communitymarket.login.LoginActivity;
import cm.studio.devbee.communitymarket.login.RegisterActivity;

public class ChoiceActivity extends AppCompatActivity {
        private static Button gotoLogin;
        private static Button gotoRegister;
        private static ImageView devant;
        private static WeakReference<ChoiceActivity> choiceActivityWeakReference;
        private static LoginButton facebook_button;
        private static FirebaseAuth firebaseAuth;
        private static   CallbackManager callbackManager;
        private static ImageView image_de_choix;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_choice );
        gotoLogin=findViewById ( R.id.gotoLogin );
        firebaseAuth=FirebaseAuth.getInstance();
        gotoRegister=findViewById ( R.id.gotoRegister );
        facebook_button=findViewById(R.id.facebook_button);
        //image_de_choix=findViewById(R.id.image_de_choix);
        choiceActivityWeakReference=new WeakReference<>(this);
        login ();
        register ();
        printkey();
        ConstraintLayout constraintLayout=findViewById(R.id.layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
        callbackManager=CallbackManager.Factory.create();
        facebook_button.setReadPermissions("email");
        facebook_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facebookSignIn();
            }
        });
        firebaseAuth=FirebaseAuth.getInstance();
        /*if (firebaseAuth.getCurrentUser() != null) {
            // User is signed in (getCurrentUser() will be null if not signed in)
            Intent intent = new Intent(getApplicationContext(), Accueil.class);
            startActivity(intent);
            finish();
        }*/

        if(CheckNetwork.isInternetAvailable(getApplicationContext ())){//returns true if internet available{

        }
        else{

            Toast.makeText(getApplicationContext (),getString(R.string.tost_erreur_de_connexion),Toast.LENGTH_LONG).show();
            finish();
        }

        //image_de_choix.animate().scaleX(2).scaleY(2).setDuration(2000).start();
    }

    private void facebookSignIn() {
        facebook_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccesToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });



    }

    private void handleFacebookAccesToken(AccessToken loginResult) {
        Toast.makeText(getApplicationContext(),getString(R.string.bienvenu),Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(),getString(R.string.redirection),Toast.LENGTH_LONG).show();
        AuthCredential authCredential=FacebookAuthProvider.getCredential(loginResult.getToken());
        firebaseAuth.signInWithCredential(authCredential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                Intent gotoHome=new Intent(getApplicationContext(),Accueil.class);
                startActivity(gotoHome);
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }

    private void printkey() {

        try {
            PackageInfo info= getPackageManager().getPackageInfo("cm.studio.devbee.communitymarket",PackageManager.GET_SIGNATURES);
            for (Signature signature:info.signatures){
                try {
                    MessageDigest messageDigest=MessageDigest.getInstance("SHA");
                    messageDigest.update(signature.toByteArray());
                    Log.e("key",Base64.encodeToString(messageDigest.digest(),Base64.DEFAULT));
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void login(){
        gotoLogin.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent gotoLogin=new Intent ( getApplicationContext(),LoginActivity.class );
                startActivity ( gotoLogin );
                finish ();
            }
        } );
    }
    public void register(){
        gotoRegister.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent gotoRegister=new Intent ( getApplicationContext(),RegisterActivity.class );
                startActivity ( gotoRegister );
                finish ();
            }
        } );
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        gotoLogin=null;
        gotoRegister=null;
        devant=null;
    }
}
