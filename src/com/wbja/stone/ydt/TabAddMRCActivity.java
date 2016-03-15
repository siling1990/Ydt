package com.wbja.stone.ydt;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.viewpagerindicator.TabPageIndicator;
import com.wbja.stone.ydt.adapter.TabStateAdapter;
import com.wbja.stone.ydt.entity.MRClinic;
import com.wbja.stone.ydt.entity.PatientUpload;
import com.wbja.stone.ydt.fragment.FragmentBs;
import com.wbja.stone.ydt.fragment.FragmentBzzd;
import com.wbja.stone.ydt.fragment.FragmentJyjc;
import com.wbja.stone.ydt.fragment.FragmentLczz;
import com.wbja.stone.ydt.fragment.FragmentZlff;
import com.wbja.stone.ydt.util.Constants;
import com.wbja.stone.ydt.util.DB;
import com.wbja.stone.ydt.util.DateUtil;
import com.wbja.stone.ydt.util.StringUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.LinearLayout.LayoutParams;

public class TabAddMRCActivity extends FragmentActivity {
	private Intent intent;
	private ImageView headMore;
	private String src;
	private EditText txtName;
	private DB db;
	private ViewPager pager;
	private FragmentStatePagerAdapter adapter;
	private int mrc_id;
	
	private YdtApplication ydt;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // �ޱ���
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab_clinic_record);
		intent=getIntent();
		mrc_id=intent.getIntExtra("MRCId", 0);
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			// �ڴ濨���ڽ������²���
			String name = new SimpleDateFormat("yyyyMMddhhmmss")
					.format(new Date());
			String fileNmae = Environment.getExternalStorageDirectory()
					.toString() + File.separator + Constants.RECORDPATH;
			src = fileNmae + name + File.separator;
			StringUtil.saveInfo(TabAddMRCActivity.this, "addsrc", src);
		}

		File srcFile = new File(src);
		if (!srcFile.exists()) {
			srcFile.mkdirs();
		}
		
