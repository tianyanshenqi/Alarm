package com.sairijal.alarm.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sairijal.alarm.R;
import com.sairijal.alarm.sharedpreferences.LoginInfoSharePreference;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import static android.os.AsyncTask.THREAD_POOL_EXECUTOR;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressBar mLogin_progressbar;
    private LinearLayout mLogin_form;
    private EditText mUsername_ET;
    private EditText mPassword_ET;
    private Button mLogin_BT;
    private Button mRegister_BT;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        mLogin_progressbar = (ProgressBar) findViewById(R.id.login_progressbar);
        mLogin_form = (LinearLayout) findViewById(R.id.login_form);
        mUsername_ET = (EditText) findViewById(R.id.username_ET);
        mPassword_ET = (EditText) findViewById(R.id.password_ET);
        mLogin_BT = (Button) findViewById(R.id.login_BT);
        mLogin_BT.setOnClickListener(this);
        mRegister_BT = (Button) findViewById(R.id.register_BT);
        mRegister_BT.setOnClickListener(this);
        mButton = (Button) findViewById(R.id.button);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_BT:
                if (!TextUtils.isEmpty(mUsername_ET.getText().toString())) {
                    LoginInfoSharePreference.getInstancce(this).putData("userName", mUsername_ET.getText().toString());
                }else{
                    Toast.makeText(this,"手机号不能为空",Toast.LENGTH_LONG).show();
                }
                if (!TextUtils.isEmpty(mPassword_ET.getText().toString())) {
                    LoginInfoSharePreference.getInstancce(this).putData("password", mPassword_ET.getText().toString());
                }else{
                    Toast.makeText(this,"密码不能为空",Toast.LENGTH_LONG).show();
                }
                if (!TextUtils.isEmpty(mUsername_ET.getText().toString())&&!TextUtils.isEmpty(mPassword_ET.getText().toString())){
                    LoginInfoSharePreference.getInstancce(this).putData("subscribeTopic"
                            ,mUsername_ET.getText().toString());
                    LoginInfoSharePreference.getInstancce(this).putData("clientId"
                            ,String.valueOf((
                                    mUsername_ET.getText().toString() + mPassword_ET.getText().toString())
                                    .hashCode()));
                    launchMainActivity();
                    finish();
                }
                break;
            case R.id.register_BT:
                break;
        }
    }

    private void launchMainActivity() {
        startActivity(new Intent(this,AlarmActivity.class));
    }
}

