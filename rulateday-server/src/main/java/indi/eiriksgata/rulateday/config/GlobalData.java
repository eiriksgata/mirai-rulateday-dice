package indi.eiriksgata.rulateday.config;

import com.alibaba.fastjson.JSONObject;
import indi.eiriksgata.rulateday.utlis.HmacSHA256Util;
import indi.eiriksgata.rulateday.utlis.OsUtils;

import java.util.Map;

public class GlobalData {

    public static JSONObject configData;

    public static Map<String, Map<String, String>> documentContext;

    public static String machineCode;

    public static Map<String, Boolean> messageRecordMap;

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
