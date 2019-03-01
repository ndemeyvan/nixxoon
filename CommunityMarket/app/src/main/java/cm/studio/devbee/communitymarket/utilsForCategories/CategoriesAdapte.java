package cm.studio.devbee.communitymarket.utilsForCategories;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cm.studio.devbee.communitymarket.R;

public class CategoriesAdapte extends RecyclerView.Adapter<CategoriesAdapte.ViewHolder> {
    List<CategoriesModel> categoriesModelList;
    Context context;

    public CategoriesAdapte(List<CategoriesModel> categoriesModelList, Context context) {
        this.categoriesModelList = categoriesModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v=LayoutInflater.from ( viewGroup.getContext () ).inflate (R.layout.item_categories ,viewGroup,false);
        return new ViewHolder ( v );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.categories_text.setText ( categoriesModelList.get ( i ).getTitre_categories () );
        viewHolder.image_categories.setImageResource ( categoriesModelList.get ( i ).getImage_categories () );
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
            image_categories=itemView.findViewById ( R.id.item_categories_image );
            categories_text=itemView.findViewById ( R.id.item_text_categories );
        }

    }
}
