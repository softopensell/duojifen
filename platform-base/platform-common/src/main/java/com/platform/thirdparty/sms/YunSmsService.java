package com.platform.thirdparty.sms;


public abstract class YunSmsService {
	/**
	 * 发送短信
	 * @param content
	 * @param mobileNumber
	 * @throws SmsSendErrorException
	 */
	public abstract int send(String sign,String content, String mobileNumber) throws SmsSendErrorException;
}
