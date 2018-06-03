package com.education.shengnongcollege.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.education.shengnongcollege.BaseFragment;
import com.education.shengnongcollege.R;
import com.education.shengnongcollege.activity.MainSerchActivity;
import com.education.shengnongcollege.activity.TestActivity;
import com.education.shengnongcollege.adapter.HomeTopTabAdapter;
import com.education.shengnongcollege.adapter.MainCenterAdapter;
import com.education.shengnongcollege.api.LiveBroadcastApiManager;
import com.education.shengnongcollege.model.GetCategoryListRespData;
import com.education.shengnongcollege.model.GetVideoListRespData;
import com.education.shengnongcollege.model.ListRespObj;
import com.education.shengnongcollege.network.listener.GWResponseListener;
import com.education.shengnongcollege.network.model.ListResponseResult;
import com.education.shengnongcollege.utils.Ilisten.ListenerManager;
import com.education.shengnongcollege.view.CustomGridView;
import com.education.shengnongcollege.view.CustomViewPager;
import com.education.shengnongcollege.view.PagerSlidingTabStrip;
import com.education.shengnongcollege.widget.DialogUtil;

import java.io.Serializable;
import java.util.ArrayList;
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
    private HomeTopTabAdapter mHomeAdapter;
    private PagerSlidingTabStrip tabs;
    private LinearLayout rightBtn;
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
                Intent intent = new Intent(getActivity(), MainSerchActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.anim_enter_from_top, R.anim.anim_exit_from_bottom);
            }
        });
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ListenerManager.getInstance().sendBroadCast("MainActivity_replace","");//先切换
                ListenerManager.getInstance().sendBroadCast("ClassifyFragment_itemShow",adapter.getItem(i));//展现出来
            }
        });
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListenerManager.getInstance().sendBroadCast("MainActivity_replace","");//切换
            }
        });

        //测试代码
        mFragmentView.findViewById(R.id.layou1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), TestActivity.class));
            }
        });
    }

    private void initView(){
        rightBtn=mFragmentView.findViewById(R.id.layou2);
        mGridView=mFragmentView.findViewById(R.id.main_gridview);
        serchBar=mFragmentView.findViewById(R.id.search_et);
        viewPager =mFragmentView.findViewById(R.id.morelist_viewpager);
        tabs = mFragmentView.findViewById(R.id.morelist_tabs);
        tabs.setItemResId(R.layout.main_top_tab_item);
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
                        initToptab(data);
                        ListenerManager.getInstance().sendBroadCast("ClassifyFragment",data);//通知分类页面也加载页面
                        Toast.makeText(getActivity(), "分类列表获取成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void errorResult(Serializable result, String path, int requestCode, int resultCode) {
                        Toast.makeText(getActivity(), "分类列表获取失败", Toast.LENGTH_SHORT).show();
                    }
                }, 1, 10);
    }
    private void initToptab(final List<GetCategoryListRespData> data){
        List<String> str = new ArrayList<>();
        if(data==null||data.size()==0){
            return;
        }
        for(int i=0;i<data.size();i++){
            str.add(data.get(i).getName());
        }
        FragmentManager fragmentManager = getChildFragmentManager();
        mHomeAdapter = new HomeTopTabAdapter(fragmentManager, str);
        viewPager.setAdapter(mHomeAdapter);
        tabs.setmItems(str);
        tabs.setViewPager(viewPager);
        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                Log.e("选中状态",data.get(arg0).getName());
                getVidioListData(data.get(arg0).getId());
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        tabs.setScanScroll(true);
        tabs.setCurrentItem(0);
    }
    private void getVidioListData(String CategoryId){//首页传这个id
        DialogUtil.getInstance().showProgressDialog(getActivity());
        LiveBroadcastApiManager.getVideoList(new GWResponseListener() {
            @Override
            public void successResult(Serializable result, String path, int requestCode, int resultCode) {
                DialogUtil.getInstance().cancelProgressDialog();
                ListResponseResult<GetVideoListRespData, ListRespObj> responseResult = (ListResponseResult<GetVideoListRespData, ListRespObj>) result;
                List<GetVideoListRespData> data=responseResult.getData();
            }

            @Override
            public void errorResult(Serializable result, String path, int requestCode, int resultCode) {
                DialogUtil.getInstance().cancelProgressDialog();
                Toast.makeText(getActivity(), "直播列表获取失败", Toast.LENGTH_SHORT).show();
            }
        }, CategoryId,"",0, 10);
    }


}
