package com.demo.sboot.entity.po;

/**
 * 
 * @ClassName: Role 
 * @Description: 
 * @author wangxq 
 * @date 2017年7月14日 上午10:16:04 
 *
 */
public class Role {
	private int id;
	private String rolename;
	private String rolecode;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRolename() {
		return rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	public String getRolecode() {
		return rolecode;
	}

	public void setRolecode(String rolecode) {
		this.rolecode = rolecode;
	}

	@Override
	public String toString() {
		return "Role [id=" + id + ", rolename=" + rolename + ", rolecode=" + rolecode + "]";
	}

}
