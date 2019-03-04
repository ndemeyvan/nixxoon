package cm.studio.devbee.communitymarket.gridView_post;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import cm.studio.devbee.communitymarket.R;

public class GridViewAdapter extends RecyclerView.Adapter<GridViewAdapter.ViewHolder> {
    List<ModelGridView> modelGridViewList;
    Context context;

    public GridViewAdapter(List<ModelGridView> modelGridViewList, Context context) {
        this.modelGridViewList = modelGridViewList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_post_layout,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
    String produit_image =modelGridViewList.get(i).getImage_du_produit();
    viewHolder.image_produit(produit_image);
    }

    @Override
    public int getItemCount() {
        return modelGridViewList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView produit;

       public ViewHolder(@NonNull View itemView) {
           super(itemView);
           produit=itemView.findViewById(R.id.post_image);
       }
       public void image_produit(String image){
           Picasso.with(context).load(image).into (produit );
       }
   }
}
