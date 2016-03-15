package com.wbja.stone.ydt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wbja.stone.ydt.adapter.DeptAdapter;
import com.wbja.stone.ydt.adapter.HospitalSearchAdapter;
import com.wbja.stone.ydt.entity.DeptInfo;
import com.wbja.stone.ydt.entity.HospitalInfo;
import com.wbja.stone.ydt.util.AlertDialogUtil;
import com.wbja.stone.ydt.util.Constants;
import com.wbja.stone.ydt.util.HttpUtil;
import com.wbja.stone.ydt.util.StringUtil;
import com.wbja.stone.ydt.window.MyProgressDialog;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class RegActivity extends Activity {

	private MyProgressDialog m_customProgrssDialog;
	private EditText eduname;
	private EditText edpwd;
	private EditText code;
	private EditText eddisPlay;
	private Button btLogin, btReg, btCode;
	private MainFrameTask mMainFrameTask;
	private RegCodeTask regCodeTask;
	private SearchHospitalTask searchHospitalTask;
	private DeptListTask deptListTask;
	private String msg;
	private int time = 30;
	private Timer timer;
	
	private Spinner spHos,spDept;
	private List<HospitalInfo> hosList;
	private List<DeptInfo> deptList;
	private HospitalSearchAdapter adapterHos;
	private DeptAdapter adapterDept;
	
	private Map<String, String> map;
	private Gson gson;
	private Handler mH = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			if (msg.what == 1) {
				timer.cancel();
				btCode.setText("获取验证码");
				btCode.setEnabled(true);
			} else {
				time--;
				btCode.setText(time + "s");
				
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reg);

		eduname = (EditText) findViewById(R.id.eduname);
		edpwd = (EditText) findViewById(R.id.edpwd);
		code = (EditText) findViewById(R.id.code);
		eddisPlay = (EditText) findViewById(R.id.eddisPlay);

		btLogin = (Button) findViewById(R.id.btLogin);
		btReg = (Button) findViewById(R.id.btReg);
		btCode = (Button) findViewById(R.id.btCode);
		
		spHos=(Spinner)findViewById(R.id.spHos);
		spDept=(Spinner)findViewById(R.id.spDept);
		
		hosList=new ArrayList<HospitalInfo>();
		adapterHos=new HospitalSearchAdapter(RegActivity.this, hosList);
		spHos.setAdapter(adapterHos);
		
		deptList=new ArrayList<DeptInfo>();
		adapterDept=new DeptAdapter(RegActivity.this, deptList);
		spDept.setAdapter(adapterDept);

		spHos.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int postion, long arg3) {
				// TODO Auto-generated method stub
				deptList.clear();
				searchDept(String.valueOf(hosList.get(postion).getId()));
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		searchHos();
		
		btCode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(eduname.getText().toString().isEmpty()){
					AlertDialogUtil.showAlertDialog(RegActivity.this, "请输入手机号！");
				}else{
					if (regCodeTask != null
							&& regCodeTask.getStatus() == AsyncTask.Status.RUNNING) {
						regCodeTask.cancel(true); // 如果Task还在运行，则先取消它
					}
					StringUtil.saveInfo(RegActivity.this, "username", eduname.getText()
							.toString());
					regCodeTask = new RegCodeTask(RegActivity.this);
					regCodeTask.execute();
					
					time = 30;
					timer = new Timer();// 实例化Timer类
					btCode.setEnabled(false);
					btCode.setText(time+"s");
					timer.schedule(new TimerTask() {
						public void run() {
							if (time > 0) {
								mH.sendEmptyMessage(0);
							}else{
								mH.sendEmptyMessage(1);
							}
						}
					}, 1000,1000);
				}
			}
		});
	}
	public void back(View view){
		this.finish();
	}
	public void reg(View view) {
		if (check()) {
			if (mMainFrameTask != null
					&& mMainFrameTask.getStatus() == AsyncTask.Status.RUNNING) {
				mMainFrameTask.cancel(true); // 如果Task还在运行，则先取消它
			}
			mMainFrameTask = new MainFrameTask(this);
			mMainFrameTask.execute();
		}

	}
	
	private void searchHos(){
		
			if (searchHospitalTask != null
					&& searchHospitalTask.getStatus() == AsyncTask.Status.RUNNING) {
				searchHospitalTask.cancel(true); // 如果Task还在运行，则先取消它
			}

			searchHospitalTask = new SearchHospitalTask(this,"");
			searchHospitalTask.execute();
	}

	private boolean check() {
		if (eduname.getText().toString().isEmpty()) {
			AlertDialogUtil.showAlertDialog(RegActivity.this, "请输入手机号！");
			return false;
		}
		if (code.getText().toString().isEmpty()) {
			AlertDialogUtil.showAlertDialog(RegActivity.this, "请输入验证码！");
			return false;
		}
		if (edpwd.getText().toString().isEmpty()) {
			AlertDialogUtil.showAlertDialog(RegActivity.this, "请输入密码！");
			return false;
		}
		if (eddisPlay.getText().toString().isEmpty()) {
			AlertDialogUtil.showAlertDialog(RegActivity.this, "请输入姓名！");
			return false;
		}
		if (hosList.size()==0) {
			return false;
		}
		if (deptList.size()==0) {
			return false;
		}

		return true;
	}

	/**
	 * 异步
	 * */
	public class RegCodeTask extends AsyncTask<Integer, String, Integer>{

		private Context mainFrame = null;
		public RegCodeTask(Context mainFrame) {
			this.mainFrame = mainFrame;
		}

		@Override
		protected Integer doInBackground(Integer... arg0) {
			// TODO Auto-generated method stub
			map = new HashMap<String, String>();
			map.put("mobile", eduname.getText().toString());
			String result = HttpUtil.doPostForm(map, Constants.REGCODE, false,
					RegActivity.this);
			
			JSONObject myJsonObject = null;
			try {
				myJsonObject = new JSONObject(result);
			
				msg = myJsonObject.getString("r");
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				msg = "验证码发送失败！";
			}
			
			return Constants.FAILURE;
		}
		@Override
		protected void onPostExecute(Integer result) {
			AlertDialogUtil.showAlertDialog(RegActivity.this, msg);
		}
	}
	
	
	
	/**
	 * 异步加载
	 * */
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
			// TODO
			// 封装数据
			HospitalInfo hos=hosList.get(spHos.getSelectedItemPosition());
			DeptInfo dept=deptList.get(spDept.getSelectedItemPosition());
			Map<String, String> map = new HashMap<String, String>();
			map.put("mobile", eduname.getText().toString());
			map.put("mm", edpwd.getText().toString());
			map.put("yzm", code.getText().toString());
			map.put("displayname", eddisPlay.getText().toString());
			map.put("id_hos", String.valueOf(hos.getId()));
			map.put("id_dept",String.valueOf(dept.getId()));
			map.put("name_hos", hos.getName());
			map.put("name_dept", dept.getName());
			map.put("usertype", Constants.USERTYPE);
			String result = HttpUtil.doPostForm(map, Constants.REGE, true,
					RegActivity.this);
			JSONObject myJsonObject = null;
			
			try {
				myJsonObject = new JSONObject(result);
				
				msg = myJsonObject.getString("r");

			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				msg = "注册失败！";
			}
			if (!msg.isEmpty()) {

				
				Log.d("*******注册返回结果*****", msg);
				return Constants.SUCCESS;
			}

			return Constants.FAILURE;
		}

		@Override
		protected void onPreExecute() {
			showCustomProgrssDialog("正在加载...");
		}

		@Override
		protected void onPostExecute(Integer result) {
			hideCustomProgressDialog();
//			if (result == Constants.SUCCESS) {
//				new AlertDialog.Builder(RegActivity.this)
//						.setMessage("注册成功！")
//						.setPositiveButton("确认",
//								new DialogInterface.OnClickListener() {
//
//									public void onClick(DialogInterface dialog,
//											int whichButton) {
//										finish();
//									}
//								}).show();
//			} else {
				AlertDialogUtil.showAlertDialog(RegActivity.this, msg);
//			}

		}

	}
	//医院列表
		public class SearchHospitalTask extends
				AsyncTask<Integer, String, Integer> {
			private Context mainFrame = null;
			private String key;

			public SearchHospitalTask(Context mainFrame, String key) {
				this.mainFrame = mainFrame;
				this.key = key;
			}

			@Override
			protected void onCancelled() {
				hideCustomProgressDialog();
				super.onCancelled();
			}

			@Override
			protected Integer doInBackground(Integer... params) {

				map = new HashMap<String, String>();
				map.put("keyword", key);
				gson = new Gson();
				String result = HttpUtil.doPostForm(map, Constants.SEARCHHOSPITAL,
						false, RegActivity.this);
				if (StringUtil.isEmpty(result)) {
					msg = "连接服务器超时，请确认网络畅通";
					return Constants.FAILURE;
				}
				Log.d("********SearchHospitalTask*******", result);
				JSONObject myJsonObject = null;
				try {
					myJsonObject = new JSONObject(result);
					String r = myJsonObject.getString("r");
					if (r.equals("no")) {
						msg = myJsonObject.getString("msg");
						return Constants.FAILURE;
					}
				} catch (Exception e) {

				}
				try {
					hosList = gson.fromJson(result, new TypeToken<List<HospitalInfo>>() {
					}.getType());
					Log.d("********SearchHospitalTask*******", hosList.size() + "");
					return Constants.SUCCESS;
				} catch (Exception e) {
					msg = e.getMessage();
				}
				return Constants.FAILURE;
			}

			@Override
			protected void onPreExecute() {
				showCustomProgrssDialog("正在加载...");
			}

			@Override
			protected void onPostExecute(Integer result) {
				// TODO
				if (result == Constants.SUCCESS) {
					if (hosList != null && hosList.size() > 0) {
						adapterHos.updateListView(hosList);
						searchDept(String.valueOf(hosList.get(0).getId()));
					}
				} else {
					if (!StringUtil.isEmpty(msg)) {
						AlertDialogUtil.showAlertDialog(RegActivity.this, msg);
					}
				}
				hideCustomProgressDialog();
			}

		}
		 private void searchDept(String keyword){
				if(!StringUtil.isEmpty(keyword)){
					if (deptListTask != null
							&& deptListTask.getStatus() == AsyncTask.Status.RUNNING) {
						deptListTask.cancel(true); // 如果Task还在运行，则先取消它
					}

					deptListTask = new DeptListTask(this,keyword);
					deptListTask.execute();
				}
			}
			
			//查找好友
			public class DeptListTask extends
					AsyncTask<Integer, String, Integer> {
				private Context mainFrame = null;
				private String key;

				public DeptListTask(Context mainFrame, String key) {
					this.mainFrame = mainFrame;
					this.key = key;
				}

				@Override
				protected void onCancelled() {
					hideCustomProgressDialog();
					super.onCancelled();
				}

				@Override
				protected Integer doInBackground(Integer... params) {

					map = new HashMap<String, String>();
					map.put("id_hosp", key);
					gson = new Gson();
					String result = HttpUtil.doPostForm(map, Constants.SEARCHDEPT,
							false, RegActivity.this);
					if (StringUtil.isEmpty(result)) {
						msg = "连接服务器超时，请确认网络畅通";
						return Constants.FAILURE;
					}
					Log.d("********deptListTask*******", result);
					JSONObject myJsonObject = null;
					try {
						myJsonObject = new JSONObject(result);
						String r = myJsonObject.getString("r");
						if (r.equals("no")) {
							msg = myJsonObject.getString("msg");
							return Constants.FAILURE;
						}
					} catch (Exception e) {

					}
					try {
						deptList = gson.fromJson(result, new TypeToken<List<DeptInfo>>() {
						}.getType());
						Log.d("********deptListTask*******", deptList.size() + "");
						return Constants.SUCCESS;
					} catch (Exception e) {
						msg = e.getMessage();
					}
					return Constants.FAILURE;
				}

				@Override
				protected void onPreExecute() {
					showCustomProgrssDialog("正在加载...");
				}

				@Override
				protected void onPostExecute(Integer result) {
					// TODO
					if (result == Constants.SUCCESS) {
						if (deptList != null && deptList.size() > 0) {
							adapterDept.updateListView(deptList);
						}
					} else {
						if (!StringUtil.isEmpty(msg)) {
							AlertDialogUtil.showAlertDialog(RegActivity.this, msg);
						}
					}
					hideCustomProgressDialog();
				}

			}
	
	
	void showCustomProgrssDialog(String msg) {
		if (null == m_customProgrssDialog)
			m_customProgrssDialog = MyProgressDialog
					.createProgrssDialog(RegActivity.this);
		if (null != m_customProgrssDialog) {
			m_customProgrssDialog.setMessage(msg);
			m_customProgrssDialog.show();
			m_customProgrssDialog.setCancelable(true);
		}
	}

	void hideCustomProgressDialog() {
		if (null != m_customProgrssDialog) {
			m_customProgrssDialog.dismiss();
			m_customProgrssDialog = null;
		}
	}
}
