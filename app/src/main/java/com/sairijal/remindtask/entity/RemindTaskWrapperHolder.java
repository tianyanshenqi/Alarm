package com.sairijal.remindtask.entity;

import android.util.Log;

import com.sairijal.remindtask.application.RemindTaskApplication;

/**
 * Created by sayujya on 2016-02-21.
 */
public class RemindTaskWrapperHolder {
    private static RemindTaskWrapper ourInstance;

    public static RemindTaskWrapper getInstance() {
        if (ourInstance == null){
            Log.e(RemindTaskApplication.APP_TAG, "RemindTask not found. Are you sure you set the alarm?");
            return null;
        } else {
            return ourInstance;
        }
    }

    public static void setInstance(RemindTaskWrapper instance) {
        ourInstance = instance;
    }
}
