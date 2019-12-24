package com.platform.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platform.dao.PaymentInfoDao;
import com.platform.dao.PaymentLogDao;
import com.platform.dao.PaymentOutDao;
import com.platform.dao.UserDao;
import com.platform.entity.PaymentInfoEntity;
import com.platform.entity.PaymentLogEntity;
import com.platform.entity.PaymentOutEntity;
import com.platform.entity.UserEntity;
import com.platform.service.PaymentOutService;

/**
 * Service实现类
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-29 22:33:28
 */
@Service("paymentOutService")
public class PaymentOutServiceImpl implements PaymentOutService {
    @Autowired
    private PaymentOutDao paymentOutDao;
    @Autowired
    private PaymentInfoDao paymentInfoDao;
    @Autowired
    private PaymentLogDao paymentLogDao;
    @Autowired
    private UserDao userDao;

    @Override
    public PaymentOutEntity queryObject(Long id) {
        return paymentOutDao.queryObject(id);
    }
    @Override
    public List<PaymentOutEntity> queryList(Map<String, Object> map) {
        return paymentOutDao.queryList(map);
    }
    @Override
    public List<PaymentOutEntity> queryAll(Map<String, Object> map) {
        return paymentOutDao.queryAll(map);
    }
    @Override
    public Map<String,Object> queryStat(Map<String, Object> params){
    	
    	return paymentOutDao.queryStat(params);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return paymentOutDao.queryTotal(map);
    }

    @Override
    public int save(PaymentOutEntity paymentOut) {
        return paymentOutDao.save(paymentOut);
    }

    @Override
    public int update(PaymentOutEntity paymentOut) {
        return paymentOutDao.update(paymentOut);
    }

    @Override
    public int delete(Long id) {
        return paymentOutDao.delete(id);
    }

    @Override
    public int deleteBatch(Long[] ids) {
        return paymentOutDao.deleteBatch(ids);
    }

	@Override
	public void updatePayMentData(PaymentOutEntity paymentOut, PaymentInfoEntity paymentInfo,
			PaymentLogEntity paymentLog, UserEntity user) {
		if(paymentOut!=null&&paymentOut.getId()!=null) {
			paymentOutDao.update(paymentOut);
		}
		if(paymentInfo!=null) {
			paymentInfoDao.save(paymentInfo);
		}
		if(paymentLog!=null) {
			paymentLogDao.save(paymentLog);
		}
		if(user!=null&&user.getUserId()!=null) {
			userDao.update(user);
		}
	}
	@Override
	public void savePaymentOutData(PaymentOutEntity paymentOut,PaymentInfoEntity paymentInfo,UserEntity user) {
    	if(paymentOut!=null&&paymentOut.getId()==null) {
    		paymentOutDao.save(paymentOut);
    	}
    	if(paymentInfo!=null&&paymentInfo.getId()==null) {
    		paymentInfoDao.save(paymentInfo);
    	}
    	if(user!=null&&user.getUserId()!=null) {
    		userDao.update(user);
    	}
    }
}
