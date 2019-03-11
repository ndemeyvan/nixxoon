package cm.studio.devbee.communitymarket.Fragments;


import android.app.ProgressDialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
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
import cm.studio.devbee.communitymarket.utilsForNouveautes.CategoriesAdapteNouveaux;
import cm.studio.devbee.communitymarket.utilsForNouveautes.CategoriesModelNouveaux;
import cm.studio.devbee.communitymarket.utilsForUserApp.UserAdapter;
import cm.studio.devbee.communitymarket.utilsForUserApp.UserModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private FirebaseFirestore firebaseFirestore;
    private ImageView imageOne,imageTwo,imageThree,imageFour;
    private TextView img1,img2,img3,img4;
    private ProgressBar content_progresbar;
    private RecyclerView content_recyclerView;
    private CategoriesAdapteNouveaux categoriesAdapte;
    private List<CategoriesModelNouveaux> categoriesModelList;
    private ViewFlipper viewFlipper;
    private CategoriesAdapteNouveaux categoriesAdapteNouveaux;
    private RecyclerView nouveauxRecyclerView;
    private RecyclerView chaussureRecyclerView;
    private RecyclerView jupesRecyclerView;
    private List<CategoriesModelNouveaux> categoriesAdapteChaussureList;
    private List<CategoriesModelNouveaux> categoriesModelJupeList;
    private RecyclerView user_recyclerView;
    private UserAdapter userAdapter;
    private List<UserModel> userList;
    //////////tshirt
    private List<CategoriesModelNouveaux> categoriesModelTshirtList;
    private RecyclerView tshirtRecycler;
    //////chargement
    private DocumentSnapshot lastVisible;
    private  RecyclerView recyclerpull;
    List<CategoriesModelNouveaux> categoriesModelPullList;
    ////accesoire
    private  List<CategoriesModelNouveaux> categoriesModelAccessoireList;
    private RecyclerView recycleraccessoire;
    /////pubfixe
    private ImageView imagePubFixe;
    private TextView imagePubText;
    //cullote
    private RecyclerView recyclercullote;
    private List<CategoriesModelNouveaux> categoriesModelCulloteList;
    ////pantaloos
    private  RecyclerView recyclerpantalons;
    List<CategoriesModelNouveaux> categoriesModelPantalonsList;
    ///chemise
    private RecyclerView recyclerchemise;
    private List<CategoriesModelNouveaux> categoriesModelChemiseList;
    ///robe
    private List<CategoriesModelNouveaux> categoriesModelRobeList;
    private RecyclerView recyclerrobe;
    ProgressDialog progressDialog;
    private View v;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_home, container, false);
        ////////chaussure
        categoriesAdapteChaussureList=new ArrayList<>(  );
        chaussureRecyclerView=v.findViewById ( R.id.chaussureRecyclerView );
        categoriesAdapteNouveaux=new CategoriesAdapteNouveaux (categoriesAdapteChaussureList,getContext().getApplicationContext());
        chaussureRecyclerView.setAdapter ( categoriesAdapteNouveaux );
        chaussureRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        /////chaussure
        ////////nouveaux
        categoriesModelList= new ArrayList<>();
        nouveauxRecyclerView=v.findViewById(R.id.nouveautes_recyclerView);
        categoriesAdapteNouveaux=new CategoriesAdapteNouveaux(categoriesModelList,getActivity());
        nouveauxRecyclerView.setAdapter(categoriesAdapteNouveaux);
        nouveauxRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        ////nouveaux
        ///////jupes
        jupesRecyclerView=v.findViewById ( R.id.jupesRecyclerView );
        categoriesModelJupeList=new ArrayList<> (  );
        categoriesAdapteNouveaux=new CategoriesAdapteNouveaux (categoriesModelJupeList,getActivity());
        jupesRecyclerView.setAdapter ( categoriesAdapteNouveaux );
        jupesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        ///////fin jupes
        ///////:utilisateur
        user_recyclerView=v.findViewById ( R.id.user_recyclerView );
        userList=new ArrayList<> (  );
        userAdapter=new UserAdapter (userList,getActivity());
        user_recyclerView.setAdapter ( userAdapter );
        user_recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        //////fin utilisateur
        //////tshirt
        tshirtRecycler=v.findViewById ( R.id.tshirtRecycler );
        categoriesModelTshirtList=new ArrayList<> (  );
        categoriesAdapteNouveaux=new CategoriesAdapteNouveaux (categoriesModelTshirtList,getActivity());
        tshirtRecycler.setAdapter ( categoriesAdapteNouveaux );
        tshirtRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        ///fin tshirt
        //////pull
        recyclerpull=v.findViewById ( R.id.recyclerpull );
        categoriesModelPullList=new ArrayList<> (  );
        categoriesAdapteNouveaux=new CategoriesAdapteNouveaux (categoriesModelPullList,getActivity());
        recyclerpull.setAdapter ( categoriesAdapteNouveaux );
        recyclerpull.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        /////pull

        ///accessoire
        recycleraccessoire=v.findViewById ( R.id.recycleraccessoire );
        categoriesModelAccessoireList=new ArrayList<> (  );
        categoriesAdapteNouveaux=new CategoriesAdapteNouveaux (categoriesModelAccessoireList,getActivity());
        recycleraccessoire.setAdapter ( categoriesAdapteNouveaux );
        recycleraccessoire.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        //accessoire
        ///cullote
        recyclercullote=v.findViewById ( R.id.recyclercullote );
        categoriesModelCulloteList=new ArrayList<> (  );
        categoriesAdapteNouveaux=new CategoriesAdapteNouveaux (categoriesModelCulloteList,getActivity());
        recyclercullote.setAdapter ( categoriesAdapteNouveaux );
        recyclercullote.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        //culotte
        ///pantalos
        recyclerpantalons=v.findViewById ( R.id. recyclerpantalons );
        categoriesModelPantalonsList=new ArrayList<> (  );
        categoriesAdapteNouveaux=new CategoriesAdapteNouveaux (categoriesModelPantalonsList,getActivity());
        recyclerpantalons.setAdapter ( categoriesAdapteNouveaux );
        recyclerpantalons.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        //pantalons
        ////chemise
        recyclerchemise=v.findViewById ( R.id. recyclerchemise );
        categoriesModelChemiseList=new ArrayList<> (  );
        categoriesAdapteNouveaux=new CategoriesAdapteNouveaux(categoriesModelChemiseList,getActivity());
        recyclerchemise.setAdapter ( categoriesAdapteNouveaux );
        recyclerchemise.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        //chemise
        ////robe
        recyclerrobe=v.findViewById(R.id.recyclerrobe);
        categoriesModelRobeList=new ArrayList<> (  );
        categoriesAdapteNouveaux=new CategoriesAdapteNouveaux(categoriesModelRobeList,getActivity());
        recyclerrobe.setAdapter ( categoriesAdapteNouveaux );
        recyclerrobe.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        //robe
        //////////////slider
        imageOne=v.findViewById(R.id.imageSlideOne);
        imageTwo=v.findViewById(R.id.imageSlideTwo);
        imageThree=v.findViewById(R.id.imageSlideThree);
        imageFour=v.findViewById(R.id.imageSlideFour);
        img1=v.findViewById(R.id.img1);
        img2=v.findViewById(R.id.img2);
        img3=v.findViewById(R.id.img3);
        img4=v.findViewById(R.id.img4);
        ///////fin slider
        content_progresbar=v.findViewById ( R.id.content_progresbar );
        imagePubFixe=v.findViewById(R.id.pubImage);
        imagePubText=v.findViewById(R.id.pubImageText);
        viewFlipper=v.findViewById(R.id.viewFlipper);
        firebaseFirestore=FirebaseFirestore.getInstance();
        AsyncTask asyncTask=new AsyncTask ();
        asyncTask.execute (  );
        return v;
    }

    public void uptdate(){

        DocumentReference user = firebaseFirestore.collection("sliders").document("images");
        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()){
                    viewFlipper.setOutAnimation(getActivity(),android.R.anim.slide_out_right);
                    viewFlipper.setInAnimation(getActivity(),android.R.anim.slide_in_left);
                    content_progresbar.setVisibility ( View.INVISIBLE );
                    DocumentSnapshot doc =task.getResult();
                    StringBuilder image=new StringBuilder("");
                    image.append(doc.get("imageOne"));
                    img1.setText(image.toString());
                    String lien = img1.getText().toString();
                    Picasso.with(getActivity()).load(lien).into(imageOne);
                    //////////image deux
                    StringBuilder image2=new StringBuilder("");
                    image2.append(doc.get("imageTwo"));
                    img1.setText(image.toString());
                    String lien2 = img1.getText().toString();
                    Picasso.with(getActivity()).load(lien2).into(imageTwo);
                    //////image trois
                    StringBuilder image3=new StringBuilder("");
                    image3.append(doc.get("imageThree"));
                    img1.setText(image3.toString());
                    String lien3 = img1.getText().toString();
                    Picasso.with(getActivity()).load(lien3).into(imageThree);
                    //////////image quatre
                    StringBuilder image4=new StringBuilder("");
                    image4.append(doc.get("imageFour"));
                    img1.setText(image4.toString());
                    String lien4 = img1.getText().toString();
                    Picasso.with(getActivity()).load(lien4).into(imageFour);

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),"erreur lors du chargement du slider",Toast.LENGTH_LONG).show();
            }
        });

    }
    public void imagePub(){
        DocumentReference user = firebaseFirestore.collection("publicit").document("imageFixe");
        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()){
                    DocumentSnapshot doc =task.getResult();
                    StringBuilder imagePub=new StringBuilder("");
                    imagePub.append(doc.get("pub"));
                    imagePubText.setText(imagePub.toString());
                    String lien = imagePubText.getText().toString();
                    Picasso.with(getActivity()).load(lien).into(imagePubFixe);

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),"erreur lors du chargement du slider",Toast.LENGTH_LONG).show();
            }
        });
    }

 public void nouveautes(){
        Query firstQuery =firebaseFirestore.collection ( "publication" ).document ("categories").collection ( "nouveaux" ).orderBy ( "date_de_publication",Query.Direction.DESCENDING );
               // .limit(3);
        firstQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentChange doc:queryDocumentSnapshots.getDocumentChanges()){
                    if (doc.getType()==DocumentChange.Type.ADDED){
                        String idupost=doc.getDocument ().getId ();
                        CategoriesModelNouveaux categoriesModelNouveaux =doc.getDocument().toObject(CategoriesModelNouveaux.class).withId ( idupost );
                        categoriesModelList.add(categoriesModelNouveaux);
                        categoriesAdapteNouveaux.notifyDataSetChanged();
                    }
                }

            }
        });
    }
   public void chaussuresRecycler(){
        Query firstQuery =firebaseFirestore.collection ( "publication" ).document ("categories").collection ( "Chaussures" ).orderBy ( "date_de_publication",Query.Direction.DESCENDING );
        firstQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for (DocumentChange doc:queryDocumentSnapshots.getDocumentChanges()){
                    if (doc.getType()==DocumentChange.Type.ADDED){
                        String idupost=doc.getDocument ().getId ();
                        CategoriesModelNouveaux categoriesModelChaussure =doc.getDocument().toObject(CategoriesModelNouveaux.class).withId ( idupost );
                        categoriesAdapteChaussureList.add(categoriesModelChaussure);
                        categoriesAdapteNouveaux.notifyDataSetChanged();
                    }
                }

            }
        });

    }
    public void juperecycler(){
        Query firstQuery =firebaseFirestore.collection ( "publication" ).document ("categories").collection ( "jupes" ).orderBy ( "date_de_publication",Query.Direction.DESCENDING );
        firstQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for (DocumentChange doc:queryDocumentSnapshots.getDocumentChanges()){
                    if (doc.getType()==DocumentChange.Type.ADDED){
                        String idupost=doc.getDocument ().getId ();
                        CategoriesModelNouveaux categoriesModelJupe =doc.getDocument().toObject(CategoriesModelNouveaux.class).withId ( idupost );
                        categoriesModelJupeList.add(categoriesModelJupe);
                        categoriesAdapteNouveaux.notifyDataSetChanged();
                    }
                }

            }
        });
    }
    public void utilisateurREcycler(){
        Query firstQuery =firebaseFirestore.collection ( "mes donnees utilisateur" ).orderBy ( "user_telephone",Query.Direction.DESCENDING );
        firstQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for (DocumentChange doc:queryDocumentSnapshots.getDocumentChanges()){
                    if (doc.getType()==DocumentChange.Type.ADDED){
                        UserModel userModel =doc.getDocument().toObject(UserModel.class);
                        userList.add(userModel);
                        userAdapter.notifyDataSetChanged();
                    }
                }

            }
        });

    }
    public void tshirtRecyclerView(){
        Query firstQuery =firebaseFirestore.collection ( "publication" ).document ("categories").collection ( "T-shirts" ).orderBy ( "date_de_publication",Query.Direction.DESCENDING );
        firstQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for (DocumentChange doc:queryDocumentSnapshots.getDocumentChanges()){
                    if (doc.getType()==DocumentChange.Type.ADDED){
                        String idupost=doc.getDocument ().getId ();
                        CategoriesModelNouveaux categoriesModelTshirt =doc.getDocument().toObject(CategoriesModelNouveaux.class).withId ( idupost );
                        categoriesModelTshirtList.add(categoriesModelTshirt);
                        categoriesAdapteNouveaux.notifyDataSetChanged();
                    }
                }

            }
        });
    }

    public void recyclerPull(){
        Query firstQuery =firebaseFirestore.collection ( "publication" ).document ("categories").collection ( "pull" ).orderBy ( "date_de_publication",Query.Direction.DESCENDING );
        firstQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for (DocumentChange doc:queryDocumentSnapshots.getDocumentChanges()){
                    if (doc.getType()==DocumentChange.Type.ADDED){
                        String idupost=doc.getDocument ().getId ();
                        CategoriesModelNouveaux categoriesModelPull =doc.getDocument().toObject(CategoriesModelNouveaux.class).withId ( idupost );
                        categoriesModelPullList.add(categoriesModelPull);
                        categoriesAdapteNouveaux.notifyDataSetChanged();
                    }
                }

            }
        });
    }
    public void recyclerAccessoire(){
        Query firstQuery =firebaseFirestore.collection ( "publication" ).document ("categories").collection ( "accessoires" ).orderBy ( "date_de_publication",Query.Direction.DESCENDING );
        firstQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for (DocumentChange doc:queryDocumentSnapshots.getDocumentChanges()){
                    if (doc.getType()==DocumentChange.Type.ADDED){
                        String idupost=doc.getDocument ().getId ();
                        CategoriesModelNouveaux categoriesModelAccessoire =doc.getDocument().toObject(CategoriesModelNouveaux.class).withId ( idupost );
                        categoriesModelAccessoireList.add(categoriesModelAccessoire);
                        categoriesAdapteNouveaux.notifyDataSetChanged();
                    }
                }

            }
        });
    }
    public void recyclerCullote(){
        Query firstQuery =firebaseFirestore.collection ( "publication" ).document ("categories").collection ( "Cullotes" ).orderBy ( "date_de_publication",Query.Direction.DESCENDING );
        firstQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for (DocumentChange doc:queryDocumentSnapshots.getDocumentChanges()){
                    if (doc.getType()==DocumentChange.Type.ADDED){
                        String idupost=doc.getDocument ().getId ();
                        CategoriesModelNouveaux categoriesModelCullote =doc.getDocument().toObject(CategoriesModelNouveaux.class).withId ( idupost );
                        categoriesModelCulloteList.add(categoriesModelCullote);
                        categoriesAdapteNouveaux.notifyDataSetChanged();
                    }
                }

            }
        });
    }
    public void recyclerpantalons(){
        Query firstQuery =firebaseFirestore.collection ( "publication" ).document ("categories").collection ( "Pantalons" ).orderBy ( "date_de_publication",Query.Direction.DESCENDING );
        firstQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for (DocumentChange doc:queryDocumentSnapshots.getDocumentChanges()){
                    if (doc.getType()==DocumentChange.Type.ADDED){
                        String idupost=doc.getDocument ().getId ();
                        CategoriesModelNouveaux categoriesModelPantalons =doc.getDocument().toObject(CategoriesModelNouveaux.class).withId ( idupost );
                        categoriesModelPantalonsList.add(categoriesModelPantalons);
                        categoriesAdapteNouveaux.notifyDataSetChanged();
                    }
                }

            }
        });
    }
    public void recyclerChemise(){
        Query firstQuery =firebaseFirestore.collection ( "publication" ).document ("categories").collection ( "Chemises" ).orderBy ( "date_de_publication",Query.Direction.DESCENDING );
        firstQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for (DocumentChange doc:queryDocumentSnapshots.getDocumentChanges()){
                    if (doc.getType()==DocumentChange.Type.ADDED){
                        String idupost=doc.getDocument ().getId ();
                        CategoriesModelNouveaux categoriesModelChemise=doc.getDocument().toObject(CategoriesModelNouveaux.class).withId ( idupost );
                        categoriesModelChemiseList.add(categoriesModelChemise);
                        categoriesAdapteNouveaux.notifyDataSetChanged();
                    }
                }

            }
        });
    }
    public void recyclerRobe(){
        Query firstQuery =firebaseFirestore.collection ( "publication" ).document ("categories").collection ( "robe" ).orderBy ( "date_de_publication",Query.Direction.DESCENDING );
        firstQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for (DocumentChange doc:queryDocumentSnapshots.getDocumentChanges()){
                    if (doc.getType()==DocumentChange.Type.ADDED){
                        String idupost=doc.getDocument ().getId ();
                        CategoriesModelNouveaux categoriesModelRobe=doc.getDocument().toObject(CategoriesModelNouveaux.class).withId ( idupost );
                        categoriesModelRobeList.add(categoriesModelRobe);
                        categoriesAdapteNouveaux.notifyDataSetChanged();
                    }
                }

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
            uptdate();
            nouveautes();
            chaussuresRecycler ();
            juperecycler ();
            utilisateurREcycler ();
            tshirtRecyclerView();
            recyclerPull();
            recyclerAccessoire();
            imagePub();
            recyclerCullote();
            recyclerpantalons();
            recyclerChemise();
            recyclerRobe();
            imagePub();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute ( aVoid );
            progressDialog.dismiss();
        }
    }

}
