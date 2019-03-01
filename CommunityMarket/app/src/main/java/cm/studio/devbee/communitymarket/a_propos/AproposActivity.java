package cm.studio.devbee.communitymarket.a_propos;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import cm.studio.devbee.communitymarket.R;

public class AproposActivity extends AppCompatActivity {
    private Button call_button;
    private Toolbar aporpos_toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apropos);
        call_button=findViewById ( R.id.call_button );
        aporpos_toolbar=findViewById(R.id.aporpos_toolbar);
        setSupportActionBar(aporpos_toolbar);
        getSupportActionBar().setTitle("publiciter");

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
}
