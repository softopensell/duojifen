package com.platform.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.platform.dao.PlatformFwManagerDao;
import com.platform.entity.PlatformFwManagerEntity;
import com.platform.service.PlatformFwManagerService;

/**
 * Service实现类
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-07-10 01:14:48
 */
@Service("platformFwManagerService")
public class PlatformFwManagerServiceImpl implements PlatformFwManagerService {
    @Autowired
    private PlatformFwManagerDao platformFwManagerDao;

    @Override
    public PlatformFwManagerEntity queryObject(Integer id) {
        return platformFwManagerDao.queryObject(id);
    }
    @Override
    public PlatformFwManagerEntity queryByUserId(Integer userId){
    	Map<String, Object> params=new HashMap<>();
    	params.put("userId", userId);
    	List<PlatformFwManagerEntity> platformFwManagerEntities=platformFwManagerDao.queryByUserId(params);
    	if(platformFwManagerEntities!=null&&platformFwManagerEntities.size()>0)return platformFwManagerEntities.get(0);
    	return null;
    }

    @Override
    public List<PlatformFwManagerEntity> queryList(Map<String, Object> map) {
        return platformFwManagerDao.queryList(map);
    }
    @Override
    public HashMap<Integer,PlatformFwManagerEntity> queryAll(){
    	List<PlatformFwManagerEntity> platformFwManagerEntities=platformFwManagerDao.queryList(new HashMap<>());
    	HashMap<Integer, PlatformFwManagerEntity> result=new HashMap<>();
    	for(PlatformFwManagerEntity platformFwManagerEntity:platformFwManagerEntities) {
    		result.put(platformFwManagerEntity.getFwUserId(), platformFwManagerEntity);
    	}
    	return result;
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return platformFwManagerDao.queryTotal(map);
    }

    @Override
    public int save(PlatformFwManagerEntity platformFwManager) {
    	platformFwManager.setState(0);
    	platformFwManager.setCreateTime(new Date());
    	platformFwManager.setUpdateTime(new Date());
        return platformFwManagerDao.save(platformFwManager);
    }

    @Override
    public int update(PlatformFwManagerEntity platformFwManager) {
    	platformFwManager.setUpdateTime(new Date());
        return platformFwManagerDao.update(platformFwManager);
    }

    @Override
    public int delete(Integer id) {
    	PlatformFwManagerEntity platformFwManager=platformFwManagerDao.queryObject(id);
    	if(platformFwManager!=null) {
    		platformFwManager.setState(1);
    		platformFwManager.setUpdateTime(new Date());
    		return platformFwManagerDao.update(platformFwManager);
    	}
       return -1;
//    	return platformFwManagerDao.delete(id);
    }

    @Override
    public int deleteBatch(Integer[] ids) {
    	for(Integer id:ids) {
    		delete(id);
    	}
    	return 1;
//        return platformFwManagerDao.deleteBatch(ids);
    }
}
