package com.test.mobilesafe.activity;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.test.mobilesafe.R;
import com.test.mobilesafe.utils.StreamUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Huyanglin on 2016/8/9.
 */
public class SplashActivity extends Activity {

    private TextView tv_splash_versionname;
    private String apkCode;
    private String apkurl;
    private String apkdes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash_activity);
        tv_splash_versionname = (TextView) findViewById(R.id.tv_splash_versionname);
        tv_splash_versionname.setText("版本号 "+getVersionName());
        update();
    }

    //提醒用户更新版本
    private void update() {
        //连接服务器，查看当前是否存在更新版本，耗时操作，在子线程中执行
        new Thread(){
            @Override
            public void run() {
                //连接服务器
                try {
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

                        //// TODO: 2016/8/9  完成apk信息的获取


                    }else {
                        //连接失败
                    }


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
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
