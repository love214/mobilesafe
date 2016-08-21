package com.test.mobilesafe.engine;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.SystemClock;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Huyanglin on 2016/8/21.
 * 获取联系人的工具类
 * 一定记得清单文件中加入权限
 */
public class ContactsEngine {

    //获取联系人的方法
    public static List<HashMap<String,String>> getAllContactsInfo(Context context){
        SystemClock.sleep(1000);//模拟数据加载耗时情景
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        //1.获取内容解析者
        ContentResolver resolver = context.getContentResolver();
        //2.获取内容提供者的地址 com.android.contacts
        //raw_contacts表的地址:raw_contacts
        //view_data表的地址:data
        Uri raw_uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri data_uri = Uri.parse("content://com.android.contacts/data");
        Cursor cursor = resolver.query(raw_uri, new String[]{"contact_id"}, null, null, null);
        //解析cursor
        while (cursor.moveToNext()){
            //获取查询的数据
            String contact_id=cursor.getString(0);
            //用于查询字段较多String contact_id = cursor.getString(cursor.getColumnIndex("contact_id"));
            //判断contact_id是否为空
            if(!TextUtils.isEmpty(contact_id)){

                Cursor view_cursor = resolver.query(data_uri, new String[]{"data1", "mimetype"},
                        "raw_contact_id=?", new String[]{contact_id}, null);
                //解析
                HashMap<String, String> map = new HashMap<String, String>();
                while (view_cursor.moveToNext()){

                    String data1 = view_cursor.getString(0);
                    String mimetype = view_cursor.getString(1);
                    if (mimetype.equals("vnd.android.cursor.item/phone_v2")){
                        //电话号码
                        map.put("phone",data1);
                    }else if (mimetype.equals("vnd.android.cursor.item/name")){
                        //姓名
                        map.put("name",data1);
                    }
                }
                list.add(map);

                view_cursor.close();
            }
        }
        cursor.close();

        return list;
    }
}
