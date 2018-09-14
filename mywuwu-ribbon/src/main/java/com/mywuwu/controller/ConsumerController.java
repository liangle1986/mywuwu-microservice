package com.mywuwu.controller;

import com.alibaba.fastjson.JSON;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RefreshScope
public class ConsumerController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Value("${paypal.mode}")
    private String name;

    @RequestMapping(value = "/getsub", method = RequestMethod.POST)
    @HystrixCommand(fallbackMethod = "findOrderFallback")
    public List<Map<String, Object>> sub(Integer a, Integer b) {
        System.out.println(name + " =================================");
        ServiceInstance instance = this.loadBalancerClient.choose("CLINBRAIN-SERVICE");
        if(instance != null){
            System.out.println("服务名"+ instance.getServiceId() + ";服务链接="+instance.getHost() + ";端口号="+instance.getPort());
            Map<String, Object> map = new HashMap<>();
            map.put("a",a);
            map.put("b",b);
            MultiValueMap<String, Integer> requestEntity = new LinkedMultiValueMap<>();
            requestEntity.add("a", a);
            requestEntity.add("b", b);
           /* MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
            postParameters.add("userCode", "291974");*/
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
//            headers.add("Content-Type", "application/json; charset=UTF-8");
            HttpEntity<MultiValueMap<String, Object>> r = new HttpEntity(requestEntity, headers);
            ResponseEntity responseEntity = restTemplate.getForEntity("http://"+instance.getServiceId()+"/testController/selectTest", String.class);
            return (List)JSON.parseArray(responseEntity.getBody().toString());
        }

//        return restTemplate.getForEntity("http://MYWUWU-SERVICE/add?a="+a+"&b="+b,String.class).getBody();
        return null;
    }

    public  List<Map<String, Object>>  findOrderFallback(Integer a, Integer b) {
        return null;
//        return "订单查找失败！";
    }

    @RequestMapping(value = "/select1", method = RequestMethod.GET)
    @HystrixCommand(fallbackMethod = "findOrderFallback1")
    public String select() {
        ServiceInstance instance = this.loadBalancerClient.choose("CLINBRAIN-SERVICE");
        if(instance != null){
            System.out.println("服务名"+ instance.getServiceId() + ";服务链接="+instance.getHost() + ";端口号="+instance.getPort());
            MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
//            Map<String, String> map = new HashMap<>();
            requestEntity.add("value","中文接收参数");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            HttpEntity<MultiValueMap<String, String>> r = new HttpEntity<>(requestEntity, headers);
           return restTemplate.postForObject("http://"+instance.getServiceId()+"/testController/selectTest",requestEntity, String.class);
        }
//        return restTemplate.getForEntity("http://MYWUWU-SERVICE/add?a="+a+"&b="+b,String.class).getBody();
        return null;
    }

    public String findOrderFallback1() {
        return "订单查找失败！";
    }


}