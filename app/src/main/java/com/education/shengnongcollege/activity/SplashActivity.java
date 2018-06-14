package com.education.shengnongcollege.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.education.shengnongcollege.MainActivity;
import com.education.shengnongcollege.R;
import com.education.shengnongcollege.api.UserApiManager;
import com.education.shengnongcollege.model.LoginRespData;
import com.education.shengnongcollege.model.RespObjBase;
import com.education.shengnongcollege.network.listener.GWResponseListener;
import com.education.shengnongcollege.network.model.ResponseResult;
import com.education.shengnongcollege.utils.BaseUtil;
import com.education.shengnongcollege.utils.CacheUtil;

import java.io.Serializable;

/**
 * @ClassName: SplashActivity
 * @Description: 启动页面
 * @author yangyikun
 * @date 2018-6-7 上午9:18:43
 * 
 */
public class SplashActivity extends Activity {
	private Button openBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		CacheUtil.getInstance().init(this);
		openBtn=findViewById(R.id.open_btn);
		if(!TextUtils.isEmpty(CacheUtil.getInstance().getUserId())) {
			getUserLoginState();
		}else {
			openBtn.setVisibility(View.VISIBLE);
		}
		openBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}


	//获取用户登录状态
	private void getUserLoginState() {
		UserApiManager.loginState(new GWResponseListener() {
			@Override
			public void successResult(Serializable result, String path, int requestCode, int resultCode) {
				Log.e("获取用户登录状态返回", "");
				ResponseResult<LoginRespData, RespObjBase> responseResult = (ResponseResult<LoginRespData, RespObjBase>) result;
				LoginRespData data = responseResult.getData();
				CacheUtil.getInstance().setUserId(data.getUserId());
				CacheUtil.getInstance().setAllowLiveBroadcast(data.getAllowLiveBroadcast());
				BaseUtil.UserId = data.getUserId();
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						Intent intent = new Intent(SplashActivity.this, MainActivity.class);
						startActivity(intent);
						finish();
					}
				}, 2000);
			}

			@Override
			public void errorResult(Serializable result, String path, int requestCode, int resultCode) {
				Log.e("获取用户状态","走了失败方法");
				Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
				startActivity(intent);
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						finish();
					}
				}, 1000);;
			}
		}, BaseUtil.UserId);
	}
}
