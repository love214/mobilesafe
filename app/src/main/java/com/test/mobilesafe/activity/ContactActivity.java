package com.test.mobilesafe.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.test.mobilesafe.utils.MyAsycnTask;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        mContext=this;
        ViewUtils.inject((Activity) mContext);
        lv_contact_contacts = (ListView) findViewById(R.id.lv_contact_contacts);

        //异步加载框架，将子线程所有操作封装到工具类中
        new MyAsycnTask() {
            @Override
            public void preTask() {
                loading.setVisibility(View.VISIBLE);
            }

            @Override
            public void doInTask() {
                //在子线程中执行查询联系人
                list = ContactsEngine.getAllContactsInfo(mContext);
            }

            @Override
            public void postTask() {
                lv_contact_contacts.setAdapter(new MyAdapter());
                loading.setVisibility(View.INVISIBLE);
            }
        }.execute();//注意调用执行方法

/*
        //Android系统的异步加载
        //三个参数为了提高扩展性
        //参数1：子线程执行所需的参数
        //参数2：执行的进度的参数
        //参数3：子线程执行的结果
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }
        };
*/

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
