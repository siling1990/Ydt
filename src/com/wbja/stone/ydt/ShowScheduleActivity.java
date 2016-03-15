package com.wbja.stone.ydt;

import java.io.File;

import com.wbja.stone.ydt.entity.Schedule;
import com.wbja.stone.ydt.util.Constants;
import com.wbja.stone.ydt.util.GsonUtil;
import com.wbja.stone.ydt.util.StringUtil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ShowScheduleActivity extends Activity {

	private TextView headTitle,content,txttel;
	private ImageView img;
	private ImageView imgV;
	private Schedule sch;
	private Intent intent;
	private LinearLayout mainContentVoice;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_schedule);
		headTitle=(TextView)findViewById(R.id.headTitle);
		content=(TextView)findViewById(R.id.content);
		txttel=(TextView)findViewById(R.id.txttel);
		mainContentVoice = (LinearLayout)findViewById(R.id.mainContentVoice);
		img=(ImageView)findViewById(R.id.img);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;
		Bitmap bm;
		headTitle.setText("备忘事项");
		Log.d("*******schedule******", StringUtil.getInfo(ShowScheduleActivity.this, "shcedule", "{}"));
		sch=GsonUtil.getObject(StringUtil.getInfo(ShowScheduleActivity.this, "shcedule", "{}"), Schedule.class);
		String[] schType=getResources().getStringArray(R.array.schtype);
		if(sch!=null){
			content.setText("类型："+schType[sch.getType()]+"\n日期："+sch.getDate()+"\n时间："+sch.getTime()+"\n主题："+sch.getContent());
			
			if(!StringUtil.isEmpty(sch.getContel())){
				txttel.setText("联系人电话(点击拨打)："+sch.getContel());
				txttel.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+sch.getContel())); 
					    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
					    startActivity(intent);
					}
				});
			}
			
			bm = BitmapFactory.decodeFile(sch.getImgPath(),options);
			img.setImageBitmap(bm);
			img.setTag(sch.getImgPath());
			img.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					intent=new Intent();
					 intent.setAction(android.content.Intent.ACTION_VIEW);
				     intent.setDataAndType(Uri.fromFile(new File(v.getTag().toString())), "image/*");
					startActivity(intent);
				}
			});
			
			if(!StringUtil.isEmpty(sch.getVoiPath())){
				imgV=new ImageView(ShowScheduleActivity.this);
				imgV.setImageResource(R.drawable.audio_icon);
				imgV.setTag(sch.getVoiPath());
				imgV.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						intent = new Intent(ShowScheduleActivity.this,
								VoicePlayerActivity.class);
						intent.putExtra("srcPath", v.getTag().toString());
						startActivity(intent);
					}
				});
				mainContentVoice.addView(imgV);
			}
			
		}
		
	}
	public void back(View view){
		this.finish();
	}
}
