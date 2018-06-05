package com.education.shengnongcollege.common.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.education.shengnongcollege.BaseTopActivity;
import com.education.shengnongcollege.api.LiveBroadcastApiManager;
import com.education.shengnongcollege.common.utils.FileUtils;
import com.education.shengnongcollege.common.widget.VideoWorkProgressFragment;
import com.education.shengnongcollege.network.listener.GWResponseListener;
import com.education.shengnongcollege.push.LivePublisherActivity;
import com.education.shengnongcollege.videopublish.server.PublishSigListener;
import com.education.shengnongcollege.videopublish.server.ReportVideoInfoListener;
import com.education.shengnongcollege.videopublish.server.VideoDataMgr;
import com.education.shengnongcollege.videoupload.TXUGCPublish;
import com.education.shengnongcollege.videoupload.TXUGCPublishTypeDef;
import com.tencent.liteav.basic.log.TXCLog;
import com.tencent.rtmp.TXLog;
import com.tencent.ugc.TXRecordCommon;
import com.tencent.ugc.TXVideoInfoReader;

import java.io.Serializable;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

import static com.education.shengnongcollege.videopublish.TCVideoPublishActivity.saveBitmap;

/**
 * Created by wuweixiang on 18/6/5.
 */

public class VideoPublishBaseActivity extends BaseTopActivity {
    private static final String TAG = VideoPublishBaseActivity.class.getSimpleName();
    //录制视频上传
    protected VideoWorkProgressFragment mWorkLoadingProgress; // 进度
    protected TXUGCPublish mTXugcPublish;
    protected PublishSigListener mPublishSiglistener;
    protected ReportVideoInfoListener mReportVideoInfoListener;
    protected String signature;
    protected boolean isCancelPublish;
    private String mVideoPath, mCoverImagePath;
    protected boolean isShowLoadingProgress = true;
    //需要上级页面传入
    protected String CategoryId;
    protected String Title;

