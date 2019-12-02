package com.platform.codegen.utils;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**  
 * @ClassName: UrlUtils
 * @Description: 验证KR的输入词是否为URL
 * @date 2011-1-5 上午11:50:25
 * 
 */
public class UrlUtils {
	public static final String[] URL_GLOBAL_SUFFIX = new String[] { "aero",
		"asia", "biz", "cat", "com", "coop", "edu", "gov", "info", "int",
		"jobs", "mil", "mobi", "museum", "name", "net", "org", "pro",
		"tel", "travel" };

	public static final String[] URL_COUNTRY_SUFFIX = new String[] { "ac",
		"ad", "ae", "af", "ag", "ai", "al", "am", "an", "ao", "aq", "ar",
		"as", "at", "au", "aw", "ax", "az", "ba", "bb", "bd", "be", "bf",
		"bg", "bh", "bi", "bj", "bm", "bn", "bo", "br", "bs", "bt", "bv",
		"bw", "by", "bz", "ca", "cc", "cd", "cf", "cg", "ch", "ci", "ck",
		"cl", "cm", "cn", "co", "cr", "cu", "cv", "cx", "cy", "cz", "de",
		"dj", "dk", "dm", "do", "dz", "ec", "ee", "eg", "er", "es", "et",
		"eu", "fi", "fj", "fk", "fm", "fo", "fr", "ga", "gb", "gd", "ge",
		"gf", "gg", "gh", "gi", "gl", "gm", "gn", "gp", "gq", "gr", "gs",
		"gt", "gu", "gw", "gy", "hk", "hm", "hn", "hr", "ht", "hu", "id",
		"ie", "il", "im", "in", "io", "iq", "ir", "is", "it", "je", "jm",
		"jo", "jp", "ke", "kg", "kh", "ki", "km", "kn", "kp", "kr", "kw",
		"ky", "kz", "la", "lb", "lc", "li", "lk", "lr", "ls", "lt", "lu",
		"lv", "ly", "ma", "mc", "md", "me", "mg", "mh", "mk", "ml", "mm",
		"mn", "mo", "mp", "mq", "mr", "ms", "mt", "mu", "mv", "mw", "mx",
		"my", "mz", "na", "nc", "ne", "nf", "ng", "ni", "nl", "no", "np",
		"nr", "nu", "nz", "om", "pa", "pe", "pf", "pg", "ph", "pk", "pl",
		"pm", "pn", "pr", "ps", "pt", "pw", "py", "qa", "re", "ro", "rs",
		"ru", "rw", "sa", "sb", "sc", "sd", "se", "sg", "sh", "si", "sj",
		"sk", "sl", "sm", "sn", "so", "sr", "st", "su", "sv", "sy", "sz",
		"tc", "td", "tf", "tg", "th", "tj", "tk", "tl", "tm", "tn", "to",
		"tp", "tr", "tt", "tv", "tw", "tz", "ua", "ug", "uk", "us", "uy",
		"uz", "va", "vc", "ve", "vg", "vi", "vn", "vu", "wf", "ws", "ye",
		"yt", "yu", "za", "zm", "zw" };


	public static final String[] URL_KNOWN_LEVEL2_SUFFIX = new String[] {
		"ac.cn", "ac.il", "ac.kr", "ac.nz", "ac.uk", "ad.jp", "ah.cn",
		"bj.cn", "busan.kr", "chungbuk.kr", "chungnam.kr", "club.tw",
		"co.il", "co.jp", "co.kr", "co.nz", "co.uk", "com.cn", "com.hk",
		"com.tw", "cq.cn", "cri.nz", "daegu.kr", "daejeon.kr", "ebiz.tw",
		"ed.jp", "edu.cn", "edu.hk", "edu.tw", "es.kr", "fj.cn", "game.tw",
		"gangwon.kr", "gd.cn", "geek.nz", "gen.nz", "go.jp", "go.kr",
		"gov.cn", "gov.hk", "gov.il", "gov.tw", "gov.uk", "govt.nz",
		"gr.jp", "gs.cn", "gwangju.kr", "gx.cn", "gyeongbuk.kr",
		"gyeonggi.kr", "gyeongnam.kr", "gz.cn", "ha.cn", "hb.cn", "he.cn",
		"hi.cn", "hl.cn", "hn.cn", "hs.kr", "idf.il", "idv.hk", "idv.tw",
		"incheon.kr", "iwi.nz", "jeju.kr", "jeonbuk.kr", "jeonnam.kr",
		"jl.cn", "js.cn", "jx.cn", "k12.il", "kg.kr", "lg.jp", "ln.cn",
		"ltd.uk", "maori.nz", "me.uk", "mil.kr", "mil.nz", "mil.tw",
		"mod.uk", "ms.kr", "muni.il", "ne.jp", "ne.kr", "net.cn", "net.hk",
		"net.il", "net.nz", "net.tw", "net.uk", "nhs.uk", "nic.uk",
		"nm.cn", "nx.cn", "or.jp", "or.kr", "org.cn", "org.hk", "org.il",
		"org.nz", "org.tw", "org.uk", "parliament.nz", "parliament.uk",
		"pe.kr", "plc.uk", "qh.cn", "re.kr", "sc.cn", "sc.kr", "sch.uk",
		"sd.cn", "seoul.kr", "sh.cn", "sn.cn", "sx.cn", "tj.cn", "tw.cn",
		"ulsan.kr", "xj.cn", "xz.cn", "yn.cn", "zj.cn" };

