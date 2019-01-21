package com.mywuwu.common.filter;

import com.mywuwu.common.utils.JwtTokenDto;
import com.mywuwu.common.utils.JwtTokenUtils;
import com.mywuwu.common.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @Auther: 梁乐乐
 * @Date: 2019/1/15 14:56
 * @Description:
 */
@Component
public class AuthFilter implements GlobalFilter, Ordered {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthFilter.class);

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${auth.skip.urls}")
    private String[] skipAuthUrls;

    @Value("${jwt.blacklist.key.format}")
    private String jwtBlacklistKeyFormat;

    @Value("${jwt.refresh.token.key.format}")
    private String jwtRefreshTokenKeyFormat;


    //    @Autowired
//    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public int getOrder() {
        return -100;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String url = exchange.getRequest().getURI().getPath();
        //跳过不需要验证的路径
        if (Arrays.asList(skipAuthUrls).contains(url)) {
            return chain.filter(exchange);
        }

        // 获取信息的解码形式
        if (JwtTokenDto.secretKey == null) {
            JwtTokenDto.secretKey = secretKey;
        }

        //从请求头中取出token
        String token = exchange.getRequest().getQueryParams().getFirst("token");
//                getHeaders().getFirst("token");
        //未携带token或token在黑名单内
        if (token == null ||
                token.isEmpty() ||
                isBlackToken(token)) {
            ServerHttpResponse originalResponse = exchange.getResponse();
            originalResponse.setStatusCode(HttpStatus.OK);
            originalResponse.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
            byte[] response = "{\"code\": \"401\",\"msg\": \"401 Unauthorized.\"}"
                    .getBytes(StandardCharsets.UTF_8);
            DataBuffer buffer = originalResponse.bufferFactory().wrap(response);
            return originalResponse.writeWith(Flux.just(buffer));
        }
        //取出token包含的身份

        String userName = (String) redisUtil.hget(String.format(jwtRefreshTokenKeyFormat, token), "userName");
        //JwtTokenUtils.getUsername(token);
        if (userName.isEmpty()) {
            ServerHttpResponse originalResponse = exchange.getResponse();
            originalResponse.setStatusCode(HttpStatus.OK);
            originalResponse.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
            byte[] response = "{\"code\": \"10002\",\"msg\": \"invalid token.\"}"
                    .getBytes(StandardCharsets.UTF_8);
            DataBuffer buffer = originalResponse.bufferFactory().wrap(response);
            return originalResponse.writeWith(Flux.just(buffer));
        }
        //将现在的request，添加当前身份
        ServerHttpRequest mutableReq = exchange.getRequest().mutate().header("token-userName", userName).build();
        ServerWebExchange mutableExchange = exchange.mutate().request(mutableReq).build();
        return chain.filter(mutableExchange);
    }

    /**
     * 判断token是否在黑名单内
     *
     * @param token
     * @return
     */
    private boolean isBlackToken(String token) {
        assert token != null;
        return redisUtil.hasKey(String.format(jwtBlacklistKeyFormat, token));
    }
}