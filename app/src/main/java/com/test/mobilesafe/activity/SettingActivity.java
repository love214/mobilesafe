package com.test.mobilesafe.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.test.mobilesafe.R;
import com.test.mobilesafe.service.AddressService;
import com.test.mobilesafe.service.BlackNumService;
import com.test.mobilesafe.ui.SettingClickView;
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
    private SettingClickView scv_setting_changebg;
    private SettingClickView scv_setting_location;
    private SettingView sv_setting_blacknum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        setContentView(R.layout.activity_setting);
        sp=getSharedPreferences("config",MODE_PRIVATE);//保存设置信息
        sv_setting_update = (SettingView) findViewById(R.id.sv_setting_update);
        sv_setting_address = (SettingView) findViewById(R.id.sv_setting_address);
        scv_setting_changebg = (SettingClickView) findViewById(R.id.scv_setting_changebg);
        scv_setting_location = (SettingClickView) findViewById(R.id.scv_setting_location);
        sv_setting_blacknum = (SettingView) findViewById(R.id.sv_setting_blacknum);
        //sv_setting_update.setTitle("提示更新");
        update();
        address();
        changebg();
        location();
    }

    /**
     * 归属地提示位置
     */
    private void location() {
        scv_setting_location.setTitle("归属地提示框位置");
        scv_setting_location.setDes("设置归属地提示框的显示位置");
        scv_setting_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DragViewActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        //当界面可见的时候，刷新一下服务是否开启
        super.onStart();
        address();
        blackNum();
    }

    /**
     * 黑名单拦截的操作
     */
    private void blackNum() {
        if (AddressUtils.isRunningService(mContext,"com.test.mobilesafe.service.BlackNumService")){
            //服务已经开启
            sv_setting_blacknum.setChecked(true);
        }else {
            //服务没有开启
            sv_setting_blacknum.setChecked(false);
        }
        sv_setting_blacknum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, BlackNumService.class);
                if (sv_setting_blacknum.isChecked()){
                    //关闭显示归属地
                    stopService(intent);
                    sv_setting_blacknum.setChecked(false);
                }else {
                    //打开显示归属地
                    startService(intent);
                    sv_setting_blacknum.setChecked(true);
                }
            }
        });
    }

    /**
     * 设置归属地提示框的颜色
     */
    private void changebg() {
        final String[] items={"半透明","活力橙","卫士蓝","金属灰","苹果绿"};
        scv_setting_changebg.setTitle("归属地提示框风格");
        scv_setting_changebg.setDes(items[sp.getInt("which", 0)]); //恢复上次选中的风格
        scv_setting_changebg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
                builder.setIcon(R.mipmap.ic_launcherweb);
                builder.setTitle("归属地提示框风格");
                //设置取消按钮
                builder.setNegativeButton("取消", null);//因为只是隐藏对话框，所以不需要第二个参数
                int spInt = sp.getInt("which", 0);//恢复上次选中的风格
                builder.setSingleChoiceItems(items, spInt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("which",which);
                        editor.commit();
                        //which ：选中的索引值
                        //设置描述信息
                        scv_setting_changebg.setDes(items[which]);
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });

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
