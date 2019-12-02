package com.platform.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 名称：StringUtils <br>
 * 描述：String工具类<br>
 *
 * @author 李鹏军
 * @version 1.0
 * @since 1.0.0
 */
public class StringUtils {
    public static final String EMPTY = "";
    private static Pattern linePattern = Pattern.compile("_(\\w)");

    /**
     * 判断字符串是否不为空，不为空则返回true
     *
     * @param str 源数据
     * @return Boolean
     */
    public static boolean isNotEmpty(String str) {
        if (str != null && !"".equals(str.trim())) {
            return true;
        }
        return false;
    }

    /**
     * 判断对象或对象数组中每一个对象是否为空: 对象为null，字符序列长度为0，集合类、Map为empty
     *
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
     * 下划线转驼峰
     */
    public static String lineToHump(String str) {
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
    /**
     * Object 对象转换成字符串
     * @param obj
     * @return
     */
    public static String toStringByObject(Object obj){
        return toStringByObject(obj,false,null);
    }
    /**
     * Object 对象转换成字符串，并可以根据参数去掉两端空格
     * @param obj
     * @return
     */
    public static String toStringByObject(Object obj, boolean isqdkg, String datatype){
        if(obj==null){
            return "";
        }else{
            if(isqdkg){
                return obj.toString().trim();
            }else{
                //如果有设置时间格式类型，这转换
                if(StringUtils.hasText(datatype)){
                    if(obj instanceof Timestamp){
                        return DateUtils.format((Timestamp)obj,datatype);
                    }else if(obj instanceof Date){
                        return DateUtils.format((Timestamp)obj,datatype);
                    }
                }
                return obj.toString();
            }


        }
    }
    public static boolean hasText(CharSequence str) {
        if (!hasLength(str)) {
            return false;
        } else {
            int strLen = str.length();

            for(int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    return true;
                }
            }

            return false;
        }
    }

    public static boolean hasLength(CharSequence str) {
        return str != null && str.length() > 0;
    }

    public static boolean hasText(String str) {
        return hasText((CharSequence)str);
    }

    public static int parseInt(Object str) {
        return parseInt(str, 0);
    }

    public static int parseInt(Object str, int defaultValue) {
        if (str == null || str.equals("")) {
            return defaultValue;
        }
        String s = str.toString().trim();
        if (!s.matches("-?\\d+")) {
            return defaultValue;
        }
        return Integer.parseInt(s);
    }
    
