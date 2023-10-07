package com.github.eiriksgata.rulateday.config;

import com.alibaba.fastjson.JSONObject;
import com.github.eiriksgata.rulateday.dto.GroupRecordDTO;
import com.github.eiriksgata.rulateday.utlis.HmacSHA256Util;
import com.github.eiriksgata.rulateday.utlis.OsUtils;

import java.util.HashMap;
import java.util.Map;

public class GlobalData {

    public static JSONObject configData;

    public static Map<String, Map<String, String>> documentContext;

    public static String machineCode;

    public static Map<String, Long> groupChatRecordEnableMap = new HashMap<>();
    public static Map<String, GroupRecordDTO> groupChatRecordDataMap = new HashMap<>();

    public static int randomPictureApiType = 6;

    static {
        try {
            String result = "";
            if (OsUtils.isLinux()) {
                result = OsUtils.getIdentifierByLinux();
            } else {
                result = OsUtils.getIdentifierByWindows();
            }
            machineCode = HmacSHA256Util.hmacSHA256("rulateday-dice", result);
            System.out.println("Rulateday-dice INFO: your device code:" + machineCode);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Rulateday-dice ERROR: your device code get fail!");
        }
    }

}
