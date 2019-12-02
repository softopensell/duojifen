package com.platform.util.wechat;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import com.platform.util.CommonUtil;

public class Test {

		// TODO Auto-generated method stub

		public static void main(String[] args) {
			 SortedMap<String, String> packageParams = new TreeMap<String, String>();
				packageParams.put("noncestr", "eccd3001-dd35-46c3-b7ab-bc465531723d");
				packageParams.put("jsapi_ticket", "HoagFKDcsGMVCIY2vOjf9utYyEuTbkfEmVrpLUSvbtq8cnzIoNcEOLcIwfGdMlU8s3D9OAM3Mkyy6TOMzQ_8qA");
				packageParams.put("timestamp", "1555294799");
				packageParams.put("url", "http://water.yussin.com/guanshifu/index.html?0.28383502932965454");
				
//				CommonUtil.sortMap(packageParams);
				
				StringBuffer sb = new StringBuffer();
				Set es = packageParams.entrySet();
				Iterator it = es.iterator();
				while (it.hasNext()) {
					Map.Entry entry = (Map.Entry) it.next();
					String k = (String) entry.getKey();
					String v = (String) entry.getValue();
					if (null != v && !"".equals(v) && !"sign".equals(k)
							&& !"key".equals(k)) {
						sb.append(k + "=" + v + "&");
					}
				}

//				sb.append("key=" + this.getKey());
				
				String tostring = sb.toString();
				tostring = tostring.substring(0, tostring.length() - 1);
				System.out.println("-----------createSign-----sb.toString() =="+tostring);
				
				
				 //验证用户信息完整性
//		        String sign = CommonUtil.getSha1(sb.toString());
		        String sign = CommonUtil.getSha1(tostring);
				
		        System.out.println("---- 获取微信getWxConfig--createSign---------:" + sign);
		}

}
