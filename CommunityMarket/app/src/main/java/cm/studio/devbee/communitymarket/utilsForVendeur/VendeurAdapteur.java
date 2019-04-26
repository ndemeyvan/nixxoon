package cm.studio.devbee.communitymarket.utilsForVendeur;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.gridView_post.ModelGridView;
import cm.studio.devbee.communitymarket.postActivity.DetailActivity;
import cm.studio.devbee.communitymarket.postActivity.DetailActivityThree;
import cm.studio.devbee.communitymarket.postActivity.DetailActivityTwo;
import cm.studio.devbee.communitymarket.vendeurContact.VendeurActivity;

public class VendeurAdapteur extends RecyclerView.Adapter<VendeurAdapteur.ViewHolder> {
    List<ModelGridView> modelGridViewList;
    Context context;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    public VendeurAdapteur(List<ModelGridView> modelGridViewList, Context context) {
        this.modelGridViewList = modelGridViewList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        View v=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_post_layout_vendeur,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        String produit_image =modelGridViewList.get(i).getImage_du_produit();
        String nom=modelGridViewList.get(i).getNom_du_produit();
        String prix_produit=modelGridViewList.get(i).getPrix_du_produit();
        final String nom_utilisateur=modelGridViewList.get(i).getUtilisateur();
        final String idDuPost=modelGridViewList.get ( i ).PostId;
        final String categorie=modelGridViewList.get(i).getCategories();
        viewHolder.setCatrogies_name(categorie);
        viewHolder.prix_produit(prix_produit);
        viewHolder.image_produit(produit_image);
        viewHolder.nom_produit(nom);
        viewHolder.setUser(nom_utilisateur);
        viewHolder.produit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoDetail =new Intent(context,DetailActivityThree.class);
                gotoDetail.putExtra("id du post",idDuPost);
                gotoDetail.putExtra("id de l'utilisateur",nom_utilisateur);
                gotoDetail.putExtra("id_categories",categorie);
                context.startActivity(gotoDetail);
            }
        });
        firebaseFirestore.collection("mes donnees utilisateur").document(nom_utilisateur).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult ().exists ()){
                        String image_user=task.getResult ().getString ( "user_profil_image" );
                        viewHolder.profil_post ( image_user );
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
        return modelGridViewList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView produit;
        ImageView post_image_profil;
        TextView post_titre_produit_description;
        TextView prix_post;
        TextView catrogies_name;
        TextView nom_user;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            produit=itemView.findViewById(R.id.post_image_vendeur );
           // post_titre_produit_description=itemView.findViewById(R.id.post_titre_produit_descriptionl_vendeur);
            prix_post=itemView.findViewById(R.id.prix_postl_vendeur );
            post_image_profil=itemView.findViewById ( R.id.profil_vendeur );
            catrogies_name=itemView.findViewById(R.id.catrogies_name_vendeur );
            //nom_user=itemView.findViewById(R.id.nom_userl_vendeur);
        }
        public void image_produit(String image){
            Picasso.with(context).load(image).into (produit );
        }
        public void nom_produit(String nom){
            post_titre_produit_description.setText(nom);
        }
        public void prix_produit(String prix){
            prix_post.setText(prix);
        }
        public void profil_post(String profil){
            Picasso.with(context).load(profil).transform(new CircleTransform()).into (post_image_profil );
        }
        public void setCatrogies_name(String cat){
            catrogies_name.setText(cat);
        }
        public void setUser(String user){
            nom_user.setText(user);
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
