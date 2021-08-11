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
import indi.eiriksgata.rulateday.event.EventAdapter;
import indi.eiriksgata.rulateday.event.EventUtils;
import indi.eiriksgata.rulateday.service.HumanNameService;
import indi.eiriksgata.rulateday.service.UserTempDataService;
import indi.eiriksgata.rulateday.service.impl.HumanNameServiceImpl;
import indi.eiriksgata.rulateday.service.impl.UserTempDataServiceImpl;
import indi.eiriksgata.rulateday.utlis.CharacterUtils;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.GroupTempMessageEvent;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.util.Random;

/**
 *  author: create by Keith
 *  version: v1.0
 *  description: indi.eiriksgata.dice
 *  date:2020/9/24
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

    @Resource
    public static final HumanNameService humanNameService = new HumanNameServiceImpl();

    @InstructReflex(value = {".ra", ".rc", "。ra", "。rc"}, priority = 2)
    public String attributeCheck( MessageData<Object> data) {
        String attribute = userTempDataService.getUserAttribute(data.getQqID());
        data.setMessage(data.getMessage().replaceAll(" ", ""));
        data.setMessage(CharacterUtils.operationSymbolProcessing(
                data.getMessage()
        ));
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
    public String setAttribute( MessageData<Object> data) {
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

    @InstructReflex(value = {".r", "。r"})
    public String roll( MessageData<Object> data) {
        Integer diceFace = userTempDataService.getUserDiceFace(data.getQqID());
        data.setMessage(CharacterUtils.operationSymbolProcessing(data.getMessage()));
        if (diceFace != null) {
            diceSet.setDiceFace(data.getQqID(), diceFace);
        }
        if (data.getMessage().equals("") || data.getMessage().equals(" ")) {
            return rollBasics.rollRandom("d", data.getQqID());
        } else {
            //正则筛选
            String result = RegularExpressionUtils.getMatcher("[0-9dD +\\-*/＋－×÷]+", data.getMessage());
            if (result != null) {
                return rollBasics.rollRandom(result, data.getQqID()) + data.getMessage().replace(result, "");
            }
            return rollBasics.rollRandom(data.getMessage(), data.getQqID());
        }
    }


    @InstructReflex(value = {".MessageData", ".set", "。set"})
    public String setDiceFace( MessageData<Object> data) throws DiceInstructException {
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

    @InstructReflex(value = {".sc", "。sc"})
    public String sanCheck( MessageData<Object> data) {
        data.setMessage(CharacterUtils.operationSymbolProcessing(data.getMessage()));

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
    public String rollHide( MessageData<Object> data) {
        EventUtils.eventCallback(data.getEvent(), new EventAdapter() {
            @Override
            public void group(GroupMessageEvent event) {
                event.getSender().sendMessage(roll(data));
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

    @InstructReflex(value = {".rb", "。rb", ",rb"}, priority = 3)
    public String rollBonusDice( MessageData<Object> data) {
        data.setMessage(data.getMessage().replaceAll(" ", ""));
        data.setMessage(CharacterUtils.operationSymbolProcessing(data.getMessage()));

        String attribute = userTempDataService.getUserAttribute(data.getQqID());
        return rollBasics.rollBonus(data.getMessage(), attribute, true);
    }

    @InstructReflex(value = {".rp", "。rp", ",rp", ".Rp"}, priority = 3)
    public String rollPunishment( MessageData<Object> data) {
        data.setMessage(data.getMessage().replaceAll(" ", ""));
        data.setMessage(CharacterUtils.operationSymbolProcessing(data.getMessage()));
        String attribute = userTempDataService.getUserAttribute(data.getQqID());
        return rollBasics.rollBonus(data.getMessage(), attribute, false);
    }

    @InstructReflex(value = {".coc", "。coc", ".Coc"})
    public String randomCocRole( MessageData<Object> data) {
        int createNumber;
        createNumber = checkCreateRandomRoleNumber(data.getMessage());
        if (createNumber == -1) return CustomText.getText("dice.base.parameter.error");
        if (createNumber > 20 | createNumber < 1) {
            return "参数范围需要在1-20内";
        }
        return rollRole.createCocRole(createNumber);
    }

    @InstructReflex(value = {".dnd", "。dnd", ".Dnd", "。DND"})
    public String randomDndRole( MessageData<Object> data) {
        int createNumber;
        createNumber = checkCreateRandomRoleNumber(data.getMessage());
        if (createNumber == -1) return CustomText.getText("dice.base.parameter.error");
        if (createNumber > 20 | createNumber < 1) {
            return "参数范围需要在1-20内";
        }
        return rollRole.createDndRole(createNumber);
    }

    @InstructReflex(value = {".jrrp", ".JRRP", "。jrrp", ".todayRandom"})
    public String todayRandom( MessageData<Object> data) {
        return rollBasics.todayRandom(data.getQqID(), 8);
    }


    @InstructReflex(value = {".name"})
    public String randomName( MessageData<Object> data) {
        if (StringUtils.isNumeric(data.getMessage())) {
            int number ;
            try {
                number = Integer.parseInt(data.getMessage());
            } catch (Exception e) {
                return CustomText.getText("dice.base.parameter.error");
            }
            if (number > 0 && number <= 20) {
                return humanNameService.randomName(Integer.parseInt(data.getMessage()));
            }
            return "参数范围在1-20内";
        } else {
            return humanNameService.randomName(1);
        }
    }


    @InstructReflex(value = {".ga", "。ga"}, priority = 2)
    public String attributeGetAttribute( MessageData<Object> data) {
        return userTempDataService.getUserAttribute(data.getQqID());
    }

    @InstructReflex(value = {".en"})
    public String attributeEn( MessageData<Object> data) {
        if (data.getMessage().equals("")) {
            return "请输入属性名和数值或者属性名";
        }
        String checkAttribute = RegularExpressionUtils.getMatcher("[\\u4E00-\\u9FA5A-z]+[0-9]+", data.getMessage());
        if (checkAttribute == null) {
            checkAttribute = RegularExpressionUtils.getMatcher("[\\u4E00-\\u9FA5A-z]+", data.getMessage());
            if (checkAttribute == null) {
                return "请输入正确的参数形式";
            }

            String userAttribute = userTempDataService.getUserAttribute(data.getQqID());
            if (userAttribute == null || userAttribute.equals("")) {
                return "你尚未设置你的个人属性可以通过.st进行设置";
            }

            String tempData = RegularExpressionUtils.getMatcher(checkAttribute + "[0-9]+", userAttribute);
            if (tempData == null) {
                return "你通过.st设置的属性中，不存在[" + checkAttribute + "]这个技能";
            }

            int checkNumber = Integer.parseInt(tempData.substring(checkAttribute.length()));

            int randomNumber = new Random().nextInt(100);
            if (randomNumber > checkNumber) {
                int addValue = new Random().nextInt(10);
                int count = checkNumber + addValue;
                String updateAttribute = userAttribute.replaceAll(tempData, checkAttribute + count);
                userTempDataService.updateUserAttribute(data.getQqID(), updateAttribute);
                return "D100=" + randomNumber + "/" + checkNumber + " [" + checkAttribute + "] 成长成功! " +
                        "你当前的[" + checkAttribute + "]为D10=" + addValue + "+" + checkNumber + "=" + count;
            }
            return "D100=" + randomNumber + "/" + checkNumber + " [" + checkAttribute + "] 成长失败!";
        }

        int randomNumber = new Random().nextInt(100);
        int checkNumber = Integer.parseInt(RegularExpressionUtils.getMatcher("[0-9]+", checkAttribute));
        if (randomNumber > checkNumber) {
            int addValue = new Random().nextInt(10);
            int count = addValue + checkNumber;
            return "D100=" + randomNumber + "/" + checkNumber + " [" + checkAttribute + "] 成长成功! " +
                    "你当前的[" + checkAttribute + "]为D10=" + addValue + "+" + checkNumber + "=" + count;
        }
        return "D100=" + randomNumber + "/" + checkNumber + " [" + checkAttribute + "] 成长失败!";
    }

    @InstructReflex(value = {".sa", "。sa"})
    public String attributeSetAttribute( MessageData<Object> data) {
        String changeValue = RegularExpressionUtils.getMatcher("[0-9]+", data.getMessage());
        if (changeValue == null) {
            return "你输入的指令参数中没有需要更改的数值";
        }
        String changeName = data.getMessage().substring(0, data.getMessage().length() - changeValue.length());
        if (changeName.equals("")) {
            return "你输入的指令参数中没有需要更改的数值";
        }

        //查询属性
        String findAttribute = userTempDataService.getUserAttribute(data.getQqID());
        if (findAttribute == null || findAttribute.equals("")) {
            return "你尚未设置属性，可以通过指令.st来进行设置";
        }

        String attribute = RegularExpressionUtils.getMatcher(changeName + "[0-9]+", findAttribute);
        if (attribute == null) {
            return "你的属性中，不存在该属性，请通过.st重新设置";
        }
        String updateData = findAttribute.replaceAll(attribute, changeName + changeValue);
        userTempDataService.updateUserAttribute(data.getQqID(), updateData);
        return "您的属性已更新:" + attribute + " => " + changeName + changeValue;
    }


    private int checkCreateRandomRoleNumber( String message) {
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
