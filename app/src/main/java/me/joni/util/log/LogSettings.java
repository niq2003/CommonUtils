package me.joni.util.log;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

public class LogSettings {
    private Context mContext;
    /** DEBUG模式. */
    private boolean mIsDebug;
    /** 字符集. */
    private String mCharset;
    /** 时间格式. */
    private String mTimeFormat;
    /** 日志保存的目录. */
    private String mLogDir;
    /** 日志文件的前缀. */
    private String mLogPrefix;
    /** 日志是否记录到文件中. */
    private boolean mWriteToFile;
    /** 写入文件的日志级别. */
    private int mLogLevelForFile;
    /** 日志保留到天数,不设置则不清除日志. */
    private int mLogKeepDays;

    public LogSettings() {
        mIsDebug = true;
        mCharset = "UTF-8";
        mTimeFormat = "yyyy-MM-dd hh:mm:ss.SSS";
        mLogDir = "logs";
        mLogPrefix = "";
        mWriteToFile = true;
        mLogLevelForFile = Log.ERROR;
    }

    public Context getContext() {
        return mContext;
    }

    public LogSettings setContext(@NonNull Context context) {
        mContext = context;
        return this;
    }

    public String getCharset() {
        return mCharset;
    }

    public LogSettings setCharset(@NonNull String charset) {
        mCharset = charset;
        return this;
    }

    public String getTimeFormat() {
        return mTimeFormat;
    }

    public LogSettings setTimeFormat(@NonNull String timeFormat) {
        mTimeFormat = timeFormat;
        return this;
    }

    public String getLogDir() {
        return mLogDir;
    }

    public LogSettings setLogDir(@NonNull String logDir) {
        mLogDir = logDir;
        return this;
    }

    public String getLogPrefix() {
        return mLogPrefix;
    }

    public LogSettings setLogPrefix(@NonNull String logPrefix) {
        mLogPrefix = logPrefix;
        return this;
    }

    public boolean isWriteToFile() {
        return mWriteToFile;
    }

    public LogSettings writeToFile(boolean isWriteToFile) {
        mWriteToFile = isWriteToFile;
        return this;
    }

    public int getLogLevelForFile() {
        return mLogLevelForFile;
    }

    public LogSettings setLogLevelForFile(int logLevel) {
        mLogLevelForFile = logLevel;
        return this;
    }

    public boolean isDebug() {
        return mIsDebug;
    }

    public LogSettings setDebug(boolean isDebug) {
        mIsDebug = isDebug;
        return this;
    }

    public void setLogKeepDays(int days) {
        mLogKeepDays = days;
    }

    public int getLogKeepDays() {
        return mLogKeepDays;
    }
}
