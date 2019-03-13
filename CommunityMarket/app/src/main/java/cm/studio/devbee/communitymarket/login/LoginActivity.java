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

import cm.studio.devbee.communitymarket.Accueil;
import cm.studio.devbee.communitymarket.R;

public class LoginActivity extends AppCompatActivity {
    private static EditText loginEdit;
    private static EditText registerEdit;
    private static Button loginButton;
    private static Button registerText;
    private static TextView textView4;
    private static ProgressBar login_progressBar;
    private static FirebaseAuth mAuth;
    private static WeakReference<LoginActivity> loginActivityWeakReference;
    private static   AsyncTask asyncTask;
    private static TextView home;



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
        home=findViewById(R.id.home);
        mAuth = FirebaseAuth.getInstance();
        asyncTask=new AsyncTask();
        asyncTask.execute();
        loginActivityWeakReference=new WeakReference<>(this);
        registerText.setOnClickListener ( new View.OnClickListener () {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent ( getApplicationContext(),RegisterActivity.class );
               startActivity ( intent );
               finish ();
           }
       } );
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home=new Intent ( getApplicationContext(),Accueil.class );
                startActivity ( home );
                finish ();
            }
        });
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
                                    Intent intent=new Intent ( getApplicationContext(),Accueil.class );
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
                    login_progressBar.setVisibility ( View.INVISIBLE );
                    Toast.makeText ( getApplicationContext(),"Veuillez remplir tous les champs svp !!",Toast.LENGTH_LONG ).show ();
                    }
            }
        }
        );
    }
    public class AsyncTask extends android.os.AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute ();
        }

        @Override
        protected Void doInBackground(Void... voids) {
           register();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute ( aVoid );

        }
    }

    @Override
    protected void onDestroy() {
        asyncTask.cancel(true);
        super.onDestroy();
        asyncTask.cancel(true);
        loginEdit=null;
       registerEdit=null;
        loginButton=null;
        registerText=null;
        textView4=null;
         login_progressBar=null;
        mAuth=null;
    }
}
