package com.handmark.pulltorefresh.library;

import android.content.Context;

import com.education.shengnongcollege.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;

/**
 * 
* @author hjq
* @Create at 2014年4月30日 下午2:36:40
* @Version 1.0
* <p>	
	处理列表的初始化
  </p>
 */
public class ListViewUtil {
	
	/**
	 * 下拉刷新控件的设置
	 * @param context 
	 * @param parent PullToRefreshListView列表
	 */
	public static void initPullToRefreshLabel(Context context,PullToRefreshListView parent) {
		parent.setMode(Mode.BOTH);
		
		ILoadingLayout proxyRefresh = parent.getLoadingLayoutProxy(true, false);
		proxyRefresh.setPullLabel(context.getString(R.string.base_text_pull_down_refresh));
		proxyRefresh.setReleaseLabel(context.getString(R.string.base_text_release_to_refresh));
		proxyRefresh.setRefreshingLabel(context.getString(R.string.base_label_refreshing_now));
		
		ILoadingLayout proxyLoadMore = parent.getLoadingLayoutProxy(false, true);
		proxyLoadMore.setPullLabel(context.getString(R.string.base_text_pull_up_loading_more));
		proxyLoadMore.setReleaseLabel(context.getString(R.string.base_text_release_to_loading_more));
		proxyLoadMore.setRefreshingLabel(context.getString(R.string.base_text_loading_more));
		
	}

	/**
	 * 下拉刷新控件的设置
	 * @param context 
	 * @param parent PullToRefreshListView列表
	 */
	public static void initPullToRefreshLabel(Context context,PullToRefreshListView parent, boolean refresh, boolean loadMore) {
		if (refresh && loadMore) {
			parent.setMode(Mode.BOTH);
		} else if (refresh) {
			parent.setMode(Mode.PULL_FROM_START);
		} else if (loadMore) {
			parent.setMode(Mode.PULL_FROM_END);
		} else {
			parent.setMode(Mode.DISABLED);
		}
		
		if (refresh) {
			ILoadingLayout proxy = parent.getLoadingLayoutProxy(true, false);
			proxy.setPullLabel(context.getString(R.string.base_text_pull_down_refresh));
			proxy.setReleaseLabel(context.getString(R.string.base_text_release_to_refresh));
			proxy.setRefreshingLabel(context.getString(R.string.base_label_refreshing_now));
		}
		if (loadMore) {
			ILoadingLayout proxy = parent.getLoadingLayoutProxy(false, true);
			proxy.setPullLabel(context.getString(R.string.base_text_pull_up_loading_more));
			proxy.setReleaseLabel(context.getString(R.string.base_text_release_to_loading_more));
			proxy.setRefreshingLabel(context.getString(R.string.base_text_loading_more));
		}
	}

	
	/**
	 * 下拉刷新控件的设置
	 * @param context 
	 * @param parent PullToRefreshListView列表
	 * @param first 是否是第一次加载
	 * @param refresh 是否可以下拉更新数据
	 * @param loadMore 是否可以上拉加载更多数据
	 */
	@Deprecated
	public static void initPullToRefreshLabel(Context context,PullToRefreshListView parent,boolean first, boolean refresh, boolean loadMore) {
		if (refresh && loadMore) {
			parent.setMode(Mode.BOTH);
		} else if (refresh) {
			parent.setMode(Mode.PULL_FROM_START);
		} else if (loadMore) {
			parent.setMode(Mode.PULL_FROM_END);
		} else {
			parent.setMode(Mode.DISABLED);
		}

		if (!first) {
			if (refresh) {
				ILoadingLayout proxy = parent.getLoadingLayoutProxy(true, false);
				proxy.setPullLabel(context.getString(R.string.base_text_pull_down_refresh));
				proxy.setReleaseLabel(context.getString(R.string.base_text_release_to_refresh));
				proxy.setRefreshingLabel(context.getString(R.string.base_label_refreshing_now));
			}
			if (loadMore) {
				ILoadingLayout proxy = parent.getLoadingLayoutProxy(false, true);
				proxy.setPullLabel(context.getString(R.string.base_text_pull_up_loading_more));
				proxy.setReleaseLabel(context.getString(R.string.base_text_release_to_loading_more));
				proxy.setRefreshingLabel(context.getString(R.string.base_text_loading_more));
			}
		} else {
			ILoadingLayout proxy = parent.getLoadingLayoutProxy(true, false);
			proxy.setPullLabel(context.getString(R.string.base_text_pull_down_refresh));
			proxy.setReleaseLabel(context.getString(R.string.base_text_release_to_refresh));
			proxy.setRefreshingLabel(context.getString(R.string.base_label_refreshing_now));
		}
	}
	
	
	/**
	 * 设置提示信息
	 * @param parent PullToRefreshListView列表
	 * @param text 提示信息
	 */
	public static void setHeaderLastUpdatedLabel(PullToRefreshListView parent, CharSequence text) {
		ILoadingLayout proxy = parent.getLoadingLayoutProxy(true, false);
		proxy.setLastUpdatedLabel(text);
	}
	
	/**
	 * 设置提示信息
	 * @param parent PullToRefreshListView列表
	 * @param text 提示信息
	 */
	public static void setFooterLastUpdatedLabel(PullToRefreshListView parent, CharSequence text) {
		ILoadingLayout proxy = parent.getLoadingLayoutProxy(false, true);
		proxy.setLastUpdatedLabel(text);
	}
	
}
