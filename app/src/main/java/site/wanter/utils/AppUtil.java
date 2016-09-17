package site.wanter.utils;

import android.app.ActivityManager;
import android.content.Context;
import java.io.File;
import site.wanter.MyApplication;

/**
 * Created by Huyanglin on 2016/9/17.
 */
public class AppUtil {
    /**
     * 获取SD卡可用空间
     */
    public static long getAvailableSD(){
        //API高于18时调用
        //File path = Environment.getExternalStorageDirectory();
        //StatFs statFs = new StatFs(path.getPath());
        //int blockSize = statFs.getBlockSize();
        //long totalBytes = statFs.getTotalBytes();
        //int availableBlocks = statFs.getAvailableBlocks();
        //return availableBlocks*blockSize;

        long freeBytesExternal = new File(MyApplication.getContext().getExternalFilesDir(null).toString()).getFreeSpace();
        return freeBytesExternal;
    }

    /**
     * 获取手可用空间
     * @return 可用空间
     */
    public static long getAvailableROM(){
        //API高于18时候调用
        //File path = Environment.getDataDirectory();
        //StatFs statFs = new StatFs(path.getPath());
        //int blockSize = statFs.getBlockSize();
        //long totalBytes = statFs.getTotalBytes();
        //int availableBlocks = statFs.getAvailableBlocks();
        //return availableBlocks*blockSize;
        ActivityManager am = (ActivityManager) MyApplication.getContext().getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        //mi.availMem; 当前系统的可用内存

        return mi.availMem;
    }
}
