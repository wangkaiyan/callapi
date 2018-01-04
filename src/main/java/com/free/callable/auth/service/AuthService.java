package com.free.callable.auth.service;

/**
 * Created by  on 2016/9/19.
 */

import java.util.HashMap;
import java.util.Map;

import com.free.callable.auth.service.support.TokenCodecSupport;
import com.free.callable.auth.struct.AuthException;
import com.free.callable.auth.struct.AuthRequest;
import com.free.callable.auth.struct.AuthResponse;
import com.free.callable.auth.struct.TokenRequest;
import com.free.callable.auth.utils.AuthConstants;
import com.free.framework.common.util.JsonUtil;
import org.apache.log4j.Logger;

public class AuthService {
    private static final Logger log = Logger.getLogger(AuthService.class);

    public AuthService() {
    }

    public static String generateToken(TokenRequest request) throws AuthException {
        HashMap map = new HashMap();
        map.put(AuthConstants.TOKEN_UID, request.getUid());
        map.put(AuthConstants.TOKEN_USERNAME, request.getUsername());
        map.put(AuthConstants.TOKEN_SOURCE_TYPE, request.getSourceType());
        map.put(AuthConstants.TOKEN_EXPIRE_TIME, request.getExpireTime());
        map.putAll(request.getMap());
        String token = null;
        try {
            token = TokenCodecSupport.generateToken(map);
            return token;
        } catch (Exception e) {
            log.error("generate token error" + JsonUtil.Java2Json(request) + e);
            throw new AuthException("generate token error" + request.toString());
        }
    }

    public static AuthResponse auth(AuthRequest request) throws AuthException {
        Map map;
        try {
            map = TokenCodecSupport.parseToken(request.getToken());
        } catch (Exception e) {
            log.error("auth token error " + JsonUtil.Java2Json(request) + e);
            throw new AuthException("auth token error" + request.toString());
        }

        Boolean is_expire = Boolean.valueOf(true);
        if(Long.valueOf((String)map.get(AuthConstants.TOKEN_EXPIRE_TIME)).longValue() > System.currentTimeMillis()) {
            is_expire = Boolean.valueOf(false);
        }

        AuthResponse response = new AuthResponse((String)map.get(AuthConstants.TOKEN_UID), "Token_auth", (String)map.get(AuthConstants.TOKEN_SOURCE_TYPE), is_expire, map);
        return response;
    }
}
