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
 * @Description: TODO(������һ�仰��������������)
 * @author Stone
 * @date 2015-7-8 ����2:42:25
 * 
 */
public class HttpUtil {
	private static final int TIME_OUT = 10 * 1000; // ��ʱʱ��
	private static final String CHARSET = "utf-8"; // ���ñ���
	private static DefaultHttpClient client;
	/**
	 * android�ϴ��ļ���������
	 * 
	 * @param file
	 *            ��Ҫ�ϴ����ļ�
	 * @param RequestURL
	 *            �����rul
	 * @return ������Ӧ������
	 */
	public static String uploadFile(File file, String RequestURL) {
		String result = null;
		String BOUNDARY = UUID.randomUUID().toString(); // �߽��ʶ �������
		String PREFIX = "--", LINE_END = "\r\n";
		String CONTENT_TYPE = "multipart/form-data"; // ��������

		try {
			URL url = new URL(RequestURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(TIME_OUT);
			conn.setConnectTimeout(TIME_OUT);
			conn.setDoInput(true); // ����������
			conn.setDoOutput(true); // ���������
			conn.setUseCaches(false); // ������ʹ�û���
			conn.setRequestMethod("POST"); // ����ʽ
			conn.setRequestProperty("Charset", CHARSET); // ���ñ���
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="
					+ BOUNDARY);
			if (file != null) {
				/**
				 * ���ļ���Ϊ�գ����ļ���װ�����ϴ�
				 */
				DataOutputStream dos = new DataOutputStream(
						conn.getOutputStream());
				StringBuffer sb = new StringBuffer();
				sb.append(PREFIX);
				sb.append(BOUNDARY);
				sb.append(LINE_END);
				/**
				 * �����ص�ע�⣺ name�����ֵΪ����������Ҫkey ֻ�����key �ſ��Եõ���Ӧ���ļ�
				 * filename���ļ������֣�������׺���� ����:abc.png
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
				 * ��ȡ��Ӧ�� 200=�ɹ� ����Ӧ�ɹ�����ȡ��Ӧ����
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
		 Log.d("����url", path);
	        StringBuffer sb = new StringBuffer();  
	        String line = null;  
	        BufferedReader reader = null;  
	        try {  
	            // ����һ��url����  
	            URL url = new URL(path);  
	            // ͨ�^url���󣬴���һ��HttpURLConnection�������ӣ�  
	            HttpURLConnection urlConnection = (HttpURLConnection) url  
	                    .openConnection();  
	            // ͨ��HttpURLConnection���󣬵õ�InputStream  
	            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),"gb2312"));  
	            // ʹ��io����ȡ�ļ�  
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
	        Log.d("��������", info);
	        UpdateInfo updateInfo = new UpdateInfo();  
	        updateInfo.setVersion(info.split("&")[0]);  
	        updateInfo.setDescription(info.split("&")[1]);  
	        updateInfo.setUrl(info.split("&")[2]);  
	        return updateInfo;  
	    }  
	/**
	 * �������token
	 * **/
	public static String doPostFormToken(Map<String, String> parmas, String url,Context context) {

		
		client = new DefaultHttpClient();// http�ͻ���
		 // ����ʱ
		client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIME_OUT);
        // ��ȡ��ʱ
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, TIME_OUT    );

		Log.d("********HTTPURL********", url);
		HttpPost httpPost = new HttpPost(url);
		
		Random rnd = new Random();
		int nonce = rnd.nextInt(89999) + 10000;
		long timestamp=new Date().getTime();
		String sign=String.valueOf(Constants.APPSCRET)+String.valueOf(nonce)+String.valueOf(timestamp);
		
		String signature =encryptToSHA(sign);
		
		Log.d("********�����ַ���******", sign);
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
	 * �ύ��
	 * */
	public static String doPostForm(Map<String, String> parmas, String url,
			boolean isLogin, Context context) {

		// ��װ����
		if (!isLogin) {
			parmas.put(Constants.TOKEN,
					StringUtil.getInfo(context, Constants.TOKEN, ""));
			parmas.put("id_user",
					StringUtil.getInfo(context, Constants.USERID, ""));
		}

		client = new DefaultHttpClient();// http�ͻ���
		 // ����ʱ
		client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIME_OUT);
        // ��ȡ��ʱ
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
	 * �ύ�����ļ��ı�
	 * */
	public static String doPostFileForm(Map<String, String> parmas, String url,
			boolean isLogin, Context context, File file) {
		final ContentType TEXT_PLAIN = ContentType.create("text/plain",  
	            Charset.forName(HTTP.UTF_8));
		client = new DefaultHttpClient();
		// ����ʱ
		client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIME_OUT);
		        // ��ȡ��ʱ
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, TIME_OUT    );
		if (!isLogin) {
			parmas.put(Constants.TOKEN,
					StringUtil.getInfo(context, Constants.TOKEN, ""));
			parmas.put("id_user",
					StringUtil.getInfo(context, Constants.USERID, ""));
		}
		try {
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			HttpPost httppost = new HttpPost(url);// ���� HTTP POST ����
			
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
	 * ת�����ؽ��
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

		//SHA1 ����ʵ��
		public static String encryptToSHA(String info) {
		byte[] digesta = null;
		try {
		// �õ�һ��SHA-1����ϢժҪ
		MessageDigest alga = MessageDigest.getInstance("SHA-1");
		// ���Ҫ���м���ժҪ����Ϣ
		alga.update(info.getBytes());
		// �õ���ժҪ
		digesta = alga.digest();
		} catch (NoSuchAlgorithmException e) {
		e.printStackTrace();
		}
		// ��ժҪתΪ�ַ���
		String rs = byte2hex(digesta);
		return rs;
		}
}
