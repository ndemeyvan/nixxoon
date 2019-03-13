package cm.studio.devbee.communitymarket.login;

import android.app.ProgressDialog;
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

import java.lang.ref.WeakReference;

import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.profile.ParametrePorfilActivity;

public class RegisterActivity extends AppCompatActivity {
private static EditText email;
private static  EditText password;
private static EditText confirm_password;
private static  Button enregister;
private static TextView textView5;
private static Button register_text;
private static FirebaseAuth mAuth;
private static ProgressBar login_progressBar;
private static  AsyncTask asyncTask;
private WeakReference<RegisterActivity> registerActivityWeakReference;

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
        registerActivityWeakReference=new WeakReference<>(this);
        asyncTask=new AsyncTask();
        asyncTask.execute();
        register_text.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent login=new Intent ( getApplicationContext(),LoginActivity.class );
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
                                Intent intent=new Intent ( getApplicationContext(),ParametrePorfilActivity.class );
                                startActivity ( intent );
                                finish ();
                            }   else {
                                login_progressBar.setVisibility ( View.INVISIBLE );
                                String error =task.getException ().getMessage ();
                                Toast.makeText ( getApplicationContext(),error,Toast.LENGTH_LONG ).show ();
                            }
                           }
                       } );
                   }else{
                     Toast.makeText ( getApplicationContext(),"les mots de passe ne correspondent pas !!!",Toast.LENGTH_LONG ).show ();
                   }
               }else{
                   login_progressBar.setVisibility ( View.INVISIBLE );
                    Toast.makeText ( getApplicationContext(),"Veuillez remplir tous les champs svp !!",Toast.LENGTH_LONG ).show ();
               }

            }

        } );
    }public class AsyncTask extends android.os.AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute ();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            createAccount ();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            asyncTask.cancel(true);
            super.onPostExecute ( aVoid );
            asyncTask.cancel(true);
            email=null;
           password=null;
             confirm_password=null;
            enregister=null;
           textView5=null;
            register_text=null;
           mAuth=null;
            login_progressBar=null;
        }
    }

}
