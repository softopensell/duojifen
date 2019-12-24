package com.platform.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.platform.entity.PaymentInfoEntity;
import com.platform.entity.PaymentLogEntity;
import com.platform.entity.PaymentOutEntity;
import com.platform.entity.UserEntity;

/**
 * Service接口
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-29 22:33:28
 */
public interface PaymentOutService {

    /**
     * 根据主键查询实体
     *
     * @param id 主键
     * @return 实体
     */
    PaymentOutEntity queryObject(Long id);

    /**
     * 分页查询
     *
     * @param map 参数
     * @return list
     */
    List<PaymentOutEntity> queryList(Map<String, Object> map);
    List<PaymentOutEntity> queryAll(Map<String, Object> map);
    
    Map<String,Object> queryStat(Map<String, Object> params);

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
     * @param paymentOut 实体
     * @return 保存条数
     */
    int save(PaymentOutEntity paymentOut);

    /**
     * 根据主键更新实体
     *
     * @param paymentOut 实体
     * @return 更新条数
     */
    int update(PaymentOutEntity paymentOut);

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
    
    /**
     * 更新提现申请数据
     * @param paymentOut
     * @param paymentInfo
     * @param paymentLog
     * @param user
     */
    void updatePayMentData(PaymentOutEntity paymentOut,PaymentInfoEntity paymentInfo,PaymentLogEntity paymentLog,UserEntity user);
    
    public void savePaymentOutData(PaymentOutEntity paymentOut,PaymentInfoEntity paymentInfo,UserEntity user);
}
