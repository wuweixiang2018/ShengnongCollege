package com.education.shengnongcollege.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.education.shengnongcollege.BaseTopActivity;
import com.education.shengnongcollege.R;

public class MainSerchActivity extends BaseTopActivity {

    private EditText mSearchEt;
    private ImageView finsh;
    private ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_serch_activity);
        initView();
        initListener();
        initData();
    }

    private void initData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSearchEt.setFocusable(true);
                mSearchEt.setFocusableInTouchMode(true);
                mSearchEt.requestFocus();
                mSearchEt.findFocus();
                InputMethodManager inputManager =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(mSearchEt, 0);
            }
        }, 100);
    }

    private void initListener() {
        finsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mSearchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 关闭软键盘
                    ((InputMethodManager) mSearchEt.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    String keyWord = mSearchEt.getText().toString();
                    if (TextUtils.isEmpty(keyWord)) {
                        Toast.makeText(MainSerchActivity.this, R.string.toast_keyword_empty, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainSerchActivity.this, keyWord, Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });
    }

    private void initView(){
        mSearchEt=findViewById(R.id.search_et);
        finsh=findViewById(R.id.search_clear_iv);
        mListView=findViewById(R.id.serch_list);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.anim_enter_from_top, R.anim.anim_exit_from_bottom);
    }
}
