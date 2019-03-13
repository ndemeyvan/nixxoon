package cm.studio.devbee.communitymarket.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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
public class PullFragment extends Fragment {

private View v;
    private static GridViewAdapter gridViewAdapter;
    private static List<ModelGridView> modelGridViewList;
    private static ImageView imagePubpull;
    private static TextView textPubpull;
    private static FirebaseFirestore firebaseFirestore;
    private static ProgressDialog progressDialog;
    private static AsyncTask asyncTask;
    private static WeakReference<PullFragment> pullFragmentWeakReference;
    public PullFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate ( R.layout.fragment_pull, container, false );
        firebaseFirestore=FirebaseFirestore.getInstance ();
        imagePubpull=v.findViewById ( R.id.pubImag_pull );
        textPubpull=v.findViewById ( R.id.pubImageText_pull );
        modelGridViewList=new ArrayList<> (  );
        gridViewAdapter=new GridViewAdapter ( modelGridViewList,getActivity () );
        pullFragmentWeakReference=new WeakReference<>(this);
        asyncTask=new AsyncTask();
        asyncTask.execute();


        return v;
    }
    public  void imagePub_pull(){
        DocumentReference user = firebaseFirestore.collection("publicit").document("imageFixe");
        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot> () {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()){
                    DocumentSnapshot doc =task.getResult();
                    StringBuilder imagePub=new StringBuilder("");
                    imagePub.append(doc.get("pub_tshirt"));
                    textPubpull.setText(imagePub.toString());
                    String lien = textPubpull.getText().toString();
                    Picasso.with(getActivity()).load(lien).into(imagePubpull);

                }
            }
        }).addOnFailureListener(new OnFailureListener () {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),"erreur lors du chargement du slider",Toast.LENGTH_LONG).show();
            }
        });
    }
    public  void pullRecyclerView(){
        Query firstQuery =firebaseFirestore.collection ( "publication" ).document ("categories").collection ( "pull" ).orderBy ( "date_de_publication",Query.Direction.DESCENDING );
        firstQuery.addSnapshotListener(new EventListener<QuerySnapshot> () {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for (DocumentChange doc:queryDocumentSnapshots.getDocumentChanges()){
                    if (doc.getType()==DocumentChange.Type.ADDED){
                        String idupost=doc.getDocument ().getId ();
                        ModelGridView modelGridView =doc.getDocument().toObject(ModelGridView.class).withId ( idupost );
                        modelGridViewList.add(modelGridView);
                        gridViewAdapter.notifyDataSetChanged();
                    }
                }

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
            pullRecyclerView ();
            imagePub_pull ();
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
        gridViewAdapter=null;
        modelGridViewList=null;
        imagePubpull=null;
        textPubpull=null;
        firebaseFirestore=null;
        progressDialog=null;
    }
}
