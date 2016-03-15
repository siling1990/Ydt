package com.wbja.stone.ydt;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.wbja.stone.ydt.entity.PatientUpload;
import com.wbja.stone.ydt.util.AlertDialogUtil;
import com.wbja.stone.ydt.util.Constants;
import com.wbja.stone.ydt.util.DB;
import com.wbja.stone.ydt.util.DateUtil;
import com.wbja.stone.ydt.util.GsonUtil;
import com.wbja.stone.ydt.util.HttpUtil;
import com.wbja.stone.ydt.util.StringUtil;
import com.wbja.stone.ydt.window.MyProgressDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class PatientInfoActivity extends Activity {

	private TextView headTitle;
	private PatientUpload patient;
	private TextView pName;
	private ImageView back, pPhoto;
	private MyProgressDialog m_customProgrssDialog;
	private UploadPatientTask uploadPatientTask;
	private String msg;
	private String r;
	private DB db;
	private int pNo;
	private StringBuilder strB;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patient_info);

		back = (ImageView) findViewById(R.id.headBack);
		back.setVisibility(View.VISIBLE);
		
		
		headTitle = (TextView) findViewById(R.id.headTitle);
		headTitle.setText("���߲���");
		pName = (TextView) findViewById(R.id.pName);

		pPhoto = (ImageView) findViewById(R.id.pPhoto);

		String json = StringUtil.getInfo(PatientInfoActivity.this,
				"patientInfo", "[]");
		strB = new StringBuilder();
		Log.d("****Activity PatientInfo****", json);
		patient = GsonUtil.getObject(json, PatientUpload.class);
		if (patient == null || patient.getName().isEmpty()) {
			pName.setText("û�л�����Ϣ��");
		} else {
			if(patient.getId()==0){
				strB.append("����������").append(patient.getName()).append("\t\t�Ա�")
				.append(patient.getGender());
			}else{
				strB.append("����������").append(patient.getName()).append("\t\t�Ա�")
				.append(patient.getGender()).append("\t\t����ID:").append(patient.getId());
			}
		}
		pName.setText(strB);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;
		if (!StringUtil.isEmpty(patient.getIdPhotoPath())) {
			Log.d("**********IMAGEPATH**********", patient.getIdPhotoPath());
			Bitmap bm = BitmapFactory.decodeFile(patient.getIdPhotoPath(),
					options);
			if (null != bm) {
				pPhoto.setImageBitmap(bm);
			}
		}
	}

	public void back(View view) {
		this.finish();
	}

	public void uploadPatient(View view) {
		if(patient.getIsUpdate()==0){
			if (uploadPatientTask != null && uploadPatientTask.getStatus() == AsyncTask.Status.RUNNING) {
				uploadPatientTask.cancel(true); // ���Task�������У�����ȡ����
				}
			
			uploadPatientTask = new UploadPatientTask(this,patient);
			uploadPatientTask.execute();
		}else{
			AlertDialogUtil.showAlertDialog(PatientInfoActivity.this, "���ϴ��������ظ��ϴ�");
		}
		
	}

	void showCustomProgrssDialog(String msg) {

		if (null == m_customProgrssDialog)

			m_customProgrssDialog = MyProgressDialog

			.createProgrssDialog(PatientInfoActivity.this);

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

	/**
	 * �첽����
	 * */
	public class UploadPatientTask extends AsyncTask<Integer, String, Integer> {
		private Context mainFrame = null;
		private PatientUpload p;

		public UploadPatientTask(Context mainFrame, PatientUpload p) {
			this.mainFrame = mainFrame;
			this.p = p;
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
			String dateStr=p.getDate_birth();
			Date form=null;
			try {
				form = DateUtil.stringToDate(dateStr,"yyyy-MM-dd");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
			}
			
			map.put("age", String.valueOf(DateUtil.yearsBetween(form, new Date())));
			map.put("gender", p.getGender());
			map.put("name", p.getName());
			map.put("nation", p.getNation());
			map.put("occupation", p.getOccupation());
			map.put("genrenshi", p.getGerenshi());
			map.put("idcard", p.getIdcard());
			map.put("date_birth", p.getDate_birth());
			
			String result = HttpUtil.doPostForm(map, Constants.ADDPATIENT, false,
					PatientInfoActivity.this);
			if(StringUtil.isEmpty(result)){
				msg="���ӷ�������ʱ����ȷ�����糩ͨ";
				return Constants.FAILURE;
			}
			JSONObject myJsonObject = null;
			try {
				myJsonObject = new JSONObject(result);
				// msg=myJsonObject.getString("r");
				r=myJsonObject.getString("r");
				msg=myJsonObject.getString("msg");
				
				if (!StringUtil.isEmpty(r)&&r.equals("ok")) {
					// Log.d("*******Token*****", );
					pNo=myJsonObject.getInt("patientId");
					
					return Constants.SUCCESS;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				msg=e.getMessage();
			}

			return Constants.FAILURE;
		}

		@Override
		protected void onPreExecute() {
			showCustomProgrssDialog("�����ϴ�...");
		}

		@Override
		protected void onPostExecute(Integer result) {
			// ����a-z��������Դ����
			hideCustomProgressDialog();
			if (result == Constants.SUCCESS) {
				new AlertDialog.Builder(PatientInfoActivity.this)
						.setTitle("��ʾ")
						.setMessage(msg)
						.setPositiveButton("ȷ��",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int whichButton) {
										db=new DB(PatientInfoActivity.this);
										patient.setIsUpdate(1);
										//
										patient.setId(pNo);
										db.updatePatientUp(patient);
										strB.append("\t\t����ID:").append(pNo);
										pName.setText(strB);
									}
								}).show();
			} else {
				AlertDialogUtil.showAlertDialog(PatientInfoActivity.this, msg);
			}

		}

	}
}
