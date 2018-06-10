package com.education.shengnongcollege.im;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.education.shengnongcollege.common.utils.TCConstants;
import com.education.shengnongcollege.utils.BaseUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConnListener;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMSdkConfig;
import com.tencent.imsdk.TIMTextElem;
import com.tencent.imsdk.TIMUserConfig;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.qalsdk.QALSDKManager;
//import com.tencent.qcloud.netwrapper.qal.QALSDKManager;
//import com.tencent.qalsdk.QALSDKManager;

import tencent.tls.platform.TLSErrInfo;
import tencent.tls.platform.TLSGuestLoginListener;
import tencent.tls.platform.TLSHelper;
import tencent.tls.platform.TLSLoginHelper;
import tencent.tls.platform.TLSRefreshUserSigListener;
import tencent.tls.platform.TLSUserInfo;

/**
 * Created by wuweixiang on 18/6/9.
 */

public class GuestIMMgr {
    private static GuestIMMgr instance;
    private IMMessageMgr mImMessageMgr;
    private IMMessageMgr.Callback callback;
    //是否已经登录
    private boolean isLogin;

    public boolean isLogin() {
        return isLogin;
    }

    public static GuestIMMgr getInstance() {
        if (instance == null)
            instance = new GuestIMMgr();
        return instance;
    }

    public void setCallback(IMMessageMgr.Callback callback) {
        this.callback = callback;
    }

    public void imInit(Context context, IMMessageMgr.IMMessageListener listener) {
        mImMessageMgr = new IMMessageMgr(context);
        mImMessageMgr.setIMMessageListener(listener);
    }

    public void imDestroy() {
        mImMessageMgr.setIMMessageListener(null);
        mImMessageMgr = null;
    }

    private static final class UserInfo {
        String nickName;
        String headPic;
    }

    private static class CommonJson<T> {
        String cmd;
        T data;
    }

    public void sendText(String mGroupID, String text) {
        if (!isLogin) {
            return;
        }

        TIMMessage message = new TIMMessage();
        try {
            CommonJson<UserInfo> txtHeadMsg = new CommonJson<UserInfo>();
            txtHeadMsg.cmd = "CustomTextMsg";
            txtHeadMsg.data = new UserInfo();
            txtHeadMsg.data.nickName = BaseUtil.UserId;
            txtHeadMsg.data.headPic = BaseUtil.userData.getPhotograph();
            String strCmdMsg = new Gson().toJson(txtHeadMsg, new TypeToken<CommonJson<UserInfo>>() {
            }.getType());

            TIMCustomElem customElem = new TIMCustomElem();
            customElem.setData(strCmdMsg.getBytes("UTF-8"));

            TIMTextElem textElem = new TIMTextElem();
            textElem.setText(text);

            message.addElement(customElem);
            message.addElement(textElem);
        } catch (Exception e) {
//            printDebugLog("[sendGroupTextMessage] 发送群{%s}消息失败，组包异常", mGroupID);
            if (callback != null) {
                callback.onError(-1, "发送群消息失败");
            }
            return;
        }

//        sendTIMMessage(msg, timValueCallBack);

//        TIMConversation conversation = TIMManager.getInstance().getConversation(TIMConversationType.Group,
//                mGroupID);
//        conversation.sendMessage(message, new TIMValueCallBack<TIMMessage>() {
//            @Override
//            public void onError(int i, String s) {
////                printDebugLog("[sendGroupTextMessage] 发送群{%s}消息失败: %s(%d)", mGroupID, s, i);
//
//                if (callback != null)
//                    callback.onError(i, s);
//            }
//
//            @Override
//            public void onSuccess(TIMMessage timMessage) {
////                printDebugLog("[sendGroupTextMessage] 发送群消息成功");
//
//                if (callback != null)
//                    callback.onSuccess();
//            }
//        });

//        String userName = BaseUtil.userData.getRealName();
//        String headPic = BaseUtil.userData.getPhotograph();
//        mImMessageMgr.sendGroupTextMessage(userName, headPic, text, callback);
    }

