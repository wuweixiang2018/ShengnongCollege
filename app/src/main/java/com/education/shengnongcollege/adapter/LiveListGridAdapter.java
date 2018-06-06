package com.education.shengnongcollege.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.education.shengnongcollege.BaseTopActivity;
import com.education.shengnongcollege.R;
import com.education.shengnongcollege.activity.LiveBroadcastActivity;
import com.education.shengnongcollege.model.GetLvbListRespData;
import com.education.shengnongcollege.play.LivePlayerActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pjy
 */
public class LiveListGridAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater mInflater;
    private List<GetLvbListRespData> dataList = new ArrayList<>();

    public LiveListGridAdapter(Context context, List<GetLvbListRespData> dataList) {
        this.context = context;
        this.dataList = dataList;
        this.mInflater = LayoutInflater.from(context);
    }


    public void setDataList(List<GetLvbListRespData> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public GetLvbListRespData getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        ViewHolde holder;
        if (view == null) {
            view = mInflater.inflate(R.layout.classify_gridview_item, null);
            holder = new ViewHolde();
            holder.mItemName = view.findViewById(R.id.gridview_title);
            holder.mItemType = view.findViewById(R.id.gridview_type);
            holder.mItemImage = view.findViewById(R.id.gridview_image);
            holder.mItemTypeLayout = view.findViewById(R.id.gridview_type_layout);
            holder.onclick = view.findViewById(R.id.gridview_onclick);
            view.setTag(holder);
        } else {
            holder = (ViewHolde) view.getTag();
        }
        final GetLvbListRespData item = getItem(position);
        if (!TextUtils.isEmpty((CharSequence) item.getCoverPhotoUrl())) {
            Glide.with(context).load(item.getCoverPhotoUrl()).into(holder.mItemImage);
        }
        holder.mItemTypeLayout.setVisibility(View.VISIBLE);
        holder.mItemType.setTextColor(Color.parseColor("#FF9800"));
        if (item.getState() == 1) {
            holder.mItemType.setText("等待开播");
        } else if (item.getState() == 2) {
            holder.mItemType.setText("直播中");
        } else if (item.getState() == 3) {
            holder.mItemType.setText("直播已结束");
        } else {
            holder.mItemType.setText("等待开播");
        }
        holder.mItemName.setText((CharSequence) item.getTitle());
        holder.onclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context,"在适配器里面写点击事件"+item.getCategoryId(),Toast.LENGTH_SHORT).show();
                Activity activity = BaseTopActivity.getTopActivity();
                Intent data = new Intent(activity,
                        LivePlayerActivity.class);
                data.putExtra("lvbData", item);
                activity.startActivity(data);
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

    public List<GetLvbListRespData> getDataList() {
        return this.dataList;
    }

}
