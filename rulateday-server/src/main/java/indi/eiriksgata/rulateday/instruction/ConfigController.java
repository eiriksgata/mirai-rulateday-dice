package indi.eiriksgata.rulateday.instruction;

import indi.eiriksgata.dice.injection.InstructReflex;
import indi.eiriksgata.dice.injection.InstructService;
import indi.eiriksgata.dice.reply.CustomText;
import indi.eiriksgata.dice.vo.MessageData;
import indi.eiriksgata.rulateday.config.GlobalData;
import indi.eiriksgata.rulateday.event.EventAdapter;
import indi.eiriksgata.rulateday.event.EventUtils;
import indi.eiriksgata.rulateday.service.DiceConfigService;
import indi.eiriksgata.rulateday.utlis.MyBatisUtil;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.MemberPermission;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;

import java.util.ResourceBundle;

@InstructService
public class ConfigController {

    @InstructReflex(value = {"pcon"}, priority = 3)
    public String privateChatOn(MessageData<?> data) {
        //启动私聊
        String number = GlobalData.configData.getString("master.QQ.number");
        if (number.equals("")) {
            return CustomText.getText("dice.master.number.no.set");
        }
        if (data.getQqID() == Long.parseLong(number)) {
            DiceConfigService.diceConfigMapper.updateByPrivateChat(true);
            MyBatisUtil.getSqlSession().commit();
            return CustomText.getText("dice.private.chat.enable");
        } else {
            return CustomText.getText("dice.private.chat.no.permission");
        }
    }

    @InstructReflex(value = {"pcoff"}, priority = 3)
    public String privateChatOff(MessageData<?> data) {
        String number = GlobalData.configData.getString("master.QQ.number");
        if (number.equals("")) {
            return CustomText.getText("dice.master.number.no.set");
        }
        if (data.getQqID() == Long.parseLong(number)) {
            DiceConfigService.diceConfigMapper.updateByPrivateChat(false);
            MyBatisUtil.getSqlSession().commit();
            return CustomText.getText("dice.private.chat.disable");
        } else {
            return CustomText.getText("dice.private.chat.no.permission");
        }
    }

    @InstructReflex(value = {"betaon"}, priority = 3)
    public String betaVersionOn(MessageData<?> data) {
        String number = GlobalData.configData.getString("master.QQ.number");
        if (number.equals("")) {
            return CustomText.getText("dice.master.number.no.set");
        }
        if (data.getQqID() == Long.parseLong(number)) {
            DiceConfigService.diceConfigMapper.updateByBetaVersion(true);
            MyBatisUtil.getSqlSession().commit();
            return CustomText.getText("dice.bate.enable");
        } else {
            return CustomText.getText("dice.beta.no.permission");
        }
    }

    @InstructReflex(value = {"betaoff"}, priority = 3)
    public String betaVersionOff(MessageData<?> data) {
        String number = GlobalData.configData.getString("master.QQ.number");
        if (number.equals("")) {
            return CustomText.getText("dice.master.number.no.set");
        }
        if (data.getQqID() == Long.parseLong(number)) {
            DiceConfigService.diceConfigMapper.updateByBetaVersion(false);
            MyBatisUtil.getSqlSession().commit();
            return CustomText.getText("dice.bate.disable");
        } else {
            return CustomText.getText("dice.bate.no.permission");
        }
    }

    @InstructReflex(value = {"version"}, priority = 3)
    public String getProgramVersion(MessageData<?> data) {
        ResourceBundle config = ResourceBundle.getBundle("application");
        return "\n" + config.getString("version");
    }

    @InstructReflex(value = {"dismiss"}, priority = 3)
    public String dismissCurrentGroup(MessageData<?> data) {
        String number = GlobalData.configData.getString("master.QQ.number");
        EventUtils.eventCallback(data.getEvent(), new EventAdapter() {
            @Override
            public void group(GroupMessageEvent event) {
                event.getGroup().sendMessage(CustomText.getText("bot.group.dismiss"));

                if (event.getSender().getPermission().getLevel() == MemberPermission.ADMINISTRATOR.getLevel() ||
                        event.getSender().getPermission().getLevel() == MemberPermission.OWNER.getLevel() ||
                        data.getQqID() == Long.parseLong(number)
                ) {
                    event.getGroup().quit();
                } else {
                    event.getGroup().sendMessage(CustomText.getText("bot.group.dismiss.no.permission"));
                }
            }
        });
        return null;
    }

    @InstructReflex(value = {"quitGroup", ".quitgroup"}, priority = 4)
    public String quitGroupByMaster(MessageData<?> data) {
        String number = GlobalData.configData.getString("master.QQ.number");
        if (!number.equals("" + data.getQqID())) {
            return CustomText.getText("bot.group.quit.no.permission");
        }
        int groupId;
        if (data.getMessage().matches("^\\d{1,20}$")) {
            groupId = Integer.parseInt(data.getMessage());
        } else {
            return CustomText.getText("bot.group.quit.id.error");
        }

        EventUtils.eventCallback(data.getEvent(), new EventAdapter() {
            @Override
            public void group(GroupMessageEvent event) {
                Group group = event.getBot().getGroup(groupId);
                if (group == null) {
                    event.getGroup().sendMessage(CustomText.getText("bot.group.quit.not.found", groupId));
                } else {
                    if (group.quit()) {
                        event.getGroup().sendMessage(CustomText.getText("bot.group.quit.success", groupId));
                    } else {
                        event.getGroup().sendMessage(CustomText.getText("bot.group.quit.fail", groupId));
                    }
                }
            }

            @Override
            public void friend(FriendMessageEvent event) {
                Group group = event.getBot().getGroup(groupId);
                if (group == null) {
                    event.getSender().sendMessage(CustomText.getText("bot.group.quit.not.found", groupId));
                } else {
                    if (group.quit()) {
                        event.getSender().sendMessage(CustomText.getText("bot.group.quit.success", groupId));
                    } else {
                        event.getSender().sendMessage(CustomText.getText("bot.group.quit.fail", groupId));
                    }
                }
            }
        });

        return null;
    }
}
