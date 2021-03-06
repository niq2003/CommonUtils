package me.joni.util.log;

import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by niqiang on 16/3/24.
 */
public class SimpleLogger {

    private static final String TAG = "SimpleLogger";

    private static final int VERBOSE = android.util.Log.VERBOSE;
    private static final int DEBUG = android.util.Log.DEBUG;
    private static final int INFO = android.util.Log.INFO;
    private static final int WARN = android.util.Log.WARN;
    private static final int ERROR = android.util.Log.ERROR;
    private static final int ASSERT = android.util.Log.ASSERT;
    private static final long MAX_LOG_FILE = 1024 * 1024 * 8; //8MB

    private static String LOG_FOLDER = "CommonUtils/Log/";
    private static int sFileLevel = ERROR;
    private static boolean sLoggable = true;
    private static final SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private static final SimpleDateFormat sFormat1 = new SimpleDateFormat("yyyyMMdd");

    private SimpleLogger() {
    }

    private static boolean isFileLog(int logLevel) {
        return logLevel >= sFileLevel;
    }

    /**
     *
     * @param isLoggable set loggable
     * @param fileLevel set log level to write file when level >= fileLevel
     * @param logFolder set log folder to save log
     */
    public static void setLogger(boolean isLoggable, int fileLevel, String logFolder) {
        sLoggable = isLoggable;
        sFileLevel = fileLevel;
        LOG_FOLDER = logFolder;
    }

    public static void setLogger(boolean isLoggable, int fileLevel) {
        setLogger(isLoggable, fileLevel, LOG_FOLDER);
    }

    private static boolean isLoggable() {
        return sLoggable;
    }

    private static String levelToStr(int level) {
        switch (level) {
            case VERBOSE:
                return "V";
            case DEBUG:
                return "D";
            case INFO:
                return "I";
            case WARN:
                return "W";
            case ERROR:
                return "E";
            case ASSERT:
                return "A";
            default:
                return "UNKNOWN";
        }
    }

    private static File getLogFile() {
        File file = new File(Environment.getExternalStorageDirectory(), String.format(LOG_FOLDER + "Log_%s_%s.log", sFormat1.format(new Date()), android.os.Process.myPid()));
        File dir = file.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return file;
    }

    private static HandlerThread sHandlerThread;
    private static Handler sHandler;

    static {
        sHandlerThread = new HandlerThread("SimpleLogger@FileLogThread");
        sHandlerThread.start();
        sHandler = new Handler(sHandlerThread.getLooper());
    }

    private static void logToFile(final int level, final String tag, final String format, final Object[] args, final Throwable tr) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                logToFileInner(level, tag, format, args, tr);
            }
        });
    }

    private static void logToFileInner(int level, String tag, String format, Object[] args, Throwable tr) {
        PrintWriter writer = null;
        try {
            if (!isFileLog(level)) {
                return;
            }

            File logFile = getLogFile();
            if (logFile.length() > MAX_LOG_FILE) {
                logFile.delete();
            }
            writer = new PrintWriter(new FileWriter(logFile, true));
            String msg = String.format(format, args);
            String log = String.format("%s %s-%s %s/%s: %s", sFormat.format(new Date()), Process.myPid(), Process.myUid(), levelToStr(level), tag, msg);
            writer.println(log);
            if (tr != null) {
                tr.printStackTrace(writer);
                writer.println();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (Throwable e) {
                }
            }
        }
    }

    private static void println(final int level, final String tag, final String format, final Object[] args, final Throwable tr) {
        logToFile(level, tag, format, args, tr);
        String message;
        if (args != null && args.length > 0) {
            message = String.format(format, args);
        } else {
            message = format;
        }

        if (tr != null) {
            message += android.util.Log.getStackTraceString(tr);
        }
        android.util.Log.println(level, tag, message);
    }

    public static void v(String tag, String format, Object... args) {
        v(tag, format, null, args);
    }

    public static void v(String tag, String format, Throwable tr, Object... args) {
        if (!isLoggable()) {
            return;
        }

        println(VERBOSE, tag, format, args, tr);
    }


    public static void d(String tag, String format, Object... args) {
        d(tag, format, null, args);
    }

    public static void d(String tag, String format, Throwable tr, Object... args) {
        if (!isLoggable()) {
            return;
        }
        println(DEBUG, tag, format, args, tr);
    }

    public static void i(String tag, String format, Object... args) {
        i(tag, format, null, args);
    }

    public static void i(String tag, String format, Throwable tr, Object... args) {
        if (!isLoggable()) {
            return;
        }
        println(INFO, tag, format, args, tr);
    }

    public static void w(String tag, String format, Object... args) {
        w(tag, format, null, args);
    }

    public static void w(String tag, String format, Throwable tr, Object... args) {
        if (!isLoggable()) {
            return;
        }
        println(WARN, tag, format, args, tr);
    }

    public static void w(String tag, Throwable tr) {
        w(tag, "warn", tr);
    }

    public static void e(String tag, String format, Object... args) {
        e(tag, format, null, args);
    }

    public static void e(String tag, String format, Throwable tr, Object... args) {
        if (!isLoggable()) {
            return;
        }
        println(ERROR, tag, format, args, tr);
    }

    public static void wtf(String tag, String format, Object... args) {
        wtf(tag, format, null, args);
    }

    public static void wtf(String tag, Throwable tr) {
        wtf(tag, "wtf", tr);
    }

    public static void wtf(String tag, String format, Throwable tr, Object... args) {
        println(ASSERT, tag, format, args, tr);
    }
}
