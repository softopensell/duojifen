package com.platform.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.platform.entity.BonusParamVo;


/**
 * BonusParamCache
 */
@Component
public class BonusParamCache{

	public Map<String, String> bonusParamMap=new HashMap<>();
	/**
	 * 调用该方法可以初始化或者更新缓存，具体实现需保证线程安全
	 * <p>
	 *
	 * @param dataList
	 */
	void initOrUpdate(List<BonusParamVo> bonusParams) {
		synchronized (this) {
			
			for(BonusParamVo  bonusParam:bonusParams) {
				bonusParamMap.put(bonusParam.getParamKey(), bonusParam.getParamValue());
			}
		}
	}
	/**
	 * @return the bonusParamMap
	 */
	public Map<String, String> getBonusParamMap() {
		return bonusParamMap;
	}
	
}
