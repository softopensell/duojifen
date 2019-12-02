package com.platform.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import com.alibaba.druid.sql.dialect.mysql.ast.MysqlForeignKey.Match;
import com.platform.constants.BonusConstant;
import com.platform.dao.ApiBonusPointsMapper;
import com.platform.entity.BonusPointsVo;
import com.platform.page.Page;
import com.platform.utils.ObjToStringUtil;
import com.platform.utils.StringUtils;

/**
 * Service
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-29 10:46:58
 */
@Service
public class ApiBonusPointsService {
    protected Logger logger = Logger.getLogger(getClass());

    @Autowired
    private ApiBonusPointsMapper bonusPointsMapper;

    public BonusPointsVo queryObject(Long id) {
        return bonusPointsMapper.queryObject(id);
    }

    public List<BonusPointsVo> queryList(Map<String, Object> map) {
        return bonusPointsMapper.queryList(map);
    }

    public int queryTotal(Map<String, Object> map) {
        return bonusPointsMapper.queryTotal(map);
    }

    public int save(BonusPointsVo BonusPointsVos) {
        return bonusPointsMapper.save(BonusPointsVos);
    }

    public int update(BonusPointsVo BonusPointsVos) {
        return bonusPointsMapper.update(BonusPointsVos);
    }

    public int delete(Long id) {
        return bonusPointsMapper.delete(id);
    }

    public int deleteBatch(Long[] ids) {
        return bonusPointsMapper.deleteBatch(ids);
    }
    
    public BonusPointsVo getUesrPoint(Integer userId) {
        return bonusPointsMapper.getUesrPoint(userId,BonusConstant.BONUS_PARENT_TYPE_MORE_TREE);
    }
    public BonusPointsVo queryByUserIdAndBloodType(Integer userId,Integer bloodType) {
    	return bonusPointsMapper.queryByUserIdAndBloodType(userId, bloodType);
    }
    public List<BonusPointsVo> queryByParentUserId(Integer userId,Integer bloodType){
    	return bonusPointsMapper.queryByParentUserId(userId, bloodType);
    }
    
