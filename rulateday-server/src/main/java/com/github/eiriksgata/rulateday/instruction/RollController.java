package com.github.eiriksgata.rulateday.instruction;

import com.alibaba.fastjson.JSONObject;
import com.github.eiriksgata.rulateday.event.EventUtils;
import com.github.eiriksgata.rulateday.service.impl.ApiReportImpl;

import com.github.eiriksgata.rulateday.event.EventAdapter;
import com.github.eiriksgata.rulateday.service.DiceConfigService;
import com.github.eiriksgata.rulateday.service.UserTempDataService;
import com.github.eiriksgata.rulateday.service.impl.UserTempDataServiceImpl;
import com.github.eiriksgata.rulateday.utlis.CharacterUtils;
import com.github.eiriksgata.rulateday.utlis.RestUtil;
import com.github.eiriksgata.trpg.dice.config.DiceConfig;
import com.github.eiriksgata.trpg.dice.exception.DiceInstructException;
import com.github.eiriksgata.trpg.dice.exception.ExceptionEnum;
import com.github.eiriksgata.trpg.dice.injection.InstructReflex;
import com.github.eiriksgata.trpg.dice.injection.InstructService;
import com.github.eiriksgata.trpg.dice.operation.DiceSet;
import com.github.eiriksgata.trpg.dice.operation.RollBasics;
import com.github.eiriksgata.trpg.dice.operation.RollRole;
import com.github.eiriksgata.trpg.dice.operation.impl.RollBasicsImpl;
import com.github.eiriksgata.trpg.dice.operation.impl.RollRoleImpl;
import com.github.eiriksgata.trpg.dice.reply.CustomText;
import com.github.eiriksgata.trpg.dice.utlis.RegularExpressionUtils;
import com.github.eiriksgata.trpg.dice.vo.MessageData;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.GroupTempMessageEvent;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.security.SecureRandom;
import java.util.List;

