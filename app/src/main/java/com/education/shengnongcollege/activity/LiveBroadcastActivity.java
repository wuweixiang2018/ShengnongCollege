package com.education.shengnongcollege.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.education.shengnongcollege.BaseTopActivity;
import com.education.shengnongcollege.R;
import com.education.shengnongcollege.adapter.LiveListGridAdapter;
import com.education.shengnongcollege.api.LiveBroadcastApiManager;
import com.education.shengnongcollege.model.GetLvbListRespData;
import com.education.shengnongcollege.model.ListRespObj;
import com.education.shengnongcollege.network.listener.GWResponseListener;
import com.education.shengnongcollege.network.model.ListResponseResult;
import com.education.shengnongcollege.push.LivePublisherActivity;
import com.education.shengnongcollege.utils.CacheUtil;
import com.education.shengnongcollege.widget.DialogUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//直播列表页面
public class LiveBroadcastActivity extends BaseTopActivity {
    private LinearLayout back, botomItem,bottomLayout;
    private PullToRefreshGridView mGridView;
    private LiveListGridAdapter gridAdapter;
    private int pageindex = 0;
    private List<GetLvbListRespData> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_broadcast_view);
        initView();
        initListener();
        initData();
    }

    private void initData() {
        if(CacheUtil.getInstance().getAllowLiveBroadcast()>0){
            bottomLayout.setVisibility(View.VISIBLE);
        }
        gridAdapter = new LiveListGridAdapter(this, dataList);
        mGridView.setAdapter(gridAdapter);
        getLiveListData();
    }

    private void initListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //在这里执行PullToRefreshGridView的点击item后要做的事

            }
        });
        mGridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<GridView> refreshView) {
//                 Log.e("TAG", "onPullDownToRefresh");//下拉刷新
                pageindex = 0;
                getLiveListData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
//                   Log.e("TAG", "onPullUpToRefresh"); // 上拉加载
                pageindex++;
                getLiveListData();

            }
        });
        botomItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent(LiveBroadcastActivity.this,
                        LivePublisherActivity.class);
                startActivity(data);
//                Toast.makeText(LiveBroadcastActivity.this, "立即直播", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        CacheUtil.getInstance().init(this);
        back = findViewById(R.id.live_top_back);
        botomItem = findViewById(R.id.live_bottom_ljzb_layout);
        bottomLayout=findViewById(R.id.live_broadcast_bottomlayout);
        mGridView = findViewById(R.id.live_main_gridview);
        mGridView.setMode(PullToRefreshBase.Mode.BOTH);//能下拉刷新和上拉加载
    }

    private void getLiveListData() {//首页传这个id
        DialogUtil.getInstance().showProgressDialog(this);
        LiveBroadcastApiManager.getLVBList(new GWResponseListener() {
            @Override
            public void successResult(Serializable result, String path, int requestCode, int resultCode) {
                mGridView.onRefreshComplete();
                DialogUtil.getInstance().cancelProgressDialog();
                ListResponseResult<GetLvbListRespData, ListRespObj> responseResult = (ListResponseResult<GetLvbListRespData, ListRespObj>) result;
                List<GetLvbListRespData> data = responseResult.getData();
                if (pageindex == 0) {
                    dataList.clear();
                }
                if (data != null) {
                    dataList.addAll(data);
                    if (data.size() < 10) {
                        mGridView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                        if (data.size() == 0) {
                            Toast.makeText(LiveBroadcastActivity.this, "搜索无结果", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        mGridView.setMode(PullToRefreshBase.Mode.BOTH);//能下拉刷新和上拉加载
                    }
                    gridAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(LiveBroadcastActivity.this, "搜索无结果", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void errorResult(Serializable result, String path, int requestCode, int resultCode) {
                DialogUtil.getInstance().cancelProgressDialog();
                mGridView.onRefreshComplete();
                Toast.makeText(LiveBroadcastActivity.this, "直播列表获取失败", Toast.LENGTH_SHORT).show();
            }
        }, pageindex, 10);
    }
}
