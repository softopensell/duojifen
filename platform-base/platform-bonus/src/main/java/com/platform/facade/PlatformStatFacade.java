package com.platform.facade;

import java.util.Date;

public interface PlatformStatFacade {
	
     /** 
	 * 每日处理统计数据
	 */
	public void  everyDayDealStat();
	/**
	 * 按时间处理统计数据
	 * @param date
	 */
	public void  platformDealStat(String  poolDateNumber,Date date);

	  
}
