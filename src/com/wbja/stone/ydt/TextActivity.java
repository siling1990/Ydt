package com.wbja.stone.ydt;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


import com.wbja.stone.ydt.util.Constants;
import com.wbja.stone.ydt.util.FileUtil;
import com.wbja.stone.ydt.util.StringUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class TextActivity extends Activity {

	private ImageView back;
	private EditText edContent;
	private Button btOk;
	private Button btCancle;
	private String src;
	private String type;
	private String content;
	private File[] files;
	private Intent intent ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_text);
		
		intent = getIntent();
		src=intent.getStringExtra("src");
		type=intent.getStringExtra("type");
		
		back = (ImageView) findViewById(R.id.headBack);
		back.setVisibility(View.VISIBLE);
		
		edContent=(EditText)findViewById(R.id.edContent);
		btOk=(Button)findViewById(R.id.btOk);
		btCancle=(Button)findViewById(R.id.btCancle);
		
		btCancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		btOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				intent = new Intent();
				/* 创建一个临时文件，用来存放*/
				if(files==null||files.length==0){//文件不存在
					String name = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
					String srcPath = src + type + name + ".txt";
					Log.d("********Text PATH************", srcPath + "----------");
					FileUtil.writeSDFile(srcPath,edContent.getText().toString());
					intent.putExtra(Constants.TEXTSTR,srcPath);
				}else{
					FileUtil.writeSDFile(files[0].getAbsolutePath(),edContent.getText().toString());
					intent.putExtra(Constants.TEXTSTR,files[0].getAbsolutePath());
				}
				
				setResult(RESULT_OK, intent);
				
				finish(); 
			}
		});
		//TODO
		if(!StringUtil.isEmpty(src)&&!StringUtil.isEmpty(type)){
			files=FileUtil.getTextFiles(src,type);
			if(files!=null&&files.length>0){
				content=FileUtil.readSDFile(files[0].getAbsolutePath());
				edContent.setText(content);
			}
		}
		
		
	}
	
	public void back(View view) {
		this.finish();
	}
	
}
