package com.free.callable.auth.struct;

import java.util.Map;

/**
 * Created by  on 2016/9/19.
 */

public class TokenRequest {
    private String uid;
    private String username;
    private String sourceType;
    private String expireTime;
    private Map<String, String> map;

    public TokenRequest(String uid, String username, String sourceType, String expireTime, Map<String, String> map) {
        this.uid = uid;
        this.username = username;
        this.sourceType = sourceType;
        this.expireTime = expireTime;
        this.map = map;
    }

    public String getUid() {
        return this.uid;
    }

    public String getUsername() {
        return this.username;
    }

    public String getSourceType() {
        return this.sourceType;
    }

    public String getExpireTime() {
        return this.expireTime;
    }

    public Map<String, String> getMap() {
        return this.map;
    }

    public String toString() {
        return "TokenRequest [uid=" + this.uid + ", username=" + this.username + ", sourceType=" + this.sourceType + ", expireTime=" + this.expireTime + ", map=" + this.map + "]";
    }
}
