package com.mywuwu.common.utils;

/**
 * @Auther: 梁乐乐
 * @Date: 2019/1/15 17:22
 * @Description:
 */
public class JwtTokenDto {

    public static String secretKey;
    /**
     * 选择了记住我之后的过期时间为7天
     */
    public static long tokenExpireTime;

    /**
     * 过期时间是3600秒，既是1个小时
     */
    public static long refreshTokenExpireTime;

    public static String jwtRefreshTokenKeyFormat;

    public static String jwtBlacklistKeyFormat;

    /**
     * 签发者
     */
    public static final String ISS = "LLL";

    public static void setJwtTokenUtils(String secretKey, long tokenExpireTime, long refreshTokenExpireTime, String jwtRefreshTokenKeyFormat, String jwtBlacklistKeyFormat) {
        JwtTokenDto.secretKey = secretKey;
        JwtTokenDto.tokenExpireTime = tokenExpireTime;
        JwtTokenDto.refreshTokenExpireTime = refreshTokenExpireTime;
        JwtTokenDto.jwtRefreshTokenKeyFormat = jwtRefreshTokenKeyFormat;
        JwtTokenDto.jwtBlacklistKeyFormat = jwtBlacklistKeyFormat;
    }

}
