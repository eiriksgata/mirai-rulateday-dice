package com.github.eiriksgata.rulateday.instruction;

import com.github.eiriksgata.rulateday.dto.DiceMessageDTO;
import com.github.eiriksgata.rulateday.event.EventUtils;
import com.github.eiriksgata.rulateday.trpggame.GameData;
import com.github.eiriksgata.trpg.dice.injection.InstructReflex;
import com.github.eiriksgata.trpg.dice.injection.InstructService;
import com.github.eiriksgata.trpg.dice.reply.CustomText;

import com.github.eiriksgata.rulateday.event.EventAdapter;
import com.github.eiriksgata.rulateday.trpggame.TrpgGameServiceImpl;
import com.github.eiriksgata.rulateday.trpggame.utils.PlayerRoleAttributeSetUtil;
import net.mamoe.mirai.event.events.GroupMessageEvent;


@InstructService
public class TrpgGameController {

    @InstructReflex(value = {"trpg-list"}, priority = 3)
    public String trpgGameList(DiceMessageDTO data) {
        return TrpgGameServiceImpl.getAllTrpgModelFiles();
    }

    @InstructReflex(value = {"trpg-role-get"}, priority = 3)
    public String trpgRoleDataGet(DiceMessageDTO data) {
        return PlayerRoleAttributeSetUtil.roleDataShow(data.getId());
    }

    @InstructReflex(value = {"trpg-role-set"}, priority = 3)
    public String trpgRoleDataSet(DiceMessageDTO data) {
        String[] inputText = data.getBody().split(",");
        return CustomText.getText("trpg.role.card.set.title") + "\n1. " + PlayerRoleAttributeSetUtil.nameCheck(data.getId(), inputText[0]) + "\n2. " + PlayerRoleAttributeSetUtil.skillCheck(data.getId(), inputText[1]) + "\n3. " + PlayerRoleAttributeSetUtil.attributeCheck(data.getId(), inputText[2]);
    }

    @InstructReflex(value = {"trpg-reload"}, priority = 3)
    public String trpgGameLoad(DiceMessageDTO data) {
        //TODO: 检测玩家数据是否满足要求
        if (GameData.playerRoleSaveDataMap.get(data.getId()) == null || GameData.playerRoleSaveDataMap.get(data.getId()).getName() == null || GameData.playerRoleSaveDataMap.get(data.getId()).getAttribute() == null || GameData.playerRoleSaveDataMap.get(data.getId()).getSkill() == null || GameData.playerRoleSaveDataMap.get(data.getId()).getName().equals("") || GameData.playerRoleSaveDataMap.get(data.getId()).getAttribute().equals("") || GameData.playerRoleSaveDataMap.get(data.getId()).getSkill().equals("")) {
            return CustomText.getText("trpg.reload.not.found.role.attribute");
        }
        EventUtils.eventCallback(data.getEvent(), new EventAdapter() {
            @Override
            public void group(GroupMessageEvent event) {
                event.getGroup().sendMessage(CustomText.getText("trpg.reload.start.help"));
                event.getGroup().sendMessage(CustomText.getText("trpg.reload.start.waring"));
                event.getGroup().sendMessage(TrpgGameServiceImpl.loadScriptData(data.getId(), data.getBody()));
            }
        });
        return TrpgGameServiceImpl.loadEventText(data.getId());
    }


    @InstructReflex(value = {"trpg-quit"}, priority = 3)
    public String quitGameModel(DiceMessageDTO data) {
        TrpgGameServiceImpl.playerQuitGame(data.getId());
        return CustomText.getText("trpg.game.quit");
    }


    @InstructReflex(value = {"trpg-option-"}, priority = 3)
    public String trpgOptionSelect(DiceMessageDTO data) {
        if (GameData.TrpgGamePlayerList.get(data.getId()) == null || !GameData.TrpgGamePlayerList.get(data.getId())) {
            return CustomText.getText("trpg.option.no.found.staring.model");
        } else {
            //TODO: 进行选项的动作
            EventUtils.eventCallback(data.getEvent(), new EventAdapter() {
                @Override
                public void group(GroupMessageEvent event) {
                    event.getGroup().sendMessage(TrpgGameServiceImpl.optionSelect(data.getId(), data.getBody()));
                }
            });
            return TrpgGameServiceImpl.loadEventText(data.getId());
        }
    }


}
