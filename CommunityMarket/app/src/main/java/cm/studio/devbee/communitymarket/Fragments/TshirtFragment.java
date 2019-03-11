package cm.studio.devbee.communitymarket.Fragments;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.gridView_post.GridViewAdapter;
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
    private GridViewAdapter categoriesAdapteTshirt;
    List<ModelGridView> categoriesModelTshirtList;
    private ImageView imagePubTshirt;
    private TextView textPubTshirt;
    ProgressDialog progressDialog;

    public TshirtFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_tshirt, container, false);
        tshirtRecyclerView=v.findViewById ( R.id.tshirtRecyclerView );
        firebaseFirestore=FirebaseFirestore.getInstance();
        categoriesModelTshirtList=new ArrayList<>(  );
        categoriesAdapteTshirt=new GridViewAdapter (categoriesModelTshirtList,getActivity());
        tshirtRecyclerView.setAdapter ( categoriesAdapteTshirt );
        tshirtRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        imagePubTshirt=v.findViewById ( R.id.pubImag_tshire );
        textPubTshirt=v.findViewById ( R.id.pubImageText_tshirt );
        AsyncTask asyncTask=new AsyncTask ();
        asyncTask.execute (  );
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
                        ModelGridView categoriesModelTshirt =doc.getDocument().toObject(ModelGridView.class).withId ( idupost );
                        categoriesModelTshirtList.add(categoriesModelTshirt);
                        categoriesAdapteTshirt.notifyDataSetChanged();
                    }
                }

            }
        });
    }
    public void imagePub_t_shirt(){
        DocumentReference user = firebaseFirestore.collection("publicit").document("imageFixe");
        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot> () {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()){
                    DocumentSnapshot doc =task.getResult();
                    StringBuilder imagePub=new StringBuilder("");
                    imagePub.append(doc.get("pub_tshirt"));
                    textPubTshirt.setText(imagePub.toString());
                    String lien = textPubTshirt.getText().toString();
                    Picasso.with(getActivity()).load(lien).into(imagePubTshirt);

                }
            }
        }).addOnFailureListener(new OnFailureListener () {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),"erreur lors du chargement du slider",Toast.LENGTH_LONG).show();
            }
        });
    }
    public class AsyncTask extends android.os.AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog (getActivity ());
            progressDialog.setTitle("chargement"); // Setting Title
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
            progressDialog.show(); // Display Progress Dialog
            progressDialog.setCancelable(false);
            super.onPreExecute ();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            tshirtRecyclerView();
            imagePub_t_shirt ();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute ( aVoid );
            progressDialog.dismiss();
        }
    }




}
