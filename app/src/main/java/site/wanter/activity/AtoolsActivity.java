package site.wanter.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import site.wanter.mobilesafe.R;

/**
 * Created by Huyanglin on 2016/8/23.
 * 高级工具
 */
public class AtoolsActivity extends Activity{
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atools);
        mContext=this;
    }

    public void queryaddress(View view){
        //跳转到号码归属地查询界面
        Intent intent = new Intent(mContext, AddressActivity.class);
        startActivity(intent);
    }
}
