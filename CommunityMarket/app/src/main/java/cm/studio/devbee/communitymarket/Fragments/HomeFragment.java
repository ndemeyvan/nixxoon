package cm.studio.devbee.communitymarket.Fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import cm.studio.devbee.communitymarket.Accueil;
import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.Utilpantalons.CategoriesAdaptePantalons;
import cm.studio.devbee.communitymarket.Utilpantalons.CategoriesModelPantalons;
import cm.studio.devbee.communitymarket.postActivity.PostActivity;
import cm.studio.devbee.communitymarket.utilForCullotes.CategoriesAdapteCullote;
import cm.studio.devbee.communitymarket.utilForCullotes.CategoriesModelCullote;
import cm.studio.devbee.communitymarket.utilForJupe.CategoriesAdapteJupe;
import cm.studio.devbee.communitymarket.utilForJupe.CategoriesModelJupe;
import cm.studio.devbee.communitymarket.utilForT_shirt.CategoriesAdapteTshirt;
import cm.studio.devbee.communitymarket.utilForT_shirt.CategoriesModelTshirt;
import cm.studio.devbee.communitymarket.utilsForAccessoire.CategoriesAdapteAccessoire;
import cm.studio.devbee.communitymarket.utilsForAccessoire.CategoriesModelAccessoire;
import cm.studio.devbee.communitymarket.utilsForCategories.CategoriesAdapte;
import cm.studio.devbee.communitymarket.utilsForCategories.CategoriesModel;
import cm.studio.devbee.communitymarket.utilsForChaussure.CategoriesAdapteChaussure;
import cm.studio.devbee.communitymarket.utilsForChaussure.CategoriesModelChaussure;
import cm.studio.devbee.communitymarket.utilsForChemise.CategoriesAdapteChemise;
import cm.studio.devbee.communitymarket.utilsForChemise.CategoriesModelChemise;
import cm.studio.devbee.communitymarket.utilsForNouveautes.CategoriesAdapteNouveaux;
import cm.studio.devbee.communitymarket.utilsForNouveautes.CategoriesModelNouveaux;
import cm.studio.devbee.communitymarket.utilsForPull.CategoriesAdaptePull;
import cm.studio.devbee.communitymarket.utilsForPull.CategoriesModelPull;
import cm.studio.devbee.communitymarket.utilsForRobe.CategoriesAdapteRobe;
import cm.studio.devbee.communitymarket.utilsForRobe.CategoriesModelRobe;
import cm.studio.devbee.communitymarket.utilsForUserApp.UserAdapter;
import cm.studio.devbee.communitymarket.utilsForUserApp.UserModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private FirebaseAuth mAuth;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private String current_user_id;
    private ImageView imageOne,imageTwo,imageThree,imageFour;
    private TextView img1,img2,img3,img4;
    private ProgressBar content_progresbar;
    private RecyclerView content_recyclerView;
    private CategoriesAdapte categoriesAdapte;
    private List<CategoriesModel> categoriesModelList;
    private FloatingActionButton content_floating_action_btn;
    private ViewFlipper viewFlipper;
    private CategoriesAdapteNouveaux categoriesAdapteNouveaux;
    private RecyclerView nouveauxRecyclerView;
    private RecyclerView chaussureRecyclerView;
    private CategoriesAdapteChaussure categoriesAdapteChaussure;
    private List<CategoriesModelChaussure> categoriesAdapteChaussureList;
    private List<CategoriesModelNouveaux> categoriesModelNouveauxList;
    private RecyclerView jupesRecyclerView;
    private CategoriesAdapteJupe categoriesAdapteJupe;
    private CategoriesModelJupe categoriesModelJupe;
    private List<CategoriesModelJupe> categoriesModelJupeList;
    private RecyclerView user_recyclerView;
    private UserAdapter userAdapter;
    private List<UserModel> userList;
    //////////tshirt
    private CategoriesAdapteTshirt categoriesAdapteTshirt;
    private List<CategoriesModelTshirt> categoriesModelTshirtList;
    private RecyclerView tshirtRecycler;
    //////chargement
    private DocumentSnapshot lastVisible;
    private  RecyclerView recyclerpull;
    private CategoriesAdaptePull categoriesAdaptePull;
    List<CategoriesModelPull> categoriesModelPullList;
    ////accesoire
    private  List<CategoriesModelAccessoire> categoriesModelAccessoireList;
    private CategoriesAdapteAccessoire categoriesAdapteAccessoire;
    private RecyclerView recycleraccessoire;
    /////pubfixe
    private ImageView imagePubFixe;
    private TextView imagePubText;
    //cullote
    private RecyclerView recyclercullote;
    private CategoriesAdapteCullote categoriesAdapteCullote;
    private List<CategoriesModelCullote> categoriesModelCulloteList;
    ////pantaloos
    private  RecyclerView recyclerpantalons;
    private CategoriesAdaptePantalons categoriesAdaptePantalons;
    List<CategoriesModelPantalons> categoriesModelPantalonsList;
    ///chemise
    private RecyclerView recyclerchemise;
    private CategoriesAdapteChemise categoriesAdapteChemise;
    private List<CategoriesModelChemise> categoriesModelChemiseList;
    ///robe
    private List<CategoriesModelRobe> categoriesModelRobeList;
    private CategoriesAdapteRobe categoriesAdapteRobe;
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
        categoriesAdapteChaussure=new CategoriesAdapteChaussure (categoriesAdapteChaussureList,getActivity());
        chaussureRecyclerView.setAdapter ( categoriesAdapteChaussure );
        chaussureRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        /////chaussure
        ////////nouveaux
        categoriesModelNouveauxList=new ArrayList<>();
        nouveauxRecyclerView=v.findViewById(R.id.nouveautes_recyclerView);
        categoriesAdapteNouveaux=new CategoriesAdapteNouveaux(categoriesModelNouveauxList,getActivity());
        nouveauxRecyclerView.setAdapter(categoriesAdapteNouveaux);
        nouveauxRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
     /*   nouveauxRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Boolean arriveOcoin = !nouveauxRecyclerView.canScrollHorizontally(1);
                if (arriveOcoin){
                    Toast.makeText(getActivity(),"chargement",Toast.LENGTH_LONG).show();
                    //chargerPlus();
                }else{

                }

            }
        }); */
        ////nouveaux
        ///////jupes
        jupesRecyclerView=v.findViewById ( R.id.jupesRecyclerView );
        categoriesModelJupeList=new ArrayList<> (  );
        categoriesAdapteJupe=new CategoriesAdapteJupe (categoriesModelJupeList,getActivity());
        jupesRecyclerView.setAdapter ( categoriesAdapteJupe );
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
        categoriesAdapteTshirt=new CategoriesAdapteTshirt (categoriesModelTshirtList,getActivity());
        tshirtRecycler.setAdapter ( categoriesAdapteTshirt );
        tshirtRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        ///fin tshirt
        //////pull
        recyclerpull=v.findViewById ( R.id.recyclerpull );
        categoriesModelPullList=new ArrayList<> (  );
        categoriesAdaptePull=new CategoriesAdaptePull (categoriesModelPullList,getActivity());
        recyclerpull.setAdapter ( categoriesAdaptePull );
        recyclerpull.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        /////pull

        ///accessoire
        recycleraccessoire=v.findViewById ( R.id.recycleraccessoire );
        categoriesModelAccessoireList=new ArrayList<> (  );
        categoriesAdapteAccessoire=new CategoriesAdapteAccessoire (categoriesModelAccessoireList,getActivity());
        recycleraccessoire.setAdapter ( categoriesAdapteAccessoire );
        recycleraccessoire.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        //accessoire
        ///cullote
        recyclercullote=v.findViewById ( R.id.recyclercullote );
        categoriesModelCulloteList=new ArrayList<> (  );
        categoriesAdapteCullote=new CategoriesAdapteCullote (categoriesModelCulloteList,getActivity());
        recyclercullote.setAdapter ( categoriesAdapteCullote );
        recyclercullote.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        //culotte
        ///pantalos
        recyclerpantalons=v.findViewById ( R.id. recyclerpantalons );
        categoriesModelPantalonsList=new ArrayList<> (  );
        categoriesAdaptePantalons=new CategoriesAdaptePantalons (categoriesModelPantalonsList,getActivity());
        recyclerpantalons.setAdapter ( categoriesAdaptePantalons );
        recyclerpantalons.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        //pantalons
        ////chemise
        recyclerchemise=v.findViewById ( R.id. recyclerchemise );
        categoriesModelChemiseList=new ArrayList<> (  );
        categoriesAdapteChemise=new CategoriesAdapteChemise(categoriesModelChemiseList,getActivity());
        recyclerchemise.setAdapter ( categoriesAdapteChemise );
        recyclerchemise.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        //chemise
        ////robe
        recyclerrobe=v.findViewById(R.id.recyclerrobe);
        categoriesModelRobeList=new ArrayList<> (  );
        categoriesAdapteRobe=new CategoriesAdapteRobe(categoriesModelRobeList,getActivity());
        recyclerrobe.setAdapter ( categoriesAdapteRobe );
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
        storageReference=FirebaseStorage.getInstance().getReference();
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
                lastVisible = queryDocumentSnapshots.getDocuments()
                        .get(queryDocumentSnapshots.size() -1);
                for (DocumentChange doc:queryDocumentSnapshots.getDocumentChanges()){
                    if (doc.getType()==DocumentChange.Type.ADDED){
                        String idupost=doc.getDocument ().getId ();
                        CategoriesModelNouveaux categoriesModelNouveaux =doc.getDocument().toObject(CategoriesModelNouveaux.class).withId ( idupost );
                        categoriesModelNouveauxList.add(categoriesModelNouveaux);
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
                        CategoriesModelChaussure categoriesModelChaussure =doc.getDocument().toObject(CategoriesModelChaussure.class).withId ( idupost );
                        categoriesAdapteChaussureList.add(categoriesModelChaussure);
                        categoriesAdapteChaussure.notifyDataSetChanged();
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
                        CategoriesModelJupe categoriesModelJupe =doc.getDocument().toObject(CategoriesModelJupe.class).withId ( idupost );
                        categoriesModelJupeList.add(categoriesModelJupe);
                        categoriesAdapteJupe.notifyDataSetChanged();
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
                        CategoriesModelTshirt categoriesModelTshirt =doc.getDocument().toObject(CategoriesModelTshirt.class).withId ( idupost );
                        categoriesModelTshirtList.add(categoriesModelTshirt);
                        categoriesAdapteTshirt.notifyDataSetChanged();
                    }
                }

            }
        });
    }
   /* public void chargerPlus(){

        Query prochain =firebaseFirestore.collection ( "publication" ).document ("categories").collection ( "nouveaux" ).orderBy ( "date_de_publication",Query.Direction.DESCENDING )
                .startAfter(lastVisible)
                .limit(3);


        prochain.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    lastVisible = queryDocumentSnapshots.getDocuments()
                            .get(queryDocumentSnapshots.size() - 1);
                    for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                        if (doc.getType() == DocumentChange.Type.ADDED) {
                            String idupost = doc.getDocument().getId();
                            CategoriesModelNouveaux categoriesModelNouveaux = doc.getDocument().toObject(CategoriesModelNouveaux.class).withId(idupost);
                            categoriesModelNouveauxList.add(categoriesModelNouveaux);
                            categoriesAdapteNouveaux.notifyDataSetChanged();
                        }
                    }
                }else {

                }

            }
        });
    }*/
    public void recyclerPull(){
        Query firstQuery =firebaseFirestore.collection ( "publication" ).document ("categories").collection ( "pull" ).orderBy ( "date_de_publication",Query.Direction.DESCENDING );
        firstQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for (DocumentChange doc:queryDocumentSnapshots.getDocumentChanges()){
                    if (doc.getType()==DocumentChange.Type.ADDED){
                        String idupost=doc.getDocument ().getId ();
                        CategoriesModelPull categoriesModelPull =doc.getDocument().toObject(CategoriesModelPull.class).withId ( idupost );
                        categoriesModelPullList.add(categoriesModelPull);
                        categoriesAdaptePull.notifyDataSetChanged();
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
                        CategoriesModelAccessoire categoriesModelAccessoire =doc.getDocument().toObject(CategoriesModelAccessoire.class).withId ( idupost );
                        categoriesModelAccessoireList.add(categoriesModelAccessoire);
                        categoriesAdapteAccessoire.notifyDataSetChanged();
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
                        CategoriesModelCullote categoriesModelCullote =doc.getDocument().toObject(CategoriesModelCullote.class).withId ( idupost );
                        categoriesModelCulloteList.add(categoriesModelCullote);
                        categoriesAdapteCullote.notifyDataSetChanged();
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
                        CategoriesModelPantalons categoriesModelPantalons =doc.getDocument().toObject(CategoriesModelPantalons.class).withId ( idupost );
                        categoriesModelPantalonsList.add(categoriesModelPantalons);
                        categoriesAdaptePantalons.notifyDataSetChanged();
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
                        CategoriesModelChemise categoriesModelChemise=doc.getDocument().toObject(CategoriesModelChemise.class).withId ( idupost );
                        categoriesModelChemiseList.add(categoriesModelChemise);
                        categoriesAdapteChemise.notifyDataSetChanged();
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
                        CategoriesModelRobe categoriesModelRobe=doc.getDocument().toObject(CategoriesModelRobe.class).withId ( idupost );
                        categoriesModelRobeList.add(categoriesModelRobe);
                        categoriesAdapteRobe.notifyDataSetChanged();
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
