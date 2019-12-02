package com.platform.api;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.platform.annotation.FrequencyLimit;
import com.platform.annotation.IgnoreAuth;
import com.platform.annotation.LoginUser;
import com.platform.annotation.SingleLock;
import com.platform.cache.IdWorkCache;
import com.platform.constants.ActivityConstant;
import com.platform.constants.PluginConstant;
import com.platform.entity.ActivityItemEntity;
import com.platform.entity.ActivityTickeorderEntity;
import com.platform.entity.GoodsCartVo;
import com.platform.entity.GoodsVo;
import com.platform.entity.UserEntity;
import com.platform.entity.UserVo;
import com.platform.mq.model.PaymentTask;
import com.platform.mq.producer.TaskPaymentProducer;
import com.platform.service.ActivityItemService;
import com.platform.service.ActivityTickeorderService;
import com.platform.service.UserService;
import com.platform.util.ApiBaseAction;
import com.platform.util.CommonUtil;
import com.platform.util.constants.OrderConstant;
import com.platform.utils.DateUtils;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.RandomUtil;
import com.platform.utils.StringUtils;
import com.platform.validator.Assert;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * ApiController
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-11-05 23:10:44
 */
@Api(tags = "ActivityTickeorder") 
@RestController
@RequestMapping("/api/activitytickeorder")
public class ApiActivityTickeorderController extends ApiBaseAction {
    @Autowired
    private ActivityTickeorderService activityTickeorderService;
    @Autowired
    private ActivityItemService activityItemService;
    @Autowired
    private UserService userService;

    /**
     * 查看列表
     */
    @ApiOperation(value = "ActivityTickeorder_查看列表")
    @PostMapping("/list")
    public Object list(@LoginUser UserVo loginUser) {
                  
         JSONObject jsonParam = getJsonRequest();
        Integer page = 1;
        Integer size = 10;
         page = jsonParam.getInteger("page");
         size = jsonParam.getInteger("size");         
         if(loginUser.getUserId()<=0) {
        	 return this.toResponsFail("获取数据失败！");
         }
    	Map params = new HashMap();
        params.put("memberId", loginUser.getUserId());
        params.put("page", page);
        params.put("limit", size);
        params.put("sidx", "id");
        params.put("order", "desc");
        //查询列表数据
        Query query = new Query(params);

        List<ActivityTickeorderEntity> activityTickeorderList = activityTickeorderService.queryList(query);
        int total = activityTickeorderService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(activityTickeorderList, total, query.getLimit(), query.getPage());

        return toResponsSuccess(pageUtil);
    }

    /**
     * 查看信息
     */
    @ApiOperation(value = "ActivityTickeorder_查看信息")
    @RequestMapping("/info")
    public Object info(@LoginUser UserVo loginUser) {
    	JSONObject jsonParam = getJsonRequest();
        Integer id = jsonParam.getInteger("id");
     	Map resultObj = new HashMap();
     	ActivityTickeorderEntity activityTickeorder = activityTickeorderService.queryObject(id);
		if (null == activityTickeorder) {
            return toResponsObject(400, "数据不存在", "");
        }
		resultObj.put("activityTickeorder", activityTickeorder);
		return toResponsSuccess(resultObj);
    }
    

    
    @ApiOperation(value = "报名活动")
    @PostMapping("join")
    @SingleLock
    public Object join(@LoginUser UserVo loginUser) {
        JSONObject jsonParam = getJsonRequest();
        UserEntity toUserEntity=userService.queryObject(loginUser.getUserId()); 
        Integer itemId = jsonParam.getInteger("id");
        Integer ticketSum = jsonParam.getInteger("ticketSum");
        String memberPhone = jsonParam.getString("memberPhone");
        String memberName = jsonParam.getString("memberName");
        String memberAddress = jsonParam.getString("memberAddress");
        String memberRemark = jsonParam.getString("memberRemark");
        
        if(StringUtils.isNullOrEmpty(memberPhone)){
			return toResponsFail("手机号不能为空");
		}
        if(StringUtils.isNullOrEmpty(memberName)){
        	return toResponsFail("联系人不能为空");
        }
        if(ticketSum<=0){
        	return toResponsFail("参与人数不能为空");
        }
        
        
        ActivityItemEntity activityItemEntity=activityItemService.queryObject(itemId);
        
        int totalPrice= activityItemEntity.getProductPrice()*ticketSum;
        
    	String orderNo = "T"+DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN_YYYY_MM_DD_HH_MM_SS_SSS)+RandomUtil.getRandom(1000l,9999l);
        
