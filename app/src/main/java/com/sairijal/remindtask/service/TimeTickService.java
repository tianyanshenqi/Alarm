package com.sairijal.remindtask.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.util.Log;

import com.sairijal.remindtask.R;
import com.sairijal.remindtask.activities.RemindTaskActivity;
import com.sairijal.remindtask.receiver.TimeTickReceiver;

public class TimeTickService extends Service {
    private TimeTickReceiver timeTickReceiver;

    @Override
    public void onCreate() {
        registerReceiver();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //notificate();
//        alarm = getNextAlarm();  update: 这行居然是元凶！我稍微看看这个方法再更新一下题目。。。
        // 在API11之后构建Notification的方式
        Notification.Builder builder = new Notification.Builder(this.getApplicationContext()); //获取一个Notification构造器
        Intent nfIntent = new Intent(this, RemindTaskActivity.class);
        nfIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        builder.setContentIntent(PendingIntent.getActivity(this, 0, nfIntent, 0)) // 设置点击时要跳转的页面
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                        R.mipmap.ic_launcher)) // 设置下拉列表中的图标(大图标)
                .setContentTitle(getString(R.string.app_name)) // 设置下拉列表里的标题
                .setSmallIcon(R.mipmap.ic_launcher) // 设置状态栏内的小图标
                .setContentText("后台运行中") // 设置上下文内容
                .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间
        Notification notification = builder.build(); // 获取构建好的Notification
        // notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音
        // 参数一：唯一的通知标识；参数二：通知消息。
        startForeground(110, notification);// 开始前台服务
        return START_NOT_STICKY;
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        timeTickReceiver = new TimeTickReceiver();
        registerReceiver(timeTickReceiver, intentFilter);
    }



    @Override
    public void onDestroy() {
        Log.d("unregister", "onDestroy: ");
        unregisterReceiver(timeTickReceiver);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

