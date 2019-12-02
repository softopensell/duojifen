/**
 * 
 */
package com.platform.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.apache.commons.lang.StringUtils;

/**
 * @author softopensell
 * @version 1.0.0
 *
 */
public class MoneyFormatUtils {

	public final static String DEFAULT_MAONEY_FORMATTER  ="###,##0.00";
	
	public final static String DEFAULT_PERCENT_FORMATTER  ="#.00";

	/**
	 * 
	 * @author softopensell
	 * @param formatter
	 * @param money
	 * @return
	 */
	public static String format(String formatter, double money){
		DecimalFormat decimalFormat = new DecimalFormat(formatter);
		return decimalFormat.format(money);
	}
	
	public static String newformat(String formatter, double money){
		DecimalFormat newformat = (DecimalFormat)DecimalFormat.getInstance();
		newformat.setGroupingUsed(false);
		newformat.setMaximumFractionDigits(2);
		newformat.setMinimumFractionDigits(2);
		return String.format(formatter, newformat.format(money));
	}
	/**
	 * 将 money int 换算成 元
	 * @param money
	 * @return
	 */
	public static double formatIntToDouble(int money){
		double tempPrice=((double)money)/100;
		return tempPrice;
	}
	
	public static double formatBigDecimalToDouble(BigDecimal money){
		return money.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	public static BigDecimal formatBigDecimal(BigDecimal money){
		return money.setScale(2, BigDecimal.ROUND_HALF_UP);
	}
	public static BigDecimal formatBigDecimal4(BigDecimal money){
		return money.setScale(4, BigDecimal.ROUND_HALF_UP);
	}
	/**
	 * 换成分
	 * @param money
	 * @return
	 */
	public static int formatBigDecimalToInt(BigDecimal money){
		Double tempMoney= money.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		tempMoney=tempMoney*100;
		return tempMoney.intValue();
	}
	/**
	 * 转化成字符 元角分
	 * @param money
	 * @return
	 */
	public static String formatBigDecimalToDoubleStr(BigDecimal money){
		DecimalFormat decimalFormat = new DecimalFormat(MoneyFormatUtils.DEFAULT_PERCENT_FORMATTER);
		String s = decimalFormat.format(money.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		return s;
	}
	
	public static double getFee(double money,double rate){
		double fee=Math.round(money*rate*100)*0.01d;
		return fee;
	}
	public static double getFee(int money,double rate){
		double fee=Math.round(money*rate*100)*0.01d;
		return fee;
	}
	/**
	 * 
	 * @param money 分哟
	 * @param rate 百分比
	 * @return
	 */
	public static int getMoney(int money,double rate){
		Double tempMoney=Math.round(money*rate*100)*0.01d;
		return tempMoney.intValue();
	}
	public static int getMoney(long money,double rate){
		Double tempMoney=Math.round(money*rate*100)*0.01d;
		return tempMoney.intValue();
	}
	
	public static double getFee(BigDecimal money,double rate){
         BigDecimal rateBig = new BigDecimal(Double.valueOf(rate));
		BigDecimal fee=money.multiply(rateBig);
		return fee.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	/**
	 * 分转元 
	 * @param money 分
	 * @return
	 */
	public static BigDecimal getBigDecimalFee(int money){
		BigDecimal rateBig = new BigDecimal(Double.valueOf(money));
		return rateBig.divide(new BigDecimal("100"),2,BigDecimal.ROUND_HALF_UP);
	}
	/**
	 * 分转元 
	 * @param money 分
	 * @return
	 */
	public static BigDecimal getBigDecimalFee(long money){
		BigDecimal rateBig = new BigDecimal(Double.valueOf(money));
		return rateBig.divide(new BigDecimal("100"),2,BigDecimal.ROUND_HALF_UP);
	}
	
	
	/**
	 * int Money 除以100
	 * @param money 分
	 * @return
	 */
	public static int getIntDivide100(int money){
		BigDecimal rateBig = new BigDecimal(Double.valueOf(money));
		BigDecimal divide = rateBig.divide(new BigDecimal("100"),4,BigDecimal.ROUND_HALF_UP);
		return divide.intValue();
	}
	
	/**
	 * 除法
	 * @return
	 */
	/**
	 * 除法 a/b 默认精度 小数点后 4 位
	 * @param a
	 * @param b
	 * @param scale
	 * @return
	 */
	public static BigDecimal getDivide(BigDecimal a,BigDecimal b,int scale){
		BigDecimal result = a.divide(b,scale,BigDecimal.ROUND_HALF_UP);
		return result;
	}
	/**
	 * 除法 a/b 默认精度 小数点后 4 位
	 * @param a
	 * @param b
	 * @param scale
	 * @return
	 */
	public static BigDecimal getDivide(BigDecimal a,BigDecimal b){
		BigDecimal result = a.divide(b,4,BigDecimal.ROUND_HALF_UP);
		return result;
	}
	/**
	 * a*b 乘法 默认精度 4
	 * @param a
	 * @param b
	 * @param scale
	 * @return
	 */
	public static BigDecimal getMultiply(BigDecimal a,BigDecimal b,int scale){
		BigDecimal result = a.multiply(b);
		return result.setScale(scale,BigDecimal.ROUND_HALF_UP);
	}
	/**
	 * a*b 乘法 默认精度 4 
	 * @param a
	 * @param b
	 * @param scale
	 * @return
	 */
	public static BigDecimal getMultiply(BigDecimal a,BigDecimal b){
		BigDecimal result = a.multiply(b);
		return result.setScale(4,BigDecimal.ROUND_HALF_UP);
	}
	public static BigDecimal getMultiply(BigDecimal a,int b){
		BigDecimal result = a.multiply(new BigDecimal(b));
		return result.setScale(4,BigDecimal.ROUND_HALF_UP);
	}
	public static BigDecimal getMultiply(BigDecimal a,double b){
		BigDecimal result = a.multiply(new BigDecimal(b));
		return result.setScale(4,BigDecimal.ROUND_HALF_UP);
	}
	public static BigDecimal getMultiply(int a,int b){
		BigDecimal result = new BigDecimal(a*b);
		return result.setScale(4,BigDecimal.ROUND_HALF_UP);
	}
	
	/**
	 * double *100 换算成分 存储
	 * @param money
	 * @return
	 */
	public static int formatDoubleToInt(double money){
		 Double tempPrice=money*100;
		return tempPrice.intValue();
	}
	public static String percentformat(String formatter, double number){
		DecimalFormat nf = new DecimalFormat(formatter);
		return nf.format(number);
	}
	
	
	public static byte intToByte(int x) {
		return (byte) x;
	}
	public static int byteToInt(byte b) {
		// Java 总是把 byte 当做有符处理；我们可以通过将其和 0xFF 进行二进制与得到它的无符值
		return b & 0xFF;
	}
	
	public static String getBinaryString24AND(int... is) {
		if (is == null || is.length == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (int i : is) {
			sb.append(StringUtils.reverse(getBinaryString24(i)));
		}
		return sb.toString();
	}
	public static int getIntBySubString(String s, int begin, int end) {
		if (s.length() < end) {
			return 0;
		}
		return Integer.valueOf(StringUtils.reverse(s.substring(begin, end)), 2);
	}
	public static int getIntByBinaryString(String s) {
		return Integer.valueOf(s, 2);
	}
	public static String getBinaryString(Integer num){
		String bs = Integer.toBinaryString(num);
		return bs;
	}
	
	public static String getBinaryString24(Integer num){
			String bs = Integer.toBinaryString(num);
			int length = bs.length();
			if(length < 24){
				StringBuilder sb = new StringBuilder();
				int dis = 24 - length;
				for(int i = 0 ; i < dis; i ++){
					sb.append("0");
				}
				sb.append(bs);
			return sb.toString();
		}
	 return bs.substring(0, 24);
	}	
	public static BigDecimal getFeeBigDecimal(BigDecimal money,double rate){
        BigDecimal rateBig = new BigDecimal(Double.valueOf(rate));
		BigDecimal fee=money.multiply(rateBig);
		return fee.setScale(2, BigDecimal.ROUND_HALF_UP);
	}
	

   public static void main(String[] args) {
	   byte b=0x1f;
	   
//	   System.out.println(byteToInt(b));
//	   System.out.println(StringUtils.reverse("abc"));
//	   System.out.println(intToByte(12));
//	   System.out.println(getBinaryString(7&1));
//	   System.out.println(getBinaryString((Integer.MAX_VALUE-8)&127));
//	   System.out.println(getBinaryString(Integer.MAX_VALUE).length());
//	   System.out.println(getBinaryString(1023));
//	   System.out.println(getBinaryString24(1023));
//	   System.out.println(getIntByBinaryString(getBinaryString24(1023)));
//	   
//		public static BigDecimal getBigDecimalFee(int money){
//			BigDecimal rateBig = new BigDecimal(Double.valueOf(money));
//			BigDecimal fee=rateBig.divide(new BigDecimal("100"));
//			return fee.setScale(2, BigDecimal.ROUND_HALF_UP);
//		}999
	   
	   
		int incrPoint = 127777;
		int one_free=0;//分 //incrPoint*tempRate/100 分
		int two_free=0;
		int three_free=0;
		if((1+1+0)>0) {
			one_free=incrPoint*3/(1+1+0)/100;
		}

		if((1+0)>0) {
			two_free=incrPoint*2/(1+0)/100;
		}
//		if((three_star_sum)>0) {
//			three_free=incrPoint*three_star_rate/(three_star_sum)/100;
//		}
//	   System.out.println("wenfeng:"+one_free);
//	   System.out.println("shuai"+one_free+two_free);
//	   System.out.println(127777/100);
//	   System.out.println(getIntDivide100(3));
	   
	   //按分存储 60
		 int addBalance=127777*3/100;//incrPoint*tempRate/100 分
	   System.out.println("addBalance::"+addBalance);
	   
	   
//	double money=1000000.80432232;
//	System.out.println(MoneyFormatUtils.formatBigDecimalToInt(new BigDecimal(1.01)));
//	
//	
//	DecimalFormat decimalFormat = new DecimalFormat(MoneyFormatUtils.DEFAULT_MAONEY_FORMATTER);
//
//	String s = decimalFormat.format(money);
//	System.out.println(s);
//	
//	 System.out.println(MoneyFormatUtils.newformat(MoneyFormatUtils.DEFAULT_MAONEY_FORMATTER, money));  
//	 System.out.println(MoneyFormatUtils.formatBigDecimalToDoubleStr(new BigDecimal(money)));
//	 
//	 System.out.println(MoneyFormatUtils.formatIntToDouble(10001));
//	 System.out.println(MoneyFormatUtils.formatDoubleToInt(100.01));
//	 System.out.println(MoneyFormatUtils.getFee(money, 1));
//	 System.out.println(MoneyFormatUtils.getFee(new BigDecimal(money),1));
	   
	   int a=(int) MoneyFormatUtils.getFee(10, 0.02);
	   System.out.println(a);
	   a=(int) MoneyFormatUtils.getFee(10, 0.2);
	   System.out.println(a);
	   BigDecimal bigDecimal=new BigDecimal("1110.1111");
	   System.out.println(bigDecimal.intValue());
	   System.out.println(MoneyFormatUtils.formatBigDecimal4(new BigDecimal(0.00434)));
	   BigDecimal a1=new BigDecimal(1);
	   BigDecimal b2=new BigDecimal(3);
	   System.out.println(MoneyFormatUtils.getDivide(a1, b2));
	   System.out.println(MoneyFormatUtils.getDivide(a1, b2,3));
	   System.out.println(new BigDecimal(2100).multiply(new BigDecimal(0.0001)));
	   System.out.println(MoneyFormatUtils.getMultiply(new BigDecimal(1000), new BigDecimal(0)));
	   System.out.println(MoneyFormatUtils.getMultiply(new BigDecimal(1000), 0.003d));
	 
}
}
