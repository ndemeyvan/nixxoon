package cm.studio.devbee.communitymarket.postActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.utilForPost.CategoriesAdaptePost;
import cm.studio.devbee.communitymarket.utilForPost.CategoriesModelPost;


public class PostActivity extends AppCompatActivity {
    private static android.support.v7.widget.Toolbar toolbar_post;
    private static RecyclerView post_cat_recycler;
    private static CategoriesAdaptePost categoriesAdaptePost;
    private static List<CategoriesModelPost> categoriesModelPostList;
    private static WeakReference<PostActivity> postActivityWeakReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_post );
        toolbar_post=findViewById ( R.id.post_cat_toolbar );
        setSupportActionBar ( toolbar_post );
        getSupportActionBar ().setTitle ( "choisissez une categorie de vente" );
        post_cat_recycler=findViewById ( R.id.post_cat_recycler );
        categoriesModelPostList=new ArrayList<> (  );
        categoriesModelPostList.add ( new CategoriesModelPost ( "Chaussures",R.drawable.chaussure ) );
        categoriesModelPostList.add ( new CategoriesModelPost ( "jupes",R.drawable.jupes ) );
        categoriesModelPostList.add ( new CategoriesModelPost ( "accessoires",R.drawable.accessoires ) );
        categoriesModelPostList.add ( new CategoriesModelPost ( "Cullotes",R.drawable.cullotes ) );
        categoriesModelPostList.add ( new CategoriesModelPost ( "Pantalons",R.drawable.pantalons ) );
        categoriesModelPostList.add ( new CategoriesModelPost ( "T-shirts",R.drawable.t_shirt ) );
        categoriesModelPostList.add ( new CategoriesModelPost ( "Chemises",R.drawable.chemise ) );
        categoriesModelPostList.add ( new CategoriesModelPost ( "robe",R.drawable.robe ) );
        categoriesModelPostList.add ( new CategoriesModelPost ( "pull",R.drawable.robe ) );
        postActivityWeakReference=new WeakReference<>(this);
        categoriesAdaptePost=new CategoriesAdaptePost (categoriesModelPostList,getApplicationContext() );
        post_cat_recycler.setAdapter ( categoriesAdaptePost );
        post_cat_recycler.setLayoutManager ( new LinearLayoutManager ( getApplicationContext(),LinearLayoutManager.VERTICAL ,false) );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        toolbar_post=null;
        post_cat_recycler=null;
        categoriesAdaptePost=null;
        categoriesModelPostList=null;
    }
}
