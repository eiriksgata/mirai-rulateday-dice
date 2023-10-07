package com.github.eiriksgata.rulateday.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.github.eiriksgata.rulateday.RulatedayCore;
import com.github.eiriksgata.rulateday.service.ApiReport;
import com.github.eiriksgata.rulateday.utlis.RestUtil;
import com.github.eiriksgata.rulateday.vo.ResponseBaseVo;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * author: create by Keith
 * version: v1.0
 * description: com.github.eiriksgata.rulateday.service.impl
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
                    JSONObject.parseObject(
                            RestUtil.postForJson(apiUrl + path, JSONObject.toJSONString(requestData)),
                            new TypeReference<ResponseBaseVo<Object>>() {
                            }.getType());
            if (result.getCode() != 0) {
                RulatedayCore.INSTANCE.getLogger().info("Rulateday exception report fail.");
            }
        } catch (Exception e) {
            RulatedayCore.INSTANCE.getLogger().info("rulateday exception report url error");
        }


    }

}
