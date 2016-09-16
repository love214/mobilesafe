package site.wanter.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import site.wanter.mobilesafe.R;

/**
 * Created by Huyanglin on 2016/8/13.
 */
public class SettingView extends RelativeLayout {

    private TextView tv_setting_title;
    private TextView tv_setting_des;
    private CheckBox cb_setting_update;
    private String des_on;
    private String des_off;

    public SettingView(Context context) {
        super(context);
        init();
    }

    //attrs保存有控件的所有属性
    public SettingView(Context context, AttributeSet attrs) {
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
        if (isChecked()){
            tv_setting_des.setText(des_on);
        }else {
            tv_setting_des.setText(des_off);
        }
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
        if (isChecked()){
            tv_setting_des.setText(des_on);
        }else {
            tv_setting_des.setText(des_off);
        }
    }

    //获取checkbox的状态
    public Boolean isChecked(){
        return cb_setting_update.isChecked();
    }
}
