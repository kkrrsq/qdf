package com.qdf.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import com.qdf.annotation.Model;
import com.qdf.util.JsonUtil;
import com.qdf.util.LogUtil;
import com.qdf.util.StringUtil;

public class Table {

	private String modelPackage;
	
	private Map<Class<?>, String> tableMap = new HashMap<>();
	private Map<Class<?>, String> pkMap = new HashMap<>();
	
	public Map<Class<?>, String> getTableMap() {
		return tableMap;
	}
	
	public Map<Class<?>, String> getPkMap() {
		return pkMap;
	}
	
	public void scanModel() {
		try{
			ImmutableSet<ClassInfo> classInfos = ClassPath.from(Thread.currentThread().getContextClassLoader()).getTopLevelClasses(modelPackage);
			for (ClassInfo classInfo : classInfos) {
				Class<?> clazz = classInfo.load();
				//符合条件的Action
				Model model = clazz.getAnnotation(Model.class);
				if(null != model) {
					String table = model.table();
					if(Strings.isNullOrEmpty(table))
						table = StringUtil.firstCharToLowerCase(clazz.getSimpleName());
					tableMap.put(clazz, table);
					String pk = model.pk();
					if(Strings.isNullOrEmpty(pk)) 
						pk = "id";
					pkMap.put(clazz, pk);
				}
			}
			LogUtil.info("添加表:"+JsonUtil.toJsonString(tableMap));
		}catch( IOException e ){
			throw new RuntimeException( e );
		}
	}

	public String getModelPackage() {
		return modelPackage;
	}

	public void setModelPackage(String modelPackage) {
		this.modelPackage = modelPackage;
	}
	
}
