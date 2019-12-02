package com.platform.utils;

import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

/**
 * 
 * #func 日期格式化utils #desc 在此添加实现相关说明
 * 
 * @author hedan
 * @version 4.0
 * 
 */
public class DateUtils {

	public static final int SECOND = 1;
	public static final int MINUTE = 2;
	public static final int HOUR = 3;
	public static final int DAY = 4;

	// 日志
    private static final Logger logger = Logger.getLogger(DateUtils.class);

    /**
     * 时间格式(yyyy-MM-dd)
     */
    public final static String DATE_PATTERN = "yyyy-MM-dd";
    /**
     * 无分隔符日期格式 "yyyyMMddHHmmssSSS"
     */
    public static String DATE_TIME_PATTERN_YYYY_MM_DD_HH_MM_SS_SSS = "yyyyMMddHHmmssSSS";
    /**
     * 时间格式(yyyy-MM-dd HH:mm:ss)
     */
    public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    // 日期转换格式数组
    public static String[][] regularExp = new String[][]{

            // 默认格式
            {"\\d{4}-((([0][1,3-9]|[1][0-2]|[1-9])-([0-2]\\d|[3][0,1]|[1-9]))|((02|2)-(([1-9])|[0-2]\\d)))\\s+([0,1]\\d|[2][0-3]|\\d):([0-5]\\d|\\d):([0-5]\\d|\\d)",
                    DATE_TIME_PATTERN},
            // 仅日期格式 年月日
            {"\\d{4}-((([0][1,3-9]|[1][0-2]|[1-9])-([0-2]\\d|[3][0,1]|[1-9]))|((02|2)-(([1-9])|[0-2]\\d)))",
                    DATE_PATTERN},
            //  带毫秒格式
            {"\\d{4}((([0][1,3-9]|[1][0-2]|[1-9])([0-2]\\d|[3][0,1]|[1-9]))|((02|2)(([1-9])|[0-2]\\d)))([0,1]\\d|[2][0-3])([0-5]\\d|\\d)([0-5]\\d|\\d)\\d{1,3}",
                    DATE_TIME_PATTERN_YYYY_MM_DD_HH_MM_SS_SSS}
    };

    public static String timeToStr(Long time, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        if (time.toString().length() < 13) {
            time = time * 1000L;
        }
        Date date = new Date(time);
        String value = dateFormat.format(date);
        return value;
    }

    public static long strToTime(String timeStr) {
        Date time = strToDate(timeStr);
        return time.getTime() / 1000;
    }
	public static String getTimeStamp() {
		return String.valueOf(System.currentTimeMillis() / 1000);
	}

