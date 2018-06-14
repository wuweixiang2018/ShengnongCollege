package com.education.shengnongcollege.model;

/**
 * Created by wuweixiang on 18/6/3.
 */

public class GetPushFlowPlayUrlRespData extends RespDataBase {


    /**
     * UserId : 108fddbd-cc72-4471-81b9-7391d61943e0
     * PushUrl : rtmp://21696.livepush.myqcloud.com/live/21696_108fddbdcc?bizid=21696&txSecret=0185cadd1c5577afb870bde9a6bd96cc&txTime=5B1E7483
     * PlayUrl : null
     * RoomNo : 8654187
     * Title : null
     * CoverPhotoUrl : null
     * State : 2
     * Audience : 0
     * Fabulous : 0
     * GroupId : @TGS#aM4ZKJIFP
     */

    private String UserId;
    private String PushUrl;
    private Object PlayUrl;
    private String RoomNo;
    private Object Title;
    private Object CoverPhotoUrl;
    private int State;
    private int Audience;
    private int Fabulous;
    private String GroupId;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    public String getPushUrl() {
        return PushUrl;
    }

    public void setPushUrl(String PushUrl) {
        this.PushUrl = PushUrl;
    }

    public Object getPlayUrl() {
        return PlayUrl;
    }

    public void setPlayUrl(Object PlayUrl) {
        this.PlayUrl = PlayUrl;
    }

    public String getRoomNo() {
        return RoomNo;
    }

    public void setRoomNo(String RoomNo) {
        this.RoomNo = RoomNo;
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

    public String getGroupId() {
        return GroupId;
    }

    public void setGroupId(String GroupId) {
        this.GroupId = GroupId;
    }
}
