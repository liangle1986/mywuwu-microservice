package com.mywuwu.gatewayfilter;

import com.mywuwu.common.utils.JwtTokenUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @Auther: 梁乐乐
 * @Date: 2019/1/14 17:04
 * @Description:
 * token的校验
 * 该类继承自BasicAuthenticationFilter，在doFilterInternal方法中，
 * 从http头的Authorization 项读取token数据，然后用Jwts包提供的方法校验token的合法性。
 * 如果校验通过，就认为这是一个取得授权的合法请求
 */
public class JWTAuthenticationFilter extends BasicAuthenticationFilter {

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager){
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(JwtTokenUtils.TOKEN_HEADER);

        // 如果请求头中没有Authorization信息则直接放行了
        if(header == null || !header.startsWith(JwtTokenUtils.TOKEN_PREFIX)){
            chain.doFilter(request,response);
            return;
        }

        // 如果请求头中有token，则进行解析，并且设置认证信息
        UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(header);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request,response);
    }

    /**
     *这里从token中获取用户信息并新建一个token
     */
    private UsernamePasswordAuthenticationToken getAuthentication(String header) {

        String token = header.replace(JwtTokenUtils.TOKEN_PREFIX, "");
        String principal = JwtTokenUtils.getUsername(token);

        if (principal != null) {
            return new UsernamePasswordAuthenticationToken(principal, null, new ArrayList<>());
        }
        return null;
    }
}
