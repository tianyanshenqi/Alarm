package com.sairijal.alarm.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.sairijal.alarm.R;
import com.sairijal.alarm.receiver.TimeTickReceiver;

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

