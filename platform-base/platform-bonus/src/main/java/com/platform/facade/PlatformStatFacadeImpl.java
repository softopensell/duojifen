package com.platform.facade;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.platform.api.entity.BonusPoolJoinMemberVo;
import com.platform.api.service.ApiBonusPoolJoinMemberService;
import com.platform.constants.BonusConstant;
import com.platform.constants.PluginConstant;
import com.platform.entity.BonusPointsVo;
import com.platform.entity.PlatformStatEntity;
import com.platform.entity.PlatformWithdrawShareEntity;
import com.platform.entity.PlatformWithdrawShareOrderEntity;
import com.platform.entity.UserEntity;
import com.platform.mq.model.PaymentTask;
import com.platform.mq.producer.TaskPaymentProducer;
import com.platform.service.ApiBonusPointsService;
import com.platform.service.PaymentInfoService;
import com.platform.service.PlatformStatService;
import com.platform.service.PlatformWithdrawShareOrderService;
import com.platform.service.PlatformWithdrawShareService;
import com.platform.service.SysConfigService;
import com.platform.service.UserService;
import com.platform.util.constants.ProductConstant;
import com.platform.utils.DateUtils;
import com.platform.utils.JsonUtil;
import com.platform.utils.MoneyFormatUtils;
import com.platform.utils.R;

@Component
public class PlatformStatFacadeImpl  implements PlatformStatFacade {
	 protected Logger logger = LoggerFactory.getLogger(getClass());
	 @Autowired
	 private UserService userService;
	 @Autowired
	 private ApiBonusPointsService bonusPointsService;
	 @Autowired
	 private ApiBonusPoolJoinMemberService apiBonusPoolJoinMemberService;
	 @Autowired
	 private PaymentInfoService paymentInfoService ;
	 @Autowired
	 private PlatformStatService platformStatService ;
	 @Autowired
	 private SysConfigService sysConfigService ;
	 @Autowired
	 private PlatformWithdrawShareOrderService platformWithdrawShareOrderService ;
	
	 @Autowired
	 private PlatformWithdrawShareService platformWithdrawShareService ;
	 
