package com.platform.service.impl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platform.cache.IdWorkCache;
import com.platform.cache.UserCacheLockUtil;
import com.platform.constants.PluginConstant;
import com.platform.dao.PaymentInfoDao;
import com.platform.dao.UserDao;
import com.platform.entity.PaymentInfoEntity;
import com.platform.entity.SmsLogEntity;
import com.platform.entity.UserEntity;
import com.platform.service.UserService;
import com.platform.util.ShopConstant;
import com.platform.utils.ApiRRException;
import com.platform.utils.DateUtils;
import com.platform.utils.JsonUtil;
import com.platform.utils.RandomUtil;
import com.platform.utils.ShiroUtils;
import com.platform.validator.ApiAssert;

/**
 * Service实现类
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-29 10:07:38
 */
@Service("userService")
public class UserServiceImpl implements UserService {
	protected Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private UserDao userDao;
    @Autowired
    private PaymentInfoDao paymentInfoDao;
    @Autowired
	private IdWorkCache idWorkCache ;
    @Override
    public UserEntity getById(Integer userId) {
    	 return userDao.getById(userId);
    }
    @Override
    public UserEntity queryObject(Integer userId) {
        return userDao.queryObject(userId);
    }
    @Override
    public  UserEntity queryByUserName(String userName){
    	return userDao.queryByUserName(userName);
    }

    @Override
    public List<UserEntity> queryList(Map<String, Object> map) {
        return userDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return userDao.queryTotal(map);
    }

    @Override
    public int save(UserEntity user) {
    	
    	user.setBalance(new BigDecimal(0));
    	user.setFreezeBalance(new BigDecimal(0));
    	user.setTotalIncome(new BigDecimal(0));
    	user.setWaitingIncome(new BigDecimal(0));
    	
    	user.setRegisterTime(new Date());
    	user.setCreatetime(new Date());
    	
    	user.setIntegralScore(0);
    	user.setFund(new BigDecimal(0));
    	user.setInvestIncomeMoney(new BigDecimal(0));
    	user.setTotalInvestIncomeMoney(new BigDecimal(0));
    	user.setShareInvestLastTime(new Date());
    	user.setTotalInvestMoney(new BigDecimal(0));
    	user.setTotalInverstOrderSum(0);
    	user.setTotalIncome(new BigDecimal(0));
    	user.setShareInvestLastTime(new Date());
    	user.setUserPreBalance(new BigDecimal(0));
    	
        return userDao.save(user);
    }

    @Override
    public int update(UserEntity user) {
//    	String userLockkey="USER_"+user.getUserId();
//    	Lock lock =UserCacheLockUtil.getKey(userLockkey);
//		lock.lock();
//		int flag=0;
//        try{
//            flag=userDao.update(user);
//        }catch (Exception e){
//        }finally {
//            lock.unlock();
//        }
        int flag=userDao.update(user);
        return flag;
    }
    @Override
    public int delete(Integer userId) {
        return userDao.delete(userId);
    }

