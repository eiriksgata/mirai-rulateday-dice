package indi.eiriksgata.rulateday.galgame.utils;

import indi.eiriksgata.dice.utlis.RegularExpressionUtils;
import indi.eiriksgata.rulateday.galgame.GameData;
import indi.eiriksgata.rulateday.galgame.entity.PlayerRoleDataEntity;
import org.apache.commons.lang3.RandomUtils;

import java.util.List;

public class PlayerRoleAttributeSetUtil {

    public static String roleDataShow(Long id) {
        if (GameData.playerRoleSaveDataMap.get(id) == null) {
            return "你当前没有人物数据";
        }
        return "人物名称:" + GameData.playerRoleSaveDataMap.get(id).getName() +
                "属性:" + GameData.playerRoleSaveDataMap.get(id).getAttribute() +
                "衍生数值" + GameData.playerRoleSaveDataMap.get(id).getConsumables();
    }

    public static String nameCheck(Long id, String inputString) {
        if (inputString.length() > 0 && inputString.length() <= 10) {
            if (GameData.playerRoleSaveDataMap.get(id) == null) {
                PlayerRoleDataEntity entity = new PlayerRoleDataEntity();
                entity.setName(inputString);
                GameData.playerRoleSaveDataMap.put(id, entity);
            } else {
                GameData.playerRoleSaveDataMap.get(id).setName(inputString);
            }
            return "设置完毕";
        }
        return "名字长度范围需要在(0,10]范围内";
    }

    public static String skillCheck(Long id, String inputString) {
        String errorSkillName = "力量|体质|体型|敏捷|外貌|智力|意志|教育|幸运|生命";
        List<String> list = RegularExpressionUtils.getMatchers(errorSkillName, inputString);
        if (list.size() != 0) {
            return "不可以出现任何与基础属性相关的关键词";
        }
        int count = 0;
        list = RegularExpressionUtils.getMatchers("[0-9]+", inputString);
        for (String temp : list) {
            int value = Integer.parseInt(temp);
            if (value >= 0 && value <= 80) {
                count += value;
            } else {
                return "数据范围需要在[0,80]";
            }
        }
        if (count == 380) {
            if (GameData.playerRoleSaveDataMap.get(id) == null) {
                PlayerRoleDataEntity entity = new PlayerRoleDataEntity();
                entity.setSkill(inputString);
                GameData.playerRoleSaveDataMap.put(id, entity);
            } else {
                GameData.playerRoleSaveDataMap.get(id).setSkill(inputString);
            }
            return "设置成功最终输出:" + inputString;
        }
        return "统计不符合380数值，请重新分配。";
    }


    public static String attributeCheck(Long id, String inputString) {
        String attributeName = "力量[0-9]+|体质[0-9]+|体型[0-9]+|敏捷[0-9]+|外貌[0-9]+|智力[0-9]+|意志[0-9]+|教育[0-9]+";
        //检测数值，最高为80 最低为40 合计 = 470
        int valueCount = 0;
        int hp = 0;
        int mp = 0;
        int san = 0;
        StringBuilder result = new StringBuilder();
        int luck = 5 * RandomUtils.nextInt(1, 20);
        List<String> list = RegularExpressionUtils.getMatchers(attributeName, inputString);
        if (list.size() == 8) {
            for (String temp : list) {
                int attributeValue = Integer.parseInt(temp.substring(2));
                if (temp.contains("体型") || temp.contains("体质")) {
                    hp += attributeValue;
                }
                if (temp.contains("意志")) {
                    mp = attributeValue;
                    san = attributeValue;
                }
                if (attributeValue >= 40 && attributeValue <= 80) {
                    if (attributeValue % 5 != 0) {
                        return "属性[" + temp + "]必须为5的倍数";
                    }
                    valueCount += attributeValue;
                    result.append(temp);
                } else {
                    return "检测到[" + temp + "]不符合要求。请重新设置区间:[40,80]";
                }
            }
        } else {
            return "输入的格式不符合要求，输入的属性必须为8项[力量|体质|体型|敏捷|外貌|智力|意志|教育]，多和少都不可以";
        }

        if (valueCount == 470) {
            result.append("幸运").append(luck);
            StringBuilder consumables = new StringBuilder();
            consumables.append("生命").append(hp / 10);
            consumables.append("魔法").append(mp / 5);
            consumables.append("理智").append(san);

            if (GameData.playerRoleSaveDataMap.get(id) == null) {
                PlayerRoleDataEntity entity = new PlayerRoleDataEntity();
                entity.setAttribute(result.toString());
                entity.setConsumables(consumables.toString());
            } else {
                GameData.playerRoleSaveDataMap.get(id).setAttribute(result.toString());
                GameData.playerRoleSaveDataMap.get(id).setAttribute(consumables.toString());
            }
            return "最终输出(幸运随机仅随机生成)\n 基础属性:" + result + "\n 衍生数值:" + consumables;
        } else {
            return "总计数值不符合470";
        }
    }
}
