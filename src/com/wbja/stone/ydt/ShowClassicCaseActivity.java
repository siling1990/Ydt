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
				getClassicCaseTask.cancel(true); // 如果Task还在运行，则先取消它
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
			showCustomProgrssDialog("正在加载...");
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
					strB.append("医案标题:").append(clc.getCh_title()).append("\n\n医案摘要：")
					.append(clc.getCh_zhaiyao()).append("\n\n医生姓名：").append(clc.getFi_name())
					.append("\n\n患者："+clc.getChp_name()).append("\n\n性别："+clc.getChp_gender())
					.append("\n\n出生年月："+clc.getChp_birthday()).append("\n\n就诊时间："+clc.getChf_date())
					.append("\n\n节气："+clc.getChp_season())
					.append("\n\n主诉："+clc.getChp_zhusu()).append("\n\n现病："+clc.getChp_xianbing())
					.append("\n\n刻下症："+clc.getChp_birthday()).append("\n\n症状拆解："+clc.getChf_date())
					.append("\n\n既往史："+clc.getChp_jiwang()).append("\n\n个人史："+clc.getChp_geren())
					.append("\n\n过敏史："+clc.getChp_guomin()).append("\n\n婚育史："+clc.getChp_hunyu())
					.append("\n\n家族史："+clc.getChp_jiazu()).append("\n\n体温："+clc.getChf_tiwen())
					.append("\n\n脉搏："+clc.getChf_maibo()).append("\n\n血压："+clc.getChf_xueya())
					.append("\n\n呼吸："+clc.getChf_huxi()).append("\n\n舌质："+clc.getChf_bzfx())
					.append("\n\n舌苔："+clc.getChf_shetai()).append("\n\n脉象："+clc.getChf_maixiang())
					.append("\n\n辅助检查："+clc.getChf_fuzhujiancha()).append("\n\n辩证分析："+clc.getChf_bzfx())
					.append("\n\n中医诊断："+clc.getChf_zyzd()).append("\n\n西医诊断："+clc.getChf_xyzd())
					.append("\n\n中医证候："+clc.getChf_zyzh()).append("\n\n治则治法："+clc.getChf_zzzf())
					.append("\n\n方名："+clc.getChf_fm()).append("\n\n组成："+clc.getChf_fjzc())
					.append("\n\n用法："+clc.getChf_yf()).append("\n\n针灸："+clc.getChf_zhenjiu())
					.append("\n\n针灸穴位："+clc.getChf_zhenjiuxuewei()).append("\n\n推拿治疗："+clc.getChf_tuina())
					.append("\n\n其他治疗："+clc.getChf_qita()).append("\n\n医嘱："+clc.getChf_yizhu());
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
