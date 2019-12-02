package com.platform.dao;

import org.apache.ibatis.annotations.Param;

import com.platform.entity.SmsLogEntity;
import com.platform.entity.UserVo;

/**
 * Dao
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-26 00:11:50
 */
public interface ApiUserMapper extends BaseDao<UserVo> {
	     UserVo queryByMobile(String mobile);

	    UserVo queryByOpenId(@Param("openId") String openId);

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
