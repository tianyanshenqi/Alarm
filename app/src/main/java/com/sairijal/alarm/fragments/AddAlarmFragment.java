package com.sairijal.alarm.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.sairijal.alarm.R;
import com.sairijal.alarm.activities.AlarmActivity;
import com.sairijal.alarm.alarm.AlarmWrapper;
import com.sairijal.alarm.alarm.RemindTask;
import com.sairijal.alarm.alarm.User;
import com.sairijal.alarm.listeners.AddAlarmClickListener;
import com.sairijal.alarm.listeners.AlarmTimeSetListener;
import com.sairijal.alarm.listeners.SetAlarmTimeListener;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.UUID;

import io.realm.Realm;

/**
 * Created by sayujya on 2016-01-29.
 */
public class AddAlarmFragment extends Fragment {

    private View mView;
    private TextView mALarmTime;
    private EditText mContent;
    private ToggleButton[] mRepeating;
    private Button mConfirm;
    private Realm mRealm;
    private TextView mUserName;
    private TextView mUserPhone;
    private TextView mAmpPmTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_add_alarm, container, false);
        initalizeViews();
        addListeners();
        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();
        mRealm.close();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initalizeViews() {
        mALarmTime = (TextView) mView.findViewById(R.id.time_picker);
        mAmpPmTime = (TextView) mView.findViewById(R.id.time_ampm_picker);
        mUserName = (TextView) mView.findViewById(R.id.username_picker);
        mUserPhone = (TextView) mView.findViewById(R.id.userphone_picker);
        mContent = (EditText) mView.findViewById(R.id.content);
        mRepeating = new ToggleButton[7];
        mRepeating[0] = (ToggleButton) mView.findViewById(R.id.monday_repeating);
        mRepeating[1] = (ToggleButton) mView.findViewById(R.id.tuesday_repeating);
        mRepeating[2] = (ToggleButton) mView.findViewById(R.id.wednesday_repeating);
        mRepeating[3] = (ToggleButton) mView.findViewById(R.id.thursday_repeating);
        mRepeating[4] = (ToggleButton) mView.findViewById(R.id.friday_repeating);
        mRepeating[5] = (ToggleButton) mView.findViewById(R.id.saturday_repeating);
        mRepeating[6] = (ToggleButton) mView.findViewById(R.id.sunday_repeating);

        mConfirm = (Button) mView.findViewById(R.id.add_confirm);
    }

    private void addListeners() {
        AlarmTimeSetListener alarmTimeSetListener = new AlarmTimeSetListener(mALarmTime,mAmpPmTime);
        SetAlarmTimeListener setAlarmTimeListener = new SetAlarmTimeListener(this.getActivity(), alarmTimeSetListener);
        mALarmTime.setOnClickListener(setAlarmTimeListener);

        AddAlarmClickListener confirmClickListener = new AddAlarmClickListener(this);
        mConfirm.setOnClickListener(confirmClickListener);

    }

    public void addAlarm(){
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
        try {
            Time time = new Time(formatter.parse(
                    this.mALarmTime.getText().toString()+" "+this.mAmpPmTime.getText().toString()
            ).getTime());
            boolean[] repeating = new boolean[7];
            for (int i=0; i<7; i++){
                repeating[i] = this.mRepeating[i].isChecked();
            }
            int alarmType = com.sairijal.alarm.alarm.RemindTask.NONE;
            String userName = mUserName.getText().toString();
            String userPhone = mUserPhone.getText().toString();
            String content = mContent.getText().toString();
            AlarmWrapper newAlarm = createAlarm(time,userName, repeating,userPhone,content ,alarmType);
            if (getActivity() instanceof  AlarmActivity){
                ((AlarmActivity) getActivity()).addAlarm(newAlarm);
            }
            getFragmentManager().popBackStack();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private AlarmWrapper createAlarm(Time time, String userName, boolean[] repeating, String phone, String content, int alarmType) {
        RemindTask newRemindTask = new RemindTask();
        newRemindTask.setmTime(time.getTime());
        newRemindTask.setRepeatingMonday(repeating[0]);
        newRemindTask.setRepeatingTuesday(repeating[1]);
        newRemindTask.setRepeatingWednesday(repeating[2]);
        newRemindTask.setRepeatingThursday(repeating[3]);
        newRemindTask.setRepeatingFriday(repeating[4]);
        newRemindTask.setRepeatingSaturday(repeating[5]);
        newRemindTask.setRepeatingSunday(repeating[6]);
        newRemindTask.setAuthenticationType(alarmType);
        newRemindTask.setState(com.sairijal.alarm.alarm.RemindTask.ON);
        newRemindTask.setUniqueID(UUID.randomUUID().toString());
        newRemindTask.setContent(content);
        newRemindTask.setFromName(userName);
        newRemindTask.setTo(phone);

        mRealm.beginTransaction();
        newRemindTask = mRealm.copyToRealm(newRemindTask);
        mRealm.commitTransaction();
        return new AlarmWrapper(newRemindTask);
    }

}
