package com.test.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import com.test.mobilesafe.R;
import com.test.mobilesafe.db.dao.AddressDao;

/**
 * Created by Huyanglin on 2016/8/24.
 * 位置服务
 */
public class AddressService extends Service {

    private TelephonyManager telephonyManager;
    private Context mContext;
    private MyPhoneStateListener stateListener;
    private WindowManager windowManager;
    private View view;
    private MyOutgoingCallReceiver outgoingCallReceiver;
    private SharedPreferences sp;
    private WindowManager.LayoutParams params;
    private int width;
    private int height;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        sp = getSharedPreferences("config", MODE_PRIVATE);
        //设置一个广播接收者，监听拨号状态
        outgoingCallReceiver = new MyOutgoingCallReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.NEW_OUTGOING_CALL");
        registerReceiver(outgoingCallReceiver, intentFilter);

        //监听电话状态的操作
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        stateListener = new MyPhoneStateListener();
        telephonyManager.listen(stateListener, PhoneStateListener.LISTEN_CALL_STATE);

        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels;
        height = metrics.heightPixels;
    }

    /**
     * 外拨电话广播接收者
     */
    private class MyOutgoingCallReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String phone = getResultData();//获取电话号码
            String address = AddressDao.queryAddress(mContext, phone);
            if (!TextUtils.isEmpty(address)) {
                showToast(address);
            }
        }
    }

    private class MyPhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            //state:来电状态, incomingNumber：来电号码
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE://空闲状态。挂断也属于空闲状态
                    hideToast();
                    break;

                case TelephonyManager.CALL_STATE_RINGING://响铃状态
                    //查询号码归属地并显示
                    String address = AddressDao.queryAddress(mContext, incomingNumber);
                    if (!TextUtils.isEmpty(address)) {
                        //                        Toast.makeText(mContext, address, Toast.LENGTH_LONG).show();
                        //自定义toast
                        showToast(address);
                    }
                    break;

                case TelephonyManager.CALL_STATE_OFFHOOK://通话状态

                    break;
            }
        }
    }

    /**
     * 显示toast
     */
    public void showToast(String queryAddress) {
        int[] bgcolor = new int[]{
                R.drawable.call_locate_white,
                R.drawable.call_locate_orange, R.drawable.call_locate_blue,
                R.drawable.call_locate_gray, R.drawable.call_locate_green};
        //1.获取windowmaneger
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        view = View.inflate(mContext, R.layout.toast_custom, null);
        TextView tv_toastcustom_address = (TextView) view.findViewById(R.id.tv_toastcustom_address);
        tv_toastcustom_address.setText(queryAddress);
        //根据设置中心的风格设置对应的风格
        view.setBackgroundResource(bgcolor[sp.getInt("which", 0)]);

        params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;//高度包裹内容
        params.width = WindowManager.LayoutParams.WRAP_CONTENT; //宽度包裹内容
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE  //没有焦点
                //                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE  // 不可触摸
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON; // 保持当前屏幕
        params.format = PixelFormat.TRANSLUCENT; // 透明
        params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE; // 如果执行toast的类型,toast没有触摸和获取焦点的能力,必须添加权限！！！
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.x = sp.getInt("x", 100);
        params.y = sp.getInt("y", 100);
        setTouoch();
        windowManager.addView(view, params);
    }

    /**
     * 通话界面提示框设置位置
     */
    private void setTouoch() {
        view.setOnTouchListener(new View.OnTouchListener() {
            int startX = 0;
            int newX = 0;
            int startY = 0;
            int newY = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN://按下
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE://移动
                        newX = (int) event.getRawX();
                        newY = (int) event.getRawY();
                        int dX = newX - startX;
                        int dY = newY - startY;
                        //重新绘制控件
                        params.x += dX;
                        params.y += dY;
                        //控件的坐标不能移出拨号界面
                        if (params.x < 0) {
                            params.x = 0;
                        }
                        if (params.y < 0) {
                            params.y = 0;
                        }
                        if (params.x > width - view.getWidth()) {
                            params.x = width - view.getWidth();
                        }
                        if (params.x > height - view.getHeight()) {
                            params.x = width - view.getHeight();
                        }
                        windowManager.updateViewLayout(view, params);//更新windowmanager中的控件

                        //更新开始的坐标
                        startY = newY;
                        startX = newX;
                        break;
                    case MotionEvent.ACTION_UP://抬起
                        //保存控件的坐标（不是手指的坐标）
                        SharedPreferences.Editor editor = sp.edit();
                        int x = params.x;
                        int y = params.y;
                        editor.putInt("x", x);
                        editor.putInt("y", y);
                        editor.commit();
                        break;
                }
                return false;//如果此时只有触摸事件，return true；
                // 如果点击事件和触摸事件共存，返回true的话，不能触发点击事件
            }
        });
    }

    /**
     * 隐藏toast
     */
    public void hideToast() {
        if (windowManager != null && view != null) {
            windowManager.removeView(view);
            windowManager = null;
            view = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        telephonyManager.listen(stateListener, PhoneStateListener.LISTEN_NONE);//取消监听
        //注销外拨电话广播接收者
        unregisterReceiver(outgoingCallReceiver);
    }
}
