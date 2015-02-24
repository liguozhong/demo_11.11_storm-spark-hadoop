package liguozhong.bigdata.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 日期相关操作工具类
 * 
 * @author 李国忠<br>
 * @version 创建时间：2015年2月24日 下午7:32:28
 */
public class DateUtils {

	public static final String YMDHMS = "yyyy-MM-dd HH:mm:ss";

	public static String date2String(String date, String patton) {
		SimpleDateFormat sdf = new SimpleDateFormat(patton);
		Calendar cal = Calendar.getInstance();
		if (date != null) {
			try {
				cal.setTime(sdf.parse(date));

			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return sdf.format(cal.getTime());
	}

}
