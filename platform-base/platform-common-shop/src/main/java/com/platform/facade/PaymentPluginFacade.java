package com.platform.facade;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import com.platform.entity.PaymentInfoEntity;
import com.platform.entity.PaymentLogEntity;
import com.platform.entity.UserEntity;
import com.platform.mq.model.PaymentTask;
import com.platform.util.constants.TradeStatus;




public interface PaymentPluginFacade {

    /**
	 * 记录流水
	 * @param memberId
	 * @param addBalance
	 * @return
	 */
  public void addPaymentTask(PaymentTask paymentTask);

  public PaymentInfoEntity findLastPaymentByOrderNoAndOrderType(String orderNo,Integer orderType);
  /**
   * 获取支付订单
   * @param sn
   * @return
   */
  public PaymentInfoEntity findPaymentBySn(String paySn);
	 
	  /**
	   * 提交支付订单
	   * @param companySn 系统公司编号 
	   * @param paySn 支付流水号
	   * @param payMethod 支付方式 
	   * @param payMethodName 支付方式名称
	   * @param receiptAccount 收款号 默认公司账号
	   * @param orderType 订单类型
	   * @param orderNo 订单编号
	   * @param amountBank 金额
	   * @param feeBank 邮费
	   * @param orderDesc 描述
	   * @param visitor 操作用户
	   * @param memberId 会员ID
	   * @param memberName 会员名称
	   * @param remark 备注
	   * @param tradeStatus 支付状态 默认为未支付，
	   * @param moneyTypeWallet 对钱包的影响
	   * @param paymentType   //支付类型：0 进账 1 出账
	   * @return
	   */
	  public PaymentInfoEntity submitPayOrder(String companySn,String paySn,String payMethod,String payMethodName,String receiptAccount,
				 Integer orderType,String orderNo,BigDecimal amountBank,
				 BigDecimal feeBank, String orderDesc,UserEntity loginUser, 
				Integer memberId,String memberName,String remark,TradeStatus tradeStatus,
				Integer moneyTypeWallet,Integer paymentType);
	  /**
	   * 提交支付订单
	   * @param companySn 系统公司编号 
	   * @param paySn 支付流水号
	   * @param payMethod 支付方式 
	   * @param payMethodName 支付方式名称
	   * @param orderType 订单类型
	   * @param orderNo 订单编号
	   * @param amountBank 金额
	   * @param feeBank 邮费
	   * @param orderDesc 描述
	   * @param visitor 操作用户
	   * @param memberId 会员ID
	   * @param memberName 会员名称
	   * @return
	   */
//	  public PaymentInfoVo submitPayOrder(String companySn,String paySn,String payMethod,String payMethodName,
//				 Integer orderType,String orderNo,BigDecimal amountBank,
//				double feeBank, String orderDesc,UserVo loginUser, 
//				Integer memberId,String memberName);
	  /**
	   * 
	   * @param companySn 系统公司编号 
	   * @param paySn 支付流水号
	   * @param payMethod 支付方式 
	   * @param payMethodName 支付方式名称
	   * @param receiptAccount 收款号 默认公司账号
	   * @param orderType 订单类型
	   * @param orderNo 订单编号
	   * @param amountBank 金额
	   * @param feeBank 邮费
	   * @param orderDesc 描述
	   * @param visitor 操作用户
	   * @param memberId 会员ID
	   * @param memberName 会员名称
	   * @param remark 备注
	   * @param tradeStatus 支付状态 默认为未支付，
	   * @param payWorkCallable
	   * @return
	   */
//	  public PaymentInfoVo submitPayOrder(String companySn,String paySn,String payMethod,String payMethodName,String receiptAccount,
//				 Integer orderType,String orderNo,BigDecimal amountBank,
//				double feeBank, String orderDesc,UserVo loginUser, 
//				Integer memberId,String memberName,String remark,TradeStatus tradeStatus,Integer moneyTypeWallet,PayWorkCallable<PaymentInfoVo> payWorkCallable);
	  
