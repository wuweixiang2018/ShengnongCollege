package com.education.shengnongcollege;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.LinkedList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by wuweixiang on 16/8/24.
 * 最顶层activity，放置所有activity的公用处理
 */
public abstract class BaseTopActivity extends AppCompatActivity {


    protected Context context;
    protected boolean isMIUI = true;

    protected Context getContext() {
        return context;
    }

    public static List<Activity> getsActivities() {
        return sActivities;
    }

    public static List<Activity> sActivities = new LinkedList<Activity>();

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            String FRAGMENTS_TAG = "android:support:fragments";
            savedInstanceState.remove(FRAGMENTS_TAG);
        }
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        sActivities.add(this);
    }

    @Override
    protected void onDestroy() {
        sActivities.remove(this);
        clearDispose();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    public static Activity getTopActivity() {
        if (sActivities.size() > 0) {
            return sActivities.get(sActivities.size() - 1);
        } else {
            return null;
        }
    }

    public static void finishAll() {
        for (int i = sActivities.size() - 1; i >= 0; i--) {
            Activity a = sActivities.get(i);
            if (a instanceof Activity && !a.isFinishing()) {
//                sActivities.remove(i);
                a.finish();
                a.overridePendingTransition(0, 0);
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //为了处理某些场景(比如长期放置)被回收的时候,重新启动app使用,暂时去掉
//        try {
//            startActivity(new Intent(this, Class.forName("com.jkyshealth.activity.other.NewHelloActivity")));
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//            try {
//                startActivity(new Intent(this, Class.forName("com.mintcode.area_patient.area_login.HelloActivity")));
//            } catch (ClassNotFoundException e1) {
//                e1.printStackTrace();
//            }
//        }
//        finish();
    }

    protected CompositeDisposable compositeDisposable;

    protected synchronized void addRequestDispose(Disposable disposable) {

        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        if (disposable != null) {
            compositeDisposable.add(disposable);
        }
    }

    protected synchronized void clearDispose() {
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }
}
