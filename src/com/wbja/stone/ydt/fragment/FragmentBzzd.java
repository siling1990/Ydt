package com.wbja.stone.ydt.fragment;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wbja.stone.ydt.PhotoActivity;
import com.wbja.stone.ydt.R;
import com.wbja.stone.ydt.VoicePlayerActivity;
import com.wbja.stone.ydt.YdtApplication;
import com.wbja.stone.ydt.entity.MRClinic;
import com.wbja.stone.ydt.util.AlertDialogUtil;
import com.wbja.stone.ydt.util.Constants;
import com.wbja.stone.ydt.util.FileUtil;
import com.wbja.stone.ydt.util.GsonUtil;
import com.wbja.stone.ydt.util.HttpUtil;
import com.wbja.stone.ydt.util.StringUtil;
import com.wbja.stone.ydt.window.MyProgressDialog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class FragmentBzzd extends Fragment {
	private EditText txtbm, txtzyzd, txtxyzd;
	private LinearLayout  zyzdHs, xyzdHs;
	private Intent intent;
	private File[] files;
	private ImageView image;
	private String src;
	private MRClinic mrc;
	private ImageView imageBo;
	private CheckBox checkBox;
	private View vv;
	private YdtApplication ydt;

	private MyProgressDialog m_customProgrssDialog;
	private ZZBMTask zZBMTask;
	private JSONArray jsonArray;
	private Map<String, String> map;
	private String msg;
	private StringBuilder zHongYZH;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment_bzzd, null);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		txtbm = (EditText) getView().findViewById(R.id.txtbm);
		txtzyzd = (EditText) getView().findViewById(R.id.txtzyzd);
		txtxyzd = (EditText) getView().findViewById(R.id.txtxyzd);

		mrc = GsonUtil.getObject(
				StringUtil.getInfo(getActivity(), "mrc", "{}"), MRClinic.class);
	//	bzfxHs = (LinearLayout) getView().findViewById(R.id.bzfxHs);
		zyzdHs = (LinearLayout) getView().findViewById(R.id.zyzdHs);
		xyzdHs = (LinearLayout) getView().findViewById(R.id.xyzdHs);
		ydt = YdtApplication.getInstance();

		txtbm.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence text, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				if(!text.toString().equals(ydt.getMrc().getBingming())){//跟原来不一样
					searchZZBM(text.toString());
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		zyzdHs.removeAllViews();
		xyzdHs.removeAllViews();

		src = StringUtil.getInfo(getActivity(), "addsrc", "");
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;
		Bitmap bm;
		// 加载图片
		intent = new Intent(getActivity(), PhotoActivity.class);
		StringUtil.saveInfo(getActivity(), "imgPath", src);

		// 加载文字
		if (ydt.getMrc() != null) {
			if (!StringUtil.isEmpty(ydt.getMrc().getZhongyizhenduan())) {
				txtzyzd.setText(ydt.getMrc().getZhongyizhenduan());
			}
			if (!StringUtil.isEmpty(ydt.getMrc().getXiyizhenduan())) {
				txtxyzd.setText(ydt.getMrc().getXiyizhenduan());
			}
			if (!StringUtil.isEmpty(ydt.getMrc().getBingming())) {
				txtbm.setText(ydt.getMrc().getBingming());
			}
			txtzyzd.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int arg1, int arg2,
						int arg3) {
					// TODO Auto-generated method stub
					ydt.getMrc().setZhongyizhenduan(s.toString());
				}

				@Override
				public void beforeTextChanged(CharSequence arg0, int arg1,
						int arg2, int arg3) {
					// TODO Auto-generated method stub

				}

				@Override
				public void afterTextChanged(Editable arg0) {
					// TODO Auto-generated method stub

				}
			});
			txtxyzd.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int arg1, int arg2,
						int arg3) {
					// TODO Auto-generated method stub
					ydt.getMrc().setXiyizhenduan(s.toString());
				}

				@Override
				public void beforeTextChanged(CharSequence arg0, int arg1,
						int arg2, int arg3) {
					// TODO Auto-generated method stub

				}

				@Override
				public void afterTextChanged(Editable arg0) {
					// TODO Auto-generated method stub

				}
			});
			txtbm.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int arg1, int arg2,
						int arg3) {
					// TODO Auto-generated method stub
					ydt.getMrc().setBingming(s.toString());
				}

				@Override
				public void beforeTextChanged(CharSequence arg0, int arg1,
						int arg2, int arg3) {
					// TODO Auto-generated method stub

				}

				@Override
				public void afterTextChanged(Editable arg0) {
					// TODO Auto-generated method stub

				}
			});

		}

		// 加载图片
		files = FileUtil.getImageFiles(src, Constants.ZYZD);

		// 加载文件
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				vv = LayoutInflater.from(getActivity()).inflate(
						R.layout.image_show, null);
				image = (ImageView) vv.findViewById(R.id.imgMain);
				imageBo = (ImageView) vv.findViewById(R.id.imgBottom);

				if (files[i].getName().contains("-upload")) {
					imageBo.setVisibility(View.VISIBLE);
				}
				checkBox = (CheckBox) vv.findViewById(R.id.checkBox);
				if (Constants.isCheckAll) {
					checkBox.setChecked(true);
					Constants.checkMap.put(files[i].getName(),
							files[i].getName());
				}
				if (Constants.isShow) {
					checkBox.setVisibility(View.VISIBLE);
					checkBox.setTag(files[i].getName());
					checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton v,
								boolean checked) {
							// TODO Auto-generated method stub
							if (checked) {
								Constants.checkMap.put(v.getTag().toString(), v
										.getTag().toString());
							} else {
								Constants.checkMap
										.remove(v.getTag().toString());
							}
						}
					});
				}
				bm = BitmapFactory.decodeFile(files[i].getAbsolutePath(),
						options);
				image.setImageBitmap(bm);
				zyzdHs.addView(vv);
				image.setTag(i);
				image.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						intent.putExtra("filter", Constants.ZYZD);
						intent.putExtra("index", v.getTag().toString());
						startActivity(intent);
					}
				});
			}
		}


		// 加载声音
		files = FileUtil.getVoiceFiles(src, Constants.ZYZD);
		// 加载文件
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				vv = LayoutInflater.from(getActivity()).inflate(
						R.layout.image_show, null);
				image = (ImageView) vv.findViewById(R.id.imgMain);
				imageBo = (ImageView) vv.findViewById(R.id.imgBottom);
				if (files[i].getName().contains("-upload")) {
					imageBo.setVisibility(View.VISIBLE);
				}
				image.setImageResource(R.drawable.audio_icon);
				image.setTag(files[i].getAbsolutePath());
				image.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						intent = new Intent(getActivity(),
								VoicePlayerActivity.class);
						intent.putExtra("srcPath", v.getTag().toString());
						startActivity(intent);
					}
				});
				checkBox = (CheckBox) vv.findViewById(R.id.checkBox);
				if (Constants.isCheckAll) {
					checkBox.setChecked(true);
					Constants.checkMap.put(files[i].getName(),
							files[i].getName());
				}
				if (Constants.isShow) {
					checkBox.setVisibility(View.VISIBLE);
					checkBox.setTag(files[i].getName());
					checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton v,
								boolean checked) {
							// TODO Auto-generated method stub
							if (checked) {
								Constants.checkMap.put(v.getTag().toString(), v
										.getTag().toString());
							} else {
								Constants.checkMap
										.remove(v.getTag().toString());
							}
						}
					});
				}
				zyzdHs.addView(vv);
			}
		}
		// 加载图片
		files = FileUtil.getImageFiles(src, Constants.XYZD);

		// 加载文件
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				vv = LayoutInflater.from(getActivity()).inflate(
						R.layout.image_show, null);
				image = (ImageView) vv.findViewById(R.id.imgMain);
				imageBo = (ImageView) vv.findViewById(R.id.imgBottom);
				checkBox = (CheckBox) vv.findViewById(R.id.checkBox);

				if (Constants.isShow) {
					checkBox.setVisibility(View.VISIBLE);
					checkBox.setTag(files[i].getName());
					checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton v,
								boolean checked) {
							if (checked) {
								Constants.checkMap.put(v.getTag().toString(), v
										.getTag().toString());
							} else {
								Constants.checkMap
										.remove(v.getTag().toString());
							}
						}
					});

				}
				if (Constants.isCheckAll) {
					checkBox.setChecked(true);
					Constants.checkMap.put(files[i].getName(),
							files[i].getName());
				}
				if (files[i].getName().contains("-upload")) {
					imageBo.setVisibility(View.VISIBLE);
				}
				bm = BitmapFactory.decodeFile(files[i].getAbsolutePath(),
						options);
				image.setImageBitmap(bm);
				xyzdHs.addView(vv);
				image.setTag(i);
				image.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						intent.putExtra("filter", Constants.XYZD);
						intent.putExtra("index", v.getTag().toString());
						startActivity(intent);
					}
				});
			}
		}
		// 加载声音
		files = FileUtil.getVoiceFiles(src, Constants.XYZD);
		// 加载文件
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				vv = LayoutInflater.from(getActivity()).inflate(
						R.layout.image_show, null);
				image = (ImageView) vv.findViewById(R.id.imgMain);
				imageBo = (ImageView) vv.findViewById(R.id.imgBottom);
				if (files[i].getName().contains("-upload")) {
					imageBo.setVisibility(View.VISIBLE);
				}
				checkBox = (CheckBox) vv.findViewById(R.id.checkBox);
				if (Constants.isCheckAll) {
					checkBox.setChecked(true);
					Constants.checkMap.put(files[i].getName(),
							files[i].getName());
				}
				if (Constants.isShow) {
					checkBox.setVisibility(View.VISIBLE);
					checkBox.setTag(files[i].getName());
					checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton v,
								boolean checked) {
							// TODO Auto-generated method stub
							if (checked) {
								Constants.checkMap.put(v.getTag().toString(), v
										.getTag().toString());
							} else {
								Constants.checkMap
										.remove(v.getTag().toString());
							}
						}
					});
				}
				image.setImageResource(R.drawable.audio_icon);
				image.setTag(files[i].getAbsolutePath());
				image.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						intent = new Intent(getActivity(),
								VoicePlayerActivity.class);
						intent.putExtra("srcPath", v.getTag().toString());
						startActivity(intent);
					}
				});
				xyzdHs.addView(vv);
			}
		}

	}

	private void searchZZBM(String zhengh) {
		if (!StringUtil.isEmpty(zhengh)) {
			if (zZBMTask != null
					&& zZBMTask.getStatus() == AsyncTask.Status.RUNNING) {
				zZBMTask.cancel(true); // 如果Task还在运行，则先取消它
			}

			zZBMTask = new ZZBMTask(getActivity(), zhengh);
			zZBMTask.execute();
		}
	}

	// 症候病名
	public class ZZBMTask extends AsyncTask<Integer, String, Integer> {
		private Context mainFrame = null;
		private String BingM;

		public ZZBMTask(Context mainFrame, String BingM) {
			this.mainFrame = mainFrame;
			this.BingM = BingM;
		}

		@Override
		protected void onCancelled() {
			hideCustomProgressDialog();
			super.onCancelled();
		}

		@Override
		protected Integer doInBackground(Integer... params) {

			map = new HashMap<String, String>();
			map.put("keyword", BingM);
			String result = HttpUtil.doPostForm(map, Constants.BMFJ, false,
					getActivity());
			if (StringUtil.isEmpty(result)) {
				msg = "连接服务器超时，请确认网络畅通";
				return Constants.FAILURE;
			}
			Log.d("********症候*******", result);
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
				// clc =GsonUtil.getObject(result, ClassicRote.class);
				// Log.d("********patientList*******", clc.getKeyword());
				jsonArray = new JSONArray(result);

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
				zHongYZH = new StringBuilder();
//				for (int i = 0; i < jsonArray.length(); i++) {
//					try {
//						zHongYZH.append(jsonArray.getJSONObject(i).getString(
//								"name"));
//						zHongYZH.append(",");
//						Log.d("******JSONOBJ******", jsonArray.getJSONObject(i)
//								.getString("name"));
//					} catch (JSONException e) {
//						// TODO Auto-generated catch block
//						Log.d("******JSONOBJ******", e.getMessage());
//					}
//				}
				Log.d("******JSONOBJZH******", zHongYZH.toString());
				try {
					ydt.getMrc().setFangming(jsonArray.getJSONObject(0).getString("name"));
					ydt.getMrc().setFangjizucheng(jsonArray.getJSONObject(0).getString("desp"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				if (!StringUtil.isEmpty(msg)) {
					AlertDialogUtil.showAlertDialog(getActivity(), msg);
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
