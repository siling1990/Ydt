package com.wbja.stone.ydt.provider;

import org.json.JSONException;
import org.json.JSONObject;

import com.wbja.stone.ydt.util.Constants;
import com.wbja.stone.ydt.util.StringUtil;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import io.rong.imkit.RongIM.ConversationListBehaviorListener;
import io.rong.imkit.model.UIConversation;
import io.rong.imlib.model.MessageContent;
import io.rong.message.ContactNotificationMessage;

public class MyConversationListBehaviorListener implements ConversationListBehaviorListener{

	@Override
	public boolean onConversationClick(Context context, View view,
			UIConversation conv) {
		// TODO Auto-generated method stub
		
		MessageContent mc=conv.getMessageContent();
		
		String jsonContent=new String(mc.encode());
		JSONObject myJsonObject = null;
		boolean bo=false;
		if(mc.getClass().equals(ContactNotificationMessage.class)){
			try {
				myJsonObject = new JSONObject(jsonContent);
				if(!StringUtil.isEmpty(myJsonObject.getString("operation"))){
					String op=myJsonObject.getString("operation");
					bo=true;
					if(op.equals(Constants.OPADDFRIEND)){
						new AlertDialog.Builder(context)
						.setMessage("确认好友请求？")
						.setPositiveButton("确认",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int whichButton) {
										
									}
								}).setNegativeButton("取消", new DialogInterface.OnClickListener(){

									@Override
									public void onClick(DialogInterface arg0, int arg1) {
										// TODO Auto-generated method stub
										
									}}).show();
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
				
		return bo;
	}

	@Override
	public boolean onConversationLongClick(Context arg0, View arg1,
			UIConversation arg2) {
		// TODO Auto-generated method stub
		return false;
	}

}