    /**
     * 转换为时间类型格式
     *
     * @param strDate 日期
     * @return
     */
    public static Date strToDate(String strDate) {
        try {
            String strType = getDateFormat(strDate);
            SimpleDateFormat sf = new SimpleDateFormat(strType);
            return new Date((sf.parse(strDate).getTime()));
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 根据传入的日期格式字符串，获取日期的格式
     *
     * @return 秒
     */
    public static String getDateFormat(String date_str) {
        String style = null;
        if (StringUtils.isEmpty(date_str)) {
            return null;
        }
        boolean b = false;
        for (int i = 0; i < regularExp.length; i++) {
            b = date_str.matches(regularExp[i][0]);
            if (b) {
                style = regularExp[i][1];
            }
        }
        if (StringUtils.isEmpty(style)) {
            logger.info("date_str:" + date_str);
            logger.info("日期格式获取出错，未识别的日期格式");
        }
        return style;
    }

    /**
     * 将字符串类型的转换成Date类型
     *
     * @param dateStr
     *            字符串类型的日期 yyyy-MM-dd
     * @return Date类型的日期
     * @throws ParseException
     */
    public static Date convertStringToDate(String dateStr) {
        // 返回的日期
        Date resultDate = null;
        try {
            // 日期格式转换
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            resultDate = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return resultDate;
    }

    public static int getAgeByBirth(Date birthDay) throws ParseException {
        int age = 0;
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthDay)) { //出生日期晚于当前时间，无法计算
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);  //当前年份
        int monthNow = cal.get(Calendar.MONTH);  //当前月份
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH); //当前日期
        cal.setTime(birthDay);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
        age = yearNow - yearBirth;   //计算整岁数
        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) age--;//当前日期在生日之前，年龄减一
            } else {
                age--;//当前月份在生日之前，年龄减一
            }
        }
        return age;
    }
    
    public static int getAgeByBirthYear(Date birthDay){
    	int age = 0;
    	Calendar cal = Calendar.getInstance();
        if (cal.before(birthDay)) { //出生日期晚于当前时间，无法计算
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);  //当前年份
        cal.setTime(birthDay);
        int yearBirth = cal.get(Calendar.YEAR);
        age = yearNow - yearBirth+1; 
    	return age;
    }
	/**
	 * 
	 * #func 判断指定日期的格式是否合法<br>
	 * #desc 在此添加实现相关说明
	 * 
	 * @author hedan
	 * @version 4.0
	 */
	public static boolean isValid(String date, String pattern) {
		Date d = parseDate(date, pattern);
		return d != null && format(d, pattern).equals(date);
	}

	/**
	 * 
	 * #func 格式化日期<br>
	 * #desc 在此添加实现相关说明
	 * 
	 * @author hedan
	 * @version 4.0
	 */
	public static String format(Date date, String pattern) {
		return DateFormatUtils.format(date, pattern);
	}

	/**
	 * 
	 * #func 格式化日期<br>
	 * #desc 使用yyyy-MM-dd作为样式
	 * 
	 * @author hedan
	 * @version VERSION
	 */
	public static String format(Date date) {
		return DateFormatUtils.format(date, "yyyy-MM-dd");
	}
	public static String formatYYYYMMDD(Date date) {
		return DateFormatUtils.format(date, "yyyyMMdd");
	}
	public static String formatYY(Date date) {
		return DateFormatUtils.format(date, "yyyy");
	}

	/**
	 * 
	 * #func 格式化日期<br>
	 * #desc 使用yyyy-MM-dd HH:mm作为样式
	 * 
	 * @author hedan
	 * @version VERSION
	 */
	public static String formatYYMMDDHHMM(Date date) {
		return DateFormatUtils.format(date, "yyyy-MM-dd HH:mm");
	}

	public static String formatYYMMDDHHMMSS(Date date) {
		return DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 
	 * #func 格式化日期<br>
	 * #desc 使用yyyyMMddHHmmssss作为样式
	 * 
	 * @version VERSION
	 */
	public static String format2(Date date) {
		return DateFormatUtils.format(date, "yyMMddHHmmssss");
	}

	/**
	 * 
	 * #func 格式化日期<br>
	 * #desc 使用MMddHHmmssss作为样式
	 * 
	 * @version VERSION
	 */
	public static String format3(Date date) {
		return DateFormatUtils.format(date, "MMddHHmmssss");
	}

	/**
	 * 
	 * #func 格式化日期<br>
	 * #desc 使用MMddHHmm作为样式
	 * 
	 * @version VERSION
	 */
	public static String format4(Date date) {
		return DateFormatUtils.format(date, "MMddHHmm");
	}

	/**
	 * 
	 * #func 格式化日期<br>
	 * #desc 使用yyyyMMddHHmm作为样式
	 * 
	 * @version VERSION
	 */
	public static String format5(Date date) {
		return DateFormatUtils.format(date, "yyyyMMddHHmm");
	}

	/**
	 * 
	 * #func 把字符串转化为日期<br>
	 * #desc 在此添加实现相关说明
	 * 
	 * @author hedan
	 * @version 4.0
	 */
	public static Date parseDate(String date, String pattern) {
		try {
			return org.apache.commons.lang.time.DateUtils.parseDate(date, new String[] { pattern });
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 
	 * @param date
	 * @param pattern
	 *            yyyy-u周
	 * @return
	 */
	public static Date parseWeekDate(String date, String pattern) {
		try {
			Date tempDate = org.apache.commons.lang.time.DateUtils.parseDate(date, new String[] { pattern });
			tempDate = addDays(tempDate, 1);
			return tempDate;
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 
	 * #func 把字符串转化为日期<br>
	 * #desc 使用yyyy-MM-dd作为样式
	 * 
	 * @author hedan
	 * @version 4.0
	 */
	public static Date parseDate(String date) {
		if (date == null) {
			return null;
		}
		try {
			return org.apache.commons.lang.time.DateUtils.parseDate(date, new String[] { "yyyy-MM-dd" });
		} catch (ParseException e) {
			return null;
		}
	}

	public static Date parseDateYYMM(String date) {
		if (date == null) {
			return null;
		}
		try {
			return org.apache.commons.lang.time.DateUtils.parseDate(date, new String[] { "yyyy-MM" });
		} catch (ParseException e) {
			return null;
		}
	}

	public static Date parseDateYYMMddHHmm(String date) {
		if (date == null) {
			return null;
		}
		try {
			return org.apache.commons.lang.time.DateUtils.parseDate(date, new String[] { "yyyy-MM-dd hh:mm" });
		} catch (ParseException e) {
			try {
				return org.apache.commons.lang.time.DateUtils.parseDate(date, new String[] { "yyyy-MM-dd" });
			} catch (ParseException e1) {
				return null;
			}
		}
	}

	/**
	 * 
	 * #func 计算相对年<br>
	 * #desc amount可以为负数
	 * 
	 * @author hedan
	 * @version 4.0
	 */
	public static Date addYears(Date date, int amount) {
		return org.apache.commons.lang.time.DateUtils.addYears(date, amount);
	}

	/**
	 * 
	 * #func 计算相对月<br>
	 * #desc amount可以为负数
	 * 
	 * @author hedan
	 * @version 4.0
	 */
	public static Date addMonths(Date date, int amount) {
		return org.apache.commons.lang.time.DateUtils.addMonths(date, amount);
	}

	/**
	 * 
	 * #func 计算相对日<br>
	 * #desc amount可以为负数
	 * 
	 * @author hedan
	 * @version 4.0
	 */
	public static Date addDays(Date date, int amount) {
		return org.apache.commons.lang.time.DateUtils.addDays(date, amount);
	}

	/**
	 * 
	 * #func 计算相对星期<br>
	 * #desc 在此添加实现相关说明
	 * 
	 * @author hedan
	 * @version 4.0
	 */
	public static Date addWeeks(Date date, int amount) {
		return org.apache.commons.lang.time.DateUtils.addWeeks(date, amount);
	}

	/**
	 * 
	 * #func 计算时间跨度<br>
	 * #desc 计算方式：结束时间减开始时间
	 * 
	 * @author hedan
	 * @version 4.0
	 */
	public static long getTimeSpan(Date begin, Date end, int type) {
		long diff = end.getTime() - begin.getTime();
		switch (type) {
		case DAY:
		default:
			return diff / org.apache.commons.lang.time.DateUtils.MILLIS_PER_DAY;
		case HOUR:
			return diff / org.apache.commons.lang.time.DateUtils.MILLIS_PER_HOUR;
		case MINUTE:
			return diff / org.apache.commons.lang.time.DateUtils.MILLIS_PER_MINUTE;
		case SECOND:
			return diff / org.apache.commons.lang.time.DateUtils.MILLIS_PER_SECOND;
		}
	}

	/**
	 * 
	 * #func 获得日期差<br>
	 * #desc 不足一天的忽略
	 * 
	 * @author hedan
	 * @version 4.0
	 */
	public static long getDaySpan(Date begin, Date end) {
		return getTimeSpan(begin, end, DAY);
	}

	/**
	 * 
	 * #func 获得小时差<br>
	 * #desc 不足一小时的忽略
	 * 
	 * @author hedan
	 * @version 4.0
	 */
	public static long getHourSpan(Date begin, Date end) {
		return getTimeSpan(begin, end, HOUR);
	}

	/**
	 * 
	 * #func 获得分钟差<br>
	 * #desc 不足一分钟的忽略
	 * 
	 * @author hedan
	 * @version 4.0
	 */
	public static long getMinuteSpan(Date begin, Date end) {
		return getTimeSpan(begin, end, MINUTE);
	}

	/**
	 * 
	 * #func 获得秒差<br>
	 * #desc 不足一秒的忽略
	 * 
	 * @author hedan
	 * @version 4.0
	 */
	public static long getSecondSpan(Date begin, Date end) {
		return getTimeSpan(begin, end, SECOND);
	}

	/**
	 * 
	 * #func 获取月份差<br>
	 * 
	 * @author dongguoshuang
	 */
	public static int getMonthSpan(Date begin, Date end) {
		Calendar beginCal = new GregorianCalendar();
		beginCal.setTime(begin);
		Calendar endCal = new GregorianCalendar();
		endCal.setTime(end);
		int m = (endCal.get(Calendar.MONTH)) - (beginCal.get(Calendar.MONTH));
		int y = (endCal.get(Calendar.YEAR)) - (beginCal.get(Calendar.YEAR));
		return y * 12 + m;
	}

	/**
	 * 
	 * #func 获取当前时间当月的第一天<br>
	 * 
	 * @author dongguoshuang
	 */
	// public static Date getFirstDayOfMonth(Date date) {
	// Calendar calendar = Calendar.getInstance();
	// calendar.setTime(date);
	// int firstDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
	// return org.apache.commons.lang.time.DateUtils.setDays(date, firstDay);
	// }

	/**
	 * 
	 * #func 获取当前时间当月的最后一天<br>
	 * 
	 * @author dulin
	 */
	// public static Date getLastDayOfMonth(Date date) {
	// Calendar calendar = Calendar.getInstance();
	// calendar.setTime(date);
	// int firstDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	// return org.apache.commons.lang.time.DateUtils.setDays(date, firstDay);
	// }

	/**
	 * 
	 * #func 获取当前时间本周的第一天<br>
	 * 
	 * @author dulin
	 */
	public static Date getFirstDayOfWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return getADayOfWeek(calendar, Calendar.MONDAY).getTime();
	}

	/**
	 * 周的最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getLastDayOfWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		return getADayOfWeek(calendar, Calendar.SUNDAY).getTime();
	}

	private static Calendar getADayOfWeek(Calendar day, int dayOfWeek) {
		int week = day.get(Calendar.DAY_OF_WEEK);// 当前周几

		if (week == dayOfWeek)
			return day;
		int diffDay = dayOfWeek - week;
		if (week == Calendar.SUNDAY) {
			diffDay -= 7;
		} else if (dayOfWeek == Calendar.SUNDAY) {
			diffDay += 7;
		}
		day.add(Calendar.DATE, diffDay);
		return day;
	}

	/**
	 * 
	 * #func 获取时间的完整格式<br>
	 * #desc addDays为计算相对时间，可为负数
	 * 
	 * @author dulin
	 * @version 4.0.14
	 * 
	 */
	public static String parseFullFormat(String dateStr, String pattern, int addDays) {
		try {
			Date date = parseDate(dateStr, pattern);
			if (addDays != 0) {
				date = addDays(date, addDays);
			}
			return DateUtils.format(date, "yyyy-MM-dd HH:mm:ss");
		} catch (Exception e) {
			return null;
		}
	}


	/**
	 * 返回指定字符串表示的日期，时，分，秒为0
	 * 
	 * @author zengyunfeng
	 * @version 1.1.0
	 * @param date
	 *            ： 格式为yyyy-MM-dd
	 * @return
	 * @throws ParseException
	 */
	public static Date str_ToDate(String date) throws ParseException {

		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");

		java.util.Date d = sdf.parse(date);

		return d;
	}

	public static Date str_ToDate(String date, String formatStr) throws ParseException {

		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(formatStr);

		java.util.Date d = sdf.parse(date);

		return d;
	}


	/**
	 * 生成当前时间对应的包含小时的时间字符串：yyyyMMddHH
	 * 
	 * @return String 时间字符串
	 */
	public static String getHourStr() {
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMddHH");
		return sdf.format(new Date());
	}

	/**
	 * 返回指定格式的时间字符串：yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 * @return
	 * @throws ParseException下午01:12:32
	 */
	public static String getDateStr(Date date) {
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	/**
	 * 生成java.util.Date类型的对象
	 * 
	 * @param year
	 *            int 年
	 * @param month
	 *            int 月
	 * @param day
	 *            int 日
	 * @return Date java.util.Date类型的对象
	 */
	public static Date getDate(int year, int month, int day) {
		GregorianCalendar d = new GregorianCalendar(year, month - 1, day);
		return d.getTime();
	}

	public static Date getDate(int yyyyMMdd) {
		int dd = yyyyMMdd % 100;
		int yyyyMM = yyyyMMdd / 100;
		int mm = yyyyMM % 100;
		int yyyy = yyyyMM / 100;
		GregorianCalendar d = new GregorianCalendar(yyyy, mm - 1, dd);
		return d.getTime();
	}

	/**
	 * 生成java.util.Date类型的对象
	 * 
	 * @param year
	 *            int 年
	 * @param month
	 *            int 月
	 * @param day
	 *            int 日
	 * @param hour
	 *            int 小时
	 * @return Date java.util.Date对象
	 */
	public static Date getDate(int year, int month, int day, int hour) {
		GregorianCalendar d = new GregorianCalendar(year, month - 1, day, hour, 0);
		return d.getTime();
	}

	/**
	 * 生成圆整至小时的当前时间 例如：若当前时间为（2004-08-01 11:30:58），将获得（2004-08-01 11:00:00）的日期对象
	 * 
	 * @return Date java.util.Date对象
	 */
	public static Date getRoundedHourCurDate() {

		Calendar cal = GregorianCalendar.getInstance();

		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);

		return cal.getTime();

	}

	/**
	 * 生成当天零时的日期对象 例如：若当前时间为（2004-08-01 11:30:58），将获得（2004-08-01 00:00:00）的日期对象
	 * 
	 * @return Date java.util.Date对象
	 */
	public static Date getRoundedDayCurDate() {
		Calendar cal = new GregorianCalendar();

		return new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
				.getTime();
	}

	/**
	 * 生成某天零时的日期对象 例如：若输入时间为（2004-08-01 11:30:58），将获得（2004-08-01 00:00:00）的日期对象
	 * 
	 * @return Date java.util.Date对象
	 */
	public static Date getRoundedDay(Date dt) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(dt);
		return new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
				.getTime();
	}

	/**
	 * 生成圆整至小时的当前时间 例如：若给定时间为（2004-08-01 11:30:58），将获得（2004-08-01 11:00:00）的日期对象
	 * 
	 * @param dt
	 *            Date java.util.Date对象
	 * @return Date java.util.Date对象
	 */
	public static Date getRoundedHourDate(Date dt) {

		Calendar cal = new GregorianCalendar();

		cal.setTime(dt);

		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);

		return cal.getTime();
	}

	/**
	 * 获得给定时间的第二天零时的日期对象 例如：若给定时间为（2004-08-01 11:30:58），将获得（2004-08-02
	 * 00:00:00）的日期对象 若给定时间为（2004-08-31 11:30:58），将获得（2004-09-01 00:00:00）的日期对象
	 * 
	 * @param dt
	 *            Date 给定的java.util.Date对象
	 * @return Date java.util.Date对象
	 */

	public static Date getNextDay(Date dt) {

		if (dt == null)
			return dt;

		Calendar cal = new GregorianCalendar();
		cal.setTime(dt);

		// return new GregorianCalendar(cal.get(Calendar.YEAR), cal
		// .get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + 1) //这个方法会出现
		// 32 号的情况
		// .getTime();
		//

		// modified by zhuqian 2009-03-25
		cal.add(Calendar.DAY_OF_YEAR, 1);
		return cal.getTime();

	}

	// added by zhuqian 2009-03-25
	public static Date getPreviousDay(Date dt) {
		if (dt == null)
			return dt;

		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		cal.add(Calendar.DAY_OF_YEAR, -1);
		return cal.getTime();
	}

	/**
	 * @param dt
	 *            Date 给定的java.util.Date对象
	 * @param weekDay
	 *            int 就是周几的”几“，周日是7
	 * @return Date java.util.Date对象
	 */
	public static Date getWeekDay(Date dt, int weekDay) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(dt);
		if (weekDay == 7)
			weekDay = 1;
		else
			weekDay++;
		cal.set(GregorianCalendar.DAY_OF_WEEK, weekDay);
		return cal.getTime();
	}

	public static int getWeekSumOfYearByDay(Date dt) {
		Calendar c = Calendar.getInstance();
		c.setTime(dt);
		int i = c.get(Calendar.WEEK_OF_YEAR);
		return i;
	}

	/**
	 * 获得给定时间的第N天零时的日期对象 例如：若给定时间为（2004-08-01 11:30:58），将获得（2004-08-02
	 * 00:00:00）的日期对象 若给定时间为（2004-08-31 11:30:58），将获得（2004-09-01 00:00:00）的日期对象
	 * 
	 * @param dt
	 *            Date 给定的java.util.Date对象
	 * @return Date java.util.Date对象
	 */
	public static Date getNextDay(Date dt, Long n) {

		Calendar cal = new GregorianCalendar();
		cal.setTime(dt);

		return new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.get(Calendar.DAY_OF_MONTH) + n.intValue()).getTime();

	}

	/**
	 * 如果当天在后续这个月不存在这天，则返回期望这个月的最后一天 20081231 -1 返回20081130
	 * 
	 * @param dt
	 * @param n
	 * @return上午11:16:22
	 */
	public static Date getNextMonth(Date dt, Long n) {

		Calendar cal = new GregorianCalendar();
		cal.setTime(dt);

		Calendar firstCal = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + n.intValue(), 1);
		if (firstCal.getActualMaximum(Calendar.DAY_OF_MONTH) < cal.get(Calendar.DAY_OF_MONTH)) {
			return new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + n.intValue(),
					firstCal.getActualMaximum(Calendar.DAY_OF_MONTH)).getTime();
		} else {
			return new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + n.intValue(),
					cal.get(Calendar.DAY_OF_MONTH)).getTime();
		}

	}

	/**
	 * 如果当天在后续这个月不存在这天，则返回期望这个月后一个月的第一天 20081231 - 1 返回20081201 20080831 + 1
	 * 返回20080930
	 * 
	 * @param dt
	 * @param n
	 * @return上午11:22:39
	 */
	public static Date getNextMonthExtention(Date dt, Long n) {

		Calendar cal = new GregorianCalendar();
		cal.setTime(dt);

		Calendar firstCal = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + n.intValue(), 1);
		if (firstCal.getActualMaximum(Calendar.DAY_OF_MONTH) < cal.get(Calendar.DAY_OF_MONTH)) {
			return new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + n.intValue() + 1, 1)
					.getTime();

		} else {
			return new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + n.intValue(),
					cal.get(Calendar.DAY_OF_MONTH)).getTime();
		}
	}

	/**
	 * 获取当前时间的前后间隔小时
	 * 
	 * @param startDate
	 * @param hour
	 * @return
	 */
	public static Date getDateBetweenHour(Date startDate, int hour) {
		long startDateTime = startDate.getTime();
		long hourTime = 60 * 60 * 1000 * hour;
		long time = startDateTime + hourTime;
		Calendar validCal = new GregorianCalendar();
		validCal.setTimeInMillis(time);
		return validCal.getTime();
	}

	public static long getBetweenDate(Date startDate, Date endDate) {
		long startDateTime = startDate.getTime();
		long endDateTime = endDate.getTime();
		long dayTime = 24 * 60 * 60 * 1000;
		long days = (endDateTime - startDateTime) / dayTime;
		return days;
	}

	public static long getBetweenDay(Date startDate, Date endDate) {
		long startDateTime = startDate.getTime();
		long endDateTime = endDate.getTime();

		long dayTime = 24 * 60 * 60 * 1000;
		long days = (endDateTime - startDateTime) / dayTime;
		return days;
	}

	public static long getBetween(Date startDate, Date endDate) {
		long startDateTime = startDate.getTime();
		long endDateTime = endDate.getTime();
		long days = (endDateTime - startDateTime);
		return days;
	}

	public static long getMonthLength(String countDate) throws ParseException {
		String firstDay = countDate.substring(0, countDate.length() - 2) + "01";
		Date startDate = strToDate(firstDay);
		Date endDate = getNextMonth(startDate, new Long(1));
		long startDateTime = startDate.getTime();
		long endDateTime = endDate.getTime();
		long dayTime = 24 * 60 * 60 * 1000;
		long days = (endDateTime - startDateTime) / dayTime;
		return days;
	}

	/**
	 * 获得当前时间的第二天零时的日期对象 例如：若当前时间为（2004-08-01 11:30:58），将获得（2004-08-02
	 * 00:00:00）的日期对象 若当前时间为（2004-08-31 11:30:58），将获得（2004-09-01 00:00:00）的日期对象
	 * 
	 * @return Date java.util.Date对象
	 */
	public static Date getNextDay() {

		Calendar cal = GregorianCalendar.getInstance();
		return new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.get(Calendar.DAY_OF_MONTH) + 1).getTime();

	}

	/**
	 * 获得当前时间的第二天零时的日期对象 例如：若当前时间为（2004-08-01 11:30:58），将获得（2004-08-02
	 * 00:00:00）的日期对象 若当前时间为（2004-08-31 11:30:58），将获得（2004-09-01 00:00:00）的日期对象
	 * 
	 * @return Date java.util.Date对象
	 */
	public static Date getTodayDay() {
		Calendar cal = GregorianCalendar.getInstance();
		return new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
				.getTime();

	}

	/**
	 * 将java.util.Date类型的对象转换为java.sql.Timestamp类型的对象
	 * 
	 * @param dt
	 *            Date
	 * @return Timestamp
	 */
	public static java.sql.Timestamp convertSqlDate(Date dt) {
		if (dt == null) {
			return new java.sql.Timestamp(0);
		}
		return new java.sql.Timestamp(dt.getTime());
	}

	/**
	 * 格式化当前时间，返回如："yyyyMMdd"形式的字符串
	 * 
	 * @return String
	 */
	public static String formatCurrrentDate() {
		java.util.Date pdate = new Date();
		return formatDate(pdate, "yyyyMMdd");
	}

	/**
	 * 格式化当前时间，返回如："yyMMdd"形式的字符串
	 * 
	 * @return
	 */
	public static String formatTodayDate() {
		java.util.Date pdate = new Date();
		return formatDate(pdate, "yyMMdd");
	}
	/**
	 * 格式化当前时间，返回如："yyMMddHHMMSS"形式的字符串
	 * 
	 * @return
	 */
	public static String formatTodayTime() {
		java.util.Date pdate = new Date();
		return formatDate(pdate, "yyMMddHHmmssSSS");
	}

	/**
	 * 格式化当前时间，返回如："yyMMdd"形式的字符串
	 * 
	 * @return
	 */
	public static String formatCurrrentDates() {
		java.util.Date pdate = new Date();
		return formatDate(pdate, "yyyy-MM-dd");
	}

	/**
	 * 按照给定格式返回代表日期的字符串
	 * 
	 * @param pDate
	 *            Date
	 * @param format
	 *            String 日期格式
	 * @return String 代表日期的字符串
	 */
	public static String formatDate(java.util.Date pDate, String format) {

		if (pDate == null) {
			pDate = new java.util.Date();
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(pDate);
	}

	/**
	 * 返回给定时间的小时数 例如：时间（2004-08-01 3:12:23）将返回 03 时间（2004-08-01 19:12:23）将返回19
	 * 
	 * @param pDate
	 *            Date 给定时间
	 * @return String 代表小时数的字符串
	 */
	public static String getHour(Date pDate) {
		return formatDate(pDate, "HH");
	}

	/**
	 * 获得上一个月的最后一天
	 * 
	 * @return
	 */
	public static Calendar getTheLastDayOfTheMonth(int year, int month) {
		Calendar cal = new GregorianCalendar();
		cal.set(year, month, 1);
		return new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.get(Calendar.DAY_OF_MONTH) - 1);

	}

	public static boolean isFirstDayOfMonth(Date date) {
		if (date == null)
			return false;

		// 如果date减一天后变成另一个月，便是该月的第一天

		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int thisMonth = c.get(Calendar.MONTH);
		c.add(Calendar.DATE, -1);

		return thisMonth != c.get(Calendar.MONTH);
	}

	/**
	 * 取得指定日期所在周的前一周的第一天
	 * 
	 * @param date
	 * @return
	 * @author yang_yun
	 */
	public static Date getFirstDayOfLastWeek(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(date.getTime() - 604800000L);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		Date lastWeekDate = c.getTime();

		c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(new Date(lastWeekDate.getTime()));
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
		return c.getTime();
	}

	/**
	 * 验证字符串是不是合法的日期；严格判断日期格式YYYYMMDD的正则表达式：包括闰年的判断、大月小月的判断
	 * 
	 * @param dateString
	 *            待验证的日期字符串
	 * @return 满足则返回true，不满足则返回false
	 * @author zhangpeng mrd3.4.0
	 */
	public static boolean validateDateString(String dateString) {

		if (dateString == null || dateString.equals("")) {
			return false;
		}

		// 日期格式YYYYMMDD的正则表达式,世纪年为闰年，如2000
		String regDate = "^(((([02468][048])|([13579][26]))[0]{2})(02)(([0][1-9])|([1-2][0-9])))" +
				// 世纪年不为闰年如2100
				"|(((([02468][1235679])|([13579][01345789]))[0]{2})(02)(([0][1-9])|([1][0-9])|([2][0-8])))" +
				// 非世纪年为闰年，如1996
				"|(([0-9]{2}(([0][48])|([2468][048])|([13579][26])))(02)(([0][1-9])|([1-2][0-9])))" +
				// 非世纪年不为闰年，如1997
				"|(([0-9]{2}(([02468][1235679])|([13579][01345789])))(02)(([0][1-9])|([1][0-9])|([2][0-8])))" +
				// 大月，有31天
				"|(([0-9]{4})(([0]{1}(1|3|5|7|8))|10|12)(([0][1-9])|([1-2][0-9])|30|31))" +
				// 小月，只有30天
				"|(([0-9]{4})(([0]{1}(4|6|9))|11)(([0][1-9])|([1-2][0-9])|30))$";

		return dateString.matches(regDate);
	}

	/**
	 * 获取指定日期的0时 如输入2008-11-13 16:00，则输出2008-11-13 00:00
	 */
	public static Calendar getDateCeil(Date date) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		return new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
	}

	/**
	 * 获取指定日期的23时 如输入2008-11-13 16:00，则输出2008-11-13 23:00
	 */
	public static Calendar getDateFloor(Date date) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		return new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH),
				23, 0);
	}

	/**
	 * 获取今日0时
	 */
	public static Calendar getCurDateCeil() {
		Calendar cal = new GregorianCalendar();
		return new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
	}

	/**
	 * 获取今日23时
	 */
	public static Calendar getCurDateFloor() {
		Calendar cal = new GregorianCalendar();
		return new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH),
				23, 0);
	}

	public static Date getYearFirstDayOfDate(Date date) {
		if (date == null)
			return date;
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(date.getTime());
		c.set(Calendar.MONTH, 0);
		c.set(Calendar.DATE, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}

	public static Date getYearLastDayOfDate(Date date) {
		if (date == null)
			return date;

		Calendar cal = Calendar.getInstance();

		cal.setTimeInMillis(date.getTime());
		cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + 1);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DATE, 1);
		Calendar calendar = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.get(Calendar.DAY_OF_MONTH) - 1);

		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		return calendar.getTime();
	}

	/**
	 * 获取月的第一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getMonthFirstDayOfDate(Date date) {
		if (date == null)
			return date;
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(date.getTime());
		c.set(Calendar.DATE, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}

	/**
	 * 获取月的最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getMonthLastDayOfDate(Date date) {
		if (date == null)
			return date;

		Calendar cal = Calendar.getInstance();

		cal.setTimeInMillis(date.getTime());
		cal.set(Calendar.DATE, 1);

		Calendar calendar = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1,
				cal.get(Calendar.DAY_OF_MONTH) - 1);

		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		return calendar.getTime();
	}

	public static Date getStartOfDate(Date date) {
		if (date == null)
			return date;
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(date.getTime());
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}

	public static Date getEndOfDate(Date date) {
		if (date == null)
			return date;
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(date.getTime());
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);

		return c.getTime();
	}

	/**
	 * 获取服务器时间的毫秒数
	 * 
	 * @author zhangpingan
	 * @param 无
	 * @return 返回服务器时间的毫秒数
	 * @author zhangpingan mrd Beidou2.0.0
	 */
	public static long getTimeInMillionSeconds() {
		Calendar c = Calendar.getInstance();
		return c.getTimeInMillis();
	}

	/**
	 * @author zhangpingan
	 * @param dateString
	 * @param format1，如yyyyMMdd
	 * @param format2,
	 *            如yyyy/MM/dd
	 * @return
	 */
	public static String formatDateString(String dateString, String format1, String format2) {
		if (dateString == null) {
			return null;
		}
		java.text.SimpleDateFormat beforeFormat = new java.text.SimpleDateFormat(format1);
		java.text.SimpleDateFormat endFormat = new java.text.SimpleDateFormat(format2);
		try {
			return endFormat.format(beforeFormat.parse(dateString));
		} catch (ParseException e) {
			e.printStackTrace();
			return dateString;
		}
	}

	/**
	 * 解析从doris获取的日期
	 * 
	 * @param doirsDate
	 * @return
	 * @author kanghongwei
	 */
	public static String formatDorisReturnDate(Object doirsDate, Format format) {
		if (doirsDate == null || (!(doirsDate instanceof Date))) {
			return null;
		}
		Date date = (Date) doirsDate;
		Format localFormat = (SimpleDateFormat) format;
		return localFormat.format(date);
	}

	/**
	 * 获取两天之间的所有的日期
	 * 
	 * @param startDate
	 * @param endDate
	 * @param format
	 * @return
	 * @author kanghongwei
	 */
	public static List<String> getDayListBetween2Day(String startDate, String endDate, Format format) {
		List<String> dayList = new ArrayList<String>();
		int dataNum = Integer.parseInt(getTwoDay2String(startDate, endDate, format));
		for (int i = 0; i < (dataNum + 1); i++) {
			String resultDate = getNextDay2String(startDate, String.valueOf(i), format);
			dayList.add(resultDate);
		}
		return dayList;
	}

	/**
	 * 指定fotmat,得到二个日期间的间隔天数
	 * 
	 * @param startDate
	 * @param endDate
	 * @param format
	 * @return
	 * @author kanghongwei
	 */
	public static String getTwoDay2String(String startDate, String endDate, Format format) {
		SimpleDateFormat localFormat = (SimpleDateFormat) format;
		long day = 0;
		try {
			java.util.Date startDay = localFormat.parse(startDate);
			java.util.Date endDay = localFormat.parse(endDate);
			day = (endDay.getTime() - startDay.getTime()) / (24 * 60 * 60 * 1000);
		} catch (Exception e) {
			return "";
		}
		return day + "";
	}

	/**
	 * 将当前时间前移或后移若干天之后的日期(前移则将delay赋负值)
	 * 
	 * @param nowdate
	 * @param delay
	 * @param format
	 * @return
	 * @author kanghongwei
	 */
	public static String getNextDay2String(String nowdate, String delay, Format format) {
		try {
			SimpleDateFormat localFormat = (SimpleDateFormat) format;
			String mdate = "";
			Date date = strToDateWithFormat(nowdate, localFormat);
			long myTime = (date.getTime() / 1000) + Integer.parseInt(delay) * 24 * 60 * 60;
			date.setTime(myTime * 1000);
			mdate = localFormat.format(date);
			return mdate;
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 指定fotmat, 将String转化为util.Date
	 * 
	 * @param strDate
	 * @param format
	 * @return
	 * @author kanghongwei
	 */
	public static Date strToDateWithFormat(String strDate, Format format) {
		SimpleDateFormat localFormat = (SimpleDateFormat) format;
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = localFormat.parse(strDate, pos);
		return strtodate;
	}

	/**
	 * 判定一个日期specificDate是否在指定的起止日期startDate，endDate之间
	 * 
	 * @param startDate
	 * @param endDate
	 * @param format
	 * @param specificDate
	 * @return
	 * @author kanghongwei
	 */
	public static boolean isBetweenDate(String startDate, String endDate, Format format, String specificDate) {
		boolean result = false;
		List<String> dateList = getDayListBetween2Day(startDate, endDate, format);
		for (String date : dateList) {
			if (date.equalsIgnoreCase(specificDate)) {
				return true;
			}
		}
		return result;
	}
	public static boolean isBetweenHour(Date startDate, Date endDate, Date specificDate) {
		boolean result = false;
		long startTime=startDate.getTime();
		long endTime=endDate.getTime();
		long specTime=specificDate.getTime();
		if(startTime<specTime&&specTime<endTime) {
			result=true;
		}
		return result;
	}

	public static boolean isTodayDate(Date validDate) {
		Calendar validCal = new GregorianCalendar();
		validCal.setTime(validDate);
		Calendar endCal = new GregorianCalendar();
		endCal.setTime(new Date());
		int d = (endCal.get(Calendar.DATE)) - (validCal.get(Calendar.DATE));
		int m = (endCal.get(Calendar.MONTH)) - (validCal.get(Calendar.MONTH));
		int y = (endCal.get(Calendar.YEAR)) - (validCal.get(Calendar.YEAR));
		if (y == 0 && m == 0 && d == 0) {
			return true;
		}
		return false;
	}

	public static Date getDateByTimeLong(Long timeLong) {
		return new Date(timeLong);
	}

	public static Calendar toCalendar(Date tempDate) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(tempDate);
		return cal;
	}

	// 根据日期取得星期几
	public static String getWeek(Date date) {
		String[] weeks = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (week_index < 0) {
			week_index = 0;
		}
		return weeks[week_index];
	}

	public static String getPastDate(String type, String formatType) {
		// SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd
		// HH:mm:ss");
		SimpleDateFormat format = new SimpleDateFormat(formatType);

		Calendar c = Calendar.getInstance();
		// 过去七天
		// c.setTime(new Date());
		// c.add(Calendar.DATE, - 7);
		// Date d = c.getTime();
		// String day = format.format(d);
		// System.out.println("过去七天："+day);

		// 过去一月
		c.setTime(new Date());
		c.add(Calendar.MONTH, -1);
		Date m = c.getTime();
		String mon = format.format(m);
		return mon;
		// System.out.println("过去一个月："+mon);

		// //过去三个月
		// c.setTime(new Date());
		// c.add(Calendar.MONTH, -3);
		// Date m3 = c.getTime();
		// String mon3 = format.format(m3);
		// System.out.println("过去三个月："+mon3);
		//
		// //过去一年
		// c.setTime(new Date());
		// c.add(Calendar.YEAR, -1);
		// Date y = c.getTime();
		// String year = format.format(y);
		// System.out.println("过去一年："+year);
	}

	/**
	 * String[] weeks = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
	 * 
	 * @param date
	 * @return
	 */
	public static int getWeekByDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (week_index < 0) {
			week_index = 0;
		}
		return week_index;
	}

	public static void main(String args[]) throws ParseException {
		Date todayDate = new Date();
		Date tempDate=DateUtils.getStartOfDate(todayDate);
		Date startDate=DateUtils.getDateBetweenHour(tempDate, 8);
		Date endDate=DateUtils.getDateBetweenHour(tempDate, 23);
		System.out.println(DateUtils.format(tempDate, "yyyy-MM-dd HH:mm"));
		System.out.println(DateUtils.format(startDate, "yyyy-MM-dd HH:mm"));
		System.out.println(DateUtils.format(endDate, "yyyy-MM-dd HH:mm"));
		boolean flag=DateUtils.isBetweenHour(startDate, endDate,todayDate);
		System.out.println(flag);
		
		
		
	}

}
