package com.education.shengnongcollege.push;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.education.shengnongcollege.R;
import com.education.shengnongcollege.api.LiveBroadcastApiManager;
import com.education.shengnongcollege.common.activity.VideoPublishBaseActivity;
import com.education.shengnongcollege.common.widget.BeautySettingPannel;
import com.education.shengnongcollege.model.GetPushFlowPlayUrlRespData;
import com.education.shengnongcollege.model.RespObjBase;
import com.education.shengnongcollege.network.listener.GWResponseListener;
import com.education.shengnongcollege.network.model.ResponseResult;
import com.education.shengnongcollege.utils.BaseUtil;
import com.education.shengnongcollege.utils.ImageLoadManager;
import com.education.shengnongcollege.widget.DialogUtil;
import com.tencent.rtmp.ITXLivePushListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.TXLivePusher;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.tencent.ugc.TXRecordCommon;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 自己的直播
 */
public class LivePublisherActivity extends VideoPublishBaseActivity implements View.OnClickListener, ITXLivePushListener, BeautySettingPannel.IOnBeautyParamsChangeListener/*, ImageReader.OnImageAvailableListener*/ {
    private static final String TAG = LivePublisherActivity.class.getSimpleName();


    private TXLivePushConfig mLivePushConfig;
    private TXLivePusher mLivePusher;
    private TXCloudVideoView mCaptureView;

    private LinearLayout mBitrateLayout;
    private BeautySettingPannel mBeautyPannelView;
    private RadioGroup mRadioGroupBitrate;
    private Button mBtnBitrate;
    private Button mBtnPlay;
    private Button mBtnFaceBeauty;
    private Button mBtnTouchFocus;
    private Button mBtnHWEncode;
    private Button mBtnOrientation;

    //开始录制logo，点击后显示，开始录制布局
    private ImageView startRecordLogoIV;

    //录制直播
    private TextView recordLiveBroadcastTV;
    //退出录制直播
    private TextView exitRecordTV;

    //开始直播
    private ImageView startPublisherIV;

    //录制时间
    private TextView recordTimeTV;

    private TextView roomNoTV;

    //顶部布局
    private RelativeLayout topRL;
    //录制布局
    private RelativeLayout recordRL;
    //录制时间布局
    private RelativeLayout timeRL;

    private RelativeLayout commentRL;

    protected EditText mRtmpUrlView;
    private boolean mPortrait = true;         //手动切换，横竖屏推流
    private boolean mFrontCamera = true;

    private boolean mVideoPublish;

    private int mVideoSrc = VIDEO_SRC_CAMERA;
    private boolean mHWVideoEncode = true;
    private boolean mTouchFocus = true;
    private boolean mHWListConfirmDialogResult = false;
    private int mBeautyLevel = 5;
    private int mWhiteningLevel = 3;
    private int mRuddyLevel = 2;
    private int mBeautyStyle = TXLiveConstants.BEAUTY_STYLE_SMOOTH;

    private Handler mHandler = new Handler();

    private Bitmap mBitmap;

    private static final int VIDEO_SRC_CAMERA = 0;
    private static final int VIDEO_SRC_SCREEN = 1;
    private int mCurrentVideoResolution = TXLiveConstants.VIDEO_RESOLUTION_TYPE_360_640;

    private int mNetBusyCount = 0;
    private Handler mMainHandler;
    private TextView mNetBusyTips;
    LinearLayout mBottomLinear = null;

    private int mVideoQuality = TXLiveConstants.VIDEO_QUALITY_STANDARD_DEFINITION;
    private boolean mAutoBitrate = false;
    private boolean mAutoResolution = false;
    private Button mBtnAutoResolution = null;
    private Button mBtnAutoBitrate = null;
    private boolean mIsRealTime = false;
    public static final int ACTIVITY_TYPE_PUBLISH = 1;
    public static final int ACTIVITY_TYPE_LIVE_PLAY = 2;
    public static final int ACTIVITY_TYPE_VOD_PLAY = 3;
    public static final int ACTIVITY_TYPE_LINK_MIC = 4;

//    private final int           REQUEST_CODE_CS = 10001;

//    private MediaProjectionManager      mProjectionManager;
//    private MediaProjection             mMediaProjection;
//    private VirtualDisplay mVirtualDisplay         = null;
//    private ImageReader mImageReader            = null;

    private boolean mStartRecord = false;
    private ProgressBar mRecordProgressBar;
//    private TextView mRecordTimeTV;

    //获取推流地址
    private OkHttpClient mOkHttpClient = null;
    private boolean mFetching = false;
    private ProgressDialog mFetchProgressDialog;

    // 关注系统设置项“自动旋转”的状态切换
    private RotationObserver mRotationObserver = null;
    private boolean mIsLogShow = false;

    //推流地址
    private String pushUrl;

    private Bitmap decodeResource(Resources resources, int id) {
        TypedValue value = new TypedValue();
        resources.openRawResource(id, value);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inTargetDensity = value.density;
        return BitmapFactory.decodeResource(resources, id, opts);
    }

