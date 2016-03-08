package me.joni.util;

import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import me.joni.util.log.CommonLogger;

public class FileUtils {

    private static final String TAG = FileUtils.class.getSimpleName();
    private static final int IO_BUFFER_SIZE = 8 * 1024;

    public static boolean exists(String path) {
        boolean isExisting = false;
        if (!TextUtils.isEmpty(path)) {
            try {
                File target = new File(path);
                if (target.exists() && target.canRead() && !target.isDirectory()) {
                    isExisting = true;
                }
            } catch (Exception e) {
                CommonLogger.d(TAG, e.getCause().toString());
            }
        }
        return isExisting;
    }

    public static boolean delete(String path) {
        boolean result = false;
        if (!TextUtils.isEmpty(path)) {
            try {
                File target = new File(path);
                if (target.exists() && !target.isDirectory()) {
                    result = target.delete();
                }
            } catch (Exception e) {
                CommonLogger.w(TAG, "Error on deleting file: " + path, e);
            }
        }
        return result;
    }

    public static boolean deleteIfExists(String path) {
        return exists(path) && delete(path);
    }

    public static final void download(String urlString, String dest) throws IOException {
        InputStream in = null;
        OutputStream out = null;
        URLConnection conn = null;
        try {
            final URL url = new URL(urlString);
            conn = url.openConnection();
            conn.setConnectTimeout(4000);
            conn.setReadTimeout(10000);
            in = new BufferedInputStream(conn.getInputStream(), IO_BUFFER_SIZE);
            out = new BufferedOutputStream(new FileOutputStream(dest));
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                CommonLogger.d(TAG, "Failed close Input/Output streams: " + e.getMessage() + ", doesn't matter.");
            }
        }
    }

    public static final long getFileSize(String path) {
        long filesize = 0;
        if (TextUtils.isEmpty(path)) {
            return filesize;
        }
        File file = new File(path);
        if (file.exists() && !file.isDirectory()) {
            filesize = file.length();
        }
        return filesize;
    }
}
