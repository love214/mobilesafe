package com.test.mobilesafe.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.test.mobilesafe.R;

/**
 * Created by Huyanglin on 2016/8/25.
 * 设置归属地显示位置
 */
public class DragViewActivity extends Activity{
    private Context mContext;
    private LinearLayout ll_dragview_toast;
    private SharedPreferences sp;
    private TextView tv_dragview_top;
    private TextView tv_dragview_bottom;
    int width;
    int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dragview);
        mContext=this;
        sp=getSharedPreferences("config",MODE_PRIVATE);
        ll_dragview_toast = (LinearLayout) findViewById(R.id.ll_dragview_toast);
        tv_dragview_top = (TextView) findViewById(R.id.tv_dragview_top);
        tv_dragview_bottom = (TextView) findViewById(R.id.tv_dragview_bottom);
        //归属地提示框位置回显
        int x = sp.getInt("x", 0);
        int y = sp.getInt("y", 0);
        //重新设置控件属性，实现回显操作
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ll_dragview_toast.getLayoutParams();
        params.leftMargin=x;
        params.topMargin=y;
        ll_dragview_toast.setLayoutParams(params);

        //获取屏幕的宽度
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels;
        height = metrics.heightPixels;
        //根据提示框的位置确定textview的位置
        if (y>=height/2){
            //隐藏下面
            tv_dragview_bottom.setVisibility(View.INVISIBLE);
            tv_dragview_top.setVisibility(View.VISIBLE);
        }else {
            //隐藏上面
            tv_dragview_bottom.setVisibility(View.VISIBLE);
            tv_dragview_top.setVisibility(View.INVISIBLE);
        }

        setTouch();
        setDoubleClick();
    }

    /**
     * 双击事件
     */
    private void setDoubleClick() {
        ll_dragview_toast.setOnClickListener(new View.OnClickListener() {

            long[] mHits = new long[2];//拷贝的原数组
            @Override
            public void onClick(View v) {
                System.arraycopy(mHits, 1, mHits, 0, mHits.length-1);
                mHits[mHits.length-1] = SystemClock.uptimeMillis();
                if (mHits[0] >= (SystemClock.uptimeMillis()-500)) {
                    int l=(width-ll_dragview_toast.getWidth())/2;
                    int t=(height -25-ll_dragview_toast.getHeight())/2;
                    ll_dragview_toast.layout(l,t,l+ll_dragview_toast.getWidth(),t+ll_dragview_toast.getHeight());
                    //保存最后的坐标，方便下次进入的时候恢复上次的坐标
                    SharedPreferences.Editor editor = sp.edit();
                    int x=ll_dragview_toast.getLeft();
                    int y=ll_dragview_toast.getTop();
                    editor.putInt("x",x);
                    editor.putInt("y",y);
                    editor.commit();
                }
            }
        });
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

                        //判断是否超出屏幕了，如果超出了屏幕，就不再进行绘制
                        if (left<0 || r>width || top<0 || b> height -25){//-25：减去通知栏的位置
                            break;
                        }
                        ll_dragview_toast.layout(left,top,r,b);

                        int t=ll_dragview_toast.getTop();
                        if (t>=height/2){
                            //隐藏下面
                            tv_dragview_bottom.setVisibility(View.INVISIBLE);
                            tv_dragview_top.setVisibility(View.VISIBLE);
                        }else {
                            //隐藏上面
                            tv_dragview_bottom.setVisibility(View.VISIBLE);
                            tv_dragview_top.setVisibility(View.INVISIBLE);
                        }

                        //更新开始的坐标
                        startY=newY;
                        startX=newX;
                        break;
                    case MotionEvent.ACTION_UP ://抬起
                        //保存控件的坐标（不是手指的坐标）
                        SharedPreferences.Editor editor = sp.edit();
                        int x=ll_dragview_toast.getLeft();
                        int y=ll_dragview_toast.getTop();
                        editor.putInt("x",x);
                        editor.putInt("y",y);
                        editor.commit();
                        break;
                }
                return false;//如果此时只有触摸事件，return true；
                // 如果点击事件和触摸事件共存，返回true的话，不能触发点击事件
            }
        });
    }
}
