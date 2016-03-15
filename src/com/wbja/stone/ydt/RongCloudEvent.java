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
     * ��ʼ�� RongCloud.
     *
     * @param context �����ġ�
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
	 * ���췽����
	 *
	 * @param context
	 *            �����ġ�
	 */
	private RongCloudEvent(Context context) {
		mContext = context;
		initDefaultListener();
		// mHandler = new Handler(this);
	}

	/**
	 * RongIM.init(this) ��ֱ�ӿ�ע���Listener��
	 */
	private void initDefaultListener() {
		RongIM.setUserInfoProvider(this, true);// �����û���Ϣ�ṩ�ߡ�
		// RongIM.setGroupInfoProvider(this, true);//����Ⱥ����Ϣ�ṩ�ߡ�
		// RongIM.setConversationBehaviorListener(this);//���ûỰ��������ļ�������
		RongIM.setLocationProvider(this);// ���õ���λ���ṩ��,����λ�õ�ͬѧ����ע�����д���
		RongIM.setConversationListBehaviorListener(this);
		RongIM.getInstance().setMessageAttachedUserInfo(true);
		// RongIM.setPushMessageBehaviorListener(this);//�Զ��� push ֪ͨ��
	}

	/**
	 * ���ӳɹ�ע�ᡣ
	 * <p/>
	 * ��RongIM-connect-onSuccess����á�
	 */
	public void setOtherListener() {
//		RongIM.getInstance().getRongIMClient().setOnReceiveMessageListener(this);// ������Ϣ���ռ�������
		// RongIM.getInstance().setSendMessageListener(this);//���÷�����Ϣ���ռ�����.
		RongIM.getInstance().getRongIMClient().setConnectionStatusListener(this);// ��������״̬��������

		// //��չ�����Զ���
		InputProvider.ExtendProvider[] provider = {
				// new PhotoCollectionsProvider(RongContext.getInstance()),//ͼƬ
				new CameraInputProvider(RongContext.getInstance()), // ���
				new LocationInputProvider(RongContext.getInstance()), // ����λ��
				new VoIPInputProvider(RongContext.getInstance()),// ����ͨ��
				// new ContactsProvider(RongContext.getInstance())//ͨѶ¼
		};
		InputProvider.ExtendProvider[] provider1 = {
				// new PhotoCollectionsProvider(RongContext.getInstance()),//ͼƬ
				new CameraInputProvider(RongContext.getInstance()), // ���
				new LocationInputProvider(RongContext.getInstance()),// ����λ��
		};
		RongIM.getInstance().resetInputExtensionProvider(Conversation.ConversationType.PRIVATE, provider);
		RongIM.getInstance().resetInputExtensionProvider(Conversation.ConversationType.DISCUSSION, provider1);
		RongIM.getInstance().resetInputExtensionProvider(Conversation.ConversationType.GROUP, provider1);
		RongIM.getInstance().resetInputExtensionProvider(Conversation.ConversationType.CUSTOMER_SERVICE, provider1);
		// RongIM.getInstance().setPrimaryInputProvider(new
		// InputTestProvider((RongContext) mContext));

	}

	/**
	 * ��ȡRongCloud ʵ����
	 *
	 * @return RongCloud��
	 */
	public static RongCloudEvent getInstance() {
		return mRongCloudInstance;
	}
	 /**
     * ����Ự�б� item ��ִ�С�
     *
     * @param context      �����ġ�
     * @param view         ��������� View��
     * @param conversation �Ự��Ŀ��
     * @return ���� true ����ִ������ SDK �߼������� false ��ִ������ SDK �߼���ִ�и÷�����
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
						new AlertDialog.Builder(context).setMessage("ȷ�Ϻ�������")
								.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog, int whichButton) {

									}
								}).setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

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
	 * ����״̬���������Ի�ȡ�������״̬:ConnectionStatusListener �Ļص�����������״̬�仯ʱִ�С�
	 *
	 * @param status
	 *            ����״̬��
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

		if (messageContent instanceof TextMessage) {// �ı���Ϣ
			TextMessage textMessage = (TextMessage) messageContent;
			Log.d(TAG, "onReceived-TextMessage:" + textMessage.getContent());
			if (textMessage.getContent().equals("11")) {
				Log.e(TAG, "---onReceived--111111--");
				return false;
			}
		} else if (messageContent instanceof ImageMessage) {// ͼƬ��Ϣ
			ImageMessage imageMessage = (ImageMessage) messageContent;
			Log.d(TAG, "onReceived-ImageMessage:" + imageMessage.getRemoteUri());
		} else if (messageContent instanceof VoiceMessage) {// ������Ϣ
			VoiceMessage voiceMessage = (VoiceMessage) messageContent;
			Log.d(TAG, "onReceived-voiceMessage:" + voiceMessage.getUri().toString());
		} else if (messageContent instanceof RichContentMessage) {// ͼ����Ϣ
			RichContentMessage richContentMessage = (RichContentMessage) messageContent;
			Log.d(TAG, "onReceived-RichContentMessage:" + richContentMessage.getContent());
		} else if (messageContent instanceof InformationNotificationMessage) {// С������Ϣ
			InformationNotificationMessage informationNotificationMessage = (InformationNotificationMessage) messageContent;
			Log.d(TAG, "onReceived-informationNotificationMessage:" + informationNotificationMessage.getMessage());
			// if (DemoContext.getInstance() != null)
			// getFriendByUserIdHttpRequest =
			// DemoContext.getInstance().getDemoApi().getUserInfoByUserId(message.getSenderUserId(),
			// (ApiCallback<User>) this);
		}
		// else if (messageContent instanceof DeAgreedFriendRequestMessage)
		// {//������ӳɹ���Ϣ
		// DeAgreedFriendRequestMessage deAgreedFriendRequestMessage =
		// (DeAgreedFriendRequestMessage) messageContent;
		// Log.d(TAG, "onReceived-deAgreedFriendRequestMessage:" +
		// deAgreedFriendRequestMessage.getMessage());
		// receiveAgreeSuccess(deAgreedFriendRequestMessage);
		// }
		else if (messageContent instanceof ContactNotificationMessage) {// ���������Ϣ
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
			Log.d(TAG, "onReceived-������Ϣ���Լ����жϴ���");
		}

		return false;
	}

}