/**
 * author: create by Keith
 * version: v1.0
 * description: com.github.eiriksgata.dice
 * date:2020/9/24
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

    @InstructReflex(value = {"ra", "rc"}, priority = 2)
    public String attributeCheck(MessageData<?> data) {
        String attribute = userTempDataService.getUserAttribute(data.getQqID());
        data.setMessage(data.getMessage().trim());
        data.setMessage(CharacterUtils.operationSymbolProcessing(
                data.getMessage()
        ));
        if (attribute == null) {
            attribute = "";
        }
        String matcherResult = RegularExpressionUtils.getMatcher("^[0-9]+$", data.getMessage());
        if (matcherResult != null) {
            data.setMessage("未指定" + matcherResult);
        }
        try {
            return rollBasics.attributeCheck(data.getMessage(), attribute);
        } catch (DiceInstructException e) {

            //一般异常都是没有给定属性。 如果没有给定属性那就是按 100 计算。 相当于 1d100
            e.printStackTrace();
            return CustomText.getText("dice.attribute.error");
        }
    }

    @InstructReflex(value = {"st"})
    public String setAttribute(MessageData<?> data) {
        if (data.getMessage().equals("")) {
            return CustomText.getText("dice.set.attribute.error");
        }
        try {
            userTempDataService.updateUserAttribute(data.getQqID(), data.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return CustomText.getText("dice.set.attribute.error");
        }
        return CustomText.getText("dice.set.attribute.success");
    }

    @InstructReflex(value = {"r"})
    public String roll(MessageData<?> data) {
        Integer diceFace = userTempDataService.getUserDiceFace(data.getQqID());
        data.setMessage(CharacterUtils.operationSymbolProcessing(data.getMessage()));
        int repeatedlyValue = 1;
        StringBuilder resultText = new StringBuilder();
        if (diceFace != null) {
            diceSet.setDiceFace(data.getQqID(), diceFace);
        }
        String repeatedlyText = RegularExpressionUtils.getMatcher("^[1-9]?[0-9]+#", data.getMessage());
        if (repeatedlyText != null) {
            try {
                repeatedlyValue = Integer.parseInt(repeatedlyText.substring(0, repeatedlyText.length() - 1));
                if (repeatedlyValue < 1 || repeatedlyValue > 20) {
                    return CustomText.getText("dice.base.parameter.error");
                }
                data.setMessage(data.getMessage().substring(repeatedlyText.length()));
            } catch (Exception e) {
                return CustomText.getText("dice.base.parameter.error");
            }
        }

        for (int i = 0; i < repeatedlyValue; i++) {
            if (data.getMessage().equals("") || data.getMessage().equals(" ") ||
                    data.getMessage().equals("d") || data.getMessage().equals("D")) {
                resultText.append("\n").append(rollBasics.rollRandom("d", data.getQqID()));
            } else {
                //正则筛选
                String result = RegularExpressionUtils.getMatcher("(([0-9]{0,2}[dD]?[0-9]{0,5}[\\+\\-\\*\\/][0-9]{0,2}[dD]?[0-9]{0,5})+|[0-9]{0,2}[dD]?[0-9]{1,5})", data.getMessage());
                if (result != null) {
                    if (result.endsWith("+") || result.endsWith("-") || result.endsWith("*") || result.endsWith("/")) {
                        result = result.substring(0, result.length() - 1);
                    }
                    resultText.append("\n").append(rollBasics.rollRandom(result, data.getQqID()));
                } else {
                    return CustomText.getText("dice.base.parameter.error");
                }
            }
        }

        return resultText.toString();
    }


    @InstructReflex(value = {"MessageData", "set"})
    public String setDiceFace(MessageData<?> data) throws DiceInstructException {
        //移除所有的空格
        int setDiceFace;
        data.setMessage(data.getMessage().replaceAll(" ", ""));
        try {
            setDiceFace = Integer.parseInt(data.getMessage());
        } catch (Exception e) {
            return CustomText.getText("dice.set.face.error");
        }

        if (setDiceFace > Integer.parseInt(DiceConfig.diceSet.getString("dice.face.max"))) {
            throw new DiceInstructException(ExceptionEnum.DICE_SET_FACE_MAX_ERR);
        }
        if (setDiceFace <= Integer.parseInt(DiceConfig.diceSet.getString("dice.face.min"))) {
            throw new DiceInstructException(ExceptionEnum.DICE_SET_FACE_MIN_ERR);
        }
        diceSet.setDiceFace(data.getQqID(), setDiceFace);
        userTempDataService.updateUserDiceFace(data.getQqID(), setDiceFace);
        return CustomText.getText("dice.set.face.success", setDiceFace);
    }

    @InstructReflex(value = {"sc"})
    public String sanCheck(MessageData<?> data) {
        data.setMessage(CharacterUtils.operationSymbolProcessing(data.getMessage()));

        //检查指令前缀空格符
        for (int i = 0; i < data.getMessage().length(); i++) {
            if (data.getMessage().charAt(i) != ' ') {
                data.setMessage(data.getMessage().substring(i));
                break;
            }
        }

        //优先检测指令是否包含有数值
        if (data.getMessage().matches("^((([1-9]\\d|[1-9])?[dD]?([1-9]\\d\\d|[1-9]\\d|[1-9])\\+?){1,10}|0)/((([1-9]\\d|[1-9])?[dD]?([1-9]\\d\\d|[1-9]\\d|[1-9])\\+?){1,10}|0)\\s([1-9]\\d\\d|[1-9]\\d|\\d)$")) {
            //检测到包含数值 进行 空格符 分割 0为计算公式，1为给定的数值
            String[] tempArr = data.getMessage().split(" ");
            return rollBasics.sanCheck(tempArr[0], "san" + tempArr[1], (attribute, random, sanValue, calculationProcess, surplus) -> {
            });
        }

        //检测用户输入的指令格式是否正确
        if (data.getMessage().matches("^((([1-9]\\d|[1-9])?[dD]?([1-9]\\d\\d|[1-9]\\d|[1-9])\\+?){1,10}|0)/((([1-9]\\d|[1-9])?[dD]?([1-9]\\d\\d|[1-9]\\d|[1-9])\\+?){1,10}|0)$")) {
            //查询用户数据
            String attribute = userTempDataService.getUserAttribute(data.getQqID());

            //对于没有属性的用户 返回错误
            if (attribute == null) {
                return CustomText.getText("dice.sc.not-found.error");
            }

            String sanAttribute = RegularExpressionUtils.getMatcher("san\\d{1,3}", attribute);
            if (sanAttribute == null) {
                return CustomText.getText("dice.sc.not-found.error");
            }

            String inputData = RegularExpressionUtils.getMatcher("((\\d?[Dd]\\d+|[Dd]|\\d)\\+?)+/((\\d?[Dd]\\d+|[Dd]|\\d)\\+?)+", data.getMessage());


            //要进行是否有用户属性确认

            return rollBasics.sanCheck(inputData, attribute, (resultAttribute, random, sanValue, calculationProcess, surplus) -> {
                //修改属性
                userTempDataService.updateUserAttribute(data.getQqID(), resultAttribute);
            });

        }
        return CustomText.getText("dice.sc.instruct.error");
    }

    @InstructReflex(value = {"rh"}, priority = 3)
    public String rollHide(MessageData<?> data) {
        if (!DiceConfigService.diceConfigMapper.selectById().getPrivate_chat()) {
            return CustomText.getText("dice.roll.hide.private.chat.disable");
        }
        EventUtils.eventCallback(data.getEvent(), new EventAdapter() {
            @Override
            public void group(GroupMessageEvent event) {
                event.getSender().sendMessage(
                        roll(data)
                );
            }

            @Override
            public void friend(FriendMessageEvent event) {
                event.getFriend().sendMessage(roll(data));
            }

            @Override
            public void groupTemp(GroupTempMessageEvent event) {
                event.getSender().sendMessage(roll(data));
            }
        });
        return CustomText.getText("coc7.roll.hide");
    }

    @InstructReflex(value = {"rb"}, priority = 3)
    public String rollBonusDice(MessageData<?> data) {
        data.setMessage(data.getMessage().replaceAll(" ", ""));
        data.setMessage(CharacterUtils.operationSymbolProcessing(data.getMessage()));

        String attribute = userTempDataService.getUserAttribute(data.getQqID());
        return rollBasics.rollBonus(data.getMessage(), attribute, true);
    }

    @InstructReflex(value = {"rp", "Rp"}, priority = 3)
    public String rollPunishment(MessageData<?> data) {
        data.setMessage(data.getMessage().replaceAll(" ", ""));
        data.setMessage(CharacterUtils.operationSymbolProcessing(data.getMessage()));
        String attribute = userTempDataService.getUserAttribute(data.getQqID());
        return rollBasics.rollBonus(data.getMessage(), attribute, false);
    }

    @InstructReflex(value = {"coc", "Coc"})
    public String randomCocRole(MessageData<?> data) {
        int createNumber;
        createNumber = checkCreateRandomRoleNumber(data.getMessage());
        if (createNumber == -1) return CustomText.getText("dice.base.parameter.error");
        if (createNumber > 20 | createNumber < 1) {
            return CustomText.getText("coc7.role.create.size.max");
        }
        return rollRole.createCocRole(createNumber);
    }

    @InstructReflex(value = {"dnd", "Dnd", "DND"}, priority = 3)
    public String randomDndRole(MessageData<?> data) {
        int createNumber;
        createNumber = checkCreateRandomRoleNumber(data.getMessage());
        if (createNumber == -1) return CustomText.getText("dice.base.parameter.error");
        if (createNumber > 20 | createNumber < 1) {
            return CustomText.getText("dr5e.role.create.size.max");
        }
        return rollRole.createDndRole(createNumber);
    }

    @InstructReflex(value = {"dnd5e", "Dnd5e", "DND5e"}, priority = 4)
    public String randomDnd5eRole(MessageData<?> data) {
        return "\n" + rollRole.createDnd5eRole();
    }


    @InstructReflex(value = {"jrrp", "JRRP", "todayRandom"})
    public String todayRandom(MessageData<?> data) {
        return rollBasics.todayRandom(data.getQqID(), 8);
    }


    @InstructReflex(value = {"name"})
    public String randomName(MessageData<?> data) {
        JSONObject jsonObject = new JSONObject();
        String url = ApiReportImpl.apiUrl + "/openapi/v1/random/human/name";
        if (StringUtils.isNumeric(data.getMessage())) {
            int number;
            try {
                number = Integer.parseInt(data.getMessage());
            } catch (Exception e) {
                return CustomText.getText("dice.base.parameter.error");
            }

            if (number > 0 && number <= 20) {
                jsonObject.put("number", number);
                return CustomText.getText("names.result.title", JSONObject.parseObject(RestUtil.postForJson(url, jsonObject.toJSONString())).getString("data"));
            }
            return CustomText.getText("names.create.size.max");
        } else {
            jsonObject.put("number", 1);
            return CustomText.getText("names.result.title", JSONObject.parseObject(RestUtil.postForJson(url, jsonObject.toJSONString())).getString("data"));
        }
    }


    @InstructReflex(value = {"ga"}, priority = 2)
    public String attributeGetAttribute(MessageData<?> data) {
        return userTempDataService.getUserAttribute(data.getQqID());
    }

    @InstructReflex(value = {"en"})
    public String attributeEn(MessageData<?> data) {
        if (data.getMessage().equals("")) {
            return CustomText.getText("dice.en.parameter.null");
        }
        String checkAttribute = RegularExpressionUtils.getMatcher("[\\u4E00-\\u9FA5A-z]+\\d+", data.getMessage());
        if (checkAttribute == null) {
            checkAttribute = RegularExpressionUtils.getMatcher("[\\u4E00-\\u9FA5A-z]+", data.getMessage());
            if (checkAttribute == null) {
                return CustomText.getText("dice.en.parameter.format.error");
            }

            String userAttribute = userTempDataService.getUserAttribute(data.getQqID());
            if (userAttribute == null || userAttribute.equals("")) {
                return CustomText.getText("dice.en.not.set.attribute");
            }

            String tempData = RegularExpressionUtils.getMatcher(checkAttribute + "\\d+", userAttribute);
            if (tempData == null) {
                return CustomText.getText("dice.en.not.found.attribute", checkAttribute);
            }
            int checkNumber = Integer.parseInt(tempData.substring(checkAttribute.length()));

            int randomNumber = new SecureRandom().nextInt(101);
            if (randomNumber > checkNumber) {
                int addValue = new SecureRandom().nextInt(11);
                int count = checkNumber + addValue;
                String updateAttribute = userAttribute.replaceAll(tempData, checkAttribute + count);
                userTempDataService.updateUserAttribute(data.getQqID(), updateAttribute);
                return CustomText.getText("dice.en.success",
                        randomNumber, checkNumber, checkAttribute, checkAttribute, addValue, checkNumber, count);
            }
            return CustomText.getText("dice.en.fail", randomNumber, checkNumber, checkAttribute);
        }

        int randomNumber = new SecureRandom().nextInt(101);
        int checkNumber = Integer.parseInt(RegularExpressionUtils.getMatcher("\\d+", checkAttribute));
        if (randomNumber > checkNumber) {
            int addValue = new SecureRandom().nextInt(11);
            int count = addValue + checkNumber;
            return CustomText.getText("dice.en.success",
                    randomNumber, checkNumber, checkAttribute, checkAttribute, addValue, checkNumber, count);
        }
        return CustomText.getText("dice.en.fail", randomNumber, checkNumber, checkAttribute);
    }

    @InstructReflex(value = {"sa"})
    public String attributeSetAttribute(MessageData<?> data) {
        String changeValue = RegularExpressionUtils.getMatcher("\\d+", data.getMessage());
        if (changeValue == null) {
            return CustomText.getText("dice.sa.parameter.null");
        }
        String changeName = data.getMessage().substring(0, data.getMessage().length() - changeValue.length());
        if (changeName.equals("")) {
            return CustomText.getText("dice.sa.parameter.error");
        }

        //查询属性
        String findAttribute = userTempDataService.getUserAttribute(data.getQqID());
        if (findAttribute == null || findAttribute.equals("")) {
            return CustomText.getText("dice.sa.not.set.attribute");
        }

        String attribute = RegularExpressionUtils.getMatcher(changeName + "[0-9]+", findAttribute);
        if (attribute == null) {
            return CustomText.getText("dice.sa.not.found.attribute");
        }
        String updateData = findAttribute.replaceAll(attribute, changeName + changeValue);
        userTempDataService.updateUserAttribute(data.getQqID(), updateData);
        return CustomText.getText("dice.sa.update.success", attribute, changeName, changeValue);
    }

    @InstructReflex(value = {"ww", "dp"})
    public static String dicePoolGen(MessageData<?> data) {
        data.setMessage(data.getMessage().toLowerCase());
        data.setMessage(data.getMessage().trim());
        int diceNumber = 1;
        int addDiceCheck = 10;
        StringBuilder resultText = new StringBuilder();
        StringBuilder returnText = new StringBuilder();
        int count = 0;
        int successDiceCheck = 8;
        int diceFace = 10;
        if (data.getMessage().equals("") || data.getMessage() == null) {
            return CustomText.getText("dice.pool.parameter.format.error");
        }
        int repeat = 1;
        int index = data.getMessage().indexOf("#");
        if (index != -1) {
            try {
                repeat = Integer.parseInt(data.getMessage().substring(0, index));
            } catch (Exception e) {
                return CustomText.getText("dice.pool.parameter.format.error");
            }
        }


        List<String> parametersList = RegularExpressionUtils.getMatchers("[0-9]+|a[0-9]+|k[0-9]+|m[0-9]+|\\+[0-9]+|b[0-9]+", data.getMessage());
        if (parametersList.size() <= 0) {
            return CustomText.getText("dice.pool.parameter.format.error");
        }
        try {
            diceNumber = Integer.parseInt(parametersList.get(0));
            if (diceNumber < 0 || diceNumber > 300) {
                return CustomText.getText("dice.pool.parameter.range.error");
            }
            parametersList.remove(0);
        } catch (Exception e) {
            return CustomText.getText("dice.pool.parameter.format.error");
        }
        for (String parameter : parametersList) {
            switch (parameter.charAt(0)) {
                case 'a':
                    addDiceCheck = Integer.parseInt(parameter.substring(1));
                    break;
                case 'k':
                    successDiceCheck = Integer.parseInt(parameter.substring(1));
                    break;
                case 'm':
                    diceFace = Integer.parseInt(parameter.substring(1));
                    break;
                case '+':
                case 'b':
                    count += Integer.parseInt(parameter.substring(1));
                    break;
            }
        }
        returnText.append(diceNumber);
        returnText.append("a").append(addDiceCheck);
        returnText.append("k").append(successDiceCheck);
        returnText.append("m").append(diceFace);
        returnText.append("b").append(count);
        for (int i = 1; i < repeat; i++) {
            rollBasics.dicePoolCount(diceNumber, resultText, count, addDiceCheck, count, diceFace, successDiceCheck);
            EventUtils.eventCallback(data.getEvent(), new EventAdapter() {
                @Override
                public void group(GroupMessageEvent event) {
                    String senderName = event.getSender().getNameCard();
                    if (senderName.trim().equals("")) {
                        senderName = event.getSender().getNick();
                    }
                    event.getGroup().sendMessage(
                            "[" + senderName + "]" +
                                    CustomText.getText("dice.pool.result", returnText, resultText)
                    );
                }

                @Override
                public void friend(FriendMessageEvent event) {
                    event.getFriend().sendMessage(
                            CustomText.getText("dice.pool.result", returnText, resultText));
                }

                @Override
                public void groupTemp(GroupTempMessageEvent event) {
                    event.getGroup().sendMessage(
                            CustomText.getText("dice.pool.result", returnText, resultText));
                }
            });
            resultText.delete(0, resultText.length());
        }
        rollBasics.dicePoolCount(diceNumber, resultText, count, addDiceCheck, count, diceFace, successDiceCheck);
        return CustomText.getText("dice.pool.result", returnText, resultText);
    }

    @InstructReflex(value = {"rl"}, priority = 7)
    public static String LCDSV1Check1(MessageData<?> data) {
        List<String> result = RegularExpressionUtils.getMatchers("\\d+", data.getMessage());
        int dice1 = new SecureRandom().nextInt(10) + 1;
        int dice2 = new SecureRandom().nextInt(10) + 1;
        int skillNumber = Integer.parseInt(result.get(0));
        if (skillNumber > 200) {
            skillNumber = 200;
        }
        double formulaResult = 0;
        String formulaText = "";
        StringBuilder outText = new StringBuilder();
        if (result.size() > 0) {
            if (skillNumber >= 0 && skillNumber <= 30) {
                formulaText = "0.3x" + skillNumber + "+0.6x" + dice1 + "x" + dice2;
                formulaResult = (0.3 * skillNumber) + (0.6 * dice1 * dice2);
            }
            if (skillNumber >= 31 && skillNumber <= 50) {
                formulaText = "0.3x" + skillNumber + "+0.8x" + dice1 + "x" + dice2;
                formulaResult = (0.3 * skillNumber) + (0.8 * dice1 * dice2);
            }
            if (skillNumber >= 51 && skillNumber <= 100) {
                formulaText = "0.5x" + skillNumber + "+1.2x" + dice1 + "x" + dice2;
                formulaResult = (0.5 * skillNumber) + (1.2 * dice1 * dice2);
            }
            if (skillNumber >= 101 && skillNumber <= 150) {
                formulaText = "0.7x" + skillNumber + "+" + dice1 + "x" + dice2;
                formulaResult = (0.7 * skillNumber) + (dice1 * dice2);
            }

            if (skillNumber >= 151) {
                formulaText = "140+(" + skillNumber + "-150)x0.4+0.5x" + dice1 + "x" + dice2;
                formulaResult = 140 + ((skillNumber - 150) * 0.4) + (0.5 * dice1 * dice2);
            }

        } else {
            return "无计算参数,指令格式:.rl <技能数值> <DC难度> ，即:.rl 40 50";
        }
        if (formulaResult > 200) {
            outText.append(formulaText).append("=").append(String.format("%.2f", formulaResult)).append("=>").append("200");
            formulaResult = 200;
        } else {
            outText.append(formulaText).append("=").append(String.format("%.2f", formulaResult));
        }
        if (result.size() > 1) {
            int DCValue = Integer.parseInt(result.get(1));
            if (DCValue > formulaResult) {
                outText.append("<").append(DCValue).append("\n").append("检定失败!");
            } else {
                outText.append(">=").append(DCValue).append("\n").append("检定成功!");
            }
        }
        return outText.toString();
    }

    @InstructReflex(value = {"rlo", "RL"}, priority = 8)
    public static String LCDSV1Check2(MessageData<?> data) {
        List<String> result = RegularExpressionUtils.getMatchers("\\d+", data.getMessage());
        int dice = new SecureRandom().nextInt(101) + 1;
        int skillNumber = Integer.parseInt(result.get(0));
        if (skillNumber > 200) {
            skillNumber = 200;
        }
        double formulaResult = 0;
        String formulaText = "";
        StringBuilder outText = new StringBuilder();
        if (result.size() > 0) {
            if (skillNumber >= 0 && skillNumber <= 30) {
                formulaText = "0.3x" + skillNumber + "+0.6x" + dice;
                formulaResult = (0.3 * skillNumber) + (0.6 * dice);
            }
            if (skillNumber >= 31 && skillNumber <= 50) {
                formulaText = "0.3x" + skillNumber + "+0.8x" + dice;
                formulaResult = (0.3 * skillNumber) + (0.8 * dice);
            }
            if (skillNumber >= 51 && skillNumber <= 100) {
                formulaText = "0.5x" + skillNumber + "+1.2x" + dice;
                formulaResult = (0.5 * skillNumber) + (1.2 * dice);
            }
            if (skillNumber >= 101 && skillNumber <= 150) {
                formulaText = "0.7x" + skillNumber + "+" + dice;
                formulaResult = (0.7 * skillNumber) + (dice);
            }

            if (skillNumber >= 151) {
                formulaText = "140+(" + skillNumber + "-150)x0.4+0.5x" + dice;
                formulaResult = 140 + ((skillNumber - 150) * 0.4) + (0.5 * dice);
            }

        } else {
            return "无计算参数,指令格式:.rl <技能数值> <DC难度> ，即:.rl 40 50";
        }
        if (formulaResult > 200) {
            outText.append(formulaText).append("=").append(String.format("%.2f", formulaResult)).append("=>").append("200");
            formulaResult = 200;
        } else {
            outText.append(formulaText).append("=").append(String.format("%.2f", formulaResult));
        }
        if (result.size() > 1) {
            int DCValue = Integer.parseInt(result.get(1));
            if (DCValue > formulaResult) {
                outText.append("<").append(DCValue).append("\n").append("检定失败!");
            } else {
                outText.append(">=").append(DCValue).append("\n").append("检定成功!");
            }
        }
        return outText.toString();
    }

    private int checkCreateRandomRoleNumber(String message) {
        if (message.equals("")) {
            return 1;
        } else {
            try {
                return Integer.parseInt(message);
            } catch (Exception e) {
                return -1;
            }
        }
    }


}
