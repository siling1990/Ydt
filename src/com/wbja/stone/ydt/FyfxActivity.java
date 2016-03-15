package com.wbja.stone.ydt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class FyfxActivity extends Activity {

	private TextView headTitle;
	private Intent intent;
	private Button btSingle,btMulti;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fyfx);

		headTitle = (TextView) findViewById(R.id.headTitle);
		headTitle.setText("·½Ò©·ÖÎö");
		
		btSingle=(Button) findViewById(R.id.btSingle);
		btMulti=(Button) findViewById(R.id.btMulti);
		
		btSingle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				single();
			}
		});

	}

	public void single() {
			intent =new Intent(this, SingleAnalysisActivity.class);
			startActivity(intent);
	}

	public void multi() {
		
	}
	public void back(View view) {
		this.finish();
	}
}
