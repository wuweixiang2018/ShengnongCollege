package com.education.shengnongcollege.api;

import com.education.shengnongcollege.model.LoginRespData;
import com.education.shengnongcollege.model.LoginRespObj;
import com.education.shengnongcollege.model.RegisterRespData;
import com.education.shengnongcollege.model.RespObjBase;
import com.education.shengnongcollege.model.UserInfoRespData;
import com.education.shengnongcollege.network.listener.GWResponseListener;
import com.education.shengnongcollege.network.present.GWApiPresent;

import java.util.HashMap;

/**
 * Created by wuweixiang on 18/6/1.
 * 用户中心相关接口
 */

public class UserApiManager {

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
     * 发送修改密码短信验证码
     *
     * @param listener 参数result的类型是ResponseResult<String,RespObjBase>
     */
    public static void sendmodifypwdvcode(GWResponseListener listener, String Tel) {
        HashMap<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("Tel", Tel);
        new GWApiPresent(listener).commonPost(bodyMap, String.class, RespObjBase.class,
                UserApiPath.SEND_UPDATEPASS_SMS_VERIFY_CODE_PATH, 0);
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
    /**
     * 获取个人信息
     *
     * @param listener 参数result的类型是ResponseResult<UserInfoRespData,RespObjBase>
     */
    public static void getUserInfoById(GWResponseListener listener, String UserId) {
        HashMap<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("UserId", UserId);
        new GWApiPresent(listener).commonPost(bodyMap, UserInfoRespData.class, RespObjBase.class,
                UserApiPath.GET_USERINFO, 0);

    }
    /**
     * 退出登录
     *
     * @param listener 参数result的类型是ResponseResult<String,RespObjBase>
     */
    public static void exitLogin(GWResponseListener listener, String UserId) {
        HashMap<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("UserId", UserId);
        new GWApiPresent(listener).commonPost(bodyMap, String.class, RespObjBase.class,
                UserApiPath.EXIT_LOGIN, 0);
    }
    /**
     * 获取用户登录状态
     *
     * @param listener 参数result的类型是ResponseResult<LoginRespData,RespObjBase>
     */
    public static void loginState(GWResponseListener listener, String UserId) {
        HashMap<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("UserId", UserId);
        new GWApiPresent(listener).commonPost(bodyMap, LoginRespData.class, LoginRespObj.class,
                UserApiPath.LOGIN_STATE, 0);

    }
    /**
     * 完善个人信息
     *{
     "UserId": "string",
     "UserName": "string",
     "NickName": "string",
     "Gender": "string",
     "Birthday": "2018-06-04T08:20:08.029Z",
     "Mobile": "string",
     "IDCard": "string",
     "PhotoGraph": "string"
     }
     * @param listener 参数result的类型是ResponseResult<String,RespObjBase>
     */
    public static void perfectinfo(GWResponseListener listener, String UserId, String UserName, String NickName, String Gender, String Birthday, String Mobile, String IDCard, String PhotoGraph) {
        HashMap<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("UserId", UserId);
        bodyMap.put("UserName", UserName);
        bodyMap.put("NickName", NickName);
        bodyMap.put("Gender", Gender);
        bodyMap.put("Birthday", Birthday);
        bodyMap.put("Mobile", Mobile);
        bodyMap.put("IDCard", IDCard);
        bodyMap.put("PhotoGraph", PhotoGraph);
        new GWApiPresent(listener).commonPost(bodyMap, String.class, RespObjBase.class,
                UserApiPath.WANSHAN_USERINFO, 0);
    }
    /**
     * 修改密码，参数含义详见接口文档
     *
     * @param listener   参数result的类型是ResponseResult<RegisterRespData,RespObjBase>  跟注册一样的返回参数
     * @param UserId
     * @param Mobile
     * @param NewPassword
     * @param VerificationCode    验证码
     */
    public static void modifypassword(GWResponseListener listener, String UserId, String Mobile,
                                    String VerificationCode, String NewPassword) {
        HashMap<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("UserId", UserId);
        bodyMap.put("Mobile", Mobile);
        bodyMap.put("VerificationCode", VerificationCode);
        bodyMap.put("NewPassword", NewPassword);
        new GWApiPresent(listener).commonPost(bodyMap, RegisterRespData.class, RespObjBase.class,
                UserApiPath.MODIFYPASSWORD, 0);

    }
//    public static void feedback(GWResponseListener listener, String Tel) {
//        HashMap<String, Object> bodyMap = new HashMap<>();
//        bodyMap.put("Tel", Tel);
//        new GWApiPresent(listener).commonPost(bodyMap, String.class, RespObjBase.class,
//                UserApiPath.FEEDBACK_PATH, 0);
//    }
}
