package com.platform.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.platform.entity.GoodsOrderEntity;
import com.platform.entity.UserEntity;

/**
 * Service接口
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-25 15:10:15
 */
public interface GoodsOrderService {

    /**
     * 根据主键查询实体
     *
     * @param id 主键
     * @return 实体
     */
    GoodsOrderEntity queryObject(Integer id);
    public GoodsOrderEntity queryObjectByNo(String orderNo);
    /**
     * 分页查询
     *
     * @param map 参数
     * @return list
     */
    List<GoodsOrderEntity> queryList(Map<String, Object> map);

    /**
     * 分页统计总数
     *
     * @param map 参数
     * @return 总数
     */
    int queryTotal(Map<String, Object> map);
    /**
     * 分页统计总金额
     *
     * @param map 参数
     * @return 总数
     */
    Map<String,Object> queryTotalPrice(Map<String, Object> map);
    /**
     * 保存实体
     *
     * @param goodsOrder 实体
     * @return 保存条数
     */
    int save(GoodsOrderEntity goodsOrder);

    /**
     * 根据主键更新实体
     *
     * @param goodsOrder 实体
     * @return 更新条数
     */
    int update(GoodsOrderEntity goodsOrder);

    /**
     * 根据主键删除
     *
     * @param id
     * @return 删除条数
     */
    int delete(Integer id);

    /**
     * 根据主键批量删除
     *
     * @param ids
     * @return 删除条数
     */
    int deleteBatch(Integer[] ids);
    
    int queryTotalByStatus(Integer orderStatus);
    Map<String,Object> queryTotalByDate(Map<String, Object> map);
	public Map<String, Object> submit(JSONObject jsonParam, int userId);
	
	/**
	 * 直接购买订单
	 * @param userEntity
	 * @param goodId
	 * @param sum
	 * @param isUserPay 1 表示用户自己购买
	 * @return
	 */
	public Map<String, Object> toSubmitImmediateBuyGoodOrder(UserEntity userEntity,int goodId,int sum,int payType,String goodTags);
	
	
}
