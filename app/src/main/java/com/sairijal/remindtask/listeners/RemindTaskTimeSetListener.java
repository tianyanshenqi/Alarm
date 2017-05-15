package com.sairijal.remindtask.listeners;

import android.app.TimePickerDialog;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import com.sairijal.remindtask.application.RemindTaskApplication;
import com.sairijal.remindtask.entity.RemindTaskWrapper;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by sayujya on 2016-01-30.
 */
public class RemindTaskTimeSetListener implements TimePickerDialog.OnTimeSetListener, Parcelable {

    TextView mAlarmTime;
    TextView mAmPmTime;

    public RemindTaskTimeSetListener(TextView mAlarmTime, TextView mAmPmTime) {
        this.mAlarmTime = mAlarmTime;
        this.mAmPmTime = mAmPmTime;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        SimpleDateFormat formatter;
        SimpleDateFormat aformatter = null;
        Log.i(RemindTaskApplication.APP_TAG, hourOfDay + ":" + minute);
        if (RemindTaskWrapper.is24Hours()){
            formatter = new SimpleDateFormat("HH:mm ", Locale.getDefault());
        } else {
            formatter = new SimpleDateFormat("hh:mm", Locale.getDefault());
            aformatter = new SimpleDateFormat("a", Locale.getDefault());
        }
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        if (aformatter!=null){
            aformatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            String aformattedTime = aformatter.format((hourOfDay * 3600 + minute * 60) * 1000);
            mAmPmTime.setText(aformattedTime);
        }else{
            mAmPmTime.setVisibility(View.GONE);
        }
        String formattedTime = formatter.format((hourOfDay * 3600 + minute * 60) * 1000);
        mAlarmTime.setText(formattedTime);

    }

    protected RemindTaskTimeSetListener(Parcel in) {
        mAlarmTime = (TextView) in.readValue(TextView.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(mAlarmTime);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<RemindTaskTimeSetListener> CREATOR = new Parcelable.Creator<RemindTaskTimeSetListener>() {
        @Override
        public RemindTaskTimeSetListener createFromParcel(Parcel in) {
            return new RemindTaskTimeSetListener(in);
        }

        @Override
        public RemindTaskTimeSetListener[] newArray(int size) {
            return new RemindTaskTimeSetListener[size];
        }
    };
}
