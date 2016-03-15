package com.wbja.stone.ydt.fragment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.wbja.stone.ydt.AddMediaActivity;
import com.wbja.stone.ydt.AddPatientActivity;
import com.wbja.stone.ydt.R;
import com.wbja.stone.ydt.RecorderActivity;
import com.wbja.stone.ydt.entity.Schedule;
import com.wbja.stone.ydt.util.AlertDialogUtil;
import com.wbja.stone.ydt.util.Constants;
import com.wbja.stone.ydt.util.DB;
import com.wbja.stone.ydt.util.StringUtil;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class FragmentBw extends Fragment implements OnClickListener {

	private TextView date;
	private TextView content;
	private TextView time;
	private TextView contel;
	private Schedule sch;
	private Spinner spType;
	private Button brAdd;
	private DB db;
	private Calendar calendar;
	private Dialog dialog;

	private ImageView img;
	private ImageView imgV;
	private Button nati;
	private Button pai, luyin;
	private String srcPath;
	private Intent intent;
	private File myCaptureFile = null;
	public static final String IMAGE_UNSPECIFIED = "image/*";
	private LinearLayout mainContentVoice;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment_bw, null);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		date = (TextView) getView().findViewById(R.id.date);
		content = (TextView) getView().findViewById(R.id.content);
		time = (TextView) getView().findViewById(R.id.time);
		contel = (TextView) getView().findViewById(R.id.tel);
		brAdd = (Button) getView().findViewById(R.id.btAdd);
		spType = (Spinner) getView().findViewById(R.id.sptype);

		img = (ImageView) getView().findViewById(R.id.img);
		nati = (Button) getView().findViewById(R.id.natives);
		pai = (Button) getView().findViewById(R.id.pai);
		luyin = (Button) getView().findViewById(R.id.luyin);

		mainContentVoice = (LinearLayout) getView().findViewById(
				R.id.mainContentVoice);
		nati.setOnClickListener(this);
		pai.setOnClickListener(this);
		luyin.setOnClickListener(this);
		sch = new Schedule();
		sch.setId_user(Integer.parseInt(StringUtil.getID_User(getActivity())));
		myCaptureFile = refile();

		// 建立数据源
		String[] mItems = getResources().getStringArray(R.array.schtype);
		// 建立Adapter并且绑定数据源
		ArrayAdapter<String> spA = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_dropdown_item, mItems);
		// 绑定 Adapter到控件
		spType.setAdapter(spA);

		spType.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				sch.setType(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		db = new DB(getActivity());

		brAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				add();
			}
		});

		date.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				calendar = Calendar.getInstance();
				dialog = new DatePickerDialog(getActivity(),
						new DatePickerDialog.OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth) {
								String m;
								String d;
								monthOfYear = monthOfYear + 1;
								if (monthOfYear < 10) {
									m = "0" + monthOfYear;
								} else {
									m = "" + monthOfYear;
								}
								if (dayOfMonth < 10) {
									d = "0" + dayOfMonth;
								} else {
									d = "" + dayOfMonth;
								}

								System.out.println("年-->" + year + "月-->"
										+ monthOfYear + "日-->" + dayOfMonth);
								date.setText(year + "-" + m + "-" + d);
							}
						}, calendar.get(Calendar.YEAR), calendar
								.get(Calendar.MONTH), calendar
								.get(Calendar.DAY_OF_MONTH));
				dialog.show();
			}
		});

		time.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				calendar = Calendar.getInstance();
				dialog = new TimePickerDialog(getActivity(),
						new OnTimeSetListener() {

							@Override
							public void onTimeSet(TimePicker arg0, int hour,
									int min) {
								// TODO Auto-generated method stub
								time.setText(hour + ":" + min);
							}
						}, calendar.get(Calendar.HOUR_OF_DAY), calendar
								.get(Calendar.MINUTE), true);
				dialog.show();
			}
		});
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	public void add() {
		if (check()) {
			sch.setContent(content.getText().toString());
			sch.setDate(date.getText().toString());
			sch.setTime(time.getText().toString());
			sch.setContel(contel.getText().toString());
			if (db.insertSchedule(sch)) {
				AlertDialogUtil.showAlertDialog(getActivity(), "添加成功！");
				date.setText("");
				content.setText("");
				time.setText("");
				contel.setText("");
				img.setVisibility(View.GONE);
				mainContentVoice.removeAllViews();
				// type.setText("");
			} else {
				AlertDialogUtil.showAlertDialog(getActivity(), "添加失败！");
			}
		}
	}

	private boolean check() {

		if (date.getText().toString().isEmpty()) {
			AlertDialogUtil.showAlertDialog(getActivity(), "请选择日期！");
			return false;
		}
		if (content.getText().toString().isEmpty()) {
			AlertDialogUtil.showAlertDialog(getActivity(), "说点什么吧！");
			return false;
		}
		// if (type.getText().toString().isEmpty()) {
		// AlertDialogUtil.showAlertDialog(getActivity(),
		// "请输入备忘类型！");
		// return false;
		// }
		return true;
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
		case R.id.luyin:
			intent = new Intent(getActivity(), RecorderActivity.class);
			intent.putExtra("src", Environment.getExternalStorageDirectory()
					.toString() + File.separator + Constants.SCHEDULEPATH);
			intent.putExtra("type", "schedule");
			// 发送意图标示为REQUSET=1
			startActivityForResult(intent, Constants.VOICE);
			break;
		default:
			break;
		}
	}

	/**
	 * 拍照上传
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;
		Bitmap bm;
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
				img.setVisibility(View.VISIBLE);
				try {
					if (myCaptureFile == null) {
						myCaptureFile = refile();
					}
					if (Environment.getExternalStorageState().equals(
							Environment.MEDIA_MOUNTED)) {
						sch.setImgPath(myCaptureFile.getAbsolutePath());
						BufferedOutputStream bos;
						bos = new BufferedOutputStream(new FileOutputStream(
								myCaptureFile));
						photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);// (0
																				// -
						// 100)压缩文件
						bos.flush();
						bos.close();
					} else {

						Toast toast = Toast.makeText(getActivity(),
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

		// 处理结果
		if (requestCode == Constants.VOICE) {

			String src = data.getStringExtra(Constants.VOICESTR);
			sch.setVoiPath(src);
			// 自动生成图片
			imgV = new ImageView(getActivity());
			imgV.setImageResource(R.drawable.audio_icon);
			if (src != null && !src.isEmpty()) {
				imgV.setTag(src);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				lp.setMargins(5, 0, 5, 0);
				imgV.setLayoutParams(lp);
			}
			mainContentVoice.addView(imgV);
			// 计数音频
			// item.setCountVoi(item.getCountVoi() + 1);

		}
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
					+ Constants.SCHEDULEPATH
					+ name + ".jpg";
			srcPath = fileNmae;
			System.out.println(srcPath + "----------");
			// pa.setIdPhotoPath(fileNmae);
			File File = new File(fileNmae);
			if (!File.getParentFile().exists()) {
				File.getParentFile().mkdirs();
			}
			return File;
		} else {

			Toast toast = Toast.makeText(getActivity(), "保存失败，SD卡无效",
					Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
		return null;
	}

}
