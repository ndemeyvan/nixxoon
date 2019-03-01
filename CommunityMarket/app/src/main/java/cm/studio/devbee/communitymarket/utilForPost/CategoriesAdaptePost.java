package cm.studio.devbee.communitymarket.utilForPost;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.postActivity.PostActivityFinal;

public class CategoriesAdaptePost extends RecyclerView.Adapter<CategoriesAdaptePost.ViewHolder> {
    List<CategoriesModelPost> categoriesModelList;
    Context context;

    public CategoriesAdaptePost(List<CategoriesModelPost> categoriesModelList, Context context) {
        this.categoriesModelList = categoriesModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v=LayoutInflater.from ( viewGroup.getContext () ).inflate (R.layout.item_post_categorie ,viewGroup,false);
        return new ViewHolder ( v );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        //viewHolder.categories_text.setText ( categoriesModelList.get ( i ).getPost_titre_categories () );
        String desc =categoriesModelList.get ( i).getPost_titre_categories ();
        viewHolder.image_categories.setImageResource ( categoriesModelList.get ( i ).getPost_image_categories () );
        viewHolder.setNom ( desc );
    }

    @Override
    public int getItemCount() {
        return categoriesModelList.size ();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image_categories;
        TextView categories_text;
        public ViewHolder(@NonNull View itemView) {
            super ( itemView );
            image_categories=itemView.findViewById ( R.id.post_cat_image );
            categories_text=itemView.findViewById ( R.id.post_cat_name );
        }
        public void setNom(final String name){
            categories_text.setText ( name );
            itemView.setOnClickListener ( new View.OnClickListener () {
                @Override
                public void onClick(View v) { Intent categoryIntent=new Intent ( itemView.getContext (),PostActivityFinal.class );
                   categoryIntent.putExtra ( "categoryName",name );
                   itemView.getContext ().startActivity ( categoryIntent );

                }
            } );

        }

    }
}
