package cm.studio.devbee.communitymarket.utilsForUserApp;

import android.content.Context;
import android.content.Intent;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.postActivity.DetailActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    List<UserModel> userModelList;
    Context context;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;


    public UserAdapter(List<UserModel> userModelList, Context context) {
        this.userModelList = userModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v=LayoutInflater.from ( viewGroup.getContext () ).inflate (R.layout.item_user_layout,viewGroup,false);
        viewGroup.getContext();
        return new ViewHolder ( v );
    }

    @Override
    public void onBindViewHolder(@NonNull  final ViewHolder viewHolder, int i) {
       String nom_utilisateur=userModelList.get ( i ).getUser_prenom ();
       String image =userModelList.get ( i ).getUser_profil_image ();
       viewHolder.setNom ( nom_utilisateur );
       viewHolder.setimage ( image );
    }

    @Override
    public int getItemCount() {
        return userModelList.size ();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView nom_utilisateur;
        CircleImageView profil_utilisateur;

        public ViewHolder(@NonNull View itemView) {
            super ( itemView );
            nom_utilisateur=itemView.findViewById ( R.id.user_text_name );
            profil_utilisateur=itemView.findViewById ( R.id.user_image );
        }

        public void setNom(final String nom){
            nom_utilisateur.setText(nom);

        }
        public void setimage(final String image){
            Picasso.with ( context ).load ( image ).into (profil_utilisateur);

        }


    }
}
