package com.bada.mydemo;

import android.os.Environment;
import android.os.Looper;
import android.text.format.DateFormat;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;

public class DebugUtil {
    public static final String TAG = "DebugUtil";
    public static final boolean DEBUG = true;
    static File mFile = null;

    public static void debug(String tag, String msg) {
        if (DEBUG) {
            if (null == msg)
                msg = "null";
            if (tag == null)
                tag = "null";
            Log.d(tag, msg);
        }
    }

    public static void debug(String msg) {
        if (DEBUG) {
            if (null == msg)
                msg = "null";
            Log.d(TAG, msg);
        }
    }

    public static void error(String tag, String error) {
        if (null == error)
            error = "null";
        Log.e(tag, error);
    }

    public static void error(String error) {
        if (null == error)
            error = "null";
        Log.e(TAG, error);
    }

    public static void e(String error){
        error(error);
    }

    public static void e(String tag, String error) {
        error(tag, error);
    }

    public static void info(String info) {
        if (null == info)
            info = "null";
        Log.i(TAG, info);
    }

    public static void fileLog(String log) {
        try {
            if (log == null || log.length() == 0) {
                return;
            }
            String path = Environment.getExternalStorageDirectory().getPath();
            if (!path.endsWith("/") && !(path.endsWith("\\"))) {
                path += "/";
            }
            path += "GTLog/";

            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            Calendar calendar = Calendar.getInstance();
            String str = "%1$d-%2$d-%3$d";
            str = String.format(str, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));

            path += "GTLog" + str + ".log";
            if (mFile == null || !path.equals(mFile.getAbsolutePath())) {
                mFile = new File(path);

                if (!mFile.exists()) {
                    mFile.createNewFile();
                }
            }

            FileOutputStream os = new FileOutputStream(mFile, true);

            String fullLog = "";

            long sysTime = System.currentTimeMillis();
            CharSequence sysTimeStr = DateFormat.format("kk:mm:ss:", sysTime);
            String timestr = sysTimeStr.toString();
            timestr += (sysTime % 1000);

            fullLog += timestr;

            StackTraceElement[] traceElement = new Exception().getStackTrace();
            if (traceElement != null && traceElement.length >= 2) {
                fullLog += " ";
                fullLog += traceElement[1].toString();
            }


            fullLog += " ";
            fullLog += log;
            fullLog += "\n";

            os.write(fullLog.getBytes());
            os.flush();
            os.close();
        } catch (Exception e) {

        }
    }


    public static void warn(String error) {
        if (error == null)
            error = "null";

        Log.w(TAG, error);
    }

    public static void logThread(String from, long id) {

        if (Thread.currentThread() != Looper.getMainLooper().getThread())
            Thread.currentThread().setName(from);

        id = android.os.Process.myTid();

        info(from + " thread started with id " + id);
    }

    public static void logUserAction(String action, String param) {
        if (action == null)
            action = "null";
        if (param == null)
            param = "null";

        Log.e("UserAction", action + " -- " + param);
    }

    public static void v(String message) {
        if (DEBUG) {
            if (null == message)
                message = "null";
            Log.d(TAG, message);
        }
    }
}

