package com.wbja.stone.ydt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wbja.stone.ydt.adapter.ClassicRoteAdapter;
import com.wbja.stone.ydt.entity.ClassicRote;
import com.wbja.stone.ydt.util.AlertDialogUtil;
import com.wbja.stone.ydt.util.Constants;
import com.wbja.stone.ydt.util.HttpUtil;
import com.wbja.stone.ydt.util.StringUtil;
import com.wbja.stone.ydt.window.ClearEditText;
import com.wbja.stone.ydt.window.MyProgressDialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class ClassicRoteActivity extends Activity {

	private ListView lvClassic;
	private ClearEditText filter_edit;
	private TextView dialog;
	private List<ClassicRote> clcList;
	private MyProgressDialog m_customProgrssDialog;
	private SearchClassicCaseTask searchClassicCaseTask;
	private Gson gson;
	private Map<String, String> map;
	private String msg;
	private ClassicRoteAdapter adapter;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_classic_case);
		initView();
	}

	private void initView() {
		dialog = (TextView) findViewById(R.id.dialog);
		lvClassic = (ListView) findViewById(R.id.lvClassic);
		filter_edit = (ClearEditText) findViewById(R.id.filter_edit);
		clcList = new ArrayList<ClassicRote>();
		adapter = new ClassicRoteAdapter(ClassicRoteActivity.this, clcList);
		lvClassic.setAdapter(adapter);
		
		lvClassic.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				ClassicRote cls=(ClassicRote) adapter.getItem(position);
				intent=new Intent(ClassicRoteActivity.this,ShowClassicRoteActivity.class);
				intent.putExtra("id", String.valueOf(cls.getId_disease()));
				startActivity(intent);
				
			}
		});
		
		// 根据输入框输入值的改变来过滤搜索
		filter_edit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				search(s.toString());
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
	public void back(View view){
		this.finish();
	}
	private void search(String keyword){
		if(!StringUtil.isEmpty(keyword)){
			if (searchClassicCaseTask != null
					&& searchClassicCaseTask.getStatus() == AsyncTask.Status.RUNNING) {
				searchClassicCaseTask.cancel(true); // 如果Task还在运行，则先取消它
			}

			searchClassicCaseTask = new SearchClassicCaseTask(this,keyword);
			searchClassicCaseTask.execute();
		}
	}

	public class SearchClassicCaseTask extends
			AsyncTask<Integer, String, Integer> {
		private Context mainFrame = null;
		private String key;

		public SearchClassicCaseTask(Context mainFrame,String key) {
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
			String result = HttpUtil.doPostForm(map, Constants.GetCLRListM,
					false, ClassicRoteActivity.this);
			if (StringUtil.isEmpty(result)) {
				msg = "连接服务器超时，请确认网络畅通";
				return Constants.FAILURE;
			}
			Log.d("********patientList*******", result);
			JSONObject myJsonObject = null;
			 try{
				 myJsonObject = new JSONObject(result);
				 String r=myJsonObject.getString("r");
				 if(r.equals("no")){
				 msg=myJsonObject.getString("msg");
				 return Constants.FAILURE;
				 }
			 }
			 catch(Exception e){
				 
			 }
			try {
				clcList = gson.fromJson(result,
						new TypeToken<List<ClassicRote>>() {
						}.getType());
				Log.d("********patientList*******", clcList.size() + "");
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
				if(clcList!=null&&clcList.size()>0){
					adapter.updateListView(clcList);
				//	lvClassic.setAdapter(adapter);
				}
			} else {
				if (!StringUtil.isEmpty(msg)) {
					AlertDialogUtil.showAlertDialog(ClassicRoteActivity.this,
							msg);
				}
			}
			hideCustomProgressDialog();
		}

	}

	void showCustomProgrssDialog(String msg) {

		if (null == m_customProgrssDialog)

			m_customProgrssDialog = MyProgressDialog

			.createProgrssDialog(ClassicRoteActivity.this);

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
