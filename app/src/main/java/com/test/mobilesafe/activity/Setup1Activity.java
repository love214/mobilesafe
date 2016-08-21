package com.test.mobilesafe.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.test.mobilesafe.R;

/**
 * Created by Huyanglin on 2016/8/13.
 * 手机防盗引导界面
 */
public class Setup1Activity extends SetupBaseActivity {

    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        setContentView(R.layout.activity_setup1);
    }

    @Override
    public void next_activity() {
        //跳转到下一个界面
        Intent intent = new Intent(mContext, Setup2Activity.class);
        startActivity(intent);
        finish();
        //执行平移动画
        overridePendingTransition(R.anim.setup_enter_next,R.anim.setup_exit_next);
    }

    public void pre_activity(){};
}
