package cm.studio.devbee.communitymarket.Notification;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFireBaseiDService extends FirebaseInstanceIdService {
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String currnt_user;
    String refreshToken;
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh ();
        firebaseAuth=FirebaseAuth.getInstance ();
        currnt_user=firebaseAuth.getCurrentUser ().getUid ();
        refreshToken= FirebaseInstanceId.getInstance ().getToken ();
        if (currnt_user!=null){
            updateToke(refreshToken);
        }
    }

    private void updateToke(String refreshToken) {
        Token token=new Token ( refreshToken );
        DocumentReference  db=firebaseFirestore.collection ( "Token" ).document ( currnt_user );
        db.set ( token );


    }
}
