package com.education.shengnongcollege.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.education.shengnongcollege.BaseFragment;
import com.education.shengnongcollege.R;

/**
 * 我的页面
 *
 * @author shihaoxian
 * @date 2018/4/12
 */
public class MineFragment extends BaseFragment {

    private View mFragmentView;
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
        }
        return mFragmentView;
    }

}
