package io.mine.ft.train.log;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * Created by wangjinfeng on 2016/8/5.
 */
public class LogBizSeqNoContext {

    private static final Logger logger = LoggerFactory.getLogger(LogBizSeqNoContext.class);

    private static final InheritableThreadLocal<String> THREAD_LOCAL_CONTEXT = new InheritableThreadLocal<String>();

    /**
     * get local thread context
     *
     * @return
     */
    public static String getCurrentContext() {

        try {
            String context = THREAD_LOCAL_CONTEXT.get();
            if (StringUtils.isBlank(context)) {
                context = UUID.randomUUID().toString().replace("-", "");
                THREAD_LOCAL_CONTEXT.set(context);
            }
            return context;
        } catch (Exception e) {
            logger.error("==>getCurrentContext ERROR, errorMsg:{}", e.getMessage(), e);
            return UUID.randomUUID().toString();
        }
    }

    /**
     * init thread local context
     */
    public static void initCurrentContext() {

        try {
            String context;
            if (StringUtils.isNotBlank(THREAD_LOCAL_CONTEXT.get())) {

                removeCurrentContext();
            }
            context = UUID.randomUUID().toString().replace("-", "");
            THREAD_LOCAL_CONTEXT.set(context);
        } catch (Exception e) {
            logger.error("==>initCurrentContext ERROR, errorMsg:{}", e.getMessage(), e);
        }
    }

    /**
     * remove thread local context
     */
    public static void removeCurrentContext() {
        String context = THREAD_LOCAL_CONTEXT.get();

        try {
            if (StringUtils.isNotBlank(context)) {

                THREAD_LOCAL_CONTEXT.remove();
            }
        } catch (Exception e) {
            logger.error("==>removeCurrentContext ERROR, errorMsg:{}", e.getMessage(), e);
        }
    }

    public static void recoverCurrentContext(String context) {
        try {

            if (StringUtils.isNotBlank(THREAD_LOCAL_CONTEXT.get())) {

                removeCurrentContext();
            }

            THREAD_LOCAL_CONTEXT.set(context);

        } catch (Exception e) {
            logger.error("==>recoverCurrentContext ERROR, errorMsg:{}", e.getMessage(), e);
        }
    }

    public static void setContext(String context){
        try {
            THREAD_LOCAL_CONTEXT.set(context);
        } catch (Exception e) {
            logger.error("==>setContext ERROR, errorMsg:{}", e.getMessage(), e);
        }
    }
}