    @Override
    public int deleteBatch(Integer[] userIds) {
        return userDao.deleteBatch(userIds);
    }
    /**
     * 增加用户收益
     * @param userId
     * @param addBalance
     * @return
     */
    @Override
	public int addUserBalanceAndTotalIncome(Integer userId, BigDecimal  addBalance) {
    	UserEntity user = this.queryObject(userId);
		BigDecimal balance = user.getBalance();
		BigDecimal totalIncome = user.getTotalIncome();
		BigDecimal add_subtract = balance.add(addBalance);
		BigDecimal add_totalIncome = totalIncome.add(addBalance);
		user.setBalance(add_subtract);
		user.setTotalIncome(add_totalIncome);
		return update(user);
	}
    @Override
    public int addInverst(Integer userId, BigDecimal investMoney,BigDecimal totalIncomeMoney) {
    	UserEntity user = this.queryObject(userId);
    	BigDecimal totalInvestMoney = user.getTotalInvestMoney();
    	if(totalInvestMoney==null)totalInvestMoney=new BigDecimal(0);
		BigDecimal add_subtract = totalInvestMoney.add(investMoney);//固定投资额
		user.setTotalInvestMoney(add_subtract);
		
		BigDecimal totalInvestIncomeMoney = user.getTotalInvestIncomeMoney();
		if(totalInvestIncomeMoney==null)totalInvestIncomeMoney=new BigDecimal(0);
		BigDecimal add_totalIncome = totalInvestIncomeMoney.add(totalIncomeMoney);//总收益 额度 增加
		user.setTotalInvestIncomeMoney(add_totalIncome);
		
		BigDecimal surplusInvestMoney = user.getSurplusInvestMoney();
		if(surplusInvestMoney==null)surplusInvestMoney=new BigDecimal(0);
		BigDecimal add_surplusInvestMoney = surplusInvestMoney.add(totalIncomeMoney);//剩余收益 额度 增加
		user.setSurplusInvestMoney(add_surplusInvestMoney);
		if(user.getTotalInverstOrderSum()==null)user.setTotalInverstOrderSum(0);
		//添加投资订单数量
		user.setTotalInverstOrderSum(user.getTotalInverstOrderSum()+1);
		logger.info("--------addInverst-------"+JsonUtil.getJsonByObj(user));
    	return update(user);
    }
    @Override
    public int addUserBalanceAndTotalIncomeAndReduceInverst(Integer userId, BigDecimal addBalance) {
    	UserEntity user = this.queryObject(userId);
    	
		BigDecimal balance = user.getBalance();
		if(balance==null)balance=new BigDecimal(0);
		BigDecimal add_subtract = balance.add(addBalance);
		
		BigDecimal totalIncome = user.getTotalIncome();
		if(totalIncome==null)totalIncome=new BigDecimal(0);
		BigDecimal add_totalIncome = totalIncome.add(addBalance);
		user.setBalance(add_subtract);
		user.setTotalIncome(add_totalIncome);
		
		
		BigDecimal inverstIncome = user.getInvestIncomeMoney();
		if(inverstIncome==null)inverstIncome=new BigDecimal(0);
		BigDecimal add_inverstIncome = inverstIncome.add(addBalance);
		user.setInvestIncomeMoney(add_inverstIncome);
		
		BigDecimal surplusInvestMoney = user.getSurplusInvestMoney();
		if(surplusInvestMoney==null)surplusInvestMoney=new BigDecimal(0);
		BigDecimal sub_surplusInvestMoney = surplusInvestMoney.subtract(addBalance);
		user.setSurplusInvestMoney(sub_surplusInvestMoney);
		
		user.setShareInvestLastTime(new Date());
		return update(user);
    }
    
