package indi.eiriksgata.rulateday.instruction;

import indi.eiriksgata.dice.reply.CustomText;
import indi.eiriksgata.rulateday.config.GlobalData;
import indi.eiriksgata.rulateday.service.BotControlService;
import indi.eiriksgata.rulateday.service.impl.BotControlServiceImpl;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.MemberPermission;
import net.mamoe.mirai.event.events.GroupMessageEvent;

/**
 * author: create by Keith
 * version: v1.0
 * description: indi.eiriksgata.rulateday.instruction
 * date: 2020/11/13
 **/

public class BotServiceControl {

    public final static BotControlService botControl = new BotControlServiceImpl();

    public boolean groupBotOff(GroupMessageEvent event) {
        if (event.getSender().getPermission().getLevel() == MemberPermission.ADMINISTRATOR.getLevel() ||
                event.getSender().getPermission().getLevel() == MemberPermission.OWNER.getLevel() ||
                event.getSender().getId() == Long.parseLong(GlobalData.configData.getString("master.QQ.number"))
        ) {
            long id = Bot.getInstances().get(0).getId();
            String regex = ".*\\[mirai:at:" + id + "].*\\.botoff";
            String messageSource = event.getMessage().toString().replaceAll(" ", "");
            String messageContent = event.getMessage().contentToString().replaceAll(" ", "");
            if (messageSource.matches(regex) || messageContent.equals(".botoff")) {
                botControl.groupDisable(event.getGroup().getId());
                event.getGroup().sendMessage(CustomText.getText("dice.bot.off"));
                return true;
            }
        } else {
            event.getGroup().sendMessage("需要群主或者管理员或者骰主才能关闭服务。");
        }
        return false;
    }

    public boolean groupBotOn(GroupMessageEvent event) {
        if (event.getSender().getPermission().getLevel() == MemberPermission.ADMINISTRATOR.getLevel() ||
                event.getSender().getPermission().getLevel() == MemberPermission.OWNER.getLevel() ||
                event.getSender().getId() == Long.parseLong(GlobalData.configData.getString("master.QQ.number"))
        ) {
            long id = Bot.getInstances().get(0).getId();
            String regex = ".*\\[mirai:at:" + id + "].*\\.boton";
            String messageSource = event.getMessage().toString().replaceAll(" ", "");
            String messageContent = event.getMessage().contentToString().replaceAll(" ", "");
            if (messageSource.matches(regex) || messageContent.equals(".boton")) {
                botControl.groupEnable(event.getGroup().getId());
                event.getGroup().sendMessage(CustomText.getText("dice.bot.on"));
                return true;
            }
        } else {
            event.getGroup().sendMessage("需要群主或者管理员或者骰主才能打开服务。");
        }
        return false;
    }

    public boolean isSpeakers(GroupMessageEvent event) {
        long groupId = event.getGroup().getId();
        return botControl.groupIsEnable(groupId);
    }

    public boolean isBlacklist(GroupMessageEvent event) {
        long groupId = event.getGroup().getId();
        return botControl.groupIsBlacklist(groupId);
    }


}
