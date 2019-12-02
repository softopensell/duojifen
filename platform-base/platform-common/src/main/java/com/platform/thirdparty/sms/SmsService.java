package com.platform.thirdparty.sms;
public abstract class SmsService {
	/**
	 * 发送短信
	 * @param sign 【 sign】
	 * @param content
	 * @param mobileNumber
	 * @throws SmsSendErrorException
	 */
	public abstract int send(String sign,String content, String mobileNumber) throws SmsSendErrorException;
}
