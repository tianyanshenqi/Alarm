package com.sairijal.remindtask.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sairijal.remindtask.adapters.RemindTaskRecyclerAdapter;
import com.sairijal.remindtask.application.RemindTaskApplication;
import com.sairijal.remindtask.entity.RemindTask;
import com.sairijal.remindtask.callbacks.RemindTaskSwipeTouchHelperCallback;
import com.sairijal.remindtask.entity.RemindTaskWrapper;
import com.sairijal.remindtask.R;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by sayujya on 2016-01-29.
 */
public class RemindTaskViewFragment extends Fragment {

    private View mMainView;
    private LinearLayoutManager mLinearLayoutManager;
    private RemindTaskRecyclerAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private Realm mRealm;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter!=null){
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();
        // clear cached alarm
        mAdapter.clearAlarms();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mRealm.close();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initializeViews(inflater, container);
        initializeRecyclerView();
        return mMainView;

    }

    private void initializeViews(LayoutInflater inflater, ViewGroup container) {
        // Inflate the layout for this fragment
        mMainView = inflater.inflate(R.layout.fragment_alarm_list, container, false);

        // link recylerview to layout for alarms
        mRecyclerView = (RecyclerView) mMainView.findViewById(R.id.alarm_recycler_view);
    }

    private void initializeRecyclerView() {
        // initialize Linear Layout Manager
        mLinearLayoutManager= new LinearLayoutManager(this.getActivity());

        // set Layout Manager
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        List<RemindTaskWrapper> savedAlarms = loadAlarms();
        // create and set adapter
        mAdapter = new RemindTaskRecyclerAdapter(this.getActivity(), savedAlarms, mLinearLayoutManager);
        mAdapter.addRealmObject(mRealm);
        mRecyclerView.setAdapter(mAdapter);

        // callbacks for swipe to remove
        ItemTouchHelper.Callback swipeCallback = new RemindTaskSwipeTouchHelperCallback(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(swipeCallback);
        touchHelper.attachToRecyclerView(mRecyclerView);

    }

    private List<RemindTaskWrapper> loadAlarms() {
        RealmQuery<RemindTask> query = mRealm.where(RemindTask.class);
        RealmResults<RemindTask> savedRemindTasks = query.findAll();
        Log.i(RemindTaskApplication.APP_TAG, String.valueOf(savedRemindTasks.size()));
        List<RemindTaskWrapper> savedAlarmsList = new ArrayList<>();
        for (RemindTask remindTask : savedRemindTasks){
            savedAlarmsList.add(new RemindTaskWrapper(remindTask));
        }
        return  savedAlarmsList;
    }

    public void addAlarm(RemindTaskWrapper alarm){
        mAdapter.addAlarm(alarm);
    }
}
