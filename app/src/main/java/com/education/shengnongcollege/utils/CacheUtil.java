package com.education.shengnongcollege.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 用于缓存数据的存取工具类
 *
 * @author JimmyZhang
 */

public class CacheUtil {

	public static final String ICOLLEAGUE2_CACHE_SN = "shennong.cache";

	protected CacheUtil() {
	}

	private static CacheUtil instance;

	public synchronized static CacheUtil getInstance() {
		if (null == instance) {
			instance = new CacheUtil();
		}
		return instance;
	}

	protected Context context;

	public void init(Context context) {
		this.context = context;
	}

	public void clearData(){
		if(context == null){
			return;
		}
		SharedPreferences sp = context.getSharedPreferences(ICOLLEAGUE2_CACHE_SN, Context.MODE_PRIVATE);
		sp.edit().clear().commit();
	}
	/**
	 * 保存用户id
	 *
	 * @param userId
	 */
	public void setUserId(String userId) {
		if (null == context) {
			return;
		}
		SharedPreferences sp = context.getSharedPreferences(ICOLLEAGUE2_CACHE_SN, Context.MODE_PRIVATE);
		sp.edit().putString("UserId", userId).commit();
	}


	/**
	 * 获取用户id
	 */
	public String getUserId() {
		if (null == context) {
			return "";
		}
		SharedPreferences sp = context.getSharedPreferences(ICOLLEAGUE2_CACHE_SN, Context.MODE_PRIVATE);
		String value = sp.getString("UserId", "");
		return value;
	}
	/**
	 * 保存用户登录账号
	 *
	 * @param userName
	 */
	public void setUserName(String userName) {
		if (null == context) {
			return;
		}
		SharedPreferences sp = context.getSharedPreferences(ICOLLEAGUE2_CACHE_SN, Context.MODE_PRIVATE);
		sp.edit().putString("userName", userName).commit();
	}


	/**
	 * 获取用户登录账号
	 */
	public String getUserName() {
		if (null == context) {
			return "";
		}
		SharedPreferences sp = context.getSharedPreferences(ICOLLEAGUE2_CACHE_SN, Context.MODE_PRIVATE);
		String value = sp.getString("userName", "");
		return value;
	}
	/**
	 * 保存用户登录密码
	 *
	 * @param UserPassword
	 */
	public void setUserPassword(String UserPassword) {
		if (null == context) {
			return;
		}
		SharedPreferences sp = context.getSharedPreferences(ICOLLEAGUE2_CACHE_SN, Context.MODE_PRIVATE);
		sp.edit().putString("UserPassword", UserPassword).commit();
	}


	/**
	 * 获取用户登录密码
	 */
	public String getUserPassword() {
		if (null == context) {
			return "";
		}
		SharedPreferences sp = context.getSharedPreferences(ICOLLEAGUE2_CACHE_SN, Context.MODE_PRIVATE);
		String value = sp.getString("UserPassword", "");
		return value;
	}
	/**
	 * 保存用户是否有直播权限
	 *
	 * @param AllowLiveBroadcast
	 */
	public void setAllowLiveBroadcast(int AllowLiveBroadcast) {
		if (null == context) {
			return;
		}
		SharedPreferences sp = context.getSharedPreferences(ICOLLEAGUE2_CACHE_SN, Context.MODE_PRIVATE);
		sp.edit().putInt("AllowLiveBroadcast", AllowLiveBroadcast).commit();
	}


	/**
	 * 获取用户是否有直播权限 默认0 没有
	 */
	public int getAllowLiveBroadcast() {
		if (null == context) {
			return 0;
		}
		SharedPreferences sp = context.getSharedPreferences(ICOLLEAGUE2_CACHE_SN, Context.MODE_PRIVATE);
		int value = sp.getInt("AllowLiveBroadcast", 0);
		return value;
	}

}
