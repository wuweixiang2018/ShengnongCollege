package com.education.shengnongcollege.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.education.shengnongcollege.BaseTopActivity;
import com.education.shengnongcollege.MainActivity;
import com.education.shengnongcollege.R;
import com.education.shengnongcollege.widget.DialogUtil;

public class LoginActivity extends BaseTopActivity {
    private EditText userName,passWord;
    private Button login;
    private TextView register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initListener();
        initData();
    }

    private void initData() {

    }

    private void initListener() {
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initView(){
        userName=findViewById(R.id.username_edit_text);
        passWord=findViewById(R.id.password_edit_text);
        login=findViewById(R.id.login_btn);
        register=findViewById(R.id.register_tv);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.anim_enter_from_top, R.anim.anim_exit_from_bottom);
    }
    private void Login(String userName,String passWord){
        DialogUtil.getInstance().showProgressDialog(this);
    }
}
