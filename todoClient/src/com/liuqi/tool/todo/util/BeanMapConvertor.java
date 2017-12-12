/**
 * 文   件  名：BeanMapConvertor.java
 * 作          者：刘奇
 * 创建日期：2014年8月2日
 * 版          本：1.0
 */
package com.liuqi.tool.todo.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @作者： 刘奇
 * @时间：2014年8月2日
 *
 */
public class BeanMapConvertor {
	public static List<Map<String, String>> toMap(List<Object> list) {
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		
		for (Object obj: list) {
			result.add(toMap(obj));
		}
		
		return result;
	}
	
	/**
	 * 对象转换成Map
	 * 
	 * @param obj
	 * @return
	 */
	public static Map<String, String> toMap(Object obj) {
		Map<String, String> map = new HashMap<String, String>();
		
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
			PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
			
			for (PropertyDescriptor pd: pds) {
				String name = pd.getName();
				
				if ("class".equals(name)) {
					continue;
				}
				
				Method method = pd.getReadMethod();
				Object value = method.invoke(obj);
				
				if (null != value) {
					map.put(name, value.toString());
				}
			}
			
		} catch (IntrospectionException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return map;
	}
	
	public static <T> List<T> toBean(List<Map<String, String>> list, Class<T> c) {
		if (null == list) {
			return null;
		}
		
		List<T> result = new ArrayList<T>();
		
		for (Map<String, String> map: list) {
			result.add(toBean(map, c));
		}
		
		return result;
	}
	
	/**
	 * Map转换成对象
	 * 
	 * @param map
	 * @param c
	 * @return
	 */
	public static <T> T toBean(Map<String, String> map, Class<T> c) {
		T t = null;
		
		try {
			t = c.newInstance();
			BeanInfo beanInfo = Introspector.getBeanInfo(c);
			
			PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor pd: pds) {
				String name = pd.getName();
				if ("class".equals(name)) {
					continue;
				}
				
				Method method = pd.getWriteMethod();
				if (null == method) {
					continue;
				}
				
				Object value = map.get(name);

				Class<?>[] types = method.getParameterTypes(); 
				if (1 != types.length) {
					continue;  
				}
				
				Class<?> type = types[0];  
				if (Integer.TYPE.equals(type)) {
					method.invoke(t, Integer.valueOf(value.toString()));
				} else {
					method.invoke(t, value);
				}
			}
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IntrospectionException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return t;
	}
}
