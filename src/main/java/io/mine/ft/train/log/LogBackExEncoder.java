package io.mine.ft.train.log;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;

import java.io.IOException;
import java.util.Map;

/**
 * Created by wangjinfeng on 2016/7/27.
 */
public class LogBackExEncoder extends PatternLayoutEncoder {

    static {
    	Map<String, String> defaultconvertermap = PatternLayout.defaultConverterMap;
    	defaultconvertermap.put("T", LogBizSeqNoConverter.class.getName());
    	defaultconvertermap.put("X", LogSysSeqNoConverter.class.getName());
    	//defaultconvertermap.put("msg", SensitiveDataConverter.class.getName());
    }

    @Override
    public void doEncode(ILoggingEvent event) throws IOException {
        super.doEncode(event);
    }
}
