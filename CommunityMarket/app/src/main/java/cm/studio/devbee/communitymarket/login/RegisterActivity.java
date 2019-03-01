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

import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.profile.ParametrePorfilActivity;

public class RegisterActivity extends AppCompatActivity {
private EditText email;
private  EditText password;
private  EditText confirm_password;
private Button enregister;
private TextView textView5;
private Button register_text;
private FirebaseAuth mAuth;
private ProgressBar login_progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_register );
        email=findViewById ( R.id.register_email );
        password=findViewById ( R.id.password );
        confirm_password=findViewById ( R.id.password_register );
        enregister=findViewById ( R.id.button );
        textView5=findViewById ( R.id.textView5 );
        register_text=findViewById ( R.id.register_text );
        login_progressBar=findViewById ( R.id.login_progressBarRegister );
        mAuth = FirebaseAuth.getInstance();
        createAccount ();
        register_text.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent login=new Intent ( RegisterActivity.this,LoginActivity.class );
                startActivity ( login );
                finish ();
            }
        } );
    }
    public void createAccount(){
        enregister.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                login_progressBar.setVisibility ( View.VISIBLE );
                textView5.setText ( "Bienvenue ... " );
                String user_email=email.getText ().toString ();
                String user_password=password.getText ().toString ();
                String user_confirm=confirm_password.getText ().toString ();
               if (!TextUtils.isEmpty (user_email)&&!TextUtils.isEmpty(user_password)&&!TextUtils.isEmpty(user_confirm)){
                   if (user_password.equals ( user_confirm )){
                       mAuth.createUserWithEmailAndPassword ( user_email,user_password ).addOnCompleteListener ( new OnCompleteListener<AuthResult> () {
                           @Override
                           public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful ()){
                                Intent intent=new Intent ( RegisterActivity.this,ParametrePorfilActivity.class );
                                startActivity ( intent );
                                finish ();
                            }   else {
                                login_progressBar.setVisibility ( View.INVISIBLE );
                                String error =task.getException ().getMessage ();
                                Toast.makeText ( RegisterActivity.this,error,Toast.LENGTH_LONG ).show ();
                            }
                           }
                       } );
                   }else{
                     Toast.makeText ( RegisterActivity.this,"les mots de passe ne correspondent pas !!!",Toast.LENGTH_LONG ).show ();
                   }
               }else{
                   login_progressBar.setVisibility ( View.INVISIBLE );
                    Toast.makeText ( RegisterActivity.this,"Veuillez remplir tous les champs svp !!",Toast.LENGTH_LONG ).show ();
               }

            }

        } );
    }

}
