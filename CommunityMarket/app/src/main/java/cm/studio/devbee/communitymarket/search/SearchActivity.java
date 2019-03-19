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

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
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
    private FirestoreRecyclerAdapter adapter;
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
               // getFriendList();
            }
        } );


    }
    private void searchUsers(String recherche) {
        if(recherche.length() > 0)
            recherche = recherche.substring(0,1).toUpperCase() + recherche.substring(1).toLowerCase();
        db.collection ( "publication" ).document ("categories").collection ( "nouveaux" ).whereGreaterThanOrEqualTo("name", recherche)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            System.err.println("Listen failed:" + e);
                            return;
                        }
                        for (DocumentSnapshot doc : snapshots) {
                            SearchModel user = doc.toObject(SearchModel.class);
                            listUsers.add(user);
                        }
                        searchAdapter.notifyDataSetChanged();
                    }
                });
    }

    public void getFriendList(){
        Query query = db.collection ( "publication" ).document ("categories").collection ( "nouveaux" );
        FirestoreRecyclerOptions < SearchModel > response =  new  FirestoreRecyclerOptions.Builder<SearchModel> ().setQuery ( query,SearchModel.class ).build ();
         adapter =  new  FirestoreRecyclerAdapter < SearchModel , categorieSearchHolder > (response) {
            @NonNull
            @Override
            public categorieSearchHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext ()).inflate (R.layout.item_seacrh_layout,viewGroup,false);
                return new categorieSearchHolder ( view );
            }

            @Override
            protected void onBindViewHolder(@NonNull categorieSearchHolder holder, int position, @NonNull SearchModel model) {
               holder.search_description.setText ( model.getDecription_du_produit () );
               holder.search_price.setText ( model.getPrix_du_produit () );
               holder.utilisateur_search.setText ( model.getUtilisateur ());
               Picasso.with ( getApplicationContext () ).load ( model.getImage_du_produit () ).into ( holder.search_user_profil_image );


                }
        };

        search_recyclerview.setAdapter ( adapter );


    }

    public class categorieSearchHolder extends RecyclerView.ViewHolder{
        TextView search_price;
        TextView search_user_name;
        CircleImageView search_user_profil_image;
        ImageView search_post_image;
        TextView search_description;
        TextView utilisateur_search;
        public categorieSearchHolder(@NonNull View itemView) {
            super ( itemView );
            search_price=itemView.findViewById ( R.id.search_prix );
            utilisateur_search=itemView.findViewById ( R.id.utilisateur_search );
            search_user_name=itemView.findViewById ( R.id.search_usr_name );
            search_user_profil_image=itemView.findViewById ( R.id.search_profil_image );
            search_post_image=itemView.findViewById ( R.id.seacrh_image_post );
            search_description=itemView.findViewById ( R.id.search_description );
        }
        public void search_dat(Context context, String prix,String search_post,String description){
            search_price.setText ( prix );
            Picasso.with ( context ).load (search_post  ).into (search_post_image  );
            search_description.setText ( description );
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        //adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
