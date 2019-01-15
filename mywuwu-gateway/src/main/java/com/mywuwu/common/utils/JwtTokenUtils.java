package com.mywuwu.common.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

/**
 * @Auther: 梁乐乐
 * @Date: 2019/1/14 16:54
 * @Description:
 */
public class JwtTokenUtils {



    /**
     * 创建token
     *
     * @param username
     * @return
     */
    public static String createToken(String username) {

        //生成jwt
        Date now = new Date();
        Algorithm algo = Algorithm.HMAC256(JwtTokenDto.secretKey);
        String token = JWT.create()
                .withIssuer(JwtTokenDto.ISS)
                .withIssuedAt(now)
                .withExpiresAt(new Date(now.getTime() + JwtTokenDto.tokenExpireTime))
                .withClaim("userName", username)//保存身份标识
                .sign(algo);
        return token;
    }

    /**
     * 从token中获取用户名
     *
     * @param token
     * @return
     */
    public static String getUsername(String token) {
        return getDecodedJWT(token).getClaim("userName").asString();
    }


    /**
     * 判断是否已过期
     *
     * @param token
     * @return
     */
    public static boolean isExpiration(String token) {
        return getDecodedJWT(token).getExpiresAt().before(new Date());
    }

    /**
     * JWT验证
     *
     * @param token
     * @return userName
     */
    private static DecodedJWT getDecodedJWT(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(JwtTokenDto.secretKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(JwtTokenDto.ISS)
                    .build();
            return verifier.verify(token);
        } catch (JWTVerificationException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

}
