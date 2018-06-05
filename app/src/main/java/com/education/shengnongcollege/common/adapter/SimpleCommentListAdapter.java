package com.education.shengnongcollege.common.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.education.shengnongcollege.R;
import com.education.shengnongcollege.common.model.SimpleCommentModel;
import com.education.shengnongcollege.play.NewVodListAdapter;

import java.util.ArrayList;

/**
 * Created by wuweixiang on 18/6/5.
 */

public class SimpleCommentListAdapter extends RecyclerView.Adapter<SimpleCommentListAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<SimpleCommentModel> mCommentList;

    public SimpleCommentListAdapter(Context context) {
        mContext = context;
        mCommentList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_simple_comment, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final SimpleCommentModel data = mCommentList.get(position);
        holder.nicknameTV.setText(data.nickname);
        holder.contentTV.setText(data.content);
    }

    @Override
    public int getItemCount() {
        return mCommentList.size();
    }

    public void clear() {
        mCommentList.clear();
        notifyDataSetChanged();
    }

    public void addComment(SimpleCommentModel data) {
        notifyItemInserted(mCommentList.size());
        mCommentList.add(data);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nicknameTV;
        private TextView contentTV;

        public ViewHolder(final View itemView) {
            super(itemView);
            contentTV = (TextView) itemView.findViewById(R.id.content_tv);
            nicknameTV = (TextView) itemView.findViewById(R.id.nickname_tv);
        }
    }
}
