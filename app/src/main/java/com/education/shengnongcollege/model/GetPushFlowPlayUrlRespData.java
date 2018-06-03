package com.education.shengnongcollege.model;

/**
 * Created by wuweixiang on 18/6/3.
 */

public class GetPushFlowPlayUrlRespData extends RespDataBase {

    /**
     * PushUrl : rtmp://21696.livepush.myqcloud.com/live/21696_a99622010e?bizid=21696&txSecret=2adb2ec58474901db664cd36b7ece943&txTime=5AFBFC7A
     */

    private String PushUrl;

    public String getPushUrl() {
        return PushUrl;
    }

    public void setPushUrl(String PushUrl) {
        this.PushUrl = PushUrl;
    }
}
