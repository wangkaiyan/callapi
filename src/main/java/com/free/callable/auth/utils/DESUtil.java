package com.free.callable.auth.utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

/**
 * Created by  on 2016/9/19.
 */
public class DESUtil {
    private static String KEYSTR = "-7-*d@#5EdxBvrTRe-#5CtUs";

    public DESUtil() {
    }

    private static Key getKey() {
        SecretKeySpec key = new SecretKeySpec(KEYSTR.getBytes(), "TripleDES");
        return key;
    }

    public static byte[] encrypt(byte[] inputByte) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
        Cipher encrypt = Cipher.getInstance("TripleDES");
        encrypt.init(1, getKey());
        byte[] ciperByte = encrypt.doFinal(inputByte);
        byte[] encode = XBase64.encodeBase64(ciperByte);
        return encode;
    }

    public static byte[] decrypt(byte[] inputByte) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
        byte[] encodeStr = XBase64.decodeBase64(inputByte);
        Cipher decrypt = Cipher.getInstance("TripleDES");
        decrypt.init(2, getKey());
        byte[] ciperByte = decrypt.doFinal(encodeStr);
        return ciperByte;
    }
}

