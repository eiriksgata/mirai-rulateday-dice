package com.github.eiriksgata.rulateday.instruction;

import com.github.eiriksgata.rulateday.config.GlobalData;
import com.github.eiriksgata.trpg.dice.reply.CustomText;
import com.github.eiriksgata.rulateday.init.CacheReuseData;
import com.github.eiriksgata.rulateday.service.BotControlService;
import com.github.eiriksgata.rulateday.service.impl.BotControlServiceImpl;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.MemberPermission;
import net.mamoe.mirai.event.events.GroupMessageEvent;

/**
 * author: create by Keith
 * version: v1.0
 * description: com.github.eiriksgata.rulateday.instruction
 * date: 2020/11/13
 **/

public class BotServiceControl {

    public final static BotControlService botControl = new BotControlServiceImpl();

    public boolean groupBotOff(GroupMessageEvent event) {
        long id = Bot.getInstances().get(0).getId();
        String regex = "^(.*\\[mirai:at:" + id + "].*" + CacheReuseData.prefixMatchListRegex + "botoff|" + CacheReuseData.prefixMatchListRegex + "botoff)$";
        String messageSource = event.getMessage().toString().trim();
        String messageContent = event.getMessage().contentToString().trim();
        if (messageSource.matches(regex) || messageContent.matches(regex)) {
            if (event.getSender().getPermission().getLevel() == MemberPermission.ADMINISTRATOR.getLevel() ||
                    event.getSender().getPermission().getLevel() == MemberPermission.OWNER.getLevel() ||
                    event.getSender().getId() == Long.parseLong(GlobalData.configData.getString("master.QQ.number"))
            ) {
                botControl.groupDisable(event.getGroup().getId());
                event.getGroup().sendMessage(CustomText.getText("dice.bot.off"));
                return true;
            } else {
                event.getGroup().sendMessage(CustomText.getText("bot.group.no.permission"));
            }
        }
        return false;
    }

    public boolean groupBotOn(GroupMessageEvent event) {
        long id = Bot.getInstances().get(0).getId();
        String regex = "^(.*\\[mirai:at:" + id + "].*" + CacheReuseData.prefixMatchListRegex + "boton|" + CacheReuseData.prefixMatchListRegex + "boton)$";
        String messageSource = event.getMessage().toString().trim();
        String messageContent = event.getMessage().contentToString().trim();
        if (messageSource.matches(regex) || messageContent.matches(regex)) {
            if (event.getSender().getPermission().getLevel() == MemberPermission.ADMINISTRATOR.getLevel() ||
                    event.getSender().getPermission().getLevel() == MemberPermission.OWNER.getLevel() ||
                    event.getSender().getId() == Long.parseLong(GlobalData.configData.getString("master.QQ.number"))
            ) {
                botControl.groupEnable(event.getGroup().getId());
                event.getGroup().sendMessage(CustomText.getText("dice.bot.on"));
                return true;
            } else {
                event.getGroup().sendMessage(CustomText.getText("bot.group.no.permission"));
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
        if (inputText == null || inputText.equals("")) {
            return null;
        }
        for (String temp : CacheReuseData.prefixMatchList) {
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
