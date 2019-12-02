package com.platform.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platform.dao.PlatformWithdrawShareDao;
import com.platform.entity.PlatformWithdrawShareEntity;
import com.platform.service.PlatformWithdrawShareService;

/**
 * Service实现类
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-07-10 01:14:48
 */
@Service("platformWithdrawShareService")
public class PlatformWithdrawShareServiceImpl implements PlatformWithdrawShareService {
    @Autowired
    private PlatformWithdrawShareDao platformWithdrawShareDao;

    @Override
    public PlatformWithdrawShareEntity queryObject(Integer id) {
        return platformWithdrawShareDao.queryObject(id);
    }
    @Override
    public PlatformWithdrawShareEntity queryByUserId(Integer userId) {
    	Map<String, Object> params=new HashMap<>();
    	params.put("userId", userId);
    	List<PlatformWithdrawShareEntity> platformWithdrawShareEntities=platformWithdrawShareDao.queryByUserId(params);
    	if(platformWithdrawShareEntities!=null&&platformWithdrawShareEntities.size()>0)return platformWithdrawShareEntities.get(0);
    	return null;
    }

    @Override
    public List<PlatformWithdrawShareEntity> queryList(Map<String, Object> map) {
        return platformWithdrawShareDao.queryList(map);
    }
    @Override
    public List<PlatformWithdrawShareEntity> queryByWithdrawTypeStar(Integer withdrawTypeStar){
    	Map<String, Object> params=new HashMap<>();
    	params.put("withdrawTypeStar", withdrawTypeStar);
    	return platformWithdrawShareDao.queryList(params);
    }
    

    @Override
    public int queryTotal(Map<String, Object> map) {
        return platformWithdrawShareDao.queryTotal(map);
    }

    @Override
    public int save(PlatformWithdrawShareEntity platformWithdrawShare) {
    	platformWithdrawShare.setState(0);
    	platformWithdrawShare.setCreateTime(new Date());
    	platformWithdrawShare.setUpdateTime(new Date());
        return platformWithdrawShareDao.save(platformWithdrawShare);
    }

    @Override
    public int update(PlatformWithdrawShareEntity platformWithdrawShare) {
    	platformWithdrawShare.setUpdateTime(new Date());
        return platformWithdrawShareDao.update(platformWithdrawShare);
    }

    @Override
    public int delete(Integer id) {
        return platformWithdrawShareDao.delete(id);
    }

    @Override
    public int deleteBatch(Integer[] ids) {
        return platformWithdrawShareDao.deleteBatch(ids);
    }
}
