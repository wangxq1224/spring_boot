package com.demo.sboot.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisUtil {

	@Autowired
	private RedisTemplate<String, String> strRedisTemplate;
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	public void put(String key,String value){
		stringRedisTemplate.opsForValue().set(key, value);
	}
	
	@SuppressWarnings("unchecked")
	public void put(String key,List<String> values){
		stringRedisTemplate.opsForList().rightPushAll(key, values);
	}

}
