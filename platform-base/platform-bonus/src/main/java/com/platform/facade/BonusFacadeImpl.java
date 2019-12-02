package com.platform.facade;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
import com.platform.api.service.ApiBonusPoolService;
import com.platform.cache.BonusAppCache;
import com.platform.constants.BonusConstant;
import com.platform.constants.PluginConstant;
import com.platform.dao.GoodsDao;
import com.platform.entity.BonusPointsLogVo;
import com.platform.entity.BonusPointsVo;
import com.platform.entity.GoodsEntity;
import com.platform.entity.GoodsOrderEntity;
import com.platform.entity.UserEntity;
import com.platform.entity.UserInvestLevelEntity;
import com.platform.entity.UserInvitedChgLogVo;
import com.platform.mq.model.BonusTaskVo;
import com.platform.mq.model.PaymentTask;
import com.platform.mq.producer.TaskPaymentProducer;
import com.platform.service.ApiBonusPointsLogService;
import com.platform.service.ApiBonusPointsService;
import com.platform.service.ApiUserInvitedChgLogService;
import com.platform.service.GoodsOrderService;
import com.platform.service.SysConfigService;
import com.platform.service.UserInvestLevelService;
import com.platform.service.UserService;
import com.platform.util.ShopConstant;
import com.platform.util.constants.OrderConstant;
import com.platform.utils.CommonUtil;
import com.platform.utils.DateUtils;
import com.platform.utils.JsonUtil;
//import com.platform.service.ApiUserService;
import com.platform.utils.ObjToStringUtil;
import com.platform.utils.R;
import com.platform.utils.RandomUtil;
import com.platform.utils.StringUtils;

@Component
public class BonusFacadeImpl  implements BonusFacade {
	private static final Logger LOG = LoggerFactory
			.getLogger(BonusFacadeImpl.class);
	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Resource
	private ApiBonusPointsService bonusPointService;
	@Resource
	private ApiUserInvitedChgLogService userInvitedChgLogService;
	@Resource
	private BonusAppCache bonusAppCache;
//	
	@Autowired
	private ApiBonusPointsLogService apiBonusPointsLogService;
	@Autowired
	private ApiBonusInvestOrderService apiBonusInvestOrderService;
	
	private BigDecimal BONUS_MEMBER_ROLE_TYPE_manager_least_money = new BigDecimal(1999);//升级为经理的业绩要求
	public   int  BONUS_MEMBER_ROLE_TYPE_shopper_one_least_numbers = 3;//升级为店长要求3名经理
	public   int  BONUS_MEMBER_ROLE_TYPE_shopper_two_least_numbers = 3;//升级为二级店长，下面要3名店长
	public   int  BONUS_MEMBER_ROLE_TYPE_shopper_three_least_numbers = 9;//升级为三级级店长，下面要9名店长
	public   int  BONUS_MEMBER_ROLE_TYPE_shopper_four_least_numbers = 81;//升级为四级店长，下面要81名店长

	
	 @Autowired
	 private UserService userService;
	 @Autowired
	 private ApiBonusPointsService bonusPointsService;
	 
	 @Autowired
	 private SysConfigService sysConfigService;
	 
	 @Autowired
	 private ApiBonusPoolJoinMemberService apiBonusPoolJoinMemberService;
	 @Autowired
	 private ApiBonusPoolService apiBonusPoolService ;
	 @Autowired
	 private UserInvestLevelService userInvestLevelService ;
	 
	 @Autowired
	 private GoodsOrderService goodsOrderService ;
	 @Autowired
	 private GoodsDao goodsDao ;
	 
	 
	 
	 @Override
	 public R  updateUserLevel(UserEntity userEntity,BigDecimal consumedMoney) {
		 Map<String, Object> map =new HashMap<>();
		 UserInvestLevelEntity consumedLevelType=null;
		 
		 //获取消费对应的级别
		 List<UserInvestLevelEntity> userInvestLevelEntities= userInvestLevelService.queryList(map);
		 for(UserInvestLevelEntity level:userInvestLevelEntities) {
			 if(level.getUserLevelMoneyValue().intValue()==consumedMoney.intValue()) {
//				 userLevelType=level.getUserLevelType();
				 consumedLevelType=level;
				 break;
			 }
		 }
		 if(consumedLevelType!=null&&consumedLevelType.getUserLevelType()>userEntity.getUserLevelType()) {//大于当前级别 可以升级
			 userEntity.setUserLevelType(consumedLevelType.getUserLevelType());
//			 userEntity.setUserLevelTypeValue(consumedLevelType.getUserLevelMoneyValue());
			 userEntity.setUserNodeBonusLevel(consumedLevelType.getUserLevelNodeLevel());
		 }
		 
		return R.ok(); 
	 }
	 
