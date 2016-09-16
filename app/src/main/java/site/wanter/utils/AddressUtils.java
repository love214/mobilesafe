package site.wanter.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;

import java.util.List;

/**
 * Created by Huyanglin on 2016/8/24.
 * 后台服务工具类
 */
public class AddressUtils {
    /**
     * 动态获取服务是否开启
     */
    public static boolean isRunningService(Context context,String className){
        //获取进程管理者
        ActivityManager activityManager= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //获取正在运行的服务
        List<ActivityManager.RunningServiceInfo> runningServices = activityManager.getRunningServices(1000);
        for(ActivityManager.RunningServiceInfo runningServiceInfo:runningServices){
            //获取控件的标示
            ComponentName componentName = runningServiceInfo.service;
            String className1 = componentName.getClassName();//获取全类名，便于区分应用
            //将获取到的服务的全类名与传递过来的类名比较，一致则表示服务正在运行
            if (className.equals(className1)){
                return true;
            }
        }
        return false;
    }
}
