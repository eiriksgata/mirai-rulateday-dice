package com.github.eiriksgata.rulateday.instruction;

import com.github.eiriksgata.rulateday.config.GlobalData;
import com.github.eiriksgata.rulateday.dto.DiceMessageDTO;
import com.github.eiriksgata.trpg.dice.injection.InstructReflex;
import com.github.eiriksgata.trpg.dice.injection.InstructService;
import com.github.eiriksgata.trpg.dice.reply.CustomText;

import com.github.eiriksgata.rulateday.service.DiceConfigService;
import com.github.eiriksgata.rulateday.utlis.MyBatisUtil;

import java.util.ResourceBundle;

@InstructService
public class ConfigController {

    @InstructReflex(value = {"pcon"}, priority = 3)
    public String privateChatOn(DiceMessageDTO data) {
        //启动私聊
        String number = GlobalData.configData.getString("master.QQ.number");
        if (number.equals("")) {
            return CustomText.getText("dice.master.number.no.set");
        }
        if (data.getId() == Long.parseLong(number)) {
            DiceConfigService.diceConfigMapper.updateByPrivateChat(true);
            MyBatisUtil.getSqlSession().commit();
            return CustomText.getText("dice.private.chat.enable");
        } else {
            return CustomText.getText("dice.private.chat.no.permission");
        }
    }

    @InstructReflex(value = {"pcoff"}, priority = 3)
    public String privateChatOff(DiceMessageDTO data) {
        String number = GlobalData.configData.getString("master.QQ.number");
        if (number.equals("")) {
            return CustomText.getText("dice.master.number.no.set");
        }
        if (data.getId() == Long.parseLong(number)) {
            DiceConfigService.diceConfigMapper.updateByPrivateChat(false);
            MyBatisUtil.getSqlSession().commit();
            return CustomText.getText("dice.private.chat.disable");
        } else {
            return CustomText.getText("dice.private.chat.no.permission");
        }
    }

    @InstructReflex(value = {"betaon"}, priority = 3)
    public String betaVersionOn(DiceMessageDTO data) {
        String number = GlobalData.configData.getString("master.QQ.number");
        if (number == null || number.equals("")) {
            return CustomText.getText("dice.master.number.no.set");
        }
        if (data.getId() == Long.parseLong(number)) {
            DiceConfigService.diceConfigMapper.updateByBetaVersion(true);
            MyBatisUtil.getSqlSession().commit();
            return CustomText.getText("dice.beta.enable");
        } else {
            return CustomText.getText("dice.beta.no.permission");
        }
    }

    @InstructReflex(value = {"betaoff"}, priority = 3)
    public String betaVersionOff(DiceMessageDTO data) {
        String number = GlobalData.configData.getString("master.QQ.number");
        if (number.equals("")) {
            return CustomText.getText("dice.master.number.no.set");
        }
        if (data.getId() == Long.parseLong(number)) {
            DiceConfigService.diceConfigMapper.updateByBetaVersion(false);
            MyBatisUtil.getSqlSession().commit();
            return CustomText.getText("dice.beta.disable");
        } else {
            return CustomText.getText("dice.beta.no.permission");
        }
    }

    @InstructReflex(value = {"version"}, priority = 3)
    public String getProgramVersion(DiceMessageDTO data) {
        ResourceBundle config = ResourceBundle.getBundle("application");
        return "\n" + config.getString("version");
    }


    @InstructReflex(value = {"kkpset1"}, priority = 4)
    public String pictureRandomSourceSet1(DiceMessageDTO data) {
        GlobalData.randomPictureApiType = 1;
        return "切换图片解析线路1(默认)";
    }

    @InstructReflex(value = {"kkpset2"}, priority = 4)
    public String pictureRandomSourceSet2(DiceMessageDTO data) {
        GlobalData.randomPictureApiType = 2;
        return "切换图片解析线路2";
    }

    @InstructReflex(value = {"kkpset3"}, priority = 4)
    public String pictureRandomSourceSet3(DiceMessageDTO data) {
        GlobalData.randomPictureApiType = 3;
        return "切换图片解析线路3";
    }

    @InstructReflex(value = {"kkpset4"}, priority = 4)
    public String pictureRandomSourceSet4(DiceMessageDTO data) {
        GlobalData.randomPictureApiType = 4;
        return "切换图片解析线路4";
    }

    @InstructReflex(value = {"kkpset5"}, priority = 4)
    public String pictureRandomSourceSet5(DiceMessageDTO data) {
        GlobalData.randomPictureApiType = 5;
        return "切换图片解析线路5";
    }

    @InstructReflex(value = {"kkpset6"}, priority = 4)
    public String pictureRandomSourceSet6(DiceMessageDTO data) {
        GlobalData.randomPictureApiType = 6;
        return "切换图片解析线路6";
    }
}
