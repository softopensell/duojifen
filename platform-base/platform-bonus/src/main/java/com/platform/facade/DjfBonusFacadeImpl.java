package com.platform.facade;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.platform.api.entity.BonusInvestOrderVo;
import com.platform.api.entity.BonusPoolJoinMemberVo;
import com.platform.api.service.ApiBonusInvestOrderService;
import com.platform.api.service.ApiBonusPoolJoinMemberService;
import com.platform.cache.BonusAppCache;
import com.platform.cache.UserBlackCacheUtil;
import com.platform.constants.BonusConstant;
import com.platform.constants.PluginConstant;
import com.platform.entity.BonusPointsVo;
import com.platform.entity.GoodsOrderEntity;
import com.platform.entity.PlatformFwManagerEntity;
import com.platform.entity.UserBlackEntity;
import com.platform.entity.UserEntity;
import com.platform.entity.UserInvestLevelEntity;
import com.platform.mq.model.BonusTaskVo;
import com.platform.mq.model.PaymentTask;
import com.platform.mq.producer.TaskPaymentProducer;
import com.platform.service.ApiBonusPointsService;
import com.platform.service.ApiUserInvitedChgLogService;
import com.platform.service.GoodsOrderService;
import com.platform.service.PlatformFwManagerService;
import com.platform.service.SysConfigService;
import com.platform.service.UserInvestLevelService;
import com.platform.service.UserService;
import com.platform.util.ShopConstant;
import com.platform.util.constants.OrderConstant;
import com.platform.utils.CommonUtil;
import com.platform.utils.DateUtils;
import com.platform.utils.JsonUtil;
import com.platform.utils.MoneyFormatUtils;
import com.platform.utils.R;
import com.platform.utils.RandomUtil;
import com.platform.utils.StringUtils;

@Component
public class DjfBonusFacadeImpl  implements DjfBonusFacade {
	private static final Logger LOG = LoggerFactory
			.getLogger(DjfBonusFacadeImpl.class);
	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Resource
	private ApiUserInvitedChgLogService userInvitedChgLogService;
	@Resource
	private BonusAppCache bonusAppCache;
	@Autowired
	private ApiBonusInvestOrderService apiBonusInvestOrderService;
	 @Autowired
	 private UserService userService;
	 @Autowired
	 private ApiBonusPointsService bonusPointsService;
	 
	 @Autowired
	 private SysConfigService sysConfigService;
	 
	 @Autowired
	 private ApiBonusPoolJoinMemberService apiBonusPoolJoinMemberService;
	 @Autowired
	 private UserInvestLevelService userInvestLevelService ;
	 
	 @Autowired
	 private GoodsOrderService goodsOrderService ;
	 @Autowired
	 private PlatformFwManagerService platformFwManagerService ;
	 /**
	  * 查询用户附近的服务区
	  */
	 public PlatformFwManagerEntity getLastFwUserId(int userId) {
		//查询附近服务中心
     	//统计服务中心业绩
     	HashMap<Integer, PlatformFwManagerEntity> pHashMap=platformFwManagerService.queryAll();
     	Integer lastUserId=0;
     	//判断一下自己是服务中心
     	if(pHashMap.containsKey(userId)) {
     		lastUserId=userId;
     	}else {
     		BonusPointsVo bonusPoint=bonusPointsService.getUserPoint(userId,BonusConstant.BONUS_PARENT_TYPE_BINARY_TREE);
     		if(bonusPoint==null) return null;
     		//获取父类推荐关系集合
     		List<String> strUserIds=StringUtils.splitToList(bonusPoint.getInvitedParentUserIds(), BonusConstant.INVITED_MEMBER_IDS_SPLIT);
     		if(strUserIds.size()==0)return null;
     		for(String str:strUserIds) {
     			//查找最近的服务中心
     			if(pHashMap.containsKey(Integer.valueOf(str))) {
     				lastUserId=Integer.valueOf(str);
     				 break;
     			}
     		}
     	}
	   logger.info("--------最近的服务中心--"+JsonUtil.getJsonByObj(lastUserId));
	   if(lastUserId>0) {
		   return pHashMap.get(lastUserId);
	   }
	   return null;
	 }
	 
	 /**
	 * 断掉节点，清除父节点业绩数据 ，推荐关系id=0,节点关系为0 生成一级子链
	 * @param userId
	 */
	public R brokeNodeByUserId(int userId) {
		UserEntity user=userService.queryObject(userId);
		if(user==null) return R.error("查不到该用户");
		logger.info("--------注册用户数据-------user--"+JsonUtil.getJsonByObj(user)); 
		//获取二叉树推荐人
		BonusPointsVo nodeBonusPoint=bonusPointsService.queryByUserIdAndBloodType(userId, BonusConstant.BONUS_POINT_BLOODTYPE_BINARYTREE);
		if(nodeBonusPoint==null) return R.error("不存在层级节点关系");
		//推荐人树
		BonusPointsVo recommondBonusPoint=bonusPointsService.queryByUserIdAndBloodType(userId, BonusConstant.BONUS_POINT_BLOODTYPE_RECOMMD);
		if(recommondBonusPoint==null) return R.error("不存在推荐关系");
		//清楚推荐关系数据 
		recommondBonusPoint.setInvitedUserId(0);
		String recommondInvitedParentUserIds=recommondBonusPoint.getInvitedParentUserIds();
		recommondBonusPoint.setInvitedParentUserIds(BonusConstant.INVITED_MEMBER_IDS_SPLIT+"0");
		//获取父类推荐关系集合
		List<String> strUserIds=StringUtils.splitToList(recommondInvitedParentUserIds, BonusConstant.INVITED_MEMBER_IDS_SPLIT);
		List<Integer> recommondUserIds=new ArrayList<>();
		for(String str:strUserIds) {
			if(Integer.valueOf(str)>0)recommondUserIds.add(Integer.valueOf(str));
		}
		LOG.info("----------------------------recommondUserIds:"+recommondUserIds);
		//业绩需要取消
		//取消子几点推荐关系
		resetRecommond(recommondBonusPoint);
		bonusPointsService.update(recommondBonusPoint);
		
		//断节点
		nodeBonusPoint.setInvitedUserId(0);
		nodeBonusPoint.setInvitedRightUserId(0);
		String nodeInvitedParentUserIds=nodeBonusPoint.getInvitedParentUserIds();
		nodeBonusPoint.setInvitedParentUserIds(BonusConstant.INVITED_MEMBER_IDS_SPLIT+"0");
		//获取父类推荐关系集合
		List<String> nodeStrUserIds=StringUtils.splitToList(nodeInvitedParentUserIds, BonusConstant.INVITED_MEMBER_IDS_SPLIT);
		List<Integer> nodeUserIds=new ArrayList<>();
		for(String str:nodeStrUserIds) {
			if(Integer.valueOf(str)>0)nodeUserIds.add(Integer.valueOf(str));
		}
		LOG.info("----------------------------nodeUserIds:"+nodeUserIds);
		//清楚父类业绩
		BigDecimal addPoint=nodeBonusPoint.getBonusMeInvitedPoints().add(nodeBonusPoint.getBonusTeamInvitedPoints());
		if(nodeUserIds.size()>0)bonusPointsService.reduceBonusTeamInvitedPoints(nodeUserIds, addPoint);
		resetNode(nodeBonusPoint);
		bonusPointsService.update(nodeBonusPoint);
		return R.ok();
	}
	
	
	private void resetRecommond(BonusPointsVo parentBonusPointsVo) {
		List<BonusPointsVo> sonBonusPointsVos=bonusPointsService.findSubsByParentUserIds(parentBonusPointsVo.getUserId(),BonusConstant.BONUS_POINT_BLOODTYPE_RECOMMD);
		String ParentUserIdStr=BonusConstant.INVITED_MEMBER_IDS_SPLIT+parentBonusPointsVo.getUserId()+BonusConstant.INVITED_MEMBER_IDS_SPLIT;
		for(BonusPointsVo bonusPointsVo:sonBonusPointsVos) {
			String oldUserIds=bonusPointsVo.getInvitedParentUserIds();
			List<String> strUserIds=StringUtils.splitToList(oldUserIds,ParentUserIdStr);
			String newUserIds="";
			if(strUserIds.size()>0) {
				newUserIds=strUserIds.get(0);
			}
			newUserIds=newUserIds+BonusConstant.INVITED_MEMBER_IDS_SPLIT+parentBonusPointsVo.getUserId()+BonusConstant.INVITED_MEMBER_IDS_SPLIT+"0";
			bonusPointsVo.setInvitedParentUserIds(newUserIds);
			bonusPointsService.update(bonusPointsVo);
		}
	}
	
