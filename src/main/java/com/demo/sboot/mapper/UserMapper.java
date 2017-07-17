package com.demo.sboot.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.demo.sboot.entity.po.User;
import com.demo.sboot.page.Page;

public interface UserMapper {
	public List<User> listByPage(Page page);

}
