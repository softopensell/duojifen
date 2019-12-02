package com.platform.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.commons.codec.digest.DigestUtils;

public class DateUtil extends DateTimeUtil {

	/**
	 * 获取跟当前时间相差几小时的时间，可以跨天
	 * 
	 * @param date
	 * @param fix
	 * @param type
	 * @return
	 */
	public static String getDateStrHour(String startStr, int fix, int type) {
		Date date = DateUtil.stringToDate(startStr);
		String result = "";
		String format = "yyyyMMddHH";
		if (type == 1) {
			format = "yyyy-MM-dd HH";
		} else if (type == 2) {
			format = "yyyy-MM-dd HH:mm";
		} else if (type == 3) {
			format = "yyyy-MM-dd HH:mm:ss";
		} else if (type == 4) {
			format = "yyyyMMddHH";
		} else if (type == 5) {
			format = "yyyyMMddHHmm";
		} else if (type == 5) {
			format = "yyyyMMddHHmmss";
		} else {
			format = "yyyyMMddHHmmsssss";
		}
		java.util.Calendar Cal = java.util.Calendar.getInstance();
		Cal.setTime(date);
		Cal.add(java.util.Calendar.HOUR_OF_DAY, fix);
		result = new SimpleDateFormat(format).format(Cal.getTime());
		return result;
	}

	/**
	 * 获取跟当前时间相差几天的时间，可以跨月
	 * 
	 * @param date
	 * @param fix
	 * @return
	 */
	public static String getDateStrDay(String startStr, int fix, int type) {
		Date date = DateUtil.stringToDate(startStr);
		String format = "yyyy-MM-dd";
		if (type == 1) {
			format = "yyyy-MM-dd";
		} else if (type == 2) {
			format = "yyyy-MM-dd HH";
		} else if (type == 3) {
			format = "yyyy-MM-dd HH:mm";
		} else if (type == 4) {
			format = "yyyy-MM-dd HH:mm:ss";
		} else if (type == 5) {
			format = "yyyyMMdd";
		} else if (type == 6) {
			format = "yyyyMMddHH";
		} else if (type == 7) {
			format = "yyyyMMddHHmm";
		} else if (type == 8) {
			format = "yyyyMMddHHmmss";
		} else {
			format = "yyyyMMddHHmmsssss";
		}
		java.util.Calendar Cal = java.util.Calendar.getInstance();

		Cal.setTime(date);

		Cal.add(java.util.Calendar.DAY_OF_MONTH, fix);

		return new SimpleDateFormat(format).format(Cal.getTime());
	}

	/**
	 * 获得两个日期之前相差的天数
	 * 
	 * @param start
	 * @param e
	 * @return
	 */
	public static int getDaysInterval(String startStr, String endStr) {
		Date startDate = stringToDate(startStr);
		Date endDate = stringToDate(endStr);
		if (startDate.after(endDate)) {
			Date t = startDate;
			startDate = endDate;
			endDate = t;
		}
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(startDate);
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(endDate);
		int days = (int) ((endCalendar.getTimeInMillis() - startCalendar.getTimeInMillis()) / (3600L * 1000 * 24));
		return days;
	}

	/**
	 * 获得两个日期之间相差的 type 1:天、2:时、3:分、4秒
	 * 
	 * @param startStr
	 * @param endStr
	 * @return
	 */
	public static int getTimesInterval(String startStr, String endStr, int type) {
		Date startDate = stringToDate(startStr);
		Date endDate = stringToDate(endStr);
		if (startDate.after(endDate)) {
			Date t = startDate;
			startDate = endDate;
			endDate = t;
		}
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(startDate);
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(endDate);
		Long intervals = endCalendar.getTimeInMillis() - startCalendar.getTimeInMillis();
		int times = 0;
		if (type == 1) {
			times = (int) ((intervals) / (3600L * 1000 * 24));
		} else if (type == 2) {
			times = (int) ((intervals) / (3600L * 1000));
		} else if (type == 3) {
			times = (int) ((intervals) / (60L * 1000));
		} else if (type == 4) {
			times = (int) ((intervals) / (1L * 1000));
		}
		return times;
	}

