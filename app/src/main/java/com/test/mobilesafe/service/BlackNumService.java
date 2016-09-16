package com.test.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

import com.test.mobilesafe.db.dao.BlackNumDao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BlackNumService extends Service {

    SmsReceiver mSmsReceiver;
    BlackNumDao mBlackNumDao;
    MyPhoneStateListener stateListener;
    TelephonyManager telephonyManager;
    private Context mContext;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBlackNumDao = new BlackNumDao(getApplicationContext());
        mContext=this;
        //注册短信到来的广播接收者
        mSmsReceiver = new SmsReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(Integer.MAX_VALUE);//这里设置优先级最大
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(mSmsReceiver, filter);
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        stateListener = new MyPhoneStateListener();
        telephonyManager.listen(stateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    private class MyPhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, final String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            if (state == TelephonyManager.CALL_STATE_RINGING) {
                //响铃状态，检测拦截模式是否是电话拦截
                int mode = mBlackNumDao.queryBlackNumMode(incomingNumber);
                if (mode == BlackNumDao.ALL || mode == BlackNumDao.CALL) {
                    //挂断电话
                    endCall();
                    //删除通话记录的逻辑
                    final ContentResolver resolver = getContentResolver();
                    final Uri uri=Uri.parse("content://call_log/calls");
                    //采用内容观察者来实现，一旦发生变化，就删除通话记录
                    resolver.registerContentObserver(uri, true, new ContentObserver(new Handler()) {
                        @Override
                        public void onChange(boolean selfChange) {
                            super.onChange(selfChange);
                            resolver.delete(uri,"number=?",new String[]{incomingNumber});
                        }
                    });
                }
            }
        }
    }

    /**
     * 挂断电话
     */
    private void endCall() {
        try {
            Object telephonyObject = getTelephonyObject(mContext);
            if (null != telephonyObject) {
                Class telephonyClass = telephonyObject.getClass();

                Method endCallMethod = telephonyClass.getMethod("endCall");
                endCallMethod.setAccessible(true);

                endCallMethod.invoke(telephonyObject);
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    /**
     * 挂电话逻辑
     * @param context
     * @return
     */
    private static Object getTelephonyObject(Context context) {
        Object telephonyObject = null;
        try {
            // 初始化iTelephony
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            // Will be used to invoke hidden methods with reflection
            // Get the current object implementing ITelephony interface
            Class telManager = telephonyManager.getClass();
            Method getITelephony = telManager.getDeclaredMethod("getITelephony");
            getITelephony.setAccessible(true);
            telephonyObject = getITelephony.invoke(telephonyManager);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return telephonyObject;
    }

    private class SmsReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Object[] objs = (Object[]) intent.getExtras().get("pdus");
            for (Object object : objs) {
                //解析成短信
                SmsMessage message = SmsMessage.createFromPdu((byte[]) object);
                String body = message.getMessageBody();//获取短信内容
                String address = message.getOriginatingAddress();//获取发件人
                int mode = mBlackNumDao.queryBlackNumMode(address);
                if (mode == BlackNumDao.SMS || mode == BlackNumDao.ALL) {
                    //拦截短信
                    abortBroadcast();//拦截短信
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mSmsReceiver);
        telephonyManager.listen(stateListener, PhoneStateListener.LISTEN_NONE);
    }
}
