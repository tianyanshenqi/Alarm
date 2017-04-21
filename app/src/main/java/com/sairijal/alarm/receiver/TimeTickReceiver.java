package com.sairijal.alarm.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

import com.sairijal.alarm.R;
import com.sairijal.alarm.alarm.Message;
import com.sairijal.alarm.alarm.RemindTask;
import com.sairijal.alarm.mqtt.MqttPublishService;
import com.sairijal.alarm.utils.TimeUtil;

import org.json.JSONException;
import org.json.JSONObject;

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
            receive(intent,context);
        }
    }

    private void receive(Intent intent, Context context) {
        if (mRealm == null) {
            mRealm = Realm.getDefaultInstance();
        }
        try {
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("msg"));
            Message message = new Message();
            message.setFrom(jsonObject.getString("from"));
          //  message.setFromName(jsonObject.getString("fromName"));
            message.setTo(jsonObject.getString("to"));
            message.setToName(jsonObject.getString("toName"));
            message.setContent(jsonObject.getString("content"));
            message.setmTime(jsonObject.getString("mTime"));
            mRealm.beginTransaction();
            mRealm.copyToRealm(message);
            mRealm.commitTransaction();
            notificate(context,message.getContent(),message.getFrom());
            Log.d("msgSaved", "receive: "+"true");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("msgSaved", "receive: "+"false");
        }
    }
    private void notificate(Context context,String title,String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.ic_contact_blue_24dp);
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setDefaults(Notification.DEFAULT_SOUND);
        /*数组第一个参数表示延迟震动时间
         第二个参数表示震动持续时间
         第三个参数表示震动后的休眠时间
         第四个参数又表示震动持续时间
         第五个参数也表示正到休眠时间
         */
        long[] pattern = {0, 100, 1000, 300, 200, 100, 500, 200, 100};
        builder.setVibrate (pattern);
        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
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
            Log.d("matchRemind", "matchRemind: "+remindTask.getTo()+" : "+remindTask.getContent());

            if(remindTask.isRepeatingMonday()&& TimeUtil.getDayOfWeek()==TimeUtil.MONDAY){
                publish(context, remindTask, currentDate, remindDate);
            }
            if(remindTask.isRepeatingTuesday()&&TimeUtil.getDayOfWeek()==TimeUtil.TUEDAY){
                publish(context, remindTask, currentDate, remindDate);
            }
            if(remindTask.isRepeatingWednesday()&&TimeUtil.getDayOfWeek()==TimeUtil.WENDAY){
                publish(context, remindTask, currentDate, remindDate);
            }
            if(remindTask.isRepeatingThursday()&&TimeUtil.getDayOfWeek()==TimeUtil.THRDAY){
                publish(context, remindTask, currentDate, remindDate);
            }
            if(remindTask.isRepeatingFriday()&&TimeUtil.getDayOfWeek()==TimeUtil.FRIDAY){
                publish(context, remindTask, currentDate, remindDate);
            }
            if(remindTask.isRepeatingSaturday()&&TimeUtil.getDayOfWeek()==TimeUtil.STADAY){
                publish(context, remindTask, currentDate, remindDate);
            }
            if(remindTask.isRepeatingSunday()&&TimeUtil.getDayOfWeek()==TimeUtil.SUNDAY){
                publish(context, remindTask,currentDate, remindDate);
            }
        }
    }

    private void publish(Context context, RemindTask remindTask, Date currentDate, Date remindDate) {
        if (mWatchTime.format(currentDate).equals(mWatchTime.format(remindDate))){
            JSONObject jsonObject = new JSONObject();
            try {
               // jsonObject.put("fromName",remindTask.getFromName());
                jsonObject.put("from",remindTask.getFrom());
                jsonObject.put("toName",remindTask.getTo());
                jsonObject.put("to",remindTask.getTo());
                jsonObject.put("content",remindTask.getContent());
                jsonObject.put("mTime",remindTask.getmTime());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("publishMessage", "publish: "+jsonObject.toString());
            MqttPublishService.getInstance(context).publish(remindTask.getTo(),jsonObject.toString(),2);
        }
    }
}
