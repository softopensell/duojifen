package com.platform.facade;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.platform.cache.IdWorkCache;
import com.platform.constants.PluginConstant;
import com.platform.entity.PaymentInfoEntity;
import com.platform.entity.PaymentLogEntity;
import com.platform.entity.PlatformMonitorEntity;
import com.platform.entity.PlatformMonitorStadetailEntity;
import com.platform.entity.UserEntity;
import com.platform.mq.model.PaymentTask;
import com.platform.mq.producer.TaskPaymentProducer;
import com.platform.service.PaymentInfoService;
import com.platform.service.PaymentLogService;
import com.platform.service.PaymentOutService;
import com.platform.service.PlatformMonitorService;
import com.platform.service.PlatformMonitorStadetailService;
import com.platform.service.UserService;
import com.platform.util.ShopConstant;
import com.platform.util.constants.TradeStatus;
import com.platform.utils.DateUtils;
import com.platform.utils.HttpUtils;
import com.platform.utils.JsonUtil;
import com.platform.utils.RandomUtil;
import com.platform.utils.StringUtils;



@Component
public class PaymentPluginFacadeImpl  implements PaymentPluginFacade {
	private static final Logger LOG = LoggerFactory
			.getLogger(PaymentPluginFacadeImpl.class);
	@Autowired
	PaymentInfoService paymentInfoService;
	@Autowired
	PaymentOutService paymentOutService;
	@Autowired
	private PaymentLogService paymentLogService;
	
	@Autowired
	private PlatformMonitorService platformMonitorService;
	@Autowired
	private PlatformMonitorStadetailService platformMonitorStadetailService;
	
	@Autowired
	private UserService userService;
	
	
	 
