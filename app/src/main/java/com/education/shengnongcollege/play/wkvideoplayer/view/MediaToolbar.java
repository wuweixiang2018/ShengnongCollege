package com.education.shengnongcollege.play.wkvideoplayer.view;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.education.shengnongcollege.R;

//import com.tencent.liteav.demo.R;

/**
 * Created by Ted on 2015/8/4.
 * MediaController
 */
public class MediaToolbar extends FrameLayout implements View.OnClickListener {

    private ImageView mDanmukuBtn;
    private RelativeLayout userRL;
    private boolean mDanmukuOn;
    private MediaToolbarImpl mMediaToolbarImpl;
    private ImageView mMoreBtn;
    private LinearLayout mBackBtn;
    private ImageView mSnapshotBtn;
    private TextView mTitle;

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.danmuku) {
            mDanmukuOn = !mDanmukuOn;
            if (mDanmukuOn) {
                Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_danmuku_on);
                mDanmukuBtn.setImageBitmap(bmp);
            } else {
                Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_danmuku_off);
                mDanmukuBtn.setImageBitmap(bmp);
            }
            mMediaToolbarImpl.onDanmaku(mDanmukuOn);
        } else if (view.getId() == R.id.snapshoot) {
            mMediaToolbarImpl.onSnapshoot();
        } else if (view.getId() == R.id.menu_more) {
            mMediaToolbarImpl.onMoreSetting();
        } else if (view.getId() == R.id.back_ll) {
            portraitProcess();
            mMediaToolbarImpl.onBack();
        }
    }


    public void setMediaControl(MediaToolbarImpl mediaControl) {
        mMediaToolbarImpl = mediaControl;
    }

    public MediaToolbar(Context context) {
        super(context);
        initView(context);
    }

    public MediaToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public MediaToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.biz_video_media_toolbar, this);

        userRL = findViewById(R.id.user_rl);
        mDanmukuBtn = (ImageView) findViewById(R.id.danmuku);
        mMoreBtn = (ImageView) findViewById(R.id.menu_more);
        mBackBtn = (LinearLayout) findViewById(R.id.back_ll);
        mSnapshotBtn = (ImageView) findViewById(R.id.snapshoot);
        mTitle = (TextView) findViewById(R.id.title);

        mDanmukuBtn.setOnClickListener(this);
        mMoreBtn.setOnClickListener(this);
        mBackBtn.setOnClickListener(this);
        mSnapshotBtn.setOnClickListener(this);
        initData();
    }

    public void setOrientation(int orientation) {
        if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            //横屏
            landscapeProcess();
        } else if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            //竖屏
            portraitProcess();
        }
    }

    private void landscapeProcess() {
        mBackBtn.setVisibility(VISIBLE);
        mDanmukuBtn.setVisibility(VISIBLE);
        userRL.setVisibility(VISIBLE);

    }

    private void portraitProcess() {
        mBackBtn.setVisibility(GONE);
        mDanmukuBtn.setVisibility(GONE);
        userRL.setVisibility(GONE);
    }

    private void initData() {

    }

    public void udpateTitle(String title) {
        if (mTitle != null) {
            mTitle.setText(title);
        }
    }


    public interface MediaToolbarImpl {
        void onDanmaku(boolean on);

        void onSnapshoot();

        void onMoreSetting();

        void onBack();
    }

}
