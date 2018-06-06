package com.education.shengnongcollege.model;

/**
 * Created by wuweixiang on 18/6/5.
 */

public class GetLvbListRespData extends RespDataBase {

    /**
     {
     "UserId": "108fddbd-cc72-4471-81b9-7391d61943e0",
     "PushUrl": "rtmp://21696.livepush.myqcloud.com/live/21696_108fddbdcc?bizid=21696&txSecret=a07777d81dbfcb306169b1963d958a83&txTime=5B19890F",
     "Title": null,
     "CoverPhotoUrl": null,
     "State": 2, 1 等待开播 2 直播中 3 直播已结束
     "Audience": 0,
     "Fabulous": 0
     },
     */

    private String UserId;
    private Object Title;
    private Object CoverPhotoUrl;
    private Object PushUrl;
    private int State;
    private int Audience;
    private int Fabulous;
    private int DeleteMark;
    private String CreateTime;

    public Object getPushUrl() {
        return PushUrl;
    }

    public void setPushUrl(Object pushUrl) {
        PushUrl = pushUrl;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    public Object getTitle() {
        return Title;
    }

    public void setTitle(Object Title) {
        this.Title = Title;
    }

    public Object getCoverPhotoUrl() {
        return CoverPhotoUrl;
    }

    public void setCoverPhotoUrl(Object CoverPhotoUrl) {
        this.CoverPhotoUrl = CoverPhotoUrl;
    }

    public int getState() {
        return State;
    }

    public void setState(int State) {
        this.State = State;
    }

    public int getAudience() {
        return Audience;
    }

    public void setAudience(int Audience) {
        this.Audience = Audience;
    }

    public int getFabulous() {
        return Fabulous;
    }

    public void setFabulous(int Fabulous) {
        this.Fabulous = Fabulous;
    }

    public int getDeleteMark() {
        return DeleteMark;
    }

    public void setDeleteMark(int DeleteMark) {
        this.DeleteMark = DeleteMark;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }
}
