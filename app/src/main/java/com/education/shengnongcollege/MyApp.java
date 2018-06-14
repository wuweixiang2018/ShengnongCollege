package com.education.shengnongcollege;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDexApplication;

/**
 * Created by wuweixiang on 18/6/1.
 */

public class MyApp extends MultiDexApplication {
    public static Context context;
    public static MyApp mApp;

    public static MyApp getInstence() {
        return mApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        mApp = this;
    }
}
