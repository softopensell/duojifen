package com.platform.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.platform.dao.ApiBonusLevelAwardRuleMapper;
import com.platform.dao.ApiBonusParamMapper;
import com.platform.entity.BonusLevelAwardRuleVo;
import com.platform.entity.BonusParamVo;

@Component("bonusAppCache")
public class BonusAppCache {

	protected final static Log LOG = LogFactory.getLog(BonusAppCache.class);
	
	@Resource
	private BonusLevelAwardRuleCache bonusLevelAwardRuleCache;
	@Resource
	private  BonusParamCache bonusParamCache;
	
	@Resource
	private ApiBonusLevelAwardRuleMapper bonusLevelAwardRuleDao;

	@Resource
	private ApiBonusParamMapper bonusParamDao;
	
	@PostConstruct
	public void init() {
		LOG.info("Init BonusAppCache app cache start");
		initOrUpdateBonusParamCache();
		initOrUpdateBonusLevelAwardRuleCache();
		LOG.info("Init BonusAppCache app cache end");
	}
	
	private void initOrUpdateBonusParamCache() {
		Map<String, Object> map = new HashMap();
	    List<BonusParamVo> bonusParams= bonusParamDao.queryList(map);
		bonusParamCache.initOrUpdate(bonusParams);
	}
	private void initOrUpdateBonusLevelAwardRuleCache() {
		Map<String, Object> map = new HashMap();
		List<BonusLevelAwardRuleVo> bonusLevelAwardRules=bonusLevelAwardRuleDao.queryList(map);
		bonusLevelAwardRuleCache.initOrUpdate(bonusLevelAwardRules);
	}

	/**
	 * @return the bonusLevelAwardRuleCache
	 */
	public BonusLevelAwardRuleCache getBonusLevelAwardRuleCache() {
		return bonusLevelAwardRuleCache;
	}

	/**
	 * @param bonusLevelAwardRuleCache the bonusLevelAwardRuleCache to set
	 */
	public void setBonusLevelAwardRuleCache(BonusLevelAwardRuleCache bonusLevelAwardRuleCache) {
		this.bonusLevelAwardRuleCache = bonusLevelAwardRuleCache;
	}

	/**
	 * @return the bonusParamCache
	 */
	public BonusParamCache getBonusParamCache() {
		return bonusParamCache;
	}

	/**
	 * @param bonusParamCache the bonusParamCache to set
	 */
	public void setBonusParamCache(BonusParamCache bonusParamCache) {
		this.bonusParamCache = bonusParamCache;
	}

	/**
	 * @return the bonusLevelAwardRuleDao
	 */
	public ApiBonusLevelAwardRuleMapper getBonusLevelAwardRuleDao() {
		return bonusLevelAwardRuleDao;
	}

	/**
	 * @param bonusLevelAwardRuleDao the bonusLevelAwardRuleDao to set
	 */
	public void setBonusLevelAwardRuleDao(ApiBonusLevelAwardRuleMapper bonusLevelAwardRuleDao) {
		this.bonusLevelAwardRuleDao = bonusLevelAwardRuleDao;
	}

	/**
	 * @return the bonusParamDao
	 */
	public ApiBonusParamMapper getBonusParamDao() {
		return bonusParamDao;
	}

	/**
	 * @param bonusParamDao the bonusParamDao to set
	 */
	public void setBonusParamDao(ApiBonusParamMapper bonusParamDao) {
		this.bonusParamDao = bonusParamDao;
	}
	
	
}
