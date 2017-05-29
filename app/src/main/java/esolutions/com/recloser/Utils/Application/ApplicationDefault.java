package esolutions.com.recloser.Utils.Application;

import android.app.Application;
import android.content.Context;

import esolutions.com.recloser.R;
import esolutions.com.recloser.Utils.Class.Define;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * lưu ý thẻ
 * <application
 * android:name=".Utils.Class.ApplicationDefault"
 * Created by VinhNB on 2/28/2017.
 */

public class ApplicationDefault extends Application {
    private static ApplicationDefault sInstance;
    private static Context sContext;


    public static synchronized ApplicationDefault getInstance() {
        if (sInstance == null) {
            sInstance = new ApplicationDefault();
        }
        return sInstance;
    }

    public static Context getContext(){
        return sContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(Define.TYPE_FONT.IOS_VNI.getPathFont())
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
