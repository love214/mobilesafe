package com.test.mobilesafe.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.test.mobilesafe.R;

/**
 * Created by Huyanglin on 2016/8/13.
 */
public class Setup4Activity extends SetupBaseActivity{
    private Context mContext;
    private CheckBox cb_setup4_protect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        setContentView(R.layout.activity_setup4);
        cb_setup4_protect = (CheckBox) findViewById(R.id.cb_setup4_protect);
        if (sp.getBoolean("protected",false)){
            //开启了防盗保护
            cb_setup4_protect.setText("你已经开启了防盗保护");
            cb_setup4_protect.setChecked(true);
        }else {
            cb_setup4_protect.setText("你还没有开启防盗保护");
            cb_setup4_protect.setChecked(false);
        }
        cb_setup4_protect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = sp.edit();
                //注意：isChecked是点击之后的值
                if (isChecked){
                    //开启防盗保护
                    cb_setup4_protect.setText("你已经开启了防盗保护");
                    cb_setup4_protect.setChecked(true);
                    editor.putBoolean("protected",true);
                }else {
                    //关闭防盗保护
                    cb_setup4_protect.setText("你还没有开启防盗保护");
                    cb_setup4_protect.setChecked(false);
                    editor.putBoolean("protected",false);
                }
                editor.commit();
            }
        });
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