	@Override
	public void everyDayDealStat() {
		//统计之前 升级机制
//		toUpUserShareLevel();
		
		logger.info("----------------------toUpUserShareLevel-------------------------end-----");
		//公司每日收益率
		Date start=new Date();
		logger.info("----------------------每日统计-------------------------开始时间："+start.getTime());
		//统计前一天得数据
		String poolDateNumber="PPD_"+DateUtils.formatYYYYMMDD(new Date());//拿当日分红
		Date yesterday=DateUtils.addDays(new Date(), -1);
		yesterday=DateUtils.getEndOfDate(yesterday);
		platformDealStat(poolDateNumber,yesterday);
		Date end = new Date();
		logger.info("----------------------每日统计-------------------------结束时间："+end.getTime()+",本次耗时："+(end.getTime() - start.getTime())+ "毫秒；" );
		
	}
	@Override
	public void platformDealStat(String  poolDateNumber,Date statDay) {
		logger.info("----------------------统计-------------------------日期："+DateUtils.format(statDay));
		PlatformStatEntity platformStatEntity=new PlatformStatEntity();
		platformStatEntity.setStatDate(statDay);
//		String poolDateNumber="PPD_"+DateUtils.formatYYYYMMDD(statDay);
		
		BonusPoolJoinMemberVo bonusPoolJoinMember=apiBonusPoolJoinMemberService.queryByDateNumberAndMemberId(poolDateNumber, BonusConstant.BONUS_POOL_JOIN_MEMBER_ID);
		if(bonusPoolJoinMember!=null) {
			platformStatEntity.setStatDayRate(bonusPoolJoinMember.getPoolJoinMoney());
			logger.info("----------------------返利比例-------------------------rate:"+bonusPoolJoinMember.getPoolJoinMoney());
		}
		long zeroSum=0l;
    	BigDecimal zero=new BigDecimal(0);
		
		platformStatEntity.setStatDateNumber(poolDateNumber);
		platformStatEntity.setCreateTime(new Date());
		platformStatEntity.setStatDate(statDay);
		Map<String,Object> resultMap=new HashMap<String,Object>();
    	Map<String,Object> userTotalMap=userService.queryStatByDay(statDay);
    	platformStatEntity.setStatUserDayAddSum(zeroSum);
    	platformStatEntity.setStatUserDayAddV0Sum(zeroSum);
    	platformStatEntity.setStatUserDayAddV1Sum(zeroSum);
    	platformStatEntity.setStatUserDayAddV2Sum(zeroSum);
    	platformStatEntity.setStatUserDayAddV3Sum(zeroSum);
    	platformStatEntity.setStatUserDayAddV4Sum(zeroSum);
    	
    	platformStatEntity.setStatUserV0Sum(zeroSum);
    	platformStatEntity.setStatUserV1Sum(zeroSum);
    	platformStatEntity.setStatUserV2Sum(zeroSum);
    	platformStatEntity.setStatUserV3Sum(zeroSum);
    	platformStatEntity.setStatUserV4Sum(zeroSum);
    	
 		platformStatEntity.setStatUserTotalJf(zero);
 		platformStatEntity.setStatUserTotalZc(zero);
 		platformStatEntity.setStatUserTotalSyZc(zero);
 		platformStatEntity.setStatUserTotalFund(zero);
 		platformStatEntity.setStatDayJfZz(zero);
 		platformStatEntity.setStatDayJfZzSum(zeroSum);
 		
 		
 		platformStatEntity.setStatDayMoneyQy(zero);
 		platformStatEntity.setStatDayMoneyQySum(zeroSum);
 		
 		platformStatEntity.setStatDayMoneyFw(zero);
 		platformStatEntity.setStatDayMoneyFwSum(zeroSum);
 		
 		platformStatEntity.setStatDayMoneyXx(zero);
 		platformStatEntity.setStatDayMoneyXxSum(zeroSum);
 		
 		platformStatEntity.setStatDayMoneySq(zero);
 		platformStatEntity.setStatDayMoneySqSum(zeroSum);
 		
 		platformStatEntity.setStatDayMoneyFund(zero);
 		platformStatEntity.setStatDayMoneyFundSum(zeroSum);
 		
 		platformStatEntity.setStatDayMoneySq(zero);
 		platformStatEntity.setStatDayMoneySqSum(zeroSum);
 		
    	
    	//统计
    	if(userTotalMap!=null&&!userTotalMap.isEmpty()) {
    		   logger.info("---------------------会员总-统计-------------------------userTotalMap："+JsonUtil.getJsonByObj(userTotalMap));
    		
    	     	if(userTotalMap.containsKey("allV")) {
    	     		Long allV=(Long) userTotalMap.get("allV");
    	     		platformStatEntity.setStatUserDayAddSum(allV);
    	     	}
    	     	if(userTotalMap.containsKey("v0")) {
    	     		Long v0=(Long) userTotalMap.get("v0");
    	     		platformStatEntity.setStatUserDayAddV0Sum(v0);
    	     	}
    	     	if(userTotalMap.containsKey("v1")) {
    	     		Long v1=(Long) userTotalMap.get("v1");
    	     		platformStatEntity.setStatUserDayAddV1Sum(v1);
    	     	}
    	     	if(userTotalMap.containsKey("v2")) {
    	     		Long v2=(Long) userTotalMap.get("v2");
    	     		platformStatEntity.setStatUserDayAddV2Sum(v2);
    	     	}
    	     	if(userTotalMap.containsKey("v3")) {
    	     		Long v3=(Long) userTotalMap.get("v3");
    	     		platformStatEntity.setStatUserDayAddV3Sum(v3);
    	     	}
    	     	if(userTotalMap.containsKey("v4")) {
    	     		Long v4=(Long) userTotalMap.get("v4");
    	     		platformStatEntity.setStatUserDayAddV4Sum(v4);
    	     	}
    	}
    	resultMap.put("userTotalMap", userTotalMap);
    	
    	Map<String,Object> allTotalMap=userService.queryTotalStat();
    	
    	if(allTotalMap!=null&&!allTotalMap.isEmpty()) {
    		logger.info("----------------金额------统计-------------------------allTotalMap："+JsonUtil.getJsonByObj(allTotalMap));
    		
	     	if(allTotalMap.containsKey("totalSum")) {
	     		Long statUserTotalSum=(Long) allTotalMap.get("totalSum");
	     		platformStatEntity.setStatUserTotalSum(statUserTotalSum);
	     	}
	     	
	     	if(allTotalMap.containsKey("v0")) {
	     		Long v0=(Long) allTotalMap.get("v0");
	     		platformStatEntity.setStatUserV0Sum(v0);
	     	}
	     	if(allTotalMap.containsKey("v1")) {
	     		Long v1=(Long) allTotalMap.get("v1");
	     		platformStatEntity.setStatUserV1Sum(v1);
	     	}
	     	if(allTotalMap.containsKey("v2")) {
	     		Long v2=(Long) allTotalMap.get("v2");
	     		platformStatEntity.setStatUserV2Sum(v2);
	     	}
	     	if(allTotalMap.containsKey("v3")) {
	     		Long v3=(Long) allTotalMap.get("v3");
	     		platformStatEntity.setStatUserV3Sum(v3);
	     	}
	     	if(allTotalMap.containsKey("v4")) {
	     		Long v4=(Long) allTotalMap.get("v4");
	     		platformStatEntity.setStatUserV4Sum(v4);
	     	}
	     	
	     	if(allTotalMap.containsKey("totalPoint")) {
	     		BigDecimal totalPoint=(BigDecimal) allTotalMap.get("totalPoint");
	     		platformStatEntity.setStatUserTotalJf(totalPoint);
	     	}
	     	if(allTotalMap.containsKey("totalBalance")) {
	     		BigDecimal totalPoint=(BigDecimal) allTotalMap.get("totalBalance");
	     		platformStatEntity.setStatUserTotalZc(totalPoint);
	     	}
	     	if(allTotalMap.containsKey("totalInvestMoney")) {
	     		BigDecimal totalCz=(BigDecimal) allTotalMap.get("totalInvestMoney");
	     		platformStatEntity.setStatUserTotalCz(totalCz);
	     	}
	     	
	     	if(allTotalMap.containsKey("totalSurplusInvestMoney")) {
	     		BigDecimal totalSurplusInvestMoney=(BigDecimal) allTotalMap.get("totalSurplusInvestMoney");
	     		platformStatEntity.setStatUserTotalSyZc(totalSurplusInvestMoney);
	     	}
	     	if(allTotalMap.containsKey("totalFund")) {
	     		BigDecimal totalFund=(BigDecimal) allTotalMap.get("totalFund");
	     		platformStatEntity.setStatUserTotalFund(totalFund);
	     	}
	     	
    	}
    	resultMap.put("allTotalMap", allTotalMap);
    	
    	
    	
    	
    	
    	
    	Map<String,Object> payTotalMap=paymentInfoService.queryStatByDay(statDay);
    	
    	
    	
    	platformStatEntity.setStatDayPayBalanceSum(zeroSum);
 		platformStatEntity.setStatDayPayBalance(zero);
 		
 		platformStatEntity.setStatDayPayJfSum(zeroSum);
 		platformStatEntity.setStatDayPayJf(zero);
 		
 		platformStatEntity.setStatDayMoneyRechargeSum(zeroSum);
 		platformStatEntity.setStatDayMoneyRecharge(zero);
 		
 		platformStatEntity.setStatDayBalanceZzInSum(zeroSum);
 		platformStatEntity.setStatDayBalanceZzIn(zero);
 		
 		platformStatEntity.setStatDayBalanceZzOutSum(zeroSum);
 		platformStatEntity.setStatDayBalanceZzOut(zero);
 		
 		platformStatEntity.setStatDayMoneyTxSum(zeroSum);
 		platformStatEntity.setStatDayMoneyTx(zero);
 		
 		
 		platformStatEntity.setStatDayJfDhSum(zeroSum);
 		platformStatEntity.setStatDayJfDh(zero);
 		
    	
        if(payTotalMap!=null&&!payTotalMap.isEmpty()) {
        	logger.info("----------------每日流水-----统计-------------------------payTotalMap："+JsonUtil.getJsonByObj(payTotalMap));
        		
	     	if(payTotalMap.containsKey("walletType1")) {
	     		BigDecimal walletTypeJe=(BigDecimal) payTotalMap.get("walletType1");
	     		platformStatEntity.setStatDayPayBalance(walletTypeJe);
	     	}
	     	if(payTotalMap.containsKey("walletType1Sum")) {
	     		Long walletTypeSum=(Long) payTotalMap.get("walletType1Sum");
	     		platformStatEntity.setStatDayPayBalanceSum(walletTypeSum);
	     	}
	     	
	     	if(payTotalMap.containsKey("walletType92")) {
	     		BigDecimal walletTypeJe=(BigDecimal) payTotalMap.get("walletType92");
	     		platformStatEntity.setStatDayPayJf(walletTypeJe);
	     	}
	     	if(payTotalMap.containsKey("walletType92Sum")) {
	     		Long walletTypeSum=(Long) payTotalMap.get("walletType92Sum");
	     		platformStatEntity.setStatDayPayJfSum(walletTypeSum);
	     	}
	     	
	     	if(payTotalMap.containsKey("walletType6")) {
	     		BigDecimal walletTypeJe=(BigDecimal) payTotalMap.get("walletType6");
	     		platformStatEntity.setStatDayMoneyRecharge(walletTypeJe);
	     	}
	     	if(payTotalMap.containsKey("walletType6Sum")) {
	     		Long walletTypeSum=(Long) payTotalMap.get("walletType6Sum");
	     		platformStatEntity.setStatDayMoneyRechargeSum(walletTypeSum);
	     	}
	     	if(payTotalMap.containsKey("walletType62")) {
	     		BigDecimal walletTypeJe=(BigDecimal) payTotalMap.get("walletType62");
	     		platformStatEntity.setStatDayJfDh(walletTypeJe);
	     	}
	     	if(payTotalMap.containsKey("walletType62Sum")) {
	     		Long walletTypeSum=(Long) payTotalMap.get("walletType62Sum");
	     		platformStatEntity.setStatDayJfDhSum(walletTypeSum);
	     	}
	     	
	     	
	     	if(payTotalMap.containsKey("walletType2")) {
	     		BigDecimal walletType=(BigDecimal) payTotalMap.get("walletType2");
	     		platformStatEntity.setStatDayBalanceZzOut(walletType);
	     	}
	     	if(payTotalMap.containsKey("walletType2Sum")) {
	     		Long walletTypeSum=(Long) payTotalMap.get("walletType2Sum");
	     		platformStatEntity.setStatDayBalanceZzOutSum(walletTypeSum);
	     	}
	     	if(payTotalMap.containsKey("walletType3")) {
	     		BigDecimal walletType=(BigDecimal) payTotalMap.get("walletType3");
	     		platformStatEntity.setStatDayBalanceZzIn(walletType);
	     	}
	     	if(payTotalMap.containsKey("walletType3Sum")) {
	     		Long walletTypeSum=(Long) payTotalMap.get("walletType3Sum");
	     		platformStatEntity.setStatDayBalanceZzInSum(walletTypeSum);
	     	}
	     	
	     	if(payTotalMap.containsKey("walletType5")) {
	     		BigDecimal walletTypeJe=(BigDecimal) payTotalMap.get("walletType5");
	     		platformStatEntity.setStatDayMoneyTx(walletTypeJe);
	     	}
	     	if(payTotalMap.containsKey("walletType5Sum")) {
	     		Long walletTypeSum=(Long) payTotalMap.get("walletType5Sum");
	     		platformStatEntity.setStatDayMoneyTxSum(walletTypeSum);
	     	}
	     	
	     	
	     	
	     	
	     	if(payTotalMap.containsKey("walletType43")) {
	     		BigDecimal walletTypeJe=(BigDecimal) payTotalMap.get("walletType43");
	     		platformStatEntity.setStatDayMoneyQy(walletTypeJe);
	     	}
	     	if(payTotalMap.containsKey("walletType43Sum")) {
	     		Long walletTypeSum=(Long) payTotalMap.get("walletType43Sum");
	     		platformStatEntity.setStatDayMoneyQySum(walletTypeSum);
	     	}
	     	
	     	if(payTotalMap.containsKey("walletType411")) {
	     		BigDecimal walletTypeJe=(BigDecimal) payTotalMap.get("walletType411");
	     		platformStatEntity.setStatDayMoneyFw(walletTypeJe);
	     	}
	     	if(payTotalMap.containsKey("walletType411Sum")) {
	     		Long walletTypeSum=(Long) payTotalMap.get("walletType411Sum");
	     		platformStatEntity.setStatDayMoneyFwSum(walletTypeSum);
	     	}
	     	
	 		
	     	if(payTotalMap.containsKey("walletType421")) {
	     		BigDecimal walletTypeJe=(BigDecimal) payTotalMap.get("walletType421");
	     		platformStatEntity.setStatDayMoneyXx(walletTypeJe);
	     	}
	     	if(payTotalMap.containsKey("walletType421Sum")) {
	     		Long walletTypeSum=(Long) payTotalMap.get("walletType421Sum");
	     		platformStatEntity.setStatDayMoneyXxSum(walletTypeSum);
	     	}
	     	
	     	if(payTotalMap.containsKey("walletType431")) {
	     		BigDecimal walletTypeJe=(BigDecimal) payTotalMap.get("walletType431");
	     		platformStatEntity.setStatDayMoneySq(walletTypeJe);
	     	}
	     	if(payTotalMap.containsKey("walletType431Sum")) {
	     		Long walletTypeSum=(Long) payTotalMap.get("walletType431Sum");
	     		platformStatEntity.setStatDayMoneySqSum(walletTypeSum);
	     	}
	     	
	     	if(payTotalMap.containsKey("walletType4000")) {
	     		BigDecimal walletTypeJe=(BigDecimal) payTotalMap.get("walletType4000");
	     		platformStatEntity.setStatDayMoneyFund(walletTypeJe);
	     	}
	     	if(payTotalMap.containsKey("walletType4000Sum")) {
	     		Long walletTypeSum=(Long) payTotalMap.get("walletType4000Sum");
	     		platformStatEntity.setStatDayMoneyFundSum(walletTypeSum);
	     	}
	     	
	     	
    	}
    	
    	resultMap.put("payTotalMap", payTotalMap);
		
		platformStatService.save(platformStatEntity);
		
		//7.21 
		//提现分红金额==提现金额+转账金额
//		2
		
//		BigDecimal toShareSxfTotalMoney=platformStatEntity.getStatDayBalanceZzOut().add(platformStatEntity.getStatDayMoneyTx());
		
		BigDecimal toShareSxfTotalMoney=platformStatEntity.getStatDayMoneyTx();
		
		logger.info("----------------------提现分红-------------------------日期："+DateUtils.format(statDay)+"，转账总额："+platformStatEntity.getStatDayBalanceZzOut()+"，提现总额："+platformStatEntity.getStatDayMoneyTx());
		//处理提现分红
		toPlatformShareWithdraw(poolDateNumber,statDay,toShareSxfTotalMoney);
		
	}
	private void toPlatformShareWithdraw(String poolDateNumber,Date statDay,BigDecimal todayWithdrawMoney) {
		logger.info("----------------------提现分红-------------------------日期："+DateUtils.format(statDay)+",提现+转账总额："+todayWithdrawMoney);
		//手续费 一共10%
		double defaultSysRate=Double.valueOf(sysConfigService.getValue(BonusConstant.BONUS_WITHDRAW_USER_SYS_RATE, "0.03"));
		double defaultGsRate=Double.valueOf(sysConfigService.getValue(BonusConstant.BONUS_WITHDRAW_USER_GS_RATE, "0.03"));
		double defaultHhRate=Double.valueOf(sysConfigService.getValue(BonusConstant.BONUS_WITHDRAW_USER_HH_RATE, "0.04"));
		double totalPay=defaultSysRate+defaultGsRate+defaultHhRate;
		
		double defaultRateV1=Double.valueOf(sysConfigService.getValue(BonusConstant.BONUS_WITHDRAW_USER_GS_RATE+"_"+1, "0.05"));
		double defaultRateV2=Double.valueOf(sysConfigService.getValue(BonusConstant.BONUS_WITHDRAW_USER_GS_RATE+"_"+2, "0.10"));
		double defaultRateV3=Double.valueOf(sysConfigService.getValue(BonusConstant.BONUS_WITHDRAW_USER_GS_RATE+"_"+3, "0.15"));
		double defaultRateV4=Double.valueOf(sysConfigService.getValue(BonusConstant.BONUS_WITHDRAW_USER_GS_RATE+"_"+4, "0.20"));
		double defaultRateV5=Double.valueOf(sysConfigService.getValue(BonusConstant.BONUS_WITHDRAW_USER_GS_RATE+"_"+5, "0.50"));
		
		double defaultRateV6=Double.valueOf(sysConfigService.getValue(BonusConstant.BONUS_WITHDRAW_USER_GS_RATE+"_"+6, "0.05"));
		double defaultRateV7=Double.valueOf(sysConfigService.getValue(BonusConstant.BONUS_WITHDRAW_USER_GS_RATE+"_"+7, "0.10"));
		double defaultRateV8=Double.valueOf(sysConfigService.getValue(BonusConstant.BONUS_WITHDRAW_USER_GS_RATE+"_"+8, "0.15"));
		double defaultRateV9=Double.valueOf(sysConfigService.getValue(BonusConstant.BONUS_WITHDRAW_USER_GS_RATE+"_"+9, "0.20"));
		double defaultRateV10=Double.valueOf(sysConfigService.getValue(BonusConstant.BONUS_WITHDRAW_USER_GS_RATE+"_"+10, "0.50"));
		
		
//		String poolDateNumber="PPD_"+DateUtils.formatYYYYMMDD(statDay);
		
		PlatformWithdrawShareOrderEntity platformWithdrawShareOrderEntity=platformWithdrawShareOrderService.queryByDateNo(poolDateNumber);
		
		if(platformWithdrawShareOrderEntity!=null) {
			logger.info("----------------------已经分红过了-------------------------");
			return ;
		}
		platformWithdrawShareOrderEntity=new PlatformWithdrawShareOrderEntity();
		platformWithdrawShareOrderEntity.setCreateTime(new Date());
		platformWithdrawShareOrderEntity.setOrderDateNo(poolDateNumber);
		platformWithdrawShareOrderEntity.setShareEndDate(statDay);
		platformWithdrawShareOrderEntity.setShareStartDate(statDay);
		platformWithdrawShareOrderEntity.setUpdateTime(new Date());
		platformWithdrawShareOrderEntity.setTotalPayMoney(MoneyFormatUtils.getMultiply(todayWithdrawMoney,new BigDecimal(totalPay)));
		platformWithdrawShareOrderEntity.setState(0);
		
		platformWithdrawShareOrderEntity.setShareUserDaySys(MoneyFormatUtils.getMultiply(todayWithdrawMoney,new BigDecimal(defaultSysRate)));
		//分配给平台admin 账号
		 UserEntity toUserEntity=userService.getById(ProductConstant.PLATFORM_MEMBER_ID);
		 if(toUserEntity!=null) {
			 userService.addUserBalanceAndTotalIncome(toUserEntity.getUserId(), platformWithdrawShareOrderEntity.getShareUserDaySys());
			 apiBonusPoolJoinMemberService.incrBonusPoolJoinMemberMoney(poolDateNumber, toUserEntity.getUserId(), platformWithdrawShareOrderEntity.getShareUserDaySys());
			 //写日志
			 PaymentTask paymentTask=new PaymentTask();
			 paymentTask.setAmount(platformWithdrawShareOrderEntity.getShareUserDaySys());
			 paymentTask.setFee(0);
			 paymentTask.setUserId(toUserEntity.getUserId());
			 paymentTask.setUserName(toUserEntity.getUserName());
			 paymentTask.setPayer("sys");
			 paymentTask.setMemo("每日提现手续费分红：平台运营费用");
			 paymentTask.setMoneyTypeWallet(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_IN_FW_TX_PAY);
			 paymentTask.setPaymentType(PluginConstant.PAYMENT_TYPE_IN);
			 TaskPaymentProducer.addPaymentTaskVo(paymentTask);  
		 }
		
		
		
//		platformWithdrawShareOrderEntity.setShareUserDayV1(todayWithdrawMoney.multiply(new BigDecimal(defaultGsRate*defaultRateV1)));
//		platformWithdrawShareOrderEntity.setShareUserDayV2(todayWithdrawMoney.multiply(new BigDecimal(defaultGsRate*defaultRateV2)));
//		platformWithdrawShareOrderEntity.setShareUserDayV3(todayWithdrawMoney.multiply(new BigDecimal(defaultGsRate*defaultRateV3)));
//		platformWithdrawShareOrderEntity.setShareUserDayV4(todayWithdrawMoney.multiply(new BigDecimal(defaultGsRate*defaultRateV4)));
//		platformWithdrawShareOrderEntity.setShareUserDayV5(todayWithdrawMoney.multiply(new BigDecimal(defaultGsRate*defaultRateV5)));
//		
//		platformWithdrawShareOrderEntity.setShareUserDayV6(todayWithdrawMoney.multiply(new BigDecimal(defaultHhRate*defaultRateV6)));
//		platformWithdrawShareOrderEntity.setShareUserDayV7(todayWithdrawMoney.multiply(new BigDecimal(defaultHhRate*defaultRateV7)));
//		platformWithdrawShareOrderEntity.setShareUserDayV8(todayWithdrawMoney.multiply(new BigDecimal(defaultHhRate*defaultRateV8)));
//		platformWithdrawShareOrderEntity.setShareUserDayV9(todayWithdrawMoney.multiply(new BigDecimal(defaultHhRate*defaultRateV9)));
//		platformWithdrawShareOrderEntity.setShareUserDayV10(todayWithdrawMoney.multiply(new BigDecimal(defaultHhRate*defaultRateV10)));
		
		
		platformWithdrawShareOrderEntity.setShareUserDayV1(MoneyFormatUtils.getMultiply(todayWithdrawMoney, defaultGsRate*defaultRateV1));
		platformWithdrawShareOrderEntity.setShareUserDayV2(MoneyFormatUtils.getMultiply(todayWithdrawMoney, defaultGsRate*defaultRateV2));
		platformWithdrawShareOrderEntity.setShareUserDayV3(MoneyFormatUtils.getMultiply(todayWithdrawMoney, defaultGsRate*defaultRateV3));
		platformWithdrawShareOrderEntity.setShareUserDayV4(MoneyFormatUtils.getMultiply(todayWithdrawMoney, defaultGsRate*defaultRateV4));
		platformWithdrawShareOrderEntity.setShareUserDayV5(MoneyFormatUtils.getMultiply(todayWithdrawMoney, defaultGsRate*defaultRateV5));
		platformWithdrawShareOrderEntity.setShareUserDayV6(MoneyFormatUtils.getMultiply(todayWithdrawMoney, defaultGsRate*defaultRateV6));
		platformWithdrawShareOrderEntity.setShareUserDayV7(MoneyFormatUtils.getMultiply(todayWithdrawMoney, defaultGsRate*defaultRateV7));
		platformWithdrawShareOrderEntity.setShareUserDayV8(MoneyFormatUtils.getMultiply(todayWithdrawMoney, defaultGsRate*defaultRateV8));
		platformWithdrawShareOrderEntity.setShareUserDayV9(MoneyFormatUtils.getMultiply(todayWithdrawMoney, defaultGsRate*defaultRateV9));
		platformWithdrawShareOrderEntity.setShareUserDayV10(MoneyFormatUtils.getMultiply(todayWithdrawMoney, defaultGsRate*defaultRateV10));
		
		
		platformWithdrawShareOrderService.save(platformWithdrawShareOrderEntity);//保存日志
		
		//分红打款
		toEveryDayShareWithdrawEveryOrder(platformWithdrawShareOrderEntity);
	}
	
