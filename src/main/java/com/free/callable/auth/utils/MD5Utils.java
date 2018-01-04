package com.free.callable.auth.utils;

import java.security.MessageDigest;

/**
 * Created by  on 2016/9/19.
 */
public class MD5Utils {
    private static ThreadLocal<MessageDigest> MD5 = new ThreadLocal() {
        protected MessageDigest initialValue() {
            try {
                return MessageDigest.getInstance("MD5");
            } catch (Exception var2) {
                return null;
            }
        }
    };

    public MD5Utils() {
    }

    public static String md5(String src) {
        return md5(src.getBytes());
    }

    public static String md5(byte[] bytes) {
        MessageDigest md5 = (MessageDigest)MD5.get();
        md5.reset();
        md5.update(bytes);
        byte[] digest = md5.digest();
        return encodeHex(digest);
    }

    public static String encodeHex(byte[] bytes) {
        StringBuilder buf = new StringBuilder(bytes.length + bytes.length);

        for(int i = 0; i < bytes.length; ++i) {
            if((bytes[i] & 255) < 16) {
                buf.append("0");
            }

            buf.append(Long.toString((long)(bytes[i] & 255), 16));
        }

        return buf.toString();
    }

    public static void main(String[] args) {
        System.out.println(md5("12312312312312"));
    }
}
