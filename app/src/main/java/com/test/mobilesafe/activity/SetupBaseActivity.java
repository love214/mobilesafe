package com.test.mobilesafe.activity;

import android.app.Activity;
import android.content.Context;
import android.view.View;

/**
 * Created by Huyanglin on 2016/8/13.
 * 创建一个setup基类
 */
public abstract class SetupBaseActivity extends Activity {
    private Context mContext;

    public void previous(View view){
        //跳转到上一个界面
        pre_activity();
    }

    public void next(View view){
        next_activity();
    }

    public abstract void next_activity();
    public abstract void pre_activity();

}
