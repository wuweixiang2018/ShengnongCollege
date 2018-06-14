package com.education.shengnongcollege.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by jzf on 2015/10/22.
 * 像SharedPreferences中插入数据
 */
public class SpUtil {
    public static final String SP_FILE_NAME = "spdata";
//    public static final String SUGAR_FILE_NAME = "spsugar";
//
//    public static void inputSugarSP(Context mContext, String Key, Object object) {
//        inputSP(mContext, Key, object, SUGAR_FILE_NAME);
//    }
//
//    public static Object getSugarSP(Context mContext, String Key, Object defaultObject) {
//        return getSP(mContext, Key, defaultObject, SUGAR_FILE_NAME);
//    }
//
//    public static void deleteSugarByKey(Context mContext, String Key) {
//        deleteByKey(mContext, Key, SUGAR_FILE_NAME);
//    }
//
    public static void inputSP(Context mContext, String Key, Object object) {
        inputSP(mContext, Key, object, SP_FILE_NAME);
    }

    public static void inputSP(Context mContext, String Key, int object) {
        inputSP(mContext, Key, new Integer(object), SP_FILE_NAME);
    }

    public static int getSP(Context mContext, String Key, int defaultObject) {
        return (int) getSP(mContext, Key, new Integer(defaultObject), SP_FILE_NAME);

    }

    public static Object getSP(Context mContext, String Key, Object defaultObject) {
        return getSP(mContext, Key, defaultObject, SP_FILE_NAME);
    }

    public static void deleteByKey(Context mContext, String Key) {
        deleteByKey(mContext, Key, SP_FILE_NAME);
    }

    /**
     * SharedPreferences input
     */
    public static void inputSP(Context mContext, String Key, Object object, String spName) {
        if (null == object) return;
        @SuppressWarnings("static-access")
        SharedPreferences preferences = mContext.getSharedPreferences(spName, mContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        if (object instanceof String) {
            editor.putString(Key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(Key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(Key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(Key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(Key, (Long) object);
        } else {
            editor.putString(Key, object.toString());
        }
        editor.commit();
    }

    /**
     * SharedPreferences get
     */
    public static Object getSP(Context mContext, String Key, Object defaultObject, String spName) {
        @SuppressWarnings("static-access")
        SharedPreferences sp = mContext.getSharedPreferences(spName, mContext.MODE_PRIVATE);

        if (defaultObject instanceof String) {
            return sp.getString(Key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(Key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(Key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(Key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(Key, (Long) defaultObject);
        }
        return defaultObject;
    }

    /**
     * 根据key删除数据
     *
     * @param mContext
     * @param Key
     */
    public static void deleteByKey(Context mContext, String Key, String spName) {
        SharedPreferences preferences = mContext.getSharedPreferences(spName, mContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(Key);
        editor.commit();
    }
}
