package com.education.shengnongcollege.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.education.shengnongcollege.BaseTopActivity;
import com.education.shengnongcollege.R;
import com.education.shengnongcollege.api.UserApiManager;
import com.education.shengnongcollege.model.RespObjBase;
import com.education.shengnongcollege.network.listener.GWResponseListener;
import com.education.shengnongcollege.network.model.ResponseResult;
import com.education.shengnongcollege.utils.BaseUtil;

import java.io.Serializable;

//意见反馈
public class FeedBackActivity extends BaseTopActivity{
    private LinearLayout back;
    private EditText mOpinionEt;
    private Button comitBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_view);
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
        comitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOpinionEt.getText().toString().trim().length()<10){
                    Toast.makeText(FeedBackActivity.this,"不少于10个字",Toast.LENGTH_SHORT).show();
                    return;
                }
                comitFeedBack();
            }
        });
    }

    private void initView(){
        back=findViewById(R.id.aboutme_top_back);
        mOpinionEt=findViewById(R.id.opinion_et);
        comitBtn=findViewById(R.id.feedback_btn);
    }
    //提交反馈信息
    private void comitFeedBack(){
        UserApiManager.comitFeedBack(new GWResponseListener() {
            @Override
            public void successResult(Serializable result, String path, int requestCode, int resultCode) {
                ResponseResult<String, RespObjBase> responseResult = (ResponseResult<String, RespObjBase>) result;
                String data = responseResult.getData();
                Toast.makeText(FeedBackActivity.this,"已提交您的反馈信息",Toast.LENGTH_SHORT).show();
                finish();
                Log.e("意见反馈返回",""+data);
            }
            @Override
            public void errorResult(Serializable result, String path, int requestCode, int resultCode) {
                Toast.makeText(FeedBackActivity.this,"意见反馈失败",Toast.LENGTH_SHORT).show();
            }
        }, BaseUtil.UserId,mOpinionEt.getText().toString());
    }
}
