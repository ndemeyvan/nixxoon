package cm.studio.devbee.communitymarket.login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import cm.studio.devbee.communitymarket.Accueil;
import cm.studio.devbee.communitymarket.R;

public class LoginActivity extends AppCompatActivity {
    private EditText loginEdit;
    private EditText registerEdit;
    private Button loginButton;
    private Button registerText;
    private TextView textView4;
    private ProgressBar login_progressBar;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_login );
        loginEdit=findViewById ( R.id.email );
        registerEdit=findViewById ( R.id.login_password );
        loginButton=findViewById ( R.id.connexion );
        registerText=findViewById ( R.id.login_register );
        textView4=findViewById ( R.id.text_connexion );
        login_progressBar=findViewById ( R.id.login_progressBar );
        mAuth = FirebaseAuth.getInstance();
        register ();
        registerText.setOnClickListener ( new View.OnClickListener () {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent ( LoginActivity.this,RegisterActivity.class );
               startActivity ( intent );
               finish ();
           }
       } );
    }
    public void register(){
        loginButton.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                textView4.setText ( "Heureux de vous revoir ... " );
                login_progressBar.setVisibility ( View.VISIBLE );
                String user_email=loginEdit.getText ().toString ();
                String user_password=registerEdit.getText ().toString ();
                if (!TextUtils.isEmpty (user_email)&&!TextUtils.isEmpty(user_password)){
                        mAuth.signInWithEmailAndPassword ( user_email,user_password ).addOnCompleteListener ( new OnCompleteListener<AuthResult> () {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful ()){
                                    Intent intent=new Intent ( LoginActivity.this,Accueil.class );
                                    startActivity ( intent );
                                    finish ();
                                }   else {
                                    login_progressBar.setVisibility ( View.INVISIBLE );
                                    String error =task.getException ().getMessage ();
                                    Toast.makeText ( LoginActivity.this,error,Toast.LENGTH_LONG ).show ();
                                }
                            }
                        } );

                }else{
                    login_progressBar.setVisibility ( View.INVISIBLE );
                    Toast.makeText ( LoginActivity.this,"Veuillez remplir tous les champs svp !!",Toast.LENGTH_LONG ).show ();
                    }
            }
        }
        );
    }
}
