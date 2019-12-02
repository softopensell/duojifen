package com.platform.mq.consumer.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.platform.facade.DjfBonusFacade;
import com.platform.mq.model.BonusTaskVo;
import com.platform.mq.queue.BonusQueue;
import com.platform.utils.JsonUtil;

/**
 * 日志的consumer线程，从队列中取待发短信，然后调用adapter发送
 */
public class BonusConsumerThread extends Thread {
	
    private static Logger logger = LoggerFactory.getLogger(BonusConsumerThread.class);
    private int thread_id;
    private DjfBonusFacade djfBonusFacade;
    /**
     * 构造函数
     * @param thread_id
     */
    public BonusConsumerThread(DjfBonusFacade djfBonusFacade ,int thread_id) {
    	this.thread_id = thread_id;
		logger.info("奖励 处理线程 " + thread_id + " 创建成功");
		this.djfBonusFacade=djfBonusFacade;
    }
    @Override
    public void run() {
    	logger.info("奖励线程【{}】从队列取出奖励为进来了....run...", thread_id);
        BonusTaskVo bonusTaskVo =(BonusTaskVo) BonusQueue.getInstance().take();
        if (bonusTaskVo == null) {
        	logger.info("奖励线程【{}】从队列取出奖励为null", thread_id);
            return;
        }
        logger.info("奖励线程【{}】从队列取出奖励【{}】", thread_id, JsonUtil.getJsonByObj(bonusTaskVo));
        djfBonusFacade.bonusConsumedMallOrderShare(bonusTaskVo);
    }
}
