package com.education.shengnongcollege.network;

import com.education.shengnongcollege.MyApp;
import com.education.shengnongcollege.network.model.HttpMethodType;
import com.education.shengnongcollege.network.model.ListResponseResult;
import com.education.shengnongcollege.network.model.ResponseResult;
import com.education.shengnongcollege.network.subscribers.GWApiSubscriber;
import com.education.shengnongcollege.network.utils.RetrofitUtil;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by tom on 2017/4/25.
 */

public class CommonGWService {

//    public enum HttpMethodType {GET, POST, PUT, DELETE}

    private static ICommonPTService commonPTService = new RetrofitUtil(MyApp.context).getGWRetrofit().create(ICommonPTService.class);

    public static <K, V> Disposable commonRequest(HttpMethodType methodType,
                                                  GWApiSubscriber<ResponseResult<K, V>> subscriber,
                                                  Class<K> kClass,
                                                  Class<V> vClass,
                                                  String apiPath,
                                                  Map<String, String> headMap,
                                                  Map<String, String> queryMap,
                                                  Map<String, Object> bodyMap) {
        return commonRequest(methodType, subscriber, null, kClass, vClass, apiPath,
                headMap, queryMap, bodyMap);
    }

    public static <K, V> Disposable commonListRequest(HttpMethodType methodType,
                                                      GWApiSubscriber<ListResponseResult<K, V>> subscriberlist,
                                                      Class<K> kClass,
                                                      Class<V> vClass,
                                                      String apiPath,
                                                      Map<String, String> headMap,
                                                      Map<String, String> queryMap,
                                                      Map<String, Object> bodyMap) {
        return commonRequest(methodType, null, subscriberlist, kClass, vClass, apiPath,
                headMap, queryMap, bodyMap);
    }

    private static <K, V> Disposable commonRequest(HttpMethodType methodType,
                                                   GWApiSubscriber<ResponseResult<K, V>> subscriber,
                                                   GWApiSubscriber<ListResponseResult<K, V>> subscriberlist,
                                                   Class<K> kClass,
                                                   Class<V> vClass,
                                                   String apiPath,
                                                   Map<String, String> headMap,
                                                   Map<String, String> queryMap,
                                                   Map<String, Object> bodyMap) {
        if (headMap == null) {
            headMap = new HashMap<>();
        }

        switch (methodType) {
            case GET:
                if (queryMap == null) {
                    queryMap = new HashMap<>();
                }
                if (subscriber != null) {
                    return NetworkController.getInstance(MyApp.context)
                            .gwApiCallGenic(kClass, vClass, subscriber, commonPTService.getRquest(apiPath, headMap, queryMap));
                } else {
                    return NetworkController.getInstance(MyApp.context)
                            .gwApiCallGenicList(kClass, vClass, subscriberlist, commonPTService.getRquest(apiPath, headMap, queryMap));
                }

            case POST:
//                if (bodyMap == null) {
//                    bodyMap = new HashMap<>();
//                }

                if (queryMap == null) {
                    queryMap = new HashMap<>();
                }

                Observable<ResponseBody> postObservable;
                if (bodyMap == null) {
                    postObservable = commonPTService.postRquest(apiPath, headMap, queryMap);
                } else {
                    postObservable = commonPTService.postRquest(apiPath, headMap, queryMap, bodyMap);
                }

                if (subscriber != null) {
                    return NetworkController.getInstance(MyApp.context)
                            .gwApiCallGenic(kClass, vClass, subscriber, postObservable);
                } else {
                    return NetworkController.getInstance(MyApp.context)
                            .gwApiCallGenicList(kClass, vClass, subscriberlist, postObservable);
                }

            case PUT:
//                if (bodyMap == null) {
//                    bodyMap = new HashMap<>();
//                }
                if (queryMap == null) {
                    queryMap = new HashMap<>();
                }

                Observable<ResponseBody> putObservable;
                if (bodyMap == null) {
                    putObservable = commonPTService.putRquest(apiPath, headMap, queryMap);
                } else {
                    putObservable = commonPTService.putRquest(apiPath, headMap, queryMap, bodyMap);
                }

                if (subscriber != null) {
                    return NetworkController.getInstance(MyApp.context)
                            .gwApiCallGenic(kClass, vClass, subscriber, putObservable);
                } else {
                    return NetworkController.getInstance(MyApp.context)
                            .gwApiCallGenicList(kClass, vClass, subscriberlist, putObservable);
                }

            case DELETE:
                break;
        }
        return null;
    }

    private interface ICommonPTService {
        @GET
        Observable<ResponseBody> getRquest(@Url String path,
                                           @HeaderMap Map<String, String> headMap,
                                           @QueryMap Map<String, String> map);

        @POST
        Observable<ResponseBody> postRquest(@Url String path,
                                            @HeaderMap Map<String, String> headMap,
                                            @QueryMap Map<String, String> querymap,
                                            @Body Map<String, Object> bodyMap);

        @PUT
        Observable<ResponseBody> putRquest(@Url String path,
                                           @HeaderMap Map<String, String> headMap,
                                           @QueryMap Map<String, String> querymap,
                                           @Body Map<String, Object> bodyMap);

        @POST
        Observable<ResponseBody> postRquest(@Url String path,
                                            @HeaderMap Map<String, String> headMap,
                                            @QueryMap Map<String, String> querymap);

        @PUT
        Observable<ResponseBody> putRquest(@Url String path,
                                           @HeaderMap Map<String, String> headMap,
                                           @QueryMap Map<String, String> querymap);
    }
}
