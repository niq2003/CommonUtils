package me.joni.util.log;

import android.content.Context;
import android.util.Log;

/**
 * CommonLogger.init(appContext).setDebug(BuidConfig.DEBUG)...
 */
public final class CommonLogger {

    private static LogSettings mSettings;
    private static LogPrinter mPrinter;

    public static LogSettings init(Context context) {
        mSettings = new LogSettings();
        return mSettings.setContext(context);
    }

    public static LogSettings getSettings() {
        return mSettings;
    }

    public static final void v(String tag, String data) {
        if (mSettings.isDebug()) {
            Log.v(tag, data);
        }
        writeLog(Log.VERBOSE, tag, data);
    }

    public static final void d(String tag, String data) {
        if (mSettings.isDebug()) {
            Log.d(tag, data);
        }
        writeLog(Log.DEBUG, tag, data);
    }

    public static final void i(String tag, String data) {
        if (mSettings.isDebug()) {
            Log.i(tag, data);
        }
        writeLog(Log.INFO, tag, data);
    }

    public static final void w(String tag, String data) {
        if (mSettings.isDebug()) {
            Log.w(tag, data);
        }
        writeLog(Log.WARN, tag, data);
    }

    public static final void w(String tag, String data, Throwable e) {
        if (mSettings.isDebug()) {
            Log.d(tag, data, e);
        }
        writeLog(Log.WARN, tag, data + '\n' + Log.getStackTraceString(e));
    }

    public static final void e(String tag, String data) {
        if (mSettings.isDebug()) {
            Log.e(tag, data);
        }
        writeLog(Log.ERROR, tag, data);
    }

    public static final void e(String tag, String data, Throwable e) {
        if (mSettings.isDebug()) {
            Log.e(tag, data, e);
        }
        writeLog(Log.ERROR, tag, data + '\n' + Log.getStackTraceString(e));
    }

    private static void writeLog(int logLevel, String tag, String msg) {
        if (!mSettings.isWriteToFile()) return;
        if (mPrinter == null) {
            mPrinter = new LogPrinter();
        }
        mPrinter.writeLog(logLevel, tag, msg);
    }
}
