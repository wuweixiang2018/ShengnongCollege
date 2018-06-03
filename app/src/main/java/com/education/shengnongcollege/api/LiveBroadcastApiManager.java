package com.education.shengnongcollege.api;

import android.text.TextUtils;

import com.education.shengnongcollege.model.GetCategoryListRespData;
import com.education.shengnongcollege.model.GetPushFlowPlayUrlRespData;
import com.education.shengnongcollege.model.GetVideoListRespData;
import com.education.shengnongcollege.model.ListRespObj;
import com.education.shengnongcollege.model.RespDataBase;
import com.education.shengnongcollege.model.RespObjBase;
import com.education.shengnongcollege.network.listener.GWResponseListener;
import com.education.shengnongcollege.network.present.GWApiPresent;

import java.util.HashMap;

/**
 * Created by wuweixiang on 18/6/1.
 * 直播相关API
 */

public class LiveBroadcastApiManager {
    /**
     * 获取分类列表
     *
     * @param listener  参数result的类型是ListResponseResult<GetCategoryListRespData,ListRespObj>
     * @param PageIndex
     * @param PageSize
     */
    public static void getCategoryList(GWResponseListener listener, int PageIndex, int PageSize) {
        HashMap<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("PageIndex", PageIndex);
        bodyMap.put("PageSize", PageSize);
        new GWApiPresent(listener).commonListPost(bodyMap, GetCategoryListRespData.class,
                ListRespObj.class,
                LiveBroadcastApiPath.GET_CATEGORY_LIST_PATH, 0);
    }

    /**
     * 获取直播中的频道列表
     *
     * @param listener
     */
    public static void getLiveChannelList(GWResponseListener listener) {
        HashMap<String, String> queryMap = new HashMap<>();
        new GWApiPresent(listener).commonListGet(queryMap, RespDataBase.class,
                RespObjBase.class,
                LiveBroadcastApiPath.GET_LIVE_CHANNEL_LIST_PATH, 0);
    }

    /**
     * 获取频道列表
     *
     * @param listener
     */
    public static void getChannelList(GWResponseListener listener) {
        HashMap<String, String> queryMap = new HashMap<>();
        new GWApiPresent(listener).commonListGet(queryMap, RespDataBase.class,
                RespObjBase.class,
                LiveBroadcastApiPath.GET_CHANNEL_LIST_PATH, 0);
    }

    /**
     * 获取视频列表
     *
     * @param listener   参数result的类型是ListResponseResult<GetVideoListRespData,ListRespObj>
     * @param CategoryId 按分类获取视频列表
     * @param Title      搜索获取视频列表
     * @param PageIndex
     * @param PageSize
     */
    public static void getVideoList(GWResponseListener listener, String CategoryId, String Title,
                                    int PageIndex, int PageSize) {
        HashMap<String, Object> bodyMap = new HashMap<>();
        if (!TextUtils.isEmpty(CategoryId)) {
            bodyMap.put("CategoryId", CategoryId);
        }
        if (!TextUtils.isEmpty(Title)) {
            bodyMap.put("Title", Title);
        }
        bodyMap.put("PageIndex", PageIndex);
        bodyMap.put("PageSize", PageSize);
        new GWApiPresent(listener).commonListPost(bodyMap, GetVideoListRespData.class,
                ListRespObj.class,
                LiveBroadcastApiPath.GET_VIDEO_LIST_PATH, 0);
    }

    public static void getVideoDetail(GWResponseListener listener, String Id) {
        HashMap<String, Object> bodyMap = new HashMap<>();
        if (!TextUtils.isEmpty(Id)) {
            bodyMap.put("Id", Id);
        }
        new GWApiPresent(listener).commonListPost(bodyMap, RespDataBase.class,
                RespObjBase.class,
                LiveBroadcastApiPath.GET_VIDEO_DETAIL_PATH, 0);
    }

    public static void getPushFlowPlayUrl(GWResponseListener listener, String UserId, String Title,
                                          String CoverPhotoUrl) {
        HashMap<String, Object> bodyMap = new HashMap<>();
        if (!TextUtils.isEmpty(UserId)) {
            bodyMap.put("UserId", UserId);
        }
        if (!TextUtils.isEmpty(Title)) {
            bodyMap.put("Title", Title);
        }
        if (!TextUtils.isEmpty(CoverPhotoUrl)) {
            bodyMap.put("CoverPhotoUrl", CoverPhotoUrl);
        }
        new GWApiPresent(listener).commonPost(bodyMap, GetPushFlowPlayUrlRespData.class,
                RespObjBase.class,
                LiveBroadcastApiPath.GET_PUSH_FLOW_PLAYURL_PATH, 0);
    }
}
