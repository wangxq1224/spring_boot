package com.demo.sboot.page;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @ClassName: DataMap 
 *
 * @Description: 自定义map，用于数据传递
 *
 * @author wangxq 
 * @date 2017年7月14日 下午3:09:52 
 *
 */
@SuppressWarnings("all")
public class DataMap extends HashMap{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6226295683762041864L;
	
	Map map = null;

	/**
	 * 普通构造函数
	 */
	public DataMap() {
		map = new HashMap();
	}
	
	/**
	 * 处理http请求数据，将请求参数存入map中
	 * @param request
	 */
	public DataMap(HttpServletRequest request) {
		Map<String, Object> reqMap = new HashMap<String, Object>();
		Map params = request.getParameterMap();
		if (params.size() > 0) {
			Iterator<Map.Entry<String, Object>> iterator = params.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, Object> entry=iterator.next();
				reqMap.put(entry.getKey(),entry.getValue()==null?"":entry.getValue().toString());
			}
		}
		map=reqMap;
	}
	
	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	@Override
	public Set entrySet() {
		return map.entrySet();
	}

	@Override
	public Object get(Object key) {
		return map.get(key);
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public Set keySet() {
		return map.entrySet();
	}

	@Override
	public Object put(Object key, Object value) {
		return map.put(key, value);
	}

	@Override
	public Object remove(Object key) {
		return map.remove(key);
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public Collection values() {
		return map.values();
	}
	
	public String getString(Object key){
		Object value=get(key);
		if(value==null){
			return "";
		}
		return (String)value;
	}

}
