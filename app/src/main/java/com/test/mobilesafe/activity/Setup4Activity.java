package com.test.mobilesafe.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.test.mobilesafe.R;

/**
 * Created by Huyanglin on 2016/8/13.
 */
public class Setup4Activity extends SetupBaseActivity{
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        setContentView(R.layout.activity_setup4);
    }


    @Override
    public void next_activity() {
        Intent intent = new Intent(mContext, LostfindActivity.class);
        startActivity(intent);
        //保存用户已经不是第一次进入的状态
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("first",false);
        editor.commit();
        finish();
    }

    @Override
    public void pre_activity() {
        //跳转到上一个界面
        Intent intent = new Intent(mContext, Setup3Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.setup_enter_pre,R.anim.setup_exit_pre);
    }


}
