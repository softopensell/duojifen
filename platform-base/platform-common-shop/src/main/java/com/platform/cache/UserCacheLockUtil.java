package com.platform.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.platform.utils.JsonUtil;


public class UserCacheLockUtil{
	// 放并发锁的map
	public static Map<String, Lock> currentUserLocks = new ConcurrentHashMap<String, Lock>();
	/**
	 * 获取锁
	 * 
	 * @param key
	 * @return
	 */
	public static synchronized Lock getKey(String key) {
		Lock obj = currentUserLocks.get(key);
		if (obj == null) {
			obj = new ReentrantLock();
			currentUserLocks.put(key, obj);
		}
		System.out.println("获得用户操作锁 key:" + key + ":" + JsonUtil.getJsonByObj(obj));
		return obj;
	}
}