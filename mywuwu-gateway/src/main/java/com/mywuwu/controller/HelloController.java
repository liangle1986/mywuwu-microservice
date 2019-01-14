package com.mywuwu.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @Auther: 梁乐乐
 * @Date: 2019/1/14 16:51
 * @Description:
 */
@RestController
@RequestMapping("/hello")
public class HelloController {
    @GetMapping("/getNews")
    public String getNews() throws IOException {
//        String news = OkhttpUtil.get("https://www.apiopen.top/journalismApi",null);
        return "asdfasfasdf";
    }

    @RequestMapping("/hello")
    public String hello() throws IOException {
        return "请求成功";
    }
}
