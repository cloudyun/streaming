package com.yands.streaming.create;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**  
 * @Title:  ConstantsUtil.java   
 * @Package com.posa.utils   
 * @Description:    (用一句话描述该文件做什么)   
 * @author: gaoyun     
 * @edit by: 
 * @date:   2018年4月2日 上午9:41:22   
 * @version V1.0 
 */ 
public class ConstantsUtil {

	public static SimpleDateFormat sdf = new SimpleDateFormat();

	// 完整时间格式化
	public static String sdffullTime = "yyyy-MM-dd HH:mm:ss";
	// Day时间格式化
	public static String sdfday = "yyyy-MM-dd";

	public static Random rdm;

	public static synchronized String getDateFormat(java.util.Date date, String pattern) {
		synchronized (sdf) {
			String str = null;
			sdf.applyPattern(pattern);
			str = sdf.format(date);
			return str;
		}
	}

	/**
	 * @Description : 生成连续key
	 * @param qz  前缀
	 * @param begin 开始数字
	 * @param count 个数
	 * @return
	 */
	public static String[] createContinueKey(String qz, int begin, int count, int wei) {
		String[] resultArr = new String[count];
		while (count > 0) {
			Integer temp = begin + count - 1;
			String tempString = temp.toString();
			while (tempString.length() < wei)
				tempString = "0" + tempString;
			resultArr[count - 1] = qz + tempString;
			count--;
		}
		return resultArr;
	}

	public static String[] createContinueKey(String qz, int begin, int count) {
		String[] resultArr = new String[count];
		while (count > 0) {
			Integer temp = begin + count - 1;
			String tempString = temp.toString();
			resultArr[count - 1] = qz + tempString;
			count--;
		}
		return resultArr;
	}

	public static void createRandom(int seed) {
		rdm = new Random(System.currentTimeMillis() * seed);
	}

	public static int getNumber(int num, int seed) {
		return Math.abs(rdm.nextInt()) % num;
	}

	public static int getNumber(int num) {
		return Math.abs(new Random().nextInt()) % num;
	}

	public static String getConvertID(int length, int seed) {
		String id = String.valueOf(System.currentTimeMillis());
		if (id.length() > length) {
			id = id.substring(0, 15);
		}
		int len = length - id.length();
		String tmp = "";
		for (int i = 1; i <= len; i++) {
			tmp += "0";
		}
		id = tmp + id;
		StringBuffer sb = new StringBuffer(id);
		return sb.reverse().toString() + String.format("%03d", ConstantsUtil.getNumber(1000, seed));
	}

	public static String getCaptureTime(Date stand, int days, int seed) {
		Calendar calendar = Calendar.getInstance();
		int randomNum = ConstantsUtil.getNumber(days, seed);
		
		calendar.setTime(stand);

		calendar.add(Calendar.DAY_OF_MONTH, -randomNum);
		calendar.add(Calendar.HOUR_OF_DAY, ConstantsUtil.getNumber(24, seed));
		calendar.set(Calendar.MINUTE, ConstantsUtil.getNumber(60, seed * 1));
		calendar.set(Calendar.SECOND, ConstantsUtil.getNumber(60, seed * 7));
		calendar.set(Calendar.MILLISECOND, 0);// 清空毫秒
		return String.valueOf(calendar.getTimeInMillis());
	}

	public static String getCaptureTime() {
		Calendar calendar = Calendar.getInstance();
		return String.valueOf(calendar.getTimeInMillis());
	}
}