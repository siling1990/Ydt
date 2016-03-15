package com.wbja.stone.ydt;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import com.wbja.stone.ydt.adapter.ImageAdapter;
import com.wbja.stone.ydt.util.FileUtil;
import com.wbja.stone.ydt.util.StringUtil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class PhotoActivity extends FragmentActivity {

	private List<View> guides = new ArrayList<View>();
	private ViewPager pager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo);
		
		Intent intent =getIntent();
		String imgPath=StringUtil.getInfo(PhotoActivity.this, "imgPath", "");
		String filter=intent.getStringExtra("filter");
		String index=intent.getStringExtra("index");
		int in=Integer.parseInt(index);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;
		Bitmap bm;
		File[] files=FileUtil.getImageFiles(imgPath, filter);
		for (int i = 0; i < files.length; i++) {
			ImageView iv = new ImageView(this);
			bm = BitmapFactory.decodeFile(files[i].getAbsolutePath(),
					options);
			iv.setImageBitmap(bm);
			ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT);
			iv.setLayoutParams(params);
			iv.setScaleType(ScaleType.FIT_XY);
			guides.add(iv);
		}
		
		ImageAdapter adapter = new ImageAdapter(guides);
		pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(adapter);
		if(in>0){
			
		}
		pager.setCurrentItem(in);
	}
	
	

}
