package com.education.shengnongcollege.model;

/**
 * Created by wuweixiang on 18/6/3.
 */

public class LoginRespData extends RespDataBase {

    /**
     * UserId : a9962201-0e0d-40f5-ae9c-fafcc15f63ac
     * Account : Admin
     * RealName : null
     * Online : 1
     */

    private String UserId;
    private String Account;
    private Object RealName;
    private int Online;
    private int AllowLiveBroadcast;

    public int getAllowLiveBroadcast() {
        return AllowLiveBroadcast;
    }

    public void setAllowLiveBroadcast(int allowLiveBroadcast) {
        AllowLiveBroadcast = allowLiveBroadcast;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    public String getAccount() {
        return Account;
    }

    public void setAccount(String Account) {
        this.Account = Account;
    }

    public Object getRealName() {
        return RealName;
    }

    public void setRealName(Object RealName) {
        this.RealName = RealName;
    }

    public int getOnline() {
        return Online;
    }

    public void setOnline(int Online) {
        this.Online = Online;
    }
}
