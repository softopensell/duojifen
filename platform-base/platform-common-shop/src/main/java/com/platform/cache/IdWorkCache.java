package com.platform.cache;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.platform.utils.DateUtils;
import com.platform.utils.RandomUtil;
import com.platform.utils.SnowflakeIdWorker;

/**
 * IdWorkCache
 *
 */
@Component
public class IdWorkCache{
	

	public static  String  ID_WORK_TYPE_QR_CARD_ORDER_NO="ID_WORK_TYPE_QR_CARD_ORDER_NO";//订单号生产
	public static  String  ID_WORK_TYPE_COMPANY_SN="ID_WORK_TYPE_COMPANY_SN";//订单号生产
	public static  String  ID_WORK_TYPE_HALL_ORDER_SN="ID_WORK_TYPE_HALL_ORDER_SN";//订单号生产
	public static  String  ID_WORK_TYPE_SHOP_PRODUCT_SN="ID_WORK_TYPE_SHOP_PRODUCT_SN";//
	public static  String  ID_WORK_TYPE_ZB_SN="ID_WORK_TYPE_ZB_SN";//
	public static  String  ID_WORK_TYPE_PAY_SN="ID_WORK_TYPE_PAY_SN";//
	public static  String  ID_WORK_TYPE_ACTIVITY_ITEM_SN="ID_WORK_TYPE_ACTIVITY_ITEM_SN";//
	
	
	
	
	public Map<String, Integer> idWorkMap=new HashMap<String, Integer>();
	public Date currentDate=new Date();
	SnowflakeIdWorker snowflakeIdWorker;
	
	
	void initOrUpdateIdWork(long workerId, long datacenterId) {
		synchronized (this) {
			this.snowflakeIdWorker=new SnowflakeIdWorker(workerId, datacenterId);
		}
	}
	
	/**
	 * 获取顺序变量
	 * @param idType key 存储
	 * @return
	 */
	public  int getIdWorkByIdType(String idType) {
	   Integer idTemp=idWorkMap.get(idType);
	   if(idTemp==null){
		   idTemp=1000;
	   }else{
		   if(DateUtils.isTodayDate(currentDate)){
			   idTemp=idTemp+1;
		   }else{
			   //重新设置日期 重置为1
			   currentDate=new Date();
			   idTemp=1000;
		   } 
	   }
	   idWorkMap.put(idType, idTemp);
		return idTemp;
	}
	/**
	 * 
	 * @param prefixStr
	 * @param idType
	 * @param endStr
	 * @return
	 */
	public String getId(String prefixStr,String idType,String endStr){
		return prefixStr+getIdWorkByIdType(idType)+endStr;
	}
	/**
	 * 
	 * @param prefixStr 前缀
	 * @param idType 自增值存储Key
	 * @param randomLength 末尾随机长度
	 * @return
	 */
	public String getIdEndRadom(String prefixStr,String idType,int randomLength){
		return prefixStr+getIdWorkByIdType(idType)+RandomUtil.getRandomByLength(randomLength);
	}
	/**
	 * 
	 * @param prefixStr 前缀
	 * @param idType 自增值存储Key
	 * @param randomLength 末尾随机长度
	 * @return
	 */
	public String getIdDayEndRadom(String prefixStr,String idType,int randomLength){
		return prefixStr+DateUtils.formatTodayDate()+getIdWorkByIdType(idType)+RandomUtil.getRandomByLength(randomLength);
	}
	/**
	 * 
	 * @param prefixStr 前缀
	 * @param idType 自增值存储Key
	 * @param randomLength 末尾随机长度
	 * @return
	 */
	public String getIdDayHHMMEndRadom(String prefixStr,String idType,int randomLength){
		return prefixStr+DateUtils.formatTodayTime()+getIdWorkByIdType(idType)+RandomUtil.getRandomByLength(randomLength);
	}
	
	public long getIdWorkRadom(){
		return snowflakeIdWorker.nextId();
	}
	public static void main(String[] args) {
		IdWorkCache idWorkCache=new IdWorkCache();
		System.out.println(idWorkCache.getIdDayHHMMEndRadom("PT", IdWorkCache.ID_WORK_TYPE_PAY_SN, 5));
		System.out.println(idWorkCache.getIdDayHHMMEndRadom("PT", IdWorkCache.ID_WORK_TYPE_PAY_SN, 5));
		System.out.println(idWorkCache.getIdDayHHMMEndRadom("PT", IdWorkCache.ID_WORK_TYPE_PAY_SN, 5).length());
		System.out.println(DateUtils.formatTodayTime());
		System.out.println(idWorkCache.getIdWorkRadom());
	}
	
}
