package indi.eiriksgata.rulateday.galgame;

import com.alibaba.fastjson.JSONObject;
import indi.eiriksgata.rulateday.galgame.entity.PlayerRoleDataEntity;

import java.util.HashMap;
import java.util.Map;


public class GameData {

    public static Map<Long, PlayerRoleDataEntity> playerRoleSaveDataMap = new HashMap<>();

    public static Map<Long, JSONObject> modelDataMap = new HashMap<>();

    public static Map<Long, Map<String, Integer>> optionCountMap = new HashMap<>();

    public static Map<Long, Map<String, JSONObject>> optionJSONObjectMap = new HashMap<>();

    public static Map<Long, Map<String, JSONObject>> roleMap = new HashMap<>();

    public static Map<Long, Map<String, JSONObject>> nodeMap = new HashMap<>();


}
