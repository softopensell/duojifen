package com.platform.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platform.dao.UserAuditDao;
import com.platform.entity.UserAuditEntity;
import com.platform.service.UserAuditService;
import com.platform.utils.ShiroUtils;

/**
 * Service实现类
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-05-30 15:38:31
 */
@Service("userAuditService")
public class UserAuditServiceImpl implements UserAuditService {
    @Autowired
    private UserAuditDao userAuditDao;

    @Override
    public UserAuditEntity queryObject(Integer id) {
        return userAuditDao.queryObject(id);
    }

    @Override
    public List<UserAuditEntity> queryList(Map<String, Object> map) {
        return userAuditDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return userAuditDao.queryTotal(map);
    }

    @Override
    public int save(UserAuditEntity userAudit) {
        return userAuditDao.save(userAudit);
    }

    @Override
    public int update(UserAuditEntity userAudit) {
        return userAuditDao.update(userAudit);
    }

    @Override
    public int delete(Integer id) {
        return userAuditDao.delete(id);
    }

    @Override
    public int deleteBatch(Integer[] ids) {
        return userAuditDao.deleteBatch(ids);
    }

	@Override
	public void saveAudit(UserAuditEntity userAudit) {
		UserAuditEntity quserAudit = userAuditDao.queryObject(userAudit.getId());
    	if(quserAudit!=null) {
    		quserAudit.setAuditStatus(userAudit.getAuditStatus());
    		quserAudit.setAuditTime(new Date());
    		quserAudit.setAuditUserId(ShiroUtils.getUserEntity().getUserId().intValue());
    		quserAudit.setPayAmount(userAudit.getPayAmount());
    		quserAudit.setAuditOpinion(userAudit.getAuditOpinion());
    		userAuditDao.update(quserAudit);
    		if(userAudit.getAuditStatus().intValue()==2) {
    			Map<String,Object> queryMap = new HashMap<String,Object>();
    			queryMap.put("userId", quserAudit.getApplyUserId());
//    			List<BonusPointsEntity> bonusPointsList = bonusPointsDao.queryList(queryMap);
//    			if(bonusPointsList!=null&&bonusPointsList.size()>0) {
//    				BonusPointsEntity bonusPoints = bonusPointsList.get(0);
//    				bonusPoints.setUserNamedType(quserAudit.getApplyLevel());
//    				bonusPoints.setUpdateTime(new Date());
//    				bonusPointsDao.update(bonusPoints);
//    			}
    		}
    	}
	}
}
