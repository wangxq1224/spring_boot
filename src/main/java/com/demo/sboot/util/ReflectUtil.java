package com.demo.sboot.util;

import java.lang.reflect.Field;

/**
 * 
 * @ClassName: ReflectUtil 
 *
 * @Description: 反射工具类
 *
 * @author wangxq 
 * @date 2017年7月14日 下午3:08:58 
 *
 */
public class ReflectUtil {
	
	/**
	 * 获取属性
	 * @param obj 
	 * @param fieldName
	 * @return
	 */
	public static Field getField(Object obj,String fieldName){
		for(Class<?> classT= obj.getClass();classT!=Object.class;classT=classT.getSuperclass()){
			try {
				return classT.getDeclaredField(fieldName);
			}catch (Exception e) {
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param obj
	 * @param fieldName
	 * @return
	 */
	public static Object getFieldValue(Object obj,String fieldName){
		Field field=getField(obj, fieldName);
		Object value=null;
		try {
			if(field!=null){
				if(field.isAccessible()){
					value=field.get(obj);
				}else{
					field.setAccessible(true);
					value=field.get(obj);
					field.setAccessible(false);
				}
				
			}
		}  catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}
	
	/**
	 * 给对象的某个属性赋值
	 * @param obj
	 * @param fieldName
	 * @param value
	 */
	public static void setFieldValue(Object obj,String fieldName,Object value){
		try {
		Field field=obj.getClass().getDeclaredField(fieldName);
			if(field!=null){
				if(field.isAccessible()){
					field.set(obj, value);
				}else{
					field.setAccessible(true);
					field.set(obj, value);
					field.setAccessible(false);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
