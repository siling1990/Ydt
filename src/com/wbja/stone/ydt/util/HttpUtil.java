package com.wbja.stone.ydt.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;

import com.wbja.stone.ydt.entity.UpdateInfo;

import android.content.Context;
import android.util.Log;

/**
 * @ClassName: HttpUtil
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Stone
 * @date 2015-7-8 下午2:42:25
 * 
 */
public class HttpUtil {
	private static final int TIME_OUT = 10 * 1000; // 超时时间
	private static final String CHARSET = "utf-8"; // 设置编码
	private static DefaultHttpClient client;
	/**
	 * android上传文件到服务器
	 * 
	 * @param file
	 *            需要上传的文件
	 * @param RequestURL
	 *            请求的rul
	 * @return 返回响应的内容
	 */
	public static String uploadFile(File file, String RequestURL) {
		String result = null;
		String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
		String PREFIX = "--", LINE_END = "\r\n";
		String CONTENT_TYPE = "multipart/form-data"; // 内容类型

		try {
			URL url = new URL(RequestURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(TIME_OUT);
			conn.setConnectTimeout(TIME_OUT);
			conn.setDoInput(true); // 允许输入流
			conn.setDoOutput(true); // 允许输出流
			conn.setUseCaches(false); // 不允许使用缓存
			conn.setRequestMethod("POST"); // 请求方式
			conn.setRequestProperty("Charset", CHARSET); // 设置编码
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="
					+ BOUNDARY);
			if (file != null) {
				/**
				 * 当文件不为空，把文件包装并且上传
				 */
				DataOutputStream dos = new DataOutputStream(
						conn.getOutputStream());
				StringBuffer sb = new StringBuffer();
				sb.append(PREFIX);
				sb.append(BOUNDARY);
				sb.append(LINE_END);
				/**
				 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
				 * filename是文件的名字，包含后缀名的 比如:abc.png
				 */
				sb.append("Content-Disposition: form-data; name=\"fup\"; filename=\""
						+ file.getName() + "\"" + LINE_END);
				sb.append("Content-Type: image/pjpeg; charset=" + CHARSET
						+ LINE_END);
				sb.append(LINE_END);
				dos.write(sb.toString().getBytes());
				InputStream is = new FileInputStream(file);
				byte[] bytes = new byte[1024];
				int len = 0;
				while ((len = is.read(bytes)) != -1) {
					dos.write(bytes, 0, len);
				}
				is.close();
				dos.write(LINE_END.getBytes());
				byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
						.getBytes();
				dos.write(end_data);
				dos.flush();
				/**
				 * 获取响应码 200=成功 当响应成功，获取响应的流
				 */

				int res = conn.getResponseCode();
				System.out.println("res=========" + res);
				if (res == 200) {
					InputStream input = conn.getInputStream();
					StringBuffer sb1 = new StringBuffer();
					int ss;
					while ((ss = input.read()) != -1) {
						sb1.append((char) ss);
					}
					result = sb1.toString();
				} else {
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	 public static UpdateInfo getUpDateInfo(String path) {  
		 Log.d("更新url", path);
	        StringBuffer sb = new StringBuffer();  
	        String line = null;  
	        BufferedReader reader = null;  
	        try {  
	            // 创建一个url对象  
	            URL url = new URL(path);  
	            // 通過url对象，创建一个HttpURLConnection对象（连接）  
	            HttpURLConnection urlConnection = (HttpURLConnection) url  
	                    .openConnection();  
	            // 通过HttpURLConnection对象，得到InputStream  
	            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),"gb2312"));  
	            // 使用io流读取文件  
	            while ((line = reader.readLine()) != null) {  
	                sb.append(line);  
	            }  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        } finally {  
	            try {  
	                if (reader != null) {  
	                    reader.close();  
	                }  
	            } catch (Exception e) {  
	                e.printStackTrace();  
	            }  
	        }  
	        String info = sb.toString(); 
	        if(StringUtil.isEmpty(info)){
	        	return null;
	        }
	        Log.d("返回数据", info);
	        UpdateInfo updateInfo = new UpdateInfo();  
	        updateInfo.setVersion(info.split("&")[0]);  
	        updateInfo.setDescription(info.split("&")[1]);  
	        updateInfo.setUrl(info.split("&")[2]);  
	        return updateInfo;  
	    }  
	/**
	 * 或得融云token
	 * **/
	public static String doPostFormToken(Map<String, String> parmas, String url,Context context) {

		
		client = new DefaultHttpClient();// http客户端
		 // 请求超时
		client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIME_OUT);
        // 读取超时
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, TIME_OUT    );

		Log.d("********HTTPURL********", url);
		HttpPost httpPost = new HttpPost(url);
		
		Random rnd = new Random();
		int nonce = rnd.nextInt(89999) + 10000;
		long timestamp=new Date().getTime();
		String sign=String.valueOf(Constants.APPSCRET)+String.valueOf(nonce)+String.valueOf(timestamp);
		
