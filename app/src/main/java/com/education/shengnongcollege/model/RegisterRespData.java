package com.education.shengnongcollege.model;

import java.io.Serializable;

/**
 * Created by wuweixiang on 18/6/1.
 * 注册响应Data类
 */

public class RegisterRespData extends RespDataBase {

    /**
     * UserId : a9962201-0e0d-40f5-ae9c-fafcc15f63ac
     * Mobile : 18406506300
     * Password : 07cf500c37fd52fd5c48211ce928a71a
     * RealName : null
     * Integral : null
     * DeleteMark : null
     * CreateDate : 2018-05-13T18:33:01.137
     * LastVisit : null
     * Photograph : null
     */

    private String UserId;
    private String Mobile;
    private String Password;
    private Object RealName;
    private Object Integral;
    private Object DeleteMark;
    private String CreateDate;
    private Object LastVisit;
    private Object Photograph;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String Mobile) {
        this.Mobile = Mobile;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public Object getRealName() {
        return RealName;
    }

    public void setRealName(Object RealName) {
        this.RealName = RealName;
    }

    public Object getIntegral() {
        return Integral;
    }

    public void setIntegral(Object Integral) {
        this.Integral = Integral;
    }

    public Object getDeleteMark() {
        return DeleteMark;
    }

    public void setDeleteMark(Object DeleteMark) {
        this.DeleteMark = DeleteMark;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String CreateDate) {
        this.CreateDate = CreateDate;
    }

    public Object getLastVisit() {
        return LastVisit;
    }

    public void setLastVisit(Object LastVisit) {
        this.LastVisit = LastVisit;
    }

    public Object getPhotograph() {
        return Photograph;
    }

    public void setPhotograph(Object Photograph) {
        this.Photograph = Photograph;
    }
}
