package com.education.shengnongcollege.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.education.shengnongcollege.BaseTopActivity;
import com.education.shengnongcollege.R;
import com.education.shengnongcollege.api.UserApiManager;
import com.education.shengnongcollege.model.RespObjBase;
import com.education.shengnongcollege.network.listener.GWResponseListener;
import com.education.shengnongcollege.network.model.ResponseResult;
import com.education.shengnongcollege.utils.BaseUtil;
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
import java.util.Calendar;
import java.util.UUID;

//完善个人信息页面
public class PerfectinfoActivity extends BaseTopActivity implements DatePicker.OnDateChangedListener{
    private String UserName,  NickName,  Gender,  Birthday,  Mobile,  IDCard,  PhotoGraph;
    private EditText userName,nickName,mobile,idCard;
    private LinearLayout back;
    private TextView uploadImage,genDer,birThDay;
    private ImageView  userImage;
    private RelativeLayout genDerChoose;
    private Button sureBtn;
    public static final int REQUEST_CHOOSE_IMAGE = 50;
    public static final int REQUEST_TAKE_PHOTO = 51;
    private String mCurrentPhotoPath="";//图片本地路径
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wsgrxx_view);
        initView();
        initListener();
        initData();
    }

    private void initData() {

    }

    private void initListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //选择图片按钮
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowChoisePhoto();
            }
        });
        //选择生日
        birThDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseBirthDay();
            }
        });
        genDerChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseGenDer();
            }
        });
        sureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserName=userName.getText().toString();
                NickName=nickName.getText().toString();
                Gender=genDer.getText().toString();
                Birthday=birThDay.getText().toString();
                Mobile=mobile.getText().toString();
                IDCard=idCard.getText().toString();
                perFectinfoData();
            }
        });
    }
    private void chooseGenDer(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示：");
        builder.setMessage("请选择性别？");
        builder.setIcon(R.mipmap.ic_launcher_round);
        //点击对话框以外的区域是否让对话框消失
        builder.setCancelable(true);
        //设置正面按钮
        builder.setPositiveButton("男", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                genDer.setText("男");
                dialog.dismiss();
            }
        });
        //设置反面按钮
        builder.setNegativeButton("女", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                genDer.setText("女");
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        //显示对话框
        dialog.show();
    }

    private int year, month, day;
    private StringBuffer date;
    private void chooseBirthDay(){
        initDateTime();
        date = new StringBuffer();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (date.length() > 0) { //清除上次记录的日期
                    date.delete(0, date.length());
                }
                birThDay.setText(date.append(String.valueOf(year)).append("-").append(String.valueOf(month)).append("-").append(day));
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final AlertDialog dialog = builder.create();
        View dialogView = View.inflate(this, R.layout.dialog_date, null);
        final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.datePicker);
        dialog.setTitle("设置日期");
        dialog.setView(dialogView);
        dialog.show();
        //初始化日期监听事件
        datePicker.init(year, month - 1, day, this);
    }
    /**
     * 获取当前的日期和时间
     */
    private void initDateTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }
    //选择相册还是相机
    private void ShowChoisePhoto()
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                        if(intent.resolveActivity(PerfectinfoActivity.this.getPackageManager()) != null){
                            File image=new File(getImagePath(), picName);
                            mCurrentPhotoPath = image.getAbsolutePath();
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                                    .fromFile(image));
                            startActivityForResult(intent,REQUEST_TAKE_PHOTO);
                        }else{
                            DialogUtil.showCustomToast(PerfectinfoActivity.this, "没有相机权限", Gravity.CENTER);
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 回调成功
        if (resultCode == RESULT_OK) {
            String filePath = null;
            //判断是哪一个的回调
            if (requestCode == REQUEST_CHOOSE_IMAGE) {
                //返回的是content://的样式
                filePath = getFilePathFromContentUri(data.getData(), this);
                mCurrentPhotoPath=filePath;
            } else if (requestCode == REQUEST_TAKE_PHOTO) {//相机
                if (mCurrentPhotoPath != null) {
                    filePath = mCurrentPhotoPath;
                }
            }
            Log.e("图片照相或者选择图片返回来的",mCurrentPhotoPath);
            if (!TextUtils.isEmpty(filePath)) {
                // 自定义大小，防止OOM
                Bitmap bitmap = getSmallBitmap(filePath, 200, 200);
                userImage.setImageBitmap(bitmap);
                DialogUtil.getInstance().showProgressDialog(PerfectinfoActivity.this);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        uploadFile(new File(mCurrentPhotoPath));
                    }
                }).start();
            }
        }
    }
    /**
     * 获取小图片，防止OOM
     *
     * @param filePath
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            BitmapFactory.decodeFile(filePath, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }
    /**
     * 计算图片缩放比例
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }
    /**
     * @param uri     content:// 样式
     * @param context
     * @return real file path
     */
    public static String getFilePathFromContentUri(Uri uri, Context context) {
        String filePath;
        String[] filePathColumn = {MediaStore.MediaColumns.DATA};
        Cursor cursor = context.getContentResolver().query(uri, filePathColumn, null, null, null);
        if (cursor == null) return null;
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }

    private void initView(){
        back=findViewById(R.id.wsgrxx_top_back);
        uploadImage=findViewById(R.id.wsgrxx_top_upload);
        userImage=findViewById(R.id.wsgrxx_top_userImage);
        userName=findViewById(R.id.wsgrxx_item_UserName);
        nickName=findViewById(R.id.wsgrxx_item_NickName);
        mobile=findViewById(R.id.wsgrxx_item_Mobile);
        idCard=findViewById(R.id.wsgrxx_item_IDCard);
        birThDay=findViewById(R.id.wsgrxx_item_Birthday);
        genDer=findViewById(R.id.wsgrxx_item_Gender);
        genDerChoose=findViewById(R.id.wsgrxx_item_Gender_choose);
        sureBtn=findViewById(R.id.wsgrxx_btn);
    }
    //完善信息接口
    private void perFectinfoData() {
        DialogUtil.getInstance().showProgressDialog(PerfectinfoActivity.this);
        UserApiManager.perfectinfo(new GWResponseListener() {
            @Override
            public void successResult(Serializable result, String path, int requestCode, int resultCode) {
                DialogUtil.getInstance().cancelProgressDialog();
                ResponseResult<String, RespObjBase> responseResult = (ResponseResult<String, RespObjBase>) result;
                String data=responseResult.getData();
                if(!TextUtils.isEmpty(data)&&TextUtils.equals(data,"1")){
                    Toast.makeText(getApplicationContext(), "完善信息成功", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "完善信息失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void errorResult(Serializable result, String path, int requestCode, int resultCode) {
                DialogUtil.getInstance().cancelProgressDialog();
                Toast.makeText(getApplicationContext(), "请求失败", Toast.LENGTH_SHORT).show();
            }
        }, BaseUtil.UserId,UserName,  NickName,  Gender,  Birthday,  Mobile,  IDCard,  PhotoGraph);
    }

    @Override
    public void onDateChanged(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.month = monthOfYear;
        this.day = dayOfMonth;
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
    private static final int TIME_OUT = 10 * 10000000; // 超时时间
    private static final String CHARSET = "utf-8"; // 设置编码
    public static final String SUCCESS = "1";
    public static final String FAILURE = "0";

    public String uploadFile(File file) {
        String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
        String PREFIX = "--", LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data"; // 内容类型
        String RequestURL = "http://api.liveeducation.ymstudio.xyz/api/common/uploadimage";
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
                            PhotoGraph=data;
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
}
