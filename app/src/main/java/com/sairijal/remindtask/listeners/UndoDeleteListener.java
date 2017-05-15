package com.sairijal.remindtask.listeners;

import android.view.View;

import com.sairijal.remindtask.adapters.RemindTaskRecyclerAdapter;

/**
 * Created by sayujya on 2016-01-29.
 */
public class UndoDeleteListener implements View.OnClickListener {

    RemindTaskRecyclerAdapter mRecyclerAdapter;
    int mPosition;

    // constructor
    public UndoDeleteListener(RemindTaskRecyclerAdapter recyclerAdapter, int positon){
        // set member fields
        this.mRecyclerAdapter = recyclerAdapter;
        this.mPosition = positon;
    }

    @Override
    public void onClick(View v) {
        // undo delete
        mRecyclerAdapter.undoDelete(mPosition);
    }
}
