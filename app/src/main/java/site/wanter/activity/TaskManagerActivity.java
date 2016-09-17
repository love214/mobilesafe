package site.wanter.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import site.wanter.MyApplication;
import site.wanter.bean.TaskInfo;
import site.wanter.engine.TaskEngine;
import site.wanter.mobilesafe.R;
import site.wanter.utils.MyAsycnTask;

/**
 * Created by Huyanglin on 2016/9/17.
 */
public class TaskManagerActivity extends Activity {
    private Context mContext;
    private ListView lv_taskmanager_process;
    private ProgressBar loading;
    private List<TaskInfo> userTasklist;//用户app集合
    private List<TaskInfo> sysTasklist;//系统app集合
    private MyAdapter adapter;
    private List<TaskInfo> list;
    private TaskInfo taskInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_taskmanager);
        lv_taskmanager_process = (ListView) findViewById(R.id.lv_taskmanager_process);
        loading = (ProgressBar) findViewById(R.id.loading);
        fillData();
        listViewItemClick();
    }

    /**
     * listview条目点击事件
     */
    private void listViewItemClick() {
        lv_taskmanager_process.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0 || position == userTasklist.size() + 1) {//判断是否是提示框
                    return;
                }
                if (position <= userTasklist.size()) {
                    //从用户app集合中获取
                    taskInfo = userTasklist.get(position - 1);
                } else {
                    //从系统app集合中获取数据
                    taskInfo = sysTasklist.get(position - userTasklist.size() - 2);
                }
                if (taskInfo.isCheacked()) {
                    taskInfo.setCheacked(false);
                } else {
                    taskInfo.setCheacked(true);
                }
//                adapter.notifyDataSetChanged();此处不更新所有条目，只更新发送改变的条目
                ViewHolder viewHolder=(ViewHolder)view.getTag();
                viewHolder.cb_taskmanager_ischecked.setChecked(taskInfo.isCheacked());

            }
        });
    }

    private void fillData() {
        new MyAsycnTask() {
            @Override
            public void preTask() {
                loading.setVisibility(View.VISIBLE);
            }

            @Override
            public void doInTask() {

                list = TaskEngine.getTaskAllInfo();
                userTasklist = new ArrayList<TaskInfo>();
                sysTasklist = new ArrayList<TaskInfo>();
                for (TaskInfo tempTaskInfo : list) {
                    if (tempTaskInfo.isUser()) {
                        userTasklist.add(tempTaskInfo);
                    } else {
                        sysTasklist.add(tempTaskInfo);
                    }
                }
            }

            @Override
            public void postTask() {
                if (adapter == null) {
                    adapter = new MyAdapter();
                    lv_taskmanager_process.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }

                loading.setVisibility(View.INVISIBLE);
            }
        }.execute();
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size() + 2;
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
            if (position == 0) {
                //添加用户程序统计
                TextView textView = new TextView(MyApplication.getContext());
                textView.setBackgroundColor(Color.GRAY);
                textView.setTextColor(Color.WHITE);
                textView.setText("用户进程：" + userTasklist.size() + "个");
                return textView;
            } else if (position == userTasklist.size() + 1) {
                //添加系统app数量统计
                TextView textView = new TextView(MyApplication.getContext());
                textView.setBackgroundColor(Color.GRAY);
                textView.setTextColor(Color.WHITE);
                textView.setText("系统进程：" + sysTasklist.size() + "个");
                return textView;
            }
            View view;
            ViewHolder viewHolder;
            if (convertView != null && convertView instanceof RelativeLayout) {//此处判断复用的控件，如果是textview则不复用
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            } else {
                view = View.inflate(mContext, R.layout.item_taskmanager, null);
                viewHolder = new ViewHolder();
                viewHolder.iv_itemtaskmanager_icon = (ImageView) view.findViewById(R.id.iv_itemtaskmanager_icon);
                viewHolder.tv_itemtaskmanager_name = (TextView) view.findViewById(R.id.tv_itemtaskmanager_name);
                viewHolder.tv_itemtaskmanager_ram = (TextView) view.findViewById(R.id.tv_itemtaskmanager_ram);
                viewHolder.cb_taskmanager_ischecked = (CheckBox) view.findViewById(R.id.cb_taskmanager_ischecked);
                view.setTag(viewHolder);
            }

            if (position <= userTasklist.size()) {
                //从用户app集合中获取
                taskInfo = userTasklist.get(position - 1);
            } else {
                //从系统app集合中获取数据
                taskInfo = sysTasklist.get(position - userTasklist.size() - 2);

            }
            if (taskInfo.getIcon()==null){
                viewHolder.iv_itemtaskmanager_icon.setImageResource(R.drawable.ic_default);
            }else {
                viewHolder.iv_itemtaskmanager_icon.setImageDrawable(taskInfo.getIcon());
            }
            if (TextUtils.isEmpty(taskInfo.getName())){
                viewHolder.tv_itemtaskmanager_name.setText(taskInfo.getPackageName());
            }else {
                viewHolder.tv_itemtaskmanager_name.setText(taskInfo.getName());
            }

            String size = Formatter.formatFileSize(mContext, taskInfo.getRamSize());//格式化大小显示
            viewHolder.tv_itemtaskmanager_ram.setText(size);

            if (taskInfo.isCheacked()){
                viewHolder.cb_taskmanager_ischecked.setChecked(true);
            }else {
                viewHolder.cb_taskmanager_ischecked.setChecked(false);
            }

            return view;
        }
    }

    static class ViewHolder {
        ImageView iv_itemtaskmanager_icon;
        TextView tv_itemtaskmanager_name, tv_itemtaskmanager_ram;
        CheckBox cb_taskmanager_ischecked;

    }
}
