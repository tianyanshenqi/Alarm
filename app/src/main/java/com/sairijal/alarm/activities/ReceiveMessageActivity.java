package com.sairijal.alarm.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.sairijal.alarm.R;
import com.sairijal.alarm.alarm.Message;
import com.sairijal.alarm.alarm.UserWrapper;
import com.sairijal.alarm.dialog.OnUserItemClickListener;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class ReceiveMessageActivity extends AppCompatActivity {

    private Realm mRealm;
    private RecyclerView mRecyclerView;
    private ReceivedMessageAdapter mAdapter;
    private List<Message> mMessages = new ArrayList<>();
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_message);


        loadMessages();
        mAdapter = new ReceivedMessageAdapter(this, mMessages, new OnUserItemClickListener() {
            @Override
            public void OnUserItemClick(UserWrapper userWrapper) {

            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.alarm_recycler_view);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }

    private void loadMessages() {
        if (mRealm ==null) {
            mRealm = Realm.getDefaultInstance();
        }
        mMessages.clear();
        RealmQuery<Message> query = mRealm.where(Message.class);
        RealmResults<Message> results = query.findAll();
        Log.d("savedMassage", " : "+results.size());
        mMessages.addAll(results);
    }
}