        ActivityTickeorderEntity activityTickeorder= new ActivityTickeorderEntity();
        activityTickeorder.setCreateTime(new Date());
        activityTickeorder.setItemTitle(activityItemEntity.getTitle());
        activityTickeorder.setMemberAddress(memberAddress);
        activityTickeorder.setMemberName(memberName);
        activityTickeorder.setMemberPhone(memberPhone);
        activityTickeorder.setMemberId(loginUser.getUserId());
        activityTickeorder.setMemberRemark(memberRemark);
        activityTickeorder.setMemberStatu(0);
        activityTickeorder.setOrderNo(orderNo);
        activityTickeorder.setOrderStatu(ActivityConstant.TicketOrderStatu.DEFAULT.nCode);
        activityTickeorder.setOrderType(OrderConstant.ORDER_TYPE_ACTIVTIY);
        activityTickeorder.setPayStatu(0);
        activityTickeorder.setStatu(0);
        activityTickeorder.setTicketId(0);
        activityTickeorder.setTicketName(toUserEntity.getUserName());
        activityTickeorder.setTicketPrice(activityItemEntity.getProductPrice());
        activityTickeorder.setTicketSum(ticketSum);
        activityTickeorder.setTotalPrice(totalPrice);
        activityTickeorder.setUpdateTime(new Date());
        activityTickeorder.setCompanySn("");
        activityTickeorder.setMemberStatu(activityItemEntity.getItemPayType());
        activityTickeorder.setItemNo(activityItemEntity.getItemNo());
       
		 PaymentTask paymentTask=new PaymentTask();
		 
		 if(activityItemEntity.getItemPayType()==0) {
			 paymentTask.setMoneyTypeWallet(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_OUT_CONSUMED);
			 BigDecimal balance = toUserEntity.getBalance();
			 if(balance==null)balance=new BigDecimal(0);
			 if(balance.intValue()<totalPrice) {
				 return this.toResponsFail("余额不足！");
			 }
			 BigDecimal sub_balance = balance.subtract(new BigDecimal(totalPrice));
			 toUserEntity.setBalance(sub_balance);
			 
		 }else if(activityItemEntity.getItemPayType()==1) {
			 paymentTask.setMoneyTypeWallet(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_IN_INTEGRAL_PAY);
			 Integer point = toUserEntity.getIntegralScore();
			 if(point==null)point=0;
			 if(point.intValue()<totalPrice) {
				 return this.toResponsFail("积分不足！");
			 }
			 int sub_point = point-totalPrice;
			 toUserEntity.setIntegralScore(sub_point);
		 }else if(activityItemEntity.getItemPayType()==2) {
			 paymentTask.setMoneyTypeWallet(PluginConstant.PAYMENT_MONEY_TYPE_WALLET_OUT_ZC);
			 BigDecimal surplusInvestMoney = toUserEntity.getSurplusInvestMoney();
			 if(surplusInvestMoney==null)surplusInvestMoney=new BigDecimal(0);
			 if(surplusInvestMoney.intValue()<totalPrice) {
				 return this.toResponsFail("资产不足！");
			 }
			 BigDecimal sub_surplusInvestMoney = surplusInvestMoney.subtract(new BigDecimal(totalPrice));//剩余收益 额度 增加
			 toUserEntity.setSurplusInvestMoney(sub_surplusInvestMoney);
		 }
		 
		 activityTickeorder.setOrderStatu(ActivityConstant.TicketOrderStatu.PAYSUCCESS.nCode);
		 
		 activityTickeorderService.save(activityTickeorder);
		 activityItemEntity.setApplyPeopleSum(activityItemEntity.getApplyPeopleSum()+ticketSum);
		 activityItemService.update(activityItemEntity);
		 userService.update(toUserEntity);
		 paymentTask.setAmount(new BigDecimal(totalPrice));
		 paymentTask.setFee(0);
		 paymentTask.setUserId(toUserEntity.getUserId());
		 paymentTask.setPayer(toUserEntity.getUserName());
		 paymentTask.setUserName(toUserEntity.getUserName());
		 paymentTask.setMemo("参加活动消费："+new BigDecimal(totalPrice));
		 paymentTask.setPaymentType(PluginConstant.PAYMENT_TYPE_OUT);
		 paymentTask.setOrderNo(orderNo);
		 paymentTask.setOrderType(OrderConstant.ORDER_TYPE_ACTIVTIY);
		 
		 TaskPaymentProducer.addPaymentTaskVo(paymentTask);
        
        
        return this.toResponsMsgSuccess("报名成功！");
    }
    @ApiOperation(value = "签到")
    @PostMapping("checkIn")
    @SingleLock
    public Object checkIn(@LoginUser UserVo loginUser) {
    	JSONObject jsonParam = getJsonRequest();
    	Integer  id= jsonParam.getInteger("id");
    	ActivityTickeorderEntity activityTickeorder= activityTickeorderService.queryObject(id);
    	if(!activityTickeorder.getMemberId().equals(loginUser.getUserId())){
    		return toResponsFail("没有操作权限");
    	}
    	if(activityTickeorder.getOrderStatu().intValue()==ActivityConstant.TicketOrderStatu.PAYSUCCESS.nCode
    			||activityTickeorder.getOrderStatu().intValue()==ActivityConstant.TicketOrderStatu.AUDITSUCCESS.nCode
    			) {
    		activityTickeorder.setOrderStatu(ActivityConstant.TicketOrderStatu.CHECKIN.nCode);
        	activityTickeorderService.update(activityTickeorder);
    	}
    	return this.toResponsMsgSuccess("成功！");
    }

}
