package com.education.shengnongcollege.model;

/**
 * Created by wuweixiang on 18/6/3.
 */

public class GetNoticeListRespData extends RespDataBase {

    /**
     {
     "Id": "2ce22cdc-ea57-4c19-bae6-b9b9e07b085c",
     "Type": 0,
     "Title": "热烈欢迎【水墨书法】入驻神农学院",
     "CoverPhoto": "http://localhost:8098/文件的宽高不符合标准,请重新选择",
     "Content": "%3Cp%3E%3Cimg%20src%3D%22/Content/Scripts/ueditor1_4_3_3/net/upload/image/20180612/6366439522291620444184436.png%22%20title%3D%22%u5FAE%u4FE1%u622A%u56FE_20180313111432.png%22%20alt%3D%22%u5FAE%u4FE1%u622A%u56FE_20180313111432.png%22/%3E%3C/p%3E",
     "ReadRate": 1,
     "RealeaseUserId": null,
     "RealeaseDate": "2018-06-12",
     "IsTop": 0,
     "DeleteMark": 0
     }
     */

    private String Id;
    private String Type;
    private String Title;
    private String CoverPhoto;
    private String Content;
    private String ReadRate;
    private String RealeaseUserId;
    private Object RealeaseDate;
    private Object IsTop;
    private Object DeleteMark;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getCoverPhoto() {
        return CoverPhoto;
    }

    public void setCoverPhoto(String coverPhoto) {
        CoverPhoto = coverPhoto;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getReadRate() {
        return ReadRate;
    }

    public void setReadRate(String readRate) {
        ReadRate = readRate;
    }

    public String getRealeaseUserId() {
        return RealeaseUserId;
    }

    public void setRealeaseUserId(String realeaseUserId) {
        RealeaseUserId = realeaseUserId;
    }

    public Object getRealeaseDate() {
        return RealeaseDate;
    }

    public void setRealeaseDate(Object realeaseDate) {
        RealeaseDate = realeaseDate;
    }

    public Object getIsTop() {
        return IsTop;
    }

    public void setIsTop(Object isTop) {
        IsTop = isTop;
    }

    public Object getDeleteMark() {
        return DeleteMark;
    }

    public void setDeleteMark(Object deleteMark) {
        DeleteMark = deleteMark;
    }
}
