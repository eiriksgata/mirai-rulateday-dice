package indi.eiriksgata.rulateday.utlis;

import com.alibaba.fastjson.JSONObject;
import indi.eiriksgata.dice.reply.CustomText;
import indi.eiriksgata.rulateday.config.GlobalData;

import java.io.File;
import java.io.IOException;

public class JSONFileUtils {


    public static void configFileMerge(String filePath, JSONObject defaultJSONObject) {
        defaultJSONObject.forEach((key, value) -> {
            if (GlobalData.configData.getString(key) == null) {
                GlobalData.configData.put(key, value);
            }
        });
        GlobalData.configData.put("file.version", defaultJSONObject.getString("file.version"));
        try {
            CustomText.fileOut(new File(filePath), GlobalData.configData.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
