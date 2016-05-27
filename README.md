# qdf
quick development framework

一.使用说明

1.新建Java Web项目,导入qdf_1.0.0.jar

2.web.xml配置qdf过滤器
	
		<filter>  
			<filter-name>qdfFilter</filter-name>  
			<filter-class>com.qdf.core.QdfFilter</filter-class>  
		</filter>  
		<filter-mapping>  
			<filter-name>qdfFilter</filter-name>  
			<url-pattern>/*</url-pattern> 
			<!-以下配置实现forward可以跳转到Action-->
			<dispatcher>REQUEST</dispatcher>
			<dispatcher>FORWARD</dispatcher>
		</filter-mapping> 
		
3.qdfConfig.properties配置
		
		#框架编码
		#不设置默认为utf-8
		qdf.encoding=utf-8
		
		#jdbc
		db.url=jdbc:mysql://127.0.0.1:3306/qdf
		db.username=root
		db.password=root
		db.maxActive=300
		
		#过滤器忽略的url后缀
		qdf.ignoreUrl=html|css|js|json|ico|png|gif|woff|map|txt
		#自动扫描的action包名
		qdf.scan.action=com.test.action
		#自动扫描的model包名
		qdf.scan.model=com.test.model
		
		#全局拦截器
		qdf.global.interceptors=com.test.interceptor.GlobalIntercep,com.test.interceptor.GlobalIntercep2
		
		#注册插件 
		qdf.plugins=com.qdf.plugin.ehcache.EhcachePlugin
		
		#声明式事务
		#添加事务的方法名正则
		qdf.tx.method=save.*|update.*
		#事务级别
		qdf.tx.level=2
	
4.如果使用日志,则需配log4j.properties

5.如果使用Ehcache,则需配置ehcache.xml