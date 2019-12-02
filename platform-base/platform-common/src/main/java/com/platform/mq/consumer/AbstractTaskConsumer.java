package com.platform.mq.consumer;


/**
 * @version 1.0
 */
public abstract class AbstractTaskConsumer {

    protected int consumer_thread_number=3;

    // 每个线程轮询队列的时间间隔
    protected int interval=10;

    /**
     * 从队列中拿出一个Task，调用Adapter发送
     *
     * @param message
     */
    public abstract void startConsume();


    /**
     * Spring注入线程数
     *
     * @param consumer_thread_number the consumer_thread_number to set
     */
    public void setConsumer_thread_number(int consumer_thread_number) {
        this.consumer_thread_number = consumer_thread_number;
    }
    /**
     * Spring注入轮询queue间隔
     *
     * @param interval the interval to set
     */
    public void setInterval(int interval) {
        this.interval = interval;
    }
}
