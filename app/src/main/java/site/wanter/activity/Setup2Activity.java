package site.wanter.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import site.wanter.mobilesafe.R;
import site.wanter.ui.SettingView;

/**
 * Created by Huyanglin on 2016/8/13.
 */
public class Setup2Activity extends SetupBaseActivity {
    private Context mContext;
    private SettingView sv_setup2_sim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        setContentView(R.layout.activity_setup2);
        sv_setup2_sim = (SettingView) findViewById(R.id.sv_setup2_sim);
        if (TextUtils.isEmpty(sp.getString("sim",""))){
            //没有绑定
            sv_setup2_sim.setChecked(false);
        }else {
            //绑定成功
            sv_setup2_sim.setChecked(true);
        }
        sv_setup2_sim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sp.edit();
                //绑定SIM卡
                //根据checkbox的状态设置状态
                if (sv_setup2_sim.isChecked()){
                    //解绑SIM卡
                    editor.putString("sim","");
                    sv_setup2_sim.setChecked(false);

                }else {
                    //绑定SIM卡
                    //电话管理者
                    TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(TELEPHONY_SERVICE);
                    //String line1Number = telephonyManager.getLine1Number();//获取SIM卡绑定的电话号码 如果手机没有绑定号码 无法获取
                    String sim = telephonyManager.getSimSerialNumber();//SIM卡 的序列号

                    editor.putString("sim",sim);
                    sv_setup2_sim.setChecked(true);
                }
                editor.commit();
            }
        });
    }

    @Override
    public void next_activity() {
        //检查SIM卡绑定状态
        if (sv_setup2_sim.isChecked()){
            //跳转到第三个界面
            Intent intent = new Intent(mContext, Setup3Activity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.setup_enter_next,R.anim.setup_exit_next);
        }else {
            Toast.makeText(mContext, "请绑定SIM卡", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void pre_activity() {
        //跳转到上一个界面
        Intent intent = new Intent(mContext, Setup1Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.setup_enter_pre,R.anim.setup_exit_pre);
    }

}
