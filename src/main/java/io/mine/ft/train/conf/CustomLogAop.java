package io.mine.ft.train.conf;

import java.lang.reflect.Field;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.mine.ft.train.log.LogSysSeqNoContext;
import io.mine.ft.train.log.LogBizSeqNoContext;

/**  
 * <p>Title: CustomLogAop.java</p>  
 * <p>Description: </p>  
 * @author machao  
 * @date 2019年7月3日  
 * @version 1.0  
*/
@Aspect
public class CustomLogAop {
	private static final Logger logger = LoggerFactory.getLogger(CustomLogAop.class);
	
	@Around(value = "@annotation(customLog)")
	public Object logProcess(ProceedingJoinPoint pjp, CustomLog customLog) {
		
		logger.info("【CustomLogAop logProcess start】");
		
		if (customLog != null && customLog.value()) {
			setRequestFlowNo(pjp.getArgs());
		}
		
		Object proceed = null;
		try {
			proceed = pjp.proceed();
		} catch (Throwable e) {
			logger.error("【CustomLogAop logProcess error】", e);
		}
		return proceed;
		
	}
	
	private void setRequestFlowNo(Object[] args) {
		for (Object obj : args) {
			Class<? extends Object> clazz = obj.getClass().getSuperclass();
			// Field field = class1.getField("");
			Field bizSeqField = null;
			Field sysSeqField = null;

			// field = clazz.getField("bizSeq");
			try {
				bizSeqField = clazz.getDeclaredField("bizSeq");
				sysSeqField = clazz.getDeclaredField("sysSeq");
			} catch (Exception e1) {
				logger.error("【CustomLogAop setRequestFlowNo不存在流水号】~~ ");
			}
			
			try {
				if (bizSeqField != null && sysSeqField != null) {
					bizSeqField.setAccessible(true);
					Object bizSeqStr = bizSeqField.get(obj);
					LogBizSeqNoContext.setContext(bizSeqStr.toString());
				
					sysSeqField.setAccessible(true);
					Object sysSeqStr = sysSeqField.get(obj);
					LogSysSeqNoContext.setContext(sysSeqStr.toString());
					break;
				}
			} catch (Exception e) {
				logger.error("【CustomLogAop setRequestFlowNo无法获取流水号 】~~");
			}
		}
	}
}
