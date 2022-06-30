package indi.eiriksgata.rulateday.instruction;

import indi.eiriksgata.dice.injection.InstructReflex;
import indi.eiriksgata.dice.injection.InstructService;
import indi.eiriksgata.dice.vo.MessageData;
import indi.eiriksgata.rulateday.event.EventAdapter;
import indi.eiriksgata.rulateday.event.EventUtils;
import indi.eiriksgata.rulateday.trpggame.GameData;
import indi.eiriksgata.rulateday.trpggame.TrpgGameServiceImpl;
import indi.eiriksgata.rulateday.trpggame.utils.PlayerRoleAttributeSetUtil;
import net.mamoe.mirai.event.events.GroupMessageEvent;


@InstructService
public class GalGameController {

    @InstructReflex(value = {".trpg-list"}, priority = 3)
    public String trpgGameList(MessageData<?> data) {
        return TrpgGameServiceImpl.getAllTrpgModelFiles();
    }

    @InstructReflex(value = {".trpg-role-get"}, priority = 3)
    public String trpgRoleDataGet(MessageData<?> data) {
        return PlayerRoleAttributeSetUtil.roleDataShow(data.getQqID());
    }

    @InstructReflex(value = {".trpg-role-set"}, priority = 3)
    public String trpgRoleDataSet(MessageData<?> data) {
        String[] inputText = data.getMessage().split(",");
        return "你的角色卡设置结果:\n1. " + PlayerRoleAttributeSetUtil.nameCheck(data.getQqID(), inputText[0]) +
                "\n2. " +
                PlayerRoleAttributeSetUtil.skillCheck(data.getQqID(), inputText[1]) +
                "\n3. " +
                PlayerRoleAttributeSetUtil.attributeCheck(data.getQqID(), inputText[2]);
    }

    @InstructReflex(value = {".trpg-load"}, priority = 3)
    public String trpgGameLoad(MessageData<?> data) {
        EventUtils.eventCallback(data.getEvent(), new EventAdapter() {
            @Override
            public void group(GroupMessageEvent event) {
                event.getGroup().sendMessage("在游戏游玩之前，请先确认你已经设置好自己的角色属性，以免出现错误。如果你尚未清楚如何进行游戏，请先阅读相关的文档，了解完毕后再进行游戏。需要注意的是trpg指令是单独用你个人作为单位区分开，而不是以群作为分开。因此相关指令甚至可以跨群使用。");
                event.getGroup().sendMessage("由于数据都是存储在内存之中，如果系统离线后，之前的数据将会丢失。此外如果不需要游玩的本模组，请使用指令[.trpg-quit]释放空间。");
                event.getGroup().sendMessage(TrpgGameServiceImpl.loadScriptData(data.getQqID(), data.getMessage()));
            }
        });
        return TrpgGameServiceImpl.loadEventText(data.getQqID());
    }


    @InstructReflex(value = {".trpg-quit"}, priority = 3)
    public String quitGameModel(MessageData<?> data) {
        TrpgGameServiceImpl.playerQuitGame(data.getQqID());
        return "已退出TRPG游戏模式，并释放空间。";
    }


    @InstructReflex(value = {".trpg-option-"}, priority = 3)
    public String trpgOptionSelect(MessageData<?> data) {
        if (GameData.TrpgGamePlayerList.get(data.getQqID()) == null || !GameData.TrpgGamePlayerList.get(data.getQqID())) {
            return "尚未启动任何TRPG游戏模组，请先输入相关指令进行游戏";
        } else {
            //TODO: 进行选项的动作
            EventUtils.eventCallback(data.getEvent(), new EventAdapter() {
                @Override
                public void group(GroupMessageEvent event) {
                    event.getGroup().sendMessage(TrpgGameServiceImpl.optionSelect(data.getQqID(), data.getMessage()));
                }
            });
            return TrpgGameServiceImpl.loadEventText(data.getQqID());
        }
    }


}
