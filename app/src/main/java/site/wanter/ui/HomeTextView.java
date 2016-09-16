package site.wanter.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Huyanglin on 2016/8/12.
 * 自定义控件入门
 */
public class HomeTextView extends TextView {

    //new TextView的时候调用
    public HomeTextView(Context context) {
        super(context);
    }

    //在布局文件中调用
    //AttributeSet保存了所有在布局文件中设置的所有属性
    public HomeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //AttributeSet保存了所有在布局文件中设置的所有属性，比两个参数的构造方法多了个样式文件
    public HomeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused() {
        //返回true 获取焦点 false则为不获取焦点
        return true;

    }
}
