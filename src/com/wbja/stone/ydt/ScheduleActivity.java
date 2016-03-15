package com.wbja.stone.ydt;

import com.viewpagerindicator.TabPageIndicator;
import com.wbja.stone.ydt.adapter.TabAdapter;
import com.wbja.stone.ydt.adapter.TabStateAdapter;
import com.wbja.stone.ydt.fragment.FragmentBw;
import com.wbja.stone.ydt.fragment.FragmentRc;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
* @ClassName: ScheduleActivity
* @Description: TODO(������һ�仰��������������)
* @author Stone
* @date 2015-7-1 ����10:24:31
*
*/ 
public class ScheduleActivity extends FragmentActivity {

	private TextView headTitle;
	private ImageView back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule);
		
		headTitle=(TextView) findViewById(R.id.headTitle);
		headTitle.setText("�ճ�����");
		back = (ImageView) findViewById(R.id.headBack);
		back.setVisibility(View.VISIBLE);
		
		  String[] title={"�ճ̰���","����¼"};
	    	Fragment[] fArray={new FragmentRc(),new FragmentBw()};
			FragmentStatePagerAdapter adapter = new TabStateAdapter(
					getSupportFragmentManager(),title,fArray);
			// ��ͼ�л���
			ViewPager pager = (ViewPager) findViewById(R.id.pager);
			pager.setOffscreenPageLimit(1);
			pager.setAdapter(adapter);
			// ҳ��ָʾ��
			TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
			indicator.setTabIconLocation (TabPageIndicator.LOCATION_UP);
			indicator.setViewPager(pager);
		}
	public void back(View view) {
		this.finish();
	}
}
