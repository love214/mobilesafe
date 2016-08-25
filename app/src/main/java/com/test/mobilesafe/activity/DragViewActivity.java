package com.test.mobilesafe.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.test.mobilesafe.R;

/**
 * Created by Huyanglin on 2016/8/25.
 * 设置归属地显示位置
 */
public class DragViewActivity extends Activity{
    private Context mContext;
    private LinearLayout ll_dragview_toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dragview);
        mContext=this;
        ll_dragview_toast = (LinearLayout) findViewById(R.id.ll_dragview_toast);
        setTouch();
    }

    /**
     * 设置触摸监听
     */
    private void setTouch() {

        ll_dragview_toast.setOnTouchListener(new View.OnTouchListener() {
            int startX = 0;
            int newX = 0;
            int startY = 0;
            int newY = 0;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN ://按下
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE ://移动
                        newX = (int) event.getRawX();
                        newY = (int) event.getRawY();
                        int dX=newX-startX;
                        int dY=newY-startY;
                        //重新绘制控件
                        //获取的是原控件的参数
                        int left = ll_dragview_toast.getLeft();
                        int top = ll_dragview_toast.getTop();
                        //获取移动后的新的偏移量
                        left+=dX;
                        top+=dY;
                        int r=left+ll_dragview_toast.getWidth();
                        int b=top+ll_dragview_toast.getHeight();
                        ll_dragview_toast.layout(left,top,r,b);
                        //更新开始的坐标
                        startY=newY;
                        startX=newX;
                        break;
                    case MotionEvent.ACTION_UP ://抬起

                        break;
                }
                return true;
            }
        });
    }
}
