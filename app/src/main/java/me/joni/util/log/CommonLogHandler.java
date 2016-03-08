package me.joni.util.log;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import me.joni.util.Base;

public class CommonLogHandler implements CommonLogger.LogHandler {
	
	private static final String LOGFOLDER_NAME = "common";
	private static final int LOG_WRITE_LEVEL = Log.ERROR;
	private static final String mFileNamePrefix = "CN";
	private static final boolean USE_APP_STORAGE = false;

	private String logId;
	private String logPath;
	private FileOutputStream foStream;
	
	public CommonLogHandler() {
		String logFileName = createLogFileName();
		logId = logFileName;
		logPath = createLogFilePath(logFileName);
		foStream = getFileOutputStream(logPath);
	}
	
	public String getLogId() {
		return logId;
	}

	public void writeLog(int logLevel, String msg) {
		if (isSDCardAvailable() && logLevel >= LOG_WRITE_LEVEL) {
			try {
				foStream.write(msg.getBytes());
			} catch (Exception e) {
				foStream = getFileOutputStream(logPath);
			}
		}		
	}

	public void close() {
		try {
			if (foStream != null) {
				foStream.flush();
				foStream.close();
				foStream = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean sameAs(CommonLogger.LogHandler handler) {
		return false;
	}

	public Context getAppContext() {
		return Base.getContext();
	}

	public synchronized static void cleanLogFiles(int days) {
		if (!isSDCardAvailable()) {
			return;
		}
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
	
	private static final File getAppLogDir() {
		File dir = null;
		File appRootDir = null;
		if(USE_APP_STORAGE) {
			appRootDir = Base.getContext().getExternalFilesDir(Environment.DIRECTORY_PODCASTS);
		} else {
			appRootDir = Environment.getExternalStorageDirectory();
		}
		if (appRootDir != null) {
			String stDir = appRootDir.getPath() + File.separator + LOGFOLDER_NAME + File.separator;
			dir = new File(stDir);

			if (!dir.exists()) {
				dir.mkdir();
			}
		}
		return dir;
	}	

	private static final String getAbsouteFilePath(String fileName) {
		return createLogFilePath(fileName);
	}
	
	private static final boolean isSDCardAvailable() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}	

	private static final String createLogFilePath(String fileName) {
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
	
	private static final String createLogFileName() {
		Calendar nowDateCalendar = Calendar.getInstance();
		String logFileNameString = mFileNamePrefix + "-" + nowDateCalendar.get(Calendar.YEAR) + "-"
				+ (nowDateCalendar.get(Calendar.MONTH) + 1) + "-"
				+ nowDateCalendar.get(Calendar.DAY_OF_MONTH) + "-logger.txt";

		return logFileNameString;
	}
	
	private synchronized FileOutputStream getFileOutputStream(String filePath) {	
		FileOutputStream fileOut = null;
		if (isSDCardAvailable()) {
			try {
				File logFile = new File(filePath);
				if (!logFile.exists()) {
					logFile.createNewFile();
				}
				close();	// close existine stream if not null
				fileOut = new FileOutputStream(filePath, true);
			} catch (Exception e) {	
				e.printStackTrace();
			}	
		}
		return fileOut;	
	}

}
