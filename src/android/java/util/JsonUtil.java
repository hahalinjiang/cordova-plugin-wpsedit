package com.skytech.wps.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/*

 * File: JsonUtil.java

 * Author: jianglj

 * Create: 2019-07-31 09:32

 */public class JsonUtil {
    public static Map<String, Object> toMap(JSONObject object) {
        Map<String, Object> map = new HashMap<String, Object>();
        Object value;
        String key;
        for (Iterator<?> it = object.keys(); it.hasNext() ; ) {
            key = (String) it.next();
            if (object.isNull(key)) {
                map.put(key, null);
            } else {
                try {
                    value = object.get(key);
                    if (value instanceof JSONArray) {
                        value = toList((JSONArray) value);
                    } else if (value instanceof JSONObject) {
                        value = toMap((JSONObject) value);
                    }
                    map.put(key, value);
                } catch (JSONException e) {
                }
            }
        }
        return map;
    }



    private static List toList(JSONArray array) {
        List list = new ArrayList();
        Object value;
        for (int i = 0; i < array.length(); i++) {
            try {
                value = array.get(i);
                if (value instanceof JSONArray) {
                    value = toList((JSONArray) value);
                } else if (value instanceof JSONObject) {
                    value = toMap((JSONObject) value);
                }
                list.add(value);
            } catch (JSONException e) {
            }
        }
        return list;
    }

}