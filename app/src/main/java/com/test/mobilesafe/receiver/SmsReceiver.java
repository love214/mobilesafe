package com.test.mobilesafe.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;

import com.test.mobilesafe.R;
import com.test.mobilesafe.service.GPSService;

/**
 * Created by Huyanglin on 2016/8/21.
 * 短信指令监听
 */
public class SmsReceiver extends BroadcastReceiver {

    private static MediaPlayer player;//避免重复播放音乐；因为每次收到广播后，会重新生成广播接收者的实例
    private SharedPreferences sp;


    @Override
    public void onReceive(Context context, Intent intent) {
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName componentName = new ComponentName(context, Admin.class);
        sp=context.getSharedPreferences("config",Context.MODE_PRIVATE);
        String safenum = sp.getString("safenum", "");
        //接受解析短信
        Object[] objs = (Object[]) intent.getExtras().get("pdus");
        for(Object object:objs){
            //解析成短信
            SmsMessage message = SmsMessage.createFromPdu((byte[]) object);
            String body = message.getMessageBody();//获取短信内容
            String address = message.getOriginatingAddress();//获取发件人
            if (address.equals(safenum)){
                //判断是否来自安全手机号码
                //判断内容是哪个指令
                if ("#*location*#".equals(body)){
                    //GPS追踪
                    Intent gpsservice = new Intent(context, GPSService.class);
                    context.startService(gpsservice);
                    abortBroadcast();//拦截，避免系统短信接收到
                }else if ("#*alarm*#".equals(body)){
                    //播放音乐之前，把音量设置成最大
                    //播放报警音乐
                    AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,15,0);//3个参数：声音类型，声音大小，flag
                    if (player!=null){//判断是否在播放，如果在，就释放准备重新播放
                        player.release();
                    }
                    player = MediaPlayer.create(context, R.raw.ylzs);
                    player.setVolume(1.0f,1.0f);
                    player.start();

                    abortBroadcast();
                }else if ("#*wipedata*#".equals(body)){
                    //远程删除数据
                    if (devicePolicyManager.isAdminActive(componentName)){
                        devicePolicyManager.wipeData(0);//远程清除数据，恢复出厂设置
                    }
                    System.out.println("远程删除数据");
                    abortBroadcast();
                }else if ("#*lockscreen*#".equals(body)){
                    //远程锁屏
                    if (devicePolicyManager.isAdminActive(componentName)){
                        devicePolicyManager.lockNow();//锁屏
                    }
                    System.out.println("远程锁屏");
                    abortBroadcast();
                }
            }
        }
    }
}
