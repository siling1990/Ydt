package com.wbja.stone.ydt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.wbja.stone.ydt.adapter.SortAdapter;
import com.wbja.stone.ydt.entity.SortModel;
import com.wbja.stone.ydt.util.CharacterParser;
import com.wbja.stone.ydt.util.PinyinComparator;
import com.wbja.stone.ydt.window.SideBar;
import com.wbja.stone.ydt.window.SideBar.OnTouchingLetterChangedListener;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ShowZHActivity extends Activity {
	private ListView lvkebie, lvbing;
	private SideBar sidrbarkebie, sidrbarbing;
	private TextView dialogkebie, dialogbing;
	private SortAdapter adapterK;
	private SortAdapter adapterB;
	private Button btCancle,btOk;
	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	private List<SortModel> kebieList;
	private List<SortModel> bingList;

	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_zh);
		initViews();
	}
	public void back(View view){
		this.finish();
	}
	private void initViews() {
		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();

		pinyinComparator = new PinyinComparator();

		sidrbarkebie = (SideBar) findViewById(R.id.sidrbarkebie);
		sidrbarbing = (SideBar) findViewById(R.id.sidrbarbing);
		dialogbing = (TextView) findViewById(R.id.dialogbing);
		dialogkebie = (TextView) findViewById(R.id.dialogkebie);

		btCancle = (Button) findViewById(R.id.btCancle);
		btOk = (Button) findViewById(R.id.btOk);
		
		sidrbarkebie.setTextView(dialogkebie);
		sidrbarbing.setTextView(dialogbing);

		btCancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		// 设置右侧触摸监听
		sidrbarkebie
				.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

					@Override
					public void onTouchingLetterChanged(String s) {
						// 该字母首次出现的位置
						int position = adapterK.getPositionForSection(s
								.charAt(0));
						if (position != -1) {
							lvkebie.setSelection(position);
						}

					}
				});

		sidrbarbing
				.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

					@Override
					public void onTouchingLetterChanged(String s) {
						// 该字母首次出现的位置
						if(adapterB!=null){
							int position = adapterB.getPositionForSection(s
									.charAt(0));
							if (position != -1) {
								lvbing.setSelection(position);
							}
						}

					}
				});

		lvkebie = (ListView) findViewById(R.id.lvkebie);
		lvbing = (ListView) findViewById(R.id.lvbing);

		lvkebie.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 这里要利用adapter.getItem(position)来获取当前position所对应的对象
				// Toast.makeText(getApplication(),
				// ((SortModel)adapter.getItem(position)).getName(),
				// Toast.LENGTH_SHORT).show();
//				if(position==1){
//					bingList = filledDataB(getResources().getStringArray(
//							R.array.zz));
//				}else{
//					bingList = filledDataB(getResources().getStringArray(
//							R.array.bsz));
//				}
				
				Collections.sort(bingList, pinyinComparator);
				adapterB = new SortAdapter(ShowZHActivity.this, bingList);
				lvbing.setAdapter(adapterB);
			}
		});

		kebieList = filledDataKe(getResources().getStringArray(R.array.kebie));
		// kebieList = filledData(getResources().getStringArray(R.array.kebie));

		// 根据a-z进行排序源数据
		Collections.sort(kebieList, pinyinComparator);
		adapterK = new SortAdapter(ShowZHActivity.this, kebieList);
		lvkebie.setAdapter(adapterK);

		// mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);
		//
		// //根据输入框输入值的改变来过滤搜索
		// mClearEditText.addTextChangedListener(new TextWatcher() {
		//
		// @Override
		// public void onTextChanged(CharSequence s, int start, int before, int
		// count) {
		// //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
		// filterData(s.toString());
		// }
		//
		// @Override
		// public void beforeTextChanged(CharSequence s, int start, int count,
		// int after) {
		//
		// }
		//
		// @Override
		// public void afterTextChanged(Editable s) {
		// }
		// });
	}

	/**
	 * 为ListView填充数据
	 * 
	 * @param date
	 * @return
	 */
	private List<SortModel> filledDataKe(String[] data) {
		List<SortModel> mSortList = new ArrayList<SortModel>();

		for (int i = 0; i < data.length; i++) {
			SortModel sortModel = new SortModel();
			sortModel.setName(data[i]);
			// 汉字转换成拼音
			String pinyin = characterParser.getSelling(data[i]);
			String sortString = pinyin.substring(0, 1).toUpperCase();

			// 正则表达式，判断首字母是否是英文字母
			if (sortString.matches("[A-Z]")) {
				sortModel.setSortLetters(sortString.toUpperCase());
			} else {
				sortModel.setSortLetters("#");
			}

			mSortList.add(sortModel);
		}
		return mSortList;

	}

	private List<SortModel> filledDataB(String[] data) {
		List<SortModel> mSortList = new ArrayList<SortModel>();

		for (int i = 0; i < data.length; i++) {
			SortModel sortModel = new SortModel();
			sortModel.setName(data[i]);
			// 汉字转换成拼音
			String pinyin = characterParser.getSelling(data[i]);
			String sortString = pinyin.substring(0, 1).toUpperCase();

			// 正则表达式，判断首字母是否是英文字母
			if (sortString.matches("[A-Z]")) {
				sortModel.setSortLetters(sortString.toUpperCase());
			} else {
				sortModel.setSortLetters("#");
			}

			mSortList.add(sortModel);
		}
		return mSortList;

	}

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * 
	 * @param filterStr
	 */
	private void filterData(String filterStr) {
		List<SortModel> filterDateList = new ArrayList<SortModel>();

		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = kebieList;
		} else {
			filterDateList.clear();
			for (SortModel sortModel : kebieList) {
				String name = sortModel.getName();
				if (name.indexOf(filterStr.toString()) != -1
						|| characterParser.getSelling(name).startsWith(
								filterStr.toString())) {
					filterDateList.add(sortModel);
				}
			}
		}

		// 根据a-z进行排序
		Collections.sort(filterDateList, pinyinComparator);
		adapterK.updateListView(filterDateList);
	}
}