    private void backprocess() {
        if (mVideoPublish) {
            stopPublishRtmp();
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backprocess();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLivePusher = new TXLivePusher(this);
        mLivePushConfig = new TXLivePushConfig();
        mLivePushConfig.setVideoEncodeGop(5);
        mLivePushConfig.setBeautyFilter(mBeautyLevel, mWhiteningLevel, mRuddyLevel);
        mLivePusher.setConfig(mLivePushConfig);

        mBitmap = decodeResource(getResources(), R.drawable.watermark);

        mRotationObserver = new RotationObserver(new Handler());
        mRotationObserver.startObserver();

        mMainHandler = new Handler(Looper.getMainLooper());

        setContentView();

        LinearLayout backLL = (LinearLayout) findViewById(R.id.back_ll);
        backLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVideoPublish) {
                    DialogUtil.getInstance().showProgressDialog(getApplicationContext());
                    LiveBroadcastApiManager.closeLVB(new GWResponseListener() {
                        @Override
                        public void successResult(Serializable result, String path, int requestCode, int resultCode) {
                            DialogUtil.getInstance().cancelProgressDialog();
                            ResponseResult<Boolean, RespObjBase> responseResult = (ResponseResult<Boolean, RespObjBase>) result;
                            boolean closeResult = (Boolean) responseResult.getData();
                            if (closeResult) {
                                Toast.makeText(getApplicationContext(), "关闭直播间成功", Toast.LENGTH_SHORT).show();
                                backprocess();
                            } else
                                Toast.makeText(getApplicationContext(), "关闭直播间失败", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void errorResult(Serializable result, String path, int requestCode, int resultCode) {
                            DialogUtil.getInstance().cancelProgressDialog();
                            Toast.makeText(getApplicationContext(), "关闭直播间失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    backprocess();
                }


            }
        });
//        TextView titleTV = (TextView) findViewById(R.id.title_tv);
//        titleTV.setText(getIntent().getStringExtra("TITLE"));

        mBottomLinear = (LinearLayout) findViewById(R.id.btns_tests);

        checkPublishPermission();

        getWindow().addFlags(WindowManager.LayoutParams.
                FLAG_KEEP_SCREEN_ON);

        mPhoneListener = new TXPhoneStateListener(mLivePusher);
        TelephonyManager tm = (TelephonyManager) getApplicationContext().getSystemService(Service.TELEPHONY_SERVICE);
        tm.listen(mPhoneListener, PhoneStateListener.LISTEN_CALL_STATE);

        LiveBroadcastApiManager.getPushFlowPlayUrl(new GWResponseListener() {
            @Override
            public void successResult(Serializable result, String path, int requestCode, int resultCode) {
                ResponseResult<GetPushFlowPlayUrlRespData, RespObjBase> responseResult = (ResponseResult<GetPushFlowPlayUrlRespData, RespObjBase>) result;
                GetPushFlowPlayUrlRespData data = responseResult.getData();
                pushUrl = data.getPushUrl();
                mRtmpUrlView.setText(pushUrl);

                roomNoTV.setText(data.getRoomNo());

//                mLivePusher.startScreenCapture();

                publishRtmpPrepare();
//                startPublishRtmp();
//                findViewById(R.id.record_layout).setVisibility(View.VISIBLE);
            }

            @Override
            public void errorResult(Serializable result, String path, int requestCode, int resultCode) {

            }
        }, BaseUtil.UserId, "", "");
    }


    protected void initView() {
        mRtmpUrlView = (EditText) findViewById(R.id.roomid);

        Button scanBtn = (Button) findViewById(R.id.btnScan);
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(LivePublisherActivity.this, QRCodeScanActivity.class);
//                startActivityForResult(intent, 100);
            }
        });
        scanBtn.setEnabled(true);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) scanBtn.getLayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            params.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        }
        scanBtn.setLayoutParams(params);
    }

    public static void scroll2Bottom(final ScrollView scroll, final View inner) {
        if (scroll == null || inner == null) {
            return;
        }
        int offset = inner.getMeasuredHeight() - scroll.getMeasuredHeight();
        if (offset < 0) {
            offset = 0;
        }
        scroll.scrollTo(0, offset);
    }

    public void setContentView() {
        super.setContentView(R.layout.activity_publish);

        initView();

        TextView nameTV = findViewById(R.id.name_tv);
        if (BaseUtil.userData != null)
            nameTV.setText(BaseUtil.userData.getRealName());
        else {
            nameTV.setText("");
        }

        roomNoTV = findViewById(R.id.room_num_tv);
        roomNoTV.setText("");

        if (BaseUtil.userData != null) {
            String avatar = BaseUtil.userData.getPhotograph();
//        avatar="http://imgsrc.baidu.com/forum/w=580/sign=1588b7c5d739b6004dce0fbfd9503526/7bec54e736d12f2eb97e1a464dc2d56285356898.jpg";
            if (!TextUtils.isEmpty(avatar)) {
                ImageView avatarIV = findViewById(R.id.avatar_iv);
                ImageLoadManager.loadImageRounded(this, avatar, avatarIV,
                        R.drawable.default_avatar, 360);
            }
        }

        mCaptureView = (TXCloudVideoView) findViewById(R.id.video_view);
        mCaptureView.setLogMargin(12, 12, 110, 60);

        mNetBusyTips = (TextView) findViewById(R.id.netbusy_tv);
        mVideoPublish = false;

        mRtmpUrlView.setHint(" 请输入或扫二维码获取推流地址");
        mRtmpUrlView.setText("");

        Button btnNew = (Button) findViewById(R.id.btnNew);
        btnNew.setVisibility(View.VISIBLE);
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchPushUrl();
            }
        });

        mRecordProgressBar = (ProgressBar) findViewById(R.id.record_progress);
//        mRecordTimeTV = (TextView) findViewById(R.id.record_time);

        //美颜p图部分
        mBeautyPannelView = (BeautySettingPannel) findViewById(R.id.layoutFaceBeauty);
        mBeautyPannelView.setBeautyParamsChangeListener(this);

        mBtnFaceBeauty = (Button) findViewById(R.id.btnFaceBeauty);
        mBtnFaceBeauty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBeautyPannelView.setVisibility(mBeautyPannelView.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                mBottomLinear.setVisibility(mBeautyPannelView.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            }
        });

        //播放部分
        mBtnPlay = (Button) findViewById(R.id.btnPlay);
        mBtnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mVideoPublish) {
                    stopPublishRtmp();
                } else {
                    if (mVideoSrc == VIDEO_SRC_CAMERA) {
                        FixOrAdjustBitrate();  //根据设置确定是“固定”还是“自动”码率
                    } else {
                        //录屏横竖屏采用两种分辨率，和摄像头推流逻辑不一样
                    }
                    mVideoPublish = startPublishRtmp();
                }
            }
        });


        //log部分
        final Button btnLog = (Button) findViewById(R.id.btnLog);
        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsLogShow) {
                    mIsLogShow = false;
                    btnLog.setBackgroundResource(R.drawable.log_show);
                    mCaptureView.showLog(false);
                } else {
                    mIsLogShow = true;
                    btnLog.setBackgroundResource(R.drawable.log_hidden);
                    mCaptureView.showLog(true);
                }
            }
        });

        //切换前置后置摄像头
        final ImageView changeCamIV = findViewById(R.id.camera_change_iv);
        changeCamIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFrontCamera = !mFrontCamera;

//                if (mLivePusher.isPushing()) {
                mLivePusher.switchCamera();
//                }
                mLivePushConfig.setFrontCamera(mFrontCamera);
                changeCamIV.setBackgroundResource(mFrontCamera ? R.drawable.camera_change : R.drawable.camera_change);
            }
        });

        topRL = findViewById(R.id.top_rl);
        recordRL = findViewById(R.id.record_rl);
        timeRL = findViewById(R.id.time_rl);
        commentRL = findViewById(R.id.comment_rl);
        commentRL.setVisibility(View.GONE);

        //开始直播
        startPublisherIV = findViewById(R.id.start_publisher_iv);
        startPublisherIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topRL.setVisibility(View.VISIBLE);
                startRecordLogoIV.setVisibility(View.VISIBLE);
                recordRL.setVisibility(View.GONE);
                timeRL.setVisibility(View.GONE);
                startPublisherIV.setVisibility(View.GONE);
                commentRL.setVisibility(View.VISIBLE);

                startPublisherIV.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mVideoPublish) {
                            stopPublishRtmp();
                        } else {
                            if (mVideoSrc == VIDEO_SRC_CAMERA) {
                                FixOrAdjustBitrate();  //根据设置确定是“固定”还是“自动”码率
                            } else {
                                //录屏横竖屏采用两种分辨率，和摄像头推流逻辑不一样
                            }
                            mVideoPublish = startPublishRtmp();
                        }
                    }
                });
            }
        });

        recordTimeTV = findViewById(R.id.record_time_tv);

        //开始录制logo单击
        startRecordLogoIV = findViewById(R.id.start_record_logo_iv);
        startRecordLogoIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topRL.setVisibility(View.GONE);
