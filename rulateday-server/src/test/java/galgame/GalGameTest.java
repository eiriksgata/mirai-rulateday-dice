package galgame;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import indi.eiriksgata.dice.utlis.RegularExpressionUtils;
import indi.eiriksgata.rulateday.galgame.DetectionEntity;
import indi.eiriksgata.rulateday.galgame.GameData;
import indi.eiriksgata.rulateday.galgame.entity.PlayerRoleDataEntity;
import indi.eiriksgata.rulateday.galgame.utils.DetectionUtil;
import indi.eiriksgata.rulateday.utlis.FileUtil;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class GalGameTest {


    @Test
    public void readFile() throws Exception {

        String jsonText = FileUtil.readJsonFile("D:\\workspace\\mirai-rulateday-dice\\rulateday-server\\src\\main\\resources\\coctext\\trpg-event-model.json");
        JSONObject modelJSON = JSONObject.parseObject(jsonText);

        String playerAttribute = "力量40体质55体型60敏捷75外貌60智力80意志50教育50幸运60生命11魔法10";
        String playerSkill = "侦查80聆听80图书馆80闪避60信用30急救50";

        Map<String, Integer> optionCount = modelJSON.getJSONObject("config")
                .getJSONObject("optionNumber")
                .toJavaObject(new TypeReference<Map<String, Integer>>() {
                }.getType());

        Map<String, JSONObject> optionJSONObject = modelJSON.getJSONObject("option").toJavaObject(new TypeReference<Map<String, JSONObject>>() {
        }.getType());

        Map<String, JSONObject> role = modelJSON.getJSONObject("importData").getJSONObject("role").toJavaObject(new TypeReference<Map<String, JSONObject>>() {
        }.getType());

        Map<String, JSONObject> nodeMap = modelJSON.getJSONObject("event").getJSONObject("node").toJavaObject(new TypeReference<Map<String, JSONObject>>() {
        }.getType());

        //合并用户数据
        JSONObject playerTempJSONObject = new JSONObject();
        playerTempJSONObject.put("attribute", playerAttribute);
        playerTempJSONObject.put("skill", playerSkill);
        playerTempJSONObject.put("consumables", "生命11魔法6理智60绷带3警长好感度5");

        role.put("player", playerTempJSONObject);

        String nodeId = "1";

        System.out.println("模组名称:" + modelJSON.getString("name"));
        System.out.println("简介:" + modelJSON.getString("introduction"));

        while (true) {
            String context = nodeMap.get(nodeId).getString("text");
            System.out.println(context);

            if (nodeMap.get(nodeId).getBooleanValue("end")) {
                return;
            }

            JSONArray optionArray = nodeMap.get(nodeId).getJSONArray("option");

            StringBuilder optionOutText = new StringBuilder();
            Map<String, String> optionMap = new HashMap<>();
            for (int i = 0; i < optionArray.size(); i++) {
                String optionId = optionArray.getString(i);
                if (optionCount.get(optionId) > 0) {
                    optionMap.put(i + "", optionId);
                    optionOutText.append(i).append(".").append(
                            optionJSONObject.get(optionId).getString("text")).append("\n");
                }
            }

            System.out.println(optionOutText);

            Scanner input = new Scanner(System.in);
            //接受String类型
            String str = input.next();

            String optionId = optionMap.get(str);
            if (optionId == null) {
                System.out.println("没有这个选项");
                continue;
            }

            //将选项次数减少1
            boolean passOption = false;
            boolean showDice;
            String detectionRoleName = optionJSONObject.get(optionId).getJSONObject("detection").getString("role");
            String detectionAttributeName = optionJSONObject.get(optionId).getJSONObject("detection").getString("attribute");
            String detectionConsumablesName = optionJSONObject.get(optionId).getJSONObject("detection").getString("consumables");

            String attributeSource = role.get(detectionRoleName).getString("attribute");
            String consumablesSource = role.get(detectionRoleName).getString("consumables");
            showDice = optionJSONObject.get(optionId).getJSONObject("detection").getBooleanValue("showDice");

            if (detectionAttributeName == null && detectionConsumablesName == null) {
                System.out.println("错误的detection设置，attribute 和 consumables 必须要有一项");
            }
            DetectionEntity attributeResult = DetectionUtil.attribute(attributeSource, detectionAttributeName);
            DetectionEntity consumablesResult = DetectionUtil.consumables(consumablesSource, detectionConsumablesName);

            if (attributeResult != null && consumablesResult != null) {
                passOption = attributeResult.isResult() && consumablesResult.isResult();
            } else {
                if (attributeResult != null) {
                    passOption = attributeResult.isResult();
                    if (showDice) {
                        //显示骰子数值
                        System.out.println(attributeResult.getDiceText());
                    }
                }
                if (consumablesResult != null) {
                    passOption = consumablesResult.isResult();
                }
            }

            JSONObject passResult;
            if (passOption) {
                passResult = optionJSONObject.get(optionId).getJSONObject("detection").getJSONObject("success");
            } else {
                passResult = optionJSONObject.get(optionId).getJSONObject("detection").getJSONObject("fail");
            }
            System.out.println(passResult.getString("text"));
            nodeId = passResult.getString("nextNode");
            optionCount.put(optionId, optionCount.get(optionId) - 1);
        }


    }

    @Test
    void byteTest() {

    }

    @Test
    void consoleInput() {
        Scanner input = new Scanner(System.in);
        //接受String类型
        String str = input.next();
        //输出结果
        System.out.println(str);
    }


    @Test
    void attributeCheck() {
        String attributeName = "力量[0-9]+|体质[0-9]+|体型[0-9]+|敏捷[0-9]+|外貌[0-9]+|智力[0-9]+|意志[0-9]+|教育[0-9]+";
        //检测数值，最高为80 最低为40 合计 = 470
        String inputString = "力量40体质55体型60敏捷75外貌60智力80意志50教育50";
        int valueCount = 0;
        int hp = 0;
        int mp = 0;
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
                }
                if (attributeValue >= 40 && attributeValue <= 80) {
                    if (attributeValue % 5 != 0) {
                        System.out.println("属性[" + temp + "]必须为5的倍数");
                        return;
                    }
                    valueCount += attributeValue;
                    result.append(temp);
                } else {
                    System.out.println("检测到[" + temp + "]不符合要求。请重新设置区间:[40,80]");
                    return;
                }
            }
        } else {
            System.out.println("输入的格式不符合要求，输入的属性必须为8项，多和少都不可以");
        }

        if (valueCount == 470) {
            result.append("幸运").append(luck);
            result.append("生命").append(hp / 10);
            result.append("魔法").append(mp / 5);
            System.out.println("最终输出(幸运随机仅随机生成):" + result);
        } else {
            System.out.println("总计数值不符合470");
        }
    }

    @Test
    void skillCheck() {
        String errorSkillName = "力量|体质|体型|敏捷|外貌|智力|意志|教育|幸运|生命";
        String inputString = "侦查80聆听80图书馆80闪避60信用30急救50";
        List<String> list = RegularExpressionUtils.getMatchers(errorSkillName, inputString);
        if (list.size() != 0) {
            System.out.println("不可以出现任何与基础属性相关的关键词");
            return;
        }
        int count = 0;
        list = RegularExpressionUtils.getMatchers("[0-9]+", inputString);
        for (String temp : list) {
            int value = Integer.parseInt(temp);
            if (value >= 0 && value <= 80) {
                count += value;
            } else {
                System.out.println("数据范围需要在[0,80]");
                return;
            }
        }
        System.out.println(count);
        if (count == 380) {
            System.out.println("最终输出:" + inputString);
            return;
        }
        System.out.println("统计不符合380数值，请重新分配。");
    }


    @Test
    void DetectionUtilTest() {
        System.out.println(DetectionUtil.attribute("力量50敏捷70", "力量"));
        System.out.println(DetectionUtil.consumables("绷带3好感度20", "绷带>0"));


    }

    @Test
    void mapTest() {
        PlayerRoleDataEntity entity = new PlayerRoleDataEntity();
        entity.setSkill("123456");
        GameData.playerRoleSaveDataMap.put(1L, entity);

        System.out.println(GameData.playerRoleSaveDataMap.get(1L));


        GameData.playerRoleSaveDataMap.get(1L).setAttribute("456789");
        System.out.println(GameData.playerRoleSaveDataMap.get(1L));


    }

}
