/*
 * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.education.shengnongcollege.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.education.shengnongcollege.R;
import com.education.shengnongcollege.bean.MenuItemNav;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@SuppressLint("NewApi")
public class PagerSlidingTabStrip2 extends HorizontalScrollView {

	public void setScanScroll(boolean isCanScroll) {
        pager.setScanScroll(isCanScroll);
    }

	// @formatter:off
	private static final int[] ATTRS = new int[] {
		android.R.attr.textSize,
		android.R.attr.textColor
    };
	// @formatter:on

	private LinearLayout.LayoutParams defaultTabLayoutParams;
	private LinearLayout.LayoutParams expandedTabLayoutParams;

	private final PageListener pageListener = new PageListener();
	public OnPageChangeListener delegatePageListener;

	public LinearLayout tabsContainer;
	private CustomViewPager pager;

	private int tabCount;

	private int currentPosition = 0;
	private float currentPositionOffset = 0f;

	private Paint rectPaint;
	private Paint dividerPaint;

	private int indicatorColor = 0xFFf1a336;
	private int underlineColor = 0x1A000000;
	private int dividerColor = 0x00000000;

	private boolean shouldExpand = false;
	private boolean textAllCaps = true;

	private int scrollOffset = 8;
	private int indicatorHeight = 18;
	private int underlineHeight = 1;
	private int dividerPadding = 1;
	private int tabPadding = 8;
	private int mMinPadding = 0;
	private int dividerWidth = 1;
	private int indicatorMarginLeftRight = 0;

	private int tabTextSize = 12;
	private int tabTextColor = 0xFF727272;
	private Typeface tabTypeface = null;
	private int tabTypefaceStyle = Typeface.BOLD;

	private int selectedTabTextColor = 0xFFf1a336;
	private int selectedPosition = 0;

	private int lastScrollX = 0;
	private int itemResId = R.layout.base_pager_sliding_tab_item;
	private int tabBackgroundResId = R.drawable.background_tab;
	public List<MenuItemNav> mItems = new ArrayList<>();
	public Map<String,String> mapUnReadNum = new HashMap<>();
	private Locale locale;

	public PagerSlidingTabStrip2(Context context) {
		this(context, null);
	}

	public PagerSlidingTabStrip2(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public PagerSlidingTabStrip2(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		setFillViewport(true);
		setWillNotDraw(false);

		tabsContainer = new LinearLayout(context);
		tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		tabsContainer.setLayoutParams(layoutParams);
		addView(tabsContainer);

		DisplayMetrics dm = getResources().getDisplayMetrics();

		scrollOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, scrollOffset, dm);
		indicatorHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, indicatorHeight, dm);
		underlineHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, underlineHeight, dm);
		dividerPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dividerPadding, dm);
		indicatorMarginLeftRight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, indicatorMarginLeftRight, dm);
		tabPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, tabPadding, dm);
		dividerWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dividerWidth, dm);
		tabTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, tabTextSize, dm);

		// get system attrs (android:textSize and android:textColor)

		TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);

		tabTextSize = a.getDimensionPixelSize(0, tabTextSize);
		tabTextColor = a.getColor(1, tabTextColor);

		a.recycle();

		// get custom attrs

		a = context.obtainStyledAttributes(attrs, R.styleable.PagerSlidingTabStrip);

		indicatorColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsIndicatorColor, indicatorColor);
		underlineColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsUnderlineColor, underlineColor);
		dividerColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsDividerColor, dividerColor);
		indicatorHeight = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsIndicatorHeight, indicatorHeight);
		underlineHeight = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsUnderlineHeight, underlineHeight);
		indicatorMarginLeftRight = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsIndicatorMarginLeftRight, indicatorMarginLeftRight);
		dividerPadding = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsDividerPadding, dividerPadding);
		tabPadding = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsTabPaddingLeftRight, tabPadding);
		tabBackgroundResId = a.getResourceId(R.styleable.PagerSlidingTabStrip_pstsTabBackground, tabBackgroundResId);
		shouldExpand = a.getBoolean(R.styleable.PagerSlidingTabStrip_pstsShouldExpand, shouldExpand);
		scrollOffset = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsScrollOffset, scrollOffset);
		textAllCaps = a.getBoolean(R.styleable.PagerSlidingTabStrip_pstsTextAllCaps, textAllCaps);
		tabTextSize = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsTextSize, tabTextSize);

		a.recycle();

		rectPaint = new Paint();
		rectPaint.setAntiAlias(true);
		rectPaint.setStyle(Style.FILL);

		dividerPaint = new Paint();
		dividerPaint.setAntiAlias(true);
		dividerPaint.setStrokeWidth(dividerWidth);

		defaultTabLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		expandedTabLayoutParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);

		if (locale == null) {
			locale = getResources().getConfiguration().locale;
		}
	}

	public void setItemResId(int resId){
		this.itemResId = resId;
	}

	public void setmItems(List<MenuItemNav> list){
		this.mItems = list;
	}

	public void setViewPager(CustomViewPager pager) {
		this.pager = pager;

		if (pager.getAdapter() == null) {
			throw new IllegalStateException("ViewPager does not have adapter instance.");
		}

		pager.setOnPageChangeListener(pageListener);

		notifyDataSetChanged();
	}

	public void setOnPageChangeListener(OnPageChangeListener listener) {
		this.delegatePageListener = listener;
	}

	public void notifyDataSetChanged() {

		tabsContainer.removeAllViews();

		tabCount = pager.getAdapter().getCount();

		for (int i = 0; i < tabCount; i++) {
			addViewTab(i);
		}

		updateTabStyles();

		getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@SuppressWarnings("deprecation")
			@SuppressLint("NewApi")
			@Override
			public void onGlobalLayout() {

				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
					getViewTreeObserver().removeGlobalOnLayoutListener(this);
				} else {
					getViewTreeObserver().removeOnGlobalLayoutListener(this);
				}

				currentPosition = pager.getCurrentItem();
				scrollToChild(currentPosition, 0);
			}
		});

	}

	private void addViewTab(final int position) {
		View view = LayoutInflater.from(getContext()).inflate(itemResId, null);
		try {
			setItemStyle(view, position == pager.getCurrentItem(), position);
		} catch (Exception e) {
			e.printStackTrace();
		}
		addTab(position, view);
	}

	public void setItemStyle(View view, boolean check, int position){
		try {
			TextView tv = (TextView) view.findViewById(R.id.textView);
			TextView numTv = (TextView) view.findViewById(R.id.top_num_tv);
			ImageView iv = (ImageView) view.findViewById(R.id.imageView);
			ImageView redDoc = (ImageView) view.findViewById(R.id.top_red_doc_iv);
			String text = "";
			if(mapUnReadNum != null){
				text = mapUnReadNum.get(mItems.get(position).id);
			}
			if(TextUtils.equals(text,"-1")){
				if(redDoc != null) {
					redDoc.setVisibility(VISIBLE);
				}
				numTv.setVisibility(View.INVISIBLE);
			}else if(!TextUtils.isEmpty(text)){
				numTv.setText(text);
				numTv.setVisibility(View.VISIBLE);
				if(redDoc != null) {
					redDoc.setVisibility(INVISIBLE);
				}
			}else{
				if(redDoc != null) {
					redDoc.setVisibility(INVISIBLE);
				}
				numTv.setVisibility(View.INVISIBLE);
			}
			tv.setText(mItems.get(position).title);
			int resId = 0;
			if(TextUtils.isEmpty(mItems.get(position).textNorColor) || TextUtils.isEmpty(mItems.get(position).textSelColor)){
				if (check) {
					tv.setTextColor(Color.parseColor("#339ffb"));
					resId = getContext().getResources().getIdentifier(getContext().getPackageName() + ":drawable/" + mItems.get(position).preIcon, null, null);
				} else {
					tv.setTextColor(Color.parseColor("#535353"));
					resId = getContext().getResources().getIdentifier(getContext().getPackageName() + ":drawable/" + mItems.get(position).norIcon, null, null);
				}
			}else {
				if (check) {
					resId = getContext().getResources().getIdentifier(getContext().getPackageName() + ":drawable/" + mItems.get(position).preIcon, null, null);
					tv.setTextColor(Color.parseColor(mItems.get(position).textSelColor));
				} else {
					resId = getContext().getResources().getIdentifier(getContext().getPackageName() + ":drawable/" + mItems.get(position).norIcon, null, null);
					tv.setTextColor(Color.parseColor(mItems.get(position).textNorColor));
				}
			}
			if(resId > 0) {
				iv.setImageResource(resId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addTab(final int position, View tab) {
		tab.setFocusable(true);
		tab.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mOnTabItemClickListen != null){
					mOnTabItemClickListen.onTabitemClick(position);
				}
				pager.setCurrentItem(position);
			}
		});

		//tab.setPadding(tabPadding, 0, tabPadding, 0);
		if(shouldExpand){
			tab.setLayoutParams(defaultTabLayoutParams);
		}else{
			tab.setLayoutParams(expandedTabLayoutParams);
		}
		tabsContainer.addView(tab, position);
	}

	public void updateTabStyles() {
		for (int i = 0; i < tabCount; i++) {
			View v = tabsContainer.getChildAt(i);
			if(shouldExpand){
				v.setLayoutParams(defaultTabLayoutParams);
			}else{
				if(mMinPadding > 0){
					v.setPadding(mMinPadding,0,mMinPadding,0);
				}else {
					v.setLayoutParams(expandedTabLayoutParams);
				}
			}
			setItemStyle(v, i == selectedPosition, i);
		}
	}
	public void updateTabStyles(int padding) {
		shouldExpand = false;
		mMinPadding = padding;
		for (int i = 0; i < tabCount; i++) {
			View v = tabsContainer.getChildAt(i);
			v.setPadding(padding,0,padding,0);
		}
	}

	private void scrollToChild(int position, int offset) {

		if (tabCount == 0) {
			return;
		}

		int newScrollX = tabsContainer.getChildAt(position).getLeft() + offset;

		if (position > 0 || offset > 0) {
			newScrollX -= scrollOffset;
		}

		if (newScrollX != lastScrollX) {
			lastScrollX = newScrollX;
			scrollTo(newScrollX, 0);
		}

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (isInEditMode() || tabCount == 0) {
			return;
		}

		final int height = getHeight();

		// draw indicator line

		rectPaint.setColor(indicatorColor);

		// default: line below current tab
		View currentTab = tabsContainer.getChildAt(currentPosition);
		float lineLeft = currentTab.getLeft();
		float lineRight = currentTab.getRight();

		// if there is an offset, start interpolating left and right coordinates between current and next tab
		if (currentPositionOffset > 0f && currentPosition < tabCount - 1) {

			View nextTab = tabsContainer.getChildAt(currentPosition + 1);
			final float nextTabLeft = nextTab.getLeft();
			final float nextTabRight = nextTab.getRight();

			lineLeft = (currentPositionOffset * nextTabLeft + (1f - currentPositionOffset) * lineLeft);
			lineRight = (currentPositionOffset * nextTabRight + (1f - currentPositionOffset) * lineRight);
		}

		canvas.drawRect(lineLeft+indicatorMarginLeftRight, height - indicatorHeight, lineRight-indicatorMarginLeftRight, height, rectPaint);

		// draw underline

		rectPaint.setColor(underlineColor);
		canvas.drawRect(0, height - underlineHeight, tabsContainer.getWidth(), height, rectPaint);

		// draw divider

		dividerPaint.setColor(dividerColor);
		for (int i = 0; i < tabCount - 1; i++) {
			View tab = tabsContainer.getChildAt(i);
			canvas.drawLine(tab.getRight(), dividerPadding, tab.getRight(), height - dividerPadding, dividerPaint);
		}
	}

	private class PageListener implements OnPageChangeListener {

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			currentPosition = position;
			currentPositionOffset = positionOffset;

			scrollToChild(position, (int) (positionOffset * tabsContainer.getChildAt(position).getWidth()));

			invalidate();

			if (delegatePageListener != null) {
				delegatePageListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
			}
		}

		@Override
		public void onPageScrollStateChanged(int state) {
			if (state == ViewPager.SCROLL_STATE_IDLE) {
				scrollToChild(pager.getCurrentItem(), 0);
			}

			if (delegatePageListener != null) {
				delegatePageListener.onPageScrollStateChanged(state);
			}
		}

		@Override
		public void onPageSelected(int position) {
			selectedPosition = position;
            updateTabStyles();
			if (delegatePageListener != null) {
				delegatePageListener.onPageSelected(position);
			}
		}

	}

	public void addMapUnReadNum(String key, String value){
		if(!TextUtils.isEmpty(key)) {
			if (mapUnReadNum == null) {
				mapUnReadNum = new HashMap<>();
			}
			value = TextUtils.isEmpty(value)?"":value;
			mapUnReadNum.put(key, value);
		}
		updateTabStyles();
	}

	public void setIndicatorColor(int indicatorColor) {
		this.indicatorColor = indicatorColor;
		invalidate();
	}

	public void setIndicatorColorResource(int resId) {
		this.indicatorColor = getResources().getColor(resId);
		invalidate();
	}

	public int getIndicatorColor() {
		return this.indicatorColor;
	}

	public void setIndicatorHeight(int indicatorLineHeightPx) {
		this.indicatorHeight = indicatorLineHeightPx;
		invalidate();
	}

	public int getIndicatorHeight() {
		return indicatorHeight;
	}
	
	public void setIndicatorMarginLeftRight(int indicatorMarginLeftRight) {
		this.indicatorMarginLeftRight = indicatorMarginLeftRight;
		invalidate();
	}
	
	public int getIndicatorMarginLeftRight() {
		return indicatorMarginLeftRight;
	}

	public void setUnderlineColor(int underlineColor) {
		this.underlineColor = underlineColor;
		invalidate();
	}

	public void setUnderlineColorResource(int resId) {
		this.underlineColor = getResources().getColor(resId);
		invalidate();
	}

	public int getUnderlineColor() {
		return underlineColor;
	}

	public void setDividerColor(int dividerColor) {
		this.dividerColor = dividerColor;
		invalidate();
	}

	public void setDividerColorResource(int resId) {
		this.dividerColor = getResources().getColor(resId);
		invalidate();
	}

	public int getDividerColor() {
		return dividerColor;
	}

	public void setUnderlineHeight(int underlineHeightPx) {
		this.underlineHeight = underlineHeightPx;
		invalidate();
	}

	public int getUnderlineHeight() {
		return underlineHeight;
	}

	public void setDividerPadding(int dividerPaddingPx) {
		this.dividerPadding = dividerPaddingPx;
		invalidate();
	}

	public int getDividerPadding() {
		return dividerPadding;
	}

	public void setScrollOffset(int scrollOffsetPx) {
		this.scrollOffset = scrollOffsetPx;
		invalidate();
	}

	public int getScrollOffset() {
		return scrollOffset;
	}

	public void setShouldExpand(boolean shouldExpand) {
		this.shouldExpand = shouldExpand;
		updateTabStyles();
		requestLayout();
	}

	public boolean getShouldExpand() {
		return shouldExpand;
	}

	public boolean isTextAllCaps() {
		return textAllCaps;
	}

	public void setAllCaps(boolean textAllCaps) {
		this.textAllCaps = textAllCaps;
	}

	public void setTextSize(int textSizePx) {
		this.tabTextSize = textSizePx;
		updateTabStyles();
	}

	public int getTextSize() {
		return tabTextSize;
	}

	public void setTextColor(int textColor) {
		this.tabTextColor = textColor;
		updateTabStyles();
	}
	
	public void setSelectedTextColorResource(int resId) {
        this.selectedTabTextColor = getResources().getColor(resId);
        updateTabStyles();
    }

    public int getSelectedTextColor() {
        return selectedTabTextColor;
    }

	public void setTextColorResource(int resId) {
		this.tabTextColor = getResources().getColor(resId);
		updateTabStyles();
	}

	public int getTextColor() {
		return tabTextColor;
	}

	public void setTypeface(Typeface typeface, int style) {
		this.tabTypeface = typeface;
		this.tabTypefaceStyle = style;
		updateTabStyles();
	}


	public void setTabPaddingLeftRight(int paddingPx) {
		this.tabPadding = paddingPx;
		updateTabStyles();
	}

	public int getTabPaddingLeftRight() {
		return tabPadding;
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		SavedState savedState = (SavedState) state;
		super.onRestoreInstanceState(savedState.getSuperState());
		currentPosition = savedState.currentPosition;
		requestLayout();
	}

	@Override
	public Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		SavedState savedState = new SavedState(superState);
		savedState.currentPosition = currentPosition;
		return savedState;
	}

	static class SavedState extends BaseSavedState {
		int currentPosition;

		public SavedState(Parcelable superState) {
			super(superState);
		}

		private SavedState(Parcel in) {
			super(in);
			currentPosition = in.readInt();
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			super.writeToParcel(dest, flags);
			dest.writeInt(currentPosition);
		}

		public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
			@Override
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			@Override
			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}
	
	public void setCurrentItem(int position){
		if(this.pager != null){
			if(this.pager.getCurrentItem() != position){
				this.pager.setCurrentItem(position);
			}else{
				selectedPosition = position;
				updateTabStyles();
			}
			invalidate();
		}
	}
	
	private OnTabItemClickListen mOnTabItemClickListen;
	public void setOnTabItemClickListen(OnTabItemClickListen mOnTabItemClickListen){
		this.mOnTabItemClickListen = mOnTabItemClickListen;
	}
	public interface OnTabItemClickListen{
		void onTabitemClick(int position);
	}

}
