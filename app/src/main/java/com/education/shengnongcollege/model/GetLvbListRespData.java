package com.education.shengnongcollege.model;

/**
 * Created by wuweixiang on 18/6/5.
 */

public class GetLvbListRespData extends RespDataBase {

    /**
     * {
     * "UserId": "108fddbd-cc72-4471-81b9-7391d61943e0",
     * "PushUrl": "rtmp://21696.livepush.myqcloud.com/live/21696_108fddbdcc?bizid=21696&txSecret=a07777d81dbfcb306169b1963d958a83&txTime=5B19890F",
     * "Title": null,
     * "CoverPhotoUrl": null,
     * "State": 2, 1 等待开播 2 直播中 3 直播已结束
     * "Audience": 0,
     * "Fabulous": 0
     * "RoomNo":9867767
     * },
     */

    private String UserId;
    private String Title;
    private String CoverPhotoUrl;
    private String PushUrl;
    private String PlayUrl;
    private int State;
    private int Audience;
    private int Fabulous;
    private int DeleteMark;
    private String CreateTime;
    private String RoomNo;

    public String getRoomNo() {
        return RoomNo;
    }

    public void setRoomNo(String roomNo) {
        RoomNo = roomNo;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getCoverPhotoUrl() {
        return CoverPhotoUrl;
    }

    public void setCoverPhotoUrl(String coverPhotoUrl) {
        CoverPhotoUrl = coverPhotoUrl;
    }

    public String getPlayUrl() {
        return PlayUrl;
    }

    public void setPlayUrl(String playUrl) {
        PlayUrl = playUrl;
    }

    public String getPushUrl() {
        return PushUrl;
    }

    public void setPushUrl(String pushUrl) {
        PushUrl = pushUrl;
    }

    public int getState() {
        return State;
    }

    public void setState(int state) {
        State = state;
    }

    public int getAudience() {
        return Audience;
    }

    public void setAudience(int audience) {
        Audience = audience;
    }

    public int getFabulous() {
        return Fabulous;
    }

    public void setFabulous(int fabulous) {
        Fabulous = fabulous;
    }

    public int getDeleteMark() {
        return DeleteMark;
    }

    public void setDeleteMark(int deleteMark) {
        DeleteMark = deleteMark;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }
}
