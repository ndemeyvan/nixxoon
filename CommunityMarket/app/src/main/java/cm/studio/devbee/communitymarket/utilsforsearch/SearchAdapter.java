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
        firebaseFirestore.collection ( "publication" ).document ("categories").get ().addOnCompleteListener ( new OnCompleteListener<DocumentSnapshot> () {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful ()){
                    String pri_two=task.getResult ().getString ( "prix_du_produit" );
                    viewHolder.setprix ( prix );

                }else{
                    String error =task.getException ().getMessage ();
                    Toast.makeText (context,error,Toast.LENGTH_LONG ).show ();
                }
            }
        } );

    }

    @Override
    public int getItemCount() {
        return searchModelList.size ();
    }

    public class  viewHolder extends RecyclerView.ViewHolder{
        TextView search_prix;
        TextView search_usr_name;
        TextView search_title;
        ImageView search_image_post;
        TextView search_description;
        CircleImageView search_profil_image;
        public viewHolder(@NonNull View itemView) {
            super ( itemView );
            search_title=itemView.findViewById ( R.id.search_title );
            search_prix=itemView.findViewById ( R.id.search_prix );
            search_profil_image=itemView.findViewById (R.id.search_profil_image);
            search_description=itemView.findViewById ( R.id.search_description );
            search_usr_name =itemView.findViewById ( R.id.search_title );
            search_image_post=itemView.findViewById ( R.id.search_image_post );
            search_usr_name=itemView.findViewById ( R.id.search_usr_name );
        }
        public void seardata(String prix,String user_name,String title,String imageDupost,String desc,String profil_image){
            search_title.setText ( title );
            search_prix.setText ( prix );
            search_description.setText ( desc );
            search_usr_name.setText ( user_name );
            Picasso.with ( context ).load ( imageDupost ).into ( search_image_post );
            Picasso.with ( context ).load ( profil_image ).into ( search_profil_image );


        }
        public void setprix(String prix){

        }
    }
}
