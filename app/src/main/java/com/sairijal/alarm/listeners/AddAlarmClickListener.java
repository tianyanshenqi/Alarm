package com.sairijal.alarm.listeners;

import android.content.Intent;
import android.view.View;

import com.sairijal.alarm.fragments.AddAlarmFragment;
import com.sairijal.alarm.service.TimeTickService;

/**
 * Created by sayujya on 2016-02-03.
 */
public class AddAlarmClickListener implements View.OnClickListener {

    AddAlarmFragment mFragment;

    public AddAlarmClickListener(AddAlarmFragment mFragment) {
        this.mFragment = mFragment;
    }

    @Override
    public void onClick(View v) {
        mFragment.addAlarm();
        Intent intent = new Intent(v.getContext(), TimeTickService.class);
        v.getContext().startService(intent);
    }
}
