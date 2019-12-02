package com.platform.mq.entity;

import java.util.UUID;

/**
 * 向第三方发送的消息类的父类
 *
 * @version 1.0
 */
public abstract class ThirdPartyTask {

    private UUID taskID;

    public static final String TASK_ID = "TaskID";

    /**
     * 生成一个新的taskID
     */
    public ThirdPartyTask() {
        this.taskID = UUID.randomUUID();
    }


    public ThirdPartyTask(UUID taskID) {
        this.taskID = taskID;
    }

    /**
     * @return the taskID
     */
    public UUID getTaskID() {
        return taskID;
    }

}
