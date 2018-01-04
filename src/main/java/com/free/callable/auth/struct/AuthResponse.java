package com.free.callable.auth.struct;

import java.util.Map;

/**
 * Created by  on 2016/9/19.
 */
public class AuthResponse {
    private String uid;
    private Map<String, String> attributes;
    private String ip;
    private String authedBy;
    private String acccountType;
    private Boolean is_expire;

    public AuthResponse(String uid, String authedBy, String acccountType, Boolean is_expire, Map<String, String> map) {
        this.uid = uid;
        this.authedBy = authedBy;
        this.acccountType = acccountType;
        this.attributes = map;
        this.is_expire = is_expire;
    }

    public Object getAttribute(String name) {
        return this.attributes.get(name);
    }

    public String getUid() {
        return this.uid;
    }

    public Map<String, String> getAttributes() {
        return this.attributes;
    }

    public String getIp() {
        return this.ip;
    }

    public String getAuthedBy() {
        return this.authedBy;
    }

    public String getAcccountType() {
        return this.acccountType;
    }

    public Boolean getIs_expire() {
        return this.is_expire;
    }
}
