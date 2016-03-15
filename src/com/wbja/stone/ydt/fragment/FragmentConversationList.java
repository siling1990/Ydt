package com.wbja.stone.ydt.fragment;


import com.wbja.stone.ydt.R;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentConversationList extends Fragment{
	private View rootView;
	
	private TextView headTitle;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 if (rootView == null)
		    {
		      rootView = inflater.inflate(R.layout.fragment_conversation_list, null);
		    }
		    // �����rootView��Ҫ�ж��Ƿ��Ѿ����ӹ�parent�������parent��Ҫ��parentɾ����Ҫ��Ȼ�ᷢ�����rootview�Ѿ���parent�Ĵ���
		    ViewGroup parent = (ViewGroup) rootView.getParent();
		    if (parent != null)
		    {
		      parent.removeView(rootView);
		    }
		    return rootView;
	}
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		headTitle=(TextView) getView().findViewById(R.id.headTitle);
		headTitle.setText("��Ϣ");
		
		ConversationListFragment fragment = new ConversationListFragment();
		Uri uri = Uri.parse("rong://" +getActivity().getApplicationInfo().packageName).buildUpon()
		        .appendPath("conversationlist")
		        .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //����˽�ĻỰ�Ƿ�ۺ���ʾ
		        .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")
		       .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")
		        .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")
		        .build();
		fragment.setUri(uri);

		//fragment.onItemClick(arg0, arg1, arg2, arg3)
		FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
		//xxx Ϊ��Ҫ���ص� Id
		transaction.add(R.id.conversationlist, fragment);
		transaction.commit();
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
}
