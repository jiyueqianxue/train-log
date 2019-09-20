package io.mine.ft.train.maintest;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.LoggerContextVO;
import io.mine.ft.train.util.TuoMinMarker;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogMarkerTest {
	public static void main(String[] args) {
		
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		Logger logger = loggerContext.getLogger("root");
		logger.setLevel(Level.valueOf("INFO"));
		LoggerContextVO contextVO = loggerContext.getLoggerContextRemoteView();
		Map<String, String> propertyMap = contextVO.getPropertyMap();
		propertyMap.put("tuoMinSwitch", "1");

		String tempMsg = "{sign=f88898b2677e62f1ad54b9e330c0a27e, idcard=130333198901192762, realname=%E5%BE%90%E5%BD%A6%E5%A8%9C, key=c5d34d4c3c71cc45c88f32b4f13da887, mobile=13210141605, bankcard=6226430106137525}";
		String tempMsg1 = "{\"reason\":\"成功 \",\"result\":{\"jobid\":\"JH2131171027170837443588J6\",\"realname\":\"李哪娜\",\"bankcard\":\"6226430106137525\",\"idcard\":\"130333198901192762\",\"mobile\":\"13210141605\",\"res\":\"1\",\"message\":\"验证成功\"},\"error_code\":0}";
		
		tempMsg1 = "(\"reason\":\"成功\")";
		
		Map<String, String> map = new HashMap<>();
		map.put("mobile", TuoMinMarker.mobilePhone);

		Map<String, String> map2 = new HashMap<>();
		map2.put("realname", TuoMinMarker.chineseName);

//		TuoMinMarker marker = TuoMinMarker.create(map);
//		System.out.println("the hashCode of marker~~ " + marker.hashCode());

//		log.info(marker, "tempMsg~~:{}", tempMsg);
		log.info(TuoMinMarker.create(map2), "tempMsg1~~:{}", tempMsg1);
		log.info("原始tempMsg~~:{}", tempMsg);
	}
}
