package site.wanter.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import site.wanter.mobilesafe.R;
import site.wanter.db.dao.AddressDao;

/**
 * Created by Huyanglin on 2016/8/23.
 * 号码归属地查询工具
 */
public class AddressActivity extends Activity{
    private Context mContext;
    private EditText et_address_queryphone;
    private TextView tv_address_queryresult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        setContentView(R.layout.activity_address);
        et_address_queryphone = (EditText) findViewById(R.id.et_address_queryphone);
        tv_address_queryresult = (TextView) findViewById(R.id.tv_address_queryresult);

        et_address_queryphone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //文本变化之前
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //文本变化完成
                String phone = s.toString();
                String address = AddressDao.queryAddress(mContext, phone);
                if (!TextUtils.isEmpty(phone)){
                    tv_address_queryresult.setText(address);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //文本变化之后
            }
        });

    }
    public void query(View view){
        String phone = et_address_queryphone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)){
            //电话为空
            Toast.makeText(mContext, "请输入要查询的号码", Toast.LENGTH_SHORT).show();
            //加入一个抖动的效果
            Animation shake = AnimationUtils.loadAnimation(mContext, R.anim.address_shake);
            findViewById(R.id.et_address_queryphone).startAnimation(shake);//开启动画
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(100);
        }else {
            //查询归属地
            String address = AddressDao.queryAddress(mContext, phone);
            if (!TextUtils.isEmpty(address)){
                tv_address_queryresult.setText(address);
            }
        }
    }
}
