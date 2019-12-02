package com.platform.util.constants;

import java.util.HashMap;
import java.util.Map;
/**
 * 物流类型
 */
public enum LogisticsType {
	/** 必填，三个值可选：EXPRESS（快递）、POST（平邮）、EMS（EMS） */
	POST(1, "平邮"), 
	EXPRESS(2, "快递"), 
	EMS(3, "EMS");
	// FIELD
	public int code;
	public String msg;
	// INDEX/REFLECT
	private static Map<Integer, LogisticsType> idxCode = new HashMap<Integer, LogisticsType>();
	public static LogisticsType forCode(int code) {
		return (idxCode.get(code));
	}
	public static LogisticsType forName(String name) {
		return (Enum.valueOf(LogisticsType.class, name));
	}
	private LogisticsType(int aCode, String aMsg) {
		this.code = aCode;
		this.msg = aMsg;
		return;
	}
	static {
		for (LogisticsType i : LogisticsType.values())
			idxCode.put(i.code, i);
	}
}