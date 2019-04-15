package cm.studio.devbee.communitymarket.Fragments;

import cm.studio.devbee.communitymarket.notification.MyResponse;
import cm.studio.devbee.communitymarket.notification.Sender;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers ( {
            "Content-Type:application/json",
            "Authorization:key=AAAAfR8BveM:APA91bEgCOmnLz5LKrc4ms8qvCBYqAUwbXpoMswSYyuQJT0cg3FpLSvH-S_nAiaCARSdeolPbGpxTX5nHVm5AP6tI7N9sCYEL4IUkR_eF4lYZXN4oeXhWtCKavTHIaA8pH6eklL4yBO5"
    } )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
