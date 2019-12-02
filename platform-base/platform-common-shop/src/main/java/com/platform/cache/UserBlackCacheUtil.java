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
    public static void init() {
    	UserBlackDao userBlackDao = SpringContextUtils.getBean(UserBlackDao.class);
        if (null != userBlackDao) {
        	userBlackEntityList = userBlackDao.queryList(new HashMap<String, Object>());
        	userBlackMap=new HashMap<>();
        	for(UserBlackEntity userBlackEntity:userBlackEntityList) {
        		userBlackMap.put(userBlackEntity.getUserId(), userBlackEntity);
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
    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }
}