	public static final String[] URL_KNOWN_2ND_SUFFIX = new String[] { "co",
		"com", "edu", "gov", "net", "org" };


	/** 以http开头，不含有汉字,判断为url */
	private static final Pattern URL_PATTERN1 = Pattern.compile(
			"^http[s]?[\\u0000-\\u00ff]+$");
	
	/** 截取主域地址(RFC & COM 组建通用) */
	private static final Pattern DOMAIN_PATTERN = Pattern.compile(
			"^(http[s]?(:+\\/*|\\/+:*))?((([\\w-]*\\w)\\.)+[\\w-]+)(:\\d+)?((\\/|\\?|#).*)?$");
	
	/** ipv4地址的判断 */
	private static final Pattern IP_PATTERN = Pattern.compile(
			"((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)");

	/**
	 * 验证输入内容是否为URL
	 * @param input
	 * @return
	 */
	public static boolean isUrl(final String urlAddress){
		if (urlAddress == null || urlAddress.trim().length()<1) {
			return false;
		}
		String tmpUrl = urlAddress;
		tmpUrl = tmpUrl.replace('\\','/').trim().toLowerCase();
		String domain = null;
		Matcher url1Matcher = URL_PATTERN1.matcher(tmpUrl);
		if(url1Matcher.find()){
			return true;
		}
		
		Matcher domainMatcher = DOMAIN_PATTERN.matcher(tmpUrl);
		//step1:* 第一个字符是否在规定字符集,是否为汉字（由正则完成）
		//step4:* 包含至少一个英文浮点号"."（由正则完成）
		//step5:* 第一位不允许特殊字符.:-（由正则完成）
		//step6:* 主域的所有字符都在规定字符集中（由正则完成）
		if(domainMatcher.find()){
			//step2: 提取主域 ()
			domain = domainMatcher.group(3);
		}
		if(domain == null || domain.trim().length() == 0){
			return false;
		}
		
		// 判断主域是否为ipv4地址
		Matcher ipMatcher = IP_PATTERN.matcher(domain.toLowerCase());
		if(ipMatcher.matches()){
			return true;
		}
		
		//按COM组规则截取主域,
		//step3: 去除所有空格
		//step7:* 顶级域名为国际域名 ->URL
		//step8:* 顶级域名是国家域名，且二级域名不是国家域名 --> URL
		//step9:* 如顶级域名为国家域名，且二级域名也是国家域名，且主域中包含英文符号点”.”个数大于等于2，则为URL，如小于2则为关键词。
		//step10:* 否则为关键词
		String mainDomain = getMainDomainComStandard(domain.trim());
		
		//如果还截取不到，就是非常特殊的情况的，需要打WARNING日志
		if(mainDomain == null){
			return false;
		} else {
			return true;
		}
	}
	public static void main(String[] args) {
		System.out.println(UrlUtils.isUrl("https://www.baidu.com"));
	}
	
	/**
	 * COM组的主域切割规则。如果不符合规则，则返回NULL
	 * 
	 * @param domain
	 * @return null - 不满足COM组主域切割规则
	 */
	private static String getMainDomainComStandard(final String domain){
		
		if(domain == null){
			return null;
		}
		
		String[] digits = domain.split("\\.");
		int length = digits.length;
		
		//如果按点分隔后不足两位，则返回错误
		if(length < 2){
			return null;
		}
		
		String lastDigit = digits[length-1];
		String last2Digits = digits[length-2]+"."+digits[length-1];
				
		//如果以国际通用域名结尾，则将倒数第二节开始的部分作为域名
		if(Arrays.binarySearch(URL_GLOBAL_SUFFIX, lastDigit) >= 0){
			return last2Digits;
		}
		
		//如果不是以国家域名结尾的，则返回错误
		if(Arrays.binarySearch(URL_COUNTRY_SUFFIX, lastDigit) < 0){
			return null;
		}
		
		//如果其国家二级域名是我们所熟知的，则返回倒数第三节开始的部分作为域名
		if(Arrays.binarySearch(URL_KNOWN_LEVEL2_SUFFIX, last2Digits) >= 0 || 
				Arrays.binarySearch(URL_KNOWN_2ND_SUFFIX, digits[length-2]) >= 0){
			
			if(length < 3){
				return last2Digits; //如果只有两位，则返回错误
			}
			return digits[length-3]+"."+last2Digits;
		}
		//否则，返回最后两位
		return last2Digits;
	}

}
