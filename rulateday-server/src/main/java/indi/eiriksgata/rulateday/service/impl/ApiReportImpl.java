package indi.eiriksgata.rulateday.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import indi.eiriksgata.rulateday.DiceMessageEventHandle;
import indi.eiriksgata.rulateday.RulatedayCore;
import indi.eiriksgata.rulateday.service.ApiReport;
import indi.eiriksgata.rulateday.utlis.RestUtil;
import indi.eiriksgata.rulateday.vo.ResponseBaseVo;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * author: create by Keith
 * version: v1.0
 * description: indi.eiriksgata.rulateday.service.impl
 * date: 2021/6/7
 **/
public class ApiReportImpl implements ApiReport {

    public static String apiUrl = ResourceBundle.getBundle("resources").getString("api.server.url");

    @Override
    public void exceptionReport(String title, String content, Long qqId) {
        String path = "/feedback/exception";
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("qqId", qqId);
        requestData.put("content", content);
        requestData.put("title", title);
        try {
            ResponseBaseVo<Object> result =
                    new Gson().fromJson(RestUtil.postForJson(apiUrl + path, new Gson().toJson(requestData)),
                            new TypeToken<ResponseBaseVo<Object>>() {
                            }.getType());
            if (result.getCode() != 0) {
                RulatedayCore.INSTANCE.getLogger().info("Rulateday exception report fail.");
            }
        } catch (Exception e) {
            RulatedayCore.INSTANCE.getLogger().info("rulateday exception report url error");
        }


    }

}
