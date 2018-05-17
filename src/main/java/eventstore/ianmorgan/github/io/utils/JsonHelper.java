package eventstore.ianmorgan.github.io.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonHelper {

    /**
     * Take a JSONObject and convert to normal Java Map by
     * examining each key.
     *
     * @param json The JSONObject
     * @return a Java Map with same content as the original JSONObject
     */
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


    /**
     * Take a JSONArray an convert to a normal Java List , examining each
     * element in the array individually
     *
     * @param array The JSONArray
     * @return A Java List with the same content as the original JSONArray
     */
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
