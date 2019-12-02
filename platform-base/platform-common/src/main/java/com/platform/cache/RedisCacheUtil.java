package com.platform.cache;

import org.apache.log4j.Logger;

import com.platform.redis.RedisUtil;
import com.platform.utils.StringUtils;

public class RedisCacheUtil{
	protected Logger logger = Logger.getLogger(getClass());
	
	public static String put(final String key, final Object obj) {
		return put(key, -1, obj);
	}

	public static String put(final String key, final Integer expiration,
			final Object obj) {
		return RedisUtil.set(key, obj, expiration);
	}
	
	public static boolean expiration(final String key, final Integer expiration) {
		return RedisUtil.expire(key, expiration);
	}
	
	public static Long incr(final String key) {
		return RedisUtil.incr(key);
	}

	public static Object get(final String key) {
			return RedisUtil.get(key);
	}

	public static void lpush(final String key,
			final String obj) {
	    	RedisUtil.lpush(key, obj);
	}
	
	public static Object lpop(final String key) {
		return RedisUtil.lpop(key);
	}
	/**
	 * 
	 * @param action 操作动作
	 * @param userId 发起操作的用户ID
	 * @param time 时间范围X秒内
	 * @param number 限制操作数Y次
	 * @param expire 超出封印时间Z
	 * @return
	 */
	public static boolean frequencyLimit(String action, int  userId, int time, int number, int expireTime) {
		 if(StringUtils.isEmpty(action)||userId<=0||time<=0|| number<=0) {
			 return false;
		 }
		  String redisKey="act:limit:"+action+":"+userId;
		  Long currentTime=0l;
		  String tempStr=(String) get(redisKey);
		  if(tempStr==null)currentTime=0l;
		  else {
			  currentTime=Long.valueOf(tempStr);
		  }
		  if(currentTime>number) {
			  return false;
		  }
		  currentTime=incr(redisKey);
		  System.out.println("-----currentTime---"+currentTime+"----redisKey----------"+redisKey+"----time----------"+time+"----number----------"+number);
		  if(currentTime==1) {
			  expiration(redisKey, time);
			  return true;
		  }
		  if(currentTime<=number) {
			  return true;
		  }
		  if(currentTime==number) {
			  expiration(redisKey, expireTime);
		  }
		  return false;
	}
	public static Object getFrequencyLimit(String action, int  userId) {
		if(StringUtils.isEmpty(action)||userId<=0) {
			return false;
		}
		String redisKey="act:limit:"+action+":"+userId;
		return get(redisKey);
	}

	
	
	
	public static void main(String[] args) {
		String key="ss22s";
//		RedisCacheUtil.lpush(key, key);
//		System.out.println("---------"+RedisCacheUtil.lpop(key));
//		System.out.println("---------"+RedisCacheUtil.lpop(key));
//		RedisCacheUtil.put(key,1,1);
//		System.out.println("---------"+RedisCacheUtil.get(key));
//		System.out.println("---------"+RedisCacheUtil.incr(key));
//		System.out.println("---------"+RedisCacheUtil.get(key));
//		System.out.println("---------"+RedisCacheUtil.incr(key));
		int userId=100;
		int xzTime=5;
		int yxtime=4;
		int maxTime=1;
		
		System.out.println("---------"+RedisCacheUtil.getFrequencyLimit(key, userId));
		System.out.println("---------"+RedisCacheUtil.frequencyLimit(key, userId, yxtime, maxTime, xzTime));
		for(int i=0;i<20;i++) {
			try {
				Thread.currentThread().sleep(1000*1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		 System.out.println("-----"+i+"----"+RedisCacheUtil.frequencyLimit(key, userId, yxtime, maxTime, xzTime));
		}
	}
}