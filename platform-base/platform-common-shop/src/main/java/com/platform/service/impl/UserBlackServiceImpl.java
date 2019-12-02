package com.platform.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.platform.dao.UserBlackDao;
import com.platform.entity.UserBlackEntity;
import com.platform.service.UserBlackService;

/**
 * Service实现类
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-10-21 03:14:08
 */
@Service("userBlackService")
public class UserBlackServiceImpl implements UserBlackService {
    @Autowired
    private UserBlackDao userBlackDao;

    @Override
    public UserBlackEntity queryObject(Integer id) {
        return userBlackDao.queryObject(id);
    }

    @Override
    public List<UserBlackEntity> queryList(Map<String, Object> map) {
        return userBlackDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return userBlackDao.queryTotal(map);
    }

    @Override
    public int save(UserBlackEntity userBlack) {
        return userBlackDao.save(userBlack);
    }

    @Override
    public int update(UserBlackEntity userBlack) {
        return userBlackDao.update(userBlack);
    }

    @Override
    public int delete(Integer id) {
        return userBlackDao.delete(id);
    }

    @Override
    public int deleteBatch(Integer[] ids) {
        return userBlackDao.deleteBatch(ids);
    }
}
