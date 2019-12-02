package com.platform.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platform.dao.ApiUserInvitedChgLogMapper;
import com.platform.entity.UserInvitedChgLogVo;

/**
 * Service
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-05-17 17:09:31
 */
@Service
public class ApiUserInvitedChgLogService {
    @Autowired
    private ApiUserInvitedChgLogMapper userInvitedChgLogMapper;

    public UserInvitedChgLogVo queryObject(Integer userId) {
        return userInvitedChgLogMapper.queryObject(userId);
    }

    public List<UserInvitedChgLogVo> queryList(Map<String, Object> map) {
        return userInvitedChgLogMapper.queryList(map);
    }

    public int queryTotal(Map<String, Object> map) {
        return userInvitedChgLogMapper.queryTotal(map);
    }

    public int save(UserInvitedChgLogVo userInvitedChgLog) {
        return userInvitedChgLogMapper.save(userInvitedChgLog);
    }

    public int update(UserInvitedChgLogVo userInvitedChgLog) {
        return userInvitedChgLogMapper.update(userInvitedChgLog);
    }

    public int delete(Integer userId) {
        return userInvitedChgLogMapper.delete(userId);
    }

    public int deleteBatch(Integer[] userIds) {
        return userInvitedChgLogMapper.deleteBatch(userIds);
    }
}
