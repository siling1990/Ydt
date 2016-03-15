package com.wbja.stone.ydt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wbja.stone.ydt.adapter.PatientSortAdapter;
import com.wbja.stone.ydt.adapter.PatientUpSortAdapter;
import com.wbja.stone.ydt.entity.Patient;
import com.wbja.stone.ydt.entity.PatientUpload;
import com.wbja.stone.ydt.util.AlertDialogUtil;
import com.wbja.stone.ydt.util.CharacterParser;
import com.wbja.stone.ydt.util.Constants;
import com.wbja.stone.ydt.util.DB;
import com.wbja.stone.ydt.util.DateUtil;
import com.wbja.stone.ydt.util.GsonUtil;
import com.wbja.stone.ydt.util.HttpUtil;
import com.wbja.stone.ydt.util.PatientPinyinComparator;
import com.wbja.stone.ydt.util.StringUtil;
import com.wbja.stone.ydt.window.ClearEditText;
import com.wbja.stone.ydt.window.MyProgressDialog;
import com.wbja.stone.ydt.window.SideBar;
import com.wbja.stone.ydt.window.SideBar.OnTouchingLetterChangedListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class PatientActivity extends Activity {
	private MyProgressDialog m_customProgrssDialog;
	private ImageView headMore;
	private MainFrameTask mMainFrameTask;
	private UpdatePatientTask updatePatientTask;
	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private PatientSortAdapter adapter;
	private ClearEditText mClearEditText;
	private TextView headTitle;
	private ImageView back;
	private String msg;
	private Patient p;
	private Map<String,String>map;

	private DB db; // 数据库引用

	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	private List<Patient> sourceDateList;
	private int isSet;
	private Intent intent;
	private List<Patient>pList;
	private Gson gson;

	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PatientPinyinComparator pinyinComparator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patient);

		intent = getIntent();
		isSet = intent.getIntExtra("isSetPatient", -1);

		headMore = (ImageView) findViewById(R.id.headMore);
		headMore.setImageResource(R.drawable.top_add_icon);
		headMore.setVisibility(View.VISIBLE);

		headTitle = (TextView) findViewById(R.id.headTitle);
		headTitle.setText("我的患者");

		back = (ImageView) findViewById(R.id.headBack);
		back.setVisibility(View.VISIBLE);
		db = new DB(this);
		// db.delete();

		headMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(PatientActivity.this,
						AddPatientActivity.class);
				startActivity(intent);
			}
		});

		initViews();
	}

	public void back(View view) {
		this.finish();
	}

	void showCustomProgrssDialog(String msg) {

		if (null == m_customProgrssDialog)

			m_customProgrssDialog = MyProgressDialog

			.createProgrssDialog(PatientActivity.this);

		if (null != m_customProgrssDialog) {

			m_customProgrssDialog.setMessage(msg);

			m_customProgrssDialog.show();

			m_customProgrssDialog.setCancelable(false);

		}

	}

	void hideCustomProgressDialog() {

		if (null != m_customProgrssDialog) {

			m_customProgrssDialog.dismiss();

			m_customProgrssDialog = null;

		}

	}

	public class MainFrameTask extends AsyncTask<Integer, String, Integer> {
		private Context mainFrame = null;

		public MainFrameTask(Context mainFrame) {
			this.mainFrame = mainFrame;
		}

		@Override
		protected void onCancelled() {
			hideCustomProgressDialog();
			super.onCancelled();
		}

		@Override
		protected Integer doInBackground(Integer... params) {

			try {
				Thread.sleep(500);

			} catch (Exception e) {

			}

			sourceDateList = filledData();
			return null;
		}

		@Override
		protected void onPreExecute() {
			showCustomProgrssDialog("正在加载...");
		}

		@Override
		protected void onPostExecute(Integer result) {
			// 根据a-z进行排序源数据
			Collections.sort(sourceDateList, pinyinComparator);
			adapter = new PatientSortAdapter(PatientActivity.this,
					sourceDateList);
			sortListView.setAdapter(adapter);

			mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);

			// 根据输入框输入值的改变来过滤搜索
			mClearEditText.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					// 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
					filterData(s.toString());
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {

				}

				@Override
				public void afterTextChanged(Editable s) {
				}
			});
			hideCustomProgressDialog();
		}

	}

	private void initViews() {
		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();

		pinyinComparator = new PatientPinyinComparator();

		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);

		// 设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					sortListView.setSelection(position);
				}

			}
		});

		sortListView = (ListView) findViewById(R.id.country_lvcountry);
		if (isSet == 1) {
			sortListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					//
					p = (Patient) adapter.getItem(position);
						intent = new Intent();
						// 返回pNO，数据库中patient的id
						intent.putExtra("pNo", p.getId_user());
						setResult(RESULT_OK, intent);
						finish();

				}
			});
		} else {

			// 查看患者信息
			sortListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					//拨打电话
				}
			});

			sortListView
					.setOnItemLongClickListener(new OnItemLongClickListener() {

						@Override
						public boolean onItemLongClick(AdapterView<?> parent,
								View view, final int position, long id) {
							// TODO Auto-generated method stub
							new AlertDialog.Builder(PatientActivity.this)
									.setMessage("删除")
									.setPositiveButton(
											"确认",
											new DialogInterface.OnClickListener() {

												public void onClick(
														DialogInterface dialog,
														int whichButton) {
													// delete from db
													db.deletePatient(sourceDateList
															.get(position)
															.getId_local());
													// delete from ui
													sourceDateList
															.remove(position);
													// update ui
													adapter.updateListView(sourceDateList);
												}
											}).setNegativeButton("取消", null)
									.show();

							return true;
						}
					});
		}

		if (mMainFrameTask != null
				&& mMainFrameTask.getStatus() == AsyncTask.Status.RUNNING) {
			mMainFrameTask.cancel(true); // 如果Task还在运行，则先取消它
		}

		mMainFrameTask = new MainFrameTask(this);
		mMainFrameTask.execute();

	}

	/**
	 * 为ListView填充数据
	 * 
	 * @param date
	 * @return
	 */
	private List<Patient> filledData() {
		List<Patient> mSortList = new ArrayList<Patient>();
		List<Patient> data = db.queryPatient(StringUtil.getID_User(PatientActivity.this));
		for (int i = 0; i < data.size(); i++) {
			Patient sortModel = data.get(i);
			// 汉字转换成拼音
			try {
				String pinyin = characterParser.getSelling(data.get(i)
						.getName_doctor());
				String sortString = pinyin.substring(0, 1).toUpperCase();

				// 正则表达式，判断首字母是否是英文字母
				if (sortString.matches("[A-Z]")) {
					sortModel.setSortLetters(sortString.toUpperCase());
				} else {
					sortModel.setSortLetters("#");
				}
			} catch (Exception e) {
				Log.d("FILLEDDATA:", e.getMessage());
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
		List<Patient> filterDateList = new ArrayList<Patient>();

		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = sourceDateList;
		} else {
			filterDateList.clear();
			for (Patient sortModel : sourceDateList) {
				String name = sortModel.getName_doctor();
				if (name.indexOf(filterStr.toString()) != -1
						|| characterParser.getSelling(name).startsWith(
								filterStr.toString())) {
					filterDateList.add(sortModel);
				}
			}
		}

		// 根据a-z进行排序
		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		initViews();
		super.onRestart();
	}

	/*
	 * 同步数据 *
	 */
	public void updatePatient(View v) {
		if (!StringUtil.isEmpty(StringUtil.getInfo(
				PatientActivity.this, "token", ""))) {
			if (updatePatientTask != null
					&& updatePatientTask.getStatus() == AsyncTask.Status.RUNNING) {
				updatePatientTask.cancel(true); // 如果Task还在运行，则先取消它
			}
			updatePatientTask = new UpdatePatientTask(this);
			updatePatientTask.execute();
		}else{
			new AlertDialog.Builder(PatientActivity.this)
			.setMessage("请先登录！")
			.setPositiveButton("确认",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog,
								int whichButton) {
						intent =new Intent(PatientActivity.this, LoginActivity.class);
						startActivity(intent);
						}
					}).setNegativeButton("取消", null)
			.show();
		}

		
	}

	public class UpdatePatientTask extends AsyncTask<Integer, String, Integer> {
		private Context mainFrame = null;

		public UpdatePatientTask(Context mainFrame) {
			this.mainFrame = mainFrame;
		}

		@Override
		protected void onCancelled() {
			hideCustomProgressDialog();
			super.onCancelled();
		}

		@Override
		protected Integer doInBackground(Integer... params) {
			// TODO
			map=new HashMap<String, String>();
			gson=new Gson();
			String result=HttpUtil.doPostForm(map, Constants.GETPATIENTLIST, false, PatientActivity.this);
			if(StringUtil.isEmpty(result)){
				msg="连接服务器超时，请确认网络畅通";
				return Constants.FAILURE;
			}
			Log.d("********patientList*******", result);
			JSONObject myJsonObject = null;
			
			try {
//				myJsonObject = new JSONObject(result);
//				String r=myJsonObject.getString("r");
//				if(r.equals("no")){
//					msg=myJsonObject.getString("msg");
//					return Constants.FAILURE;
//				}
				pList= gson.fromJson(result,new TypeToken<List<Patient>>() {}.getType());
				Log.d("********patientList*******", pList.size()+"");
				return Constants.SUCCESS;
			} catch (Exception e) {
				msg=e.getMessage();
			}
			return Constants.FAILURE;
		}

		@Override
		protected void onPreExecute() {
			showCustomProgrssDialog("正在加载...");
		}

		@Override
		protected void onPostExecute(Integer result) {
			//TODO 
			if(result==Constants.SUCCESS){
				//updateToDB(pList);
				Patient pp;
				db.deletePatient();
				for(int i=0;i<pList.size();i++){
					pp=pList.get(i);
					db.insertPatient(pp);
				}
				initViews();
				AlertDialogUtil.showAlertDialog(PatientActivity.this, "同步完成!");
			}else{
				if(!StringUtil.isEmpty(msg)){
					AlertDialogUtil.showAlertDialog(PatientActivity.this, msg);
				}
			}
			hideCustomProgressDialog();
		}

	}
	private void updateToDB(List<PatientUpload> list){
		if(sourceDateList!=null&&sourceDateList.size()>0){
			for(int i=0;i<list.size();i++){
				PatientUpload up1=list.get(i);
				int j=0;
				for(;j<sourceDateList.size();j++){
					if(up1.getId()==sourceDateList.get(j).getId())
						break;//找到之后返回
				}
				if(j>=sourceDateList.size()){//数据全部遍历之后没有,存入数据库
					up1.setIsUpdate(1);
					up1.setDate_birth(DateUtil.dateToString(DateUtil.jsonDateToDate(up1.getDate_birth()),"yyyy-MM-dd"));
					db.insertPatientUp(up1);
				}
			}
		}
		if(sourceDateList==null||sourceDateList.size()==0){
			for(int i=0;i<list.size();i++){
				PatientUpload pp=list.get(i);
				pp.setIsUpdate(1);
				pp.setDate_birth(DateUtil.dateToString(DateUtil.jsonDateToDate(pp.getDate_birth()),"yyyy-MM-dd"));
				db.insertPatientUp(pp);
			}
		}
	}
}
