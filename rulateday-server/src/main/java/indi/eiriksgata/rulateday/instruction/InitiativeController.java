package indi.eiriksgata.rulateday.instruction;

import indi.eiriksgata.dice.injection.InstructReflex;
import indi.eiriksgata.dice.injection.InstructService;
import indi.eiriksgata.dice.vo.MessageData;
import indi.eiriksgata.rulateday.event.EventAdapter;
import indi.eiriksgata.rulateday.event.EventUtils;
import indi.eiriksgata.rulateday.service.impl.UserInitiativeServerImpl;
import net.mamoe.mirai.event.events.*;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * author: create by Keith
 * version: v1.0
 * description: indi.eiriksgata.rulateday.instruction
 * date: 2021/3/12
 **/

@InstructService
public class InitiativeController {

    @Resource
    public static UserInitiativeServerImpl initiativeServer = new UserInitiativeServerImpl();

    @InstructReflex(value = {".atklist", "atkList"}, priority = 2)
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


    @InstructReflex(value = {".atkdel", ".atkDel", ".Atkdel", ".AtlDel"}, priority = 2)
    public String delAtk(MessageData<?> data) {
        String tempName = null;
        String resultText = "已删除你的先攻骰";
        if (!data.getMessage().equals("") && data.getMessage() != null) {
            tempName = data.getMessage();
            resultText = "已删除" + tempName + "的先攻骰";
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


    @InstructReflex(value = {".atkClear", ".clearAtk", ".atkclear", "AtkClear"}, priority = 2)
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
        return "已清空当前的先攻池";
    }

    @InstructReflex(value = {".atk", "。atk"})
    public String generateInitiativeDice(MessageData<?> data) {
        String name = null;
        String[] tempList;
        String diceFace = "d";
        AtomicBoolean isLimit = new AtomicBoolean(false);
        EventUtils.eventCallback(data.getEvent(), new EventAdapter() {
            @Override
            public void group(GroupMessageEvent event) {
                String groupId = "" + event.getGroup().getId();
                isLimit.set(initiativeServer.diceLimit(groupId));
            }

            @Override
            public void friend(FriendMessageEvent event) {
                String groupId = "-" + event.getFriend().getId();
                isLimit.set(initiativeServer.diceLimit(groupId));
            }
        });

        if (isLimit.get()) {
            return "先攻池骰子数量不能超过20个";
        }

        final String[] resultText = {"你的先攻骰为:"};
        if (!data.getMessage().equals("") && data.getMessage() != null) {
            tempList = data.getMessage().split(" ");
            if (tempList.length > 1) {
                name = tempList[1];
                resultText[0] = name + "的先攻骰为:";
            }
            diceFace = tempList[0];
        }
        String finalName = name;
        RollController.rollBasics.rollRandom(diceFace, data.getQqID(), (i, s) -> {
            //处理可能会出现小数等其他情况
            int numberValue;
            try {
                numberValue = Integer.parseInt(i);
                resultText[0] += s;
            } catch (NumberFormatException e) {
                resultText[0] = "先攻数值生成不符合要求，请符合整数型";
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
