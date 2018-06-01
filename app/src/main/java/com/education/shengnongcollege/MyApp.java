package com.education.shengnongcollege;

import android.app.Application;
import android.content.Context;

/**
 * Created by wuweixiang on 18/6/1.
 */

public class MyApp extends Application {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
