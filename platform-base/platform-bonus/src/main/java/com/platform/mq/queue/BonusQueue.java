package com.platform.mq.queue;

/**
 * 奖励队列
 */
public class BonusQueue extends AbstractTaskQueue {

    private static final long serialVersionUID = -972802808152063585L;
    private static BonusQueue bonusQueue;

    public static BonusQueue getInstance() {
        if (bonusQueue == null) {
        	bonusQueue = new BonusQueue();
        }
        return bonusQueue;
    }
}
