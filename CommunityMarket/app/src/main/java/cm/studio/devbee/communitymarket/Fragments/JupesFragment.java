package cm.studio.devbee.communitymarket.Fragments;


import android.app.ProgressDialog;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
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
public class JupesFragment extends Fragment {
    private View v;
    private static RecyclerView jupeRecyclerView;
    private static ImageView imagePubjupe;
    private static TextView textPubjupe;
    private static FirebaseFirestore firebaseFirestore;
    private static ProgressDialog progressDialog;
    private static AsyncTask asyncTask;
    private static GridViewAdapter categoriesAdaptejupe;
    private static List<ModelGridView> categoriesModeljupeList;
    private static WeakReference<JupesFragment> jupeFragmentWeakReference;
    private static FirebaseAuth firebaseAuth;
    String curent_user;

    public JupesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate ( R.layout.fragment_jupes, container, false );
        firebaseFirestore=FirebaseFirestore.getInstance ();
        imagePubjupe=v.findViewById ( R.id.pubImag_jupe);
        textPubjupe=v.findViewById ( R.id.pubImageText_jupe);
        firebaseAuth=FirebaseAuth.getInstance ();
        curent_user=firebaseAuth.getCurrentUser ().getUid ();
        //////////
        jupeRecyclerView=v.findViewById ( R.id.jupeRecyclerView );
        categoriesModeljupeList=new ArrayList<> (  );
        categoriesAdaptejupe=new GridViewAdapter ( categoriesModeljupeList,getActivity () );
        jupeRecyclerView.setAdapter ( categoriesAdaptejupe );
        jupeRecyclerView.setLayoutManager(new LinearLayoutManager (getActivity(),LinearLayoutManager.VERTICAL,false));
        ////////pull
        jupeFragmentWeakReference=new WeakReference<>(this);
        asyncTask=new AsyncTask ();
        asyncTask.execute();
        ConstraintLayout constraintLayout=v.findViewById(R.id.layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
        return v;
    }

    public void userstatus(String status){
        DocumentReference user = firebaseFirestore.collection("mes donnees utilisateur" ).document(curent_user);
        user.update("status", status)
                .addOnSuccessListener(getActivity (),new OnSuccessListener<Void> () {
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
    public void jupeRecyclerView(){

        Query firstQuery =firebaseFirestore.collection ( "publication" ).document ("categories").collection ( "jupes" ).orderBy ( "prix_du_produit",Query.Direction.ASCENDING );
        firstQuery.addSnapshotListener(getActivity (),new EventListener<QuerySnapshot> () {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for (DocumentChange doc:queryDocumentSnapshots.getDocumentChanges()){
                    if (doc.getType()==DocumentChange.Type.ADDED){
                        String idupost=doc.getDocument ().getId ();
                        ModelGridView categoriesModelpull =doc.getDocument().toObject(ModelGridView.class).withId ( idupost );
                        categoriesModeljupeList.add(categoriesModelpull);
                        categoriesAdaptejupe.notifyDataSetChanged();
                    }
                }

            }
        });
    }
    public  void imagePub_jupe(){
        DocumentReference user = firebaseFirestore.collection("publicit").document("imageFixe");
        user.get().addOnCompleteListener(getActivity (),new OnCompleteListener<DocumentSnapshot> () {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot doc =task.getResult();
                    StringBuilder imagePub=new StringBuilder("");
                    imagePub.append(doc.get("pub_jupe"));
                    textPubjupe.setText(imagePub.toString());
                    String lien = textPubjupe.getText().toString();
                    Picasso.with(getActivity()).load(lien).into(imagePubjupe);

                }
            }
        }).addOnFailureListener(getActivity (),new OnFailureListener () {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),getString(R.string.erreur_slider),Toast.LENGTH_LONG).show();
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
            jupeRecyclerView ();
           imagePub_jupe ();
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
        jupeRecyclerView=null;
        imagePubjupe=null;
        textPubjupe=null;
        firebaseFirestore=null;
        progressDialog=null;
        categoriesAdaptejupe=null;
        categoriesModeljupeList=null;
    }

}
