package com.test.mobilesafe.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.test.mobilesafe.R;
import com.test.mobilesafe.utils.MD5Util;

/**
 * Created by Huyanglin on 2016/8/10.
 */
public class HomeActivity extends Activity {

    private GridView gv_home_gridview;
    private AlertDialog dialog = null;
    private Context mContext;
    private SharedPreferences sp;
    boolean isHide=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        sp=getSharedPreferences("config",MODE_PRIVATE);
        mContext=this;
        gv_home_gridview = (GridView) findViewById(R.id.gv_home_gridview);
        gv_home_gridview.setAdapter(new MyAdpater());
        gv_home_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //根据条目的位置判断用户点击的条目
                switch(position){
                    case 0:
                        //跳转手机防盗模块，需要密码跳转
                        //判断用户是否第一次打开，是第一次，设置密码；再次点击，输入密码，密码正确，才能进入手机防盗
                        //判断用户是否设置过密码
                        if (TextUtils.isEmpty(sp.getString("password",""))){
                            //设置密码
                            showSetPassWordDialog();
                        }else {
                            //输入密码
                            showEnterPassWordDialog();
                        }

                        break;

                    case 8 :
                        Intent intent = new Intent(mContext, SettingActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });

    }

    private void showEnterPassWordDialog() {
        //输入密码对话框
        AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
        builder.setCancelable(false);//不能通过触摸空白处隐藏对话框
        View view = View.inflate(mContext, R.layout.dialog_enterpassword, null);
        final EditText et_enterpassword_password = (EditText) view.findViewById(R.id.et_enterpassword_password);
        builder.setView(view);
        ImageView iv_enterpassword_hide = (ImageView)view.findViewById(R.id.iv_enterpassword_hide);
        iv_enterpassword_hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //隐藏/显示密码
                if (isHide){
                    //隐藏密码
                    et_enterpassword_password.setInputType(129);
                    isHide=false;
                }else {
                    //显示密码
                    et_enterpassword_password.setInputType(0);
                    isHide=true;
                }

            }
        });
        Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
        Button btn_cancle = (Button) view.findViewById(R.id.btn_cancle);
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消按钮事件
                dialog.dismiss();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取输入的密码
                String password = et_enterpassword_password.getText().toString().trim();
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(mContext, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    String sp_password = sp.getString("password", "");
                    if (MD5Util.passwordMD5(password).equals(sp_password)){
                        //跳转到手机防盗界面
                        Intent intent = new Intent(mContext, LostfindActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                        Toast.makeText(mContext, "密码正确", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(mContext, "密码错误", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    private void showSetPassWordDialog() {

        //设置密码
        AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
        builder.setCancelable(false);//不能通过触摸空白处隐藏对话框
        View view = View.inflate(mContext, R.layout.dialog_setpassword, null);
        final EditText et_setpassword_password = (EditText) view.findViewById(R.id.et_setpassword_password);
        final EditText et_setpassword_confrim = (EditText) view.findViewById(R.id.et_setpassword_confrim);
        Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
        Button btn_cancle = (Button) view.findViewById(R.id.btn_cancle);
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消按钮事件
                dialog.dismiss();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //确定按钮事件
                String pass = et_setpassword_password.getText().toString().trim();
                String confrim = et_setpassword_confrim.getText().toString().trim();
                if (TextUtils.isEmpty(pass)){
                    Toast.makeText(mContext, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pass.equals(confrim)){
                    //判断两次密码是否一致,一致则保存密码，隐藏对话框
                    dialog.dismiss();
                    Toast.makeText(mContext, "密码设置成功", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("password", MD5Util.passwordMD5(pass));
                    editor.commit();
                }else {
                    Toast.makeText(mContext, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setView(view);
        dialog = builder.create();
        dialog.show();

    }

    private class MyAdpater extends BaseAdapter{

        int[] imageId = { R.drawable.safe, R.drawable.callmsgsafe, R.drawable.app,
                R.drawable.taskmanager, R.drawable.netmanager, R.drawable.trojan,
                R.drawable.sysoptimize, R.drawable.atools, R.drawable.settings };
        String[] names = { "手机防盗", "通讯卫士", "软件管理", "进程管理", "流量统计", "手机杀毒", "缓存清理",
                "高级工具", "设置中心" };

        @Override
        public int getCount() {
            return 9;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //设置条目的样式
            View view = View.inflate(mContext, R.layout.item_home, null);
//            初始化控件
            ImageView iv_ithema_icon = (ImageView) view.findViewById(R.id.iv_ithema_icon);
            TextView tv_ithema_text = (TextView) view.findViewById(R.id.tv_ithema_text);
            iv_ithema_icon.setImageResource(imageId[position]);
            tv_ithema_text.setText(names[position]);
            return view;
        }

        @Override
        public Object getItem(int position) {

            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }



    }
}