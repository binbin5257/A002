package cn.lds.chatcore.common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * JSON 帮助类
 * 
 * @author user
 * 
 */
public class JsonHelper {

	/**
	 * 解析简单JSON.解析错误返回null;
	 * 
	 * @param strJson
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> parseSimpleJson(String strJson) {
		JSONObject jsonObject;
		try {
			System.out.println("--->> JSON:" + strJson);
			jsonObject = new JSONObject(strJson);
			Iterator<String> keyIter = jsonObject.keys();
			String key;
			Object value;
			Map<String, Object> valueMap = new HashMap<String, Object>();
			while (keyIter.hasNext()) {
				key = keyIter.next();
				value = jsonObject.get(key);
				valueMap.put(key, value);
			}
			return valueMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解析数组JSON
	 * 
	 * @param jsonStr
	 * @return
	 */
	public static List<Map<String, Object>> parseListJson(String jsonStr) {
		System.out.println("--->> JSON:" + jsonStr);
		List<Map<String, Object>> list = null;
		if (!checkListJson(jsonStr)) {
			return null;
		}
		try {
			JSONArray jsonArray = new JSONArray(jsonStr);
			JSONObject jsonObj;
			list = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < jsonArray.length(); i++) {
				jsonObj = (JSONObject) jsonArray.get(i);
				list.add(parseSimpleJson(jsonObj.toString()));
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Map<String, Object>>();
	}

	/**
	 * 
	 * @param jsonStr
	 * @return
	 */
	public static boolean checkListJson(String jsonStr) {
		boolean status = true;
		try {
			String key = jsonStr.substring(0, 1);
			if ("{".equals(key)) {
				JSONObject object = new JSONObject(jsonStr);
				status = object.getBoolean("status");
			} else if ("[".equals(key)) {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	/**
	 * 从JSON对象中获取值（String）
	 * @param json
	 * @param key
	 * @return
	 */
	public static String getString(JSONObject json,String key){
		try {
			if(!json.has(key) || json.isNull(key))
				return null;
			return json.getString(key);
		} catch (JSONException e) {
			LogHelper.e("JsonHelper::getString", e);
			return null;
		}
	}

	/**
	 * 从JSON对象中获取值（Long）
	 * @param json
	 * @param key
	 * @return
	 */
	public static Long getLong(JSONObject json,String key){
		try {
			if(!json.has(key) || json.isNull(key))
				return null;
			return json.getLong(key);
		} catch (JSONException e) {
			LogHelper.e("JsonHelper::getLong",e);
			return null;
		}
	}

	/**
	 * 从JSON对象中获取值（Boolean）
	 * @param json
	 * @param key
	 * @return
	 */
	public static boolean getBool(JSONObject json,String key){
		try {
			if(!json.has(key) || json.isNull(key))
				return false;
			return json.getBoolean(key);
		} catch (JSONException e) {
			LogHelper.e("JsonHelper::getLong",e);
			return false;
		}
	}

	/**
	 * 从JSON对象中获取值（Double）
	 * @param json
	 * @param key
	 * @return
	 */
	public static Double getDouble(JSONObject json,String key){
		try {
			return json.isNull(key)?null:json.getDouble(key);
		} catch (JSONException e) {
			LogHelper.e("JsonHelper::getDouble",e);
			return null;
		}
	}
	/**
	 * 从JSON对象中获取值（Integer）
	 * @param json
	 * @param key
	 * @return
	 */
	public static Integer getInt(JSONObject json,String key){
		try {
			return json.isNull(key)?null:json.getInt(key);
		} catch (JSONException e) {
			LogHelper.e("JsonHelper::getInt",e);
			return null;
		}
	}

	/**
	 * 从JSON对象中获取值（JSONObject）
	 * @param json
	 * @param key
	 * @return
	 */
	public static JSONObject getJSONObject(JSONObject json,String key){
		try {
			return json.isNull(key)?null:json.getJSONObject(key);
		} catch (JSONException e) {
			LogHelper.e("JsonHelper::getJSONObject",e);
			return null;
		}
	}
}
