package com.education.shengnongcollege.api;

/**
 * Created by wuweixiang on 18/6/1.
 */

public class LiveBroadcastApiPath {
    //获取分类列表
    public static final String GET_CATEGORY_LIST_PATH = "api/livebroadcast/getcategorylist";
    //获取直播中的频道列表
    public static final String GET_LIVE_CHANNEL_LIST_PATH = "/api/livebroadcast/livechannelgetlivechannellist";
    //获取频道列表
    public static final String GET_CHANNEL_LIST_PATH = "/api/livebroadcast/livechannelgetchannellist";
    //获取视频列表
    public static final String GET_VIDEO_LIST_PATH = "/api/livebroadcast/getvideolist";
    //存储视频
    public static final String STORAGE_VIDEO_PATH = "/api/livebroadcast/storagevideo";
    //获取视频详情
    public static final String GET_VIDEO_DETAIL_PATH = "/api/livebroadcast/getvideodetail";
    //获取推流地址(请求开播，并创建房间)
    public static final String GET_PUSH_FLOW_PLAYURL_PATH = "/api/livebroadcast/getpushflowplayurl";
    //获取直播列表
    public static final String GET_LVB_LIST_PATH = "/api/livebroadcast/getlvblist";
    //关闭直播间接口
    public static final String CLOSE_LVB_PATH = "/api/livebroadcast/closelvb";
    //获取公告列表
    public static final String GET_INFONOTICE_LIST_PATH = "/api/common/infonoticelist";
    //等待开播 接口
    public static final String WAIT_PLAY_PATH = "/api/livebroadcast/waitplay";
    //点赞接口
    public static final String ZAN_PATH = "/api/livebroadcast/fabulous";
    //取消点赞接口
    public static final String CANCEL_ZAN_PATH = "/api/livebroadcast/cancelfabulous";
}
