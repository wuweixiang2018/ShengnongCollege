package com.education.shengnongcollege.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.education.shengnongcollege.BaseTopActivity;
import com.education.shengnongcollege.R;
import com.education.shengnongcollege.activity.MainSearchActivity;
import com.education.shengnongcollege.api.LiveBroadcastApiManager;
import com.education.shengnongcollege.model.GetVideoDetailRespData;
import com.education.shengnongcollege.model.GetVideoListRespData;
import com.education.shengnongcollege.model.RespObjBase;
import com.education.shengnongcollege.network.listener.GWResponseListener;
import com.education.shengnongcollege.network.model.ResponseResult;
import com.education.shengnongcollege.play.VodPlayerActivity;
import com.education.shengnongcollege.utils.JkysLog;

import java.io.Serializable;
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


    public void setDataList(List<GetVideoListRespData> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dataList == null ? 0 : dataList.size();
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
        final GetVideoListRespData item = getItem(position);
        if (!TextUtils.isEmpty(item.getCoverUrl())) {
            Glide.with(context).load(item.getCoverUrl()).into(holder.mItemImage);
        }
        holder.mItemName.setText(item.getTitle());
        holder.onclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context,"在适配器里面写点击事件"+item.getCategoryId(),Toast.LENGTH_SHORT).show();
                String id = item.getId();
                LiveBroadcastApiManager.getVideoDetail(new GWResponseListener() {
                    @Override
                    public void successResult(Serializable result, String path, int requestCode, int resultCode) {
                        ResponseResult<GetVideoDetailRespData, RespObjBase> responseResult = (ResponseResult<GetVideoDetailRespData, RespObjBase>) result;
                        if (responseResult != null && responseResult.getData() != null) {
                            GetVideoDetailRespData data = responseResult.getData();
                            Activity activity = BaseTopActivity.getTopActivity();
                            Intent intent = new Intent(activity, VodPlayerActivity.class);
                            intent.putExtra("videoDetail", data);
                            activity.startActivity(intent);
                        }
                    }

                    @Override
                    public void errorResult(Serializable result, String path, int requestCode, int resultCode) {
                        JkysLog.d("wuwx", "error");
                    }
                }, id);
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

    public List<GetVideoListRespData> getDataList() {
        return this.dataList;
    }

}
