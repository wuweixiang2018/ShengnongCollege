package com.education.shengnongcollege.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.education.shengnongcollege.BaseTopActivity;
import com.education.shengnongcollege.MainActivity;
import com.education.shengnongcollege.R;
import com.education.shengnongcollege.api.UserApiManager;
import com.education.shengnongcollege.model.LoginRespData;
import com.education.shengnongcollege.model.RespObjBase;
import com.education.shengnongcollege.network.listener.GWResponseListener;
import com.education.shengnongcollege.network.model.ResponseResult;
import com.education.shengnongcollege.utils.BaseUtil;
import com.education.shengnongcollege.utils.CacheUtil;
import com.education.shengnongcollege.widget.DialogUtil;

import java.io.Serializable;

public class LoginActivity extends BaseTopActivity {
    private EditText userName, passWord;
    private Button login;
    private TextView register;
    public static Activity loginThis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initListener();
        initData();
        loginThis = this;
    }

    private void initData() {
        //存的id不为空的时候自动登录一下
        if (!TextUtils.isEmpty(CacheUtil.getInstance().getUserId())) {
            userName.setText(CacheUtil.getInstance().getUserName());
            passWord.setText(CacheUtil.getInstance().getUserPassword());
            getUserLoginState();
        }
    }

    private void initListener() {
        //测试按钮
        findViewById(R.id.test_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, TestActivity.class));
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(userName.getText().toString())) {
                    Toast.makeText(LoginActivity.this, "登录名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(passWord.getText().toString())) {
                    Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                Login(userName.getText().toString(), passWord.getText().toString());
            }
        });
    }

    private void initView() {
        userName = findViewById(R.id.username_edit_text);
        passWord = findViewById(R.id.password_edit_text);
        login = findViewById(R.id.login_btn);
        register = findViewById(R.id.register_tv);
        CacheUtil.getInstance().init(this);
    }

    private void Login(final String userName, final String passWord) {
        DialogUtil.getInstance().showProgressDialog(this);
        UserApiManager.login(new GWResponseListener() {
            @Override
            public void successResult(Serializable result, String path, int requestCode, int resultCode) {
                DialogUtil.getInstance().cancelProgressDialog();
                ResponseResult<LoginRespData, RespObjBase> responseResult = (ResponseResult<LoginRespData, RespObjBase>) result;
                LoginRespData data = responseResult.getData();
                BaseUtil.UserId = data.getUserId();
                CacheUtil.getInstance().setUserId(data.getUserId());
                CacheUtil.getInstance().setUserName(userName);
                CacheUtil.getInstance().setUserPassword(passWord);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void errorResult(Serializable result, String path, int requestCode, int resultCode) {
                DialogUtil.getInstance().cancelProgressDialog();
                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();

            }
        }, userName, passWord);
    }

    //获取用户登录状态
    private void getUserLoginState() {
        DialogUtil.getInstance().showProgressDialog(this);
        UserApiManager.loginState(new GWResponseListener() {
            @Override
            public void successResult(Serializable result, String path, int requestCode, int resultCode) {
                Log.e("获取用户登录状态返回", "");
                DialogUtil.getInstance().cancelProgressDialog();
                ResponseResult<LoginRespData, RespObjBase> responseResult = (ResponseResult<LoginRespData, RespObjBase>) result;
                LoginRespData data = responseResult.getData();
                CacheUtil.getInstance().setUserId(data.getUserId());
                CacheUtil.getInstance().setUserName(userName.getText().toString());
                CacheUtil.getInstance().setUserPassword(passWord.getText().toString());
                BaseUtil.UserId = data.getUserId();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void errorResult(Serializable result, String path, int requestCode, int resultCode) {
                DialogUtil.getInstance().cancelProgressDialog();
                Toast.makeText(LoginActivity.this, "获取用户登录状态失败", Toast.LENGTH_SHORT).show();
            }
        }, BaseUtil.UserId);
    }

    @Override
    public void onBackPressed() {
        finishAll();
        finish();
    }
}
