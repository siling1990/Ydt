package com.wbja.stone.ydt.adapter;



import com.viewpagerindicator.IconPagerAdapter;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * FragmentPager适配�?
 * 
 * @author wwj_748
 * @2014/8/9
 */
@SuppressLint("DefaultLocale")
public class TabStateAdapter extends FragmentStatePagerAdapter implements IconPagerAdapter{

	private String title[];
	private int imageArray[];
	private Fragment fArrray[];

	public TabStateAdapter(FragmentManager fm,String[] title,int imageArray[],Fragment[] fArray) {
		super(fm);
		this.title=title;
		this.imageArray=imageArray;
		this.fArrray=fArray;
	}
	public TabStateAdapter(FragmentManager fm,String[] title,Fragment[] fArray) {
		super(fm);
		this.title=title;
		this.fArrray=fArray;
	}
	// 获取�?
	@Override
	public Fragment getItem(int position) {
		System.out.println("Fragment position:" + position);
		
		return fArrray[position];
	}

	@Override
	public CharSequence getPageTitle(int position) {
		// 返回页面标题
		return title[position % title.length].toUpperCase();
	}
	@Override
	public int getCount() {
		// 页面个数
		return title.length;
	}
	@Override
	public int getIconResId(int position) {
		if(null!=imageArray){
			return imageArray[position];
		}
		return 0;
	}

}
