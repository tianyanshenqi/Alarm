package com.sairijal.remindtask.listeners;

import android.content.Context;
import android.view.View;

import com.sairijal.remindtask.activities.RemindTaskActivity;

/**
 * Created by sayujya on 2016-01-30.
 */
public class SetRemindTaskTimeListener implements View.OnClickListener {

    Context mContext;
    RemindTaskTimeSetListener mRemindTaskTimeSetListener;

    public SetRemindTaskTimeListener(Context c, RemindTaskTimeSetListener remindTaskTimeSetListener){
        this.mContext = c;
        this.mRemindTaskTimeSetListener = remindTaskTimeSetListener;
    }
    @Override
    public void onClick(View v) {
        if (mContext instanceof RemindTaskActivity){
            ((RemindTaskActivity) mContext).showTimePickerDialog(mRemindTaskTimeSetListener);
        }
    }
}
