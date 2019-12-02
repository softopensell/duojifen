package com.platform.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.platform.entity.BonusLevelAwardRuleVo;

/**
 * BonusParamCache
 */
@Component
public class BonusLevelAwardRuleCache{

	public Map<Integer, BonusLevelAwardRuleVo> bonusLevelAwardRuleMap=new HashMap<>();
	public List<BonusLevelAwardRuleVo> bonusLevelAwardRules;
	/**
	 * 调用该方法可以初始化或者更新缓存，具体实现需保证线程安全
	 * <p>
	 *
	 * @param dataList
	 */
	void initOrUpdate(List<BonusLevelAwardRuleVo> tempBonusLevelAwardRules) {
		synchronized (this) {
			bonusLevelAwardRules=tempBonusLevelAwardRules;
			for(BonusLevelAwardRuleVo bonusLevelAwardRule:tempBonusLevelAwardRules) {
				bonusLevelAwardRuleMap.put(bonusLevelAwardRule.getBonusLevel(), bonusLevelAwardRule);
			}
		}
	}
	/**
	 * @return the bonusLevelAwardRuleMap
	 */
	public Map<Integer, BonusLevelAwardRuleVo> getBonusLevelAwardRuleMap() {
		return bonusLevelAwardRuleMap;
	}
	
	/**
	 * @return the bonusLevelAwardRules
	 */
	public List<BonusLevelAwardRuleVo> getBonusLevelAwardRules() {
		return bonusLevelAwardRules;
	}
	
	
	
}
