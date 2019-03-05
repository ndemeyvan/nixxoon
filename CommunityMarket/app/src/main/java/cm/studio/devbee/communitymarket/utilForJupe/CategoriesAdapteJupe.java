package cm.studio.devbee.communitymarket.utilForJupe;

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

public class CategoriesAdapteJupe extends RecyclerView.Adapter<CategoriesAdapteJupe.ViewHolder> {
    List<CategoriesModelJupe> CategoriesModelJupeList;
    Context context;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;


    public CategoriesAdapteJupe(List<CategoriesModelJupe> CategoriesModelJupeList, Context context) {
        this.CategoriesModelJupeList = CategoriesModelJupeList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v=LayoutInflater.from ( viewGroup.getContext () ).inflate (R.layout.item_nouveautes ,viewGroup,false);
        viewGroup.getContext();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        return new ViewHolder ( v );
    }

    @Override
    public void onBindViewHolder(@NonNull  final ViewHolder viewHolder, int i) {
        viewHolder.setIsRecyclable ( false );
        String desc =CategoriesModelJupeList.get ( i).getDecription_du_produit();
        String nvxPrix=CategoriesModelJupeList.get(i).getPrix_du_produit();
        String imageproduit=CategoriesModelJupeList.get ( i ).getImage_du_produit ();
        final String nom_id=CategoriesModelJupeList.get ( i ).getUtilisateur ();
        String tempsdepub=CategoriesModelJupeList.get ( i ).getDate_de_publication ();
        String produinom=CategoriesModelJupeList.get ( i ).getNom_du_produit ();
        final String postId=CategoriesModelJupeList.get ( i ).PostId;
        final String current_user=firebaseAuth.getCurrentUser ().getUid ();
        final String idDuPost=CategoriesModelJupeList.get ( i ).PostId;
        viewHolder.imageproduitxi ( imageproduit );
        viewHolder.setNom ( desc );
        viewHolder.setPrix(nvxPrix);
        viewHolder.temps ( tempsdepub );
        viewHolder.nomproduit ( produinom );
        viewHolder.imageDuproduit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent gotoDetail =new Intent(context,DetailActivity.class);
                    gotoDetail.putExtra("id du post",idDuPost);
                    gotoDetail.putExtra("id de l'utilisateur",nom_id);
                    context.startActivity(gotoDetail);

                }
            });
        firebaseFirestore.collection("mes donnees utilisateur").document(nom_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult ().exists ()){

                        String name_user= task.getResult ().getString ( "user_name" );
                        String image_user=task.getResult ().getString ( "user_profil_image" );
                        viewHolder.setuserData ( name_user,image_user );
                    }
                }else {
                    String error=task.getException().getMessage();
                    Toast.makeText(context,error,Toast.LENGTH_LONG).show();
                }
            }
        });
        firebaseFirestore.collection ( "publication" ).document ("categories").collection ( "nouveaux" ).document (idDuPost).collection ( "likes" ).addSnapshotListener ( new EventListener<QuerySnapshot> () {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (!queryDocumentSnapshots.isEmpty ()){
                    int i=queryDocumentSnapshots.size ();
                    viewHolder.likexa ( i );

                }else{
                        viewHolder.likexa ( 0 );
                }
            }
        } );

        firebaseFirestore.collection ( "publication" ).document ("categories").collection ( "nouveaux" ).document (idDuPost).collection ( "likes" ).document (current_user).addSnapshotListener ( new EventListener<DocumentSnapshot> () {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot.exists ()){
                    viewHolder.like.setImageDrawable ( context.getDrawable ( R.mipmap.ic_like_accent ));
                }else {
                    viewHolder.like.setImageDrawable ( context.getDrawable ( R.drawable.ic_like ));

                }
            }
        } );

        viewHolder.like.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {

                firebaseFirestore.collection ( "publication" ).document ("categories").collection ( "nouveaux" ).document (idDuPost).collection ( "likes" ).document (current_user).get ().addOnCompleteListener ( new OnCompleteListener<DocumentSnapshot> () {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (!task.getResult ().exists ()){
                            Map<String,String>likesMaps=new HashMap<> (  );
                            likesMaps.put ("lol" ,"lol" );
                            firebaseFirestore.collection ( "publication" ).document ("categories").collection ( "nouveaux" ).document (idDuPost).collection ( "likes" ).document (current_user).set ( likesMaps );

                        }else {
                            firebaseFirestore.collection ( "publication" ).document ("categories").collection ( "nouveaux" ).document (idDuPost).collection ( "likes" ).document (current_user).delete ();

                        }
                    }
                } );


            }
        } );
    }

    @Override
    public int getItemCount() {
        return CategoriesModelJupeList.size ();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView description;
        TextView prix;
        TextView liker;
        ImageView imageDuproduit;
        TextView nom_utilisateur;
        CircleImageView profil_utilisateur;
        TextView temps_de_la_pub;
        CircleImageView image_profil;
        TextView nouveaux_tire;
        ImageView like;
        TextView likeCount;
        public ViewHolder(@NonNull View itemView) {
            super ( itemView );
            description=itemView.findViewById ( R.id.nouveaux_description_du_produit );
            prix=itemView.findViewById(R.id.nouveaux_prix);
            nouveaux_tire=itemView.findViewById ( R.id.nouveaux_tire );
            image_profil=itemView.findViewById ( R.id.nouveaux_image_profile );
            liker=itemView.findViewById(R.id.nouveaux_nombre_de_like);
            imageDuproduit=itemView.findViewById(R.id.nouveaute_image);
            nom_utilisateur=itemView.findViewById ( R.id.nouveaux_user_name );
            temps_de_la_pub=itemView.findViewById ( R.id.nouveaux_temps );
            like =itemView.findViewById ( R.id.like_image );
            profil_utilisateur=itemView.findViewById ( R.id.nouveaux_image_profile );
            likeCount=itemView.findViewById ( R.id.nouveaux_nombre_de_like );
        }
        public void setNom(final String desc){
            description.setText(desc);
            /*itemView.setOnClickListener ( new View.OnClickListener () {
                @Override
                public void onClick(View v) { Intent categoryIntent=new Intent ( itemView.getContext (),PostActivityFinal.class );
                   categoryIntent.putExtra ( "categoryName",name );
                   itemView.getContext ().startActivity ( categoryIntent );
                }
            } );*/
        }
        public void setPrix(String nouveauxPrix){
            prix.setText(nouveauxPrix);
        }
        public void temps(String postTemps){
           temps_de_la_pub.setText(postTemps);
        }
        public void likexa(int lelike){
            likeCount.setText(lelike+"");
        }
        public void imageproduitxi(String image){
            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent gotoDetail =new Intent(itemView.getContext(),DetailActivity.class);
                    itemView.getContext().startActivity(gotoDetail);
                }
            });*/
            Picasso.with(context).load(image).into (imageDuproduit );
        }
        public void setuserData(String name,String image){
            nom_utilisateur.setText ( name );
            Picasso.with(context).load(image).into (profil_utilisateur );
        }
        public void nomproduit(String produitnom){
            nouveaux_tire.setText ( produitnom );
        }

    }
}
