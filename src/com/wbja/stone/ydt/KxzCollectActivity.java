package com.wbja.stone.ydt;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.wbja.stone.ydt.adapter.ZhengzhuangAdapter;
import com.wbja.stone.ydt.util.AlertDialogUtil;
import com.wbja.stone.ydt.util.Constants;
import com.wbja.stone.ydt.util.HttpUtil;
import com.wbja.stone.ydt.util.StringUtil;
import com.wbja.stone.ydt.window.DialogKx;
import com.wbja.stone.ydt.window.MyProgressDialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class KxzCollectActivity extends Activity {

	private Spinner spKebie;
	private String[] mItems;
	private String[] itembuwei;
	private ListView lvJyZz;
	private ListView lvBsz;
	private LinearLayout ly1;
	private CheckBox checkBox;

	private ArrayAdapter<String> adapterb;
	private Button btCancle, btOk;

	private String src;
	private String type;
	private Intent intent;
	private File[] files;
	private ListView lvzz;
	private List<String> zzList;
	private ZhengzhuangAdapter zzAd;
	private List<String> zZList;
	private ZhengzhuangAdapter zZAd;
	private ZhengzhuangAdapter zhzAd;
	private int pos;
	private YdtApplication ydt;

	private MyProgressDialog m_customProgrssDialog;
	private GetClassicCaseTask getClassicCaseTask;
	private ZZZHTask zZZHTask;
	private ZZBMTask zZBMTask;
	private Gson gson;
	private JSONArray jsonArray;
	private Map<String, String> map;
	private String msg;
	private StringBuilder zHongYZH;
	
	private ImageView headMore;
	private TextView headTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kxz_collect);
		headTitle = (TextView)findViewById(R.id.headTitle);
		headTitle.setText("聊天");
		headMore=(ImageView)findViewById(R.id.headMore);
		headMore.setVisibility(View.VISIBLE);
		headMore.setImageResource(R.drawable.top_add_icon);
		headMore.setVisibility(View.VISIBLE);
		
		spKebie = (Spinner) findViewById(R.id.spKebie);

		lvJyZz = (ListView) findViewById(R.id.lvJyZz);
		lvBsz = (ListView) findViewById(R.id.lvBsz);

		lvzz = (ListView) findViewById(R.id.lvzz);

		btCancle = (Button) findViewById(R.id.btCancle);
		btOk = (Button) findViewById(R.id.btOk);

		intent = getIntent();
		src = intent.getStringExtra("src");
		type = intent.getStringExtra("type");

		btCancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		// files=FileUtil.getTextFiles(src,type);

		ydt = YdtApplication.getInstance();
		btOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				StringBuilder strB = new StringBuilder();
				StringBuilder strBZ = new StringBuilder();

				if (zzList != null && zzList.size() > 0) {
					for (int i = 0; i < zzList.size(); i++) {
						strB.append("\n症状：");
						strB.append(zzList.get(i) + "\t");
						strBZ.append(zzList.get(i).split("\n")[0] + ",");
					}
				}
				Log.d("*****youmeiyou****", strBZ.toString());
				// zHongYZH=new StringBuilder();
				searchZZZH(strBZ.toString());

				ydt.getMrc().setIsEdit(1);
				ydt.getMrc().setKexiazheng(strB.toString());
			}
		});
		
		headMore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				StringBuilder strB = new StringBuilder();
				StringBuilder strBZ = new StringBuilder();

				if (zzList != null && zzList.size() > 0) {
					for (int i = 0; i < zzList.size(); i++) {
						strB.append("\n症状：");
						strB.append(zzList.get(i) + "\t");
						strBZ.append(zzList.get(i).split("\n")[0] + ",");
					}
				}
				Log.d("*****youmeiyou****", strBZ.toString());
				// zHongYZH=new StringBuilder();
				searchZZZH(strBZ.toString());

				ydt.getMrc().setIsEdit(1);
				ydt.getMrc().setKexiazheng(strB.toString());
			}
		});
		
		ly1 = (LinearLayout) findViewById(R.id.ly1);
		// 建立数据源
		mItems = getResources().getStringArray(R.array.kebie);
		zzList = new ArrayList<String>();
		zZList = new ArrayList<String>();
		zzAd = new ZhengzhuangAdapter(KxzCollectActivity.this, zzList);
		zhzAd = new ZhengzhuangAdapter(KxzCollectActivity.this, zzList);
		zZAd = new ZhengzhuangAdapter(KxzCollectActivity.this, zzList);

		ArrayAdapter<String> spA = new ArrayAdapter<String>(this,
				R.layout.item_spinner, mItems);

		spKebie.setAdapter(spA);

		lvzz.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				zzList.remove(position);
				zzAd.updateListView(zzList);
				lvzz.setAdapter(zhzAd);
				return false;
			}
		});

		spKebie.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				ly1.removeAllViews();
				switch (position) {
				case 0:
					itembuwei = getResources().getStringArray(R.array.buwei);
					break;
				case 1:
					;
					break;
				case 2:
					;
					break;
				case 3:
					break;
				default:
					break;
				}
				for (int i = 0; i < (itembuwei.length / 4 + 1); i++) {
					LinearLayout ly = new LinearLayout(KxzCollectActivity.this);
					ly.setOrientation(LinearLayout.HORIZONTAL);
					for (int j = i * 4; j < (i * 4 + 4) && j < itembuwei.length; j++) {
						checkBox = new CheckBox(KxzCollectActivity.this);
						checkBox.setText(itembuwei[j]);
						checkBox.setTag(j);
						checkBox.setLayoutParams(new LinearLayout.LayoutParams(
								LinearLayout.LayoutParams.WRAP_CONTENT,
								LinearLayout.LayoutParams.WRAP_CONTENT, 2));
						checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

							@Override
							public void onCheckedChanged(CompoundButton cb,
									boolean checked) {
								// TODO Auto-generated method stub
								if (checked) {
									checkCheckBox(cb,ly1);
									search(itembuwei[(Integer) cb.getTag()]);
								}
							}
						});
						ly.addView(checkBox);
					}
					ly1.addView(ly);
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

	}
	
    private void checkCheckBox(CompoundButton cb,LinearLayout layout){
    	View child;
    	int radioCount = layout.getChildCount();
    	for(int i = 0; i < radioCount; i++){
    		child = layout.getChildAt(i);
    		if (child instanceof CheckBox) {
    			if(child==cb){
    				// do nothing
    			} else {
    				((CheckBox) child).setChecked(false);
    			}
    		} else if(child instanceof LinearLayout){
    			int childCount = ((LinearLayout) child).getChildCount();
    			for(int j = 0; j < childCount; j++){
    				View view = ((LinearLayout) child).getChildAt(j);
    				if (view instanceof CheckBox) {
    					CheckBox button = (CheckBox) view;
    	    			if(button!=cb){
    	    				button.setChecked(false);
    	    			} 
    				}
    			}
    		}
    	}
    }

	public void back(View view) {
		this.finish();
	}
	public void save(View view) {
		StringBuilder strB = new StringBuilder();
		StringBuilder strBZ = new StringBuilder();

		if (zzList != null && zzList.size() > 0) {
			for (int i = 0; i < zzList.size(); i++) {
				strB.append("\n症状：");
				strB.append(zzList.get(i) + "\t");
				strBZ.append(zzList.get(i).split("\n")[0] + ",");
			}
		}
		Log.d("*****youmeiyou****", strBZ.toString());
		// zHongYZH=new StringBuilder();
		searchZZZH(strBZ.toString());

		ydt.getMrc().setIsEdit(1);
		ydt.getMrc().setKexiazheng(strB.toString());
	}

	private void search(String buwei) {
		if (!StringUtil.isEmpty(buwei)) {
			if (getClassicCaseTask != null
					&& getClassicCaseTask.getStatus() == AsyncTask.Status.RUNNING) {
				getClassicCaseTask.cancel(true); // 如果Task还在运行，则先取消它
			}

			getClassicCaseTask = new GetClassicCaseTask(this, buwei);
			getClassicCaseTask.execute();
		} else {
			finish();
		}
	}

	private void searchZZZH(String zhengh) {
		if (!StringUtil.isEmpty(zhengh)) {
			if (zZZHTask != null
					&& zZZHTask.getStatus() == AsyncTask.Status.RUNNING) {
				zZZHTask.cancel(true); // 如果Task还在运行，则先取消它
			}

			zZZHTask = new ZZZHTask(this, zhengh);
			zZZHTask.execute();
		} else {
			finish();
		}
	}

	private void searchZZBM(String zhengh) {
		if (!StringUtil.isEmpty(zhengh)) {
			if (zZBMTask != null
					&& zZBMTask.getStatus() == AsyncTask.Status.RUNNING) {
				zZBMTask.cancel(true); // 如果Task还在运行，则先取消它
			}

			zZBMTask = new ZZBMTask(this, zhengh);
			zZBMTask.execute();
		} else {
			finish();
		}
	}

	// 部位症状
	public class GetClassicCaseTask extends AsyncTask<Integer, String, Integer> {
		private Context mainFrame = null;
		private String buwei;

		public GetClassicCaseTask(Context mainFrame, String buwei) {
			this.mainFrame = mainFrame;
			this.buwei = buwei;
		}

		@Override
		protected void onCancelled() {
			hideCustomProgressDialog();
			super.onCancelled();
		}

		@Override
		protected Integer doInBackground(Integer... params) {

			map = new HashMap<String, String>();
			map.put("keyword", buwei);
			gson = new Gson();
			String result = HttpUtil.doPostForm(map, Constants.BWZZ, false,
					KxzCollectActivity.this);
			if (StringUtil.isEmpty(result)) {
				msg = "连接服务器超时，请确认网络畅通";
				return Constants.FAILURE;
			}
			Log.d("********症状*******", result);
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
				// clc =GsonUtil.getObject(result, ClassicRote.class);
				// Log.d("********patientList*******", clc.getKeyword());
				jsonArray = new JSONArray(result);

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
				zZList.clear();
				for (int i = 0; i < jsonArray.length(); i++) {
					try {
						zZList.add(jsonArray.getJSONObject(i).getString("name"));
						Log.d("******JSONOBJ******", jsonArray.getJSONObject(i)
								.getString("name"));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						Log.d("******JSONOBJ******", e.getMessage());
					}
				}

				Log.d("******JSONOBJSIZE******", zZList.size() + "");

				zZAd.updateListView(zZList);
				lvJyZz.setAdapter(zZAd);
				lvJyZz.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							final int position, long arg3) {

						DialogKx dialog = new DialogKx(KxzCollectActivity.this,
								zZList.get(position),
								new DialogKx.OnCustomDialogListener() {

									@Override
									public void back(String name) {
										zzList.add(zZList.get(position) + name);
										zzAd.updateListView(zzList);
										lvzz.setAdapter(zzAd);

										// if (position == 1) {
										// adapterb = new ArrayAdapter<String>(
										// KxzCollectActivity.this,
										// R.layout.item_spinner,
										// getResources()
										// .getStringArray(
										// R.array.bsz));
										// } else {
										// adapterb = new ArrayAdapter<String>(
										// KxzCollectActivity.this,
										// R.layout.item_spinner,
										// getResources()
										// .getStringArray(
										// R.array.zzxt));
										// }
										//
										// lvBsz.setAdapter(adapterb);
									}
								});
						dialog.show();

						// zzList.add(zZAd.getItem(position).toString());

					}
				});
				lvBsz.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						zzList.add(adapterb.getItem(position));
						zzAd.updateListView(zzList);
						lvzz.setAdapter(zzAd);
					}
				});
			} else {
				if (!StringUtil.isEmpty(msg)) {
					AlertDialogUtil.showAlertDialog(KxzCollectActivity.this,
							msg);
				}
			}
			hideCustomProgressDialog();
		}

	}

	// 症状症候
	public class ZZZHTask extends AsyncTask<Integer, String, Integer> {
		private Context mainFrame = null;
		private String zhengh;

		public ZZZHTask(Context mainFrame, String zhengh) {
			this.mainFrame = mainFrame;
			this.zhengh = zhengh;
		}

		@Override
		protected void onCancelled() {
			hideCustomProgressDialog();
			super.onCancelled();
		}

		@Override
		protected Integer doInBackground(Integer... params) {

			map = new HashMap<String, String>();
			map.put("keyword", zhengh);
			gson = new Gson();
			String result = HttpUtil.doPostForm(map, Constants.ZZZH, false,
					KxzCollectActivity.this);
			if (StringUtil.isEmpty(result)) {
				msg = "连接服务器超时，请确认网络畅通";
				return Constants.FAILURE;
			}
			Log.d("********症候*******", result);
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
				// clc =GsonUtil.getObject(result, ClassicRote.class);
				// Log.d("********patientList*******", clc.getKeyword());
				jsonArray = new JSONArray(result);

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
			zHongYZH = new StringBuilder();
			if (result == Constants.SUCCESS) {
				for (int i = 0; i < jsonArray.length(); i++) {
					try {
						zHongYZH.append(jsonArray.getJSONObject(i).getString(
								"name"));
						zHongYZH.append(",");
						Log.d("******JSONOBJ******", jsonArray.getJSONObject(i)
								.getString("name"));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						Log.d("******JSONOBJ******", e.getMessage());
					}
				}
				Log.d("******JSONOBJZH******", zHongYZH.toString());
				ydt.getMrc().setZhongyizhenduan(zHongYZH.toString());

			} else {
				if (!StringUtil.isEmpty(msg)) {
					AlertDialogUtil.showAlertDialog(KxzCollectActivity.this,
							msg);
				}
			}
			hideCustomProgressDialog();
			searchZZBM(ydt.getMrc().getZhongyizhenduan());
		}

	}

	// 症候病名
	public class ZZBMTask extends AsyncTask<Integer, String, Integer> {
		private Context mainFrame = null;
		private String BingM;

		public ZZBMTask(Context mainFrame, String BingM) {
			this.mainFrame = mainFrame;
			this.BingM = BingM;
		}

		@Override
		protected void onCancelled() {
			hideCustomProgressDialog();
			super.onCancelled();
		}

		@Override
		protected Integer doInBackground(Integer... params) {

			map = new HashMap<String, String>();
			map.put("keyword", BingM);
			gson = new Gson();
			String result = HttpUtil.doPostForm(map, Constants.ZHBM, false,
					KxzCollectActivity.this);
			if (StringUtil.isEmpty(result)) {
				msg = "连接服务器超时，请确认网络畅通";
				return Constants.FAILURE;
			}
			Log.d("********病名*******", result);
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
				// clc =GsonUtil.getObject(result, ClassicRote.class);
				// Log.d("********patientList*******", clc.getKeyword());
				jsonArray = new JSONArray(result);

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
				zHongYZH = new StringBuilder();
				for (int i = 0; i < jsonArray.length(); i++) {
					try {
						zHongYZH.append(jsonArray.getJSONObject(i).getString(
								"name"));
						zHongYZH.append(",");
						Log.d("******JSONOBJ******", jsonArray.getJSONObject(i)
								.getString("name"));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						Log.d("******JSONOBJ******", e.getMessage());
					}
				}
				Log.d("******JSONOBJZH******", zHongYZH.toString());
				ydt.getMrc().setXiyizhenduan(zHongYZH.toString());
				ydt.getMrc().setBingming(zHongYZH.toString());

			} else {
				if (!StringUtil.isEmpty(msg)) {
					AlertDialogUtil.showAlertDialog(KxzCollectActivity.this,
							msg);
				}
			}
			hideCustomProgressDialog();
			finish();
		}

	}

	void showCustomProgrssDialog(String msg) {

		if (null == m_customProgrssDialog)

			m_customProgrssDialog = MyProgressDialog

			.createProgrssDialog(KxzCollectActivity.this);

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
