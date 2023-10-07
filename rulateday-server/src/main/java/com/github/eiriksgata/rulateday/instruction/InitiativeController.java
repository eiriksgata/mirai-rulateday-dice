package com.github.eiriksgata.rulateday.instruction;

import com.github.eiriksgata.rulateday.event.EventUtils;
import com.github.eiriksgata.trpg.dice.injection.InstructReflex;
import com.github.eiriksgata.trpg.dice.injection.InstructService;
import com.github.eiriksgata.trpg.dice.reply.CustomText;
import com.github.eiriksgata.trpg.dice.vo.MessageData;
import com.github.eiriksgata.rulateday.event.EventAdapter;
import com.github.eiriksgata.rulateday.service.impl.UserInitiativeServerImpl;
import net.mamoe.mirai.event.events.*;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * author: create by Keith
 * version: v1.0
 * description: com.github.eiriksgata.rulateday.instruction
 * date: 2021/3/12
 **/

@InstructService
public class InitiativeController {

    @Resource
    public static UserInitiativeServerImpl initiativeServer = new UserInitiativeServerImpl();

    @InstructReflex(value = {"atklist", "atkList"}, priority = 2)
    public String getAtkList(MessageData<?> data) {
        final String[] resultText = {""};
        EventUtils.eventCallback(data.getEvent(), new EventAdapter() {
            @Override
            public void group(GroupMessageEvent event) {
                String group = "" + event.getGroup().getId();
                resultText[0] = initiativeServer.showInitiativeList(group);
            }

            @Override
            public void friend(FriendMessageEvent event) {
                String group = "-" + event.getFriend().getId();
                resultText[0] = initiativeServer.showInitiativeList(group);
            }
        });
        return resultText[0];
    }


    @InstructReflex(value = {"atkdel", "atkDel", "Atkdel", "AtlDel"}, priority = 2)
    public String delAtk(MessageData<?> data) {
        String tempName = null;
        String resultText = CustomText.getText("initiative.delete.oneself");
        if (!data.getMessage().equals("") && data.getMessage() != null) {
            tempName = data.getMessage();
            resultText = CustomText.getText("initiative.delete.other", tempName);
        }
        String finalTempName = tempName;
        EventUtils.eventCallback(data.getEvent(), new EventAdapter() {
            @Override
            public void group(GroupMessageEvent event) {
                String groupId = "" + event.getGroup().getId();
                String name = event.getSender().getNameCard();
                if (name.equals("")) {
                    name = event.getSender().getNick();
                }
                if (finalTempName != null) {
                    name = finalTempName;
                }
                initiativeServer.deleteDice(groupId, event.getSender().getId(), name);
            }

            @Override
            public void friend(FriendMessageEvent event) {
                String groupId = "-" + event.getFriend().getId();
                String name = event.getSender().getNick();
                if (finalTempName != null) {
                    name = finalTempName;
                }
                initiativeServer.deleteDice(groupId, event.getSender().getId(), name);
            }

        });
        return resultText;
    }


    @InstructReflex(value = {"atkClear", "clearAtk", "atkclear", "AtkClear"}, priority = 2)
    public String clearAtkList(MessageData<?> data) {
        EventUtils.eventCallback(data.getEvent(), new EventAdapter() {
            @Override
            public void group(GroupMessageEvent event) {
                initiativeServer.clearGroupDice("" + event.getGroup().getId());
            }

            @Override
            public void friend(FriendMessageEvent event) {
                initiativeServer.clearGroupDice("-" + event.getFriend().getId());
            }
        });
        return CustomText.getText("initiative.clear");
    }

    @InstructReflex(value = {"atk", "atk"})
    public String generateInitiativeDice(MessageData<?> data) {
        final String[] name = {null};
        String[] tempList;
        String diceFace = "d";
        AtomicBoolean isLimit = new AtomicBoolean(false);
        EventUtils.eventCallback(data.getEvent(), new EventAdapter() {
            @Override
            public void group(GroupMessageEvent event) {
                String groupId = "" + event.getGroup().getId();
                isLimit.set(initiativeServer.diceLimit(groupId));
                name[0] = event.getSenderName();
            }

            @Override
            public void friend(FriendMessageEvent event) {
                String groupId = "-" + event.getFriend().getId();
                isLimit.set(initiativeServer.diceLimit(groupId));
                name[0] = event.getSenderName();
            }
        });

        if (isLimit.get()) {
            return CustomText.getText("initiative.list.size.max");
        }

        final String[] resultText = {CustomText.getText("initiative.result.title", name[0])};
        if (!data.getMessage().equals("") && data.getMessage() != null) {
            tempList = data.getMessage().split(" ");
            if (tempList.length > 1) {
                name[0] = tempList[1];
                resultText[0] = CustomText.getText("initiative.result.title", name[0]);
            }
            diceFace = tempList[0];
        }
        String finalName = name[0];
        RollController.rollBasics.rollRandom(diceFace, data.getQqID(), (i, s) -> {
            //处理可能会出现小数等其他情况
            int numberValue;
            try {
                numberValue = Integer.parseInt(i);
                resultText[0] += s;
            } catch (NumberFormatException e) {
                resultText[0] = CustomText.getText("initiative.parameter.format.error");
                return;
            }

            EventUtils.eventCallback(data.getEvent(), new EventAdapter() {
                @Override
                public void group(GroupMessageEvent event) {
                    String groupId = "" + event.getGroup().getId();
                    String name1 = event.getSender().getNameCard();
                    if (finalName != null) name1 = finalName;
                    initiativeServer.addInitiativeDice(
                            groupId, event.getSender().getId(), name1, numberValue);
                }

                @Override
                public void friend(FriendMessageEvent event) {
                    String groupId = "-" + event.getFriend().getId();
                    String name1 = event.getSender().getNick();
                    if (finalName != null) name1 = finalName;
                    initiativeServer.addInitiativeDice(
                            groupId, event.getSender().getId(), name1, numberValue);
                }
            });
        });
        return resultText[0];
    }
}
