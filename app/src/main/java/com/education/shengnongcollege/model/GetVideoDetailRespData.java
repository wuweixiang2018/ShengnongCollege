package com.education.shengnongcollege.model;

/**
 * Created by wuweixiang on 18/6/5.
 */

public class GetVideoDetailRespData extends RespDataBase {

    /**
     * Id : 0e9e3d1f-4644-4444-bba8-91e577bb1a42
     * Title : null
     * CategoryId : 6e1e59db-05b5-45e7-a14d-8844e5247814
     * VideoUrl : http://1254977157.vod2.myqcloud.com/86e2410fvodgzp1254977157/0c8cf6777447398156428336855/ezY8NuD0CQ0A.mp4
     * VideoId : 7447398156428336855
     * CoverUrl : http://1254977157.vod2.myqcloud.com/86e2410fvodgzp1254977157/0c8cf6777447398156428336855/7447398156428336856.jpg
     * CreateTime : 2018-06-05T16:02:22.44
     * Profile:描述
     */

    private String Id;
    private String Title;
    private String CategoryId;
    private String VideoUrl;
    private String VideoId;
    private String CoverUrl;
    private String CreateTime;
    private String Profile;

    public String getProfile() {
        return Profile;
    }

    public void setProfile(String profile) {
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

    public void setCategoryId(String CategoryId) {
        this.CategoryId = CategoryId;
    }

    public String getVideoUrl() {
        return VideoUrl;
    }

    public void setVideoUrl(String VideoUrl) {
        this.VideoUrl = VideoUrl;
    }

    public String getVideoId() {
        return VideoId;
    }

    public void setVideoId(String VideoId) {
        this.VideoId = VideoId;
    }

    public String getCoverUrl() {
        return CoverUrl;
    }

    public void setCoverUrl(String CoverUrl) {
        this.CoverUrl = CoverUrl;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }
}
