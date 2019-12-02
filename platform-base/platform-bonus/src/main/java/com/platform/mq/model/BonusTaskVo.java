package com.platform.mq.model;

import java.math.BigDecimal;

/**
 * 待奖励
 *
 * @version 1.1
 * 	2015-07-08  增加日志写入功能  add by softopensell
 */
public class BonusTaskVo extends ThirdPartyTask {

		
		String  orderNo;//订单编号 方便记录数据
		int  orderType;//订单类型
		int  consumedUserId;//消费者ID
		BigDecimal  consumedPointMoney;//消费者金额(分)
		/**
		 * 
		 * @param consumedUserId
		 * @param consumedPointMoney
		 */
		public BonusTaskVo(String orderNo,int orderType,int consumedUserId,BigDecimal consumedPointMoney) {
			super();
			this.orderNo=orderNo;
			this.orderType=orderType;
			this.consumedUserId = consumedUserId;
			this.consumedPointMoney = consumedPointMoney;
		}
		/**
		 * @return the orderNo
		 */
		public String getOrderNo() {
			return orderNo;
		}
		/**
		 * @param orderNo the orderNo to set
		 */
		public void setOrderNo(String orderNo) {
			this.orderNo = orderNo;
		}
		/**
		 * @return the orderType
		 */
		public int getOrderType() {
			return orderType;
		}
		/**
		 * @param orderType the orderType to set
		 */
		public void setOrderType(int orderType) {
			this.orderType = orderType;
		}
		/**
		 * @return the consumedUserId
		 */
		public int getConsumedUserId() {
			return consumedUserId;
		}
		/**
		 * @param consumedUserId the consumedUserId to set
		 */
		public void setConsumedUserId(int consumedUserId) {
			this.consumedUserId = consumedUserId;
		}
		/**
		 * @return the consumedPointMoney
		 */
		public BigDecimal getConsumedPointMoney() {
			return consumedPointMoney;
		}
		/**
		 * @param consumedPointMoney the consumedPointMoney to set
		 */
		public void setConsumedPointMoney(BigDecimal consumedPointMoney) {
			this.consumedPointMoney = consumedPointMoney;
		}



		
	
}
