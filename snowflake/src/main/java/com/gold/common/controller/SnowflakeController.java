package com.gold.common.controller;

import com.gold.common.snowflake.SnowflakeIdWorker;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller()
public class SnowflakeController {

    @RequestMapping("/snowflakeNumber")
    @ResponseBody
    public String snowflakeNumber(){
        Long result = SnowflakeIdWorker.generateId();
        return result.toString();
    }
}
