package com.education.shengnongcollege.api;

import com.education.shengnongcollege.model.LoginRespData;
import com.education.shengnongcollege.model.LoginRespObj;
import com.education.shengnongcollege.model.RegisterRespData;
import com.education.shengnongcollege.model.RespObjBase;
import com.education.shengnongcollege.network.listener.GWResponseListener;
import com.education.shengnongcollege.network.present.GWApiPresent;

import java.util.HashMap;

/**
 * Created by wuweixiang on 18/6/1.
 * 用户中心相关接口
 */

public class UserApiManager {

    public static void getDemandUserSign(GWResponseListener listener) {
//        HashMap<String, Object> bodyMap = new HashMap<>();
//        bodyMap.put("Identifier", Identifier);
        new GWApiPresent(listener).commonPost(null, String.class, RespObjBase.class,
                UserApiPath.GET_DEMAND_USER_SIGN_PATH, 0);
    }

    /**
     * 获取获取云通信票据（签名）
     *
     * @param Identifier 登录手机号
     * @param listener   参数result的类型是ResponseResult<String,RespObjBase>
     */
    public static void getUserSign(GWResponseListener listener, String Identifier) {
        HashMap<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("Identifier", Identifier);
        new GWApiPresent(listener).commonPost(bodyMap, String.class, RespObjBase.class,
                UserApiPath.GET_USER_SIGN_PATH, 0);
    }

    /**
     * 发送短信验证码
     *
     * @param listener 参数result的类型是ResponseResult<String,RespObjBase>
     */
    public static void sendSmsVerfyCode(GWResponseListener listener, String Tel) {
        HashMap<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("Tel", Tel);
        new GWApiPresent(listener).commonPost(bodyMap, String.class, RespObjBase.class,
                UserApiPath.SEND_SMS_VERIFY_CODE_PATH, 0);
    }

    /**
     * 用户注册，参数含义详见接口文档
     *
     * @param listener   参数result的类型是ResponseResult<RegisterRespData,RespObjBase>
     * @param Tel
     * @param Account
     * @param TelVerCode sendSmsVerfyCode接口发送
     * @param password
     * @param UserSig    getUserSign方法返回
     */
    public static void registerUser(GWResponseListener listener, String Tel, String Account,
                                    String TelVerCode, String password, String UserSig) {
        HashMap<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("Tel", Tel);
        bodyMap.put("Account", Account);
        bodyMap.put("TelVerCode", TelVerCode);
        bodyMap.put("password", password);
        bodyMap.put("UserSig", UserSig);
        new GWApiPresent(listener).commonPost(bodyMap, RegisterRespData.class, RespObjBase.class,
                UserApiPath.REGISTER_PATH, 0);

    }

    public static void login(GWResponseListener listener, String Mobile, String Password) {
        HashMap<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("Mobile", Mobile);
        bodyMap.put("Password", Password);
        new GWApiPresent(listener).commonPost(bodyMap, LoginRespData.class, LoginRespObj.class,
                UserApiPath.LOGIN_PATH, 0);

    }

//    public static void feedback(GWResponseListener listener, String Tel) {
//        HashMap<String, Object> bodyMap = new HashMap<>();
//        bodyMap.put("Tel", Tel);
//        new GWApiPresent(listener).commonPost(bodyMap, String.class, RespObjBase.class,
//                UserApiPath.FEEDBACK_PATH, 0);
//    }
}
