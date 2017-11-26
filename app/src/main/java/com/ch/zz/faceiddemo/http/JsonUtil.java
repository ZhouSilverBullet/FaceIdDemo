package com.ch.zz.faceiddemo.http;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

/**
 * @author ZengLei
 *         <p>
 * @version 2016年8月15日
 *          <p>
 */
public class JsonUtil {
    public static int getStatusCode(JSONObject jsonObject) {
        return getInt(jsonObject, "code");
    }

    public static String getServerMsg(JSONObject jsonObject) {
        return getString(jsonObject, "msg");
    }

    public static Object getJsonData(JSONObject jsonObject) {
        if (jsonObject == null)
            return null;

        Object o = jsonObject.opt("data");
        if (o instanceof JSONArray) {
            JSONArray arr = (JSONArray) o;
            if (arr.length() == 0) {
                return null;
            }
        }
        return jsonObject.opt("data");
    }

    public static String getCHB(JSONObject jsonObject) {
        if (jsonObject == null)
            return null;
        return getString(jsonObject, "");
    }

    public static HashMap<String, String> getMapFromJson(JSONObject jsonObject) {
        if (jsonObject == null)
            return null;

        Iterator<String> iterator = jsonObject.keys();
        if (!iterator.hasNext())
            return null;

        HashMap<String, String> hashMap = new HashMap<String, String>();

        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = getString(jsonObject, key);

            hashMap.put(key, value);
        }

        return hashMap;
    }

    public static String getString(JSONObject jsonObject, String key) {
        if (jsonObject == null)
            return "";

        return jsonObject.optString(key, "");
    }

    public static int getInt(JSONObject jsonObject, String key) {
        if (jsonObject == null)
            return 0;

        return jsonObject.optInt(key);
    }
}