	private void resetNode(BonusPointsVo parentBonusPointsVo) {
		List<BonusPointsVo> sonBonusPointsVos=bonusPointsService.findSubsByParentUserIds(parentBonusPointsVo.getUserId(),BonusConstant.BONUS_POINT_BLOODTYPE_BINARYTREE);
		String ParentUserIdStr=BonusConstant.INVITED_MEMBER_IDS_SPLIT+parentBonusPointsVo.getUserId()+BonusConstant.INVITED_MEMBER_IDS_SPLIT;
		for(BonusPointsVo bonusPointsVo:sonBonusPointsVos) {
			String oldUserIds=bonusPointsVo.getInvitedParentUserIds();
			List<String> strUserIds=StringUtils.splitToList(oldUserIds,ParentUserIdStr);
			String newUserIds="";
			if(strUserIds.size()>0) {
				newUserIds=strUserIds.get(0);
			}
			newUserIds=newUserIds+BonusConstant.INVITED_MEMBER_IDS_SPLIT+parentBonusPointsVo.getUserId()+BonusConstant.INVITED_MEMBER_IDS_SPLIT+"0";
			bonusPointsVo.setInvitedParentUserIds(newUserIds);
			bonusPointsService.update(bonusPointsVo);
		}
	}
	/**
	 * 挂载独立节点的父节点
	 * @param curUserId
	 * @param parentNodeUserId
	 * @param parentInvitedUserId
	 */
	public R addNodesToNodeBySubNodeUserId(int curUserId,int parentNodeUserId,Integer parentInvitedUserId) {//1528 1527  1321
		 List<BonusPointsVo> subNodes=bonusPointsService.queryByParentUserId(parentNodeUserId, BonusConstant.BONUS_PARENT_TYPE_BINARY_TREE);
		 if(subNodes!=null&&subNodes.size()>=2)return R.error("该会员已挂满!");
	  	  
		
		UserEntity user=userService.queryObject(curUserId);
		if(user==null) return R.error("查不到该用户");
		logger.info("--------注册用户数据-------user--"+JsonUtil.getJsonByObj(user)); 
		//获取二叉树推荐人
		//推荐人树
		BonusPointsVo recommondBonusPoint=bonusPointsService.queryByUserIdAndBloodType(curUserId, BonusConstant.BONUS_POINT_BLOODTYPE_RECOMMD);
		if(recommondBonusPoint==null||recommondBonusPoint.getInvitedUserId()!=0) return R.error("不存在推荐关系");
		
		//推荐人树
		BonusPointsVo parentRecommondBonusPoint=bonusPointsService.queryByUserIdAndBloodType(parentInvitedUserId, BonusConstant.BONUS_POINT_BLOODTYPE_RECOMMD);
		if(parentRecommondBonusPoint==null) return R.error("不存在推荐关系");
		
		LOG.info("----------------------------recommondBonusPoint:"+JsonUtil.getJsonByObj(recommondBonusPoint));
		
		//获取推荐人 关系
		recommondBonusPoint.setInvitedUserId(parentInvitedUserId);
		String invitedRecommondParentUserIds=""+parentInvitedUserId+BonusConstant.INVITED_MEMBER_IDS_SPLIT+parentRecommondBonusPoint.getInvitedParentUserIds();
		recommondBonusPoint.setInvitedParentUserIds(invitedRecommondParentUserIds);//所有父类节点
		
		addParentsRecommond(recommondBonusPoint);
		
		LOG.info("----------------------------recommondBonusPoint2:"+JsonUtil.getJsonByObj(recommondBonusPoint));
		bonusPointsService.update(recommondBonusPoint);
		
		
		//获取二叉树推荐人
		BonusPointsVo nodeBonusPoint=bonusPointsService.queryByUserIdAndBloodType(curUserId, BonusConstant.BONUS_POINT_BLOODTYPE_BINARYTREE);
		if(nodeBonusPoint==null) return R.error("不存在层级节点关系");
		
		if(!(nodeBonusPoint.getInvitedRightUserId()==0&&nodeBonusPoint.getInvitedUserId()==0)) return R.error("不存在层级节点关系");
		
		//获取二叉树推荐人
		BonusPointsVo parentNodeBonusPoint=bonusPointsService.queryByUserIdAndBloodType(parentNodeUserId, BonusConstant.BONUS_POINT_BLOODTYPE_BINARYTREE);
		if(parentNodeBonusPoint==null) return R.error("不存在父类层级节点关系");
		
		
		//获取推荐人 关系
		String nodeParentUserIds=""+parentNodeUserId+BonusConstant.INVITED_MEMBER_IDS_SPLIT+parentNodeBonusPoint.getInvitedParentUserIds();
		nodeBonusPoint.setInvitedParentUserIds(nodeParentUserIds);//所有父类节点
				
		//挂左边接点
  	   if(subNodes==null||subNodes.size()==0) {
  		  nodeBonusPoint.setInvitedUserId(parentNodeUserId);
  		  nodeBonusPoint.setInvitedRightUserId(null);
  	    }else if(subNodes.size()==1) {
  	    	//挂右边接点
  	    	BonusPointsVo otherNodeBonusPoint=subNodes.get(0);
  	    	if(otherNodeBonusPoint.getInvitedUserId()!=null&&otherNodeBonusPoint.getInvitedUserId()>0) {//挂右边
  	    		nodeBonusPoint.setInvitedUserId(null);
  	    		nodeBonusPoint.setInvitedRightUserId(parentNodeUserId);
  	    	}else {
  	    		nodeBonusPoint.setInvitedUserId(parentNodeUserId);
  	    		nodeBonusPoint.setInvitedRightUserId(null);
  	    	}
  	   }
		//获取父类推荐关系集合
		List<String> nodeStrUserIds=StringUtils.splitToList(nodeParentUserIds, BonusConstant.INVITED_MEMBER_IDS_SPLIT);
		List<Integer> nodeUserIds=new ArrayList<>();
		for(String str:nodeStrUserIds) {
			if(Integer.valueOf(str)>0)nodeUserIds.add(Integer.valueOf(str));
		}
		LOG.info("----------------------------nodeUserIds:"+nodeUserIds);
		LOG.info("----------------------------nodeBonusPoint:"+JsonUtil.getJsonByObj(nodeBonusPoint));
		
		
		//清楚父类业绩
		BigDecimal addPoint=nodeBonusPoint.getBonusMeInvitedPoints().add(nodeBonusPoint.getBonusTeamInvitedPoints());
		
		if(nodeUserIds.size()>0)bonusPointsService.addBonusTeamInvitedPoints(nodeUserIds, addPoint);
		addParentsNode(nodeBonusPoint);
		LOG.info("----------------------------nodeBonusPoint2:"+JsonUtil.getJsonByObj(nodeBonusPoint));
		bonusPointsService.update(nodeBonusPoint);
		
		LOG.info("----------------------------updateok");
		//更新注册数据
		
		user.setSignupInvitedPhone(parentRecommondBonusPoint.getUserName());
		user.setSignupNodePhone(parentNodeBonusPoint.getUserName());
		userService.update(user);
		return R.ok();
	}
	
	
	private void addParentsRecommond(BonusPointsVo parentBonusPointsVo) {
		List<BonusPointsVo> sonBonusPointsVos=bonusPointsService.findSubsByParentUserIds(parentBonusPointsVo.getUserId(),BonusConstant.BONUS_POINT_BLOODTYPE_RECOMMD);
		for(BonusPointsVo bonusPointsVo:sonBonusPointsVos) {
			String newUserIds=bonusPointsVo.getInvitedParentUserIds()+BonusConstant.INVITED_MEMBER_IDS_SPLIT+parentBonusPointsVo.getInvitedParentUserIds();
			bonusPointsVo.setInvitedParentUserIds(newUserIds);
			bonusPointsService.update(bonusPointsVo);
		}
	}
	private void addParentsNode(BonusPointsVo parentBonusPointsVo) {
		List<BonusPointsVo> sonBonusPointsVos=bonusPointsService.findSubsByParentUserIds(parentBonusPointsVo.getUserId(),BonusConstant.BONUS_POINT_BLOODTYPE_BINARYTREE);
		for(BonusPointsVo bonusPointsVo:sonBonusPointsVos) {
			String newUserIds=bonusPointsVo.getInvitedParentUserIds()+BonusConstant.INVITED_MEMBER_IDS_SPLIT+parentBonusPointsVo.getInvitedParentUserIds();
			bonusPointsVo.setInvitedParentUserIds(newUserIds);
			bonusPointsService.update(bonusPointsVo);
		}
	}
	 
	 
	 @Override
	 public R  updateUserLevel(int userId ,BigDecimal consumedMoney) {
		 logger.info("---------updateUserLevel----------"+JsonUtil.getJsonByObj(userId));
		 UserEntity userEntity=userService.queryObject(userId);
		 Map<String, Object> map =new HashMap<>();
		 UserInvestLevelEntity consumedLevelType=null;
		 //获取消费对应的级别
		 List<UserInvestLevelEntity> userInvestLevelEntities= userInvestLevelService.queryList(map);
		 for(UserInvestLevelEntity level:userInvestLevelEntities) {
			 if(consumedMoney.intValue()>=level.getUserLevelConsumedMin().intValue()&&consumedMoney.intValue()<=level.getUserLevelConsumedMax().intValue()) {
				 consumedLevelType=level;
				 break;
			 }
		 }
		 if(consumedLevelType!=null&&consumedLevelType.getUserLevelType()>userEntity.getUserLevelType()) {//大于当前级别 可以升级
			 userEntity.setUserLevelType(consumedLevelType.getUserLevelType());
//			 userEntity.setUserLevelTypeValue(consumedLevelType.getUserLevelMoneyValue());
			 userEntity.setUserNodeBonusLevel(consumedLevelType.getUserLevelNodeLevel());
			 userService.update(userEntity);
		 }
		 logger.info("---------updateUserLevel----------"+JsonUtil.getJsonByObj(userEntity));
		return R.ok(); 
	 }
	 
	 
	 /**
	  * 投资 
	  */
	@Override
	@Transactional(value="transactionManager")
	 public R  buyInvestBonus(int userId,String orderNo) {// investMoney消费金额 当成 投资金额
		 UserEntity user=userService.queryObject(userId);
    	 if(user==null) return R.error("查不到该用户");
    	//只是记录够买记录
		 BonusInvestOrderVo bonusInvestOrderVo=new BonusInvestOrderVo();
		 bonusInvestOrderVo.setConsumedOrderNo(orderNo);
		 GoodsOrderEntity goodsOrderEntity=goodsOrderService.queryObjectByNo(orderNo);
		 
		 if(goodsOrderEntity==null) return R.error("查不到消费订单");
//		 if(goodsOrderEntity.getOrderType()!=OrderConstant.ORDER_TYPE_INVERST) return R.error("消费订单类型不对");
//		 if(goodsOrderEntity.getOrderInvestStatu()==OrderConstant.ORDER_INVEST_STATU_YES) return R.error("已经使用了该权利");
		 
		 int investMoneyYuan=0;
		 
		 int consumedMoneyYuan=goodsOrderEntity.getTotalPayPrice().intValue();
		 //获取奖励倍数
		 UserInvestLevelEntity userInvestLevelEntity=userInvestLevelService.queryByLevelType(user.getUserLevelType());
		 investMoneyYuan=consumedMoneyYuan;
		 if(userInvestLevelEntity!=null) {
			 investMoneyYuan=consumedMoneyYuan*userInvestLevelEntity.getUserLevelTime();
		 }
		 
		 String investOrderNo = "I"+CommonUtil.generateOrderNumber();
		 bonusInvestOrderVo.setInvestOrderNo(investOrderNo);
		 bonusInvestOrderVo.setConsumedMoney(goodsOrderEntity.getTotalPayPrice());
		 bonusInvestOrderVo.setBuyMoney(goodsOrderEntity.getTotalPayPrice());
		 bonusInvestOrderVo.setCreateTime(new Date());
		 bonusInvestOrderVo.setIncomeMoney(new BigDecimal(0));
		 bonusInvestOrderVo.setTotalIncomMoney(new BigDecimal(investMoneyYuan));//十倍预计收益
		 bonusInvestOrderVo.setMaxMoney(new BigDecimal(investMoneyYuan));
		 bonusInvestOrderVo.setUpdateTime(new Date());
		 bonusInvestOrderVo.setPayStatus(BonusConstant.PAY_STATUS_PAYOK);
		 bonusInvestOrderVo.setPayType(ShopConstant.PAY_TYPE_YE);
		 bonusInvestOrderVo.setStatu(0);
		 bonusInvestOrderVo.setUserId(userId);
		 bonusInvestOrderVo.setUserName(user.getUserName());
		 bonusInvestOrderVo.setShareLastTime(new Date());
		 logger.info("--------推荐人ID----增加奖励记录----"+JsonUtil.getJsonByObj(bonusInvestOrderVo)); 
		 apiBonusInvestOrderService.save(bonusInvestOrderVo);
		 goodsOrderEntity.setOrderInvestStatu(OrderConstant.ORDER_INVEST_STATU_YES);
		 goodsOrderService.update(goodsOrderEntity);
		 //增减投资收益
		 userService.addInverst(userId, new BigDecimal(investMoneyYuan), bonusInvestOrderVo.getTotalIncomMoney());
		 
		 //写日志
		 PaymentTask paymentTask=new PaymentTask();
		 paymentTask.setAmount(new BigDecimal(investMoneyYuan));
		 paymentTask.setFee(0);
		 paymentTask.setUserId(user.getUserId());
		 paymentTask.setPayer(user.getUserName());
		 paymentTask.setUserName(user.getUserName());
		 paymentTask.setMemo("【消费】购物奖励资产");
		 paymentTask.setMoneyTypeWallet(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_BONUS_CONSUMED);
		 paymentTask.setPaymentType(PluginConstant.PAYMENT_TYPE_IN);
		 TaskPaymentProducer.addPaymentTaskVo(paymentTask); 
		 
		 logger.info("--------推荐人ID----增加奖励记录----"+JsonUtil.getJsonByObj(paymentTask)); 
		 R r=updateUserLevel(user.getUserId(), goodsOrderEntity.getTotalPayPrice());//升级会员级别
		 logger.info("--------updateUserLevel----r----"+JsonUtil.getJsonByObj(r)); 
		 return R.ok();
	 }
	@Override
	@Transactional(value="transactionManager")
	public R  buyDirectInvestBonus(int userId,String orderNo,BigDecimal investMoney){// investMoney消费金额 当成 投资金额
		UserEntity user=userService.queryObject(userId);
		if(user==null) return R.error("查不到该用户");
		//先升级倍数
		updateUserLevel(userId, investMoney);//升级会员级别
		user=userService.queryObject(userId);
		if(user==null) return R.error("查不到该用户");
		//只是记录够买记录
		BonusInvestOrderVo bonusInvestOrderVo=new BonusInvestOrderVo();
		bonusInvestOrderVo.setConsumedOrderNo(orderNo);
		int investMoneyYuan=0;
		int consumedMoneyYuan=investMoney.intValue();
		//获取奖励倍数
		UserInvestLevelEntity userInvestLevelEntity=userInvestLevelService.queryByLevelType(user.getUserLevelType());
		investMoneyYuan=consumedMoneyYuan;
		if(userInvestLevelEntity!=null) {
			investMoneyYuan=consumedMoneyYuan*userInvestLevelEntity.getUserLevelTime();
		}
		String investOrderNo = "I"+CommonUtil.generateOrderNumber();
		bonusInvestOrderVo.setInvestOrderNo(investOrderNo);
		bonusInvestOrderVo.setConsumedMoney(investMoney);
		bonusInvestOrderVo.setBuyMoney(investMoney);
		bonusInvestOrderVo.setCreateTime(new Date());
		bonusInvestOrderVo.setIncomeMoney(new BigDecimal(0));
		bonusInvestOrderVo.setTotalIncomMoney(new BigDecimal(investMoneyYuan));
		bonusInvestOrderVo.setMaxMoney(new BigDecimal(investMoneyYuan));
		bonusInvestOrderVo.setUpdateTime(new Date());
		bonusInvestOrderVo.setPayStatus(BonusConstant.PAY_STATUS_PAYOK);
		bonusInvestOrderVo.setPayType(ShopConstant.PAY_TYPE_XJ);
		bonusInvestOrderVo.setStatu(0);
		bonusInvestOrderVo.setUserId(userId);
		bonusInvestOrderVo.setUserName(user.getUserName());
		bonusInvestOrderVo.setShareLastTime(new Date());
		logger.info("--------推荐人ID----增加奖励记录----"+JsonUtil.getJsonByObj(bonusInvestOrderVo)); 
		apiBonusInvestOrderService.save(bonusInvestOrderVo);
		//增减投资收益
		userService.addInverst(userId, investMoney, bonusInvestOrderVo.getTotalIncomMoney());
		//写日志
		PaymentTask paymentTask=new PaymentTask();
		paymentTask.setAmount(new BigDecimal(investMoneyYuan));
		paymentTask.setFee(0);
		paymentTask.setUserId(user.getUserId());
		paymentTask.setPayer(user.getUserName());
		paymentTask.setUserName(user.getUserName());
		paymentTask.setMemo("【消费】充值奖励资产");
		paymentTask.setMoneyTypeWallet(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_BONUS_CONSUMED);
		paymentTask.setPaymentType(PluginConstant.PAYMENT_TYPE_IN);
		TaskPaymentProducer.addPaymentTaskVo(paymentTask); 
		
		logger.info("--------推荐人ID----增加奖励记录----"+JsonUtil.getJsonByObj(paymentTask)); 
	
		//添加积分
		userService.addUserIntegralScore(userId, investMoney.intValue());
		
		//写日志
		PaymentTask jfpaymentTask=new PaymentTask();
		jfpaymentTask.setAmount(investMoney);
		jfpaymentTask.setFee(0);
		jfpaymentTask.setUserId(user.getUserId());
		jfpaymentTask.setPayer(user.getUserName());
		jfpaymentTask.setUserName(user.getUserName());
		jfpaymentTask.setMemo("【消费】充值奖励积分");
		jfpaymentTask.setMoneyTypeWallet(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_IN_INTEGRAL_ADD);
		jfpaymentTask.setPaymentType(PluginConstant.PAYMENT_TYPE_IN);
		TaskPaymentProducer.addPaymentTaskVo(jfpaymentTask); 
		
		return R.ok();
	}
	
