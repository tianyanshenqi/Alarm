package com.sairijal.alarm.alarm;

import android.util.Log;

import com.sairijal.alarm.application.AlarmApplication;

/**
 * Created by 11255 on 2017/3/31.
 */

public class UserWrapperHolder {
    private static UserWrapper userWrapper;

    public UserWrapper getUserWrapper() {
        if (userWrapper == null){
            Log.e(AlarmApplication.APP_TAG, "userWrapper not found. Are you sure you set the alarm?");
            return null;
        }
        return userWrapper;

    }

    public static void setUserWrapper(UserWrapper userWrapper2){
        userWrapper = userWrapper2;
    }
}
