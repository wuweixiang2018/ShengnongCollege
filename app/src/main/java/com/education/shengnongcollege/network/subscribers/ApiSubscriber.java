//package com.education.shengnongcollege.network.subscribers;
//
//import android.widget.Toast;
//
//import com.jkys.jkysbase.BaseCommonUtil;
//import com.jkys.jkysbase.ThreadPoolTools;
//import com.jkys.jkysbase.ZernToast;
//import com.jkys.jkysnetwork.exce.ApiException;
//import com.jkys.jkysnetwork.proxy.GwAppProxy;
//
//import io.reactivex.Observer;
//import io.reactivex.disposables.Disposable;
//import io.reactivex.observers.DisposableObserver;
//
///**
// * Created by wuweixiang on 17/11/21.
// * IM使用
// */
//
//public class ApiSubscriber<T> implements Observer<T> {
//
//
//    @Override
//    public void onSubscribe(Disposable d) {
//
//    }
//
//    @Override
//    public void onNext(T t) {
//
//    }
//
//    @Override
//    public void onError(Throwable e) {
//        if (e instanceof ApiException) {
//            final ApiException gwApiException = (ApiException) e;
//            switch (gwApiException.getErrorCode()) {
//                case 101:
//                case 4101: // 对于拉黑的用户需要提示用户登录信息。
//                    if (BaseCommonUtil.isInMainThread())
//                        ZernToast.showToast(GwAppProxy.context, gwApiException.getMessage() + "");
//                    else {
//                        ThreadPoolTools.getInstance().postMainTask(new Runnable() {
//                            @Override
//                            public void run() {
//                                ZernToast.showToast(GwAppProxy.context, gwApiException.getMessage() + "");
//                            }
//                        });
//                    }
//                    break;
//                case 4000:
//                case 4100:
//                case 4001:
//                case 4110:
//                    //借用下网关API的方法
//                    if (BaseCommonUtil.isInMainThread()) {
//                        GwAppProxy.getiGwProxy().forceLogin();
//                        ZernToast.showToast(GwAppProxy.context, gwApiException.getMessage() + "");
//                    } else {
//                        ThreadPoolTools.getInstance().postMainTask(new Runnable() {
//                            @Override
//                            public void run() {
//                                GwAppProxy.getiGwProxy().forceLogin();
//                                ZernToast.showToast(GwAppProxy.context, gwApiException.getMessage() + "");
//                            }
//                        });
//                    }
//
//                    break;
//                case 6300:
//                case 3103:
//                case 2210:
//                    break;
//                case 3600:
//                    //借用下网关API的方法
//                    if (BaseCommonUtil.isInMainThread()) {
//                        if (GwAppProxy.getiGwProxy().getClientType() == 1) {
//                            GwAppProxy.getiGwProxy().forceLogin();
//                            ZernToast.showToast(GwAppProxy.context, "请重新登录");
//                        }
//                    } else {
//                        ThreadPoolTools.getInstance().postMainTask(new Runnable() {
//                            @Override
//                            public void run() {
//                                if (GwAppProxy.getiGwProxy().getClientType() == 1) {
//                                    GwAppProxy.getiGwProxy().forceLogin();
//                                    ZernToast.showToast(GwAppProxy.context, "请重新登录");
//                                }
//                            }
//                        });
//                    }
//
//                    break;
//                default:
//                    if (BaseCommonUtil.isInMainThread()) {
//                        Toast.makeText(GwAppProxy.context, gwApiException.getMessage(), Toast.LENGTH_SHORT).show();
//                    } else {
//                        ThreadPoolTools.getInstance().postMainTask(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(GwAppProxy.context, gwApiException.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//
//                    break;
//            }
//        } else {
//            if (BaseCommonUtil.isInMainThread()) {
//                ZernToast.showToast(GwAppProxy.context, "网络错误");
//            } else {
//                ThreadPoolTools.getInstance().postMainTask(new Runnable() {
//                    @Override
//                    public void run() {
//                        ZernToast.showToast(GwAppProxy.context, "网络错误");
//                    }
//                });
//            }
//
//        }
//    }
//
//    @Override
//    public void onComplete() {
//
//    }
//
//
//}
