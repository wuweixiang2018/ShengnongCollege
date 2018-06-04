package com.education.shengnongcollege.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.education.shengnongcollege.BaseTopActivity;
import com.education.shengnongcollege.R;
import com.education.shengnongcollege.api.UserApiManager;
import com.education.shengnongcollege.model.LoginRespData;
import com.education.shengnongcollege.model.RegisterRespData;
import com.education.shengnongcollege.model.RespObjBase;
import com.education.shengnongcollege.network.listener.GWResponseListener;
import com.education.shengnongcollege.network.model.ResponseResult;
import com.education.shengnongcollege.utils.BaseUtil;
import com.education.shengnongcollege.utils.Ilisten.ListenerManager;
import com.education.shengnongcollege.view.VerifyCodeView;
import com.education.shengnongcollege.widget.DialogUtil;

import java.io.Serializable;

//注册页面
public class RegisterActivity extends BaseTopActivity {
    private EditText telEdit,passWordSet;
    private Button getcodeBtn,sureBtn;
    private ImageView sure,cancle;
    private boolean isSure=false;//是否同意了协议
    private TextView tips,codeTip;
    private LinearLayout getCodeLayout,verifyCodeLayout,setPassWordLayout;
    private VerifyCodeView codeView;
    private String TelVerCode="";
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
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSure=false;
                sure.setVisibility(View.GONE);
                cancle.setVisibility(View.VISIBLE);
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSure=true;
                sure.setVisibility(View.VISIBLE);
                cancle.setVisibility(View.GONE);
            }
        });
        getcodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isSure){
                    Toast.makeText(RegisterActivity.this,"请同意协议",Toast.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(telEdit.getText().toString())){
                    Toast.makeText(RegisterActivity.this,"手机号不能为空",Toast.LENGTH_LONG).show();
                    return;
                }
                if(!BaseUtil.isMobileNO(telEdit.getText().toString())){
                    Toast.makeText(RegisterActivity.this,"您输入的不是手机号,请检查。",Toast.LENGTH_LONG).show();
                    return;
                }
                getCodeData(telEdit.getText().toString());
            }
        });
        sureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //最后一步完成
                if(TextUtils.isEmpty(passWordSet.getText().toString())){
                    Toast.makeText(RegisterActivity.this,"密码不能为空",Toast.LENGTH_LONG).show();
                    return;
                }
                sureRegister();
            }
        });
    }

    private void initView(){
        telEdit=findViewById(R.id.register_tel);
        passWordSet=findViewById(R.id.register_setpassword);
        getcodeBtn=findViewById(R.id.register_getcode_btn);
        sureBtn=findViewById(R.id.register_sure_btn);
        sure=findViewById(R.id.register_sure);
        cancle=findViewById(R.id.register_cancle);
        tips=findViewById(R.id.register_tip);
        codeTip=findViewById(R.id.verify_code_view_tv);
        getCodeLayout=findViewById(R.id.get_code_view_layout);
        verifyCodeLayout=findViewById(R.id.verify_code_view_layout);
        setPassWordLayout=findViewById(R.id.set_password_view_layout);
        codeView =findViewById(R.id.verify_code_view);
        codeView.setInputCompleteListener(new VerifyCodeView.InputCompleteListener() {
            @Override
            public void inputComplete() {
                tips.setText("请设置密码");
                TelVerCode=codeView.getEditContent();
                relushView();
            }

            @Override
            public void invalidContent() {

            }
        });


    }
    //获取验证码
    private void getCodeData(final String tel) {
        DialogUtil.getInstance().showProgressDialog(this);
        UserApiManager.sendSmsVerfyCode(new GWResponseListener() {
            @Override
            public void successResult(Serializable result, String path, int requestCode, int resultCode) {
                DialogUtil.getInstance().cancelProgressDialog();
                ResponseResult<String, RespObjBase> responseResult = (ResponseResult<String, RespObjBase>) result;
                Toast.makeText(getApplicationContext(), "发送验证码成功" + responseResult.getData(), Toast.LENGTH_SHORT).show();
                tips.setText("请输入验证码");
                codeTip.setText("验证码已发送手机 "+tel);
                relushView();
            }

            @Override
            public void errorResult(Serializable result, String path, int requestCode, int resultCode) {
                DialogUtil.getInstance().cancelProgressDialog();
                Toast.makeText(getApplicationContext(), "发送验证码失败", Toast.LENGTH_SHORT).show();
            }
        }, tel);
    }
    private void relushView(){
        switch (tips.getText().toString()){
            case "注册":
                getCodeLayout.setVisibility(View.VISIBLE);
                verifyCodeLayout.setVisibility(View.INVISIBLE);
                setPassWordLayout.setVisibility(View.INVISIBLE);
                break;
            case "请输入验证码":
                getCodeLayout.setVisibility(View.INVISIBLE);
                verifyCodeLayout.setVisibility(View.VISIBLE);
                setPassWordLayout.setVisibility(View.INVISIBLE);
                break;
            case "请设置密码":
                getCodeLayout.setVisibility(View.INVISIBLE);
                verifyCodeLayout.setVisibility(View.INVISIBLE);
                setPassWordLayout.setVisibility(View.VISIBLE);
                break;
        }
    }
    @Override
    public void onBackPressed() {
        switch (tips.getText().toString()){
            case "注册":
                finish();
                break;
            case "请输入验证码":
                tips.setText("注册");
                relushView();
                break;
            case "请设置密码":
                tips.setText("请输入验证码");
                relushView();
                break;
        }
    }
    //注册最后一步
    private void  sureRegister() {
        DialogUtil.getInstance().showProgressDialog(this);
        UserApiManager.getUserSign(new GWResponseListener() {
            @Override
            public void successResult(Serializable result, String path, int requestCode, int resultCode) {
                //云通信票据获取了 进行注册
                ResponseResult<String, RespObjBase> responseResult = (ResponseResult<String, RespObjBase>) result;
                String userSign = responseResult.getData();
                UserApiManager.registerUser(new GWResponseListener() {
                    @Override
                    public void successResult(Serializable result, String path, int requestCode, int resultCode) {
                        ResponseResult<RegisterRespData, RespObjBase> responseResult = (ResponseResult<RegisterRespData, RespObjBase>) result;
                        RegisterRespData data = responseResult.getData();
                        if(data!=null&&!TextUtils.isEmpty(data.getUserId())){//判断数据不为空 userid不为空 那就是注册成功
                            //注册成功了 虽然字段都有 为确保万一 还是进行一遍登录
                            UserApiManager.login(new GWResponseListener() {
                                @Override
                                public void successResult(Serializable result, String path, int requestCode, int resultCode) {
                                    DialogUtil.getInstance().cancelProgressDialog();
                                    ResponseResult<LoginRespData, RespObjBase> responseResult = (ResponseResult<LoginRespData, RespObjBase>) result;
                                    LoginRespData data = responseResult.getData();
                                    BaseUtil.UserId=data.getUserId();
                                    BaseUtil.Online=data.getOnline();
                                    finish();
                                    ListenerManager.getInstance().sendBroadCast("MineFragmentReflush","");//通知分类页面也加载页面
                                }

                                @Override
                                public void errorResult(Serializable result, String path, int requestCode, int resultCode) {
                                    DialogUtil.getInstance().cancelProgressDialog();
                                    Toast.makeText(getApplicationContext(), "登录用户失败", Toast.LENGTH_SHORT).show();
                                }
                            }, telEdit.getText().toString(), passWordSet.getText().toString());
                        }

                    }

                    @Override
                    public void errorResult(Serializable result, String path, int requestCode, int resultCode) {
                        DialogUtil.getInstance().cancelProgressDialog();
                        Toast.makeText(getApplicationContext(), "注册用户失败", Toast.LENGTH_SHORT).show();
                    }
                }, telEdit.getText().toString(), telEdit.getText().toString(), TelVerCode, passWordSet.getText().toString(), userSign);
            }

            @Override
            public void errorResult(Serializable result, String path, int requestCode, int resultCode) {
                DialogUtil.getInstance().cancelProgressDialog();
                Toast.makeText(getApplicationContext(), "云通信票据获取失败", Toast.LENGTH_SHORT).show();
            }
        }, telEdit.getText().toString());

    }
}
