package site.wanter.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import site.wanter.MyApplication;
import site.wanter.bean.AppInfo;
import site.wanter.engine.AppEngine;
import site.wanter.mobilesafe.R;
import site.wanter.utils.MyAsycnTask;

/**
 * Created by Huyanglin on 2016/9/16.
 */
public class SoftManagerActivity extends Activity implements View.OnClickListener {
    private Context context;
    private ListView lv_softmanager_application;
    private ProgressBar loading;
    private List<AppInfo> list;
    private List<AppInfo> userApplist;//用户app集合
    private List<AppInfo> sysApplist;//系统app集合
    private TextView tv_softmanager_userorsystem;
    PopupWindow popupWindow;
    private AppInfo appInfo;
    MyAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_softmanager);
        lv_softmanager_application = (ListView) findViewById(R.id.lv_softmanager_application);
        loading = (ProgressBar) findViewById(R.id.loading);
        tv_softmanager_userorsystem = (TextView) findViewById(R.id.tv_softmanager_userorsystem);
        //加载数据的操作，异步加载
        fillData();
        listviewOnscroll();
        listviewItemClick();
    }

    /**
     * listview条目点击事件
     */
    private void listviewItemClick() {
        lv_softmanager_application.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0 || position == userApplist.size() + 1) {//判断是否是提示框
                    return;
                }
