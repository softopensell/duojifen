package com.platform.mq.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.platform.mq.model.CommonLogVo;
import com.platform.mq.queue.CommonLogQueue;

public class TaskProducer {
    private static Logger logger = LoggerFactory.getLogger(TaskProducer.class);
    private static Logger loggerLOGBefore = LoggerFactory.getLogger("commLog_before");
    /**
     * 把日志消息放入待发送队列中
     *
     * @param addCommonLog
     * @param doLog
     */
    public static void addCommonLog(CommonLogVo commonLogVo, boolean doLog) {
    	logger.debug("开始加入日志到队列:" + commonLogVo);
    	CommonLogQueue.getInstance().put(commonLogVo);
    	if (doLog) {
    		loggerLOGBefore.info(commonLogVo.toString());
    	}
    }

}
