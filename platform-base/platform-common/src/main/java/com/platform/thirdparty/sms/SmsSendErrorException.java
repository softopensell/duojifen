package com.platform.thirdparty.sms;

/**   
* @Company: 91教课网 <br/>
* @Copyright: Copyright (c) 2015年3月20日<br/>
* @Title: SmsSendErrorException.java 
* @Package com.taotaoti.plugin.sms 
* @Description: 短信发送出错 
* @author  softopensell
* @email softopensell@91jiaoke.com  
* @date 2015年3月20日 下午3:21:09 
* @version V1.0   
*/
public class SmsSendErrorException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SmsSendErrorException(String msg, Throwable e) {
		super(msg, e);
	}

	public SmsSendErrorException(String msg) {
		super(msg);
	}
}
