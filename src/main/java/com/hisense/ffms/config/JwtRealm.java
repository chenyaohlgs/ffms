package com.hisense.ffms.config;

import com.hisense.ffms.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

@Slf4j
public class JwtRealm extends AuthorizingRealm {
    /**
     * 重写support
     * 标识这个Realm是专门用来验证JwtToken
     * 不负责验证其他的token
     * @param token
     * @return
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    // 授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    // 认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String jwt = (String) authenticationToken.getPrincipal();
        if (jwt == null){
            log.error("jwtToken 不允许为空");
            throw new NullPointerException();
        }
        if (!JwtUtil.checkToken(jwt)){
            log.error("用户名不正确");
            throw new UnknownAccountException();
        }
        String userId = JwtUtil.getUserIdByToken(jwt);
        log.info("在使用token登录" + userId);
        return new SimpleAuthenticationInfo(jwt,jwt,"JwtRealm");
    }
}
