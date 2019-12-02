package com.platform.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platform.dao.ApiUserPayCardMapper;
import com.platform.entity.UserPayCardVo;

/**
 * Service
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-25 18:05:33
 */
@Service
public class ApiUserPayCardService {
    @Autowired
    private ApiUserPayCardMapper userPayCardDao;

    public UserPayCardVo queryObject(Long id) {
        return userPayCardDao.queryObject(id);
    }

    public List<UserPayCardVo> queryList(Map<String, Object> map) {
        return userPayCardDao.queryList(map);
    }

    public int queryTotal(Map<String, Object> map) {
        return userPayCardDao.queryTotal(map);
    }

    public int save(UserPayCardVo userPayCard) {
        return userPayCardDao.save(userPayCard);
    }

    public int update(UserPayCardVo userPayCard) {
        return userPayCardDao.update(userPayCard);
    }

    public int delete(Long id) {
        return userPayCardDao.delete(id);
    }

    public int deleteBatch(Long[] ids) {
        return userPayCardDao.deleteBatch(ids);
    }
}
