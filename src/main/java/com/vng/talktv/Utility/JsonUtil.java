package com.vng.talktv.Utility;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import redis.clients.jedis.Tuple;

import java.util.Set;

public class JsonUtil {
    public static JSONObject JSONObjGenerator(JSONObject jsonObject) {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("name", "oOo_Nghĩa_oOo");
        jsonObj.put("avatar", "link abc xyz");
        jsonObj.putAll(jsonObject);
        return jsonObj;
    }

    public static JSONArray JsonArrayGenerator(Integer index, JSONObject jsonObject) {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < index; i++) {
            jsonArray.add(JSONObjGenerator(jsonObject));
        }
        return jsonArray;
    }

    public static JSONArray convertSetTupleToJsonArray(Set<Tuple> mySet, Integer index) {
        JSONArray jsonArray = new JSONArray();
        int i = 1;
        for (Tuple item : mySet) {
            JSONObject obj = new JSONObject();
            obj.put("rank", i++ + index * 10);
            obj.put("avatar", "ashfkasfhskhfkaf");
            obj.put("name", "BigK Đáng Iuuuuuu");
            obj.put("contribute", item.getScore());
            obj.put("id", item.getElement());
            jsonArray.add(obj);
        }
        return jsonArray;
    }
}
