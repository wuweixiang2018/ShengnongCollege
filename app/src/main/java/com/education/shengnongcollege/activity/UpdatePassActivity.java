package com.education.shengnongcollege.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.education.shengnongcollege.BaseTopActivity;
import com.education.shengnongcollege.R;
import com.education.shengnongcollege.api.UserApiManager;
import com.education.shengnongcollege.model.RegisterRespData;
import com.education.shengnongcollege.model.RespObjBase;
import com.education.shengnongcollege.network.listener.GWResponseListener;
import com.education.shengnongcollege.network.model.ResponseResult;
import com.education.shengnongcollege.utils.BaseUtil;
import com.education.shengnongcollege.widget.DialogUtil;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

//修改密码
public class UpdatePassActivity extends BaseTopActivity{
    private LinearLayout back;
    private Button comitBtn;
    private TextView moblie,getCode;
    private EditText codeEdt,passEdt;
    private int recLen = 60;
    private TimerTask task;
    private Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatepass_view);
        initView();
        initListener();
        initData();
    }

    private void initData() {
        if(BaseUtil.userData!=null){
            moblie.setText(BaseUtil.userData.getMobile()+"");
        }
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
                if(TextUtils.isEmpty(codeEdt.getText().toString())){
                    Toast.makeText(UpdatePassActivity.this,"请输入验证码",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(passEdt.getText().toString())){
                    Toast.makeText(UpdatePassActivity.this,"请输入新密码",Toast.LENGTH_SHORT).show();
                    return;
                }
                comitFeedBack();
            }
        });
        getCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCodeData(moblie.getText().toString());
            }
        });
    }

    private void initView(){
        back=findViewById(R.id.updatepass_top_back);
        moblie=findViewById(R.id.updatepass_item_Mobile);
        getCode=findViewById(R.id.updatepass_item_getCode);
        codeEdt=findViewById(R.id.wsgrxx_item_code);
        passEdt=findViewById(R.id.updatepass_item_setpass);
        comitBtn=findViewById(R.id.updatepass_btn);
    }
    //修改密码接口
    private void comitFeedBack(){
        UserApiManager.modifypassword(new GWResponseListener() {
            @Override
            public void successResult(Serializable result, String path, int requestCode, int resultCode) {
                ResponseResult<RegisterRespData, RespObjBase> responseResult = (ResponseResult<RegisterRespData, RespObjBase>) result;
                RegisterRespData data = responseResult.getData();
                BaseUtil.UserId=data.getUserId();//密码修改应该相当于换了个userID
                Toast.makeText(UpdatePassActivity.this,"密码修改成功",Toast.LENGTH_SHORT).show();
                finish();
            }
            @Override
            public void errorResult(Serializable result, String path, int requestCode, int resultCode) {
                Toast.makeText(UpdatePassActivity.this,"修改密码失败",Toast.LENGTH_SHORT).show();
            }
        }, BaseUtil.UserId,moblie.getText().toString(),codeEdt.getText().toString(),passEdt.getText().toString());
    }
    //获取验证码
    private void getCodeData(final String tel) {
        if(TextUtils.isEmpty(tel)){
            Toast.makeText(getApplicationContext(), "手机号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        DialogUtil.getInstance().showProgressDialog(this);
        UserApiManager.sendSmsVerfyCode(new GWResponseListener() {
            @Override
            public void successResult(Serializable result, String path, int requestCode, int resultCode) {
                DialogUtil.getInstance().cancelProgressDialog();
                ResponseResult<String, RespObjBase> responseResult = (ResponseResult<String, RespObjBase>) result;
                taskDate();
                Toast.makeText(getApplicationContext(), "发送验证码成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void errorResult(Serializable result, String path, int requestCode, int resultCode) {
                DialogUtil.getInstance().cancelProgressDialog();
                Toast.makeText(getApplicationContext(), "发送验证码失败", Toast.LENGTH_SHORT).show();
            }
        }, tel);
    }
    private void taskDate(){
        timer=new Timer();
        recLen=60;
        task = new TimerTask() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {      // UI thread
                    @Override
                    public void run() {
                        recLen--;
                        getCode.setText(recLen+"s后重新获取");
                        if(recLen < 0){
                            timer.cancel();
                            task.cancel();
                            getCode.setClickable(true);
                            getCode.setText("获取验证码");
                        }
                    }
                });
            }
        };
        timer.schedule(task, 1000, 1000);
    }
}
