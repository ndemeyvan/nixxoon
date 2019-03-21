package cm.studio.devbee.communitymarket.search;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.utilsforsearch.SearchAdapter;
import cm.studio.devbee.communitymarket.utilsforsearch.SearchModel;
import de.hdodenhof.circleimageview.CircleImageView;

public class SearchActivity extends AppCompatActivity {
    private EditText search_edit_text;
    private Button search_buton;
    private RecyclerView search_recyclerview;
    private Context context;
    private FirebaseFirestore db;
    LinearLayoutManager linearLayoutManager;
    private List<SearchModel> listUsers;
    SearchAdapter searchAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_search );
        search_edit_text=findViewById ( R.id.search_edit_text );
        search_buton=findViewById ( R.id.search_button );
        final String find=search_edit_text.getText ().toString ();
        search_recyclerview=findViewById ( R.id.search_recyclerview );
        listUsers = new ArrayList<>();
        searchAdapter=new SearchAdapter (listUsers,getApplicationContext ());
        search_recyclerview.setAdapter (  searchAdapter);
        search_recyclerview.setLayoutManager ( new LinearLayoutManager ( getApplicationContext (),LinearLayoutManager.VERTICAL,false ) );
        db = FirebaseFirestore.getInstance();
        search_buton.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                searchUsers (find);

            }
        } );


    }

    public void searchUsers(String s){
        db.collection ( "publication" ).document ("categories").collection ( "nouveaux" ).whereEqualTo("categories",s.toLowerCase())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                for (DocumentSnapshot doc:task.getResult()){
                    listUsers.clear();
                    SearchModel searchModel = new SearchModel(doc.getString("decription_du_produit"),doc.getString("categories"),doc.getString("date_de_publication"),doc.getString("nom_du_produit"));
                    listUsers.add(searchModel);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }


}
