package com.wbja.stone.ydt;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;


import com.viewpagerindicator.TabPageIndicator;
import com.wbja.stone.ydt.adapter.TabStateAdapter;
import com.wbja.stone.ydt.fragment.FragmentColumChart;
import com.wbja.stone.ydt.fragment.FragmentLineColumChart;


public class SingleAnalysisActivity extends FragmentActivity {

	private TextView headTitle;

	private ViewPager pager;
	private FragmentStatePagerAdapter adapter;

	private String[] title = { "������ҩ�����ԶԱ�", "ʵ��ҩ�������ҩ��" };// ,"ҩ��","����","�鷽����"};
	private TabPageIndicator indicator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_analysis);

		headTitle = (TextView) findViewById(R.id.headTitle);
		headTitle.setText("����ҩ������");

		Fragment[] fArray = { new FragmentLineColumChart(), new FragmentColumChart() };
		// ,new FragmentLibMed(), new FragmentLibMed(), new FragmentLibMed() };

		adapter = new TabStateAdapter(getSupportFragmentManager(), title, null, fArray);

		// ��ͼ�л���
		pager = (ViewPager) findViewById(R.id.pager);
		pager.setOffscreenPageLimit(1);
		pager.setAdapter(adapter);

		// ҳ��ָʾ��
		indicator = (TabPageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(pager);
	}

	public void back(View view) {
		this.finish();
	}
}