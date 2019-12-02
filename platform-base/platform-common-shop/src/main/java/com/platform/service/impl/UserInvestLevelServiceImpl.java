package com.platform.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platform.dao.UserInvestLevelDao;
import com.platform.entity.UserInvestLevelEntity;
import com.platform.service.UserInvestLevelService;

/**
 * Service实现类
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-06-15 02:21:39
 */
@Service("userInvestLevelService")
public class UserInvestLevelServiceImpl implements UserInvestLevelService {
    @Autowired
    private UserInvestLevelDao userInvestLevelDao;

    @Override
    public UserInvestLevelEntity queryObject(Integer id) {
        return userInvestLevelDao.queryObject(id);
    }

    @Override
    public List<UserInvestLevelEntity> queryList(Map<String, Object> map) {
        return userInvestLevelDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return userInvestLevelDao.queryTotal(map);
    }

    @Override
    public int save(UserInvestLevelEntity userInvestLevel) {
        return userInvestLevelDao.save(userInvestLevel);
    }

    @Override
    public int update(UserInvestLevelEntity userInvestLevel) {
        return userInvestLevelDao.update(userInvestLevel);
    }

    @Override
    public int delete(Integer id) {
        return userInvestLevelDao.delete(id);
    }

    @Override
    public int deleteBatch(Integer[] ids) {
        return userInvestLevelDao.deleteBatch(ids);
    }
    @Override
    public UserInvestLevelEntity queryByLevelType(Integer userLevelType) {
    	return userInvestLevelDao.queryByLevelType(userLevelType);
    }
}
