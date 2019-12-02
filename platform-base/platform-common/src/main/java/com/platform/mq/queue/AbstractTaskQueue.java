package com.platform.mq.queue;

import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.platform.mq.model.ThirdPartyTask;

/**
 * 所有Queue的父类
 *
 * @version 1.0
 */
public abstract class AbstractTaskQueue extends LinkedBlockingQueue<ThirdPartyTask> {

    private static final long serialVersionUID = -3645243155204152472L;
    protected final static Log LOG = LogFactory.getLog(AbstractTaskQueue.class);

    @Override
    public void put(ThirdPartyTask e) {
        try {
            super.put(e);
        } catch (InterruptedException ie) {
            LOG.error("InterruptedException while put new task in", ie);
        }
    }

    /**
     * 取队列头部的一个Task对象
     */
    @Override
    public ThirdPartyTask take() {
        try {
            return super.take();
        } catch (InterruptedException ie) {
            LOG.error("InterruptedException while put new task in", ie);
            return null;
        }
    }


}
