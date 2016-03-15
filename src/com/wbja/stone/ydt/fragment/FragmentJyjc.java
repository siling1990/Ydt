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

public class FragmentJyjc extends Fragment {
	private EditText txttzjc, txtfzjc;
	private LinearLayout tzjcHs, fzjcHs;
	private Intent intent;
	private File[] files;
	private TextView txtImg;
	private ImageView image;
	private String src;
	private ImageView imageBo;
	private CheckBox checkBox;
	private View vv;
	private YdtApplication ydt;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment_jyjc, null);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		txttzjc = (EditText) getView().findViewById(R.id.txttzjc);
		txtfzjc = (EditText) getView().findViewById(R.id.txtfzjc);

		tzjcHs = (LinearLayout) getView().findViewById(R.id.tzjcHs);
		fzjcHs = (LinearLayout) getView().findViewById(R.id.fzjcHs);

		ydt = YdtApplication.getInstance();
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

		tzjcHs.removeAllViews();
		fzjcHs.removeAllViews();

		src = StringUtil.getInfo(getActivity(), "addsrc", "");
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;
		Bitmap bm;
		// 加载图片
		intent = new Intent(getActivity(), PhotoActivity.class);
		StringUtil.saveInfo(getActivity(), "imgPath", src);
		// 记载文字
		if (ydt.getMrc() != null) {
			if (!StringUtil.isEmpty(ydt.getMrc().getTizhengjiancha())) {
				txttzjc.setText(ydt.getMrc().getTizhengjiancha());
			}
			if (!StringUtil.isEmpty(ydt.getMrc().getFuzhujianca())) {
				txtfzjc.setText(ydt.getMrc().getFuzhujianca());
			}
			txttzjc.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int arg1, int arg2,
						int arg3) {
					// TODO Auto-generated method stub
					ydt.getMrc().setTizhengjiancha(s.toString());
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
			txtfzjc.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int arg1, int arg2,
						int arg3) {
					// TODO Auto-generated method stub
					ydt.getMrc().setFuzhujianca(s.toString());
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
		// files = FileUtil.getTextFiles(src, "");
		//
		// // 加载文件
		// if (files != null) {
		// for (int i = 0; i < files.length; i++) {
		// if (files[i].getName().startsWith(Constants.TZJC)) {
		// txttzjc.setText(FileUtil.readSDFile(files[i]
		// .getAbsolutePath()));
		// }
		// }
		// }

		// 加载图片
		files = FileUtil.getImageFiles(src, Constants.TZJC);

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
				tzjcHs.addView(vv);
				image.setTag(i);
				image.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						intent.putExtra("filter", Constants.TZJC);
						intent.putExtra("index", v.getTag().toString());
						startActivity(intent);
					}
				});
			}
		}
		//
		files = FileUtil.getVoiceFiles(src, Constants.TZJC);
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
				tzjcHs.addView(vv);
			}
		}
		// 加载图片
		files = FileUtil.getImageFiles(src, Constants.FZJC);

		// 加载文件
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				vv = LayoutInflater.from(getActivity()).inflate(
						R.layout.image_show, null);
				image = (ImageView) vv.findViewById(R.id.imgMain);
				imageBo = (ImageView) vv.findViewById(R.id.imgBottom);
				txtImg = (TextView) vv.findViewById(R.id.txtImg);

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
				fzjcHs.addView(vv);
				image.setTag(i);
				image.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						intent.putExtra("filter", Constants.FZJC);
						intent.putExtra("index", v.getTag().toString());
						startActivity(intent);
					}
				});
			}
		}
		//
		files = FileUtil.getVoiceFiles(src, Constants.FZJC);
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
				fzjcHs.addView(vv);
			}
		}
	}
}
