package com.education.shengnongcollege.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.education.shengnongcollege.BaseFragment;
import com.education.shengnongcollege.bean.MenuItemNav;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2017/12/18.
 */
public class MainPagerAdapter extends android.support.v4.app.FragmentStatePagerAdapter {

	private int PAGE_COUNT ;
	public List<MenuItemNav> modelMap = new ArrayList<>();
	private Map<String,BaseFragment> fragments = new HashMap<>();
	public Map<String,BaseFragment> getFragments(){
		return fragments;
	}


	public MainPagerAdapter(FragmentManager fm, Context context) {
		super(fm);
		modelMap = new ArrayList<>();
		MenuItemNav nav=new MenuItemNav();
		nav.id="main_zhuye";
		nav.title="主页";
		nav.norIcon="main_tab_zhuye_nor";
		nav.preIcon="main_tab_zhuye_pre";
		nav.textNorColor="#a6a6b2";
		nav.textSelColor="#FF9800";
		nav.defaultShow=true;
		modelMap.add(nav);
		MenuItemNav nav1=new MenuItemNav();
		nav1.id="main_fenlei";
		nav1.title="分类";
		nav1.norIcon="main_tab_fenlei_nor";
		nav1.preIcon="main_tab_fenlei_pre";
		nav1.textNorColor="#a6a6b2";
		nav1.textSelColor="#FF9800";
		nav1.defaultShow=false;
		modelMap.add(nav1);
		MenuItemNav nav2=new MenuItemNav();
		nav2.id="main_stu";
		nav2.title="我的学习";
		nav2.norIcon="main_tab_mystu_nor";
		nav2.preIcon="main_tab_mystu_pre";
		nav2.textNorColor="#a6a6b2";
		nav2.textSelColor="#FF9800";
		nav2.defaultShow=false;
		modelMap.add(nav2);
		MenuItemNav nav3=new MenuItemNav();
		nav3.id="main_mine";
		nav3.title="我的";
		nav3.norIcon="main_tab_mine_nor";
		nav3.preIcon="main_tab_mine_pre";
		nav3.textNorColor="#a6a6b2";
		nav3.textSelColor="#FF9800";
		nav3.defaultShow=false;
		modelMap.add(nav3);
		PAGE_COUNT = modelMap.size();
	}

	@Override
	public BaseFragment getItem(int position) {
		BaseFragment fragment =null ;
		if(fragments.get(position+"") != null){
			fragment = fragments.get(position+"");
		}else{
			String className = "";
			switch (modelMap.get(position).id){
				case "main_zhuye":
					className = "com.education.shengnongcollege.fragment.MainFragment";
					break;
				case "main_fenlei":
					className = "com.education.shengnongcollege.fragment.ClassifyFragment";
					break;
				case "main_stu":
					className = "com.education.shengnongcollege.fragment.MyStudyFragment";
					break;
				case "main_mine":
					className = "com.education.shengnongcollege.fragment.MineFragment";
					break;
				default:
					className = "com.education.shengnongcollege.fragment.MineFragment";
					break;
			}
			fragment = getClass(className);
			Bundle bundle = new Bundle();
			bundle.putString("fragmentId",modelMap.get(position).id);
			if(!TextUtils.isEmpty(modelMap.get(position).defaulturl)) {
				bundle.putString("defaulturl", modelMap.get(position).defaulturl);
			}
			if(!TextUtils.isEmpty(modelMap.get(position).appCode)) {
				bundle.putString("appCode", modelMap.get(position).appCode);
			}
			if(!TextUtils.isEmpty(modelMap.get(position).topTitle)){
				bundle.putString("topTitle", modelMap.get(position).topTitle);
			}
			if(!TextUtils.isEmpty(modelMap.get(position).title)) {
				bundle.putString("title", modelMap.get(position).title);
			}
			if(!TextUtils.isEmpty(modelMap.get(position).topTitleColor)) {
				bundle.putString("topTitleColor", modelMap.get(position).topTitleColor);
			}
			if(!TextUtils.isEmpty(modelMap.get(position).topBgColor)) {
				bundle.putString("topBgColor", modelMap.get(position).topBgColor);
			}
			bundle.putBoolean("hideBack",true);
			bundle.putBoolean("needSetBarColor",true);
			bundle.putString("showRedDocType", modelMap.get(position).showRedDocType);
			bundle.putBoolean("showScanner", modelMap.get(position).showScanner);
			fragment.setArguments(bundle);
			fragments.put(position + "", fragment);
		}
		return fragment;
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position)
	{
		BaseFragment f = (BaseFragment) super.instantiateItem(container, position);
		return  f;
	}

	@Override
	public int getCount() {
		return PAGE_COUNT;
	}


	@Override
	public CharSequence getPageTitle(int position) {
		if(modelMap != null && modelMap.get(position) != null){
			return modelMap.get(position).title;
		}else{
			return position+"";
		}
	}
	@Override
	public int getItemPosition(Object object)
	{
		return POSITION_NONE;
	}


	private BaseFragment getClass(String className) {
		Class<?> clazz;
		try {
			clazz = Class.forName(className);
			return (BaseFragment) clazz.newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public int getCurrentIndexById(String id){
		int index = 0;
		if(modelMap != null){
			for(int i=0;i<modelMap.size();i++){
				if(TextUtils.equals(modelMap.get(i).id,id)){
					index = i;
					break;
				}
			}
		}
		return index;
	}
	public int getDefaultIndex(){
		int index = 0;
		if(modelMap != null){
			for(int i=0;i<modelMap.size();i++){
				if(modelMap.get(i).defaultShow){
					index = i;
					break;
				}
			}
		}
		return index;
	}

}
