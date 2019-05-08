package cm.studio.devbee.communitymarket;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;

import cm.studio.devbee.communitymarket.messagerie.ChatMessageActivity;
import cm.studio.devbee.communitymarket.messagerie.MessageActivity;

public class MessageService extends Service {
    public MessageService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(getApplicationContext());
        NotificationCompat.Builder builder= new NotificationCompat.Builder(getApplicationContext());
        builder.setContentTitle("nouveaux message");
        builder.setSmallIcon(R.drawable.ic_action_message);
        Intent gotohome= new Intent(getApplicationContext(),ChatMessageActivity.class);
        PendingIntent pendingIntent= PendingIntent.getActivity(getApplicationContext(),1,gotohome,0);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setColor(Color.BLACK);
        notificationManagerCompat.notify(1,builder.build());

    }



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
