package com.github.eiriksgata.rulateday.trpggame;

import com.alibaba.fastjson.JSONObject;
import com.github.eiriksgata.rulateday.trpggame.entity.PlayerRoleDataEntity;

import java.util.HashMap;
import java.util.Map;


public class GameData {

    public static Map<Long, PlayerRoleDataEntity> playerRoleSaveDataMap = new HashMap<>();

    public static Map<Long, Map<String, JSONObject>> optionJSONObjectMap = new HashMap<>();

    public static Map<Long, Map<String, JSONObject>> roleMap = new HashMap<>();

    public static Map<Long, Map<String, JSONObject>> nodeMap = new HashMap<>();

    public static Map<Long, String> nodeId = new HashMap<>();

    public static Map<Long, Boolean> TrpgGamePlayerList = new HashMap<>();

    public static Map<Long, Map<String, String>> optionMappingMap = new HashMap<>();

}
