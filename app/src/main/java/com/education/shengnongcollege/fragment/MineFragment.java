package com.education.shengnongcollege.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
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
import com.education.shengnongcollege.utils.CacheUtil;
import com.education.shengnongcollege.utils.Ilisten.IListener;
import com.education.shengnongcollege.utils.Ilisten.ListenerManager;
import com.education.shengnongcollege.widget.DialogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

/**
 * 我的页面
 *
 * @author shihaoxian
 * @date 2018/4/12
 */
public class MineFragment extends BaseFragment implements IListener {

    private View mFragmentView;
    private LinearLayout wsgrxxLayout,xgMmLayout,yjfkLayout,aboutMeLayout;
    private ImageView userImage;
    private TextView registerTv;
    private Button exitLogin,signIn;
    private boolean isSignIn=false;//是否已经签到过 根据接口判断
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
            getCurdaySignin();//获取签到状态

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
        signIn=mFragmentView.findViewById(R.id.mine_sign_in_btn);
        registerTv.setVisibility(View.INVISIBLE);//登录按钮暂时就不要了
        exitLogin.setVisibility(View.VISIBLE);//退出登录显示出来
        CacheUtil.getInstance().init(getActivity());
        ListenerManager.getInstance().registerListtener(this);
    }

    private void initListener() {
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowChoisePhoto();
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
        //签到按钮
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isSignIn==false){//一直等于false 就调用签到
                    getSignIn();
                }
            }
        });
    }
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
                CacheUtil.getInstance().clearData();
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

    @Override
    public void notifyAllActivity(String str, Object object) {
        if(getActivity()!=null){
            if(TextUtils.equals(str,"MineFragment")){
                getUserInfoById();
            }
        }
    }

    public static final int REQUEST_CHOOSE_IMAGE = 50;
    public static final int REQUEST_TAKE_PHOTO = 51;
    private String mCurrentPhotoPath="";//图片本地路径
    //选择相册还是相机
    private void ShowChoisePhoto()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("选择");
        //    指定下拉列表的显示数据
        final String[] cities = {"相机", "媒体库"};
        //    设置一个下拉的列表选择项
        builder.setItems(cities, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                switch (which){
                    case 0:
                        String picName = System.currentTimeMillis() + ".jpg";
                        Intent intent = new Intent();
                        intent.setAction(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        if(intent.resolveActivity(getActivity().getPackageManager()) != null){
                            File image=new File(getImagePath(), picName);
                            mCurrentPhotoPath = image.getAbsolutePath();
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                                    .fromFile(image));
                            startActivityForResult(intent,REQUEST_TAKE_PHOTO);
                        }else{
                            DialogUtil.showCustomToast(getActivity(), "没有相机权限", Gravity.CENTER);
                        }
                        break;
                    case 1:
                        //调用相册
                        Intent albumIntent = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(albumIntent, REQUEST_CHOOSE_IMAGE);
                        break;
                }

            }
        });
        builder.create().show();
    }
    /**
     * 获取图片文件本地路径
     * @return
     */
    public String getImagePath(){
        File file = new File(DIRPATH_IMAGE);
        if (!file.exists()) {
            file.mkdirs();
        }
        return DIRPATH_IMAGE;
    }
    public static final String DIRPATH_IMAGE = Environment
            .getExternalStorageDirectory().getPath()
            + "/"
            + "shennong"
            + "/"
            + "images" + "/";

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 回调成功
        if (resultCode == RESULT_OK) {
            String filePath = null;
            //判断是哪一个的回调
            if (requestCode == REQUEST_CHOOSE_IMAGE) {
                //返回的是content://的样式
                filePath = BaseUtil.getFilePathFromContentUri(data.getData(), getActivity());
                mCurrentPhotoPath=filePath;
            } else if (requestCode == REQUEST_TAKE_PHOTO) {//相机
                if (mCurrentPhotoPath != null) {
                    filePath = mCurrentPhotoPath;
                }
            }
            Log.e("图片照相或者选择图片返回来的",mCurrentPhotoPath);
            if (!TextUtils.isEmpty(filePath)) {
                // 自定义大小，防止OOM
                Bitmap bitmap = BaseUtil.getSmallBitmap(filePath, 200, 200);
                userImage.setImageBitmap(bitmap);
                DialogUtil.getInstance().showProgressDialog(getActivity());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        uploadFile(new File(mCurrentPhotoPath));
                    }
                }).start();
            }
        }
    }
    private static final int TIME_OUT = 10 * 10000000; // 超时时间
    private static final String CHARSET = "utf-8"; // 设置编码
    public static final String SUCCESS = "1";
    public static final String FAILURE = "0";

    public String uploadFile(File file) {
        String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
        String PREFIX = "--", LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data"; // 内容类型
        String RequestURL = "http://api.liveeducation.ymstudio.xyz/api/user/updatephotograph?UserId="+BaseUtil.UserId;
        try {
            URL url = new URL(RequestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIME_OUT);
            conn.setConnectTimeout(TIME_OUT);
            conn.setDoInput(true); // 允许输入流
            conn.setDoOutput(true); // 允许输出流
            conn.setUseCaches(false); // 不允许使用缓存
            conn.setRequestMethod("POST"); // 请求方式
            conn.setRequestProperty("Charset", CHARSET); // 设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="
                    + BOUNDARY);
            if (file != null) {
                /**
                 * 当文件不为空，把文件包装并且上传
                 */
                OutputStream outputSteam = conn.getOutputStream();

                DataOutputStream dos = new DataOutputStream(outputSteam);
                StringBuffer sb = new StringBuffer();
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                /**
                 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
                 * filename是文件的名字，包含后缀名的 比如:abc.png
                 */

                sb.append("Content-Disposition: form-data; name=\"img\"; filename=\""
                        + file.getName() + "\"" + LINE_END);
                sb.append("Content-Type: application/octet-stream; charset="
                        + CHARSET + LINE_END);
                sb.append(LINE_END);
                dos.write(sb.toString().getBytes());
                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.write(LINE_END.getBytes());
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
                        .getBytes();
                dos.write(end_data);
                dos.flush();
                /**
                 * 获取响应码 200=成功 当响应成功，获取响应的流
                 */
                int res = conn.getResponseCode();
                if (res == 200) {
                    //得到响应流
                    InputStream inputStream = conn.getInputStream();
                    //获取响应
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder builder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null){
                        builder.append(line).append("\n");
                    }
                    reader.close();
                    //该干的都干完了,记得把连接断了
                    conn.disconnect();
                    Log.e("上传返回数据",builder.toString());
                    //{"status_code":200,"status_message":"操作成功","timestamp":1528113955,"data":"http://api.liveeducation.ymstudio.xyz/upload//2018-06-04//20180604200555334.jpg","obj":null}
                    JSONObject jsonObject=new JSONObject(builder.toString());
                    if(jsonObject.has("data")){
                        String data=jsonObject.getString("data");
                        if(TextUtils.isEmpty(data)){
                            return FAILURE;
                        }else{
                            getUserInfoById();
                        }
                    }
                    return SUCCESS;
                }else{
                    return FAILURE;
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            DialogUtil.getInstance().cancelProgressDialog();
        }
        return FAILURE;
    }
    //签到
    private void getSignIn(){
        DialogUtil.getInstance().showProgressDialog(getActivity());
        UserApiManager.getSignIn(new GWResponseListener() {
            @Override
            public void successResult(Serializable result, String path, int requestCode, int resultCode) {
                DialogUtil.getInstance().cancelProgressDialog();
                ResponseResult<String, RespObjBase> responseResult = (ResponseResult<String, RespObjBase>) result;
                String data = responseResult.getData();
                getCurdaySignin();
                Toast.makeText(getActivity(),"签到成功。",Toast.LENGTH_SHORT).show();
                Log.e("签到信息返回",""+data);
            }
            @Override
            public void errorResult(Serializable result, String path, int requestCode, int resultCode) {
                DialogUtil.getInstance().cancelProgressDialog();
                Toast.makeText(getActivity(),"签到失败",Toast.LENGTH_SHORT).show();
            }
        }, BaseUtil.UserId);
    }
    //获取签到状态
    private void getCurdaySignin(){
        UserApiManager.getCurdaySignin(new GWResponseListener() {
            @Override
            public void successResult(Serializable result, String path, int requestCode, int resultCode) {
                ResponseResult<String, RespObjBase> responseResult = (ResponseResult<String, RespObjBase>) result;
                String data = responseResult.getData();
                if(TextUtils.equals(data+"","true")){
                    isSignIn=true;
                    signIn.setBackgroundResource(R.drawable.sign_in_nor);
                    signIn.setTextColor(Color.parseColor("#000000"));
                }else{
                    isSignIn=false;
                    signIn.setTextColor(Color.parseColor("#FFFFFF"));
                    signIn.setBackgroundResource(R.drawable.log_btn_pre);
                }
                signIn.setVisibility(View.VISIBLE);
                Log.e("签到信息返回",""+data);
            }
            @Override
            public void errorResult(Serializable result, String path, int requestCode, int resultCode) {
                isSignIn=false;
                Toast.makeText(getActivity(),"签到信息获取失败",Toast.LENGTH_SHORT).show();
            }
        }, BaseUtil.UserId);
    }

}
