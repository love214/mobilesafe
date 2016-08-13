package com.test.mobilesafe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.test.mobilesafe.R;

/**
 * Created by Huyanglin on 2016/8/13.
 */
public class SettingView extends RelativeLayout {

    private TextView tv_setting_title;
    private TextView tv_setting_des;
    private CheckBox cb_setting_update;

    public SettingView(Context context) {
        super(context);
        init();
    }

    public SettingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SettingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        //添加布局文件
//        View view = View.inflate(getContext(), R.layout.setting,null);
//        this.addView(view);
        View view = View.inflate(getContext(), R.layout.settingview, this);
        //获取控件
        tv_setting_title = (TextView) view.findViewById(R.id.tv_setting_title);
        tv_setting_des = (TextView) view.findViewById(R.id.tv_setting_des);
        cb_setting_update = (CheckBox)view.findViewById(R.id.cb_setting_update);
    }

    //需要添加一些方法，方便修改其中空间的属性或者值
    //设置标题的方法
    public void setTitle(String title){
        tv_setting_title.setText(title);
    }

    //设置描述信息
    public void setDes(String des){
        tv_setting_des.setText(des);
    }
    //设置选中状态
    public void setChecked(boolean checked){
        cb_setting_update.setChecked(checked);
    }

    //获取checkbox的状态
    public Boolean isChecked(){
        return cb_setting_update.isChecked();
    }
}
