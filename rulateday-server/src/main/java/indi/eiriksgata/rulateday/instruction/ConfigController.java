package indi.eiriksgata.rulateday.instruction;

import indi.eiriksgata.dice.injection.InstructReflex;
import indi.eiriksgata.dice.injection.InstructService;
import indi.eiriksgata.dice.vo.MessageData;
import indi.eiriksgata.rulateday.config.GlobalData;
import indi.eiriksgata.rulateday.event.EventAdapter;
import indi.eiriksgata.rulateday.event.EventUtils;
import indi.eiriksgata.rulateday.service.DiceConfigService;
import indi.eiriksgata.rulateday.utlis.MyBatisUtil;
import net.mamoe.mirai.contact.MemberPermission;
import net.mamoe.mirai.event.events.GroupMessageEvent;

import java.util.ResourceBundle;

@InstructService
public class ConfigController {

    @InstructReflex(value = {".pcon"}, priority = 3)
    public String privateChatOn(MessageData<?> data) {
        //启动私聊
        String number = GlobalData.configData.getString("master.QQ.number");
        if (number.equals("")) {
            return "本插件还未设置骰主QQ号，请在本程序目录下的 config/indi.eiriksgata.rulateday-dice/config.json 文件中的 'master.QQ.number' 中进行设置，设置后才可以使用本功能";
        }
        if (data.getQqID() == Long.parseLong(number)) {
            DiceConfigService.diceConfigMapper.updateByPrivateChat(true);
            MyBatisUtil.getSqlSession().commit();
            return "已启用私聊功能。启用私聊功能会导致账号会极大可能被冻结，作者不建议开启私聊功能。关闭私聊功能可以发送.pcoff";
        } else {
            return "你不是骰主，无法启用私聊功能";
        }
    }

    @InstructReflex(value = {".pcoff"}, priority = 3)
    public String privateChatOff(MessageData<?> data) {
        String number = GlobalData.configData.getString("master.QQ.number");
        if (number.equals("")) {
            return "本插件还未设置骰主QQ号，请在本程序目录下的 config/indi.eiriksgata.rulateday-dice/config.json 文件中的 'master.QQ.number' 中进行设置，设置后才可以使用本功能";
        }
        if (data.getQqID() == Long.parseLong(number)) {
            DiceConfigService.diceConfigMapper.updateByPrivateChat(false);
            MyBatisUtil.getSqlSession().commit();
            return "已禁用私聊功能";
        } else {
            return "你不是骰主，无法禁用私聊功能";
        }
    }

    @InstructReflex(value = {".betaon"}, priority = 3)
    public String betaVersionOn(MessageData<?> data) {
        String number = GlobalData.configData.getString("master.QQ.number");
        if (number.equals("")) {
            return "本插件还未设置骰主QQ号，请在本程序目录下的 config/indi.eiriksgata.rulateday-dice/config.json 文件中的 'master.QQ.number' 中进行设置，设置后才可以使用本功能";
        }
        if (data.getQqID() == Long.parseLong(number)) {
            DiceConfigService.diceConfigMapper.updateByBetaVersion(true);
            MyBatisUtil.getSqlSession().commit();
            return "已启用公开测试功能";
        } else {
            return "你不是骰主，无法启用公开测试功能";
        }
    }

    @InstructReflex(value = {".betaoff"}, priority = 3)
    public String betaVersionOff(MessageData<?> data) {
        String number = GlobalData.configData.getString("master.QQ.number");
        if (number.equals("")) {
            return "本插件还未设置骰主QQ号，请在本程序目录下的 config/indi.eiriksgata.rulateday-dice/config.json 文件中的 'master.QQ.number' 中进行设置，设置后才可以使用本功能";
        }
        if (data.getQqID() == Long.parseLong(number)) {
            DiceConfigService.diceConfigMapper.updateByBetaVersion(false);
            MyBatisUtil.getSqlSession().commit();
            return "已禁用公开测试功能";
        } else {
            return "你不是骰主，无法禁用公开测试功能";
        }
    }

    @InstructReflex(value = {".version"}, priority = 3)
    public String getProgramVersion(MessageData<?> data) {
        ResourceBundle config = ResourceBundle.getBundle("application");
        return "\n" + config.getString("version");
    }

    @InstructReflex(value = {".dismiss"}, priority = 3)
    public String dismissCurrentGroup(MessageData<?> data) {
        EventUtils.eventCallback(data.getEvent(), new EventAdapter() {
            @Override
            public void group(GroupMessageEvent event) {
                event.getGroup().sendMessage("正在退出群聊，请稍等。");

                if (event.getSender().getPermission().getLevel() == MemberPermission.ADMINISTRATOR.getLevel() ||
                        event.getSender().getPermission().getLevel() == MemberPermission.OWNER.getLevel()) {
                    event.getGroup().quit();
                } else {
                    event.getGroup().sendMessage("需要群主或者管理员权限，才能退出当前群聊。");
                }
            }
        });
        return null;
    }
}
