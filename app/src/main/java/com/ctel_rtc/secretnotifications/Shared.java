package com.ctel_rtc.secretnotifications;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDex;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ctel-cpu-50 on 6/1/2016.
 */
public class Shared extends Application {


    SharedPreferences preference;
    SharedPreferences.Editor editor;
    Context mContext;

    public Shared() {

    }

    public Shared(Context context) {
        // TODO Auto-generated constructor stub
        mContext = context;

        preference = mContext.getSharedPreferences("fileName", Context.MODE_PRIVATE);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
//		preference = mContext.getSharedPreferences("fileName", Context.MODE_PRIVATE);
//		preference = PreferenceManager.getDefaultSharedPreferences(this);
    }


    public String getIsUploaded() {

        return preference.getString("IsUploaded", "false");
    }

    public void setIsUploaded(String IsUploaded) {
        editor = preference.edit();
        editor.putString("IsUploaded", IsUploaded);
        editor.commit();
    }

    public Integer getNoti() {

        return preference.getInt("Noti", 0);
    }

    public void setNoti(Integer noti) {
        editor = preference.edit();
        editor.putInt("Noti", noti);
        editor.commit();
    }


    public String getIsFirst() {

        return preference.getString("IsFirst", "true");
    }

    public void setIsFirst(String IsFirst) {
        editor = preference.edit();
        editor.putString("IsFirst", IsFirst);
        editor.commit();
    }


    public String getUserName() {

        return preference.getString("UserName", "true");
    }

    public void setUserName(String UserName) {
        editor = preference.edit();
        editor.putString("UserName", UserName);
        editor.commit();
    }

}

