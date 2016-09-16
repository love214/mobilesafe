package site.wanter.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

/**
 * Created by Huyanglin on 2016/8/9.
 */
public class StreamUtil {

    //将流信息转换成字符串
    public static String parseStreamUtil(InputStream in) throws IOException {
        //字符流，读取流
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        //写入流
        StringWriter sw = new StringWriter();
        //读写操作
        String str=null;
        while ((str=reader.readLine()) !=null){
            //写入操作
            sw.write(str);
        }
        sw.close();
        reader.close();

        return sw.toString();
    }
}
