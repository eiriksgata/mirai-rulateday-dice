package indi.eiriksgata.rulateday.galgame;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import indi.eiriksgata.rulateday.utlis.FileUtil;

import java.util.Map;

public class GalGameServiceImpl {

    public static String loadScript(Long id, String fileName) {
        //TODO: 检测玩家数据是否满足要求
        if (GameData.playerRoleSaveDataMap.get(id).getName() == null ||
                GameData.playerRoleSaveDataMap.get(id).getAttribute() == null ||
                GameData.playerRoleSaveDataMap.get(id).getName().equals("") ||
                GameData.playerRoleSaveDataMap.get(id).getAttribute().equals("")
        ) {
            return "请先通过指令完善你的人物属性再进行游戏";
        }

        String jsonText = FileUtil.readJsonFile("D:\\workspace\\mirai-rulateday-dice\\rulateday-server\\src\\main\\resources\\coctext\\trpg-event-model.json");

        JSONObject modelJSON = JSONObject.parseObject(jsonText);

        Map<String, Integer> optionCount = modelJSON.getJSONObject("config")
                .getJSONObject("optionNumber")
                .toJavaObject(new TypeReference<Map<String, Integer>>() {
                }.getType());
        GameData.optionCountMap.put(id, optionCount);

        Map<String, JSONObject> optionJSONObject = modelJSON.getJSONObject("option").toJavaObject(new TypeReference<Map<String, JSONObject>>() {
        }.getType());
        GameData.optionJSONObjectMap.put(id, optionJSONObject);

        Map<String, JSONObject> role = modelJSON.getJSONObject("importData").getJSONObject("role").toJavaObject(new TypeReference<Map<String, JSONObject>>() {
        }.getType());
        GameData.roleMap.put(id, role);

        Map<String, JSONObject> nodeMap = modelJSON.getJSONObject("event").getJSONObject("node").toJavaObject(new TypeReference<Map<String, JSONObject>>() {
        }.getType());
        GameData.nodeMap.put(id, nodeMap);

        return "模组名称:" + modelJSON.getString("name") + "\n简介:" + modelJSON.getString("introduction");
    }


    public static void mergeRoleData(Long id) {



    }





}
