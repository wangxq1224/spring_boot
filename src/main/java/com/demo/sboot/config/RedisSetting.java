package com.demo.sboot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 
 * @ClassName: RedisSetting 
 * @Description: Redis配置信息
 * @author wangxq 
 * @date 2017年7月11日 下午2:32:21 
 *
 */
@Component
@ConfigurationProperties(prefix = "redis")
public class RedisSetting {
	private String host;
	private int port;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

}
