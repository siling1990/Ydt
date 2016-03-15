package com.wbja.stone.ydt.util;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
/**
 * ����ʵ����
 * @author stone
 * */
@SuppressLint("SimpleDateFormat")
public class DateUtil {
	/**
	 * ��Date ת�����ض���ʽ��String��yyMMddhhmmss��
	 * */
	public static String dateToString(Date date,String pattern){
		
		SimpleDateFormat sdf=new SimpleDateFormat(pattern);
		if(date==null){
			date=new Date();
		}
		
		return sdf.format(date);
	}
	/**
	 * ��String ת����Date
	 * @throws ParseException
	 * */
	public static Date stringToDate(String string,String pattern) throws ParseException{
		SimpleDateFormat sdf=new SimpleDateFormat(pattern);
		return sdf.parse(string);
	}
	/**
	 * ʱ���꣩
	 * */
	public static int yearsBetween(Date dateFormer,Date dateLatter){
		if(dateFormer==null){
			dateFormer=new Date();
		}
		Calendar cal=Calendar.getInstance();
		
		cal.setTime(dateLatter);
		int lYear=cal.get(Calendar.YEAR);
		
		cal.setTime(dateFormer);
		int fYear=cal.get(Calendar.YEAR);
		
		return lYear>fYear?lYear-fYear:fYear-lYear;
	}
	/**
	 * ʱ������꣩
	 * */
public static Date yearsAfterBefore(Date date,int year){
		
		Calendar cal=Calendar.getInstance();
		
		cal.setTime(date);
		cal.add(Calendar.YEAR, year);
		
		
		return cal.getTime();
	}
/**
 * ʱ��Ӽ���ʱ��
 * */
public static Date hoursAfterBefore(Date date,int hour){
	
	Calendar cal=Calendar.getInstance();
	
	cal.setTime(date);
	cal.add(Calendar.HOUR, hour);
	
	
	return cal.getTime();
}
/**
 * ���������
 * */
public  static Date millsToDate(String[] str){
	
	
	 long l;
	 String str1;
	 try{
		 l=Long.parseLong(str[0]); 
		 str1=str[1];
	 }catch(Exception e){
		 l=new Date().getTime();
		 str1="0800";
	 }
	
	 Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+"+str1));            
	 calendar.setTimeInMillis(l);  
	 Date d=calendar.getTime();
	return hoursAfterBefore(d, 8);
}

/**
 * JsonDate
 * */
public  static Date jsonDateToDate(String str){
	if(StringUtil.isEmpty(str)){
		return new Date();
	}
	Date date = new Date(Long.parseLong(str.replace("/Date(", "").replace(")/", ""), 10));
//	 Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+"+str1));            
//	 calendar.setTimeInMillis(l);  
//	 Date d=calendar.getTime();
	return date;
}
}
