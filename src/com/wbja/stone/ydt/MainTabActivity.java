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
 *	�����������Զ���TabHost
 */
public class MainTabActivity extends FragmentActivity{	
	//����FragmentTabHost����
	private FragmentTabHost mTabHost;
	
	//����һ������
	private LayoutInflater layoutInflater;
		
	//�������������Fragment����
	private Class fragmentArray[] = {FragmentIndex.class,FragmentConversationList.class,FragmentContact.class,FragmentMy.class};
	
	//������������Ű�ťͼƬ
	private int mImageViewArray[] = {R.drawable.tab_doc_btn,R.drawable.tab_service_btn,R.drawable.tab_group_btn,
									 R.drawable.tab_my_btn};
	
	//Tabѡ�������
	private String mTextviewArray[] = {"��ҳ","��Ϣ","��ϵ��","��"};
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tab_layout);
        initView();
    }
	public void back(View view){
		exit();
	}
	/**
	 * ��ʼ�����
	 */
	private void initView(){
		//ʵ�������ֶ���
		layoutInflater = LayoutInflater.from(this);
				
		//ʵ����TabHost���󣬵õ�TabHost
		mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);	
		
		//�õ�fragment�ĸ���
		int count = fragmentArray.length;	
				
		for(int i = 0; i < count; i++){	
			//Ϊÿһ��Tab��ť����ͼ�ꡢ���ֺ�����
			TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));
			//��Tab��ť��ӽ�Tabѡ���
			mTabHost.addTab(tabSpec, fragmentArray[i], null);
			//����Tab��ť�ı���
		//	mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.head_bg);
		}
	}
				
	/**
	 * ��Tab��ť����ͼ�������
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
	 * �˳�Ӧ�ó���
	 */
	private void exit() {
		new AlertDialog.Builder(MainTabActivity.this)
				.setMessage("�˳�")
				.setPositiveButton("ȷ��",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int whichButton) {
								
								android.os.Process
										.killProcess(android.os.Process.myPid());
								finish();
							}
						}).setNegativeButton("ȡ��", null)
				.show();
	}
	
	public void turn(View v){
		Intent intent=new Intent(this,LoginActivity.class);
		startActivity(intent);
	}
	
}
