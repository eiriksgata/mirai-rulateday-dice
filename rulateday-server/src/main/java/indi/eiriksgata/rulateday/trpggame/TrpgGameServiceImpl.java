package indi.eiriksgata.rulateday.trpggame;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import indi.eiriksgata.dice.reply.CustomText;
import indi.eiriksgata.dice.utlis.RegularExpressionUtils;
import indi.eiriksgata.rulateday.trpggame.utils.DetectionUtil;
import indi.eiriksgata.rulateday.utlis.FileUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class TrpgGameServiceImpl {

    public static String getAllTrpgModelFiles() {
        File file = new File("data/indi.eiriksgata.rulateday-dice/trpg-game");
        File[] files = file.listFiles();
        if (files == null) {
            return CustomText.getText("trpg.service.get.model.files.not.found");
        }
        StringBuilder result = new StringBuilder(CustomText.getText("trpg.service.get.model.result.title"));
        int i = 0;
        for (File modelFile : files) {
            result.append(++i).append(". ").append(modelFile.getName()).append("\n");
        }
        return result.toString();
    }

    public static void playerQuitGame(Long id) {
        //清理数据
        GameData.nodeId.remove(id);
        GameData.nodeMap.remove(id);
        GameData.roleMap.remove(id);
        GameData.optionJSONObjectMap.remove(id);
        GameData.optionMappingMap.remove(id);
        GameData.TrpgGamePlayerList.remove(id);
    }

    public static String loadScriptData(Long id, String fileName) {
        String jsonText = FileUtil.readJsonFile("data/indi.eiriksgata.rulateday-dice/trpg-game/" + fileName);
        if (jsonText == null) {
            return CustomText.getText("trpg.service.load.script.data.not.found.file", fileName);
        }
        JSONObject modelJSON = JSONObject.parseObject(jsonText);

        Map<String, JSONObject> optionJSONObject = modelJSON.getJSONObject("option").toJavaObject(new TypeReference<Map<String, JSONObject>>() {
        }.getType());
        GameData.optionJSONObjectMap.put(id, optionJSONObject);

        Map<String, JSONObject> role = modelJSON.getJSONObject("importData").getJSONObject("role").toJavaObject(new TypeReference<Map<String, JSONObject>>() {
        }.getType());
        //玩家的预设的属性
        role.get("player").put("attribute",
                GameData.playerRoleSaveDataMap.get(id).getAttribute() +
                        GameData.playerRoleSaveDataMap.get(id).getSkill());
        GameData.roleMap.put(id, role);


        Map<String, JSONObject> nodeMap = modelJSON.getJSONObject("event").getJSONObject("node").toJavaObject(new TypeReference<Map<String, JSONObject>>() {
        }.getType());
        GameData.nodeMap.put(id, nodeMap);

        GameData.nodeId.put(id, "1");

        GameData.TrpgGamePlayerList.put(id, true);

        return CustomText.getText("trpg.service.load.script.data.model.name", modelJSON.getString("name"), modelJSON.getString("introduction"));
    }

    public static String loadEventText(Long id) {
        String result = GameData.nodeMap.get(id).get(GameData.nodeId.get(id)).getString("text");
        if (GameData.nodeMap.get(id).get(GameData.nodeId.get(id)).getBooleanValue("end")) {
            playerQuitGame(id);
            return result;
        }
        JSONArray optionArray = GameData.nodeMap.get(id).get(GameData.nodeId.get(id)).getJSONArray("option");
        StringBuilder optionOutText = new StringBuilder();
        Map<String, String> optionMap = new HashMap<>();
        for (int i = 0; i < optionArray.size(); i++) {
            String optionId = optionArray.getString(i);
            if (GameData.optionJSONObjectMap.get(id).get(optionId).getInteger("count") > 0) {
                optionMap.put(i + "", optionId);
                optionOutText.append(i).append(".").append(
                        GameData.optionJSONObjectMap.get(id).get(optionId).getString("text")).append("\n");
            }
        }
        GameData.optionMappingMap.put(id, optionMap);
        result += CustomText.getText("trpg.service.load.event.text.option.title") + optionOutText;
        return result;
    }

    public static String optionSelect(Long id, String inputValue) {
        String optionId = GameData.optionMappingMap.get(id).get(inputValue);
        if (optionId == null) {
            return CustomText.getText("trpg.service.option.not.found");
        }
        boolean passOption = false;
        boolean showDice;
        String detectionRoleName = GameData.optionJSONObjectMap.get(id).get(optionId).getJSONObject("detection").getString("role");
        String detectionAttributeName = GameData.optionJSONObjectMap.get(id).get(optionId).getJSONObject("detection").getString("attribute");
        String detectionConsumablesName = GameData.optionJSONObjectMap.get(id).get(optionId).getJSONObject("detection").getString("consumables");

        String attributeSource = GameData.roleMap.get(id).get(detectionRoleName).getString("attribute");
        String consumablesSource = GameData.roleMap.get(id).get(detectionRoleName).getString("consumables");
        showDice = GameData.optionJSONObjectMap.get(id).get(optionId).getJSONObject("detection").getBooleanValue("showDice");

        if (detectionAttributeName == null && detectionConsumablesName == null) {
            return CustomText.getText("trpg.service.option.error");
        }

        DetectionEntity attributeResult = DetectionUtil.attribute(attributeSource, detectionAttributeName);
        DetectionEntity consumablesResult = DetectionUtil.consumables(consumablesSource, detectionConsumablesName);

        String resultText = "";
        if (attributeResult != null && consumablesResult != null) {
            passOption = attributeResult.isResult() && consumablesResult.isResult();
        } else {
            if (attributeResult != null) {
                passOption = attributeResult.isResult();
                if (showDice) {
                    //显示骰子数值
                    resultText += attributeResult.getDiceText() + "\n";
                }
            }
            if (consumablesResult != null) {
                passOption = consumablesResult.isResult();
            }
        }

        //将选项次数减少
        GameData.optionJSONObjectMap.get(id).get(optionId).put("count", GameData.optionJSONObjectMap.get(id).get(optionId).getIntValue("count") - 1);
        String passResult = passOption ? "success" : "fail";
        JSONObject optionJsonMap = GameData.optionJSONObjectMap.get(id).get(optionId).getJSONObject("detection")
                .getJSONObject(passResult);


        resultText += optionJsonMap.getString("text");

        //TODO: 更改玩家的属性、或者消耗品
        if (optionJsonMap.getJSONObject("update") != null) {
            roleDataUpdate(id,
                    optionJsonMap.getJSONObject("update").toJavaObject(
                            new TypeReference<Map<String, String>>() {
                            }.getType()
                    ));
        }

        //最后才更改nodeId
        GameData.nodeId.put(id, optionJsonMap.getString("nextNode"));
        return resultText;
    }

    public static void roleDataUpdate(Long id, Map<String, String> updateDataMap) {
        String items = "attribute|consumables";
        String roleName = updateDataMap.get("role");
        if (roleName == null) {
            return;
        }
        updateDataMap.forEach((key, value) -> {
            String item = RegularExpressionUtils.getMatcher(items, key);
            if (item != null) {
                GameData.roleMap.get(id).get(roleName).put(item,
                        RoleDataHandler.update(
                                GameData.roleMap.get(id).get(roleName).getString(item), value
                        )
                );
            }
        });
    }


}
