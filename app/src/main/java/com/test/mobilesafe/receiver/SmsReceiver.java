package com.test.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

/**
 * Created by Huyanglin on 2016/8/21.
 * 短信指令监听
 */
public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //接受解析短信
        Object[] objs = (Object[]) intent.getExtras().get("pdus");
        for(Object object:objs){
            //解析成短信
            SmsMessage message = SmsMessage.createFromPdu((byte[]) object);
            String body = message.getMessageBody();//获取短信内容
            String address = message.getOriginatingAddress();//获取发件人
            //判断内容是哪个指令
            if ("#*location*#".equals(body)){
                //GPS追踪
                abortBroadcast();//拦截，避免系统短信接收到
            }else if ("#*alarm*#".equals(body)){
                //播放报警音乐
                abortBroadcast();
            }else if ("#*wipedata*#".equals(body)){
                //远程删除数据
                abortBroadcast();
            }else if ("#*lockscreen*#".equals(body)){
                //远程锁屏
                abortBroadcast();
            }
        }
    }
}
