package com.sairijal.remindtask.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.sairijal.remindtask.R;
import com.sairijal.remindtask.sharedpreferences.LoginInfoSharePreference;

public class LaunchActivity extends AppCompatActivity {
    protected static final int CODE_UPDATE_DIALOG = 0;
    protected static final int CODE_URL_ERROR = 1;
    protected static final int CODE_NET_ERROR = 2;
    protected static final int CODE_JSON_ERROR = 3;
    protected static final int CODE_ENTER_HOME = 4;// 进入主页面

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_launch);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (LoginInfoSharePreference.getInstancce(LaunchActivity.this).getData("userName").equals("")){
                    launchLoginActivity();
                }else {
                    launchMainActivity();
                }
                finish();
            }
        }, 2900);
    }

    private void launchMainActivity() {
        startActivity(new Intent(this,RemindTaskActivity.class));
    }

    private void launchLoginActivity() {
        startActivity(new Intent(this,LoginActivity.class));
    }
}
