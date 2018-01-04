package com.free.callable.auth.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by  on 2016/9/19.
 */
public class XBase64 {
    public static byte[] encodeBase64(byte[] binaryData) {
        return Base64.encodeBase64(binaryData);
    }

    public static byte[] decodeBase64(byte[] binaryData) {
        return Base64.decodeBase64(binaryData);
    }

    /**
     * 把+还成-， 把/换成_
     *
     * @param base64Str
     * @return
     */
    public static String enReplace(String base64Str) {
        return StringUtils.isNotBlank(base64Str) ? StringUtils.replace(base64Str, "+", "-").replace("/", "_") : base64Str;
    }


    /**
     * 把+还成-， 把/换成_
     *
     * @param base64Str
     * @return
     */
    public static String deReplace(String base64Str) {
        return StringUtils.isNotBlank(base64Str) ? StringUtils.replace(base64Str, "-", "+").replace("_", "/") : base64Str;
    }
}
