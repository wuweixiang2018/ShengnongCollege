package com.education.shengnongcollege.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
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
import com.education.shengnongcollege.activity.LiveBroadcastActivity;
import com.education.shengnongcollege.activity.MainSearchActivity;
import com.education.shengnongcollege.activity.TestActivity;
import com.education.shengnongcollege.adapter.ClassifyGridViewAdapter;
import com.education.shengnongcollege.adapter.HomeTopTabAdapter;
import com.education.shengnongcollege.adapter.MainCenterAdapter;
import com.education.shengnongcollege.api.LiveBroadcastApiManager;
import com.education.shengnongcollege.im.GuestIMMgr;
import com.education.shengnongcollege.im.IMMessageMgr;
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
import com.tencent.imsdk.TIMConnListener;

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
    private CustomGridView mGridView,mGridViewTj;
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
                Intent intent = new Intent(getActivity(), MainSearchActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.anim_enter_from_top, R.anim.anim_exit_from_bottom);
            }
        });
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(TextUtils.equals("直播",adapter.getItem(i).getName())){
                    Intent intent=new Intent(getActivity(), LiveBroadcastActivity.class);//这里跳到直播列表
                    startActivity(intent);
                }else{
                    ListenerManager.getInstance().sendBroadCast("MainActivity_replace","");//先切换
                    ListenerManager.getInstance().sendBroadCast("ClassifyFragment_itemShow",adapter.getItem(i));//展现出来
                }
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
                GuestIMMgr.getInstance().guestLogin(getActivity().getApplicationContext(), new TIMConnListener() {
                    @Override
                    public void onConnected() {

                    }

                    @Override
                    public void onDisconnected(int i, String s) {

                    }

                    @Override
                    public void onWifiNeedAuth(String s) {

                    }
                });

                GuestIMMgr.getInstance().imInit(getContext(), new IMMessageMgr.IMMessageListener() {
                    @Override
                    public void onConnected() {

                    }

                    @Override
                    public void onDisconnected() {

                    }

                    @Override
                    public void onPusherChanged() {

                    }

                    @Override
                    public void onGroupTextMessage(String groupID, String senderID, String userName, String headPic, String message) {
                        Toast.makeText(getContext(), "onGroupCustomMessage groupID=" + groupID
                                        + " senderID=" + senderID + " message=" + message + " userName=" + userName,
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onGroupCustomMessage(String groupID, String senderID, String message) {
                        Toast.makeText(getContext(), "onGroupCustomMessage groupID=" + groupID + " senderID=" + senderID + " message=" + message,
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onC2CCustomMessage(String sendID, String cmd, String message) {

                    }

                    @Override
                    public void onGroupDestroyed(String groupID) {

                    }

                    @Override
                    public void onDebugLog(String log) {

                    }
                });
//                getActivity().startActivity(new Intent(getActivity(), TestActivity.class));
            }
        });
    }

    private void initView(){
        rightBtn=mFragmentView.findViewById(R.id.layou2);
        mGridView=mFragmentView.findViewById(R.id.main_gridview);
        mGridViewTj=mFragmentView.findViewById(R.id.main_gridview_tuijian);
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
                        if(data!=null){
                            //中间按钮加入直播选项
                            GetCategoryListRespData data1=new GetCategoryListRespData();
                            data1.setIschoose(true);
                            data1.setName("直播");
                            data1.setId("");
                            if(data.size()==10){
                                data.set(9,data1);//设置最后一个为直播
                            }else{
                                data.add(data1);
                            }
                            //先把头部加入最新10条数据
                            initToptab(data);
                            adapter=new MainCenterAdapter(getActivity(),data);
                            mGridView.setAdapter(adapter);
                        }else{
                            Toast.makeText(getActivity(), "分类列表暂无数据", Toast.LENGTH_SHORT).show();
                        }
                        //ListenerManager.getInstance().sendBroadCast("ClassifyFragment",data);//通知分类页面也加载页面
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
                if(TextUtils.equals("直播",data.get(arg0).getName())){
                    Intent intent=new Intent(getActivity(), LiveBroadcastActivity.class);//这里跳到直播列表
                    startActivity(intent);
                }else{
                    getVidioListData(data.get(arg0).getId());
                }
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
        getVidioListData(data.get(0).getId());//默认进来先加载第一个tab
    }
    private void getVidioListData(String CategoryId){//首页传这个id
        DialogUtil.getInstance().showProgressDialog(getActivity());
        LiveBroadcastApiManager.getVideoList(new GWResponseListener() {
            @Override
            public void successResult(Serializable result, String path, int requestCode, int resultCode) {
                DialogUtil.getInstance().cancelProgressDialog();
                ListResponseResult<GetVideoListRespData, ListRespObj> responseResult = (ListResponseResult<GetVideoListRespData, ListRespObj>) result;
                List<GetVideoListRespData> data=responseResult.getData();
                if(data!=null){
                    if(data.size()<10){
                        if(data.size()==0){
                            Toast.makeText(getActivity(), "搜索无结果", Toast.LENGTH_SHORT).show();
                        }
                    }
                    ClassifyGridViewAdapter adapter=new ClassifyGridViewAdapter(getActivity(),data);
                    mGridViewTj.setAdapter(adapter);
                }else {
                    Toast.makeText(getActivity(), "搜索无结果", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void errorResult(Serializable result, String path, int requestCode, int resultCode) {
                DialogUtil.getInstance().cancelProgressDialog();
                Toast.makeText(getActivity(), "直播列表获取失败", Toast.LENGTH_SHORT).show();
            }
        }, CategoryId,"",0, 10);
    }


}
