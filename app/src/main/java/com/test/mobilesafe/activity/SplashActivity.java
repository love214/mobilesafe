package com.test.mobilesafe.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.test.mobilesafe.R;
import com.test.mobilesafe.utils.StreamUtil;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Huyanglin on 2016/8/9.
 */
public class SplashActivity extends Activity {

    private static final int MSG_UPDATE_DIALOG = 1;
    private static final int MSG_ENTER_HOME = 2;
    private static final int MSG_SERVER_ERROR = 3;
    private static final int MSG_URL_ERROR = 4;
    private static final int MSG_IO_ERROR = 5;
    private static final int MSG_JSON_ERROR = 6;
    private TextView tv_splash_versionname;
    private TextView tv_splash_plan;
    private String apkCode;
    private String apkurl;
    private String apkdes;
    private int startTime;
    private int endTime;
    private Context mContext;
    private SharedPreferences sp;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case MSG_UPDATE_DIALOG :
                    //弹出对话框
                    showdialog();
                    break;
                case MSG_ENTER_HOME:
                    enterHome();
                    break;
                case MSG_SERVER_ERROR:
                    Toast.makeText(mContext, "服务器异常", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case MSG_IO_ERROR:
                    Toast.makeText(mContext, "亲，没有网络连接", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case MSG_URL_ERROR:
                    Toast.makeText(mContext, "错误号："+MSG_URL_ERROR, Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case MSG_JSON_ERROR:
                    Toast.makeText(mContext, "错误号："+MSG_JSON_ERROR, Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        mContext=this;
        tv_splash_versionname = (TextView) findViewById(R.id.tv_splash_versionname);
        tv_splash_versionname.setText("版本号 "+getVersionName());
        tv_splash_plan = (TextView) findViewById(R.id.tv_splash_plan);
        sp=getSharedPreferences("config",MODE_PRIVATE);
        if (sp.getBoolean("update",true)){
            update();
        }else {
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    SystemClock.sleep(2000);
                    enterHome();
                }
            }.start();

        }

    }

    //弹出对话框
    private void showdialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(false);//设置对话框不能消失的方法
        builder.setTitle("新版本:"+apkCode);
        builder.setIcon(R.mipmap.ic_launcherweb);
        builder.setMessage(apkdes);
        //升级按钮的事件
        builder.setPositiveButton("升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                download();
            }
        });
        //设置取消按钮的事件
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //隐藏对话框 跳转到下一个界面
                dialog.dismiss();
                enterHome();
            }
        });
        builder.show();
    }

    //下载升级apk文件
    private void download() {
        //下载文件使用xUtils框架，不需要再次写子线程，jar包已经封装好
        HttpUtils httpUtils = new HttpUtils();
        //参数说明： URL-下载路径 target-保存路径 callback-RequestCallBack
        //必须判断Sd卡是否挂载
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            //说明SD卡已经挂载成功
            httpUtils.download(apkurl, "/mnt/sdcard/mobilesafe.apk", new RequestCallBack<File>() {
                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    //下载成功后安装最新版本
                    installApk();
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    //下载失败调用这个方法
                    Toast.makeText(mContext, "亲，下载失败了，稍后再试吧...", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    super.onLoading(total, current, isUploading);
                    tv_splash_plan.setVisibility(View.VISIBLE);
                    tv_splash_plan.setText(current+"/"+total);
                }
            });
        }
    }

    //安装下载的新版本apk文件
    private void installApk() {
        //来自系统源码信息
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setDataAndType(Uri.fromFile(new File("/mnt/sdcard/mobilesafe.apk")),
                "application/vnd.android.package-archive");
        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //保证安装过程中，用户点击取消安装后能够进入应用的主界面
        super.onActivityResult(requestCode, resultCode, data);
        enterHome();
    }

    //跳转到主界面
    private void enterHome() {
        Intent intent = new Intent(mContext,HomeActivity.class);
        startActivity(intent);
        finish();//避免跳转到splash界面
    }

    //提醒用户更新版本
    private void update() {
        //连接服务器，查看当前是否存在更新版本，耗时操作，在子线程中执行
        new Thread(){
            @Override
            public void run() {
                Message message = Message.obtain();
                startTime = (int)System.currentTimeMillis();
                try {
                    //连接服务器
                    URL url=new URL("http://192.168.56.1/updateinfo.json");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(5000);//设置连接超时时间
                    connection.setReadTimeout(5000);//设置读取超时时间
                    connection.setRequestMethod("GET");//设置请求方式
                    int code = connection.getResponseCode();
                    if (code==200){
                        /**连接成功,获取服务器的最新版本号 apkcode：apk的版本号 apkURL：apk下载链接
                         *  des:描述信息，告诉用户增加的功能以及修改的bug
                         */
                        //获取数据之前，服务器是如何封装数据的,xml方式或者json形式
                        InputStream inputStream = connection.getInputStream();
                        //将获取到的流转化为字符串
                        String json = StreamUtil.parseStreamUtil(inputStream);
                        //解析json字符串
                        JSONObject object =  new JSONObject(json);
                        apkCode = object.getString("code");
                        apkurl = object.getString("apkurl");
                        apkdes = object.getString("des");
                        //查看是否有新版本
                        //判断服务器返回的与当前版本是否一致 不一致则有新版本
                        if (apkCode.equals(getVersionName())){
                            //没有最新版本
                            message.what=MSG_ENTER_HOME;

                        }else {
                            //有最新版本 弹出对话框 提醒用户更新版本 子线程不能更新UI
                            message.what=MSG_UPDATE_DIALOG;

                        }

                    }else {
                        //代表连接失败
                        message.what=MSG_SERVER_ERROR;
                    }


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    message.what=MSG_URL_ERROR;
                } catch (IOException e) {
                    message.what=MSG_IO_ERROR;
                    e.printStackTrace();
                } catch (JSONException e) {
                    message.what=MSG_JSON_ERROR;
                    e.printStackTrace();
                }finally {
                    //处理外网连接时间的问题
                    endTime = (int) System.currentTimeMillis();
                    int dTime=endTime-startTime;
                    if (dTime<2000){
                        SystemClock.sleep(2000-dTime);
                    }
                    //给主线程发送消息
                    handler.sendMessage(message);
                }
            }
        }.start();
    }

    //获取当前应用程序的版本号
    private String getVersionName() {
        try {
            PackageManager pm = getPackageManager();
            //0代表的是获取基础的信息，比如包名，版本号
            PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
            String versionName = info.versionName;
            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
