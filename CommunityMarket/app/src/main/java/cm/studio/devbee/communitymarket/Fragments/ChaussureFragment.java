package cm.studio.devbee.communitymarket.Fragments;


import android.app.ProgressDialog;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
public class ChaussureFragment extends Fragment {
    private static FirebaseFirestore firebaseFirestore;
    private static View v;
    private static RecyclerView chaussuresRecyclerView;
    private static GridViewAdapter categoriesAdaptechaussures;
    private static List<ModelGridView> categoriesModelchaussuresList;
    private static ImageView imagePubchaussures;
    private static TextView textPubchaussures;
    private static ProgressDialog progressDialog;
    private static WeakReference<ChaussureFragment> chaussuresFragmentWeakReference;
    private AsyncTask asyncTask;
    private static FirebaseAuth firebaseAuth;
    String curent_user;

    public ChaussureFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate ( R.layout.fragment_chaussure, container, false );
        textPubchaussures=v.findViewById ( R.id.pubImageText_chaussure );
        imagePubchaussures=v.findViewById ( R.id.pubImag_chaussure );
        chaussuresFragmentWeakReference=new WeakReference<> ( this );
        chaussuresRecyclerView=v.findViewById ( R.id.chaussureRecyclerView );
        firebaseFirestore=FirebaseFirestore.getInstance();
        categoriesModelchaussuresList=new ArrayList<> (  );
        categoriesAdaptechaussures=new GridViewAdapter (categoriesModelchaussuresList,getActivity());
        chaussuresRecyclerView.setAdapter ( categoriesAdaptechaussures );
        chaussuresRecyclerView.setLayoutManager(new LinearLayoutManager (getActivity(),LinearLayoutManager.VERTICAL,false));
        chaussuresRecyclerView=v.findViewById ( R.id.pubImag_tshire );
        chaussuresRecyclerView=v.findViewById ( R.id.pubImageText_tshirt );
        asyncTask=new AsyncTask ();
        asyncTask.execute (  );
        firebaseAuth=FirebaseAuth.getInstance ();
        curent_user=firebaseAuth.getCurrentUser ().getUid ();


        return v;
    }
    public void userstatus(String status){
        DocumentReference user = firebaseFirestore.collection("mes donnees utilisateur" ).document(curent_user);
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
    public void chaussureRecyclerView(){

        Query firstQuery =firebaseFirestore.collection ( "publication" ).document ("categories").collection ( "Chaussures" ).orderBy ( "date_de_publication",Query.Direction.DESCENDING );
        firstQuery.addSnapshotListener(getActivity (),new EventListener<QuerySnapshot> () {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for (DocumentChange doc:queryDocumentSnapshots.getDocumentChanges()){
                    if (doc.getType()==DocumentChange.Type.ADDED){
                        String idupost=doc.getDocument ().getId ();
                        ModelGridView categoriesModelTshirt =doc.getDocument().toObject(ModelGridView.class).withId ( idupost );
                        categoriesModelchaussuresList.add(categoriesModelTshirt);
                        categoriesAdaptechaussures.notifyDataSetChanged();
                    }
                }

            }
        });
    }
    public void imagePub_chaussure(){
        DocumentReference user = firebaseFirestore.collection("publicit").document("imageFixe");
        user.get().addOnCompleteListener(getActivity (),new OnCompleteListener<DocumentSnapshot> () {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()){
                    DocumentSnapshot doc =task.getResult();
                    StringBuilder imagePub=new StringBuilder("");
                    imagePub.append(doc.get("pub_chaussure"));
                    textPubchaussures.setText(imagePub.toString());
                    String lien = textPubchaussures.getText().toString();
                    Picasso.with(getActivity()).load(lien).into(imagePubchaussures);

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
           chaussureRecyclerView ();
           imagePub_chaussure ();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute ( aVoid );
            progressDialog.dismiss();
        }
    }
    @Override
    public void onDestroy() {
        asyncTask.cancel(true);
        super.onDestroy();
        asyncTask.cancel(true);
        firebaseFirestore=null;
        chaussuresRecyclerView=null;
        v=null;
        categoriesAdaptechaussures=null;
        categoriesModelchaussuresList=null;
        imagePubchaussures=null;
        textPubchaussures=null;
        progressDialog=null;
    }

}

//Chaussures
