package com.sairijal.remindtask.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.sairijal.remindtask.R;
import com.sairijal.remindtask.sharedpreferences.LoginInfoSharePreference;

import java.io.FileOutputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PerfectInfoActivity extends AppCompatActivity {

    private static final int MSG_SAVE_SUCCESS = 0;
    @Bind(R.id.realName_ET)
    EditText realNameET;
    @Bind(R.id.phone_ET)
    EditText phoneET;
    ImageView headImgIV;
    @Bind(R.id.setPw_ET)
    EditText setPwET;
    @Bind(R.id.sex)
    EditText sexET;
    @Bind(R.id.address_ET)
    EditText addressET;
    @Bind(R.id.edit_BT)
    Button editBT;
    private ActionBar actionBar;
    private Bitmap bitmap;
    private boolean isEdit = false;
    private String fileName;
    Toolbar toolbar;
    FileOutputStream fout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_perfect);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        actionBar.setTitle("注册");
        actionBar.setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.edit_BT)
    public void onClick() {

        String name = realNameET.getText().toString();
        String phone = phoneET.getText().toString();
        if (phone.length()!=11){
            Toast.makeText(this,"请输入11位手机号",Toast.LENGTH_LONG).show();
        }
        String pw = setPwET.getText().toString();
        String address = addressET.getText().toString();
        String sex = sexET.getText().toString();
        LoginInfoSharePreference.getInstancce(this).putData("registerName",name);
        LoginInfoSharePreference.getInstancce(this).putData("registerPhone",phone);
        LoginInfoSharePreference.getInstancce(this).putData("registerPw",pw);
        LoginInfoSharePreference.getInstancce(this).putData("registerSex",sex);
        LoginInfoSharePreference.getInstancce(this).putData("registerAddress",address);
        Toast.makeText(this,"注册成功",Toast.LENGTH_LONG).show();
        finish();
    }
}
