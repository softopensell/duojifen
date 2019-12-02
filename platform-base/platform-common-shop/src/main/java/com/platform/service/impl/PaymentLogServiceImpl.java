package com.platform.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.platform.dao.PaymentLogDao;
import com.platform.entity.PaymentLogEntity;
import com.platform.service.PaymentLogService;

/**
 * Service实现类
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-29 22:33:28
 */
@Service("paymentLogService")
public class PaymentLogServiceImpl implements PaymentLogService {
    @Autowired
    private PaymentLogDao paymentLogDao;

    @Override
    public PaymentLogEntity queryObject(Long id) {
        return paymentLogDao.queryObject(id);
    }

    @Override
    public List<PaymentLogEntity> queryList(Map<String, Object> map) {
        return paymentLogDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return paymentLogDao.queryTotal(map);
    }

    @Override
    public int save(PaymentLogEntity paymentLog) {
        return paymentLogDao.save(paymentLog);
    }

    @Override
    public int update(PaymentLogEntity paymentLog) {
        return paymentLogDao.update(paymentLog);
    }

    @Override
    public int delete(Long id) {
        return paymentLogDao.delete(id);
    }

    @Override
    public int deleteBatch(Long[] ids) {
        return paymentLogDao.deleteBatch(ids);
    }
}
