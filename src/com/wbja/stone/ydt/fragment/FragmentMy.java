package com.wbja.stone.ydt.fragment;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.wbja.stone.ydt.R;
import com.wbja.stone.ydt.util.Constants;
import com.wbja.stone.ydt.util.StringUtil;
import com.wbja.stone.ydt.window.CircleImageView;
import com.wbja.stone.ydt.RegListActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FragmentMy extends Fragment {

	private ImageLoader imageLoader = ImageLoader.getInstance();//
	private DisplayImageOptions options; // 显示图像设置
	
	private TextView headTitle;
	private TextView txtLogin,txtSum;
	private RelativeLayout myTitle;
	private RelativeLayout logout;
	private CircleImageView imgUser;
	private RelativeLayout reglist;
	private Intent intent;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment_my, null);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
		// 使用DisplayImageOptions.Builder()创建DisplayImageOptions
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.default_contacts) // 设置图片下载期间显示的图�?
				.showImageForEmptyUri(R.drawable.default_contacts) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.default_contacts) // 设置图片加载或解码过程中发生错误显示的图�?
				.cacheInMemory() // 设置下载的图片是否缓存在内存�?
				.cacheOnDisc() // 设置下载的图片是否缓存在SD卡中
			//	.displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图�?
				.build(); // 创建配置过得DisplayImageOption对象
		headTitle = (TextView) getView().findViewById(R.id.headTitle);
		myTitle = (RelativeLayout) getView().findViewById(R.id.myTitle);
		logout = (RelativeLayout) getView().findViewById(R.id.logout);
		reglist = (RelativeLayout) getView().findViewById(R.id.reglist);
		txtLogin = (TextView) getView().findViewById(R.id.txtLogin);
		txtSum = (TextView) getView().findViewById(R.id.txtSum);
		imgUser = (CircleImageView) getView().findViewById(R.id.imgUser);

		imageLoader.displayImage(Constants.HOST+"/download/doctor/"+StringUtil.getID_User(getActivity())+".jpg",imgUser, options);
		
		headTitle.setText("我");
		
		logout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				StringUtil.removeInfo(getActivity(), "docname");
				StringUtil.removeInfo(getActivity(), "token");
				myTitle.setClickable(true);
				txtLogin.setText("马上登录");
				txtSum.setText("");
			//	imgUser.setImageResource(R.drawable.pic_default);
				logout.setVisibility(View.GONE);
			}
		});
		reglist.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				 intent=new Intent(getActivity(), RegListActivity.class);
				 getActivity().startActivity(intent);
			}
		});
		
		String docname = StringUtil.getInfo(getActivity(), Constants.DOCNAME, "");
		String hosName = StringUtil.getInfo(getActivity(), Constants.HOSPNAME, "");
		String deptName = StringUtil.getInfo(getActivity(), Constants.DEPTNAME, "");
		if (!StringUtil.isEmpty(docname)) {
			myTitle.setClickable(false);
			txtLogin.setText(docname);
		//	imgUser.setImageResource(R.drawable.pic_male);
			logout.setVisibility(View.VISIBLE);
		}
		if(!StringUtil.isEmpty(deptName)){
			txtSum.setText(deptName+"\n"+hosName);
		}else{
			txtSum.setText("");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	// @Override
	// public void setUserVisibleHint(boolean isVisibleToUser) {
	// super.setUserVisibleHint(isVisibleToUser);
	// if (isVisibleToUser) {
	// //相当于Fragment的onResume
	// } else {
	// //相当于Fragment的onPause
	// }
	// }
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		String docname = StringUtil.getInfo(getActivity(), "docname", "");

		if (docname != null && !docname.isEmpty()) {
			myTitle.setClickable(false);
			txtLogin.setText(docname);
		//	imgUser.setImageResource(R.drawable.pic_male);
			logout.setVisibility(View.VISIBLE);
		}

	}
}