    /**
	 * 注册成营销会员
	 * @param UserId
	 * @param invitedUserberId 推荐人ID
	 * @param invitedPointUserId 经销商推荐人ID
	 * @return
	 */
	public BonusPointsVo initUserPoint(int userId,Integer invitedUserberId,Integer invitedPointUserId){

		logger.info("----initUserPoint---UserId------"+userId);
		logger.info("----initUserPoint---invitedUserberId------"+invitedUserberId);
		BonusPointsVo bonusPoint=getUserPoint(userId,BonusConstant.BONUS_PARENT_TYPE_MORE_TREE);
		if(bonusPoint==null) {
			bonusPoint=new BonusPointsVo();
			bonusPoint.setUserId(userId);
			bonusPoint.setBonusInvitedSum(0);//团队人数
			bonusPoint.setBonusMeInvitedPoints(new BigDecimal(0));//个人业绩 直接业绩
			bonusPoint.setBonusTeamInvitedPoints(new BigDecimal(0));//团队业绩
			bonusPoint.setCreateTime(new Date());
			//经理数量
		    bonusPoint.setBonusTeamRoleManagerSum(0);
		    //一星店长数量
		    bonusPoint.setBonusTeamRoleShopperOneSum(0);
		    //二星店长数量
		    bonusPoint.setBonusTeamRoleShopperTwoSum(0);
		    //三星店长数量
		    bonusPoint.setBonusTeamRoleShopperThreeSum(0);
		    //四星店长数量
		    bonusPoint.setBonusTeamRoleShopperFourSum(0);
			 
			
			if(invitedUserberId!=null&&invitedUserberId>0) {
				//一起赚推荐关系
				bonusPoint.setInvitedUserId(invitedUserberId);
				int tempInviteUserId=invitedUserberId;
				BonusPointsVo tempInviteParentPointVo=getUserPoint(tempInviteUserId,BonusConstant.BONUS_PARENT_TYPE_MORE_TREE);//查询推荐人信息
				bonusPoint.setInvitedParentUserIds("");//推荐关系
				if(tempInviteParentPointVo!=null) {
					String invitedParentUserIds=""+tempInviteUserId+BonusConstant.INVITED_MEMBER_IDS_SPLIT+tempInviteParentPointVo.getInvitedParentUserIds();
					bonusPoint.setInvitedParentUserIds(invitedParentUserIds);//所有父类节点
				}
				
			}
			if(invitedPointUserId!=null&&invitedPointUserId>0) {
				//经销商推荐关系
				bonusPoint.setInvitedPointUserId(invitedPointUserId);
				int tempInvitedPointUserId=invitedPointUserId;
				BonusPointsVo tempInvitePointParentPointVo=getUserPoint(tempInvitedPointUserId,BonusConstant.BONUS_PARENT_TYPE_MORE_TREE);//查询推荐人信息
				bonusPoint.setInvitedPointUserIds("");//推荐关系
				if(tempInvitePointParentPointVo!=null) {
					String invitedPointParentUserIds=""+tempInvitedPointUserId;
					if(tempInvitePointParentPointVo.getInvitedPointUserIds()!=null){
						invitedPointParentUserIds=invitedPointParentUserIds+BonusConstant.INVITED_MEMBER_IDS_SPLIT+tempInvitePointParentPointVo.getInvitedPointUserIds();
					}
					bonusPoint.setInvitedPointUserIds(invitedPointParentUserIds);//所有父类节点
				}
			}else {
				bonusPoint.setInvitedUserId(0);
				bonusPoint.setInvitedPointUserId(0);
				bonusPoint.setInvitedPointUserIds("");//
				bonusPoint.setInvitedParentUserIds("");//如果无推荐 
			}
			bonusPoint.setUserRoleType(0);
			bonusPoint.setUserNamedType(0);
			bonusPoint.setUpdateTime(new Date());
			bonusPoint.setStatus(0);
			bonusPoint.setCanGenerateQr("0");//默认不能生成推广二维码
			
			logger.info("-------------找到叶子节点---最终存储的数据为------------"+ObjToStringUtil.objToString(bonusPoint)); 
			bonusPointsMapper.save(bonusPoint);
			
			//统计邀请人数据
			String invitedPointParentUserIds=bonusPoint.getInvitedParentUserIds();
			List<String> nodeStrUserIds=StringUtils.splitToList(invitedPointParentUserIds, BonusConstant.INVITED_MEMBER_IDS_SPLIT);
			List<Integer> nodeUserIds=new ArrayList<>();
			for(String str:nodeStrUserIds) {
				if(Integer.valueOf(str)>0)nodeUserIds.add(Integer.valueOf(str));
			}
			bonusPointsMapper.addBonusTeamInvitedSum(nodeUserIds, 1);
		}
		
		return bonusPoint;
	
	}
	/**
	 * 查询该用户的叶子节点
	 * @param UserId
	 * @return
	 */
	public BonusPointsVo findLeafNodeBy(int UserId) {
		BonusPointsVo subBonusPoint=null;
		
		int tempUserId=UserId;
		
		boolean flag=true;
		
		while(flag) {
			
			subBonusPoint=findBySubBonusPointOrderByCreateDate(tempUserId);
			if(subBonusPoint!=null) {
				tempUserId=subBonusPoint.getUserId();//
				if(subBonusPoint.getBonusInvitedSum()<=0) {
					flag=false;
					return subBonusPoint;
				}else {
				}
			}else {
			  return null;
			}
			
		}
		return null;
	}
	
	/**
	 * 查询管理关系子节点 第一个子节点
	 * @param UserId
	 * @return
	 */
	public BonusPointsVo findBySubBonusPointOrderByCreateDate(int UserId){
		/*List<Match> matches=new ArrayList<Match>();
		matches.add(new Match(BonusPointColumns.invitedPointUserId, UserId));
		List<Order> orders=new ArrayList<>();
		orders.add(new Order(BonusPointColumns.createDate, true));
		List<BonusPointsVo> bonusPoints=this.bonusPointsMapper.findBonusPoints(matches, orders, 0,2).getList();
		if(bonusPoints!=null&&bonusPoints.size()>0) {
			return bonusPoints.get(0);
		}*/
		return null;
	}

