package com.demo.sboot;


import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.demo.sboot.entity.po.Role;
import com.demo.sboot.entity.po.User;
import com.demo.sboot.mapper.UserMapper;
import com.demo.sboot.page.Page;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=App.class)
public class TestA {
	
	@Autowired
	private UserMapper userMapper;

	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	
	
	@Test
	public void testMapper() {
		Page page=new Page();
		page.setCurrentPage(1);
		page.setPageSize(2);
		List<User> users=userMapper.listByPage(page);
		System.out.println(users.size());
		System.out.println(users.get(0).toString());
		
		User user=new User();
		user.setId(1);
		Role role=new Role();
		role.setId(2);
		role.setRolecode("rolecode");
		user.setRole(role);
		System.out.println(JSON.toJSONString(user, SerializerFeature.WriteMapNullValue));
	}
	
	@Test
	public void testRedis(){
		stringRedisTemplate.execute(new SessionCallback<List<Object>>() {

			@Override
			public List<Object> execute(RedisOperations operations) throws DataAccessException {
				operations.multi();
				operations.opsForValue().set("123", "123456");
				
				if(true){
			        throw new RuntimeException();
			    }
			
				operations.opsForValue().set("123", "1234561234567");
				return operations.exec();
			}
			
		});
        stringRedisTemplate.opsForValue().set("aaa", "111中文");
        Assert.assertEquals("111", stringRedisTemplate.opsForValue().get("aaa"));
	}

}
