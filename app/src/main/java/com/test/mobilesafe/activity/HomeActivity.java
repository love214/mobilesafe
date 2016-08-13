package com.test.mobilesafe.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.mobilesafe.R;

/**
 * Created by Huyanglin on 2016/8/10.
 */
public class HomeActivity extends Activity {

    private  GridView gv_home_gridview;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        mContext=this;
        gv_home_gridview = (GridView) findViewById(R.id.gv_home_gridview);
        gv_home_gridview.setAdapter(new MyAdpater());
        gv_home_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //根据条目的位置判断用户点击的条目
                switch(position){
                    case 8 :
                        Intent intent = new Intent(mContext, SettingActivity.class);
                        startActivity(intent);

                        break;



                }
            }
        });

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
