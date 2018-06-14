package com.education.shengnongcollege.model;

/**
 * Created by wuweixiang on 18/6/5.
 */

public class GetLvbListRespData extends RespDataBase {


    /**
     * Id : 8bba27ff-8961-4667-b58b-f393e1698a6f
     * UserId : d80221c0-adff-453f-81f9-2d02b97a175f
     * PushUrl : rtmp://21696.livepush.myqcloud.com/live/21696_d80221c0ad?bizid=21696&txSecret=2c23982690d2055efc119ccff097ec42&txTime=5B23DF7E
     * PlayUrl : rtmp://21696.liveplay.myqcloud.com/live/21696_d80221c0ad
     * RoomNo : 6752212
     * Title : null
     * CoverPhotoUrl : null
     * State : 2
     * Audience : 0
     * Fabulous : 0
     * GroupId : @TGS#aJQHRLIFB
     */

    private String Id;
    private String UserId;
    private String PushUrl;
    private String PlayUrl;
    private String RoomNo;
    private Object Title;
    private Object CoverPhotoUrl;
    private int State;
    private int Audience;
    private int Fabulous;
    private String GroupId;

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

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

    public String getPlayUrl() {
        return PlayUrl;
    }

    public void setPlayUrl(String PlayUrl) {
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
