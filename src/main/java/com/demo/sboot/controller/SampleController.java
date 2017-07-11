package com.demo.sboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo.sboot.config.RedisSetting;

@Controller
public class SampleController {
	
	@Autowired
	private RedisSetting redisSetting;

    @RequestMapping("/test")
    @ResponseBody
    String home() {
    	System.out.println(redisSetting.getHost()+"  "+redisSetting.getPort());
    	System.out.println("".substring(1,2));
        return "Hello World!";
    }


}
