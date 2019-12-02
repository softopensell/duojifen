package com.platform.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platform.dao.ApiBonusParamMapper;
import com.platform.entity.BonusParamVo;

/**
 * Service
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-05-05 23:13:21
 */
@Service
public class ApiBonusParamService {
    @Autowired
    private ApiBonusParamMapper bonusParamMapper;

    public BonusParamVo queryObject(Long id) {
        return bonusParamMapper.queryObject(id);
    }

    public List<BonusParamVo> queryList(Map<String, Object> map) {
        return bonusParamMapper.queryList(map);
    }

    public int queryTotal(Map<String, Object> map) {
        return bonusParamMapper.queryTotal(map);
    }

    public int save(BonusParamVo bonusParam) {
        return bonusParamMapper.save(bonusParam);
    }

    public int update(BonusParamVo bonusParam) {
        return bonusParamMapper.update(bonusParam);
    }

    public int delete(Long id) {
        return bonusParamMapper.delete(id);
    }

    public int deleteBatch(Long[] ids) {
        return bonusParamMapper.deleteBatch(ids);
    }
}
