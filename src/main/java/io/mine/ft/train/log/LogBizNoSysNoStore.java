package io.mine.ft.train.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**  
 * <p>Title: LogBizSysNoStore.java</p>  
 * @author machao  
 * @date 2019年7月10日  
 * @version 1.0  
*/
public class LogBizNoSysNoStore {
	
	private static final Logger logger = LoggerFactory.getLogger(LogBizNoSysNoStore.class);
	
	public static void setBizSysNo(String bizSeqNo, String sysSeqNo) {
		
		logger.info("【LogBizSysNoStore 存放流水号】bizSeqNo:{}, sysSeqNo：{}", bizSeqNo, sysSeqNo);
		LogBizSeqNoContext.setContext(bizSeqNo);
		LogSysSeqNoContext.setContext(sysSeqNo);
	} 
	
	public static String getBizReqNo() {
		return LogBizSeqNoContext.getCurrentContext();
	} 
	
	public static String getSysReqNo() {
		return LogSysSeqNoContext.getCurrentContext();
	} 
	
}
