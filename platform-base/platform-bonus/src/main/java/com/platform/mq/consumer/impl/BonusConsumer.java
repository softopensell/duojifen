package com.platform.mq.consumer.impl;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.platform.facade.DjfBonusFacade;
import com.platform.mq.consumer.AbstractTaskConsumer;

/**
 * 线程池，其中的线程从奖励队列中取待发送的BonusTaskVo对象
 *
 * @version 1.0
 */
public class BonusConsumer extends AbstractTaskConsumer {
    private ScheduledExecutorService executorService;
    private static Logger logger = LoggerFactory.getLogger(BonusConsumer.class);

    @Resource
    private DjfBonusFacade djfBonusFacade;
    
   
    /**
     * 启动Bonus Consumer的线程池
     */
    @PostConstruct
    public void startConsume() {
        logger.info("开始启动奖励写入线程池"+"interval"+interval);
        if (this.executorService == null || executorService.isShutdown()
                || executorService.isTerminated()) {
            executorService = Executors.newScheduledThreadPool(this.consumer_thread_number);
            for (int i = 1; i < (consumer_thread_number + 1); i++) {
                // 此处initialDelay是i，单位秒
                executorService.scheduleAtFixedRate(new BonusConsumerThread(djfBonusFacade,i), 0, interval,
                        TimeUnit.SECONDS);
            }
        }
        logger.info("奖励发送线程池初始化成功, 共含" + consumer_thread_number + "线程, 每" + interval + "秒钟轮询队列一次");
    }

    /**
     * @return the executorService
     */
    public ScheduledExecutorService getExecutorService() {
        return executorService;
    }
}
