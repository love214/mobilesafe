package com.test.mobilesafe.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.test.mobilesafe.R;
import com.test.mobilesafe.bean.AppInfo;
import com.test.mobilesafe.engine.AppEngine;
import com.test.mobilesafe.utils.MyAsycnTask;

import java.util.List;

/**
 * Created by Huyanglin on 2016/9/16.
 */
public class SotfManagerActivity extends Activity {
    private Context context;
    private ListView lv_softmanager_application;
    private ProgressBar loading;
    List<AppInfo> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.activity_softmanager);
        lv_softmanager_application = (ListView) findViewById(R.id.lv_softmanager_application);
        loading = (ProgressBar) findViewById(R.id.loading);
        //加载数据的操作，异步加载
        fillData();
    }

    private void fillData() {
        new MyAsycnTask(){
            @Override
            public void preTask() {
                loading.setVisibility(View.VISIBLE);
            }

            @Override
            public void doInTask() {
                list = AppEngine.getAppInfos();
            }

            @Override
            public void postTask() {
                MyAdapter adapter = new MyAdapter();
                lv_softmanager_application.setAdapter(adapter);
                loading.setVisibility(View.INVISIBLE);
            }
        }.execute();
    }
    private class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            ViewHolder viewHolder;
            if (convertView!=null){
                view=convertView;
                viewHolder=(ViewHolder)view.getTag();
            }else {
                view= View.inflate(context, R.layout.item_softmanager, null);
                viewHolder=new ViewHolder();
                viewHolder.iv_itemsoftmanager_icon=(ImageView)view.findViewById(R.id.iv_itemsoftmanager_icon);
                viewHolder.tv_itemsoftmanager_name=(TextView) view.findViewById(R.id.tv_itemsoftmanager_name);
                viewHolder.tv_itemsoftmanager_issd=(TextView) view.findViewById(R.id.tv_itemsoftmanager_issd);
                viewHolder.tv_itemsoftmanager_version=(TextView) view.findViewById(R.id.tv_itemsoftmanager_version);
                view.setTag(viewHolder);
            }

            AppInfo appInfo = list.get(position);
            viewHolder.iv_itemsoftmanager_icon.setImageDrawable(appInfo.getIcon());
            viewHolder.tv_itemsoftmanager_name.setText(appInfo.getName());
            if (appInfo.isSD()){
                viewHolder.tv_itemsoftmanager_issd.setText("SD卡");
            }else {
                viewHolder.tv_itemsoftmanager_issd.setText("手机内存");
            }
            viewHolder.tv_itemsoftmanager_version.setText(appInfo.getVerisonName());

            return view;
        }
    }

    static class ViewHolder{
        ImageView iv_itemsoftmanager_icon;
        TextView tv_itemsoftmanager_name,tv_itemsoftmanager_issd,tv_itemsoftmanager_version;

    }
}
