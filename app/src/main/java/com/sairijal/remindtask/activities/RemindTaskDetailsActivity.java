package com.sairijal.remindtask.activities;

import android.support.v4.app.SharedElementCallback;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SwitchCompat;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.sairijal.remindtask.R;
import com.sairijal.remindtask.entity.RemindTaskWrapper;
import com.sairijal.remindtask.entity.RemindTaskWrapperHolder;
import com.sairijal.remindtask.entity.RemindTask;
import com.sairijal.remindtask.application.RemindTaskApplication;

import java.util.List;

import io.realm.Realm;

public class RemindTaskDetailsActivity extends AppCompatActivity {

    private RemindTaskWrapper mAlarm;

    private CardView mContainer;
    private RelativeLayout mAlarmCardLayout;
    private TextView mAlarmTime;
    private TextView mAlarmAmPm;
    private TextView mAlarmDays;

    private ToggleButton[] mRepeating = new ToggleButton[7];
    private SwitchCompat mAlarmSwitch;
    private ImageView mAlarmIcon;

    private Realm mRealm;
    private TextView mAlarmUserName;
    private TextView mAlarmUserPhone;
    private TextView mContent;

    @Override
    protected void onStart() {
        super.onStart();
        mRealm= Realm.getDefaultInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_details);

        fetchIntentData();
        findGeneralViews(findViewById(R.id.alarm_general_activity_card));
        final View detailsCard = findViewById(R.id.alarm_details_activity_card);
        findDetailedViews(detailsCard);
        fillData();

        setEnterSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
                super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots);
                Log.i(RemindTaskApplication.APP_TAG, "animation end");
                Animation slideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_below);
                slideUp.setStartOffset(200);
                detailsCard.setAnimation(slideUp);
                slideUp.start();
            }
        });
    }


    @Override
    protected void onStop() {
        mRealm.close();
        super.onStop();
    }

    private void fetchIntentData() {
        mAlarm = RemindTaskWrapperHolder.getInstance();
    }

    private void findGeneralViews(View itemView) {
        mContainer = (CardView) itemView.findViewById(R.id.alarm_card);
        mAlarmCardLayout = (RelativeLayout) itemView.findViewById(R.id.alarm_card_layout);
        mAlarmTime = (TextView) itemView.findViewById(R.id.alarm_time);
        mAlarmAmPm = (TextView) itemView.findViewById(R.id.alarm_ampm);
        mAlarmDays = (TextView) itemView.findViewById(R.id.alarm_days);

        mAlarmDays.setText(getResources().getText(R.string.alarm_days_text), TextView.BufferType.SPANNABLE);

        boolean[] repeatingDays = mAlarm.getRepeating();
        for (int i = 0; i < repeatingDays.length; i++) {
            if (repeatingDays[i]) {
                Spannable span = (Spannable) mAlarmDays.getText();
                span.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), i * 3, (i * 3) + 2,
                        Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            }
        }
        mAlarmDays.setHeight(0);
        mAlarmSwitch = (SwitchCompat) itemView.findViewById(R.id.alarm_switch);
        mAlarmIcon = (ImageView) itemView.findViewById(R.id.alarm_card_icon);
    }

    private void findDetailedViews(View itemView) {
        mRepeating[0] = (ToggleButton) itemView.findViewById(R.id.monday_repeating);
        mRepeating[1] = (ToggleButton) itemView.findViewById(R.id.tuesday_repeating);
        mRepeating[2] = (ToggleButton) itemView.findViewById(R.id.wednesday_repeating);
        mRepeating[3] = (ToggleButton) itemView.findViewById(R.id.thursday_repeating);
        mRepeating[4] = (ToggleButton) itemView.findViewById(R.id.friday_repeating);
        mRepeating[5] = (ToggleButton) itemView.findViewById(R.id.saturday_repeating);
        mRepeating[6] = (ToggleButton) itemView.findViewById(R.id.sunday_repeating);
        mAlarmUserName = (TextView) itemView.findViewById(R.id.username_picker);
        mAlarmUserPhone = (TextView) itemView.findViewById(R.id.userphone_picker);
        mContent = (TextView) itemView.findViewById(R.id.content);
        attachDetailedViewListeners();
    }

    private void attachDetailedViewListeners() {

    }

    public void setAlarmCardIcon(int alarmType, boolean isOn, ImageView alarmCardIcon){
        if (isOn) {
            alarmCardIcon.setImageResource(R.drawable.ic_alarm_on_24dp);
        } else {
            alarmCardIcon.setImageResource(R.drawable.ic_alarm_off_24dp);
        }
    }

    private void fillData() {

        String[] time = mAlarm.getTime();

        mAlarmTime.setText(time[0]);
        mAlarmAmPm.setText(time[1]);
        mAlarmUserName.setText(mAlarm.getUserName());
        mAlarmUserPhone.setText(mAlarm.getUserPhone());

        mContent.setText(mAlarm.getContent());
        mAlarmSwitch.setChecked(mAlarm.getState().equals(RemindTask.ON));

        setAlarmCardIcon(mAlarm.getAuthenticationType(), mAlarmSwitch.isChecked(), mAlarmIcon);

        mAlarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setAlarmCardIcon(mAlarm.getAuthenticationType(), isChecked, mAlarmIcon);
                mRealm.beginTransaction();
                mAlarm.setState((isChecked)? RemindTask.ON: RemindTask.OFF);
                mRealm.commitTransaction();
            }
        });

        boolean[] repeatingDays = mAlarm.getRepeating();
        for (int i=0; i<7; i++){
            mRepeating[i].setChecked(repeatingDays[i]);
        }
    }
}
