package com.education.shengnongcollege.model;

/**
 * Created by wuweixiang on 18/6/3.
 */

public class GetVideoListRespData extends RespDataBase {

    /**
     * Id : 1ba291a2-753f-4d00-8452-31ac20e41e52
     * Title : 第一个测试视频
     * CategoryId : 6e1e59db-05b5-45e7-a14d-8844e5247814
     * VideoUrl : http://localhost:52117/swagger/ui/index#/LiveBroadCast/LiveBroadCast_GetCategoryList
     * VideoId : null
     * CoverUrl : null
     * CreateTime : 2018-04-23T18:01:26.273
     * Profile : null 简介 描述
     */

    private String Id;
    private String Title;
    private String CategoryId;
    private String VideoUrl;
    private String VideoId;
    private String CoverUrl;
    private String CreateTime;
    private Object Profile;

    public Object getProfile() {
        return Profile;
    }

    public void setProfile(Object profile) {
        Profile = profile;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

    public String getVideoUrl() {
        return VideoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        VideoUrl = videoUrl;
    }

    public String getVideoId() {
        return VideoId;
    }

    public void setVideoId(String videoId) {
        VideoId = videoId;
    }

    public String getCoverUrl() {
        return CoverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        CoverUrl = coverUrl;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }
}
