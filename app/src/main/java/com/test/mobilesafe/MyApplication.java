package com.test.mobilesafe;

import android.app.Application;
import android.content.Context;

/**
 * Created by Huyanglin on 2016/9/16.
 */
public class MyApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext=getApplicationContext();
    }

    public static Context getContext(){
        return mContext;
    }
}
