package cm.studio.devbee.communitymarket;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

public class SendNotif {
    public  SendNotif(String message,String heading,String notificationKey){
        notificationKey="0ee417cd-5789-427c-bea7-8bf2fc90eec1";
      try{
          JSONObject notificatioContent =new JSONObject (
                  "{'contents':{'en' :'" +message+ "'},"+
                  "'include_player_ids':['" + notificationKey + "']," +
                  "'headings':{'en': '" + heading + "'}}");
          OneSignal.postNotification ( notificatioContent,null );
      }catch (JSONException e){
            e.printStackTrace ();
      }
    }

}
