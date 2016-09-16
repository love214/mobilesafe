package site.wanter.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import site.wanter.mobilesafe.R;

/**
 * Created by Huyanglin on 2016/8/13.
 */
public class Setup3Activity extends SetupBaseActivity {
    private Context mContext;
    private EditText et_setup3_safenum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        setContentView(R.layout.activity_setup3);
        et_setup3_safenum = (EditText) findViewById(R.id.et_setup3_safenum);
        et_setup3_safenum.setText(sp.getString("safenum",""));//回显号码
    }

    @Override
    public void next_activity() {
        //获取安全号码
        String safenum = et_setup3_safenum.getText().toString().trim();
        if (TextUtils.isEmpty(safenum)){
            Toast.makeText(mContext, "请输入安全号码", Toast.LENGTH_SHORT).show();
            return;
        }
        //保存安全号码
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("safenum",safenum);
        editor.commit();
        //跳转到第四个界面
        Intent intent = new Intent(mContext, Setup4Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.setup_enter_next,R.anim.setup_exit_next);
    }

    @Override
    public void pre_activity() {
        //跳转到上一个界面
        Intent intent = new Intent(mContext, Setup2Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.setup_enter_pre,R.anim.setup_exit_pre);
    }

    public void selectContact(View view){
        //联系人选择
        Intent intent = new Intent(mContext, ContactActivity.class);
        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null){//避免空指针异常
            //接收回传过来的联系人号码
            String num = data.getStringExtra("num");
            //去除连接符和空格字符
            num=num.replaceAll("-","").replaceAll(" ","");
            et_setup3_safenum.setText(num);
        }
        /*
        如果不对传回来的data进行判断，如果在选择联系人界面按下back，那么并没有创建intent对象，所以，data为null
        解决办法：判断data是否为空
         */

    }
}
