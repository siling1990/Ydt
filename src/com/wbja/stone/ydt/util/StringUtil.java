package com.wbja.stone.ydt.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * String ʵ����
 * 
 * @author stone
 * */
public class StringUtil {

	/**
	 * �洢�û�������
	 * */
	public static void saveLoginInfo(Context context, String username,
			String password) {
		// ��ȡSharedPreferences����
		SharedPreferences sharedPre = context.getSharedPreferences("config",
				Context.MODE_PRIVATE);
		// ��ȡEditor����
		Editor editor = sharedPre.edit();
		// ���ò���
		if(!isEmpty(username)){
			editor.putString("username", username);
		}else{
			editor.remove("username");
		}
		if(!isEmpty(password)){
			editor.putString("password", password);
		}else{
			editor.remove("password");
		}
		// �ύ
		editor.commit();
	}

	/**
	 * �洢��Ϣ
	 * */
	public static void saveInfo(Context context, String name,String value) {
		// ��ȡSharedPreferences����
		SharedPreferences sharedPre = context.getSharedPreferences("config",
				Context.MODE_PRIVATE);
		// ��ȡEditor����
		Editor editor = sharedPre.edit();
		// ���ò���
		
		if(!isEmpty(value)){
			editor.putString(name, value);
		}else{
			editor.remove(name);
		}
		// �ύ
		editor.commit();
	}
	/**
	 * �洢��Ϣ
	 * @return 
	 * */
	public static String getInfo(Context context,String name,String def) {
		// ��ȡSharedPreferences����
		SharedPreferences sharedPre=context.getSharedPreferences("config", Context.MODE_PRIVATE);
		return sharedPre.getString(name,def);
		
	}
	
	/**
	 * �洢��Ϣ
	 * @return 
	 * */
	public static String getID_User(Context context) {
		// ��ȡSharedPreferences����
		SharedPreferences sharedPre=context.getSharedPreferences("config", Context.MODE_PRIVATE);
		return sharedPre.getString(Constants.USERID,"0");
		
	}
	/**
	 * ɾ���û�������
	 * */
	public static void removeLoginInfo(Context context) {
		// ��ȡSharedPreferences����
		SharedPreferences sharedPre = context.getSharedPreferences("config",
				Context.MODE_PRIVATE);
		// ��ȡEditor����
		Editor editor = sharedPre.edit();
		// ���ò���
		editor.remove("username");
		editor.remove("password");
		// �ύ
		editor.commit();
	}
	 public static void removeAllInfo(Context context) {
			// ��ȡSharedPreferences����
			SharedPreferences sharedPre = context.getSharedPreferences("config",
					Context.MODE_PRIVATE);
			// ��ȡEditor����
			Editor editor = sharedPre.edit();
			// ���ò���
			editor.clear();
			// �ύ
			editor.commit();
		}
	/**
	 * ɾ���û�������
	 * */
	public static void removeInfo(Context context,String name) {
		// ��ȡSharedPreferences����
		SharedPreferences sharedPre = context.getSharedPreferences("config",
				Context.MODE_PRIVATE);
		// ��ȡEditor����
		Editor editor = sharedPre.edit();
		// ���ò���
		editor.remove(name);
		// �ύ
		editor.commit();
	}

	/**
	 * ����Ƿ��¼
	 * */
	public static void signLogin(Context context, Boolean flag) {

		SharedPreferences sharedPre = context.getSharedPreferences("loginFlag",
				Context.MODE_PRIVATE);
		// ��ȡEditor����
		Editor editor = sharedPre.edit();
		// ���ò���
		editor.putBoolean("flag", flag);
		// �ύ
		editor.commit();
	}

	/**
	 * �ж��ַ����Ƿ�Ϊ�գ�null "" ��
	 * */
	public static Boolean isEmpty(String string) {

		if (null != string && !string.equals("")) {
			return false;
		}
		return true;
	}

	/**
	 * ���ַ���Ϊ��������Ӧ�ַ����滻
	 * */

	public static String emptyInsteadWIth(String string, String instead) {

		if (null != string && !string.equals("")) {
			return string;
		}
		return instead;
	}

	/**
	 * ����ַ���"/Date(1401120000000+0800)/"
	 * */
	public static String[] split(String str) {
		String[] str1=new String[2];
		int index1;
		int index2;
		int index3;
		String r2;
		try {
			index1=str.indexOf("(");
			index2=str.indexOf(")");
			r2=str.substring(index1+1, index2);
			index3=r2.indexOf("+");
			str1[0]=r2.substring(0, index3);
			str1[1]=r2.substring(index3+1);
		} catch (Exception e) {
			str1=null;
		}
		return str1;
	}
}
