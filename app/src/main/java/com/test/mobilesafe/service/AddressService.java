package com.test.mobilesafe.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.TextView;

import com.test.mobilesafe.db.dao.AddressDao;

/**
 * Created by Huyanglin on 2016/8/24.
 */
public class AddressService extends Service {

    private TelephonyManager telephonyManager;
    private Context mContext;
    private MyPhoneStateListener stateListener;
    private WindowManager windowManager;
    private TextView textView;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext=this;
        //监听电话状态的操作
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        stateListener = new MyPhoneStateListener();
        telephonyManager.listen(stateListener,PhoneStateListener.LISTEN_CALL_STATE);
    }
    private class MyPhoneStateListener extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            //state:来电状态, incomingNumber：来电号码
            switch(state){
                case TelephonyManager.CALL_STATE_IDLE ://空闲状态。挂断也属于空闲状态
                    hideToast();
                    break;

                case TelephonyManager.CALL_STATE_RINGING ://响铃状态
                    //查询号码归属地并显示
                    String address = AddressDao.queryAddress(mContext, incomingNumber);
                    if (!TextUtils.isEmpty(address)){
//                        Toast.makeText(mContext, address, Toast.LENGTH_LONG).show();
                        //自定义toast
                        showToast(address);
                    }
                    break;

                case TelephonyManager.CALL_STATE_OFFHOOK ://通话状态

                    break;
            }
        }
    }

    /**
     * 显示toast
     */
    public void showToast(String queryAddress) {
        //1.获取windowmaneger
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        textView = new TextView(getApplicationContext());
		textView.setText(queryAddress);
		textView.setTextSize(100);
		textView.setTextColor(Color.RED);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;//高度包裹内容
        params.width = WindowManager.LayoutParams.WRAP_CONTENT; //宽度包裹内容
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE  //没有焦点
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE  // 不可触摸
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON; // 保持当前屏幕
        params.format = PixelFormat.TRANSLUCENT; // 透明
        params.type = WindowManager.LayoutParams.TYPE_TOAST; // 执行toast的类型
        windowManager.addView(textView,params);
    }

    /**
     * 隐藏toast
     */
    public void hideToast(){
        if (windowManager!=null && textView!=null){
            windowManager.removeView(textView);
            windowManager=null;
            textView=null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        telephonyManager.listen(stateListener,PhoneStateListener.LISTEN_NONE);//取消监听
    }
}
