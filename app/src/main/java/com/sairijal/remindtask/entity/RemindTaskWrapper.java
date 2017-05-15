package com.sairijal.remindtask.entity;

import junit.framework.Assert;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by sayujya on 2016-02-04.
 */
public class RemindTaskWrapper implements Comparable<RemindTaskWrapper> {
    private RemindTask remindTask;
    private static boolean is24Hours;

    public RemindTaskWrapper(RemindTask remindTask) {
        this.remindTask = remindTask;
    }

    @Override
    public int compareTo(RemindTaskWrapper another) {
        if (this.remindTask.getmTime()>another.remindTask.getmTime()){
            return 1;
        } else if (this.remindTask.getmTime()<another.remindTask.getmTime()) {
            return -1;
        } else {
            return 0;
        }
    }

    public RemindTask getRemindTask() {
        return remindTask;
    }

    public String[] getTime(){
        // set formatter depending on the system date
        SimpleDateFormat formatter;
        if (RemindTaskWrapper.is24Hours){
            formatter = new SimpleDateFormat("HH:mm ", Locale.getDefault());
        } else {
            formatter = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        }
        return formatter.format(remindTask.getmTime()).split(" ", -1);
    }

    public long getTimeLong(){
        return remindTask.getmTime();
    }

    public void setTime(long time) {
        this.remindTask.setmTime(time);
    }

    public int getAuthenticationType() {
        return this.remindTask.getAuthenticationType();
    }

    public void setAuthenticationType(int authenticationType) {
        this.remindTask.setAuthenticationType(authenticationType);
    }

    public String getState() {
        return this.remindTask.getState();
    }

    public void setState(String state) {
        this.remindTask.setState(state);
    }

    public boolean[] getRepeating() {
        boolean[] repeating = new boolean[7];
        repeating[0] = this.remindTask.isRepeatingMonday();
        repeating[1] = this.remindTask.isRepeatingTuesday();
        repeating[2] = this.remindTask.isRepeatingWednesday();
        repeating[3] = this.remindTask.isRepeatingThursday();
        repeating[4] = this.remindTask.isRepeatingFriday();
        repeating[5] = this.remindTask.isRepeatingSaturday();
        repeating[6] = this.remindTask.isRepeatingSunday();
        return repeating;
    }

    public void setRepeating(boolean[] repeating) {
        Assert.assertEquals(7, repeating.length);
        this.remindTask.setRepeatingMonday(repeating[0]);
        this.remindTask.setRepeatingTuesday(repeating[1]);
        this.remindTask.setRepeatingWednesday(repeating[2]);
        this.remindTask.setRepeatingThursday(repeating[3]);
        this.remindTask.setRepeatingFriday(repeating[4]);
        this.remindTask.setRepeatingSaturday(repeating[5]);
        this.remindTask.setRepeatingSunday(repeating[6]);
    }

    public String getContent() {
        return this.remindTask.getContent();
    }

    public void setLabel(String label) {
        this.remindTask.setContent(label);
    }

    public String getUniqueID() {
        return  this.remindTask.getUniqueID();
    }

    public void setUniqueID(String uniqueID) {
        this.remindTask.setUniqueID(uniqueID);
    }

    public void removeFromRealm(){
        this.remindTask.removeFromRealm();
    }

    public static String[] getDays() {
        return com.sairijal.remindtask.entity.RemindTask.getDays();
    }

    public static int getSnoozeTime() {
        return com.sairijal.remindtask.entity.RemindTask.getSnoozeTime();
    }

    public static void setSnoozeTime(int snoozeTime) {
        com.sairijal.remindtask.entity.RemindTask.setSnoozeTime(snoozeTime);
    }

    public static boolean is24Hours() {
        return is24Hours;
    }

    public static void setIs24Hours(boolean is24Hours) {
        RemindTaskWrapper.is24Hours = is24Hours;
    }

    public String getUserName() {
        return this.remindTask.getToName();
    }

    public String getUserPhone() {
        return this.remindTask.getTo();
    }
}
