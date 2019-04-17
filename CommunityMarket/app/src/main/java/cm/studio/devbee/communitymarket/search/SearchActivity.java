package cm.studio.devbee.communitymarket.search;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.ToolbarWidgetWrapper;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentChange;
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

import cm.studio.devbee.communitymarket.Accueil;
import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.postActivity.DetailActivity;
import cm.studio.devbee.communitymarket.utilsForNouveautes.CategoriesModelNouveaux;
import cm.studio.devbee.communitymarket.utilsForPostPrincipal.PrincipalModel;
import cm.studio.devbee.communitymarket.utilsForUserApp.UserAdapter;
import cm.studio.devbee.communitymarket.utilsForUserApp.UserModel;
import cm.studio.devbee.communitymarket.utilsforsearch.SearchAdapter;
import cm.studio.devbee.communitymarket.utilsforsearch.SearchModel;
import de.hdodenhof.circleimageview.CircleImageView;

public class SearchActivity extends AppCompatActivity {
    private EditText search_edit_text;
    private Button search_buton;
    private RecyclerView search_recyclerview;
    private Context context;
    private FirebaseFirestore db;
    private static FirebaseAuth firebaseAuth;
    private static String current_user;
    Toolbar toolbarSearch;
    private LinearLayoutManager linearLayoutManager;
    private List<UserModel> listUsers;
    private UserAdapter searchAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_search );
        search_edit_text=findViewById ( R.id.search_edit_text );
        search_buton=findViewById ( R.id.search_button );
        final String find=search_edit_text.getText ().toString ();

        search_recyclerview=findViewById ( R.id.search_recyclerview );
        listUsers = new ArrayList<>();
        toolbarSearch=findViewById(R.id.toolbarSearch);
        setSupportActionBar(toolbarSearch);
        search_recyclerview.setAdapter (  searchAdapter);
        search_recyclerview.setLayoutManager ( new LinearLayoutManager ( getApplicationContext (),LinearLayoutManager.VERTICAL,false ) );
        db = FirebaseFirestore.getInstance();
        /*search_buton.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                search(find);

            }
        } );*/
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        firebaseAuth=FirebaseAuth.getInstance ();
        current_user=firebaseAuth.getCurrentUser ().getUid ();
        toolbarSearch.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity ( new Intent( getApplicationContext (),Accueil.class ).setFlags ( Intent.FLAG_ACTIVITY_CLEAR_TOP ) );
                finish ();
            }
        });
        search_edit_text.addTextChangedListener ( new TextWatcher () {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search(s.toString().toLowerCase ());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        } );


    }

    private void search(final String s) {
        Query firstQuery =db.collection ( "mes donnees utilisateur" ).orderBy ( "search").startAt ( s ).endAt ( s+"\uf0ff" );
        firstQuery.addSnapshotListener(SearchActivity.this,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                //if (search_edit_text.getText ().equals ( " " )) {
                    for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges ()) {
                        if (doc.getType () == DocumentChange.Type.ADDED) {
                            listUsers.clear ();
                            UserModel searchModel = doc.getDocument ().toObject ( UserModel.class );
                            assert searchModel!=null;
                            assert current_user!=null;
                            if (!current_user.equals ( searchModel.getId_utilisateur () )) {
                                listUsers.add ( searchModel );
                            }
                        }
                        searchAdapter = new UserAdapter ( listUsers,SearchActivity.this ,s);
                        search_recyclerview.setAdapter ( searchAdapter );
                    }
                   /*if (search_edit_text.getText ().equals ( " " )) {
                        for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges ()) {
                            if (doc.getType () == DocumentChange.Type.ADDED) {
                                listUsers.clear ();
                                UserModel searchModel = doc.getDocument ().toObject ( UserModel.class );
                                if (!current_user.equals ( searchModel.getId_utilisateur () )) {
                                    listUsers.add ( searchModel );
                                }
                            }
                            searchAdapter = new UserAdapter ( listUsers, SearchActivity.this ,s);
                            search_recyclerview.setAdapter ( searchAdapter );
                        }
                    }*/

              //  }
            }
        });
    }

}
