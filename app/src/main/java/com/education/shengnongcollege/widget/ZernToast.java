package com.education.shengnongcollege.widget;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by on
 * Author: Zern
 * DATE: 2015/12/29
 * Time: 00:14
 * email: AndroidZern@163.com
 */
//解决Toast重复出现的情况
public class ZernToast {

    private static Toast mToast = null;
    private static Toast mToast2 = null;
    private static Toast mToast3 = null;

    public static void showToast(Context context, String msgInfo) {
        if (mToast == null) {
            mToast = Toast.makeText(context.getApplicationContext(), "", Toast.LENGTH_SHORT);
        }
        mToast.setText(msgInfo);
        mToast.show();
    }

    // 设置Toast显示的位置
    public static void showToast(Context context, String msgInfo, int gravity, int xOffset, int yOffset) {
        if (mToast == null) {
            mToast = Toast.makeText(context.getApplicationContext(), "", Toast.LENGTH_SHORT);
        }
        mToast.setText(msgInfo);
        mToast.setGravity(gravity, xOffset, yOffset);
        mToast.show();
    }


    // 设置Toast显示的时间
    public static void showToastForTime(Context context, String msgInfo, int gravity, int xOffset, int yOffset, int time) {
        if (mToast == null) {
            mToast = Toast.makeText(context.getApplicationContext(), "", time);
        }
        mToast.setText(msgInfo);
        mToast.setGravity(gravity, xOffset, yOffset);
        mToast.show();
    }

    // 自定义Toast。自定义字体大小
    public static void showToastForCustom(Context context, String msgInfo, int gravity, int xOffset, int yOffset, int time, int sp) {
//        if (mToast == null) {
//            mToast = Toast.makeText(context.getApplicationContext(), "", time);
//        }
//        mToast.setText(msgInfo);
//        mToast.setGravity(gravity, xOffset, yOffset);
//        mToast.show();
        if (mToast3 == null) {
            mToast3 = Toast.makeText(context.getApplicationContext(), "", time);
        }
//        Toast toast = Toast.makeText(context.getApplicationContext(), "", time);
        LinearLayout layout = (LinearLayout) mToast3.getView();
        TextView tv = (TextView) layout.getChildAt(0);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, sp);
        tv.setText(msgInfo);
        mToast3.setGravity(gravity, xOffset, yOffset);
        mToast3.show();
    }

    public static void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
    }


}
