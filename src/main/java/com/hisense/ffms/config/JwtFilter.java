package com.hisense.ffms.config;

import com.hisense.ffms.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * 自定义一个filter， 用来拦截所有的请求判断是否携带Token
 * isAccessAllowed() 判断是否携带了有效的JwtToken
 * onAccessDenied() 是没有携带JwtToken的时候进行账号密码登录，登录成功允许访问，登录失败拒绝访问
 */
@Slf4j
public class JwtFilter extends AccessControlFilter {
    /**
     * 返回true，shiro就直接允许访问url
     * 返回false，shiro才会根据onAccessDenied的方法的返回值决定是否允许访问url
     * @param servletRequest
     * @param servletResponse
     * @param o
     * @return
     * @throws Exception
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        log.warn("isAccessAllowed 方法被调用");
        return false;
    }

    /**
     * 返回结果为true表明登录过
     * @param servletRequest
     * @param servletResponse
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        log.warn("onAccessDenied 方法被调用");
        // 这个地方和前端约定，要求前端将JWT放在请求的Header部分

        // 所以以后发起请求的时候就需要在Header中放一个token的属性，属性值就是对应的Token
//        HttpServletRequest request = (HttpServletRequest) servletRequest;
//        Enumeration<String> headerNames = request.getHeaderNames();
//        while (headerNames.hasMoreElements()){
//            System.out.println(headerNames.nextElement().toString());
//        }
        String jwt = JwtUtil.getTokenFromRequest((HttpServletRequest) servletRequest);
        log.info("请求的Header 中藏有jwtToken",jwt);
        try {

            //委托realm进行登录认证
            //所以这个地方最终还是调用JwtRealm进行的认证
            getSubject(servletRequest,servletResponse).login(new JwtToken(jwt));
            //也就是subject.login(token)
        } catch (AuthenticationException e) {
            log.error(e.getMessage());
            onLoginFail(servletResponse);
            // 调用下面的方法向客户端返回错误信息
            return false;
        }
        return true;
    }

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        httpServletResponse.setHeader("Access-control-Allow-Origin",httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods","GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers",httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个options请求，这里我们给options请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())){
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    private void onLoginFail(ServletResponse response) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED); //401状态码
        httpResponse.getWriter().write("login error");
    }


}
