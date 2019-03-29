package cm.studio.devbee.communitymarket.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import cm.studio.devbee.communitymarket.messagerie.MessageActivity;

public class MyFireBaseMessaging extends FirebaseMessagingService {
    String current_user;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived ( remoteMessage );
        String sented=remoteMessage.getData ().get ( "sented" );
        firebaseAuth=FirebaseAuth.getInstance ();
        current_user=firebaseAuth.getCurrentUser ().getUid ();
        firebaseFirestore=FirebaseFirestore.getInstance ();
        DocumentReference reference=firebaseFirestore.collection ( "Tokens" ).document (current_user);
        if (current_user!=null && sented.equals ( current_user )){
            sendNotification(remoteMessage);
        }
    }

    private void sendNotification(RemoteMessage remoteMessage) {
        String user =remoteMessage.getData ().get ( "user" );
        String icon =remoteMessage.getData ().get ( "icon" );
        String title =remoteMessage.getData ().get ( "title" );
        String body =remoteMessage.getData ().get ( "body" );
        RemoteMessage.Notification notification =remoteMessage.getNotification ();
        int j =Integer.parseInt ( user.replaceAll ( "[\\D]","" ) );
        Intent intent =new Intent ( this,MessageActivity.class );
        Bundle bundle=new Bundle (  );
        bundle.putString ( "userid",user );
        intent.putExtras ( bundle );
        intent.addFlags ( Intent.FLAG_ACTIVITY_CLEAR_TOP );
        PendingIntent pendingIntent=PendingIntent.getActivity ( this,j,intent,PendingIntent.FLAG_ONE_SHOT );
        Uri defauldSound =RingtoneManager.getDefaultUri ( RingtoneManager.TYPE_NOTIFICATION );
        NotificationCompat.Builder builder =new NotificationCompat.Builder ( this )
                .setSmallIcon ( Integer.parseInt ( icon ) )
                .setContentTitle ( title )
                .setContentText ( body )
                .setAutoCancel ( true )
                .setSound ( defauldSound )
                .setContentIntent ( pendingIntent );
        NotificationManager noti =(NotificationManager)getSystemService ( Context.NOTIFICATION_SERVICE );
        int i =0;
        if (j>0){
            i=j;
        }
        noti.notify ( i,builder.build () );

    }
}
