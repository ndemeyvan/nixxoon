package cm.studio.devbee.communitymarket;

import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler (  ).postDelayed ( new Runnable () {
            @Override
            public void run() {
               Intent gotochoice= new Intent(getApplicationContext(),ChoiceActivity.class);
                startActivity(gotochoice);
                finish();
            }
        }, 3500 );
        //SystemClock.sleep(3000);


    }
}
