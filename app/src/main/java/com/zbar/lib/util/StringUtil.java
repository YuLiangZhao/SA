package com.zbar.lib.util;

import android.util.Base64;

public class StringUtil {
	/**
	 * 判断字符串是否为空
	 * @param str 要判断的字符串
	 * @return
	 */
	public static boolean isEmpty(String str){
		if(str==null||str.trim().length()==0){
			return true;
		}
		else {
			return false;
		}
		
	}
	/**
	 * 处理url
	 * 如果不是以http://或者https://开头，就添加http://
	 * @param url 被处理的url
	 * @return
	 */
	public static String preUrl(String url){
		if(url==null){
			return null;
		}
		if(url.startsWith("http://")||url.startsWith("https://")){
			return url;
		}
		else{
			return "http://"+url;
		}
	}
	/**
	 * Base64加密
	 * @param str 要判断的字符串
	 * @return
	 */
	public static String Base64Encode(String str){
		String strBase64 = "";
		if(str==null||str.trim().length()==0){
			return "";
		}
		else {
			// 在这里使用的是encode方式，返回的是byte类型加密数据，可使用new String转为String类型
			strBase64 = new String(Base64.encode(str.getBytes(), Base64.DEFAULT));
			// 这里 encodeToString 则直接将返回String类型的加密数据
			//String strBase64 = Base64.encodeToString(str.getBytes(), Base64.DEFAULT);
			return strBase64;
		}

	}
	/**
	 * Base64解密
	 * @param str 要判断的字符串
	 * @return
	 */
	public static String Base64Decode(String str){
		String strBase64 = "";
		if(str==null||str.trim().length()==0){
			return "";
		}
		else {
			strBase64 = new String(Base64.decode(str.getBytes(), Base64.DEFAULT));
			return strBase64;
		}

	}
}
