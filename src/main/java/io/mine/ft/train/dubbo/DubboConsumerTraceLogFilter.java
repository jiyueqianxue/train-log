package io.mine.ft.train.dubbo;

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
 * Created by za-machao on 2018/7/5.
 */
@Activate(group = {Constants.CONSUMER})
@Slf4j
public class DubboConsumerTraceLogFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

        try {
        	String bizSeqStr = LogBizSeqNoContext.getCurrentContext();
        	String sysSeqStr = LogSysSeqNoContext.getCurrentContext();
        	RpcContext context = RpcContext.getContext();
        	context.setAttachment(TraceConstants.BIZ_SEQ_ID, bizSeqStr);
        	context.setAttachment(TraceConstants.SYS_SEQ_ID, sysSeqStr);
           
        	log.info("DubboConsumerTraceLogFilter BIZ_SEQ_ID:{}, SYS_SEQ_ID:{}", bizSeqStr, sysSeqStr);
        	return invoker.invoke(invocation);
        } finally {
        	
        }
    }
}