	/**
	 * 获取用户的账号情况
	 * @param UserId
	 * @return
	 */
	public BonusPointsVo getUserPoint(int userId,Integer bloodType){
		return bonusPointsMapper.getUesrPoint(userId,bloodType);
	}
	/**
	 * 获取用户的账号情况
	 * @param userIds
	 * @return
	 */
	public List<BonusPointsVo> getUserPointByUserIds(Collection<Integer> userIds){
		return bonusPointsMapper.getUserPointByUserIds(userIds);
	}
	
	/**
	 * 增加团队人数
	 * @param UserId
	 * @param addSum 1 
	 * @return
	 */
	public int addBonusInvitedSum(int UserId,int addSum){
		return 0;
	}
	/**
	 * 批量增加团队人数
	 * @param userIds
	 * @param addPoint
	 * @return
	 */
	public int addBonusInvitedSum( Collection<Integer> userIds,int addSum) {
		/*
		List<Match> matches=new ArrayList<Match>();
		matches.add(new Match(BonusPointColumns.userId, new Match.InParam(userIds)));
		List<Modify> modifies = new ArrayList<Modify>();
		modifies.add(new Modify(BonusPointColumns.bonusInvitedSum, new Match.addParam(addSum)));
		return bonusPointDao.updatePoint(modifies, matches);
		*/
		return 0 ;
	}
	
	
	/**
	 * 修改用户的职称 或 称呼
	 * @param UserId
	 * @param userRoleType 职位角色：0 普通，1经理，2 一星店长，3二星店长，4三星店长
	 * @param userMamedType 称呼:0 普通会员，1 分公司，2 代理商，3 分销商
	 * @return
	 */
	public int  updateUserRole(int UserId,Integer userRoleType ,Integer userMamedType){
		logger.info("---------修改用户的职称 或 称呼----bonusPoint:---UserId-----："+UserId);
		logger.info("---------修改用户的职称 或 称呼----bonusPoint:---userRoleType-----："+userRoleType);
		logger.info("---------修改用户的职称 或 称呼----bonusPoint:---userMamedType-----："+userMamedType);
		BonusPointsVo userPoint = getUserPoint(UserId,BonusConstant.BONUS_PARENT_TYPE_MORE_TREE);
		if(userRoleType!=null){
			userPoint.setUserRoleType(userRoleType);
			if(userRoleType.equals(BonusConstant.BONUS_MEMBER_ROLE_TYPE_shopper_two)){
				userPoint.setInvitedUserId(0);
				userPoint.setInvitedParentUserIds("");
			}
			
		}
		
		if(userMamedType!=null){
			userPoint.setUserNamedType(userMamedType);
		}
		
		logger.info("---------修改用户的职称 或 称呼----bonusPoint:---最终需要修改的数据-----："+ObjToStringUtil.objToString(userPoint));
		return bonusPointsMapper.update(userPoint);
	}
	/**
	 * 3二星店长，4三星店长 5四星店长
	 * @return
	 */
	public int countByUserRoleType(int userRoleType){
		if(userRoleType >0){
			Map<String, Object> map = new HashMap();
			map.put("userRoleType", userRoleType);
			return bonusPointsMapper.queryTotal(map);
		}
		return 0;
	}
	/**
	 * 获取角色
	 * @param userRoles
	 * @return
	 */
	public List<BonusPointsVo> findByUserRoleType(List<Integer> userRoles){
		if(userRoles!=null){
			return bonusPointsMapper.findByUserRoleType(userRoles);
		}
		return null;
	}
	/**
	 * 获取推荐用户
	 * @param matchs
	 * @param orders
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public Page<BonusPointsVo> findBonusPointsVos(List<Match> matchs,List<Order> orders,Integer curPage,Integer pageSize){
		return null;
	}
	/**
	 * 查询一级代理
	 * @param UserId
	 * @return
	 */
	public List<BonusPointsVo> findBySubBonusPoint(int UserId,int bloodType){
		Map<String, Object> paramsmap = new HashMap();
		paramsmap.put("invitedUserId", UserId);
		paramsmap.put("bloodType", bloodType);
		return this.queryList(paramsmap);
	}
	/**
	 * 查询所有下一级、包含自己的 所有子集
	 * @param UserId
	 * @return
	 */
	public List<BonusPointsVo> findSubsByParentUserIds(int ParentUserId,int bloodType){
		Map<String, Object> paramsmap = new HashMap();
		String ParentUserIdStr=BonusConstant.INVITED_MEMBER_IDS_SPLIT+ParentUserId+BonusConstant.INVITED_MEMBER_IDS_SPLIT;
		paramsmap.put("invitedParentUserIds", ParentUserIdStr);
		paramsmap.put("bloodType", bloodType);
		return this.queryList(paramsmap);
	}
	/**
	 * 查询下级的经理、店长数量
	 * @param invitedUserId  上级推荐人ID
	 * @param userRoleType  需要重新的角色数
	 * @return
	 */
	public int findSubTeamRoleSum(int invitedUserId,Integer userRoleType ){
		if(invitedUserId>0&&userRoleType>0){
			try {
				Map<String, Object> paramsmap = new HashMap();
				paramsmap.put("invitedUserId", invitedUserId);
				paramsmap.put("userRoleType", userRoleType);
			return bonusPointsMapper.queryTotal(paramsmap);
			} catch (Exception e) {
				e.printStackTrace();
				return 0;
			}
		}else{
			return 0;
		}
	}
	
