package com.platform.thirdparty.sms;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmsSender {
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	private static Client client;
	private static Properties props = new Properties();
	static {
		try {
			props.load(Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("sms_config.properties"));
			String sn = props.getProperty("sms.sn");
			String pwd = props.getProperty("sms.password");
			String postUrl = props.getProperty("sms.url");
			if (sn == null || pwd == null) {
				throw new SmsSendErrorException("短信发送失败，sn或password未定义");
			}
			client = new Client(sn, pwd, postUrl);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int send(String sign, String phoneNum, String msg) {
		String result_mt = "";
		sign = '[' + sign + ']';
		try {
			logger.info("手机号码【{}】发送内容【{}】发送方式立刻、扩展字段默认null、唯一标识默认null",
					phoneNum, msg);
			result_mt = client.mdsmssend(phoneNum,
					URLEncoder.encode(msg + sign, "utf8"), "", "", "", "");
		} catch (UnsupportedEncodingException e) {
			logger.error("手机号码【" + phoneNum + "】发送内容【" + msg
					+ "】失败, 短信内容给不支持UTF-8编码", e);
		}
		// 发送短信，如果是以负号开头就是发送失败。
		if (result_mt.startsWith("-") || result_mt.equals("")) {
			logger.info("手机号码【{}】发送内容【{}】失败.错误码【{}】", phoneNum, msg, result_mt);
			return -1;
		}
		logger.info("手机号码【{}】发送内容【{}】发送成功.返回数据【{}】", phoneNum, msg, result_mt);
		return 0;
	}

}