package com.demo.sboot.base;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @ClassName: GlobalExceptionHandler 
 * @Description: 全局异常处理（需要在application.yml中配置spring.mvc.throw-exception-if-no-handler-found=true spring.resources.addMappings=false）
 * @author wangxq 
 * @date 2017年7月11日 下午2:28:57 
 *
 */
@RestController
@ControllerAdvice //一个全局的异常处理器
public class GlobalExceptionHandler {


	/**
	 * 处理Exception异常和其所用子异常
	 * @param req
	 * @param e
	 * @return
	 * @throws Exception
	 */
	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public Object defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
		return "{\"error\":\"异常信息\"}";
	}

}
