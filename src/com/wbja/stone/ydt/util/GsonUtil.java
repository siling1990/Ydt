package com.wbja.stone.ydt.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import com.google.gson.Gson;
import com.google.gson.reflect.*;
/**
 * Google Gsonʵ����
 * @author stone
 * */
public class GsonUtil {

    public GsonUtil() {
    
    }

    /*
     * �鿴Gson api���е�fromJson(String json, Class<T> classOfT)����
     * public <T> T fromJson(String json, Class<T> classOfT)
     * ��������һ�������Json����,ʹ�÷�����ƿ��Խ�JSON�ַ����а��������ݸ�ֵ��ָ�����ࡣ��ߵ�T��ʾ����ͨ������,Ҳ���ǿ��Ա�ʾ��������͡�
     * ����˵����
     * json : ָ�������������JSON�ַ���Դ,����ת����Java����
     * classOfT �� ����   T ��Class����
     */
    public static <T> T getObject(String jsonString, Class<T> cls){
        T t = null;
        try {
            Gson gson = new Gson();
            t = gson.fromJson(jsonString, cls);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return t;
    }
    
    /*
     * List ����ʹ�õ�Gson�е�
     * public <T> T fromJson(String json, Type typeOfT)
     * ���������Ҫȡ��List<T>�в�ͬ�Ķ�����ͨ��ʵ�ַ�ʽ������һ����org.Json��������JSON�ķ�ʽһ��(�����Ķ���һ������)��
     * ��������ͨ�� Gson�е� TypeToken���Ǽ����������typeOfT���÷���ͨ��������ư�T����Ķ�������Ե�ֵӳ�������Ȼ��ͨ����json�ַ���ȡ�õ�ֵ��ֵ��ȥ�Ϳ����ˡ�
     * getType()����˼���Ǳ�ʾ��jsonString���ַ�����������֮���װ��List�����У�Ȼ��ֱ��T����ȡ�����ͽ��临�ơ�
     */
    
    public static <T> List<T> getObjectList(String jsonString, Class<T> cls){
        List<T> list = new ArrayList<T>();
        try {
            Gson gson = new Gson();
            list = gson.fromJson(jsonString, 
                    new TypeToken<List<T>>(){}.getType());
        } catch (Exception e) {
            // TODO: handle exception
        }
        return list;
    }
    
    public static List<String> getList(String jsonString){
        List<String> list = new ArrayList<String>();
        try {
            Gson gson = new Gson();
            list = gson.fromJson(jsonString, 
                    new TypeToken<List<String>>(){}.getType());
        } catch (Exception e) {
            // TODO: handle exception
        }
        return list;
    }
    
    public static List<Map<String, Object>> getListMaps(String jsonString){
        List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
        try {
            Gson gson = new Gson();
            list = gson.fromJson(jsonString, 
                    new TypeToken<List<Map<String, Object>>>(){}.getType());
        } catch (Exception e) {
            // TODO: handle exception
        }
        return list;
    }
    public static  String getJsonValue(Object ob) {
		String strError="Error";
		try {
			 Gson gson = new Gson();
			 return gson.toJson(ob);
			 
		} catch (Exception e) {
			// TODO: handle exception
			strError=e.toString();
		}
		return strError;

	}
    public static  String getJsonValue(List<?> list) {
		String strError="Error";
		try {
			 Gson gson = new Gson();
			 return gson.toJson(list);
			 
		} catch (Exception e) {
			// TODO: handle exception
			strError=e.toString();
		}
		return strError;

	}
    
}