	  /**
	   * 
	   * @param localPaymentPlugin
	   * @param localPayment
	   * @param tradeStatus
	   */
	  public PaymentInfoEntity dealOrder(PaymentInfoEntity localPayment, TradeStatus tradeStatus);
	  public PaymentInfoEntity dealOrder(PaymentInfoEntity localPayment, TradeStatus tradeStatus,PayWorkCallable<PaymentInfoEntity> payWorkCallable);
	 
	  
//	  public List<PaymentInfoVo> findPayments( Integer curPage, Integer pageSize);
	 
	  
	  /**
	   * 支付日志
	   * @param paySn
	   * @param steps
	   * @param remark
	   * @param request
	   * @param message
	   * @return
	   */
	  public PaymentLogEntity PaymentLogSubmit(String paySn, Integer steps, String remark,
				HttpServletRequest request,String message);
	  /**
	   * 支付日志
	   * @param paySn
	   * @param steps
	   * @param remark
	   * @param request
	   * @return
	   */
	  public PaymentLogEntity PaymentLogSubmit(String paySn, Integer steps, String remark,
				HttpServletRequest request);
	  
	 /**
	  * 查询所有提现订单
	  * @param matchs
	  * @param orders
	  * @param curPage
	  * @param pageSize
	  * @return
	  */
//	  public List<PaymentOutVo> findPaymentOutVos( Integer curPage, Integer pageSize);
	  
	  
//	  public boolean createUserVoConsumePaymentOutVo(int memberId,String memberName,double amount,  UserVo loginUser);
	  /**
	   * 申请提现 
	   * @param paymentOut
	   * @param paymentAudits 审核名单 ，不审核为空
	   * @param payWorkCallable 
	   * @return
	   */
//	  public boolean createPaymentOutVoMoney(PaymentOutVo paymentOut,List<PaymentAudit> paymentAudits,PayWorkCallable<PaymentOutVo> payWorkCallable);
	  
	  /**
	   * 
	   * @param paymentOut
	   * @param payWorkCallable
	   * @return
	   */
//	  public boolean updatePaymentOutVoMoney(PaymentOutVo paymentOut,PayWorkCallable<PaymentOutVo> payWorkCallable);
//	  public boolean addOrUpdatePaymentAudit(PaymentAudit paymentAudit);
//	  public boolean addPaymentAudits(List<PaymentAudit> paymentAudits);
	  
	  /**
	   * 查找发起的审批
	   * @param matchs
	   * @param orders
	   * @param curPage
	   * @param pageSize
	   * @return
	   */
//	  public List<PaymentOutVo> findPaymentOutVosByUserVoId(Integer memberId, Integer curPage, Integer pageSize);
	  /**
	   * 查找用户审批的账单
	   * @param memberId
	   * @param curPage
	   * @param pageSize
	   * @return
	   */
//	  public Page<PaymentAudit> findUserVoAuditPaymentAuditsByUserVoId(Integer memberId, Integer curPage, Integer pageSize);
//	  public Page<PaymentAudit> findUserVoAuditPaymentAuditsByAuditTypeAndSn(
//				int auditType,String auditSN, Integer curPage, Integer pageSize);
	 
	  /**
	   * 申请提现 
	   * @param member
	   * @param paymentOut
	   * @param payment
	   * @return
	   */
//	  public boolean createPaymentOutVo(UserVo member,PaymentOutVo paymentOut,PaymentInfoVo payment);
	  
	  /**
	   * 退款
	   * 
	   * @param memberId 会员ID
	   * @param refundType 类型：0 现金余额，1 积分 2 资产
	   * @param money 金额：
	   * @return
	   * 
	   */
	  public boolean refundMoney(int memberId,int refundType,BigDecimal money,Integer orderType, String orderNo,String remark);
	  
	  /**
	   * 监控用户异常数据
	   * @return
	   */
	  public boolean monitorUser();
	  
	  
}
