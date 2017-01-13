package com.iscas.quickframe.util;

import com.alibaba.druid.filter.config.ConfigTools;

/**
*@auhor:zhuquanwen
*@date:2016年12月20日
*@desc:目前功能主要是加密数据库
*/
public class DruidUtil {
//	public static void main(String[] args) {
//		 String password = "root";
//		 try {
//			System.out.println(encrypt(password));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	     
//	}
	/** 
	 * @Title: 加密字符串 
	 * @Description: 加密字符串，使用公钥为null的方式加密
	 * @param pwd 要加密的字符串
	 * @return String
	 * @throws Exception
	 */
	public static String encrypt(String pwd) throws Exception{
		 return ConfigTools.encrypt(pwd);
	    
	}
}
