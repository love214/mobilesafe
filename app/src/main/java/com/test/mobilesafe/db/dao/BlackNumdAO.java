package com.test.mobilesafe.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.test.mobilesafe.bean.BlackNumInfo;
import com.test.mobilesafe.db.BlackNumOpenHelper;
import java.util.ArrayList;
import java.util.List;

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
    public int queryBlackNumMode(String blacknum){
        int mode=-1;
        SQLiteDatabase db = openHelper.getReadableDatabase();
        Cursor cursor = db.query(BlackNumOpenHelper.DB_NAME, new String[]{"mode"}, "blacknum=?", new String[]{blacknum}, null, null, "_id desc");
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

    /**
     * 查询所有号码和拦截模式
     */
    public List<BlackNumInfo> queryAllBlackNum(){
        ArrayList<BlackNumInfo> list = new ArrayList<BlackNumInfo>();
        SQLiteDatabase db = openHelper.getReadableDatabase();
        Cursor cursor = db.query(BlackNumOpenHelper.DB_NAME, new String[]{"blacknum", "mode"}, null, null, null, null, "_id desc");
        while (cursor.moveToNext()){
            String blacknum = cursor.getString(0);
            int mode = cursor.getInt(1);
            BlackNumInfo blackNumInfo = new BlackNumInfo(blacknum,mode);
            list.add(blackNumInfo);
        }
        cursor.close();
        db.close();
        return list;
    }

    /**
     * 查询20条数据，分批加载
     */
    public List<BlackNumInfo> getPartBlackNum(int maxNum,int startindex){
        ArrayList<BlackNumInfo> list = new ArrayList<BlackNumInfo>();
        SQLiteDatabase db = openHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select blacknum,mode from info order by _id desc limit ? offset ?", new String[]{maxNum + "", startindex + ""});

        while (cursor.moveToNext()){
            String blacknum = cursor.getString(0);
            int mode = cursor.getInt(1);
            BlackNumInfo blackNumInfo = new BlackNumInfo(blacknum,mode);
            list.add(blackNumInfo);
        }
        cursor.close();
        db.close();
        return list;
    }
}
