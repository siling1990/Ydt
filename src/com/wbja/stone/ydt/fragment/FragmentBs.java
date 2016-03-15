package com.wbja.stone.ydt.fragment;

import java.io.File;

import com.wbja.stone.ydt.PhotoActivity;
import com.wbja.stone.ydt.R;
import com.wbja.stone.ydt.VoicePlayerActivity;
import com.wbja.stone.ydt.YdtApplication;
import com.wbja.stone.ydt.util.Constants;
import com.wbja.stone.ydt.util.FileUtil;
import com.wbja.stone.ydt.util.StringUtil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class FragmentBs extends Fragment {
	private EditText txtjws, txtxbs;
	private LinearLayout jwsHs, xbsHs;
	private Intent intent;
	private File[] files;
	private TextView txtImg;
	private ImageView image;
	private String src;
	private CheckBox checkBox;
	private ImageView imageBo;
	private View vv;
	private YdtApplication ydt;

	public FragmentBs() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d("********Bsoncreateview******", "");
		return inflater.inflate(R.layout.fragment_bs, null);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		Log.d("********BsoncreateAct******", "");
		src = StringUtil.getInfo(getActivity(), "addsrc", "");

		ydt = YdtApplication.getInstance();

		txtjws = (EditText) getView().findViewById(R.id.txtjws);
		jwsHs = (LinearLayout) getView().findViewById(R.id.jwsHs);
		txtxbs = (EditText) getView().findViewById(R.id.txtxbs);
		xbsHs = (LinearLayout) getView().findViewById(R.id.xbsHs);

		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.d("********Bsoncreate******", "");
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		Log.d("********BsonResume*******", "");
		jwsHs.removeAllViews();
		xbsHs.removeAllViews();

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;
		Bitmap bm;
		// 加载图片
		intent = new Intent(getActivity(), PhotoActivity.class);
		StringUtil.saveInfo(getActivity(), "imgPath", src);

		if (ydt.getMrc() != null) {
			if (!StringUtil.isEmpty(ydt.getMrc().getJiwangshi())) {
				txtjws.setText(ydt.getMrc().getJiwangshi());
			}
			if (!StringUtil.isEmpty(ydt.getMrc().getXianbingshi())) {
				txtxbs.setText(ydt.getMrc().getXianbingshi());
			}
			txtjws.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int arg1, int arg2,
						int arg3) {
					// TODO Auto-generated method stub
					ydt.getMrc().setJiwangshi(s.toString());
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
			txtxbs.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int arg1, int arg2,
						int arg3) {
					// TODO Auto-generated method stub
					ydt.getMrc().setXianbingshi(s.toString());
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
		files = FileUtil.getImageFiles(src, Constants.JWS);

		// 加载文件
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				vv = LayoutInflater.from(getActivity()).inflate(
						R.layout.image_show, null);
				image = (ImageView) vv.findViewById(R.id.imgMain);
				imageBo = (ImageView) vv.findViewById(R.id.imgBottom);
				checkBox = (CheckBox) vv.findViewById(R.id.checkBox);
				txtImg = (TextView) vv.findViewById(R.id.txtImg);

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
				int post = -1;

				if (files[i].getName().contains("-upload")) {
					imageBo.setVisibility(View.VISIBLE);
					try {
						post = Integer
								.parseInt(files[i].getName().split("-")[2]);
					} catch (Exception e) {

					}
					if (post > -1
							&& post < getResources().getStringArray(
									R.array.bsname).length - 1) {
						txtImg.setText(getResources().getStringArray(
								R.array.bsname)[post]);
					}
				} else {
					try {
						post = Integer
								.parseInt(files[i].getName().split("-")[1]);
					} catch (Exception e) {

					}
					if (post > -1
							&& post < getResources().getStringArray(
									R.array.bsname).length - 1) {
						txtImg.setText(getResources().getStringArray(
								R.array.bsname)[post]);
					}
				}
				bm = BitmapFactory.decodeFile(files[i].getAbsolutePath(),
						options);
				image.setImageBitmap(bm);
				jwsHs.addView(vv);
				image.setTag(i);
				image.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						intent.putExtra("filter", Constants.JWS);
						intent.putExtra("index", v.getTag().toString());
						startActivity(intent);
					}
				});
			}
		}
		// 加载声音
		files = FileUtil.getVoiceFiles(src, Constants.JWS);
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
				jwsHs.addView(vv);
			}
		}

		// 加载图片
		files = FileUtil.getImageFiles(src, Constants.XBS);

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
				xbsHs.addView(vv);
				image.setTag(i);
				image.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						intent.putExtra("filter", Constants.XBS);
						intent.putExtra("index", v.getTag().toString());
						startActivity(intent);
					}
				});
			}
		}
		// 加载声音
		files = FileUtil.getVoiceFiles(src, Constants.XBS);
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
				xbsHs.addView(vv);
			}
		}
		super.onResume();
	}
}
