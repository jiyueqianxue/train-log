package io.mine.ft.train.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

import lombok.extern.slf4j.Slf4j;

/**
 * Json转换工具类
 * 
 * Created by machao on 2016-04-05
 */

@Slf4j
public final class JsonUtil {

	private static final ObjectMapper OBJECT_MAPPER;

	static {
		OBJECT_MAPPER = new ObjectMapper();
		// OBJECT_MAPPER.getSerializationConfig().withSerializationInclusion(JsonInclude.Include.NON_NULL);
		// OBJECT_MAPPER.getSerializationConfig().withPropertyInclusion(JsonInclude.Value.empty());

		OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		// 空值处理为空串
		OBJECT_MAPPER.getSerializerProvider().setNullKeySerializer(new JsonSerializer<Object>() {
			@Override
			public void serialize(Object paramT, JsonGenerator paramJsonGenerator,
					SerializerProvider paramSerializerProvider) throws IOException, JsonProcessingException {
				paramJsonGenerator.writeString("");
			}
		});
		// 默认格式化时间格式
		OBJECT_MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		// allow the private and package private fields to be detected
		OBJECT_MAPPER.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
	}

	private JsonUtil() {

	}

	public static ObjectMapper getObjectMapper() {
		return OBJECT_MAPPER;
	}

	/**
	 * JSON串转换为Java泛型对象，可以是各种类型
	 * 
	 * @param            <T> return type
	 * @param jsonString JSON
	 * @param tr         TypeReference,例如: new TypeReference< List<FamousUser> >(){}
	 * @return List对象列表
	 */
	@SuppressWarnings("unchecked")
	public static <T> T json2GenericObject(String jsonString, TypeReference<T> tr) {
		if (!StringUtils.isBlank(jsonString)) {
			try {
				return (T) OBJECT_MAPPER.readValue(jsonString, tr);
			} catch (Exception e) {
				log.warn("json error:" + e.getMessage());
			}
		}
		return null;
	}

	/**
	 * Java对象转JSON字符串
	 * 
	 * @param object Java对象，可以是对象，数组，List,Map等
	 * @return JSON 字符串
	 */
	public static String toJson(Object object) {
		try {
			if (null == object) {
				return "";
			} else {
				return OBJECT_MAPPER.writeValueAsString(object);
			}
		} catch (Exception e) {
			log.warn("json error:" + e.getMessage());
		}
		return "";
	}

	/**
	 * JSON字符串转Java对象
	 * 
	 * @param jsonString JSON
	 * @param c          class
	 * @return
	 */
	public static Object json2Object(String jsonString, Class<?> c) {
		if (!StringUtils.isBlank(jsonString)) {
			try {
				return OBJECT_MAPPER.readValue(jsonString, c);
			} catch (Exception e) {
				log.warn("json error:" + e.getMessage());
			}
		}
		return null;
	}

	/**
	 * 验证字符串是否为json格式
	 * 
	 * @param json
	 * @return
	 */
	public static boolean isValidJson(final String json) {
		boolean valid = false;
		try {
			final JsonParser parser = new ObjectMapper().getFactory().createParser(json);
			while (parser.nextToken() != null) {
			}
			valid = true;
		} catch (JsonParseException jpe) {
			return valid;
		} catch (IOException ioe) {
			return valid;
		}
		return valid;
	}
	/**
	 * 比较json相等
	 *  machao
	 * @param json
	 * @return
	 */
	public static boolean compareJsonStr(String st1, String st2) {
		
		if (StringUtils.isEmpty(st1) || StringUtils.isEmpty(st2)) {
			return false;
		}
		
		if (st1.equals(st2)) {
			return true;
		} else {
			JSONObject jsonObject1 = JSONObject.parseObject(st1);
			JSONObject jsonObject2 = JSONObject.parseObject(st2);
			return compareJson(jsonObject1, jsonObject2, null);
		}

	}

