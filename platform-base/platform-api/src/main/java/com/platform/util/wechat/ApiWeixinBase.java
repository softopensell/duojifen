package com.platform.util.wechat;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.SortedMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.platform.util.CommonUtil;
import com.platform.util.HttpClientUtil;
import com.platform.util.HttpKit;
import com.platform.util.StringUtil;

public class ApiWeixinBase{
	protected final static  Logger logger = Logger.getLogger(ApiWeixinBase.class);
	private static String   createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	private static String   sysAdminTokenURL = "https://api.weixin.qq.com/cgi-bin/token";
	private static String   userAccessTokenURL = "https://api.weixin.qq.com/sns/oauth2/access_token";
	private static String   getTicketURL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket";
	private static String   getBackIpURL = "https://api.weixin.qq.com/cgi-bin/getcallbackip";
	private static String   getAuthorURL = "https://open.weixin.qq.com/connect/oauth2/authorize";
	private static String   getOpenTokenURL = "https://api.weixin.qq.com/sns/oauth2/access_token";
	private static String   getUserInfoURL = "https://api.weixin.qq.com/sns/userinfo";
	private static String   getUSERURL = "https://api.weixin.qq.com/cgi-bin/user/get";
	public static  String   getRefundURL = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";
	public static  String   sendredpackUrl = "https://api.mch.weixin.qq.com/mmpaymkttransfers/sendredpack";
	
