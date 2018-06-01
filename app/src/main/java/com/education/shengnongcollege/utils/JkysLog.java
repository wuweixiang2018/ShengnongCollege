package com.education.shengnongcollege.utils;

import android.text.TextUtils;
import android.util.Log;

import com.education.shengnongcollege.BuildConfig;

/**
 * Created by wuweixiang on 17/5/25.
 */

public class JkysLog {
    /**
     * 编译类型
     */
//    public static String buildType;
    private static boolean isDebug() {
        String buildType = BuildConfig.BUILD_TYPE;
        if (!TextUtils.isEmpty(buildType) && buildType.equals("mcpRelease")) {
            return false;
        } else {
            return true;
        }
    }

    public static void e(String tag, String msg) {
        if (isDebug())
            Log.e(tag, msg);
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (isDebug())
            Log.e(tag, msg, tr);
    }

    public static void d(String tag, String msg) {
        if (isDebug())
            Log.d(tag, msg);
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (isDebug())
            Log.d(tag, msg, tr);
    }

    public static void i(String tag, String msg) {
        if (isDebug())
            Log.i(tag, msg);
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (isDebug())
            Log.i(tag, msg, tr);
    }

    public static void v(String tag, String msg) {
        if (isDebug())
            Log.v(tag, msg);
    }

    public static void v(String tag, String msg, Throwable tr) {
        if (isDebug())
            Log.v(tag, msg, tr);
    }

    public static void w(String tag, String msg) {
        if (isDebug())
            Log.w(tag, msg);
    }

    public static void w(String tag, String msg, Throwable tr) {
        if (isDebug())
            Log.w(tag, msg, tr);
    }
}
