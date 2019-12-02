package com.platform.mq.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.platform.mq.model.BonusTaskVo;
import com.platform.mq.queue.BonusQueue;
import com.platform.utils.ObjToStringUtil;
public class TaskBonusProducer {
    private static Logger logger = LoggerFactory.getLogger(TaskBonusProducer.class);
    private static Logger loggerLOGBefore = LoggerFactory.getLogger("task_bonus_before");
   
    /**
     * 把奖励消息放入待发送队列中
     * @param bonusTaskVo
     * @param doLog
     */
    public static void addBonusTaskVo(BonusTaskVo bonusTaskVo, boolean doLog) {
    	logger.debug("开始加入奖励到队列:" + bonusTaskVo);
    	BonusQueue.getInstance().put(bonusTaskVo);
    	if (doLog) {
    		loggerLOGBefore.info(ObjToStringUtil.objToString(bonusTaskVo.toString()));
    	}
    }

}
