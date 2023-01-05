package indi.eiriksgata.rulateday.instruction;

import com.alibaba.fastjson.JSONObject;
import indi.eiriksgata.dice.injection.InstructReflex;
import indi.eiriksgata.dice.injection.InstructService;
import indi.eiriksgata.dice.vo.MessageData;
import indi.eiriksgata.rulateday.config.GlobalData;
import indi.eiriksgata.rulateday.utlis.RestUtil;
import indi.eiriksgata.rulateday.vo.OpenAiRequestCompletions;

import java.util.HashMap;
import java.util.Map;

@InstructService
public class OpenAiController {

    public String apiKey = GlobalData.configData.getJSONObject("open-ai").getString("api-key");


    @InstructReflex(value = {"chat"}, priority = 3)
    public String openAiChat(MessageData<?> data) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + apiKey);
        OpenAiRequestCompletions submitData = new OpenAiRequestCompletions();
        submitData.setPrompt(data.getMessage());
        String resultText = RestUtil.postForJson("https://api.openai.com/v1/completions", JSONObject.toJSONString(submitData), headers);
        return JSONObject.parseObject(resultText).getJSONArray("choices").getJSONObject(0).getString("text");
    }
}
