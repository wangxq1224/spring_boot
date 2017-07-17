package com.demo.sboot.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo.sboot.config.RedisSetting;
import com.demo.sboot.entity.po.User;

@Controller
public class SampleController {
	
	@Autowired
	private RedisSetting redisSetting;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
    @RequestMapping("/test")
    @ResponseBody
    String home() {
    	List<User> users=jdbcTemplate.queryForList("select t from tuser t", User.class);
    		System.out.println(users.size());
    	System.out.println(redisSetting.getHost()+"  "+redisSetting.getPort());
    	System.out.println("".substring(1,2));
        return "Hello World!";
    }


}
