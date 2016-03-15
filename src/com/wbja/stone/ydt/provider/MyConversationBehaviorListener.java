package com.wbja.stone.ydt.provider;

import io.rong.imkit.RongIM.ConversationBehaviorListener;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.imlib.model.Conversation.ConversationType;
import android.content.Context;
import android.view.View;
import android.widget.Toast;


public class MyConversationBehaviorListener implements ConversationBehaviorListener{
	
	@Override
	public boolean onMessageClick(Context context, View arg1, Message message) {
		// TODO Auto-generated method stub
		//if(message.getConversationType().equals("subconversation")){
			Toast.makeText(context, "Ìø×ª", Toast.LENGTH_SHORT).show();
		//}
		return false;
	}
	
	@Override
	public boolean onMessageLongClick(Context arg0, View arg1, Message arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onUserPortraitClick(Context arg0, ConversationType arg1,
			UserInfo arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onUserPortraitLongClick(Context arg0,
			ConversationType arg1, UserInfo arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onMessageLinkClick(Context arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
