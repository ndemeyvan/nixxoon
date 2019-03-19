package cm.studio.devbee.communitymarket.search;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.utilsforsearch.SearchModel;

public class SearchActivity extends AppCompatActivity {
    private EditText search_edit_text;
    private Button search_buton;
    private RecyclerView search_recyclerview;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_search );
        search_edit_text=findViewById ( R.id.search_edit_text );
        search_buton=findViewById ( R.id.search_button );
        search_recyclerview=findViewById ( R.id.search_recyclerview );

        search_buton.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                firebaseSarch();
            }
        } );
    }
    public void firebaseSarch(){


    }

    public class categorieSearchHolder extends RecyclerView.ViewHolder{

        public categorieSearchHolder(@NonNull View itemView) {
            super ( itemView );
        }
    }
}
