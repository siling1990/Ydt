package com.wbja.stone.ydt;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.wbja.stone.ydt.entity.ClassicRote;
import com.wbja.stone.ydt.util.AlertDialogUtil;
import com.wbja.stone.ydt.util.Constants;
import com.wbja.stone.ydt.util.GsonUtil;
import com.wbja.stone.ydt.util.HttpUtil;
import com.wbja.stone.ydt.util.StringUtil;
import com.wbja.stone.ydt.window.MyProgressDialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class ShowClassicRoteActivity extends Activity {

	private TextView txtContent;
	private ClassicRote clc;
	private MyProgressDialog m_customProgrssDialog;
	private GetClassicCaseTask getClassicCaseTask;
	private Map<String, String> map;
	private Gson gson;
	private String msg;
	private StringBuilder strB;
	private Intent intent;
	private TextView headTitle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_classic_case);
		txtContent=(TextView)findViewById(R.id.txtContent);
		headTitle=(TextView)findViewById(R.id.headTitle);
		intent=getIntent();
		String id = intent.getStringExtra("id");
		search(id);
	}
	public void back(View view){
		this.finish();
	}
	private void search(String id){
		if(!StringUtil.isEmpty(id)){
			if (getClassicCaseTask != null
					&& getClassicCaseTask.getStatus() == AsyncTask.Status.RUNNING) {
				getClassicCaseTask.cancel(true); // 如果Task还在运行，则先取消它
			}

			getClassicCaseTask = new GetClassicCaseTask(this,id);
			getClassicCaseTask.execute();
		}
	}

	public class GetClassicCaseTask extends
			AsyncTask<Integer, String, Integer> {
		private Context mainFrame = null;
		private String id;

		public GetClassicCaseTask(Context mainFrame,String id) {
			this.mainFrame = mainFrame;
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
			map.put("id", id);
			gson = new Gson();
			String result = HttpUtil.doPostForm(map, Constants.GetCLRDetailM,
					false, ShowClassicRoteActivity.this);
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
				clc =GsonUtil.getObject(result, ClassicRote.class);
				Log.d("********patientList*******", clc.getKeyword());
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
				if(clc!=null){
					strB=new StringBuilder();
					if(clc.getKeyword().length()>16){
						headTitle.setText(clc.getKeyword().subSequence(0, 15)+"...");
					}else{
						headTitle.setText(clc.getKeyword());
					}
					strB.append("病名:").append(clc.getName_disease()).append("\n\n").append(clc.getInhosp_progress()).append("\n时间：").append(clc.getInhosp_sheet());
				txtContent.setText(Html.fromHtml(strB.toString()));
				//	lvClassic.setAdapter(adapter);
				}
			} else {
				if (!StringUtil.isEmpty(msg)) {
					AlertDialogUtil.showAlertDialog(ShowClassicRoteActivity.this,
							msg);
				}
			}
			hideCustomProgressDialog();
		}

	}

	void showCustomProgrssDialog(String msg) {

		if (null == m_customProgrssDialog)

			m_customProgrssDialog = MyProgressDialog

			.createProgrssDialog(ShowClassicRoteActivity.this);

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
