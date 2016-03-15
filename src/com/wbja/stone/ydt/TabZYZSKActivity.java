package com.wbja.stone.ydt;


import com.viewpagerindicator.TabPageIndicator;
import com.wbja.stone.ydt.adapter.TabStateAdapter;
import com.wbja.stone.ydt.fragment.FragmentCRM;
import com.wbja.stone.ydt.fragment.FragmentClassicRote;
import com.wbja.stone.ydt.util.StringUtil;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;

public class TabZYZSKActivity extends FragmentActivity {
	private ViewPager pager;
	private FragmentStatePagerAdapter adapter;


	private String[] title = { "临床路径","医案"};//,"药库","方库","组方分析"};
	private TabPageIndicator indicator ;
	
	private String mrcStr;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab_zyzsk);
		mrcStr = StringUtil.getInfo(TabZYZSKActivity.this, "record", "");
		Log.d("******resordJSON*****", mrcStr);
		// ydt.getMrc() = GsonUtil.getObject(mrcStr, MRClinic.class);
	
		Fragment[] fArray = { new FragmentClassicRote(), new FragmentCRM()};
				//,new FragmentLibMed(), new FragmentLibMed(), new FragmentLibMed() };
		
		adapter = new TabStateAdapter(getSupportFragmentManager(), title, null,
				fArray);

		// 视图切换器
		pager = (ViewPager) findViewById(R.id.pager);
		pager.setOffscreenPageLimit(1);
		pager.setAdapter(adapter);

		// 页面指示器
		indicator = (TabPageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(pager);
	}

	public void back(View view) {
		this.finish();
	}

}
