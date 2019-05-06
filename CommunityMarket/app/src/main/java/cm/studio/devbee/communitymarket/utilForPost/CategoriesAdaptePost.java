package cm.studio.devbee.communitymarket.utilForPost;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Transformation;

import java.util.List;

import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.postActivity.PostActivityFinal;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

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
        viewGroup.getContext();
        return new ViewHolder ( v );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        //viewHolder.categories_text.setText ( categoriesModelList.get ( i ).getPost_titre_categories () );
        final String desc =categoriesModelList.get ( i).getPost_titre_categories ();
        viewHolder.choix_des_categories_container.setAnimation ( AnimationUtils.loadAnimation ( context,R.anim.fade_scale_animation ) );
        viewHolder.image_categories.setImageResource ( categoriesModelList.get ( i ).getPost_image_categories () );
        viewHolder.setNom ( desc );
        viewHolder.choix_des_categories_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent categoryIntent=new Intent ( context,PostActivityFinal.class ).setFlags(FLAG_ACTIVITY_NEW_TASK);
                categoryIntent.putExtra ( "categoryName",desc );
                context.startActivity(categoryIntent);
                //((Activity)context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoriesModelList.size ();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image_categories;
        TextView categories_text;
        ConstraintLayout choix_des_categories_container;
        public ViewHolder(@NonNull View itemView) {
            super ( itemView );
            image_categories=itemView.findViewById ( R.id.post_cat_image );
            categories_text=itemView.findViewById ( R.id.post_cat_name );
            choix_des_categories_container=itemView.findViewById ( R.id.choix_des_categories_container );
        }
        public void setNom(final String name){
            categories_text.setText ( name );

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
