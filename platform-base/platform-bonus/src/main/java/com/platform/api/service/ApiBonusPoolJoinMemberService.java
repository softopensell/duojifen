package com.platform.api.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platform.api.dao.ApiBonusPoolJoinMemberMapper;
import com.platform.api.entity.BonusPoolJoinMemberVo;
import com.platform.constants.BonusConstant;

/**
 * Service
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-06-12 01:26:17
 */
@Service
public class ApiBonusPoolJoinMemberService {
    @Autowired
    private ApiBonusPoolJoinMemberMapper apiBonusPoolJoinMemberMapper;

    public BonusPoolJoinMemberVo queryObject(Integer id) {
        return apiBonusPoolJoinMemberMapper.queryObject(id);
    }
    
    /**
     * 添加日奖励
     * @param poolDateNumber
     * @param poolJoinMemberId
     * @param incrMoney
     * @return
     */
    public BonusPoolJoinMemberVo incrBonusPoolJoinMemberMoney(String poolDateNumber,Integer poolJoinMemberId,BigDecimal incrMoney) {
    	BonusPoolJoinMemberVo bonusPoolJoinMemberVo=apiBonusPoolJoinMemberMapper.queryByDateNumberAndMemberId(poolDateNumber, poolJoinMemberId);
    	if(bonusPoolJoinMemberVo!=null) {
    		apiBonusPoolJoinMemberMapper.incrBonusPoolJoinMemberMoney(poolDateNumber, poolJoinMemberId, incrMoney);
    	}else {
    		bonusPoolJoinMemberVo=new BonusPoolJoinMemberVo();
    		bonusPoolJoinMemberVo.setCreatetime(new Date());
    		bonusPoolJoinMemberVo.setPoolDateNumber(poolDateNumber);
    		bonusPoolJoinMemberVo.setPoolJoinMemberId(poolJoinMemberId);
    		bonusPoolJoinMemberVo.setPoolJoinMoney(incrMoney);
    		bonusPoolJoinMemberVo.setPoolJoinType(BonusConstant.BONUS_POOL_JOIN_TYPE_MEMBER);
    		bonusPoolJoinMemberVo.setPoolJoinSum(1);
    		bonusPoolJoinMemberVo.setStatus(0);
    		int id=apiBonusPoolJoinMemberMapper.save(bonusPoolJoinMemberVo);
    		bonusPoolJoinMemberVo=apiBonusPoolJoinMemberMapper.queryObject(id);	
    	}
    	return bonusPoolJoinMemberVo;
    }
    public BonusPoolJoinMemberVo queryByDateNumberAndMemberId(String poolDateNumber,Integer poolJoinMemberId) {
    	BonusPoolJoinMemberVo bonusPoolJoinMemberVo=apiBonusPoolJoinMemberMapper.queryByDateNumberAndMemberId(poolDateNumber, poolJoinMemberId);
    	return bonusPoolJoinMemberVo;
    }

    public List<BonusPoolJoinMemberVo> queryList(Map<String, Object> map) {
        return apiBonusPoolJoinMemberMapper.queryList(map);
    }

    public int queryTotal(Map<String, Object> map) {
        return apiBonusPoolJoinMemberMapper.queryTotal(map);
    }

    public int save(BonusPoolJoinMemberVo bonusPoolJoinMember) {
        return apiBonusPoolJoinMemberMapper.save(bonusPoolJoinMember);
    }

    public int update(BonusPoolJoinMemberVo bonusPoolJoinMember) {
        return apiBonusPoolJoinMemberMapper.update(bonusPoolJoinMember);
    }

    public int delete(Integer id) {
        return apiBonusPoolJoinMemberMapper.delete(id);
    }

    public int deleteBatch(Integer[] ids) {
        return apiBonusPoolJoinMemberMapper.deleteBatch(ids);
    }
}
