package com.platform.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platform.dao.ApiUserMapper;
import com.platform.entity.SmsLogEntity;
import com.platform.entity.UserVo;
import com.platform.utils.ApiRRException;
import com.platform.validator.ApiAssert;

/**
 * Service
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-25 18:05:33
 */
@Service
public class ApiUserService {
    @Autowired
    private ApiUserMapper userDao;

    public UserVo queryObject(Integer userId) {
        return userDao.queryObject(userId);
    }

    public List<UserVo> queryList(Map<String, Object> map) {
        return userDao.queryList(map);
    }

    public int queryTotal(Map<String, Object> map) {
        return userDao.queryTotal(map);
    }

    public int save(UserVo user) {
        return userDao.save(user);
    }

    public int update(UserVo user) {
        return userDao.update(user);
    }

    public int delete(Integer userId) {
        return userDao.delete(userId);
    }

    public int deleteBatch(Integer[] userIds) {
        return userDao.deleteBatch(userIds);
    }
    
    public UserVo queryByMobile(String mobile) {
        return userDao.queryByMobile(mobile);
    }
    public void save(String mobile, String password) {
        UserVo user = new UserVo();
        user.setMobile(mobile);
        user.setUserName(mobile);
        user.setNickname(mobile);
        user.setPassword(DigestUtils.sha256Hex(password));
        user.setCreatetime(new Date());
        
        //审核状态
        user.setAuthTypeStatu(1);
        //余额钱包
        user.setBalance(new BigDecimal(0));
        //消费金额
        user.setAmount(new BigDecimal(0));
        //累积购物钱包
        user.setTotalPoint(new BigDecimal(0));
        //购物余额钱包
        user.setPoint(new BigDecimal(0));
        //累积收益
        user.setTotalIncome(new BigDecimal(0));
        //等待收益
        user.setWaitingIncome(new BigDecimal(0));
        //积分数
        user.setIntegralScore(0);
        //冻结余额
        user.setFreezeBalance(new BigDecimal(0));
        
        
//        user.setInvestIncomeMoney(new BigDecimal(0));
//    	user.setTotalInvestIncomeMoney(new BigDecimal(0));
//    	user.setShareInvestLastTime(new Date());
//    	user.setTotalInvestMoney(new BigDecimal(0));
//    	user.setTotalInverstOrderSum(0);
//    	user.setTotalIncome(new BigDecimal(0));
        
        userDao.save(user);
    }
    public Integer login(String mobile, String password) {
        UserVo user = queryByMobile(mobile);
        ApiAssert.isNull(user, "手机号或密码错误");

        //密码错误
        if (!user.getPassword().equals(DigestUtils.sha256Hex(password))) {
            throw new ApiRRException("手机号或密码错误");
        }

        return user.getUserId();
    }

    public SmsLogEntity querySmsCodeByUserId(Integer user_id) {
        return userDao.querySmsCodeByUserId(user_id);
    }
    public SmsLogEntity querySmsCodeByPhone(String phone){
    	return userDao.querySmsCodeByPhone(phone);
    }


    public int saveSmsCodeLog(SmsLogEntity smsLogVo) {
        return userDao.saveSmsCodeLog(smsLogVo);
    }
    public UserVo queryByOpenId(String openId) {
        return userDao.queryByOpenId(openId);
    }
    
    

	/**
	 * 消费余额
	 * @param userId
	 * @param addBalance
	 * @return
	 */
	public int reduceUserBalance(Integer userId, BigDecimal  reduceBalance) {
		UserVo user = this.queryObject(userId);
		BigDecimal balance = user.getBalance();
		BigDecimal subtract_subtract = balance.subtract(reduceBalance);
		user.setBalance(subtract_subtract);
		update(user);
		return update(user);
	}
	 /**
     * 增加余额
     * @param userId
     * @param addBalance
     * @return
     */
	public int addUserBalance(Integer userId, BigDecimal  addBalance) {
		UserVo user = this.queryObject(userId);
		BigDecimal balance = user.getBalance();
		BigDecimal add_subtract = balance.add(addBalance);
		user.setBalance(add_subtract);
		update(user);
		return update(user);
	}
	
	
	/**
	 * 消费积分
	 * @param userId
	 * @param addBalance
	 * @return
	 */
	public int reduceUserIntegralScore(Integer userId, Integer  integralScore) {
		UserVo user = this.queryObject(userId);
		Integer integralNum = user.getIntegralScore();
		Integer subtract_integralNum = integralNum-integralScore;
		user.setIntegralScore(subtract_integralNum);
		return update(user);
	}
	 /**
     * 增加积分
     * @param userId
     * @param addBalance
     * @return
     */
	public int addUserIntegralScore(Integer userId, Integer  integralScore) {
		UserVo user = this.queryObject(userId);
		Integer integralNum = user.getIntegralScore();
		Integer add_integralNum = integralNum+integralScore;
		user.setIntegralScore(add_integralNum);
		return update(user);
	}
}
