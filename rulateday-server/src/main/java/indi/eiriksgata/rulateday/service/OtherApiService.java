package indi.eiriksgata.rulateday.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import indi.eiriksgata.rulateday.utlis.RestUtil;

public class OtherApiService {

    public static String translateToEnglishByYouDu(String text) {
        if (text == null || text.equals("")) {
            return "";
        }
        String url = "http://fanyi.youdao.com/translate?&doctype=json&type=AUTO&i=";
        String result = RestUtil.get(url + text);
        JSONObject jsonObject = JSON.parseObject(result);
        return jsonObject.getJSONArray("translateResult").getJSONArray(0).getJSONObject(0).getString("tgt");
    }
}
