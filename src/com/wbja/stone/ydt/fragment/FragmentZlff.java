package com.wbja.stone.ydt.fragment;

import java.io.File;

import com.wbja.stone.ydt.PhotoActivity;
import com.wbja.stone.ydt.R;
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
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class FragmentZlff extends Fragment{
	private TextView txtzzzf,txtfm,txtzc,txtqtzl;
	private LinearLayout zzzfHs,fmHs,zcHs,qtzlHs;
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
			Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.fragment_zlff, null);		
	}
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mrc = GsonUtil.getObject(
				StringUtil.getInfo(getActivity(), "mrc", "{}"), MRClinic.class);
		txtzzzf=(TextView)getView().findViewById(R.id.txtzzzf);
		txtfm=(TextView)getView().findViewById(R.id.txtfm);
		txtzc=(TextView)getView().findViewById(R.id.txtzc);
		txtqtzl=(TextView)getView().findViewById(R.id.txtqtzl);
		
		zzzfHs=(LinearLayout)getView().findViewById(R.id.zzzfHs);
		fmHs=(LinearLayout)getView().findViewById(R.id.fmHs);
		zcHs=(LinearLayout)getView().findViewById(R.id.zcHs);
		qtzlHs=(LinearLayout)getView().findViewById(R.id.qtzlHs);
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
		
		zzzfHs.removeAllViews();
		fmHs.removeAllViews();
		zcHs.removeAllViews();
		qtzlHs.removeAllViews();
		
		src = StringUtil.getInfo(getActivity(), "addsrc", "");
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;
		Bitmap bm;
		// 加载图片
		intent = new Intent(getActivity(), PhotoActivity.class);
		StringUtil.saveInfo(getActivity(), "imgPath", src);
		// 记载文字
		if (ydt.getMrc() != null) {
			if (!StringUtil.isEmpty(ydt.getMrc().getZhizezhifa())) {
				txtzzzf.setText(ydt.getMrc().getZhizezhifa());
			}
			if (!StringUtil.isEmpty(ydt.getMrc().getFangming())) {
				txtfm.setText(ydt.getMrc().getFangming());
			}
			if (!StringUtil.isEmpty(ydt.getMrc().getFangjizucheng())) {
				txtzc.setText(ydt.getMrc().getFangjizucheng());
			}
			if (!StringUtil.isEmpty(ydt.getMrc().getQitazhiliao())) {
				txtqtzl.setText(ydt.getMrc().getQitazhiliao());
			}
			txtzzzf.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int arg1, int arg2,
						int arg3) {
					// TODO Auto-generated method stub
					ydt.getMrc().setZhizezhifa(s.toString());
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
			txtfm.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int arg1, int arg2,
						int arg3) {
					// TODO Auto-generated method stub
					ydt.getMrc().setFangming(s.toString());
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
			txtzc.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int arg1, int arg2,
						int arg3) {
					// TODO Auto-generated method stub
					ydt.getMrc().setFangjizucheng(s.toString());
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
			txtqtzl.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int arg1, int arg2,
						int arg3) {
					// TODO Auto-generated method stub
					ydt.getMrc().setQitazhiliao(s.toString());
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
		files = FileUtil.getImageFiles(src, Constants.ZZZF);

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
				bm = BitmapFactory.decodeFile(files[i].getAbsolutePath(),
						options);
				image.setImageBitmap(bm);
				zzzfHs.addView(vv);
				image.setTag(i);
				image.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						intent.putExtra("filter", Constants.ZZZF);
						intent.putExtra("index", v.getTag().toString());
						startActivity(intent);
					}
				});
			}
		}
		//
		files = FileUtil.getVoiceFiles(src, Constants.ZZZF);
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
				zzzfHs.addView(vv);
			}
		}
		// 加载图片
				files = FileUtil.getImageFiles(src, Constants.FM);

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
						fmHs.addView(vv);
						image.setTag(i);
						image.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								intent.putExtra("filter", Constants.FM);
								intent.putExtra("index", v.getTag().toString());
								startActivity(intent);
							}
						});
					}
				}
				// 加载声音
				files = FileUtil.getVoiceFiles(src, Constants.FM);
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
						fmHs.addView(vv);
					}
				}
				// 加载图片
				files = FileUtil.getImageFiles(src, Constants.ZC);

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
						zcHs.addView(vv);
						image.setTag(i);
						image.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								intent.putExtra("filter", Constants.ZC);
								intent.putExtra("index", v.getTag().toString());
								startActivity(intent);
							}
						});
					}
				}
				// 加载声音
				files = FileUtil.getVoiceFiles(src, Constants.ZC);
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
						zcHs.addView(vv);
					}
				}
				// 加载图片
				files = FileUtil.getImageFiles(src, Constants.QTZL);

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
						qtzlHs.addView(vv);
						image.setTag(i);
						image.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								intent.putExtra("filter", Constants.QTZL);
								intent.putExtra("index", v.getTag().toString());
								startActivity(intent);
							}
						});
					}
				}
				// 加载声音
				files = FileUtil.getVoiceFiles(src, Constants.QTZL);
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
						qtzlHs.addView(vv);
					}
				}
				
		// 重新保存
			StringUtil.saveInfo(getActivity(), "mrc", GsonUtil.getJsonValue(mrc));
		
	}
}
