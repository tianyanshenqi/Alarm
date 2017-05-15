package com.sairijal.remindtask.utils;

import java.util.Calendar;

/**
 * Created by 11255 on 2017/4/21.
 */

public class TimeUtil {

    public static final int SUNDAY = 0;
    public static final int MONDAY = 1;
    public static final int TUEDAY = 2;
    public static final int WENDAY = 3;
    public static final int THRDAY = 4;
    public static final int FRIDAY = 5;
    public static final int STADAY = 6;

    public static int getDayOfWeek(){
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek-1;
    }
}
