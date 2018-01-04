package com.free.callable.auth.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by  on 2016/9/19.
 */
public class MapSerializeUtil {
    public MapSerializeUtil() {
    }

    public static Map<String, String> ConvertBytes2Map(byte[] dataBytes) throws Exception {
        HashMap map = new HashMap();
        int offset = 0;
        String key = null;
        String value = null;
        byte[] lengthBytes = new byte[4];

        for(boolean value_len = false; dataBytes.length > offset; map.put(key, value)) {
            System.arraycopy(dataBytes, offset, lengthBytes, 0, 4);
            int key_len = my_bb_to_int(lengthBytes);
            if(key_len == 0) {
                break;
            }

            offset += 4;
            byte[] keyBytes = new byte[key_len];
            System.arraycopy(dataBytes, offset, keyBytes, 0, key_len);
            key = new String(keyBytes, "UTF-8");
            offset += key_len;
            System.arraycopy(dataBytes, offset, lengthBytes, 0, 4);
            int value_len1 = my_bb_to_int(lengthBytes);
            offset += 4;
            if(value_len1 > 0) {
                byte[] valueBytes = new byte[value_len1];
                System.arraycopy(dataBytes, offset, valueBytes, 0, value_len1);
                value = new String(valueBytes, "UTF-8");
                offset += value_len1;
            }
        }

        return map;
    }

    public static byte[] ConvertMap2Bytes(Map<String, String> map) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        Iterator i$ = map.entrySet().iterator();

        while(i$.hasNext()) {
            Map.Entry entry = (Map.Entry)i$.next();
            String key = (String)entry.getKey();
            byte[] keybytes = key.getBytes();
            int keybytes_leng = keybytes.length;
            byte[] keybytes_lengbytes = my_int_to_bb(keybytes_leng);
            byteStream.write(keybytes_lengbytes);
            byteStream.write(keybytes);
            String value = (String)entry.getValue();
            byte[] valuebytes = value.getBytes();
            int valuebytes_leng = valuebytes.length;
            byte[] valuebytes_lengbytes = my_int_to_bb(valuebytes_leng);
            byteStream.write(valuebytes_lengbytes);
            byteStream.write(valuebytes);
        }

        return byteStream.toByteArray();
    }

    public static byte[] my_int_to_bb(int myInteger) {
        return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(myInteger).array();
    }

    public static int my_bb_to_int(byte[] byteBarray) {
        return ByteBuffer.wrap(byteBarray).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }

    public static void main(String[] args) throws Exception {
        HashMap map = new HashMap();
        String value = "{\"accountId\":\"ff8080814d21d093014d21d093030000\",\"memberId\":\"ff8080814d21d093014d21d093260000\",\"name\":\"湿哒哒\",\"sourceType\":0}}";
        map.put("key", value);
        byte[] datas = ConvertMap2Bytes(map);
        System.out.println(datas.length);
        Map map1 = ConvertBytes2Map(datas);
        System.out.print((String)map1.get("key"));
    }
}
