package com.sairijal.alarm.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import com.sairijal.alarm.R;
import com.sairijal.alarm.alarm.RemindTaskWrapper;
import com.sairijal.alarm.alarm.User;
import com.sairijal.alarm.alarm.UserWrapper;
import com.sairijal.alarm.application.AlarmApplication;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import static android.R.id.list;

/**
 * Created by 11255 on 2017/3/27.
 */

public class UserSelectDialog {
    private final Context mContext;
    private final Dialog mDialog;
    private OnUserItemClickListener onUserItemClickListener;
    private View mView;
    private RecyclerView mRecycleView;
    private UserAdapter mUserAdapter;
    private List<UserWrapper> mUserListWrapper;
    private View mListLayout;
    private Realm mRealm;
    private RelativeLayout mNoContactLayout;

    public UserSelectDialog(Context context){
        this.mContext = context;
        mDialog = new Dialog(mContext
                , R.style.TransParent);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mRealm = Realm.getDefaultInstance();
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.layout_select_dialog,null);
        mDialog.setContentView(mView);

    }

    public void setOnUserItemClickListener(OnUserItemClickListener onUserItemClickListener){
        this.onUserItemClickListener = onUserItemClickListener;
        initView();
    }

    private void initView() {
        mRecycleView = (RecyclerView) mView.findViewById(R.id.alarm_recycler_view);
        mListLayout = mView.findViewById(R.id.list_layout);
        mNoContactLayout = (RelativeLayout)mView.findViewById(R.id.no_contact_layout);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(mContext);
        mUserListWrapper = loadUsers();
        if(mUserListWrapper.size()==0){
            mUserListWrapper = loadUsersFromContacts();
        }
        if(mUserListWrapper.size()==0){
            mListLayout.setVisibility(View.GONE);
            mNoContactLayout.setVisibility(View.VISIBLE);
        }else{
            mUserAdapter = new UserAdapter(mContext,mUserListWrapper,mLinearLayoutManager,onUserItemClickListener);
            mRecycleView.setLayoutManager(mLinearLayoutManager);
            mRecycleView.setAdapter(mUserAdapter);
        }
    }

    private List<UserWrapper> loadUsersFromContacts() {
        List<UserWrapper> savedUserList = new ArrayList<>();
        try {
            Uri contactUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            Cursor cursor = mContext.getContentResolver().query(contactUri,
                    new String[]{"display_name", "sort_key", "contact_id","data1"},
                    null, null, "sort_key");
            String contactName;
            String contactNumber;
            int contactId;
            while (cursor.moveToNext()) {
                contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                contactNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                contactId = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                User user = new User();
                user.setPhone(contactNumber);
                user.setName(contactName);
                String formatString = String.format("%04d",contactId);
                user.setId(formatString);
                user.setAge(20);
                user.setSex("女");
                savedUserList.add(new UserWrapper(user));
                mRealm.beginTransaction();
                user = mRealm.copyToRealm(user);
                mRealm.commitTransaction();

            }
            cursor.close();//使用完后一定要将cursor关闭，不然会造成内存泄露等问题

        }catch (Exception e){
            e.printStackTrace();
        }

        return  savedUserList;
    }

    public void show(){
        mDialog.show();
    }

    private List<UserWrapper> loadUsers() {
        /*RealmQuery<RemindTask> query = Realm.getDefaultInstance().where(RemindTask.class);
        RealmResults<RemindTask> savedRemindTasks = query.findAll();
        Log.i(AlarmApplication.APP_TAG, String.valueOf(savedRemindTasks.size()));
        List<RemindTaskWrapper> savedAlarmsList = new ArrayList<>();
        for (RemindTask remindTask : savedRemindTasks){
            savedAlarmsList.add(new RemindTaskWrapper(remindTask));
        }*/
        List<UserWrapper> savedUserList = new ArrayList<>();

        /*for (int i = 0 ;i<10;i++) {
            User user = new User();
            user.setPhone("1532222333"+i);
            user.setName("小"+i);
            user.setId("0000"+i);
            user.setAge(20+i);
            user.setSex(i%2==0?"男":"女");
            //savedUserList.add(new UserWrapper(user));
            mRealm.beginTransaction();
            user = mRealm.copyToRealm(user);
            mRealm.commitTransaction();
        }*/
        RealmQuery<User> query = Realm.getDefaultInstance().where(User.class);
        RealmResults<User> savedRemindTasks = query.findAll();
        Log.i(AlarmApplication.APP_TAG, String.valueOf(savedRemindTasks.size()));
        List<RemindTaskWrapper> savedAlarmsList = new ArrayList<>();
        for (User remindTask : savedRemindTasks) {
            savedUserList.add(new UserWrapper(remindTask));
        }
        return  savedUserList;
    }

    public void dismiss() {
        mDialog.dismiss();
    }
}