	private void toEveryDayShareWithdrawEveryOrder(PlatformWithdrawShareOrderEntity platformWithdrawShareOrderEntity) {
		 logger.info("--------------------- 日分享提现------user-------------------platformWithdrawShareOrderEntity:"+JsonUtil.getJsonByObj(platformWithdrawShareOrderEntity));
		 //获取vi
		
		logger.info("---------------------------提现分红------v1---------------"+platformWithdrawShareOrderEntity.getShareUserDayV1());
		toVShareMoney(platformWithdrawShareOrderEntity.getOrderDateNo(),1,platformWithdrawShareOrderEntity.getShareUserDayV1());
		logger.info("---------------------------提现分红------v2---------------"+platformWithdrawShareOrderEntity.getShareUserDayV2());
		toVShareMoney(platformWithdrawShareOrderEntity.getOrderDateNo(),2,platformWithdrawShareOrderEntity.getShareUserDayV2());
		logger.info("---------------------------提现分红------v3---------------"+platformWithdrawShareOrderEntity.getShareUserDayV3());
		toVShareMoney(platformWithdrawShareOrderEntity.getOrderDateNo(),3,platformWithdrawShareOrderEntity.getShareUserDayV3());
		logger.info("---------------------------提现分红------v4---------------"+platformWithdrawShareOrderEntity.getShareUserDayV4());
		toVShareMoney(platformWithdrawShareOrderEntity.getOrderDateNo(),4,platformWithdrawShareOrderEntity.getShareUserDayV4());
		logger.info("---------------------------提现分红------v5---------------"+platformWithdrawShareOrderEntity.getShareUserDayV5());
		toVShareMoney(platformWithdrawShareOrderEntity.getOrderDateNo(),5,platformWithdrawShareOrderEntity.getShareUserDayV5());
		
		
		logger.info("---------------------------提现分红------v6---------------"+platformWithdrawShareOrderEntity.getShareUserDayV6());
		toVShareMoney(platformWithdrawShareOrderEntity.getOrderDateNo(),6,platformWithdrawShareOrderEntity.getShareUserDayV6());
		logger.info("---------------------------提现分红------v7---------------"+platformWithdrawShareOrderEntity.getShareUserDayV7());
		toVShareMoney(platformWithdrawShareOrderEntity.getOrderDateNo(),7,platformWithdrawShareOrderEntity.getShareUserDayV7());
		logger.info("---------------------------提现分红------v8---------------"+platformWithdrawShareOrderEntity.getShareUserDayV8());
		toVShareMoney(platformWithdrawShareOrderEntity.getOrderDateNo(),8,platformWithdrawShareOrderEntity.getShareUserDayV8());
		logger.info("---------------------------提现分红------v9---------------"+platformWithdrawShareOrderEntity.getShareUserDayV9());
		toVShareMoney(platformWithdrawShareOrderEntity.getOrderDateNo(),9,platformWithdrawShareOrderEntity.getShareUserDayV9());
		logger.info("---------------------------提现分红------v10---------------"+platformWithdrawShareOrderEntity.getShareUserDayV10());
		toVShareMoney(platformWithdrawShareOrderEntity.getOrderDateNo(),10,platformWithdrawShareOrderEntity.getShareUserDayV10());
		
	}
	private void toVShareMoney(String poolDateNumber,Integer vStarType,BigDecimal allMoney) {
		 
		 logger.info("---------------------------分红-----allMoney---------------"+allMoney);
		 List<PlatformWithdrawShareEntity> vithdrawShareVs =platformWithdrawShareService.queryByWithdrawTypeStar(vStarType);
		 
		 if(vithdrawShareVs==null||vithdrawShareVs.size()==0) {
			 
			 return;
		 }
		 if(allMoney.compareTo(new BigDecimal(0))<=0)return;
		
//		 BigDecimal everyOneMoney=allMoney.divide(new BigDecimal(vithdrawShareVs.size()));
		 
		 BigDecimal everyOneMoney= MoneyFormatUtils.getDivide(allMoney,new BigDecimal(vithdrawShareVs.size()));;
		 
		 logger.info("---------------------------分红-----v---------------"+vStarType+",会员数量："+vithdrawShareVs.size()+",分红"+JsonUtil.getJsonByObj(everyOneMoney));
		 
		 for(PlatformWithdrawShareEntity withdrawShareEntity:vithdrawShareVs) {
			  UserEntity toUserEntity=userService.getById(withdrawShareEntity.getUserId());
			//增加钱包收益，总收益，增加资产总收益 减少杠杆收益
//			 userService.addUserBalanceAndTotalIncomeAndReduceInverst(toUserEntity.getUserId(), everyOneMoney);
			 userService.addUserBalanceAndTotalIncome(toUserEntity.getUserId(), everyOneMoney);
			 //添加日收益
			 apiBonusPoolJoinMemberService.incrBonusPoolJoinMemberMoney(poolDateNumber, toUserEntity.getUserId(), everyOneMoney);
			 //写日志
			 PaymentTask paymentTask=new PaymentTask();
			 paymentTask.setAmount(everyOneMoney);
			 paymentTask.setFee(0);
			 paymentTask.setUserId(toUserEntity.getUserId());
			 paymentTask.setUserName(toUserEntity.getUserName());
			 paymentTask.setPayer("sys");
			 paymentTask.setMemo("每日提现手续费分红");
			 paymentTask.setMoneyTypeWallet(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_IN_FW_TX_PAY);
			 paymentTask.setPaymentType(PluginConstant.PAYMENT_TYPE_IN);
			 TaskPaymentProducer.addPaymentTaskVo(paymentTask); 
		 }
	}
	
