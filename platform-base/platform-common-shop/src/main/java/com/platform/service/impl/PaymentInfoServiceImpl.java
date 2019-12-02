package com.platform.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platform.cache.IdWorkCache;
import com.platform.dao.PaymentInfoDao;
import com.platform.entity.PaymentInfoEntity;
import com.platform.mq.model.PaymentTask;
import com.platform.service.PaymentInfoService;
import com.platform.util.constants.TradeStatus;
import com.platform.utils.DateUtils;
import com.platform.utils.JsonUtil;
import com.platform.utils.RandomUtil;

/**
 * Service实现类
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-29 22:33:28
 */
@Service("paymentInfoService")
public class PaymentInfoServiceImpl implements PaymentInfoService {
	private static final Logger LOG = LoggerFactory
			.getLogger(PaymentInfoServiceImpl.class);
    @Autowired
    private PaymentInfoDao paymentInfoDao;
    @Autowired
	private IdWorkCache idWorkCache;
	
    @Override
    public PaymentInfoEntity queryObject(Long id) {
        return paymentInfoDao.queryObject(id);
    }
    @Override
    public PaymentInfoEntity findBySn(String sn) {
    	return paymentInfoDao.findBySn(sn);
    }
    @Override
    public PaymentInfoEntity findByOrderNoAndOrderType(String orderNo,Integer orderType) {
        Map<String,Object> params = new HashMap<String,Object>();
		params.put("orderNo", orderNo);
		params.put("orderType", orderType);
        List<PaymentInfoEntity> paymentInfoEntities = this.queryList(params);
        if(paymentInfoEntities!=null&&paymentInfoEntities.size()>0) {
        	return paymentInfoEntities.get(0);
        }else {
        	return null;
        }
    }
    

    @Override
    public List<PaymentInfoEntity> queryList(Map<String, Object> map) {
        return paymentInfoDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return paymentInfoDao.queryTotal(map);
    }

    @Override
    public int save(PaymentInfoEntity paymentInfo) {
        return paymentInfoDao.save(paymentInfo);
    }

    @Override
    public int update(PaymentInfoEntity paymentInfo) {
        return paymentInfoDao.update(paymentInfo);
    }

    @Override
    public int delete(Long id) {
        return paymentInfoDao.delete(id);
    }

    @Override
    public int deleteBatch(Long[] ids) {
        return paymentInfoDao.deleteBatch(ids);
    }
    
    @Override
	public Map<String, Object> queryTotalByDate(Map<String, Object> map) {
		return paymentInfoDao.queryTotalByDate(map);
	}
    @Override
    public BigDecimal queryTodayIncome(Map<String, Object> params) {
    	BigDecimal todayIncome = new BigDecimal("0");
    	Map<String,Object> map = paymentInfoDao.queryTodayIncome(params);
    	if(map!=null&&map.containsKey("todayTotalAmount")) {
    		todayIncome = new BigDecimal(map.get("todayTotalAmount")+"");
    	}
    	return todayIncome;
    }
    @Override
   public Map<String,Object> queryStatByUserId(Integer userId,Date createTimeDate){
    	Map<String, Object> params=new HashMap<>();
		params.put("userId", userId);
		String dateStr=DateUtils.format(createTimeDate, "yyyy-MM-dd HH:mm");
		params.put("createTimeStart", dateStr);
		return paymentInfoDao.queryStatByUserId(params);
    }
    @Override
    public Map<String,Object> queryStatByDay(Date day){
    	Map<String, Object> params=new HashMap<>();
    	params.put("createTime", DateUtils.format(day));//已经切换支付时间
    	return paymentInfoDao.queryStatByDay(params);
    }
    @Override
    public List<Map<String,Object>> queryGroupStat(Integer curPage, Integer pageSize){
    	Map<String, Object> params=new HashMap<>();
	    if(curPage!=null&&pageSize!=null&&curPage>0) {
	    	params.put("offset", (curPage - 1) * pageSize);
	    	params.put("limit", pageSize);
	    }
		return paymentInfoDao.queryGroupStat(params);
    }
    @Override
    public void addPaymentTask(PaymentTask paymentTask) {
    	String paySn = "QA"+DateUtils.format2(new Date())+RandomUtil.getRandom(1000l,9999l);
    	paySn=idWorkCache.getIdDayHHMMEndRadom("PT", IdWorkCache.ID_WORK_TYPE_PAY_SN, 5);
		LOG.info("addPaymentTask------paySn---- "+JsonUtil.getJsonByObj(paySn));
    	PaymentInfoEntity payment = new PaymentInfoEntity();
		payment.setSn(paySn);
		payment.setStatus(TradeStatus.TRADE_SUCCESS.code);
		payment.setPaymentMethod("");
		payment.setPaymentPluginId("");
		payment.setFee(new BigDecimal(paymentTask.getFee()));
		payment.setAmount(paymentTask.getAmount());
		payment.setExpire(new Date());
		payment.setOrderNo(paymentTask.getOrderNo());
		payment.setOrderType(paymentTask.getOrderType());
		payment.setOrderDesc("");
		payment.setMemo(paymentTask.getMemo());
		payment.setAccount(paymentTask.getAccount());
		payment.setOperatorName("系统");
		payment.setOperatorId(0);
		payment.setUserId(paymentTask.getUserId().longValue());
		payment.setUserName(paymentTask.getUserName());
		payment.setCompanySn("");
		payment.setPaymentType(paymentTask.getPaymentType());
		payment.setMoneyTypeWallet(paymentTask.getMoneyTypeWallet());
		payment.setCreateTime(new Date());
		payment.setUpdateTime(new Date());
		payment.setPaymentDate(new Date());
		paymentInfoDao.save(payment);
    }
	
}
