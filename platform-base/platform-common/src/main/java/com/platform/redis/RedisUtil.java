package com.platform.redis;

import java.util.Map;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

import com.alibaba.fastjson.JSON;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

public class RedisUtil {
	public  static  JedisPool jedisPool; // 池化管理jedis链接池
	private static final Properties properties = new Properties();
	
	static {	    
	 	JedisPoolConfig config = new JedisPoolConfig();  
	    //设置最大连接数
	    config.setMaxTotal(300);
	    //设置最大空闲数
	    config.setMaxIdle(600);
//			    //设置超时时间
	    config.setMaxWaitMillis(10000);
	    try {
			properties.load(
					Thread.currentThread().getContextClassLoader().getResourceAsStream("redis-config.properties"));
			PropertyConfigurator.configure(properties);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//初始化连接池
		jedisPool = new JedisPool(config, properties.getProperty("redis.host"), Integer.valueOf(properties.getProperty("redis.port")), Protocol.DEFAULT_TIMEOUT, properties.getProperty("redis.password"));
	  }
	
	
	/**
	 * 获取 redis链接
	 * @return
	 * 2017年9月13日
	 */
	public static Jedis getResource(){
		   return jedisPool.getResource();
	}
	
	
	
	/************************************************String Key 类型*******************************************/
	
	 /**
	   * 向缓存中设置字符串内容
	   * 失败返回0  不覆盖 成功 返回1
	   * @param key key
	   * @param value value
	   * @return
	   * @throws Exception
	   */
	public static long  setnx(String key,String value){
	    Jedis jedis = null;
	    try {
	      jedis = jedisPool.getResource();
	      return jedis.setnx(key, value);
	    } catch (Exception e) {
	      e.printStackTrace();
	    }finally{
	    	jedis.close();
	    }
	    return 0;
	  }
	public static long  setnx(String key,String value,Integer expiration){
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.setnx(key, value);
			if (expiration > 0) {
			  jedis.expire(key, expiration);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			jedis.close();
		}
		return 0;
	}
	public static String set(String key,String value, Integer expiration){
		String result = "";
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			if (expiration > 0) {
				result=jedis.setex(key, expiration, value);
			} else {
				result = jedis.set(key, value);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			jedis.close();
		}
		return result;
	}
	public static String set(String key,Object value, Integer expiration){
		String result = "";
		String objectJson = JSON.toJSONString(value);
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			if (expiration > 0) {
				result=jedis.setex(key, expiration, objectJson);
			} else {
			   result = jedis.set(key, objectJson);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			jedis.close();
		}
		return result;
	}
	public static String  set(String key,String value){
		return set(key, value, -1);
	}
	
	
	 /** 成功返回  OK
	   * 向缓存中设置对象(自动把对象转换成json数据存储到缓层中)
	   * @param key 
	   * @param value
	   * @return
	   */
	public static long  setnx(String key,Object value){
	    Jedis jedis = null;
	    try {
	      String objectJson = JSON.toJSONString(value);
	      jedis = jedisPool.getResource();
	      return jedis.setnx(key, objectJson);
	    } catch (Exception e) {
	      e.printStackTrace();
	    }finally{
	    	jedis.close();
	    }
	    return 0;
	  }
	 /**
	   * 删除缓存中得对象，根据key
	   * @param key
	   * @return
	   */
	public static boolean del(String key){
	    Jedis jedis = null;
	    try {
	      jedis = jedisPool.getResource();
	      jedis.del(key);
	      return true;
	    } catch (Exception e) {
	      e.printStackTrace();
	      return false;
	    }finally{
	    	jedis.close();
	    }
	  }
	
	
	/**
	   * 根据key 获取内容
	   * @param key
	   * @return
	   */
	public static Object get(String key){
	    Jedis jedis = null;
	    try {
	      jedis = jedisPool.getResource();
	      Object value = jedis.get(key);
	      return value;
	    } catch (Exception e) {
	      e.printStackTrace();
	      return false;
	    }finally{
	    	jedis.close();
	    }
	  }
	
	public static Long incr(String key){
	    Jedis jedis = null;
	    try {
	      jedis = jedisPool.getResource();
	      return jedis.incr(key);
	    } catch (Exception e) {
	      e.printStackTrace();
	      return null;
	    }finally{
	    	jedis.close();
	    }
	  }
	 
	 /**
	   * 根据key 获取对象
	   * @param key
	   * @return
	   */
	public static <T> T get(String key,Class<T> clazz){
	    Jedis jedis = null;
	    try {
	      jedis = jedisPool.getResource();
	      String value = jedis.get(key);
	      return JSON.parseObject(value, clazz);
	    } catch (Exception e) {
	      e.printStackTrace();
	      return null;
	    }finally{
	    	jedis.close();
	    }
	  }
	 
	 
	 /***
	  * 检查key是否存在
	  * @param key
	  * @return
	  * true 存在
	  * false 不存在
	  */
	public static boolean  checkExists(String key){
		Jedis jedis = null;
	    try {
	      jedis = jedisPool.getResource();
	      return jedis.exists(key); 
	    } catch (Exception e) {
	       e.printStackTrace();
	        return false;
	    }finally{
	    	jedis.close();
	    }
		
	}
	
	
	/***
	 * 往指定的key追加内容，key不在则添加key
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean appendStr(String key,String value){
		Jedis jedis = null;
	    try {
	      jedis = jedisPool.getResource();
	      jedis.append(key, value);	    
	      return true;
	    } catch (Exception e) {
	       e.printStackTrace();
	        return false;
	    }finally{
	    	jedis.close();
	    }
	}
	
	/***
	 * 批量获取key的value值
	 * @param keys
	 * @return
	 */
	public static Object bathKey(String[] keys){
		Jedis jedis = null;
	    try {
	      jedis = jedisPool.getResource();
	      return jedis.mget(keys);    
	    } catch (Exception e) {
	       e.printStackTrace();
	        return null;
	    }finally{
	    	jedis.close();
	    }
		
	}
	
	/***************************************hashes(哈希)类型*********************************************************/
	
	/**
	 * 设置hash field 
	 * 如果存在不会设置返回0
	 * @param key
	 * @param field
	 * @param value
	 * @return  成功返回1,失败  0
	 */
	public static long hsetnx(String key,String field,String value){
		Jedis jedis = null;
	    try {
	      jedis = jedisPool.getResource();
	      return jedis.hsetnx(key, field, value);    
	    } catch (Exception e) {
	       e.printStackTrace();
	    
	    }finally{
	    	jedis.close();
	    }
		return 0;
		
	} 
	
	/**
	 * hget取值(value)
	 * @param key
	 * @param field
	 * @return
	 */
	public static Object hget(String key,String field){
		Jedis jedis = null;
	    try {
	      jedis = jedisPool.getResource();
	      return jedis.hget(key, field) ;
	    } catch (Exception e) {
	       e.printStackTrace();
	        return null;
	    }finally{
	    	jedis.close();
	    }
	}
	
	/**
	 * hmset 批量设置值
	 * @param key
	 * @param hashmap
	 * @return 成功返回OK
	 */
	public static String  hmset(String key,Map<String, String> hashmap){
		Jedis jedis = null;
	    try {
	      jedis = jedisPool.getResource();
	      return jedis.hmset(key, hashmap);
	    } catch (Exception e) {
	       e.printStackTrace();  
	    }finally{
	    	jedis.close();
	    }
	    return null;
	}
	
	/**
	 * hmget 批量取值(value)
	 * @param key
	 * @param field
	 * @return
	 */
	public static Object hmget (String key,String...fields){
		Jedis jedis = null;
	    try {
	      jedis = jedisPool.getResource();
	      return jedis.hmget(key, fields);	     
	    } catch (Exception e) {
	       e.printStackTrace();
	        return null;
	    }finally{
	    	jedis.close();
	    }
	}
	
	/**
	 * @param key
	 * @return 返回所有的key和value
	 */
	public static Map<String, String> hgetall(String key){
		Jedis jedis = null;
	    try {
	      jedis = jedisPool.getResource();
	      return jedis.hgetAll(key);	     
	    } catch (Exception e) {
	       e.printStackTrace();
	        return null;
	    }finally{
	    	jedis.close();
	    }
	}
	
	
	/***************************************list(列表)*********************************************************/
	
	
	
	/**
	 * lpush 设置值 从头部压入一个元素
	 * @param key
	 * @param strings
	 * @return 成功返回成员的数量  失败返回0
	 */
	public static long  lpush(String key,String...strings){
		Jedis jedis = null;
	    try {
	      jedis = jedisPool.getResource();
	      return jedis.lpush(key, strings);
	    } catch (Exception e) {
	       e.printStackTrace();
	    }finally{
	    	jedis.close();
	    }
	    return 0;
	}
	public static long  lpush(String key,String value){
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.lpush(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			jedis.close();
		}
		return 0;
	}
	
	/**
	 * list列表取值(lrange)
	 * @param key
	 * @param start
	 * @param end
	 * @return start=0  end=-1(代表从开始到结束)
	 */
	public static Object lrange (String key,long start,long end){
		Jedis jedis = null;
	    try {
	      jedis = jedisPool.getResource();
	      return jedis.lrange(key, start, end);
	    } catch (Exception e) {
	       e.printStackTrace();
	    }finally{
	    	jedis.close();
	    }
	    return 0;
	}
	public static String  rpoplpush(String key,String dstkey){
		Jedis jedis = null;
	    try {
	      jedis = jedisPool.getResource();
	      return jedis.rpoplpush(key, dstkey);
	    } catch (Exception e) {
	       e.printStackTrace();
	    }finally{
	    	jedis.close();
	    }
	    return null;
	}
	public static String  rpop(String key){
		Jedis jedis = null;
	    try {
	      jedis = jedisPool.getResource();
	      return jedis.rpop(key);
	    } catch (Exception e) {
	       e.printStackTrace();
	    }finally{
	    	jedis.close();
	    }
	    return null;
	}
	public static String  lpop(String key){
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.lpop(key);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			jedis.close();
		}
		return null;
	}
	public static boolean expire(String key, int seconds) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.expire(key, seconds);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jedis.close();
		}
		return false;
	}
	
	
	public static void main(String[] args) {
		String key="xxxx2x";
		System.out.println(RedisUtil.incr(key));
		System.out.println(RedisUtil.get(key));
		
	}
	
}