package indi.eiriksgata.rulateday.instruction;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import indi.eiriksgata.dice.injection.InstructReflex;
import indi.eiriksgata.dice.injection.InstructService;
import indi.eiriksgata.dice.reply.CustomText;
import indi.eiriksgata.dice.vo.MessageData;
import indi.eiriksgata.rulateday.event.EventAdapter;
import indi.eiriksgata.rulateday.event.EventUtils;
import indi.eiriksgata.rulateday.pojo.QueryDataBase;
import indi.eiriksgata.rulateday.pojo.RuleBook;
import indi.eiriksgata.rulateday.service.CrazyLibraryService;
import indi.eiriksgata.rulateday.service.Dnd5eLibService;
import indi.eiriksgata.rulateday.service.RuleService;
import indi.eiriksgata.rulateday.service.UserConversationService;
import indi.eiriksgata.rulateday.service.impl.*;
import indi.eiriksgata.rulateday.utlis.HexConvertUtil;
import indi.eiriksgata.rulateday.vo.ResponseBaseVo;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.GroupTempMessageEvent;
import net.mamoe.mirai.utils.ExternalResource;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * author: create by Keith
 * version: v1.0
 * description: indi.eiriksgata.rulateday.instruction
 * date: 2020/11/4
 **/

@InstructService
public class QueryController {

    public static ExecutorService cachedThread = Executors.newCachedThreadPool();

    @Resource
    private final CrazyLibraryService crazyLibraryService = new CrazyLibraryImpl();

    @Resource
    private final Dnd5eLibService dnd5eLibService = new Dnd5eLibServiceImpl();

    @Resource
    private final RuleService ruleService = new RuleServiceImpl();

    @Resource
    private final UserConversationService conversationService = new UserConversationImpl();


    //发疯状态确认
    @InstructReflex(value = {".ti", "。ti"})
    public String getCrazyState(MessageData<?> data) {
        return crazyLibraryService.getRandomCrazyDescribe();
    }

    //发疯结束总结
    @InstructReflex(value = {".li", "。li", ".Li", ".LI"})
    public String getCrazyOverEvent(MessageData<?> data) {
        return crazyLibraryService.getCrazyOverDescribe();
    }

    @InstructReflex(value = {".cr", "。cr", ".cr7"})
    public String queryCoc7Rule(MessageData<?> data) {
        data.setMessage(data.getMessage().replaceAll(" ", ""));
        return  ruleService.selectRule(data.getMessage());
    }

    @InstructReflex(value = {".dr", "。dr", ".d5er", ".Dr", ".DR"})
    public String queryDnd5eRule(MessageData<?> data) {
        //如果输入的数据是无关键字的
        if (data.getMessage().equals("")) {
            return CustomText.getText("dr5e.rule.not.parameter");
        }
        if (data.getMessage().equals(" ")) {
            return CustomText.getText("dr5e.rule.not.parameter");
        }

        //先进行模糊查询
        List<QueryDataBase> result = dnd5eLibService.findName("%" + data.getMessage() + "%");
        List<QueryDataBase> saveData = new ArrayList<>();
        if (result.size() > 1) {
            StringBuilder text = new StringBuilder(CustomText.getText("dr5e.rule.lib.result.list.title"));
            int count = 0;
            for (QueryDataBase temp : result) {
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
            if (result.size() == 0) {
                return CustomText.getText("dr5e.rule.lib.result.list.not.found");
            }
            if (result.get(0).getName().length() > 5) {
                if (result.get(0).getName().startsWith("怪物图鉴:")) {
                    cachedThread.execute(() -> dnd5eLibService.sendMMImage(data.getEvent(), result.get(0)));
                }
            }
            return result.get(0).getName() + "\n" + result.get(0).getDescribe().replaceAll("\n\n", "\n");
        }
    }


    @InstructReflex(value = {".help", "。help"})
    public String help(MessageData<?> data) {
        return CustomText.getText("instructions.help.result");
    }

    @InstructReflex(value = {".help指令", "。help指令"}, priority = 3)
    public String helpInstruct(MessageData<?> data) {
        return CustomText.getText("instructions.all.result");
    }

    @InstructReflex(value = {".rmm", "。rmm"})
    public String rollMM(MessageData<?> data) {
        QueryDataBase result = dnd5eLibService.getRandomMMData();
        cachedThread.execute(() -> dnd5eLibService.sendMMImage(data.getEvent(), result));
        return result.getName() + "\n" + result.getDescribe().replaceAll("\n\n", "\n");
    }

    @InstructReflex(value = {".kkp", "。kkp"})
    public String randomPicture(MessageData<?> data) {
        String url = ApiReportImpl.apiUrl + "/picture/random";
        String resultJson;
        try {
            resultJson = HttpRequest.get(url).body();
        } catch (Exception e) {
            return CustomText.getText("api.request.error");
        }
        ResponseBaseVo<String> response = new Gson().fromJson(
                resultJson, new TypeToken<ResponseBaseVo<String>>() {
                }.getType());
        byte[] pictureData = HexConvertUtil.hexStringToByteArray(response.getData());
        EventUtils.eventCallback(data.getEvent(), new EventAdapter() {
            @Override
            public void group(GroupMessageEvent event) {
                event.getGroup().sendMessage(
                        event.getGroup().uploadImage(ExternalResource.create(pictureData)));
            }

            @Override
            public void friend(FriendMessageEvent event) {
                event.getSender().sendMessage(
                        event.getSender().uploadImage(ExternalResource.create(pictureData))
                );
            }

            @Override
            public void groupTemp(GroupTempMessageEvent event) {
                event.getSender().sendMessage(
                        event.getSender().uploadImage(ExternalResource.create(pictureData))
                );
            }
        });
        return null;
    }

    @InstructReflex(value = {".rmi", "。rmi"})
    public String rollMagicItem() {
        return "null";
    }

    @InstructReflex(value = {".rt", "。rt"})
    public String rollTool() {
        return "null";
    }

    @InstructReflex(value = {".drw", "。drw"})
    public String rollWeapon() {
        return "null";
    }


}
