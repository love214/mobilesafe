package com.test.mobilesafe.db.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;

/**
 * Created by Huyanglin on 2016/8/23.
 * 手机号码归属地查询
 */
public class AddressDao {

    public static String queryAddress(Context context, String num){
        String location = "";
        //获取数据库路径
        File file=new File(context.getFilesDir(),"address.db");
        //打开数据库
        //getAbsolutePath()获取文件的绝对路径
        SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openDatabase(file.getAbsolutePath(),
                null, SQLiteDatabase.OPEN_READONLY);//以只读的方式打开
        //使用正则表达式进行判断号码是否符合规则
        //^1[34578]\d{9}$    正则表达式
        if (num.matches("^1[34578]\\d{9}$")){
            Cursor cursor = sqLiteDatabase.rawQuery("select location from data2 where id=(select outkey from data1 where id=?)",
                    new String[]{num.substring(0, 7)});
            //解析cursor
            if (cursor.moveToNext()){
                //因为查询出来的只有一条数据，所以此处不需要用while，直接判断是否为空就可以了
                location = cursor.getString(0);
            }
            cursor.close();

        }else {
            switch (num.length()) {
                case 3:
                    location = "特殊电话";
                    break;

                case 5:
                    location = "客服电话";
                    break;

                case 7:
                case 8:
                    location = "本地固定电话";
                    break;
                default:
                    if (num.length() > 10 && num.startsWith("0")) {
                        String result = num.substring(1, 3);
                        Cursor cursor = sqLiteDatabase.rawQuery("select location from data2 where area=?",
                                new String[]{result});
                        if (cursor.moveToNext()) {
                            location = cursor.getString(0);
                            location = location.substring(0, location.length() - 2);
                            cursor.close();
                        } else {
                            //3位没查到，查4位区号
                            result = num.substring(1, 4);
                            cursor = sqLiteDatabase.rawQuery("select location from data2 where area=?",
                                    new String[]{result});
                            if (cursor.moveToNext()) {
                                location = cursor.getString(0);
                                location = location.substring(0, location.length() - 2);
                                cursor.close();
                            }
                        }

                        break;
                    }
            }
        }
        sqLiteDatabase.close();
        return location;
    }
}
