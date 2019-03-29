package cm.studio.devbee.communitymarket.notification;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFireBaseidService extends FirebaseInstanceIdService {
    String current_user;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh ();

        firebaseAuth=FirebaseAuth.getInstance ();
        current_user=firebaseAuth.getCurrentUser ().getUid ();
        String refreshToken=FirebaseInstanceId.getInstance ().getToken ();
        if (current_user!=null){
            updateToken(refreshToken);
        }

    }

    private void updateToken(String refreshToken) {
        firebaseAuth=FirebaseAuth.getInstance ();
        current_user=firebaseAuth.getCurrentUser ().getUid ();
        firebaseFirestore=FirebaseFirestore.getInstance ();
        DocumentReference reference=firebaseFirestore.collection ( "Tokens" ).document (current_user);
        Token token=new Token ( refreshToken );
        reference.set ( token );
    }
}
