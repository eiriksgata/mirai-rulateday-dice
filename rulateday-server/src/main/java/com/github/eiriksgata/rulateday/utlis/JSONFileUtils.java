package com.github.eiriksgata.rulateday.utlis;

import com.alibaba.fastjson.JSONObject;
import com.github.eiriksgata.rulateday.config.GlobalData;
import com.github.eiriksgata.trpg.dice.reply.CustomText;

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
