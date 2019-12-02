package com.platform.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platform.dao.PlatformWithdrawShareOrderDao;
import com.platform.entity.PlatformWithdrawShareOrderEntity;
import com.platform.service.PlatformWithdrawShareOrderService;

/**
 * Service实现类
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-07-10 01:14:48
 */
@Service("platformWithdrawShareOrderService")
public class PlatformWithdrawShareOrderServiceImpl implements PlatformWithdrawShareOrderService {
    @Autowired
    private PlatformWithdrawShareOrderDao platformWithdrawShareOrderDao;

    @Override
    public PlatformWithdrawShareOrderEntity queryObject(Integer id) {
        return platformWithdrawShareOrderDao.queryObject(id);
    }
    @Override
    public PlatformWithdrawShareOrderEntity queryByDateNo(String orderDateNo) {
    	return platformWithdrawShareOrderDao.queryByDateNo(orderDateNo);
    }

    @Override
    public List<PlatformWithdrawShareOrderEntity> queryList(Map<String, Object> map) {
        return platformWithdrawShareOrderDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return platformWithdrawShareOrderDao.queryTotal(map);
    }

    @Override
    public int save(PlatformWithdrawShareOrderEntity platformWithdrawShareOrder) {
    	platformWithdrawShareOrder.setState(0);
    	platformWithdrawShareOrder.setCreateTime(new Date());
    	platformWithdrawShareOrder.setUpdateTime(new Date());
        return platformWithdrawShareOrderDao.save(platformWithdrawShareOrder);
    }

    @Override
    public int update(PlatformWithdrawShareOrderEntity platformWithdrawShareOrder) {
    	platformWithdrawShareOrder.setUpdateTime(new Date());
        return platformWithdrawShareOrderDao.update(platformWithdrawShareOrder);
    }

    @Override
    public int delete(Integer id) {
        return platformWithdrawShareOrderDao.delete(id);
    }

    @Override
    public int deleteBatch(Integer[] ids) {
        return platformWithdrawShareOrderDao.deleteBatch(ids);
    }
}
