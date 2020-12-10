package com.hisense.ffms.config;

import org.apache.shiro.authc.AuthenticationToken;

public class JwtToken implements AuthenticationToken {

    private String jwt;

    public JwtToken(String jwt){
        this.jwt = jwt;
    }

    // 类似用户名
    @Override
    public Object getPrincipal() {
        return jwt;
    }

    // 类似密码
    @Override
    public Object getCredentials() {
        return jwt;
    }
}