    public final static boolean isLegalUrl(String url) {
		return url
				.matches("^((https|http|ftp|rtsp|mms)?://)"
						+ "+(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?"
						+ "(([0-9]{1,3}\\.){3}[0-9]{1,3}" + "|"
						+ "([0-9a-z_!~*'()-]+\\.)*"
						+ "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\."
						+ "[a-z]{2,6})" + "(:[0-9]{1,4})?" + "((/?)|"
						+ "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$");
	}
	/**
	 * 
	 * @param email abc@126.com
	 * @return ****@126.com
	 */
	public static String hideEmail(String email){
		String abc=email.substring(0,email.indexOf('@'));
		email=email.replace(abc, "****");
		return email;
	}
	/**
	 * 
	 * @param phone 18201211111
	 * @return 1820121****
	 */
	public static String hidePhone(String phone){
		String abc=phone.substring(phone.length()-4,phone.length());
		phone=phone.replace(abc, "****");
		return phone;
	}
	/**
	 * 
	 * @param phone 18201211111
	 * @return 182****1111
	 */
	public static String hidePhone2(String phone){
		phone = phone.substring(0, 3) + "****" + phone.substring(7, phone.length());
		return phone;
	}
	public static String Html2Text(String inputString){
	     String htmlStr = inputString; //含html标签的字符串
	     String textStr ="";
	     java.util.regex.Pattern p_script;
	     java.util.regex.Matcher m_script;
	     java.util.regex.Pattern p_style;
	     java.util.regex.Matcher m_style;
	     java.util.regex.Pattern p_html;
	     java.util.regex.Matcher m_html;
	    try{
	          String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; //定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script> }
	          String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; //定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style> }
	          String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式
	          p_script = Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE);
	          m_script = p_script.matcher(htmlStr);
	          htmlStr = m_script.replaceAll(""); //过滤script标签
	          p_style = Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE);
	          m_style = p_style.matcher(htmlStr);
	          htmlStr = m_style.replaceAll(""); //过滤style标签
	          p_html = Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE);
	          m_html = p_html.matcher(htmlStr);
	          htmlStr = m_html.replaceAll(""); //过滤html标签
	          textStr = htmlStr;
	     }catch(Exception e){
	     }
	     return textStr;//返回文本字符串
	 }  
	public static String Html2NoScriptText(String inputString){
	     String htmlStr = inputString; //含html标签的字符串
	     String textStr ="";
	     java.util.regex.Pattern p_script;
	     java.util.regex.Matcher m_script;
	    try{
	          String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; //定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script> }
	          p_script = Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE);
	          m_script = p_script.matcher(htmlStr);
	          htmlStr = m_script.replaceAll(""); //过滤script标签
	          textStr = htmlStr;
	     }catch(Exception e){
	     }
	     return textStr;//返回文本字符串
	 }  
	/**
	 * 判断字符串是否为空
	 * @param s
	 * @return
	 */
	public final static boolean isEmpty(String s) {
		return s == null || s.length() == 0||s.equals("");
	}
	public final static boolean isNotBlank(String s) {
		return !(s == null || s.length() == 0||s.equals(""));
	}
	
	/**
	 * 判断字符串是否逻辑为空，trim后的长度是否为0
	 * @param s
	 * @return
	 */
	public final static boolean isLogicEmpty(String s) {
		return s == null || s.trim().length() == 0;
	}

	/**
	 * 是否是合法的UC用户名
	 * @param s
	 * @return
	 */
	public final static boolean isLegalUserName(String s) {
		//TODO 逻辑未实现
		return false;
	}

	/**
	 * 是否是合法的ip地址
	 * @param ip
	 * @return
	 */
	public final static boolean isLegalIpAddress(String ip) {
		if (isEmpty(ip)) {
			return false;
		}
		return IP4PATTERN.matcher(ip).matches()
				|| IP6PATTERN.matcher(ip).matches();
	}

	/**
	 * IPV4的地址正则
	 */
	private static final Pattern IP4PATTERN = Pattern
			.compile(
					"^(([1-9][0-9]?)|(1[0-9]{2})|(2[0-4][0-9])|(25[0-5]))\\."
							+ "((0)|([1-9][0-9]?)|(1[0-9]{2})|(2[0-4][0-9])|(25[0-5]))\\."
							+ "((0)|([1-9][0-9]?)|(1[0-9]{2})|(2[0-4][0-9])|(25[0-5]))\\."
							+ "((0)|([1-9][0-9]?)|(1[0-9]{2})|(2[0-4][0-9])|(25[0-5]))$",
					Pattern.CASE_INSENSITIVE);

	/**
	 * IPV6的地址的正则
	 */
	private static final Pattern IP6PATTERN = Pattern
			.compile(
					"^(^::$)|(^([\\d|a-fA-F]{1,4}:){7}([\\d|a-fA-F]{1,4})$)"
							+ "|(^(::(([\\d|a-fA-F]{1,4}):){0,5}([\\d|a-fA-F]{1,4}))$)"
							+ "|(^(([\\d|a-fA-F]{1,4})(:|::)){0,6}([\\d|a-fA-F]{1,4})$)$",
					Pattern.CASE_INSENSITIVE);

	private static char md5Chars[] = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	private static MessageDigest messagedigest;

	/**
	 * 获取一个字符串的md5
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String getStringMD5String(String str)
			throws Exception {
		messagedigest = MessageDigest.getInstance("MD5");
		messagedigest.update(str.getBytes());
		return bufferToHex(messagedigest.digest());
	}

	/**
	 * 验证一个字符串的MD5
	 * @param str
	 * @param md5
	 * @return
	 * @throws Exception
	 */
	public static boolean check(String str, String md5)
			throws Exception {
		if (getStringMD5String(str).equals(md5))
			return true;
		else
			return false;
	}

	private static String bufferToHex(byte bytes[]) {
		return bufferToHex(bytes, 0, bytes.length);
	}

	private static String bufferToHex(byte bytes[], int m, int n) {
		StringBuffer stringbuffer = new StringBuffer(2 * n);
		int k = m + n;
		for (int l = m; l < k; l++) {
			appendHexPair(bytes[l], stringbuffer);
		}
		return stringbuffer.toString();
	}

	private static void appendHexPair(byte bt,
			StringBuffer stringbuffer) {
		char c0 = md5Chars[(bt & 0xf0) >> 4];
		char c1 = md5Chars[bt & 0xf];
		stringbuffer.append(c0);
		stringbuffer.append(c1);
	}
	
	/**
	 * GBK验证
	 */
	public static boolean isGBK(String input) {
		try {
			if (input.equals(new String(input.getBytes("GBK"), "GBK"))) {
				return true;
			} else {
				return false;
			}
		} catch (UnsupportedEncodingException e) {
			return false;
		}
	}
	
	/**
	 * 将一个list组装成字符串，list各个元素之间以split分割
	 * 
	 * @param collection
	 * @param split 分隔符, 如果为null, 则返回长度为0的字符串
	 * @return
	 */
	public static String makeStrFromCollection(final Collection<? extends Object> collection,
			final String split) {

		if (collection == null || collection.isEmpty()) {
			return "";
		}
		if (split == null){
			return "";
		}

		StringBuilder builder = new StringBuilder();

		for(Object element : collection){
			builder.append(element).append(split);
		}
		builder.delete(builder.length()-split.length(), builder.length());

		return builder.toString();
	}
	
	/**
	 * 把以分割的字符串切割成字符串列表，每个字符串进行trim,列表中不包含空字符串
	 * 
	 * @param listString
	 * @param split 连接的字符串，正则表达式
	 * @return
	 */
	public static List<String> splitToList(final String listString, final String split) {
		List<String> result = new ArrayList<String>();
		if (listString == null) {
			return result;
		}
		if(split== null || split.length() == 0){
			return result;
		}
		String tmpListString = listString.trim();

		String[] array = tmpListString.split(split);
		for (String str : array) {
			str = str.trim();
			if (!"".equals(str)) {
				result.add(str);
			}
		}
		return result;
	}
	
	

	public static boolean sqlValidate(String str) {
		str = str.toLowerCase();// 统一转为小写
		String badStr = "'|and|exec|execute|insert|select|delete|update|count|drop|*|%|chr|mid|master|truncate|"
				+
				"char|declare|sitename|net user|xp_cmdshell|;|or|-|+|,|like'|and|exec|execute|insert|create|drop|"
				+
				"table|from|grant|use|group_concat|column_name|"
				+
				"information_schema.columns|table_schema|union|where|select|delete|update|order|by|count|*|"
				+
				"chr|mid|master|truncate|char|declare|or|;|-|--|+|,|like|//|/|%|#";// 过滤掉的sql关键字，可以手动添加
		String[] badStrs = badStr.split("\\|");
		for (int i = 0; i < badStrs.length; i++) {
			if (str.indexOf(badStrs[i]) >= 0) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 字符串转换unicode
	 */
	public static String string2Unicode(String string) {
	 
	    StringBuffer unicode = new StringBuffer();
	 
	    for (int i = 0; i < string.length(); i++) {
	 
	        // 取出每一个字符
	        char c = string.charAt(i);
	 
	        // 转换为unicode
	        unicode.append("\\u" + Integer.toHexString(c));
	    }
	 
	    return unicode.toString();
	}
	/**
	 * 字符串转换unicode
	 */
	public static String str2Unicode(String str) {
		String encoderStr=str;
		try {
			encoderStr = URLEncoder.encode(str,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encoderStr;
	}
	
	/**
	 * unicode 转字符串
	 */
	public static String unicode2String(String unicode) {
	 
	    StringBuffer string = new StringBuffer();
	 
	    String[] hex = unicode.split("\\\\u");
	 
	    for (int i = 1; i < hex.length; i++) {
	 
	        // 转换出每一个代码点
	        int data = Integer.parseInt(hex[i], 16);
	 
	        // 追加成string
	        string.append((char) data);
	    }
	 
	    return string.toString();
	}
	
	public static String decodeUnicode(String theString) {
	    char aChar;
	    int len = theString.length();
	    StringBuffer outBuffer = new StringBuffer(len);
	    for (int x = 0; x < len;) {
	      aChar = theString.charAt(x++);
	      if (aChar == '\\') {
	        aChar = theString.charAt(x++);
	        if (aChar == 'u') {
	          // Read the xxxx
	          int value = 0;
	          for (int i = 0; i < 4; i++) {
	            aChar = theString.charAt(x++);
	            switch (aChar) {
	              case '0':
	              case '1':
	              case '2':
	              case '3':
	              case '4':
	              case '5':
	              case '6':
	              case '7':
	              case '8':
	              case '9':
	                value = (value << 4) + aChar - '0';
	                break;
	              case 'a':
	              case 'b':
	              case 'c':
	              case 'd':
	              case 'e':
	              case 'f':
	                value = (value << 4) + 10 + aChar - 'a';
	                break;
	              case 'A':
	              case 'B':
	              case 'C':
	              case 'D':
	              case 'E':
	              case 'F':
	                value = (value << 4) + 10 + aChar - 'A';
	                break;
	              default:
	                throw new IllegalArgumentException("Malformed   \\uxxxx   encoding.");
	            }
	          }
	          outBuffer.append((char) value);
	        } else {
	          if (aChar == 't')
	            aChar = '\t';
	          else if (aChar == 'r')
	            aChar = '\r';
	          else if (aChar == 'n')
	            aChar = '\n';
	          else if (aChar == 'f')
	            aChar = '\f';
	          outBuffer.append(aChar);
	        }
	      } else
	        outBuffer.append(aChar);
	    }
	    return outBuffer.toString();
	  }
	public static String fomartStringAll(String orgStr) {
        orgStr=fomartStringNoNull(orgStr);
        orgStr=fomartStringNoSign(orgStr);
        return orgStr;
	}
	public static String fomartStringNoNumber(String orgStr) {
        //去除字符串中的数字  
        return orgStr.replaceAll("\\d+","");  
	}
	
	 public static String filterEmoji(String str) {  
	        if(str.trim().isEmpty()){  
	            return str;  
	        }  
	        String pattern="[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]";  
	        String reStr="";  
	        Pattern emoji=Pattern.compile(pattern);  
	        Matcher  emojiMatcher=emoji.matcher(str);  
	        str=emojiMatcher.replaceAll(reStr);  
	        return str;  
	    }  
	public static String fomartStringNoChar(String orgStr) {
		//去除字符串中的字母和数字  
        return orgStr.replaceAll("[A-Za-z0-9]+","");  
	}
	public static String fomartStringNoSign(String orgStr) {
		  //去除字符串中的符号例如@#￥%=+-....  
		orgStr=orgStr.trim().replaceAll("_", "").replaceAll("/", "").replaceAll("\\(|\\)", "").replaceAll("\\s*", "");
        return orgStr.replaceAll("\\p{Punct}","");  
	}
	public static String fomartStringNoNull(String orgStr) {
	        //去除字符串中的空格、回车、换行符、制表符  
	        Pattern p = Pattern.compile("\\s*|\t|\r|\n");  
	        Matcher m = p.matcher(orgStr);  
	        return  m.replaceAll("");  
	}
	  /**
	* 描述：是否是中文.
	*
	* @param str
	*            指定的字符串
	* @return 是否是中文:是为true，否则false
	*/
	public static Boolean isChinese(String str) {
		Boolean isChinese = true;// [\u4e00-\u9fa5] [\u0391-\uFFE5]
		String chinese = "[\u4e00-\u9fa5]";
		if (!isEmpty(str)) {
			// 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
			for (int i = 0; i < str.length(); i++) {
				// 获取一个字符
				String temp = str.substring(i, i + 1);
				// 判断是否为中文字符
				if (temp.matches(chinese)) {
				} else {
					isChinese = false;
				}
			}
		}
		return isChinese;
	}
	public static void main(String[] args) {
		String oldUserIds="1104#-1103#-1089#-1088#-1000#-0";
		List<String> strUserIds=StringUtils.splitToList(oldUserIds,"#-1088#-");
		String newUserIds="";
		if(strUserIds.size()>0) {
			newUserIds=strUserIds.get(0);
		}
		newUserIds=newUserIds+"#-"+"1088";
		
		
		System.out.println(JsonUtil.getJsonByObj(newUserIds));
		
		
	}
}
