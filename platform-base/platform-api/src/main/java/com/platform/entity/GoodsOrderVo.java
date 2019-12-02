package com.platform.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 实体
 * 表名 t_goods_order
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-28 23:01:26
 */
public class GoodsOrderVo implements Serializable {
    private static final long serialVersionUID = 1L;

    //主键ID
    private Integer id;
    //店铺ID
    private Integer shopId;
    //订单号
    private String orderNo;
    //买的类型 0 默认值为线上商城订单, 1线下手工录入订单
    private Integer orderType;
    //用户id
    private Integer userId;
    //购买商品总数量
    private Integer goodsCount;
    //快递费用
    private BigDecimal expressPrice;
    //使用积分数
    private BigDecimal totalIntegralNum;
    //使用积分抵扣价
    private BigDecimal totalIntegralPrice;
    //在线支付总费用
    private BigDecimal totalPayPrice;
    //总价
    private BigDecimal totalPrice;
    //状态：	1待付款、2待发货、3待收货、4已完成、  5、用户主动取消   6 超时未支付取消7、审核员取消订单8、退货9、超时确认收货
    private Integer orderStatus;
    //支付类型:0货到付款1微信 2支付宝3余额4、积分支付5、线下现金交易6、线下刷卡交易
    private Integer payType;
    //支付状态：0、待付款  1、付款中  2 付款成功 3 付款失败    4、退款中 5、退款成功 6、退款失败
    private Integer payStatus;
    //支付单号
    private String payNo;
    //支付时间
    private Date payTime;
    //默认关联地址
    private Integer addressId;
    //省
    private Integer provinceId;
    //市
    private Integer cityId;
    //区
    private Integer regionId;
    //地址
    private String address;
    //总和地址
    private String pcrdetail;
    //电话
    private String telephone;
    //联系人
    private String contactName;
    //邮编
    private String zipcode;
    //不限送货时间 1\n工作日送货 2\n双休日、假日送货 3
    private Integer sendOrderTimeType;
    //发货时间
    private Date sendOrderTime;
    //发货状态:0未发货1已发货2已签收
    private String sendStatu;
    //物流公司
    private String logisticsName;
    //物流订单号
    private String logisticsNumber;
    //物流状态
    private Integer logisticsStatu;
    //签收人
    private String logisticsReceiveName;
    //签收时间
    private Date logisticsReceiveTime;
    //创建时间
    private Date createTime;
    //修改时间
    private Date updateTime;
    
    private List<GoodsOrderDetailVo> orderDetailList ;

    public List<GoodsOrderDetailVo> getOrderDetailList() {
		return orderDetailList;
	}

	public void setOrderDetailList(List<GoodsOrderDetailVo> orderDetailList) {
		this.orderDetailList = orderDetailList;
	}

	/**
     * 设置：主键ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取：主键ID
     */
    public Integer getId() {
        return id;
    }
    /**
     * 设置：店铺ID
     */
    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    /**
     * 获取：店铺ID
     */
    public Integer getShopId() {
        return shopId;
    }
    /**
     * 设置：订单号
     */
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    /**
     * 获取：订单号
     */
    public String getOrderNo() {
        return orderNo;
    }
    /**
     * 设置：买的类型 0 默认值为线上商城订单, 1线下手工录入订单
     */
    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    /**
     * 获取：买的类型 0 默认值为线上商城订单, 1线下手工录入订单
     */
    public Integer getOrderType() {
        return orderType;
    }
    /**
     * 设置：用户id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取：用户id
     */
    public Integer getUserId() {
        return userId;
    }
    /**
     * 设置：购买商品总数量
     */
    public void setGoodsCount(Integer goodsCount) {
        this.goodsCount = goodsCount;
    }

    /**
     * 获取：购买商品总数量
     */
    public Integer getGoodsCount() {
        return goodsCount;
    }
    /**
     * 设置：快递费用
     */
    public void setExpressPrice(BigDecimal expressPrice) {
        this.expressPrice = expressPrice;
    }

    /**
     * 获取：快递费用
     */
    public BigDecimal getExpressPrice() {
        return expressPrice;
    }
    /**
     * 设置：使用积分数
     */
    public void setTotalIntegralNum(BigDecimal totalIntegralNum) {
        this.totalIntegralNum = totalIntegralNum;
    }

    /**
     * 获取：使用积分数
     */
    public BigDecimal getTotalIntegralNum() {
        return totalIntegralNum;
    }
    /**
     * 设置：使用积分抵扣价
     */
    public void setTotalIntegralPrice(BigDecimal totalIntegralPrice) {
        this.totalIntegralPrice = totalIntegralPrice;
    }

    /**
     * 获取：使用积分抵扣价
     */
    public BigDecimal getTotalIntegralPrice() {
        return totalIntegralPrice;
    }
    /**
     * 设置：在线支付总费用
     */
    public void setTotalPayPrice(BigDecimal totalPayPrice) {
        this.totalPayPrice = totalPayPrice;
    }

