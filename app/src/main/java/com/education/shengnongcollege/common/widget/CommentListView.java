package com.education.shengnongcollege.common.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.education.shengnongcollege.R;
import com.education.shengnongcollege.common.adapter.SimpleCommentListAdapter;
import com.education.shengnongcollege.common.model.SimpleCommentModel;
import com.education.shengnongcollege.im.GuestIMMgr;
import com.education.shengnongcollege.im.IMMessageMgr;
import com.education.shengnongcollege.im.LiveIMMgr;
import com.education.shengnongcollege.utils.BaseUtil;
import com.education.shengnongcollege.utils.JkysLog;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.ITXLivePushListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.TXLivePusher;

import java.io.UnsupportedEncodingException;

/**
 * Created by wuweixiang on 18/6/5.
 */

public class CommentListView extends LinearLayout {
    private TXLivePusher mLivePusher;
    private TXLivePlayer mLivePlayer;
    private Button sendBtn;
    private EditText commentET;
    private RecyclerView mRecyclerView;
    private SimpleCommentListAdapter mAdapter;
    private String roomId;

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public void setLivePusher(TXLivePusher mLivePusher) {
        this.mLivePusher = mLivePusher;
    }

    public void setLivePlayer(TXLivePlayer mLivePlayer) {
        this.mLivePlayer = mLivePlayer;

//        mLivePlayer.setPlayListener(new ITXLivePlayListener() {
//
//            @Override
//            public void onPlayEvent(int event, Bundle param) {
//                if (event == TXLiveConstants.PLAY_ERR_NET_DISCONNECT) {
////                    roomListenerCallback.onDebugLog("[AnswerRoom] 拉流失败：网络断开");
////                    roomListenerCallback.onError(-1, "网络断开，拉流失败");
//                } else if (event == TXLiveConstants.PLAY_EVT_GET_MESSAGE) {
//                    String msg = null;
//                    try {
//                        msg = new String(param.getByteArray(TXLiveConstants.EVT_GET_MSG), "UTF-8");
//                        JkysLog.e("wuwx", "接收到消息：" + msg);
////                        roomListenerCallback.onRecvAnswerMsg(msg);
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onNetStatus(Bundle bundle) {
//
//            }
//        });
    }

    public CommentListView(Context context) {
        super(context);
        init();
    }

    public CommentListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CommentListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        try {
            View view = View.inflate(getContext(), R.layout.comment_list_widget, this);
            commentET = view.findViewById(R.id.comment_et);
            sendBtn = view.findViewById(R.id.send_btn);
            sendBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String comment = commentET.getText().toString();
                    if (TextUtils.isEmpty(comment)) {
                        Toast.makeText(getContext(), "评论内容不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String userName = BaseUtil.userData.getRealName();
                    String headPic = BaseUtil.userData.getPhotograph();
                    LiveIMMgr.getInstance().getIMMessageMgr().sendGroupTextMessage(userName,
                            headPic, comment, new IMMessageMgr.Callback() {
                                @Override
                                public void onError(int code, String errInfo) {

                                }

                                @Override
                                public void onSuccess(Object... args) {
                                    Toast.makeText(getContext(), "发送消息成功", Toast.LENGTH_SHORT).show();
                                }
                            });

//                    GuestIMMgr.getInstance().setCallback(new IMMessageMgr.Callback() {
//                        @Override
//                        public void onError(int code, String errInfo) {
//                            Toast.makeText(getContext(), "发送失败=" + errInfo,
//                                    Toast.LENGTH_SHORT).show();
//                        }
//
//                        @Override
//                        public void onSuccess(Object... args) {
//                            Toast.makeText(getContext(), "发送成功",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                    GuestIMMgr.getInstance().sendText(roomId, comment);
                    commentET.setText("");
//                    if (mLivePusher != null) {
//                        try {
//                            mLivePusher.sendMessage(comment.getBytes("UTF-8"));
//                            addComment(comment);
//                            commentET.setText("");
//                        } catch (UnsupportedEncodingException e) {
//                            e.printStackTrace();
//                        }
//                    }
                }
            });
            mRecyclerView = view.findViewById(R.id.recycler_view);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mAdapter = new SimpleCommentListAdapter(getContext());
            mRecyclerView.setAdapter(mAdapter);

//            SimpleCommentModel model = new SimpleCommentModel();
//            model.content = "你好";
//            model.nickname = "橘子";
//            mAdapter.addComment(model);
//
//            SimpleCommentModel model1 = new SimpleCommentModel();
//            model1.content = "谢谢";
//            model1.nickname = "淘淘";
//            mAdapter.addComment(model1);
//            mAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addComment(String content) {
        SimpleCommentModel model = new SimpleCommentModel();
        model.content = content;
        model.nickname = BaseUtil.userData.getRealName();
        mAdapter.addComment(model);
        mAdapter.notifyDataSetChanged();
    }
}
