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
import com.google.android.gms.tasks.OnSuccessListener;
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
public class AccesoireFragment extends Fragment {
    private  static View v;
    private static RecyclerView acessoireRecyclerView;
    private static ImageView imagePubacessoire;
    private static TextView textPubacessoire;
    private static FirebaseFirestore firebaseFirestore;
    private static ProgressDialog progressDialog;
    private static AsyncTask asyncTask;
    private static GridViewAdapter categoriesAdapteacessoire;
    private static List<ModelGridView> categoriesModelacessoireList;
    private static WeakReference<AccesoireFragment> acessoireFragmentWeakReference;

    public AccesoireFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate ( R.layout.fragment_accesoire, container, false );
        firebaseFirestore=FirebaseFirestore.getInstance ();
        imagePubacessoire=v.findViewById ( R.id.pubImag_accessoires);
        textPubacessoire=v.findViewById ( R.id.pubImageText_accessoires);
        //////////
        acessoireRecyclerView=v.findViewById ( R.id.accessoiresRecyclerView );
        categoriesModelacessoireList=new ArrayList<> (  );
        categoriesAdapteacessoire=new GridViewAdapter (categoriesModelacessoireList,getActivity () );
        acessoireRecyclerView.setAdapter ( categoriesAdapteacessoire );
        acessoireRecyclerView.setLayoutManager(new GridLayoutManager (getActivity(),2));
        ////////pull
        asyncTask=new AsyncTask ();
        asyncTask.execute();
        acessoireFragmentWeakReference=new WeakReference<> ( this );
        return v;
    }
    public void userstatus(String status){
        DocumentReference user = firebaseFirestore.collection("mes donnees utilisateur" ).document(current_user_id);
        user.update("status", status)
                .addOnSuccessListener(new OnSuccessListener<Void> () {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume ();
        userstatus("online");
    }

    @Override
    public void onPause() {
        super.onPause ();
        userstatus("offline");
    }
    public void RecyclerView(){

        Query firstQuery =firebaseFirestore.collection ( "publication" ).document ("categories").collection ( "accessoires" ).orderBy ( "dete-en-seconde",Query.Direction.DESCENDING );
        firstQuery.addSnapshotListener(new EventListener<QuerySnapshot> () {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for (DocumentChange doc:queryDocumentSnapshots.getDocumentChanges()){
                    if (doc.getType()==DocumentChange.Type.ADDED){
                        String idupost=doc.getDocument ().getId ();
                        ModelGridView categoriesModelpull =doc.getDocument().toObject(ModelGridView.class).withId ( idupost );
                        categoriesModelacessoireList.add(categoriesModelpull);
                        categoriesAdapteacessoire.notifyDataSetChanged();
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
                    imagePub.append(doc.get("pub_accessoire"));
                    textPubacessoire.setText(imagePub.toString());
                    String lien = textPubacessoire.getText().toString();
                    Picasso.with(getActivity()).load(lien).into(imagePubacessoire);
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
        acessoireRecyclerView =null;
        imagePubacessoire=null;
        textPubacessoire=null;
        firebaseFirestore=null;
        progressDialog=null;
        categoriesAdapteacessoire=null;
        categoriesModelacessoireList=null;
    }

}
