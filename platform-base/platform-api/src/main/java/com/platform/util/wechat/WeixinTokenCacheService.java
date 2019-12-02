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
public class WeixinTokenCacheService{
    protected Logger logger = Logger.getLogger(getClass());


	public String _weixinToken;
	public Integer _expireSeconds;
	public Timestamp _createTime=new Timestamp(2016, 6, 6, 0, 0, 0, 0);
	void initOrUpdate() {
		synchronized (this) {
			logger.info("---- WeixinTokenCacheService--initOrUpdate-------:" +_createTime);
			  _weixinToken=ApiWeixinBase.getAccessToken();
			  _expireSeconds=7200;//60*12*10 两小时
			  _createTime=new Timestamp(System.currentTimeMillis());
		}
	}

	/**
	 * @return the _weixinToken
	 */
	public String get_weixinToken() {
        logger.info("---- WeixinTokenCacheService--get_weixinToken----  进来了--_createTime---:" +_createTime);

		if(_createTime==null||DateUtils.getTimeSpan(_createTime, new Timestamp(System.currentTimeMillis()), DateUtils.SECOND)>7100){
			 logger.info("---- WeixinTokenCacheService--get_weixinToken----  执行了---_createTime--:" +_createTime);
			initOrUpdate();
		}
		logger.info("---- WeixinTokenCacheService--get_weixinToken----  出去了---_weixinToken--:" +_weixinToken);
		return _weixinToken;
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
