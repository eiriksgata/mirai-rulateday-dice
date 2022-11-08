package indi.eiriksgata.rulateday.instruction;

import indi.eiriksgata.dice.injection.InstructReflex;
import indi.eiriksgata.dice.injection.InstructService;
import indi.eiriksgata.dice.reply.CustomText;
import indi.eiriksgata.dice.vo.MessageData;
import indi.eiriksgata.rulateday.event.EventAdapter;
import indi.eiriksgata.rulateday.event.EventUtils;
import indi.eiriksgata.rulateday.trpggame.GameData;
import indi.eiriksgata.rulateday.trpggame.TrpgGameServiceImpl;
import indi.eiriksgata.rulateday.trpggame.utils.PlayerRoleAttributeSetUtil;
import net.mamoe.mirai.event.events.GroupMessageEvent;


@InstructService
public class TrpgGameController {

    @InstructReflex(value = {"trpg-list"}, priority = 3)
    public String trpgGameList(MessageData<?> data) {
        return TrpgGameServiceImpl.getAllTrpgModelFiles();
    }

    @InstructReflex(value = {"trpg-role-get"}, priority = 3)
    public String trpgRoleDataGet(MessageData<?> data) {
        return PlayerRoleAttributeSetUtil.roleDataShow(data.getQqID());
    }

    @InstructReflex(value = {"trpg-role-set"}, priority = 3)
    public String trpgRoleDataSet(MessageData<?> data) {
        String[] inputText = data.getMessage().split(",");
        return CustomText.getText("trpg.role.card.set.title") + "\n1. " + PlayerRoleAttributeSetUtil.nameCheck(data.getQqID(), inputText[0]) + "\n2. " + PlayerRoleAttributeSetUtil.skillCheck(data.getQqID(), inputText[1]) + "\n3. " + PlayerRoleAttributeSetUtil.attributeCheck(data.getQqID(), inputText[2]);
    }

    @InstructReflex(value = {"trpg-reload"}, priority = 3)
    public String trpgGameLoad(MessageData<?> data) {
        //TODO: 检测玩家数据是否满足要求
        if (GameData.playerRoleSaveDataMap.get(data.getQqID()) == null || GameData.playerRoleSaveDataMap.get(data.getQqID()).getName() == null || GameData.playerRoleSaveDataMap.get(data.getQqID()).getAttribute() == null || GameData.playerRoleSaveDataMap.get(data.getQqID()).getSkill() == null || GameData.playerRoleSaveDataMap.get(data.getQqID()).getName().equals("") || GameData.playerRoleSaveDataMap.get(data.getQqID()).getAttribute().equals("") || GameData.playerRoleSaveDataMap.get(data.getQqID()).getSkill().equals("")) {
            return CustomText.getText("trpg.reload.not.found.role.attribute");
        }
        EventUtils.eventCallback(data.getEvent(), new EventAdapter() {
            @Override
            public void group(GroupMessageEvent event) {
                event.getGroup().sendMessage(CustomText.getText("trpg.reload.start.help"));
                event.getGroup().sendMessage(CustomText.getText("trpg.reload.start.waring"));
                event.getGroup().sendMessage(TrpgGameServiceImpl.loadScriptData(data.getQqID(), data.getMessage()));
            }
        });
        return TrpgGameServiceImpl.loadEventText(data.getQqID());
    }


    @InstructReflex(value = {"trpg-quit"}, priority = 3)
    public String quitGameModel(MessageData<?> data) {
        TrpgGameServiceImpl.playerQuitGame(data.getQqID());
        return CustomText.getText("trpg.game.quit");
    }


    @InstructReflex(value = {"trpg-option-"}, priority = 3)
    public String trpgOptionSelect(MessageData<?> data) {
        if (GameData.TrpgGamePlayerList.get(data.getQqID()) == null || !GameData.TrpgGamePlayerList.get(data.getQqID())) {
            return CustomText.getText("trpg.option.no.found.staring.model");
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
