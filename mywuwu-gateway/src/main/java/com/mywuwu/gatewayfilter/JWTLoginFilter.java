package com.mywuwu.gatewayfilter;

import com.mywuwu.common.dto.JwtUser;
import com.mywuwu.common.utils.JwtTokenUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @Auther: 梁乐乐
 * 验证用户名密码正确后，生成一个token，并将token返回给客户端
 * 该类继承自UsernamePasswordAuthenticationFilter，重写了其中的2个方法
 * attemptAuthentication ：接收并解析用户凭证。
 * successfulAuthentication ：用户成功登录后，这个方法会被调用，我们在这个方法里生成token。
 * @Date: 2019/1/14 17:00
 * @Description:
 */
public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public JWTLoginFilter(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
    }

    /**
     *接收并解析用户凭证
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        //从输入流中获取登录信息
        try{
            System.out.println("尝试登录");

            //手机号
            String phone = request.getParameter("phone");

            //用户openId
            String openId = request.getParameter("openId");

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            phone,openId,new ArrayList<>()
                    )
            );
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 用户成功登录后，这个方法会被调用，我们在这个方法里生成token
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        String principal = ((JwtUser)authResult.getPrincipal()).getUsername();

        String token = JwtTokenUtils.createToken(principal,false);

        System.out.println("【登录成功，token->】"+JwtTokenUtils.TOKEN_PREFIX+token);
        response.addHeader(JwtTokenUtils.TOKEN_HEADER,JwtTokenUtils.TOKEN_PREFIX+token);
    }

    /**
     * 这是验证失败时候调用的方法
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.getWriter().write("authentication failed, reason: " + failed.getMessage());
    }
}
