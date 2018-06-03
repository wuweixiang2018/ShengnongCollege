package com.education.shengnongcollege.adapter;

import android.content.Context;
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
public class ClassifyGridAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater mInflater;
    private List<GetCategoryListRespData> dataList = new ArrayList<>();
    private int[] imagList={R.drawable.bg_circle_green,R.drawable.bg_circle_fense1,R.drawable.bg_circle_qianyellow
    ,R.drawable.bg_circle_blue,R.drawable.bg_circle_zifense,R.drawable.bg_circle_fense2,R.drawable.bg_circle_qingse
    ,R.drawable.bg_circle_zise,R.drawable.bg_circle_qianlanse,R.drawable.bg_circle_yellow};

    public ClassifyGridAdapter(Context context, List<GetCategoryListRespData> dataList) {
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
        holder.mItemName.setText(getItem(position).getName());
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
