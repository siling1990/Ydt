package com.wbja.stone.ydt;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.wbja.stone.ydt.entity.ClassicCase;
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
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class ShowClassicCaseActivity extends Activity {

	private TextView txtContent;
	private ClassicCase clc;
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
	private void search(String id){
		if(!StringUtil.isEmpty(id)){
			if (getClassicCaseTask != null
					&& getClassicCaseTask.getStatus() == AsyncTask.Status.RUNNING) {
				getClassicCaseTask.cancel(true); // ���Task�������У�����ȡ����
			}

			getClassicCaseTask = new GetClassicCaseTask(this,id);
			getClassicCaseTask.execute();
		}
	}
	public void back(View view){
		this.finish();
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
			String result = HttpUtil.doPostForm(map, Constants.GetCLCDetailM,
					false, ShowClassicCaseActivity.this);
			if (StringUtil.isEmpty(result)) {
				msg = "���ӷ�������ʱ����ȷ�����糩ͨ";
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
				clc =GsonUtil.getObject(result, ClassicCase.class);
				Log.d("********patientList*******", clc.getCh_title());
				return Constants.SUCCESS;
			} catch (Exception e) {
				msg = e.getMessage();
			}
			return Constants.FAILURE;
		}

		@Override
		protected void onPreExecute() {
			showCustomProgrssDialog("���ڼ���...");
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO
			if (result == Constants.SUCCESS) {
				if(clc!=null){
					strB=new StringBuilder();
					if(clc.getCh_title().length()>16){
						headTitle.setText(clc.getCh_title().subSequence(0, 15)+"...");
					}else{
						headTitle.setText(clc.getCh_title());
					}
					strB.append("ҽ������:").append(clc.getCh_title()).append("\n\nҽ��ժҪ��")
					.append(clc.getCh_zhaiyao()).append("\n\nҽ��������").append(clc.getFi_name())
					.append("\n\n���ߣ�"+clc.getChp_name()).append("\n\n�Ա�"+clc.getChp_gender())
					.append("\n\n�������£�"+clc.getChp_birthday()).append("\n\n����ʱ�䣺"+clc.getChf_date())
					.append("\n\n������"+clc.getChp_season())
					.append("\n\n���ߣ�"+clc.getChp_zhusu()).append("\n\n�ֲ���"+clc.getChp_xianbing())
					.append("\n\n����֢��"+clc.getChp_birthday()).append("\n\n֢״��⣺"+clc.getChf_date())
					.append("\n\n����ʷ��"+clc.getChp_jiwang()).append("\n\n����ʷ��"+clc.getChp_geren())
					.append("\n\n����ʷ��"+clc.getChp_guomin()).append("\n\n����ʷ��"+clc.getChp_hunyu())
					.append("\n\n����ʷ��"+clc.getChp_jiazu()).append("\n\n���£�"+clc.getChf_tiwen())
					.append("\n\n������"+clc.getChf_maibo()).append("\n\nѪѹ��"+clc.getChf_xueya())
					.append("\n\n������"+clc.getChf_huxi()).append("\n\n���ʣ�"+clc.getChf_bzfx())
					.append("\n\n��̦��"+clc.getChf_shetai()).append("\n\n����"+clc.getChf_maixiang())
					.append("\n\n������飺"+clc.getChf_fuzhujiancha()).append("\n\n��֤������"+clc.getChf_bzfx())
					.append("\n\n��ҽ��ϣ�"+clc.getChf_zyzd()).append("\n\n��ҽ��ϣ�"+clc.getChf_xyzd())
					.append("\n\n��ҽ֤��"+clc.getChf_zyzh()).append("\n\n�����η���"+clc.getChf_zzzf())
					.append("\n\n������"+clc.getChf_fm()).append("\n\n��ɣ�"+clc.getChf_fjzc())
					.append("\n\n�÷���"+clc.getChf_yf()).append("\n\n��ģ�"+clc.getChf_zhenjiu())
					.append("\n\n���Ѩλ��"+clc.getChf_zhenjiuxuewei()).append("\n\n�������ƣ�"+clc.getChf_tuina())
					.append("\n\n�������ƣ�"+clc.getChf_qita()).append("\n\nҽ����"+clc.getChf_yizhu());
				txtContent.setText(strB.toString());
				//	lvClassic.setAdapter(adapter);
				}
			} else {
				if (!StringUtil.isEmpty(msg)) {
					AlertDialogUtil.showAlertDialog(ShowClassicCaseActivity.this,
							msg);
				}
			}
			hideCustomProgressDialog();
		}

	}

	void showCustomProgrssDialog(String msg) {

		if (null == m_customProgrssDialog)

			m_customProgrssDialog = MyProgressDialog

			.createProgrssDialog(ShowClassicCaseActivity.this);

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
