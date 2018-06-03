package com.education.shengnongcollege.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 
* @author pjy
* @Version 1.0
* <p>	
	自定义ViewPager，可以设置不可滑动
  </p>
 */
public class CustomViewPager extends ViewPager {
    private boolean isCanScroll = false;  
    private boolean smoothScroll = true;

    public boolean isSmoothScroll() {
        return smoothScroll;
    }

    public void setSmoothScroll(boolean smoothScroll) {
        this.smoothScroll = smoothScroll;
    }

    public CustomViewPager(Context context) {
        super(context);  
    }  
  
    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);  
    }  
  
    public void setScanScroll(boolean isCanScroll) {  
        this.isCanScroll = isCanScroll;  
    }  
  
    @Override
    public void scrollTo(int x, int y) {  
            super.scrollTo(x, y);  
    }  
  
    /*@Override  
    public boolean onTouchEvent(MotionEvent arg0) {  
        if(isCanScroll) {
        	return super.onTouchEvent(arg0); 
        } else {
        	return false;
        }
    }  */
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        try
        {
            if (isCanScroll) {
                return super.onInterceptTouchEvent(event);
            } else {
                return false;
            }
        }
        catch (Exception e)
        {
            return false;
        }
    }

    @Override
    public boolean onTouchEvent (MotionEvent ev)
    {
        try
        {
            if (isCanScroll) {
                return super.onTouchEvent (ev);
            } else {
                return false;
            }
        }
        catch (Exception e)
        {
            return false;
        }
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {  
        // TODO Auto-generated method stub  
        super.setCurrentItem(item, this.smoothScroll);
    }


    @Override
    public void setCurrentItem(int item) {  
        // TODO Auto-generated method stub  
        super.setCurrentItem(item,smoothScroll);
    }  
}  