	/**
	 * 查询下级的经理、店长数量
	 * @param invitedUserId  上级推荐人ID
	 * @param userRoleTypes  角色集合数
	 * @return
	 */
	public int findSubTeamRoleSum(int invitedUserId,Collection<Integer>   userRoleTypes ){
			if(invitedUserId>0&&userRoleTypes.size()>0){
				try {
					logger.info("增加个查询下级的经理、店长数量");
				Map<String, Object> paramsmap = new HashMap();
				paramsmap.put("invitedUserId", invitedUserId);
				paramsmap.put("userRoleTypes", userRoleTypes);
				return bonusPointsMapper.queryTotal(paramsmap);
			} catch (Exception e) {
				e.printStackTrace();
				return 0;
			}
			}else{
				return 0;
			}
	}
	
	
	/**
	 * 增加个人消费额度
	 * @param UserId
	 * @param addPoint
	 * @return
	 */
	public int addBonusMeInvitedPoints(int UserId,BigDecimal addPoint){
		logger.info("增加个人【"+UserId+"】消费额度["+addPoint.toString()+"]");
		BonusPointsVo uesrPoint = queryByUserIdAndBloodType(UserId,BonusConstant.BONUS_POINT_BLOODTYPE_BINARYTREE);
		BigDecimal bonusMeInvitedPoints = uesrPoint.getBonusMeInvitedPoints();
		BigDecimal tmp_bonusMeInvitedPoints = bonusMeInvitedPoints.add(addPoint);
		uesrPoint.setBonusMeInvitedPoints(tmp_bonusMeInvitedPoints);
		logger.info("更新后的uesrPoint："+ObjToStringUtil.objToString(uesrPoint));
		return bonusPointsMapper.update(uesrPoint);
	}
	
	
	/**
	 * 增加团队消费额度
	 * @param UserId
	 * @param addPoint
	 * @return
	 */
	public int addBonusTeamInvitedPoints(int UserId,BigDecimal addPoint){
		logger.info("增加团队推荐人【"+UserId+"】消费额度["+addPoint.toString()+"]");
		BonusPointsVo uesrPoint = getUesrPoint(UserId);
		BigDecimal bonusTeamInvitedPoints = uesrPoint.getBonusTeamInvitedPoints();
		BigDecimal tmp_bonusTeamInvitedPoints = bonusTeamInvitedPoints.add(addPoint);
		uesrPoint.setBonusTeamInvitedPoints(tmp_bonusTeamInvitedPoints);
		return bonusPointsMapper.update(uesrPoint);
	}
	
