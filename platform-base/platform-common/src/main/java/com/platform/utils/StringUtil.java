package com.platform.utils;

public class StringUtil {
	  private static final char[] zeroArray = "0000000000000000".toCharArray();
	/**
	 * 判断字符串是否为null或者""，首先trim然后再进行判断 
		 StringUtils.isBlank(null)      = true
		 StringUtils.isBlank("")        = true
		 StringUtils.isBlank(" ")       = true
		 StringUtils.isBlank("bob")     = false
		 StringUtils.isBlank("  bob  ") = false
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		return (str == null) || (str.length() == 0);
	}
	
	/**
	 * 判断字符串是否为null或者""，首先trim然后再进行判断 
	 StringUtils.isBlank(null)      = true
	 StringUtils.isBlank("")        = true
	 StringUtils.isBlank(" ")       = true
	 StringUtils.isBlank("bob")     = false
	 StringUtils.isBlank("  bob  ") = false
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}
	
	/**
	 * 判断字符串是否为null或者""，首先trim然后再进行判断 
		 StringUtils.isBlank(null)      = true
		 StringUtils.isBlank("")        = true
		 StringUtils.isBlank(" ")       = true
		 StringUtils.isBlank("bob")     = false
		 StringUtils.isBlank("  bob  ") = false
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str) {
		int strLen;
		if ((str == null) || ((strLen = str.length()) == 0))
			return true;
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 判断字符串是否为null或者""，首先trim然后再进行判断 
		 StringUtils.isBlank(null)      = true
		 StringUtils.isBlank("")        = true
		 StringUtils.isBlank(" ")       = true
		 StringUtils.isBlank("bob")     = false
		 StringUtils.isBlank("  bob  ") = false
	 * @param str
	 * @return
	 */
	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}
	
	public static final String zeroPadString(String string, int length)
	  {
	    if ((string == null) || (string.length() > length)) {
	      return string;
	    }
	    StringBuffer buf = new StringBuffer(length);
	    buf.append(zeroArray, 0, length - string.length()).append(string);
	    return buf.toString();
	  }
	public static String replace(String src, String mod, String str)
	  {
	    if ((src == null) || (src.length() == 0)) {
	      return src;
	    }
	    if ((mod == null) || (mod.length() == 0)) {
	      return src;
	    }
	    if (src == null) {
	      src = "";
	    }
	    StringBuffer buffer = new StringBuffer();
	    int idx1 = 0;
	    int idx2 = 0;
	    while ((idx2 = src.indexOf(mod, idx1)) != -1) {
	      buffer.append(src.substring(idx1, idx2)).append(str);
	      idx1 = idx2 + mod.length();
	    }
	    buffer.append(src.substring(idx1));
	    return buffer.toString();
	  }

}
