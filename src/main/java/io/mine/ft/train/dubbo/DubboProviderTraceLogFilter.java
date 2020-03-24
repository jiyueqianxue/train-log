package io.mine.ft.train.dubbo;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;

import io.mine.ft.train.log.LogBizSeqNoContext;
import io.mine.ft.train.log.LogSysSeqNoContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author machao
 */
@Activate(group = { Constants.PROVIDER })
@Slf4j
public class DubboProviderTraceLogFilter implements Filter {

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

		try {
			RpcContext context = RpcContext.getContext();
			String bizSeqStr = context.getAttachment(TraceConstants.BIZ_SEQ_ID);
			String sysSeqStr = context.getAttachment(TraceConstants.SYS_SEQ_ID);
			log.info("DubboConsumerTraceLogFilter BIZ_SEQ_ID:{}, SYS_SEQ_ID:{}", bizSeqStr, sysSeqStr);
			
			if (StringUtils.isNotBlank(bizSeqStr) || StringUtils.isNotBlank(sysSeqStr) ) {
				LogBizSeqNoContext.setContext(bizSeqStr);
				LogSysSeqNoContext.setContext(sysSeqStr);
			} else {
				log.info("DubboConsumerTraceLogFilter init bizSeqStr, sysSeqStr");
				LogBizSeqNoContext.initCurrentContext();
				LogSysSeqNoContext.initCurrentContext();
				log.info("DubboConsumerTraceLogFilter init end ");
			}
			return invoker.invoke(invocation);
		} finally {
			LogBizSeqNoContext.removeCurrentContext();
			LogSysSeqNoContext.removeCurrentContext();
		}
	}
}
