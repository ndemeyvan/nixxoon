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

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    private Context context;
    String current_user;
    final int MSG_TYPE_RIGHT=1;
    final int MSG_TYPE_LEFT=0;
        List<ModelChat> modelChatList;
        String imageUrl;

    public ChatAdapter(Context context, List<ModelChat> modelChatList, String imageUrl) {
        this.context = context;
        this.modelChatList = modelChatList;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        viewGroup.getContext();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        if(i==MSG_TYPE_RIGHT){
            View v=LayoutInflater.from ( viewGroup.getContext () ).inflate ( R.layout.right_item_chat ,viewGroup,false);
            return new ViewHolder ( v );

        }else{
            View v=LayoutInflater.from ( viewGroup.getContext () ).inflate ( R.layout.left_item_chat ,viewGroup,false);
            return new ViewHolder ( v );
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        ModelChat modelChat=modelChatList.get ( i );
        viewHolder.message.setText ( modelChat.getMessage () );
        firebaseFirestore.collection("mes donnees utilisateur").document(modelChat.getRecepteur ()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot> () {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult ().exists ()){
                        Picasso.with(context).load(imageUrl).into(viewHolder.image);
                        // sendMessage(current_user,user_id_message,image_user);
                    }
                }else {
                    String error=task.getException().getMessage();
                    Toast.makeText (context, error, Toast.LENGTH_LONG ).show ();

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelChatList.size ();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView message;
        CircleImageView image;

        public ViewHolder(@NonNull View itemView) {
            super ( itemView );
            message=itemView.findViewById ( R.id.show_message );
            image=itemView.findViewById ( R.id.chat_imag_item );
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseAuth=FirebaseAuth.getInstance ();
         current_user=firebaseAuth.getCurrentUser ().getUid ();
         if (modelChatList.get ( position ).getExpediteur ().equals ( current_user )){
             return MSG_TYPE_RIGHT;
         }else{
             return MSG_TYPE_LEFT;
         }

    }
}
