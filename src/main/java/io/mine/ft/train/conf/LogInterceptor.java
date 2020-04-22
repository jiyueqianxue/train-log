package io.mine.ft.train.conf;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**  
 * <p>Title: LogInterceptor.java</p>  
 * @author machao  
 * @date 2019年7月3日  
 * @version 1.0  
*/
public class LogInterceptor implements MethodInterceptor {
	
	private final static Logger logger = LoggerFactory.getLogger(LogInterceptor.class);

	@Override
	public Object invoke(MethodInvocation mi) throws Throwable {
	

		//boolean flag = true;
		// 判断该方法是否加了@LittleLogin 注解
		if (mi.getMethod().isAnnotationPresent(CustomLog.class)) {
			// TODO
			logger.info("~待定 TODO ~");
		}
		
		return null;
	}
	
}
