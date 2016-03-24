package me.joni.util;

import android.app.Application;
import android.util.Log;

import me.joni.util.crash.CrashHandler;
import me.joni.util.log.CommonLogger;
import me.joni.util.log.SimpleLogger;

/**
 * Created by niqiang on 16/3/8.
 */
public class CommonApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().register(this);
        Base.initialize(this);
        CommonLogger.init(this).setDebug(true);
        SimpleLogger.setLogger(BuildConfig.DEBUG, Log.ERROR, "CommonUtils/Log/");
    }
}
