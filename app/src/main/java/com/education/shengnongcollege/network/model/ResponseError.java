package com.education.shengnongcollege.network.model;

import java.io.Serializable;

/**
 * Created by wuweixiang on 16/9/23.
 */
public class ResponseError implements Serializable {
    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
