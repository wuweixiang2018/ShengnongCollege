package com.education.shengnongcollege.network.model;

import java.io.Serializable;

/**
 * Created by wuweixiang on 16/9/19.
 */
public class ResponseResult<D, O> implements Serializable {


    /**
     * status_code : 200
     * status_message : 操作成功
     * timestamp : 1526348410
     * data : {"PushUrl":"rtmp://21696.livepush.myqcloud.com/live/21696_a99622010e?bizid=21696&txSecret=2adb2ec58474901db664cd36b7ece943&txTime=5AFBFC7A"}
     * obj : null
     */

    private int status_code;
    private String status_message;
    private int timestamp;
    private D data;
    private O obj;

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public String getStatus_message() {
        return status_message;
    }

    public void setStatus_message(String status_message) {
        this.status_message = status_message;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public D getData() {
        return data;
    }

    public void setData(D data) {
        this.data = data;
    }

    public O getObj() {
        return obj;
    }

    public void setObj(O obj) {
        this.obj = obj;
    }
}
