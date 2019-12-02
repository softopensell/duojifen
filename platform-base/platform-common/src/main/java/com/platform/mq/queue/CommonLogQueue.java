package com.platform.mq.queue;


/**
 * 日志队列
 */
public class CommonLogQueue extends AbstractTaskQueue {

    private static final long serialVersionUID = -972802808152063585L;
    private static CommonLogQueue commonLogQueue;

    public static CommonLogQueue getInstance() {
        if (commonLogQueue == null) {
        	commonLogQueue = new CommonLogQueue();
        }
        return commonLogQueue;
    }
}
