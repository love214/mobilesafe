package site.wanter.bean;

/**
 * Created by Huyanglin on 2016/9/1.
 */
public class BlackNumInfo {
    /**
     * 黑名单号码、模式
     */
    private String blacknum;
    private int mode;

    public BlackNumInfo() {
        super();
    }

    public BlackNumInfo(String blacknum, int mode) {
        this.blacknum = blacknum;
        if (mode >= 0 && mode <= 2) {
            this.mode = mode;
        } else {
            mode = 0;
        }
    }

    public String getBlacknum() {
        return blacknum;
    }

    public void setBlacknum(String blacknum) {
        this.blacknum = blacknum;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        //取值只能为0.1.2
        if (mode >= 0 && mode <= 2) {
            this.mode = mode;
        } else {
            mode = 0;
        }

    }

    @Override
    public String toString() {
        return "BlackNumInfo{" +
                "blacknum='" + blacknum + '\'' +
                ", mode=" + mode +
                '}';
    }
}
