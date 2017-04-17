package com.sairijal.alarm.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 11255 on 2017/4/11.
 */

public class LoginInfoSharePreference {
    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private static LoginInfoSharePreference instance;
    private LoginInfoSharePreference(Context context){
        mContext = context;
        mSharedPreferences = mContext.getSharedPreferences("loginInfo",Context.MODE_PRIVATE);
    }

    public static LoginInfoSharePreference getInstancce(Context context){
        if (instance == null){
            synchronized (context) {
                if (instance == null) {
                    instance = new LoginInfoSharePreference(context);
                }
            }
        }
        return instance;
    }

    public void putData(String key, String value){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getData(String key){
        return mSharedPreferences.getString(key,"");
    }
}
