package com.platform.constants;

public class ActivityConstant {

	 public enum State {
	        TEMP(0,"编辑中"),
	        EDIT(1,"编辑中"),
	        PULISHED(2,"发布"),
	        OFFLINE(3,"下线"),
	        DELETE(10,"删除");
		 	public int nCode;
	        public String nCodeName;
	        private State(int _nCode,String _nCodeName) {
	            this.nCode = _nCode;
	            this.nCodeName = _nCodeName;
	        }
	 };
	 public enum AuditState {
		 DEFAULT(0,"未审核"),
		 APPLY(1,"待审核"),
		 PASS(2,"已通过"),
		 FAIL(3,"已拒绝"),
		 RETOPASS(4,"重审中");
		 public int nCode;
		 public String nCodeName;
		 private AuditState(int _nCode,String _nCodeName) {
			 this.nCode = _nCode;
			 this.nCodeName = _nCodeName;
		 }
	 };
	 public enum ItemPayType {
		 BALANCE(0,"现金"),
		 POIT(1,"积分"),
		 ASSET(2,"资产");
		 public int nCode;
		 public String nCodeName;
		 private ItemPayType(int _nCode,String _nCodeName) {
			 this.nCode = _nCode;
			 this.nCodeName = _nCodeName;
		 }
	 };
	 
	 public enum TicketOrderStatu {
		 DEFAULT(0,"未支付"),
		 PAYSUCCESS(1,"支付成功"),
		 AUDITSUCCESS(2,"确认订单，【发货】"),
		 AUDITFAIL(21,"退款"),
		 CHECKIN(100,"已签到【签收】"),
		 DELETE(4,"删除");
		 public int nCode;
		 public String nCodeName;
		 private TicketOrderStatu(int _nCode,String _nCodeName) {
			 this.nCode = _nCode;
			 this.nCodeName = _nCodeName;
		 }
	 };
}
