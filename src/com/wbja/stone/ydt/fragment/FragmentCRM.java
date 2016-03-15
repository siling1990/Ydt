package com.wbja.stone.ydt.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wbja.stone.ydt.R;
import com.wbja.stone.ydt.ShowClassicCaseActivity;
import com.wbja.stone.ydt.adapter.ClassicCaseAdapter;
import com.wbja.stone.ydt.entity.ClassicCase;
import com.wbja.stone.ydt.util.AlertDialogUtil;
import com.wbja.stone.ydt.util.Constants;
import com.wbja.stone.ydt.util.HttpUtil;
import com.wbja.stone.ydt.util.StringUtil;
import com.wbja.stone.ydt.window.ClearEditText;
import com.wbja.stone.ydt.window.MyProgressDialog;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class FragmentCRM extends Fragment{
	private ListView lvClassic;
	private ClearEditText filter_edit;
	private TextView dialog;
	private List<ClassicCase> clcList;
	private MyProgressDialog m_customProgrssDialog;
	private SearchClassicCaseTask searchClassicCaseTask;
	private Gson gson;
	private Map<String, String> map;
	private String msg;
	private ClassicCaseAdapter adapter;
	private Intent intent;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.activity_classic_case, null);		
	}
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initView();
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	private void initView() {
		dialog = (TextView) getView().findViewById(R.id.dialog);
		lvClassic = (ListView) getView().findViewById(R.id.lvClassic);
		filter_edit = (ClearEditText) getView().findViewById(R.id.filter_edit);
		clcList = new ArrayList<ClassicCase>();
		adapter = new ClassicCaseAdapter(getActivity(), clcList);
		lvClassic.setAdapter(adapter);
		
		lvClassic.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				ClassicCase cls=(ClassicCase) adapter.getItem(position);
				intent=new Intent(getActivity(),ShowClassicCaseActivity.class);
				intent.putExtra("id", String.valueOf(cls.getCh_ID()));
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
	private void search(String keyword){
		if(!StringUtil.isEmpty(keyword)){
			if (searchClassicCaseTask != null
					&& searchClassicCaseTask.getStatus() == AsyncTask.Status.RUNNING) {
				searchClassicCaseTask.cancel(true); // 如果Task还在运行，则先取消它
			}

			searchClassicCaseTask = new SearchClassicCaseTask(getActivity(),keyword);
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
			String result = HttpUtil.doPostForm(map, Constants.GetCLCListM,
					false, getActivity());
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
						new TypeToken<List<ClassicCase>>() {
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
					AlertDialogUtil.showAlertDialog(getActivity(),
							msg);
				}
			}
			hideCustomProgressDialog();
		}

	}

	void showCustomProgrssDialog(String msg) {

		if (null == m_customProgrssDialog)

			m_customProgrssDialog = MyProgressDialog

			.createProgrssDialog(getActivity());

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
