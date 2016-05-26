package com.qdf.core;

import java.lang.reflect.Method;
import java.util.Map;

import com.qdf.util.StringUtil;

/**
 * javaBean注入器
 * 
 * @author xiezq
 *
 */
public class Injector {

	/**
	 * 将前端传入的参数注入到bean
	 * @param <T>
	 * @param clazz
	 * @param paramMap
	 * @return javaBean
	 */
	public static <T> T injectBean(Class<T> clazz, Map<String, String> paramMap) {

		Object object = null;
		try {

			object = clazz.newInstance();

			Method[] methods = clazz.getMethods();
			for (Method method : methods) {
				// 选择符合要求的setXXX方法
				if (method.getName().startsWith("set") && method.getParameters().length == 1
						&& method.getName().length() > 3) {
					String key = StringUtil.firstCharToLowerCase(method.getName().substring(3));
					if (paramMap.containsKey(key)) {
						Class<?> paramType = method.getParameterTypes()[0];
						method.invoke(object, TypeConverter.convert(paramType, paramMap.get(key)));
					}
				}
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return (T)object;
	}
}
