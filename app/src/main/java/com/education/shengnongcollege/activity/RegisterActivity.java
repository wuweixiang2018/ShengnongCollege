package com.education.shengnongcollege.activity;

import android.os.Bundle;

import com.education.shengnongcollege.BaseTopActivity;
import com.education.shengnongcollege.R;
import com.education.shengnongcollege.widget.DialogUtil;
//注册页面
public class RegisterActivity extends BaseTopActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        initListener();
        initData();
    }

    private void initData() {

    }

    private void initListener() {
    }

    private void initView(){
    }
    private void Register(String userName,String passWord){
        DialogUtil.getInstance().showProgressDialog(this);
    }
}
