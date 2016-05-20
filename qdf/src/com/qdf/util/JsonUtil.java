package com.qdf.util;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
/**
 * json帮助类
 * 基于阿里巴巴fastJson实现
 * @author xiezq
 *
 */
public class JsonUtil {

	private static final String KEY_CODE = "code";
	private static final String KEY_DATA = "data";
	private static final String KEY_MSG = "msg";


	public static String toJsonCDM(int code,Object data,String msg) {
		Map<String, Object> map = new HashMap<>();
		map.put(KEY_CODE, code);
		map.put(KEY_DATA, data);
		map.put(KEY_MSG, msg);
		return JSONObject.toJSONString(map);
	}
	
	public static String toJsonString(Object data) {
		return JSONObject.toJSONString(data);
	}
}
