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
public class ChemiseFragment extends Fragment {
    private  static View v;
    private static RecyclerView chemiseRecyclerView;
    private static ImageView imagePubcullote;
    private static TextView textPubcullote;
    private static FirebaseFirestore firebaseFirestore;
    private static ProgressDialog progressDialog;
    private static AsyncTask asyncTask;
    private static GridViewAdapter categoriesAdaptechemise;
    private static List<ModelGridView> categoriesModelchemiseList;
    private static WeakReference<ChemiseFragment>chemiseFragmentWeakReference;

    public ChemiseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate ( R.layout.fragment_chemise, container, false );
        firebaseFirestore=FirebaseFirestore.getInstance ();
        ///////
        imagePubcullote=v.findViewById ( R.id.pubImag_chemise);
        textPubcullote=v.findViewById ( R.id.pubImageText_chemise);
        //////////
        chemiseRecyclerView=v.findViewById ( R.id.chemiseRecyclerView );
        categoriesModelchemiseList=new ArrayList<> (  );
        categoriesAdaptechemise=new GridViewAdapter (categoriesModelchemiseList,getActivity () );
        chemiseRecyclerView.setAdapter ( categoriesAdaptechemise );
        chemiseRecyclerView.setLayoutManager(new GridLayoutManager (getActivity(),2));
        ////////pull
        asyncTask=new AsyncTask ();
        asyncTask.execute();
        chemiseFragmentWeakReference=new WeakReference<> ( this );
        return v;
    }
    public void RecyclerView(){

        Query firstQuery =firebaseFirestore.collection ( "publication" ).document ("categories").collection ( "Chemises" ).orderBy ( "dete-en-seconde",Query.Direction.DESCENDING );
        firstQuery.addSnapshotListener(new EventListener<QuerySnapshot> () {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for (DocumentChange doc:queryDocumentSnapshots.getDocumentChanges()){
                    if (doc.getType()==DocumentChange.Type.ADDED){
                        String idupost=doc.getDocument ().getId ();
                        ModelGridView categoriesModelpull =doc.getDocument().toObject(ModelGridView.class).withId ( idupost );
                        categoriesModelchemiseList.add(categoriesModelpull);
                        categoriesAdaptechemise.notifyDataSetChanged();
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
                    imagePub.append(doc.get("pub_chemise"));
                    textPubcullote.setText(imagePub.toString());
                    String lien = textPubcullote.getText().toString();
                    Picasso.with(getActivity()).load(lien).into(imagePubcullote);
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
        chemiseRecyclerView=null;
        imagePubcullote=null;
        textPubcullote=null;
        firebaseFirestore=null;
        progressDialog=null;
        categoriesAdaptechemise=null;
        categoriesModelchemiseList=null;
    }

}