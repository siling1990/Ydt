package com.wbja.stone.ydt;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.abbyy.ocrsdk.Client;
import com.abbyy.ocrsdk.ProcessingSettings;
import com.abbyy.ocrsdk.Task;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.speech.setting.IatSettings;
import com.iflytek.speech.util.ApkInstaller;
import com.iflytek.speech.util.JsonParser;
import com.iflytek.sunflower.FlowerCollector;
import com.wbja.stone.ydt.entity.PatientUpload;
import com.wbja.stone.ydt.util.AlertDialogUtil;
import com.wbja.stone.ydt.util.Constants;
import com.wbja.stone.ydt.util.DB;
import com.wbja.stone.ydt.util.StringUtil;
import com.wbja.stone.ydt.window.MyProgressDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @ClassName: AddPatientActivity
 * @Description: add patient info.
 * @author Stone
 * @date 2015-6-25 下午4:40:39
 * 
 */
public class AddPatientActivity extends Activity implements OnClickListener {

	private TextView headTitle;
	private MyProgressDialog m_customProgrssDialog;
	private MainFrameTask mMainFrameTask;
	private ImageView img;
	private Button nati;
	private Button pai;
	private String srcPath;
	private DB db;
	private EditText pID, pName, nation, occu, country, tel, danweiM, danweiA,
			contacts, tel_contacts, sn_shebao, sn_yibao;
	private RadioButton radiomale, radioFemale;
	private TextView birth;
	private Calendar calendar;
	private Dialog dialog;
	private PatientUpload pa;
	private String[] arryBt;
	private String[] arrMar;
	private Intent intent;
	private Spinner spBt, spMar;

	private static String TAG = IatActivity.class.getSimpleName();
	// 语音听写对象
	private SpeechRecognizer mIat;
	// 语音听写UI
	private RecognizerDialog mIatDialog;
	// 用HashMap存储听写结果
	private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
	//
	private Toast mToast;
	private SharedPreferences mSharedPreferences;
	// 引擎类型
	private String mEngineType = SpeechConstant.TYPE_CLOUD;
	// 语记安装助手�?
	ApkInstaller mInstaller;
	int ret = 0;
	private int txtId;

	private ImageView imgID, imgShebao, imgYibao;
	private String imageUrl;
	private String outputPath;

