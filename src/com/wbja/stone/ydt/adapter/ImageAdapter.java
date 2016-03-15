package com.wbja.stone.ydt.adapter;

import java.util.List;


import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public class ImageAdapter extends PagerAdapter{
	private List<View> guidePages;
	
	public ImageAdapter(List<View> guidePages)
	{
		this.guidePages = guidePages;
	}
	

	@Override
	public int getCount() {
		return this.guidePages.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object obj) {
		return view == obj;
	}
	
	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager) container).removeView(guidePages.get(position));
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		((ViewPager) container).addView(guidePages.get(position), 0);
		return guidePages.get(position);
	}

}  