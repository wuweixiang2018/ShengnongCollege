package com.education.shengnongcollege.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Build;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.Method;
import java.util.UUID;

//import com.jkys.jkyscommon.baseclass.CommonTopActivity;

public class DeviceUtil {
    static float density = 0;
    static float scaledDensity = 0;
    static int screenW;
    static int screenH;
    static int screenRealW;
    static int screenRealH;
    static int statusBarHeight;

    private static String DEVICE_UUID;
    //    public static String DEVICE_NAME;
    private static String VERSION_NAME;
    public static String OS = "Android";

    public static String getDeviceName() {
        return Build.MODEL;
    }

    public static String getOsVer() {
        return Build.VERSION.RELEASE;
    }

    /**
     * e get the application version name.you can edit the version name in
     * AndroidManifest.xml.<br/>
     * example:<br/>
     * android:versionName="0.0.1"
     *
     * @param context the application context
     * @return app version name
     * @author RobinLin
     */
    public static String getAppVer(Context context) {
        if (!TextUtils.isEmpty(VERSION_NAME))
            return VERSION_NAME;
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        VERSION_NAME = versionName;
        return versionName;
    }

    public static String getDeviceUUID(Context context) {
        try {
            if (!TextUtils.isEmpty(DEVICE_UUID)) {
                return DEVICE_UUID;
            }

            DEVICE_UUID = UuidUtils.getUuidFromFile(context, UuidUtils.UUID_FILE_DIR, UuidUtils.UUID_FILE_NAME);
            if (TextUtils.isEmpty(DEVICE_UUID)) {
                DEVICE_UUID = UuidUtils.getUuidFromFile(context, UuidUtils.UUID_HIDE_FILE_DIR, UuidUtils.UUID_FILE_NAME);
            }

            if (TextUtils.isEmpty(DEVICE_UUID)) {
                DEVICE_UUID = UuidUtils.getUuidFromSetting(context);
            }

            if (TextUtils.isEmpty(DEVICE_UUID)) {
                DEVICE_UUID = UuidUtils.getUuidFromSpf(context);
            }

            if (TextUtils.isEmpty(DEVICE_UUID)) {
                DEVICE_UUID = UUID.randomUUID().toString();
            }

            UuidUtils.saveUuidToAllFile(context, DEVICE_UUID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return DEVICE_UUID;
    }

    public static DisplayMetrics getDisplay(Context context) {
        return context.getResources().getDisplayMetrics();
    }

    public static float getDensity(Context context) {
        if (density <= 0)
            density = context.getResources().getDisplayMetrics().density;
        return density;
    }

    public static float getScaledDensity(Context context) {
        if (scaledDensity <= 0) {
            scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        }

        return scaledDensity;
    }

    public static int dp2px(Context context, double dpValue) {
        return (int) (dpValue * getDensity(context) + 0.5f);
    }

    public static float sp2px(Context context, float spValue) {
        float scale = getScaledDensity(context);
        return (spValue * scale + 0.5f);
    }

    public static int dp2sp(Context context, float dpValue) {
        //sp
        float scaledDensity = getScaledDensity(context);
        //dp
        float density = getDensity(context);
        return (int) (dpValue * density / scaledDensity + 0.5f);
    }

    /**
     * 获取屏幕宽度,包括状态栏和底部虚拟工具栏
     *
     * @return
     */
    public static int getRealScreenW(Context context) {
        if (screenRealW <= 0) {
//            WindowManager w = activity.getWindowManager();
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display d = windowManager.getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            d.getMetrics(metrics);
            screenRealW = metrics.widthPixels;
            if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
                try {
                    screenRealW = (Integer) Display.class.getMethod("getRawWidth").invoke(d);
                } catch (Exception ignored) {
                }
            // includes window decorations (statusbar bar/menu bar)
            if (Build.VERSION.SDK_INT >= 17)
                try {
                    Point realSize = new Point();
                    Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
                    screenRealW = realSize.x;
                } catch (Exception ignored) {
                }
        }
        return screenRealW;
    }

    /**
     * 获得屏幕高度,包括状态栏、标题栏和底部虚拟工具栏
     *
     * @return
     */
    public static int getRealScreenH(Context context) {
        if (screenRealH <= 0) {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//            WindowManager w = activity.getWindowManager();
            Display d = windowManager.getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            d.getMetrics(metrics);
            screenRealH = metrics.widthPixels;
            if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
                try {
                    screenRealH = (Integer) Display.class.getMethod("getRawHeight").invoke(d);
                } catch (Exception ignored) {
                }
            // includes window decorations (statusbar bar/menu bar)
            if (Build.VERSION.SDK_INT >= 17)
                try {
                    Point realSize = new Point();
                    Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
                    screenRealH = realSize.y;
                } catch (Exception ignored) {
                }
        }
        return screenRealH;
    }

    /**
     * 获取屏幕宽度(app的)
     *
     * @return
     */
    public static int getScreenW(Context context) {
        if (screenW <= 0) {
            WindowManager mWm = (WindowManager) context.getApplicationContext()
                    .getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dMetrics = new DisplayMetrics();
            mWm.getDefaultDisplay().getMetrics(dMetrics);
            screenW = dMetrics.widthPixels;
        }

        return screenW;
    }

    /**
     * 获得屏幕高度(app的),不包括状态栏、标题栏和底部虚拟工具栏
     *
     * @return
     */
    public static int getScreenH(Context context) {
        if (screenH <= 0) {
            WindowManager wm = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics outMetrics = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(outMetrics);
            screenH = outMetrics.heightPixels;
        }
        return screenH;
    }

    /**
     * 获取 虚拟按键的高度(隐藏标题栏的情况下)
     *
     * @return
     */
    public static int getVirtualToolbarH(Context context) {
        return getRealScreenH(context) - getScreenH(context) - getStatusBarHeight(context);
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        if (statusBarHeight <= 0) {
            //获取status_bar_height资源的ID
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                //根据资源ID获取响应的尺寸值
                statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
            }
        }
        return statusBarHeight;
    }


    public static boolean isScreenOn(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        try {
            if (pm.isScreenOn()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
        return false;
    }

    public static String getSerialNumber() {

        String serial = null;

        try {

            Class<?> c = Class.forName("android.os.SystemProperties");

            Method get = c.getMethod("get", String.class);

            serial = (String) get.invoke(c, "ro.serialno");

        } catch (Exception e) {

            e.printStackTrace();

        }

        return serial;

    }
}
