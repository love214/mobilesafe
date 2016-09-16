package site.wanter.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by Huyanglin on 2016/9/16.
 */
public class AppInfo {
    private String name;
    private Drawable icon;
    private String packagName;
    private String verisonName;
    private boolean isSD;//是否安装到内存卡
    private boolean isUser;//是否是用户程序

    public AppInfo(){
        super();
    }
    public AppInfo(String name, Drawable icon, String packagName, String verisonName, boolean isSD, boolean isUser) {
        this.name = name;
        this.icon = icon;
        this.packagName = packagName;
        this.verisonName = verisonName;
        this.isSD = isSD;
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

    public String getPackagName() {
        return packagName;
    }

    public void setPackagName(String packagName) {
        this.packagName = packagName;
    }

    public String getVerisonName() {
        return verisonName;
    }

    public void setVerisonName(String verisonName) {
        this.verisonName = verisonName;
    }

    public boolean isSD() {
        return isSD;
    }

    public void setSD(boolean SD) {
        isSD = SD;
    }

    public boolean isUser() {
        return isUser;
    }

    public void setUser(boolean user) {
        isUser = user;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "name='" + name + '\'' +
                ", icon=" + icon +
                ", packagName='" + packagName + '\'' +
                ", verisonName='" + verisonName + '\'' +
                ", isSD=" + isSD +
                ", isUser=" + isUser +
                '}';
    }
}
