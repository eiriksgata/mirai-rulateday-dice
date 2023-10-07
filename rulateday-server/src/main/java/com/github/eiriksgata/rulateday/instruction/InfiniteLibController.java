package com.github.eiriksgata.rulateday.instruction;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.github.eiriksgata.rulateday.pojo.QueryDataBase;
import com.github.eiriksgata.trpg.dice.injection.InstructReflex;
import com.github.eiriksgata.trpg.dice.injection.InstructService;
import com.github.eiriksgata.trpg.dice.reply.CustomText;
import com.github.eiriksgata.trpg.dice.vo.MessageData;
import com.github.eiriksgata.rulateday.service.DiceConfigService;
import com.github.eiriksgata.rulateday.service.UserConversationService;
import com.github.eiriksgata.rulateday.service.impl.UserConversationImpl;
import com.github.eiriksgata.rulateday.utlis.RestUtil;
import com.github.eiriksgata.rulateday.vo.ResponseBaseVo;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.github.eiriksgata.rulateday.service.impl.ApiReportImpl.apiUrl;

@InstructService
public class InfiniteLibController {

    @Resource
    private final UserConversationService conversationService = new UserConversationImpl();


    @InstructReflex(value = {"ir2"})
    public String infiniteLibOnlineQuery(MessageData<?> data) {
        if (!DiceConfigService.diceConfigMapper.selectById().getBeta_version()) {
            return CustomText.getText("infinite.lib.ir2.no.enable");
        }

        //如果输入的数据是无关键字的
        if (data.getMessage().equals("")) {
            return CustomText.getText("dr5e.rule.not.parameter");
        }
        if (data.getMessage().equals(" ")) {
            return CustomText.getText("dr5e.rule.not.parameter");
        }
        String url = apiUrl + "/openapi/v1/infinite/lib/query/name";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", data.getMessage());
        ResponseBaseVo<List<QueryDataBase>> result =
                JSONObject.parseObject(
                        RestUtil.postForJson(
                                url,
                                jsonObject.toJSONString()
                        ), new TypeReference<ResponseBaseVo<List<QueryDataBase>>>() {
                        }.getType()
                );
        List<QueryDataBase> saveData = new ArrayList<>();
        if (result.getCode() == 0) {
            if (result.getData().size() > 1) {
                StringBuilder text = new StringBuilder(CustomText.getText("dr5e.rule.lib.result.list.title"));
                int count = 0;
                for (QueryDataBase temp : result.getData()) {
                    if (count >= 20) {
                        text.append(CustomText.getText("dr5e.rule.lib.result.list.tail"));
                        break;
                    } else {
                        text.append("\n").append(count).append(". ").append(temp.getName());
                        saveData.add(temp);
                    }
                    count++;
                }
                //将记录暂时存入数据库
                conversationService.saveConversation(data.getQqID(), saveData);
                return text.toString();

            } else {
                if (result.getData().size() == 0) {
                    return CustomText.getText("dr5e.rule.lib.result.list.not.found");
                }
                return "\n" + result.getData().get(0).getName() + "\n" + result.getData().get(0).getDescribe();
            }
        }
        return CustomText.getText("infinite.lib.online.query.error");
    }

}
