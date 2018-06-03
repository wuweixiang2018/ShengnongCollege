package com.education.shengnongcollege.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.education.shengnongcollege.BaseFragment;
import com.education.shengnongcollege.R;
import com.education.shengnongcollege.activity.MainSerchActivity;
import com.education.shengnongcollege.adapter.MainCenterAdapter;
import com.education.shengnongcollege.adapter.MainPagerAdapter;
import com.education.shengnongcollege.api.LiveBroadcastApiManager;
import com.education.shengnongcollege.model.GetCategoryListRespData;
import com.education.shengnongcollege.model.ListRespObj;
import com.education.shengnongcollege.network.listener.GWResponseListener;
import com.education.shengnongcollege.network.model.ListResponseResult;
import com.education.shengnongcollege.utils.Ilisten.ListenerManager;
import com.education.shengnongcollege.view.CustomGridView;
import com.education.shengnongcollege.view.CustomViewPager;
import com.education.shengnongcollege.view.PagerSlidingTabStrip2;

import java.io.Serializable;
import java.util.List;

/**
 * 首页页面
 *
 * @author shihaoxian
 * @date 2018/4/12
 */
public class MainFragment extends BaseFragment {
    private CustomGridView mGridView;
    private MainCenterAdapter adapter;
    private View mFragmentView;
    private TextView serchBar;
    private CustomViewPager viewPager;
    private PagerSlidingTabStrip2 tabLayout;
    private MainPagerAdapter mainPagerAdapter;
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
            mFragmentView = inflater.inflate(R.layout.main_fragment_view, container, false);
            initView();
            initListener();
            getGridData();
        }
        return mFragmentView;
    }

    private void initListener() {
        serchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), MainSerchActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.anim_enter_from_top, R.anim.anim_exit_from_bottom);
            }
        });
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ListenerManager.getInstance().sendBroadCast("MainActivity_replace","");//先切换
                ListenerManager.getInstance().sendBroadCast("ClassifyFragment_itemShow","");//展现出来
            }
        });
    }

    private void initView(){
        mGridView=mFragmentView.findViewById(R.id.main_gridview);
        serchBar=mFragmentView.findViewById(R.id.search_et);
    }
    private void getGridData(){
                LiveBroadcastApiManager.getCategoryList(new GWResponseListener() {
                    @Override
                    public void successResult(Serializable result, String path, int requestCode, int resultCode) {
                        ListResponseResult<GetCategoryListRespData, ListRespObj> responseResult = (ListResponseResult<GetCategoryListRespData, ListRespObj>) result;
                        List<GetCategoryListRespData> data = responseResult.getData();
                        ListRespObj obj = responseResult.getObj();
                        adapter=new MainCenterAdapter(getActivity(),data);
                        mGridView.setAdapter(adapter);
                        ListenerManager.getInstance().sendBroadCast("ClassifyFragment",data);//通知分类页面也加载页面
                        Toast.makeText(getActivity(), "分类列表获取成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void errorResult(Serializable result, String path, int requestCode, int resultCode) {
                        Toast.makeText(getActivity(), "分类列表获取失败", Toast.LENGTH_SHORT).show();
                    }
                }, 1, 10);
    }

}
