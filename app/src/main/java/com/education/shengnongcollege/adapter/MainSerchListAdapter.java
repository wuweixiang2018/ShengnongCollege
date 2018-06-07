package com.education.shengnongcollege.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.education.shengnongcollege.R;
import com.education.shengnongcollege.model.GetVideoListRespData;

import java.util.ArrayList;
import java.util.List;


/**
 * 社会家园
 */
public class MainSerchListAdapter extends BaseAdapter {

    private List<GetVideoListRespData> items;
    private LayoutInflater inflater;
    private Context context;
    public MainSerchListAdapter(Context context, List<GetVideoListRespData> items) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        if (items == null) {
            this.items = new ArrayList<>();
        } else {
            this.items = items;
        }
    }


    @Override
    public int getCount() {
        return this.items == null ? 0 : this.items.size();
    }

    @Override
    public GetVideoListRespData getItem(int i) {
        return this.items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            view = inflater.inflate(R.layout.main_serchlist_item, null);
            holder = new ViewHolder();
            holder.sq_context = view.findViewById(R.id.recommend_context);
            holder.sq_title = view.findViewById(R.id.recommend_title);
            holder.sq_image =view.findViewById(R.id.recommend_image);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        GetVideoListRespData item = getItem(position);

        holder.sq_context.setText((CharSequence) item.getProfile());
        holder.sq_title.setText(item.getTitle());
        if(!TextUtils.isEmpty(item.getCoverUrl())){
            Glide.with(context).load(item.getCoverUrl()).into(holder.sq_image);
        }
        return view;
    }

    class ViewHolder {
        public TextView sq_context;
        public TextView sq_title;
        public ImageView sq_image;
    }

}
