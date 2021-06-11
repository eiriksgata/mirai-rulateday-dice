package indi.eiriksgata.rulateday.utlis;

import com.github.kevinsawicki.http.HttpRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * author: create by Keith
 * version: v1.0
 * description: com.ptsq2101.facejavasdk.utils
 * date: 2021/3/5
 **/
public class RestUtil {

    public static String getForObject(String url) {
        return HttpRequest.get(url).body();
    }

    public static String deleteForObject(String url) {
        return HttpRequest.delete(url).body();
    }


    public static String postForMap(String url, Map<?, ?> data) {
        //HttpRequest.
        return HttpRequest.post(url, data, true).body();
    }


    public static String postForJson(String url, String jsonStr) {
        Map<String, String> map = new HashMap<String, String>();
        return postForJson(url, jsonStr, map);
    }

    public static String postForJson(String url, String jsonStr, Map<String, String> head) {
        HttpRequest request = HttpRequest.post(url);
        head.put("Content-Type", "application/json");
        head.forEach(request::header);
        return request.send(jsonStr).body();
    }

    public static String get(String url) {
        return HttpRequest.get(url).body();
    }


}