    //3分钟
    protected static final int RECORD_MAX_TIME = 3*60;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CategoryId = getIntent().getStringExtra("CategoryId");
        Title = getIntent().getStringExtra("Title");
        //录制视频上传
        mTXugcPublish = new TXUGCPublish(this.getApplicationContext(), "customID");
        initListener();
    }

    private void initListener() {
        mPublishSiglistener = new PublishSigListener() {
            @Override
            public void onSuccess(String signatureStr) {
                signature = signatureStr;
                publish();
            }

            @Override
            public void onFail(final int errCode) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mWorkLoadingProgress != null && mWorkLoadingProgress.isAdded()) {
                            mWorkLoadingProgress.dismiss();
                        }
                        Toast.makeText(VideoPublishBaseActivity.this, "err code = " + errCode, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        VideoDataMgr.getInstance().setPublishSigListener(mPublishSiglistener);

        mReportVideoInfoListener = new ReportVideoInfoListener() {
            @Override
            public void onFail(int errCode) {
                TXCLog.e(TAG, "reportVideoInfo, report video info fail");
            }

            @Override
            public void onSuccess() {
                TXCLog.i(TAG, "reportVideoInfo, report video info success");
            }
        };
        VideoDataMgr.getInstance().setReportVideoInfoListener(mReportVideoInfoListener);
    }

    //视频上传
    private void reportVideoInfo(TXUGCPublishTypeDef.TXPublishResult result) {
        if (TextUtils.isEmpty(CategoryId) && TextUtils.isEmpty(Title)) {
            //用于测试
            CategoryId = "6e1e59db-05b5-45e7-a14d-8844e5247814";
        }
        LiveBroadcastApiManager.storageVideo(new GWResponseListener() {
            @Override
            public void successResult(Serializable result, String path, int requestCode, int resultCode) {

            }

            @Override
            public void errorResult(Serializable result, String path, int requestCode, int resultCode) {

            }
        }, CategoryId, Title, result.videoURL, result.videoId, result.coverURL);
//        VideoDataMgr.getInstance().reportVideoInfo(result.videoId, "腾讯云");
    }

    private void initWorkLoadingProgress() {
        if (mWorkLoadingProgress == null) {
            mWorkLoadingProgress = VideoWorkProgressFragment.newInstance("发布中...");
            mWorkLoadingProgress.setOnClickStopListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTXugcPublish != null) {
                        mTXugcPublish.canclePublish();
                        isCancelPublish = true;
                        mWorkLoadingProgress.setProgress(0);
                        mWorkLoadingProgress.dismiss();
                    }
                }
            });
        }
        mWorkLoadingProgress.setProgress(0);
    }

    protected void publishVideo(String videoPath, String coverImagePath) {
        this.mVideoPath = videoPath;
        this.mCoverImagePath = coverImagePath;
        if (isShowLoadingProgress) {
            if (mWorkLoadingProgress == null) {
                initWorkLoadingProgress();
            }
            mWorkLoadingProgress.setProgress(0);
            mWorkLoadingProgress.show(getSupportFragmentManager(), "progress_dialog");
        }
        getPublishSig();
        isCancelPublish = false;
    }

    private void getPublishSig() {
        VideoDataMgr.getInstance().getPublishSig();
    }

    private void publish() {
        mTXugcPublish.setListener(new TXUGCPublishTypeDef.ITXVideoPublishListener() {
            @Override
            public void onPublishProgress(long uploadBytes, long totalBytes) {
                TXLog.d(TAG, "onPublishProgress [" + uploadBytes + "/" + totalBytes + "]");
                if (isCancelPublish || !isShowLoadingProgress) {
                    return;
                }

                mWorkLoadingProgress.setProgress((int) ((uploadBytes * 100) / totalBytes));
            }

            @Override
            public void onPublishComplete(TXUGCPublishTypeDef.TXPublishResult result) {
                TXLog.d(TAG, "onPublishComplete [" + result.retCode + "/" + (result.retCode == 0 ? result.videoURL : result.descMsg) + "]");
                if (isShowLoadingProgress && mWorkLoadingProgress != null && mWorkLoadingProgress.isAdded()) {
                    mWorkLoadingProgress.dismiss();
                }

                if (isCancelPublish) {
                    return;
                }

                // 这里可以把上传返回的视频信息以及自定义的视频信息上报到自己的业务服务器
                reportVideoInfo(result);

                // 注意：如果取消发送时，是取消的剩余未上传的分片发送，如果视频比较小，分片已经进入任务队列了是无法取消的。此时不跳转到下一个界面。
                if (result.retCode == TXUGCPublishTypeDef.PUBLISH_RESULT_OK) {
                    Toast.makeText(VideoPublishBaseActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(LivePublisherActivity.this, NewVodPlayerActivity.class);
//                    intent.putExtra(TCConstants.PLAYER_DEFAULT_VIDEO, false);
//                    intent.putExtra(TCConstants.PLAYER_VIDEO_ID, result.videoId);
////                    intent.putExtra(TCConstants.PLAYER_VIDEO_NAME, mTitleStr);
//                    startActivity(intent);

                } else {
                    if (result.descMsg.contains("java.net.UnknownHostException") || result.descMsg.contains("java.net.ConnectException")) {
                        Toast.makeText(VideoPublishBaseActivity.this, "网络连接断开，视频上传失败" + result.descMsg, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(VideoPublishBaseActivity.this, "发布失败，errCode = " + result.retCode + ", msg = " + result.descMsg, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        if (TextUtils.isEmpty(mCoverImagePath)) {
            Observable.create(new ObservableOnSubscribe<Object>() {

                @Override
                public void subscribe(ObservableEmitter<Object> e) throws Exception {
                    mCoverImagePath = FileUtils.getFilesDir(getContext()).getAbsolutePath() + "/cover.jpg";
                    final Bitmap coverBitmap = TXVideoInfoReader.getInstance().getSampleImage(0, mVideoPath);
                    saveBitmap(coverBitmap, mCoverImagePath);

                    txUgcPublishVideo();
                }
            }).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe();
        } else {
            txUgcPublishVideo();
        }

    }

    private void txUgcPublishVideo() {
        TXUGCPublishTypeDef.TXPublishParam param = new TXUGCPublishTypeDef.TXPublishParam();
        // signature计算规则可参考 https://www.qcloud.com/document/product/266/9221
        param.signature = signature;
        param.videoPath = mVideoPath;
        param.coverPath = mCoverImagePath;
//        mTitleStr = mEtVideoTitle.getText().toString();
//        if (TextUtils.isEmpty(mTitleStr)) {
//            mTitleStr = "测试";
//        }
        param.fileName = "" + System.currentTimeMillis();
        mTXugcPublish.publishVideo(param);
    }
}
