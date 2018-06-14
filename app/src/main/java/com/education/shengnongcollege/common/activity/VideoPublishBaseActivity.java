package com.education.shengnongcollege.common.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.education.shengnongcollege.BaseTopActivity;
import com.education.shengnongcollege.R;
import com.education.shengnongcollege.api.LiveBroadcastApiManager;
import com.education.shengnongcollege.common.utils.FileUtils;
import com.education.shengnongcollege.common.widget.CommentListView;
import com.education.shengnongcollege.common.widget.VideoWorkProgressFragment;
import com.education.shengnongcollege.common.widget.danmaku.TCDanmuMgr;
import com.education.shengnongcollege.im.ILiveRoomListener;
import com.education.shengnongcollege.im.PusherInfo;
import com.education.shengnongcollege.im.TCChatEntity;
import com.education.shengnongcollege.im.TCConstants;
import com.education.shengnongcollege.im.TCLiveRoomMgr;
import com.education.shengnongcollege.im.TCSimpleUserInfo;
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
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;
import master.flame.danmaku.controller.IDanmakuView;

import static com.education.shengnongcollege.videopublish.TCVideoPublishActivity.saveBitmap;

/**
 * Created by wuweixiang on 18/6/5.
 */

public class VideoPublishBaseActivity extends BaseTopActivity implements ILiveRoomListener {
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
    protected static final int RECORD_MAX_TIME = 3 * 60;

    //弹幕
    protected TCDanmuMgr mDanmuMgr;
    protected Handler mHandler = new Handler();

    protected CommentListView mCommentListView;

    protected ImageView danmukuIV;

    protected boolean danmukuOn = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CategoryId = getIntent().getStringExtra("CategoryId");
        Title = getIntent().getStringExtra("Title");
        //录制视频上传
        mTXugcPublish = new TXUGCPublish(this.getApplicationContext(), "customID");
        initListener();
        mDanmuMgr = new TCDanmuMgr(this);
        TCLiveRoomMgr.getLiveRoom().setLiveRoomListener(this);
    }

    protected void onCreateAfter(){
        IDanmakuView danmakuView = (IDanmakuView) findViewById(R.id.danmakuView);
        mDanmuMgr.setDanmakuView(danmakuView);

        danmukuIV = (ImageView) findViewById(R.id.danmuku_iv);
        danmukuIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (danmukuOn) {
                    danmukuIV.setImageResource(R.drawable.ic_danmuku_off);
                } else {
                    danmukuIV.setImageResource(R.drawable.ic_danmuku_on);
                }
                danmukuOn = !danmukuOn;

                mCommentListView.setDanmuOpen(danmukuOn);
            }
        });
        mCommentListView = findViewById(R.id.comment_list);
