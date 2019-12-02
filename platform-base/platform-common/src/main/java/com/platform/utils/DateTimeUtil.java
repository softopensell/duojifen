 package com.platform.utils;
 
 import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
 
 public class DateTimeUtil
 {
   public static int DATE_FORMAT = 2;
 
   public static Locale DATE_LOCALE = Locale.SIMPLIFIED_CHINESE;
 
   public static String dateToMillis(java.util.Date date)
   {
     return StringUtil.zeroPadString(Long.toString(date.getTime()), 15);
   }
 
   public static String getCurrentTimeStamp()
   {
     DateFormat df = DateFormat.getDateTimeInstance(0, 1, new Locale("zh", "CN"));
 
     return df.format(new java.util.Date(System.currentTimeMillis()));
   }
 
   public static int compareTo(Timestamp st1, Timestamp st2)
   {
     long thisTime = st1 != null ? st1.getTime() : 0L;
     long anotherTime = st2 != null ? st2.getTime() : 0L;
     return thisTime == anotherTime ? 0 : thisTime < anotherTime ? -1 : 1;
   }
 
   public static String toDateTimeString(Timestamp ts)
   {
     if (ts == null) {
       return "";
     }
     return DateFormat.getDateTimeInstance(1, 2, Locale.CHINA).format(ts);
   }
 
   public static String toDateString(Timestamp ts)
   {
     if (ts == null) {
       return "";
     }
     return DateFormat.getDateInstance(1, Locale.CHINA).format(ts);
   }
 
   public static String toDateString(Timestamp ts, String format)
   {
     if (ts == null) {
       return "";
     }
     DateFormat df = new SimpleDateFormat(format);
     return df.format(ts);
   }
 
   public static String toTimeString(Timestamp ts)
   {
     if (ts == null) {
       return "";
     }
     return DateFormat.getTimeInstance(1, Locale.CHINA).format(ts);
   }
 
   public static long nowMillis()
   {
     return System.currentTimeMillis();
   }
 
   public static Timestamp nowTimestamp()
   {
     return new Timestamp(System.currentTimeMillis());
   }
 
   public static java.util.Date nowDate()
   {
     return new java.util.Date();
   }
 
   public static Timestamp getDayStart(Timestamp stamp)
   {
     return getDayStart(stamp, 0);
   }
 
   public static Timestamp getDayStart(Timestamp stamp, int daysLater)
   {
     Calendar tempCal = Calendar.getInstance();
 
     tempCal.setTime(new java.util.Date(stamp.getTime()));
     tempCal.set(tempCal.get(1), tempCal.get(2), tempCal.get(5), 0, 0, 0);
 
     tempCal.add(5, daysLater);
     return new Timestamp(tempCal.getTime().getTime());
   }
 
   public static Timestamp getNextDayStart(Timestamp stamp)
   {
     return getDayStart(stamp, 1);
   }
 
   public static Timestamp getDayEnd(Timestamp stamp)
   {
     return getDayEnd(stamp, 0);
   }
 
   public static Timestamp getDayEnd(Timestamp stamp, int daysLater)
   {
     Calendar tempCal = Calendar.getInstance();
 
     tempCal.setTime(new java.util.Date(stamp.getTime()));
     tempCal.set(tempCal.get(1), tempCal.get(2), tempCal.get(5), 23, 59, 59);
 
     tempCal.add(5, daysLater);
     return new Timestamp(tempCal.getTime().getTime());
   }
 
   public static Timestamp toTimestamp(String dateString)
   {
     if ((dateString == null) || (dateString.trim().length() == 0)) {
       return null;
     }
 
     SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
     try {
       java.util.Date date = format.parse(dateString.trim());
       return new Timestamp(date.getTime()); } catch (ParseException e) {
     }
     return null;
   }
 
   public static Timestamp parseTime(String dateString, String timeFormat)
   {
     if ((dateString == null) || (dateString.trim().length() == 0)) {
       return null;
     }
 
     SimpleDateFormat format = new SimpleDateFormat(timeFormat);
     try {
       java.util.Date date = format.parse(dateString.trim());
       return new Timestamp(date.getTime()); } catch (ParseException e) {
     }
     return null;
   }
 
   public static java.util.Date parseDate(String dateString, String timeFormat)
   {
     if ((dateString == null) || (dateString.trim().length() == 0)) {
       return null;
     }
 
     SimpleDateFormat format = new SimpleDateFormat(timeFormat);
     try {
       return format.parse(dateString.trim());
     } catch (ParseException e) {
     }
     return null;
   }
 
   public static Timestamp toTimestampAdjustTime(String dateString)
   {
     Timestamp st = toTimestamp(dateString);
     if (st != null) {
       Calendar cal = Calendar.getInstance();
       cal.setTime(st);
       cal.set(10, 23);
       cal.set(12, 59);
       cal.set(13, 59);
       st.setTime(cal.getTime().getTime());
 
       return st;
     }
 
     return null;
   }
 
   public static Timestamp monthBegin()
   {
     Calendar mth = Calendar.getInstance();
 
     mth.set(5, 1);
     mth.set(11, 0);
     mth.set(12, 0);
     mth.set(13, 0);
     mth.set(9, 0);
     return new Timestamp(mth.getTime().getTime());
   }
 
   public static String timestampDiff(Timestamp st1, Timestamp st2)
   {
     int count = timestampDiff(5, st1, st2);
     if (count < 0) {
       return null;
     }
     return count + " 天";
   }
 
   public static int timestampDiff(int unit, Timestamp st1, Timestamp st2)
   {
     if ((st1 == null) || (st2 == null)) {
       return -1;
     }
 
     Calendar g1 = Calendar.getInstance();
     Calendar g2 = Calendar.getInstance();
     g1.setTime(st1);
     g2.setTime(st2);
     Calendar gc1;
     Calendar gc2;
     if (g2.after(g1)) {
       gc2 = (Calendar)g2.clone();
       gc1 = (Calendar)g1.clone();
     } else {
       gc2 = (Calendar)g1.clone();
       gc1 = (Calendar)g2.clone();
     }
 
     if (5 == unit) {
       gc1.clear(14);
       gc1.clear(13);
       gc1.clear(12);
       gc1.clear(11);
 
       gc2.clear(14);
       gc2.clear(13);
       gc2.clear(12);
       gc2.clear(11);
     } else if (10 == unit) {
       gc1.clear(14);
       gc1.clear(13);
       gc1.clear(12);
 
       gc2.clear(14);
       gc2.clear(13);
       gc2.clear(12);
     } else if (12 == unit) {
       gc1.clear(14);
       gc1.clear(13);
 
       gc2.clear(14);
       gc2.clear(13);
     } else if (2 == unit) {
       gc1.clear(14);
       gc1.clear(13);
       gc1.clear(12);
       gc1.clear(11);
       gc1.clear(5);
 
       gc2.clear(14);
       gc2.clear(13);
       gc2.clear(12);
       gc2.clear(11);
       gc2.clear(5);
     } else if (1 == unit) {
       gc1.clear(14);
       gc1.clear(13);
       gc1.clear(12);
       gc1.clear(11);
       gc1.clear(5);
       gc1.clear(2);
 
       gc2.clear(14);
       gc2.clear(13);
       gc2.clear(12);
       gc2.clear(11);
       gc2.clear(5);
       gc2.clear(2);
     } else if (13 == unit) {
       gc1.clear(14);
 
       gc2.clear(14);
     } else {
       return 0;
     }
 
     int elapsed = 0;
     while (gc1.before(gc2)) {
       gc1.add(unit, 1);
       elapsed++;
     }
     return elapsed;
   }
 
   public static String formatDatetime(java.util.Date aDate)
   {
     if (aDate == null)
       return "";
     Calendar calendar = Calendar.getInstance();
     calendar.setTime(aDate);
     SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
     String s = df.format(calendar.getTime());
     return s;
   }
 
   public static java.util.Date stringToDate(String date) {
     java.util.Date ret = null;
     SimpleDateFormat df = null;
     if (date.length() <= 10)
       df = new SimpleDateFormat("yyyy-MM-dd", DATE_LOCALE);
     else {
       df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", DATE_LOCALE);
     }
     ret = df.parse(date, new ParsePosition(0));
     return ret;
   }
 
   public static String getDatesInterval(java.util.Date startDate, java.util.Date endDate)
     throws Exception
   {
     String start = formatDatetime(startDate);
     String end = formatDatetime(endDate);
     startDate = stringToDate(start);
     endDate = stringToDate(end);
 
     String interval = "";
     long miSecondInterval = endDate.getTime() - startDate.getTime();
     if (miSecondInterval < 0L)
       throw new Exception("开始时间不能在结束时间之后! endDate.getTime() = " + endDate.getTime() + "; startDate.getTime() = " + startDate.getTime() + "; miSecondInterval = " + miSecondInterval);
     long ddInterval = miSecondInterval / 86400000L;
     long left = miSecondInterval % 86400000L;
     long hhInterval = left / 3600000L;
     left %= 3600000L;
     long mmInterval = left / 60000L;
     left %= 60000L;
     long ssInterval = left / 1000L;
     if (ddInterval != 0L)
       interval = interval + ddInterval + "天";
     if ((hhInterval != 0L) || (interval.length() != 0))
       interval = interval + hhInterval + "小时";
     if ((mmInterval != 0L) || (interval.length() != 0))
       interval = interval + mmInterval + "分";
     interval = interval + ssInterval + "秒";
     return interval;
   }
 
   public static String formatDate(String s, int iType, int iLanguage)
   {
     String _tempStr = null;
     s = s.trim();
     if (s.equals("")) {
       return "";
     }
     String sYear = s.substring(0, 4);
     String sMonth = s.substring(4, 6);
     String sDay = s.substring(6, 8);
     String sHours = s.substring(8, 10);
     String sMinutes = s.substring(10, 12);
     String sSeconds = s.substring(12, 14);
     switch (iType) {
     case 0:
       if (iLanguage == 0) {
         _tempStr = sYear + "-" + sMonth + "-" + sDay;
       } else {
         _tempStr = sYear + "年";
         if (!sMonth.equals("00")) {
           _tempStr = _tempStr + sMonth + "月";
         }
         if (!sDay.equals("00"))
           _tempStr = _tempStr + sDay + "日";  } break;
     case 1:
       if (iLanguage == 0) {
         _tempStr = sYear + "-" + sMonth + "-" + sDay + " " + sHours;
       } else {
         _tempStr = sYear + "年";
         if (!sMonth.equals("00")) {
           _tempStr = _tempStr + sMonth + "月";
         }
         if (!sDay.equals("00")) {
           _tempStr = _tempStr + sDay + "日";
         }
         if (!sHours.equals("00"))
           _tempStr = _tempStr + sHours + "时";  } break;
     case 2:
       if (iLanguage == 0) {
         _tempStr = sYear + "-" + sMonth + "-" + sDay + " " + sHours + ":" + sMinutes;
       } else {
         _tempStr = sYear + "年";
         if (!sMonth.equals("00")) {
           _tempStr = _tempStr + sMonth + "月";
         }
         if (!sDay.equals("00")) {
           _tempStr = _tempStr + sDay + "日";
         }
         if (!sHours.equals("00")) {
           _tempStr = _tempStr + sHours + "时";
         }
         if (!sMinutes.equals("00"))
           _tempStr = _tempStr + sMinutes + "分";  } break;
     case 3:
       if (iLanguage == 0) {
         _tempStr = sYear + "-" + sMonth + "-" + sDay + " " + sHours + ":" + sMinutes + ":" + sSeconds;
       } else {
         _tempStr = sYear + "年";
         if (!sMonth.equals("00")) {
           _tempStr = _tempStr + sMonth + "月";
         }
         if (!sDay.equals("00")) {
           _tempStr = _tempStr + sDay + "日";
         }
         if (!sHours.equals("00")) {
           _tempStr = _tempStr + sHours + "时";
         }
         if (!sMinutes.equals("00")) {
           _tempStr = _tempStr + sMinutes + "分";
         }
         if (!sSeconds.equals("00")) {
           _tempStr = _tempStr + sSeconds + "秒";
         }
       }
       break;
     }
     return _tempStr;
   }
 
   public static int getLastDate(String selectDate)
   {
     int dates = 0;
 
     Calendar calendar = Calendar.getInstance();
     DateFormat dateFormat = DateFormat.getDateInstance(DATE_FORMAT, DATE_LOCALE);
     try {
       calendar.setTime(dateFormat.parse(selectDate));
     } catch (ParseException e) {
       System.err.println("catch exception in getLastDate(selectDate). select date:" + selectDate);
     }
     int year = calendar.get(1);
     switch (calendar.get(2) + 1) {
     case 1:
       dates = 31;
       break;
     case 2:
       if ((year % 400 == 0) || ((year % 4 == 0) && (year % 100 != 0)))
         dates = 29;
       else {
         dates = 28;
       }
       break;
     case 3:
       dates = 31;
       break;
     case 4:
       dates = 30;
       break;
     case 5:
       dates = 31;
       break;
     case 6:
       dates = 30;
       break;
     case 7:
       dates = 31;
       break;
     case 8:
       dates = 31;
       break;
     case 9:
       dates = 30;
       break;
     case 10:
       dates = 31;
       break;
     case 11:
       dates = 30;
       break;
     case 12:
       dates = 31;
     }
 
     return dates;
   }
 
   public static String getLastDateOfMonth()
   {
     return getCurrentYear() + "-" + getCurrentMonth() + "-" + getLastDate(getCurrentDate());
   }
 
   public static String getLastDateOfQuarter()
   {
     String date = getCurrentYear();
     switch (Integer.parseInt(getCurrentQuarter())) {
     case 1:
       date = date + "-03-31";
       break;
     case 2:
       date = date + "-06-30";
       break;
     case 3:
       date = date + "-09-30";
       break;
     case 4:
       date = date + "-12-31";
     }
 
     return date;
   }
 
   public static String getFirstDateOfMonth()
   {
     SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-");
     String s = df.format(new java.util.Date());
     s = s + "01";
     return s;
   }
 
   public static String getLastDateOfLastMonth()
   {
     DateFormat dateFormat = DateFormat.getDateInstance(DATE_FORMAT, DATE_LOCALE);
     java.util.Date uDate = null;
     try {
       uDate = dateFormat.parse(getFirstDateOfMonth());
     } catch (ParseException e) {
       System.err.println("Catch Exception in getLastDateOfLastMonth of DateUtil.");
     }
     Calendar calendar = Calendar.getInstance();
     calendar.setTime(uDate);
     calendar.add(5, -1);
     SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
     String s = df.format(calendar.getTime());
     return s;
   }
 
   public static String getMaxDate(String strSource)
   {
     if (strSource.equals("")) {
       return "";
     }
     if (strSource.length() != 10) {
       return strSource;
     }
     if (strSource.indexOf("-00") == -1) {
       return strSource;
     }
     strSource = StringUtil.replace(strSource, "-00", "");
     strSource = StringUtil.replace(strSource, "-", "");
     if (strSource.length() == 4) {
       return strSource + "-12-31";
     }
 
     String sYear = strSource.substring(0, 4);
     int iYear = Integer.parseInt(sYear);
     int iMonth = Integer.parseInt(strSource.substring(4, 6));
     switch (iMonth) {
     case 1:
       strSource = sYear + "-01-31";
       break;
     case 2:
       if ((iYear % 400 == 0) || ((iYear % 4 == 0) && (iYear % 100 != 0)))
         strSource = sYear + "-02-29";
       else {
         strSource = sYear + "-02-28";
       }
       break;
     case 3:
       strSource = sYear + "-03-31";
       break;
     case 4:
       strSource = sYear + "-04-30";
       break;
     case 5:
       strSource = sYear + "-05-31";
       break;
     case 6:
       strSource = sYear + "-06-30";
       break;
     case 7:
       strSource = sYear + "-07-31";
       break;
     case 8:
       strSource = sYear + "-08-31";
       break;
     case 9:
       strSource = sYear + "-09-30";
       break;
     case 10:
       strSource = sYear + "-10-31";
       break;
     case 11:
       strSource = sYear + "-11-30";
       break;
     case 12:
       strSource = sYear + "-12-31";
     }
 
     return strSource;
   }
 
   public static String getNextDate()
   {
     Calendar calendar = Calendar.getInstance();
     calendar.add(5, 1);
     SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
     String s = df.format(calendar.getTime());
     return s;
   }
 
   public static String getPreviousDate()
   {
     Calendar calendar = Calendar.getInstance();
     calendar.add(5, -1);
     SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
     String s = df.format(calendar.getTime());
     return s;
   }
 
   public static String getCurrentMonth()
   {
     SimpleDateFormat df = new SimpleDateFormat("MM");
     String month = df.format(new java.util.Date());
     return month;
   }
 
   public static String getCurrentQuarter()
   {
     String quarter = null;
     Calendar mydate = Calendar.getInstance();
     Double dd = new Double(Math.floor(mydate.get(2) / 3));
     switch (dd.intValue()) {
     case 0:
       quarter = "1";
       break;
     case 1:
       quarter = "2";
       break;
     case 2:
       quarter = "3";
       break;
     case 3:
       quarter = "4";
     }
 
     return quarter;
   }
 
   public static String getCurrentYear()
   {
     SimpleDateFormat df = new SimpleDateFormat("yyyy");
     return df.format(new java.util.Date());
   }
 
   public static java.sql.Date getCurrentSqlDate(int iType)
   {
     String format = null;
     switch (iType) {
     case 0:
       format = "yyyy-MM-dd";
       break;
     case 1:
       format = "yyyy-MM-dd HH";
       break;
     case 2:
       format = "yyyy-MM-dd HH:mm";
       break;
     case 3:
     case 4:
     case 5:
       format = "yyyy-MM-dd HH:mm:ss";
     }
 
     return getSqlDate(getCurrentDate(iType), format);
   }
 
   public static java.sql.Date getSqlDate(String dateStr, String format)
   {
     DateFormat df = new SimpleDateFormat(format);
     java.sql.Date date = null;
     try
     {
       if ((dateStr != null) && (dateStr.trim().length() > 0))
         date = new java.sql.Date(df.parse(dateStr).getTime());
     }
     catch (ParseException e) {
       System.err.println("the given string cannot be parsed as a date : " + dateStr + "|" + format);
     }
     return date;
   }
 
   public static String getCurrentDate()
   {
     return getCurrentDate(0);
   }
 
   public static String getCurrentDate(int iType)
   {
     SimpleDateFormat df = null;
 
     switch (iType) {
     case 0:
       df = new SimpleDateFormat("yyyy-MM-dd");
       break;
     case 1:
       df = new SimpleDateFormat("yyyy-MM-dd HH");
       break;
     case 2:
       df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
       break;
     case 3:
       df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       break;
     case 4:
       long currentTimeMillis = System.currentTimeMillis();
       Timestamp ts = new Timestamp(currentTimeMillis);
       return ts.toString();
     }
     return df.format(new java.util.Date());
   }
 
   public static String getDay(Timestamp timestamp)
   {
     Calendar calendar = Calendar.getInstance();
     calendar.setTime(timestamp);
     return String.valueOf(calendar.get(5));
   }
 
   public static String getPeriod(Timestamp timestamp)
   {
     int day = Integer.parseInt(getDay(timestamp));
     if ((day >= 1) && (day <= 10))
       return "1";
     if ((day >= 11) && (day <= 20)) {
       return "2";
     }
     return "3";
   }
 
   public static String getQuarterByDay(String sDate1)
   {
     String quarter = "";
     DateFormat dateFormat = DateFormat.getDateInstance(DATE_FORMAT, DATE_LOCALE);
     java.util.Date date1 = null;
     try {
       date1 = dateFormat.parse(sDate1);
     } catch (ParseException e) {
       System.err.println("Catch Exception in getQuarterByDay(sDate1) sDate1:" + sDate1);
       return "";
     }
     Calendar mydate = Calendar.getInstance();
     mydate.setTime(date1);
     Double dd = new Double(Math.floor(mydate.get(2) / 3));
     switch (dd.intValue()) {
     case 0:
       quarter = "1";
       break;
     case 1:
       quarter = "2";
       break;
     case 2:
       quarter = "3";
       break;
     case 3:
       quarter = "4";
     }
 
     return quarter;
   }
 
   public static String getQuarter(Timestamp timestamp)
   {
     String quarter = null;
     Calendar calendar = Calendar.getInstance();
     calendar.setTime(timestamp);
     int month = calendar.get(2) + 1;
     if ((month == 1) || (month == 2) || (month == 3))
       quarter = "1";
     else if ((month == 4) || (month == 5) || (month == 6))
       quarter = "2";
     else if ((month == 7) || (month == 8) || (month == 9))
       quarter = "3";
     else if ((month == 10) || (month == 11) || (month == 12)) {
       quarter = "4";
     }
     return quarter;
   }
 
   public static String[] getStartEndDates(String year, String quarter, String month)
   {
     String[] ret = new String[2];
     ret[0] = year;
     ret[1] = year;
     if (quarter.equals("")) {
       ret[0] = (ret[0] + "-01-01");
       ret[1] = (ret[1] + "-12-31");
     }
     else if (month.equals("")) {
       switch (Integer.parseInt(quarter)) {
       case 1:
         ret[0] = (ret[0] + "-01-" + getStartEndDates(year, "1")[0]);
         ret[1] = (ret[1] + "-03-" + getStartEndDates(year, "3")[1]);
         break;
       case 2:
         ret[0] = (ret[0] + "-04-" + getStartEndDates(year, "4")[0]);
         ret[1] = (ret[1] + "-06-" + getStartEndDates(year, "6")[1]);
         break;
       case 3:
         ret[0] = (ret[0] + "-07-" + getStartEndDates(year, "7")[0]);
         ret[1] = (ret[1] + "-09-" + getStartEndDates(year, "9")[1]);
         break;
       case 4:
         ret[0] = (ret[0] + "-10-" + getStartEndDates(year, "10")[0]);
         ret[1] = (ret[1] + "-12-" + getStartEndDates(year, "12")[1]);
       }
     }
     else {
       ret[0] = (year + "-" + month + "-" + getStartEndDates(year, month)[0]);
       ret[1] = (year + "-" + month + "-" + getStartEndDates(year, month)[1]);
     }
 
     return ret;
   }
 
   public static String[] getStartEndDates(String year, String month)
   {
     String[] ret = new String[2];
     int iYear = Integer.parseInt(year);
     int iMonth = Integer.parseInt(month);
     switch (iMonth) {
     case 1:
       ret[0] = "1";
       ret[1] = "31";
       break;
     case 2:
       if (((iYear % 400 == 0) || (iYear % 4 == 0)) && (iYear % 100 != 0)) {
         ret[0] = "1";
         ret[1] = "29";
       } else {
         ret[0] = "1";
         ret[1] = "28";
       }
       break;
     case 3:
       ret[0] = "1";
       ret[1] = "31";
       break;
     case 4:
       ret[0] = "1";
       ret[1] = "30";
       break;
     case 5:
       ret[0] = "1";
       ret[1] = "31";
       break;
     case 6:
       ret[0] = "1";
       ret[1] = "30";
       break;
     case 7:
       ret[0] = "1";
       ret[1] = "31";
       break;
     case 8:
       ret[0] = "1";
       ret[1] = "31";
       break;
     case 9:
       ret[0] = "1";
       ret[1] = "30";
       break;
     case 10:
       ret[0] = "1";
       ret[1] = "31";
       break;
     case 11:
       ret[0] = "1";
       ret[1] = "30";
       break;
     case 12:
       ret[0] = "1";
       ret[1] = "31";
     }
 
     return ret;
   }
 
   public static int getCurrentWeekDay()
   {
     Calendar c = Calendar.getInstance();
     int num = c.get(7);
     int weekDayNum = 1;
     if (num == 1)
       weekDayNum = 7;
     else {
       weekDayNum = num - 1;
     }
     return weekDayNum;
   }
 }