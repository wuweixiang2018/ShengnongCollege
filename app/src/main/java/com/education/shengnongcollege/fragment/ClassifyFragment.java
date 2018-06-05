package com.education.shengnongcollege.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.education.shengnongcollege.BaseFragment;
import com.education.shengnongcollege.R;
import com.education.shengnongcollege.activity.MainSerchActivity;
import com.education.shengnongcollege.adapter.ClassifyGridAdapter;
import com.education.shengnongcollege.adapter.ClassifyGridViewAdapter;
import com.education.shengnongcollege.adapter.ClassifyListAdapter;
import com.education.shengnongcollege.api.LiveBroadcastApiManager;
import com.education.shengnongcollege.model.GetCategoryListRespData;
import com.education.shengnongcollege.model.GetVideoListRespData;
import com.education.shengnongcollege.model.ListRespObj;
import com.education.shengnongcollege.network.listener.GWResponseListener;
import com.education.shengnongcollege.network.model.ListResponseResult;
import com.education.shengnongcollege.utils.Ilisten.IListener;
import com.education.shengnongcollege.utils.Ilisten.ListenerManager;
import com.education.shengnongcollege.widget.DialogUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分类页面
 *
 * @author shihaoxian
 * @date 2018/4/12
 */
public class ClassifyFragment extends BaseFragment implements IListener {

    private View mFragmentView;
    private TextView serchBar;
    private ListView mListView;
    private PullToRefreshGridView mGridView;
    private ClassifyGridAdapter gridAdapter;
    private ClassifyListAdapter listAdapter;
    private ImageView topItem;
    private boolean isTopShow=false;//决定list列表展现出来没有
    private int pageindex = 0;
    private String CategoryId="";//当前页面的唯一id  什么都不传 就是查全部
    private ClassifyGridViewAdapter gridViewAdapter;
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
            mFragmentView = inflater.inflate(R.layout.activity_classify_view, container, false);
            initView();
            initListener();
            initData();
        }
        return mFragmentView;
    }

    private void initData() {
        gridViewAdapter = new ClassifyGridViewAdapter(getActivity(), videoListRespData);
        mGridView.setAdapter(gridViewAdapter);
    }

    private void initView() {
        serchBar=mFragmentView.findViewById(R.id.search_et);
        topItem=mFragmentView.findViewById(R.id.imageView_item);
        mGridView=mFragmentView.findViewById(R.id.classify_main_gridview);
        mListView=mFragmentView.findViewById(R.id.classify_main_listview);
        mGridView.setMode(PullToRefreshBase.Mode.BOTH);//能下拉刷新和上拉加载
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
        topItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isTopShow){
                    isTopShow=false;
                    mListView.setVisibility(View.GONE);
                }else{
                    isTopShow=true;
                    mListView.setVisibility(View.VISIBLE);
                }
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                relushListView(i,false);
            }
        });
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //在这里执行PullToRefreshGridView的点击item后要做的事
            }
        });
        mGridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>(){
            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<GridView> refreshView)
            {
//                 Log.e("TAG", "onPullDownToRefresh");//下拉刷新
                pageindex = 0;
                getVidioListData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView)
            {
//                   Log.e("TAG", "onPullUpToRefresh"); // 上拉加载
                pageindex++;
                getVidioListData();

            }
        });
        ListenerManager.getInstance().registerListtener(this);
    }
    private void relushListView(int position,boolean isShow){
        List<GetCategoryListRespData> dataList=listAdapter.getDataList();
        GetCategoryListRespData bean=dataList.get(position);
        for(GetCategoryListRespData data:dataList){
            if(data.ischoose()){
                data.setIschoose(false);
            }
            if(TextUtils.equals(bean.getId(),data.getId())){
                data.setIschoose(true);
            }
        }
        listAdapter.setDataList(dataList);
        if (isShow){
            isTopShow=true;
            mListView.setVisibility(View.VISIBLE);
        }else {
            isTopShow=false;
            mListView.setVisibility(View.GONE);
            pageindex=0;
            CategoryId=bean.getId();
            getVidioListData();
        }


    }
    @Override
    public void notifyAllActivity(String str, Object object) {
        if(getActivity()!=null){
            if(TextUtils.equals("ClassifyFragment",str)){
                List<GetCategoryListRespData> dataList= (List<GetCategoryListRespData>) object;
                GetCategoryListRespData data=new GetCategoryListRespData();
                data.setIschoose(true);
                data.setName("全部");
                data.setId("");
                dataList.add(0,data);
                listAdapter=new ClassifyListAdapter(getActivity(),dataList);
                mListView.setAdapter(listAdapter);
                pageindex=0;
                CategoryId=dataList.get(0).getId();
                getVidioListData();
            }
            if(TextUtils.equals("ClassifyFragment_itemShow",str)){
                GetCategoryListRespData bean= (GetCategoryListRespData) object;
                List<GetCategoryListRespData> dataList=listAdapter.getDataList();
                int position=0;
                for(int i=0;i<dataList.size();i++){
                    if(TextUtils.equals(bean.getId(),dataList.get(i).getId())){
                        position=i;
                        break;
                    }
                }
                relushListView(position,true);
                pageindex=0;
                CategoryId=bean.getId();
                getVidioListData();

            }
        }
    }

    @Override
    public void changeRefreshData() {
        super.changeRefreshData();
        isTopShow=false;
        mListView.setVisibility(View.GONE);
    }
    private List<GetVideoListRespData> videoListRespData=new ArrayList<>();
    private void getVidioListData(){//首页传这个id
        DialogUtil.getInstance().showProgressDialog(getActivity());
        LiveBroadcastApiManager.getVideoList(new GWResponseListener() {
            @Override
            public void successResult(Serializable result, String path, int requestCode, int resultCode) {
                DialogUtil.getInstance().cancelProgressDialog();
                ListResponseResult<GetVideoListRespData, ListRespObj> responseResult = (ListResponseResult<GetVideoListRespData, ListRespObj>) result;
                List<GetVideoListRespData> data=responseResult.getData();
                if(pageindex==0){
                    videoListRespData.clear();
                }
                if(data!=null){
                    videoListRespData.addAll(data);
                    if(data.size()<10){
                        mGridView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                        if(data.size()==0){
                            Toast.makeText(getActivity(), "搜索无结果", Toast.LENGTH_SHORT).show();
                        }
                    }
                    gridViewAdapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(getActivity(), "搜索无结果", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void errorResult(Serializable result, String path, int requestCode, int resultCode) {
                DialogUtil.getInstance().cancelProgressDialog();
                Toast.makeText(getActivity(), "直播列表获取失败", Toast.LENGTH_SHORT).show();
            }
        }, CategoryId,"",pageindex, 10);
    }
}
