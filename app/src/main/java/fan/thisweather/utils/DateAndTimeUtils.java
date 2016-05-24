package fan.thisweather.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by fan on 2016/5/23.
 */
public class DateAndTimeUtils {

    public static int getYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public static int getMonth() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    public static int getCurrentMonthDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    public static int getWeekDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 返回当前时间的格式为 yyyy-MM-dd E
     * @return
     */
    public static String  getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd E");
        return sdf.format(System.currentTimeMillis());
    }

    /**
     * 返回当前时间的格式为 HH:mm
     * @return
     */
    public static String  getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(System.currentTimeMillis());
    }

}
