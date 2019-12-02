package com.platform.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * 
 * 随机数工具类
 *
 */
public class RandomUtil {
	
	public static List<Integer> randomSerial(int limit) {
		List<Integer> list = new ArrayList<Integer>(limit);
	    for (int ix = 0; ix < limit; ++ix){
	        list.add(ix);
	    }
	    Collections.shuffle(list, new Random());
		return list;
	}
	public static Integer randomSerial(int min,int max) {
		 Random random = new Random();
		int s = random.nextInt(max)%(max-min+1) + min;
		return s;
	}
	public static final String getRandomByLength(int length) {
		if (0 == length) {
			return "";
		}
		String uuid = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();

		int realLength = uuid.length();
		if (realLength >= length) {
			return uuid.substring(0, length);
		}
		String tempString = "";
		String randomString = uuid;
		while (realLength < length) {
			tempString = "" + Math.random();
			randomString += tempString.substring(2);
			realLength = randomString.length();
		}
		return uuid.substring(0, length);
	}
	
	
	public static long getRandom(){
		long result = 0;
		Random random=new Random();
		result=Math.abs(random.nextLong());
		return result;
	}
	public static long getRandom(long max){
		long result = 0;
		Random random=new Random();
		result=Math.abs(random.nextLong())%(max+1);
		return result;
	}
	public static int getRandom(int start,int end){
		int result = 0;
		Random random=new Random();
		result=Math.abs(random.nextInt())%(end-start+1)+start;
		return result; 
	}
	public static long getRandom(long start,long end){
		long result = 0;
		Random random=new Random();
		result=Math.abs(random.nextLong())%(end-start+1)+start;
		return result; 
	}
	
	public static void main(String[] args) {
		System.out.println(RandomUtil.randomSerial(0,10));
		System.out.println(RandomUtil.getRandomByLength(20));
		System.out.println(RandomUtil.getRandom(60,70));
	}

}
