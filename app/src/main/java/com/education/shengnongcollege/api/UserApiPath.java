package com.education.shengnongcollege.api;

/**
 * Created by wuweixiang on 18/6/1.
 */

public class UserApiPath {
    //用户注册
    public static final String REGISTER_PATH = "api/user/register";
    //用户登录
    public static final String LOGIN_PATH = "/api/user/login";
    //获取获取云通信票据（签名）
    public static final String GET_USER_SIGN_PATH = "api/common/getusersig";
    //发送短信验证码
    public static final String SEND_SMS_VERIFY_CODE_PATH = "api/user/sendsmsverificationcode";
    public static final String FEEDBACK_PATH = "api/user/feedback";
    //获取个人信息
    public static final String GET_USERINFO = "/api/user/getuserinfobyuserid";
    //退出登录
    public static final String EXIT_LOGIN = "/api/user/signout";
    //获取登录状态
    public static final String LOGIN_STATE = "/api/user/islogin";
    //完善个人信息
    public static final String WANSHAN_USERINFO = "/api/user/perfectinfo";
}
