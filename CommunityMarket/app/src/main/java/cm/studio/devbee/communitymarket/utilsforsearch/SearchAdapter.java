package cm.studio.devbee.communitymarket.utilsforsearch;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.annotation.Nullable;

import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.utilsForUserApp.UserModel;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.facebook.FacebookSdk.getApplicationContext;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.viewHolder> {
    List<UserModel> searchModelList;
    Context context;
    FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
     String nom_id;
    String give;

    public SearchAdapter(List<UserModel> searchModelList, Context context,String give) {
        this.searchModelList = searchModelList;
        this.context = context;
        this.give = give;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v =LayoutInflater.from ( viewGroup.getContext () ).inflate ( R.layout.item_user_layout,viewGroup,false );
        firebaseFirestore=FirebaseFirestore.getInstance ();
        viewGroup.getContext ();
        firebaseAuth=FirebaseAuth.getInstance();
        return new viewHolder ( v );
    }

    @Override
    public void onBindViewHolder(@NonNull final viewHolder viewHolder, int i) {
        String user_name=searchModelList.get ( i ).getUser_prenom ();
        String image=searchModelList.get ( i ).getUser_prenom ();
        firebaseFirestore.collection("mes donnees utilisateur").document (give).get().addOnCompleteListener((Activity) context,new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult ().exists ()){
                        String name_user= task.getResult ().getString ( "user_name" );
                        String image_user=task.getResult ().getString ( "user_profil_image" );
                        viewHolder.userNameAndProfile ( image_user,name_user );
                    }
                }else {
                    String error=task.getException().getMessage();
                    Toast.makeText(context,error,Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchModelList.size ();
    }

    public class  viewHolder extends RecyclerView.ViewHolder{
        TextView search_price;
        TextView search_user_name;
        CircleImageView search_user_profil_image;
        ImageView search_post_image;
        TextView search_description;
        TextView utilisateur_search;
        TextView search_title;
        public viewHolder(@NonNull View itemView) {
            super ( itemView );
            /*search_price=itemView.findViewById ( R.id.search_prix );
            utilisateur_search=itemView.findViewById ( R.id.utilisateur_search );
            search_user_name=itemView.findViewById ( R.id.search_usr_name );
            search_user_profil_image=itemView.findViewById ( R.id.search_profil_image );
            search_post_image=itemView.findViewById ( R.id.seacrh_image_post );
            search_description=itemView.findViewById ( R.id.search_description );
            search_title=itemView.findViewById ( R.id.search_title );*/
        }
        public void searchdata (String titre){
            search_title.setText ( titre );

        }
        public void stdesc(String desc){
            search_description.setText ( desc );
        }
        public void userNameAndProfile(String name,String profil_image){
            search_user_name.setText ( name );
            Picasso.with ( context ).load ( profil_image ).into ( search_user_profil_image );
        }
        public void image_produit(String imageDuproduit){
            Picasso.with ( context ).load( imageDuproduit ).into ( search_post_image );
        }

        public void setprix(String prix){
            search_price.setText ( prix );
        }
    }
}
