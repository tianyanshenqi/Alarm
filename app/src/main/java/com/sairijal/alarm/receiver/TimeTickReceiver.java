package com.sairijal.alarm.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

import com.sairijal.alarm.alarm.RemindTask;
import com.sairijal.alarm.mqtt.MqttPublishService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class TimeTickReceiver extends BroadcastReceiver {

    private SimpleDateFormat mWatchTime;
    private SimpleDateFormat mWatchDay;
    private Realm mRealm;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().compareTo(Intent.ACTION_TIME_TICK) == 0) {
            Toast.makeText(context, "received", Toast.LENGTH_SHORT).show();
            Log.d("received", "onReceive: ");

            if (DateFormat.is24HourFormat(context)) {
                mWatchTime = new SimpleDateFormat("HH:mm", Locale.getDefault());
            } else {
                mWatchTime = new SimpleDateFormat("hh:mm", Locale.getDefault());
            }
            mWatchDay = new SimpleDateFormat("EEEE", Locale.getDefault());
            matchRemind(context);
        }
        if (intent.getAction().equals("intent.action.MQTT_RECEIVER")){
            Toast.makeText(context, "matt", Toast.LENGTH_SHORT).show();
        }
    }

    private void matchRemind(final Context context) {
        if (mRealm == null) {
            mRealm = Realm.getDefaultInstance();
        }
        RealmQuery<RemindTask> query = mRealm.where(RemindTask.class);
        RealmResults<RemindTask> savedRemindTasks = query.findAll();
        /*List<RemindTaskWrapper> savedAlarmsList = new ArrayList<>();*/
        for (RemindTask remindTask : savedRemindTasks){
            Date currentDate = new Date();
            Date remindDate = new Date(remindTask.getmTime());
            MqttPublishService.getInstance(context).publish(remindTask.getTo(),remindTask.getContent(),2);

            if(remindTask.isRepeatingMonday()&&mWatchDay.format(remindDate).equals("星期一")){
                if (mWatchTime.format(currentDate).equals(mWatchTime.format(remindDate))){
                    Toast.makeText(context, "remind", Toast.LENGTH_SHORT).show();
                }
            }
            if(remindTask.isRepeatingTuesday()&&mWatchDay.format(remindDate).equals("星期二")){
                if (mWatchTime.format(currentDate).equals(mWatchTime.format(remindDate))){
                    Toast.makeText(context, "remind", Toast.LENGTH_SHORT).show();
                }
            }
            if(remindTask.isRepeatingWednesday()&&mWatchDay.format(remindDate).equals("星期三")){
                if (mWatchTime.format(currentDate).equals(mWatchTime.format(remindDate))){
                    Toast.makeText(context, "remind", Toast.LENGTH_SHORT).show();
                }
            }
            if(remindTask.isRepeatingThursday()&&mWatchDay.format(remindDate).equals("星期四")){
                if (mWatchTime.format(currentDate).equals(mWatchTime.format(remindDate))){
                    Toast.makeText(context, "remind", Toast.LENGTH_SHORT).show();
                }
            }
            if(remindTask.isRepeatingFriday()&&mWatchDay.format(remindDate).equals("星期五")){
                if (mWatchTime.format(currentDate).equals(mWatchTime.format(remindDate))){
                    Toast.makeText(context, "remind", Toast.LENGTH_SHORT).show();
                }
            }
            if(remindTask.isRepeatingSaturday()&&mWatchDay.format(remindDate).equals("星期六")){
                if (mWatchTime.format(currentDate).equals(mWatchTime.format(remindDate))){
                    Toast.makeText(context, "remind", Toast.LENGTH_SHORT).show();
                }
            }
            if(remindTask.isRepeatingSunday()&&mWatchDay.format(remindDate).equals("星期日")){
                if (mWatchTime.format(currentDate).equals(mWatchTime.format(remindDate))){
                    Toast.makeText(context, "remind", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
