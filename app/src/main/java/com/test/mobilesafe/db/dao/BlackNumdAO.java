package com.test.mobilesafe.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.test.mobilesafe.db.BlackNumOpenHelper;

/**
 * Created by Huyanglin on 2016/9/1.
 */
public class BlackNumDao {

    public static final int CALL=0;//拦截电话
    public static final int SMS=1;//拦截短信
    public static final int ALL=2;//拦截全部

    private BlackNumOpenHelper openHelper;

    public BlackNumDao(Context context) {
        openHelper = new BlackNumOpenHelper(context);
    }

    /**
     *添加黑名单号码
     */
    public void addBlackNum(String blacknum,int mode){
        SQLiteDatabase db = openHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("blacknum",blacknum);
        values.put("mode",mode);
        db.insert(BlackNumOpenHelper.DB_NAME,null,values);
        db.close();//关闭数据库，防止内存溢出
    }

    /**
     * 更新黑名单的拦截模式
     */
    public void updateBlackNum(String blacknum,int mode){
        SQLiteDatabase db = openHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("mode",mode);
        db.update(BlackNumOpenHelper.DB_NAME,values,"blacknum=?",new String[]{blacknum});
        db.close();
    }

    /**
     * 查询拦截模式
     */
    public int queryBlackNum(String blacknum){
        int mode=-1;
        SQLiteDatabase db = openHelper.getReadableDatabase();
        Cursor cursor = db.query(BlackNumOpenHelper.DB_NAME, new String[]{"mode"}, "blacknum=?", new String[]{blacknum}, null, null, null);
        if (cursor.moveToNext()){
            mode = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return mode;
    }

    /**
     * 删除黑名单
     */
    public void deleteBlackNum(String blacknum){
        SQLiteDatabase db = openHelper.getWritableDatabase();
        db.delete(BlackNumOpenHelper.DB_NAME,"blacknum=?",new String[]{blacknum});
        db.close();
    }
}
