package cm.studio.devbee.communitymarket;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

import cm.studio.devbee.communitymarket.login.LoginActivity;
import cm.studio.devbee.communitymarket.login.RegisterActivity;

public class ChoiceActivity extends AppCompatActivity {
        private static Button gotoLogin;
        private static Button gotoRegister;
        private static ImageView devant;
        private static WeakReference<ChoiceActivity> choiceActivityWeakReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_choice );
        gotoLogin=findViewById ( R.id.gotoLogin );
        gotoRegister=findViewById ( R.id.gotoRegister );
        devant=findViewById(R.id.devant);
        choiceActivityWeakReference=new WeakReference<>(this);
        login ();
        register ();
        va();
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
