package io.mine.ft.train.log;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * Created by wangjinfeng on 2016/7/27.
 */
public class LogSysSeqNoConverter extends ClassicConverter {

    /**
     * 依据当前线程号获取UUID
     *
     * @param loggingEvent
     * @return
     */
    @Override
    public String convert(ILoggingEvent loggingEvent) {
        return LogSysSeqNoContext.getCurrentContext();
    }
}
