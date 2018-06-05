package com.education.shengnongcollege.common.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.education.shengnongcollege.R;
import com.education.shengnongcollege.common.adapter.SimpleCommentListAdapter;
import com.education.shengnongcollege.common.model.SimpleCommentModel;

/**
 * Created by wuweixiang on 18/6/5.
 */

public class CommentListView extends LinearLayout {
    private EditText commentET;
    private RecyclerView mRecyclerView;
    private SimpleCommentListAdapter mAdapter;

    public CommentListView(Context context) {
        super(context);
        init();
    }

    public CommentListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CommentListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        try {
            View view = View.inflate(getContext(), R.layout.comment_list_widget, this);
            commentET = view.findViewById(R.id.comment_et);
            mRecyclerView = view.findViewById(R.id.recycler_view);

            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mAdapter = new SimpleCommentListAdapter(getContext());
            mRecyclerView.setAdapter(mAdapter);

            SimpleCommentModel model = new SimpleCommentModel();
            model.content = "你好";
            model.nickname = "橘子";
            mAdapter.addComment(model);

            SimpleCommentModel model1 = new SimpleCommentModel();
            model1.content = "谢谢";
            model1.nickname = "淘淘";
            mAdapter.addComment(model1);
            mAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
