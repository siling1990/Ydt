package com.wbja.stone.ydt;


import com.wbja.stone.ydt.fragment.FragmentContact;
import com.wbja.stone.ydt.fragment.FragmentConversationList;
import com.wbja.stone.ydt.fragment.FragmentIndex;
import com.wbja.stone.ydt.fragment.FragmentMy;
import com.wbja.stone.ydt.fragment.FragmentNote;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

/**
 * @author yangyu
 *	功能描述：自定义TabHost
 */
public class MainTabActivity extends FragmentActivity{	
	//定义FragmentTabHost对象
	private FragmentTabHost mTabHost;
	
	//定义一个布局
	private LayoutInflater layoutInflater;
		
	//定义数组来存放Fragment界面
	private Class fragmentArray[] = {FragmentIndex.class,FragmentConversationList.class,FragmentContact.class,FragmentMy.class};
	
	//定义数组来存放按钮图片
	private int mImageViewArray[] = {R.drawable.tab_doc_btn,R.drawable.tab_service_btn,R.drawable.tab_group_btn,
									 R.drawable.tab_my_btn};
	
	//Tab选项卡的文字
	private String mTextviewArray[] = {"首页","消息","联系人","我"};
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tab_layout);
        initView();
    }
	public void back(View view){
		exit();
	}
	/**
	 * 初始化组件
	 */
	private void initView(){
		//实例化布局对象
		layoutInflater = LayoutInflater.from(this);
				
		//实例化TabHost对象，得到TabHost
		mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);	
		
		//得到fragment的个数
		int count = fragmentArray.length;	
				
		for(int i = 0; i < count; i++){	
			//为每一个Tab按钮设置图标、文字和内容
			TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));
			//将Tab按钮添加进Tab选项卡中
			mTabHost.addTab(tabSpec, fragmentArray[i], null);
			//设置Tab按钮的背景
		//	mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.head_bg);
		}
	}
				
	/**
	 * 给Tab按钮设置图标和文字
	 */
	private View getTabItemView(int index){
		View view = layoutInflater.inflate(R.layout.item_tab_view, null);
	
		ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
		imageView.setImageResource(mImageViewArray[index]);
		
		TextView textView = (TextView) view.findViewById(R.id.textview);		
		textView.setText(mTextviewArray[index]);
	
		return view;
	}
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getAction() != 1) {
			exit();
			return true;
		}

		return super.dispatchKeyEvent(event);
	}
	/*
	 * 退出应用程序
	 */
	private void exit() {
		new AlertDialog.Builder(MainTabActivity.this)
				.setMessage("退出")
				.setPositiveButton("确认",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int whichButton) {
								
								android.os.Process
										.killProcess(android.os.Process.myPid());
								finish();
							}
						}).setNegativeButton("取消", null)
				.show();
	}
	
	public void turn(View v){
		Intent intent=new Intent(this,LoginActivity.class);
		startActivity(intent);
	}
	
}