    @Override
    public List<UserEntity> queryByUserIds(Collection<Integer> userIds){
    	return userDao.queryByUserIds(userIds);
    }
    @Override
    public Map<Integer,UserEntity> getByUserIds(Collection<Integer> userIds){
    	return userDao.getByUserIds(userIds);
    }
	@Override
	public void updateIntegral(UserEntity user,int addIntegralScore) {
		UserEntity quser = userDao.queryObject(user.getUserId());
    	if(quser!=null) {
    		Integer integralScore = quser.getIntegralScore();
    		if(integralScore==null) {
    			integralScore = 0;
    		}
    		integralScore = addIntegralScore+user.getIntegralScore();
    		quser.setIntegralScore(integralScore);
    		quser.setUpdatetime(new Date());
    		update(quser);
    		
	    	PaymentInfoEntity paymentInfo=new PaymentInfoEntity();
//			String paymentSn = "QA"+DateUtils.format(new Date(),"yyMMddHHmmssss")+RandomUtil.getRandom(1000l,9999l);
			
	    	String paymentSn = "QA"+DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN_YYYY_MM_DD_HH_MM_SS_SSS)+RandomUtil.getRandom(1000l,9999l);
			paymentSn=idWorkCache.getIdDayHHMMEndRadom("PT", IdWorkCache.ID_WORK_TYPE_PAY_SN, 5);
			
			paymentInfo.setAmount(new BigDecimal(user.getIntegralScore()+""));//积分
			paymentInfo.setOperatorId(ShiroUtils.getUserEntity().getUserId().intValue());
			paymentInfo.setOperatorName(ShiroUtils.getUserEntity().getUsername());
			paymentInfo.setPaymentDate(new Date());
			paymentInfo.setSn(paymentSn);
			paymentInfo.setStatus(10);//交易结束
			paymentInfo.setFee(new BigDecimal("0"));
			paymentInfo.setUserId(quser.getUserId().longValue());
			paymentInfo.setPayer(quser.getUserName());
			paymentInfo.setUserName(quser.getUserName());
			paymentInfo.setOrderType(1);//
			paymentInfo.setMoneyTypeWallet(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_IN_INTEGRAL_ADD);
			paymentInfo.setPaymentType(PluginConstant.PAYMENT_TYPE_IN);
			paymentInfo.setCreateTime(new Date());
			paymentInfo.setUpdateTime(new Date());
			paymentInfoDao.save(paymentInfo);
    	}
	}
	
	 public UserEntity queryByMobile(String mobile) {
	        return userDao.queryByMobile(mobile);
	  }
	public Integer login(String mobile, String password) {
        UserEntity user = queryByMobile(mobile);
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
    public UserEntity queryByOpenId(String openId) {
        return userDao.queryByOpenId(openId);
    }
    
    public List<UserEntity> queryListToShareInvestUserByDate(Date today) {
    	Map<String, Object> map=new HashMap<>();
    	map.put("state", ShopConstant.SHOP_USER_STATU_SUCCESS);//正常的订单
    	today=DateUtils.getStartOfDate(today);
    	map.put("shareInvestLastTimeEnd", DateUtils.format(today));//结束时间 <
    	return userDao.queryList(map);
    }
    public List<UserEntity> queryAllList() {
    	Map<String, Object> map=new HashMap<>();
    	map.put("state", ShopConstant.SHOP_USER_STATU_SUCCESS);//正常的订单
    	return userDao.queryList(map);
    }
    
    
	/**
	 * 消费余额
	 * @param userId
	 * @param addBalance
	 * @return
	 */
	public int reduceUserBalance(Integer userId, BigDecimal  reduceBalance) {
		UserEntity user = this.queryObject(userId);
		BigDecimal balance = user.getBalance();
		BigDecimal subtract_subtract = balance.subtract(reduceBalance);
		user.setBalance(subtract_subtract);
		return update(user);
	}
	 /**
     * 增加余额
     * @param userId
     * @param addBalance
     * @return
     */
	public int addUserBalance(Integer userId, BigDecimal  addBalance) {
		UserEntity user = this.queryObject(userId);
		BigDecimal balance = user.getBalance();
		if(balance==null) balance=new BigDecimal(0);
		BigDecimal add_subtract = balance.add(addBalance);
		user.setBalance(add_subtract);
		return update(user);
	}
	/**
	 * 增加投资收益
	 * @param userId
	 * @param addBalance
	 * @return
	 */
	public int addUserInverstBalance(Integer userId, BigDecimal  addBalance) {
		UserEntity user = this.queryObject(userId);
		BigDecimal balance = user.getBalance();
		BigDecimal add_subtract = balance.add(addBalance);
		user.setBalance(add_subtract);
		return update(user);
	}
	
	
	/**
	 * 消费积分
	 * @param userId
	 * @param addBalance
	 * @return
	 */
	public int reduceUserIntegralScore(Integer userId, Integer  integralScore) {
		UserEntity user = this.queryObject(userId);
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
		UserEntity user = this.queryObject(userId);
		Integer integralNum = user.getIntegralScore();
		Integer add_integralNum = integralNum+integralScore;
		user.setIntegralScore(add_integralNum);
		return update(user);
	}
	public List<UserEntity> queryNodesByParentNodeName(String signupNodePhone){
	     return userDao.queryNodesByParentNodeName(signupNodePhone);	
	}
	public int addUserFund(Integer userId, BigDecimal fund) {
		UserEntity user = this.queryObject(userId);
		BigDecimal fundNum = user.getFund();
		BigDecimal add_fundNum = fundNum.add(fund);
		user.setFund(add_fundNum);
		return update(user);
	}
	
	/**
	 * 统计平台用户数据
	 * @return
	 */
	public Map<String,Object> queryTotalStat(){
		
		return userDao.queryTotalStat();
	}
	/**
	 * 统计平台每日新增用户数据
	 * @param params
	 * @return
	 */
	public Map<String,Object> queryStatByDay(Date date){
		Map<String, Object> params=new HashMap<>();
		params.put("registerTime", DateUtils.format(date));
		return userDao.queryStatByDay(params);
	}
	/**
	 * Group 统计平台数据
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> queryGroupStat(Integer curPage, Integer pageSize){
		
		Map<String, Object> params=new HashMap<>();
	    if(curPage!=null&&pageSize!=null&&curPage>0) {
	    	params.put("offset", (curPage - 1) * pageSize);
	    	params.put("limit", pageSize);
	    }
		return userDao.queryGroupStat(params);
	}
	
}
