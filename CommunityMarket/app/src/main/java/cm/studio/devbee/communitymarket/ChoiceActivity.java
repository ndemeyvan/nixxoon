package cm.studio.devbee.communitymarket;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import cm.studio.devbee.communitymarket.login.LoginActivity;
import cm.studio.devbee.communitymarket.login.RegisterActivity;

public class ChoiceActivity extends AppCompatActivity {
        private Button gotoLogin;
        private Button gotoRegister;
        private ImageView devant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_choice );
        gotoLogin=findViewById ( R.id.gotoLogin );
        gotoRegister=findViewById ( R.id.gotoRegister );
        devant=findViewById(R.id.devant);
        login ();
        register ();
        va();
    }
    public void login(){
        gotoLogin.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent gotoLogin=new Intent ( ChoiceActivity.this,LoginActivity.class );
                startActivity ( gotoLogin );
                finish ();
            }
        } );
    }
    public void register(){
        gotoRegister.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent gotoRegister=new Intent ( ChoiceActivity.this,RegisterActivity.class );
                startActivity ( gotoRegister );
                finish ();
            }
        } );
    }
    public void va(){
        devant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(ChoiceActivity.this,Accueil.class);
                startActivity(go);
                finish();
            }
        });
    }
}
