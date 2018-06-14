package com.education.shengnongcollege.api;

import android.text.TextUtils;

import com.education.shengnongcollege.model.GetCategoryListRespData;
import com.education.shengnongcollege.model.GetLvbListRespData;
import com.education.shengnongcollege.model.GetNoticeListRespData;
import com.education.shengnongcollege.model.GetPushFlowPlayUrlRespData;
import com.education.shengnongcollege.model.GetVideoDetailRespData;
import com.education.shengnongcollege.model.GetVideoListRespData;
import com.education.shengnongcollege.model.ListRespObj;
import com.education.shengnongcollege.model.RespDataBase;
import com.education.shengnongcollege.model.RespObjBase;
import com.education.shengnongcollege.network.listener.GWResponseListener;
import com.education.shengnongcollege.network.present.GWApiPresent;
import com.education.shengnongcollege.utils.BaseUtil;

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

    /**
     * 获取直播列表
     *
     * @param listener
     * @param PageIndex
     * @param PageSize
     */
    public static void getLVBList(GWResponseListener listener, int PageIndex, int PageSize) {
        HashMap<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("PageIndex", PageIndex);
        bodyMap.put("PageSize", PageSize);
        new GWApiPresent(listener).commonListPost(bodyMap, GetLvbListRespData.class,
                ListRespObj.class,
                LiveBroadcastApiPath.GET_LVB_LIST_PATH, 0);
    }

    /**
     * 获取视频详情
     *
     * @param listener
     * @param Id
     */
    public static void getVideoDetail(GWResponseListener listener, String Id) {
        HashMap<String, Object> bodyMap = new HashMap<>();
        if (!TextUtils.isEmpty(Id)) {
            bodyMap.put("Id", Id);
        }
        new GWApiPresent(listener).commonPost(bodyMap, GetVideoDetailRespData.class,
                RespObjBase.class,
                LiveBroadcastApiPath.GET_VIDEO_DETAIL_PATH, 0);
    }

    public static void storageVideo(GWResponseListener listener, String CategoryId, String Title,
                                    String Profile, String VideoUrl, String VideoId, String CoverUrl) {
        HashMap<String, Object> bodyMap = new HashMap<>();
        if (CategoryId == null)
            CategoryId = "";
        if (Title == null)
            Title = "";
        if (Profile == null)
            Profile = "";
        bodyMap.put("CategoryId", CategoryId);
        bodyMap.put("Title", Title);
        bodyMap.put("Profile", Profile);
        bodyMap.put("VideoUrl", VideoUrl);
        bodyMap.put("VideoId", VideoId);
        bodyMap.put("CoverUrl", CoverUrl);
        new GWApiPresent(listener).commonPost(bodyMap, RespDataBase.class,
                RespObjBase.class,
                LiveBroadcastApiPath.STORAGE_VIDEO_PATH, 0);
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

    public static void closeLVB(GWResponseListener listener) {
        HashMap<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("UserId", BaseUtil.UserId);
        new GWApiPresent(listener).commonPost(bodyMap, Boolean.class,
                RespObjBase.class,
                LiveBroadcastApiPath.CLOSE_LVB_PATH, 0);
    }
    /**
     * 获取公告列表
     *
     * @param listener   参数result的类型是ListResponseResult<GetNoticeListRespData,ListRespObj>
     * @param Title      搜索获取视频列表
     * @param PageIndex
     * @param PageSize
     */
    public static void getInfonoticelist(GWResponseListener listener,String Title,
                                    int PageIndex, int PageSize) {
        HashMap<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("Title", Title);
        bodyMap.put("PageIndex", PageIndex);
        bodyMap.put("PageSize", PageSize);
        new GWApiPresent(listener).commonListPost(bodyMap, GetNoticeListRespData.class,
                ListRespObj.class,
                LiveBroadcastApiPath.GET_INFONOTICE_LIST_PATH, 0);
    }

    public static void waitPlay(GWResponseListener listener) {
        HashMap<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("UserId", BaseUtil.UserId);
        new GWApiPresent(listener).commonPost(bodyMap, Boolean.class,
                RespObjBase.class,
                LiveBroadcastApiPath.WAIT_PLAY_PATH, 0);
    }

    public static void zan(GWResponseListener listener, String LiveRoomId) {
        HashMap<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("LiveRoomId", LiveRoomId);
        new GWApiPresent(listener).commonPost(bodyMap, Boolean.class,
                RespObjBase.class,
                LiveBroadcastApiPath.ZAN_PATH, 0);
    }

    public static void cancalZan(GWResponseListener listener, String LiveRoomId) {
        HashMap<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("LiveRoomId", LiveRoomId);
        new GWApiPresent(listener).commonPost(bodyMap, Boolean.class,
                RespObjBase.class,
                LiveBroadcastApiPath.CANCEL_ZAN_PATH, 0);
    }

}