	/**
	 * 获得两个日期之前相差的月份
	 * 
	 * @param start
	 * @param e
	 * @return
	 */
	public static int getMonthsInterval(String startStr, String endStr) {
		Date startDate = stringToDate(startStr);
		Date endDate = stringToDate(endStr);
		if (startDate.after(endDate)) {
			Date t = startDate;
			startDate = endDate;
			endDate = t;
		}
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(startDate);
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(endDate);
		Calendar temp = Calendar.getInstance();
		temp.setTime(endDate);
		temp.add(Calendar.DATE, 1);

		int year = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
		int month = endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);

		if ((startCalendar.get(Calendar.DATE) == 1) && (temp.get(Calendar.DATE) == 1)) {
			return year * 12 + month + 1;
		} else if ((startCalendar.get(Calendar.DATE) != 1) && (temp.get(Calendar.DATE) == 1)) {
			return year * 12 + month;
		} else if ((startCalendar.get(Calendar.DATE) == 1) && (temp.get(Calendar.DATE) != 1)) {
			return year * 12 + month;
		} else {
			return (year * 12 + month - 1) < 0 ? 0 : (year * 12 + month);
		}
	}

	/**
	 * 获得两个日期之前相差的月份
	 * 
	 * @param start
	 * @param e
	 * @return
	 */
	public static int getYearsInterval(String startStr, String endStr) {
		Date startDate = stringToDate(startStr);
		Date endDate = stringToDate(endStr);
		if (startDate.after(endDate)) {
			Date t = startDate;
			startDate = endDate;
			endDate = t;
		}
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(startDate);
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(endDate);
		Calendar temp = Calendar.getInstance();
		temp.setTime(endDate);
		temp.add(Calendar.DATE, 1);

		int year = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
		int month = endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
		if (month > 0) {
			year = year + 1;
		}

		return year;
	}

	/**
	 * 计算开始日期days天之后的日期
	 * 
	 * @param startTime
	 *            开始日期
	 * @param days
	 *            天数
	 * @param format
	 *            格式
	 * @author apple
	 * @date 2012-04-24
	 * @return
	 */
	public static String getAddDates(Timestamp startTime, int days, String format) {
		if (startTime != null) {
			Timestamp endTime = DateUtil.getDayStart(startTime, days);
			return DateUtil.toDateString(endTime, format);
		} else {
			return "";
		}
	}

	/**
	 * 计算开始日期days天之后的日期
	 * 
	 * @param startTime
	 *            开始日期
	 * @param days
	 *            天数
	 * @param format
	 *            格式
	 * @author apple
	 * @date 2012-04-24
	 * @return
	 */
	public static String getAddDates(String startDate, int days, String format) {
		if (startDate != null) {
			Timestamp startTime = DateUtil.toTimestamp(startDate);
			Timestamp endTime = DateUtil.getDayStart(startTime, days);
			return DateUtil.toDateString(endTime, format);
		} else {
			return "";
		}
	}

	/**
	 * 计算开始日期增加月数后的日期
	 * 
	 * @param startTime
	 *            开始日期
	 * @param month
	 *            月数
	 * @param format
	 *            返回格式
	 * @author apple
	 * @date 2012-04-24
	 * @return
	 */
	public static String getAddMonth(String startDate, int month, String format) {
		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat(format);
		if (startDate != null) {
			Calendar c = Calendar.getInstance();
			c.setTime(DateUtil.parseDate(startDate, "yyyy-MM-dd"));
			c.add(Calendar.MONTH, month);
			c.add(Calendar.DAY_OF_YEAR, -1);
			Date endDate = c.getTime();
			return df.format(endDate);
		} else {
			return "";
		}
	}

	/**
	 * 
	 * @param type
	 *            1:2010-09-02 22; 2:2010-09-22 22:23; 3:2010-09-22 22:23:59
	 * @return
	 */
	public static String getCurrentTime(int type) {
		String format = "yyyy-MM-dd";
		if (type == 1) {
			format = "yyyy-MM-dd HH";
		} else if (type == 2) {
			format = "yyyy-MM-dd HH:mm";
		} else if (type == 3) {
			format = "yyyy-MM-dd HH:mm:ss";
		} else if (type == 4) {
			format = "yyyyMMddHHmmss";
		} else if (type == 5) {
			format = "yyyyMMddHHmmsssss";
		}
		return toDateString(DateUtil.nowTimestamp(), format);
	}

	public static String formate(Date date, int type) {
		String format = "yyyy-MM-dd";
		if (type == 1) {
			format = "yyyy-MM-dd HH";
		} else if (type == 2) {
			format = "yyyy-MM-dd HH:mm";
		} else if (type == 3) {
			format = "yyyy-MM-dd HH:mm:ss";
		}
		return new SimpleDateFormat(format).format(date);
	}

	/**
	 * 比较两个日期的大小，如果date1>=date2，则返回true，反之返回false
	 * 
	 * @param date1
	 *            第一个日期
	 * @param date2
	 *            第二个日期
	 * @param type
	 *            日期格式
	 * @return
	 */
	public static boolean compareDate(String date1, String date2, int type) {
		boolean result = true;
		String format = "";
		switch (type) {
		case 1:
			format = "yyyy-MM-dd hh";
			break;
		case 2:
			format = "yyyy-MM-dd HH:mm";
			break;
		case 3:
			format = "yyyy-MM-dd HH:mm:ss";
			break;
		case 4:
			format = "yyyy-MM-dd";
		}
		DateFormat df = new SimpleDateFormat(format);
		try {
			Date dt1 = df.parse(date1);
			Date dt2 = df.parse(date2);
			if (dt1.getTime() < dt2.getTime()) {
				result = false;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 测试方法<br>
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		String dbpass=DigestUtils.sha256Hex("123456");
		System.out.println(dbpass);
		String startDate = "2008-05-07";
		String endDate = "2009-03-31";
		String nowdate = "2018-12-15";
		String date2 = "2018-12-14";
		// System.out.println(compareDate(nowdate,date2,4));
//		System.out.println(DateUtil.getAddDates("2018-12-16", Integer.parseInt("15"), "yyyy-MM-dd"));
		System.out.println(getDateStrDay("2019-03-29",-2,4));
//		System.out.println(getDateStrHour("2018121718",-48,4));
		System.out.println(System.currentTimeMillis()+""+(char)(Math.random()*26+'A'));
		
		double firstLevelRate=10.0d;
		double secondLevelRate=200.10d;
		Queue<Double> rateQueue = new LinkedList<>();
		System.out.println(rateQueue.isEmpty());
		rateQueue.offer(firstLevelRate); //进队
		rateQueue.offer(secondLevelRate);//进队列
		System.out.println(JsonUtil.getJsonByObj(rateQueue.toArray()));
		System.out.println(rateQueue.peek());
		System.out.println(JsonUtil.getJsonByObj(rateQueue.toArray()));
		System.out.println(rateQueue.poll());
		System.out.println("----"+rateQueue.isEmpty());
		double temp=300.10d;
		System.out.println(JsonUtil.getJsonByObj(rateQueue.toArray()));
		System.out.println(rateQueue.add(temp));
		System.out.println(JsonUtil.getJsonByObj(rateQueue.toArray()));
		System.out.println(rateQueue.poll());
		System.out.println(JsonUtil.getJsonByObj(rateQueue.toArray()));
		System.out.println(rateQueue.peek());
		System.out.println(JsonUtil.getJsonByObj(rateQueue.toArray()));
		System.out.println(rateQueue.poll());
		System.out.println(rateQueue.isEmpty());
		System.out.println(rateQueue.poll());
		System.out.println(rateQueue.isEmpty());
		System.out.println(rateQueue.poll());
		System.out.println(rateQueue.isEmpty());
		
	}

}
