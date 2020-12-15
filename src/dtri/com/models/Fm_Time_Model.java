package dtri.com.models;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Fm_Time_Model {
	
	private static SimpleDateFormat format_yyyyMMdd_HHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat format_yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
	/**格式:yyyy-MM-dd HH:mm:ss**/
	public static String to_yMd_Hms(Date date) {
		return format_yyyyMMdd_HHmmss.format(date);
	}
	/**格式:yyyyMMdd**/
	public static String to_yMd(Date date) {
		return format_yyyyMMdd.format(date);
	}
	/**計算:加/減 天數**/
	public static Date to_count(Integer n,Date dt) {
		Calendar c = Calendar.getInstance(); 
		c.setTime(dt); 
		c.add(Calendar.DATE, n);
		dt = c.getTime();
		return dt;
	}
}
