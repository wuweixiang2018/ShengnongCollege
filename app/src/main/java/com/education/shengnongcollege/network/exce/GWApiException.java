package com.education.shengnongcollege.network.exce;

import com.education.shengnongcollege.network.model.ResponseError;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 */
public class GWApiException extends RuntimeException {

    private int errorCode;
    private String errorMsg;

    public GWApiException(ResponseError errorBean) {
        super(errorBean != null ? "" + errorBean.getMessage() : "网络错误");
        if (errorBean != null) {
            errorCode = errorBean.getCode();
            errorMsg = errorBean.getMessage();

        } else {
            errorCode = 0;
            errorMsg = "网络错误";
        }

    }

    @Override
    public String getMessage() {
        return errorMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}