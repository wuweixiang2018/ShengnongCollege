package com.education.shengnongcollege.network.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wuweixiang on 17/11/1.
 */

public class ListResponseResult<D, O> implements Serializable {

    /**
     * status_code : 200
     * status_message : 操作成功
     * timestamp : 1526261557
     * data : [{"Id":"6e1e59db-05b5-45e7-a14d-8844e5247814","TencentCategoryId":"406523","Name":"演示4","CreateTime":"2018-04-23T16:07:32.207"}]
     * obj : {"TotalCount":1}
     */

    private int status_code;
    private String status_message;
    private int timestamp;
    private O obj;
    private List<D> data;

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

    public O getObj() {
        return obj;
    }

    public void setObj(O obj) {
        this.obj = obj;
    }

    public List<D> getData() {
        return data;
    }

    public void setData(List<D> data) {
        this.data = data;
    }
}
