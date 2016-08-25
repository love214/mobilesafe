package com.test.mobilesafe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.test.mobilesafe.R;

/**
 * Created by Huyanglin on 2016/8/13.
 */
public class SettingClickView extends RelativeLayout {

    private TextView tv_setting_title;
    private TextView tv_setting_des;
    private String des_on;
    private String des_off;

    public SettingClickView(Context context) {
        super(context);
        init();
    }

    //attrs保存有控件的所有属性
    public SettingClickView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
//        int count = attrs.getAttributeCount();
//        for (int i=0;i<count;i++){
//            //获取每个属性的值
//            String attributeValue = attrs.getAttributeValue(i);
//
//        }

        String title = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "title");
        des_on = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "des_on");
        des_off = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "des_off");
        tv_setting_title.setText(title);

    }

    public SettingClickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        //添加布局文件
//        View view = View.inflate(getContext(), R.layout.setting,null);
//        this.addView(view);
        View view = View.inflate(getContext(), R.layout.settingclickview, this);
        //获取控件
        tv_setting_title = (TextView) view.findViewById(R.id.tv_setting_title);
        tv_setting_des = (TextView) view.findViewById(R.id.tv_setting_des);

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



}
