package com.github.eiriksgata.rulateday.trpggame.utils;

import com.github.eiriksgata.rulateday.trpggame.GameData;
import com.github.eiriksgata.trpg.dice.reply.CustomText;
import com.github.eiriksgata.trpg.dice.utlis.RegularExpressionUtils;
import com.github.eiriksgata.rulateday.trpggame.entity.PlayerRoleDataEntity;
import org.apache.commons.lang3.RandomUtils;

import java.util.List;

public class PlayerRoleAttributeSetUtil {

    public static String roleDataShow(Long id) {
        if (GameData.playerRoleSaveDataMap.get(id) == null) {
            return CustomText.getText("trpg.player.role.data.show.not.found");
        }
        return CustomText.getText("trpg.player.role.data.show.result",
                GameData.playerRoleSaveDataMap.get(id).getName(),
                GameData.playerRoleSaveDataMap.get(id).getAttribute(),
                GameData.playerRoleSaveDataMap.get(id).getConsumables(),
                GameData.playerRoleSaveDataMap.get(id).getSkill()
        );
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
            return CustomText.getText("trpg.player.role.name.check.success");
        }
        return CustomText.getText("trpg.player.role.name.length.error");
    }

    public static String skillCheck(Long id, String inputString) {
        String errorSkillName = "力量|体质|体型|敏捷|外貌|智力|意志|教育|幸运|生命";
        List<String> list = RegularExpressionUtils.getMatchers(errorSkillName, inputString);
        if (list.size() != 0) {
            return CustomText.getText("trpg.skill.check.attribute.name.error");
        }
        int count = 0;
        list = RegularExpressionUtils.getMatchers("[0-9]+", inputString);
        for (String temp : list) {
            int value = Integer.parseInt(temp);
            if (value >= 0 && value <= 80) {
                count += value;
            } else {
                return CustomText.getText("trpg.skill.number.value.size.error");
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
            return CustomText.getText("trpg.skill.set.success", inputString);
        }
        return CustomText.getText("trpg.skill.count.value.size.error");
    }


    public static String attributeCheck(Long id, String inputString) {
        String attributeName = "力量[0-9]+|体质[0-9]+|体型[0-9]+|敏捷[0-9]+|外貌[0-9]+|智力[0-9]+|意志[0-9]+|教育[0-9]+";
        //检测数值，最高为80 最低为40 合计 = 470
        int valueCount = 0;
        int hp = 0;
        int mp = 0;
        int san = 0;
        StringBuilder result = new StringBuilder();
        int luck = 5 * RandomUtils.nextInt(1, 21);
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
                        return CustomText.getText("trpg.attribute.value.format.error");
                    }
                    valueCount += attributeValue;
                    result.append(temp);
                } else {
                    return CustomText.getText("trpg.attribute.value.scope.error", temp);
                }
            }
        } else {
            return CustomText.getText("trpg.attribute.name.format.error");
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
                GameData.playerRoleSaveDataMap.get(id).setConsumables(consumables.toString());
            }
            return CustomText.getText("trpg.attribute.set.success", result, consumables);

        } else {
            return CustomText.getText("trpg.attribute.count.value.size.error");
        }
    }
}
