package com.education.shengnongcollege.network.present;

import android.app.Activity;
import android.support.v4.app.Fragment;

import java.lang.ref.WeakReference;

/**
 * Created by tom on 2017/4/18.
 */

public class BaseFragmentPresent<T extends Fragment> {
    private WeakReference<T> mfragref;
    public BaseFragmentPresent(T fragment){
        mfragref=new WeakReference<T>(fragment);
    }
    protected boolean isAvaiable(){
        return mfragref.get()!=null &&mfragref.get().getActivity()!=null;
    }

    public WeakReference<T> getMfragref() {
        return mfragref;
    }

    public T getFragment(){
        return mfragref.get();
    }
}
