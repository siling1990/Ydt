package com.wbja.stone.ydt;

import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imkit.fragment.SubConversationListFragment;
import io.rong.imlib.model.Conversation;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

public class ConversationListActivity extends FragmentActivity {
	private TextView headTitle;
	private String type;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_conversation_list);

		//RongIM.setConversationListBehaviorListener(new MyConversationListBehaviorListener());
		headTitle = (TextView) findViewById(R.id.headTitle);
		headTitle.setText("聊天");
		
		
		ConversationListFragment fragment = new ConversationListFragment();
		Uri uri = Uri.parse("rong://" +this.getApplicationInfo().packageName).buildUpon()
		        .appendPath("conversationlist")
		        .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
		        .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")
		       .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")
		        .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")
		        .build();
		fragment.setUri(uri);

		//fragment.onItemClick(arg0, arg1, arg2, arg3)
		FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
		//xxx 为你要加载的 Id
		transaction.add(R.id.conversationlist, fragment);
		transaction.commit();
		
		
		
		Intent intent = getIntent();


//        type = intent.getData().getQueryParameter("type");
//
//		String tag = null;
//		if (intent != null) {
//			Fragment fragment = null;
//
//	        if (type.equals("group")) {
//	        	headTitle.setText(R.string.de_actionbar_sub_group);
//	        } else if (type.equals("private")) {
//	        	headTitle.setText(R.string.de_actionbar_sub_private);
//	        } else if (type.equals("discussion")) {
//	        	headTitle.setText(R.string.de_actionbar_sub_discussion);
//	        } else if (type.equals("system")) {
//	        	headTitle.setText(R.string.de_actionbar_sub_system);
//	        } else {
//	        	headTitle.setText(R.string.de_actionbar_sub_defult);
//	        }
//				tag = "subconversationlist";
//				String fragmentName = SubConversationListFragment.class
//						.getCanonicalName();
//				fragment = Fragment.instantiate(this, fragmentName);
//				
//			if (fragment != null) {
//				FragmentTransaction transaction = getSupportFragmentManager()
//						.beginTransaction();
//				transaction.add(R.id.de_content, fragment, tag);
//				transaction.commit();
//			}
//		}

	}
	
	public void back(View view){
		this.finish();
	}
}
