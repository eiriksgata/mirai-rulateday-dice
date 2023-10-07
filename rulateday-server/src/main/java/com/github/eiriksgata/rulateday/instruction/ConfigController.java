package com.github.eiriksgata.rulateday.instruction;

import com.github.eiriksgata.rulateday.config.GlobalData;
import com.github.eiriksgata.trpg.dice.injection.InstructReflex;
import com.github.eiriksgata.trpg.dice.injection.InstructService;
import com.github.eiriksgata.trpg.dice.reply.CustomText;
import com.github.eiriksgata.trpg.dice.vo.MessageData;
import com.github.eiriksgata.rulateday.service.DiceConfigService;
import com.github.eiriksgata.rulateday.utlis.MyBatisUtil;

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


    @InstructReflex(value = {"kkpset1"}, priority = 4)
    public String pictureRandomSourceSet1(MessageData<?> data) {
        GlobalData.randomPictureApiType = 1;
        return "切换图片解析线路1(默认)";
    }

    @InstructReflex(value = {"kkpset2"}, priority = 4)
    public String pictureRandomSourceSet2(MessageData<?> data) {
        GlobalData.randomPictureApiType = 2;
        return "切换图片解析线路2";
    }

    @InstructReflex(value = {"kkpset3"}, priority = 4)
    public String pictureRandomSourceSet3(MessageData<?> data) {
        GlobalData.randomPictureApiType = 3;
        return "切换图片解析线路3";
    }

    @InstructReflex(value = {"kkpset4"}, priority = 4)
    public String pictureRandomSourceSet4(MessageData<?> data) {
        GlobalData.randomPictureApiType = 4;
        return "切换图片解析线路4";
    }

    @InstructReflex(value = {"kkpset5"}, priority = 4)
    public String pictureRandomSourceSet5(MessageData<?> data) {
        GlobalData.randomPictureApiType = 5;
        return "切换图片解析线路5";
    }

    @InstructReflex(value = {"kkpset6"}, priority = 4)
    public String pictureRandomSourceSet6(MessageData<?> data) {
        GlobalData.randomPictureApiType = 6;
        return "切换图片解析线路6";
    }
}
