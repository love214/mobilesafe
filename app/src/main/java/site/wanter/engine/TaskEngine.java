package site.wanter.engine;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Debug;

import java.util.ArrayList;
import java.util.List;

import site.wanter.MyApplication;
import site.wanter.bean.TaskInfo;

/**
 * Created by Huyanglin on 2016/9/17.
 */
public class TaskEngine {

    private static Context mContext;
    /**
     * 获取系统所有的进程信息
     * @return
     */
    public static List<TaskInfo> getTaskAllInfo(){
        List<TaskInfo> list=new ArrayList<>();
        mContext=MyApplication.getContext();
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        PackageManager pm = mContext.getPackageManager();
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        for(ActivityManager.RunningAppProcessInfo appProcesses :runningAppProcesses){
            TaskInfo taskInfo = new TaskInfo();
            String packageName = appProcesses.processName;
            taskInfo.setPackageName(packageName);
            //后去进程占用的空间
            Debug.MemoryInfo[] memoryInfo = activityManager.getProcessMemoryInfo(new int[]{appProcesses.pid});
            int totalPss = memoryInfo[0].getTotalPss();
            int ramSize = totalPss * 1024;
            taskInfo.setRamSize(ramSize);
            ApplicationInfo applicationInfo = null;
            try {
                applicationInfo = pm.getApplicationInfo(packageName, 0);
                Drawable icon = applicationInfo.loadIcon(pm);
                taskInfo.setIcon(icon);
                String name = applicationInfo.loadLabel(pm).toString();
                taskInfo.setName(name);
                //判断是否是系统程序
                int flags = applicationInfo.flags;
                boolean isUser;
                if ((applicationInfo.FLAG_SYSTEM & flags)==applicationInfo.FLAG_SYSTEM){
                    isUser=false;
                }else {
                    isUser=true;
                }
                taskInfo.setUser(isUser);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            list.add(taskInfo);
        }
        return list;
    }
}
