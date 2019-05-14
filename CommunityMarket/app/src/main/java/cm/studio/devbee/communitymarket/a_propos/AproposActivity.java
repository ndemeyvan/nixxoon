package cm.studio.devbee.communitymarket.a_propos;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import java.lang.ref.WeakReference;

import cm.studio.devbee.communitymarket.R;

public class AproposActivity extends AppCompatActivity {
    private static Button call_button;
    private static Toolbar aporpos_toolbar;
    private static WeakReference<AproposActivity> aproposActivityWeakReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apropos);
        call_button=findViewById ( R.id.call_button );
        aporpos_toolbar=findViewById(R.id.aporpos_toolbar);
        setSupportActionBar(aporpos_toolbar);
        getSupportActionBar().setTitle(getString(R.string.publicite));
        aproposActivityWeakReference=new WeakReference<>(this);
        call_button.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                String uri = "tel:+237656209008" ;
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(uri));
                startActivity(intent);
            }
        } );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        call_button=null;
        aporpos_toolbar=null;
    }
}
