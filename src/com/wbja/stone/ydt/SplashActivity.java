package com.wbja.stone.ydt;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient.ConnectCallback;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wbja.stone.ydt.entity.Friend;
import com.wbja.stone.ydt.entity.Patient;
import com.wbja.stone.ydt.entity.UpdateInfo;
import com.wbja.stone.ydt.util.Constants;
import com.wbja.stone.ydt.util.DB;
import com.wbja.stone.ydt.util.FileUtil;
import com.wbja.stone.ydt.util.HttpUtil;
import com.wbja.stone.ydt.util.StringUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends Activity {

	private DB db;
	private Intent intent;
	private FriendListTask friendListTask;
	private UpdateTask updateTask;

	private UpdateInfo updateInfo;
	private ProgressDialog pBar;

	private TextView version;
	private Gson gson;
	private Map<String, String> map;
	private String msg;
	private List<Friend> friList;
	private List<Patient> pList;
	private SQLiteDatabase sDB;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		version = (TextView) findViewById(R.id.version);
		db = new DB(SplashActivity.this);
			// 检查更新
			if (updateTask != null && updateTask.getStatus() == AsyncTask.Status.RUNNING) {
				updateTask.cancel(true); // 如果Task还在运行，则先取消它
			}
			updateTask = new UpdateTask(SplashActivity.this);
			updateTask.execute();
	}

	/**
	 * 融云token
	 */
	public void rongToken() {
		String rongtoken = StringUtil.getInfo(SplashActivity.this, Constants.RONGTOKEN, "");
		if (!StringUtil.isEmpty(rongtoken)) {
			RongIM.connect(StringUtil.getInfo(SplashActivity.this, Constants.RONGTOKEN, ""), new ConnectCallback() {

				@Override
				public void onSuccess(String arg0) {
					// TODO Auto-generated method stub
					Log.d("*******连接融云成功****", "");
					if (friendListTask != null && friendListTask.getStatus() == AsyncTask.Status.RUNNING) {
						friendListTask.cancel(true); // 如果Task还在运行，则先取消它
					}
					friendListTask = new FriendListTask(SplashActivity.this);
					friendListTask.execute();
				}

				@Override
				public void onError(ErrorCode arg0) {
					// TODO Auto-generated method stub
					Log.d("*******连接融云失败****", "错误：" + arg0.getMessage());

					toLogin();
				}

				@Override
				public void onTokenIncorrect() {
					// TODO Auto-generated method stub
					Log.d("*******连接融云失败****", "Token过期");
					toLogin();
				}
			});
		} else {
			toLogin();
		}
	}

	/**
	 * 好友列表
	 */
	public class FriendListTask extends AsyncTask<Integer, String, Integer> {
		private Context mainFrame = null;

		public FriendListTask(Context mainFrame) {
			this.mainFrame = mainFrame;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected Integer doInBackground(Integer... params) {
			// TODO
			// 封装数据
			Map<String, String> map = new HashMap<String, String>();

			String result = HttpUtil.doPostForm(map, Constants.FRIENDLIST, false, SplashActivity.this);

			JSONObject myJsonObject = null;
			gson = new Gson();
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
				friList = gson.fromJson(result, new TypeToken<List<Friend>>() {
				}.getType());
				Log.d("********patientList*******", friList.size() + "");
				// return Constants.SUCCESS;
			} catch (Exception e) {
				msg = e.getMessage();
			}
			// 更新患者信息
			result = HttpUtil.doPostForm(map, Constants.GETPATIENTLIST, false, SplashActivity.this);
			if (StringUtil.isEmpty(result)) {
				msg = "连接服务器超时，请确认网络畅通";
				return Constants.FAILURE;
			}
			Log.d("********patientList*******", result);
			try {
				pList = gson.fromJson(result, new TypeToken<List<Patient>>() {
				}.getType());
				Log.d("********patientList*******", pList.size() + "");
				return Constants.SUCCESS;
			} catch (Exception e) {
				msg = e.getMessage();
			}
			return Constants.FAILURE;

		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onPostExecute(Integer result) {
			// 根据a-z进行排序源数据
			if (result == Constants.SUCCESS) {

				Patient pp;
				db.deletePatient();
				for (int i = 0; i < pList.size(); i++) {
					pp = pList.get(i);
					db.insertPatient(pp);
				}

				RongIM.getInstance().refreshUserInfoCache(new UserInfo(StringUtil.getID_User(SplashActivity.this),
						StringUtil.getInfo(SplashActivity.this, Constants.DOCNAME, ""), null));
				if (friList.size() > 0) {
					db = new DB(SplashActivity.this);
					db.deleteFriend();

					RongIM.getInstance().refreshGroupInfoCache(new Group("g1", "医患交流群",
							Uri.parse("http://rongcloud-web.qiniudn.com/docs_demo_rongcloud_logo.png")));
					for (int i = 0; i < friList.size(); i++) {
						if (StringUtil.getInfo(SplashActivity.this, Constants.USERID, "0")
								.equals(friList.get(i).getId_user())) {
							RongIM.getInstance().refreshUserInfoCache(new UserInfo(friList.get(i).getId_friend(),
									friList.get(i).getDisplayname_friend(), null));
						} else {
							RongIM.getInstance().refreshUserInfoCache(new UserInfo(friList.get(i).getId_user(),
									friList.get(i).getDisplayname_user(), null));
						}
						db.insertFriend(friList.get(i));
					}
				} else {
					db = new DB(SplashActivity.this);
					db.deleteFriend();
				}
				msg = "";
				String username = StringUtil.getInfo(SplashActivity.this, Constants.USERNAME, "");
				String token = StringUtil.getInfo(SplashActivity.this, "token", "");

				if ((!StringUtil.isEmpty(token)) && (!StringUtil.isEmpty(username))) {
					intent = new Intent(SplashActivity.this, MainTabActivity.class);
				} else {
					intent = new Intent(SplashActivity.this, LoginActivity.class);
					intent.putExtra("isLoad", true);
				}
				startActivity(intent);
				SplashActivity.this.finish();
			}
			if (result == Constants.FAILURE) {
				toLogin();
			}

		}

	}

	/**
	 * banben
	 */
	public class UpdateTask extends AsyncTask<Integer, String, Integer> {
		private Context mainFrame = null;

		public UpdateTask(Context mainFrame) {
			this.mainFrame = mainFrame;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected Integer doInBackground(Integer... params) {
			// 封装数据
			updateInfo = HttpUtil.getUpDateInfo(Constants.UPDATEURL);
			if (updateInfo != null) {
				return Constants.SUCCESS;
			}

			return Constants.FAILURE;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onPostExecute(Integer result) {
			if (result == Constants.SUCCESS) {
				
				
				// 版本更新
				// 获取packagemanager的实例
				PackageManager packageManager = getPackageManager();
				// 是否删删除数据库
				// getPackageName()是你当前类的包名，0代表是获取版本信息
				try {
					PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
					version.setText(packInfo.versionName);

					if (!updateInfo.getVersion().equals(packInfo.versionName)) {
						showUpdateDialog();
						sDB = db.getWritableDatabase();
						db.onUpgrade(sDB, 1, 2);
						StringUtil.removeAllInfo(SplashActivity.this);
						// StringUtil.saveInfo(SplashActivity.this, "version",
						// packInfo.versionName);
						if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
							FileUtil.delFolder(Environment.getExternalStorageDirectory().toString() + File.separator
									+ Constants.RECORDPATH);
						}

					} else {
						// 版本最新
						// 是不是第一次登陆
						if (!StringUtil.getInfo(SplashActivity.this, "isfirstLogin", "0").equals("1")) {
							StringUtil.saveInfo(mainFrame, "isfirstLogin", "1");
							new AlertDialog.Builder(SplashActivity.this)
							.setTitle("版本介绍")
							.setMessage(updateInfo.getDescription().replaceAll("<br/>", "\n"))
							.setPositiveButton("确认",
									new DialogInterface.OnClickListener() {

										public void onClick(DialogInterface dialog,
												int whichButton) {
											rongToken();
											
										}
									}).show();
							
						}else{
							rongToken();
						}
						// 登录融云
					}

				} catch (NameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					sDB = db.getWritableDatabase();
					db.onUpgrade(sDB, 1, 2);
					toLogin();
				}
			} else {
				new AlertDialog.Builder(SplashActivity.this).setMessage("登录失败！")
						.setPositiveButton("确认", new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog, int whichButton) {
								toLogin();
							}
						}).show();
			}
		}

	}

	private void showUpdateDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle("请升级APP至版本" + updateInfo.getVersion());
		builder.setMessage(updateInfo.getDescription().replaceAll("<br/>", "\n"));
		builder.setCancelable(false);

		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
					downFile(updateInfo.getUrl()); // 在下面的代码段
				} else {
					Toast.makeText(SplashActivity.this, "SD卡不可用，请插入SD卡", Toast.LENGTH_SHORT).show();
				}
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				toLogin();
			}

		});
		builder.create().show();
	}

	void downFile(final String url) {
		pBar = new ProgressDialog(SplashActivity.this); // 进度条，在下载的时候实时更新进度，提高用户友好度
		pBar.setCancelable(false);
		pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pBar.setTitle("正在下载");
		pBar.setMessage("请稍候...");
		pBar.setProgress(0);
		pBar.show();
		new Thread() {
			public void run() {
				try {
					URL uri = new URL(url);
					HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
					conn.connect();
					int length = conn.getContentLength();
					InputStream is = conn.getInputStream();

					pBar.setMax(length); // 设置进度条的总长度
					FileOutputStream fileOutputStream = null;
					if (is != null) {
						File file = new File(Environment.getExternalStorageDirectory(), "ydt.apk");
						if (file.exists()) {
							file.delete();
						}
						if (!file.getParentFile().exists()) {
							file.getParentFile().mkdir();
						}
						fileOutputStream = new FileOutputStream(file);
						byte[] buf = new byte[1024]; // 这个是缓冲区，即一次读取10个比特，我弄的小了点，因为在本地，所以数值太大一
														// 下就下载完了，看不出progressbar的效果。
						int ch = -1;
						int process = 0;
						while ((ch = is.read(buf)) != -1) {
							fileOutputStream.write(buf, 0, ch);
							process += ch;
							pBar.setProgress(process); // 这里就是关键的实时更新进度了！
						}

					}
					fileOutputStream.flush();
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
					pBar.cancel();
					update();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}.start();
	}

	// void down() {
	// handler1.post(new Runnable() {
	// public void run() {
	// pBar.cancel();
	// update();
	// }
	// });
	// }

	// 安装文件，一般固定写法
	void update() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "ydt.apk")),
				"application/vnd.android.package-archive");
		startActivity(intent);
		SplashActivity.this.finish();
	}

	private void toLogin() {
		intent = new Intent(SplashActivity.this, LoginActivity.class);
		intent.putExtra("isLoad", true);
		startActivity(intent);
		SplashActivity.this.finish();
	}
}