//        mCommentListView.setGroupId(lvbData.getGroupId());
        mCommentListView.setDanmuMgr(mDanmuMgr);
        mCommentListView.setCallback(new CommentListView.Callback() {
            @Override
            public void onError(int code, String errInfo) {
                Toast.makeText(getApplicationContext(), "发送消息失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(Object... args) {
                Toast.makeText(getApplicationContext(), "发送消息成功", Toast.LENGTH_SHORT).show();
            }
        });
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
        }, CategoryId, Title, result.videoURL, result.videoId, result.coverURL, result.descMsg);
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

    @Override
    public void onGetPusherList(List<PusherInfo> pusherList) {

    }

    @Override
    public void onPusherJoin(PusherInfo pusherInfo) {

    }

    @Override
    public void onPusherQuit(PusherInfo pusherInfo) {

    }

    @Override
    public void onRecvJoinPusherRequest(String userID, String userName, String userAvatar) {

    }

    @Override
    public void onKickOut() {

    }

    @Override
    public void onRecvPKRequest(String userID, String userName, String userAvatar, String streamUrl) {

    }

    @Override
    public void onRecvPKFinishRequest(String userID) {

    }

    @Override
    public void onRecvRoomTextMsg(String roomID, String userID, String userName, String userAvatar, String message) {
        TCSimpleUserInfo userInfo = new TCSimpleUserInfo(userID, userName, userAvatar);
        handleTextMsg(userInfo, message);
    }

    @Override
    public void onRecvRoomCustomMsg(String roomID, String userID, String userName, String userAvatar, String cmd, String message) {
        TCSimpleUserInfo userInfo = new TCSimpleUserInfo(userID, userName, userAvatar);
        int type = Integer.valueOf(cmd);
        switch (type) {
            case TCConstants.IMCMD_ENTER_LIVE:
                handleMemberJoinMsg(userInfo);
                break;
            case TCConstants.IMCMD_EXIT_LIVE:
                handleMemberQuitMsg(userInfo);
                break;
            case TCConstants.IMCMD_PRAISE:
                handlePraiseMsg(userInfo);
                break;
            case TCConstants.IMCMD_PAILN_TEXT:
                handleTextMsg(userInfo, message);
                break;
            case TCConstants.IMCMD_DANMU:
                handleDanmuMsg(userInfo, message);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRoomClosed(String roomID) {

    }

    @Override
    public void onDebugLog(String log) {

    }

    @Override
    public void onError(int errorCode, String errorMessage) {

    }

    private void notifyMsg(final TCChatEntity entity) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {

                mCommentListView.addComment(entity.getSenderName(),entity.getContext());


//                if (mArrayListChatEntity.size() > 1000)
//                {
//                    while (mArrayListChatEntity.size() > 900)
//                    {
//                        mArrayListChatEntity.remove(0);
//                    }
//                }
//
//                mArrayListChatEntity.add(entity);
//                mChatMsgListAdapter.notifyDataSetChanged();
            }
        });
    }

    public void handleMemberJoinMsg(TCSimpleUserInfo userInfo) {
//        //更新头像列表 返回false表明已存在相同用户，将不会更新数据
//        if (!mAvatarListAdapter.addItem(userInfo))
//            return;
//
//        mCurrentMemberCount++;
//        mTotalMemberCount++;
//        mMemberCount.setText(String.format(Locale.CHINA,"%d",mCurrentMemberCount));
//
//        //左下角显示用户加入消息
//        TCChatEntity entity = new TCChatEntity();
//        entity.setSenderName("通知");
//        if (TextUtils.isEmpty(userInfo.nickname))
//            entity.setContext(userInfo.userid + "加入直播");
//        else
//            entity.setContext(userInfo.nickname + "加入直播");
//        entity.setType(TCConstants.MEMBER_ENTER);
//        notifyMsg(entity);
    }

    public void handleMemberQuitMsg(TCSimpleUserInfo userInfo) {
//        if(mCurrentMemberCount > 0)
//            mCurrentMemberCount--;
//        else
//            Log.d(TAG, "接受多次退出请求，目前人数为负数");
//
//        mMemberCount.setText(String.format(Locale.CHINA,"%d",mCurrentMemberCount));
//
//        mAvatarListAdapter.removeItem(userInfo.userid);
//
//        TCChatEntity entity = new TCChatEntity();
//        entity.setSenderName("通知");
//        if (TextUtils.isEmpty(userInfo.nickname))
//            entity.setContext(userInfo.userid + "退出直播");
//        else
//            entity.setContext(userInfo.nickname + "退出直播");
//        entity.setType(TCConstants.MEMBER_EXIT);
//        notifyMsg(entity);
    }

    public void handlePraiseMsg(TCSimpleUserInfo userInfo) {
//        TCChatEntity entity = new TCChatEntity();
//
//        entity.setSenderName("通知");
//        if (TextUtils.isEmpty(userInfo.nickname))
//            entity.setContext(userInfo.userid + "点了个赞");
//        else
//            entity.setContext(userInfo.nickname + "点了个赞");
//        if (mHeartLayout != null) {
//            mHeartLayout.addFavor();
//        }
//        mHeartCount++;
//
//        entity.setType(TCConstants.MEMBER_ENTER);
//        notifyMsg(entity);
    }

    public void handleDanmuMsg(TCSimpleUserInfo userInfo, String text) {
        handleTextMsg(userInfo, text);
        if (mDanmuMgr != null) {
            mDanmuMgr.addDanmu(userInfo.headpic, userInfo.nickname, text);
        }
    }

    public void handleTextMsg(TCSimpleUserInfo userInfo, String text) {
        TCChatEntity entity = new TCChatEntity();
        entity.setSenderName(userInfo.nickname);
        entity.setContext(text);
        entity.setType(TCConstants.TEXT_TYPE);

        notifyMsg(entity);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDanmuMgr != null) {
            mDanmuMgr.destroy();
            mDanmuMgr = null;
        }
    }
}
