package com.wbja.stone.ydt;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wbja.stone.ydt.adapter.MRCAdapter;
import com.wbja.stone.ydt.entity.MRClinic;
import com.wbja.stone.ydt.util.AlertDialogUtil;
import com.wbja.stone.ydt.util.Constants;
import com.wbja.stone.ydt.util.DB;
import com.wbja.stone.ydt.util.DateUtil;
import com.wbja.stone.ydt.util.FileUtil;
import com.wbja.stone.ydt.util.GsonUtil;
import com.wbja.stone.ydt.util.HttpUtil;
import com.wbja.stone.ydt.util.StringUtil;
import com.wbja.stone.ydt.window.MyProgressDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ClinicRecordActivity extends Activity {

	private ImageView headMore;
	private TextView headTitle;
	private ImageView back;
	private TextView txtNote;
	private ListView lvRecord;
	private List<MRClinic> recordList;
	private List<MRClinic> mrcList;
	private DB db;
	private Intent intent;
	private UpdateMRCTask updateMRCTask;
	private MyProgressDialog m_customProgrssDialog;
	private Map<String, String> map;
	private Gson gson;
	private String msg;

	private MRCAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clinic_record);

		db = new DB(ClinicRecordActivity.this);
		headMore = (ImageView) findViewById(R.id.headMore);
		headMore.setImageResource(R.drawable.top_add_icon);
		headMore.setVisibility(View.VISIBLE);

		headTitle = (TextView) findViewById(R.id.headTitle);
		txtNote = (TextView) findViewById(R.id.txtNote);
		headTitle.setText("门诊病历");
		lvRecord = (ListView) findViewById(R.id.lvRecord);

		back = (ImageView) findViewById(R.id.headBack);
		back.setVisibility(View.VISIBLE);
		txtNote = (TextView) findViewById(R.id.txtNote);

		headMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Intent intent = new Intent(ClinicRecordActivity.this,
				// AddRecordActivity.class);TabActivity
				intent = new Intent(ClinicRecordActivity.this,
						TabAddMRCActivity.class);
				startActivity(intent);
			}
		});

		initUI();

	}

	public void back(View view) {
		this.finish();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		initUI();
		super.onResume();
	}
	private void initUI() {
		//TODO
		//intent = new Intent(ClinicRecordActivity.this, TabShowMRCActivityG.class);
		intent = new Intent(ClinicRecordActivity.this, ClinicRecordWithFuzhenActivity.class);
		recordList = db.queryMRCFirst(StringUtil.getID_User(ClinicRecordActivity.this));
		// db.close();
		if (recordList != null && recordList.size() > 0) {
			txtNote.setVisibility(View.GONE);
			adapter = new MRCAdapter(ClinicRecordActivity.this, recordList);
			lvRecord.setAdapter(adapter);

			lvRecord.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> paret, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					String gonStr = GsonUtil.getJsonValue((MRClinic) adapter
							.getItem(position));
					StringUtil.saveInfo(ClinicRecordActivity.this, "record",
							gonStr);
					intent.putExtra("MRCId",
							((MRClinic) adapter.getItem(position)).getIdLocal());
					startActivity(intent);
				}
			});
			lvRecord.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent,
						View view, final int position, long id) {
					// TODO Auto-generated method stub
					new AlertDialog.Builder(ClinicRecordActivity.this)
							.setMessage("删除")
							.setPositiveButton("确认",
									new DialogInterface.OnClickListener() {

										public void onClick(
												DialogInterface dialog,
												int whichButton) {
											// delete files
											FileUtil.delFolder(recordList.get(
													position).getSrc());
											// delete from db
											db.deleteRecord(recordList.get(
													position).getIdLocal());
											// delete from ui
											recordList.remove(position);
											// update ui
											adapter.updateListView(recordList);
										}
									}).setNegativeButton("取消", null).show();

					return true;
				}
			});
		}
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		initUI();
	}

	/*
	 * 同步数据 *
	 */
	public void updateMRC(View v) {
		if (!StringUtil.isEmpty(StringUtil.getInfo(ClinicRecordActivity.this,
				"token", ""))) {
			if (updateMRCTask != null
					&& updateMRCTask.getStatus() == AsyncTask.Status.RUNNING) {
				updateMRCTask.cancel(true); // 如果Task还在运行，则先取消它
			}
			updateMRCTask = new UpdateMRCTask(this);
			updateMRCTask.execute();
		} else {
			new AlertDialog.Builder(ClinicRecordActivity.this)
			.setMessage("请先登录！")
			.setPositiveButton("确认",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog,
								int whichButton) {
						intent =new Intent(ClinicRecordActivity.this, LoginActivity.class);
						startActivity(intent);
						}
					}).setNegativeButton("取消", null)
			.show();
		}
	}

	public class UpdateMRCTask extends AsyncTask<Integer, String, Integer> {
		private Context mainFrame = null;

		public UpdateMRCTask(Context mainFrame) {
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
			map = new HashMap<String, String>();
			gson = new Gson();
			String result = HttpUtil.doPostForm(map, Constants.GETMRCLINICLIST,
					false, ClinicRecordActivity.this);
			if (StringUtil.isEmpty(result)) {
				msg = "连接服务器超时，请确认网络畅通";
				return Constants.FAILURE;
			}
			Log.d("********patientList*******", result);
			JSONObject myJsonObject = null;

			try {
				// myJsonObject = new JSONObject(result);
				// String r=myJsonObject.getString("r");
				// if(r.equals("no")){
				// msg=myJsonObject.getString("msg");
				// return Constants.FAILURE;
				// }
				mrcList = gson.fromJson(result,
						new TypeToken<List<MRClinic>>() {
						}.getType());
				Log.d("********patientList*******", mrcList.size() + "");
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
				updateToDB(mrcList);
				initUI();
				AlertDialogUtil.showAlertDialog(ClinicRecordActivity.this,
						"同步完成!");
			} else {
				if (!StringUtil.isEmpty(msg)) {
					AlertDialogUtil.showAlertDialog(ClinicRecordActivity.this,
							msg);
				}
			}
			hideCustomProgressDialog();
		}

	}

	private void updateToDB(List<MRClinic> list) {
		// 内存卡存在进行如下操作
		String name = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		String fileNmae = Environment.getExternalStorageDirectory().toString()
				+ File.separator + Constants.RECORDPATH;
		//String srcPath = fileNmae + name + File.separator;
		if (recordList != null && recordList.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				MRClinic up1 = list.get(i);
				int j = 0;
				for (; j < recordList.size(); j++) {
					if (up1.getId() == recordList.get(j).getId())
						break;// 找到之后返回
				}
				if (j >= recordList.size()) {// 数据全部遍历之后没有,存入数据库
					up1.setJiuzhenshijian(DateUtil.dateToString(
							DateUtil.jsonDateToDate(up1.getJiuzhenshijian()),
							"yyyy-MM-dd"));
					up1.setSrc("");
					db.insertMRC(up1);
				}
			}
		}
		if (recordList == null || recordList.size() == 0) {
			for (int i = 0; i < list.size(); i++) {
				MRClinic pp = list.get(i);
				pp.setJiuzhenshijian(DateUtil.dateToString(
						DateUtil.jsonDateToDate(pp.getJiuzhenshijian()),
						"yyyy-MM-dd"));
				pp.setSrc("");
				db.insertMRC(pp);
			}
		}
	}

	void showCustomProgrssDialog(String msg) {

		if (null == m_customProgrssDialog)

			m_customProgrssDialog = MyProgressDialog

			.createProgrssDialog(ClinicRecordActivity.this);

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
