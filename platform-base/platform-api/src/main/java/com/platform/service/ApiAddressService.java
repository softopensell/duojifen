package com.platform.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platform.dao.ApiAddressMapper;
import com.platform.entity.AddressVo;

/**
 * 地址表Service
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-03-28 18:32:37
 */
@Service
public class ApiAddressService {
    @Autowired
    private ApiAddressMapper addressDao;

    public AddressVo queryObject(Integer id) {
        return addressDao.queryObject(id);
    }

    public List<AddressVo> queryList(Map<String, Object> map) {
        return addressDao.queryList(map);
    }

    public int queryTotal(Map<String, Object> map) {
        return addressDao.queryTotal(map);
    }

    public int save(AddressVo address) {
        return addressDao.save(address);
    }

    public int update(AddressVo address) {
        return addressDao.update(address);
    }

    public int delete(Integer id) {
        return addressDao.delete(id);
    }

    public int deleteBatch(Integer[] ids) {
        return addressDao.deleteBatch(ids);
    }
}
