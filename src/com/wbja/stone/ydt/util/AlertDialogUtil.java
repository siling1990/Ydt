package com.wbja.stone.ydt.util;

import android.app.AlertDialog;
import android.content.Context;

public class AlertDialogUtil {
	public static AlertDialog.Builder builder;
	public static void showAlertDialogConfirm(Context context,String msg){
			builder=new AlertDialog.Builder(context);
		builder
		.setTitle("��ʾ")
		.setMessage(msg)
		.setPositiveButton("ȷ��",null).setNegativeButton("ȡ��", null)
		.show();
		
	}
	public static void showAlertDialog(Context context,String msg){
		builder=new AlertDialog.Builder(context);
		builder
		.setTitle("��ʾ")
		.setMessage(msg)
		.setPositiveButton("ȷ��",null)
		.show();
		
	}

}