	private void toUpUserShareLevel() {//根据小区升级共识机制
		
		List<UserEntity> userEntities=userService.queryAllList();
		for(UserEntity userEntity:userEntities) {
			BonusPointsVo bonusPointsVo=bonusPointsService.getUserPoint(userEntity.getUserId(), BonusConstant.BONUS_POINT_BLOODTYPE_BINARYTREE);
			int totalBonus=bonusPointsVo.getBonusTeamInvitedPoints().intValue()+bonusPointsVo.getBonusMeInvitedPoints().intValue();
//			int levelType=totalBonus/(10000*15);
			int levelType=totalBonus/(10000*100);
			int userLevelType=0;
			switch (levelType) {
			case 1:
				userLevelType=1;
				break;
			case 3:
				userLevelType=2;
				break;
			case 4:
				userLevelType=3;
				break;
			case 10:
				userLevelType=4;
				break;
			case 20:
				userLevelType=5;
				break;
			default:
				break;
			}
			
			if(userLevelType>0) {
				PlatformWithdrawShareEntity temp=platformWithdrawShareService.queryByUserId(userEntity.getUserId());
				if(temp!=null) {
					if(temp.getWithdrawTypeStar()<userLevelType) {
						temp.setWithdrawTypeStar(userLevelType);
						temp.setUpdateTime(new Date());
						platformWithdrawShareService.update(temp);
					}
				}else {
					temp=new PlatformWithdrawShareEntity();
					temp.setCreateTime(new Date());
					temp.setState(0);
					temp.setUpdateTime(new Date());
					temp.setUserId(userEntity.getUserId());
					temp.setUserName(userEntity.getUserName());
					temp.setWithdrawType(0);
					temp.setWithdrawTypeStar(userLevelType);
					platformWithdrawShareService.save(temp);
				}
				
			}
			
		}
		
	}
}
