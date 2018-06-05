package com.education.shengnongcollege.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.education.shengnongcollege.BaseFragment;
import com.education.shengnongcollege.fragment.TopTestFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
public class HomeTopTabAdapter extends FragmentPagerAdapter {

    private Map<String,BaseFragment> mapFragment = new HashMap<>();
    private final List<String> titles;

    public HomeTopTabAdapter(FragmentManager fm,List<String> titles) {
        super(fm);
        this.titles = titles;
    }

    public HomeTopTabAdapter(FragmentManager fm) {
        super(fm);
        this.titles = new ArrayList<>();
    }

    @Override
    public BaseFragment getItem(int arg0) {
        if(mapFragment.get(arg0+"") == null){
            BaseFragment fragment = TopTestFragment.newInstance(arg0+"");
            mapFragment.put(arg0+"",fragment);
        }
        return mapFragment.get(arg0+"");
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(titles != null && position < titles.size()){
            return titles.get(position);
        }else{
            return position+"";
        }
    }

    @Override
    public int getCount() {
        if(titles != null && titles.size() > 0){
            return titles.size();
        }
        return mapFragment.size();
    }
}
