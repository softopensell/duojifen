package com.platform.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platform.dao.ApiUserAuditMapper;
import com.platform.entity.UserAuditVo;

/**
 * Service
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-05-31 12:02:47
 */
@Service
public class ApiUserAuditService {
    @Autowired
    private ApiUserAuditMapper userAuditMapper;

    public UserAuditVo queryObject(Integer id) {
        return userAuditMapper.queryObject(id);
    }

    public List<UserAuditVo> queryList(Map<String, Object> map) {
        return userAuditMapper.queryList(map);
    }

    public int queryTotal(Map<String, Object> map) {
        return userAuditMapper.queryTotal(map);
    }

    public int save(UserAuditVo userAudit) {
        return userAuditMapper.save(userAudit);
    }

    public int update(UserAuditVo userAudit) {
        return userAuditMapper.update(userAudit);
    }

    public int delete(Integer id) {
        return userAuditMapper.delete(id);
    }

    public int deleteBatch(Integer[] ids) {
        return userAuditMapper.deleteBatch(ids);
    }
}