//                AppInfo appInfo =   null;
                if (position <= userApplist.size()) {
                    //从用户app集合中获取
                    appInfo = userApplist.get(position - 1);
                } else {
                    //从系统app集合中获取数据
                    appInfo = sysApplist.get(position - userApplist.size() - 2);

                }
                hidePopuWindow();
                //popuwindow中控件功能实现
                View popu_view = View.inflate(MyApplication.getContext(), R.layout.popu_window, null);
                LinearLayout ll_popuwindow_uninstall = (LinearLayout) popu_view.findViewById(R.id.ll_popuwindow_uninstall);
                LinearLayout ll_popuwindow_setup = (LinearLayout) popu_view.findViewById(R.id.ll_popuwindow_setup);
                LinearLayout ll_popuwindow_share = (LinearLayout) popu_view.findViewById(R.id.ll_popuwindow_share);
                LinearLayout ll_popuwindow_detail = (LinearLayout) popu_view.findViewById(R.id.ll_popuwindow_detail);
                ll_popuwindow_uninstall.setOnClickListener(SoftManagerActivity.this);
                ll_popuwindow_setup.setOnClickListener(SoftManagerActivity.this);
                ll_popuwindow_share.setOnClickListener(SoftManagerActivity.this);
                ll_popuwindow_detail.setOnClickListener(SoftManagerActivity.this);

                popupWindow = new PopupWindow(popu_view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                //获取条目位置，让气泡显示在对应的位置上
                int[] location = new int[2];
                view.getLocationInWindow(location);//获取位置坐标
                int x = location[0];
                int y = location[1];
                popupWindow.showAtLocation(parent, Gravity.LEFT | Gravity.TOP, x + 50, y);
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //缩放动画
                ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0.5f);
                scaleAnimation.setDuration(5000);

                //渐变动画
                AlphaAnimation alphaAnimation = new AlphaAnimation(0.4f, 1.0f);
                alphaAnimation.setDuration(5000);

                AnimationSet animationSet = new AnimationSet(true);
                animationSet.addAnimation(scaleAnimation);
                animationSet.addAnimation(alphaAnimation);
                //执行动画
                popu_view.startAnimation(animationSet);
            }
        });
    }

    @Override
    public void onClick(View v) {
        //判断点击按钮
        switch (v.getId()) {
            case R.id.ll_popuwindow_uninstall:
                //卸载
                unInstall();
                break;

            case R.id.ll_popuwindow_setup:
                //启动
                startApp();
                break;

            case R.id.ll_popuwindow_share:
                //分享
                sharedApp();
                break;

            case R.id.ll_popuwindow_detail:
                //详情
                openAppDetail();
                break;

        }
        hidePopuWindow();
    }

    /**
     * 分享方法
     */
    private void sharedApp() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,"发现一个好玩的apk，"+appInfo.getName()+"，你可以去应用市场下载哦~");
        startActivity(intent);
    }

    /**
     * 打开app详情界面
     */
    private void openAppDetail() {
        Intent intent = new Intent();
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.parse("package:"+appInfo.getPackagName()));
        startActivity(intent);
    }

    /**
     * 启动app
     */
    private void startApp() {
        PackageManager pm = getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(appInfo.getPackagName());
        if (intent!=null && !appInfo.getPackagName().equals(getPackageName())){
            startActivity(intent);
        }
        if(appInfo.getPackagName().equals(getPackageName())){
            Toast.makeText(MyApplication.getContext(), "不能自己启动自己哦~", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(MyApplication.getContext(), "系统核心程序无法启动哦~", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 卸载app
     */
    private void unInstall() {
        /**
         * <intent-filter>
         <action android:name="android.intent.action.VIEW" />
         <action android:name="android.intent.action.DELETE" />
         <category android:name="android.intent.category.DEFAULT" />
         <data android:scheme="package" />
         </intent-filter>
         */
        if (appInfo.isUser()){
            if (!appInfo.getPackagName().equals(getPackageName())){
                Intent intent = new Intent();
                intent.setAction("android.intent.action.DELETE");
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse("package:"+appInfo.getPackagName()));
                startActivityForResult(intent,0);
            }else {
                Toast.makeText(MyApplication.getContext(), "不能自己卸载自己哦~", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(MyApplication.getContext(), "卸载系统app请先root哦~", Toast.LENGTH_SHORT).show();
        }


    }

    /**
     * 隐藏气泡
     */
    private void hidePopuWindow() {
        if (popupWindow != null) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fillData();
    }

    /**
     * 滑动监听事件
     */
    private void listviewOnscroll() {
        lv_softmanager_application.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //滑动状态改变时调用
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                hidePopuWindow();
                if (userApplist != null && sysApplist != null) {//listview初始化的时候就会调用onScroll方法。
                    if (firstVisibleItem < userApplist.size()) {
                        tv_softmanager_userorsystem.setText("用户程序(" + userApplist.size() + "个)");
                    } else {
                        tv_softmanager_userorsystem.setText("系统程序(" + sysApplist.size() + "个)");
                    }
                }

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
                list = AppEngine.getAppInfos();
                userApplist = new ArrayList<AppInfo>();
                sysApplist = new ArrayList<AppInfo>();
                for (AppInfo appinfo : list) {
                    if (appinfo.isUser()) {
                        userApplist.add(appinfo);
                    } else {
                        sysApplist.add(appinfo);
                    }
                }
            }

            @Override
            public void postTask() {
                if (adapter==null){
                    adapter = new MyAdapter();
                    lv_softmanager_application.setAdapter(adapter);
                }else {
                    adapter.notifyDataSetChanged();
                }

                loading.setVisibility(View.INVISIBLE);
            }
        }.execute();
    }


    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return userApplist.size() + sysApplist.size() + 2;
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
                textView.setText("用户程序：" + userApplist.size() + "个");
                return textView;
            } else if (position == userApplist.size() + 1) {
                //添加系统app数量统计
                TextView textView = new TextView(MyApplication.getContext());
                textView.setBackgroundColor(Color.GRAY);
                textView.setTextColor(Color.WHITE);
                textView.setText("系统程序：" + sysApplist.size() + "个");
                return textView;
            }
            View view;
            ViewHolder viewHolder;
            if (convertView != null && convertView instanceof RelativeLayout) {//此处判断复用的控件，如果是textview则不复用
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            } else {
                view = View.inflate(context, R.layout.item_softmanager, null);
                viewHolder = new ViewHolder();
                viewHolder.iv_itemsoftmanager_icon = (ImageView) view.findViewById(R.id.iv_itemsoftmanager_icon);
                viewHolder.tv_itemsoftmanager_name = (TextView) view.findViewById(R.id.tv_itemsoftmanager_name);
                viewHolder.tv_itemsoftmanager_issd = (TextView) view.findViewById(R.id.tv_itemsoftmanager_issd);
                viewHolder.tv_itemsoftmanager_version = (TextView) view.findViewById(R.id.tv_itemsoftmanager_version);
                view.setTag(viewHolder);
            }

            AppInfo appInfo = null;
            if (position <= userApplist.size()) {
                //从用户app集合中获取
                appInfo = userApplist.get(position - 1);
            } else {
                //从系统app集合中获取数据
                appInfo = sysApplist.get(position - userApplist.size() - 2);

            }
            viewHolder.iv_itemsoftmanager_icon.setImageDrawable(appInfo.getIcon());
            viewHolder.tv_itemsoftmanager_name.setText(appInfo.getName());
            if (appInfo.isSD()) {
                viewHolder.tv_itemsoftmanager_issd.setText("SD卡");
            } else {
                viewHolder.tv_itemsoftmanager_issd.setText("手机内存");
            }
            viewHolder.tv_itemsoftmanager_version.setText(appInfo.getVerisonName());

            return view;
        }
    }

    static class ViewHolder {
        ImageView iv_itemsoftmanager_icon;
        TextView tv_itemsoftmanager_name, tv_itemsoftmanager_issd, tv_itemsoftmanager_version;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hidePopuWindow();//避免报错
    }
}
