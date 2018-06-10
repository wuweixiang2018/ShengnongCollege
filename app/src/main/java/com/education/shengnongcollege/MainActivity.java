package com.education.shengnongcollege;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.education.shengnongcollege.activity.LoginActivity;
import com.education.shengnongcollege.activity.RegisterActivity;
import com.education.shengnongcollege.adapter.MainPagerAdapter;
import com.education.shengnongcollege.api.UserApiManager;
import com.education.shengnongcollege.im.GuestIMMgr;
import com.education.shengnongcollege.im.IMMessageMgr;
import com.education.shengnongcollege.im.LiveIMMgr;
import com.education.shengnongcollege.model.LoginRespData;
import com.education.shengnongcollege.model.RegisterRespData;
import com.education.shengnongcollege.model.RespObjBase;
import com.education.shengnongcollege.model.UserInfoRespData;
import com.education.shengnongcollege.network.listener.GWResponseListener;
import com.education.shengnongcollege.network.model.ResponseResult;
import com.education.shengnongcollege.utils.BaseUtil;
import com.education.shengnongcollege.utils.Ilisten.IListener;
import com.education.shengnongcollege.utils.Ilisten.ListenerManager;
import com.education.shengnongcollege.view.CustomViewPager;
import com.education.shengnongcollege.view.PagerSlidingTabStrip2;
import com.education.shengnongcollege.widget.DialogUtil;
import com.tencent.imsdk.TIMConnListener;
import com.tencent.rtmp.TXLiveBase;

import java.io.Serializable;
import java.util.Map;

public class MainActivity extends BaseTopActivity implements IListener {


    private CustomViewPager viewPager;
    private PagerSlidingTabStrip2 tabLayout;
    private MainPagerAdapter mainPagerAdapter;

