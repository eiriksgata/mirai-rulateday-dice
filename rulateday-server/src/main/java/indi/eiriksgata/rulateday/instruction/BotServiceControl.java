package indi.eiriksgata.rulateday.instruction;

import com.alibaba.fastjson2.TypeReference;
import indi.eiriksgata.dice.reply.CustomText;
import indi.eiriksgata.rulateday.config.GlobalData;
import indi.eiriksgata.rulateday.service.BotControlService;
import indi.eiriksgata.rulateday.service.impl.BotControlServiceImpl;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.MemberPermission;
import net.mamoe.mirai.event.events.GroupMessageEvent;

import java.util.List;


/**
 * author: create by Keith
 * version: v1.0
 * description: indi.eiriksgata.rulateday.instruction
 * date: 2020/11/13
 **/

public class BotServiceControl {

    public final static BotControlService botControl = new BotControlServiceImpl();

    public boolean groupBotOff(GroupMessageEvent event) {
        String prefix = isPrefixMatch(event.getMessage().contentToString());
        if (prefix == null) {
            return false;
        }
        long id = Bot.getInstances().get(0).getId();
        String regex = ".*\\[mirai:at:" + id + "].*" + prefix + "botoff";
        String messageSource = event.getMessage().toString().replaceAll(" ", "");
        String messageContent = event.getMessage().contentToString().replaceAll(" ", "");
        if (messageSource.matches(regex) || messageContent.equals(prefix + "botoff")) {
            if (event.getSender().getPermission().getLevel() == MemberPermission.ADMINISTRATOR.getLevel() ||
                    event.getSender().getPermission().getLevel() == MemberPermission.OWNER.getLevel() ||
                    event.getSender().getId() == Long.parseLong(GlobalData.configData.getString("master.QQ.number"))
            ) {
                botControl.groupDisable(event.getGroup().getId());
                event.getGroup().sendMessage(CustomText.getText("dice.bot.off"));
                return true;
            } else {
                event.getGroup().sendMessage("需要群主或者管理员或者骰主才能关闭服务。");

            }
        }
        return false;
    }

    public boolean groupBotOn(GroupMessageEvent event) {
        String prefix = isPrefixMatch(event.getMessage().contentToString());
        if (prefix == null) {
            return false;
        }
        long id = Bot.getInstances().get(0).getId();
        String regex = ".*\\[mirai:at:" + id + "].*" + prefix + "boton";
        String messageSource = event.getMessage().toString().replaceAll(" ", "");
        String messageContent = event.getMessage().contentToString().replaceAll(" ", "");
        if (messageSource.matches(regex) || messageContent.equals(prefix + "boton")) {
            if (event.getSender().getPermission().getLevel() == MemberPermission.ADMINISTRATOR.getLevel() ||
                    event.getSender().getPermission().getLevel() == MemberPermission.OWNER.getLevel() ||
                    event.getSender().getId() == Long.parseLong(GlobalData.configData.getString("master.QQ.number"))
            ) {
                botControl.groupEnable(event.getGroup().getId());
                event.getGroup().sendMessage(CustomText.getText("dice.bot.on"));
                return true;
            } else {
                event.getGroup().sendMessage("需要群主或者管理员或者骰主才能打开服务。");
            }
        }
        return false;
    }

    public boolean isSpeakers(GroupMessageEvent event) {
        long groupId = event.getGroup().getId();
        return botControl.groupIsEnable(groupId);
    }

    public boolean isBlacklist(long id) {
        return botControl.groupIsBlacklist(id);
    }


    /*
    指令前缀检测，如果满足指令前缀，则返回该指令前缀文本，如果不满足，则返回null
     */
    public String isPrefixMatch(String inputText) {
        List<String> list = GlobalData.configData.getObject("instructions.prefix.list", new TypeReference<List<String>>() {
        }.getType());
        if (inputText == null || inputText.equals("")) {
            return null;
        }
        for (String temp : list) {
            if (temp.length() > inputText.length()) {
                continue;
            }
            if (temp.equals(inputText.substring(0, temp.length()))) {
                return temp;
            }

        }
        return null;
    }

}