	 /**
	  * 投资 
	  */
	@Override
	@Transactional(value="transactionManager")
	 public R  buyInvestBonus(int userId,String orderNo,BigDecimal investMoney) {
		 UserEntity user=userService.queryObject(userId);
    	 if(user==null) return R.error("查不到该用户");
    	 if(user.getBalance().intValue()<investMoney.intValue())return R.error("账户余额不足");
		 BonusInvestOrderVo bonusInvestOrderVo=new BonusInvestOrderVo();
		 bonusInvestOrderVo.setConsumedOrderNo(orderNo);
		 GoodsOrderEntity goodsOrderEntity=goodsOrderService.queryObjectByNo(orderNo);
		 if(goodsOrderEntity==null) return R.error("查不到消费订单");
		 if(goodsOrderEntity.getOrderType()!=OrderConstant.ORDER_TYPE_INVERST) return R.error("消费订单类型不对");
		 if(goodsOrderEntity.getOrderInvestStatu()==OrderConstant.ORDER_INVEST_STATU_YES) return R.error("已经使用了该权利");
		 int investMoneyYuan=investMoney.intValue();
		 int consumedMoneyYuan=goodsOrderEntity.getTotalPrice().intValue();
		 int maxInvestMoney=consumedMoneyYuan*10;
		 if(investMoneyYuan>maxInvestMoney)return R.error("投资金额不能超过十倍");
		 if(investMoneyYuan>maxInvestMoney)return R.error("投资金额不能超过十倍");
		 if(investMoney.intValue()<=0)return R.error("投资金额不能小于0");
		 
		 String investOrderNo = "I"+CommonUtil.generateOrderNumber();
		 bonusInvestOrderVo.setInvestOrderNo(investOrderNo);
		 bonusInvestOrderVo.setConsumedMoney(goodsOrderEntity.getTotalPrice());
		 bonusInvestOrderVo.setBuyMoney(investMoney);
		 bonusInvestOrderVo.setCreateTime(new Date());
		 bonusInvestOrderVo.setIncomeMoney(new BigDecimal(0));
		 //
		 int incomeTime=investMoney.intValue()/consumedMoneyYuan;
		 bonusInvestOrderVo.setTotalIncomMoney(investMoney.multiply(new BigDecimal(incomeTime)));//十倍预计收益
		 bonusInvestOrderVo.setMaxMoney(investMoney.multiply(new BigDecimal(10)));
		 bonusInvestOrderVo.setUpdateTime(new Date());
		 bonusInvestOrderVo.setPayStatus(BonusConstant.PAY_STATUS_PAYOK);
		 bonusInvestOrderVo.setPayType(ShopConstant.PAY_TYPE_YE);
		 bonusInvestOrderVo.setStatu(0);
		 bonusInvestOrderVo.setUserId(userId);
		 bonusInvestOrderVo.setUserName(user.getUserName());
		 bonusInvestOrderVo.setShareLastTime(new Date());
		 apiBonusInvestOrderService.save(bonusInvestOrderVo);
		 goodsOrderEntity.setOrderInvestStatu(OrderConstant.ORDER_INVEST_STATU_YES);
		 goodsOrderService.update(goodsOrderEntity);
		 //扣除余额
		 userService.reduceUserBalance(userId, investMoney);
		 //增减投资收益
		 userService.addInverst(userId, investMoney, bonusInvestOrderVo.getTotalIncomMoney());
		 //写日志
		 PaymentTask paymentTask=new PaymentTask();
		 paymentTask.setAmount(investMoney);
		 paymentTask.setFee(0);
		 paymentTask.setUserId(user.getUserId());
		 paymentTask.setPayer(user.getUserName());
		 paymentTask.setMemo("【投资】购买资产");
		 paymentTask.setMoneyTypeWallet(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_OUT_CONSUMED);
		 paymentTask.setPaymentType(PluginConstant.PAYMENT_TYPE_OUT);
		 TaskPaymentProducer.addPaymentTaskVo(paymentTask); 
		 
		 return R.ok();
	 }
//	/**
//	 * 注册确认用户
//	 */
//	@Override
//	@Transactional(value="transactionManager")
//	public R  confirmSignUp(Integer userId) {
//		UserEntity user=userService.queryObject(userId);
//    	if(user==null) return R.error("查不到该用户");
//    	logger.info("--------注册用户数据-------user--"+JsonUtil.getJsonByObj(user)); 
////    	int userId=user.getUserId();//注册的新用户
//    	int signupUserId=user.getCreateUserId();//代注册的用户；
//    	String nodeUserName=user.getSignupNodePhone();//节点账号
//    	String recommondUserName=user.getSignupInvitedPhone();//推荐人账号
//    	int recommondeUserId=0;//推荐人UserId
//    	int toNodeUserId=0;//挂载人UserId
//    	
//	   if(StringUtils.isEmpty(nodeUserName)) return R.error("二叉树节点不能为空");
//	   if(StringUtils.isEmpty(recommondUserName)) return R.error("推荐人不能为空");
//    	
//	   
//	   //获取二叉树推荐人
//   	   if(user.getState().equals(ShopConstant.SHOP_USER_STATU_SUCCESS)) return R.error("已经注册成功");
//   	   if(user.getState().equals(ShopConstant.SHOP_USER_STATU_OFFLINE)) return R.error("已下线");
//   	  
//	   //获取二叉树推荐人
//   	   BonusPointsVo nodeBonusPoint=bonusPointsService.queryByUserIdAndBloodType(userId, BonusConstant.BONUS_POINT_BLOODTYPE_BINARYTREE);
//   	   if(nodeBonusPoint!=null) return R.error("已经挂过节点不能修改");
//     	//推荐人树
//       BonusPointsVo recommondBonusPoint=bonusPointsService.queryByUserIdAndBloodType(userId, BonusConstant.BONUS_POINT_BLOODTYPE_RECOMMD);
//   	   if(recommondBonusPoint!=null) return R.error("已经推荐过了不能修改");
//	   
//   	  
//   	   
//   	   
//   	   
//   	   
//   	   //获取代注册人的推荐人列表
//	   	Map<String, Object> params = new HashMap<String,Object>();
//		params.put("invitedUserId", signupUserId);
//		params.put("bloodType",BonusConstant.BONUS_POINT_BLOODTYPE_RECOMMD);
//		
//	    int recommondSum= bonusPointsService.queryTotal(params);
//	    logger.info("--------代注册人的推荐人数量-------recommondSum--"+recommondSum); 
//   	   
//   	  //代注册人 节点
//	  BonusPointsVo binaryTreeSignupBonusPointsEntity=bonusPointsService.queryByUserIdAndBloodType(signupUserId, BonusConstant.BONUS_POINT_BLOODTYPE_BINARYTREE);
// 	  if(binaryTreeSignupBonusPointsEntity==null) return R.error("注册人不存在节点");
//	  //判断代注册人金额 是否满足
//	  UserEntity invitedUser=userService.queryObject(signupUserId);
//	  if(invitedUser==null) return R.error("代注册人不能为空");
//	  //判断推荐人 金额扣除 是否满足
//	  
//	  UserInvestLevelEntity userInvestLevelEntity=userInvestLevelService.queryByLevelType(user.getUserLevelType());
//	  BigDecimal toPayMoney=userInvestLevelEntity.getUserLevelMoneyValue();
//	 
//	  if(invitedUser.getBalance().compareTo(toPayMoney)<0) {
//		  return R.error("代注册人余额不足");
//	  }
// 	 
// 	  //推荐人 节点
// 	  UserEntity recommondNodeUserEntity=userService.queryByUserName(recommondUserName);
// 	  if(recommondNodeUserEntity==null) return R.error("推荐人不存在节点");
// 	  recommondeUserId=recommondNodeUserEntity.getUserId();
// 	 BonusPointsVo binaryTreeRecommondBonusPointsEntity=bonusPointsService.queryByUserIdAndBloodType(recommondeUserId, BonusConstant.BONUS_POINT_BLOODTYPE_BINARYTREE);
// 	  if(binaryTreeRecommondBonusPointsEntity==null) return R.error("推荐人不存在节点");
// 	  //挂载人 节点
// 	  //获取二叉树
//  	  UserEntity nodeUserEntity=userService.queryByUserName(nodeUserName);
//  	  if(nodeUserEntity==null) return R.error("推荐人不存在节点");
//  	   BonusPointsVo binaryTreeBonusPointsEntity=bonusPointsService.queryByUserIdAndBloodType(nodeUserEntity.getUserId(), BonusConstant.BONUS_POINT_BLOODTYPE_BINARYTREE);
//      if(binaryTreeBonusPointsEntity==null) return R.error("推荐人不存在节点");
//      toNodeUserId=nodeUserEntity.getUserId();
//	    //判断是否已经挂满节点
//	  	List<BonusPointsVo> bonusPointsEntities=bonusPointsService.queryByParentUserId(nodeUserEntity.getUserId(), BonusConstant.BONUS_POINT_BLOODTYPE_BINARYTREE);
//	  	if(bonusPointsEntities!=null&&bonusPointsEntities.size()>=2) {
//	  		return R.error("节点已经满了");
//	  	}
//   	    //判断是否可以挂二叉树节点
//   	   //1 验证 节点人------节点人 必须在推荐人的节点树下-包括自己
//   	    //2 代注册人的推荐列表 是否为空验证----如果为空----
//   	    //获取推荐人的节点
//	  	 logger.info("--------推荐人ID----"+recommondeUserId+"--------节点ID----"+toNodeUserId+"--------代注册ID----"+signupUserId); 
//   	    //挂载节点都是推荐人的节点的子节点 或者自己
//	  	if(toNodeUserId!=recommondeUserId) {
//	  	    //to挂载人节点人 节点集合
//			List<String> strNodeUserIds=StringUtils.splitToList(binaryTreeBonusPointsEntity.getInvitedParentUserIds(), BonusConstant.INVITED_MEMBER_IDS_SPLIT);
//			List<Integer>  teamNodeUserIds=new ArrayList<>();
//			for(String str:strNodeUserIds) {
//				teamNodeUserIds.add(Integer.valueOf(str));
//			}
//			logger.info("---------strNodeUserIds----"+JsonUtil.getJsonByObj(teamNodeUserIds));
//			if(!teamNodeUserIds.contains(recommondeUserId)) {
//				return R.error("节点不在推荐人的关系树上");
//			}
//			if(recommondSum==0) {//注册的新用户 必须挂载在 节点树的左边 ----即是判断 是否被占用
//				if(bonusPointsEntities!=null&&bonusPointsEntities.size()==1) {
//					BonusPointsVo brotherNode=bonusPointsEntities.get(0);
//					if(brotherNode.getInvitedUserId()!=null) {
//						return R.error("该节点上已经挂载了");
//					}
//			  	}
//			}
////			if(recommondSum==0) {
////		   		   //节点人必须在 ---推荐人--节点树 左边
////		   		   
////		   		   //节点人左右都可以放
////		   	   }
////		   	   
////		   	  if(recommondSum>0) {
////				   //节点人必须在 ---推荐人--节点树--子节点，包括自己
////				   //节点人左右都可以放
////			   }
//			
//			
//			
//	  	}
//	  //可以挂
//  		nodeBonusPoint=new BonusPointsVo();
//  		nodeBonusPoint.setUserId(userId);
//  		nodeBonusPoint.setBonusInvitedSum(0);//团队人数
//  		nodeBonusPoint.setBonusMeInvitedPoints(new BigDecimal(0));//个人业绩 直接业绩
//  		nodeBonusPoint.setBonusTeamInvitedPoints(new BigDecimal(0));//团队业绩
//  		nodeBonusPoint.setCreateTime(new Date());
//  		
//  		nodeBonusPoint.setInvitedParentUserIds("");
//  		String invitedParentUserIds=""+binaryTreeBonusPointsEntity.getUserId()+BonusConstant.INVITED_MEMBER_IDS_SPLIT+binaryTreeBonusPointsEntity.getInvitedParentUserIds();
//  		nodeBonusPoint.setInvitedParentUserIds(invitedParentUserIds);//所有父类节点
//  		
//  		nodeBonusPoint.setUserRoleType(0);
//  		nodeBonusPoint.setUserNamedType(0);
//  		nodeBonusPoint.setUpdateTime(new Date());
//  		nodeBonusPoint.setStatus(0);
//  		nodeBonusPoint.setCanGenerateQr("0");//默认不能生成推广二维码
//  		nodeBonusPoint.setBloodType(BonusConstant.BONUS_POINT_BLOODTYPE_BINARYTREE);
//  		//优先左 再挂右边
//      	if(bonusPointsEntities==null||bonusPointsEntities.size()==0) {
//      		//挂左边
//      		nodeBonusPoint.setInvitedUserId(binaryTreeBonusPointsEntity.getUserId());
//      	}
//      	if(bonusPointsEntities!=null&&bonusPointsEntities.size()==1) {
//      		//挂右边
//      		nodeBonusPoint.setInvitedRightUserId(binaryTreeBonusPointsEntity.getUserId());
//      	}
//      	logger.info("-------------二叉树关系数据-------nodeBonusPoint-----"+JsonUtil.getJsonByObj(nodeBonusPoint));
//    	//----end----节点验证完成
//    	//推荐人 的节点 与代注册人 的节点----必须满足节点树 
//		//获取注册人节点
//		List<String> strSignUpNodeUserIds=StringUtils.splitToList(binaryTreeSignupBonusPointsEntity.getInvitedParentUserIds(), BonusConstant.INVITED_MEMBER_IDS_SPLIT);
//		List<Integer>  teamSignUpNodeUserIds=new ArrayList<>();
//		for(String str:strSignUpNodeUserIds) {
//			teamSignUpNodeUserIds.add(Integer.valueOf(str));
//		}
//		
//		//推荐人 节点集合
//		List<String> strRecommondNodeUserIds=StringUtils.splitToList(binaryTreeRecommondBonusPointsEntity.getInvitedParentUserIds(), BonusConstant.INVITED_MEMBER_IDS_SPLIT);
//		List<Integer>  teamRecommondNodeUserIds=new ArrayList<>();
//		for(String str:strRecommondNodeUserIds) {
//			teamRecommondNodeUserIds.add(Integer.valueOf(str));
//		}
//		if(recommondeUserId!=signupUserId) {
//			if(!(teamSignUpNodeUserIds.contains(recommondeUserId)||teamRecommondNodeUserIds.contains(signupUserId))) {
//				return R.error("代注册与推荐人不能跨区域注册");
//			}
//		}
//		
//    	
//    	//推荐人树
//    	recommondBonusPoint=new BonusPointsVo();
//    	recommondBonusPoint.setUserId(userId);
//    	recommondBonusPoint.setBonusInvitedSum(0);//团队人数
//    	recommondBonusPoint.setBonusMeInvitedPoints(new BigDecimal(0));//个人业绩 直接业绩
//    	recommondBonusPoint.setBonusTeamInvitedPoints(new BigDecimal(0));//团队业绩
//    	recommondBonusPoint.setCreateTime(new Date());
//    	recommondBonusPoint.setInvitedUserId(recommondNodeUserEntity.getUserId());
//    	recommondBonusPoint.setInvitedParentUserIds("");//推荐关系
//    	//获取推荐人 关系
//    	BonusPointsVo recommdBonusPointsEntity=bonusPointsService.queryByUserIdAndBloodType(recommondeUserId, BonusConstant.BONUS_POINT_BLOODTYPE_RECOMMD);
//    	String invitedRecommondParentUserIds=""+recommondeUserId+BonusConstant.INVITED_MEMBER_IDS_SPLIT+recommdBonusPointsEntity.getInvitedParentUserIds();
//    	recommondBonusPoint.setInvitedParentUserIds(invitedRecommondParentUserIds);//所有父类节点
//		recommondBonusPoint.setUserRoleType(0);
//		recommondBonusPoint.setUserNamedType(0);
//		recommondBonusPoint.setUpdateTime(new Date());
//		recommondBonusPoint.setStatus(0);
//		recommondBonusPoint.setCanGenerateQr("0");//默认不能生成推广二维码
//		recommondBonusPoint.setBloodType(BonusConstant.BONUS_POINT_BLOODTYPE_RECOMMD);
//		logger.info("-------------推荐关系数据-------recommondBonusPoint-----"+JsonUtil.getJsonByObj(recommondBonusPoint));
//		
//		bonusPointsService.save(nodeBonusPoint);
//		bonusPointsService.save(recommondBonusPoint);
//		user.setState(ShopConstant.SHOP_USER_STATU_SUCCESS);
//		user.setUpdatetime(new Date());
//		
//		
//	    user.setTotalIncome(toPayMoney);
//		
//    	userService.update(user);
//    	//生成一个消费订单
//    	//代扣金额
//    	userService.reduceUserBalance(invitedUser.getUserId(),toPayMoney);
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
//    	//生成一个订单
//		String goodSn=userInvestLevelService.queryByLevelType(user.getUserLevelType()).getGoodsSn();
//		GoodsEntity goodsEntity= goodsDao.queryByGoodSn(goodSn);
//		int sum=1;
//		Map<String, Object> resultMap =goodsOrderService.toSubmitBuyInverstGoodOrder(user, goodsEntity.getId(),sum,0);//代付0
//		Map<String, GoodsOrderEntity> orderInfoMap =(Map<String, GoodsOrderEntity>) resultMap.get("data");
//		GoodsOrderEntity orderInfo =(GoodsOrderEntity) orderInfoMap.get("orderInfo");
//		//消费订单 推荐奖励
//		BonusTaskVo bonusTaskVo=new BonusTaskVo(orderInfo.getOrderNo(), orderInfo.getOrderType(), user.getUserId(), orderInfo.getTotalPayPrice());
//		
//		bonusConsumedMallOrderShare(bonusTaskVo);
//		
//		return R.ok();
//	}
//	
	/**
	 * 奖励机制：获取队里待处理分红消息
	 */
	@Override
	@Transactional(value="transactionManager")
	public void  bonusShare(BonusTaskVo bonusTaskVo) {
		try {
			LOG.info("===================================开始处理获取队里待处理分红消息====request====bonusTaskVo==="+ObjToStringUtil.objToString(bonusTaskVo));
			 //加日志流水，回头要提取出去
			 int consumedUserId=bonusTaskVo.getConsumedUserId();
			 BigDecimal consumedPointMoney=bonusTaskVo.getConsumedPointMoney(); 
			
			 BonusPointsLogVo bonusPointsLog= new BonusPointsLogVo();
			 bonusPointsLog.setUserId(consumedUserId);
			 bonusPointsLog.setPointAmonut(consumedPointMoney);
			 bonusPointsLog.setCreateTime(new Date());
			 bonusPointsLog.setOrderNo(bonusTaskVo.getOrderNo());
			 apiBonusPointsLogService.save(bonusPointsLog);
			
			BonusPointsVo bonusPoint=bonusPointService.getUserPoint(bonusTaskVo.getConsumedUserId(),BonusConstant.BONUS_PARENT_TYPE_MORE_TREE);
			if(bonusPoint!=null) {
				//获取父类推荐关系集合
				List<String> strRecommendUserIds=StringUtils.splitToList(bonusPoint.getInvitedParentUserIds(), BonusConstant.INVITED_MEMBER_IDS_SPLIT);
				List<Integer>  teamRecommendUserIds=new ArrayList<>();
				for(String str:strRecommendUserIds) {
					teamRecommendUserIds.add(Integer.valueOf(str));
				}
				//获得父级推荐人ID
				Integer invitedUserId = bonusPoint.getInvitedUserId();
				LOG.info("==============获取父类推荐关系=======开始处理==========request===teamRecommendUserIds:"+ObjToStringUtil.objToString(teamRecommendUserIds));			

				LOG.info("==============第一步 更新自己业绩和父级推荐人业绩=======开始处理==========request==="
						+ "======consumedUserId====:"+ObjToStringUtil.objToString(consumedUserId)
						+ "======teamUserIds======:"+ObjToStringUtil.objToString(invitedUserId));
				//第一步 更新自己业绩和父级推荐人业绩
				updateMeAndParentPoint(consumedUserId,invitedUserId,consumedPointMoney);
				
				 
				LOG.info("==============第二步更新消费者的职位=====开始处理==========request=bonusPoint======:"+ObjToStringUtil.objToString(bonusPoint));

				//第二步更新消费者自己的职位
				updateBonusBole(bonusPoint);
				LOG.info("==============第三步更新父类管理者的职位=====开始处理==========request==="+ "======teamRecommendUserIds======:"+ObjToStringUtil.objToString(teamRecommendUserIds));

				//第三步更新父类管理者的职位
				if(teamRecommendUserIds.size()>0){
					updateBonusRoles(teamRecommendUserIds);
				}
				
				
				LOG.info("==============第四步计算提成奖励=======开始处理==========request======invitedUserId======:"+ObjToStringUtil.objToString(invitedUserId));
				//第四步计算销售提成奖励
				if(invitedUserId!=null&&invitedUserId>0){
					updateBonusCommissionPoint(invitedUserId,consumedPointMoney);
				}
				
				LOG.info("==============第五步更新二级店长收益奖励======开始处理==========request= ===consumedPointMoney======:"+ObjToStringUtil.objToString(consumedPointMoney));
				//第五步更新二级店收益奖励 
				updateBonusAward(consumedPointMoney);
				
				LOG.info("==============第六步更新三级店长加权分红======开始处理==========request======consumedPointMoney======:"+ObjToStringUtil.objToString(consumedPointMoney));

				//第六步更新三级店长加权分红
				updateBonusWeighting(consumedPointMoney);
				
				LOG.info("over");
				
			}else {
				LOG.info("==============如果是新用户 ====初始化用户===开始处理==========request==="+ "======consumedUserId======:"+ObjToStringUtil.objToString(consumedUserId));
				bonusPoint=bonusPointService.initUserPoint(consumedUserId, 0,0);
				
				LOG.info("==============如果是新用户 ====更新自己积分===开始处理==========request==="	+ "======consumedUserId======:"+ObjToStringUtil.objToString(consumedUserId));
				//更新自己个人业绩
				bonusPointService.addBonusMeInvitedPoints(consumedUserId, consumedPointMoney);
				
				
				bonusPoint.setBonusMeInvitedPoints( consumedPointMoney) ;
				
			}
			
			LOG.info("========================处理结束===========获取队里待处理分红消息====end====bonusPoint==="+ObjToStringUtil.objToString(bonusPoint));
		} catch (Exception e) {
			LOG.info("=======================理分红消息===出现=异常处理=="+ObjToStringUtil.objToString(e));
			e.printStackTrace();
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
		bonusPointService.addBonusMeInvitedPoints(memberId, addPoint);
		if(!CollectionUtils.isEmpty(teamUserIds)) {
			bonusPointService.addBonusTeamInvitedPoints(teamUserIds, addPoint);
		}
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
		bonusPointService.addBonusMeInvitedPoints(memberId, addPoint);
		if(invitedUserId !=null&&invitedUserId>0 ) {
			bonusPointService.addBonusTeamInvitedPoints(invitedUserId, addPoint);
		}
	}
	
	
	/**销售提成
	  * @param teamUserIds
	 * @param addPoint 
	 */
	 private void updateBonusCommissionPoint(Integer invitedUserId,BigDecimal addPoint) {
		 HashMap<String, Integer> managerPoint=new HashMap<>();
			LOG.info("===========进入updateBonusManagementPoint====计算销售提成===="
					+ "-----consumedUserId:"+ObjToStringUtil.objToString(invitedUserId)
					+ "----------addPoint:"+ObjToStringUtil.objToString(addPoint));
			
			String RECOMMEND_PERFORMANCE_BASE_MONEY=bonusAppCache.getBonusParamCache().getBonusParamMap().get(BonusConstant.RECOMMEND_PERFORMANCE_BASE_MONEY);////获得推广基础收益 800 元
			
			BonusPointsVo tempBonusPoint = bonusPointService.getUesrPoint(invitedUserId);
			int roleType=tempBonusPoint.getUserRoleType();
			BigDecimal  base_money= new BigDecimal(RECOMMEND_PERFORMANCE_BASE_MONEY);//个人基础收益
			BigDecimal addBalance= new BigDecimal(0);//最终受益
			
			 switch (roleType) {
				case BonusConstant.BONUS_MEMBER_ROLE_TYPE_member:
					break;
				case BonusConstant.BONUS_MEMBER_ROLE_TYPE_manager:
					addBalance=base_money;
					break;
				case BonusConstant.BONUS_MEMBER_ROLE_TYPE_shopper_one:
					addBalance=base_money;
					break;
				case BonusConstant.BONUS_MEMBER_ROLE_TYPE_shopper_two:
					addBalance=base_money;
					break;
				case BonusConstant.BONUS_MEMBER_ROLE_TYPE_shopper_three:
					addBalance=base_money;
					break;
				case BonusConstant.BONUS_MEMBER_ROLE_TYPE_shopper_four:
					addBalance=base_money;
					break;
				default:
					break;
				} 
		
			 if(addBalance.compareTo(new BigDecimal(0))>0) {
				 LOG.info("===========计算销售提成结束保存==="
				 		+ "tempBonusPoint.getUserId()======:"+tempBonusPoint.getUserId()
				 		+ "---------addBalance=====:"+addBalance);	
				 userService.addUserBalanceAndTotalIncome(tempBonusPoint.getUserId(), addBalance);
				 PaymentTask paymentTask=new PaymentTask();
				 paymentTask.setAmount(addBalance);
				 paymentTask.setFee(0);
				 paymentTask.setUserId(tempBonusPoint.getUserId());
				 paymentTask.setPayer(tempBonusPoint.getUserName());
				 paymentTask.setMemo("销售提成");
				 paymentTask.setMoneyTypeWallet(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_BONUS_RECOMMED);
				 paymentTask.setPaymentType(PluginConstant.PAYMENT_TYPE_IN);
				 TaskPaymentProducer.addPaymentTaskVo(paymentTask); 
			 }
	 }
	 /**
	  * 店长收益奖励
	  * @param addPoint
	  */
	 private void updateBonusAward(BigDecimal addPoint) {
			
			//获取基础参数
			String PROJECT_MONEY=bonusAppCache.getBonusParamCache().getBonusParamMap().get(BonusConstant.RECOMMEND_PERFORMANCE_PROJECT_MONEY);//项目利润=600元
			String PROJECT_PERCENT=bonusAppCache.getBonusParamCache().getBonusParamMap().get(BonusConstant.RECOMMEND_PERFORMANCE_PROJECT_PERCENT);//项目利润10%的平均分红
			
			BigDecimal project_money= new BigDecimal(0);
			BigDecimal project_percent=  new BigDecimal(0);
			if(!StringUtils.isEmpty(PROJECT_MONEY)) {
				project_money= new BigDecimal(PROJECT_MONEY);
			}
			if(!StringUtils.isEmpty(PROJECT_PERCENT)) {
				project_percent= new BigDecimal(PROJECT_PERCENT);
			}
			
			
			int shopper_two_sum=bonusPointService.countByUserRoleType(BonusConstant.BONUS_MEMBER_ROLE_TYPE_shopper_two);
			int shopper_three_sum=bonusPointService.countByUserRoleType(BonusConstant.BONUS_MEMBER_ROLE_TYPE_shopper_three);
			int shopper_four_sum=bonusPointService.countByUserRoleType(BonusConstant.BONUS_MEMBER_ROLE_TYPE_shopper_four);
			
			BigDecimal shopper_free=new BigDecimal(0);
			int total_shopper_sum = shopper_two_sum+shopper_three_sum+shopper_four_sum;
			if(total_shopper_sum>0) {
				shopper_free=project_money.multiply(project_percent).divide(new BigDecimal(100))//除以100//利润金额乘以利润比例
						.divide(new BigDecimal(total_shopper_sum),2);//除以总人数
			}
		 
 
			List<Integer> memberRoles=new ArrayList<>();
			memberRoles.add(BonusConstant.BONUS_MEMBER_ROLE_TYPE_shopper_two);
			memberRoles.add(BonusConstant.BONUS_MEMBER_ROLE_TYPE_shopper_three);
			memberRoles.add(BonusConstant.BONUS_MEMBER_ROLE_TYPE_shopper_four);
			
			//店长目前应该不多，所以暂不做批量处理
			List<BonusPointsVo> bonusPoints=bonusPointService.findByUserRoleType(memberRoles);
			for(BonusPointsVo bonusPoint:bonusPoints) {
				int roleType=bonusPoint.getUserRoleType();
				BigDecimal addBalance=new BigDecimal(0);
				 //职位角色：0 普通，1经理，2 一星店长，3二星店长，4三星店长 5四星店长
				switch (roleType) {
					case BonusConstant.BONUS_MEMBER_ROLE_TYPE_shopper_two:
						addBalance= addBalance.add(shopper_free);
						break;
					case BonusConstant.BONUS_MEMBER_ROLE_TYPE_shopper_three:
						addBalance= addBalance.add(shopper_free);
						break;
					case BonusConstant.BONUS_MEMBER_ROLE_TYPE_shopper_four:
						addBalance= addBalance.add(shopper_free);
						break;
					default:
						break;
					}
				
				 if(addBalance.compareTo(new BigDecimal(0))>0) {
					 LOG.info("===========收益奖励---结束保存==="
						 		+ "tempBonusPoint.getUserId()======:"+bonusPoint.getUserId()
						 		+ "---------addBalance=====:"+addBalance);	
					 userService.addUserBalanceAndTotalIncome(bonusPoint.getUserId(), addBalance);
					 PaymentTask paymentTask=new PaymentTask();
					 paymentTask.setAmount(addBalance);
					 paymentTask.setFee(0);
					 paymentTask.setPayer(bonusPoint.getUserName());
					 paymentTask.setUserId(bonusPoint.getUserId());
					 paymentTask.setMemo("收益奖励");
					 paymentTask.setMoneyTypeWallet(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_BONUS_DAY);
					 paymentTask.setPaymentType(PluginConstant.PAYMENT_TYPE_IN);
					 TaskPaymentProducer.addPaymentTaskVo(paymentTask);
				 }
			}
		
	}
	 /**
	  *加权分红
	  * @param addPoint
	  */
	 private void updateBonusWeighting(BigDecimal addPoint) {
			
			//获取基础参数
			String PROJECT_MONEY=bonusAppCache.getBonusParamCache().getBonusParamMap().get(BonusConstant.RECOMMEND_PERFORMANCE_PROJECT_MONEY);//项目利润=600元
			String PROJECT_PERCENT=bonusAppCache.getBonusParamCache().getBonusParamMap().get(BonusConstant.RECOMMEND_PERFORMANCE_PROJECT_PERCENT);//项目利润10%的平均分红
			
			BigDecimal project_money= new BigDecimal(0);
			BigDecimal project_percent=  new BigDecimal(0);
			if(!StringUtils.isEmpty(PROJECT_MONEY)) {
				project_money= new BigDecimal(PROJECT_MONEY);
			}
			if(!StringUtils.isEmpty(PROJECT_PERCENT)) {
				project_percent= new BigDecimal(PROJECT_PERCENT);
			}
			
			
			int shopper_three_sum=bonusPointService.countByUserRoleType(BonusConstant.BONUS_MEMBER_ROLE_TYPE_shopper_three);
			int shopper_four_sum=bonusPointService.countByUserRoleType(BonusConstant.BONUS_MEMBER_ROLE_TYPE_shopper_four);
			
			BigDecimal shopper_free=new BigDecimal(0);
			int total_shopper_sum = shopper_three_sum+shopper_four_sum;
			if(total_shopper_sum>0) {
				shopper_free=project_money.multiply(project_percent).divide(new BigDecimal(100))//除以100//利润金额乘以利润比例
						.divide(new BigDecimal(total_shopper_sum),2);//除以总人数
			}
 
			List<Integer> memberRoles=new ArrayList<>();
			memberRoles.add(BonusConstant.BONUS_MEMBER_ROLE_TYPE_shopper_three);
			memberRoles.add(BonusConstant.BONUS_MEMBER_ROLE_TYPE_shopper_four);
			
			//董事目前应该不多，所以暂不做批量处理
			List<BonusPointsVo> bonusPoints=bonusPointService.findByUserRoleType(memberRoles);
			for(BonusPointsVo bonusPoint:bonusPoints) {
				int roleType=bonusPoint.getUserRoleType();
				BigDecimal addBalance=new BigDecimal(0);
				 //职位角色：0 普通，1经理，2 一星店长，3二星店长，4三星店长 5四星店长
				switch (roleType) {
					case BonusConstant.BONUS_MEMBER_ROLE_TYPE_shopper_three:
						addBalance= addBalance.add(shopper_free);
						break;
					case BonusConstant.BONUS_MEMBER_ROLE_TYPE_shopper_four:
						addBalance= addBalance.add(shopper_free);
						break;
					default:
						break;
					}
				
				 if(addBalance.compareTo(new BigDecimal(0))>0) {
					//addPoint*tempRate/100 分
					 LOG.info("===========加权分红---结束保存==="
						 		+ "tempBonusPoint.getUserId()======:"+bonusPoint.getUserId()
						 		+ "---------addBalance=====:"+addBalance);	
					 userService.addUserBalanceAndTotalIncome(bonusPoint.getUserId(), addBalance);
					 PaymentTask paymentTask=new PaymentTask();
					 paymentTask.setAmount(addBalance);
					 paymentTask.setFee(0);
					 paymentTask.setUserId(bonusPoint.getUserId());
					 paymentTask.setPayer(bonusPoint.getUserName());
					 paymentTask.setMemo("加权分红");
					 paymentTask.setMoneyTypeWallet(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_BONUS_JQ);
					 paymentTask.setPaymentType(PluginConstant.PAYMENT_TYPE_IN);
					 TaskPaymentProducer.addPaymentTaskVo(paymentTask);
				 }
				 
				
				
			}
		
	}
		
		
	 /**
	 * 更新职位	 一级一级升 或者一级一级的降级
	 * @param teamUserIds 自己和所有上级用户
	 */
 	private void updateBonusRoles(List<Integer> teamUserIds) {
 		 
	         	//分页计算 直到统计完或者分完为止
				int pageSize=10;
				int teamPageSize=teamUserIds.size()/pageSize+1;
			//int i=0;
				for(int pageNo=0;pageNo<teamPageSize;pageNo++) {
					
					List<Integer> subTeamUserIds=new ArrayList<>();
					for(int j=pageNo*10;j<teamUserIds.size()&&j<((pageNo+1)*pageSize);j++) {
						subTeamUserIds.add(teamUserIds.get(j));
					}
					LOG.info("-------------subTeamUserIds---------------------------"+ObjToStringUtil.objToString(subTeamUserIds));
					List<BonusPointsVo> bonusPoints=bonusPointService.getUserPointByUserIds(subTeamUserIds);
					Collections.reverse(bonusPoints);
					for(BonusPointsVo bonusPoint:bonusPoints) {
					LOG.info("------Collections.reverse----bonusPoint.UserId--------------"+bonusPoint.getUserId()+"---------userRoleType()-----:"+bonusPoint.getUserRoleType());	
						updateBonusBole(bonusPoint);
					}
					
			}
	} 
	/**
	 * 升级职位
	 * @param bonusPoint
	 */
	private void updateBonusBole(BonusPointsVo bonusPoint) { 
		//获取职位晋升的审核条件 常量 		
		/*String BONUS_MEMBER_ROLE_TYPE_ZHUREN_LAST_MONEY_STR=bonusAppCache.getBonusParamCache().getBonusParamMap().get(BonusConstant.BONUS_MEMBER_ROLE_TYPE_ZHUREN_LAST_MONEY_KEY);
		if(StringUtils.isNotBlank(BONUS_MEMBER_ROLE_TYPE_ZHUREN_LAST_MONEY_STR)) {
			BONUS_MEMBER_ROLE_TYPE_ZHUREN_LAST_MONEY=Integer.valueOf(BONUS_MEMBER_ROLE_TYPE_ZHUREN_LAST_MONEY_STR);
		 }
		 */
		int roleType=bonusPoint.getUserRoleType();
		LOG.info("---------updateBonusBole----roleType---------------------------"+ObjToStringUtil.objToString(roleType));
		List<BonusPointsVo> suBonusPoints=new ArrayList<>();
		 //职位角色：0 普通，1经理，2 一星店长，3二星店长，4三星店长 5四星店长
		switch (roleType) {
			case BonusConstant.BONUS_MEMBER_ROLE_TYPE_member://0 普通
				LOG.info("---------updateBonusBole--获取：团队业绩-------------------------"+bonusPoint.getBonusTeamInvitedPoints());
				LOG.info("---------updateBonusBole----  最低业绩--------------------------"+BONUS_MEMBER_ROLE_TYPE_manager_least_money);
				suBonusPoints=bonusPointService.findBySubBonusPoint(bonusPoint.getUserId(),BonusConstant.BONUS_PARENT_TYPE_BINARY_TREE);
				//如果个人大于1999 最低业绩
				if(bonusPoint.getBonusTeamInvitedPoints().compareTo(BONUS_MEMBER_ROLE_TYPE_manager_least_money) >= 0
						&&suBonusPoints.size()>0) {
					LOG.info("---------updateBonusBole----bonusPoint.getUserId()-------------------"+bonusPoint.getUserId());
					bonusPointService.updateUserRole(bonusPoint.getUserId(), BonusConstant.BONUS_MEMBER_ROLE_TYPE_manager, null);
				}else{
					LOG.info("---------开通生成二维码的权限--userId-"+bonusPoint.getUserId());
					bonusPoint.setCanGenerateQr("1");//开通生成二维码的权限
					bonusPointService.update(bonusPoint);
					LOG.info("---------开通生成二维码的权限--getCanGenerateQr-"+bonusPoint.getCanGenerateQr());
				}
				
				break;
			case BonusConstant.BONUS_MEMBER_ROLE_TYPE_manager:
				Collection<Integer>   userRoleTypes = new ArrayList();
				userRoleTypes.add(BonusConstant.BONUS_MEMBER_ROLE_TYPE_manager);
				userRoleTypes.add(BonusConstant.BONUS_MEMBER_ROLE_TYPE_shopper_one);
				LOG.info("---------查询所有经理和店长数--的集合为------"+ObjToStringUtil.objToString(userRoleTypes));
				LOG.info("---------查询所有经理和店长数--的集合为--当前人的ID----"+ObjToStringUtil.objToString(bonusPoint.getUserId()));
				
				//下级的所有经理和店长数
				int sub_manager_Sum = bonusPointService.findSubTeamRoleSum(bonusPoint.getUserId(),userRoleTypes);
				LOG.info("---------下级的所有经理和店长数--------"+ObjToStringUtil.objToString(sub_manager_Sum));
				
				//下级所有人数
				int sub_member_Sum = bonusPointService.findBySubBonusPoint(bonusPoint.getUserId(),BonusConstant.BONUS_PARENT_TYPE_BINARY_TREE).size();
				LOG.info("---------至少三个经理、升级为一级店长-----subRoleSum:--------"+ObjToStringUtil.objToString(sub_manager_Sum));
				//至少三个经理、升级为一级店长
				if(sub_manager_Sum>=3) {
					bonusPointService.updateUserRole(bonusPoint.getUserId(), BonusConstant.BONUS_MEMBER_ROLE_TYPE_shopper_one, null);
				}else if(sub_member_Sum<1){//降级为普通会员
					bonusPointService.updateUserRole(bonusPoint.getUserId(), BonusConstant.BONUS_MEMBER_ROLE_TYPE_member, null);
				}
				
				break;
			case BonusConstant.BONUS_MEMBER_ROLE_TYPE_shopper_one:
				//查询下级的一星店长数
				int subRoleSum_shopper_one = bonusPointService.findSubTeamRoleSum(bonusPoint.getUserId(),BonusConstant.BONUS_MEMBER_ROLE_TYPE_shopper_one);
				//查询下级的经理数+店长数
				Collection<Integer>   userRoleTypes_1 = new ArrayList();
				userRoleTypes_1.add(BonusConstant.BONUS_MEMBER_ROLE_TYPE_manager);
				userRoleTypes_1.add(BonusConstant.BONUS_MEMBER_ROLE_TYPE_shopper_one);
				int sub_manager_Sum_2 = bonusPointService.findSubTeamRoleSum(bonusPoint.getUserId(),userRoleTypes_1);

				LOG.info("---------至少三个一级店长、升级为二级店长----一星店长数:--------"+ObjToStringUtil.objToString(subRoleSum_shopper_one));
				//至少三个一级店长、升级为二级店长
				if(subRoleSum_shopper_one>=3) {
					//第一步更新自己的职位	
					LOG.info("---------升级为二星店长---第一步-----更新自己的职位:--父级集合teamRecommendUserIds------:"+ObjToStringUtil.objToString(bonusPoint.getUserId()));
					bonusPointService.updateUserRole(bonusPoint.getUserId(), BonusConstant.BONUS_MEMBER_ROLE_TYPE_shopper_two, null);
					
					List<String> strRecommendUserIds=StringUtils.splitToList(bonusPoint.getInvitedParentUserIds(), BonusConstant.INVITED_MEMBER_IDS_SPLIT);
//					LOG.info("---------升级为二星店长---第二步-----断掉推荐关系----getInvitedParentUserIds()---:"+bonusPoint.getInvitedParentUserIds()+"----getInvitedUserId:--"+bonusPoint.getInvitedUserId());
					//此步放到更新职位的时候一起更新，否则事务不提交，未生效
//					bonusPointService.update(bonusPoint);
					LOG.info("---------升级为二星店长---第三步--更新父类管理者的职位:--父级集合strRecommendUserIds------:"+ObjToStringUtil.objToString(strRecommendUserIds));
					//第二步更新父类管理者的职位--降级
					if(strRecommendUserIds.size()>0){
						List<Integer> teamRecommendUserIds_2 = new ArrayList<Integer>();
						for(String userId:strRecommendUserIds) {
							teamRecommendUserIds_2.add(Integer.parseInt(userId));
						}
						LOG.info("---------第三步--更新父类管理者的职位--teamRecommendUserIds_2----:"+ObjToStringUtil.objToString(teamRecommendUserIds_2));
						updateBonusRoles(teamRecommendUserIds_2);
					}
					//第三步更更新下级所有人的父级推荐关系
					List<BonusPointsVo> subBonusPointsVos = bonusPointService.findSubsByParentUserIds(bonusPoint.getUserId(),BonusConstant.BONUS_PARENT_TYPE_BINARY_TREE);
					LOG.info("---------升级为二星店长---第四步--更新下级所有人的父级推荐关系subBonusPointsVos------:"+ObjToStringUtil.objToString(subBonusPointsVos));
					if(subBonusPointsVos.size()>0){
						updateSubBonusRoles(subBonusPointsVos,bonusPoint.getUserId());
					}
				}else if(sub_manager_Sum_2<3){//如果下级少于3个经理+店长，自动降级为经理
					LOG.info("---------如果下级少于3个经理+店长，自动降级为经理----经理+店长总数为:--------"+ObjToStringUtil.objToString(sub_manager_Sum_2));
					bonusPointService.updateUserRole(bonusPoint.getUserId(), BonusConstant.BONUS_MEMBER_ROLE_TYPE_manager, null);
				}
				break;
				
			case BonusConstant.BONUS_MEMBER_ROLE_TYPE_shopper_two:
				int subRoleSum_shopper_one_2 = bonusPointService.findSubTeamRoleSum(bonusPoint.getUserId(),BonusConstant.BONUS_MEMBER_ROLE_TYPE_shopper_one);
				//查询下级的经理数+店长数
				Collection<Integer>   userRoleTypes_2 = new ArrayList();
				userRoleTypes_2.add(BonusConstant.BONUS_MEMBER_ROLE_TYPE_manager);
				userRoleTypes_2.add(BonusConstant.BONUS_MEMBER_ROLE_TYPE_shopper_one);
				int sub_manager_shoppers_Sum_2 = bonusPointService.findSubTeamRoleSum(bonusPoint.getUserId(),userRoleTypes_2);
				
				LOG.info("---------至少9个店长 升级为三级店长----1星店长总数:--------"+ObjToStringUtil.objToString(subRoleSum_shopper_one_2));
				LOG.info("---------店长+经理数:--------"+ObjToStringUtil.objToString(sub_manager_shoppers_Sum_2));
				//至少9个店长 升级为三级店长
				if(subRoleSum_shopper_one_2>=9) {
					bonusPointService.updateUserRole(bonusPoint.getUserId(), BonusConstant.BONUS_MEMBER_ROLE_TYPE_shopper_three, null);
				}else if(sub_manager_shoppers_Sum_2<3){//店长+经理数少于3个直接降级为经理
					bonusPointService.updateUserRole(bonusPoint.getUserId(), BonusConstant.BONUS_MEMBER_ROLE_TYPE_manager, null);
				}else if(subRoleSum_shopper_one_2<3&&sub_manager_shoppers_Sum_2>=3){//1星店长少于3个，同时1星店长+经理数大于3 降级为一星店长
					bonusPointService.updateUserRole(bonusPoint.getUserId(), BonusConstant.BONUS_MEMBER_ROLE_TYPE_shopper_one, null);
				}
				break;
					 
			case BonusConstant.BONUS_MEMBER_ROLE_TYPE_shopper_three:
				int subRoleSum_shopper_one_3 = bonusPointService.findSubTeamRoleSum(bonusPoint.getUserId(),BonusConstant.BONUS_MEMBER_ROLE_TYPE_shopper_one);
				LOG.info("---------至少81个店长 升级为四级店长----subRoleSum_shopper_one_3:--------"+ObjToStringUtil.objToString(subRoleSum_shopper_one_3));

				//至少81个店长 升级为四级店长
				if(subRoleSum_shopper_one_3>=81) {
					bonusPointService.updateUserRole(bonusPoint.getUserId(), BonusConstant.BONUS_MEMBER_ROLE_TYPE_shopper_four, null);
				}else if(subRoleSum_shopper_one_3<9){//少于9个店长 降级为二级店长
					bonusPointService.updateUserRole(bonusPoint.getUserId(), BonusConstant.BONUS_MEMBER_ROLE_TYPE_shopper_two, null);
				}
				break;
		 
			default:
				break;
			}
	}



	/**
	 * 更新下级所有人的父级推荐关系集合
	 * @param teamRecommendUserIds2
	 */
	private void updateSubBonusRoles(List<BonusPointsVo> subBonusPointsVos,int ParentUserId) {
	 	//分页计算 直到统计完或者分完为止
		int pageSize=10;
		int teamPageSize=subBonusPointsVos.size()/pageSize+1;
	//int i=0;
		for(int pageNo=0;pageNo<teamPageSize;pageNo++) {
			
			List<BonusPointsVo> bonusPoints=new ArrayList<>();
			for(int j=pageNo*10;j<subBonusPointsVos.size()&&j<((pageNo+1)*pageSize);j++) {
				bonusPoints.add(subBonusPointsVos.get(j));
			}
			LOG.info("--------------------更新下级所有人的父级推荐关系集合-----所有人数---bonusPoints.size():----"+ObjToStringUtil.objToString(bonusPoints.size()));
			for(BonusPointsVo bonusPoint:bonusPoints) {
				LOG.info("--------------------更新下级所有人的父级推荐关系集合------用户-【"+bonusPoint.getUserId()+"】-----原来的父级集合为【"+bonusPoint.getInvitedParentUserIds()+"】");
				//获取父类推荐关系集合 进行拆分，剪除后只留下前面一部分的数据，例如：10810#-10798#-10795#-，拆分后只留下:10810#-10798#-
				String invitedParentUserIds =bonusPoint.getInvitedParentUserIds();
				List<String> strRecommendUserIds=StringUtils.splitToList(bonusPoint.getInvitedParentUserIds(), ParentUserId+BonusConstant.INVITED_MEMBER_IDS_SPLIT);
				if(strRecommendUserIds.size()>1){//必须保证拆分数据的左边部分不能为空
					LOG.info("--------------------更新下级所有人的父级推荐关系集合-----strRecommendUserIds----"+ObjToStringUtil.objToString(strRecommendUserIds));
					 invitedParentUserIds = strRecommendUserIds.get(0)+ParentUserId+BonusConstant.INVITED_MEMBER_IDS_SPLIT;
					 LOG.info("----------------------用户：----【"+bonusPoint.getUserId()+"】-----更新后父级集合为【"+invitedParentUserIds+"】");
					 //拆分后只留下:10810#-10798#-
					 bonusPoint.setInvitedParentUserIds(invitedParentUserIds);
					 bonusPointService.update(bonusPoint);
					
				}else if(bonusPoint.getInvitedParentUserIds().startsWith(ParentUserId+BonusConstant.INVITED_MEMBER_IDS_SPLIT)){//必须保证拆分数据的左边部分不能为空,否则直接设置
					invitedParentUserIds = ParentUserId+BonusConstant.INVITED_MEMBER_IDS_SPLIT;
					LOG.info("----------------------用户：----【"+bonusPoint.getUserId()+"】-----更新后父级集合为【"+invitedParentUserIds+"】");
					//拆分后只留下:10810#-10798#-
					bonusPoint.setInvitedParentUserIds(invitedParentUserIds);
					bonusPointService.update(bonusPoint);
				}
				
				
				LOG.info("----------------------用户：---断掉推荐关系-------------------");
				 UserInvitedChgLogVo userInvitedChgLog = new UserInvitedChgLogVo();
				 userInvitedChgLog.setUserId(bonusPoint.getUserId());
				 userInvitedChgLog.setOldInvitedParentUserIds(bonusPoint.getInvitedParentUserIds());
				 userInvitedChgLog.setNewInvitedParentUserIds(invitedParentUserIds);
				 LOG.info("----------------------用户：----【"+bonusPoint.getUserId()+"】---断掉推荐关系-------------------");
				userInvitedChgLogService.save(userInvitedChgLog );
				
			}
			
	}
	}
	
	
	
	 /**
	 * 更新别称
	 * @param bonusPoint
	 */
	/*
	 private void updateBonusRoleNamed(BonusPointVo bonusPoint,int consumedPointMoney) {
	  int roleNamedType=bonusPoint.getUserMamedType();
	  
	  if(roleNamedType<BonusConstant.BONUS_MEMBER_ROLE_NAME_TYPE_DS) {
		  long money = bonusPoint.getBonusMeInvitedPoints() + consumedPointMoney;
		  
	    	 if(money>=BonusConstant.BONUS_MEMBER_ROLE_NAMED_TYPE_DR_LAST_MONEY&&money<BonusConstant.BONUS_MEMBER_ROLE_NAMED_TYPE_DR_MAX_MONEY) {
	 			
	 			bonusPointService.updateUserRole(bonusPoint.getUserId(), null, BonusConstant.BONUS_MEMBER_ROLE_NAME_TYPE_DR);
	 		}else if(money>=BonusConstant.BONUS_MEMBER_ROLE_NAMED_TYPE_DR_MAX_MONEY&&money<BonusConstant.BONUS_MEMBER_ROLE_NAMED_TYPE_TS_MAX_MONEY) {
	 			
	 			bonusPointService.updateUserRole(bonusPoint.getUserId(), null, BonusConstant.BONUS_MEMBER_ROLE_NAME_TYPE_TS);
	 		}else if(money>=BonusConstant.BONUS_MEMBER_ROLE_NAMED_TYPE_TS_MAX_MONEY) {
	 			
	 			bonusPointService.updateUserRole(bonusPoint.getUserId(), null, BonusConstant.BONUS_MEMBER_ROLE_NAME_TYPE_DS);
	 		}
	    }
		
	}
	*/
	
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
		//推荐奖励
		recommondConsumedBonus(userId,consumedMoney);
		//节点奖励
		nodedConsumedBonus(userId,consumedMoney);
		//量化奖励
		plotdConsumedBonus(userId,consumedMoney);
		
	}
	private void recommondConsumedBonus(int userId,BigDecimal consumedMoney) {
		BonusPointsVo bonusPoint=bonusPointService.getUserPoint(userId,BonusConstant.BONUS_PARENT_TYPE_MORE_TREE);
		if(bonusPoint==null) return;
		double firstLevelRate=Double.valueOf(sysConfigService.getValue(BonusConstant.BONUS_RECOMMED_LEVEL_SP_RATE+"_"+1, "-1"));//分层查看是否存在特殊值
		double secondLevelRate=Double.valueOf(sysConfigService.getValue(BonusConstant.BONUS_RECOMMED_LEVEL_SP_RATE+"_"+2, "-1"));//分层查看是否存在特殊值
		Queue<Double> rateQueue = new LinkedList<>();
		rateQueue.offer(firstLevelRate); //进队
		rateQueue.offer(secondLevelRate);//进队列
		
		//获取父类推荐关系集合
		List<String> strRecommendUserIds=StringUtils.splitToList(bonusPoint.getInvitedParentUserIds(), BonusConstant.INVITED_MEMBER_IDS_SPLIT);
		List<Integer>  teamRecommendUserIds=new ArrayList<>();
		for(String str:strRecommendUserIds) {
			teamRecommendUserIds.add(Integer.valueOf(str));
		}
		for(int i=0;i<strRecommendUserIds.size();i++) {
			teamRecommendUserIds.add(Integer.valueOf(strRecommendUserIds.get(i)));
		}
		if(teamRecommendUserIds.size()==0)return ;
		Map<Integer,UserEntity> userEntities=userService.getByUserIds(teamRecommendUserIds);			
		
		for(int i=0;i<teamRecommendUserIds.size()&&!rateQueue.isEmpty();i++) {
			double levelRate=rateQueue.peek();//取出优先奖励比例 仅仅取出
			BigDecimal addBalance=consumedMoney.multiply(new BigDecimal(levelRate));
			UserEntity toUserEntity=userEntities.get(teamRecommendUserIds.get(i));
			if(toUserEntity==null) continue;
			
			//判断用户是否可以满足幸运奖条件
			String poolDateNumber="PPD_"+DateUtils.formatYYYYMMDD(new Date());
			BonusPoolJoinMemberVo bonusPoolJoinMemberVo=apiBonusPoolJoinMemberService.queryByDateNumberAndMemberId(poolDateNumber, toUserEntity.getUserId());
			
//			if(bonusPoolJoinMemberVo!=null&&toUserEntity.getUserLevelTypeValue().compareTo(bonusPoolJoinMemberVo.getPoolJoinMoney())<=0) {
//				 LOG.info("=====消费======今日已经满奖励额度了========:"+toUserEntity.getUserId()
//					 		+ "---------addBalance=====:"+addBalance);	
//				 continue;
//			}
			//投资扣除判读
//			if(toUserEntity.getInvestIncomeMoney().compareTo(addBalance)<0) {
//				LOG.info("===========该用的投资杠杆资产不足扣除========:"+toUserEntity.getUserId()
//				+ "---------addBalance=====:"+addBalance);	
//				continue;
//			}
			
			if(addBalance.compareTo(new BigDecimal(0))>0) {
				 LOG.info("=====消费======计算推荐提成结束保存==="
				 		+ "tempBonusPoint.getUserId()======:"+toUserEntity.getUserId()
				 		+ "---------addBalance=====:"+addBalance);	
				 rateQueue.poll();//取出优先奖励比例 并删除
				 //增加钱包收益，总收益，增加资产总收益 减少杠杆收益
//				 userService.addUserBalanceAndTotalIncomeAndReduceInverst(toUserEntity.getUserId(), addBalance);
				 userService.addUserBalanceAndTotalIncome(toUserEntity.getUserId(), addBalance);
				 //添加日收益
				 apiBonusPoolJoinMemberService.incrBonusPoolJoinMemberMoney(poolDateNumber, toUserEntity.getUserId(), addBalance);
				 //写日志
				 PaymentTask paymentTask=new PaymentTask();
				 paymentTask.setAmount(addBalance);
				 paymentTask.setFee(0);
				 paymentTask.setUserId(toUserEntity.getUserId());
				 paymentTask.setPayer(toUserEntity.getUserName());
				 paymentTask.setMemo("【消费】推荐奖励");
				 paymentTask.setMoneyTypeWallet(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_BONUS_RECOMMED_CONSUMED);
				 paymentTask.setPaymentType(PluginConstant.PAYMENT_TYPE_IN);
				 TaskPaymentProducer.addPaymentTaskVo(paymentTask); 
			 }
		}
	}
	private void nodedConsumedBonus(int userId,BigDecimal consumedMoney) {
		BonusPointsVo bonusPoint=bonusPointService.getUserPoint(userId,BonusConstant.BONUS_PARENT_TYPE_BINARY_TREE);
		if(bonusPoint==null) return;
		int maxLevel=Integer.valueOf(sysConfigService.getValue(BonusConstant.BONUS_NODE_LEVEL_MAX, "0"));
		if(maxLevel==0) return;
		double defaultRate=Double.valueOf(sysConfigService.getValue(BonusConstant.BONUS_NODE_LEVEL_MAX_DEFAULT_RATE, "0"));
	     //节点奖励都是相同的
		Queue<Double> rateQueue = new LinkedList<>();
		for(int i=0;i<maxLevel;i++) {
			rateQueue.offer(defaultRate); 
		}
		//获取父类推荐关系集合
		List<String> strUserIds=StringUtils.splitToList(bonusPoint.getInvitedParentUserIds(), BonusConstant.INVITED_MEMBER_IDS_SPLIT);
		List<Integer>  teamUserIds=new ArrayList<>();
		for(String str:strUserIds) {
			teamUserIds.add(Integer.valueOf(str));
		}
		for(int i=0;i<teamUserIds.size()&&i<maxLevel;i++) {
			teamUserIds.add(Integer.valueOf(teamUserIds.get(i)));
		}
		if(teamUserIds.size()==0)return ;
		Map<Integer,UserEntity> userEntities=userService.getByUserIds(teamUserIds);			
		
		for(int i=0;i<teamUserIds.size()&&!rateQueue.isEmpty();i++) {
			double levelRate=rateQueue.peek();//取出优先奖励比例 仅仅取出
			BigDecimal addBalance=consumedMoney.multiply(new BigDecimal(levelRate));
			UserEntity toUserEntity=userEntities.get(teamUserIds.get(i));
			if(toUserEntity==null) continue;
			//判断用户是否可以满足幸运奖条件
			
			//判单层级
			if(i>toUserEntity.getUserNodeBonusLevel()) {
				 LOG.info("=======消费====幸运奖励层级不够========:"+toUserEntity.getUserId()+"======================="+toUserEntity.getUserNodeBonusLevel()
					 		+ "---------addBalance=====:"+addBalance);	
			}
			
			//是否已经满额了
			String poolDateNumber="PPD_"+DateUtils.formatYYYYMMDD(new Date());
			BonusPoolJoinMemberVo bonusPoolJoinMemberVo=apiBonusPoolJoinMemberService.queryByDateNumberAndMemberId(poolDateNumber, toUserEntity.getUserId());
//			if(bonusPoolJoinMemberVo!=null&&toUserEntity.getUserLevelTypeValue().compareTo(bonusPoolJoinMemberVo.getPoolJoinMoney())<=0) {
//				 LOG.info("===========今日已经满奖励额度了========:"+toUserEntity.getUserId()
//					 		+ "---------addBalance=====:"+addBalance);	
//				 continue;
//			}
			
			//投资扣除判读
//			if(toUserEntity.getInvestIncomeMoney().compareTo(addBalance)<0) {
//				LOG.info("=========节点==该用的投资杠杆资产不足扣除========:"+toUserEntity.getUserId()
//				+ "---------addBalance=====:"+addBalance);	
//				continue;
//			}
			
			
			if(addBalance.compareTo(new BigDecimal(0))>0) {
				 LOG.info("===========计算推荐提成结束保存==="
				 		+ "tempBonusPoint.getUserId()======:"+toUserEntity.getUserId()
				 		+ "---------addBalance=====:"+addBalance);	
				
				 rateQueue.poll();//取出优先奖励比例 并删除
				 //增加钱包收益，总收益，增加资产总收益 减少杠杆收益
				 userService.addUserBalanceAndTotalIncome(toUserEntity.getUserId(), addBalance);
//				 userService.addUserBalanceAndTotalIncomeAndReduceInverst(toUserEntity.getUserId(), addBalance);
				 //添加日收益 统计
				 apiBonusPoolJoinMemberService.incrBonusPoolJoinMemberMoney(poolDateNumber, toUserEntity.getUserId(), addBalance);
				 
				 //写日志
				 PaymentTask paymentTask=new PaymentTask();
				 paymentTask.setAmount(addBalance);
				 paymentTask.setFee(0);
				 paymentTask.setUserId(toUserEntity.getUserId());
				 paymentTask.setPayer(toUserEntity.getUserName());
				 paymentTask.setMemo("【权益】幸运奖励");
				 paymentTask.setMoneyTypeWallet(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_BONUS_NODE_CONSUMED);
				 paymentTask.setPaymentType(PluginConstant.PAYMENT_TYPE_IN);
				 TaskPaymentProducer.addPaymentTaskVo(paymentTask); 
			 }
			
		}
	}
	private void plotdConsumedBonus(int userId,BigDecimal consumedMoney) {
		BonusPointsVo bonusPoint=bonusPointService.getUserPoint(userId,BonusConstant.BONUS_PARENT_TYPE_BINARY_TREE);
		if(bonusPoint==null) return;
		int maxLevel=Integer.valueOf(sysConfigService.getValue(BonusConstant.BONUS_NODE_POLT_LEVEL_MAX, "0"));
		if(maxLevel==0) return;
		double defaultRate=Double.valueOf(sysConfigService.getValue(BonusConstant.BONUS_NODE_POLT_LEVEL_MAX_DEFAULT_RATE, "0"));
	     //节点奖励都是相同的
		Queue<Double> rateQueue = new LinkedList<>();
		for(int i=0;i<maxLevel;i++) {
			rateQueue.offer(defaultRate); 
		}
		//获取父类推荐关系集合
		List<String> strUserIds=StringUtils.splitToList(bonusPoint.getInvitedParentUserIds(), BonusConstant.INVITED_MEMBER_IDS_SPLIT);
		List<Integer>  teamUserIds=new ArrayList<>();
		for(String str:strUserIds) {
			teamUserIds.add(Integer.valueOf(str));
		}
		for(int i=0;i<teamUserIds.size();i++) {//所有列出 判断是否满足需求
			teamUserIds.add(Integer.valueOf(teamUserIds.get(i)));
		}
		if(teamUserIds.size()==0)return ;
		
		Map<Integer,UserEntity> userEntities=userService.getByUserIds(teamUserIds);			
		
		for(int i=0;i<teamUserIds.size()&&!rateQueue.isEmpty();i++) {
			double levelRate=rateQueue.peek();//取出优先奖励比例 仅仅取出
			BigDecimal addBalance=consumedMoney.multiply(new BigDecimal(levelRate));
			UserEntity toUserEntity=userEntities.get(teamUserIds.get(i));
			if(toUserEntity==null) continue;
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
		  	if(leftBonusPointsVo.getBonusTeamInvitedPoints().compareTo(rightBonusPointsVo.getBonusTeamInvitedPoints())>0) {
		  		minUserId=rightBonusPointsVo.getUserId();
		  	}
		  	
		  	if(!teamUserIds.contains(minUserId)) {//判断是否是小区业务
		  		//不是小区
		  		LOG.info("===========奖励=======由于不是来源小区，所有不参与分红===");	
		  		continue;
		  	}
			
		    //判断用户是否可以满足幸运奖条件
			String poolDateNumber="PPD_"+DateUtils.formatYYYYMMDD(new Date());
			BonusPoolJoinMemberVo bonusPoolJoinMemberVo=apiBonusPoolJoinMemberService.queryByDateNumberAndMemberId(poolDateNumber, toUserEntity.getUserId());
			
//			if(bonusPoolJoinMemberVo!=null&&toUserEntity.getUserLevelTypeValue().compareTo(bonusPoolJoinMemberVo.getPoolJoinMoney())<=0) {
//				 LOG.info("===========今日已经满奖励额度了========:"+toUserEntity.getUserId()
//					 		+ "---------addBalance=====:"+addBalance);	
//				 continue;
//			}
			
			//投资扣除判读
//			if(toUserEntity.getInvestIncomeMoney().compareTo(addBalance)<0) {
//				LOG.info("=========小区奖励==该用的投资杠杆资产不足扣除========:"+toUserEntity.getUserId()
//				+ "---------addBalance=====:"+addBalance);	
//				continue;
//			}
		  	
			if(addBalance.compareTo(new BigDecimal(0))>0) {
				LOG.info("===========计算推荐提成结束保存==="
				 		+ "tempBonusPoint.getUserId()======:"+toUserEntity.getUserId()
				 		+ "---------addBalance=====:"+addBalance);	
				 rateQueue.poll();//取出优先奖励比例 并删除
				 //增加钱包收益，总收益，增加资产总收益 减少杠杆收益
				 userService.addUserBalanceAndTotalIncome(toUserEntity.getUserId(), addBalance);
					
//				 userService.addUserBalanceAndTotalIncomeAndReduceInverst(toUserEntity.getUserId(), addBalance);
				 //添加日收益
				 apiBonusPoolJoinMemberService.incrBonusPoolJoinMemberMoney(poolDateNumber, toUserEntity.getUserId(), addBalance);
				 //写日志
				 PaymentTask paymentTask=new PaymentTask();
				 paymentTask.setAmount(addBalance);
				 paymentTask.setFee(0);
				 paymentTask.setUserId(toUserEntity.getUserId());
				 paymentTask.setPayer(toUserEntity.getUserName());
				 paymentTask.setMemo("【权益】量碰奖励");
				 paymentTask.setMoneyTypeWallet(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_BONUS_NODE_CONSUMED_POLT);
				 paymentTask.setPaymentType(PluginConstant.PAYMENT_TYPE_IN);
				 TaskPaymentProducer.addPaymentTaskVo(paymentTask); 
			 }
		}
		
//		if(toPayCount<maxLevel) {//放入奖金池
//			
//			BigDecimal addBalance=consumedMoney.multiply(new BigDecimal(defaultRate));
//			String poolDateNumber=BonusConstant.BONUS_POOL_NUMBER_COMPANY;
//			BonusPoolVo bonusPoolVo =apiBonusPoolService.queryByPoolNumber(poolDateNumber);
//			if(bonusPoolVo==null) {
//				bonusPoolVo=new BonusPoolVo();
//				bonusPoolVo.setCreatetime(new Date());
//				bonusPoolVo.setPoolMoney(addBalance);
//				bonusPoolVo.setPoolNumber(poolDateNumber);
//				bonusPoolVo.setPoolRemark("");
//				bonusPoolVo.setPoolType(BonusConstant.BONUS_POOL_TYPE_PLOT);
//				bonusPoolVo.setStatus(0);
//				bonusPoolVo.setUpdatetime(new Date());
//				apiBonusPoolService.save(bonusPoolVo);
//			}else {
//				apiBonusPoolService.incrBonusPoolMoney(poolDateNumber, addBalance);
//			}
//			//写日志
//			 PaymentTask paymentTask=new PaymentTask();
//			 paymentTask.setAmount(addBalance);
//			 paymentTask.setFee(0);
//			 paymentTask.setUserId(BonusConstant.BONUS_POOL_JOIN_MEMBER_ID);
//			 paymentTask.setMemo("【量碰】消费沉淀奖励");
//			 paymentTask.setMoneyTypeWallet(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_BONUS_NODE_CONSUMED_POLT);
//			 paymentTask.setPaymentType(PluginConstant.PAYMENT_TYPE_IN);
//			 TaskPaymentProducer.addPaymentTaskVo(paymentTask); 
//			
//		}
	}
	
	
	