	public static boolean compareJson(JSONObject json1, JSONObject json2, String originkey) {
		
		String key = originkey;
		if (json1 == json2) {
			return true;
		}
		Iterator<String> i = json1.keySet().iterator();
		while (i.hasNext()) {
			key = i.next();
			if (json2.get(key) == null) { //json2可能没这个key 或者值为null
				if (json1.get(key) == null) {
					if (json2.containsKey(key)) {//json2有这个key
						continue;
					} else {//json2没有这个key
						return false;
					}
				} else { //json1 的key-value不为空
					return false;
				}
			}
			if (!compareJson(json1.get(key), json2.get(key), key)) {
				return false;
			}
		}
		return true;
	}

	private static boolean compareJson(Object json1, Object json2, String key) {
		if (json1 instanceof JSONObject) {
			if (!(json2 instanceof JSONObject)) {
				return false;
			}
			//System.out.println("this JSONObject----" + key);
			return compareJson((JSONObject) json1, (JSONObject) json2, key);
		} else if (json1 instanceof JSONArray) {
			if (!(json2 instanceof JSONArray)) {
				return false;
			}
			//System.out.println("this JSONArray----" + key);
			return compareJson((JSONArray) json1, (JSONArray) json2, key);
		} else if (json1 instanceof String) {
			if (!(json2 instanceof String)) {
				return false;
			}
	        //System.out.println("this String----" + key);
	        //compareJson((String) json1, (String) json2, key);
			String json1ToStr = json1.toString();
			String json2ToStr = json2.toString();
			return compareJson(json1ToStr, json2ToStr, key);

		} else {
	        //System.out.println("this other----" + key);
			if (json1 == json2) {
				return true;
			}
			if (json1 != null && json2 != null) {
				return compareJson(json1.toString(), json2.toString(), key);
			} 
			return false;
		}
		// return false;
	}

	private static boolean compareJson(String str1, String str2, String key) {

		if (!str1.equals(str2)) {
			// sb.append("key:"+key+ ",json1:"+ str1 +",json2:"+str2);
			log.info("不一致key:" + key + ",json1:" + str1 + ",json2:" + str2);
			return false;
		} else {
			log.info("一致：key:" + key + ",json1:" + str1 + ",json2:" + str2);
			return true;
		}
	}

	private static boolean compareJson(JSONArray json1, JSONArray json2, String key) {

		if (json1 == json2) {
			return true;
		} else if (json1 != null && json2 != null) {
			Iterator<?> i1 = json1.iterator();
			Iterator<?> i2 = json2.iterator();
			while (i1.hasNext()) {
				if (!compareJson(i1.next(), i2.next(), key)) {
					return false;
				}
			}
			return true;
		} else {
			log.info("不一致：key:" + key + " 有一个为null");
			return false;
		}
	}

//	public static void main(String[] args) {
//
//		String st1 = "{\"hasNotSettleOutTu\":2,\"hasOtherLoan\":2,\"hasOtherRepayPlan\":2,\"hasSpecialRelationship\":2,\"loanPurpose\":1,\"notSettleOutTu\": [],\"otherLoanInfo\":{},\"otherRepayPlan\":[],\"relevantInfo\":{}}";
//		//String st2 = "{\"loanPurpose\":null,\"otherPurpose\":\"做生意\",\"hasAgent\":2,\"hasOtherLoan\":2,\"otherLoanInfo2\":null,\"hasNotSettleOutTu\":2,\"notSettleOutTu\":null,\"hasOtherRepayPlan\":2,\"otherRepayPlan\":null,\"hasSpecialRelationship\":2,\"relevantInfo\":null}";
//		String st2 = "{\"hasNotSettleOutTu\":2,\"hasOtherLoan\":2,\"hasOtherRepayPlan\":2,\"hasSpecialRelationship\":2,\"loanPurpose\":1,\"notSettleOutTu\": [],\"otherLoanInfo\":[],\"otherRepayPlan\":[],\"relevantInfo\":{}}";
//		//System.out.println(st1.equals(st2));
//
//		boolean json = compareJsonStr(st1, st2);
//		System.out.println(json);
//	}

}
