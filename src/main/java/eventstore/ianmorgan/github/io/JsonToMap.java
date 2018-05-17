package eventstore.ianmorgan.github.io;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonToMap {

    public static Map<String, Object> jsonToMap(JSONObject json) {
        Map result = new HashMap(json.keySet().size());

        for (String key : json.keySet()) {
            Object o = json.get(key);
            if (o instanceof JSONObject) {
                result.put(key, jsonToMap((JSONObject) o));
            } else if (o instanceof JSONArray) {
                result.put(key, jsonToList((JSONArray) o));
            } else {
                result.put(key, o);
            }
        }
        return result;
    }


    public static List<Object> jsonToList(JSONArray array) {
        List result = new ArrayList(array.length());

        for (int i = 0; i < array.length(); i++) {
            Object o = array.get(i);
            if (o instanceof JSONObject) {
                result.add(i, jsonToMap((JSONObject) o));
            } else if (o instanceof JSONArray) {
                result.add(i, jsonToList((JSONArray) o));
            } else {
                result.add(i, o);
            }
        }
        return result;
    }
}