	@Autowired
	private IdWorkCache idWorkCache;
	
	
 
//	@Override
//	@Transactional
//	public PaymentInfoVo submitPayOrder(String companySn,String paySn,String payMethod,String payMethodName,String receiptAccount,
//			 Integer orderType,String orderNo,BigDecimal amountBank,
//			 BigDecimal feeBank, String orderDesc,UserVo visitor, Integer userId,String userName,String remark,TradeStatus tradeStatus,Integer moneyTypeWallet) {
//		PaymentInfoVo payment=paymentService.findBySn(paySn);
//		if(payment!=null){
//			payment.setFee(feeBank);
//			payment.setAmount(amountBank);
//			payment.setExpire(org.apache.commons.lang.time.DateUtils.addMinutes(new Date(), PluginConstant.PAY_METHOD_TIMEOUT));
//			paymentService.update(payment);
//			LOG.info("submitPayOrder == update "+ObjToStringUtil.objToString(payment));
//		}else{
//			payment = new PaymentInfoVo();
//			payment.setSn(paySn);
//			if(tradeStatus!=null){
//				payment.setStatus(tradeStatus.code);
//			}else{
//				payment.setStatus(TradeStatus.WAIT_BUYER_PAY.code);
//			}
//			payment.setPaymentPluginId(payMethod);
//			payment.setPaymentMethod(payMethodName);
//			payment.setFee(feeBank);
//			payment.setAmount(amountBank);
//			payment.setExpire(org.apache.commons.lang.time.DateUtils.addMinutes(new Date(), PluginConstant.PAY_METHOD_TIMEOUT));
//			payment.setOrderNo(orderNo);
//			payment.setOrderType(orderType);
//			payment.setOrderDesc(orderDesc);
//			
//			payment.setMemo(remark);
//			payment.setAccount(receiptAccount);
//			if(visitor!=null)payment.setOperatorId(visitor.getUserId());
//			payment.setPayer(visitor.getUserName());
//			payment.setUserId(visitor.getUserId().longValue());;
//			payment.setUserName(visitor.getUserName());
//			payment.setCompanySn(companySn);
//			payment.setMoneyTypeWallet(moneyTypeWallet);
//			LOG.info("submitPayOrder == save "+ObjToStringUtil.objToString(payment));
//			 paymentService.save(payment);
//		}
//		return payment;
//	}
 




	
	@Override
	public PaymentInfoEntity submitPayOrder(String companySn, String paySn, String payMethod, String payMethodName,
			String receiptAccount, Integer orderType, String orderNo, BigDecimal amountBank, BigDecimal feeBank,
			String orderDesc, UserEntity loginUser, Integer memberId, String memberName, String remark,
			TradeStatus tradeStatus, Integer moneyTypeWallet,Integer paymentType) {
		PaymentInfoEntity payment=paymentInfoService.findBySn(paySn);
		if(payment!=null){
			payment.setFee(feeBank);
			payment.setAmount(amountBank);
			payment.setExpire(org.apache.commons.lang.time.DateUtils.addMinutes(new Date(), PluginConstant.PAY_METHOD_TIMEOUT));
			payment.setUpdateTime(new Date());
			paymentInfoService.update(payment);
			LOG.info("submitPayOrder == update------ "+JsonUtil.getJsonByObj(payment));
		}else{
			payment = new PaymentInfoEntity();
			payment.setSn(paySn);
			if(tradeStatus!=null){
				payment.setStatus(tradeStatus.code);
			}else{
				payment.setStatus(TradeStatus.WAIT_BUYER_PAY.code);
			}
			payment.setPaymentPluginId(payMethod);
			payment.setPaymentMethod(payMethodName);
			payment.setFee(feeBank);
			payment.setAmount(amountBank);
			payment.setExpire(org.apache.commons.lang.time.DateUtils.addMinutes(new Date(), PluginConstant.PAY_METHOD_TIMEOUT));
			payment.setOrderNo(orderNo);
			payment.setOrderType(orderType);
			payment.setOrderDesc(orderDesc);
			
			payment.setMemo(remark);
			payment.setAccount(receiptAccount);
			if(loginUser!=null)payment.setOperatorId(loginUser.getUserId());
			payment.setPayer(loginUser.getUserName());
			payment.setUserId(loginUser.getUserId().longValue());;
			payment.setUserName(loginUser.getUserName());
			payment.setCompanySn(companySn);
			payment.setMoneyTypeWallet(moneyTypeWallet);
			payment.setPaymentType(paymentType);
			payment.setCreateTime(new Date());
			payment.setPaymentDate(new Date());
			payment.setUpdateTime(new Date());
			
			LOG.info("submitPayOrder == save---- "+JsonUtil.getJsonByObj(payment));
			paymentInfoService.save(payment);
		}
		return payment;
	}

	@Override
	@Transactional
	public PaymentInfoEntity dealOrder(
			PaymentInfoEntity localPayment, TradeStatus tradeStatus) {
		localPayment.setStatus(tradeStatus.code);
		localPayment.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		localPayment.setPaymentDate(new Date());
		this.paymentInfoService.update(localPayment);
		return localPayment;
	}


	@Override
	public PaymentInfoEntity dealOrder(PaymentInfoEntity localPayment, TradeStatus tradeStatus,
			PayWorkCallable<PaymentInfoEntity> payWorkCallable) {
		localPayment.setStatus(tradeStatus.code);
		localPayment.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		localPayment.setPaymentDate(new Date());
		this.paymentInfoService.update(localPayment);
		payWorkCallable.onSuccess(localPayment);
		return localPayment;
	}
	@Override
	public PaymentLogEntity PaymentLogSubmit(String paySn, Integer steps, String remark,
			HttpServletRequest request){
		return PaymentLogSubmit(paySn, steps, remark, request,null);
	}
	
