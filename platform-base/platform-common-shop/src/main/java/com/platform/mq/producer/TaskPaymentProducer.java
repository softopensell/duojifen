package com.platform.mq.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.platform.mq.model.PaymentTask;
import com.platform.mq.queue.PaymentQueue;

public class TaskPaymentProducer {
    private static Logger logger = LoggerFactory.getLogger(TaskPaymentProducer.class);
    /**
     * 把支付流水消息放入待发送队列中
     * @param bonusTaskVo
     * @param doLog
     */
    public static void addPaymentTaskVo(PaymentTask paymentTask) {
    	logger.debug("开始加入支付流水到队列:" + paymentTask);
    	PaymentQueue.getInstance().put(paymentTask);
    }
}
