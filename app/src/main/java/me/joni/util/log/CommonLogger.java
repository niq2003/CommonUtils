package me.joni.util.log;

import android.content.Context;
import android.util.Log;

import java.util.Calendar;

/**
 * add code in application oncreate and onDestroy
 * CommonLogger.registerLogHandler(new CommonLogHandler());
 * CommonLogger.releaseLogHandler();
 */
public final class CommonLogger {

    private static TelephonyInfo mTelephonyInfo;
    private static LogHandler _logHandler;

    public static void registerLogHandler(LogHandler logHandler) {
        if (_logHandler != null && !_logHandler.sameAs(logHandler)) {
            _logHandler.close();
            _logHandler = null;
        }
        mTelephonyInfo = new TelephonyInfo(logHandler.getAppContext());
        _logHandler = logHandler;
    }

    public static void releaseLogHandler() {
        if (_logHandler != null) {
            _logHandler.close();
            _logHandler = null;
        }
    }

    private static String buildLogMsg(long threadId, String logTag, String srcTag, String data) {
        String timeStamp = getTimeStamp();
        String networkType = getNetworkType();
        StringBuffer buffer = new StringBuffer("[")
                .append(logTag)
                .append("] ")
                .append(timeStamp)
                .append(" [")
                .append(formatThreadId(threadId))
                .append("][")
                .append(networkType)
                .append("]\t")
                .append(srcTag)
                .append(":")
                .append(data)
                .append("\r\n");
        return buffer.toString();
    }

    private static String getNetworkType() {
        String networkType = "?";
        if (mTelephonyInfo == null) {
            mTelephonyInfo = new TelephonyInfo(_logHandler.getAppContext());
        } else {
            networkType = mTelephonyInfo.getNetworkState();
        }
        return networkType;
    }

    private static String getTimeStamp() {
        Calendar nowDateCalendar = Calendar.getInstance();
        String miisString = getMilliSecond(nowDateCalendar);
        StringBuffer buffer = new StringBuffer(String.valueOf(nowDateCalendar.get(Calendar.YEAR)))
                .append("-")
                .append(formatTwoDigitInt((nowDateCalendar.get(Calendar.MONTH) + 1)))
                .append("-")
                .append(formatTwoDigitInt(nowDateCalendar.get(Calendar.DAY_OF_MONTH)))
                .append(" ")
                .append(formatTwoDigitInt(nowDateCalendar.get(Calendar.HOUR_OF_DAY)))
                .append(":")
                .append(formatTwoDigitInt(nowDateCalendar.get(Calendar.MINUTE)))
                .append(":")
                .append(formatTwoDigitInt(nowDateCalendar.get(Calendar.SECOND)))
                .append(".")
                .append(miisString);
        return buffer.toString();
    }

    private static String formatThreadId(final long threadId) {
        return String.format("%05d", threadId);
    }

    private static String getMilliSecond(Calendar nowDate) {
        long miis = nowDate.get(Calendar.MILLISECOND);
        return String.format("%03d", miis);
    }

    private static String formatTwoDigitInt(final int value) {
        return String.format("%02d", value);
    }

    public static final void v(String tag, String data) {
        Log.v(tag, data);
        if (_logHandler != null) {
            String msg = buildLogMsg(Thread.currentThread().getId(), "V", tag, data);
            _logHandler.writeLog(Log.VERBOSE, msg);
        }
    }

    public static final void d(String tag, String data) {
        Log.d(tag, data);
        if (_logHandler != null) {
            String msg = buildLogMsg(Thread.currentThread().getId(), "D", tag, data);
            _logHandler.writeLog(Log.DEBUG, msg);
        }
    }

    public static final void i(String tag, String data) {
        Log.i(tag, data);
        if (_logHandler != null) {
            String msg = buildLogMsg(Thread.currentThread().getId(), "I", tag, data);
            _logHandler.writeLog(Log.INFO, msg);
        }
    }

    public static final void w(String tag, String data) {
        Log.w(tag, data);
        if (_logHandler != null) {
            String msg = buildLogMsg(Thread.currentThread().getId(), "W", tag, data);
            _logHandler.writeLog(Log.WARN, msg);
        }
    }

    public static final void w(String tag, String data, Throwable e) {
        Log.w(tag, data, e);
    }

    public static final void e(String tag, String data) {
        Log.e(tag, data);
        if (_logHandler != null) {
            String msg = buildLogMsg(Thread.currentThread().getId(), "E", tag, data);
            _logHandler.writeLog(Log.ERROR, msg);
        }
    }

    public static final void e(String tag, String data, Throwable e) {
        Log.e(tag, data, e);
    }

    public interface LogHandler {

        public String getLogId();

        public void writeLog(int logLevel, String msg);

        public void close();

        public boolean sameAs(LogHandler handler);

        public Context getAppContext();
    }
}
