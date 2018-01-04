package com.free.callable.auth.struct;

import com.google.common.collect.Multimap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by  on 2016/9/19.
 */
public class AuthRequest {
    private Multimap<String, String> headers;
    private String token;
    private String source;
    private String ip;
    private Map<String, String[]> parameterMap = new HashMap();
    private String url;
    private Map<String, Object> attributes = new HashMap();

    public AuthRequest(String token, String source, String ip) {
        this.source = source;
        this.ip = ip;
        this.token = token;
    }

    public AuthRequest(String token) {
        this.token = token;
    }

    public Multimap<String, String> getHeaders() {
        return this.headers;
    }

    public String getToken() {
        return this.token;
    }

    public String getSource() {
        return this.source;
    }

    public String getIp() {
        return this.ip;
    }

    public Map<String, String[]> getParameterMap() {
        return this.parameterMap;
    }

    public String getUrl() {
        return this.url;
    }

    public Map<String, Object> getAttributes() {
        return this.attributes;
    }
}
