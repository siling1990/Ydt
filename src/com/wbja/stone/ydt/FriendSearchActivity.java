package com.wbja.stone.ydt;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;
import io.rong.message.CommandNotificationMessage;
import io.rong.message.ContactNotificationMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wbja.stone.ydt.adapter.FriendSearchAdapter;
import com.wbja.stone.ydt.entity.Friend;
import com.wbja.stone.ydt.entity.SysUser;
import com.wbja.stone.ydt.util.AlertDialogUtil;
import com.wbja.stone.ydt.util.Constants;
import com.wbja.stone.ydt.util.DB;
import com.wbja.stone.ydt.util.HttpUtil;
import com.wbja.stone.ydt.util.StringUtil;
import com.wbja.stone.ydt.window.ClearEditText;
import com.wbja.stone.ydt.window.MyProgressDialog;
import com.wbja.stone.ydt.FriendSearchActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class FriendSearchActivity extends Activity {

	private TextView headTitle;
	private ClearEditText filter_edit;
	private ListView lvFriend;
	private TextView dialog;
	private List<SysUser> friList;
	private MyProgressDialog m_customProgrssDialog;
	private SearchClassicCaseTask searchClassicCaseTask;
	private AddFriendTask addFriendTask;
	private Gson gson;
	private Map<String, String> map;
	private String msg;
	private FriendSearchAdapter adapter;
	private Intent intent;
	private Button btSearch;
	private SysUser user;
	private DB db;

	public void back(View view) {
		this.finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend);

		filter_edit = (ClearEditText) findViewById(R.id.filter_edit);
		lvFriend = (ListView) findViewById(R.id.lvFriend);
		headTitle = (TextView) findViewById(R.id.headTitle);
		headTitle.setText("查找好友");
		btSearch = (Button) findViewById(R.id.btSearch);

		db = new DB(FriendSearchActivity.this);

		friList = new ArrayList<SysUser>();
		adapter = new FriendSearchAdapter(FriendSearchActivity.this, friList);
		lvFriend.setAdapter(adapter);

		lvFriend.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				user = (SysUser) adapter.getItem(position);
				new AlertDialog.Builder(FriendSearchActivity.this)
						.setMessage("加为好友？")
						.setPositiveButton("确认",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int whichButton) {
										addFri(user);
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										// TODO Auto-generated method stub

									}
								}).show();
				// intent=new
				// Intent(FriendActivity.this,ShowClassicRoteActivity.class);
				// intent.putExtra("id", String.valueOf(cls.getId_disease()));
				// startActivity(intent);

			}
		});

		// filter_edit.addTextChangedListener(new TextWatcher() {
		//
		// @Override
		// public void onTextChanged(CharSequence txt, int arg1, int arg2,
		// int arg3) {
		// // TODO Auto-generated method stub
		// search(txt.toString());
		// }
		//
		// @Override
		// public void beforeTextChanged(CharSequence arg0, int arg1,
		// int arg2, int arg3) {
		//
		// }
		//
		// @Override
		// public void afterTextChanged(Editable arg0) {
		//
		// }
		// });

		btSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				search(filter_edit.getText().toString());
			}
		});
	}

	private void search(String keyword) {
		if (!StringUtil.isEmpty(keyword)) {
			if (searchClassicCaseTask != null
					&& searchClassicCaseTask.getStatus() == AsyncTask.Status.RUNNING) {
				searchClassicCaseTask.cancel(true); // 如果Task还在运行，则先取消它
			}

			searchClassicCaseTask = new SearchClassicCaseTask(this, keyword);
			searchClassicCaseTask.execute();
		}
	}

	private void addFri(SysUser user) {
		if (user != null) {
//			RongIM.getInstance().getRongIMClient().sendMessage(Conversation.ConversationType.SYSTEM, ""+user.getId(),
//					ContactNotificationMessage.obtain(Constants.OPADDFRIEND,StringUtil.getID_User(FriendSearchActivity.this) , ""+user.getId(), "请求加为好友"), "", "", null);
			if (addFriendTask != null
					&& addFriendTask.getStatus() == AsyncTask.Status.RUNNING) {
				addFriendTask.cancel(true); // 如果Task还在运行，则先取消它
			}

			addFriendTask = new AddFriendTask(this, user);
			addFriendTask.execute();
		}
	}

	// 查找好友
	public class SearchClassicCaseTask extends
			AsyncTask<Integer, String, Integer> {
		private Context mainFrame = null;
		private String key;

		public SearchClassicCaseTask(Context mainFrame, String key) {
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
			String result = HttpUtil.doPostForm(map, Constants.SEARCHFRIEND,
					false, FriendSearchActivity.this);
			if (StringUtil.isEmpty(result)) {
				msg = "连接服务器超时，请确认网络畅通";
				return Constants.FAILURE;
			}
			Log.d("********patientList*******", result);
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
				friList = gson.fromJson(result, new TypeToken<List<SysUser>>() {
				}.getType());
				Log.d("********patientList*******", friList.size() + "");
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
				if (friList != null && friList.size() > 0) {
					adapter.updateListView(friList);
					// lvClassic.setAdapter(adapter);
				}
			} else {
				if (!StringUtil.isEmpty(msg)) {
					AlertDialogUtil.showAlertDialog(FriendSearchActivity.this,
							msg);
				}
			}
			hideCustomProgressDialog();
		}

	}

	// 添加好友
	public class AddFriendTask extends AsyncTask<Integer, String, Integer> {
		private Context mainFrame = null;
		private SysUser sysuser;

		public AddFriendTask(Context mainFrame, SysUser sysuser) {
			this.mainFrame = mainFrame;
			this.sysuser = sysuser;
		}

		@Override
		protected void onCancelled() {
			hideCustomProgressDialog();
			super.onCancelled();
		}

		@Override
		protected Integer doInBackground(Integer... params) {

			map = new HashMap<String, String>();
			map.put("id_friend", sysuser.getId());
			map.put("displayname_friend", sysuser.getDisplayname());
			map.put("displayname_user", StringUtil.getInfo(
					FriendSearchActivity.this, Constants.DISPLAYNAME,
					StringUtil.getInfo(FriendSearchActivity.this,
							Constants.USERNAME, "")));
			gson = new Gson();
			String result = HttpUtil.doPostForm(map, Constants.ADDFRIEND,
					false, FriendSearchActivity.this);
			if (StringUtil.isEmpty(result)) {
				msg = "连接服务器超时，请确认网络畅通";
				return Constants.FAILURE;
			}
			Log.d("********patientList*******", result);
			JSONObject myJsonObject = null;
			try {
				myJsonObject = new JSONObject(result);
				String r = myJsonObject.getString("r");
				if (r.equals("no")) {
					msg = myJsonObject.getString("msg");
					return Constants.FAILURE;
				} else {
					msg = myJsonObject.getString("msg");
					return Constants.SUCCESS;
				}
			} catch (Exception e) {

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
			hideCustomProgressDialog();

			if (result == Constants.SUCCESS) {

				Friend item = new Friend();
				item.setId_friend(sysuser.getId());
				item.setId_user(StringUtil.getInfo(FriendSearchActivity.this,
						Constants.USERID, "0"));
				item.setDisplayname_friend(sysuser.getDisplayname());
				item.setDisplayname_user(StringUtil.getInfo(FriendSearchActivity.this,
						Constants.DOCNAME, "0"));
				item.setIs_pass(1);

				if (StringUtil.getInfo(FriendSearchActivity.this,
						Constants.USERID, "0").equals(item.getId_user())) {
					RongIM.getInstance().refreshUserInfoCache(
							new UserInfo(item.getId_friend(), item
									.getDisplayname_friend(), null));
				} else {
					RongIM.getInstance().refreshUserInfoCache(
							new UserInfo(item.getId_user(), item
									.getDisplayname_user(), null));
				}
				db.insertFriend(item);
				if (!StringUtil.isEmpty(msg)) {
					AlertDialogUtil.showAlertDialog(FriendSearchActivity.this,
							msg);
				}
			} else {
				if (!StringUtil.isEmpty(msg)) {
					AlertDialogUtil.showAlertDialog(FriendSearchActivity.this,
							msg);
				}
			}

		}

	}

	void showCustomProgrssDialog(String msg) {

		if (null == m_customProgrssDialog)

			m_customProgrssDialog = MyProgressDialog

			.createProgrssDialog(FriendSearchActivity.this);

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
