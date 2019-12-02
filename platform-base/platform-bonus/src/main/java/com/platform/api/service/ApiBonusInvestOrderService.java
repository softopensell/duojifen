package com.platform.api.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platform.api.dao.ApiBonusInvestOrderMapper;
import com.platform.api.entity.BonusInvestOrderVo;
import com.platform.constants.BonusConstant;
import com.platform.utils.DateUtils;

/**
 * Service
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-06-12 01:26:17
 */
@Service
public class ApiBonusInvestOrderService {
    @Autowired
    private ApiBonusInvestOrderMapper bonusInvestOrderMapper;

    public BonusInvestOrderVo queryObject(Integer id) {
        return bonusInvestOrderMapper.queryObject(id);
    }

    public List<BonusInvestOrderVo> queryList(Map<String, Object> map) {
        return bonusInvestOrderMapper.queryList(map);
    }
    /**
     * 根据时间查找小于该天需要返利的订单
     * @param today
     * @return
     */
    public List<BonusInvestOrderVo> queryListToShareInvestOrdersByDate(Date today) {
    	Map<String, Object> map=new HashMap<>();
    	map.put("payStatus", BonusConstant.PAY_STATUS_PAYOK);//支付成功
    	map.put("statu", BonusConstant.BONUS_INVEST_ORDER_STATU_OK);//正常的订单
    	
//    	map.put("shareLastTimeStart", BonusConstant.PAY_STATUS_PAYOK);//起始时间 >
    	
    	today=DateUtils.getStartOfDate(today);
    	map.put("shareLastTimeEnd", DateUtils.format(today));//结束时间 <
    	 
    	
//    	<if test="shareLastTimeStart != null and shareLastTimeStart != ''">
//		AND go.share_last_time <![CDATA[ >= ]]> #{shareLastTimeStart}
//	</if>
//	<if test="shareLastTimeEnd != null and shareLastTimeEnd != ''">
//		AND go.share_last_time <![CDATA[ <= ]]> #{shareLastTimeEnd}
//	</if>
    	
    	return bonusInvestOrderMapper.queryList(map);
    }

    public int queryTotal(Map<String, Object> map) {
        return bonusInvestOrderMapper.queryTotal(map);
    }

    public int save(BonusInvestOrderVo bonusInvestOrder) {
        return bonusInvestOrderMapper.save(bonusInvestOrder);
    }

    public int update(BonusInvestOrderVo bonusInvestOrder) {
        return bonusInvestOrderMapper.update(bonusInvestOrder);
    }

    public int delete(Integer id) {
        return bonusInvestOrderMapper.delete(id);
    }

    public int deleteBatch(Integer[] ids) {
        return bonusInvestOrderMapper.deleteBatch(ids);
    }
}
