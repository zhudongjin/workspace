package com.gold.common.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.gold.common.dubbo.service.HelloDubboService;
import java.util.Date;


@Service(version = "1.0.0")
public class HelloDubboServiceImpl implements HelloDubboService {

    @Override
    public String Hello(String str) {
        return "于"+ new Date() + ",发送者："+str;
    }
}
