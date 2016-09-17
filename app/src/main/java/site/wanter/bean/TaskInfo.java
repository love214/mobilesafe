package site.wanter.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by Huyanglin on 2016/9/17.
 */
public class TaskInfo {
    private String name;
    private Drawable icon;
    private long ramSize;
    private String packageName;
    private boolean isUser;
    private boolean isCheacked=false;

    public boolean isCheacked() {
        return isCheacked;
    }

    public void setCheacked(boolean cheacked) {
        isCheacked = cheacked;
    }

    public TaskInfo() {
    }

    public TaskInfo(String name, Drawable icon, long ramSize, String packageName, boolean isUser) {
        this.name = name;
        this.icon = icon;
        this.ramSize = ramSize;
        this.packageName = packageName;
        this.isUser = isUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public long getRamSize() {
        return ramSize;
    }

    public void setRamSize(long ramSize) {
        this.ramSize = ramSize;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public boolean isUser() {
        return isUser;
    }

    public void setUser(boolean user) {
        isUser = user;
    }
}
