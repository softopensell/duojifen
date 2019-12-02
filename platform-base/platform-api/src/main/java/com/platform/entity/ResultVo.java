package com.platform.entity;

import java.io.Serializable;


public class ResultVo implements Serializable {
    private static final long serialVersionUID = 1L;

  //体质报告结果
    private Integer questionTypeId;
  //结论表ID
    private Integer resultId;
    //预约订单号
    private String appointmentNo;
    //结论编码
	private String  resultCode;
	//结论标题
    private String resultTitle;
    //结论描述
    private String resultDesc;

	
    public String getAppointmentNo() {
		return appointmentNo;
	}

	public void setAppointmentNo(String appointmentNo) {
		this.appointmentNo = appointmentNo;
	}


	public Integer getQuestionTypeId() {
		return questionTypeId;
	}

	public void setQuestionTypeId(Integer questionTypeId) {
		this.questionTypeId = questionTypeId;
	}

	public Integer getResultId() {
		return resultId;
	}

	public void setResultId(Integer resultId) {
		this.resultId = resultId;
	}


	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultTitle() {
		return resultTitle;
	}

	public void setResultTitle(String resultTitle) {
		this.resultTitle = resultTitle;
	}

	public String getResultDesc() {
		return resultDesc;
	}

	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}
    
    
}
