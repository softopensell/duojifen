package com.platform.cache;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;

import com.platform.dao.UserBlackDao;
import com.platform.entity.UserBlackEntity;
import com.platform.utils.SpringContextUtils;


public class UserBlackCacheUtil implements InitializingBean {

    public static List<UserBlackEntity> userBlackEntityList;
    public static HashMap<Integer, UserBlackEntity> userBlackMap=new HashMap<>();
    
    public static List<UserBlackEntity> userMoneyBlackEntityList;
    public static HashMap<Integer, UserBlackEntity> userMoneyBlackMap=new HashMap<>();
    
    public static List<UserBlackEntity> userShareLevelEntityList;
    public static HashMap<Integer, UserBlackEntity> userShareLevelMap=new HashMap<>();
    
    public static void init() {
    	UserBlackDao userBlackDao = SpringContextUtils.getBean(UserBlackDao.class);
        if (null != userBlackDao) {
        	HashMap<String, Object> queryBlackMap=new HashMap<String, Object>();
        	queryBlackMap.put("blackType", 0);
        	userBlackEntityList = userBlackDao.queryList(queryBlackMap);
        	userBlackMap=new HashMap<>();
        	for(UserBlackEntity userBlackEntity:userBlackEntityList) {
        		userBlackMap.put(userBlackEntity.getUserId(), userBlackEntity);
        	}
        	
        	
        	HashMap<String, Object> queryShareLevelMap=new HashMap<String, Object>();
        	queryShareLevelMap.put("blackType", 2);
        	userShareLevelEntityList = userBlackDao.queryList(queryShareLevelMap);
        	userShareLevelMap=new HashMap<>();
        	for(UserBlackEntity item:userShareLevelEntityList) {
        		userShareLevelMap.put(item.getUserId(), item);
        	}
        	
        	HashMap<String, Object> queryMoneyBlackMap=new HashMap<String, Object>();
        	queryMoneyBlackMap.put("blackType", 100);
        	userMoneyBlackEntityList = userBlackDao.queryList(queryMoneyBlackMap);
        	userMoneyBlackMap=new HashMap<>();
        	for(UserBlackEntity item:userMoneyBlackEntityList) {
        		userMoneyBlackMap.put(item.getUserId(), item);
        	}
        	
        }
    }
    public static boolean getIsBlackUser(Integer userId) {
       if(userBlackMap.containsKey(userId)) {
    	   return true;
       }else {
    	   return false;
       }
    }
    public static boolean getIsMoneyBlackUser(Integer userId) {
    	if(userMoneyBlackMap.containsKey(userId)) {
    		return true;
    	}else {
    		return false;
    	}
    }
    
    public static boolean getIsShareLevelUser(Integer userId) {
        if(userShareLevelMap.containsKey(userId)) {
     	   return true;
        }else {
     	   return false;
        }
     }
    
    public static UserBlackEntity getShareLevelUser(Integer userId) {
    	return userShareLevelMap.get(userId);
     }
    public static UserBlackEntity getUserMoneyBlack(Integer userId) {
    	return userMoneyBlackMap.get(userId);
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }
}