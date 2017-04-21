package com.sairijal.alarm.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sairijal.alarm.R;
import com.sairijal.alarm.alarm.Message;
import com.sairijal.alarm.dialog.OnUserItemClickListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by 11255 on 2017/4/21.
 */

public class ReceivedMessageAdapter  extends RecyclerView.Adapter<ReceivedMessageAdapter.ViewHolder>{

    private final OnUserItemClickListener onItemClickListener;
    private List<Message> mMessages = new ArrayList<>();
    private Context mContext;

    public ReceivedMessageAdapter(Context context, List<Message> mDataset, OnUserItemClickListener onItemClickListener) {
        mContext = context;
        DateFormat formatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
        this.onItemClickListener = onItemClickListener;

        boolean is24Hour = android.text.format.DateFormat.is24HourFormat(mContext);
        this.mMessages = mDataset;
        // todo update when creating alarms is supported
        //this.mDataset = mDataset;
        //Collections.sort(mDataset);
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View stockCard = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_message, parent, false);
        // link views to class
        final ReceivedMessageAdapter.ViewHolder viewHolder = new ReceivedMessageAdapter.ViewHolder(stockCard);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Message receiveMessage = mMessages.get(position);

        holder.mAlarmCardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent alarmDetailsIntent = new Intent(mContext, AlarmDetailsActivity.class);
            }
        });

        holder.mContent.setText(receiveMessage.getContent());
        holder.mFrom.setText(receiveMessage.getFrom());
        Date remindDate = new Date(Long.valueOf(receiveMessage.getmTime()));
        SimpleDateFormat mWatchTime;
        mWatchTime = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault());

        holder.mTime.setText(mWatchTime.format(remindDate));
        setAnimation(holder.mContainer, position);
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        public CardView mContainer;

        public RelativeLayout mAlarmCardLayout;

        public TextView mContent;
        public TextView mFrom;
        private final TextView mTime;

        public ViewHolder(View itemView) {
            super(itemView);
            mContainer = (CardView) itemView.findViewById(R.id.card_msg);
            mAlarmCardLayout = (RelativeLayout) itemView.findViewById(R.id.card_msg_layout);
            mContent = (TextView) itemView.findViewById(R.id.card_msg_content);
            mFrom = (TextView) itemView.findViewById(R.id.card_msg_from);
            mTime = (TextView) itemView.findViewById(R.id.card_msg_time);
        }
    }

    private int lastPosition = -1;
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
}
