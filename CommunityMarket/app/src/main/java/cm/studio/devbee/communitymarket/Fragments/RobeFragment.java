package cm.studio.devbee.communitymarket.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.gridView_post.GridViewAdapter;
import cm.studio.devbee.communitymarket.gridView_post.ModelGridView;

/**
 * A simple {@link Fragment} subclass.
 */
public class RobeFragment extends Fragment {
    private  static View v;
    private static RecyclerView robeRecyclerView;
    private static ImageView imagePubrobe;
    private static TextView textPubrobe;
    private static FirebaseFirestore firebaseFirestore;
    private static ProgressDialog progressDialog;
    private static AsyncTask asyncTask;
    private static GridViewAdapter categoriesAdapterobe;
    private static List<ModelGridView> categoriesModelrobeList;
    private static WeakReference<RobeFragment> robeFragmentWeakReference;

    public RobeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate ( R.layout.fragment_robe, container, false );
        firebaseFirestore=FirebaseFirestore.getInstance ();
        imagePubrobe=v.findViewById ( R.id.pubImag_robe);
        textPubrobe=v.findViewById ( R.id.pubImageText_robe);
        //////////
        robeRecyclerView=v.findViewById ( R.id.robeRecyclerView );
        categoriesModelrobeList=new ArrayList<> (  );
        categoriesAdapterobe=new GridViewAdapter (categoriesModelrobeList,getActivity () );
        robeRecyclerView.setAdapter ( categoriesAdapterobe );
        robeRecyclerView.setLayoutManager(new GridLayoutManager (getActivity(),2));
        ////////pull
        asyncTask=new AsyncTask ();
        asyncTask.execute();
        robeFragmentWeakReference=new WeakReference<> ( this );
        return v;
    }
    public void RecyclerView(){

        Query firstQuery =firebaseFirestore.collection ( "publication" ).document ("categories").collection ( "robe" ).orderBy ( "dete-en-seconde",Query.Direction.DESCENDING );
        firstQuery.addSnapshotListener(new EventListener<QuerySnapshot> () {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for (DocumentChange doc:queryDocumentSnapshots.getDocumentChanges()){
                    if (doc.getType()==DocumentChange.Type.ADDED){
                        String idupost=doc.getDocument ().getId ();
                        ModelGridView categoriesModelpull =doc.getDocument().toObject(ModelGridView.class).withId ( idupost );
                        categoriesModelrobeList.add(categoriesModelpull);
                        categoriesAdapterobe.notifyDataSetChanged();
                    }
                }
            }
        });
    }
    public  void imagePub(){
        DocumentReference user = firebaseFirestore.collection("publicit").document("imageFixe");
        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot> () {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot doc =task.getResult();
                    StringBuilder imagePub=new StringBuilder("");
                    imagePub.append(doc.get("pub_robe"));
                    textPubrobe.setText(imagePub.toString());
                    String lien = textPubrobe.getText().toString();
                    Picasso.with(getActivity()).load(lien).into(imagePubrobe);
                }
            }
        }).addOnFailureListener(new OnFailureListener () {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),"erreur lors du chargement du slider",Toast.LENGTH_LONG).show();
            }
        });
    }
    public  class AsyncTask extends android.os.AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute ();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            RecyclerView ();
            imagePub ();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute ( aVoid );

        }
    }
    @Override
    public void onDestroy() {
        asyncTask.cancel(true);
        super.onDestroy();
        asyncTask.cancel(true);
        robeRecyclerView=null;
        imagePubrobe=null;
        textPubrobe=null;
        firebaseFirestore=null;
        progressDialog=null;
        categoriesAdapterobe=null;
        categoriesModelrobeList=null;
    }

}
