//package com.platform.mq.consumer.impl;
//
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.Resource;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.yaotuofu.common.log.service.CommonLogMgr;
//import com.yaotuofu.common.mq.consumer.AbstractTaskConsumer;
//
///**
// * 线程池，其中的线程从日志队列中取待发送的commonLogVo对象
// *
// * @version 1.0
// */
//public class CommonLogConsumer extends AbstractTaskConsumer {
//    private ScheduledExecutorService executorService;
//    private static Logger logger = LoggerFactory.getLogger(CommonLogConsumer.class);
//
//    @Resource
//    private CommonLogMgr commonLogMgr;
//    /**
//     * 启动日志Consumer的线程池
//     *
//     * @param commonLogVo
//     */
//    @PostConstruct
//    public void startConsume() {
//        logger.info("开始启动日志写入线程池");
//        if (this.executorService == null || executorService.isShutdown()
//                || executorService.isTerminated()) {
//            executorService = Executors.newScheduledThreadPool(this.consumer_thread_number);
//
//            for (int i = 1; i < (consumer_thread_number + 1); i++) {
//                // 此处initialDelay是i，单位秒
//                executorService.scheduleAtFixedRate(new CommonLogConsumerThread(commonLogMgr,i), 0, interval,
//                        TimeUnit.SECONDS);
//            }
//        }
//        logger.info("日志发送线程池初始化成功, 共含" + consumer_thread_number + "线程, 每" + interval + "秒钟轮询队列一次");
//    }
//
//    /**
//     * @return the executorService
//     */
//    public ScheduledExecutorService getExecutorService() {
//        return executorService;
//    }
//}
