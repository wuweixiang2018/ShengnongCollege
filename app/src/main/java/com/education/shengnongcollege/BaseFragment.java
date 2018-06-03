package com.education.shengnongcollege;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;

/**
 * @author JIDP
 * @date 2015-7-15-下午4:07:10
 * @describe
 */
public class BaseFragment extends Fragment {

	protected ImageView mWatermarkIv;
	private FragmentActivity activity;
	protected boolean isShow = true;
	protected boolean hasBack = false;
	protected String showRedDocType;//显示红点类型  -1为只显示小红点，1为显示红点加未读数，0为不显示
	protected String topBgColor;
	protected String topTitleColor;
	protected boolean needSetBarColor = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = getActivity();
		if(getArguments() != null) {
			hasBack = getArguments().getBoolean("back");
			showRedDocType = getArguments().getString("showRedDocType");
		}
	}

	public void changeRefreshData(){
		this.isShow = true;
	}
	public void setShow(boolean isShow){
		this.isShow = isShow;
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
