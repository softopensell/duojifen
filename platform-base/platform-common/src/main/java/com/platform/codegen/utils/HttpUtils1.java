package com.platform.codegen.utils;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * @author softopensell
 * 获取HTTP相关的属性
 */
public class HttpUtils1 {
	
    public static String android="android";    
    public static String iphone="iphone";    
    public static String ipad="ipad";  
    public static String noDevice="未知设备";  
    
	private static final String forwardHeadName = "X-Forwarded-For";
	
	//获取移动用户操作系统    
    public static String getMobileOS(HttpServletRequest request){    
    	String userAgent=request.getHeader("user-agent").toLowerCase();
        if (userAgent.contains(android)) {    
            return android;    
        }else if (userAgent.contains(iphone)){    
            return iphone;    
        }else if (userAgent.contains(ipad)){    
            return ipad;    
        }else {    
            return "others";  
        }    
    }    
	
	public static String getHttpForwardIp(HttpServletRequest request){
		if(request == null){
			return null;
		}
		String ip=request.getHeader(forwardHeadName);
		if(ip==null){
			return request.getRemoteAddr(); 
		}else{
			 int idx = ip.indexOf(',');
	         if (idx > -1) {
	        	 ip = ip.substring(0, idx);
	         }
	         return ip;
		}
	}

	
	public static boolean isWeixinBrowse(HttpServletRequest request) {
		if(request == null){
			return false;
		}
		String ua = request.getHeader("user-agent").toLowerCase();
		if(StringUtils.isEmpty(ua)){
			ua = request.getHeader("User-Agent").toLowerCase();
		}
		if (ua.indexOf("micromessenger") > 0) {// 是微信浏览器
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isAlipayBrowse(HttpServletRequest request) {
		if(request == null){
			return false;
		}
		String ua = request.getHeader("user-agent").toLowerCase();
		if(StringUtils.isEmpty(ua)){
			ua = request.getHeader("User-Agent").toLowerCase();
		}
		if (ua.indexOf("alipayclient") > 0) {// 是微信浏览器
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isYaotuofuBrowse(HttpServletRequest request) {
		if(request == null){
			return false;
		}
		String ua = request.getHeader("user-agent").toLowerCase();
		if(StringUtils.isEmpty(ua)){
			ua = request.getHeader("User-Agent").toLowerCase();
		}
		if (ua.indexOf("yaotuofuclient") > 0) {// 是自己定义浏览器
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isKeyInBrowse(HttpServletRequest request,String key) {
		if(request == null){
			return false;
		}
		String ua = request.getHeader("user-agent").toLowerCase();
		if(StringUtils.isEmpty(ua)){
			ua = request.getHeader("User-Agent").toLowerCase();
		}
		if (ua.indexOf(key) > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 获取浏览器信息
	 * @author softopensell
	 * @version 1.2.18
	 * @param request
	 * @return
	 */
	public static String getHttpBrowser(HttpServletRequest request){
		if(request == null){
			return null;
		}
		return request.getHeader("User-Agent");
	}
	
	/**
	 * 获得整个url,如http://www.weamea.com/music/index.jsp?id=4342
	 * @author softopensell
	 * @version 1.2.18
	 * @param request
	 * @return
	 */
	public static String getFullURL(HttpServletRequest request){
		if(request == null){
			return null;
		}
		String url=request.getRequestURL().toString();
		String QueryString=request.getQueryString();
		if((QueryString!=null)&&(QueryString.length()>0))
			url=url+"?"+QueryString;
		return url;
	} 
	/**
	 * 得到请求的根目录
	 * 
	 * @param request
	 * @return
	 */
	public static String getBasePath(HttpServletRequest request) {
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName()
				+ ":" + request.getServerPort() + path;
		if(request.getServerPort()==80){
		 basePath = request.getScheme() + "://" + request.getServerName()+ path;
		}
		return basePath;
	}
	/**
	 * 获取网站名称
	 * @param request
	 * @return
	 */
	public static String getSiteName(HttpServletRequest request) {
		return request.getServerName();
	}

	/**
	 * 得到结构目录
	 * 
	 * @param request
	 * @return
	 */
	public static String getContextPath(HttpServletRequest request) {
		String path = request.getContextPath();
		return path;
	}
	public static String getContextRealPath(HttpServletRequest request) {
		String ctxPath =request.getSession().getServletContext().getRealPath("/");
		return ctxPath;
	}

	public static String getWebContextRealPath() {
		String ctxPath = Thread.currentThread().getContextClassLoader()
				.getResource("/").getPath();
		int index = ctxPath.indexOf("WEB-INF");
		if (index == -1) {
			index = ctxPath.indexOf("bin");
		}
		ctxPath = ctxPath.substring(0, index);
		return ctxPath;
	}
	public static String getUrlPath(HttpServletRequest request) {
		String ctxPath =request.getServletPath();
		if(ctxPath!=null){
			ctxPath=ctxPath.substring(1);
		}
		return ctxPath;
	}
	public static String getCurrentUrlPathWithParam(HttpServletRequest request){
		 String currentUrl="";
			if(request.getQueryString()==null){
				currentUrl=request.getServletPath();
			}else{
				currentUrl=request.getServletPath()+"?"+request.getQueryString();
			}
			return currentUrl;
	}
	public static String getPreferUrl(HttpServletRequest request){
		String prereferUrl="";
		 Map<String, String> map = new HashMap<String, String>();
	    Enumeration headerNames = request.getHeaderNames();
	    while (headerNames.hasMoreElements()) {
	        String key = (String) headerNames.nextElement();
	        String value = request.getHeader(key);
	        map.put(key, value);
	    }
	    
	    if(StringUtils.isEmpty(map.get("referer"))){
			return map.get("referer");
		}
	    if(StringUtils.isEmpty(map.get("Referer"))){
	    	return map.get("Referer");
	    }
		return prereferUrl;
	}
	
	public static String getContentTypeTypeByExtension(String extension){
		extension = extension.toUpperCase(); 
		String contentType = "application/octet-stream";
		if(extension.equals("*")) 
		 contentType = "application/octet-stream"; 
		else if(extension.equals("323")) 
		 contentType = "text/h323"; 
		else if(extension.equals("ACX")) 
		 contentType = "application/internet-property-stream"; 
		else if(extension.equals("AI") )
		 contentType = "application/postscript"; 
		else if(extension.equals("AIF") )
		 contentType = "audio/x-aiff"; 
		else if(extension.equals("AIFC") )
		 contentType = "audio/x-aiff"; 
		else if(extension.equals("AIFF") )
		 contentType = "audio/x-aiff"; 
		else if(extension.equals("ASF") )
		 contentType = "video/x-ms-asf"; 
		else if(extension.equals("SR") )
		 contentType = "video/x-ms-asf"; 
		else if(extension.equals("SX") )
		 contentType = "video/x-ms-asf"; 
		else if(extension.equals("AU") )
		 contentType = "audio/basic"; 
		else if(extension.equals("AVI") )
		 contentType = "video/x-msvideo"; 
		else if(extension.equals("AXS") )
		 contentType = "application/olescript"; 
		else if(extension.equals("BAS") )
		 contentType = "text/plain"; 
		else if(extension.equals("BCPIO")) 
		 contentType = "application/x-bcpio"; 
		else if(extension.equals("BIN") )
		 contentType = "application/octet-stream"; 
		else if(extension.equals("BMP") )
		 contentType = "image/bmp"; 
		else if(extension.equals("C") )
		 contentType = "text/plain"; 
		else if(extension.equals("CAT")) 
		 contentType = "application/vnd.ms-pkiseccat"; 
		else if(extension.equals("CDF") )
		 contentType = "application/x-cdf"; 
		else if(extension.equals("CER") )
		 contentType = "application/x-x509-ca-cert"; 
		else if(extension.equals("CLASS")) 
		 contentType = "application/octet-stream"; 
		else if(extension.equals("CLP") )
		 contentType = "application/x-msclip"; 
		else if(extension.equals("CMX") )
		 contentType = "image/x-cmx"; 
		else if(extension.equals("COD") )
		 contentType = "image/cis-cod"; 
		else if(extension.equals("CPIO") )
		 contentType = "application/x-cpio"; 
		else if(extension.equals("CRD") )
		 contentType = "application/x-mscardfile"; 
		else if(extension.equals("CRL") )
		 contentType = "application/pkix-crl"; 
		else if(extension.equals("CRT") )
		 contentType = "application/x-x509-ca-cert"; 
		else if(extension.equals("CSH") )
		 contentType = "application/x-csh"; 
		else if(extension.equals("CSS") )
		 contentType = "text/css"; 
		else if(extension.equals("DCR") )
		 contentType = "application/x-director"; 
		else if(extension.equals("DER") )
		 contentType = "application/x-x509-ca-cert"; 
		else if(extension.equals("DIR") )
		 contentType = "application/x-director"; 
		else if(extension.equals("DLL") )
		 contentType = "application/x-msdownload"; 
		else if(extension.equals("DMS") )
		 contentType = "application/octet-stream"; 
		else if(extension.equals("DOC") )
		 contentType = "application/msword"; 
		else if(extension.equals("DOT") )
		 contentType = "application/msword"; 
		else if(extension.equals("DVI") )
		 contentType = "application/x-dvi"; 
		else if(extension.equals("DXR") )
		 contentType = "application/x-director"; 
		else if(extension.equals("EPS") )
		 contentType = "application/postscript"; 
		else if(extension.equals("ETX") )
		 contentType = "text/x-setext"; 
		else if(extension.equals("EVY") )
		 contentType = "application/envoy"; 
		else if(extension.equals("EXE") )
		 contentType = "application/octet-stream"; 
		else if(extension.equals("FIF") )
		 contentType = "application/fractals"; 
		else if(extension.equals("FLR") )
		 contentType = "x-world/x-vrml"; 
		else if(extension.equals("GIF") )
		 contentType = "image/gif"; 
		else if(extension.equals("GTAR") )
		 contentType = "application/x-gtar"; 
		else if(extension.equals("GZ") )
		 contentType = "application/x-gzip"; 
		else if(extension.equals("H") )
		 contentType = "text/plain"; 
		else if(extension.equals("HDF") )
		 contentType = "application/x-hdf"; 
		else if(extension.equals("HLP") )
		 contentType = "application/winhlp"; 
		else if(extension.equals("HQX") )
		 contentType = "application/mac-binhex40"; 
		else if(extension.equals("HTA") )
		 contentType = "application/hta"; 
		else if(extension.equals("HTC") )
		 contentType = "text/x-component"; 
		else if(extension.equals("HTM") )
		 contentType = "text/html"; 
		else if(extension.equals("HTML") )
		 contentType = "text/html"; 
		else if(extension.equals("HTT") )
		 contentType = "text/webviewhtml"; 
		else if(extension.equals("ICO") )
		 contentType = "image/x-icon"; 
		else if(extension.equals("IEF") )
		 contentType = "image/ief"; 
		else if(extension.equals("III") )
		 contentType = "application/x-iphone"; 
		else if(extension.equals("INS") )
		 contentType = "application/x-internet-signup"; 
		else if(extension.equals("ISP") )
		 contentType = "application/x-internet-signup"; 
		else if(extension.equals("JFIF") )
		 contentType = "image/pipeg"; 
		else if(extension.equals("JPE") )
		 contentType = "image/jpeg"; 
		else if(extension.equals("JPEG") )
		 contentType = "image/jpeg"; 
		else if(extension.equals("ZIP") )
		 contentType = "application/x-zip-compressed"; 
		else if(extension.equals("RAR") )
		 contentType = "application/octet-stream";
		return contentType;
	}
	/**
	 * 
	 * @param request
	 * @param fileParm
	 * @param targetName 没有结尾扩展名
	 * @param ctxPath
	 */
	public static String copyFileByRequestFile(HttpServletRequest  request,String fileParm,String targetPathAndName,String ctxPath){
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile(fileParm);
		if(file==null){
			return null;
		}
		if(file.isEmpty()){
			return null;
		}
		// 获得文件名：
		String realFileName = file.getOriginalFilename();
		String endType=FileUtils1.getExtensionName(realFileName);
		// 创建文件
		File dirPath = new File(ctxPath);
		if (!dirPath.exists()) {
			dirPath.mkdir();
		}
		targetPathAndName=targetPathAndName+"."+endType;
		File uploadFile = new File(ctxPath + targetPathAndName);
		if(!uploadFile.getParentFile().exists()){
			uploadFile.getParentFile().mkdirs();
		}
		try {
			FileCopyUtils.copy(file.getBytes(), uploadFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return targetPathAndName;
	}
	public static ByteArrayInputStream  getCasheFileByRequestFile(HttpServletRequest  request,String fileParm){
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile(fileParm);
		if(file==null){
			return null;
		}
		if(file.isEmpty()){
			return null;
		}
		return new ByteArrayInputStream(file.getBytes());
	}
	
	
	/**
	 * 返回 kb
	 * @param filePath
	 * @return
	 */
	public static long getFileSize(String filePath){
		File file = new File(filePath);
		if(!file.exists()){
			return 0;
		}
		return file.length()/1024;
	}
	
	public static boolean isLargeFileSizeByRequestFile(HttpServletRequest  request,String fileParm,int m){
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile(fileParm);
		if(file==null){
			return false;
		}
		if(file.isEmpty()){
			return false;
		}
		// 获得文件名：
		if(file.getSize()<m*1024*1024){
			return true;
		}
		return false;
	}
	
	
	public static boolean checkFileTypeByRequestFile(HttpServletRequest  request,String fileParm,String[] fileType){
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile(fileParm);
		if(file==null){
			return false;
		}
		if(file.isEmpty()){
			return false;
		}
		String orgFileName=file.getOriginalFilename();
		return checkFileType(orgFileName,fileType);
	}
	
	
	/**
	 * 文件类型判断
	 * 
	 * @param fileName
	 * @return
	 */
	private static  boolean checkFileType(String fileName,String[] allowFiles) {
		Iterator<String> type = Arrays.asList(allowFiles).iterator();
		while (type.hasNext()) {
			String ext = type.next();
			if (fileName.toLowerCase().endsWith(ext)) {
				return true;
			}
		}
		return false;
	}
	
	
	public static boolean isExistLargeFileSizeByRequestFile(HttpServletRequest  request,String fileParm){
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile(fileParm);
		if(file==null){
			return false;
		}
		if(file.isEmpty()){
			return false;
		}
		return true;
	}
	/**
	 * 
	 * @param request
	 * @param fileParm
	 * @return true 不为空 false 为空
	 */
	public static boolean isFileEmptyByRequestFile(HttpServletRequest  request,String fileParm){
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile(fileParm);
		if(file==null){
			return false;
		}
		if(file.isEmpty()){
			return false;
		}
		return true;
	}
	
	
	/**
	 * 
	 * @param request
	 * @param fileParm
	 */
	public static String getFileTypeByRequestFile(HttpServletRequest  request,String fileParm){
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile(fileParm);
		if(file.isEmpty()){
			return null;
		}
		// 获得文件名：
		String realFileName = file.getOriginalFilename();
		String endType=FileUtils1.getExtensionName(realFileName);
		return endType;
	}
	public static String getRequestParam(HttpServletRequest request) {
		String alireqParam = "";
		try {
			Enumeration enu = request.getParameterNames();
			while (enu.hasMoreElements()) {
				String paraName = (String) enu.nextElement();
				alireqParam += paraName + "=" + request.getParameter(paraName)
						+ "&";
			}
			if (alireqParam.length() > 1) {
				alireqParam = alireqParam.substring(0, alireqParam.length() - 1);
			}
			if (alireqParam.length() > 2000) {
				alireqParam = alireqParam.substring(0, 2000);
			}
			
		} catch (Exception ex) {
		}
		return alireqParam;
	}
	/**
	 * 获取客户端ip
	 * 
	 * @param request
	 * @return
	 */
	public static String getClientIp(HttpServletRequest request) {
		try {
			String ip = request.getHeader("x-forwarded-for");
			if (ip == null || ip.length() == 0
					|| "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0
					|| "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0
					|| "unknown".equalsIgnoreCase(ip)) {
				ip = request.getRemoteAddr();
			}
			if (ip.length() > 18) {
				ip = ip.substring(0, 18);
			}

			return ip;
		} catch (Exception ex) {
			return "";
		}

	}

}	
