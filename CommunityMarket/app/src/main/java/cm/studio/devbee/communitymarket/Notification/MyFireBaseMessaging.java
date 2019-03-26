package cm.studio.devbee.communitymarket.Notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.media.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import cm.studio.devbee.communitymarket.messagerie.MessageActivity;

public class MyFireBaseMessaging extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived ( remoteMessage );
        String sented =remoteMessage.getData ().get ( "sented" );
        FirebaseAuth firebaseAuth;
        firebaseAuth=FirebaseAuth.getInstance ();
        String current_user=firebaseAuth.getCurrentUser ().getUid ();
        if (current_user!=null && sented.equals ( current_user )){
            sendNotification(remoteMessage);
        }
    }

    private void sendNotification(RemoteMessage remoteMessage) {
        String user=remoteMessage.getData ().get ( "user" );
        String icon=remoteMessage.getData ().get ( "icon" );
        String title=remoteMessage.getData ().get ( "title" );
        String body=remoteMessage.getData ().get ( "body" );
        RemoteMessage.Notification notification =remoteMessage.getNotification ();
        int j=Integer.parseInt ( user.replaceAll (" [\\D]","" ) );
        Intent intent=new Intent ( getApplicationContext (),MessageActivity.class );
        Bundle bundle=new Bundle (  );
        bundle.putString ( "user_id",user );
        intent.putExtras( bundle );
        intent.addFlags ( intent.FLAG_ACTIVITY_CLEAR_TOP );
        PendingIntent pendingIntent=PendingIntent.getActivity ( getApplicationContext (),j,intent,PendingIntent.FLAG_ONE_SHOT );
        Uri defaultSound =RingtoneManager.getDefaultUri ( RingtoneManager.TYPE_NOTIFICATION );
        android.support.v4.app.NotificationCompat.Builder builder=new android.support.v4.app.NotificationCompat.Builder ( getApplicationContext () )
                .setSmallIcon ( Integer.parseInt ( icon ))
                .setContentTitle ( title )
                .setContentText ( body )
                .setAutoCancel ( true )
                .setSound ( defaultSound )
                .setContentIntent ( pendingIntent );
        NotificationManager notificationManager=(NotificationManager)getSystemService ( Context.NOTIFICATION_SERVICE );
        int i=0;
        if (j>0){
            i=j;
        }
        notificationManager.notify ( i,builder.build () );



    }

}
