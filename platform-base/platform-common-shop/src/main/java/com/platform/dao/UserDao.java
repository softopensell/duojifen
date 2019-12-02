package com.platform.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import com.platform.entity.SmsLogEntity;
import com.platform.entity.UserEntity;

/**
 * Dao
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-29 10:07:38
 */
public interface UserDao extends BaseDao<UserEntity> {
	UserEntity getById(@Param("id") Integer id);
	UserEntity queryByUserName(@Param("userName") String userName);
	public List<UserEntity> queryByUserIds(@Param("userIds")Collection<Integer> userIds);
	public List<UserEntity> queryNodesByParentNodeName(@Param("signupNodePhone")String signupNodePhone);
	@MapKey("userId")
	public Map<Integer,UserEntity> getByUserIds(@Param("userIds")Collection<Integer> userIds);
	
	UserEntity queryByMobile(String mobile);

	UserEntity queryByOpenId(@Param("openId") String openId);
	
	Map<String,Object> queryTotalStat();
	Map<String,Object> queryStatByDay(Map<String,Object> params);
	List<Map<String,Object>> queryGroupStat(Map<String,Object> params);
	
	

	/**
	 * 获取用户最后一条短信
	 *
	 * @param user_id
	 * @return
	 */
	SmsLogEntity querySmsCodeByUserId(@Param("user_id") Integer user_id);

	SmsLogEntity querySmsCodeByPhone(@Param("phone") String phone);
	/**
	 * 保存短信
	 *
	 * @param smsLogVo
	 * @return
	 */
	int saveSmsCodeLog(SmsLogEntity smsLogVo);
}
