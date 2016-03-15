package com.wbja.stone.ydt.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * String 实用类
 * 
 * @author stone
 * */
public class StringUtil {

	/**
	 * 存储用户名密码
	 * */
	public static void saveLoginInfo(Context context, String username,
			String password) {
		// 获取SharedPreferences对象
		SharedPreferences sharedPre = context.getSharedPreferences("config",
				Context.MODE_PRIVATE);
		// 获取Editor对象
		Editor editor = sharedPre.edit();
		// 设置参数
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
		// 提交
		editor.commit();
	}

	/**
	 * 存储信息
	 * */
	public static void saveInfo(Context context, String name,String value) {
		// 获取SharedPreferences对象
		SharedPreferences sharedPre = context.getSharedPreferences("config",
				Context.MODE_PRIVATE);
		// 获取Editor对象
		Editor editor = sharedPre.edit();
		// 设置参数
		
		if(!isEmpty(value)){
			editor.putString(name, value);
		}else{
			editor.remove(name);
		}
		// 提交
		editor.commit();
	}
	/**
	 * 存储信息
	 * @return 
	 * */
	public static String getInfo(Context context,String name,String def) {
		// 获取SharedPreferences对象
		SharedPreferences sharedPre=context.getSharedPreferences("config", Context.MODE_PRIVATE);
		return sharedPre.getString(name,def);
		
	}
	
	/**
	 * 存储信息
	 * @return 
	 * */
	public static String getID_User(Context context) {
		// 获取SharedPreferences对象
		SharedPreferences sharedPre=context.getSharedPreferences("config", Context.MODE_PRIVATE);
		return sharedPre.getString(Constants.USERID,"0");
		
	}
	/**
	 * 删除用户名密码
	 * */
	public static void removeLoginInfo(Context context) {
		// 获取SharedPreferences对象
		SharedPreferences sharedPre = context.getSharedPreferences("config",
				Context.MODE_PRIVATE);
		// 获取Editor对象
		Editor editor = sharedPre.edit();
		// 设置参数
		editor.remove("username");
		editor.remove("password");
		// 提交
		editor.commit();
	}
	 public static void removeAllInfo(Context context) {
			// 获取SharedPreferences对象
			SharedPreferences sharedPre = context.getSharedPreferences("config",
					Context.MODE_PRIVATE);
			// 获取Editor对象
			Editor editor = sharedPre.edit();
			// 设置参数
			editor.clear();
			// 提交
			editor.commit();
		}
	/**
	 * 删除用户名密码
	 * */
	public static void removeInfo(Context context,String name) {
		// 获取SharedPreferences对象
		SharedPreferences sharedPre = context.getSharedPreferences("config",
				Context.MODE_PRIVATE);
		// 获取Editor对象
		Editor editor = sharedPre.edit();
		// 设置参数
		editor.remove(name);
		// 提交
		editor.commit();
	}

	/**
	 * 标记是否登录
	 * */
	public static void signLogin(Context context, Boolean flag) {

		SharedPreferences sharedPre = context.getSharedPreferences("loginFlag",
				Context.MODE_PRIVATE);
		// 获取Editor对象
		Editor editor = sharedPre.edit();
		// 设置参数
		editor.putBoolean("flag", flag);
		// 提交
		editor.commit();
	}

	/**
	 * 判断字符串是否为空（null "" ）
	 * */
	public static Boolean isEmpty(String string) {

		if (null != string && !string.equals("")) {
			return false;
		}
		return true;
	}

	/**
	 * 若字符串为空则用相应字符串替换
	 * */

	public static String emptyInsteadWIth(String string, String instead) {

		if (null != string && !string.equals("")) {
			return string;
		}
		return instead;
	}

	/**
	 * 拆分字符串"/Date(1401120000000+0800)/"
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
