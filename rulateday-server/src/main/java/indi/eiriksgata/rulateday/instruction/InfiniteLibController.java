package indi.eiriksgata.rulateday.instruction;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import indi.eiriksgata.dice.injection.InstructReflex;
import indi.eiriksgata.dice.injection.InstructService;
import indi.eiriksgata.dice.reply.CustomText;
import indi.eiriksgata.dice.vo.MessageData;
import indi.eiriksgata.rulateday.pojo.QueryDataBase;
import indi.eiriksgata.rulateday.service.UserConversationService;
import indi.eiriksgata.rulateday.service.impl.UserConversationImpl;
import indi.eiriksgata.rulateday.utlis.RestUtil;
import indi.eiriksgata.rulateday.vo.ResponseBaseVo;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static indi.eiriksgata.rulateday.service.impl.ApiReportImpl.apiUrl;

@InstructService
public class InfiniteLibController {

    @Resource
    private final UserConversationService conversationService = new UserConversationImpl();


    @InstructReflex(value = {".ir"})
    public String infiniteLibOnlineQuery(MessageData<?> data) {
        //如果输入的数据是无关键字的
        if (data.getMessage().equals("")) {
            return CustomText.getText("dr5e.rule.not.parameter");
        }
        if (data.getMessage().equals(" ")) {
            return CustomText.getText("dr5e.rule.not.parameter");
        }
        String url = apiUrl + "/infinite/lib/query/name";
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
        return "在线数据查询出错";
    }

}
