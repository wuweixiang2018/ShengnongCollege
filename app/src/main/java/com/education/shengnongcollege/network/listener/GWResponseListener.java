package com.education.shengnongcollege.network.listener;


import java.io.Serializable;

/**
 * Created by wuweixiang on 17/7/26.
 */

public interface GWResponseListener {
    /**
     * 成功
     */
    int RESULT_SUCCESS_CODE = 0;
    /**
     * 其他失败
     */
    int RESULT_OTHER_ERROR_CODE = 1;
    /**
     * 没有网络
     */
    int RESULT_NOT_NETWORK_CODE = 2;

    /**
     * 成功结果
     *
     * @param result
     * @param path
     * @param requestCode 用于区别相同接口的不同场景,默认值为0
     * @param resultCode  结果码,一般传RESULT_SUCCESS_CODE
     */
    void successResult(Serializable result, String path, int requestCode, int resultCode);

    /**
     * 失败结果
     *
     * @param result
     * @param path
     * @param requestCode 用于区别相同接口的不同场景,默认值为0
     * @param resultCode  结果码,区别不同的失败原因
     */
    void errorResult(Serializable result, String path, int requestCode, int resultCode);
}
