package com.education.shengnongcollege.model;

/**
 * Created by wuweixiang on 18/6/1.
 */

public class GetCategoryListRespData extends RespDataBase {


    /**
     * Id : 6e1e59db-05b5-45e7-a14d-8844e5247814
     * TencentCategoryId : 406523
     * Name : 语文
     * CreateTime : 2018-04-23T16:07:32.207
     */

    private String Id;
    private String TencentCategoryId;
    private String Name;
    private String CreateTime;
    private String BgColor;
    private boolean ischoose=false;

    public boolean ischoose() {
        return ischoose;
    }

    public void setIschoose(boolean ischoose) {
        this.ischoose = ischoose;
    }

    public String getBgColor() {
        return BgColor;
    }

    public void setBgColor(String bgColor) {
        BgColor = bgColor;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getTencentCategoryId() {
        return TencentCategoryId;
    }

    public void setTencentCategoryId(String TencentCategoryId) {
        this.TencentCategoryId = TencentCategoryId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }
}
