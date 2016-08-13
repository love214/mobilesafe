package com.test.mobilesafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import com.test.mobilesafe.R;
import com.test.mobilesafe.ui.SettingView;

/**
 * Created by Huyanglin on 2016/8/12.
 */
public class SettingActivity extends Activity {

    private SettingView sv_setting_update;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        sp=getSharedPreferences("config",MODE_PRIVATE);//保存设置信息
        sv_setting_update = (SettingView) findViewById(R.id.sv_setting_update);
        //sv_setting_update.setTitle("提示更新");
        //恢复上次设置的状态
        if (sp.getBoolean("update",true)){
            //sv_setting_update.setDes("打开提示更新");
            sv_setting_update.setChecked(true);
        }else {
            //sv_setting_update.setDes("关闭提示更新");
            sv_setting_update.setChecked(false);
        }

        sv_setting_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor=sp.edit();
                //更改状态
                //根据checkbox现有的状态改变
                if (sv_setting_update.isChecked()){
                    //关闭提示更新
                    //sv_setting_update.setDes("关闭提示更新");
                    sv_setting_update.setChecked(false);
                    //保存状态
                    editor.putBoolean("update",false);
                }else {
                    //打开提示更新
                    //sv_setting_update.setDes("打开提示更新");
                    sv_setting_update.setChecked(true);
                    editor.putBoolean("update",true);
                }
                editor.commit();
            }
        });
    }
}
