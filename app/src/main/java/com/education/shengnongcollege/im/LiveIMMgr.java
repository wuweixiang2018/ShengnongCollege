package com.education.shengnongcollege.im;

import android.content.Context;
import android.print.PageRange;

import com.education.shengnongcollege.MyApp;

/**
 * Created by wuweixiang on 18/6/10.
 */

public class LiveIMMgr {
    private IMMessageMgr mIMMessageMgr;
    private static LiveIMMgr instance;

    public static LiveIMMgr getInstance() {
        if (instance == null)
            instance = new LiveIMMgr();
        return instance;
    }

    private LiveIMMgr() {
        mIMMessageMgr = new IMMessageMgr(MyApp.context);
    }

    public IMMessageMgr getIMMessageMgr() {
        return mIMMessageMgr;
    }
}
