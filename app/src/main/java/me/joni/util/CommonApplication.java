package me.joni.util;

import android.app.Application;

import me.joni.util.log.CommonLogger;

/**
 * Created by niqiang on 16/3/8.
 */
public class CommonApplication extends Application {
    private static final String TAG = CommonApplication.class.getCanonicalName();
    @Override
    public void onCreate() {
        super.onCreate();
        Base.initialize(this);
        CommonLogger.init(this).setDebug(true);
        CommonLogger.d(TAG, TAG + " oncreate.");
        CommonLogger.e(TAG, "error message");
    }
}
