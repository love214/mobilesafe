package com.test.mobilesafe.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.test.mobilesafe.R;
import com.test.mobilesafe.engine.ContactsEngine;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Huyanglin on 2016/8/21.
 */
public class ContactActivity extends Activity{

    private ListView lv_contact_contacts;
    private Context mContext;
    private List<HashMap<String, String>> list;
    //通过注解方式初始化控件
    @ViewInject(R.id.loading)
    private ProgressBar loading;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            lv_contact_contacts.setAdapter(new MyAdapter());
            loading.setVisibility(View.INVISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        mContext=this;

        ViewUtils.inject((Activity) mContext);
        loading.setVisibility(View.VISIBLE);
        //在子线程中执行查询联系人
        new Thread(){
            @Override
            public void run() {
                super.run();
                list = ContactsEngine.getAllContactsInfo(mContext);
                handler.sendEmptyMessage(0);
            }
        }.start();

        lv_contact_contacts = (ListView) findViewById(R.id.lv_contact_contacts);

        lv_contact_contacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //条目点击事件
                //将选择的联系人号码传递回
                Intent intent = new Intent();
                intent.putExtra("num",list.get(position).get("phone"));
                //传递数据
                setResult(RESULT_OK,intent);//设置结果的方法
                finish();
            }
        });
    }
    private class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(mContext, R.layout.item_contact, null);
            TextView tv_contact_name = (TextView) view.findViewById(R.id.tv_contact_name);
            TextView tv_contact_phone = (TextView) view.findViewById(R.id.tv_contact_phone);
            System.out.println(list.size());
            tv_contact_name.setText("name:"+list.get(position).get("name"));
            tv_contact_phone.setText("phone:"+list.get(position).get("phone"));
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
