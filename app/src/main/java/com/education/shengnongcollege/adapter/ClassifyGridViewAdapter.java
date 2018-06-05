package com.education.shengnongcollege.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.education.shengnongcollege.R;
import com.education.shengnongcollege.model.GetVideoListRespData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pjy
 */
public class ClassifyGridViewAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater mInflater;
    private List<GetVideoListRespData> dataList = new ArrayList<>();
    public ClassifyGridViewAdapter(Context context, List<GetVideoListRespData> dataList) {
        this.context = context;
        this.dataList = dataList;
        this.mInflater = LayoutInflater.from(context);
    }


    public void setDataList(List<GetVideoListRespData> dataList){
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dataList == null ? 0:dataList.size();
    }

    @Override
    public GetVideoListRespData getItem(int position) {
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
            view = mInflater.inflate(R.layout.classify_gridview_item, null);
            holder = new ViewHolde();
            holder.mItemName =view.findViewById(R.id.gridview_title);
            holder.mItemType =view.findViewById(R.id.gridview_type);
            holder.mItemImage =view.findViewById(R.id.gridview_image);
            holder.mItemTypeLayout =view.findViewById(R.id.gridview_type_layout);
            holder.onclick=view.findViewById(R.id.gridview_onclick);
            view.setTag(holder);
        }else{
            holder = (ViewHolde) view.getTag();
        }
        final GetVideoListRespData item=getItem(position);
        if(!TextUtils.isEmpty(item.getCoverUrl())){
            Glide.with(context).load(item.getCoverUrl()).into(holder.mItemImage);
        }
        holder.mItemName.setText(item.getTitle());
        holder.onclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"在适配器里面写点击事件"+item.getCategoryId(),Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    class ViewHolde {
        TextView mItemName;
        TextView mItemType;
        ImageView mItemImage;
        LinearLayout mItemTypeLayout;
        LinearLayout onclick;

    }
    public List<GetVideoListRespData> getDataList(){
        return this.dataList;
    }

}
