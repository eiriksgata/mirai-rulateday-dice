package indi.eiriksgata.rulateday.instruction;

import indi.eiriksgata.dice.operation.RollBasics;
import indi.eiriksgata.dice.operation.RollRole;
import indi.eiriksgata.dice.operation.impl.RollRoleImpl;
import indi.eiriksgata.dice.utlis.RegularExpressionUtils;
import indi.eiriksgata.dice.vo.MessageData;
import indi.eiriksgata.dice.config.DiceConfig;
import indi.eiriksgata.dice.exception.DiceInstructException;
import indi.eiriksgata.dice.exception.ExceptionEnum;
import indi.eiriksgata.dice.injection.InstructService;
import indi.eiriksgata.dice.injection.InstructReflex;
import indi.eiriksgata.dice.operation.DiceSet;
import indi.eiriksgata.dice.operation.impl.RollBasicsImpl;
import indi.eiriksgata.dice.reply.CustomText;
import indi.eiriksgata.rulateday.pojo.QueryDataBase;
import indi.eiriksgata.rulateday.service.UserTempDataService;
import indi.eiriksgata.rulateday.service.impl.UserTempDataServiceImpl;
import net.mamoe.mirai.Bot;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: create by Keith
 * @version: v1.0
 * @description: indi.eiriksgata.dice
 * @date:2020/9/24
 **/

@InstructService
public class RollController {

    @Resource
    public static final UserTempDataService userTempDataService = new UserTempDataServiceImpl();

    @Resource
    public static final RollBasics rollBasics = new RollBasicsImpl();

    @Resource
    public static final DiceSet diceSet = new DiceSet();

    @Resource
    public static final RollRole rollRole = new RollRoleImpl();

    @InstructReflex(value = {".ra", ".rc", "。ra", "。rc"}, priority = 2)
    public String attributeCheck(MessageData data) {
        String attribute = userTempDataService.getUserAttribute(data.getQqID());
        data.setMessage(data.getMessage().replaceAll(" ", ""));
        if (attribute == null) {
            attribute = "";
        }
        try {
            return rollBasics.attributeCheck(data.getMessage(), attribute);
        } catch (DiceInstructException e) {
            e.printStackTrace();
            return CustomText.getText("dice.attribute.error");
        }
    }

    @InstructReflex(value = {".st", "。st"})
    public String setAttribute(MessageData data) {
        try {
            userTempDataService.updateUserAttribute(data.getQqID(), data.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return CustomText.getText("dice.set.attribute.error");
        }
        return CustomText.getText("dice.set.attribute.success");
    }

    @InstructReflex(value = {".r", "。r"})
    public String roll(MessageData data) {
        Integer diceFace = userTempDataService.getUserDiceFace(data.getQqID());
        if (diceFace != null) {
            diceSet.setDiceFace(data.getQqID(), diceFace);
        }
        if (data.getMessage().equals("") || data.getMessage().equals(" ")) {
            data.setMessage("d");
        }
        return rollBasics.rollRandom(data.getMessage(), data.getQqID());
    }


    @InstructReflex(value = {".MessageData", ".set", "。set"})
    public String setDiceFace(MessageData data) throws DiceInstructException {
        //移除所有的空格
        data.setMessage(data.getMessage().replaceAll(" ", ""));

        int setDiceFace = Integer.valueOf(data.getMessage());
        if (setDiceFace > Integer.valueOf(DiceConfig.diceSet.getString("dice.face.max"))) {
            throw new DiceInstructException(ExceptionEnum.DICE_SET_FACE_MAX_ERR);
        }
        if (setDiceFace <= Integer.valueOf(DiceConfig.diceSet.getString("dice.face.min"))) {
            throw new DiceInstructException(ExceptionEnum.DICE_SET_FACE_MIN_ERR);
        }
        diceSet.setDiceFace(data.getQqID(), setDiceFace);
        userTempDataService.updateUserDiceFace(data.getQqID(), setDiceFace);
        return CustomText.getText("dice.set.face.success", setDiceFace);
    }

    @InstructReflex(value = {".sc", "。sc"})
    public String sanCheck(MessageData data) {
        //检查指令前缀空格符
        for (int i = 0; i < data.getMessage().length(); i++) {
            if (data.getMessage().charAt(i) != ' ') {
                data.setMessage(data.getMessage().substring(i));
                break;
            }
        }

        //优先检测指令是否包含有数值
        if (data.getMessage().matches("(([0-9]?[Dd][0-9]+|[Dd]|[0-9])\\+?)+/(([0-9]?[Dd][0-9]+|[Dd]|[0-9])\\+?)+ [0-9]+")) {
            //检测到包含数值 进行 空格符 分割 0为计算公式，1为给定的数值
            String[] tempArr = data.getMessage().split(" ");
            return rollBasics.sanCheck(tempArr[0], "san" + tempArr[1], (attribute, random, sanValue, calculationProcess, surplus) -> {
            });
        }

        //检测用户输入的指令格式是否正确
        if (data.getMessage().matches("(([0-9]?[Dd][0-9]+|[Dd]|[0-9])\\+?)+/(([0-9]?[Dd][0-9]+|[Dd]|[0-9])\\+?)+")) {
            //查询用户数据
            String attribute = userTempDataService.getUserAttribute(data.getQqID());
            String inputData = RegularExpressionUtils.getMatcher("(([0-9]?[Dd][0-9]+|[Dd]|[0-9])\\+?)+/(([0-9]?[Dd][0-9]+|[Dd]|[0-9])\\+?)+", data.getMessage());
            //要进行是否有用户属性确认
            //对于没有属性的用户 返回错误
            if (attribute == null) {
                return CustomText.getText("dice.sc.not-found.error");
            }

            return rollBasics.sanCheck(inputData, attribute, (resultAttribute, random, sanValue, calculationProcess, surplus) -> {
                //修改属性
                userTempDataService.updateUserAttribute(data.getQqID(), resultAttribute);
            });

        }
        return CustomText.getText("dice.sc.instruct.error");
    }

    @InstructReflex(value = {".rh", "。rh"}, priority = 3)
    public String rollHide(MessageData data) {
        Bot.getInstances().get(0).getFriend(data.getQqID()).sendMessage(roll(data));
        return CustomText.getText("coc7.roll.hide");
    }

    @InstructReflex(value = {".rb", "。rb", ",rb"}, priority = 3)
    public String rollBonusDice(MessageData data) {
        data.setMessage(data.getMessage().replaceAll(" ", ""));
        String attribute = userTempDataService.getUserAttribute(data.getQqID());
        return rollBasics.rollBonus(data.getMessage(), attribute, true);
    }

    @InstructReflex(value = {".rp", "。rp", ",rp"}, priority = 3)
    public String rollPunishment(MessageData data) {
        data.setMessage(data.getMessage().replaceAll(" ", ""));
        String attribute = userTempDataService.getUserAttribute(data.getQqID());
        return rollBasics.rollBonus(data.getMessage(), attribute, false);
    }

    @InstructReflex(value = {".coc", "。coc"})
    public String randomCocRole(MessageData data) {
        return rollRole.createCocRole(Integer.valueOf(data.getMessage()));
    }

    @InstructReflex(value = {".dnd", "。dnd"})
    public String randomDndRole(MessageData data) {
        return rollRole.createDndRole(Integer.valueOf(data.getMessage()));
    }


}
