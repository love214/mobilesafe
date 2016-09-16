package site.wanter.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import site.wanter.mobilesafe.R;

/**
 * Created by Huyanglin on 2016/8/13.
 * 手机防盗模块
 */
public class LostfindActivity extends Activity {
    private SharedPreferences sp;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        //分为两部分 1.显示设置过的手机防盗功能  2.设置防盗功能
        sp = getSharedPreferences("config", MODE_PRIVATE);
        if (sp.getBoolean("first", true)) {
            //第一次进入
            Intent intent = new Intent(mContext, Setup1Activity.class);
            startActivity(intent);
            finish();
        } else {
            //不是第一次进入
            setContentView(R.layout.activity_lostfind);
            TextView tv_lostfind_safenum = (TextView) findViewById(R.id.tv_lostfind_safenum);
            ImageView iv_lostfind_protected = (ImageView) findViewById(R.id.iv_lostfind_protected);

            tv_lostfind_safenum.setText(sp.getString("safenum", ""));
            if (sp.getBoolean("protected", false)) {
                iv_lostfind_protected.setImageResource(R.drawable.lock);
            } else {
                iv_lostfind_protected.setImageResource(R.drawable.unlock);
            }
        }

    }

    //重新进入防盗设置向导
    public void resetup(View view) {
        Intent intent = new Intent(this, Setup1Activity.class);
        startActivity(intent);
        finish();
    }
}
