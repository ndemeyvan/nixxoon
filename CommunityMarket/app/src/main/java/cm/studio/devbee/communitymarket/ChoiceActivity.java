package cm.studio.devbee.communitymarket;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
        private static  Button twitter_button;
        private static FirebaseAuth firebaseAuth;
        private static   CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_choice );
        gotoLogin=findViewById ( R.id.gotoLogin );
        firebaseAuth=FirebaseAuth.getInstance();
        gotoRegister=findViewById ( R.id.gotoRegister );
        devant=findViewById(R.id.devant);
        facebook_button=findViewById(R.id.facebook_button);
        twitter_button=findViewById(R.id.twitter_button);
        choiceActivityWeakReference=new WeakReference<>(this);
        login ();
        register ();
        va();
        printkey();
        
        if (firebaseAuth.getCurrentUser() != null) {
            // User is signed in (getCurrentUser() will be null if not signed in)
            Intent intent = new Intent(getApplicationContext(), Accueil.class);
            startActivity(intent);
            finish();
        }

        callbackManager=CallbackManager.Factory.create();
        facebook_button.setReadPermissions("email");
        facebook_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facebookSignIn();
            }
        });
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
    public void va(){
        devant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(getApplicationContext(),Accueil.class);
                startActivity(go);
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gotoLogin=null;
      gotoRegister=null;
         devant=null;
    }
}
