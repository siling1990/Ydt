package com.wbja.stone.ydt.fragment;

import java.io.File;

import com.wbja.stone.ydt.KxzCollectActivity;
import com.wbja.stone.ydt.PhotoActivity;
import com.wbja.stone.ydt.R;
import com.wbja.stone.ydt.TabAddMRCActivity;
import com.wbja.stone.ydt.VoicePlayerActivity;
import com.wbja.stone.ydt.YdtApplication;
import com.wbja.stone.ydt.entity.MRClinic;
import com.wbja.stone.ydt.util.Constants;
import com.wbja.stone.ydt.util.FileUtil;
import com.wbja.stone.ydt.util.GsonUtil;
import com.wbja.stone.ydt.util.StringUtil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
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

public class FragmentLczz extends Fragment {
	private TextView addkxz, txtkxz;
	private EditText txtzs, txtsx, txtmx;
	private LinearLayout zsHs, kxzHs, sxHs, mxHs;
	private Intent intent;
	private File[] files;
	private ImageView image;
	private String src;
	private ImageView imageBo;
	private CheckBox checkBox;
	private View vv;
	private MRClinic mrc;
	private YdtApplication ydt;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle zsHs) {

		return inflater.inflate(R.layout.fragment_lczz, null);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		addkxz = (TextView) getView().findViewById(R.id.addkxz);

		txtzs = (EditText) getView().findViewById(R.id.txtzs);
		txtkxz = (TextView) getView().findViewById(R.id.txtkxz);
		txtsx = (EditText) getView().findViewById(R.id.txtsx);
		txtmx = (EditText) getView().findViewById(R.id.txtmx);

		zsHs = (LinearLayout) getView().findViewById(R.id.zsHs);
		kxzHs = (LinearLayout) getView().findViewById(R.id.kxzHs);
		sxHs = (LinearLayout) getView().findViewById(R.id.sxHs);
		mxHs = (LinearLayout) getView().findViewById(R.id.mxHs);

		mrc = GsonUtil.getObject(
				StringUtil.getInfo(getActivity(), "mrc", "{}"), MRClinic.class);

		src=StringUtil.getInfo(getActivity(), "addsrc", "");
		addkxz.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent(getActivity(), KxzCollectActivity.class);
				intent.putExtra("type", Constants.KXZ);
				intent.putExtra("src", src);
				getActivity().startActivity(intent);
			}
		});
		ydt=YdtApplication.getInstance();
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

		zsHs.removeAllViews();
		kxzHs.removeAllViews();
		sxHs.removeAllViews();
		mxHs.removeAllViews();

		src = StringUtil.getInfo(getActivity(), "addsrc", "");
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;
		Bitmap bm;
		// 加载图片
		intent = new Intent(getActivity(), PhotoActivity.class);
		StringUtil.saveInfo(getActivity(), "imgPath", src);
		// 记载文字
		if (ydt.getMrc() != null) {
			if (!StringUtil.isEmpty(ydt.getMrc().getZhusu())) {
				txtzs.setText(ydt.getMrc().getZhusu());
			}
			if (!StringUtil.isEmpty(ydt.getMrc().getShexiang())) {
				txtsx.setText(ydt.getMrc().getShexiang());
			}
			if (!StringUtil.isEmpty(ydt.getMrc().getMaixiang())) {
				txtmx.setText(ydt.getMrc().getMaixiang());
			}
			if (!StringUtil.isEmpty(ydt.getMrc().getKexiazheng())) {
				txtkxz.setText(ydt.getMrc().getKexiazheng());
			}
			txtzs.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int arg1, int arg2,
						int arg3) {
					// TODO Auto-generated method stub
					ydt.getMrc().setZhusu(s.toString());
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
			txtsx.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int arg1, int arg2,
						int arg3) {
					// TODO Auto-generated method stub
					ydt.getMrc().setShexiang(s.toString());
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
			txtmx.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int arg1, int arg2,
						int arg3) {
					// TODO Auto-generated method stub
					ydt.getMrc().setMaixiang(s.toString());
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
		files = FileUtil.getImageFiles(src, Constants.ZS);

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
				zsHs.addView(vv);
				image.setTag(i);
				image.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						intent.putExtra("filter", Constants.ZS);
						intent.putExtra("index", v.getTag().toString());
						startActivity(intent);
					}
				});
			}
		}
		//
		files = FileUtil.getVoiceFiles(src, Constants.ZS);
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
				zsHs.addView(vv);
			}
		}
		// 加载图片
		files = FileUtil.getImageFiles(src, Constants.KXZ);

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
				kxzHs.addView(vv);
				image.setTag(i);
				image.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						intent.putExtra("filter", Constants.KXZ);
						intent.putExtra("index", v.getTag().toString());
						startActivity(intent);
					}
				});
			}
		}
		// 加载声音
		files = FileUtil.getVoiceFiles(src, Constants.KXZ);
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
				kxzHs.addView(vv);
			}
		}
		// 加载图片
				files = FileUtil.getImageFiles(src, Constants.SX);

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
										Constants.checkMap.put(v.getTag().toString(), v.getTag()
												.toString());
									} else {
										Constants.checkMap.remove(v.getTag().toString());
									}
								}
							});

						}
						if (Constants.isCheckAll) {
							checkBox.setChecked(true);
							Constants.checkMap.put(files[i].getName(), files[i].getName());
						}
						if (files[i].getName().contains("-upload")) {
							imageBo.setVisibility(View.VISIBLE);
						}
						bm = BitmapFactory.decodeFile(files[i].getAbsolutePath(),
								options);
						image.setImageBitmap(bm);
						sxHs.addView(vv);
						image.setTag(i);
						image.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								intent.putExtra("filter", Constants.SX);
								intent.putExtra("index", v.getTag().toString());
								startActivity(intent);
							}
						});
					}
				}
				//加载声音
				files = FileUtil.getVoiceFiles(src, Constants.SX);
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
							Constants.checkMap.put(files[i].getName(), files[i].getName());
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
										Constants.checkMap.put(v.getTag().toString(), v.getTag()
												.toString());
									} else {
										Constants.checkMap.remove(v.getTag().toString());
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
						sxHs.addView(vv);
					}
				}
				// 加载图片
				files = FileUtil.getImageFiles(src, Constants.MX);

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
										Constants.checkMap.put(v.getTag().toString(), v.getTag()
												.toString());
									} else {
										Constants.checkMap.remove(v.getTag().toString());
									}
								}
							});

						}
						if (Constants.isCheckAll) {
							checkBox.setChecked(true);
							Constants.checkMap.put(files[i].getName(), files[i].getName());
						}
						if (files[i].getName().contains("-upload")) {
							imageBo.setVisibility(View.VISIBLE);
						}
						bm = BitmapFactory.decodeFile(files[i].getAbsolutePath(),
								options);
						image.setImageBitmap(bm);
						mxHs.addView(vv);
						image.setTag(i);
						image.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								intent.putExtra("filter", Constants.MX);
								intent.putExtra("index", v.getTag().toString());
								startActivity(intent);
							}
						});
					}
				}
				//加载声音
				files = FileUtil.getVoiceFiles(src, Constants.MX);
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
							Constants.checkMap.put(files[i].getName(), files[i].getName());
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
										Constants.checkMap.put(v.getTag().toString(), v.getTag()
												.toString());
									} else {
										Constants.checkMap.remove(v.getTag().toString());
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
						mxHs.addView(vv);
					}
				}
		// 重新保存
		StringUtil.saveInfo(getActivity(), "mrc", GsonUtil.getJsonValue(mrc));

	}

}
