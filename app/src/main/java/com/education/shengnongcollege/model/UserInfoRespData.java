package com.education.shengnongcollege.model;

/**
 * Created by yangyikun on 18/6/4.
 */

public class UserInfoRespData extends RespDataBase {

    /**
     "UserId": "a9962201-0e0d-40f5-ae9c-fafcc15f63ac",
     "Mobile": "18406506300",
     "Password": "07cf500c37fd52fd5c48211ce928a71a",
     "RealName": null,//真实姓名
     "Integral": null,//积分
     "DeleteMark": null,//删除标识 1-删除；0-未删除
     "CreateDate": "2018-05-13T18:33:01.137",
     "LastVisit": null,
     "Photograph": null //Photograph
     */

    private String UserId;
    private String Mobile;
    private String Password;
    private String RealName;
    private int Integral;
    private int DeleteMark;
    private String CreateDate;
    private String LastVisit;
    private String Photograph;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getRealName() {
        return RealName;
    }

    public void setRealName(String realName) {
        RealName = realName;
    }

    public int getIntegral() {
        return Integral;
    }

    public void setIntegral(int integral) {
        Integral = integral;
    }

    public int getDeleteMark() {
        return DeleteMark;
    }

    public void setDeleteMark(int deleteMark) {
        DeleteMark = deleteMark;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public String getLastVisit() {
        return LastVisit;
    }

    public void setLastVisit(String lastVisit) {
        LastVisit = lastVisit;
    }

    public String getPhotograph() {
        return Photograph;
    }

    public void setPhotograph(String photograph) {
        Photograph = photograph;
    }
}
