package com.sairijal.remindtask.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sairijal.remindtask.R;
import com.sairijal.remindtask.sharedpreferences.LoginInfoSharePreference;


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

                if (mUsername_ET.getText().toString().length()==11) {
                    if (LoginInfoSharePreference.getInstancce(this).getData("registerPhone").equals(mUsername_ET.getText().toString())){
                        LoginInfoSharePreference.getInstancce(this).putData("userName", mUsername_ET.getText().toString());
                    }else {
                        Toast.makeText(this,"请输入已注册的手机号",Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                else{
                    Toast.makeText(this,"请输入11位手机号",Toast.LENGTH_LONG).show();
                    return;
                }

                if (!TextUtils.isEmpty(mPassword_ET.getText().toString())) {
                    LoginInfoSharePreference.getInstancce(this).putData("password", mPassword_ET.getText().toString());
                }else{
                    Toast.makeText(this,"密码不能为空",Toast.LENGTH_LONG).show();
                    return;
                }
                if (mUsername_ET.getText().toString().length()==11&&!TextUtils.isEmpty(mPassword_ET.getText().toString())){
                    if (LoginInfoSharePreference.getInstancce(this).getData("registerPhone").equals(mUsername_ET.getText().toString())&&
                            LoginInfoSharePreference.getInstancce(this).getData("registerPw").equals(mPassword_ET.getText().toString()) ){

                        LoginInfoSharePreference.getInstancce(this).putData("subscribeTopic"
                                ,mUsername_ET.getText().toString());
                        LoginInfoSharePreference.getInstancce(this).putData("clientId"
                                ,String.valueOf((
                                        mUsername_ET.getText().toString() + mPassword_ET.getText().toString())
                                        .hashCode()));
                        launchMainActivity();
                        finish();
                    }else{
                        Toast.makeText(this,"用户名或密码不正确",Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.register_BT:
                startActivity(new Intent(this,PerfectInfoActivity.class));
                break;
        }
    }

    private void launchMainActivity() {
        startActivity(new Intent(this,RemindTaskActivity.class));
    }
}

