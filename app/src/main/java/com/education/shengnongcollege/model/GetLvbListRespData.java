package com.education.shengnongcollege.model;

/**
 * Created by wuweixiang on 18/6/5.
 */

public class GetLvbListRespData extends RespDataBase {

    /**
     * Id : fc3bcbd3-561d-4721-b13b-efd11b030612
     * UserId : 108fddbd-cc72-4471-81b9-7391d61943e0
     * Title : null
     * CoverPhotoUrl : null
     * State : 2
     * Audience : 0
     * Fabulous : 0
     * DeleteMark : 0
     * CreateTime : 2018-06-04
     */

    private String Id;
    private String UserId;
    private Object Title;
    private Object CoverPhotoUrl;
    private int State;
    private int Audience;
    private int Fabulous;
    private int DeleteMark;
    private String CreateTime;

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
