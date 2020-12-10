package com.hisense.ffms.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * JWT 字符串分为三个部分
 * 1。Header
 * 存储两个变量
 *      密钥（可以用来对比）
 *      算法（也就是下面将Header和payload加密成Signature）
 * 2.payload
 * 存储很多东西，基础信息有如下几个
 *      签发人，也就是这个“令牌”归属于哪个用户。一般是userId
 *      创建时间，也就是这个令牌是什么时候创建的
 *      失效时间，也就是这个令牌什么时候失效
 *      唯一标识，一般可以使用算法生成一个唯一标识
 * 3.Signature
 *  这个是上面两个金国Header中的加密生成的，用于比对信息，防止篡改Header和payload
 * 然后将这三个部分的信息经过加密生成一个JwtToken的字符串，发送给客户端，客户端保存在本地。
 * 当客户端发送请求的时候携带这个到服务器端（可以是在cookie，可以实在header，可以是在localStorage中），在服务端进行验证
 */
public class JwtUtil {

    // 生成签名是所使用的秘钥
    private static final String secretKey = "ukc8BDbRigUDaY6pZFfWus2jZWLPHO";

    //生成签名的时候所使用的的加密算法
    private static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    private static final long ttlMillis = 1000 * 60 * 60 * 24; //token过期时间

    /**
     * 生成JWT Token 字符串
     *
     * @param iss 签发人名称
     * @param claims 额外添加到荷部分的信息。
     *               例如可以添加用户名、用户ID、用户（加密前的）密码等信息
     * @return
     */
    public static String encode(String iss, String id) {

        // 签发时间（）：荷载部分的标准字段之一
        long nowMills = System.currentTimeMillis();
        Date now = new Date(nowMills);

        //下面就是在为payload添加各种标准声明和私有声明
        JwtBuilder builder = Jwts.builder()
                // 荷载部分的非标准字段/附加字段，一般下载标准的字段之前。
                .claim("userId",id)
                // JWT ID(jti) : 荷载部分的标准字段之一，JWT的唯一标识，虽不强求，但尽量确保其唯一性
                .setId(UUID.randomUUID().toString())
                // 签发时间(iat) :荷载部分的标准字段之一，代表这个 JWT 的生成时间
                .setIssuedAt(now)
                // 过期时间：荷载部分的标准字段之一，代表这个Jwt 的有效期
                .setExpiration(new Date(nowMills + ttlMillis))
                // 签发人（iss）：荷载部分的标准字段之一，代表这个JWT 的所有者。通常是username
                .setSubject(iss)
                // 设置生成签名的算法和密钥
                .signWith(signatureAlgorithm,secretKey);

        return builder.compact();
    }

    /**
     * JWT Token 由 头部 荷载部 和 签名部 三部分组成。签名部分是由加密算法生成，无法反向解密。
     * 而 头部 和 荷载部分是由 Base64 编码算法生成，是可以反向反编码回原样的。
     * 这也是为什么不要在 JWT Token 中放敏感数据的原因。
     *
     * @param jwtToken 加密后的token
     * @return claims 返回荷载部分的键值对 本质是一个map集合
     */
    public static Claims decode(String jwtToken) {

        // 得到 DefaultJwtParser
        return Jwts.parser()
                // 设置签名的秘钥
                .setSigningKey(secretKey)
                // 设置需要解析的 jwt
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    /**
     * 校验 token
     * 在这里可以使用官方的校验，或，
     * 自定义校验规则，例如在 token 中携带密码，进行加密处理后和数据库中的加密密码比较。
     *
     * @param jwtToken 被校验的 jwt Token
     */
    public static boolean checkToken(String jwtToken) {
        if (StringUtils.isEmpty(jwtToken)){
            return false;
        }
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 从request中获取token
     * @param request
     * @return
     */
    public static String getTokenFromRequest(HttpServletRequest request){
        String jwtToken = "";
        try {
            jwtToken = request.getHeader("token");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jwtToken;
    }

    /**
     * 判断request中的token是否有效
     * @param request
     * @return
     */
    public static boolean checkToken(HttpServletRequest request){
        return checkToken(getTokenFromRequest(request));
    }

    /**
     * 根据request中的token获取用户id
     * @param request
     * @return
     */
    public static String getUserIdByRequest(HttpServletRequest request){
        String token = getTokenFromRequest(request);
        return getUserIdByToken(token);
    }

    public static String getUserIdByToken(String token){
        return (String) decode(token).get("sub");
    }
    public static void main(String[] args) {


        String jwtToken = JwtUtil.encode("tom", "123456");

        System.out.println(jwtToken);
        /*
        util.isVerify(jwtToken);
        System.out.println("合法");
        */
        System.out.println(JwtUtil.checkToken(jwtToken));
        JwtUtil.decode(jwtToken).entrySet().forEach((entry) -> {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        });

    }
}
