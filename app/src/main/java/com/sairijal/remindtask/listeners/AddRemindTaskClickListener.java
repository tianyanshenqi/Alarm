package com.sairijal.remindtask.listeners;

import android.content.Intent;
import android.view.View;

import com.sairijal.remindtask.fragments.AddRemindTaskFragment;
import com.sairijal.remindtask.service.TimeTickService;

/**
 * Created by sayujya on 2016-02-03.
 */
public class AddRemindTaskClickListener implements View.OnClickListener {

    AddRemindTaskFragment mFragment;

    public AddRemindTaskClickListener(AddRemindTaskFragment mFragment) {
        this.mFragment = mFragment;
    }

    @Override
    public void onClick(View v) {
        mFragment.addAlarm();
        Intent intent = new Intent(v.getContext(), TimeTickService.class);
        v.getContext().startService(intent);
    }
}
