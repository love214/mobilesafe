package com.test.mobilesafe.engine;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.test.mobilesafe.MyApplication;
import com.test.mobilesafe.bean.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Huyanglin on 2016/9/16.
 */
public class AppEngine {


    public static List<AppInfo> getAppInfos(){
        Context context = MyApplication.getContext();
        List<AppInfo> list=new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> installedApplications = packageManager.getInstalledPackages(0);
        for(PackageInfo packageInfo:installedApplications){
            String packageName = packageInfo
                    .packageName;
            String versionName = packageInfo.versionName;
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            Drawable icon = applicationInfo.loadIcon(packageManager);
            String name = applicationInfo.loadLabel(packageManager).toString();
            boolean isUser;
            boolean isSD;
            int flags = applicationInfo.flags;//获取是否是系统程序以及是否安装在SD卡
            if ((ApplicationInfo.FLAG_EXTERNAL_STORAGE & flags)==ApplicationInfo.FLAG_EXTERNAL_STORAGE){
                isSD=true;
            }else {
                isSD=false;
            }
            if ((ApplicationInfo.FLAG_SYSTEM & flags)==ApplicationInfo.FLAG_SYSTEM){
                isUser=false;
            }else {
                isUser=true;
            }

            AppInfo appInfo = new AppInfo(name, icon, packageName, versionName, isSD, isUser);
            list.add(appInfo);
        }
        return list;
    }
}
