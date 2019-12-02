package com.platform.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platform.dao.ApiShopMapper;
import com.platform.entity.ShopVo;

/**
 * Service
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-25 18:05:33
 */
@Service
public class ApiShopService {
    @Autowired
    private ApiShopMapper shopDao;

    public ShopVo queryObject(Integer id) {
        return shopDao.queryObject(id);
    }

    public List<ShopVo> queryList(Map<String, Object> map) {
        return shopDao.queryList(map);
    }

    public int queryTotal(Map<String, Object> map) {
        return shopDao.queryTotal(map);
    }

    public int save(ShopVo shop) {
        return shopDao.save(shop);
    }

    public int update(ShopVo shop) {
        return shopDao.update(shop);
    }

    public int delete(Integer id) {
        return shopDao.delete(id);
    }

    public int deleteBatch(Integer[] ids) {
        return shopDao.deleteBatch(ids);
    }
}
