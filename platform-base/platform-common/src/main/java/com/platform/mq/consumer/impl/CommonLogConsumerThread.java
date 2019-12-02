//package com.platform.mq.consumer.impl;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.yaotuofu.common.log.bo.CommonLog;
//import com.yaotuofu.common.log.service.CommonLogMgr;
//import com.yaotuofu.common.mq.model.CommonLogVo;
//import com.yaotuofu.common.mq.queue.CommonLogQueue;
//
///**
// * 日志的consumer线程，从队列中取待发短信，然后调用adapter发送
// */
//public class CommonLogConsumerThread extends Thread {
//	
//    private static Logger logger = LoggerFactory.getLogger(CommonLogConsumerThread.class);
//    private static Logger loggerLogAfterSucc = LoggerFactory.getLogger("log_after_succ");
//    private static Logger loggerLogAfterFail = LoggerFactory.getLogger("log_after_fail");
//    private int thread_id;
//    
//    private CommonLogMgr commonLogMgr;
//    /**
//     * 构造函数
//     * @param thread_id
//     */
//    public CommonLogConsumerThread(CommonLogMgr commonLogMgr,int thread_id) {
//    	this.thread_id = thread_id;
//		logger.info("日志处理线程 " + thread_id + " 创建成功");
//		this.commonLogMgr=commonLogMgr;
//    }
//    @Override
//    public void run() {
//        CommonLogVo commonLogVo =(CommonLogVo) CommonLogQueue.getInstance().take();
//        if (commonLogVo == null) {
//        	logger.info("日志线程【{}】从队列取出日志为null", thread_id);
//            return;
//        }
//        logger.info("日志线程【{}】从队列取出日志【{}】", thread_id, commonLogVo.toString());
//        CommonLog commonLog= commonLogMgr.insertCommonLog(commonLogVo.getOperator(), commonLogVo.getOperation(), commonLogVo.getContent(), commonLogVo.getIp(), commonLogVo.getParameter());
//        if (commonLog != null) {
//        	loggerLogAfterSucc.info(commonLogVo.toString());
//        } else {
//            logger.error("写入日志时发生异常, 异常日志id=" + commonLogVo.getTaskID());
//            loggerLogAfterFail.info(commonLogVo.toString());
//        }
//    }
//}