//		item = new Record();
//		item.setSrc(src);
//		item.setEdit(0);// ����Ϊδ�༭
//		item.setCreateTime(DateUtil.dateToString(new Date(), "yyyy-MM-dd"));
		ydt=YdtApplication.getInstance();
		ydt.setMrc(new MRClinic());
		ydt.getMrc().setId_user(Integer.parseInt(StringUtil.getID_User(TabAddMRCActivity.this)));
		ydt.getMrc().setSrc(src);
		ydt.getMrc().setId_chuzhen(mrc_id);
		ydt.getMrc().setIsEdit(0);// ����Ϊδ�༭
		ydt.getMrc().setDianxingyian(-1);
		ydt.getMrc().setLiaoxiaozhuangtai(-1);
		ydt.getMrc().setJiuzhenshijian(DateUtil.dateToString(new Date(), "yyyy-MM-dd"));
		//StringUtil.saveInfo(TabAddMRCActivity.this, "mrc", GsonUtil.getJsonValue(ydt.getMrc()));
		
		db = new DB(TabAddMRCActivity.this);

		String[] title = { "��ʷ", "�ٴ�֢״", "��֤���","������",  "���Ʒ���" };
		// ������������Ű�ťͼƬ
		int imageArray[] = { R.drawable.tab_my_btn, R.drawable.tab_service_btn,
				R.drawable.tab_service_btn, R.drawable.tab_sche_btn,
				R.drawable.tab_group_btn, R.drawable.tab_doc_btn };
		Fragment[] fArray = { new FragmentBs(), new FragmentLczz(),
				 new FragmentBzzd(), new FragmentJyjc(),new FragmentZlff() };
		// FragmentPagerAdapter adapter = new TabAdapter(
		// getSupportFragmentManager(),title,null,fArray);
		adapter = new TabStateAdapter(getSupportFragmentManager(), title, null,
				fArray);

		// ��ͼ�л���
		pager = (ViewPager) findViewById(R.id.pager);
		pager.setOffscreenPageLimit(1);
		pager.setAdapter(adapter);

		// ҳ��ָʾ��
		TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(pager);
		headMore = (ImageView) findViewById(R.id.headMore);
		txtName = (EditText) findViewById(R.id.txtName);
		headMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ClPopWindow morePopWindow = new ClPopWindow(TabAddMRCActivity.this);
				morePopWindow.showPopupWindow(headMore);
			}
		});
	}

	public void back(View view) {
		this.finish();
	}

	public void turn(View v) {
		intent = new Intent(TabAddMRCActivity.this, AddMediaActivity.class);
		intent.putExtra("src", src);
		switch (v.getId()) {
		case R.id.addJws:
			intent.setClass(TabAddMRCActivity.this, AddMediaSpinActivity.class);
			intent.putExtra("filter", Constants.JWS);
			intent.putExtra("title", "����ʷ");
			startActivity(intent);
			break;
//		case R.id.addbzfx:
//			intent.putExtra("filter", Constants.BZFX);
//			startActivity(intent);
//			break;
		case R.id.addfzjc:
			intent.setClass(TabAddMRCActivity.this, AddMediaJYJCActivity.class);
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
//		case R.id.addjyzh:
//			intent.putExtra("filter", Constants.JYZH);
//			startActivity(intent);
//			break;
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
			intent.setClass(TabAddMRCActivity.this, AddMediaSpinActivity.class);
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
				//item.setpNo(id);
				PatientUpload p=db.queryPatientUpByID(id);
				ydt.getMrc().setId_patient(id);
				ydt.getMrc().setName_patient(p.getName());
				ydt.getMrc().setTitle(txtName.getText().toString());
				new AlertDialog.Builder(TabAddMRCActivity.this)
				.setMessage("�����ɹ�")
				.setPositiveButton("ȷ��",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int whichButton) {
								
								db.insertMRC(ydt.getMrc());
								ydt.setMrc(null);
								finish();
							}
						}).show();
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
			conentView = inflater.inflate(R.layout.popu_layout, null);
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
			LinearLayout addRecord = (LinearLayout) conentView
					.findViewById(R.id.addRecord);
			// ��������
			setPatient.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					ClPopWindow.this.dismiss();
					intent = new Intent(TabAddMRCActivity.this, PatientActivity.class);
					intent.putExtra("isSetPatient", 1);
					// ������ͼ��ʾΪ
					startActivityForResult(intent, Constants.SETPATIENT);

				}
			});
			// ��Ӳ���
			addRecord.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(ydt.getMrc()!=null){
						ydt.getMrc().setTitle(txtName.getText().toString());
					}
				if(	db.insertMRC(ydt.getMrc())){
					new AlertDialog.Builder(TabAddMRCActivity.this)
					.setMessage("��ӳɹ�")
					.setPositiveButton("ȷ��",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int whichButton) {
									StringUtil.saveInfo(TabAddMRCActivity.this, "addsrc", "");
									ydt.setMrc(null);
									ClPopWindow.this.dismiss();
									TabAddMRCActivity.this.finish();
								}
							}).show();
				}
					
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
			checkForSave();
			return true;
		}

		return super.dispatchKeyEvent(event);
	}

	/*
	 * �˳�Ӧ�ó���
	 */
	private void exit(){
		new AlertDialog.Builder(TabAddMRCActivity.this)
		.setMessage("ȷ���˳���")
		.setPositiveButton("ȷ��",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog,
							int whichButton) {
						finish();
					}
				})
		.setNegativeButton("ȡ��",null).show();
	}
	private void checkForSave() {
		if(ydt.getMrc()!=null){
			ydt.getMrc().setTitle(txtName.getText().toString());
			if (ydt.getMrc().getId_patient() == 0) {
				new AlertDialog.Builder(TabAddMRCActivity.this)
						.setMessage("�Ƿ��������")
						.setPositiveButton("��������",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int whichButton) {
										intent = new Intent(TabAddMRCActivity.this,
												PatientActivity.class);
										intent.putExtra("isSetPatient", 1);
										// ������ͼ��ʾΪ
										startActivityForResult(intent,
												Constants.SETPATIENT);
									}
								})
						.setNegativeButton("ȡ������",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int whichButton) {
										//db.insertRecord(item);
										db.insertMRC(ydt.getMrc());
										StringUtil.saveInfo(TabAddMRCActivity.this,
												"addsrc", "");
										finish();
									}
								}).show();
			} else {
				db.insertMRC(ydt.getMrc());
			}
		}
	}
}
