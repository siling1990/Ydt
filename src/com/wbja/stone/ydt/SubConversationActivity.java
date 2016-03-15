package com.wbja.stone.ydt;



import io.rong.imkit.fragment.SubConversationListFragment;
import io.rong.imlib.model.Conversation;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

public class SubConversationActivity extends FragmentActivity {
	private TextView headTitle;
	private String targetId;
	private String targetIds;
	private String type;
	private Conversation.ConversationType mConversationType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.conversation);

		//RongIM.setConversationListBehaviorListener(new MyConversationListBehaviorListener());
		headTitle = (TextView) findViewById(R.id.headTitle);
		headTitle.setText("ÁÄÌì");
		Intent intent = getIntent();


        targetId = intent.getData().getQueryParameter("targetId");
        targetIds = intent.getData().getQueryParameter("targetIds");
        type = intent.getData().getQueryParameter("type");

		String tag = null;
		if (intent != null) {
			Fragment fragment = null;

	        if (type.equals("group")) {
	        	headTitle.setText(R.string.de_actionbar_sub_group);
	        } else if (type.equals("private")) {
	        	headTitle.setText(R.string.de_actionbar_sub_private);
	        } else if (type.equals("discussion")) {
	        	headTitle.setText(R.string.de_actionbar_sub_discussion);
	        } else if (type.equals("system")) {
	        	headTitle.setText(R.string.de_actionbar_sub_system);
	        } else {
	        	headTitle.setText(R.string.de_actionbar_sub_defult);
	        }
	       // RongIM.getInstance().getRongIMClient().getLatestMessages(type, tag, count);
				tag = "subconversationlist";
				String fragmentName = SubConversationListFragment.class
						.getCanonicalName();
				fragment = Fragment.instantiate(this, fragmentName);
				
			if (fragment != null) {
				FragmentTransaction transaction = getSupportFragmentManager()
						.beginTransaction();
				transaction.add(R.id.de_content, fragment, tag);
				transaction.commit();
			}
		}

	}
	public void back(View view){
		this.finish();
	}
}