//                startRecordLogoIV.setVisibility(View.VISIBLE);
                recordRL.setVisibility(View.VISIBLE);
                mRecordProgressBar.setVisibility(View.GONE);
                timeRL.setVisibility(View.GONE);
                startPublisherIV.setVisibility(View.GONE);
                commentRL.setVisibility(View.GONE);
            }
        });

        //点击录制
        recordLiveBroadcastTV = findViewById(R.id.record_live_broadcast_tv);
        recordLiveBroadcastTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mStartRecord) {
                    //未开始录制，点击录制
                    startRecordUIProcess();
                } else {
                    //已开始录制，停止录制
//                    stopRecordUIProcess();
                }
                startOrStopRecord();
            }
        });

        //退出录制
        exitRecordTV = findViewById(R.id.exit_record_tv);
        exitRecordTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                stopRecordUIProcess();
                mLivePusher.stopRecord();
            }
        });


        //开启硬件加速
        mBtnHWEncode = (Button) findViewById(R.id.btnHWEncode);
        mBtnHWEncode.getBackground().setAlpha(mHWVideoEncode ? 255 : 100);
        mBtnHWEncode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean HWVideoEncode = mHWVideoEncode;
                mHWVideoEncode = !mHWVideoEncode;
                mBtnHWEncode.getBackground().setAlpha(mHWVideoEncode ? 255 : 100);

                if (mHWVideoEncode) {
                    if (mLivePushConfig != null) {
                        if (Build.VERSION.SDK_INT < 18) {
                            Toast.makeText(getApplicationContext(), "硬件加速失败，当前手机API级别过低（最低18）", Toast.LENGTH_SHORT).show();
                            mHWVideoEncode = false;
                        }
                    }
                }
                if (HWVideoEncode != mHWVideoEncode) {
                    mLivePushConfig.setHardwareAcceleration(mHWVideoEncode ? TXLiveConstants.ENCODE_VIDEO_HARDWARE : TXLiveConstants.ENCODE_VIDEO_SOFTWARE);
                    if (mHWVideoEncode == false) {
                        Toast.makeText(getApplicationContext(), "取消硬件加速", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "开启硬件加速", Toast.LENGTH_SHORT).show();
                    }
                }
                if (mLivePusher != null) {
                    mLivePusher.setConfig(mLivePushConfig);
                }
            }
        });

        //码率自适应部分
        mBtnBitrate = (Button) findViewById(R.id.btnBitrate);
        mBitrateLayout = (LinearLayout) findViewById(R.id.layoutBitrate);
        mBtnBitrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBitrateLayout.setVisibility(mBitrateLayout.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                refreshBitrateBtn();
                refreshResolutiontn();
            }
        });

        mRadioGroupBitrate = (RadioGroup) findViewById(R.id.resolutionRadioGroup);
        mRadioGroupBitrate.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                boolean oldMode = mIsRealTime;
                FixOrAdjustBitrate();
