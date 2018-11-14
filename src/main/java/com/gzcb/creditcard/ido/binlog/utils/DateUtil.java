package com.gzcb.creditcard.ido.binlog.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	
	/**
	 * 日期时间样式：yyyyMMddHHmmss
	 */
	public static final SimpleDateFormat DATE_YYYYMMDDHHMMSS = new SimpleDateFormat("yyyyMMddHHmmss");
	/**
	 * 日期时间样式：yyyy-MM-dd HH:mm:ss
	 */
	public static final SimpleDateFormat DATE_YYYYMMDDHHMMSS_2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 日期时间样式：yyyy/MM/dd HH:mm:ss
	 */
	public static final SimpleDateFormat DATE_YYYYMMDDHHMMSS_3 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	/**
	 * 日期时间样式：yyyy-M-d H:mm:ss
	 */
	public static final SimpleDateFormat DATE_YYYYMDHMMSS = new SimpleDateFormat("yyyy-M-d H:mm:ss");
	/**
	 * 日期时间样式：yyyy-MM-dd HH:mm
	 */
	public static final SimpleDateFormat DATE_YYYYMMDDHHMM = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	/**
	 * 日期时间样式：yyyy-M-d H:mm
	 */
	public static final SimpleDateFormat DATE_YYYYMDHMM = new SimpleDateFormat("yyyy-M-d H:mm");
	/**
	 * 日期时间样式：yyyy-MM-dd
	 */
	public static final SimpleDateFormat DATE_YYYYMMDD = new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * 日期时间样式：yyyyMMdd
	 */
	public static final SimpleDateFormat DATE_YYYYMMDD_2 = new SimpleDateFormat("yyyyMMdd");
	/**
	 * 日期时间样式：yyMMdd
	 */
	public static final SimpleDateFormat DATE_YYMMDD = new SimpleDateFormat("yyMMdd");
	/**
	 * 日期时间样式：yyyy-M-d
	 */
	public static final SimpleDateFormat DATE_YYYYMD = new SimpleDateFormat("yyyy-M-d");
	/**
	 * 日期时间样式：MM-dd HH:mm
	 */
	public static final SimpleDateFormat DATE_MMDDHHMM = new SimpleDateFormat("MM-dd HH:mm");
	/**
	 * 日期时间样式：MM-dd HH:mm:ss
	 */
	public static final SimpleDateFormat DATE_MMDDHHMMSS = new SimpleDateFormat("MM-dd HH:mm:ss");
	/**
	 * 日期时间样式：MM-dd
	 */
	public static final SimpleDateFormat DATE_MMDD = new SimpleDateFormat("MM-dd");
	/**
	 * 日期时间样式：M月d日
	 */
	public static final SimpleDateFormat DATE_MD = new SimpleDateFormat("M月d日");
	/**
	 * 日期时间样式：M月
	 */
	public static final SimpleDateFormat DATE_M = new SimpleDateFormat("M月");
	/**
	 * 日期时间样式：MM
	 */
	public static final SimpleDateFormat DATE_MM = new SimpleDateFormat("MM");
	/**
	 * 日期时间样式：M月第w周
	 */
	public static final SimpleDateFormat DATE_MW = new SimpleDateFormat("M月第W周");
	/**
	 * 日期时间样式：MMW
	 */
	public static final SimpleDateFormat DATE_MMW = new SimpleDateFormat("MMW");
	/**
	 * 日期时间样式：M-d H:mm
	 */
	public static final SimpleDateFormat DATE_MDHMM = new SimpleDateFormat("M-d H:mm");
	/**
	 * 日期时间样式：HH:mm
	 */
	public static final SimpleDateFormat DATE_HHMM = new SimpleDateFormat("HH:mm");
	/**
	 * 日期时间样式：HH:mm:ss
	 */
	public static final SimpleDateFormat DATE_HHMMSS = new SimpleDateFormat("HH:mm:ss");

	
	/**
	 * 格式日期格式  如将�?0100803120223 格式化成�?010-08-03 12:02:23
	 * @param dateStr 输入的日期格�?20100803120223
	 * @return
	 */
	public static String formartDateStr(String dateStr){
		String format = "";
		if (dateStr==null && "".equals(dateStr)) {
			format = "&nbsp;";
		}else {
			if (dateStr.length()==14) {
				try {
					DateUtil.DATE_YYYYMMDDHHMMSS_2.parse(dateStr);
				} catch (Exception e) {
					e.printStackTrace();
				}
//				format = dateStr.substring(0,4) + "-";
//				format += dateStr.substring(4,6) + "-";
//				format += dateStr.substring(6,8) + " ";
//				format += dateStr.substring(8,10) + ":";
//				format += dateStr.substring(10,12) + ":";
//				format += dateStr.substring(12,14);
			}else if (dateStr.length()==8) {
				try {
					DateUtil.DATE_YYYYMMDD.parse(dateStr);
				} catch (Exception e) {
					e.printStackTrace();
				}
//				format = dateStr.substring(0,3) + "-";
//				format += dateStr.substring(4,5) + "-";
//				format += dateStr.substring(6,7);
//			}else {
			}
			if ("".equals(format)) {
				format = dateStr;
			}
		}
		return format;
	}
	
	/**
	 * 获取当时间的格式化输�?
	 * @param formatString 日期格式
	 */
	public static String getCurrentDate(String formatString){
		SimpleDateFormat format = new SimpleDateFormat(formatString);
		return format.format(new Date());
	}
	
	/**
	 * 获取当时间的格式化输�?
	 */
	public static String getCurrentDate(){
		return getCurrentDate("yyyy-MM-dd HH:mm:ss");
	}
	/**
	 * 将数据库中所保存的日期格式转化为正常的日期格式进行显�?
	 * @param dateStr 数据库中�?��存的日期格式
	 * @return 正常的日期格�?
	 */
	public static String converteDate(String dateStr){
		return converteDate(dateStr,"yyyy-MM-dd HH:mm:ss");
	}
	
	/**
	 * 将数据库中所保存的日期格式转化为正常的日期格式进行显�?
	 * @param dateStr 数据库中�?��存的日期格式
	 * @return 正常的日期格�?
	 */
	public static String converteDate(String dateStr, String formatString){
		if(dateStr==null || "".equals(dateStr.trim())){
			return "&nbsp;";
		}
		try{
			Date eventDate = new Date();
			long timeLong = Long.valueOf(dateStr).longValue();
			if (dateStr.length()<13) {
				timeLong = Long.valueOf(dateStr).longValue()*1000;
			}
			eventDate.setTime(timeLong);
			SimpleDateFormat format = new SimpleDateFormat(formatString);
			return format.format(eventDate);
		}catch (Exception e) {
			return dateStr;
		}
	}
	

	/**
	 * @param dateStr String 日期字符�?
	 * @param formatString String 日期的格式�?例如：yyyy-MM-dd HH:mm:ss
	 * @return 根据传入的字符串获得相对应的日期对象 
	 * */
	public static Date getDate(String dateStr, String formatString) {
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(formatString);
			date = sdf.parse(dateStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}	
	/**
	 * @param dateStr String 日期字符�?
	 * @return根据传入的字符串获得相对应的日期对象.这里指定的日期格式是 yyyy-MM-dd HH:mm:ss
	 * */	
	public static Date getDate(String dateStr) {
		return getDate(dateStr,"yyyy-MM-dd HH:mm:ss");
	} 

	/**
	 * 	注意到这里实际上用了java的getTime方法�?获得long以后除以1000. 因为数据库的中的时间只精确到秒�?而java方法精确到了毫秒�?
	 * */
	public static long getTimeX(String dateStr){
		long time = 0;
		time = getDate(dateStr).getTime()/1000;
		return time;
	}
	public static long getTimeX(String dateStr, String format){
		long time = 0;
		time = getDate(dateStr,format).getTime()/1000;
		return time;
	}	
	public static long getTimeX(Date date){
		long time = 0;
		time =date.getTime()/1000;
		return time;
	}
	/**
	 */
	public static long getTime(String dateStr){
		long time = -1;
		String format="yyyy-MM-dd HH:mm:ss";
		Date date=getDate(dateStr,format);
		if(date!=null){
			time = getDate(dateStr,format).getTime();
		}
		return time;
	}	
	
	/**
	 *
	 * */
	public static String getPartitionName(String dateStr, String protocol){
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		//P20090731080000_1249002000
		String partitionName = "P";
		
		Date date = getDate(dateStr);
		//date.setHours(date.getHours()+1);
		date.setMinutes(0);
		date.setSeconds(0);
		partitionName = partitionName + DateUtil.DATE_YYYYMMDDHHMMSS.format(date);
//		partitionName = partitionName + sdf.format(date);
		date.setHours(date.getHours()+1);
		partitionName = partitionName + "_" + date.getTime()/1000;
		return partitionName;
	}
	
	/**
	 * @author 刘伯�? 2009-7-31
	 *
	 * */
	public static String getPartitionName(long dateTime, String protocol){
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		//P20090731080000_1249002000
		String partitionName = "P";
		Date date = new Date();
		date.setTime(dateTime*1000);
		//date.setHours(date.getHours()+1);
		date.setMinutes(0);
		date.setSeconds(0);
//		partitionName = partitionName + sdf.format(date);
		partitionName = partitionName + DateUtil.DATE_YYYYMMDDHHMMSS.format(date);
		date.setHours(date.getHours()+1);
		partitionName = partitionName + "_" + date.getTime()/1000;
		return partitionName;
	}
	
	/*
	 * 将格式为"yyyy-MM-dd HH:mm:ss"的日期数据转化为长整型，以解决Long.parseLong()方法的有问题
	 * @param str
	 * @return
	 */
	public static long converteDataStr2Long(String str){
		Date date = new Date();

		String temp = str.substring(0,4);
		date.setYear(Integer.valueOf(temp).intValue()-1900);
		temp = str.substring(5,7);
		date.setMonth(Integer.valueOf(temp).intValue()-1);
		temp = str.substring(8,10);
		date.setDate(Integer.valueOf(temp).intValue());
		temp = str.substring(11,13);
		date.setHours(Integer.valueOf(temp).intValue());
		temp = str.substring(14,16);
		date.setMinutes(Integer.valueOf(temp).intValue());
		temp = str.substring(17,19);
		date.setSeconds(Integer.valueOf(temp).intValue());
		return date.getTime()/1000;
	}
	
	/**
	 * 将Date对象格式化输�?
	 * @param date Date对象
	 * @param formatStr 格式
	 * @return
	 */
	public static String formatDate(Date date, String formatStr){
		String dateStr = "";
		if (date!=null) {
			if (formatStr == null || "".equals(formatStr.trim())) {
				formatStr = "yyyy-MM-dd HH:mm:ss";
			}
			SimpleDateFormat format = new SimpleDateFormat(formatStr);
			dateStr = format.format(date);
		}
		return dateStr;
	}
	
	/**
	 * 把date转换为String
	 * @return
	 */	
	public static String formatDateToString(Date date, String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	/**
	 * 将指定日期按照yyyy-MM-dd HH:mm:ss格式输出
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date){
		return formatDate(date,"");
	}
	/**
	 * 将Date对象格式化输�?
	 * @param date Date对象
	 * @param format 格式
	 * @return
	 */
	public static String formatDate(Date date, SimpleDateFormat format){
		String dateStr = "";
		if (date!=null) {
			if (format == null ) {
				format = DateUtil.DATE_YYYYMMDDHHMMSS_2;
			}			
			dateStr = format.format(date);
		}
		return dateStr;
	}	
	/**
	 * 获取两个时间之间的间隔，不同的type返回不同的间隔单位，
	 * d-表示相隔多少天，h-表示相隔多少个小时，"m"-表示相隔多少分钟,s-表示相隔多少�?
	 * @param beginTime
	 * @param endTime
	 * @param type
	 * @return
	 */
	public static long  getTimeBetween(String beginTime, String endTime, String type){
		long begin=getDate(beginTime).getTime();//�?��时间毫秒�?
		long end=getDate(endTime).getTime();//结束时间毫秒�?
		long dis=end-begin;
		long ret=0;
		//根据不同的传入类型计算间�?
		if("d".equals(type)||type==null){//�?
			ret= Math.round(dis/(24*60*60*1000));
		}else if("h".equals(type)){
			ret= Math.round(dis/(60*60*1000));
		}else if("m".equals(type)){
			ret= Math.round(dis/(60*1000));
		}else if("s".equals(type)){
			ret= Math.round(dis/1000);
		}
		return ret;
	}
	/**
	 * 获取当前日期的前N小时的日�?
	 * @param hour
	 */
	public final  static String getDateBefore(int hour){
		Calendar c = Calendar.getInstance();
		c.add(Calendar.HOUR_OF_DAY, hour);
		String strDate=DateUtil.formatDate(c.getTime(), "yyyy-MM-dd HH:mm:ss");
		return strDate;
	} 
	
	/**
	 * 获取当前日期的前N小时的日�?
	 * @param hour
	 * @param formatStr
	 */
	public final  static String getDateBefore(int hour, String formatStr){
		Calendar c = Calendar.getInstance();
		c.add(Calendar.HOUR_OF_DAY, hour);
		String strDate=DateUtil.formatDate(c.getTime(), formatStr);
		return strDate;
	}
	
	public final static String formatDate(String dateStr, String formatStrIn, String formatStrOut){
		try{
			SimpleDateFormat sdfIn = new SimpleDateFormat(formatStrIn);
			Date date = sdfIn.parse(dateStr);
			SimpleDateFormat sdfOut = new SimpleDateFormat(formatStrOut);
			dateStr = sdfOut.format(date);
		}catch(Exception e){
			e.printStackTrace();
		}
		return dateStr;
		
	}
	
	
	/**
	 * @desc 在个位数的月和日前加�?
	 * @since 2015-3-25 
	 * @param str
	 * @return
	 */
	public static String insert0(String str) {
		String ret = str;
		try {
			if(Integer.parseInt(str) < 10) {
				str = "0" + Integer.parseInt(str);
				ret = str;
			}
		} catch (Exception e) {
			// TODO: handle exception
			ret = str;
		}
		return ret;
	}
	
	public static void main(String[] args) {
		//System.out.println(converteDate(""+(getTimeX("2012-07-01 10:05:00")-24*3600)));
		/*
		String key = "12345627";
		key = key.replaceAll("2", "0");
		System.out.println(key);
		*/
//		System.out.println(getTimeX("2010-06-29 12:01:00"));		
//		System.out.println(getTimeX("2010-03-29 03:00:00"));		
//		System.out.println(getTimeX("2010-03-29 23:59:59"));
//		System.out.println(getTimeX("2010-03-30 00:00:00"));
//		System.out.println(getTimeX("2010-03-29 00:00:00") + 60*60*24);
//		Calendar ca =  Calendar.getInstance();
//		ca.add(Calendar.DATE, -1);
//		Date date=ca.getTime();
		//System.out.println(getDate("2010-06-29 16:00:00","yyyy-mm-dd hh:mm:ss"));
		//System.out.println(TimeZone.getDefault());
//		String dateStr = "20100803120223";
//		System.out.println(DateUtil.formartDateStr(dateStr));
		//System.out.println(getPartitionName(1249349731,"QQ"));
	//	System.out.println(getDateBefore(24,"yyyy-MM-dd HH:mm:ss","2014-02-10 19:35:18"));
		//System.out.println(converteDate("1249349731"));
		//System.out.println(getTimeX("2012-03-16 00:00:00"));
		//long dis=getTimeBetween("2010-11-29 12:30:00","2010-11-29 12:44:50","m");
		//Date startTime=DateUtil.getDate("2010-11-29 12:30:00","yyyy-MM-dd");
//		System.out.println(formatDate("2011-03-18","yyyy-MM-dd","yyyyMMdd"));
//		//System.out.println(getDateBefore(-24));
//		System.out.println(getTimeX("2011-03-23 08:00:00"));
//		System.out.println(getTimeX("2011-03-23 09:00:00"));
		
		System.out.println(converteDate("1249349731"));
		
		System.out.println(converteDataStr2Long(getCurrentDate()));
	}
}
