package com.platform.facade;





public interface MallFacade {
	

	
	/**
	 * 支付成功,更新状态，分红
	 * @param orderNo
	 * @return
	 */
	public  void  paySuccessOrder(String orderNo,int payType);
	/**
	 * 
	 * 余额支付，记录流水
	 * @param orderNo
	 * @return
	 */
	public void balancePay(String orderNo); 
	
	
	/**
	 * 系统取消超过一个小时未支付的订单
	 * @return
	 */
	public void  sysCancelOrder();


}
