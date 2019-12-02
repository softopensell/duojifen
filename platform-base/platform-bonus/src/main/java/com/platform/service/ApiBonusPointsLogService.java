package com.platform.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platform.dao.ApiBonusPointsLogMapper;
import com.platform.entity.BonusPointsLogVo;

/**
 * Service
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-05-05 15:23:33
 */
@Service
public class ApiBonusPointsLogService {
    @Autowired
    private ApiBonusPointsLogMapper bonusPointsLogMapper;

    public BonusPointsLogVo queryObject(Long id) {
        return bonusPointsLogMapper.queryObject(id);
    }

    public List<BonusPointsLogVo> queryList(Map<String, Object> map) {
        return bonusPointsLogMapper.queryList(map);
    }

    public int queryTotal(Map<String, Object> map) {
        return bonusPointsLogMapper.queryTotal(map);
    }

    public int save(BonusPointsLogVo bonusPointsLog) {
        return bonusPointsLogMapper.save(bonusPointsLog);
    }

    public int update(BonusPointsLogVo bonusPointsLog) {
        return bonusPointsLogMapper.update(bonusPointsLog);
    }

    public int delete(Long id) {
        return bonusPointsLogMapper.delete(id);
    }

    public int deleteBatch(Long[] ids) {
        return bonusPointsLogMapper.deleteBatch(ids);
    }
}
