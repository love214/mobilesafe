package com.test.mobilesafe.utils;

import android.os.Handler;
import android.os.Message;

/**
 * Created by Huyanglin on 2016/8/21.
 * 贯穿子线程的方法
 */
public abstract class MyAsycnTask {
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            postTask();
        }
    };

    //在子线程之前执行的方法
    public abstract void preTask();

    //在子线程之中执行的方法
    public abstract void doinTask();

    //在子线程之后执行的方法
    public abstract void postTask();

    public void execute(){
        preTask();
        new Thread(){
            @Override
            public void run() {
                doinTask();
                handler.sendEmptyMessage(0);
            }
        }.start();
    }
}
