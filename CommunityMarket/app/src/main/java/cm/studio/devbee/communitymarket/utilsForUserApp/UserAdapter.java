package cm.studio.devbee.communitymarket.utilsForUserApp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
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
import com.squareup.picasso.Transformation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.postActivity.DetailActivity;
import cm.studio.devbee.communitymarket.postActivity.DetailActivityTwo;
import cm.studio.devbee.communitymarket.postActivity.UserGeneralPresentation;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    List<UserModel> userModelList;
    Context context;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private String current_user;
    private boolean ischat;


    public UserAdapter(List<UserModel> userModelList, Context context,boolean ischat) {
        this.userModelList = userModelList;
        this.context = context;
        this.ischat=ischat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v=LayoutInflater.from ( viewGroup.getContext () ).inflate (R.layout.item_user_layout,viewGroup,false);
        viewGroup.getContext();
        viewGroup.getContext();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        return new ViewHolder ( v );
    }

    @Override
    public void onBindViewHolder(@NonNull  final ViewHolder viewHolder, int i) {
       String nom_utilisateur=userModelList.get ( i ).getUser_prenom ();
       String image =userModelList.get ( i ).getUser_profil_image ();
       final String nom=userModelList.get ( i ).getId_utilisateur ();
       String status=userModelList.get ( i ).getStatus ();
       viewHolder.setNom ( nom );
       viewHolder.setNom ( nom_utilisateur );
       viewHolder.setimage ( image );
       viewHolder.profil_utilisateur.setOnClickListener ( new View.OnClickListener () {
           @Override
           public void onClick(View v) {
               current_user=firebaseAuth.getCurrentUser ().getUid ();
               Intent gotogneral=new Intent ( context,UserGeneralPresentation.class );
               gotogneral.putExtra ( "id de l'utilisateur",nom );
               context.startActivity ( gotogneral );

           }
       } );
       if (ischat==true){
           if (status.equals ( "online" )){
                viewHolder.online_image.setVisibility ( View.VISIBLE );
                viewHolder.offline_image.setVisibility ( View.INVISIBLE );
           }else {
               viewHolder.online_image.setVisibility ( View.INVISIBLE );
               viewHolder.offline_image.setVisibility ( View.VISIBLE );
           }
       }else{
           viewHolder.online_image.setVisibility ( View.INVISIBLE );
           viewHolder.offline_image.setVisibility ( View.INVISIBLE );
       }
    }

    @Override
    public int getItemCount() {
        return userModelList.size ();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView nom_utilisateur;
        ImageView profil_utilisateur;
        TextView textView;
        CircleImageView online_image;
        CircleImageView offline_image;
        TextView status;

        public ViewHolder(@NonNull View itemView) {
            super ( itemView );
            nom_utilisateur=itemView.findViewById ( R.id.user_text_name );
            profil_utilisateur=itemView.findViewById ( R.id.user_image );
            textView=itemView.findViewById ( R.id.id_utilisateur_user );
            online_image=itemView.findViewById ( R.id.online_image );
            offline_image=itemView.findViewById ( R.id.off_image );
            status=itemView.findViewById ( R.id.status );

        }
        public void setuser(String nom){
            textView.setText ( nom );
        }

        public void setNom(final String nom){
            nom_utilisateur.setText(nom);

        }
        public void setimage(final String image){
            Bitmap bitmap=BitmapFactory.decodeFile(image);

            Picasso.with ( context ).load ( image ).transform(new CircleTransform()).into (profil_utilisateur);

        }


    }


    public class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }


    }
}
