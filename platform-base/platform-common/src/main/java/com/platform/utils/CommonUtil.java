package com.platform.utils;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CommonUtil {

    /**
     * 生成订单的编号order_sn
     */
    public static String generateOrderNumber() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        String timeStr = DateUtils.format(cal.getTime(), DateUtils.DATE_TIME_PATTERN_YYYY_MM_DD_HH_MM_SS_SSS);
        return timeStr + CharUtil.getRandomNum(6);
    }
    
    /**
     * 生成支付订单的编号pay_order_sn
     */
    public static String generatePayOrderNumber() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        String timeStr = DateUtils.format(cal.getTime(), DateUtils.DATE_TIME_PATTERN_YYYY_MM_DD_HH_MM_SS_SSS);
        return timeStr + CharUtil.getRandomNum(4);
    }
    public static String generateGoodsCode() {
    	
    	return System.currentTimeMillis()+""+(char)(Math.random()*26+'A');
    }

    public static String getSha1(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }
    
	public static List sortMap(Map peCorporeityMap) {
		//然后我们可以将Map集合转换成List集合中，而List使用ArrayList来实现如下：
		List<Map.Entry<String,Integer>> list = new ArrayList<Map.Entry<String,Integer>>(peCorporeityMap.entrySet());
		// 最后通过Collections.sort(List l, Comparator c)方法来进行排序
		Collections.sort(list,new Comparator<Map.Entry<String,Integer>>() {
		    //升序排序
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				// TODO Auto-generated method stub
//        				return o1.getValue()-o2.getValue();//顺序
				return o2.getValue()-o1.getValue();//倒序
			}

		});
		return list;
	}
}
