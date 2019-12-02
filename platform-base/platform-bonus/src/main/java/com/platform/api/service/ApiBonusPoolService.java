package com.platform.api.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platform.api.dao.ApiBonusPoolMapper;
import com.platform.api.entity.BonusPoolVo;

/**
 * Service
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-06-12 01:26:17
 */
@Service
public class ApiBonusPoolService {
    @Autowired
    private ApiBonusPoolMapper bonusPoolMapper;

    public BonusPoolVo queryObject(Integer id) {
        return bonusPoolMapper.queryObject(id);
    }

    public List<BonusPoolVo> queryList(Map<String, Object> map) {
        return bonusPoolMapper.queryList(map);
    }

    public int queryTotal(Map<String, Object> map) {
        return bonusPoolMapper.queryTotal(map);
    }

    public int save(BonusPoolVo bonusPool) {
        return bonusPoolMapper.save(bonusPool);
    }

    public int update(BonusPoolVo bonusPool) {
        return bonusPoolMapper.update(bonusPool);
    }

    public int delete(Integer id) {
        return bonusPoolMapper.delete(id);
    }

    public int deleteBatch(Integer[] ids) {
        return bonusPoolMapper.deleteBatch(ids);
    }
    public BonusPoolVo queryByPoolNumber(@Param("poolNumber")String poolNumber) {
    	return bonusPoolMapper.queryByPoolNumber(poolNumber);
    }
    public int incrBonusPoolMoney(String poolNumber,BigDecimal incrMoney) {
    	return bonusPoolMapper.incrBonusPoolMoney(poolNumber, incrMoney);
    }
}