		String signature =encryptToSHA(sign);
		
		Log.d("********叠加字符串******", sign);
		Log.d("********jiami******", signature);
		
		httpPost.setHeader("App-Key", Constants.APPKEY);
		httpPost.setHeader("Nonce", String.valueOf(nonce));
		httpPost.setHeader("Timestamp", String.valueOf(timestamp));
		httpPost.setHeader("Signature", signature);
		
		ArrayList<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
		if (parmas != null) {
			Set<String> keys = parmas.keySet();
			for (Iterator<String> i = keys.iterator(); i.hasNext();) {
				String key = (String) i.next();
				pairs.add(new BasicNameValuePair(key, parmas.get(key)));
			}
		}
		try {
			UrlEncodedFormEntity p_entity = new UrlEncodedFormEntity(pairs,
					"utf-8");
			httpPost.setEntity(p_entity);
			HttpResponse response = client.execute(httpPost);
			HttpEntity entity = response.getEntity();
			InputStream content = entity.getContent();
			return convertStreamToString(content);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
	/**
	 * 
	 * 提交表单
	 * */
	public static String doPostForm(Map<String, String> parmas, String url,
			boolean isLogin, Context context) {

		// 封装数据
		if (!isLogin) {
			parmas.put(Constants.TOKEN,
					StringUtil.getInfo(context, Constants.TOKEN, ""));
			parmas.put("id_user",
					StringUtil.getInfo(context, Constants.USERID, ""));
		}

		client = new DefaultHttpClient();// http客户端
		 // 请求超时
		client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIME_OUT);
        // 读取超时
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, TIME_OUT    );

		Log.d("********HTTPURL********", url);
		HttpPost httpPost = new HttpPost(url);
		ArrayList<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
		if (parmas != null) {
			Set<String> keys = parmas.keySet();
			for (Iterator<String> i = keys.iterator(); i.hasNext();) {
				String key = (String) i.next();
				pairs.add(new BasicNameValuePair(key, parmas.get(key)));
			}
		}
		try {
			UrlEncodedFormEntity p_entity = new UrlEncodedFormEntity(pairs,
					"utf-8");
			httpPost.setEntity(p_entity);
			HttpResponse response = client.execute(httpPost);
			HttpEntity entity = response.getEntity();
			InputStream content = entity.getContent();
			return convertStreamToString(content);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 
	 * 提交包含文件的表单
	 * */
	public static String doPostFileForm(Map<String, String> parmas, String url,
			boolean isLogin, Context context, File file) {
		final ContentType TEXT_PLAIN = ContentType.create("text/plain",  
	            Charset.forName(HTTP.UTF_8));
		client = new DefaultHttpClient();
		// 请求超时
		client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIME_OUT);
		        // 读取超时
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, TIME_OUT    );
		if (!isLogin) {
			parmas.put(Constants.TOKEN,
					StringUtil.getInfo(context, Constants.TOKEN, ""));
			parmas.put("id_user",
					StringUtil.getInfo(context, Constants.USERID, ""));
		}
		try {
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			HttpPost httppost = new HttpPost(url);// 创建 HTTP POST 请求
			
			if(file!=null){
				builder.addBinaryBody("file", file, ContentType.create("image/jpeg"), file.getName());
			}
			if (parmas != null) {
				Set<String> keys = parmas.keySet();
				for (Iterator<String> i = keys.iterator(); i.hasNext();) {
					String key = (String) i.next();
					builder.addPart(key, new StringBody(parmas.get(key),TEXT_PLAIN));
				}
			}
			HttpEntity reqEntity = builder.build();
			httppost.setEntity(reqEntity);
			HttpResponse response = client.execute(httppost);
				System.out.println("----------------------------------------");
				System.out.println(response.getStatusLine());
				HttpEntity resEntity = response.getEntity();
				InputStream content = resEntity.getContent();
				return convertStreamToString(content);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}

	/**
	 * 转换返回结果
	 * */
	private static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Log.d("***********HTTPTRSULT**********", sb.toString());
		return sb.toString();
	}
	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
		stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
		if (stmp.length() == 1) {
		hs = hs + "0" + stmp;
		} else {
		hs = hs + stmp;
		}
		}
		return hs;
		}

		//SHA1 加密实例
		public static String encryptToSHA(String info) {
		byte[] digesta = null;
		try {
		// 得到一个SHA-1的消息摘要
		MessageDigest alga = MessageDigest.getInstance("SHA-1");
		// 添加要进行计算摘要的信息
		alga.update(info.getBytes());
		// 得到该摘要
		digesta = alga.digest();
		} catch (NoSuchAlgorithmException e) {
		e.printStackTrace();
		}
		// 将摘要转为字符串
		String rs = byte2hex(digesta);
		return rs;
		}
}
