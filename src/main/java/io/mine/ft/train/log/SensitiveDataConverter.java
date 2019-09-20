package io.mine.ft.train.log;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Marker;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;

import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggerContextVO;
import io.mine.ft.train.util.JsonUtil;
import io.mine.ft.train.util.SensitiveInfoUtil;
import io.mine.ft.train.util.TuoMinMarker;

/**
 * Title: SensitiveDataConverter.java
 * 
 * @author machao
 * @date 2019年7月24日
 * @version 1.0
 */
public class SensitiveDataConverter extends MessageConverter {
	// https://blog.csdn.net/qq_36838191/article/details/88179694

	/**
	 * 日志脱敏关键字
	 */
	// private static String sensitiveDataKeys = "idcard,realname,bankcard,mobile";
	/**
	 * 正则表达式
	 */
	private static Pattern pattern = Pattern.compile("[0-9a-zA-Z]");

	@Override
	public String convert(ILoggingEvent event) {
		// 获取原始日志
		String originLogMsg = event.getFormattedMessage();
		LoggerContextVO contextVO = event.getLoggerContextVO();
		Map<String, String> map = contextVO.getPropertyMap();
		String tuoMinSwitch = map.get("tuoMinSwitch");
		
		if (!"1".equals(tuoMinSwitch)) {
			return originLogMsg;
		}

		Map<String, String> tuoMinItemsMap = null;
		Marker marker = event.getMarker();
		if (marker == null) {
			String tuoMinItemsStr = map.get("tuoMinItems");
			if (StringUtils.isEmpty(tuoMinItemsStr)) {
				return originLogMsg;
			}
			tuoMinItemsMap = jsonToTuoMinItemsMap(tuoMinItemsStr);
		} else {
			tuoMinItemsMap = jsonToTuoMinItemsMap(marker.getName());
		}
		//如果脱敏项map为空返回
		if (CollectionUtils.isEmpty(tuoMinItemsMap)) {
			return originLogMsg;
		}
		// 获取脱敏后的日志
		return invokeMsg(originLogMsg, tuoMinItemsMap);
	}
	
	private static Map<String, String> jsonToTuoMinItemsMap(String json) {
		if (!JsonUtil.isValidJson(json)) {
			return null;
		}
		Map<String, String> tuoMinItemsMap = 
				JsonUtil.json2GenericObject(json, new TypeReference<Map<String, String>>() {});
		return tuoMinItemsMap;
	}

	/**
	 * 处理日志字符串，返回脱敏后的字符串
	 */
	private String invokeMsg(final String originLogMsg, final Map<String, String> tuoMinItemsMap) {

		String tempMsg = originLogMsg;

		for (String key : tuoMinItemsMap.keySet()) {
			int index = -1;
			do {
				index = tempMsg.indexOf(key, index + 1);
				// 判断key是否为单词字符
				if (index == -1) {// 结束while循环
					break;
				}
				if (isWordChar(tempMsg, key, index)) {// 跳出本次while循环，寻找下一个
					continue;
				}
				// 寻找值的开始位置
				int valueStart = getValueStartIndex(tempMsg, index + key.length());
				// 查找值的结束位置（逗号，分号）........................
				int valueEnd = getValuEndEIndex(tempMsg, valueStart);
				// 对获取的值进行脱敏
				String subStr = tempMsg.substring(valueStart, valueEnd);
				subStr = tuomin(subStr, tuoMinItemsMap.get(key));
				tempMsg = tempMsg.substring(0, valueStart) + subStr + tempMsg.substring(valueEnd);
			} while (index != -1);
		}
		return tempMsg;
	}

	private String tuomin(String submsg, String type) {
		// idcard：身份证号, realname：姓名, bankcard：银行卡号, mobile：手机号
		if (TuoMinMarker.idCardNum.equals(type)) {
			return SensitiveInfoUtil.idCardNum(submsg);
		}
		if (TuoMinMarker.chineseName.equals(type)) {
			return SensitiveInfoUtil.chineseName(submsg);
		}
		if (TuoMinMarker.bankCard.equals(type)) {
			return SensitiveInfoUtil.bankCard(submsg);
		}
		if (TuoMinMarker.mobilePhone.equals(type)) {
			return SensitiveInfoUtil.mobilePhone(submsg);
		}
		return "";
	}

	private boolean isWordChar(String msg, String key, int index) {
		// 必须确定key是一个单词............................
		if (index != 0) {
			// 判断key前面一个字符
			char preCh = msg.charAt(index - 1);
			Matcher match = pattern.matcher(preCh + "");
			if (match.matches()) {
				return true;
			}
		}
		// 判断key后面一个字符
		char nextCh = msg.charAt(index + key.length());
		Matcher match = pattern.matcher(nextCh + "");
		if (match.matches()) {
			return true;
		}
		return false;
	}

	/**
	 * 获取value值的开始位置
	 * 
	 * @param msg        要查找的字符串
	 * @param valueStart 查找的开始位置
	 * @return
	 */
	private int getValueStartIndex(String msg, int valueStart) {
		// 寻找值的开始位置.................................
		do {
			char ch = msg.charAt(valueStart);
			if (ch == ':' || ch == '=') { // key与 value的分隔符
				valueStart++;
				ch = msg.charAt(valueStart);
				if (ch == '"') {
					valueStart++;
				}
				break; // 找到值的开始位置
			} else {
				valueStart++;
			}
		} while (true);
		return valueStart;
	}

	/**
	 * 获取value值的结束位置
	 * 
	 * @return
	 */
	private int getValuEndEIndex(String msg, int valueEnd) {
		do {
			if (valueEnd == msg.length()) {
				break;
			}
			char ch = msg.charAt(valueEnd);

			if (ch == '"') { // 引号时，判断下一个值是结束，分号还是逗号决定是否为值的结束
				if (valueEnd + 1 == msg.length()) {
					break;
				}
				char nextCh = msg.charAt(valueEnd + 1);
				if (nextCh == ';' || nextCh == ',') {
					// 去掉前面的 \ 处理这种形式的数据
					while (valueEnd > 0) {
						char preCh = msg.charAt(valueEnd - 1);
						if (preCh != '\\') {
							break;
						}
						valueEnd--;
					}
					break;
				} else {
					valueEnd++;
				}
			} else if (ch == ';' || ch == ',' || ch == '}') {
				break;
			} else {
				valueEnd++;
			}

		} while (true);
		return valueEnd;
	}
}
