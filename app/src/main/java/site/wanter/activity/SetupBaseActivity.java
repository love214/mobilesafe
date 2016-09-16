package site.wanter.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Huyanglin on 2016/8/13.
 * 创建一个setup基类
 */
public abstract class SetupBaseActivity extends Activity {
    private Context mContext;
    private GestureDetector gestureDetector;
    protected SharedPreferences sp;

    public abstract void next_activity();
    public abstract void pre_activity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        gestureDetector = new GestureDetector(mContext,new MyOngestureListener());
        sp=getSharedPreferences("config",MODE_PRIVATE);

    }

    private class MyOngestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            //e1:按下的事件，保存按下的坐标  e2:是抬起的事件，保存抬起的坐标
            //velocityX:在x轴上的速度        velocityY：在y轴上的速度
            float startX = e1.getRawX();//得到按下的坐标
            float startY = e1.getRawY();
            float endX = e2.getRawX();//得到抬起的坐标
            float endY = e2.getRawY();
            if (startX-endX>100 && (Math.abs(startY-endY))<50){
                //下一步
                next_activity();
            }else if (endX-startX>100&& (Math.abs(startY-endY))<50){
                //上一步
                pre_activity();
            }

            return true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return true;
    }

    public void previous(View view){
        //跳转到上一个界面
        pre_activity();
    }

    public void next(View view){
        next_activity();
    }
    //监听手机物理按钮的点击事件
    //keyCode :　物理按钮的标示
    //event : 按键的处理事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断keycode是否是返回键的标示
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //true:是可以屏蔽按键的事件
            //return true;
            pre_activity();
        }
        return super.onKeyDown(keyCode, event);
    }


}
