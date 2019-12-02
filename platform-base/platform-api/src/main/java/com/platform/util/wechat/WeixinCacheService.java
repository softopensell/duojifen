package com.platform.util.wechat;

import java.sql.Timestamp;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;


@Service
public class WeixinCacheService {
	 private Logger LOG = Logger.getLogger(getClass());
//	protected final static Log LOG = LogFactory.getLog(WeixinCacheService.class);
	@Resource
	private WeixinTokenCacheService weixinTokenCache;
	@Resource
	private WeixinJsApiTicketCacheService weixinJsApiTicketCache;
	
//	@PostConstruct
	public void init() {
		LOG.info("-------------------Init system cache start----------------------");
		try {
			Thread thread = new Thread(){
				   public void run(){
					   if(GlobalConstant.getIS_OPEN_WEIXIN().equals(GlobalConstant.IS_OPEN_WEIXIN_OPEN)){
						  initOrUpdateWeixinTokenCache();
						  initOrUpdateWeixinJsApiTicketCache();   
					   }
				   }
				};
		    thread.start();
		} catch (Exception e) {
	}
	LOG.info("Init system cache end");
		
	}
	private void initOrUpdateWeixinTokenCache() {
		weixinTokenCache.initOrUpdate();
	}
	public WeixinTokenCacheService getWeixinTokenCache(){
		return weixinTokenCache;
	}
	private void initOrUpdateWeixinJsApiTicketCache() {
		weixinJsApiTicketCache.initOrUpdate(weixinTokenCache.get_weixinToken());
	}
	public WeixinJsApiTicketCacheService getWeixinJsApiTicketCache(){
         if(DateUtils.getTimeSpan(weixinJsApiTicketCache.get_createTime(), new Timestamp(System.currentTimeMillis()), DateUtils.SECOND)>7100){
			weixinJsApiTicketCache.initOrUpdate(weixinTokenCache.get_weixinToken());
		}
		return weixinJsApiTicketCache;
	}
	
	
	
}
