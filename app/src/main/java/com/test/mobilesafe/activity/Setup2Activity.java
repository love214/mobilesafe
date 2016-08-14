package com.test.mobilesafe.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.test.mobilesafe.R;

/**
 * Created by Huyanglin on 2016/8/13.
 */
public class Setup2Activity extends SetupBaseActivity {
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        setContentView(R.layout.activity_setup2);
    }

    @Override
    public void next_activity() {
        //跳转到第三个界面
        Intent intent = new Intent(mContext, Setup3Activity.class);
        startActivity(intent);
    }

    @Override
    public void pre_activity() {
        //跳转到上一个界面
        Intent intent = new Intent(mContext, Setup1Activity.class);
        startActivity(intent);
    }

}