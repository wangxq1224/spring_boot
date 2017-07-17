package com.demo.sboot.base;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.stereotype.Component;

/**
 * 
 * @ClassName: ContextInit 
 *
 * @Description: 
 *
 * @author wangxq 
 * @date 2017年7月17日 上午11:13:07 
 *
 */
@Component
public class ContextInit implements ServletContextInitializer{

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		// TODO Auto-generated method stub
		System.out.println(",,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,");
	}

}
