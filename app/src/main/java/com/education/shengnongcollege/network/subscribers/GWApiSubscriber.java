package com.education.shengnongcollege.network.subscribers;

import android.util.Log;
import android.widget.Toast;

import com.education.shengnongcollege.network.exce.GWApiException;
import com.education.shengnongcollege.network.model.ResponseResult;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by tom on 2017/4/17.
 */

public abstract class GWApiSubscriber<T> implements Observer<T> {
    /**
     * result 返回null的状态，如果正常业务逻辑，result会返回null，那么需要在onError里面处理下
     */
    public static final int RESULT_NULL_CODE = -1;

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onNext(T responseResult) {

    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof GWApiException) {
            final GWApiException gwApiException = (GWApiException) e;
            switch (gwApiException.getErrorCode()) {
                case RESULT_NULL_CODE:
                    //result节点为null
                    break;

                default:


                    break;
            }
        } else {


        }
    }
}
