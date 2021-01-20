package indi.eiriksgata.rulateday.instruction;

import indi.eiriksgata.dice.reply.CustomText;
import indi.eiriksgata.rulateday.service.BotControlService;
import indi.eiriksgata.rulateday.service.impl.BotControlServiceImpl;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.events.GroupMessageEvent;

import javax.annotation.Resource;

/**
 * author: create by Keith
 * version: v1.0
 * description: indi.eiriksgata.rulateday.instruction
 * date: 2020/11/13
 **/

public class BotServiceControl {

    @Resource
    public final static BotControlService botControl = new BotControlServiceImpl();

    public boolean groupBotOff(GroupMessageEvent event) {
        long id = Bot.getInstances().get(0).getId();
        String regex = ".*\\[mirai:at:" + id + ",@.*]\\.botoff";
        String messageSource = event.getMessage().toString().replaceAll(" ", "");
        String messageContent = event.getMessage().contentToString().replaceAll(" ", "");
        if (messageSource.matches(regex) || messageContent.equals(".botoff")) {
            botControl.groupDisable(event.getGroup().getId());
            event.getGroup().sendMessage(CustomText.getText("dice.bot.off"));
            return true;
        }
        return false;
    }

    public boolean groupBotOn(GroupMessageEvent event) {
        long id = Bot.getInstances().get(0).getId();
        String regex = ".*\\[mirai:at:" + id + ",@.*]\\.boton";
        String messageSource = event.getMessage().toString().replaceAll(" ", "");
        String messageContent = event.getMessage().contentToString().replaceAll(" ", "");
        if (messageSource.matches(regex) || messageContent.equals(".boton")) {
            botControl.groupEnable(event.getGroup().getId());
            event.getGroup().sendMessage(CustomText.getText("dice.bot.on"));
            return true;
        }
        return false;
    }

    public boolean isSpeakers(GroupMessageEvent event) {
        long groupId = event.getGroup().getId();
        return botControl.groupIsEnable(groupId);
    }

}
