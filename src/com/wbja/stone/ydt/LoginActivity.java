package com.wbja.stone.ydt;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient.ConnectCallback;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.model.UserInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wbja.stone.ydt.entity.Friend;
import com.wbja.stone.ydt.util.AlertDialogUtil;
import com.wbja.stone.ydt.util.Constants;
import com.wbja.stone.ydt.util.DB;
import com.wbja.stone.ydt.util.HttpUtil;
import com.wbja.stone.ydt.util.StringUtil;
import com.wbja.stone.ydt.window.MyProgressDialog;
import com.wbja.stone.ydt.LoginActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
* @ClassName: LoginActivity
* @Description: TODO(������һ�仰��������������)
* @author Stone
* @date 2015-7-8 ����3:25:44
*
*/ 
public class LoginActivity extends Activity {
	private MyProgressDialog m_customProgrssDialog;
	private EditText eduname;
	private EditText edpwd;
	private Button btLogin,btReg;
	private MainFrameTask mMainFrameTask;
	private RongTokenTask rongTokenTask;
	private FriendListTask friendListTask;
	private String msg;
	
	private String id=null;
	private String docname=null;
	private String usertype=null;
	private String displayname=null;
	private String hospid =null;
	private String hospname =null;
	private String idDept=null;
	private String nameDept=null;
	private String rongid=null;
	private String token=null;
	
	private Intent intent;
	private boolean isLoad;
	private DB db;
	
	private Gson gson;
	private List<Friend>friList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		intent=getIntent();
		
