package com.platform.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platform.dao.ApiBonusLevelAwardRuleMapper;
import com.platform.entity.BonusLevelAwardRuleVo;

/**
 * 推荐奖励规则Service
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-29 10:46:58
 */
@Service
public class ApiBonusLevelAwardRuleService {
    @Autowired
    private ApiBonusLevelAwardRuleMapper bonusLevelAwardRuleMapper;

    public BonusLevelAwardRuleVo queryObject(Long id) {
        return bonusLevelAwardRuleMapper.queryObject(id);
    }

    public List<BonusLevelAwardRuleVo> queryList(Map<String, Object> map) {
        return bonusLevelAwardRuleMapper.queryList(map);
    }

    public int queryTotal(Map<String, Object> map) {
        return bonusLevelAwardRuleMapper.queryTotal(map);
    }

    public int save(BonusLevelAwardRuleVo bonusLevelAwardRule) {
        return bonusLevelAwardRuleMapper.save(bonusLevelAwardRule);
    }

    public int update(BonusLevelAwardRuleVo bonusLevelAwardRule) {
        return bonusLevelAwardRuleMapper.update(bonusLevelAwardRule);
    }

    public int delete(Long id) {
        return bonusLevelAwardRuleMapper.delete(id);
    }

    public int deleteBatch(Long[] ids) {
        return bonusLevelAwardRuleMapper.deleteBatch(ids);
    }
}
