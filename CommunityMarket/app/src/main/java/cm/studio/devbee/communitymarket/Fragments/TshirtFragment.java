package cm.studio.devbee.communitymarket.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.gridView_post.ModelGridView;
import cm.studio.devbee.communitymarket.utilForT_shirt.CategoriesAdapteTshirt;
import cm.studio.devbee.communitymarket.utilForT_shirt.CategoriesModelTshirt;
import cm.studio.devbee.communitymarket.vendeurContact.VendeurActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class TshirtFragment extends Fragment {
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView tshirtRecyclerView;
    private View v;
    private CategoriesAdapteTshirt categoriesAdapteTshirt;
    List<CategoriesModelTshirt> categoriesModelTshirtList;

    public TshirtFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         v=inflater.inflate(R.layout.fragment_tshirt, container, false);
        tshirtRecyclerView=v.findViewById ( R.id.tshirtRecyclerView );
        categoriesModelTshirtList=new ArrayList<>(  );
        categoriesAdapteTshirt=new CategoriesAdapteTshirt (categoriesModelTshirtList,getActivity());
        tshirtRecyclerView.setAdapter ( categoriesAdapteTshirt );
        tshirtRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));

         tshirtRecyclerView();
         return v;
    }
    public void tshirtRecyclerView(){
        Query firstQuery =firebaseFirestore.collection ( "publication" ).document ("categories").collection ( "T-shirts" ).orderBy ( "date_de_publication",Query.Direction.DESCENDING );
        firstQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for (DocumentChange doc:queryDocumentSnapshots.getDocumentChanges()){
                    if (doc.getType()==DocumentChange.Type.ADDED){
                        String idupost=doc.getDocument ().getId ();
                        CategoriesModelTshirt categoriesModelTshirt =doc.getDocument().toObject(CategoriesModelTshirt.class).withId ( idupost );
                        categoriesModelTshirtList.add(categoriesModelTshirt);
                        categoriesAdapteTshirt.notifyDataSetChanged();
                    }
                }

            }
        });
    }

}
