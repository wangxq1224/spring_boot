package com.demo.sboot.entity.po;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 
 * @ClassName: User 
 * @Description: 
 * @author wangxq 
 * @date 2017年7月14日 上午10:16:54 
 *
 */
public class User {

	private int id;
	private String username;
	private String nickname;
	
	@JSONField(serialize=false) 
	private String password;
	private Role role;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", nickname=" + nickname + ", password=" + password
				+ ", role=" + role + "]";
	}



}
