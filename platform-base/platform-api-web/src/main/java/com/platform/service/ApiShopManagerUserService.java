package com.platform.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platform.dao.ApiShopManagerUserMapper;
import com.platform.entity.ShopManagerUserVo;

/**
 * Service
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-25 18:05:33
 */
@Service
public class ApiShopManagerUserService {
    @Autowired
    private ApiShopManagerUserMapper shopManagerUserDao;

    public ShopManagerUserVo queryObject(Integer id) {
        return shopManagerUserDao.queryObject(id);
    }

    public List<ShopManagerUserVo> queryList(Map<String, Object> map) {
        return shopManagerUserDao.queryList(map);
    }

    public int queryTotal(Map<String, Object> map) {
        return shopManagerUserDao.queryTotal(map);
    }

    public int save(ShopManagerUserVo shopManagerUser) {
        return shopManagerUserDao.save(shopManagerUser);
    }

    public int update(ShopManagerUserVo shopManagerUser) {
        return shopManagerUserDao.update(shopManagerUser);
    }

    public int delete(Integer id) {
        return shopManagerUserDao.delete(id);
    }

    public int deleteBatch(Integer[] ids) {
        return shopManagerUserDao.deleteBatch(ids);
    }
}
