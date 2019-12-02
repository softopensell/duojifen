package com.platform.mq.queue;

 

/**
 * 奖励队列
 */
public class PaymentQueue extends AbstractTaskQueue {

    private static final long serialVersionUID = -972802808152063585L;
    private static PaymentQueue paymentQueue;
    public static PaymentQueue getInstance() {
        if (paymentQueue == null) {
        	paymentQueue = new PaymentQueue();
        }
        return paymentQueue;
    }
}
