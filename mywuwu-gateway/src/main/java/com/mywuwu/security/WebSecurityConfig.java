package com.mywuwu.security;

import com.mywuwu.gatewayfilter.JWTAuthenticationFilter;
import com.mywuwu.gatewayfilter.JWTLoginFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @Auther: 梁乐乐
 * @Date: 2019/1/14 16:56
 * @Description: 通过SpringSecurity的配置，将JWTLoginFilter，JWTAuthenticationFilter组合在一起
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig  extends WebSecurityConfigurerAdapter {

        @Autowired
        @Qualifier("userDetailsServiceImpl")
        private UserDetailsService userDetailsService;

        public BCryptPasswordEncoder bCryptPasswordEncoder(){
            return new BCryptPasswordEncoder();
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {

            http.cors().and().csrf().disable().authorizeRequests()
                    .antMatchers(HttpMethod.GET,"/hello/hello").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .formLogin()
                    .and()
                    .logout()
                    .and()
                    .addFilter(new JWTLoginFilter(authenticationManager()))
                    .addFilter(new JWTAuthenticationFilter(authenticationManager()));
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
        }

}