//                if (oldMode != mIsRealTime && mLivePusher != null && mLivePusher.isPushing()) {
//                    stopPublishRtmp();
//                    startPublishRtmp();
//                }
            }
        });

        mBtnAutoBitrate = (Button) findViewById(R.id.btn_auto_bitrate);
        mBtnAutoBitrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAutoBitrate = !mAutoBitrate;
                refreshBitrateBtn();
                mLivePusher.setVideoQuality(mVideoQuality, mAutoBitrate, mAutoResolution);
                if (mVideoQuality == TXLiveConstants.VIDEO_QUALITY_STANDARD_DEFINITION ||
                        mVideoQuality == TXLiveConstants.VIDEO_QUALITY_HIGH_DEFINITION ||
                        mVideoQuality == TXLiveConstants.VIDEO_QUALITY_SUPER_DEFINITION) {
                    mLivePushConfig.setVideoEncodeGop(5);
                }
            }
        });

        mBtnAutoResolution = (Button) findViewById(R.id.btn_auto_resolution);
        mBtnAutoResolution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAutoResolution = !mAutoResolution;
                refreshResolutiontn();
                mLivePusher.setVideoQuality(mVideoQuality, mAutoBitrate, mAutoResolution);
                if (mVideoQuality == TXLiveConstants.VIDEO_QUALITY_STANDARD_DEFINITION ||
                        mVideoQuality == TXLiveConstants.VIDEO_QUALITY_HIGH_DEFINITION ||
                        mVideoQuality == TXLiveConstants.VIDEO_QUALITY_SUPER_DEFINITION) {
                    mLivePushConfig.setVideoEncodeGop(5);
                }
            }
        });
        //手动对焦/自动对焦
        mBtnTouchFocus = (Button) findViewById(R.id.btnTouchFoucs);
        mBtnTouchFocus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFrontCamera) {
                    return;
                }

                mTouchFocus = !mTouchFocus;
                mLivePushConfig.setTouchFocus(mTouchFocus);
                v.setBackgroundResource(mTouchFocus ? R.drawable.automatic : R.drawable.manual);

                if (mLivePusher.isPushing()) {
                    mLivePusher.stopCameraPreview(false);
                    mLivePusher.startCameraPreview(mCaptureView);
                }

                Toast.makeText(LivePublisherActivity.this, mTouchFocus ? "已开启手动对焦" : "已开启自动对焦", Toast.LENGTH_SHORT).show();
            }
        });

        //锁定Activity不旋转的情况下，才能进行横屏|竖屏推流切换
        mBtnOrientation = (Button) findViewById(R.id.btnPushOrientation);
        if (isActivityCanRotation()) {
            mBtnOrientation.setVisibility(View.GONE);
        }
        mBtnOrientation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPortrait = !mPortrait;
                int renderRotation = 0;
                int orientation = 0;
                boolean screenCaptureLandscape = false;
                if (mPortrait) {
                    mLivePushConfig.setHomeOrientation(TXLiveConstants.VIDEO_ANGLE_HOME_DOWN);
                    mBtnOrientation.setBackgroundResource(R.drawable.landscape);
                    orientation = TXLiveConstants.VIDEO_ANGLE_HOME_DOWN;
                    renderRotation = 0;
                } else {
                    mLivePushConfig.setHomeOrientation(TXLiveConstants.VIDEO_ANGLE_HOME_RIGHT);
                    mBtnOrientation.setBackgroundResource(R.drawable.portrait);
                    screenCaptureLandscape = true;
                    orientation = TXLiveConstants.VIDEO_ANGLE_HOME_RIGHT;
                    renderRotation = 90;
                }
                if (VIDEO_SRC_SCREEN == mVideoSrc) {
                    //录屏横竖屏推流的判断条件是，视频分辨率取360*640还是640*360
                    switch (mCurrentVideoResolution) {
                        case TXLiveConstants.VIDEO_RESOLUTION_TYPE_360_640:
                            if (screenCaptureLandscape)
                                mLivePushConfig.setVideoResolution(TXLiveConstants.VIDEO_RESOLUTION_TYPE_640_360);
                            else
                                mLivePushConfig.setVideoResolution(TXLiveConstants.VIDEO_RESOLUTION_TYPE_360_640);
                            break;
                        case TXLiveConstants.VIDEO_RESOLUTION_TYPE_540_960:
                            if (screenCaptureLandscape)
                                mLivePushConfig.setVideoResolution(TXLiveConstants.VIDEO_RESOLUTION_TYPE_960_540);
                            else
                                mLivePushConfig.setVideoResolution(TXLiveConstants.VIDEO_RESOLUTION_TYPE_540_960);
                            break;
                        case TXLiveConstants.VIDEO_RESOLUTION_TYPE_720_1280:
                            if (screenCaptureLandscape)
                                mLivePushConfig.setVideoResolution(TXLiveConstants.VIDEO_RESOLUTION_TYPE_1280_720);
                            else
                                mLivePushConfig.setVideoResolution(TXLiveConstants.VIDEO_RESOLUTION_TYPE_720_1280);
                            break;
                    }

                }
                if (mLivePusher.isPushing()) {
                    if (VIDEO_SRC_CAMERA == mVideoSrc) {
                        mLivePusher.setConfig(mLivePushConfig);
                    } else if (VIDEO_SRC_SCREEN == mVideoSrc) {
                        mLivePusher.setConfig(mLivePushConfig);
                        mLivePusher.stopScreenCapture();
                        mLivePusher.startScreenCapture();
                    }
                } else mLivePusher.setConfig(mLivePushConfig);
                mLivePusher.setRenderRotation(renderRotation);
            }
        });


        topRL.setVisibility(View.VISIBLE);
        recordLiveBroadcastTV.setText("点击录制");
        recordRL.setVisibility(View.GONE);
        mRecordProgressBar.setVisibility(View.GONE);
        timeRL.setVisibility(View.GONE);
        startPublisherIV.setVisibility(View.VISIBLE);

        View view = findViewById(android.R.id.content);
        view.setOnClickListener(this);
    }

    private void startRecordUIProcess() {
        topRL.setVisibility(View.GONE);
        recordLiveBroadcastTV.setText("录制中");
        recordRL.setVisibility(View.VISIBLE);
        mRecordProgressBar.setVisibility(View.VISIBLE);
        timeRL.setVisibility(View.VISIBLE);
        startPublisherIV.setVisibility(View.GONE);
        commentRL.setVisibility(View.GONE);
//        startOrStopRecord();
    }

    private void stopRecordUIProcess() {
        topRL.setVisibility(View.VISIBLE);
        recordLiveBroadcastTV.setText("点击录制");
        recordRL.setVisibility(View.GONE);
        mRecordProgressBar.setVisibility(View.GONE);
        timeRL.setVisibility(View.GONE);
        startPublisherIV.setVisibility(View.GONE);
        commentRL.setVisibility(View.VISIBLE);
//        startOrStopRecord();
    }

    protected void HWListConfirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LivePublisherActivity.this);
        builder.setMessage("警告：当前机型不在白名单中,是否继续尝试硬编码？");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mHWListConfirmDialogResult = true;
                throw new RuntimeException();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mHWListConfirmDialogResult = false;
                throw new RuntimeException();
            }
        });
        builder.setCancelable(false);
        builder.create().show();
        try {
            Looper.loop();
        } catch (Exception e) {
        }
    }

    private void startOrStopRecord() {
        mStartRecord = !mStartRecord;
        if (mLivePusher != null) {
            if (mStartRecord) {
                mLivePusher.setVideoRecordListener(new TXRecordCommon.ITXVideoRecordListener() {
                    @Override
                    public void onRecordEvent(int event, Bundle param) {

                    }

                    @Override
                    public void onRecordProgress(long milliSecond) {
                        Log.d(TAG, "onRecordProgress:" + milliSecond);
                        recordTimeTV.setText(String.format("%02d:%02d", milliSecond / 1000 / 60, milliSecond / 1000 % 60));
                        //注释不用
//                        mRecordTimeTV.setText(String.format("%02d:%02d", milliSecond / 1000 / 60, milliSecond / 1000 % 60));
                        int progress = (int) (milliSecond * 100 / (RECORD_MAX_TIME * 1000));
                        if (progress < 100) {
                            mRecordProgressBar.setProgress(progress);
                        } else {
                            mLivePusher.stopRecord();
                        }
                    }

                    @Override
                    public void onRecordComplete(TXRecordCommon.TXRecordResult result) {
                        Log.d(TAG, "onRecordComplete. errcode = " + result.retCode + ", errmsg = " + result.descMsg + ", output = " + result.videoPath + ", cover = " + result.coverPath);

                        if (result.retCode == TXRecordCommon.RECORD_RESULT_OK) {
                            mRecordProgressBar.setProgress(0);
                            recordTimeTV.setText("00:00");

                            stopRecordUIProcess();
                            //TCVideoPublishActivity短视频发布
//                            mVideoPath = result.videoPath;
//                            mCoverImagePath = result.coverPath;

                            publishVideo(result.videoPath, result.coverPath);

//                            mRecordTimeTV.setText("00:00");
//                            findViewById(R.id.toolbar).setVisibility(View.VISIBLE);
//                            findViewById(R.id.record_layout).setVisibility(View.GONE);

//                            Intent intent = new Intent(getApplicationContext(), TCVideoPreviewActivity.class);
//                            intent.putExtra(TCConstants.VIDEO_RECORD_TYPE, TCConstants.VIDEO_RECORD_TYPE_PUBLISH);
//                            intent.putExtra(TCConstants.VIDEO_RECORD_RESULT, result.retCode);
//                            intent.putExtra(TCConstants.VIDEO_RECORD_DESCMSG, result.descMsg);
//                            intent.putExtra(TCConstants.VIDEO_RECORD_VIDEPATH, result.videoPath);
//                            intent.putExtra(TCConstants.VIDEO_RECORD_COVERPATH, result.coverPath);
//                            startActivity(intent);
                        }
                    }
                });
                String videoPath = getDefaultDir() + "TXUGC.mp4";
                int result = mLivePusher.startRecord(videoPath);
                mStartRecord = result == 0;
                if (result == -1) {
                    Toast.makeText(getApplicationContext()
                            , "正在录制中，请结束后再启动录制", Toast.LENGTH_SHORT).show();
                } else if (result == -2) {
                    Toast.makeText(getApplicationContext(), "还未开始推流，请开始推流后再启动录制", Toast.LENGTH_SHORT).show();
                }
            } else {
                mLivePusher.stopRecord();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStreamRecord:
//                findViewById(R.id.toolbar).setVisibility(View.GONE);
                findViewById(R.id.record_layout).setVisibility(View.VISIBLE);
                break;
            case R.id.record:
                startOrStopRecord();
                break;
            case R.id.close_record:
//                findViewById(R.id.toolbar).setVisibility(View.VISIBLE);
                findViewById(R.id.record_layout).setVisibility(View.GONE);
            case R.id.retry_record:
                if (mLivePusher != null) {
                    mLivePusher.stopRecord();
                    mStartRecord = false;
                }
                break;
            default:
                mBottomLinear.setVisibility(View.VISIBLE);
                mBeautyPannelView.setVisibility(View.GONE);
                mBitrateLayout.setVisibility(View.GONE);
        }
    }

    private String getDefaultDir() {
        String saveDir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            String outputDir = Environment.getExternalStorageDirectory() + File.separator + "TXUGC";
            File outputFolder = new File(outputDir);
            if (!outputFolder.exists()) {
                outputFolder.mkdir();
            }
            return outputDir;
        } else {
            File file = this.getFilesDir();
            if (file != null)
                saveDir = file.getPath();
        }
        return saveDir;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mCaptureView != null) {
            mCaptureView.onResume();
        }

        if (mVideoPublish && mLivePusher != null && mVideoSrc == VIDEO_SRC_CAMERA) {
            mLivePusher.resumePusher();
            mLivePusher.resumeBGM();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mCaptureView != null) {
            mCaptureView.onPause();
        }

        if (mVideoPublish && mLivePusher != null && mVideoSrc == VIDEO_SRC_CAMERA) {
            mLivePusher.pausePusher();
            mLivePusher.pauseBGM();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopPublishRtmp();
        if (mCaptureView != null) {
            mCaptureView.onDestroy();
        }

        mRotationObserver.stopObserver();

        TelephonyManager tm = (TelephonyManager) getApplicationContext().getSystemService(Service.TELEPHONY_SERVICE);
        tm.listen(mPhoneListener, PhoneStateListener.LISTEN_NONE);

    }

    // 重载android标准实现函数
    protected void enableQRCodeBtn(boolean bEnable) {
        //disable qrcode
        Button btnScan = (Button) findViewById(R.id.btnScan);
        if (btnScan != null) {
            btnScan.setEnabled(bEnable);
        }
    }

    //公用打印辅助函数
    protected String getNetStatusString(Bundle status) {
        String str = String.format("%-14s %-14s %-12s\n%-8s %-8s %-8s %-8s\n%-14s %-14s %-12s\n%-14s %-14s",
                "CPU:" + status.getString(TXLiveConstants.NET_STATUS_CPU_USAGE),
                "RES:" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_WIDTH) + "*" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_HEIGHT),
                "SPD:" + status.getInt(TXLiveConstants.NET_STATUS_NET_SPEED) + "Kbps",
                "JIT:" + status.getInt(TXLiveConstants.NET_STATUS_NET_JITTER),
                "FPS:" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_FPS),
                "GOP:" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_GOP) + "s",
                "ARA:" + status.getInt(TXLiveConstants.NET_STATUS_AUDIO_BITRATE) + "Kbps",
                "QUE:" + status.getInt(TXLiveConstants.NET_STATUS_CODEC_CACHE) + "|" + status.getInt(TXLiveConstants.NET_STATUS_CACHE_SIZE),
                "DRP:" + status.getInt(TXLiveConstants.NET_STATUS_CODEC_DROP_CNT) + "|" + status.getInt(TXLiveConstants.NET_STATUS_DROP_SIZE),
                "VRA:" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_BITRATE) + "Kbps",
                "SVR:" + status.getString(TXLiveConstants.NET_STATUS_SERVER_IP),
                "AUDIO:" + status.getString(TXLiveConstants.NET_STATUS_AUDIO_INFO));
        return str;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                for (int ret : grantResults) {
                    if (ret != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
                break;
            default:
                break;
        }
    }

    private boolean checkPublishPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissions = new ArrayList<>();
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)) {
                permissions.add(Manifest.permission.CAMERA);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)) {
                permissions.add(Manifest.permission.RECORD_AUDIO);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)) {
                permissions.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (permissions.size() != 0) {
                ActivityCompat.requestPermissions(this,
                        permissions.toArray(new String[0]),
                        100);
                return false;
            }
        }

        return true;
    }

    private boolean publishRtmpPrepare() {
        if (TextUtils.isEmpty(pushUrl) || (!pushUrl.trim().toLowerCase().startsWith("rtmp://"))) {
            Toast.makeText(getApplicationContext(), "推流地址不合法，目前支持rtmp推流!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (mVideoSrc != VIDEO_SRC_SCREEN) {
            mCaptureView.setVisibility(View.VISIBLE);
        }
        // demo默认不加水印
        //mLivePushConfig.setWatermark(mBitmap, 0.02f, 0.05f, 0.2f);

        int customModeType = 0;

        if (isActivityCanRotation()) {
            onActivityRotation();
        }
        mLivePushConfig.setCustomModeType(customModeType);
        mLivePusher.setPushListener(this);
        mLivePushConfig.setPauseImg(300, 5);
        Bitmap bitmap = decodeResource(getResources(), R.drawable.pause_publish);
        mLivePushConfig.setPauseImg(bitmap);
        mLivePushConfig.setPauseFlag(TXLiveConstants.PAUSE_FLAG_PAUSE_VIDEO | TXLiveConstants.PAUSE_FLAG_PAUSE_AUDIO);
        if (mVideoSrc != VIDEO_SRC_SCREEN) {
            mLivePushConfig.setFrontCamera(mFrontCamera);
            mLivePushConfig.setBeautyFilter(mBeautyLevel, mWhiteningLevel, mRuddyLevel);
            mLivePusher.setConfig(mLivePushConfig);
            mLivePusher.startCameraPreview(mCaptureView);
        } else {
            mLivePusher.setConfig(mLivePushConfig);
            mLivePusher.startScreenCapture();
        }
        return true;
    }

    private boolean startPublishRtmp() {
//        publishRtmpPrepare();
        mLivePusher.startPusher(pushUrl.trim());

        enableQRCodeBtn(false);

        mBtnPlay.setBackgroundResource(R.drawable.play_pause);

        return true;
    }

    private void stopPublishRtmp() {
        mVideoPublish = false;
        mLivePusher.stopBGM();
        mLivePusher.stopCameraPreview(true);
        mLivePusher.stopScreenCapture();
        mLivePusher.setPushListener(null);
        mLivePusher.stopPusher();
        mCaptureView.setVisibility(View.GONE);

        if (mBtnHWEncode != null) {
            //mHWVideoEncode = true;
            mLivePushConfig.setHardwareAcceleration(mHWVideoEncode ? TXLiveConstants.ENCODE_VIDEO_HARDWARE : TXLiveConstants.ENCODE_VIDEO_SOFTWARE);
            mBtnHWEncode.setBackgroundResource(R.drawable.quick);
            mBtnHWEncode.getBackground().setAlpha(mHWVideoEncode ? 255 : 100);
        }

        enableQRCodeBtn(true);
        mBtnPlay.setBackgroundResource(R.drawable.play_start);

        if (mLivePushConfig != null) {
            mLivePushConfig.setPauseImg(null);
        }
    }

    public void FixOrAdjustBitrate() {
        if (mRadioGroupBitrate == null || mLivePushConfig == null || mLivePusher == null) {
            return;
        }
        RadioButton rb = (RadioButton) findViewById(mRadioGroupBitrate.getCheckedRadioButtonId());
        int mode = Integer.parseInt((String) rb.getTag());
        mIsRealTime = false;
        mLivePushConfig.setVideoEncodeGop(5);
        switch (mode) {
            case 1: /*360p*/
                if (mLivePusher != null) {
                    mVideoQuality = TXLiveConstants.VIDEO_QUALITY_STANDARD_DEFINITION;
                    mLivePusher.setVideoQuality(mVideoQuality, mAutoBitrate, mAutoResolution);
                    mCurrentVideoResolution = TXLiveConstants.VIDEO_RESOLUTION_TYPE_360_640;
                    mLivePushConfig.setVideoEncodeGop(5);
                    //标清默认开启了码率自适应，需要关闭码率自适应
                    mLivePushConfig.setAutoAdjustBitrate(false);
                    mLivePushConfig.setVideoBitrate(700);
                    mLivePusher.setConfig(mLivePushConfig);
                    //标清默认关闭硬件加速
                    mHWVideoEncode = false;
                    mBtnHWEncode.getBackground().setAlpha(100);
                }
                break;
            case 2: /*540p*/
                if (mLivePusher != null) {
                    mVideoQuality = TXLiveConstants.VIDEO_QUALITY_HIGH_DEFINITION;
                    mLivePusher.setVideoQuality(mVideoQuality, mAutoBitrate, mAutoResolution);
                    mCurrentVideoResolution = TXLiveConstants.VIDEO_RESOLUTION_TYPE_540_960;
                    mHWVideoEncode = false;
                    mLivePushConfig.setVideoEncodeGop(5);
                    mBtnHWEncode.getBackground().setAlpha(100);
                }
                break;
            case 3: /*720p*/
                if (mLivePusher != null) {
                    mVideoQuality = TXLiveConstants.VIDEO_QUALITY_SUPER_DEFINITION;
                    mLivePusher.setVideoQuality(mVideoQuality, mAutoBitrate, mAutoResolution);
                    mCurrentVideoResolution = TXLiveConstants.VIDEO_RESOLUTION_TYPE_720_1280;
                    mLivePushConfig.setVideoEncodeGop(5);
                    //超清默认开启硬件加速
                    if (Build.VERSION.SDK_INT >= 18) {
                        mHWVideoEncode = true;
                    }
                    mBtnHWEncode.getBackground().setAlpha(255);
                }
                break;
            case 4: /*连麦大主播*/
                if (mLivePusher != null) {
                    mVideoQuality = TXLiveConstants.VIDEO_QUALITY_LINKMIC_MAIN_PUBLISHER;
                    mLivePusher.setVideoQuality(mVideoQuality, mAutoBitrate, mAutoResolution);
                    mCurrentVideoResolution = TXLiveConstants.VIDEO_RESOLUTION_TYPE_360_640;
                    mHWVideoEncode = true;
                    mBtnHWEncode.getBackground().setAlpha(255);
                }
                break;
            case 5: /*连麦小主播*/
                if (mLivePusher != null) {
                    mVideoQuality = TXLiveConstants.VIDEO_QUALITY_LINKMIC_SUB_PUBLISHER;
                    mLivePusher.setVideoQuality(mVideoQuality, mAutoBitrate, mAutoResolution);
                    mCurrentVideoResolution = TXLiveConstants.VIDEO_RESOLUTION_TYPE_320_480;
                    mHWVideoEncode = true;
                    mBtnHWEncode.getBackground().setAlpha(255);
                }
                break;
            case 6: /*实时*/
                if (mLivePusher != null) {
                    mVideoQuality = TXLiveConstants.VIDEO_QUALITY_REALTIEM_VIDEOCHAT;
                    mLivePusher.setVideoQuality(mVideoQuality, mAutoBitrate, mAutoResolution);
                    mCurrentVideoResolution = TXLiveConstants.VIDEO_RESOLUTION_TYPE_360_640;
                    //超清默认开启硬件加速
                    if (Build.VERSION.SDK_INT >= 18) {
                        mHWVideoEncode = true;
                        mBtnHWEncode.getBackground().setAlpha(255);
                    }
                    mIsRealTime = true;
                }
                break;
            default:
                break;
        }
    }

    private void showNetBusyTips() {
        if (mNetBusyTips.isShown()) {
            return;
        }
        mNetBusyTips.setVisibility(View.VISIBLE);
        mMainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mNetBusyTips.setVisibility(View.GONE);
            }
        }, 5000);
    }

    private void fetchPushUrl() {
        if (mFetching) return;
        mFetching = true;
        if (mFetchProgressDialog == null) {
            mFetchProgressDialog = new ProgressDialog(this);
            mFetchProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条
            mFetchProgressDialog.setCancelable(false);// 设置是否可以通过点击Back键取消
            mFetchProgressDialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        }
        mFetchProgressDialog.show();

        if (mOkHttpClient == null) {
            mOkHttpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .build();
        }

        String reqUrl = "https://lvb.qcloud.com/weapp/utils/get_test_pushurl";
        Request request = new Request.Builder()
                .url(reqUrl)
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .build();
        Log.d(TAG, "start fetch push url");
        if (mFechCallback == null) {
            mFechCallback = new TXFechPushUrlCall(this);
        }
        mOkHttpClient.newCall(request).enqueue(mFechCallback);

    }

    @Override
    public void onPushEvent(int event, Bundle param) {
//        Log.e("NotifyCode","LivePublisherActivity :" + event);
        String msg = param.getString(TXLiveConstants.EVT_DESCRIPTION);
        String pushEventLog = "receive event: " + event + ", " + msg;
        Log.d(TAG, pushEventLog);
//        if (mLivePusher != null) {
//            mLivePusher.onLogRecord("[event:" + event + "]" + msg + "\n");
//        }
        //错误还是要明确的报一下
        if (event < 0) {
            Toast.makeText(getApplicationContext(), param.getString(TXLiveConstants.EVT_DESCRIPTION), Toast.LENGTH_SHORT).show();
            if (event == TXLiveConstants.PUSH_ERR_OPEN_CAMERA_FAIL || event == TXLiveConstants.PUSH_ERR_OPEN_MIC_FAIL) {
                stopPublishRtmp();
            }
        }

        if (event == TXLiveConstants.PUSH_ERR_NET_DISCONNECT) {
            stopPublishRtmp();
        } else if (event == TXLiveConstants.PUSH_WARNING_HW_ACCELERATION_FAIL) {
            Toast.makeText(getApplicationContext(), param.getString(TXLiveConstants.EVT_DESCRIPTION), Toast.LENGTH_SHORT).show();
            mLivePushConfig.setHardwareAcceleration(TXLiveConstants.ENCODE_VIDEO_SOFTWARE);
            mBtnHWEncode.setBackgroundResource(R.drawable.quick2);
            mLivePusher.setConfig(mLivePushConfig);
            mHWVideoEncode = false;
        } else if (event == TXLiveConstants.PUSH_ERR_SCREEN_CAPTURE_UNSURPORT) {
            stopPublishRtmp();
        } else if (event == TXLiveConstants.PUSH_ERR_SCREEN_CAPTURE_START_FAILED) {
            stopPublishRtmp();
        } else if (event == TXLiveConstants.PUSH_EVT_CHANGE_RESOLUTION) {
            Log.d(TAG, "change resolution to " + param.getInt(TXLiveConstants.EVT_PARAM2) + ", bitrate to" + param.getInt(TXLiveConstants.EVT_PARAM1));
        } else if (event == TXLiveConstants.PUSH_EVT_CHANGE_BITRATE) {
            Log.d(TAG, "change bitrate to" + param.getInt(TXLiveConstants.EVT_PARAM1));
        } else if (event == TXLiveConstants.PUSH_WARNING_NET_BUSY) {
            ++mNetBusyCount;
            Log.d(TAG, "net busy. count=" + mNetBusyCount);
            showNetBusyTips();
        } else if (event == TXLiveConstants.PUSH_EVT_START_VIDEO_ENCODER) {
            int encType = param.getInt(TXLiveConstants.EVT_PARAM1);
            mHWVideoEncode = (encType == 1);
            mBtnHWEncode.getBackground().setAlpha(mHWVideoEncode ? 255 : 100);
        }
    }

    @Override
    public void onNetStatus(Bundle status) {
        String str = getNetStatusString(status);
        Log.d(TAG, "Current status, CPU:" + status.getString(TXLiveConstants.NET_STATUS_CPU_USAGE) +
                ", RES:" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_WIDTH) + "*" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_HEIGHT) +
                ", SPD:" + status.getInt(TXLiveConstants.NET_STATUS_NET_SPEED) + "Kbps" +
                ", FPS:" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_FPS) +
                ", ARA:" + status.getInt(TXLiveConstants.NET_STATUS_AUDIO_BITRATE) + "Kbps" +
                ", VRA:" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_BITRATE) + "Kbps");
