package com.wbja.stone.ydt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wbja.stone.ydt.adapter.RegAdapter;
import com.wbja.stone.ydt.entity.Registration;
import com.wbja.stone.ydt.util.AlertDialogUtil;
import com.wbja.stone.ydt.util.Constants;
import com.wbja.stone.ydt.util.HttpUtil;
import com.wbja.stone.ydt.util.StringUtil;
import com.wbja.stone.ydt.window.MyProgressDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class RegListActivity extends Activity {

	private TextView headTitle;
	private ListView lvReg;
	private TextView dialog;
	private List<Registration> regList;
	private MyProgressDialog m_customProgrssDialog;
	private RegListTask regListTask;
	private MngTask mngTask;
	private Gson gson;
	private Map<String, String> map;
	private String msg;
	private RegAdapter adapter;
	private Registration reg;
	public void back(View view){
		this.finish();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reg_list);

		lvReg = (ListView) findViewById(R.id.lvReg);
		headTitle = (TextView) findViewById(R.id.headTitle);
		dialog=(TextView)findViewById(R.id.dialog);
		headTitle.setText("我的预约");

		regList = new ArrayList<Registration>();
		adapter = new RegAdapter(RegListActivity.this, regList);
		lvReg.setAdapter(adapter);

		lvReg.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				reg = (Registration) adapter.getItem(position);
				new AlertDialog.Builder(RegListActivity.this)
				.setMessage("是否审核通过？")
				.setPositiveButton("通过",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int whichButton) {
								mng(true,reg.getId());
							}
						}).setNeutralButton("拒绝", new DialogInterface.OnClickListener(){

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								mng(false,reg.getId());
							}}).setNegativeButton("取消", new DialogInterface.OnClickListener(){

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								
							}}).show();
				

			}
		});
		search();

	}

	private void search() {
			if (regListTask != null
					&& regListTask.getStatus() == AsyncTask.Status.RUNNING) {
				regListTask.cancel(true); // 如果Task还在运行，则先取消它
			}

			regListTask = new RegListTask(this);
			regListTask.execute();
	}
	
	private void mng(boolean isPass,int id) {
		if (mngTask != null
				&& mngTask.getStatus() == AsyncTask.Status.RUNNING) {
			mngTask.cancel(true); // 如果Task还在运行，则先取消它
		}

		mngTask = new MngTask(this,isPass,id);
		mngTask.execute();
}


	// 
	public class RegListTask extends AsyncTask<Integer, String, Integer> {
		private Context mainFrame = null;

		public RegListTask(Context mainFrame) {
			this.mainFrame = mainFrame;
		}

		@Override
		protected void onCancelled() {
			hideCustomProgressDialog();
			super.onCancelled();
		}

		@Override
		protected Integer doInBackground(Integer... params) {

			map = new HashMap<String, String>();
			gson = new Gson();
			String result = HttpUtil.doPostForm(map, Constants.REGLIST,
					false, RegListActivity.this);
			if (StringUtil.isEmpty(result)) {
				msg = "连接服务器超时，请确认网络畅通";
				return Constants.FAILURE;
			}
			Log.d("********DocTimeTask*******", result);
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
				regList = gson.fromJson(result,
						new TypeToken<List<Registration>>() {
						}.getType());
				Log.d("********DocTimeTask*******", regList.size() + "");
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
				if (regList != null && regList.size() > 0) {
					adapter.updateListView(regList);
					dialog.setVisibility(View.GONE);
				}else{
					dialog.setVisibility(View.VISIBLE);
				}
			} else {
				if (!StringUtil.isEmpty(msg)) {
					AlertDialogUtil.showAlertDialog(RegListActivity.this,
							msg);
				}
			}
			
			hideCustomProgressDialog();
		}

	}
	
	public class MngTask extends AsyncTask<Integer, String, Integer> {
		private Context mainFrame = null;
		private boolean isPass;
		private int id;

		public MngTask(Context mainFrame,boolean isPass,int id) {
			this.mainFrame = mainFrame;
			this.isPass = isPass;
			this.id = id;
		}

		@Override
		protected void onCancelled() {
			hideCustomProgressDialog();
			super.onCancelled();
		}

		@Override
		protected Integer doInBackground(Integer... params) {

			map = new HashMap<String, String>();
			map.put("id", String.valueOf(id));
			gson = new Gson();
			String result;
			if(isPass){
				result = HttpUtil.doPostForm(map, Constants.MNGOK,
							false, RegListActivity.this);
			}else{
				result = HttpUtil.doPostForm(map, Constants.MNGNO,
						false, RegListActivity.this);
			}
			if (StringUtil.isEmpty(result)) {
				msg = "连接服务器超时，请确认网络畅通";
				return Constants.FAILURE;
			}
			Log.d("********DocTimeTask*******", result);
			JSONObject myJsonObject = null;
			try {
				myJsonObject = new JSONObject(result);
				String r = myJsonObject.getString("r");
				if (r.equals("no")) {
					msg = myJsonObject.getString("msg");
					return Constants.FAILURE;
				}else if (r.equals("ok")) {
					msg = myJsonObject.getString("msg");
					return Constants.SUCCESS;
					
				}
			} catch (Exception e) {
				msg = e.getMessage();
			}
			msg="未知错误";
			return Constants.FAILURE;
		}

		@Override
		protected void onPreExecute() {
			showCustomProgrssDialog("正在加载...");
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO
			hideCustomProgressDialog();
			if (result == Constants.SUCCESS) {
				if (StringUtil.isEmpty(msg)) {
					msg="审核成功";
				}
				search();
				AlertDialogUtil.showAlertDialog(RegListActivity.this,
						msg);
			} else {
				if (StringUtil.isEmpty(msg)) {
					msg="审核失败";
				}
				AlertDialogUtil.showAlertDialog(RegListActivity.this,
						msg);
			}
		}

	}


	void showCustomProgrssDialog(String msg) {

		if (null == m_customProgrssDialog)

			m_customProgrssDialog = MyProgressDialog

			.createProgrssDialog(RegListActivity.this);

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
}
