package site.wanter.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Huyanglin on 2016/9/1.
 */
public class BlackNumOpenHelper extends SQLiteOpenHelper {
    public static final String DB_NAME="info";//数据库表明
    private String CREATEDB="create table "+DB_NAME+"(_id integer primary key autoincrement,blacknum text,mode text)";

    public BlackNumOpenHelper(Context context) {
        super(context, "blacknum.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //数据库创建的时候调用
        db.execSQL(CREATEDB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