		isLoad=intent.getBooleanExtra("isLoad", false);
		eduname=(EditText)findViewById(R.id.eduname);
		edpwd=(EditText)findViewById(R.id.edpwd);
		btLogin=(Button)findViewById(R.id.btLogin);
		btReg=(Button)findViewById(R.id.btReg);
		
	}
	public void back(View view){
		this.finish();
	}
	public void login(View view){
		if(check()){
			if (mMainFrameTask != null && mMainFrameTask.getStatus() == AsyncTask.Status.RUNNING) {
				mMainFrameTask.cancel(true); // ���Task�������У�����ȡ����
				}
			mMainFrameTask = new MainFrameTask(this);
			mMainFrameTask.execute();
		}
		
	}
	
	public void reg(View view){
		intent=new Intent(LoginActivity.this,RegActivity.class);
		startActivity(intent);
		
	}
	
	private boolean check(){
		if(eduname.getText().toString().isEmpty()){
			AlertDialogUtil.showAlertDialog(LoginActivity.this,
					"�������û�����");
			return false;
		}
		if(edpwd.getText().toString().isEmpty()){
			AlertDialogUtil.showAlertDialog(LoginActivity.this,
					"���������룡");
			return false;
		}
		
		return true;
	}
	/**
	 * �첽����
	 * */
	public class MainFrameTask extends AsyncTask<Integer, String, Integer> {
		private Context mainFrame = null;

		public MainFrameTask(Context mainFrame) {
			this.mainFrame = mainFrame;
		}

		@Override
		protected void onCancelled() {
			hideCustomProgressDialog();
			super.onCancelled();
		}

		@Override
		protected Integer doInBackground(Integer... params) {
			//TODO
			//��װ����
			Map<String,String> map=new HashMap<String,String>();
			map.put("username", eduname.getText().toString());
			map.put("pwd", edpwd.getText().toString());
			String result=HttpUtil.doPostForm(map, Constants.LOGIN,true,LoginActivity.this);
			JSONObject myJsonObject=null;
			try {
				myJsonObject = new JSONObject(result);
				token=myJsonObject.getString(Constants.TOKEN);
				id=myJsonObject.getString(Constants.USERID);
				msg=myJsonObject.getString("r");
				
				docname=myJsonObject.getString(Constants.DOCNAME);
				usertype=myJsonObject.getString(Constants.USERTYPERE);
				displayname=myJsonObject.getString(Constants.DISPLAYNAME);
				hospname=myJsonObject.getString(Constants.HOSPNAME);
				hospid=myJsonObject.getString(Constants.HOSPID);
				idDept=myJsonObject.getString(Constants.DEPTID);
				nameDept=myJsonObject.getString(Constants.DEPTNAME);
				
				//����idʹ���û�id
				rongid=id;//myJsonObject.getString("username");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				token="";
				msg="��¼ʧ�ܣ�";
			}
			if(!token.isEmpty()){
				Log.d("*******Token*****", token);
				return Constants.SUCCESS;
			}
			
			return Constants.FAILURE;
		}

		@Override
		protected void onPreExecute() {
			showCustomProgrssDialog("���ڼ���...");
		}

		@Override
		protected void onPostExecute(Integer result) {
			// ����a-z��������Դ����
			hideCustomProgressDialog();
			if(result==Constants.SUCCESS){
				if(usertype.equals(Constants.USERTYPE)){
					StringUtil.saveInfo(LoginActivity.this, Constants.TOKEN, token);
					StringUtil.saveInfo(LoginActivity.this, Constants.USERID, id);
					StringUtil.saveInfo(LoginActivity.this, Constants.RONGID, rongid);
					
					StringUtil.saveInfo(LoginActivity.this, Constants.DOCNAME, docname);
					StringUtil.saveInfo(LoginActivity.this, Constants.USERTYPERE, usertype);
					StringUtil.saveInfo(LoginActivity.this,Constants.DISPLAYNAME, displayname);
					StringUtil.saveInfo(LoginActivity.this, Constants.HOSPNAME, hospname);
					StringUtil.saveInfo(LoginActivity.this, Constants.HOSPID, hospid);
					StringUtil.saveInfo(LoginActivity.this, Constants.DEPTID, idDept);
					StringUtil.saveInfo(LoginActivity.this, Constants.DEPTNAME, nameDept);
					
					StringUtil.saveInfo(LoginActivity.this,Constants.USERNAME, eduname.getText().toString());
					StringUtil.saveInfo(LoginActivity.this, "password", edpwd.getText().toString());
					if (friendListTask != null && friendListTask.getStatus() == AsyncTask.Status.RUNNING) {
						friendListTask.cancel(true); // ���Task�������У�����ȡ����
						}
					friendListTask = new FriendListTask(LoginActivity.this);
					friendListTask.execute();
				}else{
					AlertDialogUtil.showAlertDialog(LoginActivity.this,"����ע��Ϊ����,��ͨ��һ��ͨ��¼");
				}
				
			}else{
					AlertDialogUtil.showAlertDialog(LoginActivity.this,
							msg);
			}
			
		}

	}
	
	/**
	 * ��ȡ����token
	 * */
	
	public class RongTokenTask extends AsyncTask<Integer, String, Integer> {
		private Context mainFrame = null;

		public RongTokenTask(Context mainFrame) {
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
			// ��װ����
			Map<String, String> map = new HashMap<String, String>();
			map.put("userId",StringUtil.getInfo(LoginActivity.this, Constants.RONGID, rongid));
			map.put("name", StringUtil.getInfo(LoginActivity.this, "docname", eduname.getText().toString()));
			map.put("portraitUri", "http://www.touxiang.cn/uploads/20131114/14-065802_226.jpg");
			
			String result = HttpUtil.doPostFormToken(map,Constants.RONGURI,LoginActivity.this);
			
			JSONObject myJsonObject=null;
			String token=null;
			String id=null;
			try {
				myJsonObject = new JSONObject(result);
				token=myJsonObject.getString(Constants.TOKEN);
				id=myJsonObject.getString("userId");
				msg="��¼�ɹ�";
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				token="";
				msg="��¼�쳣";
			}
			
			if(!StringUtil.isEmpty(token)){
				StringUtil.saveInfo(LoginActivity.this,  Constants.RONGTOKEN, token);
				return Constants.SUCCESS;
			}

			return Constants.FAILURE;
		}

		@Override
		protected void onPreExecute() {
			showCustomProgrssDialog("���ڼ���...");
		}

		@Override
		protected void onPostExecute(Integer result) {
			// ����a-z��������Դ����
				if (result == Constants.SUCCESS) {
					RongIM.connect(StringUtil.getInfo(LoginActivity.this,  Constants.RONGTOKEN, ""), new ConnectCallback() {
						
						@Override
						public void onSuccess(String arg0) {
							// TODO Auto-generated method stub
							hideCustomProgressDialog();
							Log.d("*******�������Ƴɹ�****", "");
							new AlertDialog.Builder(LoginActivity.this)
							.setMessage("��¼�ɹ���")
							.setPositiveButton("ȷ��",
									new DialogInterface.OnClickListener() {

										public void onClick(DialogInterface dialog,
												int whichButton) {
											if(isLoad){
												intent = new Intent(LoginActivity.this, MainTabActivity.class);
												startActivity(intent);
											}
											finish();
											
										}
									}).show();
							//Toast.makeText(LoginActivity.this, "���ӳɹ�", Toast.LENGTH_SHORT).show();
						}
						
						@Override
						public void onError(ErrorCode arg0) {
							// TODO Auto-generated method stub
							hideCustomProgressDialog();
							Log.d("*******��������ʧ��****", "����"+arg0.getMessage());
							AlertDialogUtil.showAlertDialog(LoginActivity.this, "��¼ʧ��");
							//Toast.makeText(LoginActivity.this, "����"+arg0.getMessage(), Toast.LENGTH_SHORT).show();
						}
						
						@Override
						public void onTokenIncorrect() {
							// TODO Auto-generated method stub
							hideCustomProgressDialog();
							Log.d("*******��������ʧ��****", "Token����");
							AlertDialogUtil.showAlertDialog(LoginActivity.this,  "��¼ʧ��");
							//Toast.makeText(LoginActivity.this, "Token����", Toast.LENGTH_SHORT).show();
						}
					});
				} else {
					hideCustomProgressDialog();
					AlertDialogUtil.showAlertDialog(LoginActivity.this, msg);
				}

			}
			

	}
	
	/**
	 * �����б�
	 * */
	public class FriendListTask extends AsyncTask<Integer, String, Integer> {
		private Context mainFrame = null;

		public FriendListTask(Context mainFrame) {
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
			// ��װ����
			Map<String, String> map = new HashMap<String, String>();
			
			String result = HttpUtil.doPostForm(map,Constants.FRIENDLIST,false,LoginActivity.this);
			
			JSONObject myJsonObject=null;
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
			// ����a-z��������Դ����
				if (result == Constants.SUCCESS) {
					
					if(friList.size()>0){
						db=new DB(LoginActivity.this);
						db.deleteFriend();
						for(int i=0;i<friList.size();i++){
							if(StringUtil.getInfo(LoginActivity.this, Constants.USERID,"0").equals(friList.get(i).getId_user())){
								RongIM.getInstance().refreshUserInfoCache(new UserInfo( friList.get(i).getId_friend(), friList.get(i).getDisplayname_friend(),null));
							}
							else{
								RongIM.getInstance().refreshUserInfoCache(new UserInfo( friList.get(i).getId_user(), friList.get(i).getDisplayname_user(),null));
							}
							db.insertFriend(friList.get(i));
						}
					}
					hideCustomProgressDialog();
					
					
				} else {
					hideCustomProgressDialog();
					AlertDialogUtil.showAlertDialog(LoginActivity.this, msg);
				}
				if (rongTokenTask != null && rongTokenTask.getStatus() == AsyncTask.Status.RUNNING) {
					rongTokenTask.cancel(true); // ���Task�������У�����ȡ����
					}
				rongTokenTask = new RongTokenTask(LoginActivity.this);
				rongTokenTask.execute();

			}
			

	}
	
	void showCustomProgrssDialog(String msg) {
		if (null == m_customProgrssDialog)
			m_customProgrssDialog = MyProgressDialog
			.createProgrssDialog(LoginActivity.this);
		if (null != m_customProgrssDialog) {
			m_customProgrssDialog.setMessage(msg);
			m_customProgrssDialog.show();
			m_customProgrssDialog.setCancelable(true);
		}
	}
	void hideCustomProgressDialog() {
		if (null != m_customProgrssDialog) {
			m_customProgrssDialog.dismiss();
			m_customProgrssDialog = null;
		}
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		eduname=(EditText)findViewById(R.id.eduname);
		eduname.setText(StringUtil.getInfo(LoginActivity.this, "username", ""));
	}
}
