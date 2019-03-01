package cm.studio.devbee.communitymarket.postActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.utilForPost.CategoriesAdaptePost;
import cm.studio.devbee.communitymarket.utilForPost.CategoriesModelPost;
import cm.studio.devbee.communitymarket.utilsForCategories.CategoriesModel;

public class PostActivity extends AppCompatActivity {
    private android.support.v7.widget.Toolbar toolbar_post;
    private RecyclerView post_cat_recycler;
    private CategoriesAdaptePost categoriesAdaptePost;
    private List<CategoriesModelPost> categoriesModelPostList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_post );
        toolbar_post=findViewById ( R.id.post_cat_toolbar );
        setSupportActionBar ( toolbar_post );
        getSupportActionBar ().setTitle ( "choisissez une categorie de vente" );
        post_cat_recycler=findViewById ( R.id.post_cat_recycler );
        categoriesModelPostList=new ArrayList<> (  );
        categoriesModelPostList.add ( new CategoriesModelPost ( "Marques",R.drawable.logo ) );
        categoriesModelPostList.add ( new CategoriesModelPost ( "Chaussures",R.drawable.chaussure ) );
        categoriesModelPostList.add ( new CategoriesModelPost ( "jupes",R.drawable.jupes ) );
        categoriesModelPostList.add ( new CategoriesModelPost ( "accesoires",R.drawable.accessoires ) );
        categoriesModelPostList.add ( new CategoriesModelPost ( "Cullotes",R.drawable.cullotes ) );
        categoriesModelPostList.add ( new CategoriesModelPost ( "Pantalons",R.drawable.pantalons ) );
        categoriesModelPostList.add ( new CategoriesModelPost ( "T-shirts",R.drawable.t_shirt ) );
        categoriesModelPostList.add ( new CategoriesModelPost ( "Chemises",R.drawable.chemise ) );
        categoriesAdaptePost=new CategoriesAdaptePost (categoriesModelPostList,PostActivity.this );
        post_cat_recycler.setAdapter ( categoriesAdaptePost );
        post_cat_recycler.setLayoutManager ( new LinearLayoutManager ( this,LinearLayoutManager.VERTICAL ,false) );
    }
}
