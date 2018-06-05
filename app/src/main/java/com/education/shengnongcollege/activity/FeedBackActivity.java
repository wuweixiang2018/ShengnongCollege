package com.education.shengnongcollege.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.education.shengnongcollege.BaseTopActivity;
import com.education.shengnongcollege.R;

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
                if(TextUtils.isEmpty(mOpinionEt.getText().toString())){
                    Toast.makeText(FeedBackActivity.this,"不少于10个字",Toast.LENGTH_SHORT).show();
                    return;
                }
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
          Toast.makeText(this,"接口没有好",Toast.LENGTH_SHORT).show();
//        UserApiManager.getUserInfoById(new GWResponseListener() {
//            @Override
//            public void successResult(Serializable result, String path, int requestCode, int resultCode) {
//                ResponseResult<UserInfoRespData, RespObjBase> responseResult = (ResponseResult<UserInfoRespData, RespObjBase>) result;
//                UserInfoRespData data = responseResult.getData();
//                Glide.with(getActivity()).load(data.getPhotograph()).into(userImage);//设置头像
//                Log.e("获取个人信息返回",""+data.toString());
//            }
//            @Override
//            public void errorResult(Serializable result, String path, int requestCode, int resultCode) {
//                Toast.makeText(getActivity(),"获取个人信息失败",Toast.LENGTH_SHORT).show();
//            }
//        }, BaseUtil.UserId);
    }
}
