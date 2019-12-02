package com.platform.mq.consumer.impl;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.platform.facade.PaymentPluginFacade;
import com.platform.mq.consumer.AbstractTaskConsumer;

/**
 * 线程池，其中的线程从支付流水队列中取待发送的对象
 *
 * @version 1.0
 */
public class PaymentConsumer extends AbstractTaskConsumer {
    private ScheduledExecutorService executorService;
    private static Logger logger = LoggerFactory.getLogger(PaymentConsumer.class);
    @Resource
    private PaymentPluginFacade paymentPluginFacade;
    /**
     * 启动支付流水Consumer的线程池
     * @param paymentTask
     */
    @PostConstruct
    public void startConsume() {
        logger.info("开始启动支付流水写入线程池");
        if (this.executorService == null || executorService.isShutdown()
                || executorService.isTerminated()) {
            executorService = Executors.newScheduledThreadPool(this.consumer_thread_number);
            for (int i = 1; i < (consumer_thread_number + 1); i++) {
                // 此处initialDelay是i，单位秒
                executorService.scheduleAtFixedRate(new PaymentConsumerThread(paymentPluginFacade,i), 0, interval,
                        TimeUnit.SECONDS);
            }
        }
        logger.info("支付流水线程池初始化成功, 共含" + consumer_thread_number + "线程, 每" + interval + "秒钟轮询队列一次");
    }
    /**
     * @return the executorService
     */
    public ScheduledExecutorService getExecutorService() {
        return executorService;
    }

}