	@Transactional(value="transactionManager")
	public R  confirmSignUpOld(Integer userId) {
		UserEntity user=userService.queryObject(userId);
		if(user==null) return R.error("查不到该用户");
		logger.info("--------注册用户数据-------user--"+JsonUtil.getJsonByObj(user)); 
		
		int signupUserId=user.getCreateUserId();//代注册的用户；
		String nodeUserName=user.getSignupNodePhone();//节点账号
		String recommondUserName=user.getSignupInvitedPhone();//推荐人账号
		int recommondeUserId=0;//推荐人UserId
		int toNodeUserId=0;//挂载人UserId
		
		if(StringUtils.isEmpty(nodeUserName)) return R.error("二叉树节点不能为空");
		if(StringUtils.isEmpty(recommondUserName)) return R.error("推荐人不能为空");
		
		
		//获取二叉树推荐人
		if(user.getState().equals(ShopConstant.SHOP_USER_STATU_SUCCESS)) return R.error("已经注册成功");
		if(user.getState().equals(ShopConstant.SHOP_USER_STATU_OFFLINE)) return R.error("已下线");
		
		//获取二叉树推荐人
		BonusPointsVo nodeBonusPoint=bonusPointsService.queryByUserIdAndBloodType(userId, BonusConstant.BONUS_POINT_BLOODTYPE_BINARYTREE);
		if(nodeBonusPoint!=null) return R.error("已经挂过节点不能修改");
		//推荐人树
		BonusPointsVo recommondBonusPoint=bonusPointsService.queryByUserIdAndBloodType(userId, BonusConstant.BONUS_POINT_BLOODTYPE_RECOMMD);
		if(recommondBonusPoint!=null) return R.error("已经推荐过了不能修改");
		
		
		
		//获取代注册人的推荐人列表
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("invitedUserId", signupUserId);
		params.put("bloodType",BonusConstant.BONUS_POINT_BLOODTYPE_RECOMMD);
		
		int recommondSum= bonusPointsService.queryTotal(params);
		logger.info("--------代注册人的推荐人数量-------recommondSum--"+recommondSum); 
		
		//代注册人 节点
		BonusPointsVo binaryTreeSignupBonusPointsEntity=bonusPointsService.queryByUserIdAndBloodType(signupUserId, BonusConstant.BONUS_POINT_BLOODTYPE_BINARYTREE);
		if(binaryTreeSignupBonusPointsEntity==null) return R.error("注册人不存在节点");
		//判断代注册人金额 是否满足
		UserEntity invitedUser=userService.queryObject(signupUserId);
		if(invitedUser==null) return R.error("代注册人不能为空");
		//判断推荐人 金额扣除 是否满足
//	  UserInvestLevelEntity userInvestLevelEntity=userInvestLevelService.queryByLevelType(user.getUserLevelType());
//	  BigDecimal toPayMoney=userInvestLevelEntity.getUserLevelMoneyValue();
		
//	  if(invitedUser.getBalance().compareTo(toPayMoney)<0) {
//		  return R.error("代注册人余额不足");
//	  }
		
		//推荐人 节点
		UserEntity recommondNodeUserEntity=userService.queryByUserName(recommondUserName);
		if(recommondNodeUserEntity==null) return R.error("推荐人不存在节点");
		recommondeUserId=recommondNodeUserEntity.getUserId();
		BonusPointsVo binaryTreeRecommondBonusPointsEntity=bonusPointsService.queryByUserIdAndBloodType(recommondeUserId, BonusConstant.BONUS_POINT_BLOODTYPE_BINARYTREE);
		if(binaryTreeRecommondBonusPointsEntity==null) return R.error("推荐人不存在节点");
		//挂载人 节点
		//获取二叉树
		UserEntity nodeUserEntity=userService.queryByUserName(nodeUserName);
		if(nodeUserEntity==null) return R.error("推荐人不存在节点");
		BonusPointsVo binaryTreeBonusPointsEntity=bonusPointsService.queryByUserIdAndBloodType(nodeUserEntity.getUserId(), BonusConstant.BONUS_POINT_BLOODTYPE_BINARYTREE);
		if(binaryTreeBonusPointsEntity==null) return R.error("推荐人不存在节点");
		toNodeUserId=nodeUserEntity.getUserId();
		//判断是否已经挂满节点
		List<BonusPointsVo> bonusPointsEntities=bonusPointsService.queryByParentUserId(nodeUserEntity.getUserId(), BonusConstant.BONUS_POINT_BLOODTYPE_BINARYTREE);
		if(bonusPointsEntities!=null&&bonusPointsEntities.size()>=2) {
			return R.error("节点已经满了");
		}
		//判断是否可以挂二叉树节点
		//1 验证 节点人------节点人 必须在推荐人的节点树下-包括自己
		//2 代注册人的推荐列表 是否为空验证----如果为空----
		//获取推荐人的节点
		logger.info("--------推荐人ID----"+recommondeUserId+"--------节点ID----"+toNodeUserId+"--------代注册ID----"+signupUserId); 
		//挂载节点都是推荐人的节点的子节点 或者自己
		if(toNodeUserId!=recommondeUserId) {
			//to挂载人节点人 节点集合
			List<String> strNodeUserIds=StringUtils.splitToList(binaryTreeBonusPointsEntity.getInvitedParentUserIds(), BonusConstant.INVITED_MEMBER_IDS_SPLIT);
			List<Integer>  teamNodeUserIds=new ArrayList<>();
			for(String str:strNodeUserIds) {
				teamNodeUserIds.add(Integer.valueOf(str));
			}
			logger.info("---------strNodeUserIds----"+JsonUtil.getJsonByObj(teamNodeUserIds));
			if(!teamNodeUserIds.contains(recommondeUserId)) {
				return R.error("节点不在推荐人的关系树上");
			}
			if(recommondSum==0) {//注册的新用户 必须挂载在 节点树的左边 ----即是判断 是否被占用
				if(bonusPointsEntities!=null&&bonusPointsEntities.size()==1) {
					BonusPointsVo brotherNode=bonusPointsEntities.get(0);
					if(brotherNode.getInvitedUserId()!=null) {
						return R.error("该节点上已经挂载了");
					}
				}
			}
//			if(recommondSum==0) {
//		   		   //节点人必须在 ---推荐人--节点树 左边
//		   		   
//		   		   //节点人左右都可以放
//		   	   }
//		   	   
//		   	  if(recommondSum>0) {
//				   //节点人必须在 ---推荐人--节点树--子节点，包括自己
//				   //节点人左右都可以放
//			   }
			
			
			
		}
		//可以挂
		nodeBonusPoint=new BonusPointsVo();
		nodeBonusPoint.setUserId(userId);
		nodeBonusPoint.setBonusInvitedSum(0);//团队人数
		nodeBonusPoint.setBonusMeInvitedPoints(new BigDecimal(0));//个人业绩 直接业绩
		nodeBonusPoint.setBonusTeamInvitedPoints(new BigDecimal(0));//团队业绩
		nodeBonusPoint.setCreateTime(new Date());
		
		nodeBonusPoint.setInvitedParentUserIds("");
		String invitedParentUserIds=""+binaryTreeBonusPointsEntity.getUserId()+BonusConstant.INVITED_MEMBER_IDS_SPLIT+binaryTreeBonusPointsEntity.getInvitedParentUserIds();
		nodeBonusPoint.setInvitedParentUserIds(invitedParentUserIds);//所有父类节点
		
		nodeBonusPoint.setUserRoleType(0);
		nodeBonusPoint.setUserNamedType(0);
		nodeBonusPoint.setUpdateTime(new Date());
		nodeBonusPoint.setStatus(0);
		nodeBonusPoint.setCanGenerateQr("0");//默认不能生成推广二维码
		nodeBonusPoint.setBloodType(BonusConstant.BONUS_POINT_BLOODTYPE_BINARYTREE);
		//优先左 再挂右边
		if(bonusPointsEntities==null||bonusPointsEntities.size()==0) {
			//挂左边
			nodeBonusPoint.setInvitedUserId(binaryTreeBonusPointsEntity.getUserId());
		}
		if(bonusPointsEntities!=null&&bonusPointsEntities.size()==1) {
			//挂右边
			nodeBonusPoint.setInvitedRightUserId(binaryTreeBonusPointsEntity.getUserId());
		}
		logger.info("-------------二叉树关系数据-------nodeBonusPoint-----"+JsonUtil.getJsonByObj(nodeBonusPoint));
		//----end----节点验证完成
		//推荐人 的节点 与代注册人 的节点----必须满足节点树 
		//获取注册人节点
		List<String> strSignUpNodeUserIds=StringUtils.splitToList(binaryTreeSignupBonusPointsEntity.getInvitedParentUserIds(), BonusConstant.INVITED_MEMBER_IDS_SPLIT);
		List<Integer>  teamSignUpNodeUserIds=new ArrayList<>();
		for(String str:strSignUpNodeUserIds) {
			teamSignUpNodeUserIds.add(Integer.valueOf(str));
		}
		
		//推荐人 节点集合
		List<String> strRecommondNodeUserIds=StringUtils.splitToList(binaryTreeRecommondBonusPointsEntity.getInvitedParentUserIds(), BonusConstant.INVITED_MEMBER_IDS_SPLIT);
		List<Integer>  teamRecommondNodeUserIds=new ArrayList<>();
		for(String str:strRecommondNodeUserIds) {
			teamRecommondNodeUserIds.add(Integer.valueOf(str));
		}
		if(recommondeUserId!=signupUserId) {
			if(!(teamSignUpNodeUserIds.contains(recommondeUserId)||teamRecommondNodeUserIds.contains(signupUserId))) {
				return R.error("代注册与推荐人不能跨区域注册");
			}
		}
		
		//推荐人树
		recommondBonusPoint=new BonusPointsVo();
		recommondBonusPoint.setUserId(userId);
		recommondBonusPoint.setBonusInvitedSum(0);//团队人数
		recommondBonusPoint.setBonusMeInvitedPoints(new BigDecimal(0));//个人业绩 直接业绩
		recommondBonusPoint.setBonusTeamInvitedPoints(new BigDecimal(0));//团队业绩
		recommondBonusPoint.setCreateTime(new Date());
		recommondBonusPoint.setInvitedUserId(recommondNodeUserEntity.getUserId());
		recommondBonusPoint.setInvitedParentUserIds("");//推荐关系
		//获取推荐人 关系
		BonusPointsVo recommdBonusPointsEntity=bonusPointsService.queryByUserIdAndBloodType(recommondeUserId, BonusConstant.BONUS_POINT_BLOODTYPE_RECOMMD);
		String invitedRecommondParentUserIds=""+recommondeUserId+BonusConstant.INVITED_MEMBER_IDS_SPLIT+recommdBonusPointsEntity.getInvitedParentUserIds();
		recommondBonusPoint.setInvitedParentUserIds(invitedRecommondParentUserIds);//所有父类节点
		recommondBonusPoint.setUserRoleType(0);
		recommondBonusPoint.setUserNamedType(0);
		recommondBonusPoint.setUpdateTime(new Date());
		recommondBonusPoint.setStatus(0);
		recommondBonusPoint.setCanGenerateQr("0");//默认不能生成推广二维码
		recommondBonusPoint.setBloodType(BonusConstant.BONUS_POINT_BLOODTYPE_RECOMMD);
		logger.info("-------------推荐关系数据-------recommondBonusPoint-----"+JsonUtil.getJsonByObj(recommondBonusPoint));
		
		bonusPointsService.save(nodeBonusPoint);
		bonusPointsService.save(recommondBonusPoint);
		user.setState(ShopConstant.SHOP_USER_STATU_SUCCESS);
		user.setUpdatetime(new Date());
		
		userService.update(user);
		//生成一个消费订单
//    	//代扣金额
//    	userService.reduceUserBalance(invitedUser.getUserId(), toPayMoney);
//    	//写日志
//		 PaymentTask paymentTask=new PaymentTask();
//		 paymentTask.setAmount(toPayMoney);
//		 paymentTask.setFee(0);
//		 paymentTask.setPayer(invitedUser.getUserName());
//		 paymentTask.setUserId(invitedUser.getUserId());
//		 paymentTask.setMemo("【消费】代扣注册");
//		 paymentTask.setMoneyTypeWallet(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_NO);
//		 paymentTask.setPaymentType(PluginConstant.PAYMENT_TYPE_OUT);
//		 TaskPaymentProducer.addPaymentTaskVo(paymentTask); 
//		//生成一个订单
//		String goodSn=userInvestLevelService.queryByLevelType(user.getUserLevelType()).getGoodsSn();
//		GoodsEntity goodsEntity= goodsDao.queryByGoodSn(goodSn);
//		int sum=1;
//		Map<String, Object> resultMap =goodsOrderService.toSubmitBuyInverstGoodOrder(user, goodsEntity.getId(),sum,0);//代付0
//		Map<String, GoodsOrderEntity> orderInfoMap =(Map<String, GoodsOrderEntity>) resultMap.get("data");
//		GoodsOrderEntity orderInfo =(GoodsOrderEntity) orderInfoMap.get("orderInfo");
//		//消费订单 推荐奖励
//		BonusTaskVo bonusTaskVo=new BonusTaskVo(orderInfo.getOrderNo(), orderInfo.getOrderType(), user.getUserId(), orderInfo.getTotalPayPrice());
////		bonusConsumedMallOrderShare(bonusTaskVo);
//		TaskBonusProducer.addBonusTaskVo(bonusTaskVo, true);
		return R.ok();
	}
	
	
		 
	
	
	
	/**
	 * 业绩统计：自己的消费业绩&直属上级的消费业绩
	 * @param memberId
	 * @param teamUserIds
	 * @param addPoint
	 * @return
	 */
	@Transactional(value="transactionManager")
	private void updateMeAndParentPoint(int memberId, Integer invitedUserId,BigDecimal addPoint) {
		bonusPointsService.addBonusMeInvitedPoints(memberId, addPoint);
		if(invitedUserId !=null&&invitedUserId>0 ) {
			bonusPointsService.addBonusTeamInvitedPoints(invitedUserId, addPoint);
		}
	}
	