    private void getUserInfoById() {
        UserApiManager.getUserInfoById(new GWResponseListener() {
            @Override
            public void successResult(Serializable result, String path, int requestCode, int resultCode) {
                ResponseResult<UserInfoRespData, RespObjBase> responseResult = (ResponseResult<UserInfoRespData, RespObjBase>) result;
                UserInfoRespData data = responseResult.getData();
                BaseUtil.userData = data;
                getUserSign();
                Toast.makeText(getApplicationContext(), "获取个人信息成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void errorResult(Serializable result, String path, int requestCode, int resultCode) {
                Toast.makeText(getApplicationContext(), "获取个人信息失败", Toast.LENGTH_SHORT).show();
            }
        }, BaseUtil.UserId);
    }

    private void getUserSign() {
        UserApiManager.getUserSign(new GWResponseListener() {
            @Override
            public void successResult(Serializable result, String path, int requestCode, int resultCode) {
                //云通信票据获取了 进行注册
                ResponseResult<String, RespObjBase> responseResult = (ResponseResult<String, RespObjBase>) result;
                BaseUtil.userSig = responseResult.getData();
                LiveIMMgr.getInstance().getIMMessageMgr().initialize(
                        BaseUtil.userData.getMobile(), BaseUtil.userSig, 1400086725, new IMMessageMgr.Callback() {
                            @Override
                            public void onError(int code, String errInfo) {
                                Toast.makeText(getApplicationContext(), "初始化失败", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onSuccess(Object... args) {
                                Toast.makeText(getApplicationContext(), "初始化成功", Toast.LENGTH_SHORT).show();
                            }
                        });
            }

            @Override
            public void errorResult(Serializable result, String path, int requestCode, int resultCode) {
                DialogUtil.getInstance().cancelProgressDialog();
                Toast.makeText(getApplicationContext(), "云通信票据获取失败", Toast.LENGTH_SHORT).show();
            }
        }, BaseUtil.userData.getMobile());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getUserInfoById();
//        GuestIMMgr.getInstance().guestLogin(getApplicationContext(), new TIMConnListener() {
//            @Override
//            public void onConnected() {
//
//            }
//
//            @Override
//            public void onDisconnected(int i, String s) {
//
//            }
//
//            @Override
//            public void onWifiNeedAuth(String s) {
//
//            }
//        });
//
//        GuestIMMgr.getInstance().setCallback(new IMMessageMgr.Callback() {
//            @Override
//            public void onError(int code, String errInfo) {
//                Toast.makeText(getApplicationContext(), "发送消息失败",
//                        Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onSuccess(Object... args) {
//                Toast.makeText(getApplicationContext(), "发送消息成功",
//                        Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        GuestIMMgr.getInstance().imInit(getApplicationContext(), new IMMessageMgr.IMMessageListener() {
//            @Override
//            public void onConnected() {
//
//            }
//
//            @Override
//            public void onDisconnected() {
//
//            }
//
//            @Override
//            public void onPusherChanged() {
//
//            }
//
//            @Override
//            public void onGroupTextMessage(String groupID, String senderID, String userName, String headPic, String message) {
//                Toast.makeText(getApplicationContext(), "onGroupCustomMessage groupID=" + groupID
//                                + " senderID=" + senderID + " message=" + message + " userName=" + userName,
//                        Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onGroupCustomMessage(String groupID, String senderID, String message) {
//                Toast.makeText(getApplicationContext(), "onGroupCustomMessage groupID=" + groupID + " senderID=" + senderID + " message=" + message,
//                        Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onC2CCustomMessage(String sendID, String cmd, String message) {
//
//            }
//
//            @Override
//            public void onGroupDestroyed(String groupID) {
//
//            }
//
//            @Override
//            public void onDebugLog(String log) {
//
//            }
//        });

//        findViewById(R.id.hello_tv).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "点击hello", Toast.LENGTH_SHORT).show();
        String sdkver = TXLiveBase.getSDKVersionStr();
        Log.d("liteavsdk", "liteav sdk version is : " + sdkver);

//        findViewById(R.id.zhibo_btn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, LivePlayerActivity.class));
//            }
//        });
//
//        findViewById(R.id.dianbo_btn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, VodListPlayerActivity.class));
//            }
//        });
//
//                LiveBroadcastApiManager.getCategoryList(new GWResponseListener() {
//                    @Override
//                    public void successResult(Serializable result, String path, int requestCode, int resultCode) {
//                        ListResponseResult<GetCategoryListRespData, ListRespObj> responseResult = (ListResponseResult<GetCategoryListRespData, ListRespObj>) result;
//                        List<GetCategoryListRespData> data = responseResult.getData();
//                        GetCategoryListRespObj obj = responseResult.getObj();
//                        Toast.makeText(getApplicationContext(), "分类列表获取成功", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void errorResult(Serializable result, String path, int requestCode, int resultCode) {
//                        Toast.makeText(getApplicationContext(), "分类列表获取失败", Toast.LENGTH_SHORT).show();
//                    }
//                }, 1, 10);

//                UserApiManager.getUserSign(new GWResponseListener() {
//                    @Override
//                    public void successResult(Serializable result, String path, int requestCode, int resultCode) {
//                        ResponseResult<String, RespObjBase> responseResult = (ResponseResult<String, RespObjBase>) result;
//                        String userSign = responseResult.getData();
//                        Toast.makeText(getApplicationContext(), "云通信票据获取成功=" + userSign, Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void errorResult(Serializable result, String path, int requestCode, int resultCode) {
//                        Toast.makeText(getApplicationContext(), "云通信票据获取失败", Toast.LENGTH_SHORT).show();
//                    }
//                });

//                UserApiManager.sendSmsVerfyCode(new GWResponseListener() {
//                    @Override
//                    public void successResult(Serializable result, String path, int requestCode, int resultCode) {
//                        ResponseResult<String, RespObjBase> responseResult = (ResponseResult<String, RespObjBase>) result;
//                        Toast.makeText(getApplicationContext(), "发送验证码成功=" + responseResult.getData(), Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void errorResult(Serializable result, String path, int requestCode, int resultCode) {
//                        Toast.makeText(getApplicationContext(), "发送验证码失败", Toast.LENGTH_SHORT).show();
//                    }
//                }, "13606645039");
//            }
//        });

        ListenerManager.getInstance().registerListtener(this);
        initView();
    }

    private void initView() {
        viewPager = findViewById(R.id.viewpager);
        viewPager.setScanScroll(false);
        viewPager.setSmoothScroll(false);
        mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(mainPagerAdapter);
        viewPager.setOffscreenPageLimit(mainPagerAdapter.getCount());
        tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setItemResId(R.layout.main_tab_item);
        tabLayout.setmItems(mainPagerAdapter.modelMap);
        tabLayout.setShouldExpand(false);
        tabLayout.setViewPager(viewPager);
        tabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                setFragmentState(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayout.setBackgroundColor(getResources().getColor(R.color.table_bg));
        tabLayout.setUnderlineColor(R.color.table_bg);
        tabLayout.setIndicatorColor(R.color.table_bg);
        tabLayout.setCurrentItem(mainPagerAdapter.getCurrentIndexById("chat_icolleague2"));
        int index = mainPagerAdapter.getDefaultIndex();
        tabLayout.setCurrentItem(index);
        setFragmentState(index);
    }

    public void setFragmentState(int position) {
        if (mainPagerAdapter != null && mainPagerAdapter.getItem(position) != null) {
            try {
                Map<String, BaseFragment> fragments = mainPagerAdapter.getFragments();
                for (String key : fragments.keySet()) {
                    if (!TextUtils.equals(key, (position + "")) && fragments.get(key) != null) {
                        fragments.get(key).setShow(false);
                    }
                }
                mainPagerAdapter.getItem(position).changeRefreshData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // 不调用保存状态方法以解决应用被杀恢复时闪退问题
        super.onSaveInstanceState(outState);
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            /**
             * 系统返回键加入监听
             */

            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), R.string.main_toast_quit_warnning, Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void notifyAllActivity(String str, Object object) {
        if (TextUtils.equals("MainActivity_replace", str)) {
            tabLayout.setCurrentItem(1);
        }
    }
}
