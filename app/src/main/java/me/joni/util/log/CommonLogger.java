package me.joni.util.log;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * CommonLogger.init(appContext).setDebug(BuidConfig.DEBUG)...
 */
public final class CommonLogger {

    private static final String LOG_CLASS_NAME = CommonLogger.class.getName();
    /** 获取TAG的方法名. */
    private static final String LOG_TAG_METHOD_NAME = "getTag";

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

    public static final void v(String data) {
        v(getTag(), data);
    }

    public static final void d(String tag, String data) {
        if (mSettings.isDebug()) {
            Log.d(tag, data);
        }
        writeLog(Log.DEBUG, tag, data);
    }

    public static final void d(String data) {
        d(getTag(), data);
    }

    public static final void i(String tag, String data) {
        if (mSettings.isDebug()) {
            Log.i(tag, data);
        }
        writeLog(Log.INFO, tag, data);
    }

    public static final void i(String data) {
        i(getTag(), data);
    }

    public static final void w(String tag, String data) {
        if (mSettings.isDebug()) {
            Log.w(tag, data);
        }
        writeLog(Log.WARN, tag, data);
    }

    public static final void w(String data) {
        w(getTag(), data);
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

    public static final void e(String data) {
        e(getTag(), data);
    }

    public static final void e(String tag, String data, Throwable e) {
        if (mSettings.isDebug()) {
            Log.e(tag, data, e);
        }
        writeLog(Log.ERROR, tag, data + '\n' + Log.getStackTraceString(e));
    }

    public static final void f(String data) {
        writeLog(LogPrinter.FILE, getTag(), data);
    }

    public static final void f(String tag, String data) {
        writeLog(LogPrinter.FILE, tag, data);
    }

    private static void writeLog(int logLevel, String tag, String msg) {
        if (!mSettings.isWriteToFile()) return;
        if (mPrinter == null) {
            mPrinter = new LogPrinter();
        }
        mPrinter.writeLog(logLevel, tag, msg);
    }

    /**
     * 获取TAG。
     * @return TAG
     */
    private static String getTag() {
        StackTraceElement[] elements = new Throwable().getStackTrace();
        int index = getStackIndex(elements);
        if (index == -1) {
            throw new IllegalStateException("set -keep class *.CommonLogger.** { *; } in your proguard config file");
        }
        StackTraceElement element = elements[index];
        return getSimpleClassName(element.getClassName());
    }

    /**
     * 获取调用日志类输出方法的堆栈元素索引.
     *
     * @param elements 堆栈元素
     * @return 索引位置，-1 - 不可用
     */
    private static int getStackIndex(@NonNull StackTraceElement[] elements) {
        boolean isChecked = false;
        StackTraceElement element;
        for (int i = 0; i < elements.length; i++) {
            element = elements[i];
            if (LOG_CLASS_NAME.equals(element.getClassName())
                    && LOG_TAG_METHOD_NAME.equals(element.getMethodName())) {
                isChecked = true;
            }
            if (isChecked) {
                int index = i + 2;
                if (index < elements.length) {
                    return index;
                }
            }
        }
        return -1;
    }

    /**
     * 获取异常栈信息，不同于Log.getStackTraceString()，该方法不会过滤掉UnknownHostException.
     *
     * @param t {@link Throwable}
     * @return 异常栈里的信息
     */
    private static String getStackTraceString(@NonNull Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }

    public static String getSimpleClassName(@NonNull String className) {
        int lastIndex = className.lastIndexOf(".");
        int index = lastIndex + 1;
        if (lastIndex > 0 && index < className.length()) {
            return className.substring(index);
        }
        return className;
    }
}
