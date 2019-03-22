package cm.studio.devbee.communitymarket.utilForChat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import java.util.List;
import cm.studio.devbee.communitymarket.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatApdapter  extends RecyclerView.Adapter<ChatApdapter.ViewHolder> {
    List<ModeChat> modeChatList;
    Context context;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    String  imagUrl;
    String current8user;
    final int MSG_TYPE_LEFT=0;
    final int  MSG_TYPE_RIGHT=1;


    public ChatApdapter(List<ModeChat> modeChatList, Context context, String imagUrl) {
        this.modeChatList = modeChatList;
        this.context = context;
        this.imagUrl = imagUrl;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        firebaseAuth=FirebaseAuth.getInstance ();
        current8user=firebaseAuth.getCurrentUser ().getUid ();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        viewGroup.getContext();

        if (i==MSG_TYPE_RIGHT){
            View v=LayoutInflater.from ( viewGroup.getContext () ).inflate (R.layout.right_item_chat ,viewGroup,false);
            return new ViewHolder ( v );
        }else {
            View v=LayoutInflater.from ( viewGroup.getContext () ).inflate (R.layout.left_item_chat ,viewGroup,false);
            return new ViewHolder ( v );
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        String msg=modeChatList.get ( i ).getMessage ();
        viewHolder.setMessage ( msg );
        Picasso.with ( context ).load ( imagUrl ).into ( viewHolder.imag_profil );
       /* firebaseFirestore.collection("mes donnees utilisateur").document(current8user).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult ().exists ()){
                        String image_user=task.getResult ().getString ( "user_profil_image" );
                        viewHolder.setuserData ( image_user );
                        Picasso.with ( context ).load ( image_user ).into ( viewHolder.chat_imag_item_right);
                    }
                }else {
                    String error=task.getException().getMessage();
                    Toast.makeText(context,error,Toast.LENGTH_LONG).show();
                }
            }
        });*/
    }


    @Override
    public int getItemCount() {
        return modeChatList.size ();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView message;
        CircleImageView imag_profil;
       // CircleImageView chat_imag_item_right;

        public ViewHolder(@NonNull View itemView) {
            super ( itemView );
            message=itemView.findViewById ( R.id.show_message );
            imag_profil=itemView.findViewById ( R.id.chat_imag_item);
            //chat_imag_item_right=itemView.findViewById(R.id.chat_imag_item_right);

        }
        public void setMessage(String msg){
            message.setText ( msg );
        }


        public void setuserData(String image){
            Picasso.with(context).load(image).into(imag_profil );
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (modeChatList.get ( position ).getExpediteur ().equals ( current8user )){
            return  MSG_TYPE_RIGHT;
        }else{
            return MSG_TYPE_LEFT;
        }
    }
}

