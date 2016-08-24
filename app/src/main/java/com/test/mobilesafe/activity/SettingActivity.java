package com.test.mobilesafe.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.test.mobilesafe.R;
import com.test.mobilesafe.service.AddressService;
import com.test.mobilesafe.ui.SettingView;
import com.test.mobilesafe.utils.AddressUtils;

/**
 * Created by Huyanglin on 2016/8/12.
 */
public class SettingActivity extends Activity {

    private SettingView sv_setting_update;
    private SettingView sv_setting_address;
    private SharedPreferences sp;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        setContentView(R.layout.activity_setting);
        sp=getSharedPreferences("config",MODE_PRIVATE);//保存设置信息
        sv_setting_update = (SettingView) findViewById(R.id.sv_setting_update);
        sv_setting_address = (SettingView) findViewById(R.id.sv_setting_address);
        //sv_setting_update.setTitle("提示更新");
        update();
        address();
    }

    @Override
    protected void onStart() {
        //当界面可见的时候，刷新一下服务是否开启
        super.onStart();
        address();
    }

    /**
     * 开启归属地显示的操作
     */
    private void address(){
        if (AddressUtils.isRunningService(mContext,"com.test.mobilesafe.service.AddressService")){
            //服务已经开启
            sv_setting_address.setChecked(true);
        }else {
            //服务没有开启
            sv_setting_address.setChecked(false);
        }
        sv_setting_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AddressService.class);
                if (sv_setting_address.isChecked()){
                    //关闭显示归属地
                    stopService(intent);
                    sv_setting_address.setChecked(false);
                }else {
                    //打开显示归属地
                    startService(intent);
                    sv_setting_address.setChecked(true);
                }
            }
        });
    }

    /**
     * 提示更新的操作
     */
    private void update(){
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
