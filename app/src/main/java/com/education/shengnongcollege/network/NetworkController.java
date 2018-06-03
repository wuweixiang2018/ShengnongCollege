package com.education.shengnongcollege.network;

import android.content.Context;

import com.education.shengnongcollege.network.exce.ApiException;
import com.education.shengnongcollege.network.exce.GWApiException;
import com.education.shengnongcollege.network.model.ListResponseResult;
import com.education.shengnongcollege.network.model.ResponseError;
import com.education.shengnongcollege.network.model.ResponseResult;
import com.education.shengnongcollege.network.subscribers.GWApiSubscriber;
import com.education.shengnongcollege.network.utils.NetworkUtil;
import com.education.shengnongcollege.utils.GsonUtil;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by wuweixiang on 16/9/19.
 * 其中D对应data节点，O对应obj节点
 */
public class NetworkController {
    public static final String TAG = NetworkController.class
            .getSimpleName();
    private Context ctx;
    /**
     * 用户可见的code，将code对应的msg提示给用户
     */
    private List<String> userVisibleCode = new ArrayList<>();
    private static NetworkController mInstance;

    public static synchronized NetworkController getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new NetworkController(ctx);
        }

        return mInstance;
    }

    private NetworkController(Context ctx) {
        this.ctx = ctx.getApplicationContext();
        userVisibleCode.add("NOT_FOUND");
    }

//    /**
//     * 普通的api请求
//     *
//     * @param subscriber
//     * @param observableOrg
//     * @param <T>
//     */
//    public <T> Disposable apiCall(Observer<T> subscriber,
//                                  Observable<T> observableOrg) {
//        Observable<T> observable = observeNetStatus(observableOrg);
//        return toSubscribe(observable, subscriber);
//    }
//
//    public <T> Disposable apiCallExecIO(Observer<T> subscriber,
//                                        Observable<T> observableOrg) {
//        Observable<T> observable = observeNetStatus(observableOrg);
//        return toSubscribeExecIO(observable, subscriber);
//    }
//
//    /**
//     * 非网关泛型
//     * 用在院内里面
//     *
//     * @param tClass
//     * @param subscriber
//     * @param observableOrg
//     * @param <T>
//     */
//    public <T> Disposable noGwApiCallGenic(final Class<T> tClass, Observer<T> subscriber,
//                                           Observable<ResponseBody> observableOrg) {
//        Observable<T> observable = observeNetStatus(
//                observableOrg
//                        .observeOn(Schedulers.io())
//                        .map(new Function<ResponseBody, T>() {
//
//                            @Override
//                            public T apply(ResponseBody responseBody) throws Exception {
//                                try {
//                                    return GsonUtil.getCommonGson().fromJson(responseBody.string(), tClass);
//                                } catch (IOException e) {
//                                    throw new RuntimeException();
//                                }
//                            }
//                        }));
//        return toSubscribe(observable, subscriber);
//    }
//
//    /**
//     * 非网关泛型
//     * 在io线程回调
//     * 用在院内里面
//     *
//     * @param tClass
//     * @param subscriber
//     * @param observableOrg
//     * @param <T>
//     */
//    public <T> Disposable noGwApiCallGenicIO(final Class<T> tClass, Observer<T> subscriber,
//                                             Observable<ResponseBody> observableOrg) {
//        Observable<T> observable = observeNetStatus(
//                observableOrg
//                        .observeOn(Schedulers.io())
//                        .map(new Function<ResponseBody, T>() {
//
//                            @Override
//                            public T apply(ResponseBody responseBody) throws Exception {
//                                try {
//                                    return GsonUtil.getCommonGson().fromJson(responseBody.string(), tClass);
//                                } catch (IOException e) {
//                                    throw new RuntimeException();
//                                }
//                            }
//                        }));
//        return toSubscribeExecIO(observable, subscriber);
//    }


    /**
     * 泛型的方式请求网关
     *
     * @param subscriber
     * @param observableOrg
     */
    public <D, O> Disposable gwApiCallGenic(final Class<D> dClass, final Class<O> oClass,
                                            Observer<ResponseResult<D, O>> subscriber,
                                            Observable<ResponseBody> observableOrg) {
        Observable<ResponseResult<D, O>> observable = observeNetStatus(
                observableOrg
                        .observeOn(Schedulers.io())
                        .map(new Function<ResponseBody, ResponseResult<D, O>>() {

                            @Override
                            public ResponseResult<D, O> apply(ResponseBody responseBody) throws Exception {
                                try {
                                    return GsonUtil.getCommonGson().fromJson(responseBody.string(),
                                            TypeToken.getParameterized(ResponseResult.class, dClass, oClass).getType());
                                } catch (IOException e) {
                                    throw new RuntimeException();
                                }
                            }
                        }).map(new GWHttpResultFunc<D, O>()));
        return toSubscribe(observable, subscriber);
    }

    public <D, O> Disposable gwApiCallGenicList(final Class<D> dClass, final Class<O> oClass,
                                                Observer<ListResponseResult<D, O>> subscriber,
                                                Observable<ResponseBody> observableOrg) {
        Observable<ListResponseResult<D, O>> observable = observeNetStatusList(ctx,
                observableOrg
                        .observeOn(Schedulers.io())
                        .map(new Function<ResponseBody, ListResponseResult<D, O>>() {

                            @Override
                            public ListResponseResult<D, O> apply(ResponseBody responseBody) throws Exception {
                                try {
                                    String response = responseBody.string();
                                    return GsonUtil.getCommonGson().fromJson(response,
                                            TypeToken.getParameterized(ListResponseResult.class, dClass, oClass).getType());
                                } catch (IOException e) {
                                    throw new RuntimeException();
                                }
                            }
                        }).map(new GWHttpResultFuncList()));
        return toSubscribe(observable, subscriber);
    }

    //添加线程管理并订阅
    private <T> Disposable toSubscribe(Observable<T> o, final Observer<T> s) {
        DisposableObserver<T> disposableObserver = null;
        if (s instanceof DisposableObserver) {
            disposableObserver = (DisposableObserver<T>) s;
        } else {
            disposableObserver = new DisposableObserver<T>() {
                @Override
                public void onNext(T value) {
                    s.onNext(value);
                }

                @Override
                public void onError(Throwable e) {
                    s.onError(e);
                }

                @Override
                public void onComplete() {
                    s.onComplete();
                }
            };
        }
        return o.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribeWith(disposableObserver);
    }

