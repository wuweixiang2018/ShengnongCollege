package com.education.shengnongcollege.common.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.education.shengnongcollege.R;
import com.education.shengnongcollege.common.model.CommentModel;
import com.education.shengnongcollege.common.model.SimpleCommentModel;
import com.education.shengnongcollege.play.NewVodListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuweixiang on 18/6/5.
 */

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<CommentModel> mCommentList;
    private OnItemClickLitener mOnItemClickLitener;

    public CommentListAdapter(Context context) {
        mContext = context;
        mCommentList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_comment, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final CommentModel data = mCommentList.get(position);
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

    public void addComment(CommentModel data) {
        notifyItemInserted(mCommentList.size());
        mCommentList.add(data);
    }

    public void addCommentList(List<CommentModel> list) {
        notifyItemInserted(mCommentList.size());
        mCommentList.addAll(list);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nicknameTV;
        private TextView contentTV;
        private TextView timeTV;

        public ViewHolder(final View itemView) {
            super(itemView);
            contentTV = (TextView) itemView.findViewById(R.id.content_tv);
            nicknameTV = (TextView) itemView.findViewById(R.id.nickname_tv);
            timeTV = itemView.findViewById(R.id.time_tv);
        }
    }

    public void setOnItemClickLitener(OnItemClickLitener listener) {
        mOnItemClickLitener = listener;
    }

    public interface OnItemClickLitener {
        void onItemClick(int position, CommentModel commentModel);
    }
}
