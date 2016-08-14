package com.test.mobilesafe.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.test.mobilesafe.R;

/**
 * Created by Huyanglin on 2016/8/13.
 * 手机防盗模块
 */
public class LostfindActivity extends Activity{
    private SharedPreferences sp;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;

        //分为两部分 1.显示设置过的手机防盗功能  2.设置防盗功能
        sp=getSharedPreferences("config",MODE_PRIVATE);
        if (sp.getBoolean("first",true)){
            //第一次进入
            Intent intent = new Intent(mContext, Setup1Activity.class);
            startActivity(intent);
            finish();
        }else {
            //不是第一次进入
            setContentView(R.layout.activity_lostfind);
        }

    }
}