	/**
	 * 购买推荐奖励
	 * @param bonusTaskVo
	 */
	@Transactional
	public void  bonusConsumedMallOrderShare(BonusTaskVo bonusTaskVo) {
		//消费总额
		BigDecimal consumedMoney=bonusTaskVo.getConsumedPointMoney();
		int userId=bonusTaskVo.getConsumedUserId();
		LOG.info("--------------------消费总额-------------------consumedMoney:"+consumedMoney);
		//添加到用户投资里面
		R r=buyInvestBonus(userId,bonusTaskVo.getOrderNo());
		LOG.info("--------------------消费总额-------------------r:"+JsonUtil.getJsonByObj(r));
		//增加资产
//		//推荐奖励
//		recommondConsumedBonus(userId,consumedMoney);
//		//增减团队收益 统计
		updateNodedConsumedTeamBonusPonit(userId,consumedMoney);
//		//量化奖励
//		plotdConsumedBonus(userId,consumedMoney);
		
	}
	
	
	
	//消费统计
	@Transactional
	@Override
	public void updateNodedConsumedTeamBonusPonit(int inverstUserId,BigDecimal incomeMoney) {
			BonusPointsVo bonusPoint=bonusPointsService.getUserPoint(inverstUserId,BonusConstant.BONUS_PARENT_TYPE_BINARY_TREE);
			if(bonusPoint==null) return;
			//获取父类推荐关系集合
			List<String> strUserIds=StringUtils.splitToList(bonusPoint.getInvitedParentUserIds(), BonusConstant.INVITED_MEMBER_IDS_SPLIT);
			List<Integer>  teamUserIds=new ArrayList<>();
			if(strUserIds.size()==0)return ;
			
			for(String str:strUserIds) {
				teamUserIds.add(Integer.valueOf(str));
			}
			if(teamUserIds.size()==0)return ;
			
//			List<Integer>  toUpdateUserIds=new ArrayList<>();
//			//判断 父节点是否生效
//			Map<Integer, UserEntity> userEntityMap=userService.getByUserIds(teamUserIds);
//			for(Integer id:teamUserIds) {
//				UserEntity tempUserEntity=userEntityMap.get(id);
//				if(tempUserEntity!=null&&tempUserEntity.getState().equals(ShopConstant.SHOP_USER_STATU_SUCCESS)) {
//					toUpdateUserIds.add(id);
//				}
//			}
//			
//			if(toUpdateUserIds.size()==0)return ;
			
			
			LOG.info("----------------------------teamUserIds:"+teamUserIds);
			updateTeamPoint(inverstUserId,teamUserIds,incomeMoney);
			
			
		}
	/**
	 * 每日返利订单处理
	 * @param bonusTaskVo
	 */
	@Transactional
	public void  bonusEveryShare() {
		double rate=0.0005;
		int start=Integer.valueOf(sysConfigService.getValue(BonusConstant.BONUS_EVERY_DAY_RANDOM_MIN, "40"));
		int end=Integer.valueOf(sysConfigService.getValue(BonusConstant.BONUS_EVERY_DAY_RANDOM_MAX, "50"));
		int randomType=Integer.valueOf(sysConfigService.getValue(BonusConstant.BONUS_EVERY_DAY_RANDOM_TYPE, "1"));
		int randomRate=RandomUtil.getRandom(start, end);
		LOG.info("----------------------今日返利比例------randomRate-------------------randomRate:"+randomRate);
		Integer denom=Integer.valueOf(sysConfigService.getValue(BonusConstant.BONUS_EVERY_DAY_RANDOM_DENOM, "100000"));
		LOG.info("----------------------今日返利比例------denom-------------------denom:"+denom);
		//生成随机返利比例 存入数据库
		rate=Double.valueOf(randomRate)/Double.valueOf(denom);
		if(randomType==0) {
			rate=Double.valueOf(start)/Double.valueOf(denom);
		}
		//公司每日收益率
		String poolDateNumber="PPD_"+DateUtils.formatYYYYMMDD(new Date());
		BonusPoolJoinMemberVo bonusPoolJoinMember=apiBonusPoolJoinMemberService.queryByDateNumberAndMemberId(poolDateNumber, BonusConstant.BONUS_POOL_JOIN_MEMBER_ID);
		if(bonusPoolJoinMember==null) {
			bonusPoolJoinMember=new BonusPoolJoinMemberVo();
			bonusPoolJoinMember.setCreatetime(new Date());
			bonusPoolJoinMember.setPoolDateNumber(poolDateNumber);
			bonusPoolJoinMember.setPoolJoinMemberId(BonusConstant.BONUS_POOL_JOIN_MEMBER_ID);
			bonusPoolJoinMember.setPoolJoinMoney(new BigDecimal(rate));
			bonusPoolJoinMember.setPoolJoinType(BonusConstant.BONUS_POOL_JOIN_TYPE_RATE);
			bonusPoolJoinMember.setPoolJoinSum(0);
			bonusPoolJoinMember.setStatus(0);
			apiBonusPoolJoinMemberService.save(bonusPoolJoinMember);
		}
		LOG.info("----------------------今日返利比例-------------------------rate:"+rate);
		 
		rate=bonusPoolJoinMember.getPoolJoinMoney().doubleValue();
		List<UserEntity> userInversts=userService.queryListToShareInvestUserByDate(new Date());
		 LOG.info("----------------------该奖励分红------投资奖励人数量-------------------ordersum:"+userInversts.size());
		 LOG.info("----------------------该奖励分红------投资订reate-------------------rate:"+rate);
		 
		 
		 for(UserEntity userEntity:userInversts) {
			 
			 if(userEntity.getSurplusInvestMoney().intValue()>0) {
				 toShareBonusEveryOrder(poolDateNumber,userEntity,rate);	 
			 }
			  
		 }
	}
	private void toShareBonusEveryOrder(String poolDateNumber,UserEntity userEntity,double rate) {
		 LOG.info("----------------------该奖励分红------user-------------------userEntity:"+JsonUtil.getJsonByObj(userEntity));
		 //客户收益
		BigDecimal incomeMoney=MoneyFormatUtils.getMultiply(userEntity.getSurplusInvestMoney(), rate);
		LOG.info("----------------------该奖励分红------incomeMoney:"+incomeMoney);
		 if(incomeMoney.compareTo(new BigDecimal(0))<=0) {
			 LOG.info("----------------------金额小于0-------------------就分啦");
			 return ;
		 }
		 
		 BigDecimal surplusIncomeMoney=addFund(userEntity, incomeMoney);
		 //增加钱包收益，总收益，增加资产总收益 减少杠杆收益
		 userService.addUserBalanceAndTotalIncomeAndReduceInverst(userEntity.getUserId(), surplusIncomeMoney);
//		 String poolDateNumber="PPD_"+DateUtils.formatYYYYMMDD(new Date());
		 //添加日收益
		 apiBonusPoolJoinMemberService.incrBonusPoolJoinMemberMoney(poolDateNumber, userEntity.getUserId(), surplusIncomeMoney);
		 
		 //写日志
		 PaymentTask paymentTask=new PaymentTask();
		 paymentTask.setAmount(surplusIncomeMoney);
		 paymentTask.setFee(0);
		 paymentTask.setUserId(userEntity.getUserId());
		 paymentTask.setPayer(userEntity.getUserName());
		 paymentTask.setUserName(userEntity.getUserName());
		 paymentTask.setMemo("资产权益收益");
		 paymentTask.setMoneyTypeWallet(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_BONUS_JQ);
		 paymentTask.setPaymentType(PluginConstant.PAYMENT_TYPE_IN);
		 
//		 LOG.info("----------------------写入支付日志---1---paymentTask:"+JsonUtil.getJsonByObj(paymentTask));
//		 paymentInfoService.addPaymentTask(paymentTask);
//		 TaskPaymentProducer.addPaymentTaskVo(paymentTask); 
//		 LOG.info("----------------------写入支付日志----2--paymentTask:"+JsonUtil.getJsonByObj(paymentTask));
		 TaskPaymentProducer.addPaymentTaskVo(paymentTask); 
		 LOG.info("----------------------写入支付日志----3--paymentTask:"+JsonUtil.getJsonByObj(paymentTask));
		
		int inverstUserId=userEntity.getUserId();
		
		//推荐奖励
		recommondInvestBonus(poolDateNumber,userEntity.getUserName(),inverstUserId,incomeMoney);
		LOG.info("----------------------------推荐奖励结束---------------");
		//节点奖励
		nodeInvestBonus(poolDateNumber,userEntity.getUserName(),inverstUserId,incomeMoney);
		LOG.info("----------------------------节点奖励结束---------------");
		//量化奖励
		plotInvestBonus(poolDateNumber,userEntity.getUserName(),inverstUserId,incomeMoney);
		LOG.info("----------------------------量化奖励结束---------------");
		
	}
	//推荐奖励
	private void recommondInvestBonus(String poolDateNumber,String userName,int  inverstUserId,BigDecimal incomeMoney) {
		BonusPointsVo bonusPoint=bonusPointsService.getUserPoint(inverstUserId,BonusConstant.BONUS_PARENT_TYPE_MORE_TREE);
		if(bonusPoint==null) return;
		double firstLevelRate=Double.valueOf(sysConfigService.getValue(BonusConstant.BONUS_RECOMMED_LEVEL_SP_RATE+"_"+1, "-1"));//分层查看是否存在特殊值
		double secondLevelRate=Double.valueOf(sysConfigService.getValue(BonusConstant.BONUS_RECOMMED_LEVEL_SP_RATE+"_"+2, "-1"));//分层查看是否存在特殊值
		
		int maxLevel=Integer.valueOf(sysConfigService.getValue(BonusConstant.BONUS_RECOMMED_LEVEL_MAX, "2"));
		if(maxLevel==0) return;
		
		
		Queue<Double> rateQueue = new LinkedList<>();
		rateQueue.offer(firstLevelRate); //进队
		rateQueue.offer(secondLevelRate);//进队列
		
//		Queue<Double> rateQueue = new LinkedList<>();
//		for(int i=0;i<maxLevel;i++) {
//			rateQueue.offer(defaultRate); 
//		}
		
		LOG.info("----------------------------推荐奖励比例参数---------------"+JsonUtil.getJsonByObj(rateQueue));
		
		//获取父类推荐关系集合
		List<String> strRecommendUserIds=StringUtils.splitToList(bonusPoint.getInvitedParentUserIds(), BonusConstant.INVITED_MEMBER_IDS_SPLIT);
		List<Integer>  teamRecommendUserIds=new ArrayList<>();
		for(String str:strRecommendUserIds) {
			teamRecommendUserIds.add(Integer.valueOf(str));
		}
		if(teamRecommendUserIds.size()==0)return ;
		LOG.info("----------------------------teamRecommendUserIds:"+teamRecommendUserIds);
		Map<Integer,UserEntity> userEntities=userService.getByUserIds(teamRecommendUserIds);			
		LOG.info("----------------------------teamRecommendUserIds---------userEntities"+userEntities.size()); 
		
		for(int i=0;i<teamRecommendUserIds.size()&&!rateQueue.isEmpty()&&i<maxLevel;i++) {//队列去空 或者 超过maxleve
			
			double levelRate=rateQueue.peek();//取出优先奖励比例 仅仅取出
			
			rateQueue.poll();//取出即用
			
			
			BigDecimal addBalance=incomeMoney.multiply(new BigDecimal(levelRate));
			addBalance=MoneyFormatUtils.formatBigDecimal4(addBalance);
			
			UserEntity toUserEntity=userEntities.get(teamRecommendUserIds.get(i));
			
			if(toUserEntity==null) continue;
			
			if(!toUserEntity.getState().equals(ShopConstant.SHOP_USER_STATU_SUCCESS)) {
				LOG.info("----------------------------未激活 不分奖励---------"+toUserEntity.getUserId()); 
				continue;
			}
			//判断用户是否可以满足幸运奖条件
//			String poolDateNumber="PPD_"+DateUtils.formatYYYYMMDD(new Date());
			//投资扣除判读
			if(toUserEntity.getSurplusInvestMoney().compareTo(addBalance)<0) {
				LOG.info("===========该用的投资杠杆资产不足扣除========:"+toUserEntity.getUserId()
				+ "---------addBalance=====:"+addBalance);	
				continue;
			}
			
			if(addBalance.compareTo(new BigDecimal(0))>0) {
				 BigDecimal surplusIncomeMoney=addFund(toUserEntity, addBalance);
				 LOG.info("===========计算推荐提成结束保存==="
				 		+ "tempBonusPoint.getUserId()======:"+toUserEntity.getUserId()
				 		+ "---------surplusIncomeMoney=====:"+surplusIncomeMoney+"-----------来源层级---------："+i);	
//				 rateQueue.poll();//取出优先奖励比例 并删除
				 //增加钱包收益，总收益，增加资产总收益 减少杠杆收益
				 userService.addUserBalanceAndTotalIncomeAndReduceInverst(toUserEntity.getUserId(), surplusIncomeMoney);
				 //添加日收益
				 apiBonusPoolJoinMemberService.incrBonusPoolJoinMemberMoney(poolDateNumber, toUserEntity.getUserId(), surplusIncomeMoney);
				 //写日志
				 PaymentTask paymentTask=new PaymentTask();
				 paymentTask.setAmount(surplusIncomeMoney);
				 paymentTask.setFee(0);
				 paymentTask.setUserId(toUserEntity.getUserId());
				 paymentTask.setPayer(toUserEntity.getUserName());
				 paymentTask.setUserName(toUserEntity.getUserName());
				 if(rateQueue.size()>=1) {
					 paymentTask.setMemo("直接分享收益:"+userName);
				 }else {
					 paymentTask.setMemo("间接分享收益:"+userName);
				 }
				 
				 paymentTask.setMoneyTypeWallet(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_BONUS_RECOMMED_INVEST);
				 paymentTask.setPaymentType(PluginConstant.PAYMENT_TYPE_IN);
				 TaskPaymentProducer.addPaymentTaskVo(paymentTask); 
			 }
		}
	}
	//节点奖励
	private void nodeInvestBonus(String poolDateNumber,String userName,int inverstUserId,BigDecimal incomeMoney) {
		BonusPointsVo bonusPoint=bonusPointsService.getUserPoint(inverstUserId,BonusConstant.BONUS_PARENT_TYPE_BINARY_TREE);
		if(bonusPoint==null) return;
		int maxLevel=Integer.valueOf(sysConfigService.getValue(BonusConstant.BONUS_NODE_LEVEL_MAX, "0"));
		if(maxLevel==0) return;
		double defaultRate=Double.valueOf(sysConfigService.getValue(BonusConstant.BONUS_NODE_LEVEL_MAX_DEFAULT_RATE, "0"));
	   
		LOG.info("===========二叉树======defaultRate==:"+defaultRate+ "---------maxLevel=====:"+maxLevel);	
		
		//获取父类推荐关系集合
		List<String> strUserIds=StringUtils.splitToList(bonusPoint.getInvitedParentUserIds(), BonusConstant.INVITED_MEMBER_IDS_SPLIT);
		List<Integer>  teamUserIds=new ArrayList<>();
		if(strUserIds.size()==0)return ;
		
		for(String str:strUserIds) {
			teamUserIds.add(Integer.valueOf(str));
		}
		
		if(teamUserIds.size()==0)return ;
		LOG.info("----------------------------teamUserIds:"+teamUserIds);
		Map<Integer,UserEntity> userEntities=userService.getByUserIds(teamUserIds);			
		LOG.info("----------------------------teamUserIds---------userEntities-----"+userEntities.size()); 
		
		for(int i=0;i<teamUserIds.size()&&i<=maxLevel;i++) {
			
			BigDecimal addBalance=incomeMoney.multiply(new BigDecimal(defaultRate));
			
			addBalance=MoneyFormatUtils.formatBigDecimal4(addBalance);
			
			UserEntity toUserEntity=userEntities.get(teamUserIds.get(i));
			if(toUserEntity==null) continue;
			
			if(!toUserEntity.getState().equals(ShopConstant.SHOP_USER_STATU_SUCCESS)) {
				LOG.info("----------------------------未激活 不分奖励---------"+toUserEntity.getUserId()); 
				continue;
			}
			
			//判断用户是否可以满足幸运奖条件
			
			UserInvestLevelEntity userInvestLevelEntity=userInvestLevelService.queryByLevelType(toUserEntity.getUserLevelType());
			if(userInvestLevelEntity==null)continue;
			//判单层级
			if((i+1)>userInvestLevelEntity.getUserLevelNodeLevel()) {
				 LOG.info("===========幸运奖励层级不够========:"+toUserEntity.getUserId()+"======================="+toUserEntity.getUserNodeBonusLevel()
					 		+ "---------addBalance=====:"+addBalance);
				 continue;
			}
			
			//是否已经满额了
//			String poolDateNumber="PPD_"+DateUtils.formatYYYYMMDD(new Date());
			//投资扣除判读
			if(toUserEntity.getSurplusInvestMoney().compareTo(addBalance)<0) {
				LOG.info("=========节点==该用的投资杠杆资产不足扣除========:"+toUserEntity.getUserId()
				+ "---------addBalance=====:"+addBalance);	
				continue;
			}
			
			if(addBalance.compareTo(new BigDecimal(0))>0) {
				BigDecimal surplusIncomeMoney=addFund(toUserEntity, addBalance);
				 LOG.info("===========计算推荐提成结束保存==="
				 		+ "tempBonusPoint.getUserId()======:"+toUserEntity.getUserId()
				 		+ "---------surplusIncomeMoney=====:"+surplusIncomeMoney+"--------------层级----i------"+i);	
				 
				 //增加钱包收益，总收益，增加资产总收益 减少杠杆收益
				 userService.addUserBalanceAndTotalIncomeAndReduceInverst(toUserEntity.getUserId(), surplusIncomeMoney);
				 //添加日收益 统计
				 apiBonusPoolJoinMemberService.incrBonusPoolJoinMemberMoney(poolDateNumber, toUserEntity.getUserId(), surplusIncomeMoney);
				 //写日志
				 PaymentTask paymentTask=new PaymentTask();
				 paymentTask.setAmount(surplusIncomeMoney);
				 paymentTask.setFee(0);
				 paymentTask.setUserId(toUserEntity.getUserId());
				 paymentTask.setPayer(toUserEntity.getUserName());
				 paymentTask.setUserName(toUserEntity.getUserName());
				 paymentTask.setMemo("幸运星收益:"+userName);
				 paymentTask.setMoneyTypeWallet(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_BONUS_NODE_INVEST);
				 paymentTask.setPaymentType(PluginConstant.PAYMENT_TYPE_IN);
				 TaskPaymentProducer.addPaymentTaskVo(paymentTask); 
			 }
			
			
		}
			
	}
	//小区奖励
	private void plotInvestBonus(String poolDateNumber,String userName,int inverstUserId,BigDecimal incomeMoney) {
		BonusPointsVo bonusPoint=bonusPointsService.getUserPoint(inverstUserId,BonusConstant.BONUS_PARENT_TYPE_BINARY_TREE);
		if(bonusPoint==null) return;
		int maxLevel=Integer.valueOf(sysConfigService.getValue(BonusConstant.BONUS_NODE_POLT_LEVEL_MAX, "0"));
		if(maxLevel==0) return;
		double defaultRate=Double.valueOf(sysConfigService.getValue(BonusConstant.BONUS_NODE_POLT_LEVEL_MAX_DEFAULT_RATE, "0"));
	     //节点奖励都是相同的
		Queue<Double> rateQueue = new LinkedList<>();
		for(int i=0;i<maxLevel;i++) {
			rateQueue.offer(defaultRate); 
		}
		LOG.info("----------------------------小区比例参数---------------"+JsonUtil.getJsonByObj(rateQueue));
		//获取父类推荐关系集合
		List<String> strUserIds=StringUtils.splitToList(bonusPoint.getInvitedParentUserIds(), BonusConstant.INVITED_MEMBER_IDS_SPLIT);
		
		List<Integer>  teamUserIds=new ArrayList<>();
		for(String str:strUserIds) {
			teamUserIds.add(Integer.valueOf(str));
		}
		if(teamUserIds.size()==0)return ;
		LOG.info("----------------------------teamUserIds:"+teamUserIds);
		Map<Integer,UserEntity> userEntities=userService.getByUserIds(teamUserIds);	
		//所有节点人
		Map<Integer,UserEntity> toSharePointUserMap=new HashMap<Integer, UserEntity>();
		
		for(int i=0;i<teamUserIds.size()&&!rateQueue.isEmpty();i++) {
			
			Integer tempUserId=teamUserIds.get(i);
			
			double levelRate=rateQueue.peek();//取出优先奖励比例 仅仅取出
			BigDecimal addBalance=incomeMoney.multiply(new BigDecimal(levelRate));
			addBalance=MoneyFormatUtils.formatBigDecimal4(addBalance);
			
			UserEntity toUserEntity=userEntities.get(tempUserId);
			if(toUserEntity==null) continue;
			
			if(!toUserEntity.getState().equals(ShopConstant.SHOP_USER_STATU_SUCCESS)) {
				LOG.info("----------------------------未激活 不分奖励---------"+toUserEntity.getUserId()); 
				continue;
			}
			
			//判断用户是否可以满足幸运奖条件
			//判断是否来源小区
			//获取节点
			List<BonusPointsVo> bonusPointsEntities=bonusPointsService.queryByParentUserId(toUserEntity.getUserId(), BonusConstant.BONUS_POINT_BLOODTYPE_BINARYTREE);
		  	if(bonusPointsEntities==null||bonusPointsEntities.size()<2) {
		  		continue;
		  	}
		  	BonusPointsVo leftBonusPointsVo=null;
		  	BonusPointsVo rightBonusPointsVo=null;
		  	BonusPointsVo tempBonusPointsVo=bonusPointsEntities.get(0);
		  	if(tempBonusPointsVo.getInvitedRightUserId()!=null&&tempBonusPointsVo.getInvitedRightUserId()>0) {
		  		rightBonusPointsVo=bonusPointsEntities.get(0);
		  	}
		  	tempBonusPointsVo=bonusPointsEntities.get(1);
		  	if(tempBonusPointsVo.getInvitedRightUserId()!=null&&tempBonusPointsVo.getInvitedRightUserId()>0) {
		  		rightBonusPointsVo=bonusPointsEntities.get(1);
		  	}
		  	
		  	tempBonusPointsVo=bonusPointsEntities.get(0);
		  	if(tempBonusPointsVo.getInvitedUserId()!=null&&tempBonusPointsVo.getInvitedUserId()>0) {
		  		leftBonusPointsVo=bonusPointsEntities.get(0);
		  	}
		  	tempBonusPointsVo=bonusPointsEntities.get(1);
		  	if(tempBonusPointsVo.getInvitedUserId()!=null&&tempBonusPointsVo.getInvitedUserId()>0) {
		  		leftBonusPointsVo=bonusPointsEntities.get(1);
		  	}
		  	
//		  	LOG.info("----------------------------leftBonusPointsVo----:"+JsonUtil.getJsonByObj(leftBonusPointsVo));
//		  	LOG.info("----------------------------rightBonusPointsVo----:"+JsonUtil.getJsonByObj(rightBonusPointsVo));
		  	//left >right
		  	//min userId
		  	int minUserId=leftBonusPointsVo.getUserId();
		  	if((leftBonusPointsVo.getBonusTeamInvitedPoints().add(leftBonusPointsVo.getBonusMeInvitedPoints())).compareTo((rightBonusPointsVo.getBonusTeamInvitedPoints().add(rightBonusPointsVo.getBonusMeInvitedPoints())))>0) {
		  		minUserId=rightBonusPointsVo.getUserId();
		  	}
		  	
		  	LOG.info("----------------------------minUserId----:"+JsonUtil.getJsonByObj(minUserId));
		  	if(!teamUserIds.contains(minUserId)) {//判断是否是小区业务
		  		if(minUserId!=inverstUserId) {
		  		   //不是小区
			  		LOG.info("===========奖励=======由于不是来源小区，所有不参与分红===");	
			  		continue;
		  		}
		  	}
		  	
		    //判断用户是否可以满足幸运奖条件
//			String poolDateNumber="PPD_"+DateUtils.formatYYYYMMDD(new Date());
		  	//投资扣除判读
			if(toUserEntity.getSurplusInvestMoney().compareTo(addBalance)<0) {
				LOG.info("=========小区奖励==该用的投资杠杆资产不足扣除========:"+toUserEntity.getUserId()
				+ "---------addBalance=====:"+addBalance);	
				continue;
			}
		  	
			if(addBalance.compareTo(new BigDecimal(0))>0) {
				
				BigDecimal surplusIncomeMoney=addFund(toUserEntity, addBalance);
				LOG.info("===========小区奖励===="
				 		+ "tempBonusPoint.getUserId()======:"+toUserEntity.getUserId()
				 		+ "---------surplusIncomeMoney=====:"+surplusIncomeMoney+"--------userName-------"+userName);	
				 rateQueue.poll();//取出优先奖励比例 并删除
				 //增加钱包收益，总收益，增加资产总收益 减少杠杆收益
				 userService.addUserBalanceAndTotalIncomeAndReduceInverst(toUserEntity.getUserId(), surplusIncomeMoney);
				 //添加日收益
				 apiBonusPoolJoinMemberService.incrBonusPoolJoinMemberMoney(poolDateNumber, toUserEntity.getUserId(), surplusIncomeMoney);
				 //写日志
				 PaymentTask paymentTask=new PaymentTask();
				 paymentTask.setAmount(surplusIncomeMoney);
				 paymentTask.setFee(0);
				 paymentTask.setUserId(toUserEntity.getUserId());
				 paymentTask.setPayer(toUserEntity.getUserName());
				 paymentTask.setUserName(toUserEntity.getUserName());
				 paymentTask.setMemo("分享社区收益,来自:"+userName);
				 paymentTask.setMoneyTypeWallet(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_BONUS_NODE_INVEST_POLT);
				 paymentTask.setPaymentType(PluginConstant.PAYMENT_TYPE_IN);
				 TaskPaymentProducer.addPaymentTaskVo(paymentTask); 
				 
				 toSharePointUserMap.put(tempUserId, toUserEntity);
			 }
			
				
		}
		
		
		//特殊小区名单处理
		
		for(int i=0;i<teamUserIds.size();i++) {
			
			Integer tempUserId=teamUserIds.get(i);
			if(UserBlackCacheUtil.getIsShareLevelUser(tempUserId)) {//存在特殊值
				LOG.info("----------------------------特殊账号-----i----"+i+"-----tempUserId-----"+tempUserId); 
				if(toSharePointUserMap.containsKey(tempUserId)) {
					LOG.info("----------------------------特殊账号--------已存在-"+tempUserId); 
					continue ;
				}
				LOG.info("----------------------------特殊账号------执行---"+tempUserId); 
				UserBlackEntity userBlackEntity=UserBlackCacheUtil.getShareLevelUser(tempUserId);
				if(i<userBlackEntity.getUserShareLevel()) {
					BigDecimal addBalance=incomeMoney.multiply(new BigDecimal(defaultRate));
					addBalance=MoneyFormatUtils.formatBigDecimal4(addBalance);
					LOG.info("----------------------------特殊账号------执行---i--"+i+"--------------------------addBalance-----"+addBalance); 
					UserEntity toUserEntity=userEntities.get(tempUserId);
					if(toUserEntity==null) continue;
					LOG.info("----------------------------特殊账号------执行---i--"+i+"--------------------------toUserEntity"+toUserEntity); 
					if(!toUserEntity.getState().equals(ShopConstant.SHOP_USER_STATU_SUCCESS)) {
						LOG.info("----------------------------未激活 不分奖励---------"+toUserEntity.getUserId()); 
						continue;
					}
					//判断用户是否可以满足幸运奖条件
					//判断是否来源小区
					//获取节点
					List<BonusPointsVo> bonusPointsEntities=bonusPointsService.queryByParentUserId(toUserEntity.getUserId(), BonusConstant.BONUS_POINT_BLOODTYPE_BINARYTREE);
				  	if(bonusPointsEntities==null||bonusPointsEntities.size()<2) {
				  		continue;
				  	}
				  	BonusPointsVo leftBonusPointsVo=null;
				  	BonusPointsVo rightBonusPointsVo=null;
				  	BonusPointsVo tempBonusPointsVo=bonusPointsEntities.get(0);
				  	if(tempBonusPointsVo.getInvitedRightUserId()!=null&&tempBonusPointsVo.getInvitedRightUserId()>0) {
				  		rightBonusPointsVo=bonusPointsEntities.get(0);
				  	}
				  	tempBonusPointsVo=bonusPointsEntities.get(1);
				  	if(tempBonusPointsVo.getInvitedRightUserId()!=null&&tempBonusPointsVo.getInvitedRightUserId()>0) {
				  		rightBonusPointsVo=bonusPointsEntities.get(1);
				  	}
				  	
				  	tempBonusPointsVo=bonusPointsEntities.get(0);
				  	if(tempBonusPointsVo.getInvitedUserId()!=null&&tempBonusPointsVo.getInvitedUserId()>0) {
				  		leftBonusPointsVo=bonusPointsEntities.get(0);
				  	}
				  	tempBonusPointsVo=bonusPointsEntities.get(1);
				  	if(tempBonusPointsVo.getInvitedUserId()!=null&&tempBonusPointsVo.getInvitedUserId()>0) {
				  		leftBonusPointsVo=bonusPointsEntities.get(1);
				  	}
				  	
				  	//left >right
				  	//min userId
				  	int minUserId=leftBonusPointsVo.getUserId();
				  	if((leftBonusPointsVo.getBonusTeamInvitedPoints().add(leftBonusPointsVo.getBonusMeInvitedPoints())).compareTo((rightBonusPointsVo.getBonusTeamInvitedPoints().add(rightBonusPointsVo.getBonusMeInvitedPoints())))>0) {
				  		minUserId=rightBonusPointsVo.getUserId();
				  	}
				  	
				  	LOG.info("----------------------------minUserId----:"+JsonUtil.getJsonByObj(minUserId));
				  	if(!teamUserIds.contains(minUserId)) {//判断是否是小区业务
				  		if(minUserId!=inverstUserId) {
				  		   //不是小区
					  		LOG.info("===========奖励=======由于不是来源小区，所有不参与分红===");	
					  		continue;
				  		}
				  	}
				  	
				    //判断用户是否可以满足幸运奖条件
				  	//投资扣除判读
					if(toUserEntity.getSurplusInvestMoney().compareTo(addBalance)<0) {
						LOG.info("=========小区奖励==该用的投资杠杆资产不足扣除========:"+toUserEntity.getUserId()
						+ "---------addBalance=====:"+addBalance);	
						continue;
					}
				  	
					if(addBalance.compareTo(new BigDecimal(0))>0) {
						
						BigDecimal surplusIncomeMoney=addFund(toUserEntity, addBalance);
						LOG.info("=====特殊======小区奖励===="
						 		+ "tempBonusPoint.getUserId()======:"+toUserEntity.getUserId()
						 		+ "---------surplusIncomeMoney=====:"+surplusIncomeMoney+"======userName====="+userName);	
						 //增加钱包收益，总收益，增加资产总收益 减少杠杆收益
						 userService.addUserBalanceAndTotalIncomeAndReduceInverst(toUserEntity.getUserId(), surplusIncomeMoney);
						 //添加日收益
						 apiBonusPoolJoinMemberService.incrBonusPoolJoinMemberMoney(poolDateNumber, toUserEntity.getUserId(), surplusIncomeMoney);
						 //写日志
						 PaymentTask paymentTask=new PaymentTask();
						 paymentTask.setAmount(surplusIncomeMoney);
						 paymentTask.setFee(0);
						 paymentTask.setUserId(toUserEntity.getUserId());
						 paymentTask.setPayer(toUserEntity.getUserName());
						 paymentTask.setUserName(toUserEntity.getUserName());
						 paymentTask.setMemo("分享社区收益,来自:"+userName);
						 paymentTask.setMoneyTypeWallet(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_BONUS_NODE_INVEST_POLT);
						 paymentTask.setPaymentType(PluginConstant.PAYMENT_TYPE_IN);
						 TaskPaymentProducer.addPaymentTaskVo(paymentTask); 
					 }
				}else {
					LOG.info("----------------------------特殊账号---但是层级不够---执行----i-----"+i+"----level--------"+userBlackEntity.getUserShareLevel()); 
				}
			}
			
		}
		
	}
	
