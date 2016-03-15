package com.wbja.stone.ydt;

import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imkit.fragment.SubConversationListFragment;

import com.wbja.stone.ydt.util.StringUtil;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

public class ConversationActivity extends FragmentActivity {
	private TextView headTitle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.conversation);
		
		headTitle = (TextView)findViewById(R.id.headTitle);
		headTitle.setText("聊天");
		Intent intent=getIntent();
		String tag = null;
		if (intent != null) {
			Fragment fragment = null;
			if (intent.getData().getPathSegments().get(0)
					.equals("conversation")) {
				String title = intent.getData().getQueryParameter("title");
				if (!StringUtil.isEmpty(title)) {
					headTitle.setText(title);
				}
				tag = "conversation";
				String fragmentName = ConversationFragment.class
						.getCanonicalName();
				fragment = Fragment.instantiate(this, fragmentName);
			} else if (intent.getData().getLastPathSegment()
					.equals("subconversationlist")) {
				headTitle.setText("系统通知");
				tag = "subconversationlist";
				String fragmentName = SubConversationListFragment.class
						.getCanonicalName();
				fragment = Fragment.instantiate(this, fragmentName);
			}
			if (fragment != null) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.de_content, fragment, tag);
                transaction.commit();
            }
		}
	}
	public void back(View view){
		this.finish();
	}

}
