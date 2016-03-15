package com.wbja.stone.ydt.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.http.util.EncodingUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.view.Gravity;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
public class FileUtil {
	private static File file;
	private static File[] fileList;
	private static FileInputStream fis;
	private static FileFilter filefilter;
	private static  FileOutputStream fos;;
	private static SimpleDateFormat smf = new SimpleDateFormat("yyyyMMddhhmmss");

	// 删除文件夹
	// param folderPath 文件夹完整绝对路径

	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 删除指定文件夹下所有文件
	// param path 文件夹完整绝对路径
	public static boolean delAllFile(String path) {
		boolean flag = false;
		file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}

	//
	public static File refile(Context context, int itemPosition, String src) {

		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			String pos = Constants.GROUP0;
			switch (itemPosition) {
			case 0:
				pos = Constants.GROUP0;
				break;
			case 1:
				pos = Constants.GROUP1;
				break;
			case 2:
				pos = Constants.GROUP2;
				break;
			case 3:
				pos = Constants.GROUP3;
				break;
			case 4:
				pos = Constants.GROUP4;
				break;
			case 5:
				pos = Constants.GROUP5;
				break;
			default:
				pos = Constants.GROUP0;
			}

			String name = pos + smf.format(new Date());
			String fileNmae = src + name + ".jpg";
			// pa.setIdPhotoPath(fileNmae);
			file = new File(fileNmae);

			return file;
		} else {

			Toast toast = Toast.makeText(context, "保存失败，SD卡无效",
					Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
		return null;
	}

	/**
	 * 改进
	 * 
	 * **/
	//
	public static File refileG(Context context, String type, String src) {

		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			String name = type + smf.format(new Date());
			String fileNmae = src + name + ".jpg";
			// pa.setIdPhotoPath(fileNmae);
			file = new File(fileNmae);

			return file;
		} else {

			Toast toast = Toast.makeText(context, "保存失败，SD卡无效",
					Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
		return null;
	}

	/**
	 * 获取所有文件数量
	 * */
	public static int[] getFilesCount(String path) {
		int[] count = new int[3];// 图像音频文本

		file = new File(path);
		if (!file.exists()) {
			count[0]=0;
			count[1]=0;
			count[2]=0;
		}else{
			filefilter = new FileFilter() {

				public boolean accept(File file) {
					if (file.getName().endsWith(".jpg")) {
						return true;
					}
					return false;
				}
			};
			count[0] = file.listFiles(filefilter).length;
			filefilter = new FileFilter() {

				public boolean accept(File file) {
					if (file.getName().endsWith(".wmv")) {
						return true;
					}
					return false;
				}
			};
			count[1] = file.listFiles(filefilter).length;
			filefilter = new FileFilter() {

				public boolean accept(File file) {
					if (file.getName().endsWith(".txt")) {
						return true;
					}
					return false;
				}
			};
			count[2] = file.listFiles(filefilter).length;
		}
	

		return count;
	}

	/**
	 * 获取所有非文本文件
	 * */
	public static File[] getFiles(String path, final String filter) {

		file = new File(path);
		filefilter = new FileFilter() {

			public boolean accept(File file) {
				// if the file extension is .txt return true, else false
				if (file.getName().contains(filter)
						|| file.getName().endsWith(".txt")) {// 已上传或者并且是文本
					return false;
				}
				return true;
			}
		};
		fileList = file.listFiles(filefilter);

		return fileList;
	}

	/**
	 * 获取TEXT文件
	 * */
	public static File[] getTextFiles(String path, final String filter) {

		file = new File(path);
		filefilter = new FileFilter() {

			public boolean accept(File file) {
				// if the file extension is .txt return true, else false
				if (file.getName().contains(filter)
						&& file.getName().endsWith(".txt")) {
					return true;
				}
				return false;
			}
		};
		fileList = file.listFiles(filefilter);

		return fileList;
	}

	/**
	 * 获取图片文件
	 * */
	public static File[] getImageFiles(String path, final String filter) {

		file = new File(path);
		filefilter = new FileFilter() {

			public boolean accept(File file) {
				// if the file extension is .txt return true, else false
				if (file.getName().contains(filter)
						&& file.getName().endsWith(".jpg")) {
					return true;
				}
				return false;
			}
		};
		fileList = file.listFiles(filefilter);

		return fileList;
	}

	/**
	 * 获取声音文件
	 * */
	public static File[] getVoiceFiles(String path, final String filter) {

		file = new File(path);
		filefilter = new FileFilter() {

			public boolean accept(File file) {
				// if the file extension is .txt return true, else false
				if (file.getName().contains(filter)
						&& file.getName().endsWith(".wmv")) {
					return true;
				}
				return false;
			}
		};

		fileList = file.listFiles(filefilter);

		return fileList;
	}

	// 读文件
	public static String readSDFile(String fileName) {

		file = new File(fileName);
		int length;
		String res = null;
		byte[] buffer;
		try {
			fis = new FileInputStream(file);

			length = fis.available();

			buffer = new byte[length];
			fis.read(buffer);
			res = EncodingUtils.getString(buffer, "UTF-8");
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
  //写文件  
  public static void writeSDFile(String fileName, String write_str){    
    
          File file = new File(fileName);    
			try {
				fos = new FileOutputStream(file);
				 byte [] bytes = write_str.getBytes();   
			      
		            fos.write(bytes);   
		            fos.close();  
			} catch (Exception e) {
				e.printStackTrace();
			}    
    
  }   
	public static boolean delFile(String filePath) {
		file = new File(filePath);
		if (file.exists()) {
			file.delete();
			return true;
		}

		return false;
	}

	public static void delFile(String src, Map<String, String> map) {
		for (Map.Entry<String, String> entry : map.entrySet()) {
			file = new File(src + entry.getValue());
			if (file.exists()) {
				file.delete();
			}
		}
	}
}
