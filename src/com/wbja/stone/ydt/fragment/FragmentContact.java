package com.wbja.stone.ydt.fragment;


import io.rong.imkit.RongIM;

import java.util.ArrayList;
import java.util.Collections;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

import java.util.List;

import com.wbja.stone.ydt.FriendSearchActivity;
import com.wbja.stone.ydt.R;
import com.wbja.stone.ydt.adapter.FriendSortAdapter;
import com.wbja.stone.ydt.entity.Friend;
import com.wbja.stone.ydt.util.CharacterParser;
import com.wbja.stone.ydt.util.Constants;
import com.wbja.stone.ydt.util.DB;
import com.wbja.stone.ydt.util.FriendPinyinComparator;
import com.wbja.stone.ydt.util.StringUtil;
import com.wbja.stone.ydt.window.ClearEditText;
import com.wbja.stone.ydt.window.SideBar;
import com.wbja.stone.ydt.window.SideBar.OnTouchingLetterChangedListener;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class FragmentContact extends Fragment{
	private TextView headTitle;
	private ImageView headMore;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private FriendSortAdapter adapter;
	private ClearEditText mClearEditText;
	private Intent intent;
	private DB db;
	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	private List<Friend> SourceDateList;
	
	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private FriendPinyinComparator pinyinComparator;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.fragment_contact, null);		
	}	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		headTitle=(TextView) getView().findViewById(R.id.headTitle);
		headMore=(ImageView) getView().findViewById(R.id.headMore);
		headTitle.setText("联系人");
		headMore.setVisibility(View.VISIBLE);
		headMore.setImageResource(R.drawable.top_add_icon);
		db=new DB(getActivity());
		headMore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				intent=new Intent(getActivity(), FriendSearchActivity.class);
				getActivity().startActivity(intent);
			}
		});
	
		initViews();
	}

	private void initViews() {
		//实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();
		
		pinyinComparator = new FriendPinyinComparator();
		
		sideBar = (SideBar) getView().findViewById(R.id.sidrbar);
		dialog = (TextView) getView().findViewById(R.id.dialog);
		sideBar.setTextView(dialog);
		
		//设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
			
			@Override
			public void onTouchingLetterChanged(String s) {
				//该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if(position != -1){
					sortListView.setSelection(position);
				}
				
			}
		});
		
		sortListView = (ListView) getView().findViewById(R.id.country_lvcountry);
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//这里要利用adapter.getItem(position)来获取当前position所对应的对象
//				if(((Friend)adapter.getItem(position)).getName().equals("管理员")){
				Friend fri=(Friend)adapter.getItem(position);
				if(StringUtil.getInfo(getActivity(), Constants.USERID,"0").equals(fri.getId_user())){
					RongIM.getInstance().refreshUserInfoCache(new UserInfo( fri.getId_friend(), fri.getDisplayname_friend(),null));
					RongIM.getInstance().startConversation(getActivity(), Conversation.ConversationType.PRIVATE, fri.getId_friend(), fri.getDisplayname_friend());
				}
				else{
					RongIM.getInstance().refreshUserInfoCache(new UserInfo( fri.getId_user(), fri.getDisplayname_user(),null));
					RongIM.getInstance().startConversation(getActivity(), Conversation.ConversationType.PRIVATE, fri.getId_user(), fri.getDisplayname_user());
				}
					
			}
		});
		
		SourceDateList = filledData(db.queryFriend(StringUtil.getID_User(getActivity())));
		
		// 根据a-z进行排序源数据
		Collections.sort(SourceDateList, pinyinComparator);
		adapter = new FriendSortAdapter(getActivity(), SourceDateList);
		sortListView.setAdapter(adapter);
		
		
		mClearEditText = (ClearEditText) getView().findViewById(R.id.filter_edit);
		
		//根据输入框输入值的改变来过滤搜索
		mClearEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				filterData(s.toString());
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}


	/**
	 * 为ListView填充数据
	 * @param date
	 * @return
	 */
	private List<Friend> filledData(List<Friend> dbList){
		
		for(int i=0; i<dbList.size(); i++){
			//汉字转换成拼音
			String pinyin ;//= characterParser.getSelling(dbList.get(i).getDisplayname_friend());
			if(StringUtil.getInfo(getActivity(), Constants.USERID,"0").equals(dbList.get(i).getId_user())){
			pinyin = characterParser.getSelling(dbList.get(i).getDisplayname_friend());
			}
			else{
				 pinyin= characterParser.getSelling(dbList.get(i).getDisplayname_user());
			}
			String sortString = pinyin.substring(0, 1).toUpperCase();
			
			// 正则表达式，判断首字母是否是英文字母
			if(sortString.matches("[A-Z]")){
				dbList.get(i).setSortLetters(sortString.toUpperCase());
			}else{
				dbList.get(i).setSortLetters("#");
			}
		}
		
		return dbList;
		
	}
	
	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * @param filterStr
	 */
	private void filterData(String filterStr){
		List<Friend> filterDateList = new ArrayList<Friend>();
		
		if(TextUtils.isEmpty(filterStr)){
			filterDateList = SourceDateList;
		}else{
			filterDateList.clear();
			for(Friend sortModel : SourceDateList){
				String name = sortModel.getDisplayname_friend();
				if(name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())){
					filterDateList.add(sortModel);
				}
			}
		}
		
		// 根据a-z进行排序
		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
	initViews();
		super.onResume();
	}
	
}
