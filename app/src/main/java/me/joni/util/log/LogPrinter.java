package me.joni.util.log;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LogPrinter {

    private ExecutorService mExecutorService;
    private TelephonyInfo mTelephonyInfo;
    private LogSettings mLogSettings;
    private String logPath;

    public LogPrinter() {
        mExecutorService = Executors.newSingleThreadExecutor();
        mLogSettings = CommonLogger.getSettings();
        mTelephonyInfo = new TelephonyInfo(mLogSettings.getContext());
        logPath = getLogFilePath(generateLogFileName());
        cleanLogFiles();
    }

    public void writeLog(final int logLevel, final String tag, final String log) {
        if (isSDCardAvailable() && logLevel >= mLogSettings.getLogLevelForFile()) {
            final String msg = buildLogMsg(Thread.currentThread().getId(), logLevel, tag, log);
            mExecutorService.execute(new Runnable() {
                @Override
                public void run() {
                    FileOutputStream fos = null;
                    try {
                        File file = new File(logPath);
                        if (!file.exists()) {
                            file.createNewFile();
                        }
                        fos = new FileOutputStream(file, true);
                        fos.write(msg.getBytes(mLogSettings.getCharset()));
                    } catch (IOException e) {
                        Log.e("FileUtils", e.getMessage());
                    } finally {
                        if (fos != null) {
                            try {
                                fos.close();
                            } catch (IOException e) {
                                Log.e("FileUtils", e.getMessage());
                            }
                        }
                    }
                }
            });
        }
    }

    private synchronized void cleanLogFiles() {
        final int days = mLogSettings.getLogKeepDays();
        if (!isSDCardAvailable() || days <= 0) {
            return;
        }
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                File logDironSD = getAppLogDir();
                if (logDironSD != null && logDironSD.exists()) {
                    File[] files = logDironSD.listFiles();
                    long purgeTime = System.currentTimeMillis() - (days * 24 * 60 * 60 * 1000);
                    for (File file : files) {
                        if (file.lastModified() < purgeTime) {
                            file.delete();
                        }
                    }
                }
            }
        });
    }

    private final File getAppLogDir() {
        File dir = null;
        File appRootDir = Environment.getExternalStorageDirectory();
        if (appRootDir != null) {
            String stDir = appRootDir.getAbsolutePath() + File.separator + mLogSettings.getLogDir() + File.separator;
            dir = new File(stDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }
        return dir;
    }

    private final boolean isSDCardAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    private final String getLogFilePath(String fileName) {
        String logFilePath = null;
        File logDironSD = getAppLogDir();
        if (logDironSD != null) {
            String absDirPath = logDironSD.getAbsolutePath();
            logFilePath = absDirPath.endsWith(File.separator) ? absDirPath + fileName :
                    absDirPath + File.separator + fileName;
            //logFilePath = logDironSD.getAbsolutePath() + File.separator + fileName;
        }
        return logFilePath;
    }

    private final String generateLogFileName() {
        Calendar nowDateCalendar = Calendar.getInstance();
        String logFileNameString = mLogSettings.getLogPrefix() + nowDateCalendar.get(Calendar.YEAR) + "-"
                + (nowDateCalendar.get(Calendar.MONTH) + 1) + "-"
                + nowDateCalendar.get(Calendar.DAY_OF_MONTH) + "-logger.txt";

        return logFileNameString;
    }

    private String buildLogMsg(long threadId, int logLevel, String srcTag, String data) {
        String timeStamp = getTimeStamp();
        String logTag = getLogLevelString(logLevel);
        String networkType = mTelephonyInfo.getCurrentNetworkType();
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

    private String getTimeStamp() {
        Calendar nowDateCalendar = Calendar.getInstance();
        SimpleDateFormat fmt = new SimpleDateFormat(mLogSettings.getTimeFormat());
        String sysDatetime = fmt.format(nowDateCalendar.getTime());
        return sysDatetime;
    }

    private String getLogLevelString(int level) {
        String tag = "";
        switch (level) {
            case Log.ASSERT :
                tag = "ASSERT";
                break;
            case Log.ERROR :
                tag = "ERROR";
                break;
            case Log.WARN :
                tag = "WARNING";
                break;
            case Log.INFO :
                tag = "INFO";
                break;
            case Log.DEBUG :
                tag = "DEBUG";
                break;
            case Log.VERBOSE :
                tag = "VERBOSE";
                break;
            default :
                break;
        }
        return tag;
    }

    private String formatThreadId(final long threadId) {
        return String.format("%05d", threadId);
    }
}