	@Override
	public PaymentLogEntity PaymentLogSubmit(String paySn, Integer steps, String remark,
			HttpServletRequest request, String message) {
		String clientIp =HttpUtils.getClientIp(request);
		String alireqParam = "";
		Timestamp sysTime = new Timestamp(System.currentTimeMillis());
		try {
			Enumeration enu = request.getParameterNames();
			while (enu.hasMoreElements()) {
				String paraName = (String) enu.nextElement();
				alireqParam += paraName + "=" + request.getParameter(paraName)
						+ "&";
			}
			if (alireqParam.length() > 1) {
				alireqParam = alireqParam
						.substring(0, alireqParam.length() - 1);
			}
			if (alireqParam.length() > 2000) {
				alireqParam = alireqParam.substring(0, 2000);
			}
			PaymentLogEntity paymentlog = new PaymentLogEntity();
			paymentlog.setPaysn(paySn);
			paymentlog.setSteps(steps);
			paymentlog.setServerip(request.getRemoteAddr());
			paymentlog.setClientip(clientIp);
			paymentlog.setRemark(remark);
			if(StringUtils.isEmpty(message)){
				paymentlog.setReqparams(alireqParam);
			}else{
				paymentlog.setReqparams(message+alireqParam);
			}
			paymentlog.setCreateTime(sysTime);
			paymentlog.setUpdateTime(sysTime);
			//LOG.info("pay paymentlog ="+ ObjToStringUtil.objToString(paymentlog));
			 paymentLogService.save(paymentlog);
			return paymentlog;
		} catch (Exception ex) {
			LOG.error("paymentError", ex);
		}
		return null;
	}

	@Override
	public void addPaymentTask(PaymentTask paymentTask) {
		
		String paySn = "QA"+DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN_YYYY_MM_DD_HH_MM_SS_SSS)+RandomUtil.getRandom(1000l,9999l);
		
		paySn=idWorkCache.getIdDayHHMMEndRadom("PT", IdWorkCache.ID_WORK_TYPE_PAY_SN, 5);
		
