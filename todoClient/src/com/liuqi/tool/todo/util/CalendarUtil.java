/**
 * 文   件  名：CalendarUtil.java
 * 作          者：刘奇
 * 创建日期：2014年8月4日
 * 版          本：1.0
 */
package com.liuqi.tool.todo.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * @作者： 刘奇
 * @时间：2014年8月4日
 *
 */
public class CalendarUtil {
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
	
	private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static final SimpleDateFormat WEEK_FORMAT = new SimpleDateFormat("yyyyMMdd E");

	/**
	 * 获取上一月月初第一天的字符串
	 * @return
	 */
	public static String getLastMonthStartDateStr() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.add(Calendar.MONTH, -1);

		return formatDateStr(c.getTime());
	}

	/**
	 * 获取包含星期的日期字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String formatWeekDateStr(Date date) {
		return WEEK_FORMAT.format(date); 
	}
	
	/**
	 * 获取某天是周几 
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static int getDayOfWeek(int year, int month, int day) {
		Calendar c = Calendar.getInstance();
		c.set(year, month, day);
		return c.get(Calendar.DAY_OF_WEEK);
	}
	
	/**
	 * 获取某天是周几
	 * 
	 * @return
	 */
	public static int getDayOfWeek(Date date) {
		Calendar c = Calendar.getInstance();
		
		c.setTime(date);
		
		return c.get(Calendar.DAY_OF_WEEK);
	}
	
	public static boolean isWeekend(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int i = c.get(Calendar.DAY_OF_WEEK);
		
		if (1 == i || 7 == i) {
			return true;
		}
		
		return false;
	}

	/**
	 * 获取当前周的第一天
	 *
	 * @return
	 */
	public static Calendar getNowWeekStartDay() {
		Calendar c = Calendar.getInstance();
		c.setFirstDayOfWeek(Calendar.SUNDAY);
		int days = c.get(Calendar.DAY_OF_WEEK);
		c.add(Calendar.DAY_OF_WEEK, -days + 1);
		return c;
	}

	/**
	 * 获取上月的第一天
	 *
	 * @return
	 */
	public static Calendar getLastMonthStartDay() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.add(Calendar.MONTH, -1);
		return c;
	}

	/**
	 * 获取上月第一天的字符串
	 *
	 * @return
	 */
	public static String getLastMonthStartDayStr() {
		return formatDateStr(getLastMonthStartDay().getTime());
	}

	/**
	 * 获取当前周第一天的字符串
	 *
	 * @return
	 */
	public static String getNowWeekStartDayStr() {
		return formatDateStr(getNowWeekStartDay().getTime());
	}

	/**
	 * 获取本月的第一天
	 * @return
	 */
	public static Calendar getNowMonthStartDay() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, 1);
		return c;
	}

	/**
	 * 获取本月第一天的字符串
	 *
	 * @return
	 */
	public static String getNowMonthStartDayStr() {
		return formatDateStr(getNowMonthStartDay().getTime());
	}

    /**
     * 判断日期是否是当天
     *
     * @param date
     * @return
     */
	public static boolean isNowDate(Date date) {
	    if (getNowDateStr().equals(DATE_FORMAT.format(date))) {
	        return true;
        } else {
	        return false;
        }
    }
	
	/**
	 * 获取某月的天数
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getDayCountOfMonth(int year, int month) {
		Calendar c = Calendar.getInstance();
		c.set(year, month, 1);
		return c.getMaximum(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * 格式化日期串
	 * 串格式：yyyy-MM-dd
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDateStr(Date date) {
		return DATE_FORMAT.format(date);
	}
	
	/**
	 * 格式化时间串
	 * 串格式：yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 * @return
	 */
	public static String formatTimeStr(Date date) {
		return TIME_FORMAT.format(date);
	}
	
	/**
	 * 获取当前日期字符串
	 * 
	 * @return
	 */
	public static String getNowDateStr() {
		return DATE_FORMAT.format(Calendar.getInstance().getTime());  
	}
	
	/**
	 * 从字符串中获取日期
	 * 串格式：yyyy-MM-dd
	 * 
	 * @param str
	 * @return
	 */
	public static Date parseDate(String str) {
		try {
			return DATE_FORMAT.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
			
			return null;
		}
	}
	
	/**
	 * 从字符串中获取时间　
	 * 串格式：yyyy-MM-dd HH:mm:ss
	 * 
	 * @param str
	 * @return
	 */
	public static Date parseTime(String str) {
		try {
			return TIME_FORMAT.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
			
			return null;
		}
	}

	public static String getNowTimeStr() {
		return formatTimeStr(Calendar.getInstance().getTime());
	}
}