    /**
     * 获取：在线支付总费用
     */
    public BigDecimal getTotalPayPrice() {
        return totalPayPrice;
    }
    /**
     * 设置：总价
     */
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     * 获取：总价
     */
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
    /**
     * 设置：状态：	1待付款、2待发货、3待收货、4已完成、  5、用户主动取消   6 超时未支付取消7、审核员取消订单8、退货9、超时确认收货
     */
    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    /**
     * 获取：状态：	1待付款、2待发货、3待收货、4已完成、  5、用户主动取消   6 超时未支付取消7、审核员取消订单8、退货9、超时确认收货
     */
    public Integer getOrderStatus() {
        return orderStatus;
    }
    /**
     * 设置：支付类型:0货到付款1微信 2支付宝3余额4、积分支付5、线下现金交易6、线下刷卡交易
     */
    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    /**
     * 获取：支付类型:0货到付款1微信 2支付宝3余额4、积分支付5、线下现金交易6、线下刷卡交易
     */
    public Integer getPayType() {
        return payType;
    }
    /**
     * 设置：支付状态：0、待付款  1、付款中  2 付款成功 3 付款失败    4、退款中 5、退款成功 6、退款失败
     */
    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    /**
     * 获取：支付状态：0、待付款  1、付款中  2 付款成功 3 付款失败    4、退款中 5、退款成功 6、退款失败
     */
    public Integer getPayStatus() {
        return payStatus;
    }
    /**
     * 设置：支付单号
     */
    public void setPayNo(String payNo) {
        this.payNo = payNo;
    }

    /**
     * 获取：支付单号
     */
    public String getPayNo() {
        return payNo;
    }
    /**
     * 设置：支付时间
     */
    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    /**
     * 获取：支付时间
     */
    public Date getPayTime() {
        return payTime;
    }
    /**
     * 设置：默认关联地址
     */
    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    /**
     * 获取：默认关联地址
     */
    public Integer getAddressId() {
        return addressId;
    }
    /**
     * 设置：省
     */
    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    /**
     * 获取：省
     */
    public Integer getProvinceId() {
        return provinceId;
    }
    /**
     * 设置：市
     */
    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    /**
     * 获取：市
     */
    public Integer getCityId() {
        return cityId;
    }
    /**
     * 设置：区
     */
    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }

    /**
     * 获取：区
     */
    public Integer getRegionId() {
        return regionId;
    }
    /**
     * 设置：地址
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 获取：地址
     */
    public String getAddress() {
        return address;
    }
    /**
     * 设置：总和地址
     */
    public void setPcrdetail(String pcrdetail) {
        this.pcrdetail = pcrdetail;
    }

    /**
     * 获取：总和地址
     */
    public String getPcrdetail() {
        return pcrdetail;
    }
    /**
     * 设置：电话
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     * 获取：电话
     */
    public String getTelephone() {
        return telephone;
    }
    /**
     * 设置：联系人
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * 获取：联系人
     */
    public String getContactName() {
        return contactName;
    }
    /**
     * 设置：邮编
     */
    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    /**
     * 获取：邮编
     */
    public String getZipcode() {
        return zipcode;
    }
    /**
     * 设置：不限送货时间 1\n工作日送货 2\n双休日、假日送货 3
     */
    public void setSendOrderTimeType(Integer sendOrderTimeType) {
        this.sendOrderTimeType = sendOrderTimeType;
    }

    /**
     * 获取：不限送货时间 1\n工作日送货 2\n双休日、假日送货 3
     */
    public Integer getSendOrderTimeType() {
        return sendOrderTimeType;
    }
    /**
     * 设置：发货时间
     */
    public void setSendOrderTime(Date sendOrderTime) {
        this.sendOrderTime = sendOrderTime;
    }

    /**
     * 获取：发货时间
     */
    public Date getSendOrderTime() {
        return sendOrderTime;
    }
    /**
     * 设置：发货状态:0未发货1已发货2已签收
     */
    public void setSendStatu(String sendStatu) {
        this.sendStatu = sendStatu;
    }

    /**
     * 获取：发货状态:0未发货1已发货2已签收
     */
    public String getSendStatu() {
        return sendStatu;
    }
    /**
     * 设置：物流公司
     */
    public void setLogisticsName(String logisticsName) {
        this.logisticsName = logisticsName;
    }

    /**
     * 获取：物流公司
     */
    public String getLogisticsName() {
        return logisticsName;
    }
    /**
     * 设置：物流订单号
     */
    public void setLogisticsNumber(String logisticsNumber) {
        this.logisticsNumber = logisticsNumber;
    }

    /**
     * 获取：物流订单号
     */
    public String getLogisticsNumber() {
        return logisticsNumber;
    }
    /**
     * 设置：物流状态
     */
    public void setLogisticsStatu(Integer logisticsStatu) {
        this.logisticsStatu = logisticsStatu;
    }

    /**
     * 获取：物流状态
     */
    public Integer getLogisticsStatu() {
        return logisticsStatu;
    }
    /**
     * 设置：签收人
     */
    public void setLogisticsReceiveName(String logisticsReceiveName) {
        this.logisticsReceiveName = logisticsReceiveName;
    }

    /**
     * 获取：签收人
     */
    public String getLogisticsReceiveName() {
        return logisticsReceiveName;
    }
    /**
     * 设置：签收时间
     */
    public void setLogisticsReceiveTime(Date logisticsReceiveTime) {
        this.logisticsReceiveTime = logisticsReceiveTime;
    }

    /**
     * 获取：签收时间
     */
    public Date getLogisticsReceiveTime() {
        return logisticsReceiveTime;
    }
    /**
     * 设置：创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取：创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }
    /**
     * 设置：修改时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取：修改时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }
}
