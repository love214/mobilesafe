package site.wanter.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //发送报警短信
        //检查保存的手机号码是否发生变化
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        //根据用户是否开启防盗来判断是否发送报警短信
        boolean isFinflost = sp.getBoolean("protected", false);
        if (isFinflost){
            String sp_sim = sp.getString("sim", "");
            //再次获取本地SIM卡号码
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            //String line1Number = telephonyManager.getLine1Number();//获取SIM卡绑定的电话号码 如果手机没有绑定号码 无法获取
            String sim = telephonyManager.getSimSerialNumber();//SIM卡 的序列号
            //判断两个SIM卡号码
            if (! TextUtils.isEmpty(sp_sim) && ! TextUtils.isEmpty(sim)){
                //如果一致则不发送报警短信
                if (! sim.equals(sp_sim)){
                    //发送报警短信
                    SmsManager smsManager = SmsManager.getDefault();
                    //destinationAddress:收件人, scAddress：短信中中心号码, text：内容
                    //sentIntent: deliveryIntent
                    smsManager.sendTextMessage(sp.getString("safenum",""),null,"hello",null,null);
                }
            }
        }



    }
}
