package com.education.shengnongcollege.network.utils;

import android.content.Context;

import com.education.shengnongcollege.utils.JkysLog;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
//import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
//import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
//import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wuweixiang on 17/3/9.
 */

public class RetrofitUtil {
    private String baseUrl;
    private static final long DEFAULT_TIMEOUT = 30 * 1000;
    private static long connectTimeout = DEFAULT_TIMEOUT;
    private static long readTimeOut = DEFAULT_TIMEOUT;
    private static long writeTimeout = DEFAULT_TIMEOUT;

    public static final String CONTENTTYPE_UTF8 = "Content-Type-temp:application/json;charset=utf-8";
    public static final String CONTENTTYPE_NOT_CHARSET = "Content-Type-temp:application/json";

    private Context context;

    public static int cerRaw;

    public RetrofitUtil(Context context) {
        this.context = context;
        baseUrl = "http://api.liveeducation.ymstudio.xyz/";
    }

    private static void httpsSSLProcess(Context context, OkHttpClient.Builder builder) {
        try {
            InputStream is = context.getResources().openRawResource(cerRaw);
            HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(new InputStream[]{is}, null, null);
            builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
//                builder.hostnameVerifier(new HostnameVerifier() {
//                    @Override
//                    public boolean verify(String hostname, SSLSession session) {
//                        return true;
//                    }
//                });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static WebSocket startSocketConnect(Context context, String url,
//                                               String host,String origin,String secretKey,
                                               WebSocketListener webSocketListener) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(readTimeOut, TimeUnit.MILLISECONDS)
                .writeTimeout(writeTimeout, TimeUnit.MILLISECONDS);

        if (url.startsWith("https://") || url.startsWith("wss://")) {
            httpsSSLProcess(context, builder);
        }

        OkHttpClient okHttpClient = builder.build();
        Request.Builder builderReq = new Request.Builder().url(url);
        Request request = builderReq.build();

        WebSocket webSocket = okHttpClient.newWebSocket(request, webSocketListener);
        okHttpClient.dispatcher().executorService().shutdown();
        return webSocket;
    }

    public Retrofit getGWRetrofit() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();
                        Request.Builder build = originalRequest.newBuilder();
                        Request request = build
                                .build();
                        JkysLog.d("rx_request", request.toString());
                        Response p = chain.proceed(request);
                        return p;
                    }
                }).connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(readTimeOut, TimeUnit.MILLISECONDS)
                .writeTimeout(writeTimeout, TimeUnit.MILLISECONDS);
//        httpsProcess(builder);

        OkHttpClient okHttpClient = builder.build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().serializeNulls().create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit;
    }

    public Retrofit getRetrofit(String baseUrl) {
        return getRetrofit(baseUrl, true);
    }

    public Retrofit getRetrofit(String baseUrl, boolean isAddNetworkInterceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(readTimeOut, TimeUnit.MILLISECONDS)
                .writeTimeout(writeTimeout, TimeUnit.MILLISECONDS);
        if (isAddNetworkInterceptor) {
            builder.addNetworkInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request originalRequest = chain.request();
                    Request.Builder build = originalRequest.newBuilder();
                    //原始请求里面可以放入定制的header,比如token
                    Request request = build
                            .addHeader("Content-Type", "application/json; charset=UTF-8")
                            .build();
                    return chain.proceed(request);
                }
            });
        }
//        httpsProcess(builder);
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = builder.addInterceptor(logging).build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit;
    }


    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setConnectTimeout(long connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public void setReadTimeOut(long readTimeOut) {
        this.readTimeOut = readTimeOut;
    }

    public void setWriteTimeout(long writeTimeout) {
        this.writeTimeout = writeTimeout;
    }

}
