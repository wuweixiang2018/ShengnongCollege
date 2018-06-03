package com.education.shengnongcollege.network.present;

import android.app.Activity;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import java.lang.ref.WeakReference;

/**
 * Created by tom on 2017/4/19.
 */

public class BaseObjectPresent<T> {
    private WeakReference<T> mWref;
    private T tRefObj;

    private boolean isWeakReference(T listener) {
        if (listener instanceof android.support.v4.app.Fragment
                || listener instanceof Fragment
                || listener instanceof Activity
                || listener instanceof FragmentActivity) {
            return true;
        }
        return false;
    }

    public BaseObjectPresent(T t) {
        if (isWeakReference(t)) {
            mWref = new WeakReference<T>(t);
        } else {
            mWref = null;
            tRefObj = t;
        }
    }

    public T getRefObj() {
        if (mWref != null) {
            return mWref.get();
        }
        return tRefObj;
    }

    public boolean isAvaiable() {
        T t;
        if (mWref!=null) {
             t = mWref.get();
        }else {
            t = tRefObj;
        }
        if (t == null) return false;
        if (t instanceof Fragment) {
            if (((Fragment) t).getActivity() == null) {
                return false;
            }
        }
        if (t instanceof android.support.v4.app.Fragment) {
            if (((android.support.v4.app.Fragment) t).getActivity() == null) {
                return false;
            }
        }
        return true;
    }
}
