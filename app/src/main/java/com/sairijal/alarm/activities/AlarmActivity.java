package com.sairijal.alarm.activities;

import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.mikepenz.crossfadedrawerlayout.view.CrossfadeDrawerLayout;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.MiniDrawer;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.interfaces.ICrossfader;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;
import com.mikepenz.materialize.util.UIUtils;
import com.sairijal.alarm.alarm.RemindTaskWrapper;
import com.sairijal.alarm.R;
import com.sairijal.alarm.fragments.AddAlarmFragment;
import com.sairijal.alarm.fragments.AlarmViewFragment;
import com.sairijal.alarm.fragments.TimePickerFragment;
import com.sairijal.alarm.listeners.AddAlarmFabListener;
import com.sairijal.alarm.listeners.AlarmTimeSetListener;
import com.sairijal.alarm.listeners.FabDisableAnimationListener;
import com.sairijal.alarm.listeners.FabEnableAnimationListener;
import com.sairijal.alarm.mqtt.MqttPublishService;
import com.sairijal.alarm.service.TimeTickService;
import com.sairijal.alarm.sharedpreferences.LoginInfoSharePreference;

import android.text.format.DateFormat;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;

public class AlarmActivity extends AppCompatActivity {

    private final static String APP_TAG = "Material RemindTask";

    // realm orm object
    Realm mRealm;

    // Views
    private TextView mDate;
    private TextView mTime;
    private TextView mDay;
    private FloatingActionButton mAddFab;

    // fragment manager
    private FragmentManager mFragmentManager = getSupportFragmentManager();
    private FragmentTransaction mFragmentTransaction;

    // fragment tags
    private final static String ALARM_VIEW_FRAGMENT = "ALARM_VIEW_FRAGMENT";
    private final static String ALARM_ADD_FRAGMENT = "ALARM_ADD_FRAGMENT";

    // parcelable tags
    private final static String TIME_CHANGED_LISTENER = "RemindTask Set Listener";

    // receivers
    private BroadcastReceiver mTimeBroadcastReceiver;

    // formatters
    private SimpleDateFormat mWatchTime;
    private SimpleDateFormat mWatchDate;
    private SimpleDateFormat mWatchDay ;
    private AccountHeader headerResult;
    private Drawer result;
    private CrossfadeDrawerLayout crossfadeDrawerLayout;
    private BroadcastReceiver mqttSubscribeReceiver;
    private IntentFilter intentFilter;
    private PrimaryDrawerItem drawerItemMessage;
    private PrimaryDrawerItem drawerItemLogout;
    private PrimaryDrawerItem drawerItemSetting;
    private IDrawerItem drawerItemAuthor;

    // Activity lifecycle start
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        MqttPublishService.getInstance(this);
        if (DateFormat.is24HourFormat(this)) {
            mWatchTime = new SimpleDateFormat("HH:mm", Locale.getDefault());
        } else {
            mWatchTime = new SimpleDateFormat("hh:mm", Locale.getDefault());
        }
        mWatchDate = new SimpleDateFormat("MMMM dd", Locale.getDefault());
        mWatchDay = new SimpleDateFormat("EEEE", Locale.getDefault());
        initializeViews();
        initializeFragment();
        setListeners();
        buildDrawer(savedInstanceState);
        registerReceiver();
        startService(new Intent(this, TimeTickService.class));
    }

    private void registerReceiver() {
        mqttSubscribeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("intent.action.MQTT_RECEIVER")){
                    String current = getMessageNum();
                    String plusNum = String.valueOf(Integer.valueOf(current)+1);
                    LoginInfoSharePreference.getInstancce(AlarmActivity.this).putData("messageNum",plusNum);
                    result.updateBadge(drawerItemMessage.getIdentifier(),new StringHolder(plusNum));
                }
            }
        };
        intentFilter = new IntentFilter();
        intentFilter.addAction("intent.action.MQTT_RECEIVER");
        registerReceiver(mqttSubscribeReceiver,intentFilter);
    }

    private void buildDrawer(Bundle savedInstanceState) {
        String messageNum = getMessageNum();

        drawerItemLogout = new PrimaryDrawerItem().withName(R.string.drawer_item_logout).withIcon(GoogleMaterial.Icon.gmd_sun).withIdentifier(1);
        drawerItemMessage = new PrimaryDrawerItem().withName(R.string.drawer_item_message).withIcon(FontAwesome.Icon.faw_home).withBadge(messageNum).withBadgeStyle(new BadgeStyle(Color.RED, Color.RED)).withIdentifier(2);
        drawerItemSetting =  new PrimaryDrawerItem().withName(R.string.drawer_item_setting).withIcon(FontAwesome.Icon.faw_eye).withIdentifier(3);
        drawerItemAuthor = new SecondaryDrawerItem().withName(R.string.drawer_item_Author).withIcon(FontAwesome.Icon.faw_github);
        final IProfile profile = new ProfileDrawerItem().withName("Mike Penz").withEmail("mikepenz@gmail.com").withIcon("https://avatars3.githubusercontent.com/u/1476232?v=3&s=460");
        final IProfile profile2 = new ProfileDrawerItem().withName("Bernat Borras").withEmail("alorma@github.com").withIcon(Uri.parse("https://avatars3.githubusercontent.com/u/887462?v=3&s=460"));

        // Create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        profile, profile2
                )
                .withSavedInstance(savedInstanceState)
                .build();

        //Create the drawer

        result = new DrawerBuilder()
                .withActivity(this)
                /*.withToolbar(toolbar)*/
                .withHasStableIds(true)
