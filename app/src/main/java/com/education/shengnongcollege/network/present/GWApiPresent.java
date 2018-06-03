package com.education.shengnongcollege.network.present;

import com.education.shengnongcollege.MyApp;
import com.education.shengnongcollege.network.CommonGWService;
import com.education.shengnongcollege.network.exce.GWApiException;
import com.education.shengnongcollege.network.listener.GWResponseListener;
import com.education.shengnongcollege.network.model.HttpMethodType;
import com.education.shengnongcollege.network.model.ListResponseResult;
import com.education.shengnongcollege.network.model.ResponseResult;
import com.education.shengnongcollege.network.subscribers.GWApiSubscriber;
import com.education.shengnongcollege.network.utils.NetworkUtil;
import com.education.shengnongcollege.widget.ZernToast;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by wuweixiang on 17/7/26.
 * 其中K对应data节点，V对应obj节点
 */

public class GWApiPresent extends BaseObjectPresent<GWResponseListener> {

    public GWApiPresent(GWResponseListener apiResponseListener) {
        super(apiResponseListener);
    }

    public <D, O> Disposable commonGet(Map<String, String> queryMap,
                                       Class<D> dClass,
                                       Class<O> oClass,
                                       final String apiPath,
                                       final int requestCode) {
        return commonRequest(HttpMethodType.GET, null, queryMap, null, dClass,
                oClass, apiPath, requestCode);
    }

    public <D, O> Disposable commonPost(Map<String, Object> bodyMap,
                                        Class<D> dClass,
                                        Class<O> oClass,
                                        final String apiPath,
                                        final int requestCode) {
        return commonRequest(HttpMethodType.POST, null, null, bodyMap, dClass,
                oClass, apiPath, requestCode);
    }

    public <D, O> Disposable commonPost(Map<String, String> headMap,
                                        Map<String, Object> bodyMap,
                                        Class<D> dClass,
                                        Class<O> oClass,
                                        final String apiPath,
                                        final int requestCode) {
        return commonRequest(HttpMethodType.POST, headMap, null, bodyMap, dClass,
                oClass, apiPath, requestCode);
    }


    public <D, O> Disposable commonRequest(HttpMethodType methodType,
                                           Map<String, String> headMap,
                                           Map<String, String> queryMap,
                                           Map<String, Object> bodyMap,
                                           Class<D> dClass,
                                           Class<O> oClass,
                                           final String apiPath,
                                           final int requestCode) {
        if (!NetworkUtil.isNetworkAvailable(MyApp.context)) {
            //无网络
            GWResponseListener listener = getRefObj();
            if (listener != null) {
//                ZernToast.cancelToast();
                Observable.just(1).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        ZernToast.showToast(MyApp.context, "网络不可用");
                    }
                });
                listener.errorResult(null, apiPath, requestCode, GWResponseListener.RESULT_NOT_NETWORK_CODE);
            }

            return null;
        }
        GWApiSubscriber<ResponseResult<D, O>> gwApiSubscriber = new GWApiSubscriber<ResponseResult<D, O>>() {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                GWResponseListener listener = getRefObj();
                if (listener != null) {
                    if (e instanceof GWApiException
                            && ((GWApiException) e).getErrorCode() == GWApiSubscriber.RESULT_NULL_CODE) {
                        listener.successResult(null, apiPath, requestCode, GWResponseListener.RESULT_SUCCESS_CODE);
                    } else {
                        int errorcode = GWResponseListener.RESULT_OTHER_ERROR_CODE;
                        if (e instanceof GWApiException) {
                            errorcode = ((GWApiException) e).getErrorCode();
                        }
                        listener.errorResult(null, apiPath, requestCode, errorcode);
                    }

                }
            }

            @Override
            public void onNext(ResponseResult t) {
                super.onNext(t);
                GWResponseListener listener = getRefObj();
                if (listener != null) {
                    listener.successResult(t, apiPath, requestCode, GWResponseListener.RESULT_SUCCESS_CODE);
                }
            }

        };
        return CommonGWService.commonRequest(methodType, gwApiSubscriber, dClass, oClass,
                apiPath, headMap, queryMap, bodyMap);
    }

    public <D, O> Disposable commonListGet(Map<String, String> queryMap,
                                           Class<D> dClass,
                                           Class<O> oClass,
                                           final String apiPath,
                                           final int requestCode) {
        return commonListRequest(HttpMethodType.GET, null, queryMap, null, dClass,
                oClass, apiPath, requestCode);
    }

    public <D, O> Disposable commonListPost(Map<String, String> headMap,
                                            Map<String, Object> bodyMap,
                                            Class<D> dClass,
                                            Class<O> oClass,
                                            final String apiPath,
                                            final int requestCode) {
        return commonListRequest(HttpMethodType.POST, headMap, null, bodyMap, dClass,
                oClass, apiPath, requestCode);
    }

    public <D, O> Disposable commonListPost(Map<String, Object> bodyMap,
                                            Class<D> dClass,
                                            Class<O> oClass,
                                            final String apiPath,
                                            final int requestCode) {
        return commonListRequest(HttpMethodType.POST, null, null, bodyMap, dClass,
                oClass, apiPath, requestCode);
    }


    public <D, O> Disposable commonListRequest(HttpMethodType methodType,
                                               Map<String, String> headMap,
                                               Map<String, String> queryMap,
                                               Map<String, Object> bodyMap,
                                               Class<D> dClass,
                                               Class<O> oClass,
                                               final String apiPath,
                                               final int requestCode) {
        if (!NetworkUtil.isNetworkAvailable(MyApp.context)) {
            //无网络
            GWResponseListener listener = getRefObj();
            if (listener != null) {
//                ZernToast.cancelToast();
                Observable.just(1).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        ZernToast.showToast(MyApp.context, "网络不可用");
                    }
                });
                listener.errorResult(null, apiPath, requestCode, GWResponseListener.RESULT_NOT_NETWORK_CODE);
            }

            return null;
        }
        GWApiSubscriber<ListResponseResult<D, O>> gwApiSubscriber = new GWApiSubscriber<ListResponseResult<D, O>>() {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                GWResponseListener listener = getRefObj();
                if (listener != null) {
                    if (e instanceof GWApiException
                            && ((GWApiException) e).getErrorCode() == GWApiSubscriber.RESULT_NULL_CODE) {
                        listener.successResult(null, apiPath, requestCode, GWResponseListener.RESULT_SUCCESS_CODE);
                    } else {
                        listener.errorResult(null, apiPath, requestCode, GWResponseListener.RESULT_OTHER_ERROR_CODE);
                    }
                }
            }

            @Override
            public void onNext(ListResponseResult<D, O> ts) {
                super.onNext(ts);
                GWResponseListener listener = getRefObj();
                if (listener != null) {
                    listener.successResult(ts, apiPath, requestCode, GWResponseListener.RESULT_SUCCESS_CODE);
                }
            }

        };
        return CommonGWService.commonListRequest(methodType, gwApiSubscriber, dClass, oClass,
                apiPath, headMap, queryMap, bodyMap);
    }
}