	private File myCaptureFile = null;
	public static final String IMAGE_UNSPECIFIED = "image/*";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_patient);

		headTitle = (TextView) findViewById(R.id.headTitle);
		headTitle.setText("患者信息");

		img = (ImageView) findViewById(R.id.img);
		nati = (Button) findViewById(R.id.natives);
		pai = (Button) findViewById(R.id.pai);
		nati.setOnClickListener(this);
		pai.setOnClickListener(this);

		birth = (TextView) findViewById(R.id.birth);
		pID = (EditText) findViewById(R.id.pid);
		pName = (EditText) findViewById(R.id.pName);
		nation = (EditText) findViewById(R.id.nation);
		occu = (EditText) findViewById(R.id.occupation);
		country = (EditText) findViewById(R.id.country);
		tel = (EditText) findViewById(R.id.tel);
		danweiM = (EditText) findViewById(R.id.danweiM);
		danweiA = (EditText) findViewById(R.id.danweiA);
		contacts = (EditText) findViewById(R.id.contacts);
		tel_contacts = (EditText) findViewById(R.id.tel_contacts);
		sn_shebao = (EditText) findViewById(R.id.sn_shebao);
		sn_yibao = (EditText) findViewById(R.id.sn_yibao);

		radiomale = (RadioButton) findViewById(R.id.radioMale);
		radioFemale = (RadioButton) findViewById(R.id.radioFemale);

		spBt = (Spinner) findViewById(R.id.spBt);
		spMar = (Spinner) findViewById(R.id.spMar);
		arryBt = getResources().getStringArray(R.array.bt);
		arrMar = getResources().getStringArray(R.array.mar);
		spBt.setAdapter(new ArrayAdapter<String>(this, R.layout.item_spinner,
				arryBt));
		spMar.setAdapter(new ArrayAdapter<String>(this, R.layout.item_spinner,
				arrMar));

		pa = new PatientUpload();
		pa.setId_user(Integer.parseInt(StringUtil.getID_User(AddPatientActivity.this)));

		myCaptureFile = refile();

		birth.setOnClickListener(this);

		imgID = (ImageView) findViewById(R.id.imgID);
		imgShebao = (ImageView) findViewById(R.id.imgShebao);
		imgYibao = (ImageView) findViewById(R.id.imgYibao);

		imgID.setOnClickListener(this);
		imgShebao.setOnClickListener(this);
		imgYibao.setOnClickListener(this);

		db = new DB(this);

		// 初始化识别无UI识别对象
		// 使用SpeechRecognizer对象，可根据回调消息自定义界面；
		mIat = SpeechRecognizer.createRecognizer(AddPatientActivity.this,
				mInitListener);

		// 初始化听写Dialog，如果只使用有UI听写功能，无�?��建SpeechRecognizer
		// 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资�?
		mIatDialog = new RecognizerDialog(AddPatientActivity.this,
				mInitListener);

		mSharedPreferences = getSharedPreferences(IatSettings.PREFER_NAME,
				Activity.MODE_PRIVATE);
		mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

		mInstaller = new ApkInstaller(AddPatientActivity.this);
	}

	public void yuYin(View view) {

		mIatResults.clear();
		// 设置参数
		setParam();
		boolean isShowDialog = mSharedPreferences.getBoolean(
				getString(R.string.pref_key_iat_show), true);
		if (isShowDialog) {
			// 显示听写对话�?
			mIatDialog.setListener(mRecognizerDialogListener);
			mIatDialog.show();
			showTip(getString(R.string.text_begin));
		} else {
			// 不显示听写对话框
			ret = mIat.startListening(mRecognizerListener);
			if (ret != ErrorCode.SUCCESS) {
				showTip("听写失败,错误码：" + ret);
			} else {
				showTip(getString(R.string.text_begin));
			}
		}
		switch (view.getId()) {

		case R.id.imgName:
			txtId = pName.getId();
			break;
		case R.id.imgCountry:
			txtId = country.getId();
			break;
		case R.id.imgNation:
			txtId = nation.getId();
			break;
		case R.id.imgOcc:
			txtId = occu.getId();
			break;
		case R.id.imgDanweiM:
			txtId = danweiM.getId();
			break;
		case R.id.imgDanweiA:
			txtId = danweiA.getId();
			break;
		case R.id.imgCon:
			txtId = contacts.getId();
			break;
		default:
			break;
		}
	}

	/**
	 * back
	 * */
	public void back(View view) {
		this.finish();
	}

	/**
	 * save patient info
	 * */
	public void save(View view) {

		if (check()) {
			mMainFrameTask = new MainFrameTask(this);
			mMainFrameTask.execute();
		}

	}

	/**
	 * show progress dialog
	 * */
	void showCustomProgrssDialog(String msg) {

		if (null == m_customProgrssDialog)

			m_customProgrssDialog = MyProgressDialog

			.createProgrssDialog(AddPatientActivity.this);

		if (null != m_customProgrssDialog) {

			m_customProgrssDialog.setMessage(msg);

			m_customProgrssDialog.show();

			m_customProgrssDialog.setCancelable(false);

		}

	}

	/**
	 * hide progress dialog
	 * */
	void hideCustomProgressDialog() {

		if (null != m_customProgrssDialog) {

			m_customProgrssDialog.dismiss();

			m_customProgrssDialog = null;

		}

	}

	// Task,save info
	/**
	 * @author hadoop
	 * 
	 */
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

			pa.setDate_birth(birth.getText().toString());
			pa.setNation(nation.getText().toString());
			pa.setOccupation(occu.getText().toString());
			// pa.setGerenshi(others.getText().toString());
			pa.setIdcard(pID.getText().toString());
			pa.setName(pName.getText().toString());
			pa.setIsUpdate(0);
			if (radiomale.isChecked()) {
				pa.setGender("男");
			} else {
				pa.setGender("女");
			}
			// TODO
			pa.setXuexing(arryBt[spBt.getSelectedItemPosition()]);
			pa.setMarriage(arrMar[spMar.getSelectedItemPosition()]);
			pa.setTele(tel.getText().toString());
			pa.setCountry(country.getText().toString());
			pa.setDanweidizhi(danweiA.getText().toString());
			pa.setDanweimingcheng(danweiM.getText().toString());
			pa.setContacts(contacts.getText().toString());
			pa.setTele_contact(tel_contacts.getText().toString());
			pa.setSn_shebao(sn_shebao.getText().toString());
			pa.setSn_yibao(sn_yibao.getText().toString());

			if (db.insertPatientUp(pa)) {
				return Constants.SUCCESS;
			}
			return Constants.FAILURE;
		}

		@Override
		protected void onPreExecute() {
			showCustomProgrssDialog("正在保存...");
		}

		@Override
		protected void onPostExecute(Integer result) {
			if (Constants.SUCCESS == result) {
				new AlertDialog.Builder(AddPatientActivity.this)
						.setTitle("提示")
						.setMessage("保存成功！")
						.setPositiveButton("确认",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int whichButton) {
										finish();
									}
								}).show();

			} else {
				AlertDialogUtil.showAlertDialogConfirm(AddPatientActivity.this,
						"保存失败！");
			}
			hideCustomProgressDialog();
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.natives:
			intent = new Intent(Intent.ACTION_PICK, null);
			intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					IMAGE_UNSPECIFIED);
			startActivityForResult(intent, Constants.PHOTOZOOM);
			break;
		case R.id.pai:
			intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(myCaptureFile));
			startActivityForResult(intent, Constants.PHOTOHRAPH);
			break;
		case R.id.birth:
			calendar = Calendar.getInstance();
			dialog = new DatePickerDialog(AddPatientActivity.this,
					new DatePickerDialog.OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							System.out.println("年-->" + year + "月-->"
									+ monthOfYear + "日-->" + dayOfMonth);
							birth.setText(year + "-" + (monthOfYear + 1) + "-"
									+ dayOfMonth);
						}
					}, calendar.get(Calendar.YEAR),
					calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH));
			dialog.show();
			break;
		case R.id.imgID:
			intent = new Intent(AddPatientActivity.this, CameraActivity.class);

			startActivityForResult(intent, Constants.IMGID);
			break;
		case R.id.imgShebao:
			intent = new Intent(AddPatientActivity.this, CameraActivity.class);

			startActivityForResult(intent, Constants.IMGSHEBAO);
			break;
		case R.id.imgYibao:
			intent = new Intent(AddPatientActivity.this, CameraActivity.class);

			startActivityForResult(intent, Constants.IMGYIBAO);
			break;
		default:
			break;
		}
	}

	/**
	 * 拍照上传
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Constants.NONE)
			return;
		// 拍照
		if (requestCode == Constants.PHOTOHRAPH) {
			// myCaptureFile = refile();
			// 设置文件保存路径这里放在跟目录下
			startPhotoZoom(Uri.fromFile(myCaptureFile));
		}

		if (data == null)
			return;

		// 读取相册缩放图片
		if (requestCode == Constants.PHOTOZOOM) {
			startPhotoZoom(data.getData());
		}
		// 处理结果
		if (requestCode == Constants.PHOTORESOULT) {
			Bundle extras = data.getExtras();
			if (extras != null) {
				Bitmap photo = extras.getParcelable("data");

				img.setImageBitmap(photo);
				try {
					if (myCaptureFile == null) {
						myCaptureFile = refile();
					}
					if (Environment.getExternalStorageState().equals(
							Environment.MEDIA_MOUNTED)) {
						pa.setIdPhotoPath(myCaptureFile.getAbsolutePath());
						BufferedOutputStream bos;
						bos = new BufferedOutputStream(new FileOutputStream(
								myCaptureFile));
						photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);// (0
																				// -
						// 100)压缩文件
						bos.flush();
						bos.close();
					} else {

						Toast toast = Toast.makeText(AddPatientActivity.this,
								"保存失败，SD卡无效", Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		if (requestCode == Constants.IMGID) {

			if (data != null) {
				imageUrl = data.getStringExtra("IMAGE_PATH");
				outputPath = data.getStringExtra("RESULT_PATH");
				new AsyncProcessTask(AddPatientActivity.this,requestCode).execute(imageUrl, outputPath);
			}
		}
		if (requestCode == Constants.IMGSHEBAO) {

			if (data != null) {
				imageUrl = data.getStringExtra("IMAGE_PATH");
				outputPath = data.getStringExtra("RESULT_PATH");
				new AsyncProcessTask(AddPatientActivity.this,requestCode).execute(imageUrl, outputPath);
			}
		}
		if (requestCode == Constants.IMGYIBAO) {

			if (data != null) {
				imageUrl = data.getStringExtra("IMAGE_PATH");
				outputPath = data.getStringExtra("RESULT_PATH");
				new AsyncProcessTask(AddPatientActivity.this,requestCode).execute(imageUrl, outputPath);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, Constants.PHOTORESOULT);
	}

	private File refile() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			String name = new SimpleDateFormat("yyyyMMddhhmmss")
					.format(new Date());
			String fileNmae = Environment.getExternalStorageDirectory()
					.toString()
					+ File.separator
					+ Constants.IMAGEPATH
					+ name
					+ ".jpg";
			srcPath = fileNmae;
			System.out.println(srcPath + "----------");
			// pa.setIdPhotoPath(fileNmae);
			File File = new File(fileNmae);
			if (!File.getParentFile().exists()) {
				File.getParentFile().mkdirs();
			}
			return File;
		} else {

			Toast toast = Toast.makeText(AddPatientActivity.this, "保存失败，SD卡无效",
					Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
		return null;
	}

	private boolean check() {

		if (pID.getText().toString().isEmpty()) {
			AlertDialogUtil.showAlertDialog(AddPatientActivity.this,
					"请输入患者身份证号！");
			return false;
		}
		if (pName.getText().toString().isEmpty()) {
			AlertDialogUtil
					.showAlertDialog(AddPatientActivity.this, "请输入患者姓名！");
			return false;
		}
		if (birth.getText().toString().isEmpty()) {
			AlertDialogUtil.showAlertDialog(AddPatientActivity.this,
					"请输入患者出生日期！");
			return false;
		}
		if (nation.getText().toString().isEmpty()) {
			AlertDialogUtil
					.showAlertDialog(AddPatientActivity.this, "请输入患者民族！");
			return false;
		}
		return true;
	}

	// 退出当前界面
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
	 * 退出应用程序
	 */
	private void checkForSave() {
		new AlertDialog.Builder(AddPatientActivity.this).setMessage("确认退出？")
				.setPositiveButton("确认", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int whichButton) {
						finish();
					}
				}).setNegativeButton("取消", null).show();
	}

	/**
	 * 语音识别
	 * */
	/**
	 * 初始化监听器�?
	 */
	private InitListener mInitListener = new InitListener() {

		@Override
		public void onInit(int code) {
			Log.d(TAG, "SpeechRecognizer init() code = " + code);
			if (code != ErrorCode.SUCCESS) {
				showTip("初始化失败，错误码：" + code);
			}
		}
	};

	/**
	 * 听写监听器�?
	 */
	private RecognizerListener mRecognizerListener = new RecognizerListener() {

		@Override
		public void onBeginOfSpeech() {
			// 此回调表示：sdk内部录音机已经准备好了，用户可以�?��语音输入
			showTip("请说话");
		}

		@Override
		public void onError(SpeechError error) {
			// Tips�?
			// 错误码：10118(您没有说�?，可能是录音机权限被禁，�?��提示用户打开应用的录音权限�?
			// 如果使用本地功能（语记）�?��提示用户�?��语记的录音权限�?
			showTip(error.getPlainDescription(true));
		}

		@Override
		public void onEndOfSpeech() {
			// 此回调表示：�?��到了语音的尾端点，已经进入识别过程，不再接受语音输入
			showTip("结束说话");
		}

		@Override
		public void onResult(RecognizerResult results, boolean isLast) {
			Log.d(TAG, results.getResultString());
			printResult(results);

			if (isLast) {

			}
		}

		@Override
		public void onVolumeChanged(int volume, byte[] data) {
			showTip("当前正在说话，音量大小：" + volume);
			Log.d(TAG, "返回音频数据" + data.length);
		}

		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
			// 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
			// 若使用本地能力，会话id为null
			// if (SpeechEvent.EVENT_SESSION_ID == eventType) {
			// String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
			// Log.d(TAG, "session id =" + sid);
			// }
		}
	};

	private void printResult(RecognizerResult results) {
		String text = JsonParser.parseIatResult(results.getResultString());

		String sn = null;
		// 读取json结果中的sn字段
		try {
			JSONObject resultJson = new JSONObject(results.getResultString());
			sn = resultJson.optString("sn");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		mIatResults.put(sn, text);

		StringBuffer resultBuffer = new StringBuffer();
		for (String key : mIatResults.keySet()) {
			resultBuffer.append(mIatResults.get(key));
		}

		EditText mResultText = (EditText) findViewById(txtId);
		// 去掉。号
		mResultText.setText(resultBuffer.toString().subSequence(0,
				resultBuffer.toString().length()));
		mResultText.setSelection(mResultText.length());
	}

	/**
	 * 听写UI监听�?
	 */
	private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
		public void onResult(RecognizerResult results, boolean isLast) {
			printResult(results);
		}

		/**
		 * 识别回调错误.
		 */
		public void onError(SpeechError error) {
			showTip(error.getPlainDescription(true));
		}

	};

	private void showTip(final String str) {
		mToast.setText(str);
		mToast.show();
	}

	/**
	 * 参数设置
	 * 
	 * @param param
	 * @return
	 */
	public void setParam() {
		// 清空参数
		mIat.setParameter(SpeechConstant.PARAMS, null);

		// 设置听写引擎
		mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
		// 设置返回结果格式
		mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

		String lag = mSharedPreferences.getString("iat_language_preference",
				"mandarin");
		if (lag.equals("en_us")) {
			// 设置语言
			mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
		} else {
			// 设置语言
			mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
			// 设置语言区域
			mIat.setParameter(SpeechConstant.ACCENT, lag);
		}

		// 设置语音前端�?静音超时时间，即用户多长时间不说话则当做超时处理
		mIat.setParameter(SpeechConstant.VAD_BOS,
				mSharedPreferences.getString("iat_vadbos_preference", "4000"));

		// 设置语音后端�?后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
		mIat.setParameter(SpeechConstant.VAD_EOS,
				mSharedPreferences.getString("iat_vadeos_preference", "1000"));

		// 设置标点符号,设置�?0"返回结果无标�?设置�?1"返回结果有标�?
		mIat.setParameter(SpeechConstant.ASR_PTT,
				mSharedPreferences.getString("iat_punc_preference", "1"));

		// 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
		// 注：AUDIO_FORMAT参数语记�?��更新版本才能生效
		mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
		mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH,
				Environment.getExternalStorageDirectory() + "/msc/iat.wav");

		// 设置听写结果是否结果动�?修正，为�?”则在听写过程中动�?递增地返回结果，否则只在听写结束之后返回�?��结果
		// 注：该参数暂时只对在线听写有�?
		mIat.setParameter(SpeechConstant.ASR_DWA,
				mSharedPreferences.getString("iat_dwa_preference", "0"));
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// �?��时释放连�?
		mIat.cancel();
		mIat.destroy();
	}

	@Override
	protected void onResume() {
		// �?��统计 移动数据统计分析
		FlowerCollector.onResume(AddPatientActivity.this);
		FlowerCollector.onPageStart(TAG);
		super.onResume();
	}

	@Override
	protected void onPause() {
		// �?��统计 移动数据统计分析
		FlowerCollector.onPageEnd(TAG);
		FlowerCollector.onPause(AddPatientActivity.this);
		super.onPause();
	}

	class AsyncProcessTask extends AsyncTask<String, String, Boolean> {

		private ProgressDialog dialog;
		/** application context. */
		private Context activity;
		private int code;
		
		public AsyncProcessTask(Context activity,int code) {
			this.activity = activity;
			dialog = new ProgressDialog(activity);
			this.code=code;
		}

		

		protected void onPreExecute() {
			dialog.setMessage("处理中");
			dialog.setCancelable(false);
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		protected void onPostExecute(Boolean result) {
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
		}

		@Override
		protected Boolean doInBackground(String... args) {

			String inputFile = args[0];
			String outputFile = args[1];

			try {
				Client restClient = new Client();

				// !!! Please provide application id and password and remove
				// this line. !!!
				// To create an application and obtain a password,
				// register at http://cloud.ocrsdk.com/Account/Register
				// More info on getting your application id and password at
				// http://ocrsdk.com/documentation/faq/#faq3

				// Name of application you created
				restClient.applicationId = "Ydt_Chinese_medical";
				// You should get e-mail from ABBYY Cloud OCR SDK service with
				// the application password
				restClient.password = "GATdm/spRjfR3L0l+YyqcQCH";

				// Obtain installation id when running the application for the
				// first time
				SharedPreferences settings = AddPatientActivity.this
						.getPreferences(Activity.MODE_PRIVATE);
				String instIdName = "installationId";
				if (!settings.contains(instIdName)) {
					// Get installation id from server using device id
					String deviceId = android.provider.Settings.Secure
							.getString(activity.getContentResolver(),
									android.provider.Settings.Secure.ANDROID_ID);

					// Obtain installation id from server
				//	publishProgress("First run: obtaining installation id..");
					String installationId = restClient
							.activateNewInstallation(deviceId);
//					publishProgress("Done. Installation id is '"
//							+ installationId + "'");

					SharedPreferences.Editor editor = settings.edit();
					editor.putString(instIdName, installationId);
					editor.commit();
				}

				String installationId = settings.getString(instIdName, "");
				restClient.applicationId += installationId;

				publishProgress("正在上传...");

				String language = "English"; // Comma-separated list:
														// ,English or
														// German,French,Spanish
														// etc.

				ProcessingSettings processingSettings = new ProcessingSettings();
				processingSettings
						.setOutputFormat(ProcessingSettings.OutputFormat.txt);
				processingSettings.setLanguage(language);

				publishProgress("正在上传..");

				// If you want to process business cards, uncomment this
				/*
				 * BusCardSettings busCardSettings = new BusCardSettings();
				 * busCardSettings.setLanguage(language);
				 * busCardSettings.setOutputFormat
				 * (BusCardSettings.OutputFormat.xml); Task task =
				 * restClient.processBusinessCard(filePath, busCardSettings);
				 */
				Task task = restClient.processImage(inputFile,
						processingSettings);

				while (task.isTaskActive()) {
					// Note: it's recommended that your application waits
					// at least 2 seconds before making the first getTaskStatus
					// request
					// and also between such requests for the same task.
					// Making requests more often will not improve your
					// application performance.
					// Note: if your application queues several files and waits
					// for them
					// it's recommended that you use listFinishedTasks instead
					// (which is described
					// at
					// http://ocrsdk.com/documentation/apireference/listFinishedTasks/).

					// Thread.sleep(5000);
					publishProgress("处理中..");
					task = restClient.getTaskStatus(task.Id);
				}

				if (task.Status == Task.TaskStatus.Completed) {
					publishProgress("正在处理结果..");
					FileOutputStream fos = activity.openFileOutput(outputFile,
							Context.MODE_PRIVATE);

					try {
						restClient.downloadResult(task, fos);
					} finally {
						fos.close();
					}
					updateResults(code);
					
					publishProgress("完成");
					
					
				} else if (task.Status == Task.TaskStatus.NotEnoughCredits) {
					throw new Exception(
							"Not enough credits to process task. Add more pages to your application's account.");
				} else {
					throw new Exception("Task failed");
				}

				return true;
			} catch (Exception e) {
				final String message = "Error: " + e.getMessage();
				publishProgress(message);
				return false;
			}
		}

		@Override
		protected void onProgressUpdate(String... values) {
			// TODO Auto-generated method stub
			String stage = values[0];
			dialog.setMessage(stage);
			// dialog.setProgress(values[0]);
		}
	}
	
	public void updateResults(int code) {
		if (code==0)
			return;
		try {
			StringBuffer contents = new StringBuffer();

			FileInputStream fis = openFileInput(outputPath);
			try {
				Reader reader = new InputStreamReader(fis, "UTF-8");
				BufferedReader bufReader = new BufferedReader(reader);
				String text = null;
				while ((text = bufReader.readLine()) != null) {
					contents.append(text).append(System.getProperty("line.separator"));
				}
			} finally {
				fis.close();
			}
			if(code==Constants.IMGID){
				pID.setText(contents.toString());
			}
			if(code==Constants.IMGSHEBAO){
				sn_shebao.setText(contents.toString());
			}
			if(code==Constants.IMGYIBAO){
				sn_yibao.setText(contents.toString());
			}
			
		} catch (Exception e) {
		}
	}
	

}
