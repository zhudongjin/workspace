package com.gold.common.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gold.common.dubbo.service.HelloDubboService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloDubboController {

    /**注意 一定要配置，否则无法调通*/
    private static final String URL="dubbo://192.168.3.1:20880";

    @Reference(url = URL,version = "1.0.0")
    HelloDubboService helloDubboService;

    @RequestMapping("/sayHello")
    public String sendHello(){
        return helloDubboService.Hello("gold");
    }
}
