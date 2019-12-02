package com.platform.quartz.cron.job;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.platform.facade.DjfBonusFacade;
import com.platform.facade.PlatformStatFacade;
import com.platform.utils.DateUtils;


@Component("quartzDealBonusEveryDayTask")
public class QuartzDealBonusEveryDayTask {
	private static final Log log = LogFactory.getLog(QuartzDealBonusEveryDayTask.class);
	@Resource
	private DjfBonusFacade djfBonusFacade;
	@Resource
	private PlatformStatFacade platformStatFacade;
	
    public void tofinishBounsEveryShare() {
    	log.info(DateUtils.format(new Date(System.currentTimeMillis()),"yyyy-MM-dd HH:mm:ss")+"close tofinishBounsEveryShare  start");
    	djfBonusFacade.bonusEveryShare();//每日分红
        log.info(DateUtils.format(new Date(System.currentTimeMillis()),"yyyy-MM-dd HH:mm:ss")+"close tofinishBounsEveryShare  end");
    }
    public void toDealEveryStat() {
    	log.info(DateUtils.format(new Date(System.currentTimeMillis()),"yyyy-MM-dd HH:mm:ss")+"close toDealEveryStat  start");
    	platformStatFacade.everyDayDealStat();//系统处理统计数据
    	log.info(DateUtils.format(new Date(System.currentTimeMillis()),"yyyy-MM-dd HH:mm:ss")+"close toDealEveryStat  end");
    }
    
}