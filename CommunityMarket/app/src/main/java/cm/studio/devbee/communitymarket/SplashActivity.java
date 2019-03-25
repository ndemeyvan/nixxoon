package cm.studio.devbee.communitymarket;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {
    private static  int SPLASH_TIME_OUT=3000;
    private static FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        firebaseAuth=FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            // User is signed in (getCurrentUser() will be null if not signed in)
            Intent intent = new Intent(getApplicationContext(), Accueil.class);
            startActivity(intent);
            finish();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent gotoHome =new Intent(SplashActivity.this,ChoiceActivity.class);
                startActivity(gotoHome);
                finish();
            }
        },SPLASH_TIME_OUT);
    }
}
