package com.platform.mq.consumer.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.platform.facade.PaymentPluginFacade;
import com.platform.mq.model.PaymentTask;
import com.platform.mq.queue.PaymentQueue;
import com.platform.utils.JsonUtil;

/**
 * 支付流水的consumer线程，从队列中取待发短信，然后调用adapter发送
 */
public class PaymentConsumerThread extends Thread {
    private static Logger logger = LoggerFactory.getLogger(PaymentConsumerThread.class);
    private int thread_id;
    private PaymentPluginFacade paymentPluginFacade;
    /**
     * 构造函数
     * @param thread_id
     */
    public PaymentConsumerThread(PaymentPluginFacade paymentPluginFacade ,int thread_id) {
    	this.thread_id = thread_id;
		logger.info("支付流水 处理线程 " + thread_id + " 创建成功");
		this.paymentPluginFacade=paymentPluginFacade;
    }
    @Override
    public void run() {
        PaymentTask paymentTask =(PaymentTask)PaymentQueue.getInstance().take();
        if (paymentTask == null) {
        	logger.info("支付流水线程【{}】从队列取出奖励为null", thread_id);
            return;
        }
        logger.info("支付流水【{}】从队列取出奖励【{}】", thread_id,  JsonUtil.getJsonByObj(paymentTask));
        paymentPluginFacade.addPaymentTask(paymentTask);
    }
}
