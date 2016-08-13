package com.test.mobilesafe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Huyanglin on 2016/8/13.
 * MD5加密工具类
 */
public class MD5Util {
    public static String passwordMD5(String password) {
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] digest = messageDigest.digest(password.getBytes());
            for (int i=0;i<digest.length;i++){
                int result = digest[i] & 0xff;
                String hexString = Integer.toHexString(result);
                if (hexString.length()<2){
                    sb.append("0");
                }
                sb.append(hexString);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;

    }
}
