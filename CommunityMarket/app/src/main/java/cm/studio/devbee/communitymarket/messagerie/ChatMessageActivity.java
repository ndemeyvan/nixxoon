package cm.studio.devbee.communitymarket.messagerie;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

import java.util.List;

import javax.annotation.Nullable;

import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.utilForChat.DiplayAllChat;
import cm.studio.devbee.communitymarket.utilForChat.ModelChat;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatMessageActivity extends AppCompatActivity {
    private  static FirebaseAuth firebaseAuth;
    private  static String current_user;
    private static FirebaseFirestore firebaseFirestore;
    private static RecyclerView contatc_recyclerview;
    private GroupAdapter groupAdapter;
    private static Toolbar message_toolbar;
    private String image_profil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_chat_message );
        firebaseAuth=FirebaseAuth.getInstance ();
        message_toolbar=findViewById ( R.id.message_toolbar );
        setSupportActionBar (message_toolbar);
        getSupportActionBar ().setTitle ( "Discussions" );
        contatc_recyclerview=findViewById ( R.id.contatc_recyclerview );
        current_user=firebaseAuth.getCurrentUser ().getUid ();
        contatc_recyclerview.setLayoutManager ( new LinearLayoutManager ( getApplicationContext () ) );
        groupAdapter=new GroupAdapter ();
        firebaseFirestore=FirebaseFirestore.getInstance ();
        contatc_recyclerview.setAdapter ( groupAdapter );
        recuperation ();
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        message_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public  void recuperation(){
        firebaseFirestore.collection ( "dernier_message" )
                .document (current_user).collection ( "contacts" )
                .addSnapshotListener ( new EventListener<QuerySnapshot> () {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        List<DocumentChange> documentChanges = queryDocumentSnapshots.getDocumentChanges();
                        if (documentChanges!=null){
                            for (DocumentChange doc:documentChanges){
                                if (doc.getType()==DocumentChange.Type.ADDED){
                                    DiplayAllChat model=doc.getDocument ().toObject ( DiplayAllChat.class );
                                    groupAdapter.add ( new ContactItem ( model ) );
                                    groupAdapter.notifyDataSetChanged ();
                                }
                            }
                        }
                    }
                } );

    }




    public class ContactItem extends Item<ViewHolder> {
        private DiplayAllChat diplayAllChat;

        public ContactItem(DiplayAllChat diplayAllChat){
            this.diplayAllChat=diplayAllChat;
        }

        @Override
        public void bind(@NonNull ViewHolder viewHolder, int position) {
        final TextView lats_message=viewHolder.itemView.findViewById ( R.id.chat_last_message );
        final TextView nom_utilisateur=viewHolder.itemView.findViewById ( R.id.chat_user_name );
            CardView chat_card = viewHolder.itemView.findViewById ( R.id.chat_card );
        TextView temps=viewHolder.itemView.findViewById ( R.id.chat_temps );
            final CircleImageView profil=viewHolder.itemView.findViewById ( R.id.chat_message_image_profil );
            lats_message.setText ( diplayAllChat.getDernier_message () );
            nom_utilisateur.setText ( diplayAllChat.getNom_utilisateur () );
            temps.setText ( diplayAllChat.getTemps () );
            if ( diplayAllChat.getId_recepteur ().equals ( current_user )){
                firebaseFirestore=FirebaseFirestore.getInstance ();
                firebaseFirestore.collection("mes donnees utilisateur").document(diplayAllChat.getId_expediteur ()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot> () {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            if (task.getResult ().exists ()){
                                String prenom=task.getResult ().getString ( "user_prenom" );
                                String name_user= task.getResult ().getString ( "user_name" );
                                image_profil =task.getResult ().getString ( "user_profil_image" );
                                nom_utilisateur.setText(name_user+" "+prenom);
                                Picasso.with(getApplicationContext()).load(image_profil).into(profil);
                            }
                        }else {
                            String error=task.getException().getMessage();
                            Toast.makeText ( getApplicationContext (), error, Toast.LENGTH_LONG ).show ();

                        }
                    }
                });
            }else{
                Picasso.with ( getApplicationContext () ).load (diplayAllChat.getImage_profil ()  ).into ( profil );
            }

            chat_card.setOnClickListener ( new View.OnClickListener () {
                @Override
                public void onClick(View v) {
                    if (diplayAllChat.getId_recepteur ().equals ( current_user )){
                        Intent chatOne =new Intent ( getApplicationContext (),MessageActivity.class );
                        chatOne.putExtra ( "id de l'utilisateur" ,diplayAllChat.getId_expediteur () );
                        startActivity ( chatOne );
                    }else{
                        Intent chat =new Intent ( getApplicationContext (),MessageActivity.class );
                        chat.putExtra ( "id de l'utilisateur" ,diplayAllChat.getId_recepteur () );
                        startActivity ( chat );
                    }
                }
            } );
        }

        @Override
        public int getLayout() {
            return R.layout.item_contact_chat;
        }
    }
}
