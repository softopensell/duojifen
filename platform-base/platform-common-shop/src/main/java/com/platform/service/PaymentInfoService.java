package com.platform.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.platform.entity.PaymentInfoEntity;
import com.platform.mq.model.PaymentTask;

/**
 * Service接口
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-29 22:33:28
 */
public interface PaymentInfoService {

    /**
     * 根据主键查询实体
     *
     * @param id 主键
     * @return 实体
     */
    PaymentInfoEntity queryObject(Long id);
    
    public PaymentInfoEntity findBySn(String sn);
    public PaymentInfoEntity findByOrderNoAndOrderType(String orderNo,Integer orderType);
    /**
     * 分页查询
     *
     * @param map 参数
     * @return list
     */
    List<PaymentInfoEntity> queryList(Map<String, Object> map);

    /**
     * 分页统计总数
     *
     * @param map 参数
     * @return 总数
     */
    int queryTotal(Map<String, Object> map);

    /**
     * 保存实体
     *
     * @param paymentInfo 实体
     * @return 保存条数
     */
    int save(PaymentInfoEntity paymentInfo);

    /**
     * 根据主键更新实体
     *
     * @param paymentInfo 实体
     * @return 更新条数
     */
    int update(PaymentInfoEntity paymentInfo);

    /**
     * 根据主键删除
     *
     * @param id
     * @return 删除条数
     */
    int delete(Long id);

    /**
     * 根据主键批量删除
     *
     * @param ids
     * @return 删除条数
     */
    int deleteBatch(Long[] ids);
    
    Map<String,Object> queryTotalByDate(Map<String, Object> map);
    BigDecimal queryTodayIncome(Map<String, Object> params);
	void addPaymentTask(PaymentTask paymentTask);
	
	
	Map<String,Object> queryStatByUserId(Integer userId,Date createTimeDate);
	Map<String,Object> queryStatByDay(Date day);
	List<Map<String,Object>> queryGroupStat(Integer curPage, Integer pageSize);
	
	
}