		LOG.info("addPaymentTask------paySn---- "+JsonUtil.getJsonByObj(paySn));
		PaymentInfoEntity payment = new PaymentInfoEntity();
		payment.setSn(paySn);
		payment.setStatus(TradeStatus.TRADE_FINISHED.code);
		payment.setPaymentPluginId("");
		payment.setPaymentMethod("");
		payment.setFee(new BigDecimal(paymentTask.getFee()));
		payment.setAmount(paymentTask.getAmount());
		payment.setExpire(org.apache.commons.lang.time.DateUtils.addMinutes(new Date(), PluginConstant.PAY_METHOD_TIMEOUT));
		payment.setOrderNo(paymentTask.getOrderNo());
		payment.setOrderType(paymentTask.getOrderType());
		if(paymentTask.getOrderDesc()!=null) {
			payment.setOrderDesc(paymentTask.getOrderDesc());
		}else {
			payment.setOrderDesc("");
		}
		payment.setMemo(paymentTask.getMemo());
		payment.setAccount(paymentTask.getAccount());
		payment.setOperatorId(0);
		payment.setPayer(paymentTask.getUserName());
		payment.setUserId(paymentTask.getUserId().longValue());
		payment.setUserName(paymentTask.getUserName());
		payment.setCompanySn("");
		payment.setPaymentType(paymentTask.getPaymentType());
		payment.setMoneyTypeWallet(paymentTask.getMoneyTypeWallet());
		payment.setCreateTime(new Date());
		payment.setUpdateTime(new Date());
		payment.setPaymentDate(new Date());
		
		
		LOG.info("addPaymentTask------save---- "+JsonUtil.getJsonByObj(payment));
		paymentInfoService.save(payment);
		
	}

	@Override
	public PaymentInfoEntity findLastPaymentByOrderNoAndOrderType(String orderNo, Integer orderType) {
		return null;
	}

	@Override
	public PaymentInfoEntity findPaymentBySn(String paySn) {
		return null;
	}
	/**
	   * 退款
	   * 
	   * @param memberId 会员ID
	   * @param refundType 类型：0 现金余额，1 积分 2 资产
	   * @param money 金额：
	   * @return
	   * 
	   */
	@Override
	public boolean refundMoney(int memberId,int refundType,BigDecimal money,Integer orderType, String orderNo,String remark) {
		 UserEntity toUserEntity=userService.queryObject(memberId); 
		 PaymentTask paymentTask=new PaymentTask();
		 
		 if(refundType==0) {
			 paymentTask.setMoneyTypeWallet(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_IN_BACK_MONEY);
			 BigDecimal balance = toUserEntity.getBalance();
			 if(balance==null)balance=new BigDecimal(0);
			 BigDecimal add_balance = balance.add(money);
			 toUserEntity.setBalance(add_balance);
			 
		 }else if(refundType==1) {
			 paymentTask.setMoneyTypeWallet(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_IN_BACK_JF);
			 BigDecimal point = toUserEntity.getPoint();
			 if(point==null)point=new BigDecimal(0);
			 BigDecimal add_point = point.add(money);
			 toUserEntity.setPoint(add_point);
		 }else if(refundType==2) {
			 paymentTask.setMoneyTypeWallet(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_IN_BACK_ZC);
			 
			 BigDecimal surplusInvestMoney = toUserEntity.getSurplusInvestMoney();
			 if(surplusInvestMoney==null)surplusInvestMoney=new BigDecimal(0);
			 BigDecimal add_surplusInvestMoney = surplusInvestMoney.add(money);//剩余收益 额度 增加
			 toUserEntity.setSurplusInvestMoney(add_surplusInvestMoney);
		 }
		 
		 userService.update(toUserEntity);
		 
		 paymentTask.setAmount(money);
		 paymentTask.setFee(0);
		 paymentTask.setUserId(toUserEntity.getUserId());
		 paymentTask.setPayer(toUserEntity.getUserName());
		 paymentTask.setUserName(toUserEntity.getUserName());
		 paymentTask.setMemo("退款:"+remark);
		 paymentTask.setPaymentType(PluginConstant.PAYMENT_TYPE_IN);
		 paymentTask.setOrderNo(orderNo);
		 paymentTask.setOrderType(orderType);
		 
		 TaskPaymentProducer.addPaymentTaskVo(paymentTask);
		 return true;
	}
	
	
	
	@Override
	 public boolean monitorUser() {
		//公司每日收益率
		Date start=new Date();
		LOG.info("----------------------每日监控统计-------------------------开始时间："+start.getTime());
		//统计前一天得数据
		String poolDateNumber="PPD_"+DateUtils.formatYYYYMMDD(new Date());//拿当日分红
		PlatformMonitorEntity platformMonitor=new PlatformMonitorEntity();
		platformMonitor.setCreateTime(new Date());
		platformMonitor.setMonitorDate(new Date());
		platformMonitor.setMonitorDateNumber(poolDateNumber);
		platformMonitor.setMonitorType(0);
		platformMonitor.setMonitorUserAbnormalSum(0);
		
		int errorSum=0;
		
		Map<String,Object> usermap=new HashMap<>();
		List<UserEntity> userList=userService.queryList(usermap);
		for(UserEntity userEntity:userList) {
			int userId=userEntity.getUserId();
			Map<String,Object> payTotalMap=paymentInfoService.queryStatByUserId(userId,ShopConstant.DJF_TJ_DATE);
	    	
	    	BigDecimal walletType1020=BigDecimal.ZERO;
			BigDecimal walletType6=BigDecimal.ZERO;
			BigDecimal walletType3=BigDecimal.ZERO;
			BigDecimal walletType43=BigDecimal.ZERO;
			BigDecimal walletType411=BigDecimal.ZERO;
			BigDecimal walletType421=BigDecimal.ZERO;
			BigDecimal walletType431=BigDecimal.ZERO;
			BigDecimal walletType64=BigDecimal.ZERO;
			
			
			if(payTotalMap.containsKey("walletType1020"))walletType1020=new BigDecimal(payTotalMap.get("walletType1020").toString());
			if(payTotalMap.containsKey("walletType6")) walletType6=new BigDecimal(payTotalMap.get("walletType6").toString());
			if(payTotalMap.containsKey("walletType3"))walletType3=new BigDecimal(payTotalMap.get("walletType3").toString());
			if(payTotalMap.containsKey("walletType43"))walletType43=new BigDecimal(payTotalMap.get("walletType43").toString());
			if(payTotalMap.containsKey("walletType411")) walletType411=new BigDecimal(payTotalMap.get("walletType411").toString());
			if(payTotalMap.containsKey("walletType421")) walletType421=new BigDecimal(payTotalMap.get("walletType421").toString());
			if(payTotalMap.containsKey("walletType431")) walletType431=new BigDecimal(payTotalMap.get("walletType431").toString());
			if(payTotalMap.containsKey("walletType64")) walletType64=new BigDecimal(payTotalMap.get("walletType64").toString());
			
			BigDecimal walletType62=BigDecimal.ZERO;
			BigDecimal walletType5=BigDecimal.ZERO;
			BigDecimal walletType2=BigDecimal.ZERO;
			BigDecimal walletType0=BigDecimal.ZERO;
			BigDecimal walletType1=BigDecimal.ZERO;
			
			
			if(payTotalMap.containsKey("walletType62"))  walletType62=new BigDecimal(payTotalMap.get("walletType62").toString());
			if(payTotalMap.containsKey("walletType5"))  walletType5=new BigDecimal(payTotalMap.get("walletType5").toString());
			if(payTotalMap.containsKey("walletType2"))  walletType2=new BigDecimal(payTotalMap.get("walletType2").toString());
			if(payTotalMap.containsKey("walletType0"))  walletType0=new BigDecimal(payTotalMap.get("walletType0").toString());
			if(payTotalMap.containsKey("walletType1"))  walletType1=new BigDecimal(payTotalMap.get("walletType1").toString());
			
			
			
			
			BigDecimal walletType10000=BigDecimal.ZERO;
			BigDecimal walletType10010=BigDecimal.ZERO;
			BigDecimal walletType10020=BigDecimal.ZERO;
			
			BigDecimal walletType10001=BigDecimal.ZERO;
			BigDecimal walletType10011=BigDecimal.ZERO;
			BigDecimal walletType10021=BigDecimal.ZERO;
			
			
			if(payTotalMap.containsKey("walletType10000"))  walletType10000=new BigDecimal(payTotalMap.get("walletType10000").toString());
			if(payTotalMap.containsKey("walletType10010"))  walletType10010=new BigDecimal(payTotalMap.get("walletType10010").toString());
			if(payTotalMap.containsKey("walletType10020"))  walletType10020=new BigDecimal(payTotalMap.get("walletType10020").toString());
			
			if(payTotalMap.containsKey("walletType10001"))  walletType10000=new BigDecimal(payTotalMap.get("walletType10001").toString());
			if(payTotalMap.containsKey("walletType10011"))  walletType10010=new BigDecimal(payTotalMap.get("walletType10011").toString());
			if(payTotalMap.containsKey("walletType10021"))  walletType10020=new BigDecimal(payTotalMap.get("walletType10021").toString());
			
			
			BigDecimal allInMoney=walletType1020.add(walletType6).add(walletType3).add(walletType43).add(walletType411).add(walletType421).add(walletType431).add(walletType64);
			allInMoney=allInMoney.add(walletType10000);
			
			BigDecimal allOutMoney=walletType62.add(walletType5).add(walletType2).add(walletType0);
			allOutMoney=allOutMoney.add(walletType10001);
			allOutMoney=allOutMoney.add(walletType1);
			
			LOG.info("--------payTotalMap--------- "+JsonUtil.getJsonByObj(payTotalMap));
	    	
	    	
	    	
			Map<String, Object> params=new HashMap<>();
	    	params.put("userId", userEntity.getUserId());
	    	Map<String,Object> payoutTotalMap=paymentOutService.queryStat(params);
	    	LOG.info("--------payoutTotalMap--------- "+JsonUtil.getJsonByObj(payoutTotalMap));
			 
	    	
	    	BigDecimal status1Money=BigDecimal.ZERO;
	    	BigDecimal status1feeMoney=BigDecimal.ZERO;
	    	BigDecimal status2Money=BigDecimal.ZERO;
	    	BigDecimal status2feeMoney=BigDecimal.ZERO;
	    	
	    	if(payoutTotalMap.containsKey("status1")) {
	    		status1Money=new BigDecimal(payoutTotalMap.get("status1").toString());
	    	}
	    	if(payoutTotalMap.containsKey("status1fee")) {
	    		status1feeMoney=new BigDecimal(payoutTotalMap.get("status1fee").toString());
	    	}
	    	if(payoutTotalMap.containsKey("status2")) {
	    		status2Money=new BigDecimal(payoutTotalMap.get("status2").toString());
	    	}
	    	if(payoutTotalMap.containsKey("status2fee")) {
	    		status2feeMoney=new BigDecimal(payoutTotalMap.get("status2fee").toString());
	    	}
	    	
	    	
	    	BigDecimal allApplyPayMoney=status1Money.add(status1feeMoney);
	    	
	    	
	    	BigDecimal statBalance=allInMoney.subtract(allOutMoney).subtract(allApplyPayMoney);
	    	
	    	
	    	LOG.info("--------statBalance--------- "+JsonUtil.getJsonByObj(statBalance)+"--------userBalance--------- "+JsonUtil.getJsonByObj(userEntity.getBalance()));
		    if(statBalance.intValue()<0) {
		    	PlatformMonitorStadetailEntity platformMonitorStadetailEntity=new PlatformMonitorStadetailEntity();
		    	platformMonitorStadetailEntity.setCreateTime(new Date());
		    	platformMonitorStadetailEntity.setMonitorContent("统计余额为负，转账，或者支持存在问题！统计余额："+statBalance.intValue()+",用户余额："+userEntity.getBalance().intValue());
		    	
		    	platformMonitorStadetailEntity.setMonitorDateNumber(poolDateNumber);
		    	platformMonitorStadetailEntity.setMonitorMemberId(userId);
		    	platformMonitorStadetailEntity.setMonitorMemberName(userEntity.getUserName());
		    	platformMonitorStadetailEntity.setMonitorType(0);
		    	platformMonitorStadetailService.save(platformMonitorStadetailEntity);
		    	errorSum++;
		    }
		    
		    if(userEntity.getBalance().intValue()!=statBalance.intValue()) {
		    	PlatformMonitorStadetailEntity platformMonitorStadetailEntity=new PlatformMonitorStadetailEntity();
		    	platformMonitorStadetailEntity.setCreateTime(new Date());
		    	platformMonitorStadetailEntity.setMonitorContent("统计余额和用户余额不等，用户余额："+userEntity.getBalance().intValue()+",统计余额："+statBalance.intValue());
		    	platformMonitorStadetailEntity.setMonitorDateNumber(poolDateNumber);
		    	platformMonitorStadetailEntity.setMonitorMemberId(userId);
		    	platformMonitorStadetailEntity.setMonitorMemberName(userEntity.getUserName());
		    	platformMonitorStadetailEntity.setMonitorType(0);
		    	platformMonitorStadetailService.save(platformMonitorStadetailEntity);
		    	errorSum++;
		    }
		    
		}
		platformMonitor.setMonitorUserAbnormalSum(errorSum);
		platformMonitorService.save(platformMonitor);
		Date end = new Date();
		LOG.info("----------------------每日监控统计-------------------------结束时间："+end.getTime()+",本次耗时："+(end.getTime() - start.getTime())+ "毫秒；" );
		
		return true;
	}
	
	
}
