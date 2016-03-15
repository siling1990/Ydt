package com.wbja.stone.ydt.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {

	public static void show(String msg,Context context){
		
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
	

}
