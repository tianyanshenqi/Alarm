package com.sairijal.remindtask.listeners;

import android.content.Context;
import android.view.View;

import com.sairijal.remindtask.activities.RemindTaskActivity;

/**
 * Created by sayujya on 2016-01-28.
 */
public class AddRemindTaskFabListener implements View.OnClickListener {

    Context mContext;

    public AddRemindTaskFabListener(Context c) {
        super();
        this.mContext = c;
    }

    @Override
    public void onClick(View view) {
        if (mContext instanceof RemindTaskActivity) {
            ((RemindTaskActivity) mContext).replaceWithAddFragment();
        }
    }

}
