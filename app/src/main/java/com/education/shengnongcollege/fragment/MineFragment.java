package com.education.shengnongcollege.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.education.shengnongcollege.BaseFragment;
import com.education.shengnongcollege.R;
import com.education.shengnongcollege.activity.AboutMeActivity;
import com.education.shengnongcollege.activity.FeedBackActivity;
import com.education.shengnongcollege.activity.LoginActivity;
import com.education.shengnongcollege.activity.PerfectinfoActivity;
import com.education.shengnongcollege.activity.UpdatePassActivity;
import com.education.shengnongcollege.api.UserApiManager;
import com.education.shengnongcollege.model.RespObjBase;
import com.education.shengnongcollege.model.UserInfoRespData;
import com.education.shengnongcollege.network.listener.GWResponseListener;
import com.education.shengnongcollege.network.model.ResponseResult;
import com.education.shengnongcollege.utils.BaseUtil;

import java.io.Serializable;

/**
 * 我的页面
 *
 * @author shihaoxian
 * @date 2018/4/12
 */
public class MineFragment extends BaseFragment{

    private View mFragmentView;
    private LinearLayout wsgrxxLayout,xgMmLayout,yjfkLayout,aboutMeLayout;
    private ImageView userImage;
    private TextView registerTv;
    private Button exitLogin;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (null != mFragmentView) {
            ViewGroup parent = (ViewGroup) mFragmentView.getParent();
            if (null != parent) {
                parent.removeView(mFragmentView);
            }
        } else {
            mFragmentView = inflater.inflate(R.layout.activity_mine_view, container, false);
            initView();
            initListener();
//            if(!TextUtils.isEmpty(BaseUtil.UserId)){
//                getUserLoginState();
//            }
            getUserInfoById();

        }
        return mFragmentView;
    }

    private void initView() {
        userImage=mFragmentView.findViewById(R.id.mine_top_userImage);
        registerTv=mFragmentView.findViewById(R.id.mine_register_tv);
        wsgrxxLayout=mFragmentView.findViewById(R.id.root_mine_item_wsgexx);
        xgMmLayout=mFragmentView.findViewById(R.id.root_mine_item_xgmm);
        yjfkLayout=mFragmentView.findViewById(R.id.root_mine_item_yjfk);
        aboutMeLayout=mFragmentView.findViewById(R.id.root_mine_item_aboutme);
        exitLogin=mFragmentView.findViewById(R.id.login_out_btn);
        registerTv.setVisibility(View.INVISIBLE);//登录按钮暂时就不要了
        exitLogin.setVisibility(View.VISIBLE);//退出登录显示出来
    }

    private void initListener() {
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        registerTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        //完善个人信息
        wsgrxxLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(BaseUtil.UserId)){
                    Intent intent=new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent=new Intent(getActivity(), PerfectinfoActivity.class);
                    startActivity(intent);
                }
            }
        });
        //修改密码
        xgMmLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(getActivity(), UpdatePassActivity.class);
                startActivity(intent);
            }
        });
        //意见反馈
        yjfkLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), FeedBackActivity.class);
                startActivity(intent);
            }
        });
        //关于我们
        aboutMeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), AboutMeActivity.class);
                startActivity(intent);
            }
        });
        //退出登录
        exitLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitLoginVoid();
            }
        });
    }
    //获取用户登录状态
//    private void getUserLoginState(){
//        UserApiManager.loginState(new GWResponseListener() {
//            @Override
//            public void successResult(Serializable result, String path, int requestCode, int resultCode) {
//                Log.e("获取用户登录状态返回","");
//                ResponseResult<LoginRespData, RespObjBase> responseResult = (ResponseResult<LoginRespData, RespObjBase>) result;
//                LoginRespData data = responseResult.getData();
//                BaseUtil.UserId=data.getUserId();
//                BaseUtil.Online=data.getOnline();
//                exitLogin.setVisibility(View.VISIBLE);//退出登录显示出来
//                registerTv.setVisibility(View.INVISIBLE);//登录按钮隐藏起来
//            }
//            @Override
//            public void errorResult(Serializable result, String path, int requestCode, int resultCode) {
//                Toast.makeText(getActivity(),"获取用户登录状态失败",Toast.LENGTH_SHORT).show();
//                BaseUtil.UserId="";
//                BaseUtil.Online=0;
//            }
//        }, BaseUtil.UserId);
//    }
    //获取个人信息
    private void getUserInfoById(){
        UserApiManager.getUserInfoById(new GWResponseListener() {
            @Override
            public void successResult(Serializable result, String path, int requestCode, int resultCode) {
                ResponseResult<UserInfoRespData, RespObjBase> responseResult = (ResponseResult<UserInfoRespData, RespObjBase>) result;
                UserInfoRespData data = responseResult.getData();
                BaseUtil.userData=data;
                Glide.with(getActivity()).load(data.getPhotograph()).into(userImage);//设置头像
                Log.e("获取个人信息返回",""+data.toString());
            }
            @Override
            public void errorResult(Serializable result, String path, int requestCode, int resultCode) {
                Toast.makeText(getActivity(),"获取个人信息失败",Toast.LENGTH_SHORT).show();
            }
        }, BaseUtil.UserId);
    }
    private void exitLoginVoid(){
        UserApiManager.exitLogin(new GWResponseListener() {
            @Override
            public void successResult(Serializable result, String path, int requestCode, int resultCode) {
                ResponseResult<String, RespObjBase> responseResult = (ResponseResult<String, RespObjBase>) result;
                String data = responseResult.getData();
                BaseUtil.UserId="";
                BaseUtil.userData=null;
                Intent intent=new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
                Log.e("退出登录",data);
            }
            @Override
            public void errorResult(Serializable result, String path, int requestCode, int resultCode) {
                Toast.makeText(getActivity(),"退出登录失败",Toast.LENGTH_SHORT).show();
            }
        }, BaseUtil.UserId);
    }
}
