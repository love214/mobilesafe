package com.test.mobilesafe.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.test.mobilesafe.R;
import com.test.mobilesafe.db.dao.AddressDao;

/**
 * Created by Huyanglin on 2016/8/23.
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

    }
    public void query(View view){
        String phone = et_address_queryphone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)){
            //电话为空
            Toast.makeText(mContext, "请输入要查询的号码", Toast.LENGTH_SHORT).show();
            return;
        }else {
            //查询归属地
            String address = AddressDao.queryAddress(mContext, phone);
            if (!TextUtils.isEmpty(address)){
                tv_address_queryresult.setText(address);
            }
        }
    }
}
