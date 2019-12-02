package com.platform.service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.platform.entity.SmsLogEntity;
import com.platform.entity.UserEntity;

/**
 * Service接口
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-29 10:07:38
 */
public interface UserService {

	/**
	 * 根据主键查询实体
	 *
	 * @param id 主键
	 * @return 实体
	 */
	UserEntity queryObject(Integer userId);
	UserEntity getById(Integer userId);

	UserEntity queryByUserName(String userName);

	/**
	 * 分页查询
	 *
	 * @param map 参数
	 * @return list
	 */
	List<UserEntity> queryList(Map<String, Object> map);

	/**
	 * 分页统计总数
	 *
	 * @param map 参数
	 * @return 总数
	 */
	int queryTotal(Map<String, Object> map);

	/**
	 * 保存实体
	 *
	 * @param user 实体
	 * @return 保存条数
	 */
	int save(UserEntity user);

	/**
	 * 根据主键更新实体
	 *
	 * @param user 实体
	 * @return 更新条数
	 */
	int update(UserEntity user);

	/**
	 * 根据主键删除
	 *
	 * @param userId
	 * @return 删除条数
	 */
	int delete(Integer userId);

	/**
	 * 根据主键批量删除
	 *
	 * @param userIds
	 * @return 删除条数
	 */
	int deleteBatch(Integer[] userIds);

	void updateIntegral(UserEntity user,int addIntegralScore);

	public int addUserBalanceAndTotalIncome(Integer userId, BigDecimal addBalance);
	/**
	 * 增加 钱包收入，增加总收益 ，增加投资总收益，减少杠杆收益
	 * @param userId
	 * @param addBalance
	 * @return
	 */
	public int addUserBalanceAndTotalIncomeAndReduceInverst(Integer userId, BigDecimal addBalance);
	/**
	 * 投资 增加总资产收益，增加购买额度综合
	 * @param userId
	 * @param investMoney
	 * @param totalIncomeMoney
	 * @return
	 */
	public int addInverst(Integer userId, BigDecimal investMoney,BigDecimal totalIncomeMoney);

	public List<UserEntity> queryByUserIds(Collection<Integer> userIds);

	public Map<Integer, UserEntity> getByUserIds(Collection<Integer> userIds);

	public UserEntity queryByMobile(String mobile);

	public Integer login(String mobile, String password);

	public SmsLogEntity querySmsCodeByUserId(Integer user_id);

	public SmsLogEntity querySmsCodeByPhone(String phone);

	public int saveSmsCodeLog(SmsLogEntity smsLogVo);

	public UserEntity queryByOpenId(String openId);

	/**
	 * 消费余额
	 * 
	 * @param userId
	 * @param addBalance
	 * @return
	 */
	public int reduceUserBalance(Integer userId, BigDecimal reduceBalance);

	/**
	 * 增加余额
	 * 
	 * @param userId
	 * @param addBalance
	 * @return
	 */
	public int addUserBalance(Integer userId, BigDecimal addBalance);

	/**
	 * 消费积分
	 * 
	 * @param userId
	 * @param addBalance
	 * @return
	 */
	public int reduceUserIntegralScore(Integer userId, Integer integralScore);

	/**
	 * 增加积分
	 * 
	 * @param userId
	 * @param addBalance
	 * @return
	 */
	public int addUserIntegralScore(Integer userId, Integer integralScore);
	public List<UserEntity> queryListToShareInvestUserByDate(Date today);
	 public List<UserEntity> queryAllList();
	
	public List<UserEntity> queryNodesByParentNodeName(String signupNodePhone);
	
	
	public int addUserFund(Integer userId, BigDecimal fund);
	
	/**
	 * 统计平台用户数据
	 * @return
	 */
	public Map<String,Object> queryTotalStat();
	/**
	 * 统计平台每日新增用户数据
	 * @param params
	 * @return
	 */
	public Map<String,Object> queryStatByDay(Date date);
	/**
	 * Group 统计平台数据
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> queryGroupStat(Integer curPage, Integer pageSize);
	
	
	
}
