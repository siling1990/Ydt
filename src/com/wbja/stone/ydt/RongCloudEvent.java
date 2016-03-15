package com.wbja.stone.ydt;

import org.json.JSONException;
import org.json.JSONObject;

import com.wbja.stone.ydt.util.Constants;
import com.wbja.stone.ydt.util.StringUtil;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.UIConversation;
import io.rong.imkit.widget.provider.CameraInputProvider;
import io.rong.imkit.widget.provider.InputProvider;
import io.rong.imkit.widget.provider.LocationInputProvider;
import io.rong.imkit.widget.provider.VoIPInputProvider;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ContactNotificationMessage;
import io.rong.message.ImageMessage;
import io.rong.message.InformationNotificationMessage;
import io.rong.message.RichContentMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

public final class RongCloudEvent implements RongIMClient.OnReceiveMessageListener, RongIM.UserInfoProvider,
		RongIMClient.ConnectionStatusListener, RongIM.LocationProvider, RongIM.ConversationListBehaviorListener {
	private static final String TAG = RongCloudEvent.class.getSimpleName();
	private static RongCloudEvent mRongCloudInstance;

	private Context mContext;

	 /**
     * 初始化 RongCloud.
     *
     * @param context 上下文。
     */
    public static void init(Context context) {

        if (mRongCloudInstance == null) {

            synchronized (RongCloudEvent.class) {

                if (mRongCloudInstance == null) {
                    mRongCloudInstance = new RongCloudEvent(context);
                }
            }
        }
    }
	
	/**
	 * 构造方法。
	 *
	 * @param context
	 *            上下文。
	 */
	private RongCloudEvent(Context context) {
		mContext = context;
		initDefaultListener();
		// mHandler = new Handler(this);
	}

	/**
	 * RongIM.init(this) 后直接可注册的Listener。
	 */
	private void initDefaultListener() {
		RongIM.setUserInfoProvider(this, true);// 设置用户信息提供者。
		// RongIM.setGroupInfoProvider(this, true);//设置群组信息提供者。
		// RongIM.setConversationBehaviorListener(this);//设置会话界面操作的监听器。
		RongIM.setLocationProvider(this);// 设置地理位置提供者,不用位置的同学可以注掉此行代码
		RongIM.setConversationListBehaviorListener(this);
		RongIM.getInstance().setMessageAttachedUserInfo(true);
		// RongIM.setPushMessageBehaviorListener(this);//自定义 push 通知。
	}

	/**
	 * 连接成功注册。
	 * <p/>
	 * 在RongIM-connect-onSuccess后调用。
	 */
	public void setOtherListener() {
//		RongIM.getInstance().getRongIMClient().setOnReceiveMessageListener(this);// 设置消息接收监听器。
		// RongIM.getInstance().setSendMessageListener(this);//设置发出消息接收监听器.
		RongIM.getInstance().getRongIMClient().setConnectionStatusListener(this);// 设置连接状态监听器。

		// //扩展功能自定义
		InputProvider.ExtendProvider[] provider = {
				// new PhotoCollectionsProvider(RongContext.getInstance()),//图片
				new CameraInputProvider(RongContext.getInstance()), // 相机
				new LocationInputProvider(RongContext.getInstance()), // 地理位置
				new VoIPInputProvider(RongContext.getInstance()),// 语音通话
				// new ContactsProvider(RongContext.getInstance())//通讯录
		};
		InputProvider.ExtendProvider[] provider1 = {
				// new PhotoCollectionsProvider(RongContext.getInstance()),//图片
				new CameraInputProvider(RongContext.getInstance()), // 相机
				new LocationInputProvider(RongContext.getInstance()),// 地理位置
		};
		RongIM.getInstance().resetInputExtensionProvider(Conversation.ConversationType.PRIVATE, provider);
		RongIM.getInstance().resetInputExtensionProvider(Conversation.ConversationType.DISCUSSION, provider1);
		RongIM.getInstance().resetInputExtensionProvider(Conversation.ConversationType.GROUP, provider1);
		RongIM.getInstance().resetInputExtensionProvider(Conversation.ConversationType.CUSTOMER_SERVICE, provider1);
		// RongIM.getInstance().setPrimaryInputProvider(new
		// InputTestProvider((RongContext) mContext));

	}

	/**
	 * 获取RongCloud 实例。
	 *
	 * @return RongCloud。
	 */
	public static RongCloudEvent getInstance() {
		return mRongCloudInstance;
	}
	 /**
     * 点击会话列表 item 后执行。
     *
     * @param context      上下文。
     * @param view         触发点击的 View。
     * @param conversation 会话条目。
     * @return 返回 true 不再执行融云 SDK 逻辑，返回 false 先执行融云 SDK 逻辑再执行该方法。
     */
	@Override
	public boolean onConversationClick(Context context, View view, UIConversation conv) {
		MessageContent mc = conv.getMessageContent();

		String jsonContent = new String(mc.encode());
		JSONObject myJsonObject = null;
		boolean bo = false;
		if (mc.getClass().equals(ContactNotificationMessage.class)) {
			try {
				ContactNotificationMessage cnm=(ContactNotificationMessage)mc;
				//cnm.get
				myJsonObject = new JSONObject(jsonContent);
				if (!StringUtil.isEmpty(myJsonObject.getString("operation"))) {
					String op = myJsonObject.getString("operation");
					bo = true;
					if (op.equals(Constants.OPADDFRIEND)) {
						new AlertDialog.Builder(context).setMessage("确认好友请求？")
								.setPositiveButton("确认", new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog, int whichButton) {

									}
								}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0, int arg1) {
										// TODO Auto-generated method stub

									}
								}).show();
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
	public boolean onConversationLongClick(Context arg0, View arg1, UIConversation arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onStartLocation(Context arg0, LocationCallback arg1) {
		// TODO Auto-generated method stub

	}

	/**
	 * 连接状态监听器，以获取连接相关状态:ConnectionStatusListener 的回调方法，网络状态变化时执行。
	 *
	 * @param status
	 *            网络状态。
	 */
	@Override
	public void onChanged(ConnectionStatus status) {
		Log.d(TAG, "onChanged:" + status);
		if (status.getMessage().equals(ConnectionStatus.DISCONNECTED.getMessage())) {
		}
	}

	@Override
	public UserInfo getUserInfo(String arg0) {
		// TODO Auto-generated method stub
		return new UserInfo(StringUtil.getID_User(mContext), StringUtil.getInfo(mContext, Constants.DOCNAME, ""), null);
	}

	@Override
	public boolean onReceived(Message message, int left) {
		MessageContent messageContent = message.getContent();

		if (messageContent instanceof TextMessage) {// 文本消息
			TextMessage textMessage = (TextMessage) messageContent;
			Log.d(TAG, "onReceived-TextMessage:" + textMessage.getContent());
			if (textMessage.getContent().equals("11")) {
				Log.e(TAG, "---onReceived--111111--");
				return false;
			}
		} else if (messageContent instanceof ImageMessage) {// 图片消息
			ImageMessage imageMessage = (ImageMessage) messageContent;
			Log.d(TAG, "onReceived-ImageMessage:" + imageMessage.getRemoteUri());
		} else if (messageContent instanceof VoiceMessage) {// 语音消息
			VoiceMessage voiceMessage = (VoiceMessage) messageContent;
			Log.d(TAG, "onReceived-voiceMessage:" + voiceMessage.getUri().toString());
		} else if (messageContent instanceof RichContentMessage) {// 图文消息
			RichContentMessage richContentMessage = (RichContentMessage) messageContent;
			Log.d(TAG, "onReceived-RichContentMessage:" + richContentMessage.getContent());
		} else if (messageContent instanceof InformationNotificationMessage) {// 小灰条消息
			InformationNotificationMessage informationNotificationMessage = (InformationNotificationMessage) messageContent;
			Log.d(TAG, "onReceived-informationNotificationMessage:" + informationNotificationMessage.getMessage());
			// if (DemoContext.getInstance() != null)
			// getFriendByUserIdHttpRequest =
			// DemoContext.getInstance().getDemoApi().getUserInfoByUserId(message.getSenderUserId(),
			// (ApiCallback<User>) this);
		}
		// else if (messageContent instanceof DeAgreedFriendRequestMessage)
		// {//好友添加成功消息
		// DeAgreedFriendRequestMessage deAgreedFriendRequestMessage =
		// (DeAgreedFriendRequestMessage) messageContent;
		// Log.d(TAG, "onReceived-deAgreedFriendRequestMessage:" +
		// deAgreedFriendRequestMessage.getMessage());
		// receiveAgreeSuccess(deAgreedFriendRequestMessage);
		// }
		else if (messageContent instanceof ContactNotificationMessage) {// 好友添加消息
			ContactNotificationMessage contactContentMessage = (ContactNotificationMessage) messageContent;
			Log.d(TAG, "onReceived-ContactNotificationMessage:getExtra;" + contactContentMessage.getExtra());
			Log.d(TAG, "onReceived-ContactNotificationMessage:+getmessage:"
					+ contactContentMessage.getMessage().toString());
			// Intent in = new Intent();
			// in.setAction(MainActivity.ACTION_DMEO_RECEIVE_MESSAGE);
			// in.putExtra("rongCloud", contactContentMessage);
			// in.putExtra("has_message", true);
			// mContext.sendBroadcast(in);
		} else {
			Log.d(TAG, "onReceived-其他消息，自己来判断处理");
		}

		return false;
	}

}
