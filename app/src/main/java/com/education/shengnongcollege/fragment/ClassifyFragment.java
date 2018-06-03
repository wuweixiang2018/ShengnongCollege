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

import java.io.Serializable;
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
    private GridView mGridView;
    private ClassifyGridAdapter gridAdapter;
    private ClassifyListAdapter listAdapter;
    private ImageView topItem;
    private boolean isTopShow=false;//决定list列表展现出来没有
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
        }
        return mFragmentView;
    }
    private void initView() {
        serchBar=mFragmentView.findViewById(R.id.search_et);
        topItem=mFragmentView.findViewById(R.id.imageView_item);
        mGridView=mFragmentView.findViewById(R.id.classify_main_gridview);
        mListView=mFragmentView.findViewById(R.id.classify_main_listview);
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
        }


    }
    @Override
    public void notifyAllActivity(String str, Object object) {
        if(getActivity()!=null){
            if(TextUtils.equals("ClassifyFragment",str)){
                List<GetCategoryListRespData> dataList= (List<GetCategoryListRespData>) object;
                listAdapter=new ClassifyListAdapter(getActivity(),dataList);
                mListView.setAdapter(listAdapter);
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
                getVidioListData(bean.getId());

            }
        }
    }

    @Override
    public void changeRefreshData() {
        super.changeRefreshData();
        isTopShow=false;
        mListView.setVisibility(View.GONE);
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
