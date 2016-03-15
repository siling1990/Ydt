package com.wbja.stone.ydt;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.viewpagerindicator.TabPageIndicator;
import com.wbja.stone.ydt.adapter.TabStateAdapter;
import com.wbja.stone.ydt.entity.MRClinic;
import com.wbja.stone.ydt.entity.Patient;
import com.wbja.stone.ydt.entity.PatientUpload;
import com.wbja.stone.ydt.fragment.FragmentBs;
import com.wbja.stone.ydt.fragment.FragmentBzzd;
import com.wbja.stone.ydt.fragment.FragmentFy;
import com.wbja.stone.ydt.fragment.FragmentJyjc;
import com.wbja.stone.ydt.fragment.FragmentLczz;
import com.wbja.stone.ydt.fragment.FragmentZlff;
import com.wbja.stone.ydt.util.AlertDialogUtil;
import com.wbja.stone.ydt.util.Constants;
import com.wbja.stone.ydt.util.DB;
import com.wbja.stone.ydt.util.FileUtil;
import com.wbja.stone.ydt.util.HttpUtil;
import com.wbja.stone.ydt.util.StringUtil;
import com.wbja.stone.ydt.window.MyProgressDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class TabShowMRCActivity extends FragmentActivity {
	private Intent intent;
	private ImageView headMore;
	private String src;
	private EditText txtName;
	private TextView headTitle;
	private TextView txtNo;
	private DB db;
	private ViewPager pager;
	private FragmentStatePagerAdapter adapter;

	private MyProgressDialog m_customProgrssDialog;
	private UploadRecordTask uploadRecordTask;
	private UploadFileTask uploadFileTask;
	private DownloadFileTask downloadFileTask;
	private String msg;
	private String r;
	private int mrcId;
	private Map<String, String> map;

	private YdtApplication ydt;

	private String[] title = { "��ʷ", "�ٴ�֢״", "��֤���", "������", "���Ʒ���" };
	// ������������Ű�ťͼƬ
	private int imageArray[] = { R.drawable.tab_my_btn,
			R.drawable.tab_service_btn, R.drawable.tab_service_btn,
			R.drawable.tab_sche_btn, R.drawable.tab_group_btn,
			R.drawable.tab_doc_btn };
	private TabPageIndicator indicator ;
	
	private LinearLayout layDel;
	private Button btCancle, btDel;
	private CheckBox checkAll;

	private String mrcStr;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // �ޱ���
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab_clinic_record);
		mrcStr = StringUtil.getInfo(TabShowMRCActivity.this, "record", "");
		Log.d("******resordJSON*****", mrcStr);
		// ydt.getMrc() = GsonUtil.getObject(mrcStr, MRClinic.class);
		intent = getIntent();
		int id = intent.getIntExtra("MRCId", 0);
		db = new DB(TabShowMRCActivity.this);

		ydt = YdtApplication.getInstance();

		ydt.setMrc(db.queryMRCById(id));
			if(!StringUtil.isEmpty(ydt.getMrc().getSrc())){
				src = ydt.getMrc().getSrc();
			}else{
				String name = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
				String fileNmae = Environment.getExternalStorageDirectory().toString()
						+ File.separator + Constants.RECORDPATH;
				src= fileNmae + name + File.separator;
				ydt.getMrc().setSrc(src);
				db.updateMRC(ydt.getMrc());
			}
			
			StringUtil.saveInfo(TabShowMRCActivity.this, "addsrc", src);
		
		File srcFile = new File(src);
		if (!srcFile.exists()) {
			srcFile.mkdirs();
		}
		Fragment[] fArray = { new FragmentBs(), new FragmentLczz(),
				new FragmentBzzd(), new FragmentJyjc(), new FragmentZlff() };
		// FragmentPagerAdapter adapter = new TabAdapter(
		// getSupportFragmentManager(),title,null,fArray);
		adapter = new TabStateAdapter(getSupportFragmentManager(), title, null,
				fArray);

		// ��ͼ�л���
		pager = (ViewPager) findViewById(R.id.pager);
		pager.setOffscreenPageLimit(1);
		pager.setAdapter(adapter);

		// ҳ��ָʾ��
		indicator = (TabPageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(pager);
		headMore = (ImageView) findViewById(R.id.headMore);
		txtName = (EditText) findViewById(R.id.txtName);
		headTitle = (TextView) findViewById(R.id.headTitle);
		txtNo = (TextView) findViewById(R.id.txtNo);
		if (StringUtil.isEmpty(ydt.getMrc().getName_patient())) {
			headTitle.setText("�޸Ĳ���");
		} else {
			headTitle.setText("����������" + ydt.getMrc().getName_patient());
		}
		txtName.setText(ydt.getMrc().getTitle());
		// if (ydt.getMrc().getId() > 0) {
		// txtNo.setVisibility(View.VISIBLE);
		// txtNo.setEnabled(false);
		// txtNo.setText("��ţ�" + ydt.getMrc().getId());
		// }
		headMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ClPopWindow morePopWindow = new ClPopWindow(
						TabShowMRCActivity.this);
				morePopWindow.showPopupWindow(headMore);
			}
		});

		// ɾ��
		Constants.isCheckAll = false;
		Constants.isShow = false;
		layDel = (LinearLayout) findViewById(R.id.layDel);
		btCancle = (Button) findViewById(R.id.btCancle);
		btDel = (Button) findViewById(R.id.btDel);
		checkAll = (CheckBox) findViewById(R.id.checkAll);
		layDel.setVisibility(View.GONE);// ɾ�����ɼ�
		btCancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Constants.isShow = false;
				initUI();
			}
		});
		btDel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Constants.isShow = false;
				showCustomProgrssDialog("����ɾ��");
				FileUtil.delFile(ydt.getMrc().getSrc(), Constants.checkMap);
				initUI();
				hideCustomProgressDialog();
			}
		});
		checkAll.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean checked) {
				if (checked) {
					Constants.isCheckAll = true;
				} else {
					Constants.isCheckAll = false;
					if (Constants.checkMap != null) {
						Constants.checkMap.clear();
					}
				}
				initUI();
			}
		});
	}

	private void initUI() {
		if (Constants.isShow) {
			layDel.setVisibility(View.VISIBLE);// ɾ���ɼ�
		} else {
			layDel.setVisibility(View.GONE);// ɾ�����ɼ�
		}
		Fragment[] fArray = { new FragmentBs(), new FragmentLczz(),
				new FragmentJyjc(), new FragmentBzzd(), new FragmentZlff(),
				new FragmentFy() };
		adapter = new TabStateAdapter(getSupportFragmentManager(), title, null,
				fArray);
		pager.setAdapter(adapter);
		indicator.setViewPager(pager);
		indicator.setCurrentItem(0);
	}

	public void back(View view) {
		this.finish();
	}

	public void turn(View v) {
		intent = new Intent(TabShowMRCActivity.this, AddMediaActivity.class);
		intent.putExtra("src", src);
		ydt.getMrc().setIsEdit(1);
		
		switch (v.getId()) {
		case R.id.addJws:
			intent.setClass(TabShowMRCActivity.this, AddMediaSpinActivity.class);
			intent.putExtra("filter", Constants.JWS);
			intent.putExtra("title", "����ʷ");
			startActivity(intent);
			break;
		// case R.id.addbzfx:
		// intent.putExtra("filter", Constants.BZFX);
		// startActivity(intent);
		// break;
		case R.id.addfzjc:
			intent.setClass(TabShowMRCActivity.this, AddMediaJYJCActivity.class);
			intent.putExtra("filter", Constants.FZJC);
			intent.putExtra("title", "�������");
			startActivity(intent);
			break;
		case R.id.addjf:
			intent.putExtra("filter", Constants.JF);
			intent.putExtra("title", "����");
			startActivity(intent);
			break;
		case R.id.addjyfy:
			intent.putExtra("filter", Constants.JYFY);
			intent.putExtra("title", "���鷽ҩ");
			startActivity(intent);
			break;
		case R.id.addfm:
			intent.putExtra("filter", Constants.FM);
			intent.putExtra("title", "����");
			startActivity(intent);
			break;
		case R.id.addzc:
			intent.putExtra("filter", Constants.ZC);
			intent.putExtra("title", "�������");
			startActivity(intent);
			break;
		case R.id.addqtzl:
			intent.putExtra("filter", Constants.QTZL);
			intent.putExtra("title", "��������");
			startActivity(intent);
			break;
		// case R.id.addjyzh:
		// intent.putExtra("filter", Constants.JYZH);
		// startActivity(intent);
		// break;
		case R.id.addkxz:
			intent.putExtra("filter", Constants.KXZ);
			intent.putExtra("title", "����֢");
			startActivity(intent);
			break;
		case R.id.addmx:
			intent.putExtra("filter", Constants.MX);
			intent.putExtra("title", "����");
			startActivity(intent);
			break;
		case R.id.addsx:
			intent.putExtra("filter", Constants.SX);
			intent.putExtra("title", "����");
			startActivity(intent);
			break;
		case R.id.addtzjc:
			intent.putExtra("filter", Constants.TZJC);
			intent.putExtra("title", "�������");
			startActivity(intent);
			break;
		case R.id.addxbs:
			intent.setClass(TabShowMRCActivity.this, AddMediaSpinActivity.class);
			intent.putExtra("filter", Constants.XBS);
			intent.putExtra("title", "�ֲ�ʷ");
			startActivity(intent);
			break;
		case R.id.addxyzd:
			intent.putExtra("filter", Constants.XYZD);
			intent.putExtra("title", "��ҽ���");
			startActivity(intent);
			break;
		case R.id.addznf:
			intent.putExtra("filter", Constants.ZNF);
			intent.putExtra("title", "���ⷽ");
			startActivity(intent);
			break;
		case R.id.addzs:
			intent.putExtra("filter", Constants.ZS);
			intent.putExtra("title", "����");
			startActivity(intent);
			break;
		case R.id.addzyzd:
			intent.putExtra("filter", Constants.ZYZD);
			intent.putExtra("title", "��ҽ���");
			startActivity(intent);
			break;
		case R.id.addzzzf:
			intent.putExtra("filter", Constants.ZZZF);
			intent.putExtra("title", "�����η�");
			startActivity(intent);
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == Constants.NONE)
			return;
		// ������
		if (requestCode == Constants.SETPATIENT) {
			int id = data.getIntExtra("pNo", 0);
			if (id > 0) {
				Patient p = db.queryPatientById(id);
				ydt.getMrc().setId_patient(id);
				ydt.getMrc().setName_patient(p.getName_doctor());
				headTitle.setText(p.getName_doctor());
				ydt.getMrc().setId_patient(id);
				if (db.updateMRC(ydt.getMrc())) {
					AlertDialogUtil.showAlertDialog(TabShowMRCActivity.this,
							"�����ɹ���");
				} else {
					AlertDialogUtil.showAlertDialog(TabShowMRCActivity.this,
							"����ʧ�ܣ�");
				}

			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	// popwindow
	public class ClPopWindow extends PopupWindow {
		private View conentView;

		public ClPopWindow(Activity context) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			conentView = inflater.inflate(R.layout.popu_addr_layout, null);
			int h = context.getWindowManager().getDefaultDisplay().getHeight();
			int w = context.getWindowManager().getDefaultDisplay().getWidth();
			// ����SelectPicPopupWindow��View
			this.setContentView(conentView);
			// ����SelectPicPopupWindow��������Ŀ�
			this.setWidth(w / 3);
			// ����SelectPicPopupWindow��������ĸ�
			this.setHeight(LayoutParams.WRAP_CONTENT);
			// ����SelectPicPopupWindow��������ɵ��
			this.setFocusable(true);
			this.setOutsideTouchable(true);
			// ˢ��״̬
			this.update();
			// ʵ����һ��ColorDrawable��ɫΪ��͸��
			ColorDrawable dw = new ColorDrawable(0000000000);
			// ��back���������ط�ʹ����ʧ,������������ܴ���OnDismisslistener �����������ؼ��仯�Ȳ���
			this.setBackgroundDrawable(dw);
			// mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
			// ����SelectPicPopupWindow�������嶯��Ч��
			this.setAnimationStyle(R.style.AnimationPreview);
			LinearLayout setPatient = (LinearLayout) conentView
					.findViewById(R.id.setPatient);
			LinearLayout upRecord = (LinearLayout) conentView
					.findViewById(R.id.upRecord);
			TextView txtUp = (TextView) conentView
					.findViewById(R.id.txtUp);
			txtUp.setText("�ϴ�����");
			
			LinearLayout delFile = (LinearLayout) conentView
					.findViewById(R.id.deleteFile);
			LinearLayout downLoadFile = (LinearLayout) conentView
					.findViewById(R.id.downLoadFile);
			// ��������
			setPatient.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {

					ClPopWindow.this.dismiss();
					intent = new Intent(TabShowMRCActivity.this,
							PatientActivity.class);
					intent.putExtra("isSetPatient", 1);
					// ������ͼ��ʾΪ
					startActivityForResult(intent, Constants.SETPATIENT);

				}
			});
			// ��Ӳ���
			upRecord.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// mrcStr = StringUtil.getInfo(TabShowMRCActivity.this,
					// "record", "");
					Log.d("******resordJSON*****", mrcStr);
					// ydt.getMrc() = GsonUtil.getObject(mrcStr,
					// MRClinic.class);
					if (!StringUtil.isEmpty(StringUtil.getInfo(
							TabShowMRCActivity.this, "token", ""))) {
						ydt.getMrc().setTitle(txtName.getText().toString());
						if (ydt.getMrc().getId_patient() > 0) {// �ѹ�������
							if (ydt.getMrc().getId() > 0) {// �����Ѿ��ϴ�
								if (ydt.getMrc().getIsEdit() == 0) {// û���޸�
									AlertDialogUtil.showAlertDialog(
											TabShowMRCActivity.this,
											"δ���޸Ĳ����ظ��ϴ���");
								} else {// �޸���ֻ�ϴ��ļ�
									if (uploadFileTask != null
											&& uploadFileTask.getStatus() == AsyncTask.Status.RUNNING) {
										uploadFileTask.cancel(true); // ���Task�������У�����ȡ����
									}

									uploadFileTask = new UploadFileTask(
											TabShowMRCActivity.this,
											FileUtil.getFiles(ydt.getMrc()
													.getSrc(),
													Constants.FILEFILTER), ydt
													.getMrc());
									uploadFileTask.execute();
								}

							} else {// ����δ�ϴ�
								String json = StringUtil
										.getInfo(TabShowMRCActivity.this,
												"uploadRP", "");
								if (uploadRecordTask != null
										&& uploadRecordTask.getStatus() == AsyncTask.Status.RUNNING) {
									uploadRecordTask.cancel(true); // ���Task�������У�����ȡ����
								}

								uploadRecordTask = new UploadRecordTask(
										TabShowMRCActivity.this, ydt.getMrc());
								uploadRecordTask.execute();

							}
							ClPopWindow.this.dismiss();

						} else {
							AlertDialogUtil.showAlertDialog(
									TabShowMRCActivity.this, "δ�������ߣ�");
						}
					} else {
						new AlertDialog.Builder(TabShowMRCActivity.this)
								.setMessage("���ȵ�¼��")
								.setPositiveButton("ȷ��",
										new DialogInterface.OnClickListener() {

											public void onClick(
													DialogInterface dialog,
													int whichButton) {
												ClPopWindow.this.dismiss();
												intent = new Intent(
														TabShowMRCActivity.this,
														LoginActivity.class);
												startActivity(intent);
											}
										}).setNegativeButton("ȡ��", null).show();
					}

				}
			});

			downLoadFile.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!StringUtil.isEmpty(StringUtil.getInfo(
							TabShowMRCActivity.this, "token", ""))) {
						if (ydt.getMrc().getId() > 0) {

							if (downloadFileTask != null
									&& downloadFileTask.getStatus() == AsyncTask.Status.RUNNING) {
								downloadFileTask.cancel(true); // ���Task�������У�����ȡ����
							}

							downloadFileTask = new DownloadFileTask(
									TabShowMRCActivity.this, ydt.getMrc());
							downloadFileTask.execute();

						} else {
							AlertDialogUtil.showAlertDialog(
									TabShowMRCActivity.this, "δ�ϴ���");
						}

						ClPopWindow.this.dismiss();
					} else {
						new AlertDialog.Builder(TabShowMRCActivity.this)
								.setMessage("���ȵ�¼��")
								.setPositiveButton("ȷ��",
										new DialogInterface.OnClickListener() {

											public void onClick(
													DialogInterface dialog,
													int whichButton) {
												ClPopWindow.this.dismiss();
												intent = new Intent(
														TabShowMRCActivity.this,
														LoginActivity.class);
												startActivity(intent);
											}
										}).setNegativeButton("ȡ��", null).show();
					}
				}
			});

			delFile.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Constants.isShow = true;
					initUI();
					if (Constants.checkMap != null) {
						Constants.checkMap.clear();
					} else {
						Constants.checkMap = new HashMap<String, String>();
					}
					ClPopWindow.this.dismiss();
				}
			});

		}

		/**
		 * ��ʾpopupWindow
		 * 
		 * @param parent
		 */
		public void showPopupWindow(View parent) {
			if (!this.isShowing()) {
				// ��������ʽ��ʾpopupwindow
				this.showAsDropDown(parent, parent.getLayoutParams().width / 2,
						18);
			} else {
				this.dismiss();
			}
		}
	}

	// �˳���ǰ����
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getAction() != 1) {
			exit();
			return true;
		}

		return super.dispatchKeyEvent(event);
	}

	/*
	 * �˳�Ӧ�ó���
	 */
	private void exit() {
		new AlertDialog.Builder(TabShowMRCActivity.this).setMessage("ȷ���˳�������")
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int whichButton) {
						ydt.getMrc().setTitle(txtName.getText().toString());
						db.updateMRC(ydt.getMrc());
						ydt.setMrc(null);
						StringUtil.saveInfo(TabShowMRCActivity.this, "addsrc",
								"");
						finish();
					}
				}).setNegativeButton("ȡ��", null).show();
	}

	/**
	 * �첽����
	 * */
	public class UploadRecordTask extends AsyncTask<Integer, String, Integer> {
		private MRClinic re;

		public UploadRecordTask(Context mainFrame, MRClinic re) {
			this.re = re;
		}

		@Override
		protected void onCancelled() {
			hideCustomProgressDialog();
			super.onCancelled();
		}

		@Override
		protected Integer doInBackground(Integer... params) {
			// ��װ����
			map = new HashMap<String, String>();
			map.put("id_dept", StringUtil.getInfo(TabShowMRCActivity.this,
					Constants.DEPTID, ""));
			map.put("name_dept", StringUtil.getInfo(TabShowMRCActivity.this,
					Constants.DEPTNAME, ""));
			map.put("id_hospital", StringUtil.getInfo(TabShowMRCActivity.this,
					Constants.HOSPID, ""));
			map.put("name_hospital", StringUtil.getInfo(
					TabShowMRCActivity.this, Constants.HOSPNAME, ""));
			map.put("id_patient", String.valueOf(re.getId_patient()));
			map.put("name_patient", re.getName_patient());
			map.put("name_user", StringUtil.getInfo(TabShowMRCActivity.this,
					Constants.DOCNAME, ""));
			map.put("title", re.getTitle());
			map.put("jiuzhenshijian", re.getJiuzhenshijian());
			map.put("kexiazheng", re.getKexiazheng());
			// map.put("jiwangshi", re.getJiwangshi());
			// map.put("tizhengjiancha", txtjyjc.getText().toString());
			map.put("zhongyizhenduan",re.getZhongyizhenduan());
			map.put("xiyizhenduan",re.getXiyizhenduan());
			map.put("bingming",re.getBingming());
			// map.put("zhizezhifa", txtzlff.getText().toString());
			// map.put("fangjizucheng", txtfy.getText().toString());
			msg = "";
			String result = HttpUtil.doPostFileForm(map, Constants.ADDMRCLINIC,
					false, TabShowMRCActivity.this, null);
			if (StringUtil.isEmpty(msg)) {
				JSONObject myJsonObject = null;
				try {
					myJsonObject = new JSONObject(result);
					// msg=myJsonObject.getString("r");
					r = myJsonObject.getString("r");
					msg = myJsonObject.getString("msg");

					if (!StringUtil.isEmpty(r) && r.equals("ok")) {
						// Log.d("*******Token*****", );
						mrcId = myJsonObject.getInt("mrcId");

						return Constants.SUCCESS;
					}
				} catch (Exception e) {
					msg = e.getMessage();
				}

			}
			return Constants.FAILURE;
		}

		@Override
		protected void onPreExecute() {
			showCustomProgrssDialog("�����ϴ�����...");
		}

		@Override
		protected void onPostExecute(Integer result) {
			// ����a-z��������Դ����
			hideCustomProgressDialog();
			if (result == Constants.SUCCESS) {
				ydt.getMrc().setId(mrcId);

				txtName.setText(ydt.getMrc().getTitle());
				txtNo.setVisibility(View.VISIBLE);
				txtNo.setEnabled(false);
				txtNo.setText("��ţ�" + ydt.getMrc().getId());
				ydt.getMrc().setIsEdit(0);// �ϴ�֮������δ�༭
				db.updateMRC(ydt.getMrc());
				if (uploadFileTask != null
						&& uploadFileTask.getStatus() == AsyncTask.Status.RUNNING) {
					uploadFileTask.cancel(true); // ���Task�������У�����ȡ����
				}

				uploadFileTask = new UploadFileTask(TabShowMRCActivity.this,
						FileUtil.getFiles(ydt.getMrc().getSrc(),
								Constants.FILEFILTER), ydt.getMrc());
				uploadFileTask.execute();
			} else {
				AlertDialogUtil.showAlertDialog(TabShowMRCActivity.this, msg);
			}

		}

	}

	/**
	 * �첽�����ϴ��ļ�
	 * */
	public class UploadFileTask extends AsyncTask<Integer, String, Integer> {
		private File[] uploadFiles;
		private MRClinic re;
		int total = 0;
		int up = 0;

		public UploadFileTask(Context mainFrame, File[] uploadFiles, MRClinic re) {
			this.uploadFiles = uploadFiles;
			this.re = re;
		}

		@Override
		protected void onCancelled() {
			hideCustomProgressDialog();
			super.onCancelled();
		}

		@Override
		protected Integer doInBackground(Integer... params) {
			// ��װ����
			Map<String, String> map = new HashMap<String, String>();
			map.put("id_patient", String.valueOf(re.getId_patient()));
			map.put("id_mrclinic", String.valueOf(re.getId()));
			map.put("fujianzubie", "mzbl");

			// JSONObject myJsonObject = null;
			String result;
			for (int i = 0; i < uploadFiles.length; i++) {
				if (!uploadFiles[i].getName().contains("-upload")) {// δ�ϴ�

					total++;
					map.put("fujianleixing", uploadFiles[i].getName()
							.split("-")[0]);
					result = HttpUtil.doPostFileForm(map, Constants.ADDATTACH,
							false, TabShowMRCActivity.this, uploadFiles[i]);

					if (!StringUtil.isEmpty(result) && result.equals("ok")) {
						up++;
						String newpath = uploadFiles[i].getAbsolutePath();
						newpath = newpath.replaceFirst("-",
								Constants.FILEFILTER);
						Log.d("**********Rename file****", newpath);
						uploadFiles[i].renameTo(new File(newpath));
						uploadFiles[i].delete();
					}
					// try {
					// myJsonObject = new JSONObject(result);
					// r = myJsonObject.getString("r");
					// msg = myJsonObject.getString("msg");
					//
					// if (!StringUtil.isEmpty(r) && r.equals("ok")) {
					// up++;
					// String newpath=uploadFiles[i].getAbsolutePath();
					// newpath=newpath.replaceFirst("-", Constants.FILEFILTER);
					// Log.d("**********Rename file****", newpath);
					// uploadFiles[i].renameTo(new File(newpath));
					// uploadFiles[i].delete();
					// }
					// } catch (JSONException e) {
					// msg = e.getMessage();
					// }
				}

			}

			if (total == up) {
				return Constants.SUCCESS;
			}

			return Constants.FAILURE;
		}

		@Override
		protected void onPreExecute() {
			showCustomProgrssDialog("�����ϴ�����...");
		}

		@Override
		protected void onPostExecute(Integer result) {

			initUI();
			hideCustomProgressDialog();
			Toast.makeText(
					TabShowMRCActivity.this,
					"����" + total + "���ļ����ɹ��ϴ�" + up + "����ʧ��" + (total - up)
							+ "��", Toast.LENGTH_SHORT).show();
			if (result == Constants.SUCCESS) {
				ydt.getMrc().setIsEdit(0);// �ϴ�֮������δ�༭
				db.updateMRC(ydt.getMrc());
			} else {
				// AlertDialogUtil.showAlertDialog(TabShowMRCActivity.this,
				// msg);
			}

		}

	}

	/**
	 * �첽���ظ���
	 * */
	public class DownloadFileTask extends AsyncTask<Integer, String, Integer> {
		private MRClinic re;

		// int total = 0;
		// int up = 0;

		public DownloadFileTask(Context mainFrame,
				MRClinic re) {
			this.re = re;
		}

		@Override
		protected void onCancelled() {
			hideCustomProgressDialog();
			super.onCancelled();
		}

		@Override
		protected Integer doInBackground(Integer... params) {
			// ��װ����
			Map<String, String> map = new HashMap<String, String>();
			map.put("id_mrclinic", String.valueOf(re.getId()));
			map.put("fujianzubie", "mzbl");
			String result;
			// for (int i = 0; i < uploadFiles.length; i++) {
			// if (!uploadFiles[i].getName().contains("-upload")) {// δ�ϴ�
			//
			// total++;
			// map.put("fujianleixing", uploadFiles[i].getName()
			// .split("-")[0]);
			// result = HttpUtil.doPostFileForm(map, Constants.ADDATTACH,
			// false, TabShowMRCActivity.this, uploadFiles[i]);
			//
			// if (!StringUtil.isEmpty(result) && result.equals("ok")) {
			// up++;
			// String newpath = uploadFiles[i].getAbsolutePath();
			// newpath = newpath.replaceFirst("-",
			// Constants.FILEFILTER);
			// Log.d("**********Rename file****", newpath);
			// uploadFiles[i].renameTo(new File(newpath));
			// uploadFiles[i].delete();
			// }
			// }
			//
			// }
			result = HttpUtil.doPostFileForm(map, Constants.GETATTACHLIST, false,
					TabShowMRCActivity.this, null);
			Log.d("*****������Ϣ****", result);
			//

			// if (total == up) {
			// return Constants.SUCCESS;
			// }

			return Constants.FAILURE;
		}

		@Override
		protected void onPreExecute() {
			showCustomProgrssDialog("�������ظ���...");
		}

		@Override
		protected void onPostExecute(Integer result) {

			initUI();
			hideCustomProgressDialog();
		}

	}

	void showCustomProgrssDialog(String msg) {

		if (null == m_customProgrssDialog)

			m_customProgrssDialog = MyProgressDialog

			.createProgrssDialog(TabShowMRCActivity.this);

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
