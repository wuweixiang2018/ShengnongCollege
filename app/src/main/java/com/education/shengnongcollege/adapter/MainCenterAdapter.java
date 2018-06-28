package com.education.shengnongcollege.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.education.shengnongcollege.R;
import com.education.shengnongcollege.model.GetCategoryListRespData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pjy
 */
public class MainCenterAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater mInflater;
    private List<GetCategoryListRespData> dataList = new ArrayList<>();
    private int[] imagList={R.drawable.circle1,R.drawable.circle2,R.drawable.circle3
    ,R.drawable.circle4,R.drawable.circle5,R.drawable.circle6,R.drawable.circle7
    ,R.drawable.circle8,R.drawable.circle9,R.drawable.circle10};

    public MainCenterAdapter(Context context, List<GetCategoryListRespData> dataList) {
        this.context = context;
        this.dataList = dataList;
        this.mInflater = LayoutInflater.from(context);
    }


    public void setDataList(List<GetCategoryListRespData> dataList){
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dataList == null ? 0:dataList.size();
    }

    @Override
    public GetCategoryListRespData getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        ViewHolde holder;
        if(view == null){
            view = mInflater.inflate(R.layout.activity_main_center_gridview_item, null);
            holder = new ViewHolde();
            holder.mItemName =view.findViewById(R.id.main_gridview_item_tv);
            holder.mItemIcon =view.findViewById(R.id.main_gridview_item_layout);
            view.setTag(holder);
        }else{
            holder = (ViewHolde) view.getTag();
        }
        String name=getItem(position).getName();
        if(!TextUtils.isEmpty(name)){
            if(name.length()>2){
                name=name.substring(0,2);
            }
            holder.mItemName.setText(name);
        }else{
            holder.mItemName.setText("");
        }
        try {
            holder.mItemIcon.setBackgroundResource(imagList[position]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    class ViewHolde {
        TextView mItemName;
        LinearLayout mItemIcon;
    }

}
