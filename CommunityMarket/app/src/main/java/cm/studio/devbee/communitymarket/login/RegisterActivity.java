package cm.studio.devbee.communitymarket.login;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
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
private static ProgressBar register_progressBar;
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
        textView5=findViewById ( R.id.message_text );
        register_text=findViewById ( R.id.register_text );
        register_progressBar=findViewById ( R.id.register_progressBarRegister );
        mAuth = FirebaseAuth.getInstance();
        registerActivityWeakReference=new WeakReference<>(this);
        ConstraintLayout constraintLayout=findViewById(R.id.layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
        textView5.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_transition_animation));
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
                register_progressBar.setVisibility ( View.VISIBLE );
                textView5.setText ( getString(R.string.bienvenu));
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
                                textView5.setText ( "i n s c r i p t i o n " );
                                register_progressBar.setVisibility ( View.INVISIBLE );
                                String error =task.getException ().getMessage ();
                                Toast.makeText ( getApplicationContext(),error,Toast.LENGTH_LONG ).show ();
                            }
                           }
                       } );
                   }else{
                     Toast.makeText ( getApplicationContext(),getString(R.string.erreur_de_mot_de_passe),Toast.LENGTH_LONG ).show ();
                   }
               }else{
                   register_progressBar.setVisibility ( View.INVISIBLE );
                    Toast.makeText ( getApplicationContext(),getString(R.string.renplir_tous),Toast.LENGTH_LONG ).show ();
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
            super.onPostExecute ( aVoid );
        }
    }

    @Override
    protected void onDestroy() {
        asyncTask.cancel(true);
        super.onDestroy();
        asyncTask.cancel(true);
        email=null;
        password=null;
        confirm_password=null;
        enregister=null;
        textView5=null;
        register_text=null;
        mAuth=null;
        register_progressBar=null;
    }
}