//        if (mLivePusher != null){
//            mLivePusher.onLogRecord("[net state]:\n"+str+"\n");
//        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        onActivityRotation();
        super.onConfigurationChanged(newConfig);
    }

    protected void onActivityRotation() {
        // 自动旋转打开，Activity随手机方向旋转之后，需要改变推流方向
        int mobileRotation = this.getWindowManager().getDefaultDisplay().getRotation();
        int pushRotation = TXLiveConstants.VIDEO_ANGLE_HOME_DOWN;
        boolean screenCaptureLandscape = false;
        switch (mobileRotation) {
            case Surface.ROTATION_0:
                pushRotation = TXLiveConstants.VIDEO_ANGLE_HOME_DOWN;
                break;
            case Surface.ROTATION_180:
                pushRotation = TXLiveConstants.VIDEO_ANGLE_HOME_UP;
                break;
            case Surface.ROTATION_90:
                pushRotation = TXLiveConstants.VIDEO_ANGLE_HOME_RIGHT;
                screenCaptureLandscape = true;
                break;
            case Surface.ROTATION_270:
                pushRotation = TXLiveConstants.VIDEO_ANGLE_HOME_LEFT;
                screenCaptureLandscape = true;
                break;
            default:
                break;
        }
        mLivePusher.setRenderRotation(0); //因为activity也旋转了，本地渲染相对正方向的角度为0。
        mLivePushConfig.setHomeOrientation(pushRotation);
        if (mLivePusher.isPushing()) {
            if (VIDEO_SRC_CAMERA == mVideoSrc) {
                mLivePusher.setConfig(mLivePushConfig);
                mLivePusher.stopCameraPreview(true);
                mLivePusher.startCameraPreview(mCaptureView);
            } else if (VIDEO_SRC_SCREEN == mVideoSrc) {
                //录屏横竖屏推流的判断条件是，视频分辨率取360*640还是640*360
                switch (mCurrentVideoResolution) {
                    case TXLiveConstants.VIDEO_RESOLUTION_TYPE_360_640:
                        if (screenCaptureLandscape)
                            mLivePushConfig.setVideoResolution(TXLiveConstants.VIDEO_RESOLUTION_TYPE_640_360);
                        else
                            mLivePushConfig.setVideoResolution(TXLiveConstants.VIDEO_RESOLUTION_TYPE_360_640);
                        break;
                    case TXLiveConstants.VIDEO_RESOLUTION_TYPE_540_960:
                        if (screenCaptureLandscape)
                            mLivePushConfig.setVideoResolution(TXLiveConstants.VIDEO_RESOLUTION_TYPE_960_540);
                        else
                            mLivePushConfig.setVideoResolution(TXLiveConstants.VIDEO_RESOLUTION_TYPE_540_960);
                        break;
                    case TXLiveConstants.VIDEO_RESOLUTION_TYPE_720_1280:
                        if (screenCaptureLandscape)
                            mLivePushConfig.setVideoResolution(TXLiveConstants.VIDEO_RESOLUTION_TYPE_1280_720);
                        else
                            mLivePushConfig.setVideoResolution(TXLiveConstants.VIDEO_RESOLUTION_TYPE_720_1280);
                        break;
                }
                mLivePusher.setConfig(mLivePushConfig);
                mLivePusher.stopScreenCapture();
                mLivePusher.startScreenCapture();
            }
        }
    }

    /**
     * 判断Activity是否可旋转。只有在满足以下条件的时候，Activity才是可根据重力感应自动旋转的。
     * 系统“自动旋转”设置项打开；
     *
     * @return false---Activity可根据重力感应自动旋转
     */
    protected boolean isActivityCanRotation() {
        // 判断自动旋转是否打开
        int flag = Settings.System.getInt(this.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);
        if (flag == 0) {
            return false;
        }
        return true;
    }

    @Override
    public void onBeautyParamsChange(BeautySettingPannel.BeautyParams params, int key) {
        switch (key) {
            case BeautySettingPannel.BEAUTYPARAM_EXPOSURE:
                if (mLivePusher != null) {
                    mLivePusher.setExposureCompensation(params.mExposure);
                }
                break;
            case BeautySettingPannel.BEAUTYPARAM_BEAUTY:
                mBeautyLevel = params.mBeautyLevel;
                if (mLivePusher != null) {
                    mLivePusher.setBeautyFilter(mBeautyStyle, mBeautyLevel, mWhiteningLevel, mRuddyLevel);
                }
                break;
            case BeautySettingPannel.BEAUTYPARAM_WHITE:
                mWhiteningLevel = params.mWhiteLevel;
                if (mLivePusher != null) {
                    mLivePusher.setBeautyFilter(mBeautyStyle, mBeautyLevel, mWhiteningLevel, mRuddyLevel);
                }
                break;
            case BeautySettingPannel.BEAUTYPARAM_BIG_EYE:
                if (mLivePusher != null) {
                    mLivePusher.setEyeScaleLevel(params.mBigEyeLevel);
                }
                break;
            case BeautySettingPannel.BEAUTYPARAM_FACE_LIFT:
                if (mLivePusher != null) {
                    mLivePusher.setFaceSlimLevel(params.mFaceSlimLevel);
                }
                break;
            case BeautySettingPannel.BEAUTYPARAM_FILTER:
                if (mLivePusher != null) {
                    mLivePusher.setFilter(params.mFilterBmp);
                }
                break;
            case BeautySettingPannel.BEAUTYPARAM_GREEN:
                if (mLivePusher != null) {
                    mLivePusher.setGreenScreenFile(params.mGreenFile);
                }
                break;
            case BeautySettingPannel.BEAUTYPARAM_MOTION_TMPL:
                if (mLivePusher != null) {
                    mLivePusher.setMotionTmpl(params.mMotionTmplPath);
                }
                break;
            case BeautySettingPannel.BEAUTYPARAM_RUDDY:
                mRuddyLevel = params.mRuddyLevel;
                if (mLivePusher != null) {
                    mLivePusher.setBeautyFilter(mBeautyStyle, mBeautyLevel, mWhiteningLevel, mRuddyLevel);
                }
                break;
            case BeautySettingPannel.BEAUTYPARAM_BEAUTY_STYLE:
                mBeautyStyle = params.mBeautyStyle;
                if (mLivePusher != null) {
                    mLivePusher.setBeautyFilter(mBeautyStyle, mBeautyLevel, mWhiteningLevel, mRuddyLevel);
                }
                break;
            case BeautySettingPannel.BEAUTYPARAM_FACEV:
                if (mLivePusher != null) {
                    mLivePusher.setFaceVLevel(params.mFaceVLevel);
                }
                break;
            case BeautySettingPannel.BEAUTYPARAM_FACESHORT:
                if (mLivePusher != null) {
                    mLivePusher.setFaceShortLevel(params.mFaceShortLevel);
                }
                break;
            case BeautySettingPannel.BEAUTYPARAM_CHINSLIME:
                if (mLivePusher != null) {
                    mLivePusher.setChinLevel(params.mChinSlimLevel);
                }
                break;
            case BeautySettingPannel.BEAUTYPARAM_NOSESCALE:
                if (mLivePusher != null) {
                    mLivePusher.setNoseSlimLevel(params.mNoseScaleLevel);
                }
                break;
            case BeautySettingPannel.BEAUTYPARAM_FILTER_MIX_LEVEL:
                if (mLivePusher != null) {
                    mLivePusher.setSpecialRatio(params.mFilterMixLevel / 10.f);
                }
                break;
//            case BeautySettingPannel.BEAUTYPARAM_CAPTURE_MODE:
//                if (mLivePusher != null) {
//                    boolean bEnable = ( 0 == params.mCaptureMode ? false : true);
//                    mLivePusher.enableHighResolutionCapture(bEnable);
//                }
//                break;
//            case BeautySettingPannel.BEAUTYPARAM_SHARPEN:
//                if (mLivePusher != null) {
//                    mLivePusher.setSharpenLevel(params.mSharpenLevel);
//                }
//                break;
        }
    }

    //观察屏幕旋转设置变化，类似于注册动态广播监听变化机制
    private class RotationObserver extends ContentObserver {
        ContentResolver mResolver;

        public RotationObserver(Handler handler) {
            super(handler);
            mResolver = LivePublisherActivity.this.getContentResolver();
        }

        //屏幕旋转设置改变时调用
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            //更新按钮状态
            if (isActivityCanRotation()) {
                mBtnOrientation.setVisibility(View.GONE);
                onActivityRotation();
            } else {
                mBtnOrientation.setVisibility(View.VISIBLE);
                mPortrait = true;
                mLivePushConfig.setHomeOrientation(TXLiveConstants.VIDEO_ANGLE_HOME_DOWN);
                mBtnOrientation.setBackgroundResource(R.drawable.landscape);
                mLivePusher.setRenderRotation(0);
                mLivePusher.setConfig(mLivePushConfig);
            }

        }

        public void startObserver() {
            mResolver.registerContentObserver(Settings.System.getUriFor(Settings.System.ACCELEROMETER_ROTATION), false, this);
        }

        public void stopObserver() {
            mResolver.unregisterContentObserver(this);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != 100 || data == null || data.getExtras() == null || TextUtils.isEmpty(data.getExtras().getString("result"))) {
            return;
        }
        String result = data.getExtras().getString("result");
        if (mRtmpUrlView != null) {
            mRtmpUrlView.setText(result);
        }
    }

    static class TXFechPushUrlCall implements Callback {
        WeakReference<LivePublisherActivity> mPusher;

        public TXFechPushUrlCall(LivePublisherActivity pusher) {
            mPusher = new WeakReference<LivePublisherActivity>(pusher);
        }

        @Override
        public void onFailure(Call call, IOException e) {
            final LivePublisherActivity pusher = mPusher.get();
            if (pusher != null) {
                pusher.mFetching = false;
                pusher.mMainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        pusher.mFetchProgressDialog.dismiss();
                        Toast.makeText(pusher, "获取推流地址失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            Log.e(TAG, "fetch push url failed ");
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (response.isSuccessful()) {
                String rspString = response.body().string();
                final LivePublisherActivity pusher = mPusher.get();
                if (pusher != null) {
                    try {
                        JSONObject jsonRsp = new JSONObject(rspString);
                        final String pushUrl = jsonRsp.optString("url_push");
                        final String rtmpPlayUrl = jsonRsp.optString("url_play_rtmp");
                        final String flvPlayUrl = jsonRsp.optString("url_play_flv");
                        final String hlsPlayUrl = jsonRsp.optString("url_play_hls");
                        final String realtimePlayUrl = jsonRsp.optString("url_play_acc");
                        pusher.mMainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                pusher.mRtmpUrlView.setText(pushUrl);
                                pusher.mFetchProgressDialog.dismiss();
                                if (TextUtils.isEmpty(pushUrl)) {
                                    Toast.makeText(pusher, "获取推流地址失败", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(pusher, "获取推流地址成功，对应播放地址已复制到剪贴板", Toast.LENGTH_LONG).show();

                                    String playUrl = String.format("rtmp 协议：%s\n", rtmpPlayUrl)
                                            + String.format("flv 协议：%s\n", flvPlayUrl)
                                            + String.format("hls 协议：%s\n", hlsPlayUrl)
                                            + String.format("低时延播放：%s", realtimePlayUrl);
                                    Log.d(TAG, "fetch play url : " + playUrl);
                                    try {
                                        ClipboardManager cmb = (ClipboardManager) pusher.getSystemService(Context.CLIPBOARD_SERVICE);
                                        cmb.setText(playUrl);
                                    } catch (Exception e) {

                                    }
                                }
                            }
                        });

                        Log.d(TAG, "fetch push url : " + pushUrl);

                    } catch (Exception e) {
                        Log.e(TAG, "fetch push url error ");
                        Log.e(TAG, e.toString());
                    }

                } else {
                    pusher.mMainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(pusher, "获取推流地址失败", Toast.LENGTH_SHORT).show();
                            pusher.mFetchProgressDialog.dismiss();
                        }
                    });

                    Log.e(TAG, "fetch push url failed code: " + response.code());
                }
                pusher.mFetching = false;
            }
        }
    }

    ;
    private TXFechPushUrlCall mFechCallback = null;

    private void refreshBitrateBtn() {
        if (mBtnAutoBitrate == null) return;
        if (mAutoBitrate) {
            mBtnAutoBitrate.setBackgroundResource(R.drawable.black_bkg);
            mBtnAutoBitrate.setBackgroundColor(Color.BLACK);
            mBtnAutoBitrate.setTextColor(Color.WHITE);
        } else {
            mBtnAutoBitrate.setBackgroundResource(R.drawable.white_bkg);
            mBtnAutoBitrate.setTextColor(Color.BLACK);
        }
    }

    private void refreshResolutiontn() {
        if (mBtnAutoResolution == null) return;
        if (mAutoResolution) {
            mBtnAutoResolution.setBackgroundResource(R.drawable.black_bkg);
            mBtnAutoResolution.setTextColor(Color.WHITE);
        } else {
            mBtnAutoResolution.setBackgroundResource(R.drawable.white_bkg);
            mBtnAutoResolution.setTextColor(Color.BLACK);
        }
    }

    static class TXPhoneStateListener extends PhoneStateListener {
        WeakReference<TXLivePusher> mPusher;

        public TXPhoneStateListener(TXLivePusher pusher) {
            mPusher = new WeakReference<TXLivePusher>(pusher);
        }

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            TXLivePusher pusher = mPusher.get();
            switch (state) {
                //电话等待接听
                case TelephonyManager.CALL_STATE_RINGING:
                    if (pusher != null) pusher.pausePusher();
                    break;
                //电话接听
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    if (pusher != null) pusher.pausePusher();
                    break;
                //电话挂机
                case TelephonyManager.CALL_STATE_IDLE:
                    if (pusher != null) pusher.resumePusher();
                    break;
            }
        }
    }

    ;
    private PhoneStateListener mPhoneListener = null;

}
