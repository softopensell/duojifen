package com.platform.thirdparty.sms;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service("smsService")
public class SmsServiceImpl extends SmsService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	private static Properties props = new Properties(); 
	static{
		try {
			props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("zucp_sms_config.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @param sign String sign = "【" + CommonDef.WEBSITE_NAME_CN + "】";
	 * @param content
	 * @param phoneNum
	 * @return
	 */
	public int send(String sign,String content,String phoneNum) {
		String sn = props.getProperty("sn");
		String pwd = props.getProperty("password");
		if (sn == null || pwd == null) {
			throw new SmsSendErrorException("短信发送失败，sn或password未定义");
		}
		String result_mt = "";
		sign="【" + sign + "】";
		try {
			logger.info("手机号码【{}】发送内容【{}】发送方式立刻、扩展字段默认null、唯一标识默认null", phoneNum, content);
			ZucpWebServiceClient client = new ZucpWebServiceClient(sn, pwd);
			String msg = URLEncoder.encode(content+sign, "utf-8");
			result_mt = client.mdSmsSend_u(phoneNum, msg, "","", "");
		} catch (UnsupportedEncodingException e) {
			logger.error("手机号码【"+phoneNum+"】发送内容【"+content+"】失败, 短信内容给不支持UTF-8编码", e);
		}
		//发送短信，如果是以负号开头就是发送失败。
		if (result_mt.startsWith("-") || result_mt.equals("")){
			logger.info("手机号码【{}】发送内容【{}】失败.错误码【{}】", phoneNum, content, result_mt);
			return -1;
		}
		logger.info("手机号码【{}】发送内容【{}】发送成功.返回数据【{}】", phoneNum, content, result_mt);
		return 0;
	}
}
