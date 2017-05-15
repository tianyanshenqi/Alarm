package com.sairijal.remindtask.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sairijal.remindtask.R;
import com.sairijal.remindtask.activities.RemindTaskDetailsActivity;
import com.sairijal.remindtask.entity.UserWrapper;
import com.sairijal.remindtask.entity.UserWrapperHolder;
import com.sairijal.remindtask.listeners.OnUserItemClickListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;

/**
 * Created by 11255 on 2017/3/27.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    // tag for sending alarm position to details activity
    public static final String ALARM_POSITION_TAG = "alarm";

    // tags for adapter
    public static final String ADAPTER_TAG = "/time";

    // last removed alarm
    private List<UserWrapper> mRemovedAlarm = new ArrayList<UserWrapper>();

    // list of alarms
    private List<UserWrapper> mDataset = new ArrayList<UserWrapper>();

    // layout manager of the view this adapter is attached to
    private LinearLayoutManager mLayoutManager;

    // last view animated
    private int lastPosition = -1;

    // realm instance, not updated until required
    private Realm mRealm;

    // context of the view
    private Context mContext;
    private OnUserItemClickListener onItemClickListener;


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CardView mContainer;

        public RelativeLayout mAlarmCardLayout;

        public TextView mUserName;
        public TextView mUserPhone;
        public ImageView mUserCall;

        public ViewHolder(View itemView) {
            super(itemView);

            mContainer = (CardView) itemView.findViewById(R.id.card_user);
            mAlarmCardLayout = (RelativeLayout) itemView.findViewById(R.id.card_user_layout);
            mUserName = (TextView) itemView.findViewById(R.id.card_user_name);
            mUserPhone = (TextView) itemView.findViewById(R.id.card_user_phone);
            mUserCall = (ImageView) itemView.findViewById(R.id.card_user_call);

        }
    }

    public UserAdapter(Context context, List<UserWrapper> mDataset, LinearLayoutManager layoutManager,OnUserItemClickListener onItemClickListener) {
        mContext = context;
        mLayoutManager = layoutManager;
        DateFormat formatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
        this.onItemClickListener = onItemClickListener;

        boolean is24Hour = android.text.format.DateFormat.is24HourFormat(mContext);
        mRealm = Realm.getDefaultInstance();
        this.mDataset = mDataset;
        Collections.sort(this.mDataset);
        // todo update when creating alarms is supported
        //this.mDataset = mDataset;
        //Collections.sort(mDataset);
        notifyDataSetChanged();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view
        final View stockCard = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_user, parent, false);
        // link views to class
        final ViewHolder viewHolder = new ViewHolder(stockCard);

        viewHolder.mUserCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call(viewHolder.mUserPhone.getText().toString());
            }
        });
        return viewHolder;
    }

    private void call(String s) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse("tel:"+s));
        mContext.startActivity(intent);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final UserWrapper userWrapper = mDataset.get(position);

        holder.mAlarmCardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserWrapperHolder.setUserWrapper(userWrapper);
                Intent alarmDetailsIntent = new Intent(mContext, RemindTaskDetailsActivity.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext,
                            Pair.create((View) holder.mContainer, mContext.getString(R.string.card_transition_name)),
                            Pair.create((View) holder.mUserName, mContext.getString(R.string.card_ampm_transition_name)),
                            Pair.create((View) holder.mUserPhone, mContext.getString(R.string.card_time_transition_name)),
                            Pair.create((View) holder.mUserCall, mContext.getString(R.string.card_days_transition_name)));
                    onItemClickListener.OnUserItemClick(userWrapper);
                } else {
                    onItemClickListener.OnUserItemClick(userWrapper);
                }
            }
        });

        holder.mUserName.setText(userWrapper.getName());
        holder.mUserPhone.setText(userWrapper.getPhone());

        setAnimation(holder.mContainer, position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        // return size of items in recyclerview
        return mDataset.size();
    }

    private void setAnimation(final View viewToAnimate, int position) {
        // if the view hasn't been animated yet
        if (position > lastPosition) {
            // add an animation
            final Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            // attach animation
            viewToAnimate.startAnimation(animation);
            // update count of views animated
            lastPosition = position;
        }
    }


    @Override
    public void onItemDismiss(final int position) {

        // remove and cache the alarm
        mRemovedAlarm.add(mDataset.remove(position));

        // notify adapter that alarm has been removed
        notifyItemRemoved(position);

        // create arguments for snackbar

       // UndoDeleteListener undoDeleteListener = new UndoDeleteListener(this, position);

        // create snackbar, add action and show snackbar

    }

    public void undoDelete(int position) {

        scrollToPositionIfNeeded(position);

        UserWrapper restoredAlarm = mRemovedAlarm.remove(mRemovedAlarm.size() - 1);

        // add alarm back to dataset
        mDataset.add(position, restoredAlarm);

        // notify the adapter item has been inserted
        notifyItemInserted(position);

    }

    public void clearAlarms() {
        // cleared cached alarm
        mRealm.beginTransaction();
        for (UserWrapper alarm: mRemovedAlarm){
            alarm.removeFromRealm();
        }
        mRemovedAlarm.clear();
        mRealm.commitTransaction();
    }

    public void addAlarm(UserWrapper alarm){
        mDataset.add(alarm);
        Collections.sort(mDataset);
        int insertedPosition = mDataset.indexOf(alarm);
        scrollToPositionIfNeeded(insertedPosition);
        notifyItemInserted(insertedPosition);
    }

    private void scrollToPositionIfNeeded(int position) {
        mLayoutManager.scrollToPosition(position);
    }

    public void addRealmObject(Realm realm){
        this.mRealm = realm;
    }

    public void setAlarmCardIcon(int alarmType, boolean isOn, ImageView alarmCardIcon){
        if (isOn) {
            alarmCardIcon.setImageResource(R.drawable.ic_alarm_on_24dp);
        } else {
            alarmCardIcon.setImageResource(R.drawable.ic_alarm_off_24dp);
        }
        // // TODO: 2016-01-28 set alarm icon depending on alarm type
        /*int image;
        switch (mAlarm.getAuthenticationType()){
            case RemindTask.DISTANCE:
                image = R.drawable.ic_location_on_black_24dp;
                break;
            case RemindTask.MATH:
                image = R.drawable.ic_exposure_black_24dp;
                break;
            default:
                image = R.drawable.ic_cancel_black_24dp;
                break;
        }
        holder.mAlarmDays.setCompoundDrawablesWithIntrinsicBounds( image, 0, 0, 0);*/
    }

}