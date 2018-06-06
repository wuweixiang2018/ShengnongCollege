package com.education.shengnongcollege.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.education.shengnongcollege.BaseTopActivity;
import com.education.shengnongcollege.R;
import com.education.shengnongcollege.adapter.MainSerchListAdapter;
import com.education.shengnongcollege.api.LiveBroadcastApiManager;
import com.education.shengnongcollege.model.GetVideoDetailRespData;
import com.education.shengnongcollege.model.GetVideoListRespData;
import com.education.shengnongcollege.model.ListRespObj;
import com.education.shengnongcollege.model.RespObjBase;
import com.education.shengnongcollege.network.listener.GWResponseListener;
import com.education.shengnongcollege.network.model.ListResponseResult;
import com.education.shengnongcollege.network.model.ResponseResult;
import com.education.shengnongcollege.play.VodPlayerActivity;
import com.education.shengnongcollege.utils.JkysLog;
import com.education.shengnongcollege.widget.DialogUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainSearchActivity extends BaseTopActivity {
    private EditText mSearchEt;
    private ImageView finsh;
    private MainSerchListAdapter serchListAdapter;
    private PullToRefreshListView mListView;
    private List<GetVideoListRespData> videoListRespData = new ArrayList<>();
    private int offset = 0;
    private String title = "";//暂且把你当key值搜索关键字吧

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_serch_activity);
        initView();
        initListener();
        initData();
    }

    private void initData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSearchEt.setFocusable(true);
                mSearchEt.setFocusableInTouchMode(true);
                mSearchEt.requestFocus();
                mSearchEt.findFocus();
                InputMethodManager inputManager =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(mSearchEt, 0);
            }
        }, 100);
    }

    private void initListener() {
        finsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mSearchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 关闭软键盘
                    ((InputMethodManager) mSearchEt.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    String keyWord = mSearchEt.getText().toString();
                    if (TextUtils.isEmpty(keyWord)) {
                        Toast.makeText(MainSearchActivity.this, R.string.toast_keyword_empty, Toast.LENGTH_SHORT).show();
                    } else {
                        offset = 0;
                        title = keyWord;
                        mListView.setMode(PullToRefreshBase.Mode.BOTH);
                        getVidioListData(offset);
                    }
                }
                return false;
            }
        });
    }

    private void initView() {
        mSearchEt = findViewById(R.id.search_et);
        finsh = findViewById(R.id.search_clear_iv);
        mListView = findViewById(R.id.listView);
        serchListAdapter = new MainSerchListAdapter(this, videoListRespData);
        mListView.setAdapter(serchListAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String videoId = videoListRespData.get(position - 1).getId();
//                    Log.e("点击结果", videoListRespData.get(position - 1).getCategoryId());

                    LiveBroadcastApiManager.getVideoDetail(new GWResponseListener() {
                        @Override
                        public void successResult(Serializable result, String path, int requestCode, int resultCode) {
                            ResponseResult<GetVideoDetailRespData, RespObjBase> responseResult = (ResponseResult<GetVideoDetailRespData, RespObjBase>) result;
                            if (responseResult != null && responseResult.getData() != null) {
                                GetVideoDetailRespData data = responseResult.getData();
                                Intent intent = new Intent(MainSearchActivity.this, VodPlayerActivity.class);
                                intent.putExtra("videoDetail", data);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void errorResult(Serializable result, String path, int requestCode, int resultCode) {
                            JkysLog.d("wuwx", "error");
                        }
                    }, videoId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mListView.getRefreshableView().setSelector(new ColorDrawable(Color.TRANSPARENT));
        mListView.getRefreshableView().setDividerHeight(0);
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                //下拉刷新
                offset = 0;
                getVidioListData(offset);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                //上拉加载更多
                offset++;
                getVidioListData(offset);
            }
        });
        mListView.setMode(PullToRefreshBase.Mode.BOTH);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.anim_enter_from_top, R.anim.anim_exit_from_bottom);
    }

    private void getVidioListData(final int start) {
        DialogUtil.getInstance().showProgressDialog(this);
        LiveBroadcastApiManager.getVideoList(new GWResponseListener() {
            @Override
            public void successResult(Serializable result, String path, int requestCode, int resultCode) {
                DialogUtil.getInstance().cancelProgressDialog();
                mListView.onRefreshComplete();
                ListResponseResult<GetVideoListRespData, ListRespObj> responseResult = (ListResponseResult<GetVideoListRespData, ListRespObj>) result;
                List<GetVideoListRespData> data = responseResult.getData();
                if (start == 0) {
                    videoListRespData.clear();
                }
                if (data != null) {
                    videoListRespData.addAll(data);
                    if (data.size() < 10) {
                        mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                        if (data.size() == 0) {
                            Toast.makeText(MainSearchActivity.this, "搜索无结果", Toast.LENGTH_SHORT).show();
                        }
                    }
                    serchListAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainSearchActivity.this, "搜索无结果", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void errorResult(Serializable result, String path, int requestCode, int resultCode) {
                DialogUtil.getInstance().cancelProgressDialog();
                mListView.onRefreshComplete();
                Toast.makeText(MainSearchActivity.this, "分类列表获取失败", Toast.LENGTH_SHORT).show();
            }
        }, "", title, start, 10);
    }
}
