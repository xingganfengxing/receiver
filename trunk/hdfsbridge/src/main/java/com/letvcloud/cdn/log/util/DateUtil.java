package com.letvcloud.cdn.log.util;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author liufeng1
 */
public class DateUtil {

	private static Logger LOG = LoggerFactory.getLogger(DateUtil.class);

	/**
	 * 获取每interval分钟间隔的文件后缀
	 * 
	 * @param date
	 * @return
	 */
	public static String getTimeSuffix(Date date,int interval,String format) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int minute = (cal.get(Calendar.MINUTE) / interval + 1) * interval;
		if(cal.get(Calendar.MINUTE) % interval == 0){
			minute = (cal.get(Calendar.MINUTE) / interval) * interval;
		}
		cal.set(Calendar.MINUTE, minute);
		return new SimpleDateFormat(format).format(cal.getTime());
	}

	/**
	 * 将制定格式的日子串转换为目标格式的日期串
	 * @param srcStr            源串
	 * @param srcPattern        原格式
	 * @param desPattern        目标格式
	 * @return
	 */
	public static String toPattern(String srcStr,String srcPattern,
								   String desPattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(srcPattern, Locale.ENGLISH);
		Date date = null;
		try {
			date = sdf.parse(srcStr);
		} catch (ParseException e) {
			LOG.error(e.getMessage());
		}
		DateTime dateTime = new DateTime(date);
        return dateTime.toString(desPattern);
	}

	/**
	 * 获取昨天的指定格式输出 字符串
	 * @param pattern
	 * @return
	 */
	public static String getYesterDay(String pattern){
		return DateTime.now().minusDays(1).toString(pattern);
	}
}