	/**
	 * 业绩统计：自己的消费业绩&上游团队消费业绩
	 * @param memberId
	 * @param teamUserIds
	 * @param addPoint
	 * @return
	 */
	private void updateTeamPoint(int memberId,List<Integer> teamUserIds,BigDecimal addPoint) {
		bonusPointsService.addBonusMeInvitedPoints(memberId, addPoint);
		if(!CollectionUtils.isEmpty(teamUserIds)) {
			bonusPointsService.addBonusTeamInvitedPoints(teamUserIds, addPoint);
		}
	}
	private void updateTeamSum(List<Integer> teamUserIds,int incrSum) {
			bonusPointsService.addBonusTeamInvitedSum(teamUserIds, incrSum);
	}
	
	private BigDecimal addFund(UserEntity userEntity,BigDecimal addBalance) {
		BigDecimal totalInvestMoney=userEntity.getTotalInvestMoney();//总投资金额
		BigDecimal investIncomeMoney=userEntity.getInvestIncomeMoney();//已经收益
		if(investIncomeMoney.compareTo(totalInvestMoney)>0) {
			LOG.info("=========添加基金========---------addBalance=====:"+addBalance);	
			double defaultRate=Double.valueOf(sysConfigService.getValue(BonusConstant.BONUS_FUND_SP_RATE, "0"));
//			BigDecimal addFund=addBalance.multiply(new BigDecimal(defaultRate));
			
			BigDecimal addFund=MoneyFormatUtils.getMultiply(addBalance, defaultRate);
			
			if(addFund.compareTo(new BigDecimal(0))>0) {//大于0 才显示
				 addBalance=addBalance.subtract(addFund);
				 userService.addUserFund(userEntity.getUserId(), addFund);
				 //写日志
				 PaymentTask paymentTask=new PaymentTask();
				 paymentTask.setAmount(addFund);
				 paymentTask.setFee(0);
				 paymentTask.setUserId(userEntity.getUserId());
				 paymentTask.setPayer(userEntity.getUserName());
				 paymentTask.setUserName(userEntity.getUserName());
				 paymentTask.setMemo("扣除社区基金");
				 paymentTask.setMoneyTypeWallet(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_BONUS_NODE_FUND);
				 paymentTask.setPaymentType(PluginConstant.PAYMENT_TYPE_OUT);
				 TaskPaymentProducer.addPaymentTaskVo(paymentTask); 
			}
			return addBalance;
		}
		return addBalance;
		
	}
	
}
