package com.education.shengnongcollege.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.education.shengnongcollege.BaseTopActivity;
import com.education.shengnongcollege.R;

//关于页面
public class AboutMeActivity extends BaseTopActivity{
    private LinearLayout back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutme_view);
        initView();
        initListener();
    }

    private void initListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView(){
        back=findViewById(R.id.aboutme_top_back);
    }
}
