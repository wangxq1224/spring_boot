package com.demo.sboot.util;


/**
 * 
 * @ClassName: StringUtils 
 *
 * @Description: 字符串工具类
 *
 * @author wangxq 
 * @date 2017年7月14日 下午3:09:13 
 *
 */
public class StringUtil {

	/**
	 * 验证字符串是否为空
	 * 
	 * @param s
	 *            待验证的字符串
	 * @return 为空返回true，不为空返回false
	 */
	public static boolean isEmpty(String s) {
		return s == null || s.equals("") || s.equals("null");
	}

	/**
	 * 验证字符串是否非空
	 * 
	 * @param s
	 *            待验证的字符串
	 * @return 不为空返回true，为空返回false
	 */
	public static boolean isNotEmpty(String s) {
		return s != null && !s.equals("") && !s.equals("null");
	}

	/**
	 * 使用指定字符分割字符串
	 * 
	 * @param s
	 *            待分割的字符串
	 * @param split
	 *            分隔符
	 * @return 分割后的数组
	 */
	public static String[] splitString(String s, String split) {
		if (isEmpty(s)) {
			return null;
		}
		return s.split(split);
	}
	
	
}
