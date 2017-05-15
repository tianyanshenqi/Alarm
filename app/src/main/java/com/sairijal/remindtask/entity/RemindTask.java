package com.sairijal.remindtask.entity;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.Required;

/**
 * Created by sayujya on 2016-01-21.
 */
public class RemindTask extends RealmObject{
    private long mTime;
    private int authenticationType;
    private String state;
    private boolean repeatingMonday;
    private boolean repeatingTuesday;
    private boolean repeatingWednesday;
    private boolean repeatingThursday;
    private boolean repeatingFriday;
    private boolean repeatingSaturday;
    private boolean repeatingSunday;
    private String content;
    private String from;
    private String to;
    private String toName;
    private String fromName;
    private int remindType;

    public int getRemindType() {
        return remindType;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from.replace(" ","");
    }

    public String getTo() {
        return to;
    }


    /**
     * 设置接收人电话
     * @param mTo
     */
    public void setTo(String to) {
        this.to = to.replace(" ","");
    }

    @Required
    private String uniqueID;

    @Ignore
    public static final int DISTANCE = 0;
    @Ignore
    public static final int MATH = 1;
    @Ignore
    public static final int NONE = 2;
    @Ignore
    public static final String ON = "On";
    @Ignore
    public static final String OFF = "Off";
    @Ignore
    private static final String[] days = new String[]{"Su, M, T, W, Th, F, S"};
    @Ignore
    private static int snoozeTime = 10;

    public RemindTask(){ /*Required empty bean constructor*/ }

    public long getmTime() {
        return mTime;
    }

    public void setmTime(long mTime) {
        this.mTime = mTime;
    }

    public int getAuthenticationType() {
        return authenticationType;
    }

    public void setAuthenticationType(int authenticationType) {
        this.authenticationType = authenticationType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isRepeatingMonday() {
        return repeatingMonday;
    }

    public void setRepeatingMonday(boolean repeatingMonday) {
        this.repeatingMonday = repeatingMonday;
    }

    public boolean isRepeatingTuesday() {
        return repeatingTuesday;
    }

    public void setRepeatingTuesday(boolean repeatingTuesday) {
        this.repeatingTuesday = repeatingTuesday;
    }

    public boolean isRepeatingWednesday() {
        return repeatingWednesday;
    }

    public void setRepeatingWednesday(boolean repeatingWednesday) {
        this.repeatingWednesday = repeatingWednesday;
    }

    public boolean isRepeatingThursday() {
        return repeatingThursday;
    }

    public void setRepeatingThursday(boolean repeatingThursday) {
        this.repeatingThursday = repeatingThursday;
    }

    public boolean isRepeatingFriday() {
        return repeatingFriday;
    }

    public void setRepeatingFriday(boolean repeatingFriday) {
        this.repeatingFriday = repeatingFriday;
    }

    public boolean isRepeatingSaturday() {
        return repeatingSaturday;
    }

    public void setRepeatingSaturday(boolean repeatingSaturday) {
        this.repeatingSaturday = repeatingSaturday;
    }

    public boolean isRepeatingSunday() {
        return repeatingSunday;
    }

    public void setRepeatingSunday(boolean repeatingSunday) {
        this.repeatingSunday = repeatingSunday;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String mContent) {
        this.content = mContent;
    }

    public static String[] getDays() {
        return days;
    }

    public static int getSnoozeTime() {
        return snoozeTime;
    }

    public static void setSnoozeTime(int snoozeTime) {
        com.sairijal.remindtask.entity.RemindTask.snoozeTime = snoozeTime;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public void setRemindType(int remindType) {
        this.remindType = remindType;
    }
}