//                .withDrawerLayout(R.layout.crossfade_drawer)

//                .withGenerateMiniDrawer(true)
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        drawerItemLogout ,
                        drawerItemMessage,
                       /* new PrimaryDrawerItem().withName(R.string.drawer_item_contact).withIcon(FontAwesome.Icon.faw_gamepad).withIdentifier(3),*/
                        drawerItemSetting,
                        new SectionDrawerItem().withName(R.string.drawer_item_more),
                        drawerItemAuthor
                ) // add the items we want to use with our Drawer
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem instanceof Nameable) {
                            //Toast.makeText(AlarmActivity.this, ((Nameable) drawerItem).getName().getText(AlarmActivity.this), Toast.LENGTH_SHORT).show();
                            if (drawerItem==drawerItemLogout){
                                startActivity(new Intent(AlarmActivity.this,LoginActivity.class));
                                finish();
                            }else if(drawerItem == drawerItemMessage){
                                startActivity(new Intent(AlarmActivity.this,ReceiveMessageActivity.class));
                            }else if (drawerItem == drawerItemAuthor){
                                Toast.makeText(AlarmActivity.this, "杨鑫森", Toast.LENGTH_SHORT).show();
                            }
                        }
                        switch (position){
                            case 0:
                                break;
                            case 1:
                                break;
                            case 2:
                                break;
                            case 3:

                                break;
                            case 4:
                                Toast.makeText(AlarmActivity.this, "杨鑫森", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        //we do not consume the event and want the Drawer to continue with the event chain
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)
                .build();


        //get the CrossfadeDrawerLayout which will be used as alternative DrawerLayout for the Drawer
        //the CrossfadeDrawerLayout library can be found here: https://github.com/mikepenz/CrossfadeDrawerLayout
       /* crossfadeDrawerLayout = (CrossfadeDrawerLayout) result.getDrawerLayout();

        //define maxDrawerWidth
        crossfadeDrawerLayout.setMaxWidthPx(DrawerUIUtils.getOptimalDrawerWidth(this));
        //add second view (which is the miniDrawer)
        final MiniDrawer miniResult = result.getMiniDrawer();
        //build the view for the MiniDrawer
        View view = miniResult.build(this);
        //set the background of the MiniDrawer as this would be transparent
        view.setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(this, com.mikepenz.materialdrawer.R.attr.material_drawer_background, com.mikepenz.materialdrawer.R.color.material_drawer_background));
        //we do not have the MiniDrawer view during CrossfadeDrawerLayout creation so we will add it here
        crossfadeDrawerLayout.getSmallView().addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        //define the crossfader to be used with the miniDrawer. This is required to be able to automatically toggle open / close
        miniResult.withCrossFader(new ICrossfader() {
            @Override
            public void crossfade() {
                boolean isFaded = isCrossfaded();
                crossfadeDrawerLayout.crossfade(400);

                //only close the drawer if we were already faded and want to close it now
                if (isFaded) {
                    result.getDrawerLayout().closeDrawer(GravityCompat.START);
                }
            }

            @Override
            public boolean isCrossfaded() {
                return crossfadeDrawerLayout.isCrossfaded();
            }
        });*/
    }

    @NonNull
    private String getMessageNum() {
        String messageNum = LoginInfoSharePreference.getInstancce(this).getData("messageNum");
        if (TextUtils.isEmpty(messageNum)){
            messageNum = "0";
        }
        return messageNum;
    }

    @Override protected void onStart() {
        super.onStart();
        mRealm = Realm.getDefaultInstance();
        mTimeBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context ctx, Intent intent) {
                // get the current date;
                Date currentDate = new Date();
                // if it's a time tick update time
                if (intent.getAction().compareTo(Intent.ACTION_TIME_TICK) == 0) {
                    mTime.setText(mWatchTime.format(currentDate));
                }
                // if date is changed, update other textviews as well
                if (intent.getAction().compareTo(Intent.ACTION_DATE_CHANGED) == 0){
                    mDate.setText(mWatchDate.format(currentDate));
                    mDay.setText(mWatchDay.format(currentDate));
                }

            }
        };

        registerReceiver(mTimeBroadcastReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
    }

    @Override protected void onResume(){
        super.onResume();

        // set date
        setCurrentDate();

        // set time type
        RemindTaskWrapper.setIs24Hours(DateFormat.is24HourFormat(this));
    }

    @Override protected void onPause() {
        super.onPause();
    }

    @Override protected void onStop() {
        super.onStop();

        // if receiver exists, unregister receiver
        if (mTimeBroadcastReceiver != null)
            unregisterReceiver(mTimeBroadcastReceiver);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
        unregisterReceiver(mqttSubscribeReceiver);
    }
    // Activity lifecycle end

    private void initializeViews() {

        // floating action button initialization
        mAddFab = (FloatingActionButton) findViewById(R.id.fab_add_alarm);

        // date and time initialization
        // mTime textview initialization, add initial time
        mTime = (TextView) findViewById(R.id.current_time);

        // mDate textview initialization, add initial date
        mDate = (TextView) findViewById(R.id.current_date);

        // mDay textview initialization, add initial day
        mDay = (TextView) findViewById(R.id.current_day);

    }

    private void initializeFragment() {
        mFragmentTransaction = mFragmentManager.beginTransaction();
        AlarmViewFragment alarmViewFragment = new AlarmViewFragment();
        AddAlarmFragment addAlarmFragment = new AddAlarmFragment();
        mFragmentTransaction.add(R.id.fragment_holder, alarmViewFragment, ALARM_VIEW_FRAGMENT);
        mFragmentTransaction.add(R.id.fragment_holder, addAlarmFragment, ALARM_ADD_FRAGMENT);
        mFragmentTransaction.commit();
    }

    private void setListeners() {
        // initialize listener for add fab
        AddAlarmFabListener fabListener = new AddAlarmFabListener(this);
        mAddFab.setOnClickListener(fabListener);
    }

    private void setCurrentDate() {
        // get current date
        Date currentDate = new Date();

        // set
        mTime.setText(mWatchTime.format(currentDate));
        mDate.setText(mWatchDate.format(currentDate));
        mDay.setText(mWatchDay.format(currentDate));
    }

    public void replaceWithAddFragment() {
        disableFab(true);
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
        mFragmentTransaction.show(mFragmentManager.findFragmentByTag(ALARM_ADD_FRAGMENT)).addToBackStack(ALARM_VIEW_FRAGMENT);
        mFragmentTransaction.hide(mFragmentManager.findFragmentByTag(ALARM_VIEW_FRAGMENT));
        mFragmentTransaction.commit();
    }

    public void disableFab(boolean animate){
        mAddFab.setClickable(false);
        if (animate) {
            Animation disableAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_out);
            disableAnimation.setAnimationListener(new FabDisableAnimationListener(mAddFab));
            mAddFab.setAnimation(disableAnimation);
        } else {
            mAddFab.setVisibility(View.INVISIBLE);
        }
    }

    public void enableFab(boolean animate){
        if (animate) {
            Animation enableAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_in);
            enableAnimation.setAnimationListener(new FabEnableAnimationListener(mAddFab));
            mAddFab.setAnimation(enableAnimation);
        } else {
            mAddFab.setVisibility(View.VISIBLE);
        }
        mAddFab.setClickable(true);
    }

    public void showTimePickerDialog(AlarmTimeSetListener alarmTimeSetListener) {
        DialogFragment newFragment = new TimePickerFragment();
        Bundle args = new Bundle();
        args.putParcelable("RemindTask Set Listener", alarmTimeSetListener);
        newFragment.setArguments(args);
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public void addAlarm(RemindTaskWrapper alarm) {
        mFragmentTransaction = mFragmentManager.beginTransaction();
        AlarmViewFragment alarmViewFragment = (AlarmViewFragment) mFragmentManager.findFragmentByTag(ALARM_VIEW_FRAGMENT);
        mFragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        mFragmentTransaction.hide(mFragmentManager.findFragmentByTag(ALARM_ADD_FRAGMENT));
        enableFab(true);
        mFragmentTransaction.show(mFragmentManager.findFragmentByTag(ALARM_VIEW_FRAGMENT));
        mFragmentTransaction.commit();
        alarmViewFragment.addAlarm(alarm);
    }

    @Override public void onBackPressed() {
        if (mFragmentManager.findFragmentByTag(ALARM_ADD_FRAGMENT).isVisible()) {
            enableFab(true);
        }
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = result.saveInstanceState(outState);
        //add the values which need to be saved from the accountHeader to the bundle
        outState = headerResult.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

}
