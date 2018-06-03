package com.education.shengnongcollege.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.education.shengnongcollege.BaseFragment;
import com.education.shengnongcollege.R;

/**
 * 首页Fragment tab测试页面
 *
 * @author shihaoxian
 * @date 2018/4/12
 */
public class TopTestFragment extends BaseFragment{

    private View mFragmentView;
    public static TopTestFragment newInstance(String title) {
        TopTestFragment f = new TopTestFragment();
        Bundle b = new Bundle();
        b.putString("title", title);
        f.setArguments(b);
        return f;
    }

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
            mFragmentView = inflater.inflate(R.layout.activity_main, container, false);
            initListView(mFragmentView);
        }
        return mFragmentView;
    }





    public void initListView(View view) {

    }

}