    private void login(final Context context, final TIMConnListener mIMConnListener, int appid,
                       int accoutType, String userSig, String identifier) {
        TIMSdkConfig mTIMSdkConfig = new TIMSdkConfig(appid);
        mTIMSdkConfig.setServiceEnabled(false);
        mTIMSdkConfig.setAccoutType(accoutType);
        mTIMSdkConfig.setConnectionListener(mIMConnListener);
        TIMManager.getInstance().init(context, mTIMSdkConfig);
        TIMUserConfig timUserConfig = new TIMUserConfig();
        //发起 IM SDK 登录操作，登录成功后就可以发消息了
        TIMManager.getInstance().login(identifier, userSig, timUserConfig, new TIMCallBack() {
            @Override
            public void onSuccess() {
                // OK, 登录成功，后面就可以发消息了！
                isLogin = true;
//                TIMMessage
//                sendText("test");
                Toast.makeText(context, "登录成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int i, String s) {
                // im sdk 登录失败，请检查 IMSDK_APPID 是不是搞错了
                Toast.makeText(context, "登录失败=" + s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 示例代码： Android 平台下实现IM SDK 的访客登录
    public void guestLogin(final Context context, final TIMConnListener mIMConnListener) {
        //调用TLS（Tencent Login Server）的访客登录模式
        final int sdkAppid = 1400093465;
        final int accoutType = 26976;
        // 务必检查IMSDK已做以下初始化
        QALSDKManager.getInstance().setEnv(0);
        QALSDKManager.getInstance().init(context, sdkAppid);
        QALSDKManager.getInstance().appSetGuestMode();

        final TLSLoginHelper mTLSLoginHelper = TLSLoginHelper.getInstance();

        mTLSLoginHelper.init(context, sdkAppid, accoutType, "1.0");
        // 初始化TLSSDK
//        final TLSHelper tlsHelper = TLSHelper.getInstance().init(context, sdkAppid);
        // 调用该接口可以判断当前访客登录状态，返回null说明暂时无访客登录，否则返回访客标识
        final String guest = mTLSLoginHelper.getGuestIdentifier();
        if (TextUtils.isEmpty(guest)) {
            // 这里直接调用登录接口即可，该接口内部实现了自动注册，这也是tlsHelper需要初始化的原因。
            // 注意：每次调用该接口都会重新注册访客账号，并在本地删除旧的访客账号，请谨慎

            //调用TLS（Tencent Login Server）的访客登录模式
            mTLSLoginHelper.TLSGuestLogin(new TLSGuestLoginListener() {
                @Override
                public void OnGuestLoginSuccess(TLSUserInfo tlsUserInfo) {
                    String userSig = mTLSLoginHelper.getUserSig(tlsUserInfo.identifier);
                    login(context, mIMConnListener, sdkAppid, accoutType, userSig, guest);
                }

                @Override
                public void OnGuestLoginFail(TLSErrInfo tlsErrInfo) {
                    int a = 1;
                }

                @Override
                public void OnGuestLoginTimeout(TLSErrInfo tlsErrInfo) {
                    int a = 1;
                }
            });
        } else {
            if (mTLSLoginHelper.needLogin(guest)) {
                String userSig = mTLSLoginHelper.getUserSig(guest);
                login(context, mIMConnListener, sdkAppid, accoutType, userSig, guest);
            } else {
                mTLSLoginHelper.TLSRefreshUserSig(guest, new TLSRefreshUserSigListener() {
                    @Override
                    public void OnRefreshUserSigSuccess(TLSUserInfo tlsUserInfo) {
                        isLogin = true;
//                        sendText("test");
                    }

                    @Override
                    public void OnRefreshUserSigFail(TLSErrInfo tlsErrInfo) {

                    }

                    @Override
                    public void OnRefreshUserSigTimeout(TLSErrInfo tlsErrInfo) {

                    }
                });

            }
        }
    }
}
