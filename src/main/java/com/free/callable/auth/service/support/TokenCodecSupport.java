package com.free.callable.auth.service.support;

import com.free.callable.auth.struct.AuthException;
import com.free.callable.auth.utils.*;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by  on 2016/9/19.
 */
public class TokenCodecSupport {
    public TokenCodecSupport() {
    }

    public static String generateToken(Map<String, String> map) throws Exception {
        /*byte[] datas = MapSerializeUtil.ConvertMap2Bytes(map);
        byte[] dest = new byte[datas.length + 1];
        System.arraycopy(datas, 0, dest, 1, datas.length);
        dest[0] = AuthConstants.Magic;
        byte[] encryptDatas = DESUtil.encrypt(dest);
        String encCode = new String(encryptDatas);
        String md5 = MD5Utils.md5(encCode);
        byte[] digestbytes = RSAUtils.encryptByPrivateKey(md5.getBytes(), AuthConstants.privatekey);
        String version = AuthConstants.version;
        byte[] allbytes = new byte[encryptDatas.length + digestbytes.length];
        System.arraycopy(encryptDatas, 0, allbytes, 0, encryptDatas.length);
        System.arraycopy(digestbytes, 0, allbytes, encryptDatas.length, digestbytes.length);
        String token = version + RSAUtils.base64Encode(allbytes);
        token = prepareAfterBase64Encode(token);
        token = URLEncoder.encode(token, "UTF-8");
        return token;*/

        byte[] datas = MapSerializeUtil.ConvertMap2Bytes(map);
        byte[] dest = new byte[datas.length + 1];
        System.arraycopy(datas, 0, dest, 1, datas.length);
        dest[0] = AuthConstants.Magic;
        byte[] encryptDatas = DESUtil.encrypt(dest);
        String token = new String(encryptDatas);
        token = prepareAfterBase64Encode(token);
        token = URLEncoder.encode(token, "UTF-8");
        return token;
    }

    public static Map<String, String> parseToken(String token) throws Exception {
        if (token != null && token.length() != 0) {
            token = URLDecoder.decode(token, "UTF-8");
            token = prepareBeforeBase64Decode(token);
            byte[] decCode = DESUtil.decrypt(token.getBytes());
            if (decCode[0] != AuthConstants.Magic) {
                throw new Exception("token magic is wrong!");
            } else {
                byte[] decData = new byte[decCode.length - 1];
                System.arraycopy(decCode, 1, decData, 0, decCode.length - 1);
                Map map = MapSerializeUtil.ConvertBytes2Map(decData);
                return map;
            }
        } else {
            throw new AuthException("token is null!");
        }
    }

    private static String prepareBeforeBase64Decode(String Token) {
        Token = Token.replace("-", "+").replace("_", "/").replace(".", "=");
        return Token;
    }

    private static String prepareAfterBase64Encode(String Token) {
        Token = Token.replace("+", "-").replace("/", "_").replace("=", ".");
        return Token;
    }
}
