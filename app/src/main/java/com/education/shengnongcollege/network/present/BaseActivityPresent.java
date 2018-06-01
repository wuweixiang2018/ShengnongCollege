package com.education.shengnongcollege.network.present;

import android.app.Activity;

import java.lang.ref.WeakReference;

/**
 * Created by tom on 2017/4/18.
 */

public class BaseActivityPresent<T extends Activity> {
    private WeakReference<T> mActivityRef;
    public BaseActivityPresent(T activity){
        mActivityRef=new WeakReference<T>(activity);
    }
    protected boolean isAvaiable(){
        return mActivityRef.get()!=null ;
    }

    public WeakReference<T> getMfragref() {
        return mActivityRef;
    }

    public T getActivity(){
        return mActivityRef.get();
    }

}