	private static Properties props = new Properties(); 
	static{
		try {
			props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("platform.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static  WeixinBaseConfig getWeixinBaseConfig() {
		String callBackUrl = "";
		String appId =props.getProperty("mp.appId");// "wxa06286a707aa31c2";
		String appSecret =props.getProperty("mp.secret");// "fcc6a42d840d50030b52e0dba6797077";
	    String partner =props.getProperty("wx.mchId") ;//"1246740401";
	    String redirectUri=props.getProperty("wx.redirectUrl");
		//这个参数partnerkey是在商户后台配置的一个32位的key,微信商户平台-账户设置-安全设置-api安全
	    String partnerkey =props.getProperty("wx.paySignKey") ;// "Zaqwer91jiaoke150522122988020188";
	    String weixinRefundPath =props.getProperty("wx.certName") ;// 退款认证文件路径;
	    String feeRate =props.getProperty("fee_rate");
	    String notifyUrl =props.getProperty("wx.notifyUrl");
	    
		WeixinBaseConfig weixinBaseConfig = new WeixinBaseConfig();
		weixinBaseConfig.setAppId(appId);
		weixinBaseConfig.setAppSecret(appSecret);
		weixinBaseConfig.setCallBackUrl(callBackUrl);
		weixinBaseConfig.setPartner(partner);
		weixinBaseConfig.setPartnerkey(partnerkey);
		weixinBaseConfig.setRedirectUri(redirectUri);
		weixinBaseConfig.setWeixinRefundPath(weixinRefundPath);
		
		 
		 if(StringUtil.isEmpty(feeRate)){
			 feeRate="0";
		 }
		 weixinBaseConfig.setFee_rate(Double.parseDouble(feeRate));
		 weixinBaseConfig.setNotify_url(notifyUrl);
		
		return weixinBaseConfig;
	}
	/**
	 * 公众号的token
	 * @return
	 */
	public static String getAccessToken() {
		Map<String, String> params = null;
		Map<String, Object> resMap = null;
		WeixinBaseConfig weixinBaseConfig = getWeixinBaseConfig();
		try {
			// 获取微信access_token
			params = new HashMap<String, String>();
			params.put("grant_type", "client_credential");
			params.put("appid", weixinBaseConfig.getAppId());
			params.put("secret", weixinBaseConfig.getAppSecret());

			logger.info("getJsSDK-access_token ,grant_type = "+ params.get("grant_type") + ",appid = "+ params.get("appid") + ",secret = " + params.get("secret"));
			try {
				String wxRes = HttpClientUtil.doGet(sysAdminTokenURL, params);
				logger.info("getJsSDK-access_token ,url = https://api.weixin.qq.com/cgi-bin/token, wxRes = "+ wxRes);
				resMap = StringUtil.parseJSON2Map(wxRes);
			} catch (Exception e) {
				return null;
			}
			
		} catch (Exception e) {
			logger.error("获取access_token错误", e);
			return null;
		}
		String accessToken = (String) resMap.get("access_token");
		logger.info("getJsSDK-access_token " + accessToken);
		return accessToken;
	}
	/**
	 * 这个是用户token
	 * @param code
	 * @return
	 */
	public static Map<String, Object> getUserAccessToken(String code) {
		Map<String, String> params = null;
		Map<String, Object> resMap = null;
		WeixinBaseConfig weixinBaseConfig = getWeixinBaseConfig();
		try {
			// 获取微信access_token
			params = new HashMap<String, String>();
			params.put("grant_type", "authorization_code");
			params.put("appid", weixinBaseConfig.getAppId());
			params.put("secret", weixinBaseConfig.getAppSecret());
			params.put("code", code);
			logger.info("getJsSDK-user access_token ,grant_type = "
					+ params.get("grant_type") + ",appid = "
					+ params.get("appid") + ",secret = " + params.get("secret")+",code = "+code) ;
			String wxRes = HttpClientUtil.doGet(userAccessTokenURL, params);
			logger.info("getJsSDK-user access_token ,url = https://api.weixin.qq.com/sns/oauth2/access_token, wxRes = "
					+ wxRes);
			resMap = StringUtil.parseJSON2Map(wxRes);
			logger.info("getJsSDK-user access_token,response ==" + resMap);
			
		} catch (Exception e) {
			logger.error("获取access_token错误", e);
			return null;
		}
		String accessToken = (String) resMap.get("access_token");
		logger.info("getJsSDK-user access_token " + accessToken);
		
		return resMap;
	}

	public static String getJsTicket(String accessToken) {
		Map<String, String> params = null;
		Map<String, Object> resMap = null;
		if (accessToken == null || "".equals(accessToken)) {
			return "";
		}
		try {
			// 获取微信jsapi_ticket
			params = new HashMap<String, String>();
			params.put("access_token", accessToken);
			params.put("type", "jsapi");
			logger.info("getJsSDK-jsapi_ticket ,access_token = "
					+ params.get("access_token") + ",type = "
					+ params.get("type"));
			String wxRes = HttpClientUtil.doGet(getTicketURL,params);
			logger.info("getJsSDK-jsapi_ticket,url = "+getTicketURL+", wxRes = "
					+ wxRes);
			resMap = StringUtil.parseJSON2Map(wxRes);
			logger.info("getJsSDK-jsapi_ticket,response ==" + resMap);
		} catch (Exception e) {
			logger.error("获取jsticket错误", e);
			return null;
		}
		String ticket = (String) resMap.get("ticket");
		if(StringUtil.isEmpty(ticket)){
			int code=(Integer) resMap.get("errcode");
			if(code==40001){
				logger.info("error code=="+code);
			}
		}
		return ticket;
	}
	
	
	
	/**
	 * 创建md5摘要,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
	 */
	public static String createSign(SortedMap<String, String> packageParams) {
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
		System.out.println("-----------createSign-----sb.toString() =="+sb.toString());
		String tostring = sb.toString();
		tostring = tostring.substring(0, tostring.length() - 1);
		 //验证用户信息完整性
        String sign = CommonUtil.getSha1(tostring);
        
		System.out.println("-----------createSign-----sign =="+sign);
		return sign;
	}
	
	
	
	
	public static String getIps(String accessToken) {
		Map<String, String> params = null;
		Map<String, Object> resMap = null;
		if (accessToken == null || "".equals(accessToken)) {
			return "";
		}
		try {
			// 获取微信jsapi_ticket
			params = new HashMap<String, String>();
			params.put("access_token", accessToken);
			logger.info("getJsSDK-jsapi_ips ,access_token = "
					+ params.get("access_token"));
			String wxRes = HttpClientUtil.doGet(getBackIpURL, params);
			logger.info("getJsSDK-jsapi_ips,url = "+getBackIpURL+", wxRes = "
					+ wxRes);
			resMap = StringUtil.parseJSON2Map(wxRes);
		} catch (Exception e) {
			logger.error("获取jsapi_ips错误", e);
			return null;
		}
		Object ticket = resMap.get("ip_list");
		return ticket.toString();
	}

	public static String getAuthUrl(String redirctUrl) {
		WeixinBaseConfig weixinBaseConfig = getWeixinBaseConfig();
		String url = getAuthorURL+"?appid="
				+ weixinBaseConfig.getAppId()
				+ "&redirect_uri="
				+ redirctUrl
				+ "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
		return url;
	}

	public static String getOpenId(String code) {
		Map<String, String> params = null;
		Map<String, Object> resMap = null;
		WeixinBaseConfig weixinBaseConfig = getWeixinBaseConfig();
		try {
			// 获取微信openid
			params = new HashMap<String, String>();
			params.put("grant_type", "client_credential");
			params.put("appid", weixinBaseConfig.getAppId());
			params.put("secret", weixinBaseConfig.getAppSecret());
			params.put("code", code);
			params.put("grant_type", "authorization_code");
			logger.info("getJsSDK-openid ,grant_type = "
					+ params.get("grant_type") + ",appid = "
					+ params.get("appid") + ",secret = " + params.get("secret"));
			String wxRes = HttpClientUtil
					.doGet(getOpenTokenURL,
							params);
			logger.info("getJsSDK-openid ,url = "+getOpenTokenURL+", wxRes = "
					+ wxRes);
			resMap = StringUtil.parseJSON2Map(wxRes);
			logger.info("getJsSDK-openid,response ==" + resMap);
		} catch (Exception e) {
			logger.error("获取access_token错误", e);
			return null;
		}
		String openid = (String) resMap.get("openid");
		logger.info("getJsSDK-openid " + openid);
		return openid;
	}

	public static Map<String, Object> getWeixinUserinfo(String accessToken,
			String openId) {
		Map<String, String> params = null;
		Map<String, Object> resMap = null;
		try {
			// 获取微信openid
			params = new HashMap<String, String>();
			params.put("access_token", accessToken);
			params.put("openid", openId);
			params.put("lang", "zh_CN");
			logger.info("getJsSDK-getWeixinUserinfo ,grant_type = "
					+ params.get("grant_type") + ",appid = "
					+ params.get("appid") + ",access_token = " + params.get("access_token"));
			String wxRes = HttpClientUtil.doGet(getUserInfoURL
					, params);
			logger.info("getJsSDK-getWeixinUserinfo ,url = "+getUserInfoURL+", wxRes = "
					+ wxRes);
			resMap = StringUtil.parseJSON2Map(wxRes);
			logger.info("getJsSDK-getWeixinUserinfo,response==" + resMap);
		} catch (Exception e) {
			logger.error("获取access_token错误", e);
			return null;
		}
		return resMap;
	}
	/**
	 * 获取帐号的关注者列表
	 * @param accessToken
	 * @param next_openid
	 * @return
	 */
	public static JSONObject getFollwersList(String accessToken, String next_openid) throws Exception{
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", accessToken);
		params.put("next_openid", next_openid);
		String  jsonStr = HttpKit.get(getUSERURL, params);
		if(StringUtils.isNotEmpty(jsonStr)){
			JSONObject obj = JSONObject.parseObject(jsonStr);
			if(obj.get("errcode") != null){
				throw new Exception(obj.getString("errmsg"));
			}
			return obj;
		}
		return null;
	}
	
	/**
	 * 获取随机字符串
	 * @return
	 */
	/*public static String getNonceStr() {
		// 随机数
		String currTime = TenpayUtil.getCurrTime();
		// 8位日期
		String strTime = currTime.substring(8, currTime.length());
		// 四位随机数
		String strRandom = TenpayUtil.buildRandom(4) + "";
		// 10位序列号,可以自行调整。
		return strTime + strRandom;
	}*/
	
	
	/*public static SortedMap<String, String> getPackage(WxPayVo wxPayVo) {
		WeixinBaseConfig weixinBaseConfig=getWeixinBaseConfig();
		String openId = wxPayVo.getOpenId();
		// 1 参数
		// 订单号
		String orderId = wxPayVo.getOrderId();
		// 附加数据 原样返回
		String attach = "";
		// 总金额以分为单位，不带小数点
		String totalFee = getMoney(wxPayVo.getTotalFee());
		// 订单生成的机器 IP
		String spbill_create_ip = wxPayVo.getSpbillCreateIp();
		// 这里notify_url是 支付完成后微信发给该链接信息，可以判断会员是否支付成功，改变订单状态等。
		String notify_url = wxPayVo.getNotifyUrl();
		String trade_type = "JSAPI";
		// ---必须参数
		// 商户号
		String mch_id = weixinBaseConfig.getPartner();
		// 随机字符串
		String nonce_str = getNonceStr();

		// 商品描述根据情况修改
		String body = wxPayVo.getBody();

		// 商户订单号
		String out_trade_no = orderId;

		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", weixinBaseConfig.getAppId());
		packageParams.put("mch_id", mch_id);
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("body", body);
		packageParams.put("attach", attach);
		packageParams.put("out_trade_no", out_trade_no);

		// 这里写的金额为1 分到时修改
		packageParams.put("total_fee", totalFee);
		packageParams.put("spbill_create_ip", spbill_create_ip);
		packageParams.put("notify_url", notify_url);

		packageParams.put("trade_type", trade_type);
		packageParams.put("openid", openId);
		
		RequestHandler reqHandler = new RequestHandler(null, null);
		reqHandler.init(weixinBaseConfig.getAppId(), weixinBaseConfig.getAppSecret(), weixinBaseConfig.getPartnerkey());
		
		String sign = reqHandler.createSign(packageParams);
		
		String xml = "<xml>" + "<appid>" + weixinBaseConfig.getAppId() + "</appid>" + "<mch_id>"
				+ mch_id + "</mch_id>" + "<nonce_str>" + nonce_str
				+ "</nonce_str>" + "<sign>" + sign + "</sign>"
				+ "<body><![CDATA[" + body + "]]></body>" 
				+ "<out_trade_no>" + out_trade_no
				+ "</out_trade_no>" + "<attach>" + attach + "</attach>"
				+ "<total_fee>" + totalFee + "</total_fee>"
				+ "<spbill_create_ip>" + spbill_create_ip
				+ "</spbill_create_ip>" + "<notify_url>" + notify_url
				+ "</notify_url>" + "<trade_type>" + trade_type
				+ "</trade_type>" + "<openid>" + openId + "</openid>"
				+ "</xml>";
		
		// 预付商品id
		String prepay_id = "";
		try {
			logger.info("xml =="+xml);
			String xmlStr = HttpKit.post(createOrderURL, xml);
			logger.info("xml response =="+xmlStr);
			if (xmlStr.indexOf("SUCCESS") != -1) {
				Map<String, String> map = TenpayUtil.doXMLParse(xmlStr);
				prepay_id = (String) map.get("prepay_id");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		if(StringUtil.isEmpty(prepay_id)){
			return null;
		}
		System.out.println("获取到的预支付ID：" + prepay_id);
		//获取prepay_id后，拼接最后请求支付所需要的package
		SortedMap<String, String> finalpackage = new TreeMap<String, String>();
		String timestamp = Sha1Util.getTimeStamp();
		String packages = "prepay_id="+prepay_id;
		finalpackage.put("appId", weixinBaseConfig.getAppId());  
		finalpackage.put("timeStamp", timestamp);  
		finalpackage.put("nonceStr", nonce_str);  
		finalpackage.put("package", packages);  
		finalpackage.put("signType", "MD5");
		//要签名
		String finalsign = reqHandler.createSign(finalpackage);
		finalpackage.put("paySign", finalsign);
		String finaPackageStr = "\"appId\":\"" + weixinBaseConfig.getAppId() + "\",\"timeStamp\":\"" + timestamp
		+ "\",\"nonceStr\":\"" + nonce_str + "\",\"package\":\""
		+ packages + "\",\"signType\" : \"MD5" + "\",\"paySign\":\""
		+ finalsign + "\"";
		System.out.println("V3 jsApi package:"+finaPackageStr);
		return finalpackage;
	}*/ 
//	
	/*public static SortedMap<String, String> getPackage(WxPayVo wxPayVo) {
		WeixinBaseConfig weixinBaseConfig=getWeixinBaseConfig();
		String openId = wxPayVo.getOpenId();
		// 1 参数
		// 订单号
		String orderId = wxPayVo.getOrderId();
		// 附加数据 原样返回
		String attach = "";
		// 总金额以分为单位，不带小数点
		String totalFee = getMoney(wxPayVo.getTotalFee());
		// 订单生成的机器 IP
		String spbill_create_ip = wxPayVo.getSpbillCreateIp();
		// 这里notify_url是 支付完成后微信发给该链接信息，可以判断会员是否支付成功，改变订单状态等。
		String notify_url = wxPayVo.getNotifyUrl();
		String trade_type = "JSAPI";
		// ---必须参数
		// 商户号
		String mch_id = weixinBaseConfig.getPartner();
		
		// 商户key
		String partnerkey = weixinBaseConfig.getPartnerkey();
		// 随机字符串
//		String nonce_str = getNonceStr();

		// 商品描述根据情况修改
		String body = wxPayVo.getBody();

		// 商户订单号
		String out_trade_no = orderId;

		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", weixinBaseConfig.getAppId());
		packageParams.put("mch_id", mch_id);
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("body", body);
		packageParams.put("attach", attach);
		packageParams.put("out_trade_no", out_trade_no);

		// 这里写的金额为1 分到时修改
		
*//*******************************************************这里写的金额为1 分到时修改*********************************************************************************//*
		logger.info("=========================================测试环境 默认只扣1分钱*=======ResourceUtil.getConfigByName(wx.openPayStatus)==========================="+ResourceUtil.getConfigByName("wx.openPayStatus"));
		 String flag = ResourceUtil.getConfigByName("wx.openPayStatus");
         if("1".equals(flag)){
        	 totalFee = "1";
         }
*//*******************************************************这里写的金额为1 分到时修改*********************************************************************************//*
		
		packageParams.put("total_fee", totalFee);
		packageParams.put("spbill_create_ip", spbill_create_ip);
		packageParams.put("notify_url", notify_url);

		packageParams.put("trade_type", trade_type);
		packageParams.put("openid", openId);
		
		RequestHandler reqHandler = new RequestHandler(null, null);
		reqHandler.init(weixinBaseConfig.getAppId(), weixinBaseConfig.getAppSecret(), weixinBaseConfig.getPartnerkey());
		logger.info("-----------createSign-----packageParams =="+packageParams);
		String sign = reqHandler.createSign(packageParams);
		logger.info("-----------createSign---result--packageParams 1=="+sign);
//		String sign2 = "";
//		try {
//			sign2 = WXPayUtil.generateSignature(packageParams,partnerkey);
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		
//		logger.info("-----------createSign---result--sign 2=="+sign2);
		String xml = "<xml>" 
				+ "<appid>" + weixinBaseConfig.getAppId() + "</appid>"
				+ "<mch_id>"+ mch_id + "</mch_id>"
				+ "<nonce_str>" + nonce_str	+ "</nonce_str>" 
				+ "<sign>" + sign + "</sign>"
				+ "<body><![CDATA[" + body + "]]></body>" 
				+ "<out_trade_no>" + out_trade_no+ "</out_trade_no>"
				+ "<attach>" + attach + "</attach>"
				+ "<total_fee>" + totalFee + "</total_fee>"
				+ "<spbill_create_ip>" + spbill_create_ip+ "</spbill_create_ip>"
				+ "<notify_url>" + notify_url+ "</notify_url>" 
				+ "<trade_type>" + trade_type+ "</trade_type>" 
				+ "<openid>" + openId + "</openid>"
				+ "</xml>";
		
		// 预付商品id
		String prepay_id = "";
		String error_code = "";
		String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
					//URL地址：https://api.mch.weixin.qq.com/pay/unifiedorder
		try {
			logger.info("---HttpKit.post-----------request-xml =="+xml);
			String xmlStr = HttpKit.post(createOrderURL, xml);
			logger.info("--HttpKit.post------- response xmlStr=="+xmlStr);
			if (xmlStr.indexOf("SUCCESS") != -1) {
				Map<String, String> map = TenpayUtil.doXMLParse(xmlStr);
				prepay_id = (String) map.get("prepay_id");
				error_code=(String)map.get("err_code");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(StringUtil.isEmpty(prepay_id)){
			if(StringUtil.isEmpty(error_code)){
				logger.info("error_code="+error_code);
				SortedMap<String, String> finalpackage = new TreeMap<String, String>();
				finalpackage.put("error_code", error_code);  
				return finalpackage;
			}
			return null;
		}
		System.out.println("获取到的预支付ID：" + prepay_id);
		//获取prepay_id后，拼接最后请求支付所需要的package
		SortedMap<String, String> finalpackage = new TreeMap<String, String>();
		String timestamp = Sha1Util.getTimeStamp();
		String packages = "prepay_id="+prepay_id;
		finalpackage.put("appId", weixinBaseConfig.getAppId());  
		finalpackage.put("timeStamp", timestamp);  
		finalpackage.put("nonceStr", nonce_str);  
		finalpackage.put("package", packages);  
		finalpackage.put("signType", "MD5");
		//要签名
		String finalsign = reqHandler.createSign(finalpackage);
		finalpackage.put("paySign", finalsign);
		String finaPackageStr = "\"appId\":\"" + weixinBaseConfig.getAppId() + "\",\"timeStamp\":\"" + timestamp
		+ "\",\"nonceStr\":\"" + nonce_str + "\",\"package\":\""
		+ packages + "\",\"signType\" : \"MD5" + "\",\"paySign\":\""
		+ finalsign + "\"";
		System.out.println("V3 jsApi package:"+finaPackageStr);
		return finalpackage;
	}*/
	
	/**
	 * 获取微信扫码支付二维码连接
	 */
	/*public static String getCodeurl(WxPayVo wxPayVo){
		WeixinBaseConfig weixinBaseConfig=getWeixinBaseConfig();
		// 1 参数
		// 订单号
		String orderId = wxPayVo.getOrderId();
		// 附加数据 原样返回
		String attach = "";
		// 总金额以分为单位，不带小数点
		String totalFee = getMoney(wxPayVo.getTotalFee());
		
		// 订单生成的机器 IP
		String spbill_create_ip = wxPayVo.getSpbillCreateIp();
		// 这里notify_url是 支付完成后微信发给该链接信息，可以判断会员是否支付成功，改变订单状态等。
		String notify_url = wxPayVo.getNotifyUrl();
		String trade_type = "NATIVE";
		// 商户号
		String mch_id = weixinBaseConfig.getPartner();
		// 随机字符串
		String nonce_str = getNonceStr();
	
		// 商品描述根据情况修改
		String body = wxPayVo.getBody();
	
		// 商户订单号
		String out_trade_no = orderId;
		

		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", weixinBaseConfig.getAppId());
		packageParams.put("mch_id", mch_id);
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("body", body);
		packageParams.put("attach", attach);
		packageParams.put("out_trade_no", out_trade_no);

		// 这里写的金额为1 分到时修改
		packageParams.put("total_fee", totalFee);
		packageParams.put("spbill_create_ip", spbill_create_ip);
		packageParams.put("notify_url", notify_url);

		packageParams.put("trade_type", trade_type);

		RequestHandler reqHandler = new RequestHandler(null, null);
		
		reqHandler.init(weixinBaseConfig.getAppId(), weixinBaseConfig.getAppSecret(), weixinBaseConfig.getPartnerkey());
		
		String sign = reqHandler.createSign(packageParams);
		
		String xml = "<xml>" + "<appid>" + weixinBaseConfig.getAppId() + "</appid>" + "<mch_id>"
				+ mch_id + "</mch_id>" + "<nonce_str>" + nonce_str
				+ "</nonce_str>" + "<sign>" + sign + "</sign>"
				+ "<body><![CDATA[" + body + "]]></body>" 
				+ "<out_trade_no>" + out_trade_no
				+ "</out_trade_no>" + "<attach>" + attach + "</attach>"
				+ "<total_fee>" + totalFee + "</total_fee>"
				+ "<spbill_create_ip>" + spbill_create_ip
				+ "</spbill_create_ip>" + "<notify_url>" + notify_url
				+ "</notify_url>" + "<trade_type>" + trade_type
				+ "</trade_type>" + "</xml>";
		String code_url = "";
		try {
			logger.info("xml =="+xml);
			String xmlStr = HttpKit.post(createOrderURL, xml);
			logger.info("xml response =="+xmlStr);
			if (xmlStr.indexOf("SUCCESS") != -1) {
				Map<String, String> map = TenpayUtil.doXMLParse(xmlStr);
				code_url  = (String) map.get("code_url");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("code_url----------------"+code_url);
		return code_url;
	}*/
	/**企业付
	 *  商户付款给个人
	 *  ◆ 给同一个实名用户付款，单笔单日限额2W/2W
		◆ 不支持给非实名用户打款
		◆ 一个商户同一日付款总额限额100W
		◆ 单笔最小金额默认为1元
		◆ 每个用户每天最多可付款10次，可以在商户平台--API安全进行设置
		◆ 给同一个用户付款时间间隔不得低于15秒
	 * @param tradeNo
	 * @param openId
	 * @param amount 分
	 * @param desc 描述
	 * @return
	 */
	/*public static  Map<String, String> payMemberMoney(String tradeNo,
			String openId, int amount,String desc) {
		WeixinBaseConfig weixinBaseConfig=getWeixinBaseConfig();
		SortedMap<String, String> params = new TreeMap<String, String>();
		params.put("mch_appid", weixinBaseConfig.getAppId());//商户账号appid	
		params.put("mchid", weixinBaseConfig.getPartner());//商户号	
		params.put("nonce_str", getNonceStr());//随机字符串	
		params.put("partner_trade_no", tradeNo);//商户订单号，需保持唯一性(只能是字母或者数字，不能包含有符号)
		params.put("openid", openId);//商户appid下，某用户的openid
		params.put("check_name", "NO_CHECK");//NO_CHECK：不校验真实姓名 FORCE_CHECK：强校验真实姓名
		params.put("amount", ""+amount);//企业付款金额，单位为分
		params.put("desc", desc);//企业付款操作说明信息。必填
		String	 ip = "127.0.0.1";
		try {
			ip = java.net.InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			logger.info("[request]getLocalHost is error",e);
		}
		
		params.put("spbill_create_ip", ip);
		
		RequestHandler reqHandler = new RequestHandler(null, null);
		reqHandler.init(weixinBaseConfig.getAppId(), weixinBaseConfig.getAppSecret(), weixinBaseConfig.getPartnerkey());
		String sign = reqHandler.createSign(params);
		params.put("sign", sign);//签名
		
		String reqXML = TenpayUtil.toXml(params);
		logger.info("[refund]reqXML:" + reqXML);
		
		try {
			 String keyStorePath=Thread.currentThread().getContextClassLoader().getResource("").getPath()+"/weixin/apiclient_cert.p12";
			 System.out.println("keyStorePath=="+keyStorePath);
			 CloseableHttpClient httpClient = CloseableHttpHelper.createForSSL(keyStorePath , weixinBaseConfig.getPartner());
			 CloseableHttpResult httpResult = CloseableHttpHelper.postByStrEntity(httpClient, ApiWeixinBase.getRefundURL, reqXML,null);
				
			//String xmlStr = HttpKit.post(WeixinBaseApi.getRefundURL, reqXML);
			
			if(httpResult == null){
				logger.info("[request]httpResult is null");
				return null;
			}
			InputStream stream = httpResult.getInputStream();
			BufferedReader read = new BufferedReader(new InputStreamReader(stream,"UTF-8"));
			String valueString = null;
			StringBuffer bufferRes = new StringBuffer();
			while ((valueString = read.readLine()) != null) {
				bufferRes.append(valueString);
			}
			stream.close();
			if (httpClient != null) {
				httpClient.close();// 关闭连接
			}
			String xmlStr=bufferRes.toString();
			logger.info("xml response =="+xmlStr);
			if (xmlStr.indexOf("SUCCESS") != -1) {
				Map<String, String> resultMap = TenpayUtil.doXMLParse(xmlStr);
				return resultMap;
			}else{
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}*/
	
	/**
	 * 获取随机字符串
	 * @return
	 */
	/*public static String getNonceStr() {
		// 随机数
		String currTime = TenpayUtil.getCurrTime();
		// 8位日期
		String strTime = currTime.substring(8, currTime.length());
		// 四位随机数
		String strRandom = TenpayUtil.buildRandom(4) + "";
		// 10位序列号,可以自行调整。
		return strTime + strRandom;
	}*/
	/**
	 * 元转换成分
	 * @param money
	 * @return
	 */
	public static String getMoney(String amount) {
		if(amount==null){
			return "";
		}
		// 金额转化为分为单位
		String currency =  amount.replaceAll("\\$|\\￥|\\,", "");  //处理包含, ￥ 或者$的金额  
        int index = currency.indexOf(".");  
        int length = currency.length();  
        Long amLong = 0l;  
        if(index == -1){  
            amLong = Long.valueOf(currency+"00");  
        }else if(length - index >= 3){  
            amLong = Long.valueOf((currency.substring(0, index+3)).replace(".", ""));  
        }else if(length - index == 2){  
            amLong = Long.valueOf((currency.substring(0, index+2)).replace(".", "")+0);  
        }else{  
            amLong = Long.valueOf((currency.substring(0, index+1)).replace(".", "")+"00");  
        }  
        return amLong.toString(); 
	}
	public static boolean isWeiXin(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        if (StringUtils.isNotBlank(userAgent)) {
            Pattern p = Pattern.compile("MicroMessenger/(\\d+).+");
            Matcher m = p.matcher(userAgent);
            String version = null;
            if (m.find()) {
                version = m.group(1);
            }
            return (null != version && NumberUtils.toInt(version) >= 5);
        }
        return false;
    }
}