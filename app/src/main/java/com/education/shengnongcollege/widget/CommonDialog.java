package com.education.shengnongcollege.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.education.shengnongcollege.R;
import com.education.shengnongcollege.utils.DeviceUtil;

//import android.util.DisplayMetrics;


/**
 * Created by ylei on 16/5/17.
 */
public class CommonDialog {
    private final TextView tvTitle;
    private View tvLeft_2;
    private View tvLeft;
    private View tvRight_2;
    private Window window;
    private Dialog mDialog;
    private Context mContext;
    private ImageView dialog_icon;
    private Builder builder;

    public ImageView getDialog_icon() {
        return dialog_icon;
    }

    private CommonDialog(Context context, Builder builder) {
        this.builder = builder;
        mContext = context;
        mDialog = new Dialog(mContext, R.style.ImageloadingDialogStyle);
        window = mDialog.getWindow();
        window.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);

        mDialog.setContentView(R.layout.nowifidialog);
        mDialog.setCancelable(false);
        window = mDialog.getWindow();
        tvTitle = (TextView) (window.findViewById(R.id.title));
        tvTitle.setText(builder.title);
        dialog_icon = window.findViewById(R.id.dialog_icon);
        if (!builder.hasTitle) {
            ((TextView) (window.findViewById(R.id.title))).setVisibility(View.GONE);

        }else{
            ((TextView) (window.findViewById(R.id.title))).setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(builder.des)) {
            ((TextView) (window.findViewById(R.id.tv_des))).setVisibility(View.GONE);
        } else {
            ((TextView) (window.findViewById(R.id.tv_des))).setText(builder.des);
        }
        if (builder.checkable) {
            ((TextView) (window.findViewById(R.id.tv_hinttitle))).setText(builder.checkedtitle);
            ((window.findViewById(R.id.iv_not_hint))).setSelected(builder.checkedvalue);
            window.findViewById(R.id.rl_nothint).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((window.findViewById(R.id.iv_not_hint))).setSelected(!((window.findViewById(R.id.iv_not_hint))).isSelected());
                }
            });
        } else {
            window.findViewById(R.id.rl_nothint).setVisibility(View.GONE);
        }

        tvLeft_2 = window.findViewById(R.id.tv_btnleft_2);
        tvLeft = window.findViewById(R.id.tv_btnleft);
        if (!TextUtils.isEmpty(builder.leftText)) {
            ((TextView) tvLeft).setText(builder.leftText);
            ((TextView) tvLeft_2).setText(builder.leftText);
        }
        if (builder.twobutton) {
            window.findViewById(R.id.ll_twobtn).setVisibility(View.VISIBLE);
            tvLeft.setVisibility(View.GONE);
            if (builder.clickListenerfirtst != null) {
                tvLeft_2.setOnClickListener(builder.clickListenerfirtst);
            }
            tvRight_2 = window.findViewById(R.id.tv_btnright_2);
            ((TextView) tvRight_2).setText(builder.rightText);
            if (builder.clickListenersecond != null) {
                tvRight_2.setOnClickListener(builder.clickListenersecond);

            }
            if (builder.isLeft) {
                tvLeft_2.setBackgroundResource(R.drawable.solid_blue_btn_bg);

                ((TextView) tvLeft_2).setTextColor(0xffffffff);
                tvRight_2.setBackgroundResource(R.drawable.hollow_blue_btn_bg);
                ((TextView) tvRight_2).setTextColor(0xff4991FD);
            } else {
                tvLeft_2.setBackgroundResource(R.drawable.hollow_blue_btn_bg);
                ((TextView) tvLeft_2).setTextColor(0xff4991FD);
                tvRight_2.setBackgroundResource(R.drawable.solid_blue_btn_bg);
                ((TextView) tvRight_2).setTextColor(0xffffffff);

            }

        } else {
            window.findViewById(R.id.ll_twobtn).setVisibility(View.GONE);
            tvLeft.setVisibility(View.VISIBLE);
            if (builder.clickListenerfirtst != null) {
                tvLeft.setOnClickListener(builder.clickListenerfirtst);
            }
            if (builder.isLeft) {
                tvLeft.setBackgroundResource(R.drawable.solid_blue_btn_bg);
                ((TextView) tvLeft).setTextColor(0xffffffff);
            } else {
                tvLeft_2.setBackgroundResource(R.drawable.hollow_blue_btn_bg);
                ((TextView) tvLeft_2).setTextColor(0xff4991FD);
            }
        }


    }

    /**
     * 推荐使用builder的方式构造
     * 但是某些场合下title会频繁改变
     */
    @Deprecated
    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    /**
     * 推荐使用builder的方式构造
     * 但是某些场合下点击事件会改变
     */
    @Deprecated
    public void setOnClickListernLeft(View.OnClickListener onClickListernLeft) {
        tvLeft.setOnClickListener(onClickListernLeft);
        tvLeft_2.setOnClickListener(onClickListernLeft);
    }

    /**
     * 推荐使用builder的方式构造
     * 但是某些场合下点击事件会改变
     */
    @Deprecated
    public void setOnclickListenRight(View.OnClickListener onClickListernRight) {
        tvRight_2.setOnClickListener(onClickListernRight);
    }

    public boolean getChecked() {
        return ((window.findViewById(R.id.iv_not_hint))).isSelected();

    }

    public void show() {
        mDialog.show();
        WindowManager.LayoutParams lp = window.getAttributes();
//        DisplayMetrics metric = new DisplayMetrics();
//        window.getWindowManager().getDefaultDisplay().getMetrics(metric);
//        int height = DeviceUtil.getScreenH();
//        lp.y = (int) (0.175 * height);
        if (builder.ly == 0) {
            builder.ly = (int) (DeviceUtil.getScreenH(mContext) * 0.3);
        }
        lp.y = builder.ly;
        lp.width = (int) (DeviceUtil.getScreenW(mContext) * 0.75);
        lp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
        window.setAttributes(lp);

    }

    public void dissmiss() {
        mDialog.dismiss();
    }

    public boolean isShowing() {
        return mDialog.isShowing();
    }

    public static class Builder {

        private boolean twobutton = true;
        private int ly;
        private String title = "非wifi连接";
        private String des = "专题教学会使用较多流量，推荐您在wifi状态下使用";
        private boolean checkable = true;
        private String checkedtitle = "不再提示";
        private boolean checkedvalue = false;
        private View.OnClickListener clickListenerfirtst;
        private View.OnClickListener clickListenersecond;
        private boolean isLeft = true;
        private boolean hasTitle = true;
        private String leftText;
        private String rightText;
//        private int visibilityByTitle = View.VISIBLE;
//        private int visibilityByDesc = View.VISIBLE;
//
//        public Builder setVisibilityByTitle(int visibilityByTitle) {
//            this.visibilityByTitle = visibilityByTitle;
//            return this;
//        }
//
//        public Builder setVisibilityByDesc(int visibilityByDesc) {
//            this.visibilityByDesc = visibilityByDesc;
//            return this;
//        }

        public Builder setClickListenerfirtst(View.OnClickListener clickListenerfirtst) {
            this.clickListenerfirtst = clickListenerfirtst;
            return this;
        }

        public Builder setClickListenersecond(View.OnClickListener clickListenersecond) {
            this.clickListenersecond = clickListenersecond;
            return this;
        }

        public Builder setTwobutton(boolean twobutton) {
            this.twobutton = twobutton;
            return this;

        }

        public Builder setLy(int ly) {
            this.ly = ly;
            return this;

        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;

        }

        public Builder setHasTitle(boolean hasTitle) {
            this.hasTitle = hasTitle;
            return this;
        }

        public Builder setDes(String des) {
            this.des = des;
            return this;

        }

        public Builder setCheckable(boolean checkable) {
            this.checkable = checkable;
            return this;

        }

        public Builder setCheckedtitle(String checkedtitle) {
            this.checkedtitle = checkedtitle;
            return this;

        }

        public Builder setCheckedvalue(boolean checkedvalue) {
            this.checkedvalue = checkedvalue;
            return this;

        }

        public Builder setImportantPosLeftOrRight(boolean isLeft) {
            this.isLeft = isLeft;
            return this;
        }

        public Builder setButtonText(String leftText, String rightText) {
            this.leftText = leftText;
            this.rightText = rightText;
            return this;
        }

        public CommonDialog build(Context context) {
            return new CommonDialog(context, this);
        }
    }
}
