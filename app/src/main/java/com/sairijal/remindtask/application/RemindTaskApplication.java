package com.sairijal.remindtask.application;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by sayujya on 2016-02-04.
 */
public class RemindTaskApplication extends Application {
    // app tag for log
    public static final String APP_TAG = "com.sairijal.alarm";

    @Override
    public void onCreate() {
        super.onCreate();
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(realmConfiguration);
        
    }
}
