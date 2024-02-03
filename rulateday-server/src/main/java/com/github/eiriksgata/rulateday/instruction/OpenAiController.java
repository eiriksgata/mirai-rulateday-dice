package com.github.eiriksgata.rulateday.instruction;

import com.alibaba.fastjson.JSONObject;
import com.github.eiriksgata.rulateday.config.GlobalData;
import com.github.eiriksgata.rulateday.dto.DiceMessageDTO;
import com.github.eiriksgata.trpg.dice.injection.InstructReflex;
import com.github.eiriksgata.trpg.dice.injection.InstructService;

import com.github.eiriksgata.rulateday.utlis.RestUtil;
import com.github.eiriksgata.rulateday.vo.OpenAiRequestCompletions;

import java.util.HashMap;
import java.util.Map;

@InstructService
public class OpenAiController {

    public String apiKey = GlobalData.configData.getJSONObject("open-ai").getString("api-key");


    @InstructReflex(value = {"chat"}, priority = 3)
    public String openAiChat(DiceMessageDTO data) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + apiKey);
        OpenAiRequestCompletions submitData = new OpenAiRequestCompletions();
        submitData.setPrompt(data.getBody());
        String resultText = RestUtil.postForJson("https://api.openai.com/v1/completions", JSONObject.toJSONString(submitData), headers);
        return JSONObject.parseObject(resultText).getJSONArray("choices").getJSONObject(0).getString("text");
    }
}
