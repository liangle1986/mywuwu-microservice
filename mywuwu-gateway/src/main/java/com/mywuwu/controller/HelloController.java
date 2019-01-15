package com.mywuwu.controller;

import com.mywuwu.common.annotation.CurrentUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: 梁乐乐
 * @Date: 2019/1/15 15:06
 * @Description:
 */

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello(@CurrentUser String userName){
        return "Hello," + userName;
    }


    @GetMapping("/hello1")
    public String hello1(String userName){
        return "Hello," + userName;
    }


}