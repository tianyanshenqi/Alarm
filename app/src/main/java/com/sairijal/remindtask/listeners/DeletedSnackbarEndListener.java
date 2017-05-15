package com.sairijal.remindtask.listeners;

import android.support.design.widget.Snackbar;

import com.sairijal.remindtask.fragments.RemindTaskViewFragment;

/**
 * Created by sayujya on 2016-02-05.
 */
public class DeletedSnackbarEndListener extends Snackbar.Callback {

    RemindTaskViewFragment mFragment;

    @Override
    public void onDismissed(Snackbar snackbar, int event) {
        super.onDismissed(snackbar, event);
        if (event != Snackbar.Callback.DISMISS_EVENT_ACTION){

        }
    }
}
