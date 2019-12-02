package com.platform.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.StringWriter;
import java.io.Writer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
public class StringUtil {
	private static Logger LOG = Logger.getLogger(StringUtil.class);

	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	public static boolean isEmpty(String str) {
		if (str == null || str.length() == 0 || str.trim().length() == 0) {
			return true;
		}
		return false;
	}

	public static String NullToEmpty(Object o) {
		if (o == null||"null".equals(o.toString()))
			return "";
		else
			return o.toString();
	}
	public static String getSexFromId(String tIdNo) {
		try{
			if (tIdNo.length() != 15 && tIdNo.length() != 18) {
				return "";
			}
			String sex = "";
			if (tIdNo.length() == 15) {
				sex = tIdNo.substring(14, 15);
			} else {
				sex = tIdNo.substring(16, 17);
			}
			try {
				int iSex = Integer.parseInt(sex);
				iSex %= 2;
				if (iSex == 0) {
					return "女";
				}
				if (iSex == 1) {
					return "男";
				}
			} catch (Exception ex) {
				return "";
			}
		}catch(Exception e){
			LOG.error("获取男女失败",e);
			return "";
		}
		return "";
	}
	// MD5加密
	public static String md5(String source) {
		if (source == null) {
			return null;
		}
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(source.getBytes());
			byte tmp[] = md.digest();
			char str[] = new char[16 * 2];
			int k = 0;
			for (int i = 0; i < 16; i++) {
				byte byte0 = tmp[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (NoSuchAlgorithmException e) {
			LOG.error("A MD5 exception is occur.");
			return null;
		}
	}

	// json字符串转换成java对象
	public static <T> T json2Object(String json, Class<T> c) {
		T o = null;
		try {
			o = new ObjectMapper().readValue(json, c);
		} catch (Exception e) {
			LOG.error(e);
		}
		return o;
	}

	/**   
	* 从一个JSON 对象字符格式中得到一个java对象，形如：   
	* {"id" : idValue, "name" : nameValue, "aBean" : {"aBeanId" : aBeanIdValue, ...}}   
	* @param object   
	* @param clazz   
	* @return   
	*/
	public static Object getDTO(String jsonString, Class clazz) {
		JSONObject jsonObject = null;
		try {

			jsonObject = JSONObject.fromObject(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSONObject.toBean(jsonObject, clazz);
	}

	// java对象转换成json字符串
	public static String object2JSON(Object o) {
		ObjectMapper om = new ObjectMapper();
		Writer w = new StringWriter();
		String json = null;
		try {
			om.writeValue(w, o);
			json = w.toString();
			w.close();
		} catch (Exception e) {
			LOG.error(e);
		}
		return json;
	}

	/**
	 * 将JSON转化为MAP
	 * @author liming
	 * @param jsonStr
	 * @return
	 */
	public static Map<String, Object> parseJSON2Map(String jsonStr) {
		Map<String, Object> map = new HashMap<String, Object>();
		//最外层解析  
		JSONObject json = JSONObject.fromObject(jsonStr);
		for (Object k : json.keySet()) {
			Object v = json.get(k);
			//如果内层还是数组的话，继续解析  
			if (v instanceof JSONArray) {
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				Iterator<JSONObject> it = ((JSONArray) v).iterator();
				while (it.hasNext()) {
					JSONObject json2 = it.next();
					list.add(parseJSON2Map(json2.toString()));
				}
				map.put(k.toString(), list);
			} else {
				map.put(k.toString(), v);
			}
		}
		return map;
	}

	/**
	 * 将data第一层数据存储在att，以便较验
	 * @param request
	 */
	public static void setDataToRequest(HttpServletRequest request) {
		String datajson = request.getParameter("cd");
		//parseJSON2Map(datajson);
		JSONObject json = JSONObject.fromObject(datajson);
		for (Object k : json.keySet()) {
			Object v = json.get(k);
			if (v instanceof JSONArray) {

			} else {
				request.setAttribute(k.toString(), v);

			}
		}
	}

	/**
	 * 打印请求参数
	 * @author liming
	 * @param request
	 */
	public static void printRequestPar(HttpServletRequest request) {
		String parString = "";
		//获取请求参数KEY集合
		Enumeration<String> enumeration = request.getParameterNames();
		int i = 0;
		while (enumeration.hasMoreElements()) {
			String key = enumeration.nextElement();
			//不打印签名信息
			if (key.equals("sign") || key.equals("timestamp"))
				continue;
			//隐藏密码信息
			if (key.toLowerCase().indexOf("pwd") >= 0 || key.toLowerCase().indexOf("password") >= 0) {
				if (i > 0)
					parString += "," + key + ": ******(密码值不显示)";
				else
					parString = "" + key + ":******(密码值不显示)";
			} else {
				if (i > 0)
					parString += "," + key + ": " + request.getParameter(key);
				else
					parString = "" + key + ": " + request.getParameter(key);
			}
			i++;
		}
//		LOG.info(" 请求地址：" + request.getRequestURI() + "\n\t\t\t\t\t\t\t" + "参数：" + parString + "\n\t\t\t\t\t\t\t"
//				+ "来自IP:" + IpConvert.getIpAddr(request));
	}

	/**
	 * 返回byte的数据大小对应的文本
	 * 
	 * @param size
	 * @return
	 */
	public static String getDataSize(long size) {
		DecimalFormat formater = new DecimalFormat("####.00");
		if (size < 1024) {
			return size + "bytes";
		} else if (size < 1024 * 1024) {
			float kbsize = size / 1024f;
			return formater.format(kbsize) + "KB";
		} else if (size < 1024 * 1024 * 1024) {
			float mbsize = size / 1024f / 1024f;
			return formater.format(mbsize) + "MB";
		} else if (size < 1024 * 1024 * 1024 * 1024) {
			float gbsize = size / 1024f / 1024f / 1024f;
			return formater.format(gbsize) + "GB";
		} else {
			return "size: error";
		}
	}

	//验证码相关 
	public static int WIDTH = 65;// 设置图片的宽度
	public static int HEIGHT = 22;// 设置图片的高度

	public static void drawBackground(Graphics g) {
		g.setColor(new Color(0xDCDCDC));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		for (int i = 0; i < 120; i++) {
			int x = (int) (Math.random() * WIDTH);
			int y = (int) (Math.random() * HEIGHT);
			int red = (int) (Math.random() * 255);
			int green = (int) (Math.random() * 255);
			int blue = (int) (Math.random() * 255);
			g.setColor(new Color(red, green, blue));
			g.drawOval(x, y, 1, 0);
		}
	}

	public static void drawRands(Graphics g, char[] rands) {
		Random random = new Random();
		int red = random.nextInt(110);
		int green = random.nextInt(50);
		int blue = random.nextInt(50);
		g.setColor(new Color(red, green, blue));
		g.setFont(new Font(null, Font.ITALIC | Font.BOLD, 18));
		g.drawString("" + rands[0], 1, 17);
		g.drawString("" + rands[1], 16, 15);
		g.drawString("" + rands[2], 31, 18);
		g.drawString("" + rands[3], 46, 16);
	}

	public static char[] generateCheckCode() {
		String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		char[] rands = new char[4];
		for (int i = 0; i < 4; i++) {
			int rand = (int) (Math.random() * 36);
			rands[i] = chars.charAt(rand);
		}
		return rands;
	}

	public static String moneyFormat(Double money) {
		NumberFormat nf = new DecimalFormat("#,##0.00#元");//NumberFormat.getCurrencyInstance(Locale.CHINA);//
		String moneyStr = nf.format(money);
		return moneyStr;
	}
	/**
	 * 后缀不要元
	 * @param money
	 * @return
	 */
	public static String moneyFormat2(Double money) {
		NumberFormat nf = new DecimalFormat("#,##0.00#");//NumberFormat.getCurrencyInstance(Locale.CHINA);//
		String moneyStr = nf.format(money);
		return moneyStr;
	}
	/**
	 * 去掉样式字段
	 * @return
	 */
	public static String removeStyleChar(String styleStr) {
		if (styleStr == null)
			return null;
		char[] cs = styleStr.toCharArray();
		StringBuilder sb = new StringBuilder("");
		boolean b = true;
		for (int i = 0; i < cs.length; i++) {
			if (cs[i] == '<') {
				b = false;
				continue;
			}
			if (cs[i] == '>') {
				b = true;
				continue;
			}
			if (b) {
				sb.append(cs[i]);
			}
		}
		int len = sb.length();

		sb = new StringBuilder(sb.toString().replaceAll("马上摇奖。", ""));
		int index = sb.indexOf("91教课网团队");
		if (index > 0) {
			return sb.substring(0, index);
		} else {
			return sb.substring(0, len);
		}

		//		if(styleStr==null) return null;
		//		styleStr=styleStr.replaceAll("<span>", "");
		//		styleStr=styleStr.replaceAll("</a>", "");
		//		styleStr=styleStr.replaceAll("</div>", "");
		//		styleStr=styleStr.replaceAll("<br/>", "");
		//		
		//		StringBuilder sb=new StringBuilder(styleStr2);
		//		while(styleStr.indexOf("<div")>=0){
		//			int start=styleStr.indexOf("<div");
		//			int end=styleStr.indexOf(">");	
		//			styleStr=styleStr.substring(0, start)+""+styleStr.substring(end+1,styleStr.length());
		//		}
	}

	/**
	 * 金额格式化
	 * @param s 金额
	 * @param len 小数位数
	 * @return 格式后的金额
	 */
	public static String formatMoney(String s, int len) {
		if (s == null || s.length() < 1) {
			return "";
		}
		NumberFormat formater = null;
		double num = Double.parseDouble(s);
		if (len == 0) {
			formater = new DecimalFormat("###,##0.00");

		} else {
			StringBuffer buff = new StringBuffer();
			buff.append("###,##0.00");
			for (int i = 0; i < len; i++) {
				buff.append("#");
			}
			formater = new DecimalFormat(buff.toString());
		}
		return formater.format(num);
	}

	/**
	 * 去除 地区 名称 所带的'省'、'自治区'、'市' 后缀
	 * @param name
	 * @return
	 */
	public static String parseAreaName(String areaName) {
		if (isEmpty(areaName)) {
			return areaName;
		}
		int index = areaName.indexOf("省");
		if (index < 0) {
			index = areaName.indexOf("自治区");
		}
		if (index < 0) {
			index = areaName.indexOf("市");
		}
		if (index < 0) {
			return areaName;
		}
		return areaName.substring(0, index);
	}

	/**
	 * 判断对象或对象数组中每一个对象是否为空: 对象为null，字符序列长度为0，集合类、Map为empty
	 * @author zl
	 * @param obj
	 * @return
	 */
	public static boolean isNullOrEmpty(Object obj) {
		if (obj == null)
			return true;

		if (obj instanceof CharSequence)
			return ((CharSequence) obj).length() == 0;

		if (obj instanceof Collection)
			return ((Collection) obj).isEmpty();

		if (obj instanceof Map)
			return ((Map) obj).isEmpty();

		if (obj instanceof Object[]) {
			Object[] object = (Object[]) obj;
			if (object.length == 0) {
				return true;
			}
			boolean empty = true;
			for (int i = 0; i < object.length; i++) {
				if (!isNullOrEmpty(object[i])) {
					empty = false;
					break;
				}
			}
			return empty;
		}
		return false;
	}

	/**
	 * 校验银行卡卡号
	 * @param cardId
	 * @return
	 */
	public static boolean checkBankCardNo(String bankCardNo) {
		if (bankCardNo == null || bankCardNo.length() == 0 || !bankCardNo.matches("\\d+")) {
			return false;
		}
		char bit = getBankCardCheckCode(bankCardNo.substring(0, bankCardNo.length() - 1));
		if (bit == 'N') {
			return false;
		}
		return bankCardNo.charAt(bankCardNo.length() - 1) == bit;
	}

	/**
	 * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
	 * @param nonCheckCodeCardId
	 * @return
	 */
	public static char getBankCardCheckCode(String nonCheckCodeCardId) {
		if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
				|| !nonCheckCodeCardId.matches("\\d+")) {
			//如果传的不是数据返回N
			return 'N';
		}
		char[] chs = nonCheckCodeCardId.trim().toCharArray();
		int luhmSum = 0;
		for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
			int k = chs[i] - '0';
			if (j % 2 == 0) {
				k *= 2;
				k = k / 10 + k % 10;
			}
			luhmSum += k;
		}
		return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
	}

	private static Pattern pattern1 = Pattern.compile("[0-9]");
	private static Pattern pattern2 = Pattern.compile("[a-z]");
	private static Pattern pattern3 = Pattern.compile("[A-Z]");
	private static Pattern pattern4 = Pattern.compile("[`~!@#$^&*()_=|{}':;',\\[\\].<>/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？]");

	/**
	 * 检测 用户密码强度
	 * 
	 * @param pwd
	 * @return
	 */
	public static boolean checkUserPwd(String pwd) {
		if (pwd == null || pwd.indexOf(" ") != -1) {
			return false;
		}
		int levelNum = 0;
		if (pattern1.matcher(pwd).find()) {
			levelNum++;
		}
		if (pattern2.matcher(pwd).find()) {
			levelNum++;
		}
		if (pattern3.matcher(pwd).find()) {
			levelNum++;
		}
		if (pattern4.matcher(pwd).find()) {
			levelNum++;
		}
		if (levelNum < 2) {
			return false;
		}
		return true;
	}
	/**
	 * 将回车 ，等处理成<br>格式
	 * @return
	 */
	public static String enterToBr(Object o){
		if(o==null )return "";
		String str=o.toString();
		StringTokenizer st=new StringTokenizer(str,",\r\n. ");
		String str2="";
		int i=0;
		while(st.hasMoreTokens()){
			if(i==0)
				str2+=st.nextToken()+"<br>";
			else str2+=st.nextToken();
			i++;
		}
		return str2;
	}
	/**
	 * 获取客户端版本号
	 * 
	 * @param version
	 * @return 返回0表示获取失败
	 */
	public static int getClientVisionCode(String version ){
		if(version == null){
			return 0;
		}
		String versionCode = version.toUpperCase().replaceAll("V","");
		versionCode = versionCode.replaceAll("\\.","");
		try{
			return Integer.parseInt(versionCode);
		}catch(Exception e){
			LOG.error("getClientVisionCode is error",e);
		}
		return 0;
	}
	public static void main(String[] args) {
		int code = getClientVisionCode("V1.4.1");
		
		System.out.println(code);
	}
}
