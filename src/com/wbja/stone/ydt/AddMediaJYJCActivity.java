package com.wbja.stone.ydt;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.wbja.stone.ydt.util.Constants;
import com.wbja.stone.ydt.util.FileUtil;
import com.wbja.stone.ydt.util.StringUtil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @ClassName: AddRecordActivity
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Stone
 * @date 2015-7-8 上午11:13:49
 * 
 */
public class AddMediaJYJCActivity extends Activity implements OnClickListener {

	private LinearLayout natives;
	private LinearLayout pai;
	private LinearLayout yuyin;
	private LinearLayout wenzi;
	private TextView txtContext;

	private LinearLayout mainContent;
	private LinearLayout mainContentVoice;
	private Intent intent;

	private File myCaptureFile = null;

	public static final String IMAGE_UNSPECIFIED = "image/*";

	private Spinner headBs;
	private String[] mItems;
	private ImageView imgRight;
	private ImageView imgLeft;
	
	private String src;
	private String filter;
	private YdtApplication ydt;

	private ImageView img;// 自动加载
	private TextView headTitle, headBack;
	private String title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_media_spin);
		
		headBs = (Spinner) findViewById(R.id.headBs);
		imgLeft = (ImageView) findViewById(R.id.imgLeft);
		imgRight = (ImageView) findViewById(R.id.imgRight);
		
		natives = (LinearLayout) findViewById(R.id.natives);
		pai = (LinearLayout) findViewById(R.id.pai);
		wenzi = (LinearLayout) findViewById(R.id.wenzi);

		mainContent = (LinearLayout) findViewById(R.id.mainContent);
		mainContentVoice = (LinearLayout) findViewById(R.id.mainContentVoice);
		yuyin = (LinearLayout) findViewById(R.id.yuyin);
		txtContext = (TextView) findViewById(R.id.txtContext);
		headTitle = (TextView) findViewById(R.id.headTitle);
		headBack = (TextView) findViewById(R.id.headBack);

		String mrcStr = StringUtil.getInfo(AddMediaJYJCActivity.this, "record", "");
		Log.d("******resordJSON*****", mrcStr);
		Intent intent = getIntent();
		src = intent.getStringExtra("src");
		filter = intent.getStringExtra("filter");
		title = intent.getStringExtra("title");

		ydt = YdtApplication.getInstance();

		if (!StringUtil.isEmpty(title)) {
			headTitle.setText("添加" + title);
		} else {
			headTitle.setText("添加");
		}
		headBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AddMediaJYJCActivity.this.finish();
			}
		});
		
		imgLeft.setOnClickListener(this);
		imgRight.setOnClickListener(this);

		// 建立数据源
		mItems = getResources().getStringArray(R.array.jyname);
		// 建立Adapter并且绑定数据源
		ArrayAdapter<String> spA = new ArrayAdapter<String>(this,
				R.layout.item_spinner, mItems);
		headBs.setAdapter(spA);

		headBs.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View v, int position,
					long id) {
				// TODO Auto-generated method stub
				if(position==mItems.length-1){//最后一项被选中
					headBs.setSelection(position-1,true);
					AddMediaJYJCActivity.this.finish();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		File srcFile = new File(src);
		if (!srcFile.exists()) {
			srcFile.mkdirs();
		}
		// db.insertRecord(item);
		// 为按钮添加监听器
		natives.setOnClickListener(this);
		pai.setOnClickListener(this);
		yuyin.setOnClickListener(this);
		wenzi.setOnClickListener(this);
	}

	/**
	 * back
	 * */
	public void back(View view) {
		this.finish();
	}

	@Override
	public void onClick(View view) {
		int pos;
		switch (view.getId()) {
		case R.id.imgLeft:
			pos = headBs.getSelectedItemPosition();
			pos--;
			if (pos >= 0) {
				headBs.setSelection(pos, true);
			}
			break;
		case R.id.imgRight:
			pos = headBs.getSelectedItemPosition();
			pos++;
			if (pos < headBs.getCount() - 1) {
				headBs.setSelection(pos, true);
			}
			break;
		case R.id.natives:
			myCaptureFile = FileUtil
					.refileG(AddMediaJYJCActivity.this, filter+headBs.getSelectedItemPosition()+"-", src);
			intent = new Intent(Intent.ACTION_PICK, null);
			intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					IMAGE_UNSPECIFIED);
			startActivityForResult(intent, Constants.PHOTOZOOM);
			break;
		case R.id.pai:
			myCaptureFile = FileUtil
					.refileG(AddMediaJYJCActivity.this, filter+headBs.getSelectedItemPosition()+"-", src);
			intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(myCaptureFile));
			startActivityForResult(intent, Constants.PHOTOHRAPH);
			break;
		case R.id.yuyin:
			intent = new Intent(AddMediaJYJCActivity.this, RecorderActivity.class);
			intent.putExtra("src", src);
			intent.putExtra("type", filter+headBs.getSelectedItemPosition()+"-");
			// 发送意图标示为REQUSET=1
			startActivityForResult(intent, Constants.VOICE);
			break;
		case R.id.wenzi:
			intent = new Intent(AddMediaJYJCActivity.this, IatActivity.class);
			intent.putExtra("src", src);

			turnTxtContent();
			// 发送意图标示为
			startActivityForResult(intent, Constants.TEXT);
			break;
		default:
			break;
		}
	}

	private void turnTxtContent() {
		if (ydt.getMrc() != null) {
			if (filter.equals(Constants.JWS)) {
				if (!StringUtil.isEmpty(ydt.getMrc().getJiwangshi())) {
					intent.putExtra("txt", ydt.getMrc().getJiwangshi());
				}
			}
			if (filter.equals(Constants.XBS)) {
				if (!StringUtil.isEmpty(ydt.getMrc().getXianbingshi())) {
					intent.putExtra("txt", ydt.getMrc().getXianbingshi());
				}
			}
			if (filter.equals(Constants.ZS)) {
				if (!StringUtil.isEmpty(ydt.getMrc().getZhusu())) {
					intent.putExtra("txt", ydt.getMrc().getZhusu());
				}
			}
			if (filter.equals(Constants.SX)) {
				if (!StringUtil.isEmpty(ydt.getMrc().getShexiang())) {
					intent.putExtra("txt", ydt.getMrc().getShexiang());
				}
			}
			if (filter.equals(Constants.MX)) {
				if (!StringUtil.isEmpty(ydt.getMrc().getMaixiang())) {
					intent.putExtra("txt", ydt.getMrc().getMaixiang());
				}
			}
			if (filter.equals(Constants.TZJC)) {
				if (!StringUtil.isEmpty(ydt.getMrc().getTizhengjiancha())) {
					intent.putExtra("txt", ydt.getMrc().getTizhengjiancha());
				}
			}
			if (filter.equals(Constants.FZJC)) {
				if (!StringUtil.isEmpty(ydt.getMrc().getFuzhujianca())) {
					intent.putExtra("txt", ydt.getMrc().getFuzhujianca());
				}
			}
			if (filter.equals(Constants.ZYZD)) {
				if (!StringUtil.isEmpty(ydt.getMrc().getZhongyizhenduan())) {
					intent.putExtra("txt", ydt.getMrc().getZhongyizhenduan());
				}
			}
			if (filter.equals(Constants.XYZD)) {
				if (!StringUtil.isEmpty(ydt.getMrc().getXiyizhenduan())) {
					intent.putExtra("txt", ydt.getMrc().getXiyizhenduan());
				}
			}
			if (filter.equals(Constants.ZZZF)) {
				if (!StringUtil.isEmpty(ydt.getMrc().getZhizezhifa())) {
					intent.putExtra("txt", ydt.getMrc().getZhizezhifa());
				}
			}
			if (filter.equals(Constants.FM)) {
				if (!StringUtil.isEmpty(ydt.getMrc().getFangming())) {
					intent.putExtra("txt", ydt.getMrc().getFangming());
				}
			}
			if (filter.equals(Constants.ZC)) {
				if (!StringUtil.isEmpty(ydt.getMrc().getFangjizucheng())) {
					intent.putExtra("txt", ydt.getMrc().getFangjizucheng());
				}
			}
			if (filter.equals(Constants.QTZL)) {
				if (!StringUtil.isEmpty(ydt.getMrc().getQitazhiliao())) {
					intent.putExtra("txt", ydt.getMrc().getQitazhiliao());
				}
			}

		}
	}

	/**
	 * 拍照上传
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == Constants.NONE)
			return;
		// 拍照
		if (requestCode == Constants.PHOTOHRAPH) {
			// 设置文件保存路径这里放在跟目录下
			if (myCaptureFile.exists()) {
				startPhotoZoom(Uri.fromFile(myCaptureFile));
			}

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
				// 自动生成图片
				img = new ImageView(AddMediaJYJCActivity.this);
				img.setTag(src);
				img.setImageBitmap(photo);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				lp.setMargins(5, 0, 5, 0);
				img.setLayoutParams(lp);

				mainContent.addView(img);
				// 技术图片
				// item.setCountImg(item.getCountImg() + 1);
				try {
					if (myCaptureFile == null) {
						myCaptureFile = FileUtil.refileG(AddMediaJYJCActivity.this,
								filter, src);
					}
					if (Environment.getExternalStorageState().equals(
							Environment.MEDIA_MOUNTED)) {
						if (!myCaptureFile.getParentFile().exists()) {
							myCaptureFile.getParentFile().mkdirs();
						}
						BufferedOutputStream bos;
						bos = new BufferedOutputStream(new FileOutputStream(
								myCaptureFile));
						photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);// (0
																				// -
						// 100)压缩文件
						bos.flush();
						bos.close();
					} else {

						Toast toast = Toast.makeText(AddMediaJYJCActivity.this,
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

			// 自动生成图片
			img = new ImageView(AddMediaJYJCActivity.this);
			img.setImageResource(R.drawable.audio_icon);
			if (src != null && !src.isEmpty()) {
				img.setTag(src);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				lp.setMargins(5, 0, 5, 0);
				img.setLayoutParams(lp);
			}
			mainContentVoice.addView(img);
			// 计数音频
			// item.setCountVoi(item.getCountVoi() + 1);

		}
		// 处理结果
		if (requestCode == Constants.TEXT) {
			String txtRe = data.getStringExtra(Constants.TEXTSTR);
			txtContext.setText(txtRe);
			if (ydt.getMrc() != null) {
				if (filter.equals(Constants.JWS)) {
					ydt.getMrc().setJiwangshi(txtRe);
				}
				if (filter.equals(Constants.XBS)) {
					ydt.getMrc().setXianbingshi(txtRe);
				}
				if (filter.equals(Constants.ZS)) {
					ydt.getMrc().setZhusu(txtRe);
				}
				if (filter.equals(Constants.SX)) {
					ydt.getMrc().setShexiang(txtRe);
				}
				if (filter.equals(Constants.MX)) {
					ydt.getMrc().setMaixiang(txtRe);
				}
				if (filter.equals(Constants.TZJC)) {
					ydt.getMrc().setTizhengjiancha(txtRe);
				}
				if (filter.equals(Constants.FZJC)) {
					ydt.getMrc().setFuzhujianca(txtRe);
				}
				if (filter.equals(Constants.ZYZD)) {
					ydt.getMrc().setZhongyizhenduan(txtRe);
				}
				if (filter.equals(Constants.XYZD)) {
					ydt.getMrc().setXiyizhenduan(txtRe);
				}
				if (filter.equals(Constants.ZZZF)) {
					ydt.getMrc().setZhizezhifa(txtRe);
				}
				if (filter.equals(Constants.FM)) {
					ydt.getMrc().setFangming(txtRe);
				}
				if (filter.equals(Constants.ZC)) {
					ydt.getMrc().setFangjizucheng(txtRe);
				}
				if (filter.equals(Constants.QTZL)) {
					ydt.getMrc().setQitazhiliao(txtRe);
				}

			}

		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 3);
		intent.putExtra("aspectY", 5);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 240);
		intent.putExtra("outputY", 400);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, Constants.PHOTORESOULT);
	}

}
