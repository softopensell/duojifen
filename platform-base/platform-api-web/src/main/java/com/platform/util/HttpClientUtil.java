package com.platform.util;
/*
Copyright (c) 2007-2009, Yusuke Yamamoto
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
 * Neither the name of the Yusuke Yamamoto nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY Yusuke Yamamoto ``AS IS'' AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL Yusuke Yamamoto BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpClientUtil implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int CONNECTION_TIMEOUT = 30 * 1000;
	private static final int READ_TIMEOUT = 30 * 1000;

	public static String doPost(String requestURL, Map<String, String> params) {
		HttpURLConnection con = null;
		OutputStream osw = null;
		StringBuffer sb = new StringBuffer();
		String requestParams = null;
		for(String key : params.keySet()){
			sb.append(key);
			sb.append("=");
			String keyStr="";
			try {
				 keyStr= params.get(key);
				 keyStr  =   java.net.URLEncoder.encode(keyStr,   "utf-8");
			} catch (Exception e1) {
				e1.printStackTrace();
			}     
			
			sb.append(keyStr);
			sb.append("&");
		}
		requestParams = sb.toString();
	//	System.out.println(requestParams);
		try {
			URL url = new URL(requestURL);
			if("https".equals(url.getProtocol().toLowerCase())){
				trustAllHosts();
				con = (HttpsURLConnection) url.openConnection();
			}else{
				con = (HttpURLConnection) url.openConnection();
			}
			con.setConnectTimeout(CONNECTION_TIMEOUT);
			con.setReadTimeout(READ_TIMEOUT);
			con.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			con.setRequestMethod("POST");
			con.setDoInput(true);
			con.setDoOutput(true);

			osw = con.getOutputStream();

			if(null!=requestParams){
				osw.write(requestParams.getBytes("UTF-8"));
			}
			osw.flush();
			osw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			 if (osw != null) {
				try {osw.close();} catch (IOException e) {}
             }
		}
		return parseResponse(con);
	}
	
	/**
	 * 解析 返回值
	 * @param con
	 * @return
	 */
	private static String parseResponse(HttpURLConnection con){
		InputStream is = null;
		BufferedReader br = null;
		try{
			int responseCode = con.getResponseCode();
	        if (responseCode == HttpURLConnection.HTTP_OK) {
	            is = con.getInputStream();
	        } else {
	            is = con.getErrorStream();
	        }
	        br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
	        StringBuffer buf = new StringBuffer();
	        String line;
	        while (null != (line = br.readLine())) {
	            buf.append(line).append("\n");
	        }
	        return buf.toString();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(is!=null){
				try {is.close();} catch (IOException e) {}
			}
			con.disconnect();
		}
        return null;
	}

	static TrustManager[] xtmArray = new MytmArray[] { new MytmArray() };
	
	static class MytmArray implements X509TrustManager {  
		MytmArray(){}
	    public X509Certificate[] getAcceptedIssuers() {  
	        return new X509Certificate[] {};  
	    }  
	    @Override  
	    public void checkClientTrusted(X509Certificate[] chain, String authType)  
	            throws CertificateException {  
	    }  
	  
	    @Override  
	    public void checkServerTrusted(X509Certificate[] chain, String authType)  
	            throws CertificateException {  
	    }  
	};  
	  
    /** 
     * 信任所有主机-对于任何证书都不做检查 
     */  
    private static void trustAllHosts() {  
        try {  
            SSLContext sc = SSLContext.getInstance("TLS");  
            sc.init(null, xtmArray, new java.security.SecureRandom());  
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());  
            HttpsURLConnection.setDefaultHostnameVerifier(DO_NOT_VERIFY);//  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
  
    static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {  
        @Override  
        public boolean verify(String hostname, SSLSession session) {  
            return true;  
        }  
    }; 
    public static String doGet(String url, Map<String, String> params){
    	StringBuffer sb = new StringBuffer();
		String requestParams = null;
		for(String key : params.keySet()){
			sb.append(key);
			sb.append("=");
			String keyStr="";
			try {
				 keyStr= params.get(key);
				 keyStr  =   java.net.URLEncoder.encode(keyStr,   "utf-8");
			} catch (Exception e1) {
				e1.printStackTrace();
			}     
			
			sb.append(keyStr);
			sb.append("&");
		}
		requestParams = sb.toString();
        String result = "";
        BufferedReader in = null;  
        try{
        	URLConnection conn=null;
            String urlName = url + "?" + requestParams;
            URL realUrl = new URL(urlName);
            if("https".equals(realUrl.getProtocol().toLowerCase())){
				trustAllHosts();
				conn = (HttpsURLConnection) realUrl.openConnection();
			}else{
				conn = (HttpURLConnection) realUrl.openConnection();
			}
            // 设置通用的请求属性 
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
            "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            // 建立实际的连接  
            conn.connect();
            // 获取所有响应头字段  
            Map<String, List<String>> map = conn.getHeaderFields(); 
            // 遍历所有的响应头字段  
            for (String key : map.keySet()){  
                System.out.println(key + "--->" + map.get(key));
            }  
            // 定义BufferedReader输入流来读取URL的响应  
            in = new BufferedReader(  
                    new InputStreamReader(conn.getInputStream(),"UTF-8"));
            String line;
            while ((line = in.readLine()) != null){
                result += " " + line;
            }  
        }catch (Exception e){
            System.out.println("发送GET请求出现异常！" + e);  
            e.printStackTrace();  
        }finally{ 
        	try{
        		if (in != null){
                    in.close();
                }
            }catch (IOException ex){  
            	ex.printStackTrace(); 
            }
        }
        return result;
    }  
}
