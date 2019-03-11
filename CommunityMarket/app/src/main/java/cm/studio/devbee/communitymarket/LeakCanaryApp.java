package cm.studio.devbee.communitymarket;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

public class LeakCanaryApp extends Application {

    private RefWatcher refWatcher;

   /* public static RefWatcher getRefWatcher(Context context){
        LeakCanaryApp leak = (LeakCanaryApp)context.getApplicationContext();
        return  leak.refWatcher;
    }*/

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        // Normal app init code...
    }
}
