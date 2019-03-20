package cm.studio.devbee.communitymarket.utilsforsearch;

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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.annotation.Nullable;

import cm.studio.devbee.communitymarket.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.viewHolder> {
    List<SearchModel> searchModelList;
    Context context;
    FirebaseFirestore firebaseFirestore;

    public SearchAdapter(List<SearchModel> searchModelList, Context context) {
        this.searchModelList = searchModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v =LayoutInflater.from ( viewGroup.getContext () ).inflate ( R.layout.item_seacrh_layout,viewGroup,false );
        firebaseFirestore=FirebaseFirestore.getInstance ();
        viewGroup.getContext ();
        return new viewHolder ( v );
    }

    @Override
    public void onBindViewHolder(@NonNull final viewHolder viewHolder, int i) {
        String imagePorduit=searchModelList.get ( i ).getImage_du_produit ();
        String description=searchModelList.get ( i ).getDecription_du_produit ();
        final String prix=searchModelList.get ( i ).getPrix_du_produit ();
        viewHolder.setprix ( prix );


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
        public viewHolder(@NonNull View itemView) {
            super ( itemView );
            search_price=itemView.findViewById ( R.id.search_prix );
            utilisateur_search=itemView.findViewById ( R.id.utilisateur_search );
            search_user_name=itemView.findViewById ( R.id.search_usr_name );
            search_user_profil_image=itemView.findViewById ( R.id.search_profil_image );
            search_post_image=itemView.findViewById ( R.id.seacrh_image_post );
            search_description=itemView.findViewById ( R.id.search_description );
        }
        public void seardata(String prix,String user_name,String title,String imageDupost,String desc,String profil_image){
            //search_title.setText ( title );
            search_price.setText ( prix );
            search_description.setText ( desc );
            search_user_name.setText ( user_name );
            Picasso.with ( context ).load ( imageDupost ).into ( search_post_image );
            Picasso.with ( context ).load ( profil_image ).into ( search_user_profil_image );


        }
        public void setprix(String prix){

        }
    }
}
