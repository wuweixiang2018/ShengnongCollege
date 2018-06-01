package com.education.shengnongcollege.api;

import com.education.shengnongcollege.model.GetCategoryListRespData;
import com.education.shengnongcollege.model.GetCategoryListRespObj;
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
     * @param listener  参数result的类型是ListResponseResult<GetCategoryListRespData,GetCategoryListRespObj>
     * @param PageIndex
     * @param PageSize
     */
    public static void getCategoryList(GWResponseListener listener, int PageIndex, int PageSize) {
        HashMap<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("PageIndex", PageIndex);
        bodyMap.put("PageSize", PageSize);
        new GWApiPresent(listener).commonListPost(bodyMap, GetCategoryListRespData.class,
                GetCategoryListRespObj.class,
                LiveBroadcastApiPath.GET_CATEGORY_LIST_CODE_PATH, 0);
    }
}