	/**
	 * 每日返利订单处理
	 * @param bonusTaskVo
	 */
	@Transactional
	public void  bonusEveryShare() {
		double rate=0.0005;//默认万7
		int start=Integer.valueOf(sysConfigService.getValue(BonusConstant.BONUS_EVERY_DAY_RANDOM_MIN, "40"));
		int end=Integer.valueOf(sysConfigService.getValue(BonusConstant.BONUS_EVERY_DAY_RANDOM_MIN, "50"));
		int randomType=Integer.valueOf(sysConfigService.getValue(BonusConstant.BONUS_EVERY_DAY_RANDOM_TYPE, "1"));
		int randomRate=RandomUtil.getRandom(start, end);
		Integer denom=Integer.valueOf(sysConfigService.getValue(BonusConstant.BONUS_EVERY_DAY_RANDOM_DENOM, "100000"));
		//生成随机返利比例 存入数据库
		rate=randomRate/denom;
		if(randomType==0) {
			rate=start/denom;
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
//		List<BonusInvestOrderVo> bonusInvestOrderVos=apiBonusInvestOrderService.queryListToShareInvestOrdersByDate(new Date());
		List<UserEntity> userInversts=userService.queryListToShareInvestUserByDate(new Date());
		 LOG.info("----------------------该奖励分红------投资奖励人数量-------------------ordersum:"+userInversts.size());
		 LOG.info("----------------------该奖励分红------投资订单数量-------------------rate:"+rate);
		 for(UserEntity userEntity:userInversts) {
			 toShareBonusEveryOrder(userEntity,rate);	 
		 }
	}
	private void toShareBonusEveryOrder(UserEntity userEntity,double rate) {
		//客户收益
		BigDecimal incomeMoney=userEntity.getSurplusInvestMoney().multiply(new BigDecimal(rate));
		
		LOG.info("----------------------该奖励分红------投资订单数量-------------------ordersum:"+incomeMoney);
		userEntity.setSurplusInvestMoney(userEntity.getSurplusInvestMoney().subtract(incomeMoney));
		userEntity.setInvestIncomeMoney(userEntity.getInvestIncomeMoney().add(incomeMoney));
		userEntity.setShareInvestLastTime(new Date());
		userService.update(userEntity);
		int inverstUserId=userEntity.getUserId();
		//推荐奖励
		recommondInvestBonus(inverstUserId,incomeMoney);
		//节点奖励
		nodeInvestBonus(inverstUserId,incomeMoney);
		//量化奖励
		plotInvestBonus(inverstUserId,incomeMoney);
		
	}
	//推荐奖励
	private void recommondInvestBonus(int  inverstUserId,BigDecimal incomeMoney) {
		BonusPointsVo bonusPoint=bonusPointService.getUserPoint(inverstUserId,BonusConstant.BONUS_PARENT_TYPE_MORE_TREE);
		if(bonusPoint==null) return;
		double firstLevelRate=Double.valueOf(sysConfigService.getValue(BonusConstant.BONUS_RECOMMED_LEVEL_SP_RATE+"_"+1, "-1"));//分层查看是否存在特殊值
		double secondLevelRate=Double.valueOf(sysConfigService.getValue(BonusConstant.BONUS_RECOMMED_LEVEL_SP_RATE+"_"+2, "-1"));//分层查看是否存在特殊值
		Queue<Double> rateQueue = new LinkedList<>();
		rateQueue.offer(firstLevelRate); //进队
		rateQueue.offer(secondLevelRate);//进队列
		
		//获取父类推荐关系集合
		List<String> strRecommendUserIds=StringUtils.splitToList(bonusPoint.getInvitedParentUserIds(), BonusConstant.INVITED_MEMBER_IDS_SPLIT);
		List<Integer>  teamRecommendUserIds=new ArrayList<>();
		for(String str:strRecommendUserIds) {
			teamRecommendUserIds.add(Integer.valueOf(str));
		}
		for(int i=0;i<strRecommendUserIds.size();i++) {
			teamRecommendUserIds.add(Integer.valueOf(strRecommendUserIds.get(i)));
		}
		if(teamRecommendUserIds.size()==0)return ;
		Map<Integer,UserEntity> userEntities=userService.getByUserIds(teamRecommendUserIds);			
		for(int i=0;i<teamRecommendUserIds.size()&&!rateQueue.isEmpty();i++) {
			double levelRate=rateQueue.peek();//取出优先奖励比例 仅仅取出
			BigDecimal addBalance=incomeMoney.multiply(new BigDecimal(levelRate));
			UserEntity toUserEntity=userEntities.get(teamRecommendUserIds.get(i));
			if(toUserEntity==null) continue;
			//判断用户是否可以满足幸运奖条件
			String poolDateNumber="PPD_"+DateUtils.formatYYYYMMDD(new Date());
			BonusPoolJoinMemberVo bonusPoolJoinMemberVo=apiBonusPoolJoinMemberService.queryByDateNumberAndMemberId(poolDateNumber, toUserEntity.getUserId());
			//今日奖金限额
//			if(bonusPoolJoinMemberVo!=null&&toUserEntity.getUserLevelTypeValue().compareTo(bonusPoolJoinMemberVo.getPoolJoinMoney())<=0) {
//				 LOG.info("===========今日已经满奖励额度了========:"+toUserEntity.getUserId()
//					 		+ "---------addBalance=====:"+addBalance);	
//				 continue;
//			}
			//投资扣除判读
			if(toUserEntity.getInvestIncomeMoney().compareTo(addBalance)<0) {
				LOG.info("===========该用的投资杠杆资产不足扣除========:"+toUserEntity.getUserId()
				+ "---------addBalance=====:"+addBalance);	
				continue;
			}
			
			
			if(addBalance.compareTo(new BigDecimal(0))>0) {
				 LOG.info("===========计算推荐提成结束保存==="
				 		+ "tempBonusPoint.getUserId()======:"+toUserEntity.getUserId()
				 		+ "---------addBalance=====:"+addBalance);	
				 
				 rateQueue.poll();//取出优先奖励比例 并删除
				
				 //增加钱包收益，总收益，增加资产总收益 减少杠杆收益
				 userService.addUserBalanceAndTotalIncomeAndReduceInverst(toUserEntity.getUserId(), addBalance);
				 //添加日收益
				 apiBonusPoolJoinMemberService.incrBonusPoolJoinMemberMoney(poolDateNumber, toUserEntity.getUserId(), addBalance);
				 //写日志
				 PaymentTask paymentTask=new PaymentTask();
				 paymentTask.setAmount(addBalance);
				 paymentTask.setFee(0);
				 paymentTask.setUserId(toUserEntity.getUserId());
				 paymentTask.setPayer(toUserEntity.getUserName());
				 paymentTask.setMemo("【权益】推荐奖励");
				 paymentTask.setMoneyTypeWallet(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_BONUS_RECOMMED_INVEST);
				 paymentTask.setPaymentType(PluginConstant.PAYMENT_TYPE_IN);
				 TaskPaymentProducer.addPaymentTaskVo(paymentTask); 
			 }
		}
	}
	//节点奖励
	private void nodeInvestBonus(int inverstUserId,BigDecimal incomeMoney) {
		BonusPointsVo bonusPoint=bonusPointService.getUserPoint(inverstUserId,BonusConstant.BONUS_PARENT_TYPE_BINARY_TREE);
		if(bonusPoint==null) return;
		int maxLevel=Integer.valueOf(sysConfigService.getValue(BonusConstant.BONUS_NODE_LEVEL_MAX, "0"));
		if(maxLevel==0) return;
		double defaultRate=Double.valueOf(sysConfigService.getValue(BonusConstant.BONUS_NODE_LEVEL_MAX_DEFAULT_RATE, "0"));
	     //节点奖励都是相同的
		Queue<Double> rateQueue = new LinkedList<>();
		for(int i=0;i<maxLevel;i++) {
			rateQueue.offer(defaultRate); 
		}
		//获取父类推荐关系集合
		List<String> strUserIds=StringUtils.splitToList(bonusPoint.getInvitedParentUserIds(), BonusConstant.INVITED_MEMBER_IDS_SPLIT);
		List<Integer>  teamUserIds=new ArrayList<>();
		for(String str:strUserIds) {
			teamUserIds.add(Integer.valueOf(str));
		}
		for(int i=0;i<teamUserIds.size();i++) {
			teamUserIds.add(Integer.valueOf(teamUserIds.get(i)));
		}
		if(teamUserIds.size()==0)return ;
		Map<Integer,UserEntity> userEntities=userService.getByUserIds(teamUserIds);			
		
		for(int i=0;i<teamUserIds.size()&&!rateQueue.isEmpty();i++) {
			double levelRate=rateQueue.peek();//取出优先奖励比例 仅仅取出
			BigDecimal addBalance=incomeMoney.multiply(new BigDecimal(levelRate));
			UserEntity toUserEntity=userEntities.get(teamUserIds.get(i));
			if(toUserEntity==null) continue;
			//判断用户是否可以满足幸运奖条件
			
			//判单层级
			if(i>toUserEntity.getUserNodeBonusLevel()) {
				 LOG.info("===========幸运奖励层级不够========:"+toUserEntity.getUserId()+"======================="+toUserEntity.getUserNodeBonusLevel()
					 		+ "---------addBalance=====:"+addBalance);	
			}
			
			//是否已经满额了
			String poolDateNumber="PPD_"+DateUtils.formatYYYYMMDD(new Date());
			BonusPoolJoinMemberVo bonusPoolJoinMemberVo=apiBonusPoolJoinMemberService.queryByDateNumberAndMemberId(poolDateNumber, toUserEntity.getUserId());
//			if(bonusPoolJoinMemberVo!=null&&toUserEntity.getUserLevelTypeValue().compareTo(bonusPoolJoinMemberVo.getPoolJoinMoney())<0) {
//				 LOG.info("===========今日已经满奖励额度了========:"+toUserEntity.getUserId()
//					 		+ "---------addBalance=====:"+addBalance);	
//				 continue;
//			}
			//投资扣除判读
			if(toUserEntity.getInvestIncomeMoney().compareTo(addBalance)<0) {
				LOG.info("=========节点==该用的投资杠杆资产不足扣除========:"+toUserEntity.getUserId()
				+ "---------addBalance=====:"+addBalance);	
				continue;
			}
			
			if(addBalance.compareTo(new BigDecimal(0))>0) {
				 LOG.info("===========计算推荐提成结束保存==="
				 		+ "tempBonusPoint.getUserId()======:"+toUserEntity.getUserId()
				 		+ "---------addBalance=====:"+addBalance);	
				 rateQueue.poll();//取出优先奖励比例 并删除
				 //增加钱包收益，总收益，增加资产总收益 减少杠杆收益
				 userService.addUserBalanceAndTotalIncomeAndReduceInverst(toUserEntity.getUserId(), addBalance);
				 //添加日收益 统计
				 apiBonusPoolJoinMemberService.incrBonusPoolJoinMemberMoney(poolDateNumber, toUserEntity.getUserId(), addBalance);
				 //写日志
				 PaymentTask paymentTask=new PaymentTask();
				 paymentTask.setAmount(addBalance);
				 paymentTask.setFee(0);
				 paymentTask.setUserId(toUserEntity.getUserId());
				 paymentTask.setPayer(toUserEntity.getUserName());
				 paymentTask.setMemo("【权益】幸运奖励");
				 paymentTask.setMoneyTypeWallet(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_BONUS_NODE_INVEST);
				 paymentTask.setPaymentType(PluginConstant.PAYMENT_TYPE_IN);
				 TaskPaymentProducer.addPaymentTaskVo(paymentTask); 
			 }
			
		}
			
	}
	//小区奖励
	private void plotInvestBonus(int inverstUserId,BigDecimal incomeMoney) {
		BonusPointsVo bonusPoint=bonusPointService.getUserPoint(inverstUserId,BonusConstant.BONUS_PARENT_TYPE_BINARY_TREE);
		if(bonusPoint==null) return;
		int maxLevel=Integer.valueOf(sysConfigService.getValue(BonusConstant.BONUS_NODE_POLT_LEVEL_MAX, "0"));
		if(maxLevel==0) return;
		double defaultRate=Double.valueOf(sysConfigService.getValue(BonusConstant.BONUS_NODE_POLT_LEVEL_MAX_DEFAULT_RATE, "0"));
	     //节点奖励都是相同的
		Queue<Double> rateQueue = new LinkedList<>();
		for(int i=0;i<maxLevel;i++) {
			rateQueue.offer(defaultRate); 
		}
		//获取父类推荐关系集合
		List<String> strUserIds=StringUtils.splitToList(bonusPoint.getInvitedParentUserIds(), BonusConstant.INVITED_MEMBER_IDS_SPLIT);
		List<Integer>  teamUserIds=new ArrayList<>();
		for(String str:strUserIds) {
			teamUserIds.add(Integer.valueOf(str));
		}
		for(int i=0;i<teamUserIds.size();i++) {//所有列出 判断是否满足需求
			teamUserIds.add(Integer.valueOf(teamUserIds.get(i)));
		}
		if(teamUserIds.size()==0)return ;
		
		Map<Integer,UserEntity> userEntities=userService.getByUserIds(teamUserIds);			
		
		for(int i=0;i<teamUserIds.size()&&!rateQueue.isEmpty();i++) {
			double levelRate=rateQueue.peek();//取出优先奖励比例 仅仅取出
			BigDecimal addBalance=incomeMoney.multiply(new BigDecimal(levelRate));
			UserEntity toUserEntity=userEntities.get(teamUserIds.get(i));
			if(toUserEntity==null) continue;
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
		  	if(leftBonusPointsVo.getBonusTeamInvitedPoints().compareTo(rightBonusPointsVo.getBonusTeamInvitedPoints())>0) {
		  		minUserId=rightBonusPointsVo.getUserId();
		  	}
		  	
		  	if(!teamUserIds.contains(minUserId)) {//判断是否是小区业务
		  		//不是小区
		  		LOG.info("===========奖励=======由于不是来源小区，所有不参与分红===");	
		  		continue;
		  	}
			
		    //判断用户是否可以满足幸运奖条件
			String poolDateNumber="PPD_"+DateUtils.formatYYYYMMDD(new Date());
//			BonusPoolJoinMemberVo bonusPoolJoinMemberVo=apiBonusPoolJoinMemberService.queryByDateNumberAndMemberId(poolDateNumber, toUserEntity.getUserId());
//			if(bonusPoolJoinMemberVo!=null&&toUserEntity.getUserLevelTypeValue().compareTo(bonusPoolJoinMemberVo.getPoolJoinMoney())<0) {
//				 LOG.info("===========今日已经满奖励额度了========:"+toUserEntity.getUserId()
//					 		+ "---------addBalance=====:"+addBalance);	
//			}
			
			//投资扣除判读
			if(toUserEntity.getInvestIncomeMoney().compareTo(addBalance)<0) {
				LOG.info("=========小区奖励==该用的投资杠杆资产不足扣除========:"+toUserEntity.getUserId()
				+ "---------addBalance=====:"+addBalance);	
				continue;
			}
		  	
			if(addBalance.compareTo(new BigDecimal(0))>0) {
				LOG.info("===========计算推荐提成结束保存==="
				 		+ "tempBonusPoint.getUserId()======:"+toUserEntity.getUserId()
				 		+ "---------addBalance=====:"+addBalance);	
				 rateQueue.poll();//取出优先奖励比例 并删除
				 //增加钱包收益，总收益，增加资产总收益 减少杠杆收益
				 userService.addUserBalanceAndTotalIncomeAndReduceInverst(toUserEntity.getUserId(), addBalance);
				 //添加日收益
				 apiBonusPoolJoinMemberService.incrBonusPoolJoinMemberMoney(poolDateNumber, toUserEntity.getUserId(), addBalance);
				 //写日志
				 PaymentTask paymentTask=new PaymentTask();
				 paymentTask.setAmount(addBalance);
				 paymentTask.setFee(0);
				 paymentTask.setUserId(toUserEntity.getUserId());
				 paymentTask.setPayer(toUserEntity.getUserName());
				 paymentTask.setMemo("【权益】量碰奖励");
				 paymentTask.setMoneyTypeWallet(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_BONUS_NODE_INVEST_POLT);
				 paymentTask.setPaymentType(PluginConstant.PAYMENT_TYPE_IN);
				 TaskPaymentProducer.addPaymentTaskVo(paymentTask); 
			 }
		}
//		if(toPayCount<maxLevel) {//放入奖金池
//			
//			BigDecimal addBalance=incomeMoney.multiply(new BigDecimal(defaultRate));
//			String poolDateNumber=BonusConstant.BONUS_POOL_NUMBER_COMPANY;
//			BonusPoolVo bonusPoolVo =apiBonusPoolService.queryByPoolNumber(poolDateNumber);
//			if(bonusPoolVo==null) {
//				bonusPoolVo=new BonusPoolVo();
//				bonusPoolVo.setCreatetime(new Date());
//				bonusPoolVo.setPoolMoney(addBalance);
//				bonusPoolVo.setPoolNumber(poolDateNumber);
//				bonusPoolVo.setPoolRemark("");
//				bonusPoolVo.setPoolType(BonusConstant.BONUS_POOL_TYPE_PLOT);
//				bonusPoolVo.setStatus(0);
//				bonusPoolVo.setUpdatetime(new Date());
//				apiBonusPoolService.save(bonusPoolVo);
//			}else {
//				apiBonusPoolService.incrBonusPoolMoney(poolDateNumber, addBalance);
//			}
//			//写日志
//			 PaymentTask paymentTask=new PaymentTask();
//			 paymentTask.setAmount(addBalance);
//			 paymentTask.setFee(0);
//			 paymentTask.setUserId(BonusConstant.BONUS_POOL_JOIN_MEMBER_ID);
//			 paymentTask.setMemo("【量碰】沉淀奖励");
//			 paymentTask.setMoneyTypeWallet(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_BONUS_NODE_INVEST_POLT);
//			 paymentTask.setPaymentType(PluginConstant.PAYMENT_TYPE_IN);
//			 TaskPaymentProducer.addPaymentTaskVo(paymentTask); 
//			
//		}
	}
	
	
}
