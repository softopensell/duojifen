package com.platform.util.wechat;

import java.sql.Timestamp;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;


/**
 * SystemConfigCache
 *
 * @version 2013-7-22 上午12:37:13
 */
@Service
public class WeixinJsApiTicketCacheService{
    protected Logger logger = Logger.getLogger(getClass());

	public String _jsApiTicket;
	public Integer _expireSeconds;
	public Timestamp _createTime;
	
	void initOrUpdate(String accessToken) {
		synchronized (this) {
			  _jsApiTicket=ApiWeixinBase.getJsTicket(accessToken);
			  _expireSeconds=7200;//60*12*10 两小时
			  _createTime=new Timestamp(System.currentTimeMillis());
		}
	}

	/**
	 * @return the _weixinToken
	 */
	public String get_jsApiTicket() {
		return _jsApiTicket;
	}
	/**
	 * @return the _expireSeconds
	 */
	public Integer get_expireSeconds() {
		return _expireSeconds;
	}


	/**
	 * @return the _createTime
	 */
	public Timestamp get_createTime() {
		return _createTime;
	}
}
