package com.sairijal.remindtask.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.sairijal.remindtask.R;
import com.sairijal.remindtask.activities.RemindTaskActivity;
import com.sairijal.remindtask.entity.RemindTaskWrapper;
import com.sairijal.remindtask.entity.RemindTask;
import com.sairijal.remindtask.entity.UserWrapper;
import com.sairijal.remindtask.listeners.OnUserItemClickListener;
import com.sairijal.remindtask.dialog.UserSelectDialog;
import com.sairijal.remindtask.listeners.AddRemindTaskClickListener;
import com.sairijal.remindtask.listeners.RemindTaskTimeSetListener;
import com.sairijal.remindtask.listeners.SetRemindTaskTimeListener;
import com.sairijal.remindtask.sharedpreferences.LoginInfoSharePreference;
import com.sairijal.remindtask.utils.TimeUtil;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import io.realm.Realm;

/**
 * Created by sayujya on 2016-01-29.
 */
public class AddRemindTaskFragment extends Fragment {

    private View mView;
    private TextView mALarmTime;
    private EditText mContent;
    private ToggleButton[] mRepeating;
    private Button mConfirm;
    private Realm mRealm;
    private TextView mUserName;
    private TextView mUserPhone;
    private TextView mAmpPmTime;
    private ImageView mALarmTimeImage;
    private ImageView mUserImage;
    private Spinner mSpinner;

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
        mALarmTimeImage = (ImageView)mView.findViewById(R.id.time_picker_imageview);
        mUserImage = (ImageView)mView.findViewById(R.id.user_picker_imageview);
        mSpinner = (Spinner) mView.findViewById(R.id.type);

        mContent = (EditText) mView.findViewById(R.id.content);
        mRepeating = new ToggleButton[7];
        mRepeating[0] = (ToggleButton) mView.findViewById(R.id.sunday_repeating);
        mRepeating[1] = (ToggleButton) mView.findViewById(R.id.monday_repeating);
        mRepeating[2] = (ToggleButton) mView.findViewById(R.id.tuesday_repeating);
        mRepeating[3] = (ToggleButton) mView.findViewById(R.id.wednesday_repeating);
        mRepeating[4] = (ToggleButton) mView.findViewById(R.id.thursday_repeating);
        mRepeating[5] = (ToggleButton) mView.findViewById(R.id.friday_repeating);
        mRepeating[6] = (ToggleButton) mView.findViewById(R.id.saturday_repeating);
        mRepeating[TimeUtil.getDayOfWeek()].setChecked(true);

        mConfirm = (Button) mView.findViewById(R.id.add_confirm);
    }

    private void addListeners() {
        RemindTaskTimeSetListener remindTaskTimeSetListener = new RemindTaskTimeSetListener(mALarmTime,mAmpPmTime);
        SetRemindTaskTimeListener setAlarmTimeListener = new SetRemindTaskTimeListener(this.getActivity(), remindTaskTimeSetListener);
        mALarmTimeImage.setOnClickListener(setAlarmTimeListener);
        mUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()){
                    showContactDialog();
                }
            }
        });

        AddRemindTaskClickListener confirmClickListener = new AddRemindTaskClickListener(this);
        mConfirm.setOnClickListener(confirmClickListener);

    }

    private void showContactDialog() {
        final UserSelectDialog dialog = new UserSelectDialog(getContext());
        dialog.setOnUserItemClickListener(new OnUserItemClickListener() {
            @Override
            public void OnUserItemClick(UserWrapper userWrapper) {
                dialog.dismiss();
                mUserName.setText(userWrapper.getName());
                mUserPhone.setText(userWrapper.getPhone());
            }
        });
        dialog.show();
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(getContext(),android.Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            AddRemindTaskFragment.this.requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                    1);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ContextCompat.checkSelfPermission(getContext(),android.Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED){
            showContactDialog();
        }else{
        }
    }

    public void addAlarm(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault());
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        try {
            Time time = new Time(formatter.parse(
                    format2.format(new Date())+" "+
                    this.mALarmTime.getText().toString()+" "+this.mAmpPmTime.getText().toString()
            ).getTime());
            boolean[] repeating = new boolean[7];
            for (int i=0; i<7; i++){
                repeating[i] = this.mRepeating[i].isChecked();
            }
            int alarmType = mSpinner.getSelectedItemPosition();
            String userName = mUserName.getText().toString();
            String userPhone = mUserPhone.getText().toString();
            String content = mContent.getText().toString();
            RemindTaskWrapper newAlarm = createAlarm(time,userName, repeating,userPhone,content ,alarmType);
            if (getActivity() instanceof RemindTaskActivity){
                ((RemindTaskActivity) getActivity()).addAlarm(newAlarm);
            }
            getFragmentManager().popBackStack();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private RemindTaskWrapper createAlarm(Time time, String userName, boolean[] repeating, String phone, String content, int alarmType) {
        RemindTask newRemindTask = new RemindTask();
        newRemindTask.setmTime(time.getTime());
        newRemindTask.setRepeatingSunday(repeating[0]);
        newRemindTask.setRepeatingMonday(repeating[1]);
        newRemindTask.setRepeatingTuesday(repeating[2]);
        newRemindTask.setRepeatingWednesday(repeating[3]);
        newRemindTask.setRepeatingThursday(repeating[4]);
        newRemindTask.setRepeatingFriday(repeating[5]);
        newRemindTask.setRepeatingSaturday(repeating[6]);

        newRemindTask.setRemindType(alarmType);
        newRemindTask.setState(RemindTask.ON);
        newRemindTask.setUniqueID(UUID.randomUUID().toString());
        newRemindTask.setContent(content);
        newRemindTask.setToName(userName);
        newRemindTask.setFrom(LoginInfoSharePreference.getInstancce(getActivity()).getData("userName"));
        newRemindTask.setTo(phone);

        mRealm.beginTransaction();
        newRemindTask = mRealm.copyToRealm(newRemindTask);
        mRealm.commitTransaction();
        return new RemindTaskWrapper(newRemindTask);
    }

}
