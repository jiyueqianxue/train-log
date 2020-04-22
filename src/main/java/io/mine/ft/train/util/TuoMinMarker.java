package io.mine.ft.train.util;

import java.util.Iterator;
import java.util.Map;

import org.slf4j.Marker;

/**  
 * <p>Title: TuoMinMarker.java</p>  
 * @author machao  
 * @date 2019年7月26日  
 * @version 1.0  
*/
public class TuoMinMarker implements Marker {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public final static String idCardNum = "idCardNum";
	
	public final static String chineseName = "chineseName";
	
	public final static String bankCard = "bankCard";
	
	public final static String mobilePhone = "mobilePhone";
	
	private Map<String, String> tuoMinItemsMap;
	
	private TuoMinMarker(Map<String, String> tuoMinItemsMap) {
		this.tuoMinItemsMap = tuoMinItemsMap;
	}
	
	public static TuoMinMarker create(Map<String, String> tuoMinItemsMap) {
		return new TuoMinMarker(tuoMinItemsMap);
	}

	@Override
	public String getName() {
		return JsonUtil.toJson(tuoMinItemsMap);
	}

	@Override
	public void add(Marker reference) {
		
	}

	@Override
	public boolean remove(Marker reference) {
		return false;
	}

	@Override
	public boolean hasChildren() {
		return false;
	}

	@Override
	public boolean hasReferences() {
		return false;
	}

	@Override
	public Iterator<Marker> iterator() {
		return null;
	}

	@Override
	public boolean contains(Marker other) {
		return false;
	}

	@Override
	public boolean contains(String name) {
		if (this.getName().equals(name)) {
			return true;
		}
		return false;
	}
	
}