//    /**
//     * Subscriber在IO线程IO线程执行
//     *
//     * @param o
//     * @param s
//     * @param <T>
//     */
//    private <T> Disposable toSubscribeExecIO(Observable<T> o, final Observer<T> s) {
//        DisposableObserver<T> disposableObserver;
//        if (s instanceof DisposableObserver) {
//            disposableObserver = (DisposableObserver<T>) s;
//        } else {
//            disposableObserver = new DisposableObserver<T>() {
//                @Override
//                public void onNext(T value) {
//                    s.onNext(value);
//                }
//
//                @Override
//                public void onError(Throwable e) {
//                    s.onError(e);
//                }
//
//                @Override
//                public void onComplete() {
//                    s.onComplete();
//                }
//            };
//        }
//        return o.subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(Schedulers.io())
//                .subscribeWith(disposableObserver);
//    }

    private <D, O> Observable<ResponseResult<D, O>> observeNetStatus(final Observable<ResponseResult<D, O>> observable) {
        return Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(ObservableEmitter<String> subscriber) throws Exception {
                boolean isConnected = NetworkUtil.isNetworkAvailable(ctx);
                if (!isConnected) {
                    throw new ApiException(0, "Wi-Fi和移动数据已断开");
                }
                subscriber.onNext("ok");
                subscriber.onComplete();
            }
        }).flatMap(new Function<String, Observable<ResponseResult<D, O>>>() {

            @Override
            public Observable apply(String s) throws Exception {
                return observable;
            }
        });
    }

    public <D, O> Observable<ListResponseResult<D, O>> observeNetStatusList(final Context context,
                                                                            final Observable<ListResponseResult<D, O>> observable) {
        return Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(ObservableEmitter<String> subscriber) throws Exception {
                boolean isConnected = NetworkUtil.isNetworkAvailable(context);
                if (!isConnected) {
                    throw new ApiException(0, "Wi-Fi和移动数据已断开");
                }
                subscriber.onNext("ok");
                subscriber.onComplete();
            }
        }).flatMap(new Function<String, Observable<ListResponseResult<D, O>>>() {

            @Override
            public Observable apply(String s) throws Exception {
                return observable;
            }
        });
    }


    private class GWHttpResultFunc<D, O> implements Function<ResponseResult<D, O>, ResponseResult<D, O>> {

        @Override
        public ResponseResult<D, O> apply(ResponseResult<D, O> httpResult) throws Exception {
            if (httpResult.getStatus_code() != 200) {
                ResponseError error = new ResponseError();
                error.setCode(httpResult.getStatus_code());
                error.setMessage(httpResult.getStatus_message());
                throw new GWApiException(error);
            }
            return httpResult;
        }
    }

    private class GWHttpResultFuncList<D, O> implements Function<ListResponseResult<D, O>, ListResponseResult<D, O>> {


        @Override
        public ListResponseResult<D, O> apply(ListResponseResult<D, O> httpResult) throws Exception {
//            if (httpResult.getStatus_code() != 200) {
//                ResponseError error = new ResponseError();
//                error.setCode(httpResult.getStatus_code());
//                error.setMessage(httpResult.getStatus_message());
//                throw new GWApiException(error);
//            }
            //
            return httpResult;
        }
    }
}