	/**
	 * 批量增加团队消费额
	 * @param userIds
	 * @param addPoint
	 * @return
	 */
	public int addBonusTeamInvitedPoints( Collection<Integer> userIds,BigDecimal addPoint){
		return  bonusPointsMapper.addBonusTeamInvitedPoints(userIds,addPoint);
	}
	public int addBonusTeamInvitedSum( Collection<Integer> userIds,Integer incrSum){
		return  bonusPointsMapper.addBonusTeamInvitedSum(userIds, incrSum);
	}
	
	/**
	 * 批量增加团队消费额
	 * @param userIds
	 * @param addPoint
	 * @return
	 */
	public int reduceBonusTeamInvitedPoints( Collection<Integer> userIds,BigDecimal addPoint){
		return  bonusPointsMapper.reduceBonusTeamInvitedPoints(userIds,addPoint);
	}
	public int reduceBonusTeamInvitedSum( Collection<Integer> userIds,Integer incrSum){
		return  bonusPointsMapper.reduceBonusTeamInvitedSum(userIds, incrSum);
	}
	
	
	/**
	 * 批量增加 经理、店长
	 * @param userId
	 * @param addManager
	 * @param addShopperOne
	 * @param addShopperTwo
	 * @param addShopperThree
	 * @param addShopperFour
	 * @return
	 */
	public int addBonusTeamRoleSum( int userId,int addManager,int addShopperOne,int addShopperTwo,int addShopperThree,int addShopperFour){
		if(userId ==0){
			return 0;
		}
		BonusPointsVo uesrPoint = getUesrPoint(userId);
		if(uesrPoint!=null){
			if(addManager >0) uesrPoint.setBonusTeamRoleManagerSum(uesrPoint.getBonusTeamRoleManagerSum()+addManager);
			if(addShopperOne >0) uesrPoint.setBonusTeamRoleShopperOneSum(uesrPoint.getBonusTeamRoleShopperOneSum()+addShopperOne);
			if(addShopperTwo >0) uesrPoint.setBonusTeamRoleShopperTwoSum(uesrPoint.getBonusTeamRoleShopperTwoSum()+addShopperTwo);
			if(addShopperThree >0) uesrPoint.setBonusTeamRoleShopperThreeSum(uesrPoint.getBonusTeamRoleShopperThreeSum()+addShopperThree);
			if(addShopperFour >0) uesrPoint.setBonusTeamRoleShopperFourSum(uesrPoint.getBonusTeamRoleShopperFourSum()+addShopperFour);
			logger.info("批量增加 经理userId【"+userId+"】更新后的uesrPoint："+ObjToStringUtil.objToString(uesrPoint));
			return bonusPointsMapper.update(uesrPoint);
		}else{
			return 0;
		}
	}
	
	/**
	 * 根据管理上级节点ID，查询下面业绩最大的部门
	 * @param UserId
	 * @return
	 */
	public BonusPointsVo findSubMaxTeamBonusPointsVo(int UserId){
		return null;
	}
	
	//修改推荐奖励参数
	/*public int updateBonusLevelAwardRule(BonusForm bonusForm){
		return 0;
	}

	//修改管理奖励参数
	public int updateBonusParam(BonusForm bonusForm){
		return 0;
	}

	//查询所有的推荐奖励
	public List<BonusLevelAwardRule> findBonusLevelAwardRules(){
		return null;
	}

	//查询所有的管理奖励
	public List<BonusParam> findBonusParams(){
		return null;
	}

	//设置是否生成二维码
	public int setGenerateQr(String canQr,Integer userid){
		return 0;
	}*/
}
