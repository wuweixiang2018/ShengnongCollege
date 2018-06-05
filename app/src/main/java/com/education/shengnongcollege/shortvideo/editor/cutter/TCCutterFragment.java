package com.education.shengnongcollege.shortvideo.editor.cutter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

//import com.tencent.liteav.demo.R;
//import com.tencent.liteav.demo.common.utils.TCUtils;
//import com.tencent.liteav.demo.shortvideo.editor.TCVideoEditerActivity;
//import com.tencent.liteav.demo.shortvideo.editor.TCVideoEditerWrapper;
//import com.tencent.liteav.demo.shortvideo.editor.common.widget.videotimeline.RangeSliderViewContainer;
//import com.tencent.liteav.demo.shortvideo.editor.common.widget.videotimeline.VideoProgressController;
import com.education.shengnongcollege.R;
import com.education.shengnongcollege.common.utils.TCUtils;
import com.education.shengnongcollege.shortvideo.editor.TCVideoEditerActivity;
import com.education.shengnongcollege.shortvideo.editor.TCVideoEditerWrapper;
import com.education.shengnongcollege.shortvideo.editor.common.widget.videotimeline.RangeSliderViewContainer;
import com.education.shengnongcollege.shortvideo.editor.common.widget.videotimeline.VideoProgressController;
import com.tencent.ugc.TXVideoEditer;

/**
 * Created by hans on 2017/11/6.
 * <p>
 * 视频裁剪的Fragment
 */
public class TCCutterFragment extends Fragment {

    private static final String TAG = "TCCuterFragment";
    private TextView mTvTip;
    private VideoProgressController mActivityVideoProgressController;

    private long mVideoDuration;
    private long mCutterStartTime;
    private long mCutterEndTime;
    private long mCurrentDuration;

    private TXVideoEditer mTXVideoEditer;
    private RangeSliderViewContainer mCutterRangeSliderView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cutter, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TCVideoEditerWrapper wrapper = TCVideoEditerWrapper.getInstance();
        mTXVideoEditer = wrapper.getEditer();
        mVideoDuration = wrapper.getTXVideoInfo().duration;

        mCutterStartTime = 0;
        mCutterEndTime = mVideoDuration;

        mActivityVideoProgressController = ((TCVideoEditerActivity) getActivity()).getVideoProgressViewController();
        initViews(view);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
//        if (mCutterRangeSliderView != null) {
//            mCutterRangeSliderView.setVisibility(hidden ? View.GONE : View.VISIBLE);
//        }
    }

    private void initViews(View view) {
        mTvTip = (TextView) view.findViewById(R.id.cutter_tv_tip);

        initRangeSlider();
    }

    private void initRangeSlider() {
        mCutterRangeSliderView = new RangeSliderViewContainer(getActivity());
        mCutterRangeSliderView.init(mActivityVideoProgressController, 0, mVideoDuration, mVideoDuration);
        mCutterRangeSliderView.setDurationChangeListener(mOnDurationChangeListener);
        mActivityVideoProgressController.addRangeSliderView(mCutterRangeSliderView);
    }


    private RangeSliderViewContainer.OnDurationChangeListener mOnDurationChangeListener = new RangeSliderViewContainer.OnDurationChangeListener() {
        @Override
        public void onDurationChange(long startTime, long endTime) {
            mCutterStartTime = startTime;
            mCutterEndTime = endTime;
            if (mTXVideoEditer != null) {
                mTXVideoEditer.setCutFromTime(startTime, endTime);
            }

            mTvTip.setText(String.format("左侧 : %s, 右侧 : %s ", TCUtils.duration(startTime), TCUtils.duration(endTime)));

            TCVideoEditerWrapper.getInstance().setCutterStartTime(startTime, endTime);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTXVideoEditer = null;
    }

    public long getCutterStartTime() {
        return mCutterStartTime;
    }

    public long getCutterEndTime() {
        return mCutterEndTime;
    }


}
